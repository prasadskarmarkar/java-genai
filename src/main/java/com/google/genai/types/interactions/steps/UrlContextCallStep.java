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
import com.google.genai.types.interactions.UrlContextCallArguments;
import java.util.Optional;

/** URL context call step. */
@AutoValue
@JsonDeserialize(builder = UrlContextCallStep.Builder.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("url_context_call")
public abstract class UrlContextCallStep extends JsonSerializable implements Step {

  @JsonProperty("id")
  public abstract Optional<String> id();

  @JsonProperty("arguments")
  public abstract Optional<UrlContextCallArguments> arguments();

  @JsonProperty("signature")
  public abstract Optional<String> signature();

  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_UrlContextCallStep.Builder();
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    @JsonCreator
    private static Builder create() {
      return new AutoValue_UrlContextCallStep.Builder();
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

    @JsonProperty("arguments")
    public abstract Builder arguments(UrlContextCallArguments arguments);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder arguments(Optional<UrlContextCallArguments> arguments);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearArguments() {
      return arguments(Optional.empty());
    }

    @JsonProperty("signature")
    public abstract Builder signature(String signature);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder signature(Optional<String> signature);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearSignature() {
      return signature(Optional.empty());
    }

    public abstract UrlContextCallStep build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static UrlContextCallStep fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, UrlContextCallStep.class);
  }
}
