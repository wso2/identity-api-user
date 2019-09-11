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
import org.wso2.carbon.identity.api.user.challenge.common.ChallengeQuestionServiceHolder;
import org.wso2.carbon.identity.api.user.challenge.common.Constant;
import org.wso2.carbon.identity.api.user.common.Constants;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

import static java.util.stream.Collectors.groupingBy;

/**
 * Call internal osgi services to perform user challenge related operations
 */
public class UserChallengeService {

    private static final Log log = LogFactory.getLog(UserChallengeService.class);
    public static final String WSO2_CLAIM_DIALECT = "http://wso2.org/claims/";

    /**
     * Get challenges available for a specific user to answer
     *
     * @param user   user
     * @param offset limit
     * @param limit  offset
     * @return list of available Challenges
     */
    public List<ChallengeSetDTO> getChallengesForUser(User user, Integer offset, Integer limit) {

        try {
            return buildChallengesDTO(ChallengeQuestionServiceHolder.getChallengeQuestionManager()
                    .getAllChallengeQuestionsForUser(ContextLoader.getTenantDomainFromContext(), user), offset, limit);
        } catch (IdentityRecoveryException e) {
            throw handleIdentityRecoveryException(e,
                    Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_CHALLENGES_FOR_USER);
        }

    }

    /**
     * Set user specified answers to a new challenge combination
     *
     * @param user             user
     * @param challengeAnswers user's answers for selected challenges
     * @return operation success
     */
    public boolean setChallengeAnswersOfUser(User user, List<ChallengeAnswerDTO> challengeAnswers) {

        List<UserChallengeAnswer> answers = buildChallengeAnswers(challengeAnswers);
        try {
            List<String> answeredList = ChallengeQuestionServiceHolder.getChallengeQuestionManager()
                    .getChallengeQuestionUris(user);
            if (answeredList.size() > 0) {
                throw handleError(Response.Status.CONFLICT,
                        Constant.ErrorMessage.ERROR_CODE_USER_ALREADY_ANSWERED_CHALLENGES);
            }
            ChallengeQuestionServiceHolder.getChallengeQuestionManager()
                    .setChallengesOfUser(user, answers.toArray(new UserChallengeAnswer[answers.size()]));
        } catch (IdentityRecoveryException e) {
            throw handleIdentityRecoveryException(e,
                    Constant.ErrorMessage.ERROR_CODE_ERROR_SETTING_CHALLENGE_ANSWERS_OF_USER);
        }
        return true;
    }

    /**
     * Update user challenge answers with a new challenge combination
     *
     * @param user                user
     * @param newChallengeAnswers user's answers for selected challenges
     * @return operation success
     */
    public boolean updateChallengeAnswersOfUser(User user, List<ChallengeAnswerDTO> newChallengeAnswers) {

        List<UserChallengeAnswer> answers = buildChallengeAnswers(newChallengeAnswers);
        try {
            validateUserAnsweredChallenges(user);
            ChallengeQuestionServiceHolder.getChallengeQuestionManager()
                    .setChallengesOfUser(user, answers.toArray(new UserChallengeAnswer[answers.size()]));

        } catch (IdentityRecoveryException e) {

            throw handleIdentityRecoveryException(e,
                    Constant.ErrorMessage.ERROR_CODE_ERROR_UPDATING_CHALLENGE_ANSWERS_OF_USER);
        }
        return true;
    }

    /**
     * Update user challenge answer of a specific challenge set
     *
     * @param user            user
     * @param challengeSetId  challenge set id
     * @param challengeAnswer challenge answer
     * @return operation success
     */
    public boolean updateChallengeAnswerOfUser(User user, String challengeSetId,
                                               UserChallengeAnswerDTO challengeAnswer) {

        try {
            validateUserAnsweredChallenge(user, challengeSetId);
            UserChallengeAnswer answer = new UserChallengeAnswer(
                    createChallengeQuestion(challengeSetId, challengeAnswer.getChallengeQuestion()),
                    challengeAnswer.getAnswer());
            ChallengeQuestionServiceHolder.getChallengeQuestionManager().setChallengeOfUser(user, answer);
        } catch (IdentityRecoveryException e) {

            throw handleIdentityRecoveryException(e,
                    Constant.ErrorMessage.ERROR_CODE_ERROR_UPDATING_CHALLENGE_ANSWER_OF_USER);
        }
        return true;
    }

