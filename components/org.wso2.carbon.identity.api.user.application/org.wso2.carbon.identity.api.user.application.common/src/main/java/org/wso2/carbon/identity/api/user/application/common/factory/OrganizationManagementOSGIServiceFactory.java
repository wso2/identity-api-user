package org.wso2.carbon.identity.api.user.application.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.organization.management.application.OrgApplicationManager;

/**
 * Factory Beans serves as a factory for creating other beans within the IOC container. This factory bean is used to
 * instantiate the OrganizationManagementOSGIServiceFactory class and used to get OrganizationManagementOSGIService OSGI
 * service.
 */
public class OrganizationManagementOSGIServiceFactory extends AbstractFactoryBean<OrgApplicationManager> {

    private OrgApplicationManager orgApplicationManager;

    @Override
    public Class<?> getObjectType() {

        return null;
    }

    @Override
    protected OrgApplicationManager createInstance() throws Exception {

        if (this.orgApplicationManager == null) {
            OrgApplicationManager applicationManagementService = (OrgApplicationManager)
                    PrivilegedCarbonContext.getThreadLocalCarbonContext()
                            .getOSGiService(OrgApplicationManager.class, null);
            if (applicationManagementService != null) {
                this.orgApplicationManager = applicationManagementService;
            } else {
                throw new Exception("Unable to retrieve OrgApplicationManager service.");
            }
        }
        return this.orgApplicationManager;
    }
}
