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

package org.wso2.carbon.identity.api.user.consent.v1.impl.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.consent.mgt.core.PrivilegedConsentManager;
import org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages;
import org.wso2.carbon.consent.mgt.core.exception.ConsentManagementClientException;
import org.wso2.carbon.consent.mgt.core.exception.ConsentManagementException;
import org.wso2.carbon.consent.mgt.core.model.AddReceiptResponse;
import org.wso2.carbon.consent.mgt.core.model.ConsentAuthorization;
import org.wso2.carbon.consent.mgt.core.model.ConsentPurpose;
import org.wso2.carbon.consent.mgt.core.model.PIICategory;
import org.wso2.carbon.consent.mgt.core.model.PIICategoryValidity;
import org.wso2.carbon.consent.mgt.core.model.Purpose;
import org.wso2.carbon.consent.mgt.core.model.PurposePIICategoryBinding;
import org.wso2.carbon.consent.mgt.core.model.PurposeVersion;
import org.wso2.carbon.consent.mgt.core.model.Receipt;
import org.wso2.carbon.consent.mgt.core.model.ReceiptInput;
import org.wso2.carbon.consent.mgt.core.model.ReceiptService;
import org.wso2.carbon.consent.mgt.core.util.ConsentReceiptUtils;
import org.wso2.carbon.consent.mgt.core.util.FilterQueriesUtil;
import org.wso2.carbon.identity.api.user.common.ContextLoader;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.user.consent.v1.model.AuthorizationRequest;
import org.wso2.carbon.identity.api.user.consent.v1.model.AuthorizationResponse;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentCreateResponse;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentInput;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentPurposeInput;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentPurposeResponse;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentResponse;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentSummary;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentValidationResponse;
import org.wso2.carbon.identity.api.user.consent.v1.model.ConsentedElement;
import org.wso2.carbon.identity.api.user.consent.v1.model.ElementRef;
import org.wso2.carbon.identity.core.model.ExpressionNode;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ACTIVE_STATE;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_INVALID_FILTER_EXPRESSION;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.ErrorMessages.ERROR_CODE_INVALID_QUERY_PARAM;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.FilterConstants;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.PENDING_STATE;
import static org.wso2.carbon.consent.mgt.core.constant.ConsentConstants.REVOKE_STATE;
import static org.wso2.carbon.consent.mgt.core.util.ConsentUtils.handleClientException;

/**
 * Core service for user self-service consent management operations.
 * Always scopes operations to the currently authenticated user.
 */
public class UserConsentService {

    private static final Log LOG = LogFactory.getLog(UserConsentService.class);

    private static final Set<String> NOT_FOUND_ERROR_CODES = new HashSet<>(Arrays.asList(
            ErrorMessages.ERROR_CODE_RECEIPT_ID_INVALID.getCode(),
            ErrorMessages.ERROR_CODE_PURPOSE_ID_INVALID.getCode(),
            ErrorMessages.ERROR_CODE_PURPOSE_CATEGORY_ID_INVALID.getCode(),
            ErrorMessages.ERROR_CODE_PII_CATEGORY_ID_INVALID.getCode(),
            ErrorMessages.ERROR_CODE_PURPOSE_UUID_NOT_FOUND.getCode(),
            ErrorMessages.ERROR_CODE_ELEMENT_UUID_NOT_FOUND.getCode(),
            ErrorMessages.ERROR_CODE_PURPOSE_VERSION_ID_INVALID.getCode(),
            ErrorMessages.ERROR_CODE_PURPOSE_VERSION_NOT_FOUND.getCode()
    ));

    private static final Set<String> CONFLICT_ERROR_CODES = new HashSet<>(Arrays.asList(
            ErrorMessages.ERROR_CODE_PURPOSE_ALREADY_EXIST.getCode(),
            ErrorMessages.ERROR_CODE_PURPOSE_CATEGORY_ALREADY_EXIST.getCode(),
            ErrorMessages.ERROR_CODE_PII_CATEGORY_ALREADY_EXIST.getCode(),
            ErrorMessages.ERROR_CODE_PURPOSE_VERSION_ALREADY_EXISTS.getCode(),
            ErrorMessages.ERROR_CODE_CONSENT_INVALID_STATE_FOR_AUTHORIZE.getCode()
    ));