    /**
     * Add user challenge answer to a specific challenge
     *
     * @param user            user
     * @param challengeSetId  challenge set id
     * @param challengeAnswer challenge answer
     * @return operation success
     */
    public boolean addChallengeAnswerOfUser(User user, String challengeSetId, UserChallengeAnswerDTO challengeAnswer) {

        try {
            List<String> answeredList = ChallengeQuestionServiceHolder.getChallengeQuestionManager()
                    .getChallengeQuestionUris(user);
            if (!answeredList.isEmpty() && answeredList.contains(WSO2_CLAIM_DIALECT + challengeSetId)) {
                throw handleError(Response.Status.CONFLICT,
                        Constant.ErrorMessage.ERROR_CODE_USER_ALREADY_ANSWERED_CHALLENGE);
            }
            UserChallengeAnswer answer = new UserChallengeAnswer(
                    createChallengeQuestion(challengeSetId, challengeAnswer.getChallengeQuestion()),
                    challengeAnswer.getAnswer());
            ChallengeQuestionServiceHolder.getChallengeQuestionManager().setChallengeOfUser(user, answer);
        } catch (IdentityRecoveryException e) {

            throw handleIdentityRecoveryException(e,
                    Constant.ErrorMessage.ERROR_CODE_ERROR_SETTING_CHALLENGE_ANSWER_OF_USER);
        }
        return true;
    }

    /**
     * Get answered challenges of a user
     *
     * @param user user
     * @return list of answered challenges
     */
    public List<UserChallengeAnswerResponseDTO> getChallengeAnswersOfUser(User user) {

        try {
            return getUserChallengeAnswerDTOsOfUser(user);
        } catch (IdentityRecoveryException e) {

            throw handleIdentityRecoveryException(e,
                    Constant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_CHALLENGE_ANSWERS_OF_USER);
        }
    }

    /**
     * Remove answers of all the challenges that user has answered
     *
     * @param user user
     * @return operation success
     */
    public boolean removeChallengeAnswersOfUser(User user) {

        try {
            validateUserAnsweredChallenges(user);

            ChallengeQuestionServiceHolder.getChallengeQuestionManager().removeChallengeAnswersOfUser(user);
        } catch (IdentityRecoveryException e) {

            throw handleIdentityRecoveryException(e,
                    Constant.ErrorMessage.ERROR_CODE_ERROR_DELETING_CHALLENGE_ANSWERS_OF_USER);
        }
        return true;
    }

    /**
     * Remove answer of a specific challenge that user has answered
     *
     * @param user           user
     * @param challengeSetId challenge set id
     * @return operation success
     */
    public boolean removeChallengeAnswerOfUser(User user, String challengeSetId) {
        try {
            validateUserAnsweredChallenge(user, challengeSetId);
            ChallengeQuestionServiceHolder.getChallengeQuestionManager()
                    .removeChallengeAnswerOfUser(user, WSO2_CLAIM_DIALECT + challengeSetId);
        } catch (IdentityRecoveryException e) {

            throw handleIdentityRecoveryException(e,
                    Constant.ErrorMessage.ERROR_CODE_ERROR_DELETING_CHALLENGE_ANSWER_OF_USER);
        }
        return true;
    }

    /**
     * Check whether user has answered any challenge
     *
     * @param user
     * @throws IdentityRecoveryException
     */
    private void validateUserAnsweredChallenges(User user) throws IdentityRecoveryException {
        List<String> answeredList = ChallengeQuestionServiceHolder.getChallengeQuestionManager()
                .getChallengeQuestionUris(user);
        if (answeredList.size() < 1) {
            throw handleError(Response.Status.NOT_FOUND,
                    Constant.ErrorMessage.ERROR_CODE_USER_HAS_NOT_ANSWERED_CHALLENGES);
        }
    }

    /**
     * Check whether user has answered a specific challenge
     *
     * @param user
     * @param challengeSetId
     * @throws IdentityRecoveryException
     */
    private void validateUserAnsweredChallenge(User user, String challengeSetId) throws IdentityRecoveryException {
        List<String> answeredList = ChallengeQuestionServiceHolder.getChallengeQuestionManager()
                .getChallengeQuestionUris(user);
        if (answeredList.isEmpty() || !answeredList.contains(WSO2_CLAIM_DIALECT + challengeSetId)) {
            throw handleError(Response.Status.NOT_FOUND,
                    Constant.ErrorMessage.ERROR_CODE_USER_HAS_NOT_ANSWERED_CHALLENGE);
        }
    }

