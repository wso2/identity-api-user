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

package org.wso2.carbon.identity.rest.api.user.application.v1.core.function;

import org.wso2.carbon.identity.application.common.model.ApplicationBasicInfo;
import org.wso2.carbon.identity.rest.api.user.application.v1.model.ApplicationResponse;

import java.net.URI;
import java.util.Optional;
import java.util.function.Function;

/**
 * Converts the internal {@link ApplicationBasicInfo} model to corresponding API model {@link ApplicationResponse}
 */
public class ApplicationBasicInfoToApiModel implements Function<ApplicationBasicInfo, ApplicationResponse> {

    @Override
    public ApplicationResponse apply(ApplicationBasicInfo applicationBasicInfo) {

        return new ApplicationResponse()
                .id(String.valueOf(applicationBasicInfo.getApplicationResourceId()))
                .name(applicationBasicInfo.getApplicationName())
                .description(applicationBasicInfo.getDescription())
                .accessUrl(Optional.ofNullable(applicationBasicInfo.getAccessUrl()).isPresent() ? URI.create
                        (applicationBasicInfo.getAccessUrl()) : null)
                .image(Optional.ofNullable(applicationBasicInfo.getImageUrl()).isPresent() ? URI.create
                        (applicationBasicInfo.getImageUrl()) : null);
    }
}
