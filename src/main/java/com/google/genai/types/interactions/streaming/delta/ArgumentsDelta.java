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

package com.google.genai.types.interactions.streaming.delta;

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

/** Incremental arguments delta for streaming function call arguments. */
@AutoValue
@JsonDeserialize(builder = ArgumentsDelta.Builder.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("arguments_delta")
public abstract class ArgumentsDelta extends JsonSerializable implements Delta {

  @JsonProperty("arguments")
  public abstract Optional<String> arguments();

  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_ArgumentsDelta.Builder();
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    @JsonCreator
    private static Builder create() {
      return new AutoValue_ArgumentsDelta.Builder();
    }

    @JsonProperty("arguments")
    public abstract Builder arguments(String arguments);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder arguments(Optional<String> arguments);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearArguments() {
      return arguments(Optional.empty());
    }

    public abstract ArgumentsDelta build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static ArgumentsDelta fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, ArgumentsDelta.class);
  }
}