    private static final Set<String> FORBIDDEN_ERROR_CODES = new HashSet<>(Arrays.asList(
            ErrorMessages.ERROR_CODE_USER_NOT_AUTHORIZED.getCode()
    ));

    private final PrivilegedConsentManager consentManager;

    public UserConsentService(PrivilegedConsentManager consentManager) {

        this.consentManager = consentManager;
    }

    public ConsentCreateResponse addConsent(ConsentInput consentInput) {

        String subjectId = ContextLoader.getUsernameFromContext();
        String tenantDomain = ContextLoader.getTenantDomainFromContext();

        boolean rejected = ConsentInput.StateEnum.REJECTED.equals(consentInput.getState());
        String language = consentInput.getLanguage();
        Timestamp expiryTimestamp = consentInput.getExpiryTime() != null
                ? new Timestamp(consentInput.getExpiryTime()) : null;

        try {
            List<PurposePIICategoryBinding> purposeBindings = buildPurposeBindings(consentInput);
            Map<String, String> properties = consentInput.getProperties();

            ReceiptInput receiptInput = ConsentReceiptUtils.buildReceiptInput(
                    language, subjectId, tenantDomain, expiryTimestamp, rejected, null,
                    properties, consentInput.getServiceId(), purposeBindings, consentManager);

            AddReceiptResponse addReceiptResponse = consentManager.addConsent(receiptInput);

            ConsentInput.StateEnum requestedState = consentInput.getState() != null
                    ? consentInput.getState() : ConsentInput.StateEnum.ACTIVE;
            ConsentCreateResponse response = new ConsentCreateResponse();
            response.setId(addReceiptResponse.getConsentReceiptId());
            response.setSubjectId(subjectId);
            response.setServiceId(consentInput.getServiceId());
            response.setState(ConsentCreateResponse.StateEnum.fromValue(requestedState.value()));
            return response;
        } catch (ConsentManagementException e) {
            throw handleException(e);
        }
    }

    public List<ConsentSummary> listConsents(String serviceId, String state, String purposeId,
                                             String purposeVersionId, String filter,
                                             Integer limit, String after, String before) {

        String subjectId = ContextLoader.getUsernameFromContext();

        try {
            int resolvedLimit = limit != null ? limit : 10;
            if (resolvedLimit <= 0) {
                throw handleClientException(ERROR_CODE_INVALID_QUERY_PARAM, String.valueOf(resolvedLimit));
            }

            if (StringUtils.isNotBlank(before) && StringUtils.isNotBlank(after)) {
                throw handleClientException(ERROR_CODE_INVALID_QUERY_PARAM,
                        "Both 'before' and 'after' parameters are provided.");
            }

            List<ExpressionNode> expressionNodes = FilterQueriesUtil.getExpressionNodes(filter, after, before);
            for (ExpressionNode node : expressionNodes) {
                String attr = node.getAttributeValue();
                if (FilterConstants.FILTER_ATTR_AFTER.equals(attr) ||
                        FilterConstants.FILTER_ATTR_BEFORE.equals(attr)) {
                    continue;
                }
                if (attr == null || !attr.startsWith("properties.") || attr.length() == "properties.".length()) {
                    throw handleClientException(ERROR_CODE_INVALID_FILTER_EXPRESSION,
                            "Only 'properties.<key>' attributes are supported in consent filter. Got: " + attr);
                }
                if (!"eq".equals(node.getOperation())) {
                    throw handleClientException(ERROR_CODE_INVALID_FILTER_EXPRESSION,
                            "Only 'eq' operation is supported for consent property filter.");
                }
            }

            List<Receipt> receipts = consentManager.listReceipts(
                    subjectId, serviceId, state, purposeId, purposeVersionId, expressionNodes, resolvedLimit + 1);

            if (receipts != null && receipts.size() > resolvedLimit) {
                receipts = new ArrayList<>(receipts.subList(0, resolvedLimit));
            }

            List<ConsentSummary> summaries = new ArrayList<>();
            if (receipts != null) {
                for (Receipt receipt : receipts) {
                    summaries.add(toConsentSummary(receipt));
                }
            }
            return summaries;
        } catch (ConsentManagementException e) {
            throw handleException(e);
        }
    }

