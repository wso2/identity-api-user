/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.user.idv.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class VerificationClaimUpdateRequest  {
  
    private Boolean isVerified;
    private Map<String, Object> claimMetadata = new HashMap<>();


    /**
    **/
    public VerificationClaimUpdateRequest isVerified(Boolean isVerified) {

        this.isVerified = isVerified;
        return this;
    }
    
    @ApiModelProperty(example = "true", required = true, value = "")
    @JsonProperty("isVerified")
    @Valid
    @NotNull(message = "Property isVerified cannot be null.")

    public Boolean getIsVerified() {
        return isVerified;
    }
    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    /**
    **/
    public VerificationClaimUpdateRequest claimMetadata(Map<String, Object> claimMetadata) {

        this.claimMetadata = claimMetadata;
        return this;
    }
    
    @ApiModelProperty(example = "{\"source\": \"ONFIDO\", \"trackingId\": \"123e4567-e89b-12d3-a456-556642440000\" }", required = true, value = "")
    @JsonProperty("claimMetadata")
    @Valid
    @NotNull(message = "Property claimMetadata cannot be null.")

    public Map<String, Object> getClaimMetadata() {
        return claimMetadata;
    }
    public void setClaimMetadata(Map<String, Object> claimMetadata) {
        this.claimMetadata = claimMetadata;
    }


    public VerificationClaimUpdateRequest putClaimMetadataItem(String key, Object claimMetadataItem) {
        this.claimMetadata.put(key, claimMetadataItem);
        return this;
    }

    

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VerificationClaimUpdateRequest verificationClaimUpdateRequest = (VerificationClaimUpdateRequest) o;
        return Objects.equals(this.isVerified, verificationClaimUpdateRequest.isVerified) &&
            Objects.equals(this.claimMetadata, verificationClaimUpdateRequest.claimMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isVerified, claimMetadata);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class VerificationClaimUpdateRequest {\n");
        
        sb.append("    isVerified: ").append(toIndentedString(isVerified)).append("\n");
        sb.append("    claimMetadata: ").append(toIndentedString(claimMetadata)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
    * Convert the given object to string with each line indented by 4 spaces
    * (except the first line).
    */
    private String toIndentedString(java.lang.Object o) {

        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n");
    }
}

