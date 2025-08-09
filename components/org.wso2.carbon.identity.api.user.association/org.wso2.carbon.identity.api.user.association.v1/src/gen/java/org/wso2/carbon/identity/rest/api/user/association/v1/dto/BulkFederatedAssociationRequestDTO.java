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

import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.user.association.v1.dto.BulkFederatedAssociationOperationDTO;
import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel(description = "")
public class BulkFederatedAssociationRequestDTO {

    @Valid 
    private Integer failOnErrors = null;

    @Valid 
    private List<BulkFederatedAssociationOperationDTO> operations = new ArrayList<BulkFederatedAssociationOperationDTO>();

    /**
    * This represents whether the API should stop processing the operations if any of them fail. The subsequent\noperations after the the fail count is reached will not be processed.\nIf set to 0 or not set, the API will continue processing the operations even if some of them fail.\n
    **/
    @ApiModelProperty(value = "This represents whether the API should stop processing the operations if any of them fail. The subsequent\noperations after the the fail count is reached will not be processed.\nIf set to 0 or not set, the API will continue processing the operations even if some of them fail.\n")
    @JsonProperty("failOnErrors")
    public Integer getFailOnErrors() {
        return failOnErrors;
    }
    public void setFailOnErrors(Integer failOnErrors) {
        this.failOnErrors = failOnErrors;
    }

    /**
    **/
    @ApiModelProperty(value = "")
    @JsonProperty("operations")
    public List<BulkFederatedAssociationOperationDTO> getOperations() {
        return operations;
    }
    public void setOperations(List<BulkFederatedAssociationOperationDTO> operations) {
        this.operations = operations;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class BulkFederatedAssociationRequestDTO {\n");
        
        sb.append("    failOnErrors: ").append(failOnErrors).append("\n");
        sb.append("    operations: ").append(operations).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }
}
