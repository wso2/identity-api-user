package org.wso2.carbon.identity.rest.api.user.challenge.v1;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.ChallengeAnswerDTO;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.ChallengeSetDTO;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.UserChallengeAnswerDTO;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.UserChallengeAnswerResponseDTO;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.factories.MeApiServiceFactory;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/me")


@io.swagger.annotations.Api(value = "/me", description = "the me API")
public class MeApi {

//   private final MeApiService delegate = MeApiServiceFactory.getMeApi();
    @Autowired
    private MeApiService delegate;

    @POST
    @Path("/challenge-answers/{challenge-set-id}")


    @io.swagger.annotations.ApiOperation(value = "answers a new challenge question", notes = "Adds a new challenge question answer to the system for loggedin user.\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),

            @io.swagger.annotations.ApiResponse(code = 409, message = "Element Already Exists"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error")})

    public Response addChallengeAnswerOfLoggedInUser(@ApiParam(value = "Challenge Question set Id", required = true) @PathParam("challenge-set-id") String challengeSetId,
                                                     @ApiParam(value = "challenge-question with answer") UserChallengeAnswerDTO challengeAnswer) {
        return delegate.addChallengeAnswerOfLoggedInUser(challengeSetId, challengeAnswer);
    }

    @POST
    @Path("/challenge-answers")


    @io.swagger.annotations.ApiOperation(value = "answers a new challenge question", notes = "Adds a new challenge question answer to the system\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 201, message = "Item Created"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 409, message = "Element Already Exists"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error")})

    public Response addChallengeAnswersForLoggedInUser(@ApiParam(value = "challenge-question with answer") List<ChallengeAnswerDTO> challengeAnswer) {
        return delegate.addChallengeAnswersForLoggedInUser(challengeAnswer);
    }

    @DELETE
    @Path("/challenge-answers/{challenge-set-id}")


    @io.swagger.annotations.ApiOperation(value = "removes a challenge question answer", notes = "Removes existing challenge question answers of authenticated user\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Item Deleted"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error")})

    public Response deleteChallengeAnswerOfLoggedInUser(@ApiParam(value = "Challenge Question set Id", required = true) @PathParam("challenge-set-id") String challengeSetId) {
        return delegate.deleteChallengeAnswerOfLoggedInUser(challengeSetId);
    }

    @DELETE
    @Path("/challenge-answers")


    @io.swagger.annotations.ApiOperation(value = "removes challenge question answers", notes = "Removes an existing challenge question answers of the authenticated user\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Item Deleted"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error")})

    public Response deleteChallengeAnswersOfLoggedInUser() {
        return delegate.deleteChallengeAnswersOfLoggedInUser();
    }

    @GET
    @Path("/challenge-answers")


    @io.swagger.annotations.ApiOperation(value = "get user's challenge answers", notes = "Get answered challenges in the system for a specific user.\n", response = UserChallengeAnswerResponseDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "search results matching criteria"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error")})

    public Response getAnsweredChallengesOfLoggedInUser() {
        return delegate.getAnsweredChallengesOfLoggedInUser();
    }

    @GET
    @Path("/challenges")


    @io.swagger.annotations.ApiOperation(value = "searches challenge-question for authenticated user", notes = "Retrieve the\navailable challenge-question in the system for the authenticated user\n", response = ChallengeSetDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "search results matching criteria"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error")})

    public Response getChallengesForLoggedInUser(@ApiParam(value = "number of records to skip for pagination") @QueryParam("offset") Integer offset,
                                                 @ApiParam(value = "maximum number of records to return") @QueryParam("limit") Integer limit) {
        return delegate.getChallengesForLoggedInUser(offset, limit);
    }

    @PUT
    @Path("/challenge-answers/{challenge-set-id}")


    @io.swagger.annotations.ApiOperation(value = "answers a new challenge question", notes = "Update challenge answer in a specific challenge for authenticated user.\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error")})

    public Response updateChallengeAnswerOfLoggedInUser(@ApiParam(value = "Challenge Question set Id", required = true) @PathParam("challenge-set-id") String challengeSetId,
                                                        @ApiParam(value = "challenge-question with answer") UserChallengeAnswerDTO challengeAnswer) {
        return delegate.updateChallengeAnswerOfLoggedInUser(challengeSetId, challengeAnswer);
    }

    @PUT
    @Path("/challenge-answers")


    @io.swagger.annotations.ApiOperation(value = "answers new challenge question combination", notes = "Addsnew challenge question answers to the system for logged In user.\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error")})

    public Response updateChallengeAnswersOfLoggedInUser(@ApiParam(value = "set of challenge question with answer") List<ChallengeAnswerDTO> challengeAnswers) {
        return delegate.updateChallengeAnswersOfLoggedInUser(challengeAnswers);
    }
}

