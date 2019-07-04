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
    public abstract Response getChallengesForAUser(String userId,Integer offset,Integer limit);
    public abstract Response updateChallengeAnswerOfAUser(String challengeSetId,String userId,UserChallengeAnswerDTO challengeAnswer);
    public abstract Response updateChallengeAnswersOfAUser(String userId,List<ChallengeAnswerDTO> challengeAnswers);
}

