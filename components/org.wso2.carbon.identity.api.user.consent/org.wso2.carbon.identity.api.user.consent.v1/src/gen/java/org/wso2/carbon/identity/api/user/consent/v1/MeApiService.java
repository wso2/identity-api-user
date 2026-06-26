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

package org.wso2.carbon.identity.api.user.consent.v1;

import org.wso2.carbon.identity.api.user.consent.v1.*;
import org.wso2.carbon.identity.api.user.consent.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.user.consent.v1.model.AuthorizationRequest;
import org.wso2.carbon.identity.api.user.consent.v1.model.AuthorizationResponse;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentCreateResponse;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentInput;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentResponse;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentSummary;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentValidationResponse;
import org.wso2.carbon.identity.api.user.consent.v1.model.Error;
import javax.ws.rs.core.Response;


public interface MeApiService {

      public Response addConsentOfLoggedInUser(ConsentInput consent);

      public Response authorizeConsentOfLoggedInUser(String consentId, AuthorizationRequest authorization);

      public Response getConsentOfLoggedInUser(String consentId);

      public Response listConsentsOfLoggedInUser(String serviceId, String state, String purposeId, String purposeVersionId, String filter, Integer limit, String after, String before);

      public Response revokeConsentOfLoggedInUser(String consentId);

      public Response validateConsentOfLoggedInUser(String consentId);
}
