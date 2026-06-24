/*
 * Copyright 2025 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.genai.types.interactions.steps;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.genai.JsonSerializable;
import com.google.genai.types.ExcludeFromGeneratedCoverageReport;
import java.util.Optional;

/**
 * MCP server tool result step.
 *
 * <p>The {@code result} field is typed as {@code Object} because the spec defines it as
 * {@code oneOf [object, string, array<ImageContent|TextContent>]}.
 */
@AutoValue
@JsonDeserialize(builder = McpServerToolResultStep.Builder.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("mcp_server_tool_result")
public abstract class McpServerToolResultStep extends JsonSerializable implements Step {

  @JsonProperty("call_id")
  public abstract Optional<String> callId();

  @JsonProperty("name")
  public abstract Optional<String> name();

  @JsonProperty("server_name")
  public abstract Optional<String> serverName();

  @JsonProperty("result")
  public abstract Optional<Object> result();

  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_McpServerToolResultStep.Builder();
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    @JsonCreator
    private static Builder create() {
      return new AutoValue_McpServerToolResultStep.Builder();
    }

    @JsonProperty("call_id")
    public abstract Builder callId(String callId);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder callId(Optional<String> callId);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearCallId() {
      return callId(Optional.empty());
    }

    @JsonProperty("name")
    public abstract Builder name(String name);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder name(Optional<String> name);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearName() {
      return name(Optional.empty());
    }

    @JsonProperty("server_name")
    public abstract Builder serverName(String serverName);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder serverName(Optional<String> serverName);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearServerName() {
      return serverName(Optional.empty());
    }

    @JsonProperty("result")
    public abstract Builder result(Object result);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder result(Optional<Object> result);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearResult() {
      return result(Optional.empty());
    }

    public abstract McpServerToolResultStep build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static McpServerToolResultStep fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, McpServerToolResultStep.class);
  }
}
