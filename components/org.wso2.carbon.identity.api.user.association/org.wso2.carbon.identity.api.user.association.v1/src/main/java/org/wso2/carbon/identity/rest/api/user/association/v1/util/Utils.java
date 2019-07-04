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
package org.wso2.carbon.identity.rest.api.user.association.v1.util;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.user.account.association.UserAccountAssociationService;
import org.wso2.carbon.identity.user.account.association.UserAccountConnector;

public class Utils {

    public static UserAccountConnector getUserAccountConnector() {

        return (UserAccountConnector) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(UserAccountConnector.class, null);
    }

    public static UserAccountAssociationService getUserAccountAssociationService() {

        return (UserAccountAssociationService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(UserAccountAssociationService.class, null);
    }
//
//    /**
//     * This method is used to create a BadRequestException with the known errorCode and message.
//     *
//     * @param description Error Message Desription.
//     * @param code        Error Code.
//     * @return BadRequestException with the given errorCode and description.
//     */
//    public static BadRequestException buildBadRequestException(String description, String code,
//                                                               Log log, Throwable e) {
//
//        ErrorDTO errorDTO = getErrorDTO(STATUS_BAD_REQUEST_MESSAGE_DEFAULT, description, code);
//        logDebug(log, e);
//        return new BadRequestException(errorDTO);
//    }
//
//    private static void logDebug(Log log, Throwable throwable) {
//
//        if (log.isDebugEnabled()) {
//            log.debug(STATUS_BAD_REQUEST_MESSAGE_DEFAULT, throwable);
//        }
//    }
//
//    private static ErrorDTO getErrorDTO(String message, String description, String code) {
//
//        ErrorDTO errorDTO = new ErrorDTO();
//        errorDTO.setCode(code);
//        errorDTO.setMessage(message);
//        errorDTO.setDescription(description);
//        return errorDTO;
//    }
}
