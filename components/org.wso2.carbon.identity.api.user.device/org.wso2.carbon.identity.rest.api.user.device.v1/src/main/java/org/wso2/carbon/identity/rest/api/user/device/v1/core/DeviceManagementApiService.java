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

package org.wso2.carbon.identity.rest.api.user.device.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.user.common.ContextLoader;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.user.device.common.Constants;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.device.mgt.api.constant.ErrorMessage;
import org.wso2.carbon.identity.device.mgt.api.exception.DeviceMgtClientException;
import org.wso2.carbon.identity.device.mgt.api.exception.DeviceMgtException;
import org.wso2.carbon.identity.device.mgt.api.model.Device;
import org.wso2.carbon.identity.device.mgt.api.service.DeviceManagementService;
import org.wso2.carbon.identity.rest.api.user.device.v1.core.function.DeviceResponseBuilder;
import org.wso2.carbon.identity.rest.api.user.device.v1.model.DeviceListLink;
import org.wso2.carbon.identity.rest.api.user.device.v1.model.DeviceListResponse;
import org.wso2.carbon.identity.rest.api.user.device.v1.model.DevicePatchRequest;
import org.wso2.carbon.identity.rest.api.user.device.v1.model.DeviceResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

/**
 * Core service of the user device management API. Lets the authenticated user list, view, rename, activate,
 * deactivate and delete the devices registered to them. Every operation is scoped to the devices owned by the
 * authenticated user: a device that does not exist and a device owned by another user are reported the same way.
 */
public class DeviceManagementApiService {

    private static final Log LOG = LogFactory.getLog(DeviceManagementApiService.class);

    private final DeviceManagementService deviceManagementService;

    /**
     * Creates the service.
     *
     * @param deviceManagementService Device management OSGi service.
     */
    public DeviceManagementApiService(DeviceManagementService deviceManagementService) {

        this.deviceManagementService = deviceManagementService;
    }

