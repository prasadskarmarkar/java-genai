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

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.genai.JsonSerializable;
import com.google.genai.types.interactions.InteractionContent;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Represents an interaction with a model or agent.
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
@AutoValue
@JsonDeserialize(builder = Interaction.Builder.class)
public abstract class Interaction extends JsonSerializable {
  /** Unique identifier for the interaction. */
  @JsonProperty("id")
  public abstract Optional<String> id();

  /** The status of the interaction. */
  @JsonProperty("status")
  public abstract Optional<InteractionStatus> status();

  /** The agent identifier (e.g., "deep-research-pro-preview-12-2025"). */
  @JsonProperty("agent")
  public abstract Optional<String> agent();

  /** The model used for the interaction. */
  @JsonProperty("model")
  public abstract Optional<String> model();

  /**
   * The output content from the interaction.
   *
   * <p>Note: Outputs use InteractionContent (discriminated union with type field), not the
   * standard Content type with parts.
   */
  @JsonProperty("outputs")
  public abstract Optional<List<InteractionContent>> outputs();

  /** The ID of the previous interaction for conversation continuity. */
  @JsonProperty("previous_interaction_id")
  public abstract Optional<String> previousInteractionId();

  /** The role in the conversation. */
  @JsonProperty("role")
  public abstract Optional<String> role();

  /** The creation timestamp. */
  @JsonProperty("created")
  public abstract Optional<Instant> created();

  /** The last update timestamp. */
  @JsonProperty("updated")
  public abstract Optional<Instant> updated();

  /** Token usage statistics for the interaction. */
  @JsonProperty("usage")
  public abstract Optional<UsageMetadata> usage();

  /**
   * History of interactions during Automatic Function Calling (AFC).
   *
   * <p>This field is populated when AFC is enabled and the SDK automatically executes function
   * calls. It contains the sequence of interactions that occurred during the AFC loop.
   *
   * <p>This field is not serialized to JSON.
   */
  @JsonIgnore
  public abstract Optional<List<Interaction>> automaticFunctionCallingHistory();

