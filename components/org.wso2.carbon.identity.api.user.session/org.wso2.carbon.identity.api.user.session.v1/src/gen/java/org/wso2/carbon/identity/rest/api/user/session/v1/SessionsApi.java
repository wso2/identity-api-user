/*
 * Copyright (c) 2022-2024, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.rest.api.user.session.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SearchResponseDTO;
import org.wso2.carbon.identity.rest.api.user.session.v1.factories.SessionsApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/sessions")
@Api(value = "/sessions", description = "the sessions API")
public class SessionsApi {

    private final SessionsApiService delegate;

    public SessionsApi () {

        this.delegate = SessionsApiServiceFactory.getSessionsApi();
    }

    @Valid
    @GET
    @Produces({"application/json"})
    @ApiOperation(value = "Retrieve all active sessions",
            notes = "Retrieves information related to the active sessions on the system. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/authentication/session/view <br> <b>Scope required:</b> <br> * internal_session_view",
            response = SearchResponseDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved session information."),
            @ApiResponse(code = 400, message = "Invalid input request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Resource Forbidden"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    public Response getSessions(@ApiParam(value = "Condition to filter the retrieval of records.\nThe filter parameter must contain at least one valid expression (for multiple expressions they must be combined using the 'and' logical operator).\nEach expression must contain an attribute name followed by an attribute operator and a value (attribute names, operators and values used in filters are case insensitive).\n\nThe operators supported in the expression are listed next\n| Operator | Description | Behavior |\n|----------|-------------|----------|\n| eq | equal | The attribute and operator values must be identical for a match. |\n| sw | starts with | The entire operator value must be a substring of the attribute value, starting at the beginning of the attribute value. |\n| ew | ends with | The entire operator value must be a substring of the attribute value, matching at the end of the attribute value. |\n| co | contains | The entire operator value must be a substring of the attribute value for a match. |\n| le | less than or equal to | If the attribute value is less than or equal to the operator value, there is a match. |\n| ge | greater than or equal to | If the attribute value is greater than or equal to the operator value, there is a match. |\n\nThe attributes supported in the expression are listed next\n| Name | Operators | Description |\n|------|-----------|-------------|\n| loginId | eq, sw, ew, co | Filter results by the login identifier of the user who owns the session. |\n| sessionId | eq, sw, ew, co | Filter results by the ID of the session. |\n| appName | eq, sw, ew, co | Filter results by the name of the application related to the session. |\n| ipAddress | eq | Filter results by the IP address of the session. |\n| userAgent | eq, sw, ew, co | Filter results by the user agent of the session. |\n| loginTime | le, ge | Filter results by the login time of the session. |\n| lastAccessTime | le, ge | Filter results by the last access time of the session. |\n\n_Example, filter=loginId eq john and userAgent co Chrome_\n") @QueryParam("filter") String filter,
                                @ApiParam(value = "Maximum number of records to return.\n_Default value: 20_\n") @QueryParam("limit") Integer limit,
                                @ApiParam(value = "Unix timestamp data value that points to the start of the range of data to be returned.\n_Note: As results are ordered by more recent first this will provide previous page of results._\n") @QueryParam("since") Long since,
                                @ApiParam(value = "Unix timestamp data value that points to the end of the range of data to be returned.\n_Note: As results are ordered by more recent first this will provide next page of results._\n") @QueryParam("until") Long until) {

        return delegate.getSessions(filter, limit, since, until);
    }

}
