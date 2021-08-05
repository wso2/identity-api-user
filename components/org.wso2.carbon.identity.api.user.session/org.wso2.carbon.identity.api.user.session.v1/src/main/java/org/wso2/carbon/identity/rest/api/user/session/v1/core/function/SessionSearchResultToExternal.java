/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import org.wso2.carbon.identity.application.authentication.framework.model.SessionSearchResult;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.ApplicationDTO;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SessionDTO;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Transform internal session search result object to external SessionDTO.
 */
public class SessionSearchResultToExternal implements Function<SessionSearchResult, SessionDTO> {

    @Override
    public SessionDTO apply(SessionSearchResult result) {

        List<ApplicationDTO> appDTOs = result.getApplications().stream().map(new ApplicationToExternal())
                .collect(Collectors.toList());

        SessionDTO session = new SessionDTO();
        session.setApplications(appDTOs);
        session.setIp(result.getIp());
        session.setLastAccessTime(result.getLastAccessTime());
        session.setLoginTime(result.getLoginTime());
        session.setUserAgent(result.getUserAgent());
        session.setUserId(result.getUserId());
        session.setId(result.getSessionId());

        return session;
    }
}
