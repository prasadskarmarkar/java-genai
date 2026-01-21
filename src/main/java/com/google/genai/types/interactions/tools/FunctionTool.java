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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.genai.JsonSerializable;

import com.google.genai.types.FunctionDeclaration;
import com.google.genai.types.Schema;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Function tool for the Interactions API.
 *
 * <p>Represents a callable function that the model can invoke. Unlike the GenerateContent API where
 * one Tool can contain multiple FunctionDeclarations, in the Interactions API each FunctionTool
 * represents a single function.
 *
 * <p>Example usage with manual declaration:
 *
 * <pre>{@code
 * FunctionTool weatherTool = FunctionTool.builder()
 *     .name("get_weather")
 *     .description("Get the current weather for a location")
 *     .parameters(Schema.builder()
 *         .type("object")
 *         .properties(Map.of(
 *             "location", Schema.builder().type("string").build()))
 *         .required("location")
 *         .build())
 *     .build();
 * }</pre>
 *
 * <p>Example usage with reflection (enables Automatic Function Calling):
 *
 * <pre>{@code
 * Method getCurrentWeather = MyClass.class.getMethod("getCurrentWeather", String.class);
 * FunctionTool weatherTool = FunctionTool.fromMethod(getCurrentWeather);
 * }</pre>
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
@AutoValue
@JsonDeserialize(builder = FunctionTool.Builder.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("function")
public abstract class FunctionTool extends JsonSerializable implements InteractionTool {

  /** The name of the function to call. */
  @JsonProperty("name")
  public abstract Optional<String> name();

  /** A description of what the function does, used by the model to decide when to call it. */
  @JsonProperty("description")
  public abstract Optional<String> description();

  /** The parameters schema for the function in JSON Schema format. */
  @JsonProperty("parameters")
  public abstract Optional<Schema> parameters();

  /**
   * The Java Method instance for Automatic Function Calling (AFC).
   *
   * <p>When set, the SDK can automatically invoke this method when the model requests it. Not
   * serialized to JSON.
   */
  @JsonIgnore
  public abstract Optional<Method> method();

  /** Instantiates a builder for FunctionTool. */
  
  public static Builder builder() {
    return new AutoValue_FunctionTool.Builder();
  }

  /** Creates a builder with the same values as this instance. */
  public abstract Builder toBuilder();

  /** Builder for FunctionTool. */
  @AutoValue.Builder
  public abstract static class Builder {
    /** For internal usage. Please use {@code FunctionTool.builder()} for instantiation. */
    @JsonCreator
    private static Builder create() {
      return new AutoValue_FunctionTool.Builder();
    }

    /**
     * Setter for name.
     *
     * <p>name: The name of the function to call.
     */
    @JsonProperty("name")
    public abstract Builder name(String name);

    
    abstract Builder name(Optional<String> name);

    /** Clears the value of name field. */
    
    @CanIgnoreReturnValue
    public Builder clearName() {
      return name(Optional.empty());
    }

    /**
     * Setter for description.
     *
     * <p>description: A description of what the function does.
     */
    @JsonProperty("description")
    public abstract Builder description(String description);

    
    abstract Builder description(Optional<String> description);

    /** Clears the value of description field. */
    
    @CanIgnoreReturnValue
    public Builder clearDescription() {
      return description(Optional.empty());
    }

    /**
     * Setter for parameters.
     *
     * <p>parameters: The parameters schema for the function.
     */
    @JsonProperty("parameters")
    public abstract Builder parameters(Schema parameters);

    /**
     * Setter for parameters builder.
     *
     * <p>parameters: The parameters schema for the function.
     */
    @CanIgnoreReturnValue
    public Builder parameters(Schema.Builder parametersBuilder) {
      return parameters(parametersBuilder.build());
    }

    
    abstract Builder parameters(Optional<Schema> parameters);

    /** Clears the value of parameters field. */
    
    @CanIgnoreReturnValue
    public Builder clearParameters() {
      return parameters(Optional.empty());
    }

    /**
     * Setter for method.
     *
     * <p>method: The Java Method for AFC. Not serialized to JSON.
     */
    @JsonIgnore
    public abstract Builder method(Method method);

    
    abstract Builder method(Optional<Method> method);

    /** Clears the value of method field. */
    
    @CanIgnoreReturnValue
    public Builder clearMethod() {
      return method(Optional.empty());
    }

    public abstract FunctionTool build();
  }

  /** Deserializes a JSON string to a FunctionTool object. */
  
  public static FunctionTool fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, FunctionTool.class);
  }

  /**
   * Creates a FunctionTool from a Java Method.
   *
   * <p>This enables Automatic Function Calling (AFC) - the SDK will automatically invoke this
   * method when the model requests it.
   *
   * @param method The static method to wrap. Must be a static method.
   * @param orderedParameterNames Optional ordered parameter names. If not provided, parameter names
   *     will be retrieved via reflection.
   * @return A FunctionTool that wraps the method.
   */
  public static FunctionTool fromMethod(Method method, String... orderedParameterNames) {
    return fromMethod("", method, orderedParameterNames);
  }

  /**
   * Creates a FunctionTool from a Java Method with a description.
   *
   * <p>This enables Automatic Function Calling (AFC) - the SDK will automatically invoke this
   * method when the model requests it.
   *
   * @param functionDescription Description of the function for the model.
   * @param method The static method to wrap. Must be a static method.
   * @param orderedParameterNames Optional ordered parameter names. If not provided, parameter names
   *     will be retrieved via reflection.
   * @return A FunctionTool that wraps the method.
   */
  public static FunctionTool fromMethod(
      String functionDescription, Method method, String... orderedParameterNames) {
    FunctionDeclaration fd =
        FunctionDeclaration.fromMethod(functionDescription, method, orderedParameterNames);

    Builder builder = FunctionTool.builder().method(method);

    if (fd.name().isPresent()) {
      builder.name(fd.name().get());
    }
    if (fd.description().isPresent() && !fd.description().get().isEmpty()) {
      builder.description(fd.description().get());
    }
    if (fd.parameters().isPresent()) {
      builder.parameters(fd.parameters().get());
    }

    return builder.build();
  }
}
