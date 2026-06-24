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
import java.util.Optional;

/**
 * The number of grounding tool uses for a given type in an interaction.
 *
 * <p>Valid values for {@code type}: {@code "google_search"}, {@code "google_maps"},
 * {@code "retrieval"}.
 */
@AutoValue
@JsonDeserialize(builder = GroundingToolCount.Builder.class)
public abstract class GroundingToolCount extends JsonSerializable {

  @JsonProperty("type")
  public abstract Optional<String> type();

  @JsonProperty("count")
  public abstract Optional<Integer> count();

  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_GroundingToolCount.Builder();
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    @JsonCreator
    private static Builder create() {
      return new AutoValue_GroundingToolCount.Builder();
    }

    @JsonProperty("type")
    public abstract Builder type(String type);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder type(Optional<String> type);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearType() {
      return type(Optional.empty());
    }

    @JsonProperty("count")
    public abstract Builder count(Integer count);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder count(Optional<Integer> count);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearCount() {
      return count(Optional.empty());
    }

    public abstract GroundingToolCount build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static GroundingToolCount fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, GroundingToolCount.class);
  }
}
