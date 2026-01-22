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
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.genai.JsonSerializable;
import com.google.genai.types.interactions.content.InteractionContent;
import com.google.genai.types.interactions.InteractionInput;
import com.google.genai.types.interactions.InteractionTurn;
import com.google.genai.types.interactions.tools.InteractionTool;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Configuration for creating an interaction.
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
@AutoValue
@JsonDeserialize(builder = CreateInteractionConfig.Builder.class)
public abstract class CreateInteractionConfig extends JsonSerializable {
  /** Used to override HTTP request options. */
  @JsonProperty("httpOptions")
  public abstract Optional<HttpOptions> httpOptions();

  /** Required: The input for the interaction. */
  @JsonProperty("input")
  public abstract Optional<InteractionInput> input();

  /** The model to use for the interaction. Either model or agent must be specified. */
  @JsonProperty("model")
  public abstract Optional<String> model();

  /** The agent to use for the interaction. Either model or agent must be specified. */
  @JsonProperty("agent")
  public abstract Optional<String> agent();

  /** Whether to run the interaction in the background. */
  @JsonProperty("background")
  public abstract Optional<Boolean> background();

  /** Whether to stream the interaction response. */
  @JsonProperty("stream")
  public abstract Optional<Boolean> stream();

  /** Configuration for generation (only used with model-based interactions). */
  @JsonProperty("generationConfig")
  public abstract Optional<GenerationConfig> generationConfig();

  /** Configuration for the agent (only used with agent-based interactions). */
  @JsonProperty("agentConfig")
  public abstract Optional<AgentConfig> agentConfig();

  /** The ID of the previous interaction for conversation continuity. */
  @JsonProperty("previousInteractionId")
  public abstract Optional<String> previousInteractionId();

  /** The expected response format. */
  @JsonProperty("responseFormat")
  public abstract Optional<Object> responseFormat();

  /** The MIME type for the response. */
  @JsonProperty("responseMimeType")
  public abstract Optional<String> responseMimeType();

  /** The modalities for the response (e.g., ["text", "image", "audio"]). */
  @JsonProperty("responseModalities")
  public abstract Optional<List<String>> responseModalities();

  /** Whether to store the interaction history. */
  @JsonProperty("store")
  public abstract Optional<Boolean> store();

  /** Developer set system instruction. */
  @JsonProperty("systemInstruction")
  public abstract Optional<Content> systemInstruction();

  /**
   * A list of tools the model may use to generate the next response.
   *
   * <p>Use the dedicated Interactions tool types such as {@code FunctionTool}, {@code
   * GoogleSearchTool}, {@code CodeExecutionTool}, etc.
   */
  @JsonProperty("tools")
  public abstract Optional<List<InteractionTool>> tools();

