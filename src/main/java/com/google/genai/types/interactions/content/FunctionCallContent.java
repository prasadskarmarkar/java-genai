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
import java.util.Map;
import java.util.Optional;

/** Function call content representing a request from the model to call a function. */
@AutoValue
@JsonDeserialize(builder = FunctionCallContent.Builder.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("function_call")
public abstract class FunctionCallContent extends JsonSerializable implements InteractionContent {

  /** The unique identifier for this function call. Used to match with FunctionResultContent. */
  @JsonProperty("id")
  public abstract Optional<String> id();

  /** The name of the function to call. */
  @JsonProperty("name")
  public abstract Optional<String> name();

  /** The arguments to pass to the function, as a map of parameter names to values. */
  @JsonProperty("arguments")
  public abstract Optional<Map<String, Object>> arguments();

  /** Instantiates a builder for FunctionCallContent. */
  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_FunctionCallContent.Builder();
  }

  /** Creates a builder with the same values as this instance. */
  public abstract Builder toBuilder();

  /** Builder for FunctionCallContent. */
  @AutoValue.Builder
  public abstract static class Builder {
    /** For internal usage. Please use {@code FunctionCallContent.builder()} for instantiation. */
    @JsonCreator
    private static Builder create() {
      return new AutoValue_FunctionCallContent.Builder();
    }

    /**
     * Setter for id.
     *
     * <p>id: The unique identifier for this function call.
     */
    @JsonProperty("id")
    public abstract Builder id(String id);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder id(Optional<String> id);

    /** Clears the value of id field. */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearId() {
      return id(Optional.empty());
    }

    /**
     * Setter for name.
     *
     * <p>name: The name of the function to call.
     */
    @JsonProperty("name")
    public abstract Builder name(String name);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder name(Optional<String> name);

    /** Clears the value of name field. */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearName() {
      return name(Optional.empty());
    }

    /**
     * Setter for arguments.
     *
     * <p>arguments: The arguments to pass to the function.
     */
    @JsonProperty("arguments")
    public abstract Builder arguments(Map<String, Object> arguments);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder arguments(Optional<Map<String, Object>> arguments);

    /** Clears the value of arguments field. */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearArguments() {
      return arguments(Optional.empty());
    }

    public abstract FunctionCallContent build();
  }

  /** Deserializes a JSON string to a FunctionCallContent object. */
  @ExcludeFromGeneratedCoverageReport
  public static FunctionCallContent fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, FunctionCallContent.class);
  }

  /** Convenience factory method. */
  @ExcludeFromGeneratedCoverageReport
  public static FunctionCallContent of(String id, String name, Map<String, Object> arguments) {
    return builder().id(id).name(name).arguments(arguments).build();
  }
}
