package org.wso2.carbon.identity.rest.api.user.challenge.v1.impl;

import org.wso2.carbon.identity.rest.api.user.challenge.v1.*;


import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.ChallengeAnswerDTO;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.UserChallengeAnswerDTO;

import java.util.List;

import javax.ws.rs.core.Response;

public class MeApiServiceImpl extends MeApiService {
    @Override
    public Response addChallengeAnswerOfLoggedInUser(String challengeSetId, String userId, UserChallengeAnswerDTO
            challengeAnswer) {
        return null;
    }

    @Override
    public Response addChallengeAnswersForLoggedInUser(String userId,List<ChallengeAnswerDTO> challengeAnswer){
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response deleteChallengeAnswerOfLoggedInUser(String challengeSetId,String userId){
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response deleteChallengeAnswersOfLoggedInUser(String userId){
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response getAnsweredChallengesOfLoggedInUser(String userId){
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response getChallengesForLoggedInUser(Integer offset,Integer limit){
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response updateChallengeAnswerOfLoggedInUser(String challengeSetId,String userId,UserChallengeAnswerDTO
            challengeAnswer){
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
