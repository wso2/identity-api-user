package org.wso2.carbon.identity.api.user.approval.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.humantask.core.TaskOperationService;

public class OSGIBpelService extends AbstractFactoryBean<TaskOperationService> {

    private TaskOperationService taskOperationService;

    @Override
    public Class<?> getObjectType() {
        return Object.class;
    }

    @Override
    protected TaskOperationService createInstance() throws Exception {

        if (this.taskOperationService == null) {
            TaskOperationService taskOperationService = (TaskOperationService) PrivilegedCarbonContext.
                    getThreadLocalCarbonContext().getOSGiService(TaskOperationService.class, null);
            if (taskOperationService != null) {
                this.taskOperationService = taskOperationService;
            } else {
                throw new Exception("Unable to retrieve TaskOperationService service.");
            }
        }
        return this.taskOperationService;
    }
}
