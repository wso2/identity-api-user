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

package org.wso2.carbon.identity.rest.api.user.mfa.v1;

import org.wso2.carbon.identity.rest.api.user.mfa.v1.*;
import org.wso2.carbon.identity.rest.api.user.mfa.v1.dto.*;

import org.wso2.carbon.identity.rest.api.user.mfa.v1.dto.EnabledAuthenticatorsDTO;
import org.wso2.carbon.identity.rest.api.user.mfa.v1.dto.ErrorDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import javax.ws.rs.core.Response;

/**
 * Service class for me endpoint of MFA.
 */
public abstract class MeApiService {

    /**
     * Get Enabled authenticators.
     *
     * @return API response.
     */
    public abstract Response meMfaEnabledAuthenticatorsGet();

    /**
     * Update enabled authentications.
     * @param request Enabled authenticator DTO with new enabled authenticators.
     * @return API response.
     */
    public abstract Response meMfaEnabledAuthenticatorsPost(EnabledAuthenticatorsDTO request);

}
