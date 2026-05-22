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

package org.wso2.carbon.identity.rest.api.user.credential.v2.constants;

import org.wso2.carbon.identity.api.user.credential.common.CredentialManagementConstants.CredentialTypes;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Constants for the User Credential Management Endpoint v2.
 */
public class CredentialMgtEndpointConstants {

    private CredentialMgtEndpointConstants() {

    }

    /**
     * Credential types that support admin-initiated creation in v2.
     */
    public static final Set<CredentialTypes> CREATABLE_TYPES =
            Collections.unmodifiableSet(EnumSet.of(CredentialTypes.BACKUP_CODE));

    /**
     * Credential types that support bulk deletion by type in v2.
     */
    public static final Set<CredentialTypes> DELETABLE_BY_TYPE_TYPES =
            Collections.unmodifiableSet(EnumSet.of(CredentialTypes.BACKUP_CODE));
}
