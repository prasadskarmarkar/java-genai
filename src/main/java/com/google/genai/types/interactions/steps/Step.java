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

package com.google.genai.types.interactions.steps;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Base interface for step types in an Interaction.
 *
 * <p>Steps represent the sequence of actions taken during an interaction. Each step has a
 * discriminating {@code type} field.
 *
 * <p>This follows Jackson best practices for polymorphism:
 *
 * <ul>
 *   <li>{@code @JsonTypeInfo} on the base type with property "type"
 *   <li>Explicit {@code @JsonSubTypes} registration
 *   <li>Jackson handles type discrimination via annotations
 * </ul>
 *
 * <p>The Interactions API is available in both Vertex AI and Gemini API.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = UserInputStep.class, name = "user_input"),
  @JsonSubTypes.Type(value = ModelOutputStep.class, name = "model_output"),
  @JsonSubTypes.Type(value = ThoughtStep.class, name = "thought"),
  @JsonSubTypes.Type(value = FunctionCallStep.class, name = "function_call"),
  @JsonSubTypes.Type(value = FunctionResultStep.class, name = "function_result"),
  @JsonSubTypes.Type(value = CodeExecutionCallStep.class, name = "code_execution_call"),
  @JsonSubTypes.Type(value = CodeExecutionResultStep.class, name = "code_execution_result"),
  @JsonSubTypes.Type(value = GoogleSearchCallStep.class, name = "google_search_call"),
  @JsonSubTypes.Type(value = GoogleSearchResultStep.class, name = "google_search_result"),
  @JsonSubTypes.Type(value = UrlContextCallStep.class, name = "url_context_call"),
  @JsonSubTypes.Type(value = UrlContextResultStep.class, name = "url_context_result"),
  @JsonSubTypes.Type(value = FileSearchCallStep.class, name = "file_search_call"),
  @JsonSubTypes.Type(value = FileSearchResultStep.class, name = "file_search_result"),
  @JsonSubTypes.Type(value = McpServerToolCallStep.class, name = "mcp_server_tool_call"),
  @JsonSubTypes.Type(value = McpServerToolResultStep.class, name = "mcp_server_tool_result"),
  @JsonSubTypes.Type(value = GoogleMapsCallStep.class, name = "google_maps_call"),
  @JsonSubTypes.Type(value = GoogleMapsResultStep.class, name = "google_maps_result"),
})
public interface Step {}