    public ConsentResponse getConsent(String consentId) {

        String subjectId = ContextLoader.getUsernameFromContext();

        try {
            Receipt receipt = consentManager.getReceiptWithExtendedSchema(consentId);
            validateOwnership(receipt, consentId, subjectId);
            return toConsentResponse(receipt);
        } catch (ConsentManagementException e) {
            throw handleException(e);
        }
    }

    public void revokeConsent(String consentId) {

        String subjectId = ContextLoader.getUsernameFromContext();

        try {
            Receipt receipt = consentManager.getReceiptWithExtendedSchema(consentId);
            validateOwnership(receipt, consentId, subjectId);
            consentManager.authorizeConsent(consentId, subjectId, REVOKE_STATE);
        } catch (ConsentManagementException e) {
            throw handleException(e);
        }
    }

    public AuthorizationResponse authorizeConsent(String consentId, AuthorizationRequest authorizationRequest) {

        String subjectId = ContextLoader.getUsernameFromContext();

        try {
            AuthorizationRequest.StateEnum requestState = authorizationRequest.getState();
            String authStatus = requestState != null ? requestState.value() :
                    AuthorizationRequest.StateEnum.APPROVED.value();

            Receipt receipt = consentManager.getReceiptWithExtendedSchema(consentId);
            List<ConsentAuthorization> authorizations = consentManager.getConsentAuthorizations(consentId);
            validateAuthorizer(receipt, consentId, subjectId, authorizations);
            consentManager.authorizeConsent(consentId, subjectId, authStatus);

            ConsentAuthorization userAuth = findUserAuthorization(
                    consentManager.getConsentAuthorizations(consentId), subjectId);

            AuthorizationResponse response = new AuthorizationResponse();
            response.setUserId(subjectId);
            if (userAuth != null) {
                response.setState(AuthorizationResponse.StateEnum.fromValue(userAuth.getStatus().name()));
                response.setUpdatedTime(userAuth.getUpdatedTime());
            } else {
                response.setState(AuthorizationResponse.StateEnum.fromValue(authStatus));
                response.setUpdatedTime(System.currentTimeMillis());
            }
            return response;
        } catch (ConsentManagementException e) {
            throw handleException(e);
        }
    }

    public ConsentValidationResponse validateConsent(String consentId) {

        String subjectId = ContextLoader.getUsernameFromContext();

        try {
            Receipt receipt = consentManager.getReceiptWithExtendedSchema(consentId);
            validateOwnership(receipt, consentId, subjectId);

            String status = consentManager.validateConsentStatus(consentId);
            ConsentValidationResponse response = new ConsentValidationResponse();
            response.setState(ConsentValidationResponse.StateEnum.fromValue(status));
            response.setExpiryTime(receipt.getExpiryTime() != null ? receipt.getExpiryTime().getTime() : null);
            return response;
        } catch (ConsentManagementException e) {
            throw handleException(e);
        }
    }

    private List<PurposePIICategoryBinding> buildPurposeBindings(ConsentInput consentInput)
            throws ConsentManagementException {

        List<PurposePIICategoryBinding> purposeBindings = new ArrayList<>();
        if (consentInput.getPurposes() == null) {
            return purposeBindings;
        }
        for (ConsentPurposeInput purposeInput : consentInput.getPurposes()) {
            List<PIICategory> piiCategories = new ArrayList<>();
            if (purposeInput.getElements() != null) {
                for (ElementRef elementRef : purposeInput.getElements()) {
                    PIICategory piiCategory = consentManager.getPIICategoryByUuid(elementRef.getId());
                    if (piiCategory == null) {
                        throw new ConsentManagementClientException(
                                "Element not found: " + elementRef.getId(),
                                ErrorMessages.ERROR_CODE_ELEMENT_UUID_NOT_FOUND.getCode());
                    }
                    piiCategories.add(piiCategory);
                }
            }
            purposeBindings.add(new PurposePIICategoryBinding(purposeInput.getId(), piiCategories));
        }
        return purposeBindings;
    }

