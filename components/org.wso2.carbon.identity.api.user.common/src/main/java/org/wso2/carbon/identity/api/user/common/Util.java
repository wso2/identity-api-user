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

package org.wso2.carbon.identity.api.user.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.application.authentication.framework.exception.UserSessionException;
import org.wso2.carbon.identity.application.authentication.framework.store.UserSessionStore;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.recovery.ChallengeQuestionManager;

import java.util.UUID;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.common.Constants.CORRELATION_ID_MDC;
import static org.wso2.carbon.identity.api.user.common.Constants.ErrorMessage.ERROR_CODE_INVALID_USERNAME;

/**
 * Common util class
 */
public class Util {

    private static final Log log = LogFactory.getLog(Util.class);

    /**
     * Get ChallengeQuestionManager osgi service
     * @return ChallengeQuestionManager
     */
    public static ChallengeQuestionManager getChallengeQuestionManager() {
        return (ChallengeQuestionManager) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(ChallengeQuestionManager.class, null);
    }

    /**
     * Get correlation id of current thread
     * @return correlation-id
     */
    public static String getCorrelation() {
        String ref;
        if (isCorrelationIDPresent()) {
            ref = MDC.get(CORRELATION_ID_MDC).toString();
        } else {
            ref = UUID.randomUUID().toString();

        }
        return ref;
    }

    /**
     * Check whether correlation id present in the log MDC
     * @return
     */
    public static boolean isCorrelationIDPresent() {
        return MDC.get(CORRELATION_ID_MDC) != null;
    }

    /**
     * Resolve the user id of the given user object.
     *
     * @param user user object
     * @return user-id
     */
    public static String resolveUserIdFromUser(User user) {
        int tenantId = (user.getTenantDomain() == null) ? org.wso2.carbon.utils.multitenancy.MultitenantConstants
                .INVALID_TENANT_ID : IdentityTenantUtil.getTenantId(user.getTenantDomain());
        try {
            return UserSessionStore.getInstance().getUserId(user.getUserName(), tenantId, user
                    .getUserStoreDomain(), -1);
        } catch (UserSessionException e) {
            throw new APIError(Response.Status.BAD_REQUEST, new ErrorResponse.Builder()
                    .withCode(ERROR_CODE_INVALID_USERNAME.getCode())
                    .withMessage(ERROR_CODE_INVALID_USERNAME.getMessage())
                    .withDescription(ERROR_CODE_INVALID_USERNAME.getDescription())
                    .build(log, e, "Invalid userId: " + user.getUserName()));
        }
    }
}
