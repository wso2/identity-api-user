/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.carbon.identity.rest.api.user.functionality.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class LockStatusResponse  {
  
    private Boolean lockStatus;
    private String unlockTime;
    private String lockReasonCode;
    private String lockReason;

    /**
    * returns true if it is locked
    **/
    public LockStatusResponse lockStatus(Boolean lockStatus) {

        this.lockStatus = lockStatus;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "returns true if it is locked")
    @JsonProperty("lockStatus")
    @Valid
    public Boolean getLockStatus() {
        return lockStatus;
    }
    public void setLockStatus(Boolean lockStatus) {
        this.lockStatus = lockStatus;
    }

    /**
    **/
    public LockStatusResponse unlockTime(String unlockTime) {

        this.unlockTime = unlockTime;
        return this;
    }
    
    @ApiModelProperty(example = "SecurityQuestionBasedBased", value = "")
    @JsonProperty("unlockTime")
    @Valid
    public String getUnlockTime() {
        return unlockTime;
    }
    public void setUnlockTime(String unlockTime) {
        this.unlockTime = unlockTime;
    }

    /**
    **/
    public LockStatusResponse lockReasonCode(String lockReasonCode) {

        this.lockReasonCode = lockReasonCode;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("lockReasonCode")
    @Valid
    public String getLockReasonCode() {
        return lockReasonCode;
    }
    public void setLockReasonCode(String lockReasonCode) {
        this.lockReasonCode = lockReasonCode;
    }

    /**
    **/
    public LockStatusResponse lockReason(String lockReason) {

        this.lockReason = lockReason;
        return this;
    }
    
    @ApiModelProperty(example = "SecurityQuestionBasedBased", value = "")
    @JsonProperty("lockReason")
    @Valid
    public String getLockReason() {
        return lockReason;
    }
    public void setLockReason(String lockReason) {
        this.lockReason = lockReason;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LockStatusResponse lockStatusResponse = (LockStatusResponse) o;
        return Objects.equals(this.lockStatus, lockStatusResponse.lockStatus) &&
            Objects.equals(this.unlockTime, lockStatusResponse.unlockTime) &&
            Objects.equals(this.lockReasonCode, lockStatusResponse.lockReasonCode) &&
            Objects.equals(this.lockReason, lockStatusResponse.lockReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lockStatus, unlockTime, lockReasonCode, lockReason);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class LockStatusResponse {\n");
        
        sb.append("    lockStatus: ").append(toIndentedString(lockStatus)).append("\n");
        sb.append("    unlockTime: ").append(toIndentedString(unlockTime)).append("\n");
        sb.append("    lockReasonCode: ").append(toIndentedString(lockReasonCode)).append("\n");
        sb.append("    lockReason: ").append(toIndentedString(lockReason)).append("\n");
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

