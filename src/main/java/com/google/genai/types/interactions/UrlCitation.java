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

/** A URL citation annotation for model-generated content. */
@AutoValue
@JsonDeserialize(builder = UrlCitation.Builder.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("url_citation")
public abstract class UrlCitation extends JsonSerializable implements Annotation {

  @JsonProperty("url")
  public abstract Optional<String> url();

  @JsonProperty("title")
  public abstract Optional<String> title();

  @JsonProperty("start_index")
  public abstract Optional<Integer> startIndex();

  @JsonProperty("end_index")
  public abstract Optional<Integer> endIndex();

  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_UrlCitation.Builder();
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    @JsonCreator
    private static Builder create() {
      return new AutoValue_UrlCitation.Builder();
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

    @JsonProperty("title")
    public abstract Builder title(String title);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder title(Optional<String> title);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearTitle() {
      return title(Optional.empty());
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

    public abstract UrlCitation build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static UrlCitation fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, UrlCitation.class);
  }
}
