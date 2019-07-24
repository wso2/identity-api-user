/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import org.wso2.carbon.identity.application.authentication.framework.model.Application;
import org.wso2.carbon.identity.rest.api.user.session.v1.dto.ApplicationDTO;

import java.util.function.Function;

public class ApplicationToExternal implements Function<Application, ApplicationDTO> {

    @Override
    public ApplicationDTO apply(Application application) {

        ApplicationDTO applicationDTO = new ApplicationDTO();
        applicationDTO.setAppId(application.getAppId());
        applicationDTO.setSubject(application.getSubject());
        applicationDTO.setAppName(application.getAppName());

        return applicationDTO;
    }

}
