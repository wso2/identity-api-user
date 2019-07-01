/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.rest.api.user.challenge.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.challenge.common.Constant;
import org.wso2.carbon.identity.api.user.common.ContextLoader;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.recovery.IdentityRecoveryClientException;
import org.wso2.carbon.identity.recovery.IdentityRecoveryException;
import org.wso2.carbon.identity.recovery.model.ChallengeQuestion;
import org.wso2.carbon.identity.recovery.model.UserChallengeAnswer;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.core.functions.ChallengeQuestionToExternal;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.core.functions.UserChallengeAnswerToExternal;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.ChallengeAnswerDTO;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.ChallengeQuestionDTO;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.ChallengeSetDTO;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.UserChallengeAnswerDTO;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.UserChallengeAnswerResponseDTO;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static org.wso2.carbon.identity.api.user.challenge.common.Constant.CHALLENGE_QUESTION_PREFIX;
import static org.wso2.carbon.identity.api.user.challenge.common.Constant.ErrorMessage.ERROR_CODE_ERROR_DELETING_CHALLENGE_ANSWERS_OF_USER;
import static org.wso2.carbon.identity.api.user.challenge.common.Constant.ErrorMessage.ERROR_CODE_ERROR_DELETING_CHALLENGE_ANSWER_OF_USER;
import static org.wso2.carbon.identity.api.user.challenge.common.Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIVING_CHALLENGES_FOR_USER;
import static org.wso2.carbon.identity.api.user.challenge.common.Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIVING_CHALLENGE_ANSWERS_OF_USER;
import static org.wso2.carbon.identity.api.user.challenge.common.Constant.ErrorMessage.ERROR_CODE_ERROR_SETTING_CHALLENGE_ANSWERS_OF_USER;
import static org.wso2.carbon.identity.api.user.challenge.common.Constant.ErrorMessage.ERROR_CODE_ERROR_SETTING_CHALLENGE_ANSWER_OF_USER;
import static org.wso2.carbon.identity.api.user.challenge.common.Constant.ErrorMessage.ERROR_CODE_ERROR_UPDATING_CHALLENGE_ANSWERS_OF_USER;
import static org.wso2.carbon.identity.api.user.challenge.common.Constant.ErrorMessage.ERROR_CODE_ERROR_UPDATING_CHALLENGE_ANSWER_OF_USER;
import static org.wso2.carbon.identity.api.user.challenge.common.Constant.ErrorMessage.ERROR_CODE_USER_ALREADY_ANSWERED_CHALLENGE;
import static org.wso2.carbon.identity.api.user.challenge.common.Constant.ErrorMessage.ERROR_CODE_USER_ALREADY_ANSWERED_CHALLENGES;
import static org.wso2.carbon.identity.api.user.challenge.common.Constant.ErrorMessage.ERROR_CODE_USER_HAS_NOT_ANSWERED_CHALLENGE;
import static org.wso2.carbon.identity.api.user.challenge.common.Constant.ErrorMessage.ERROR_CODE_USER_HAS_NOT_ANSWERED_CHALLENGES;
import static org.wso2.carbon.identity.api.user.common.Constants.ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.api.user.common.Util.getChallengeQuestionManager;

public class UserChallengeService {

    private static final Log log = LogFactory.getLog(UserChallengeService.class);
    public static final String WSO2_CLAIM_DIALECT = "http://wso2.org/claims/";

    public List<ChallengeSetDTO> getChallengesForUser(User user, Integer offset, Integer limit) {

        try {
            return buildChallengesDTO(getChallengeQuestionManager().getAllChallengeQuestionsForUser(ContextLoader
                    .getTenantDomainFromContext(), user), offset, limit);
        } catch (IdentityRecoveryException e) {
            throw handleIdentityRecoveryException(e, ERROR_CODE_ERROR_RETRIVING_CHALLENGES_FOR_USER);
        }

    }

