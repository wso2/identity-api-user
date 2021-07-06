package org.wso2.carbon.identity.api.user.application.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;

/**
 * Factory Beans serves as a factory for creating other beans within the IOC container. This factory bean is used to
 * instantiate the ApplicationManagementService type of object inside the container.
 */
public class ApplicationManagementOSGIServiceFactory extends AbstractFactoryBean<ApplicationManagementService> {

    private ApplicationManagementService applicationManagementService;

    @Override
    public Class<?> getObjectType() {

        return null;
    }

    @Override
    protected ApplicationManagementService createInstance() throws Exception {

        if (this.applicationManagementService == null) {
            ApplicationManagementService applicationManagementServiceImpl =
                    (ApplicationManagementService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                            .getOSGiService(ApplicationManagementService.class, null);
            if (applicationManagementServiceImpl != null) {
                this.applicationManagementService = applicationManagementServiceImpl;
            } else {
                throw new Exception("Unable to retrieve ApplicationManagementService.");
            }
        }
        return this.applicationManagementService;
    }
}
