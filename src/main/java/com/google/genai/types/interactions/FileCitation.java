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

/** A file citation annotation for model-generated content. */
@AutoValue
@JsonDeserialize(builder = FileCitation.Builder.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("file_citation")
public abstract class FileCitation extends JsonSerializable implements Annotation {

  @JsonProperty("document_uri")
  public abstract Optional<String> documentUri();

  @JsonProperty("file_name")
  public abstract Optional<String> fileName();

  @JsonProperty("source")
  public abstract Optional<String> source();

  @JsonProperty("page_number")
  public abstract Optional<Integer> pageNumber();

  @JsonProperty("media_id")
  public abstract Optional<String> mediaId();

  @JsonProperty("start_index")
  public abstract Optional<Integer> startIndex();

  @JsonProperty("end_index")
  public abstract Optional<Integer> endIndex();

  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_FileCitation.Builder();
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    @JsonCreator
    private static Builder create() {
      return new AutoValue_FileCitation.Builder();
    }

    @JsonProperty("document_uri")
    public abstract Builder documentUri(String documentUri);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder documentUri(Optional<String> documentUri);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearDocumentUri() {
      return documentUri(Optional.empty());
    }

    @JsonProperty("file_name")
    public abstract Builder fileName(String fileName);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder fileName(Optional<String> fileName);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearFileName() {
      return fileName(Optional.empty());
    }

    @JsonProperty("source")
    public abstract Builder source(String source);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder source(Optional<String> source);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearSource() {
      return source(Optional.empty());
    }

    @JsonProperty("page_number")
    public abstract Builder pageNumber(Integer pageNumber);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder pageNumber(Optional<Integer> pageNumber);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearPageNumber() {
      return pageNumber(Optional.empty());
    }

    @JsonProperty("media_id")
    public abstract Builder mediaId(String mediaId);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder mediaId(Optional<String> mediaId);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearMediaId() {
      return mediaId(Optional.empty());
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

    public abstract FileCitation build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static FileCitation fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, FileCitation.class);
  }
}
