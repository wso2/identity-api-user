/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.association.v1.model;

import org.wso2.carbon.identity.user.profile.mgt.association.federation.exception.FederatedAssociationManagerClientException;

import static org.wso2.carbon.identity.rest.api.user.association.v1.AssociationEndpointConstants.FEDERATED_USER_ASSOCIATIONS_COMPONENT;
import static org.wso2.carbon.identity.rest.api.user.association.v1.AssociationEndpointConstants.HTTP_DELETE;
import static org.wso2.carbon.identity.rest.api.user.association.v1.AssociationEndpointConstants.HTTP_POST;
import static org.wso2.carbon.identity.user.profile.mgt.association.federation.constant.FederatedAssociationConstants.ErrorMessages.INVALID_BULK_OPERATION_PATH;

/**
 * Represents a bulk operation path object for user associations.
 * This class is used to parse and hold information about bulk operations
 * related to user associations.
 */
public class BulkAssociationPathObject {

    private String userId;
    private String associationId;
    private AssociationTypes associationType;

    private Operations operation;
    private static final int USER_FEDERATED_ASSOCIATIONS_PARTS = 2;
    private static final int USER_FEDERATED_ASSOCIATION_PARTS = 3;

    /**
     * Enum representing the types of operations that can be performed on user associations.
     */
    public enum Operations {
        ADD_FEDERATED_ASSOCIATION,
        REMOVE_FEDERATED_ASSOCIATION,
        REMOVE_ALL_FEDERATED_ASSOCIATIONS
    }

    /**
     * Enum representing the types of associations.
     * LOCAL refers to local user associations, while FEDERATED refers to federated user associations.
     */
    public enum AssociationTypes {
        LOCAL,
        FEDERATED
    }

    /**
     * Parses the bulk operation method and path to create a BulkAssociationPathObject.
     *
     * @param bulkOperationMethod The HTTP method of the bulk operation (e.g., POST, DELETE).
     * @param bulkOperationPath   The path of the bulk operation.
     * @return A BulkAssociationPathObject representing the parsed operation.
     */
    public static BulkAssociationPathObject parseBulkAssociationPathObject(String bulkOperationMethod,
                                                                           String bulkOperationPath)
            throws FederatedAssociationManagerClientException {

        BulkAssociationPathObject bulkAssociationPathObject = new BulkAssociationPathObject();
        String sanitizedPath = bulkOperationPath.replaceAll("^/+", "").replaceAll("/+$", "");
        String[] pathParts = sanitizedPath.split("/");

        if (HTTP_POST.equals(bulkOperationMethod) && pathParts.length == USER_FEDERATED_ASSOCIATIONS_PARTS &&
                FEDERATED_USER_ASSOCIATIONS_COMPONENT.equals(pathParts[1])) {

            bulkAssociationPathObject.setOperation(BulkAssociationPathObject.Operations.ADD_FEDERATED_ASSOCIATION);
            bulkAssociationPathObject.setAssociationType(AssociationTypes.FEDERATED);
            bulkAssociationPathObject.setUserId(pathParts[0]);
            return bulkAssociationPathObject;
        } else if (HTTP_DELETE.equals(bulkOperationMethod)) {
            if (pathParts.length == USER_FEDERATED_ASSOCIATION_PARTS &&
                    FEDERATED_USER_ASSOCIATIONS_COMPONENT.equals(pathParts[1])) {
                bulkAssociationPathObject.setOperation(
                        BulkAssociationPathObject.Operations.REMOVE_FEDERATED_ASSOCIATION);
                bulkAssociationPathObject.setAssociationType(AssociationTypes.FEDERATED);
                bulkAssociationPathObject.setUserId(pathParts[0]);
                bulkAssociationPathObject.setAssociationId(pathParts[2]);
                return bulkAssociationPathObject;
            } else if (pathParts.length == USER_FEDERATED_ASSOCIATIONS_PARTS &&
                    FEDERATED_USER_ASSOCIATIONS_COMPONENT.equals(pathParts[1])) {
                bulkAssociationPathObject.setOperation(
                        BulkAssociationPathObject.Operations.REMOVE_ALL_FEDERATED_ASSOCIATIONS);
                bulkAssociationPathObject.setAssociationType(AssociationTypes.FEDERATED);
                bulkAssociationPathObject.setUserId(pathParts[0]);
                return bulkAssociationPathObject;
            }
        }
            throw new FederatedAssociationManagerClientException(String.valueOf(INVALID_BULK_OPERATION_PATH.getCode()),
                    INVALID_BULK_OPERATION_PATH.getDescription());
    }

    /**
     * Returns the user ID.
     *
     * @return The user ID.
     */
    public String getUserId() {

        return userId;
    }

    /**
     * Sets the user ID.
     *
     * @param userId The user ID to set.
     */
    public void setUserId(String userId) {

        this.userId = userId;
    }

    /**
     * Returns the association ID.
     *
     * @return The association ID.
     */
    public String getAssociationId() {

        return associationId;
    }

    /**
     * Sets the association ID.
     *
     * @param associationId The association ID to set.
     */
    public void setAssociationId(String associationId) {

        this.associationId = associationId;
    }

    /**
     * Returns the association type.
     *
     * @return The association type.
     */
    public AssociationTypes getAssociationType() {

        return associationType;
    }

    /**
     * Sets the association type.
     *
     * @param associationType The association type to set.
     */
    public void setAssociationType(AssociationTypes associationType) {

        this.associationType = associationType;
    }

    /**
     * Returns the operation type.
     *
     * @return The operation type.
     */
    public BulkAssociationPathObject.Operations getOperation() {

        return operation;
    }

    /**
     * Sets the operation type.
     *
     * @param operation The operation type to set.
     */
    public void setOperation(BulkAssociationPathObject.Operations operation) {

        this.operation = operation;
    }
}
