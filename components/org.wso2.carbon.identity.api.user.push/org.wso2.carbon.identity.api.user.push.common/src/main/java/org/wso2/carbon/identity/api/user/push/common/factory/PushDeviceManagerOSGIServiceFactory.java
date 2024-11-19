package org.wso2.carbon.identity.api.user.push.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.notification.push.device.handler.DeviceHandlerService;

/**
 * Push device manager OSGI service factory.
 */
public class PushDeviceManagerOSGIServiceFactory extends AbstractFactoryBean<DeviceHandlerService> {

    private DeviceHandlerService deviceHandlerService;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected DeviceHandlerService createInstance() throws Exception {

        if (this.deviceHandlerService == null) {
            DeviceHandlerService deviceHandlerService = (DeviceHandlerService) PrivilegedCarbonContext
                    .getThreadLocalCarbonContext().getOSGiService(DeviceHandlerService.class, null);
            if (deviceHandlerService != null) {
                this.deviceHandlerService = deviceHandlerService;
            } else {
                throw new Exception("Unable to retrieve DeviceHandlerService.");
            }
        }
        return this.deviceHandlerService;
    }
}
