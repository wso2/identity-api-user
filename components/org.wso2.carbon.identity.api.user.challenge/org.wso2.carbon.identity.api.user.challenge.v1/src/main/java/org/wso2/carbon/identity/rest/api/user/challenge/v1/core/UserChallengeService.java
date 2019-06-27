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
import org.wso2.carbon.identity.api.user.common.Constants;
import org.wso2.carbon.identity.api.user.common.ContextLoader;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.recovery.ChallengeQuestionManager;
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
import static org.wso2.carbon.identity.api.user.common.Constants.ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessages.ERROR_CODE_ERROR_DELETING_CHALLENGE_ANSWERS_OF_USER;
import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessages.ERROR_CODE_ERROR_DELETING_CHALLENGE_ANSWER_OF_USER;
import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessages.ERROR_CODE_ERROR_RETRIVING_CHALLENGES_FOR_USER;
import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessages.ERROR_CODE_ERROR_RETRIVING_CHALLENGE_ANSWERS_OF_USER;
import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessages.ERROR_CODE_ERROR_SETTING_CHALLENGE_ANSWERS_OF_USER;
import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessages.ERROR_CODE_ERROR_SETTING_CHALLENGE_ANSWER_OF_USER;
import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessages.ERROR_CODE_ERROR_UPDATING_CHALLENGE_ANSWERS_OF_USER;
import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessages.ERROR_CODE_ERROR_UPDATING_CHALLENGE_ANSWER_OF_USER;
import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessages.ERROR_CODE_USER_ALREADY_ANSWERED_CHALLENGE;
import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessages.ERROR_CODE_USER_ALREADY_ANSWERED_CHALLENGES;
import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessages.ERROR_CODE_USER_HAS_NOT_ANSWERED_CHALLENGE;
import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessages.ERROR_CODE_USER_HAS_NOT_ANSWERED_CHALLENGES;
import static org.wso2.carbon.identity.api.user.common.Constants.ErrorPrefix.CHALLENGE_QUESTION_PREFIX;

public class UserChallengeService {

    private static final Log log = LogFactory.getLog(UserChallengeService.class);
    private static ChallengeQuestionManager questionManager = ChallengeQuestionManager.getInstance();
    public static final String WSO2_CLAIM_DIALECT = "http://wso2.org/claims/";

    public List<ChallengeSetDTO> getChallengesForUser(User user, Integer offset, Integer limit) {

        try {
            return buildChallengesDTO(questionManager.getAllChallengeQuestionsForUser(ContextLoader
                    .getTenantDomainFromContext(), user), offset, limit);
        } catch (IdentityRecoveryException e) {
            handleIdentityRecoveryException(e, ERROR_CODE_ERROR_RETRIVING_CHALLENGES_FOR_USER);
            return null;
        }

    }

    public boolean setChallengeAnswersOfUser(User user, List<ChallengeAnswerDTO> challengeAnswers) {

        List<UserChallengeAnswer> answers = buildChallengeAnswers(challengeAnswers);
        try {
            List<String> answeredList = questionManager.getChallengeQuestionUris(user);
            if (answeredList.size() > 0) {
                handleError(Response.Status.CONFLICT, ERROR_CODE_USER_ALREADY_ANSWERED_CHALLENGES);
            }
            questionManager.setChallengesOfUser(user, answers.toArray(new UserChallengeAnswer[answers.size()]));
        } catch (IdentityRecoveryException e) {
            handleIdentityRecoveryException(e, ERROR_CODE_ERROR_SETTING_CHALLENGE_ANSWERS_OF_USER);
            return false;
        }
        return true;
    }

    public boolean updateChallengeAnswersOfUser(User user, List<ChallengeAnswerDTO> newChallengeAnswers) {

        List<UserChallengeAnswer> answers = buildChallengeAnswers(newChallengeAnswers);
        try {
            validateUserAnsweredChallenges(user);
            questionManager.setChallengesOfUser(user, answers.toArray(new UserChallengeAnswer[answers.size()]));

        } catch (IdentityRecoveryException e) {

            handleIdentityRecoveryException(e, ERROR_CODE_ERROR_UPDATING_CHALLENGE_ANSWERS_OF_USER);
            return false;
        }
        return true;
    }