  /** Instantiates a builder for Interaction. */
  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_Interaction.Builder();
  }

  /** Creates a builder with the same values as this instance. */
  public abstract Builder toBuilder();

  /** Builder for Interaction. */
  @AutoValue.Builder
  public abstract static class Builder {
    /** For internal usage. Please use `Interaction.builder()` for instantiation. */
    @JsonCreator
    private static Builder create() {
      return new AutoValue_Interaction.Builder();
    }

    /**
     * Setter for id.
     *
     * <p>id: Unique identifier for the interaction.
     */
    @JsonProperty("id")
    public abstract Builder id(String id);

    /** Internal setter for id with Optional. */
    @ExcludeFromGeneratedCoverageReport
    abstract Builder id(Optional<String> id);

    /**
     * Clear method for id.
     *
     * <p>Removes the id field.
     */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearId() {
      return id(Optional.empty());
    }

    /**
     * Setter for status.
     *
     * <p>status: The status of the interaction.
     */
    @JsonProperty("status")
    public abstract Builder status(InteractionStatus status);

    /** Internal setter for status with Optional. */
    @ExcludeFromGeneratedCoverageReport
    abstract Builder status(Optional<InteractionStatus> status);

    /**
     * Clear method for status.
     *
     * <p>Removes the status field.
     */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearStatus() {
      return status(Optional.empty());
    }

    /**
     * Setter for agent.
     *
     * <p>agent: The agent identifier.
     */
    @JsonProperty("agent")
    public abstract Builder agent(String agent);

    /** Internal setter for agent with Optional. */
    @ExcludeFromGeneratedCoverageReport
    abstract Builder agent(Optional<String> agent);

    /**
     * Clear method for agent.
     *
     * <p>Removes the agent field.
     */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearAgent() {
      return agent(Optional.empty());
    }

    /**
     * Setter for model.
     *
     * <p>model: The model used for the interaction.
     */
    @JsonProperty("model")
    public abstract Builder model(String model);

    /** Internal setter for model with Optional. */
    @ExcludeFromGeneratedCoverageReport
    abstract Builder model(Optional<String> model);

    /**
     * Clear method for model.
     *
     * <p>Removes the model field.
     */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearModel() {
      return model(Optional.empty());
    }

    /**
     * Setter for outputs.
     *
     * <p>outputs: The output content from the interaction.
     */
    @JsonProperty("outputs")
    public abstract Builder outputs(List<InteractionContent> outputs);

    /**
     * Setter for outputs (varargs convenience method).
     *
     * <p>outputs: The output content from the interaction.
     */
    @CanIgnoreReturnValue
    public Builder outputs(InteractionContent... outputs) {
      return outputs(Arrays.asList(outputs));
    }

    /** Internal setter for outputs with Optional. */
    @ExcludeFromGeneratedCoverageReport
    abstract Builder outputs(Optional<List<InteractionContent>> outputs);

    /**
     * Clear method for outputs.
     *
     * <p>Removes the outputs field.
     */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearOutputs() {
      return outputs(Optional.empty());
    }

    /**
     * Setter for previousInteractionId.
     *
     * <p>previousInteractionId: The ID of the previous interaction for conversation continuity.
     */
    @JsonProperty("previous_interaction_id")
    public abstract Builder previousInteractionId(String previousInteractionId);

    /** Internal setter for previousInteractionId with Optional. */
    @ExcludeFromGeneratedCoverageReport
    abstract Builder previousInteractionId(Optional<String> previousInteractionId);

    /**
     * Clear method for previousInteractionId.
     *
     * <p>Removes the previousInteractionId field.
     */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearPreviousInteractionId() {
      return previousInteractionId(Optional.empty());
    }

    /**
     * Setter for role.
     *
     * <p>role: The role in the conversation.
     */
    @JsonProperty("role")
    public abstract Builder role(String role);

    /** Internal setter for role with Optional. */
    @ExcludeFromGeneratedCoverageReport
    abstract Builder role(Optional<String> role);

    /**
     * Clear method for role.
     *
     * <p>Removes the role field.
     */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearRole() {
      return role(Optional.empty());
    }

    /**
     * Setter for created.
     *
     * <p>created: The creation timestamp.
     */
    @JsonProperty("created")
    public abstract Builder created(Instant created);

    /** Internal setter for created with Optional. */
    @ExcludeFromGeneratedCoverageReport
    abstract Builder created(Optional<Instant> created);

    /**
     * Clear method for created.
     *
     * <p>Removes the created field.
     */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearCreated() {
      return created(Optional.empty());
    }

    /**
     * Setter for updated.
     *
     * <p>updated: The last update timestamp.
     */
    @JsonProperty("updated")
    public abstract Builder updated(Instant updated);

    /** Internal setter for updated with Optional. */
    @ExcludeFromGeneratedCoverageReport
    abstract Builder updated(Optional<Instant> updated);

    /**
     * Clear method for updated.
     *
     * <p>Removes the updated field.
     */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearUpdated() {
      return updated(Optional.empty());
    }

    /**
     * Setter for usage.
     *
     * <p>usage: Token usage statistics for the interaction.
     */
    @JsonProperty("usage")
    public abstract Builder usage(UsageMetadata usage);

    /**
     * Setter for usage builder.
     *
     * <p>usage: Token usage statistics for the interaction.
     */
    @CanIgnoreReturnValue
    public Builder usage(UsageMetadata.Builder usageBuilder) {
      return usage(usageBuilder.build());
    }

    /** Internal setter for usage with Optional. */
    @ExcludeFromGeneratedCoverageReport
    abstract Builder usage(Optional<UsageMetadata> usage);

    /**
     * Clear method for usage.
     *
     * <p>Removes the usage field.
     */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearUsage() {
      return usage(Optional.empty());
    }

    /**
     * Setter for automaticFunctionCallingHistory.
     *
     * <p>automaticFunctionCallingHistory: History of interactions during AFC.
     */
    @JsonIgnore
    public abstract Builder automaticFunctionCallingHistory(
        List<Interaction> automaticFunctionCallingHistory);

    /**
     * Setter for automaticFunctionCallingHistory (varargs convenience method).
     *
     * <p>automaticFunctionCallingHistory: History of interactions during AFC.
     */
    @CanIgnoreReturnValue
    public Builder automaticFunctionCallingHistory(Interaction... automaticFunctionCallingHistory) {
      return automaticFunctionCallingHistory(Arrays.asList(automaticFunctionCallingHistory));
    }

    /** Internal setter for automaticFunctionCallingHistory with Optional. */
    @ExcludeFromGeneratedCoverageReport
    abstract Builder automaticFunctionCallingHistory(
        Optional<List<Interaction>> automaticFunctionCallingHistory);

    /**
     * Clear method for automaticFunctionCallingHistory.
     *
     * <p>Removes the automaticFunctionCallingHistory field.
     */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearAutomaticFunctionCallingHistory() {
      return automaticFunctionCallingHistory(Optional.empty());
    }

    /** Builds the Interaction instance. */
    public abstract Interaction build();
  }

  /** Deserializes an Interaction from a JSON string. */
  @ExcludeFromGeneratedCoverageReport
  public static Interaction fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, Interaction.class);
  }
}
