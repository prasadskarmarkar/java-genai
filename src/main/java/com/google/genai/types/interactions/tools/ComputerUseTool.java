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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.genai.JsonSerializable;
import com.google.genai.types.ExcludeFromGeneratedCoverageReport;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Computer use tool for the Interactions API.
 *
 * <p>Enables the model to interact with a computer environment.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * ComputerUseTool computerTool = ComputerUseTool.builder()
 *     .environment("browser")
 *     .build();
 * }</pre>
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
@AutoValue
@JsonDeserialize(builder = ComputerUseTool.Builder.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("computer_use")
public abstract class ComputerUseTool extends JsonSerializable implements InteractionTool {

  /** The environment for computer use (e.g., "browser", "desktop"). */
  @JsonProperty("environment")
  public abstract Optional<String> environment();

  /** List of predefined functions to exclude from computer use. */
  @JsonProperty("excludedPredefinedFunctions")
  public abstract Optional<List<String>> excludedPredefinedFunctions();

  /** Instantiates a builder for ComputerUseTool. */
  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_ComputerUseTool.Builder();
  }

  /** Creates a builder with the same values as this instance. */
  public abstract Builder toBuilder();

  /** Builder for ComputerUseTool. */
  @AutoValue.Builder
  public abstract static class Builder {
    /** For internal usage. Please use {@code ComputerUseTool.builder()} for instantiation. */
    @JsonCreator
    private static Builder create() {
      return new AutoValue_ComputerUseTool.Builder();
    }

    /**
     * Setter for environment.
     *
     * <p>environment: The environment for computer use.
     */
    @JsonProperty("environment")
    public abstract Builder environment(String environment);

    
    abstract Builder environment(Optional<String> environment);

    /** Clears the value of environment field. */
    
    @CanIgnoreReturnValue
    public Builder clearEnvironment() {
      return environment(Optional.empty());
    }

    /**
     * Setter for excludedPredefinedFunctions.
     *
     * <p>excludedPredefinedFunctions: List of predefined functions to exclude.
     */
    @JsonProperty("excludedPredefinedFunctions")
    public abstract Builder excludedPredefinedFunctions(List<String> excludedPredefinedFunctions);

    /**
     * Setter for excludedPredefinedFunctions (varargs convenience method).
     *
     * <p>excludedPredefinedFunctions: List of predefined functions to exclude.
     */
    @CanIgnoreReturnValue
    public Builder excludedPredefinedFunctions(String... excludedPredefinedFunctions) {
      return excludedPredefinedFunctions(Arrays.asList(excludedPredefinedFunctions));
    }

    
    abstract Builder excludedPredefinedFunctions(Optional<List<String>> excludedPredefinedFunctions);

    /** Clears the value of excludedPredefinedFunctions field. */
    
    @CanIgnoreReturnValue
    public Builder clearExcludedPredefinedFunctions() {
      return excludedPredefinedFunctions(Optional.empty());
    }

    public abstract ComputerUseTool build();
  }

  /** Deserializes a JSON string to a ComputerUseTool object. */
  @ExcludeFromGeneratedCoverageReport
  public static ComputerUseTool fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, ComputerUseTool.class);
  }
}