    public boolean updateChallengeAnswerOfUser(User user, String challengeSetId, UserChallengeAnswerDTO
            challengeAnswer) {

        //TODO This will override all the questions, need to implement backend to update only one
        try {
            validateUserAnsweredChallenge(user, challengeSetId);
            UserChallengeAnswer answer = new UserChallengeAnswer(
                    createChallenceQuestion(challengeSetId, challengeAnswer.getChallengeQuestion()),
                    challengeAnswer.getAnswer());
            questionManager.setChallengeOfUser(user, answer);
        } catch (IdentityRecoveryException e) {

            handleIdentityRecoveryException(e, ERROR_CODE_ERROR_UPDATING_CHALLENGE_ANSWER_OF_USER);
            return false;
        }
        return true;
    }

    public boolean addChallengeAnswerOfUser(User user, String challengeSetId, UserChallengeAnswerDTO
            challengeAnswer) {

        try {
            List<String> answeredList = questionManager.getChallengeQuestionUris(user);
            if (!answeredList.isEmpty() && answeredList.contains(WSO2_CLAIM_DIALECT + challengeSetId)) {
                handleError(Response.Status.CONFLICT, ERROR_CODE_USER_ALREADY_ANSWERED_CHALLENGE);
            }
            UserChallengeAnswer answer = new UserChallengeAnswer(
                    createChallenceQuestion(challengeSetId, challengeAnswer.getChallengeQuestion()),
                    challengeAnswer.getAnswer());
            questionManager.setChallengeOfUser(user, answer);
        } catch (IdentityRecoveryException e) {

            handleIdentityRecoveryException(e, ERROR_CODE_ERROR_SETTING_CHALLENGE_ANSWER_OF_USER);
            return true;
        }
        return true;
    }

    public List<UserChallengeAnswerResponseDTO> getChallengeAnswersOfUser(User user) {

        try {
            return getUserChallengeAnswerDTOsOfUser(user);
        } catch (IdentityRecoveryException e) {

            handleIdentityRecoveryException(e, ERROR_CODE_ERROR_RETRIVING_CHALLENGE_ANSWERS_OF_USER);
            return null;
        }
    }

    public boolean removeChallengeAnswersOfUser(User user) {

        try {
            validateUserAnsweredChallenges(user);

            questionManager.removeChallengeAnswersOfUser(user);
        } catch (IdentityRecoveryException e) {

            handleIdentityRecoveryException(e, ERROR_CODE_ERROR_DELETING_CHALLENGE_ANSWERS_OF_USER);
            return false;
        }
        return true;
    }

    public boolean removeChallengeAnswerOfUser(User user, String challengeSetId) {
        try {
            validateUserAnsweredChallenge(user, challengeSetId);
            questionManager.removeChallengeAnswerOfUser(user, WSO2_CLAIM_DIALECT + challengeSetId);
        } catch (IdentityRecoveryException e) {

            handleIdentityRecoveryException(e, ERROR_CODE_ERROR_DELETING_CHALLENGE_ANSWER_OF_USER);
            return false;
        }
        return true;
    }

    private void validateUserAnsweredChallenges(User user) throws IdentityRecoveryException {
        List<String> answeredList = questionManager.getChallengeQuestionUris(user);
        if (answeredList.size() < 1) {
            handleError(Response.Status.NOT_FOUND, ERROR_CODE_USER_HAS_NOT_ANSWERED_CHALLENGES);
        }
    }

    private void validateUserAnsweredChallenge(User user, String challengeSetId) throws IdentityRecoveryException {
        List<String> answeredList = questionManager.getChallengeQuestionUris(user);
        if (answeredList.isEmpty() || !answeredList.contains(WSO2_CLAIM_DIALECT + challengeSetId)) {
            handleError(Response.Status.NOT_FOUND, ERROR_CODE_USER_HAS_NOT_ANSWERED_CHALLENGE);
        }
    }


    private List<UserChallengeAnswerResponseDTO> getUserChallengeAnswerDTOsOfUser(User user) throws IdentityRecoveryException {
        UserChallengeAnswer[] answers = questionManager.getChallengeAnswersOfUser(user);
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

    private void handleIdentityRecoveryException(IdentityRecoveryException e, Constants.ErrorMessages errorEnum) {
        ErrorResponse errorResponse = new ErrorResponse.Builder().withError(errorEnum).build(log, e, errorEnum
                .getDescription());

        Response.Status status;

        if (e instanceof IdentityRecoveryClientException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode : CHALLENGE_QUESTION_PREFIX.getPrefix()
                        + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        throw new APIError(status, errorResponse);
    }

    private void handleError(Response.Status status, Constants.ErrorMessages error) {
        throw new APIError(status, new ErrorResponse.Builder().withError(error).build());
    }
}
