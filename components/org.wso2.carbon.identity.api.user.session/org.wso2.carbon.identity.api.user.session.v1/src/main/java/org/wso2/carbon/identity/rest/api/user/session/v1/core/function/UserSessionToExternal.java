/*
 * Copyright (c) 2022, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.rest.api.user.session.v1.core.function;

import org.wso2.carbon.identity.application.authentication.framework.model.UserSession;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.ApplicationDTO;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SessionDTO;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Transform internal user session object to external SessionDTO.
 */
public class UserSessionToExternal implements Function<UserSession, SessionDTO> {

    @Override
    public SessionDTO apply(UserSession userSession) {

        List<ApplicationDTO> appDTOs = userSession.getApplications().stream()
                .map(new ApplicationToExternal())
                .collect(Collectors.toList());

        SessionDTO session = new SessionDTO();
        session.setApplications(appDTOs);
        session.setIp(userSession.getIp());
        session.setLastAccessTime(userSession.getLastAccessTime());
        session.setLoginTime(userSession.getLoginTime());
        session.setUserAgent(userSession.getUserAgent());
        session.setUserId(userSession.getUserId());
        session.setId(userSession.getSessionId());

        return session;
    }
}
