package org.wso2.carbon.identity.rest.api.user.challenge.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.core.UserChallengeService;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.ChallengeAnswerDTO;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.UserChallengeAnswerDTO;

import javax.ws.rs.core.Response;
import java.util.List;

import static org.wso2.carbon.identity.api.user.common.ContextLoader.getUserFromContext;

/**
 * API service implementation for authenticated user's challenge operations
 */
public class MeApiServiceImpl extends MeApiService {

    @Autowired
    private UserChallengeService challengeService;

    @Override
    public Response addChallengeAnswerOfLoggedInUser(String challengeSetId, UserChallengeAnswerDTO
            challengeAnswer) {

        challengeService.addChallengeAnswerOfUser(getUserFromContext(), challengeSetId, challengeAnswer);
        return Response.ok().build();
    }

    @Override
    public Response addChallengeAnswersForLoggedInUser(List<ChallengeAnswerDTO> challengeAnswer) {

        challengeService.setChallengeAnswersOfUser(getUserFromContext(), challengeAnswer);
        return Response.ok().build();
    }

    @Override
    public Response deleteChallengeAnswerOfLoggedInUser(String challengeSetId) {

        challengeService.removeChallengeAnswerOfUser(getUserFromContext(), challengeSetId);
        return Response.noContent().build();
    }

    @Override
    public Response deleteChallengeAnswersOfLoggedInUser() {

        challengeService.removeChallengeAnswersOfUser(getUserFromContext());
        return Response.noContent().build();
    }

    @Override
    public Response getAnsweredChallengesOfLoggedInUser() {

        return Response.ok().entity(challengeService.getChallengeAnswersOfUser(getUserFromContext())).build();
    }

    @Override
    public Response getChallengesForLoggedInUser(Integer offset, Integer limit) {

        return Response.ok().entity(challengeService.getChallengesForUser(getUserFromContext(), offset, limit)).build();
    }

    @Override
    public Response updateChallengeAnswerOfLoggedInUser(String challengeSetId, UserChallengeAnswerDTO
            challengeAnswer) {

        challengeService.updateChallengeAnswerOfUser(getUserFromContext(), challengeSetId, challengeAnswer);
        return Response.ok().build();
    }

    @Override
    public Response updateChallengeAnswersOfLoggedInUser(List<ChallengeAnswerDTO> challengeAnswers) {

        challengeService.updateChallengeAnswersOfUser(getUserFromContext(), challengeAnswers);
        return Response.ok().build();
    }
}
