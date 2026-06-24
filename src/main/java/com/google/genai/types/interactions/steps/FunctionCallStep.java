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
import java.util.Map;
import java.util.Optional;

/** A function tool call step. */
@AutoValue
@JsonDeserialize(builder = FunctionCallStep.Builder.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("function_call")
public abstract class FunctionCallStep extends JsonSerializable implements Step {

  @JsonProperty("id")
  public abstract Optional<String> id();

  @JsonProperty("name")
  public abstract Optional<String> name();

  @JsonProperty("arguments")
  public abstract Optional<Map<String, Object>> arguments();

  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_FunctionCallStep.Builder();
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    @JsonCreator
    private static Builder create() {
      return new AutoValue_FunctionCallStep.Builder();
    }

    @JsonProperty("id")
    public abstract Builder id(String id);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder id(Optional<String> id);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearId() {
      return id(Optional.empty());
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

    @JsonProperty("arguments")
    public abstract Builder arguments(Map<String, Object> arguments);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder arguments(Optional<Map<String, Object>> arguments);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearArguments() {
      return arguments(Optional.empty());
    }

    public abstract FunctionCallStep build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static FunctionCallStep fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, FunctionCallStep.class);
  }
}
