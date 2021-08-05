/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.user.session.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

@ApiModel(description = "")
public class SearchResponseDTO {

    @Valid
    private URI previous = null;

    @Valid
    private URI next = null;

    @Valid
    private List<SessionDTO> resources = new ArrayList<>();

    /**
     * Endpoint that will return the previous page of data. If not included, this is the first page of data.
     **/
    @ApiModelProperty(value = "Endpoint that will return the previous page of data. If not included, this is the first page of data.")
    @JsonProperty("previous")
    public URI getPrevious() {
        return previous;
    }

    public void setPrevious(URI previous) {
        this.previous = previous;
    }

    /**
     * Endpoint that will return the next page of data. If not included, this is the last page of data.
     **/
    @ApiModelProperty(value = "Endpoint that will return the next page of data. If not included, this is the last page of data.")
    @JsonProperty("next")
    public URI getNext() {
        return next;
    }

    public void setNext(URI next) {
        this.next = next;
    }

    /**
     *
     **/
    @ApiModelProperty(value = "")
    @JsonProperty("Resources")
    public List<SessionDTO> getResources() {
        return resources;
    }

    public void setResources(List<SessionDTO> resources) {
        this.resources = resources;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class SearchResponseDTO {\n");

        sb.append("    previous: ").append(previous).append("\n");
        sb.append("    next: ").append(next).append("\n");
        sb.append("    Resources: ").append(resources).append("\n");

        sb.append("}\n");
        return sb.toString();
    }
}
