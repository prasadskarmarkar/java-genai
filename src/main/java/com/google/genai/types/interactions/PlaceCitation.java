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
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.genai.JsonSerializable;
import com.google.genai.types.ExcludeFromGeneratedCoverageReport;
import java.util.Optional;

/** A place citation annotation for model-generated content (Google Maps places). */
@AutoValue
@JsonDeserialize(builder = PlaceCitation.Builder.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("place_citation")
public abstract class PlaceCitation extends JsonSerializable implements Annotation {

  @JsonProperty("place_id")
  public abstract Optional<String> placeId();

  @JsonProperty("name")
  public abstract Optional<String> name();

  @JsonProperty("url")
  public abstract Optional<String> url();

  @JsonProperty("start_index")
  public abstract Optional<Integer> startIndex();

  @JsonProperty("end_index")
  public abstract Optional<Integer> endIndex();

  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_PlaceCitation.Builder();
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    @JsonCreator
    private static Builder create() {
      return new AutoValue_PlaceCitation.Builder();
    }

    @JsonProperty("place_id")
    public abstract Builder placeId(String placeId);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder placeId(Optional<String> placeId);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearPlaceId() {
      return placeId(Optional.empty());
    }

    @JsonProperty("name")
    public abstract Builder name(String name);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder name(Optional<String> name);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearName() {
      return name(Optional.empty());
    }

    @JsonProperty("url")
    public abstract Builder url(String url);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder url(Optional<String> url);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearUrl() {
      return url(Optional.empty());
    }

    @JsonProperty("start_index")
    public abstract Builder startIndex(Integer startIndex);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder startIndex(Optional<Integer> startIndex);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearStartIndex() {
      return startIndex(Optional.empty());
    }

    @JsonProperty("end_index")
    public abstract Builder endIndex(Integer endIndex);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder endIndex(Optional<Integer> endIndex);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearEndIndex() {
      return endIndex(Optional.empty());
    }

    public abstract PlaceCitation build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static PlaceCitation fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, PlaceCitation.class);
  }
}
