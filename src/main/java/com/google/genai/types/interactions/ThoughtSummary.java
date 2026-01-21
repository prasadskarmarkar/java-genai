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
import java.util.Optional;

@AutoValue
@JsonDeserialize(builder = ThoughtSummary.Builder.class)
public abstract class ThoughtSummary extends JsonSerializable {

  @JsonProperty("text")
  public abstract Optional<String> text();

  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_ThoughtSummary.Builder();
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    @JsonCreator
    private static Builder create() {
      return new AutoValue_ThoughtSummary.Builder();
    }

    @JsonProperty("text")
    public abstract Builder text(String text);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder text(Optional<String> text);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearText() {
      return text(Optional.empty());
    }

    public abstract ThoughtSummary build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static ThoughtSummary fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, ThoughtSummary.class);
  }

  @ExcludeFromGeneratedCoverageReport
  public static ThoughtSummary of(String text) {
    return builder().text(text).build();
  }
}