    /**
     * Returns a paginated set of the devices registered to the authenticated user. Returns devices of any status.
     *
     * @param limit  Maximum number of records to return (defaults to 30 when null).
     * @param offset Number of records to skip (defaults to 0 when null).
     * @return Paginated device list response.
     */
    public DeviceListResponse listMyDevices(Integer limit, Integer offset) {

        validateApiEnabled();
        int resolvedLimit = limit != null ? limit : Constants.DEFAULT_LIMIT;
        int resolvedOffset = offset != null ? offset : Constants.DEFAULT_OFFSET;
        validatePaginationParameters(resolvedLimit, resolvedOffset);

        String userId = getAuthenticatedUserId();
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            int totalResults = deviceManagementService.getDeviceCountByUserId(userId, tenantDomain);
            List<Device> devices = deviceManagementService.getDevicesByUserId(
                    userId, tenantDomain, resolvedOffset, resolvedLimit);

            List<DeviceResponse> items = devices.stream()
                    .map(DeviceResponseBuilder::buildDeviceResponse)
                    .collect(Collectors.toList());

            return new DeviceListResponse()
                    .totalResults(totalResults)
                    .startIndex(resolvedOffset + 1)
                    .count(items.size())
                    .devices(items)
                    .links(buildPaginationLinks(resolvedLimit, resolvedOffset, totalResults));
        } catch (DeviceMgtException e) {
            throw handleException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_DEVICES);
        }
    }

    /**
     * Returns a device registered to the authenticated user.
     *
     * @param deviceId Device ID.
     * @return Device response.
     */
    public DeviceResponse getMyDevice(String deviceId) {

        validateApiEnabled();
        String userId = getAuthenticatedUserId();
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            Device device = getOwnedDevice(deviceId, userId, tenantDomain);
            return DeviceResponseBuilder.buildDeviceResponse(device);
        } catch (DeviceMgtException e) {
            throw handleException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_DEVICE);
        }
    }

    /**
     * Updates a device registered to the authenticated user. Renames the device when a device name is given and
     * activates or deactivates it when a status is given. Both can be applied in the same request.
     *
     * @param deviceId     Device ID.
     * @param patchRequest Fields to update.
     * @return Device response reflecting the updated device.
     */
    public DeviceResponse updateMyDevice(String deviceId, DevicePatchRequest patchRequest) {

        validateApiEnabled();
        validatePatchRequest(patchRequest);

        String userId = getAuthenticatedUserId();
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            Device device = getOwnedDevice(deviceId, userId, tenantDomain);

            if (patchRequest.getDeviceName() != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Renaming the device: " + deviceId + " of the authenticated user.");
                }
                deviceManagementService.updateDeviceName(deviceId, patchRequest.getDeviceName(), tenantDomain);
            }
            if (patchRequest.getStatus() != null) {
                updateDeviceStatus(device, patchRequest.getStatus(), tenantDomain);
            }

            // Re-read the device so that the response reflects every change applied by this request.
            Device updated = getOwnedDevice(deviceId, userId, tenantDomain);
            return DeviceResponseBuilder.buildDeviceResponse(updated);
        } catch (DeviceMgtException e) {
            throw handleException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_DEVICE);
        }
    }

    /**
     * Deletes a device registered to the authenticated user.
     *
     * @param deviceId Device ID.
     */
    public void deleteMyDevice(String deviceId) {

        validateApiEnabled();
        String userId = getAuthenticatedUserId();
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            // The ownership check must run before the delete, because deleting a missing device is a no-op.
            getOwnedDevice(deviceId, userId, tenantDomain);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Deleting the device: " + deviceId + " of the authenticated user.");
            }
            deviceManagementService.deleteDevice(deviceId, tenantDomain);
        } catch (DeviceMgtException e) {
            throw handleException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_DELETING_DEVICE);
        }
    }

    /**
     * Activates or deactivates the device. Nothing is changed when the device already has the requested status.
     *
     * @param device       Device owned by the authenticated user.
     * @param status       Requested status.
     * @param tenantDomain Tenant domain.
     * @throws DeviceMgtException If the status update fails.
     */
    private void updateDeviceStatus(Device device, DevicePatchRequest.StatusEnum status, String tenantDomain)
            throws DeviceMgtException {

        Device.Status requestedStatus = Device.Status.valueOf(status.value());
        if (requestedStatus == device.getStatus()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("The device: " + device.getId() + " is already " + requestedStatus
                        + ". Skipping the status update.");
            }
            return;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Changing the status of the device: " + device.getId() + " to " + requestedStatus + ".");
        }
        if (requestedStatus == Device.Status.ACTIVE) {
            deviceManagementService.activateDevice(device.getId(), tenantDomain);
        } else {
            deviceManagementService.deactivateDevice(device.getId(), tenantDomain);
        }
    }

    /**
     * Loads the device and makes sure it belongs to the given user. A device that does not exist and a device owned
     * by another user both result in the same not found error, so that the API never reveals whether a device id
     * belonging to another user exists.
     *
     * @param deviceId     Device ID.
     * @param userId       ID of the authenticated user.
     * @param tenantDomain Tenant domain.
     * @return Device owned by the user.
     * @throws DeviceMgtException If the device could not be retrieved.
     */
    private Device getOwnedDevice(String deviceId, String userId, String tenantDomain) throws DeviceMgtException {

        Device device = deviceManagementService.getDeviceById(deviceId, tenantDomain);
        if (device == null || !userId.equals(device.getUserId())) {
            throw new APIError(Response.Status.NOT_FOUND, new ErrorResponse.Builder()
                    .withCode(Constants.ErrorMessage.ERROR_CODE_DEVICE_NOT_FOUND.code())
                    .withMessage(Constants.ErrorMessage.ERROR_CODE_DEVICE_NOT_FOUND.message())
                    .withDescription(String.format(
                            Constants.ErrorMessage.ERROR_CODE_DEVICE_NOT_FOUND.description(), deviceId))
                    .build(LOG, "No device found for the authenticated user with the device id: " + deviceId));
        }
        return device;
    }

    /**
     * Resolves the ID of the authenticated user from the carbon context.
     *
     * @return ID of the authenticated user.
     */
    private String getAuthenticatedUserId() {

        String userId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUserId();
        if (StringUtils.isBlank(userId)) {
            userId = ContextLoader.getUserIdFromContext();
        }
        if (StringUtils.isBlank(userId)) {
            Constants.ErrorMessage error = Constants.ErrorMessage.ERROR_CODE_ERROR_RESOLVING_USER;
            throw new APIError(Response.Status.INTERNAL_SERVER_ERROR, new ErrorResponse.Builder()
                    .withCode(error.code())
                    .withMessage(error.message())
                    .withDescription(error.description())
                    .build(LOG, "Unable to resolve the user id of the authenticated user."));
        }
        return userId;
    }

    /**
     * Rejects the request when the device management APIs are disabled via the
     * {@value Constants#DISABLE_DEVICE_MANAGEMENT_APIS} config. The APIs stay disabled unless the config is
     * explicitly set to false.
     */
    private void validateApiEnabled() {

        String disableApis = IdentityUtil.getProperty(Constants.DISABLE_DEVICE_MANAGEMENT_APIS);
        if (StringUtils.isBlank(disableApis) || Boolean.parseBoolean(disableApis)) {
            throw buildClientError(Response.Status.NOT_FOUND, Constants.ErrorMessage.ERROR_CODE_API_DISABLED);
        }
    }

    private void validatePaginationParameters(int limit, int offset) {

        if (limit < 1 || limit > Constants.MAXIMUM_LIMIT || offset < 0) {
            throw buildClientError(Response.Status.BAD_REQUEST, Constants.ErrorMessage.ERROR_CODE_INVALID_PAGINATION);
        }
    }

    private void validatePatchRequest(DevicePatchRequest patchRequest) {

        if (patchRequest == null || (patchRequest.getDeviceName() == null && patchRequest.getStatus() == null)) {
            throw buildClientError(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_INVALID_PATCH_REQUEST);
        }
        String deviceName = patchRequest.getDeviceName();
        if (deviceName != null && (StringUtils.isBlank(deviceName)
                || deviceName.length() > Constants.DEVICE_NAME_MAX_LENGTH)) {
            throw buildClientError(Response.Status.BAD_REQUEST, Constants.ErrorMessage.ERROR_CODE_INVALID_DEVICE_NAME);
        }
    }

    /**
     * Builds 'next' and 'previous' pagination links for the device list.
     *
     * @param limit        Value of the 'limit' parameter.
     * @param offset       Value of the 'offset' parameter.
     * @param totalResults Total number of devices of the user.
     * @return List of pagination links.
     */
    private List<DeviceListLink> buildPaginationLinks(int limit, int offset, int totalResults) {

        List<DeviceListLink> links = new ArrayList<>();

        // Next link. Widened to long so that a large 'offset' cannot overflow into a false positive.
        if (((long) offset + limit) < totalResults) {
            links.add(buildPageLink(Constants.PAGE_LINK_REL_NEXT, offset + limit, limit));
        }

        // Previous link. Matters only if offset is greater than 0.
        if (offset > 0) {
            if ((offset - limit) >= 0) {
                // A previous page of size 'limit' exists.
                links.add(buildPageLink(Constants.PAGE_LINK_REL_PREVIOUS,
                        calculateOffsetForPreviousLink(offset, limit, totalResults), limit));
            } else {
                // A previous page exists, but its size is less than the specified limit.
                links.add(buildPageLink(Constants.PAGE_LINK_REL_PREVIOUS, 0, offset));
            }
        }
        return links;
    }

    private DeviceListLink buildPageLink(String rel, int offset, int limit) {

        String endpoint = Constants.V1_API_PATH_COMPONENT + Constants.ME_DEVICES_PATH_COMPONENT
                + String.format(Constants.PAGINATION_LINK_FORMAT, offset, limit);
        return new DeviceListLink().rel(rel).href(ContextLoader.buildURIForBody(endpoint).toString());
    }

    private int calculateOffsetForPreviousLink(int offset, int limit, int total) {

        int newOffset = offset - limit;
        if (newOffset < total) {
            return newOffset;
        }

        /*
        The offset points past the end of the result set, so step back by chunks of 'limit' until a page that
        overlaps the results is found. The result is floored at 0 because a negative offset is not a valid page start.
        */
        long chunks = ((long) newOffset - total) / limit + 1;
        return (int) Math.max(newOffset - chunks * limit, 0);
    }

    private APIError buildClientError(Response.Status status, Constants.ErrorMessage error) {

        return new APIError(status, new ErrorResponse.Builder()
                .withCode(error.code())
                .withMessage(error.message())
                .withDescription(error.description())
                .build(LOG, error.message()));
    }

    /**
     * Maps a device management exception to an API error. Client errors keep the backend error code and return
     * 404 for a missing device and 400 otherwise. Server errors return 500 with the operation specific error code
     * and do not expose the backend error details.
     *
     * @param e         Device management exception.
     * @param errorEnum Operation specific server error.
     * @return API error.
     */
    private APIError handleException(DeviceMgtException e, Constants.ErrorMessage errorEnum) {

        ErrorResponse errorResponse;
        Response.Status status;

        if (e instanceof DeviceMgtClientException) {
            String backendErrorCode = e.getErrorCode();
            String errorCode;
            if (backendErrorCode == null) {
                errorCode = errorEnum.code();
            } else if (backendErrorCode.startsWith(Constants.DEVICE_MGT_ERROR_PREFIX)) {
                errorCode = backendErrorCode;
            } else {
                errorCode = Constants.DEVICE_MGT_ERROR_PREFIX + backendErrorCode;
            }
            errorResponse = new ErrorResponse.Builder()
                    .withCode(errorCode)
                    .withMessage(e.getMessage())
                    .withDescription(e.getDescription())
                    .build(LOG, e.getMessage());
            status = ErrorMessage.ERROR_DEVICE_NOT_FOUND.getCode().equals(backendErrorCode)
                    ? Response.Status.NOT_FOUND
                    : Response.Status.BAD_REQUEST;
        } else {
            errorResponse = new ErrorResponse.Builder()
                    .withCode(errorEnum.code())
                    .withMessage(errorEnum.message())
                    .withDescription(errorEnum.description())
                    .build(LOG, e, errorEnum.description());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }
}
