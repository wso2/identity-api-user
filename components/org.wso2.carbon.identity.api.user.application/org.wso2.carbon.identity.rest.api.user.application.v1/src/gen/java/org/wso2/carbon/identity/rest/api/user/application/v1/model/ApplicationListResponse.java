/*
* Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.user.application.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.user.application.v1.model.ApplicationResponse;
import org.wso2.carbon.identity.rest.api.user.application.v1.model.Link;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ApplicationListResponse  {
  
    private Integer totalResults;
    private Integer startIndex;
    private Integer count;
    private List<ApplicationResponse> applications = null;

    private List<Link> links = null;


    /**
    * The total number of results matching the query.
    **/
    public ApplicationListResponse totalResults(Integer totalResults) {

        this.totalResults = totalResults;
        return this;
    }
    
    @ApiModelProperty(example = "30", value = "The total number of results matching the query.")
    @JsonProperty("totalResults")
    @Valid
    public Integer getTotalResults() {
        return totalResults;
    }
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    /**
    * The 1-based index of the first result in the current set of results.
    **/
    public ApplicationListResponse startIndex(Integer startIndex) {

        this.startIndex = startIndex;
        return this;
    }
    
    @ApiModelProperty(example = "5", value = "The 1-based index of the first result in the current set of results.")
    @JsonProperty("startIndex")
    @Valid
    public Integer getStartIndex() {
        return startIndex;
    }
    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    /**
    * Number of results returned in response.
    **/
    public ApplicationListResponse count(Integer count) {

        this.count = count;
        return this;
    }
    
    @ApiModelProperty(example = "1", value = "Number of results returned in response.")
    @JsonProperty("count")
    @Valid
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
    * Applications matching the query.
    **/
    public ApplicationListResponse applications(List<ApplicationResponse> applications) {

        this.applications = applications;
        return this;
    }
    
    @ApiModelProperty(value = "Applications matching the query.")
    @JsonProperty("applications")
    @Valid
    public List<ApplicationResponse> getApplications() {
        return applications;
    }
    public void setApplications(List<ApplicationResponse> applications) {
        this.applications = applications;
    }

    public ApplicationListResponse addApplicationsItem(ApplicationResponse applicationsItem) {
        if (this.applications == null) {
            this.applications = new ArrayList<>();
        }
        this.applications.add(applicationsItem);
        return this;
    }

        /**
    * Navigation links.
    **/
    public ApplicationListResponse links(List<Link> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"href\":\"/t/carbon.super/api/user/v1/me/applications?offset=10&limit=10\",\"rel\":\"next\"},{\"href\":\"/t/carbon.super/api/user/v1/me/applications?offset=0&limit=5\",\"rel\":\"previous\"},{\"href\":\"/t/carbon.super/api/user/v1/me/applications?offset=0&limit=10\",\"rel\":\"first\"},{\"href\":\"/t/carbon.super/api/user/v1/me/applications?offset=20&limit=10\",\"rel\":\"last\"}]", value = "Navigation links.")
    @JsonProperty("links")
    @Valid
    public List<Link> getLinks() {
        return links;
    }
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public ApplicationListResponse addLinksItem(Link linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
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
        ApplicationListResponse applicationListResponse = (ApplicationListResponse) o;
        return Objects.equals(this.totalResults, applicationListResponse.totalResults) &&
            Objects.equals(this.startIndex, applicationListResponse.startIndex) &&
            Objects.equals(this.count, applicationListResponse.count) &&
            Objects.equals(this.applications, applicationListResponse.applications) &&
            Objects.equals(this.links, applicationListResponse.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalResults, startIndex, count, applications, links);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ApplicationListResponse {\n");
        
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    startIndex: ").append(toIndentedString(startIndex)).append("\n");
        sb.append("    count: ").append(toIndentedString(count)).append("\n");
        sb.append("    applications: ").append(toIndentedString(applications)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
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

