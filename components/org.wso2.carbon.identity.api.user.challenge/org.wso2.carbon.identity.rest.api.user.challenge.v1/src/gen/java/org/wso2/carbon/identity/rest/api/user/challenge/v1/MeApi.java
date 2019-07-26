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

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.*;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.MeApiService;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.factories.MeApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.ErrorDTO;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.UserChallengeAnswerDTO;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.ChallengeAnswerDTO;
import java.util.List;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.UserChallengeAnswerResponseDTO;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.ChallengeSetDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/me")


@io.swagger.annotations.Api(value = "/me", description = "the me API")
public class MeApi  {

   @Autowired
   private MeApiService delegate;

    @POST
    @Path("/challenge-answers/{challenge-set-id}")
    
    
    @io.swagger.annotations.ApiOperation(value = "answers a new challenge question", notes = "Adds a new challenge question answer to the system for loggedin user.\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "Item Created"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Element Already Exists"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response addChallengeAnswerOfLoggedInUser(@ApiParam(value = "Challenge Question set Id",required=true ) @PathParam("challenge-set-id")  String challengeSetId,
    @ApiParam(value = "challenge-question with answer"  ) UserChallengeAnswerDTO challengeAnswer)
    {
    return delegate.addChallengeAnswerOfLoggedInUser(challengeSetId,challengeAnswer);
    }
    @POST
    @Path("/challenge-answers")
    
    
    @io.swagger.annotations.ApiOperation(value = "answers a new challenge question", notes = "Adds a new challenge question answer to the system\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "Item Created"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Element Already Exists"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response addChallengeAnswersForLoggedInUser(@ApiParam(value = "challenge-question with answer"  ) List<ChallengeAnswerDTO> challengeAnswer)
    {
    return delegate.addChallengeAnswersForLoggedInUser(challengeAnswer);
    }
    @DELETE
    @Path("/challenge-answers/{challenge-set-id}")
    
    
    @io.swagger.annotations.ApiOperation(value = "removes a challenge question answer", notes = "Removes existing challenge question answers of authenticated user\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "No Content"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response deleteChallengeAnswerOfLoggedInUser(@ApiParam(value = "Challenge Question set Id",required=true ) @PathParam("challenge-set-id")  String challengeSetId)
    {
    return delegate.deleteChallengeAnswerOfLoggedInUser(challengeSetId);
    }
    @DELETE
    @Path("/challenge-answers")
    
    
    @io.swagger.annotations.ApiOperation(value = "removes challenge question answers", notes = "Removes an existing challenge question answers of the authenticated user\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "No Content"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response deleteChallengeAnswersOfLoggedInUser()
    {
    return delegate.deleteChallengeAnswersOfLoggedInUser();
    }
    @GET
    @Path("/challenge-answers")
    
    
    @io.swagger.annotations.ApiOperation(value = "get user's challenge answers", notes = "Get answered challenges in the system for a specific user.\n", response = UserChallengeAnswerResponseDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "search results matching criteria"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response getAnsweredChallengesOfLoggedInUser()
    {
    return delegate.getAnsweredChallengesOfLoggedInUser();
    }
    @GET
    @Path("/challenges")
    
    
    @io.swagger.annotations.ApiOperation(value = "searches challenge-question for authenticated user", notes = "Retrieve the\navailable challenge-question in the system for the authenticated user\n", response = ChallengeSetDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "search results matching criteria"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response getChallengesForLoggedInUser(@ApiParam(value = "number of records to skip for pagination") @QueryParam("offset")  Integer offset,
    @ApiParam(value = "maximum number of records to return") @QueryParam("limit")  Integer limit)
    {
    return delegate.getChallengesForLoggedInUser(offset,limit);
    }
    @PUT
    @Path("/challenge-answers/{challenge-set-id}")
    
    
    @io.swagger.annotations.ApiOperation(value = "answers a new challenge question", notes = "Update challenge answer in a specific challenge for authenticated user.\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response updateChallengeAnswerOfLoggedInUser(@ApiParam(value = "Challenge Question set Id",required=true ) @PathParam("challenge-set-id")  String challengeSetId,
    @ApiParam(value = "challenge-question with answer"  ) UserChallengeAnswerDTO challengeAnswer)
    {
    return delegate.updateChallengeAnswerOfLoggedInUser(challengeSetId,challengeAnswer);
    }
    @PUT
    @Path("/challenge-answers")
    
    
    @io.swagger.annotations.ApiOperation(value = "answers new challenge question combination", notes = "Addsnew challenge question answers to the system for logged In user.\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response updateChallengeAnswersOfLoggedInUser(@ApiParam(value = "set of challenge question with answer"  ) List<ChallengeAnswerDTO> challengeAnswers)
    {
    return delegate.updateChallengeAnswersOfLoggedInUser(challengeAnswers);
    }
}

