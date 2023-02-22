/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.user.session.v1;

import javax.ws.rs.core.Response;

public abstract class SessionsApiService {

    public abstract Response getSessions(String filter, Integer limit, Long since, Long until);

    /**
     * Terminates sessions selected based on filter.
     *
     * @param filter    the filter based on which the sessions to be terminated are selected (Mandatory)
     * @param limit maximum number of sessions to be selected (Optional)
     * @param since timestamp data value that points to the start of the range of data to be returned (Optional)
     * @param until timestamp data value that points to the end of the range of data to be returned (Optional)
     * @return Response
     */
    public abstract Response terminateFilteredSessions(String filter, Integer limit, Long since, Long until);
}
