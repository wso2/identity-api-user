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

package org.wso2.carbon.identity.api.user.common.function;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.application.common.model.User;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.function.Function;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessage.ERROR_CODE_INVALID_USERNAME;
import static org.wso2.carbon.identity.api.user.common.ContextLoader.getUser;

/**
 * Build user object from user id and tenant domain
 */
public class UserIdToUser implements Function<String[], User> {

    private static final Log log = LogFactory.getLog(UserIdToUser.class);

    @Override
    public User apply(String... args) {
        String userId = null;
        String tenantDomain;
        try {
            userId = args[0];
            tenantDomain = args[1];
            String decodedUsername = new String(Base64.getDecoder().decode(userId), StandardCharsets.UTF_8);

            if (StringUtils.isBlank(userId)) {
                throw new WebApplicationException("UserID is empty.");
            }

            return getUser(tenantDomain, decodedUsername);
        } catch (Exception e) {
            throw new APIError(Response.Status.BAD_REQUEST, new ErrorResponse.Builder()
                    .withCode(ERROR_CODE_INVALID_USERNAME.getCode())
                    .withMessage(ERROR_CODE_INVALID_USERNAME.getMessage())
                    .withDescription(ERROR_CODE_INVALID_USERNAME.getDescription())
                    .build(log, e, "Invalid userId: " + userId));
        }
    }
}
