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

package org.wso2.carbon.identity.rest.api.user.challenge.v1;

import org.wso2.carbon.identity.rest.api.user.challenge.v1.*;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.*;

import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.ErrorDTO;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.UserChallengeAnswerDTO;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.ChallengeAnswerDTO;
import java.util.List;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.UserChallengeAnswerResponseDTO;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.ChallengeSetDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import javax.ws.rs.core.Response;

public abstract class UserIdApiService {
    public abstract Response addChallengeAnswerOfAUser(String challengeSetId,String userId,UserChallengeAnswerDTO challengeAnswer);
    public abstract Response addChallengeAnswersOfAUser(String userId,List<ChallengeAnswerDTO> challengeAnswer);
    public abstract Response deleteChallengeAnswerOfAUser(String challengeSetId,String userId);
    public abstract Response deleteChallengeAnswersOfAUser(String userId);
    public abstract Response getAnsweredChallengesOfAUser(String userId);
    public abstract Response getChallengesForAUser(String userId,Integer limit,Integer offset);
    public abstract Response updateChallengeAnswerOfAUser(String challengeSetId,String userId,UserChallengeAnswerDTO challengeAnswer);
    public abstract Response updateChallengeAnswersOfAUser(String userId,List<ChallengeAnswerDTO> challengeAnswers);
}

