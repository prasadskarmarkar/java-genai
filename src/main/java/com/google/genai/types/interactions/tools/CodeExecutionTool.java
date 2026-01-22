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

package com.google.genai.types.interactions.tools;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.genai.JsonSerializable;
import com.google.genai.types.ExcludeFromGeneratedCoverageReport;


/**
 * Code execution tool for the Interactions API.
 *
 * <p>Enables the model to execute code as part of generation.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * CodeExecutionTool codeTool = CodeExecutionTool.builder().build();
 * }</pre>
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
@AutoValue
@JsonDeserialize(builder = CodeExecutionTool.Builder.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("code_execution")
public abstract class CodeExecutionTool extends JsonSerializable implements InteractionTool {

  /** Instantiates a builder for CodeExecutionTool. */
  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_CodeExecutionTool.Builder();
  }

  /** Creates a builder with the same values as this instance. */
  public abstract Builder toBuilder();

  /** Builder for CodeExecutionTool. */
  @AutoValue.Builder
  public abstract static class Builder {
    /** For internal usage. Please use {@code CodeExecutionTool.builder()} for instantiation. */
    @JsonCreator
    private static Builder create() {
      return new AutoValue_CodeExecutionTool.Builder();
    }

    public abstract CodeExecutionTool build();
  }

  /** Deserializes a JSON string to a CodeExecutionTool object. */
  @ExcludeFromGeneratedCoverageReport
  public static CodeExecutionTool fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, CodeExecutionTool.class);
  }
}
