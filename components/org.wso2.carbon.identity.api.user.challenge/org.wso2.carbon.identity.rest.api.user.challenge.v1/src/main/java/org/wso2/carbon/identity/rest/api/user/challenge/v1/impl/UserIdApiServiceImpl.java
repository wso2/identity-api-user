/*
 *  Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.wso2.carbon.identity.rest.api.user.challenge.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.user.common.ContextLoader;
import org.wso2.carbon.identity.api.user.common.function.UserIdToUser;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.UserIdApiService;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.core.UserChallengeService;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.ChallengeAnswerDTO;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.UserChallengeAnswerDTO;

import java.net.URI;
import java.util.List;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.challenge.common.Constant.USER_CHALLENGE_ANSWERS_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.user.challenge.common.Constant.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.user.common.ContextLoader.buildURI;

/**
 * API service implementation of a specific user's challenge operations
 */
public class UserIdApiServiceImpl extends UserIdApiService {

    @Autowired
    private UserChallengeService challengeService;

    @Override
    public Response addChallengeAnswerOfAUser(String challengeSetId, String userId,
            UserChallengeAnswerDTO challengeAnswer) {

        challengeService
                .addChallengeAnswerOfUser(new UserIdToUser().apply(userId, ContextLoader.getTenantDomainFromContext()),
                        challengeSetId, challengeAnswer);
        return Response.created(getUserChallengeAnswersLocation(userId)).build();
    }

    @Override
    public Response addChallengeAnswersOfAUser(String userId, List<ChallengeAnswerDTO> challengeAnswer) {

        challengeService
                .setChallengeAnswersOfUser(new UserIdToUser().apply(userId, ContextLoader.getTenantDomainFromContext()),
                        challengeAnswer);
        return Response.created(getUserChallengeAnswersLocation(userId)).build();
    }

    @Override
    public Response deleteChallengeAnswerOfAUser(String challengeSetId, String userId) {

        challengeService.removeChallengeAnswerOfUser(
                new UserIdToUser().apply(userId, ContextLoader.getTenantDomainFromContext()), challengeSetId);
        return Response.noContent().build();
    }

    @Override
    public Response deleteChallengeAnswersOfAUser(String userId) {

        challengeService.removeChallengeAnswersOfUser(
                new UserIdToUser().apply(userId, ContextLoader.getTenantDomainFromContext()));
        return Response.noContent().build();
    }

    @Override
    public Response getAnsweredChallengesOfAUser(String userId) {

        return Response.ok().entity(challengeService.getChallengeAnswersOfUser(
                new UserIdToUser().apply(userId, ContextLoader.getTenantDomainFromContext()))).build();
    }

    @Override
    public Response getChallengesForAUser(String userId, Integer offset, Integer limit) {

        return Response.ok().entity(challengeService
                .getChallengesForUser(new UserIdToUser().apply(userId, ContextLoader.getTenantDomainFromContext()),
                        offset, limit)).build();
    }

    @Override
    public Response updateChallengeAnswerOfAUser(String challengeSetId, String userId,
            UserChallengeAnswerDTO challengeAnswer) {

        challengeService.updateChallengeAnswerOfUser(
                new UserIdToUser().apply(userId, ContextLoader.getTenantDomainFromContext()), challengeSetId,
                challengeAnswer);
        return Response.ok().build();
    }

    @Override
    public Response updateChallengeAnswersOfAUser(String userId, List<ChallengeAnswerDTO> challengeAnswers) {

        challengeService.updateChallengeAnswersOfUser(
                new UserIdToUser().apply(userId, ContextLoader.getTenantDomainFromContext()), challengeAnswers);
        return Response.ok().build();
    }

    private URI getUserChallengeAnswersLocation(String userId) {
        return buildURI(String.format(V1_API_PATH_COMPONENT + USER_CHALLENGE_ANSWERS_PATH_COMPONENT, userId));
    }

}
