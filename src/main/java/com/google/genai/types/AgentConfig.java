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

// Auto-generated code. Do not edit.

package com.google.genai.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.genai.JsonSerializable;
import java.util.Optional;

/**
 * Configuration for agent-based interactions.
 *
 * <p>Note: This is a basic stub for PR 1. Full agent configuration support will be added in future
 * releases.
 */
@AutoValue
@JsonDeserialize(builder = AgentConfig.Builder.class)
public abstract class AgentConfig extends JsonSerializable {
  /** The type of agent configuration. */
  @JsonProperty("type")
  public abstract Optional<String> type();

  /** Instantiates a builder for AgentConfig. */
  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_AgentConfig.Builder();
  }

  /** Creates a builder with the same values as this instance. */
  public abstract Builder toBuilder();

  /** Builder for AgentConfig. */
  @AutoValue.Builder
  public abstract static class Builder {
    /** For internal usage. Please use `AgentConfig.builder()` for instantiation. */
    @JsonCreator
    private static Builder create() {
      return new AutoValue_AgentConfig.Builder();
    }

    /**
     * Setter for type.
     *
     * <p>type: The type of agent configuration.
     */
    @JsonProperty("type")
    public abstract Builder type(String type);

    /** Internal setter for type with Optional. */
    @ExcludeFromGeneratedCoverageReport
    abstract Builder type(Optional<String> type);

    /** Builds the AgentConfig instance. */
    public abstract AgentConfig build();
  }

  /** Deserializes an AgentConfig from a JSON string. */
  @ExcludeFromGeneratedCoverageReport
  public static AgentConfig fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, AgentConfig.class);
  }
}
