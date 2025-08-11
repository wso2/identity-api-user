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

package org.wso2.carbon.identity.rest.api.user.association.v1.dto;

import org.wso2.carbon.identity.rest.api.user.association.v1.dto.UserFederatedAssociationDataDTO;
import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel(description = "")
public class BulkFederatedAssociationOperationDTO {

    public enum MethodEnum {
         POST,  DELETE, 
    };
    @Valid 
    @NotNull(message = "Property method cannot be null.") 
    private MethodEnum method = null;

    @Valid 
    @NotNull(message = "Property bulkId cannot be null.") 
    private String bulkId = null;

    @Valid 
    @NotNull(message = "Property path cannot be null.") 
    private String path = null;

    @Valid 
    private UserFederatedAssociationDataDTO data = null;

    /**
    * HTTP method to be used for the operation. Supported methods are POST and, DELETE. These values\nIf the method is not specified, the operation will be treated as a bad request.\n
    **/
    @ApiModelProperty(required = true, value = "HTTP method to be used for the operation. Supported methods are POST and, DELETE. These values\nIf the method is not specified, the operation will be treated as a bad request.\n")
    @JsonProperty("method")
    public MethodEnum getMethod() {
        return method;
    }
    public void setMethod(MethodEnum method) {
        this.method = method;
    }

    /**
    **/
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("bulkId")
    public String getBulkId() {
        return bulkId;
    }
    public void setBulkId(String bulkId) {
        this.bulkId = bulkId;
    }

    /**
    * The path to the resource to be operated on. The path should be relative to the base path of the API.\nFull path for a federated association operation would be\n`/t/{tenant-domain}/api/users/v1/{user-id}/federated-associations/{association-id}`.\n
    **/
    @ApiModelProperty(required = true, value = "The path to the resource to be operated on. The path should be relative to the base path of the API.\nFull path for a federated association operation would be\n`/t/{tenant-domain}/api/users/v1/{user-id}/federated-associations/{association-id}`.\n")
    @JsonProperty("path")
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    /**
    **/
    @ApiModelProperty(value = "")
    @JsonProperty("data")
    public UserFederatedAssociationDataDTO getData() {
        return data;
    }
    public void setData(UserFederatedAssociationDataDTO data) {
        this.data = data;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class BulkFederatedAssociationOperationDTO {\n");
        
        sb.append("    method: ").append(method).append("\n");
        sb.append("    bulkId: ").append(bulkId).append("\n");
        sb.append("    path: ").append(path).append("\n");
        sb.append("    data: ").append(data).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
