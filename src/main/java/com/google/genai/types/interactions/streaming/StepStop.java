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

package com.google.genai.types.interactions.streaming;

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

/** Event emitted when a step completes in a streaming interaction. */
@AutoValue
@JsonDeserialize(builder = StepStop.Builder.class)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "event_type")
@JsonTypeName("step.stop")
public abstract class StepStop extends JsonSerializable implements InteractionSseEvent {

  @Override
  @JsonProperty("event_id")
  public abstract Optional<String> eventId();

  @JsonProperty("index")
  public abstract Optional<Integer> index();

  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_StepStop.Builder();
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    @JsonCreator
    private static Builder create() {
      return new AutoValue_StepStop.Builder();
    }

    @JsonProperty("event_id")
    public abstract Builder eventId(String eventId);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder eventId(Optional<String> eventId);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearEventId() {
      return eventId(Optional.empty());
    }

    @JsonProperty("index")
    public abstract Builder index(Integer index);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder index(Optional<Integer> index);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearIndex() {
      return index(Optional.empty());
    }

    public abstract StepStop build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static StepStop fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, StepStop.class);
  }
}
