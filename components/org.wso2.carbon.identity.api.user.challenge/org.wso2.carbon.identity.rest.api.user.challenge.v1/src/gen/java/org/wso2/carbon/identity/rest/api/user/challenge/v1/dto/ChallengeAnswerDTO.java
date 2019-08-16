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

package org.wso2.carbon.identity.rest.api.user.challenge.v1.dto;

import org.wso2.carbon.identity.rest.api.user.challenge.v1.dto.ChallengeQuestionDTO;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;





@ApiModel(description = "")
public class ChallengeAnswerDTO  {
  
  
  
  private String questionSetId = null;
  
  
  private ChallengeQuestionDTO challengeQuestion = null;
  
  @NotNull 
  private String answer = null;

  
  /**
   * A unique ID to identify the challenge set.
   **/
  @ApiModelProperty(value = "A unique ID to identify the challenge set.")
  @JsonProperty("questionSetId")
  public String getQuestionSetId() {
    return questionSetId;
  }
  public void setQuestionSetId(String questionSetId) {
    this.questionSetId = questionSetId;
  }

  
  /**
   * A challenge question that is selected to answer from the set.
   **/
  @ApiModelProperty(value = "A challenge question that is selected to answer from the set.")
  @JsonProperty("challengeQuestion")
  public ChallengeQuestionDTO getChallengeQuestion() {
    return challengeQuestion;
  }
  public void setChallengeQuestion(ChallengeQuestionDTO challengeQuestion) {
    this.challengeQuestion = challengeQuestion;
  }

  
  /**
   * Answer to the challenge question.
   **/
  @ApiModelProperty(required = true, value = "Answer to the challenge question.")
  @JsonProperty("answer")
  public String getAnswer() {
    return answer;
  }
  public void setAnswer(String answer) {
    this.answer = answer;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChallengeAnswerDTO {\n");
    
    sb.append("  questionSetId: ").append(questionSetId).append("\n");
    sb.append("  challengeQuestion: ").append(challengeQuestion).append("\n");
    sb.append("  answer: ").append(answer).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
