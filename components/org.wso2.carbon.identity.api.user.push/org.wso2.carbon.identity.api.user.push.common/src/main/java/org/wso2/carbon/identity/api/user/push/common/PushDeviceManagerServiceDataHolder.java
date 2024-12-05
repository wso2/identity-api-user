package org.wso2.carbon.identity.api.user.push.common;

import org.wso2.carbon.identity.notification.push.device.handler.DeviceHandlerService;

/**
 * Data holder for Push Device Manager Service.
 */
public class PushDeviceManagerServiceDataHolder {

    private static DeviceHandlerService deviceHandlerService;

    /**
     * Get Device Handler Service.
     *
     * @return DeviceHandlerService
     */
    public static DeviceHandlerService getDeviceHandlerService() {

        return PushDeviceManagerServiceDataHolder.deviceHandlerService;
    }

    /**
     * Set Device Handler Service.
     *
     * @param deviceHandlerService DeviceHandlerService
     */
    public static void setDeviceHandlerService(DeviceHandlerService deviceHandlerService) {

        PushDeviceManagerServiceDataHolder.deviceHandlerService = deviceHandlerService;
    }
}