    private void validateOwnership(Receipt receipt, String consentId, String subjectId) {

        if (receipt == null) {
            throw consentNotFoundError(consentId);
        }
        if (!subjectId.equals(receipt.getPiiPrincipalId())) {
            throw userNotAuthorizedError();
        }
    }

    private void validateAuthorizer(Receipt receipt, String consentId, String subjectId,
                                    List<ConsentAuthorization> authorizations) {

        if (receipt == null) {
            throw consentNotFoundError(consentId);
        }
        if (subjectId.equals(receipt.getPiiPrincipalId())) {
            return;
        }
        if (findUserAuthorization(authorizations, subjectId) != null) {
            return;
        }
        throw userNotAuthorizedError();
    }

    private ConsentAuthorization findUserAuthorization(List<ConsentAuthorization> authorizations, String subjectId) {

        if (authorizations != null) {
            for (ConsentAuthorization auth : authorizations) {
                if (subjectId.equals(auth.getUserId())) {
                    return auth;
                }
            }
        }
        return null;
    }

    private APIError consentNotFoundError(String consentId) {

        return new APIError(Response.Status.NOT_FOUND, new ErrorResponse.Builder()
                .withCode(ErrorMessages.ERROR_CODE_RECEIPT_ID_INVALID.getCode())
                .withMessage("Consent not found.")
                .withDescription("No consent found for ID: " + consentId)
                .build());
    }

    private APIError userNotAuthorizedError() {

        return new APIError(Response.Status.FORBIDDEN, new ErrorResponse.Builder()
                .withCode(ErrorMessages.ERROR_CODE_USER_NOT_AUTHORIZED.getCode())
                .withMessage(ErrorMessages.ERROR_CODE_USER_NOT_AUTHORIZED.getMessage())
                .build());
    }

