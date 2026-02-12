/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.user.password.v1.impl;

import org.wso2.carbon.identity.api.user.password.v1.MeApiService;
import org.wso2.carbon.identity.api.user.password.v1.core.PasswordService;
import org.wso2.carbon.identity.api.user.password.v1.factories.PasswordServiceFactory;
import org.wso2.carbon.identity.api.user.password.v1.model.PasswordUpdateRequest;
import org.wso2.carbon.identity.api.user.password.v1.model.PasswordUpdateResponse;

import javax.ws.rs.core.Response;

/**
 * Implementation of the Me API Service for password management.
 */
public class MeApiServiceImpl implements MeApiService {

    private final PasswordService passwordService;

    public MeApiServiceImpl() {
        this.passwordService = PasswordServiceFactory.getPasswordService();
    }

    @Override
    public Response updatePassword(PasswordUpdateRequest passwordUpdateRequest) {

        PasswordUpdateResponse response = passwordService.updatePassword(passwordUpdateRequest);
        return Response.ok().entity(response).build();
    }
}
