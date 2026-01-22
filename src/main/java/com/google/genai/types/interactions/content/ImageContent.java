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

package com.google.genai.types.interactions.content;

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

/**
 * Image content for interactions.
 *
 * <p>Represents image data that can be included in interaction inputs or outputs.
 * Images can be provided either as base64-encoded data or as a URI.
 *
 * <p>Example usage with data:
 *
 * <pre>{@code
 * ImageContent image = ImageContent.fromData(base64Data, "image/png");
 * }</pre>
 *
 * <p>Example usage with URI:
 *
 * <pre>{@code
 * ImageContent image = ImageContent.fromUri("https://example.com/image.png", "image/png");
 * }</pre>
 */
@AutoValue
@JsonDeserialize(builder = ImageContent.Builder.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("image")
public abstract class ImageContent extends JsonSerializable
    implements Content, ThoughtSummaryContent {

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
    return new AutoValue_ImageContent.Builder();
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    @JsonCreator
    private static Builder create() {
      return new AutoValue_ImageContent.Builder();
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

    public abstract ImageContent build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static ImageContent fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, ImageContent.class);
  }

  @ExcludeFromGeneratedCoverageReport
  public static ImageContent fromData(String data, String mimeType) {
    return builder().data(data).mimeType(mimeType).build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static ImageContent fromUri(String uri, String mimeType) {
    return builder().uri(uri).mimeType(mimeType).build();
  }
}
