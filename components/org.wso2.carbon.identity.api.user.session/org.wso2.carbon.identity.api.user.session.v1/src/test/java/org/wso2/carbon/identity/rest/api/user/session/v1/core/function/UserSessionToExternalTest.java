/*
 * Copyright (c) 2022, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.user.session.v1.core.function;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.application.authentication.framework.model.Application;
import org.wso2.carbon.identity.application.authentication.framework.model.UserSession;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.SessionDTO;

import java.util.Arrays;

public class UserSessionToExternalTest {

    @Test
    public void testApply() throws Exception {

        UserSession userSession = new UserSession();
        userSession.setSessionId("ca9c30abf30ea3755009dd19d54d1d28ebb7b4d2ef0b41bd81c1a9a735404451");
        userSession.setCreationTime(1620318745450802700L);
        userSession.setUserId("00fe873d-dd7a-4157-930c-65bfcbc3f35e");
        userSession.setIp("127.0.0.1");
        userSession.setLoginTime("1620318746646");
        userSession.setLastAccessTime("1620318746646");
        userSession.setUserAgent("Apache-HttpClient/4.5.12 (Java/1.8.0)");
        userSession.setApplications(Arrays.asList(
                new Application("john@carbon.super", "User Portal", "1"),
                new Application("john", "pickup-dispatch", "2")));

        SessionDTO sessionDTO = new UserSessionToExternal().apply(userSession);

        Assert.assertEquals(sessionDTO.getId(), userSession.getSessionId());
        Assert.assertEquals(sessionDTO.getUserId(), userSession.getUserId());
        Assert.assertEquals(sessionDTO.getIp(), userSession.getIp());
        Assert.assertEquals(sessionDTO.getLoginTime(), userSession.getLoginTime());
        Assert.assertEquals(sessionDTO.getLastAccessTime(), userSession.getLastAccessTime());
        Assert.assertEquals(sessionDTO.getUserAgent(), userSession.getUserAgent());
        Assert.assertEquals(sessionDTO.getApplications().size(), userSession.getApplications().size());
    }
}
