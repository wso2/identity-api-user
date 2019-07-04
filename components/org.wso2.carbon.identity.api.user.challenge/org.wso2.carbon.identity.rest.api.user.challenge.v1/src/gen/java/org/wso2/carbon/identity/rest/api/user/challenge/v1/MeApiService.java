package org.wso2.carbon.identity.rest.api.user.challenge.v1;

import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.ChallengeAnswerDTO;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.UserChallengeAnswerDTO;

import javax.ws.rs.core.Response;
import java.util.List;

public abstract class MeApiService {
    public abstract Response addChallengeAnswerOfLoggedInUser(String challengeSetId,UserChallengeAnswerDTO challengeAnswer);
    public abstract Response addChallengeAnswersForLoggedInUser(List<ChallengeAnswerDTO> challengeAnswer);
    public abstract Response deleteChallengeAnswerOfLoggedInUser(String challengeSetId);
    public abstract Response deleteChallengeAnswersOfLoggedInUser();
    public abstract Response getAnsweredChallengesOfLoggedInUser();
    public abstract Response getChallengesForLoggedInUser(Integer offset,Integer limit);
    public abstract Response updateChallengeAnswerOfLoggedInUser(String challengeSetId,UserChallengeAnswerDTO challengeAnswer);
    public abstract Response updateChallengeAnswersOfLoggedInUser(List<ChallengeAnswerDTO> challengeAnswers);
}

