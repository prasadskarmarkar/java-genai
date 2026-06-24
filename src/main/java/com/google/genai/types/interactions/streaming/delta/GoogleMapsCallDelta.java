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
import com.google.genai.types.interactions.GoogleMapsCallArguments;
import java.util.Optional;

/** Incremental Google Maps call delta for streaming. */
@AutoValue
@JsonDeserialize(builder = GoogleMapsCallDelta.Builder.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("google_maps_call")
public abstract class GoogleMapsCallDelta extends JsonSerializable implements Delta {

  @JsonProperty("id")
  public abstract Optional<String> id();

  @JsonProperty("arguments")
  public abstract Optional<GoogleMapsCallArguments> arguments();

  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_GoogleMapsCallDelta.Builder();
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    @JsonCreator
    private static Builder create() {
      return new AutoValue_GoogleMapsCallDelta.Builder();
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
    public abstract Builder arguments(GoogleMapsCallArguments arguments);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder arguments(Optional<GoogleMapsCallArguments> arguments);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearArguments() {
      return arguments(Optional.empty());
    }

    public abstract GoogleMapsCallDelta build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static GoogleMapsCallDelta fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, GoogleMapsCallDelta.class);
  }
}
