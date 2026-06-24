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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Google Search tool for the Interactions API.
 *
 * <p>Enables the model to search the web using Google Search. The {@code searchTypes} field
 * controls which search modalities are enabled: {@code "web_search"}, {@code "image_search"},
 * {@code "enterprise_web_search"}.
 */
@AutoValue
@JsonDeserialize(builder = GoogleSearch.Builder.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("google_search")
public abstract class GoogleSearch extends JsonSerializable implements Tool {

  @JsonProperty("search_types")
  public abstract Optional<List<String>> searchTypes();

  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_GoogleSearch.Builder();
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    @JsonCreator
    private static Builder create() {
      return new AutoValue_GoogleSearch.Builder();
    }

    @JsonProperty("search_types")
    public abstract Builder searchTypes(List<String> searchTypes);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder searchTypes(Optional<List<String>> searchTypes);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearSearchTypes() {
      return searchTypes(Optional.empty());
    }

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder searchTypes(String... searchTypes) {
      return searchTypes(Arrays.asList(searchTypes));
    }

    public abstract GoogleSearch build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static GoogleSearch fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, GoogleSearch.class);
  }
}