    public boolean setChallengeAnswersOfUser(User user, List<ChallengeAnswerDTO> challengeAnswers) {

        List<UserChallengeAnswer> answers = buildChallengeAnswers(challengeAnswers);
        try {
            List<String> answeredList = getChallengeQuestionManager().getChallengeQuestionUris(user);
            if (answeredList.size() > 0) {
                throw handleError(Response.Status.CONFLICT, ERROR_CODE_USER_ALREADY_ANSWERED_CHALLENGES);
            }
            getChallengeQuestionManager().setChallengesOfUser(user, answers.toArray(new UserChallengeAnswer[answers.size()]));
        } catch (IdentityRecoveryException e) {
            throw handleIdentityRecoveryException(e, ERROR_CODE_ERROR_SETTING_CHALLENGE_ANSWERS_OF_USER);
        }
        return true;
    }

    public boolean updateChallengeAnswersOfUser(User user, List<ChallengeAnswerDTO> newChallengeAnswers) {

        List<UserChallengeAnswer> answers = buildChallengeAnswers(newChallengeAnswers);
        try {
            validateUserAnsweredChallenges(user);
            getChallengeQuestionManager().setChallengesOfUser(user, answers.toArray(new UserChallengeAnswer[answers.size()]));

        } catch (IdentityRecoveryException e) {

            throw handleIdentityRecoveryException(e, ERROR_CODE_ERROR_UPDATING_CHALLENGE_ANSWERS_OF_USER);
        }
        return true;
    }

    public boolean updateChallengeAnswerOfUser(User user, String challengeSetId, UserChallengeAnswerDTO
            challengeAnswer) {

        try {
            validateUserAnsweredChallenge(user, challengeSetId);
            UserChallengeAnswer answer = new UserChallengeAnswer(
                    createChallenceQuestion(challengeSetId, challengeAnswer.getChallengeQuestion()),
                    challengeAnswer.getAnswer());
            getChallengeQuestionManager().setChallengeOfUser(user, answer);
        } catch (IdentityRecoveryException e) {

            throw handleIdentityRecoveryException(e, ERROR_CODE_ERROR_UPDATING_CHALLENGE_ANSWER_OF_USER);
        }
        return true;
    }

    public boolean addChallengeAnswerOfUser(User user, String challengeSetId, UserChallengeAnswerDTO
            challengeAnswer) {

        try {
            List<String> answeredList = getChallengeQuestionManager().getChallengeQuestionUris(user);
            if (!answeredList.isEmpty() && answeredList.contains(WSO2_CLAIM_DIALECT + challengeSetId)) {
                throw handleError(Response.Status.CONFLICT, ERROR_CODE_USER_ALREADY_ANSWERED_CHALLENGE);
            }
            UserChallengeAnswer answer = new UserChallengeAnswer(
                    createChallenceQuestion(challengeSetId, challengeAnswer.getChallengeQuestion()),
                    challengeAnswer.getAnswer());
            getChallengeQuestionManager().setChallengeOfUser(user, answer);
        } catch (IdentityRecoveryException e) {

            throw handleIdentityRecoveryException(e, ERROR_CODE_ERROR_SETTING_CHALLENGE_ANSWER_OF_USER);
        }
        return true;
    }

    public List<UserChallengeAnswerResponseDTO> getChallengeAnswersOfUser(User user) {

        try {
            return getUserChallengeAnswerDTOsOfUser(user);
        } catch (IdentityRecoveryException e) {

            throw handleIdentityRecoveryException(e, ERROR_CODE_ERROR_RETRIVING_CHALLENGE_ANSWERS_OF_USER);
        }
    }

    public boolean removeChallengeAnswersOfUser(User user) {

        try {
            validateUserAnsweredChallenges(user);

            getChallengeQuestionManager().removeChallengeAnswersOfUser(user);
        } catch (IdentityRecoveryException e) {

            throw handleIdentityRecoveryException(e, ERROR_CODE_ERROR_DELETING_CHALLENGE_ANSWERS_OF_USER);
        }
        return true;
    }

    public boolean removeChallengeAnswerOfUser(User user, String challengeSetId) {
        try {
            validateUserAnsweredChallenge(user, challengeSetId);
            getChallengeQuestionManager().removeChallengeAnswerOfUser(user, WSO2_CLAIM_DIALECT + challengeSetId);
        } catch (IdentityRecoveryException e) {

            throw handleIdentityRecoveryException(e, ERROR_CODE_ERROR_DELETING_CHALLENGE_ANSWER_OF_USER);
        }
        return true;
    }

