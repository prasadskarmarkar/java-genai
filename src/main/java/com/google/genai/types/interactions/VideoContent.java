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
import java.util.Optional;

@AutoValue
@JsonDeserialize(builder = VideoContent.Builder.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("video")
public abstract class VideoContent extends JsonSerializable implements InteractionContent {

  @JsonProperty("data")
  public abstract Optional<String> data();

  @JsonProperty("uri")
  public abstract Optional<String> uri();

  @JsonProperty("mime_type")
  public abstract Optional<String> mimeType();

  @JsonProperty("resolution")
  public abstract Optional<String> resolution();

  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_VideoContent.Builder();
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    @JsonCreator
    private static Builder create() {
      return new AutoValue_VideoContent.Builder();
    }

    @JsonProperty("data")
    public abstract Builder data(String data);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder data(Optional<String> data);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearData() {
      return data(Optional.empty());
    }

    @JsonProperty("uri")
    public abstract Builder uri(String uri);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder uri(Optional<String> uri);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearUri() {
      return uri(Optional.empty());
    }

    @JsonProperty("mime_type")
    public abstract Builder mimeType(String mimeType);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder mimeType(Optional<String> mimeType);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearMimeType() {
      return mimeType(Optional.empty());
    }

    @JsonProperty("resolution")
    public abstract Builder resolution(String resolution);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder resolution(Optional<String> resolution);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearResolution() {
      return resolution(Optional.empty());
    }

    public abstract VideoContent build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static VideoContent fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, VideoContent.class);
  }

  @ExcludeFromGeneratedCoverageReport
  public static VideoContent fromData(String data, String mimeType) {
    return builder().data(data).mimeType(mimeType).build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static VideoContent fromUri(String uri, String mimeType) {
    return builder().uri(uri).mimeType(mimeType).build();
  }
}
