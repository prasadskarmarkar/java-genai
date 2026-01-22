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

/** Function result content representing the result of a function call. */
@AutoValue
@JsonDeserialize(builder = FunctionResultContent.Builder.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("function_result")
public abstract class FunctionResultContent extends JsonSerializable implements InteractionContent {

  /** The unique identifier matching the corresponding FunctionCallContent. */
  @JsonProperty("call_id")
  public abstract Optional<String> id();

  /** The name of the function that was called. */
  @JsonProperty("name")
  public abstract Optional<String> name();

  /** The result returned by the function, as a map of key-value pairs. */
  @JsonProperty("result")
  public abstract Optional<Map<String, Object>> result();

  /** Instantiates a builder for FunctionResultContent. */
  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_FunctionResultContent.Builder();
  }

  /** Creates a builder with the same values as this instance. */
  public abstract Builder toBuilder();

  /** Builder for FunctionResultContent. */
  @AutoValue.Builder
  public abstract static class Builder {
    /** For internal usage. Please use {@code FunctionResultContent.builder()} for instantiation. */
    @JsonCreator
    private static Builder create() {
      return new AutoValue_FunctionResultContent.Builder();
    }

    /**
     * Setter for id.
     *
     * <p>id: The unique identifier matching the corresponding FunctionCallContent.
     */
    @JsonProperty("call_id")
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
     * <p>name: The name of the function that was called.
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
     * Setter for result.
     *
     * <p>result: The result returned by the function.
     */
    @JsonProperty("result")
    public abstract Builder result(Map<String, Object> result);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder result(Optional<Map<String, Object>> result);

    /** Clears the value of result field. */
    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearResult() {
      return result(Optional.empty());
    }

    public abstract FunctionResultContent build();
  }

  /** Deserializes a JSON string to a FunctionResultContent object. */
  @ExcludeFromGeneratedCoverageReport
  public static FunctionResultContent fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, FunctionResultContent.class);
  }

  /** Convenience factory method. */
  @ExcludeFromGeneratedCoverageReport
  public static FunctionResultContent of(String id, String name, Map<String, Object> result) {
    return builder().id(id).name(name).result(result).build();
  }
}
