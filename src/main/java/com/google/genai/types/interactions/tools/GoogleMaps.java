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
import java.util.Optional;

/** Google Maps tool for the Interactions API. */
@AutoValue
@JsonDeserialize(builder = GoogleMaps.Builder.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("google_maps")
public abstract class GoogleMaps extends JsonSerializable implements Tool {

  @JsonProperty("enable_widget")
  public abstract Optional<Boolean> enableWidget();

  @JsonProperty("latitude")
  public abstract Optional<Double> latitude();

  @JsonProperty("longitude")
  public abstract Optional<Double> longitude();

  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_GoogleMaps.Builder();
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    @JsonCreator
    private static Builder create() {
      return new AutoValue_GoogleMaps.Builder();
    }

    @JsonProperty("enable_widget")
    public abstract Builder enableWidget(Boolean enableWidget);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder enableWidget(Optional<Boolean> enableWidget);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearEnableWidget() {
      return enableWidget(Optional.empty());
    }

    @JsonProperty("latitude")
    public abstract Builder latitude(Double latitude);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder latitude(Optional<Double> latitude);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearLatitude() {
      return latitude(Optional.empty());
    }

    @JsonProperty("longitude")
    public abstract Builder longitude(Double longitude);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder longitude(Optional<Double> longitude);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearLongitude() {
      return longitude(Optional.empty());
    }

    public abstract GoogleMaps build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static GoogleMaps fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, GoogleMaps.class);
  }
}
