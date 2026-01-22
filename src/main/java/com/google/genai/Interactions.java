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

// Auto-generated code. Do not edit.

package com.google.genai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.genai.Common.BuiltRequest;
import com.google.genai.errors.GenAiIOException;
import com.google.genai.types.CancelInteractionConfig;
import com.google.genai.types.CancelInteractionParameters;
import com.google.genai.types.CreateInteractionConfig;
import com.google.genai.types.CreateInteractionParameters;
import com.google.genai.types.DeleteInteractionConfig;
import com.google.genai.types.DeleteInteractionParameters;
import com.google.genai.types.DeleteInteractionResponse;
import com.google.genai.types.GetInteractionConfig;
import com.google.genai.types.GetInteractionParameters;
import com.google.genai.types.HttpOptions;
import com.google.genai.types.HttpResponse;
import com.google.genai.types.Interaction;
import com.google.genai.types.interactions.content.FunctionCallContent;
import com.google.genai.types.interactions.content.FunctionResultContent;
import com.google.genai.types.interactions.InteractionInput;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import okhttp3.Headers;
import okhttp3.ResponseBody;

/**
 * Provides methods for managing interactions. Instantiating this class is not required. After
 * instantiating a {@link Client}, access methods through `client.interactions.methodName(...)`
 * directly.
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class Interactions {

  final ApiClient apiClient;

  public Interactions(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  @ExcludeFromGeneratedCoverageReport
  ObjectNode createInteractionConfigToMldev(JsonNode fromObject, ObjectNode parentObject) {
    ObjectNode toObject = JsonSerializable.objectMapper().createObjectNode();

    if (Common.getValueByPath(fromObject, new String[] {"input"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"input"},
          Common.getValueByPath(fromObject, new String[] {"input"}));
    }

    if (Common.getValueByPath(fromObject, new String[] {"model"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"model"},
          Transformers.tModel(
              this.apiClient, Common.getValueByPath(fromObject, new String[] {"model"})));
    }

    if (Common.getValueByPath(fromObject, new String[] {"agent"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"agent"},
          Common.getValueByPath(fromObject, new String[] {"agent"}));
    }

    if (Common.getValueByPath(fromObject, new String[] {"background"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"background"},
          Common.getValueByPath(fromObject, new String[] {"background"}));
    }

    if (Common.getValueByPath(fromObject, new String[] {"generationConfig"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"generationConfig"},
          Common.getValueByPath(fromObject, new String[] {"generationConfig"}));
    }

    if (Common.getValueByPath(fromObject, new String[] {"agentConfig"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"agentConfig"},
          Common.getValueByPath(fromObject, new String[] {"agentConfig"}));
    }

    if (Common.getValueByPath(fromObject, new String[] {"previousInteractionId"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"previousInteractionId"},
          Common.getValueByPath(fromObject, new String[] {"previousInteractionId"}));
    }

    if (Common.getValueByPath(fromObject, new String[] {"responseFormat"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"responseFormat"},
          Common.getValueByPath(fromObject, new String[] {"responseFormat"}));
    }

    if (Common.getValueByPath(fromObject, new String[] {"responseMimeType"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"responseMimeType"},
          Common.getValueByPath(fromObject, new String[] {"responseMimeType"}));
    }

    if (Common.getValueByPath(fromObject, new String[] {"responseModalities"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"responseModalities"},
          Common.getValueByPath(fromObject, new String[] {"responseModalities"}));
    }

    if (Common.getValueByPath(fromObject, new String[] {"store"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"store"},
          Common.getValueByPath(fromObject, new String[] {"store"}));
    }

    if (Common.getValueByPath(fromObject, new String[] {"systemInstruction"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"systemInstruction"},
          Transformers.tContent(
              Common.getValueByPath(fromObject, new String[] {"systemInstruction"})));
    }

    // InteractionTool types serialize directly with their type discriminator
    if (Common.getValueByPath(fromObject, new String[] {"tools"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"tools"},
          Common.getValueByPath(fromObject, new String[] {"tools"}));
    }

    return toObject;
  }

  @ExcludeFromGeneratedCoverageReport
  ObjectNode createInteractionConfigToVertex(JsonNode fromObject, ObjectNode parentObject) {
    ObjectNode toObject = JsonSerializable.objectMapper().createObjectNode();

    if (Common.getValueByPath(fromObject, new String[] {"input"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"input"},
          Common.getValueByPath(fromObject, new String[] {"input"}));
    }

    if (Common.getValueByPath(fromObject, new String[] {"model"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"model"},
          Transformers.tModel(
              this.apiClient, Common.getValueByPath(fromObject, new String[] {"model"})));
    }

    if (Common.getValueByPath(fromObject, new String[] {"agent"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"agent"},
          Common.getValueByPath(fromObject, new String[] {"agent"}));
    }

    if (Common.getValueByPath(fromObject, new String[] {"background"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"background"},
          Common.getValueByPath(fromObject, new String[] {"background"}));
    }

    if (Common.getValueByPath(fromObject, new String[] {"generationConfig"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"generationConfig"},
          Common.getValueByPath(fromObject, new String[] {"generationConfig"}));
    }

    if (Common.getValueByPath(fromObject, new String[] {"agentConfig"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"agentConfig"},
          Common.getValueByPath(fromObject, new String[] {"agentConfig"}));
    }

    if (Common.getValueByPath(fromObject, new String[] {"previousInteractionId"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"previousInteractionId"},
          Common.getValueByPath(fromObject, new String[] {"previousInteractionId"}));
    }

    if (Common.getValueByPath(fromObject, new String[] {"responseFormat"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"responseFormat"},
          Common.getValueByPath(fromObject, new String[] {"responseFormat"}));
    }

    if (Common.getValueByPath(fromObject, new String[] {"responseMimeType"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"responseMimeType"},
          Common.getValueByPath(fromObject, new String[] {"responseMimeType"}));
    }

    if (Common.getValueByPath(fromObject, new String[] {"responseModalities"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"responseModalities"},
          Common.getValueByPath(fromObject, new String[] {"responseModalities"}));
    }

    if (Common.getValueByPath(fromObject, new String[] {"store"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"store"},
          Common.getValueByPath(fromObject, new String[] {"store"}));
    }

    if (Common.getValueByPath(fromObject, new String[] {"systemInstruction"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"systemInstruction"},
          Transformers.tContent(
              Common.getValueByPath(fromObject, new String[] {"systemInstruction"})));
    }

    // InteractionTool types serialize directly with their type discriminator
    if (Common.getValueByPath(fromObject, new String[] {"tools"}) != null) {
      Common.setValueByPath(
          parentObject,
          new String[] {"tools"},
          Common.getValueByPath(fromObject, new String[] {"tools"}));
    }

    return toObject;
  }

  @ExcludeFromGeneratedCoverageReport
  ObjectNode createInteractionParametersToMldev(
      ApiClient apiClient, JsonNode fromObject, ObjectNode parentObject) {
    ObjectNode toObject = JsonSerializable.objectMapper().createObjectNode();

    if (Common.getValueByPath(fromObject, new String[] {"config"}) != null) {
      JsonNode unused =
          createInteractionConfigToMldev(
              JsonSerializable.toJsonNode(
                  Common.getValueByPath(fromObject, new String[] {"config"})),
              toObject);
    }

    return toObject;
  }

  @ExcludeFromGeneratedCoverageReport
  ObjectNode createInteractionParametersToVertex(
      ApiClient apiClient, JsonNode fromObject, ObjectNode parentObject) {
    ObjectNode toObject = JsonSerializable.objectMapper().createObjectNode();

    if (Common.getValueByPath(fromObject, new String[] {"config"}) != null) {
      JsonNode unused =
          createInteractionConfigToVertex(
              JsonSerializable.toJsonNode(
                  Common.getValueByPath(fromObject, new String[] {"config"})),
              toObject);
    }

    return toObject;
  }

  /** A shared buildRequest method for both sync and async methods. */
  BuiltRequest buildRequestForCreate(CreateInteractionConfig config) {
    // Validation: exactly one of model or agent must be specified
    boolean hasModel = config.model().isPresent() && !config.model().get().isEmpty();
    boolean hasAgent = config.agent().isPresent() && !config.agent().get().isEmpty();

    if (hasModel && hasAgent) {
      throw new IllegalArgumentException(
          "Cannot specify both 'model' and 'agent'. Please provide only one.");
    }

    if (!hasModel && !hasAgent) {
      throw new IllegalArgumentException(
          "Must specify either 'model' or 'agent' in CreateInteractionConfig.");
    }

    // Validation: agentConfig only with agent, generationConfig only with model
    if (hasAgent && config.generationConfig().isPresent()) {
      throw new IllegalArgumentException(
          "Cannot use 'generationConfig' with agent-based interactions. "
              + "Use 'agentConfig' instead.");
    }

    if (hasModel && config.agentConfig().isPresent()) {
      throw new IllegalArgumentException(
          "Cannot use 'agentConfig' with model-based interactions. "
              + "Use 'generationConfig' instead.");
    }

    CreateInteractionParameters.Builder parameterBuilder =
        CreateInteractionParameters.builder();

    if (!Common.isZero(config)) {
      parameterBuilder.config(config);
    }
    JsonNode parameterNode = JsonSerializable.toJsonNode(parameterBuilder.build());

    ObjectNode body;
    String path;
    if (this.apiClient.vertexAI()) {
      body = createInteractionParametersToVertex(this.apiClient, parameterNode, null);
      path = "interactions";
    } else {
      body = createInteractionParametersToMldev(this.apiClient, parameterNode, null);
      path = "interactions";
    }

    JsonNode queryParams = body.get("_query");
    if (queryParams != null) {
      body.remove("_query");
      path = String.format("%s?%s", path, Common.urlEncode((ObjectNode) queryParams));
    }

    Optional<HttpOptions> requestHttpOptions = Optional.empty();
    if (config != null) {
      requestHttpOptions = config.httpOptions();
    }

    return new BuiltRequest(path, JsonSerializable.toJsonString(body), requestHttpOptions);
  }

  /** A shared processResponse function for both sync and async methods. */
  Interaction processResponseForCreate(ApiResponse response, CreateInteractionConfig config) {
    ResponseBody responseBody = response.getBody();
    String responseString;
    try {
      responseString = responseBody.string();
    } catch (IOException e) {
      throw new GenAiIOException("Failed to read HTTP response.", e);
    }

    JsonNode responseNode = JsonSerializable.stringToJsonNode(responseString);

    return JsonSerializable.fromJsonNode(responseNode, Interaction.class);
  }

  // ===== GET INTERACTION METHODS =====

  @ExcludeFromGeneratedCoverageReport
  ObjectNode getInteractionParametersToMldev(
      ApiClient apiClient, JsonNode fromObject, ObjectNode parentObject) {
    ObjectNode toObject = JsonSerializable.objectMapper().createObjectNode();

    if (Common.getValueByPath(fromObject, new String[] {"id"}) != null) {
      Common.setValueByPath(
          toObject,
          new String[] {"_url", "id"},
          Transformers.tInteractionId(
              this.apiClient, Common.getValueByPath(fromObject, new String[] {"id"})));
    }

    return toObject;
  }

  @ExcludeFromGeneratedCoverageReport
  ObjectNode getInteractionParametersToVertex(
      ApiClient apiClient, JsonNode fromObject, ObjectNode parentObject) {
    ObjectNode toObject = JsonSerializable.objectMapper().createObjectNode();

    if (Common.getValueByPath(fromObject, new String[] {"id"}) != null) {
      Common.setValueByPath(
          toObject,
          new String[] {"_url", "id"},
          Transformers.tInteractionId(
              this.apiClient, Common.getValueByPath(fromObject, new String[] {"id"})));
    }

    return toObject;
  }

  /** A shared buildRequest method for both sync and async methods. */
  BuiltRequest buildRequestForGet(String id, GetInteractionConfig config) {
    // Validation: id must be non-empty
    if (id == null || id.isEmpty()) {
      throw new IllegalArgumentException("Interaction ID must not be empty");
    }

    GetInteractionParameters.Builder parameterBuilder = GetInteractionParameters.builder();

    if (!Common.isZero(id)) {
      parameterBuilder.id(id);
    }
    if (!Common.isZero(config)) {
      parameterBuilder.config(config);
    }
    JsonNode parameterNode = JsonSerializable.toJsonNode(parameterBuilder.build());

    ObjectNode body;
    String path;
    if (this.apiClient.vertexAI()) {
      body = getInteractionParametersToVertex(this.apiClient, parameterNode, null);
      path = Common.formatMap("{id}", body.get("_url"));
    } else {
      body = getInteractionParametersToMldev(this.apiClient, parameterNode, null);
      if (body.get("_url") != null) {
        path = Common.formatMap("{id}", body.get("_url"));
      } else {
        path = "{id}";
      }
    }
    body.remove("_url");

    JsonNode queryParams = body.get("_query");
    if (queryParams != null) {
      body.remove("_query");
      path = String.format("%s?%s", path, Common.urlEncode((ObjectNode) queryParams));
    }

    Optional<HttpOptions> requestHttpOptions = Optional.empty();
    if (config != null) {
      requestHttpOptions = config.httpOptions();
    }

    return new BuiltRequest(path, JsonSerializable.toJsonString(body), requestHttpOptions);
  }

  /** A shared processResponse function for both sync and async methods. */
  Interaction processResponseForGet(ApiResponse response, GetInteractionConfig config) {
    ResponseBody responseBody = response.getBody();
    String responseString;
    try {
      responseString = responseBody.string();
    } catch (IOException e) {
      throw new GenAiIOException("Failed to read HTTP response.", e);
    }

    JsonNode responseNode = JsonSerializable.stringToJsonNode(responseString);

    return JsonSerializable.fromJsonNode(responseNode, Interaction.class);
  }

  // ===== CANCEL INTERACTION METHODS =====

  @ExcludeFromGeneratedCoverageReport
  ObjectNode cancelInteractionParametersToMldev(
      ApiClient apiClient, JsonNode fromObject, ObjectNode parentObject) {
    ObjectNode toObject = JsonSerializable.objectMapper().createObjectNode();

    if (Common.getValueByPath(fromObject, new String[] {"id"}) != null) {
      Common.setValueByPath(
          toObject,
          new String[] {"_url", "id"},
          Transformers.tInteractionId(
              this.apiClient, Common.getValueByPath(fromObject, new String[] {"id"})));
    }

    return toObject;
  }

  @ExcludeFromGeneratedCoverageReport
  ObjectNode cancelInteractionParametersToVertex(
      ApiClient apiClient, JsonNode fromObject, ObjectNode parentObject) {
    ObjectNode toObject = JsonSerializable.objectMapper().createObjectNode();

    if (Common.getValueByPath(fromObject, new String[] {"id"}) != null) {
      Common.setValueByPath(
          toObject,
          new String[] {"_url", "id"},
          Transformers.tInteractionId(
              this.apiClient, Common.getValueByPath(fromObject, new String[] {"id"})));
    }

    return toObject;
  }

  /** A shared buildRequest method for both sync and async methods. */
  BuiltRequest buildRequestForCancel(String id, CancelInteractionConfig config) {
    // Validation: id must be non-empty
    if (id == null || id.isEmpty()) {
      throw new IllegalArgumentException("Interaction ID must not be empty");
    }

    CancelInteractionParameters.Builder parameterBuilder = CancelInteractionParameters.builder();

    if (!Common.isZero(id)) {
      parameterBuilder.id(id);
    }
    if (!Common.isZero(config)) {
      parameterBuilder.config(config);
    }
    JsonNode parameterNode = JsonSerializable.toJsonNode(parameterBuilder.build());

    ObjectNode body;
    String path;
    if (this.apiClient.vertexAI()) {
      body = cancelInteractionParametersToVertex(this.apiClient, parameterNode, null);
      // Cancel uses POST to /interactions/{id}/cancel
      path = Common.formatMap("{id}/cancel", body.get("_url"));
    } else {
      body = cancelInteractionParametersToMldev(this.apiClient, parameterNode, null);
      if (body.get("_url") != null) {
        // Cancel uses POST to /interactions/{id}/cancel
        path = Common.formatMap("{id}/cancel", body.get("_url"));
      } else {
        path = "{id}/cancel";
      }
    }
    body.remove("_url");

    JsonNode queryParams = body.get("_query");
    if (queryParams != null) {
      body.remove("_query");
      path = String.format("%s?%s", path, Common.urlEncode((ObjectNode) queryParams));
    }

    Optional<HttpOptions> requestHttpOptions = Optional.empty();
    if (config != null) {
      requestHttpOptions = config.httpOptions();
    }

    return new BuiltRequest(path, JsonSerializable.toJsonString(body), requestHttpOptions);
  }

  /** A shared processResponse function for both sync and async methods. */
  Interaction processResponseForCancel(ApiResponse response, CancelInteractionConfig config) {
    ResponseBody responseBody = response.getBody();
    String responseString;
    try {
      responseString = responseBody.string();
    } catch (IOException e) {
      throw new GenAiIOException("Failed to read HTTP response.", e);
    }

    JsonNode responseNode = JsonSerializable.stringToJsonNode(responseString);

    return JsonSerializable.fromJsonNode(responseNode, Interaction.class);
  }

  // ===== DELETE INTERACTION METHODS =====

  @ExcludeFromGeneratedCoverageReport
  ObjectNode deleteInteractionParametersToMldev(
      ApiClient apiClient, JsonNode fromObject, ObjectNode parentObject) {
    ObjectNode toObject = JsonSerializable.objectMapper().createObjectNode();

    if (Common.getValueByPath(fromObject, new String[] {"id"}) != null) {
      Common.setValueByPath(
          toObject,
          new String[] {"_url", "id"},
          Transformers.tInteractionId(
              this.apiClient, Common.getValueByPath(fromObject, new String[] {"id"})));
    }

    return toObject;
  }

  @ExcludeFromGeneratedCoverageReport
  ObjectNode deleteInteractionParametersToVertex(
      ApiClient apiClient, JsonNode fromObject, ObjectNode parentObject) {
    ObjectNode toObject = JsonSerializable.objectMapper().createObjectNode();

    if (Common.getValueByPath(fromObject, new String[] {"id"}) != null) {
      Common.setValueByPath(
          toObject,
          new String[] {"_url", "id"},
          Transformers.tInteractionId(
              this.apiClient, Common.getValueByPath(fromObject, new String[] {"id"})));
    }

    return toObject;
  }

  @ExcludeFromGeneratedCoverageReport
  ObjectNode deleteInteractionResponseFromMldev(JsonNode fromObject, ObjectNode parentObject) {
    ObjectNode toObject = JsonSerializable.objectMapper().createObjectNode();

    if (Common.getValueByPath(fromObject, new String[] {"sdkHttpResponse"}) != null) {
      Common.setValueByPath(
          toObject,
          new String[] {"sdkHttpResponse"},
          Common.getValueByPath(fromObject, new String[] {"sdkHttpResponse"}));
    }

    return toObject;
  }

  @ExcludeFromGeneratedCoverageReport
  ObjectNode deleteInteractionResponseFromVertex(JsonNode fromObject, ObjectNode parentObject) {
    ObjectNode toObject = JsonSerializable.objectMapper().createObjectNode();

    if (Common.getValueByPath(fromObject, new String[] {"sdkHttpResponse"}) != null) {
      Common.setValueByPath(
          toObject,
          new String[] {"sdkHttpResponse"},
          Common.getValueByPath(fromObject, new String[] {"sdkHttpResponse"}));
    }

    return toObject;
  }

  /** A shared buildRequest method for both sync and async methods. */
  BuiltRequest buildRequestForDelete(String id, DeleteInteractionConfig config) {
    // Validation: id must be non-empty
    if (id == null || id.isEmpty()) {
      throw new IllegalArgumentException("Interaction ID must not be empty");
    }

    DeleteInteractionParameters.Builder parameterBuilder = DeleteInteractionParameters.builder();

    if (!Common.isZero(id)) {
      parameterBuilder.id(id);
    }
    if (!Common.isZero(config)) {
      parameterBuilder.config(config);
    }
    JsonNode parameterNode = JsonSerializable.toJsonNode(parameterBuilder.build());

    ObjectNode body;
    String path;
    if (this.apiClient.vertexAI()) {
      body = deleteInteractionParametersToVertex(this.apiClient, parameterNode, null);
      path = Common.formatMap("{id}", body.get("_url"));
    } else {
      body = deleteInteractionParametersToMldev(this.apiClient, parameterNode, null);
      if (body.get("_url") != null) {
        path = Common.formatMap("{id}", body.get("_url"));
      } else {
        path = "{id}";
      }
    }
    body.remove("_url");

    JsonNode queryParams = body.get("_query");
    if (queryParams != null) {
      body.remove("_query");
      path = String.format("%s?%s", path, Common.urlEncode((ObjectNode) queryParams));
    }

    Optional<HttpOptions> requestHttpOptions = Optional.empty();
    if (config != null) {
      requestHttpOptions = config.httpOptions();
    }

    return new BuiltRequest(path, JsonSerializable.toJsonString(body), requestHttpOptions);
  }

  /** A shared processResponse function for both sync and async methods. */
  DeleteInteractionResponse processResponseForDelete(
      ApiResponse response, DeleteInteractionConfig config) {
    ResponseBody responseBody = response.getBody();
    String responseString;
    try {
      responseString = responseBody.string();
    } catch (IOException e) {
      throw new GenAiIOException("Failed to read HTTP response.", e);
    }

    JsonNode responseNode = JsonSerializable.stringToJsonNode(responseString);

    if (this.apiClient.vertexAI()) {
      responseNode = deleteInteractionResponseFromVertex(responseNode, null);
    } else {
      responseNode = deleteInteractionResponseFromMldev(responseNode, null);
    }

    DeleteInteractionResponse sdkResponse =
        JsonSerializable.fromJsonNode(responseNode, DeleteInteractionResponse.class);
    Headers responseHeaders = response.getHeaders();
    if (responseHeaders == null) {
      return sdkResponse;
    }
    Map<String, String> headers = new HashMap<>();
    for (String headerName : responseHeaders.names()) {
      headers.put(headerName, responseHeaders.get(headerName));
    }
    return sdkResponse.toBuilder().sdkHttpResponse(HttpResponse.builder().headers(headers)).build();
  }

  /**
   * Creates a new interaction with the specified configuration.
   *
   * <p>Either {@code model} or {@code agent} must be specified in the config, but not both.
   *
   * <p>If the config contains any {@code FunctionTool} with a callable method (created via {@code
   * FunctionTool.fromMethod()}), Automatic Function Calling (AFC) will be enabled. The SDK will
   * automatically execute the function when the model requests it and continue the conversation
   * until a final response is generated.
   *
   * <p>Example usage for model-based interaction:
   *
   * <pre>{@code
   * CreateInteractionConfig config = CreateInteractionConfig.builder()
   *     .model("gemini-2.0-flash-exp")
   *     .input("What is the capital of France?")
   *     .build();
   * Interaction response = client.interactions.create(config);
   * }</pre>
   *
   * <p>Example usage for agent-based interaction:
   *
   * <pre>{@code
   * CreateInteractionConfig config = CreateInteractionConfig.builder()
   *     .agent("deep-research-pro-preview-12-2025")
   *     .input("Research the history of quantum computing")
   *     .build();
   * Interaction response = client.interactions.create(config);
   * }</pre>
   *
   * <p>Example usage with Automatic Function Calling:
   *
   * <pre>{@code
   * Method getWeather = MyClass.class.getMethod("getWeather", String.class);
   * CreateInteractionConfig config = CreateInteractionConfig.builder()
   *     .model("gemini-2.0-flash-exp")
   *     .input("What's the weather in Paris?")
   *     .tools(FunctionTool.fromMethod(getWeather))
   *     .build();
   * Interaction response = client.interactions.create(config);
   * // The SDK automatically calls getWeather() and continues the conversation
   * }</pre>
   *
   * <p>Note: The Interactions API is in beta and subject to change.
   *
   * @param config The configuration for creating the interaction
   * @return The created Interaction with outputs
   * @throws IllegalArgumentException if both model and agent are specified, or neither is specified
   * @throws UnsupportedOperationException if using Vertex AI (not yet supported)
   * @throws GenAiIOException if the API request fails
   */
  public Interaction create(CreateInteractionConfig config) {
    // Check if AFC is enabled (any FunctionTool with a callable method)
    if (InteractionsAfcUtil.hasCallableTool(config)) {
      return createWithAfc(config);
    }
    return createWithoutAfc(config);
  }

  /**
   * Creates an interaction without AFC - a single API call.
   *
   * @param config The configuration for creating the interaction
   * @return The created Interaction
   */
  Interaction createWithoutAfc(CreateInteractionConfig config) {
    BuiltRequest builtRequest = buildRequestForCreate(config);

    try (ApiResponse response =
        this.apiClient.request(
            "post", builtRequest.path(), builtRequest.body(), builtRequest.httpOptions())) {
      return processResponseForCreate(response, config);
    }
  }

  /**
   * Creates an interaction with AFC - handles function call loop automatically.
   *
   * @param config The configuration for creating the interaction
   * @return The final Interaction with AFC history
   */
  private Interaction createWithAfc(CreateInteractionConfig config) {
    ImmutableMap<String, Method> functionMap = InteractionsAfcUtil.getFunctionMap(config);
    List<Interaction> afcHistory = new ArrayList<>();
    String previousInteractionId = null;
    CreateInteractionConfig currentConfig = config;
    int maxRemoteCalls = InteractionsAfcUtil.DEFAULT_MAX_REMOTE_CALLS_AFC;

    for (int i = 0; i < maxRemoteCalls; i++) {
      Interaction response = createWithoutAfc(currentConfig);
      afcHistory.add(response);

      // Extract function calls from response
      ImmutableList<FunctionCallContent> functionCalls =
          InteractionsAfcUtil.extractFunctionCalls(response);

      if (functionCalls.isEmpty()) {
        // No more function calls - return final response with history
        if (afcHistory.size() > 1) {
          return response.toBuilder().automaticFunctionCallingHistory(afcHistory).build();
        }
        return response;
      }

      // Execute functions and get results
      ImmutableList<FunctionResultContent> functionResults =
          InteractionsAfcUtil.executeFunctionCalls(functionCalls, functionMap);

      if (functionResults.isEmpty()) {
        // No callable functions found - return current response
        if (afcHistory.size() > 1) {
          return response.toBuilder().automaticFunctionCallingHistory(afcHistory).build();
        }
        return response;
      }

      // Prepare next config with function results
      previousInteractionId = response.id().orElse(previousInteractionId);

      // Build new config with function results as input
      CreateInteractionConfig.Builder nextConfigBuilder =
          config.toBuilder().inputFromContents(InteractionsAfcUtil.toInputContents(functionResults));

      if (previousInteractionId != null) {
        nextConfigBuilder.previousInteractionId(previousInteractionId);
      }

      currentConfig = nextConfigBuilder.build();
    }

    // Max calls exceeded
    throw new GenAiIOException(
        "Automatic Function Calling exceeded maximum remote calls ("
            + maxRemoteCalls
            + "). Set more calls or disable AFC.");
  }

  /**
   * Retrieves an interaction by its ID.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * GetInteractionConfig config = GetInteractionConfig.builder().build();
   * Interaction interaction = client.interactions.get("interaction-id-123", config);
   * System.out.println("Status: " + interaction.status());
   * }</pre>
   *
   * <p>Note: The Interactions API is in beta and subject to change.
   *
   * @param id The ID of the interaction to retrieve
   * @param config The configuration for the get request (can be null)
   * @return The retrieved Interaction
   * @throws IllegalArgumentException if the ID is empty
   * @throws UnsupportedOperationException if using Vertex AI (not yet supported)
   * @throws GenAiIOException if the API request fails
   */
  public Interaction get(String id, GetInteractionConfig config) {
    BuiltRequest builtRequest = buildRequestForGet(id, config);

    try (ApiResponse response =
        this.apiClient.request(
            "get", builtRequest.path(), builtRequest.body(), builtRequest.httpOptions())) {
      return processResponseForGet(response, config);
    }
  }

  /**
   * Cancels a background interaction that is still in progress.
   *
   * <p>Only applies to interactions created with background=true that are in IN_PROGRESS status.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * CancelInteractionConfig config = CancelInteractionConfig.builder().build();
   * Interaction cancelled = client.interactions.cancel("interaction-id-123", config);
   * System.out.println("New status: " + cancelled.status()); // Should be CANCELLED
   * }</pre>
   *
   * <p>Note: The Interactions API is in beta and subject to change.
   *
   * @param id The ID of the interaction to cancel
   * @param config The configuration for the cancel request (can be null)
   * @return The updated Interaction with CANCELLED status
   * @throws IllegalArgumentException if the ID is empty
   * @throws UnsupportedOperationException if using Vertex AI (not yet supported)
   * @throws GenAiIOException if the API request fails
   */
  public Interaction cancel(String id, CancelInteractionConfig config) {
    BuiltRequest builtRequest = buildRequestForCancel(id, config);

    try (ApiResponse response =
        this.apiClient.request(
            "post", builtRequest.path(), builtRequest.body(), builtRequest.httpOptions())) {
      return processResponseForCancel(response, config);
    }
  }

  /**
   * Deletes an interaction by its ID.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * DeleteInteractionConfig config = DeleteInteractionConfig.builder().build();
   * DeleteInteractionResponse response = client.interactions.delete("interaction-id-123", config);
   * }</pre>
   *
   * <p>Note: The Interactions API is in beta and subject to change.
   *
   * @param id The ID of the interaction to delete
   * @param config The configuration for the delete request (can be null)
   * @return The delete response with HTTP headers
   * @throws IllegalArgumentException if the ID is empty
   * @throws UnsupportedOperationException if using Vertex AI (not yet supported)
   * @throws GenAiIOException if the API request fails
   */
  public DeleteInteractionResponse delete(String id, DeleteInteractionConfig config) {
    BuiltRequest builtRequest = buildRequestForDelete(id, config);

    try (ApiResponse response =
        this.apiClient.request(
            "delete", builtRequest.path(), builtRequest.body(), builtRequest.httpOptions())) {
      return processResponseForDelete(response, config);
    }
  }
}