    private ConsentResponse toConsentResponse(Receipt receipt) {

        ConsentResponse dto = new ConsentResponse();
        dto.setId(receipt.getConsentReceiptId());
        dto.setSubjectId(receipt.getPiiPrincipalId());
        dto.setLanguage(receipt.getLanguage());
        String state = StringUtils.isNotBlank(receipt.getState()) ? receipt.getState() : ACTIVE_STATE;
        dto.setState(ConsentResponse.StateEnum.fromValue(state));
        dto.setTimestamp(receipt.getConsentTimestamp());
        dto.setExpiryTime(receipt.getExpiryTime() != null ? receipt.getExpiryTime().getTime() : null);
        dto.setProperties(receipt.getProperties());

        List<ConsentPurposeResponse> purposeResponses = new ArrayList<>();
        List<ReceiptService> services = receipt.getServices();
        if (services != null && !services.isEmpty()) {
            ReceiptService receiptService = services.get(0);
            dto.setServiceId(receiptService.getService());
            if (receiptService.getPurposes() != null) {
                for (ConsentPurpose consentPurpose : receiptService.getPurposes()) {
                    purposeResponses.add(toConsentPurposeResponse(consentPurpose));
                }
            }
        }
        dto.setPurposes(purposeResponses);

        List<AuthorizationResponse> authResponses = new ArrayList<>();
        try {
            List<ConsentAuthorization> auths = consentManager.getConsentAuthorizations(receipt.getConsentReceiptId());
            if (auths != null) {
                for (ConsentAuthorization auth : auths) {
                    if (PENDING_STATE.equals(auth.getStatus().name())) {
                        continue;
                    }
                    try {
                        AuthorizationResponse authResponse = new AuthorizationResponse();
                        authResponse.setUserId(auth.getUserId());
                        authResponse.setState(AuthorizationResponse.StateEnum.fromValue(auth.getStatus().name()));
                        authResponse.setUpdatedTime(auth.getUpdatedTime());
                        authResponses.add(authResponse);
                    } catch (IllegalArgumentException e) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Skipping unrecognized authorization state: " + auth.getStatus(), e);
                        }
                    }
                }
            }
        } catch (ConsentManagementException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Could not retrieve authorizations for consent: " + receipt.getConsentReceiptId(), e);
            }
        }
        dto.setAuthorizations(authResponses);
        return dto;
    }

    private ConsentPurposeResponse toConsentPurposeResponse(ConsentPurpose consentPurpose) {

        ConsentPurposeResponse dto = new ConsentPurposeResponse();
        dto.setName(consentPurpose.getPurpose());

        String versionUuid = consentPurpose.getPurposeVersionId();
        try {
            Purpose purpose = consentManager.getPurposeByUuid(consentPurpose.getUuid());
            if (purpose != null) {
                if (StringUtils.isNotBlank(purpose.getUuid())) {
                    dto.setId(purpose.getUuid());
                }
                if (StringUtils.isNotBlank(purpose.getGroupType())) {
                    dto.setType(purpose.getGroupType());
                }
                if (StringUtils.isNotBlank(versionUuid)) {
                    dto.setVersionId(versionUuid);
                    PurposeVersion latestVersion = purpose.getLatestVersion();
                    if (latestVersion != null && versionUuid.equals(latestVersion.getUuid())) {
                        dto.setVersion(latestVersion.getVersion());
                        dto.setProperties(latestVersion.getProperties());
                    } else if (dto.getId() != null) {
                        PurposeVersion pv = consentManager.getPurposeVersion(dto.getId(), versionUuid);
                        if (pv != null) {
                            dto.setVersion(pv.getVersion());
                            dto.setProperties(pv.getProperties());
                        }
                    }
                }
            }
        } catch (ConsentManagementException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Could not resolve purpose UUID: " + consentPurpose.getUuid(), e);
            }
        }

        List<ConsentedElement> elements = new ArrayList<>();
        if (consentPurpose.getPiiCategory() != null) {
            for (PIICategoryValidity piiCategoryValidity : consentPurpose.getPiiCategory()) {
                ConsentedElement element = new ConsentedElement();
                element.setName(piiCategoryValidity.getName());
                try {
                    PIICategory piiCategory = consentManager.getPIICategoryByUuid(piiCategoryValidity.getUuid());
                    if (piiCategory != null) {
                        element.setId(piiCategory.getUuid());
                    }
                } catch (ConsentManagementException e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Could not resolve element UUID: " + piiCategoryValidity.getUuid(), e);
                    }
                }
                elements.add(element);
            }
        }
        dto.setElements(elements);
        return dto;
    }

    private ConsentSummary toConsentSummary(Receipt receipt) {

        ConsentSummary summary = new ConsentSummary();
        summary.setId(receipt.getConsentReceiptId());
        summary.setTimestamp(receipt.getConsentTimestamp());
        String state = StringUtils.isNotBlank(receipt.getState()) ? receipt.getState() : ACTIVE_STATE;
        summary.setState(ConsentSummary.StateEnum.fromValue(state));
        if (receipt.getServices() != null && !receipt.getServices().isEmpty()) {
            summary.setServiceId(receipt.getServices().get(0).getService());
        }
        return summary;
    }

    private APIError handleException(ConsentManagementException e) {

        if (e instanceof ConsentManagementClientException) {
            String errorCode = e.getErrorCode();
            Response.Status status = Response.Status.BAD_REQUEST;
            if (errorCode != null && NOT_FOUND_ERROR_CODES.contains(errorCode)) {
                status = Response.Status.NOT_FOUND;
            } else if (errorCode != null && FORBIDDEN_ERROR_CODES.contains(errorCode)) {
                status = Response.Status.FORBIDDEN;
            } else if (errorCode != null && CONFLICT_ERROR_CODES.contains(errorCode)) {
                status = Response.Status.CONFLICT;
            }
            return new APIError(status, new ErrorResponse.Builder()
                    .withCode(errorCode)
                    .withMessage(e.getMessage())
                    .withDescription(e.getMessage())
                    .build());
        }
        LOG.error("Unexpected error in consent management.", e);
        return new APIError(Response.Status.INTERNAL_SERVER_ERROR, new ErrorResponse.Builder()
                .withCode(e.getErrorCode())
                .withMessage("Server error occurred during consent management.")
                .build());
    }
}
