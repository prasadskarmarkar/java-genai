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

package com.google.genai.types.interactions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.genai.JsonSerializable;
import com.google.genai.types.ExcludeFromGeneratedCoverageReport;
import com.google.genai.types.HttpResponse;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.steps.ModelOutputStep;
import com.google.genai.types.interactions.steps.Step;
import com.google.genai.types.interactions.tools.Tool;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents an interaction with a model or agent.
 *
 * <p>The Interactions API is available in both Vertex AI and Gemini API.
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
@AutoValue
@JsonDeserialize(builder = Interaction.Builder.class)
public abstract class Interaction extends JsonSerializable {

  /** Unique identifier for the interaction. */
  @JsonProperty("id")
  public abstract String id();

  /** The status of the interaction. */
  @JsonProperty("status")
  public abstract InteractionStatus status();

  /** The agent identifier (e.g., "deep-research-pro-preview-12-2025"). */
  @JsonProperty("agent")
  public abstract Optional<String> agent();

  /** The model used for the interaction. */
  @JsonProperty("model")
  public abstract Optional<String> model();

  /** The object type identifier. Always "interaction" for this resource. */
  @JsonProperty("object")
  public abstract Optional<String> object();

  /** The steps that make up the interaction. */
  @JsonProperty("steps")
  public abstract Optional<List<Step>> steps();

  /** The ID of the previous interaction for conversation continuity. */
  @JsonProperty("previous_interaction_id")
  public abstract Optional<String> previousInteractionId();

  /** The creation timestamp. */
  @JsonProperty("created")
  public abstract Optional<Instant> created();

  /** The last update timestamp. */
  @JsonProperty("updated")
  public abstract Optional<Instant> updated();

  /** Token usage statistics for the interaction. */
  @JsonProperty("usage")
  public abstract Optional<Usage> usage();

  /** The environment ID for the interaction. Populated if environment config was set. */
  @JsonProperty("environment_id")
  public abstract Optional<String> environmentId();

  /** Developer set system instruction (echoed back in the response). */
  @JsonProperty("system_instruction")
  public abstract Optional<String> systemInstruction();

  /** Tools available to the model (echoed back in the response). */
  @JsonProperty("tools")
  public abstract Optional<List<Tool>> tools();

  /** The requested response modalities. */
  @JsonProperty("response_modalities")
  public abstract Optional<List<ResponseModality>> responseModalities();

  /** The service tier used for the interaction. */
  @JsonProperty("service_tier")
  public abstract Optional<String> serviceTier();

  /** Webhook configuration for this interaction. */
  @JsonProperty("webhook_config")
  public abstract Optional<Object> webhookConfig();

  /** The cached content used as context. */
  @JsonProperty("cached_content")
  public abstract Optional<String> cachedContent();

  /** Used to retain the full HTTP response. */
  @JsonProperty("sdkHttpResponse")
  public abstract Optional<HttpResponse> sdkHttpResponse();

  /**
   * Returns all content items from {@link ModelOutputStep} steps, flattened into a single list.
   *
   * <p>This is a convenience method replacing the former {@code outputs()} accessor. Use this to
   * access the model's generated content without manually iterating over steps.
   */
  public List<Content> getModelOutputContents() {
    if (!steps().isPresent()) {
      return Collections.emptyList();
    }
    return steps().get().stream()
        .filter(s -> s instanceof ModelOutputStep)
        .flatMap(s -> ((ModelOutputStep) s).content().orElse(Collections.emptyList()).stream())
        .collect(Collectors.toList());
  }

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
    @JsonCreator
    private static Builder create() {
      return new AutoValue_Interaction.Builder();
    }

    @JsonProperty("id")
    public abstract Builder id(String id);

    @JsonProperty("status")
    public abstract Builder status(InteractionStatus status);

    @JsonProperty("agent")
    public abstract Builder agent(String agent);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder agent(Optional<String> agent);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearAgent() {
      return agent(Optional.empty());
    }

    @JsonProperty("model")
    public abstract Builder model(String model);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder model(Optional<String> model);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearModel() {
      return model(Optional.empty());
    }

