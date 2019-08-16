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
import org.wso2.carbon.identity.rest.api.user.challenge.v1.UserIdApiService;
import org.wso2.carbon.identity.rest.api.user.challenge.v1.factories.UserIdApiServiceFactory;

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

@Path("/{user-id}")


@io.swagger.annotations.Api(value = "/{user-id}", description = "the {user-id} API")
public class UserIdApi  {

   @Autowired
   private UserIdApiService delegate;

    @POST
    @Path("/challenge-answers/{challenge-set-id}")
    
    
    @io.swagger.annotations.ApiOperation(value = "Answers a specific new challenge.", notes = "Provide an **answer** to **a specific challenge** in the system for the user identified by user-id. The user can at most select one question from a challenge set of interest.\n\n  <b>Permission required:</b>\n    * /permission/admin/manage/identity\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "Item Created"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Element Already Exists"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response addChallengeAnswerOfAUser(@ApiParam(value = "Challenge Question Set ID",required=true ) @PathParam("challenge-set-id")  String challengeSetId,
    @ApiParam(value = "Uniquely identifiable user ID",required=true ) @PathParam("user-id")  String userId,
    @ApiParam(value = "The answer to the challenge along with the question."  ) UserChallengeAnswerDTO challengeAnswer)
    {
    return delegate.addChallengeAnswerOfAUser(challengeSetId,userId,challengeAnswer);
    }
    @POST
    @Path("/challenge-answers")
    
    
    @io.swagger.annotations.ApiOperation(value = "Answer to a collection of new challenges.", notes = "Provide answer(s) to one or more candidate challenge question set(s) available in the system for the user identified by user-id. A user can pick at maximum one question from each set to answer. A user may answer **one or more distinct** challenge question **set(s)**.\n\n  <b>Permission required:</b>\n    * /permission/admin/manage/identity\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "Item Created"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Element Already Exists"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response addChallengeAnswersOfAUser(@ApiParam(value = "Uniquely identifiable user ID",required=true ) @PathParam("user-id")  String userId,
    @ApiParam(value = "One or more challenge(s) with the answer."  ) List<ChallengeAnswerDTO> challengeAnswer)
    {
    return delegate.addChallengeAnswersOfAUser(userId,challengeAnswer);
    }
    @DELETE
    @Path("/challenge-answers/{challenge-set-id}")
    
    
    @io.swagger.annotations.ApiOperation(value = "Remove a challenge question answer.", notes = "Removes existing answer provided by the user(identified by user-id) to a specific challenge.\n\n  <b>Permission required:</b>\n    * /permission/admin/manage/identity\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "No Content"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response deleteChallengeAnswerOfAUser(@ApiParam(value = "Challenge Question Set ID",required=true ) @PathParam("challenge-set-id")  String challengeSetId,
    @ApiParam(value = "Uniquely identifiable user ID",required=true ) @PathParam("user-id")  String userId)
    {
    return delegate.deleteChallengeAnswerOfAUser(challengeSetId,userId);
    }
    @DELETE
    @Path("/challenge-answers")
    
    
    @io.swagger.annotations.ApiOperation(value = "Remove challenge question answers.", notes = "Removes all the existing challenge answers of the user identified by user-id.\n\n  <b>Permission required:</b>\n    * /permission/admin/manage/identity\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "No Content"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response deleteChallengeAnswersOfAUser(@ApiParam(value = "Uniquely identifiable user ID",required=true ) @PathParam("user-id")  String userId)
    {
    return delegate.deleteChallengeAnswersOfAUser(userId);
    }
    @GET
    @Path("/challenge-answers")
    
    
    @io.swagger.annotations.ApiOperation(value = "Get user's answered challenges.", notes = "Get previously answered challenge(s) in the system by the user idenrified by user-id.\n\n  <b>Permission required:</b>\n    * /permission/admin/manage/identity\n", response = UserChallengeAnswerResponseDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Array of Challenge(s) that are already answered by the authenticated user."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response getAnsweredChallengesOfAUser(@ApiParam(value = "Uniquely identifiable user ID",required=true ) @PathParam("user-id")  String userId)
    {
    return delegate.getAnsweredChallengesOfAUser(userId);
    }
    @GET
    @Path("/challenges")
    
    
    @io.swagger.annotations.ApiOperation(value = "Retrieve challenges available for a user identified by the user-id.", notes = "Retrieves the available challenges in the system for the specified user. In the response challenge questions are grouped as **challenge set**s.\n\n<b>Permission required:</b>\n    * /permission/admin/manage/identity\n", response = ChallengeSetDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "All the available challenges in the system that can be answered by the user.\n"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response getChallengesForAUser(@ApiParam(value = "Uniquely identifiable user ID",required=true ) @PathParam("user-id")  String userId,
    @ApiParam(value = "Maximum number of records to return. _*This filtering is not yet supported._") @QueryParam("limit")  Integer limit,
    @ApiParam(value = "Number of records to skip for pagination. _*This filtering is not yet supported._") @QueryParam("offset")  Integer offset)
    {
    return delegate.getChallengesForAUser(userId,limit,offset);
    }
    @PUT
    @Path("/challenge-answers/{challenge-set-id}")
    
    
    @io.swagger.annotations.ApiOperation(value = "Update challenge answer of an already answered challenge.", notes = "Update challenge answer in a specific challenge for authenticated user.\n\n  <b>Permission required:</b>\n    * /permission/admin/manage/identity\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response updateChallengeAnswerOfAUser(@ApiParam(value = "Challenge Question Set ID",required=true ) @PathParam("challenge-set-id")  String challengeSetId,
    @ApiParam(value = "Uniquely identifiable user ID",required=true ) @PathParam("user-id")  String userId,
    @ApiParam(value = "The challenge answer with the challenge-question."  ) UserChallengeAnswerDTO challengeAnswer)
    {
    return delegate.updateChallengeAnswerOfAUser(challengeSetId,userId,challengeAnswer);
    }
    @PUT
    @Path("/challenge-answers")
    
    
    @io.swagger.annotations.ApiOperation(value = "Answer new challenge question combination over existing answers.", notes = "Overrides the *already answered challenges* in the system with a set of *new challenge question answers* for the user identified by user-id. A user can pick at maximum one question from each set to answer. A user may answer **one or more distinct** challenge question **sets**.\n\n  <b>Permission required:</b>\n    * /permission/admin/manage/identity\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response updateChallengeAnswersOfAUser(@ApiParam(value = "Uniquely identifiable user ID",required=true ) @PathParam("user-id")  String userId,
    @ApiParam(value = "Set of challenges with answer."  ) List<ChallengeAnswerDTO> challengeAnswers)
    {
    return delegate.updateChallengeAnswersOfAUser(userId,challengeAnswers);
    }
}

