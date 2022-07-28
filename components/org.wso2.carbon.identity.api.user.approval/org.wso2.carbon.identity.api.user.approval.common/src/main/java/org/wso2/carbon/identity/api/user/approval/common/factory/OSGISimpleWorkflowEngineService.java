/*
 * Copyright (c) 2022, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.user.approval.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.workflow.engine.SimpleWorkflowEngineApprovalService;

/**
 * Factory Beans serves as a factory for creating other beans within the IOC container. This factory bean is used to
 * instantiate the ApprovalEventService type of object inside the container.
 */
public class OSGISimpleWorkflowEngineService extends AbstractFactoryBean<SimpleWorkflowEngineApprovalService> {

    private SimpleWorkflowEngineApprovalService simpleWorkflowEngineApprovalService;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected SimpleWorkflowEngineApprovalService createInstance() throws RuntimeException {

        if (this.simpleWorkflowEngineApprovalService == null) {
            SimpleWorkflowEngineApprovalService simpleWorkflowEngineApprovalService = (SimpleWorkflowEngineApprovalService) PrivilegedCarbonContext.
                    getThreadLocalCarbonContext().getOSGiService(SimpleWorkflowEngineApprovalService.class, null);
            if (simpleWorkflowEngineApprovalService != null) {
                this.simpleWorkflowEngineApprovalService = simpleWorkflowEngineApprovalService;
            } else {
                throw new RuntimeException("Unable to retrieve ApprovalEvent service.");
            }
        }
        return this.simpleWorkflowEngineApprovalService;
    }
}
