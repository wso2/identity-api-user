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

public abstract class MeApiService {
    public abstract Response addChallengeAnswerOfLoggedInUser(String challengeSetId,String userId,UserChallengeAnswerDTO challengeAnswer);
    public abstract Response addChallengeAnswersForLoggedInUser(String userId,List<ChallengeAnswerDTO> challengeAnswer);
    public abstract Response deleteChallengeAnswerOfLoggedInUser(String challengeSetId,String userId);
    public abstract Response deleteChallengeAnswersOfLoggedInUser(String userId);
    public abstract Response getAnsweredChallengesOfLoggedInUser(String userId);
    public abstract Response getChallengesForLoggedInUser(Integer offset,Integer limit);
    public abstract Response updateChallengeAnswerOfLoggedInUser(String challengeSetId,String userId,UserChallengeAnswerDTO challengeAnswer);
}