    private void validateUserAnsweredChallenges(User user) throws IdentityRecoveryException {
        List<String> answeredList = getChallengeQuestionManager().getChallengeQuestionUris(user);
        if (answeredList.size() < 1) {
            throw handleError(Response.Status.NOT_FOUND, ERROR_CODE_USER_HAS_NOT_ANSWERED_CHALLENGES);
        }
    }

    private void validateUserAnsweredChallenge(User user, String challengeSetId) throws IdentityRecoveryException {
        List<String> answeredList = getChallengeQuestionManager().getChallengeQuestionUris(user);
        if (answeredList.isEmpty() || !answeredList.contains(WSO2_CLAIM_DIALECT + challengeSetId)) {
            throw handleError(Response.Status.NOT_FOUND, ERROR_CODE_USER_HAS_NOT_ANSWERED_CHALLENGE);
        }
    }


    private List<UserChallengeAnswerResponseDTO> getUserChallengeAnswerDTOsOfUser(User user) throws IdentityRecoveryException {
        UserChallengeAnswer[] answers = getChallengeQuestionManager().getChallengeAnswersOfUser(user);
        return Arrays.stream(answers).map(new UserChallengeAnswerToExternal()).collect(Collectors.toList());
    }

    private ChallengeQuestion createChallenceQuestion(String setId, ChallengeQuestionDTO q) {
        return new ChallengeQuestion(WSO2_CLAIM_DIALECT + setId, q.getQuestionId(), q.getQuestion(), q
                .getLocale());
    }

    private List<ChallengeSetDTO> buildChallengesDTO(List<ChallengeQuestion> challengeQuestions, Integer offset,
                                                     Integer limit) {

        Map<String, List<ChallengeQuestion>> challengeSets = groupChallenges(challengeQuestions);
        return challengeSets.entrySet().stream().map((e) ->
                getChallengeSetDTO(e.getKey(), e.getValue())
        ).collect(Collectors.toList());
    }

    private ChallengeSetDTO getChallengeSetDTO(String questionSetId, List<ChallengeQuestion> questions) {
        ChallengeSetDTO challenge = new ChallengeSetDTO();
        challenge.setQuestionSetId(questionSetId);
        List<ChallengeQuestionDTO> questionDTOs = questions.stream().map(new ChallengeQuestionToExternal()).collect(
                Collectors.toList());
        challenge.setQuestions(questionDTOs);
        return challenge;
    }

    private List<UserChallengeAnswer> buildChallengeAnswers(List<ChallengeAnswerDTO> challengeAnswer) {

        return challengeAnswer.stream().map((q) ->
                new UserChallengeAnswer(
                        createChallenceQuestion(q.getQuestionSetId(), q.getChallengeQuestion()),
                        q.getAnswer()))
                .collect(Collectors.toList());
    }

    private Map<String, List<ChallengeQuestion>> groupChallenges(List<ChallengeQuestion> challengeQuestions) {
        return challengeQuestions.stream()
                .collect(groupingBy(question -> question.getQuestionSetId().split(WSO2_CLAIM_DIALECT)[1]));
    }

    private APIError handleIdentityRecoveryException(IdentityRecoveryException e, Constant.ErrorMessage errorEnum) {
        ErrorResponse errorResponse = getErrorBuilder(errorEnum).build(log, e, errorEnum.getDescription());

        Response.Status status;

        if (e instanceof IdentityRecoveryClientException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode : CHALLENGE_QUESTION_PREFIX
                        + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    private APIError handleError(Response.Status status, Constant.ErrorMessage error) {
        return new APIError(status, getErrorBuilder(error).build());
    }

    private ErrorResponse.Builder getErrorBuilder(Constant.ErrorMessage errorMsg){

       return new ErrorResponse.Builder()
               .withCode(errorMsg.getCode())
               .withMessage(errorMsg.getMessage())
               .withDescription(errorMsg.getDescription());
    }
}
