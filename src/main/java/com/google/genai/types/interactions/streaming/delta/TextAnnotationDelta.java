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
import com.google.genai.types.interactions.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/** Incremental text annotation delta for streaming (carries citation annotations). */
@AutoValue
@JsonDeserialize(builder = TextAnnotationDelta.Builder.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("text_annotation_delta")
public abstract class TextAnnotationDelta extends JsonSerializable implements Delta {

  @JsonProperty("annotations")
  public abstract Optional<List<Annotation>> annotations();

  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_TextAnnotationDelta.Builder();
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    @JsonCreator
    private static Builder create() {
      return new AutoValue_TextAnnotationDelta.Builder();
    }

    @JsonProperty("annotations")
    public abstract Builder annotations(List<Annotation> annotations);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder annotations(Optional<List<Annotation>> annotations);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearAnnotations() {
      return annotations(Optional.empty());
    }

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder annotations(Annotation... annotations) {
      return annotations(Arrays.asList(annotations));
    }

    public abstract TextAnnotationDelta build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static TextAnnotationDelta fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, TextAnnotationDelta.class);
  }
}