    /**
     * Retrieve user challenge answers and transform
     *
     * @param user
     * @return
     * @throws IdentityRecoveryException
     */
    private List<UserChallengeAnswerResponseDTO> getUserChallengeAnswerDTOsOfUser(User user)
            throws IdentityRecoveryException {
        UserChallengeAnswer[] answers = ChallengeQuestionServiceHolder.getChallengeQuestionManager()
                .getChallengeAnswersOfUser(user);
        return Arrays.stream(answers).map(new UserChallengeAnswerToExternal()).collect(Collectors.toList());
    }

    /**
     * Transform incoming ChallengeQuestionDTO to ChallengeQuestion to be sent for osgi service
     *
     * @param setId
     * @param q
     * @return
     */
    private ChallengeQuestion createChallengeQuestion(String setId, ChallengeQuestionDTO q) {
        return new ChallengeQuestion(WSO2_CLAIM_DIALECT + setId, q.getQuestionId(), q.getQuestion(), q.getLocale());
    }

    /**
     * Transform ChallengeQuestion list to ChallengeSetDTO list
     *
     * @param challengeQuestions
     * @param offset
     * @param limit
     * @return
     */
    private List<ChallengeSetDTO> buildChallengesDTO(List<ChallengeQuestion> challengeQuestions, Integer offset,
                                                     Integer limit) {

        Map<String, List<ChallengeQuestion>> challengeSets = groupChallenges(challengeQuestions);
        return challengeSets.entrySet().stream().map((e) -> getChallengeSetDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Build ChallengeSetDTO from ChallengeQuestion list
     *
     * @param questionSetId
     * @param questions
     * @return
     */
    private ChallengeSetDTO getChallengeSetDTO(String questionSetId, List<ChallengeQuestion> questions) {
        ChallengeSetDTO challenge = new ChallengeSetDTO();
        challenge.setQuestionSetId(questionSetId);
        List<ChallengeQuestionDTO> questionDTOs = questions.stream().map(new ChallengeQuestionToExternal())
                .collect(Collectors.toList());
        challenge.setQuestions(questionDTOs);
        return challenge;
    }

    /**
     * build list of UserChallengeAnswer from a ChallengeAnswerDTO list
     *
     * @param challengeAnswer
     * @return
     */
    private List<UserChallengeAnswer> buildChallengeAnswers(List<ChallengeAnswerDTO> challengeAnswer) {

        return challengeAnswer.stream().map((q) -> new UserChallengeAnswer(
                createChallengeQuestion(q.getQuestionSetId(), q.getChallengeQuestion()), q.getAnswer()))
                .collect(Collectors.toList());
    }

    private Map<String, List<ChallengeQuestion>> groupChallenges(List<ChallengeQuestion> challengeQuestions) {
        return challengeQuestions.stream()
                .collect(groupingBy(question -> question.getQuestionSetId().split(WSO2_CLAIM_DIALECT)[1]));
    }

    /**
     * Handle IdentityRecoveryException, extract error code, error description and status code to be sent in the
     * response
     *
     * @param e
     * @param errorEnum
     * @return
     */
    private APIError handleIdentityRecoveryException(IdentityRecoveryException e, Constant.ErrorMessage errorEnum) {
        ErrorResponse errorResponse = getErrorBuilder(errorEnum).build(log, e, errorEnum.getDescription());

        Response.Status status;

        if (e instanceof IdentityRecoveryClientException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode = errorCode.contains(Constants.ERROR_CODE_DELIMITER) ?
                        errorCode :
                        Constant.CHALLENGE_QUESTION_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    /**
     * Handle User errors
     *
     * @param status
     * @param error
     * @return
     */
    private APIError handleError(Response.Status status, Constant.ErrorMessage error) {
        return new APIError(status, getErrorBuilder(error).build());
    }

    /**
     * Get ErrorResponse Builder for Error enum
     *
     * @param errorEnum
     * @return
     */
    private ErrorResponse.Builder getErrorBuilder(Constant.ErrorMessage errorEnum) {

        return new ErrorResponse.Builder().withCode(errorEnum.getCode()).withMessage(errorEnum.getMessage())
                .withDescription(errorEnum.getDescription());
    }
}
