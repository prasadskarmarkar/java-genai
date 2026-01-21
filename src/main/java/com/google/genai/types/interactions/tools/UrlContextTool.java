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
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.genai.JsonSerializable;


/**
 * URL context tool for the Interactions API.
 *
 * <p>Enables the model to retrieve and use context from URLs.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * UrlContextTool urlTool = UrlContextTool.builder().build();
 * }</pre>
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
@AutoValue
@JsonDeserialize(builder = UrlContextTool.Builder.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("url_context")
public abstract class UrlContextTool extends JsonSerializable implements InteractionTool {

  /** Instantiates a builder for UrlContextTool. */
  
  public static Builder builder() {
    return new AutoValue_UrlContextTool.Builder();
  }

  /** Creates a builder with the same values as this instance. */
  public abstract Builder toBuilder();

  /** Builder for UrlContextTool. */
  @AutoValue.Builder
  public abstract static class Builder {
    /** For internal usage. Please use {@code UrlContextTool.builder()} for instantiation. */
    @JsonCreator
    private static Builder create() {
      return new AutoValue_UrlContextTool.Builder();
    }

    public abstract UrlContextTool build();
  }

  /** Deserializes a JSON string to a UrlContextTool object. */
  
  public static UrlContextTool fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, UrlContextTool.class);
  }
}