  /** Instantiates a builder for CreateInteractionConfig. */
  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_CreateInteractionConfig.Builder();
  }

  /** Creates a builder with the same values as this instance. */
  public abstract Builder toBuilder();

  /** Builder for CreateInteractionConfig. */
  @AutoValue.Builder
  public abstract static class Builder {
    /**
     * For internal usage. Please use `CreateInteractionConfig.builder()` for instantiation.
     */
    @JsonCreator
    private static Builder create() {
      return new AutoValue_CreateInteractionConfig.Builder();
    }

    /**
     * Setter for httpOptions.
     *
     * <p>httpOptions: Used to override HTTP request options.
     */
    @JsonProperty("httpOptions")
    public abstract Builder httpOptions(HttpOptions httpOptions);

    /**
     * Setter for httpOptions builder.
     *
     * <p>httpOptions: Used to override HTTP request options.
     */
    @CanIgnoreReturnValue
    public Builder httpOptions(HttpOptions.Builder httpOptionsBuilder) {
      return httpOptions(httpOptionsBuilder.build());
    }

    @ExcludeFromGeneratedCoverageReport
    abstract Builder httpOptions(Optional<HttpOptions> httpOptions);

    /** Clears the value of httpOptions field. */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearHttpOptions() {
      return httpOptions(Optional.empty());
    }

    /**
     * Setter for input.
     *
     * <p>input: The input for the interaction.
     */
    @JsonProperty("input")
    public abstract Builder input(InteractionInput input);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder input(Optional<InteractionInput> input);

    /** Clears the value of input field. */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearInput() {
      return input(Optional.empty());
    }

    /**
     * Convenience setter for input from a string.
     *
     * <p>input: The input text for the interaction.
     */
    @CanIgnoreReturnValue
    public Builder input(String text) {
      return input(InteractionInput.fromString(text));
    }

    /**
     * Convenience setter for input from a list of InteractionContent.
     *
     * <p>input: The input content for the interaction.
     */
    @CanIgnoreReturnValue
    public Builder inputFromContents(List<InteractionContent> contents) {
      return input(InteractionInput.fromContents(contents));
    }

    /**
     * Convenience setter for input from InteractionContent objects (varargs).
     *
     * <p>input: The input content for the interaction.
     */
    @CanIgnoreReturnValue
    public Builder inputFromContents(InteractionContent... contents) {
      return input(InteractionInput.fromContents(contents));
    }

    /**
     * Convenience setter for input from a list of InteractionTurn.
     *
     * <p>input: The input turns for the interaction.
     */
    @CanIgnoreReturnValue
    public Builder inputFromTurns(List<InteractionTurn> turns) {
      return input(InteractionInput.fromTurns(turns));
    }

    /**
     * Convenience setter for input from InteractionTurn objects (varargs).
     *
     * <p>input: The input turns for the interaction.
     */
    @CanIgnoreReturnValue
    public Builder inputFromTurns(InteractionTurn... turns) {
      return input(InteractionInput.fromTurns(turns));
    }

    /**
     * Setter for model.
     *
     * <p>model: The model to use for the interaction.
     */
    @JsonProperty("model")
    public abstract Builder model(String model);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder model(Optional<String> model);

    /** Clears the value of model field. */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearModel() {
      return model(Optional.empty());
    }

    /**
     * Setter for agent.
     *
     * <p>agent: The agent to use for the interaction.
     */
    @JsonProperty("agent")
    public abstract Builder agent(String agent);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder agent(Optional<String> agent);

    /** Clears the value of agent field. */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearAgent() {
      return agent(Optional.empty());
    }

    /**
     * Setter for background.
     *
     * <p>background: Whether to run the interaction in the background.
     */
    @JsonProperty("background")
    public abstract Builder background(Boolean background);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder background(Optional<Boolean> background);

    /** Clears the value of background field. */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearBackground() {
      return background(Optional.empty());
    }

    /**
     * Setter for stream.
     *
     * <p>stream: Whether to stream the interaction response.
     */
    @JsonProperty("stream")
    public abstract Builder stream(Boolean stream);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder stream(Optional<Boolean> stream);

    /** Clears the value of stream field. */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearStream() {
      return stream(Optional.empty());
    }

    /**
     * Setter for generationConfig.
     *
     * <p>generationConfig: Configuration for generation.
     */
    @JsonProperty("generationConfig")
    public abstract Builder generationConfig(GenerationConfig generationConfig);

    /**
     * Setter for generationConfig builder.
     *
     * <p>generationConfig: Configuration for generation.
     */
    @CanIgnoreReturnValue
    public Builder generationConfig(GenerationConfig.Builder generationConfigBuilder) {
      return generationConfig(generationConfigBuilder.build());
    }

    @ExcludeFromGeneratedCoverageReport
    abstract Builder generationConfig(Optional<GenerationConfig> generationConfig);

    /** Clears the value of generationConfig field. */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearGenerationConfig() {
      return generationConfig(Optional.empty());
    }

    /**
     * Setter for agentConfig.
     *
     * <p>agentConfig: Configuration for the agent.
     */
    @JsonProperty("agentConfig")
    public abstract Builder agentConfig(AgentConfig agentConfig);

    /**
     * Setter for agentConfig builder.
     *
     * <p>agentConfig: Configuration for the agent.
     */
    @CanIgnoreReturnValue
    public Builder agentConfig(AgentConfig.Builder agentConfigBuilder) {
      return agentConfig(agentConfigBuilder.build());
    }

    @ExcludeFromGeneratedCoverageReport
    abstract Builder agentConfig(Optional<AgentConfig> agentConfig);

    /** Clears the value of agentConfig field. */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearAgentConfig() {
      return agentConfig(Optional.empty());
    }

    /**
     * Setter for previousInteractionId.
     *
     * <p>previousInteractionId: The ID of the previous interaction.
     */
    @JsonProperty("previousInteractionId")
    public abstract Builder previousInteractionId(String previousInteractionId);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder previousInteractionId(Optional<String> previousInteractionId);

    /** Clears the value of previousInteractionId field. */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearPreviousInteractionId() {
      return previousInteractionId(Optional.empty());
    }

    /**
     * Setter for responseFormat.
     *
     * <p>responseFormat: The expected response format.
     */
    @JsonProperty("responseFormat")
    public abstract Builder responseFormat(Object responseFormat);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder responseFormat(Optional<Object> responseFormat);

    /** Clears the value of responseFormat field. */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearResponseFormat() {
      return responseFormat(Optional.empty());
    }

    /**
     * Setter for responseMimeType.
     *
     * <p>responseMimeType: The MIME type for the response.
     */
    @JsonProperty("responseMimeType")
    public abstract Builder responseMimeType(String responseMimeType);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder responseMimeType(Optional<String> responseMimeType);

    /** Clears the value of responseMimeType field. */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearResponseMimeType() {
      return responseMimeType(Optional.empty());
    }

    /**
     * Setter for responseModalities.
     *
     * <p>responseModalities: The modalities for the response.
     */
    @JsonProperty("responseModalities")
    public abstract Builder responseModalities(List<String> responseModalities);

    /**
     * Setter for responseModalities (varargs convenience method).
     *
     * <p>responseModalities: The modalities for the response.
     */
    @CanIgnoreReturnValue
    public Builder responseModalities(String... responseModalities) {
      return responseModalities(Arrays.asList(responseModalities));
    }

    @ExcludeFromGeneratedCoverageReport
    abstract Builder responseModalities(Optional<List<String>> responseModalities);

    /** Clears the value of responseModalities field. */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearResponseModalities() {
      return responseModalities(Optional.empty());
    }

    /**
     * Setter for store.
     *
     * <p>store: Whether to store the interaction history.
     */
    @JsonProperty("store")
    public abstract Builder store(Boolean store);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder store(Optional<Boolean> store);

    /** Clears the value of store field. */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearStore() {
      return store(Optional.empty());
    }

    /**
     * Setter for systemInstruction.
     *
     * <p>systemInstruction: Developer set system instruction.
     */
    @JsonProperty("systemInstruction")
    public abstract Builder systemInstruction(Content systemInstruction);

    /**
     * Setter for systemInstruction builder.
     *
     * <p>systemInstruction: Developer set system instruction.
     */
    @CanIgnoreReturnValue
    public Builder systemInstruction(Content.Builder systemInstructionBuilder) {
      return systemInstruction(systemInstructionBuilder.build());
    }

    @ExcludeFromGeneratedCoverageReport
    abstract Builder systemInstruction(Optional<Content> systemInstruction);

    /** Clears the value of systemInstruction field. */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearSystemInstruction() {
      return systemInstruction(Optional.empty());
    }

    /**
     * Setter for tools.
     *
     * <p>tools: A list of tools the model may use to generate the next response. Use the dedicated
     * Interactions tool types such as {@code FunctionTool}, {@code GoogleSearchTool}, etc.
     */
    @JsonProperty("tools")
    public abstract Builder tools(List<InteractionTool> tools);

    /**
     * Setter for tools (varargs convenience method).
     *
     * <p>tools: A list of tools the model may use to generate the next response.
     */
    @CanIgnoreReturnValue
    public Builder tools(InteractionTool... tools) {
      return tools(Arrays.asList(tools));
    }

    @ExcludeFromGeneratedCoverageReport
    abstract Builder tools(Optional<List<InteractionTool>> tools);

    /** Clears the value of tools field. */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearTools() {
      return tools(Optional.empty());
    }

    /** Builds the CreateInteractionConfig instance. */
    public abstract CreateInteractionConfig build();
  }

  /** Deserializes a CreateInteractionConfig from a JSON string. */
  @ExcludeFromGeneratedCoverageReport
  public static CreateInteractionConfig fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, CreateInteractionConfig.class);
  }
}