    @JsonProperty("object")
    public abstract Builder object(String object);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder object(Optional<String> object);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearObject() {
      return object(Optional.empty());
    }

    @JsonProperty("steps")
    public abstract Builder steps(List<Step> steps);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder steps(Step... steps) {
      return steps(Arrays.asList(steps));
    }

    @ExcludeFromGeneratedCoverageReport
    abstract Builder steps(Optional<List<Step>> steps);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearSteps() {
      return steps(Optional.empty());
    }

    @JsonProperty("previous_interaction_id")
    public abstract Builder previousInteractionId(String previousInteractionId);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder previousInteractionId(Optional<String> previousInteractionId);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearPreviousInteractionId() {
      return previousInteractionId(Optional.empty());
    }

    @JsonProperty("created")
    public abstract Builder created(Instant created);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder created(Optional<Instant> created);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearCreated() {
      return created(Optional.empty());
    }

    @JsonProperty("updated")
    public abstract Builder updated(Instant updated);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder updated(Optional<Instant> updated);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearUpdated() {
      return updated(Optional.empty());
    }

    @JsonProperty("usage")
    public abstract Builder usage(Usage usage);

    @CanIgnoreReturnValue
    public Builder usage(Usage.Builder usageBuilder) {
      return usage(usageBuilder.build());
    }

    @ExcludeFromGeneratedCoverageReport
    abstract Builder usage(Optional<Usage> usage);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearUsage() {
      return usage(Optional.empty());
    }

    @JsonProperty("environment_id")
    public abstract Builder environmentId(String environmentId);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder environmentId(Optional<String> environmentId);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearEnvironmentId() {
      return environmentId(Optional.empty());
    }

    @JsonProperty("system_instruction")
    public abstract Builder systemInstruction(String systemInstruction);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder systemInstruction(Optional<String> systemInstruction);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearSystemInstruction() {
      return systemInstruction(Optional.empty());
    }

    @JsonProperty("tools")
    public abstract Builder tools(List<Tool> tools);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder tools(Tool... tools) {
      return tools(Arrays.asList(tools));
    }

    @ExcludeFromGeneratedCoverageReport
    abstract Builder tools(Optional<List<Tool>> tools);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearTools() {
      return tools(Optional.empty());
    }

    @JsonProperty("response_modalities")
    public abstract Builder responseModalities(List<ResponseModality> responseModalities);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder responseModalities(ResponseModality... responseModalities) {
      return responseModalities(Arrays.asList(responseModalities));
    }

    @ExcludeFromGeneratedCoverageReport
    abstract Builder responseModalities(Optional<List<ResponseModality>> responseModalities);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearResponseModalities() {
      return responseModalities(Optional.empty());
    }

    @JsonProperty("service_tier")
    public abstract Builder serviceTier(String serviceTier);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder serviceTier(Optional<String> serviceTier);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearServiceTier() {
      return serviceTier(Optional.empty());
    }

    @JsonProperty("webhook_config")
    public abstract Builder webhookConfig(Object webhookConfig);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder webhookConfig(Optional<Object> webhookConfig);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearWebhookConfig() {
      return webhookConfig(Optional.empty());
    }

    @JsonProperty("cached_content")
    public abstract Builder cachedContent(String cachedContent);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder cachedContent(Optional<String> cachedContent);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearCachedContent() {
      return cachedContent(Optional.empty());
    }

    @JsonProperty("sdkHttpResponse")
    public abstract Builder sdkHttpResponse(HttpResponse sdkHttpResponse);

    @CanIgnoreReturnValue
    public Builder sdkHttpResponse(HttpResponse.Builder sdkHttpResponseBuilder) {
      return sdkHttpResponse(sdkHttpResponseBuilder.build());
    }

    @ExcludeFromGeneratedCoverageReport
    abstract Builder sdkHttpResponse(Optional<HttpResponse> sdkHttpResponse);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearSdkHttpResponse() {
      return sdkHttpResponse(Optional.empty());
    }

    public abstract Interaction build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static Interaction fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, Interaction.class);
  }
}
