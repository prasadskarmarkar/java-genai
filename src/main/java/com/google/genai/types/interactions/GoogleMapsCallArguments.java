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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/** Arguments for Google Maps tool calls in the Interactions API. */
@AutoValue
@JsonDeserialize(builder = GoogleMapsCallArguments.Builder.class)
public abstract class GoogleMapsCallArguments extends JsonSerializable {

  @JsonProperty("queries")
  public abstract Optional<List<String>> queries();

  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_GoogleMapsCallArguments.Builder();
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    @JsonCreator
    private static Builder create() {
      return new AutoValue_GoogleMapsCallArguments.Builder();
    }

    @JsonProperty("queries")
    public abstract Builder queries(List<String> queries);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder queries(Optional<List<String>> queries);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearQueries() {
      return queries(Optional.empty());
    }

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder queries(String... queries) {
      return queries(Arrays.asList(queries));
    }

    public abstract GoogleMapsCallArguments build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static GoogleMapsCallArguments fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, GoogleMapsCallArguments.class);
  }
}
