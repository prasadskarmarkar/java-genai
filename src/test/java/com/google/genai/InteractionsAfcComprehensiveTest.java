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

package com.google.genai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.genai.types.FunctionDeclaration;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.InteractionStatus;
import com.google.genai.types.interactions.content.FunctionCallContent;
import com.google.genai.types.interactions.content.FunctionResultContent;
import com.google.genai.types.interactions.content.TextContent;
import com.google.genai.types.interactions.tools.Function;
import com.google.genai.types.interactions.tools.GoogleSearch;
import com.google.genai.types.interactions.tools.CodeExecution;
import com.google.genai.errors.GenAiIOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * Comprehensive tests for Automatic Function Calling (AFC) in Interactions API.
 *
 * <p>This test suite validates the AFC loop logic in Interactions.java (lines 722-806), covering:
 * basic AFC flow, different tool types, error handling, and history/state management.
 *
 * <p>Uses Mockito to simulate API responses and test the full AFC orchestration logic without
 * making real API calls.
 */
public class InteractionsAfcComprehensiveTest {

  private static final String MODEL_ID = "gemini-2.5-flash";
  private static final String INTERACTION_ID_1 = "afc-interaction-1";
  private static final String INTERACTION_ID_2 = "afc-interaction-2";
  private static final String INTERACTION_ID_3 = "afc-interaction-3";

  private ApiClient mockedClient;
  private ApiResponse mockedResponse;
  private Client client;

  // Test functions for AFC
  public static String getWeather(String location) {
    return "The weather in " + location + " is sunny and 72°F";
  }

  public static int add(int a, int b) {
    return a + b;
  }

  public static String getCurrentTime() {
    return "2025-01-24T10:30:00Z";
  }

  public static double calculateTax(double amount, double rate) {
    return amount * rate;
  }

  @BeforeEach
  void setUp() throws Exception {
    mockedClient = Mockito.mock(ApiClient.class);
    mockedResponse = Mockito.mock(ApiResponse.class);

    String apiKey = "test-key";
    client = Client.builder().apiKey(apiKey).vertexAI(false).build();

    // Use reflection to inject mocked client
    Field apiClientField = Interactions.class.getDeclaredField("apiClient");
    apiClientField.setAccessible(true);
    apiClientField.set(client.interactions, mockedClient);
  }

  // ==================== Basic AFC Flow ====================

  @Test
  public void testAfc_singleFunctionCall() throws Exception {
    // Arrange
    Method weatherMethod =
        InteractionsAfcComprehensiveTest.class.getMethod("getWeather", String.class);

    Function functionTool = Function.fromMethod(weatherMethod);

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("What's the weather in Paris?")
            .tools(ImmutableList.of(functionTool))
            .build();

    // First response: model requests function call
    String functionCallResponse =
        "{"
            + "\"id\":\"" + INTERACTION_ID_1 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"functionCall\":{"
            + "      \"name\":\"getWeather\","
            + "      \"arguments\":{\"location\":\"Paris\"}"
            + "    }"
            + "  }]"
            + "}"
            + "}";

    // Second response: final answer after function execution
    String finalResponse =
        "{"
            + "\"id\":\"" + INTERACTION_ID_2 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"text\":\"The weather in Paris is sunny and 72°F.\""
            + "  }]"
            + "}"
            + "}";

    ResponseBody body1 =
        ResponseBody.create(functionCallResponse, MediaType.get("application/json"));
    ResponseBody body2 = ResponseBody.create(finalResponse, MediaType.get("application/json"));

    when(mockedResponse.getBody()).thenReturn(body1).thenReturn(body2);
    when(mockedClient.request(anyString(), anyString(), anyString(), any()))
        .thenReturn(mockedResponse);

    // Act
    Interaction result = client.interactions.create(config);

    // Assert
    assertNotNull(result);
    assertEquals(INTERACTION_ID_2, result.id());
    assertTrue(result.automaticFunctionCallingHistory().isPresent());
    assertEquals(2, result.automaticFunctionCallingHistory().get().size());

    // Verify two API calls were made
    verify(mockedClient, times(2)).request(eq("post"), anyString(), anyString(), any());
  }

  @Test
  public void testAfc_multipleFunctionCalls() throws Exception {
    // Arrange
    Method weatherMethod =
        InteractionsAfcComprehensiveTest.class.getMethod("getWeather", String.class);
    Method addMethod = InteractionsAfcComprehensiveTest.class.getMethod("add", int.class, int.class);

    // In Interactions API, each Function is a separate tool
    Function weatherTool = Function.fromMethod(weatherMethod);
    Function addTool = Function.fromMethod(addMethod);

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("What's the weather in Paris and what's 5+3?")
            .tools(ImmutableList.of(weatherTool, addTool))
            .build();

    // Response 1: first function call
    String response1 =
        "{"
            + "\"id\":\"" + INTERACTION_ID_1 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"functionCall\":{"
            + "      \"name\":\"getWeather\","
            + "      \"arguments\":{\"location\":\"Paris\"}"
            + "    }"
            + "  }]"
            + "}"
            + "}";

    // Response 2: second function call
    String response2 =
        "{"
            + "\"id\":\"" + INTERACTION_ID_2 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"functionCall\":{"
            + "      \"name\":\"add\","
            + "      \"arguments\":{\"a\":5,\"b\":3}"
            + "    }"
            + "  }]"
            + "}"
            + "}";

    // Response 3: final answer
    String response3 =
        "{"
            + "\"id\":\"" + INTERACTION_ID_3 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"text\":\"The weather is sunny and 5+3 equals 8.\""
            + "  }]"
            + "}"
            + "}";

    ResponseBody body1 = ResponseBody.create(response1, MediaType.get("application/json"));
    ResponseBody body2 = ResponseBody.create(response2, MediaType.get("application/json"));
    ResponseBody body3 = ResponseBody.create(response3, MediaType.get("application/json"));

    when(mockedResponse.getBody()).thenReturn(body1).thenReturn(body2).thenReturn(body3);
    when(mockedClient.request(anyString(), anyString(), anyString(), any()))
        .thenReturn(mockedResponse);

    // Act
    Interaction result = client.interactions.create(config);

    // Assert
    assertNotNull(result);
    assertEquals(INTERACTION_ID_3, result.id());
    assertTrue(result.automaticFunctionCallingHistory().isPresent());
    assertEquals(3, result.automaticFunctionCallingHistory().get().size());

    // Verify three API calls were made
    verify(mockedClient, times(3)).request(eq("post"), anyString(), anyString(), any());
  }

  @Test
  public void testAfc_parallelFunctionCalls() throws Exception {
    // Arrange
    Method weatherMethod =
        InteractionsAfcComprehensiveTest.class.getMethod("getWeather", String.class);
    Method timeMethod = InteractionsAfcComprehensiveTest.class.getMethod("getCurrentTime");

    // In Interactions API, each Function is a separate tool
    Function weatherTool = Function.fromMethod(weatherMethod);
    Function timeTool = Function.fromMethod(timeMethod);

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("What's the weather and current time?")
            .tools(ImmutableList.of(weatherTool, timeTool))
            .build();

    // Response 1: model requests multiple parallel function calls
    String response1 =
        "{"
            + "\"id\":\"" + INTERACTION_ID_1 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":["
            + "    {\"functionCall\":{\"name\":\"getWeather\",\"arguments\":{\"location\":\"Paris\"}}},"
            + "    {\"functionCall\":{\"name\":\"getCurrentTime\",\"arguments\":{}}}"
            + "  ]"
            + "}"
            + "}";

    // Response 2: final answer
    String response2 =
        "{"
            + "\"id\":\"" + INTERACTION_ID_2 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"text\":\"It's sunny in Paris and the current time is 10:30 AM.\""
            + "  }]"
            + "}"
            + "}";

    ResponseBody body1 = ResponseBody.create(response1, MediaType.get("application/json"));
    ResponseBody body2 = ResponseBody.create(response2, MediaType.get("application/json"));

    when(mockedResponse.getBody()).thenReturn(body1).thenReturn(body2);
    when(mockedClient.request(anyString(), anyString(), anyString(), any()))
        .thenReturn(mockedResponse);

    // Act
    Interaction result = client.interactions.create(config);

    // Assert
    assertNotNull(result);
    assertEquals(INTERACTION_ID_2, result.id());
    assertTrue(result.automaticFunctionCallingHistory().isPresent());
    assertEquals(2, result.automaticFunctionCallingHistory().get().size());

    verify(mockedClient, times(2)).request(eq("post"), anyString(), anyString(), any());
  }

  @Test
  public void testAfc_nestedFunctionCalls() throws Exception {
    // Arrange
    Method weatherMethod =
        InteractionsAfcComprehensiveTest.class.getMethod("getWeather", String.class);
    Method addMethod = InteractionsAfcComprehensiveTest.class.getMethod("add", int.class, int.class);

    // In Interactions API, each Function is a separate tool
    Function weatherTool = Function.fromMethod(weatherMethod);
    Function addTool = Function.fromMethod(addMethod);

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Get weather and calculate something based on it")
            .tools(ImmutableList.of(weatherTool, addTool))
            .build();

    // Response 1: first function call
    String response1 =
        "{"
            + "\"id\":\"" + INTERACTION_ID_1 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"functionCall\":{\"name\":\"getWeather\",\"arguments\":{\"location\":\"Paris\"}}"
            + "  }]"
            + "}"
            + "}";

    // Response 2: nested function call based on first result
    String response2 =
        "{"
            + "\"id\":\"" + INTERACTION_ID_2 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"functionCall\":{\"name\":\"add\",\"arguments\":{\"a\":72,\"b\":10}}"
            + "  }]"
            + "}"
            + "}";

    // Response 3: final answer
    String response3 =
        "{"
            + "\"id\":\"" + INTERACTION_ID_3 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"text\":\"The temperature will be 82°F later.\""
            + "  }]"
            + "}"
            + "}";

    ResponseBody body1 = ResponseBody.create(response1, MediaType.get("application/json"));
    ResponseBody body2 = ResponseBody.create(response2, MediaType.get("application/json"));
    ResponseBody body3 = ResponseBody.create(response3, MediaType.get("application/json"));

    when(mockedResponse.getBody()).thenReturn(body1).thenReturn(body2).thenReturn(body3);
    when(mockedClient.request(anyString(), anyString(), anyString(), any()))
        .thenReturn(mockedResponse);

    // Act
    Interaction result = client.interactions.create(config);

    // Assert
    assertNotNull(result);
    assertTrue(result.automaticFunctionCallingHistory().isPresent());
    assertEquals(3, result.automaticFunctionCallingHistory().get().size());
  }

  @Test
  public void testAfc_maxCallsExceeded() throws Exception {
    // Arrange
    Method weatherMethod =
        InteractionsAfcComprehensiveTest.class.getMethod("getWeather", String.class);

    Function functionTool = Function.fromMethod(weatherMethod);

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Keep calling weather function")
            .tools(ImmutableList.of(functionTool))
            .build();

    // Always return a function call response to trigger max calls
    String functionCallResponse =
        "{"
            + "\"id\":\"interaction-loop\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"functionCall\":{\"name\":\"getWeather\",\"arguments\":{\"location\":\"Paris\"}}"
            + "  }]"
            + "}"
            + "}";

    ResponseBody body = ResponseBody.create(functionCallResponse, MediaType.get("application/json"));
    when(mockedResponse.getBody()).thenReturn(body);
    when(mockedClient.request(anyString(), anyString(), anyString(), any()))
        .thenReturn(mockedResponse);

    // Act & Assert
    GenAiIOException exception =
        assertThrows(GenAiIOException.class, () -> client.interactions.create(config));

    assertTrue(exception.getMessage().contains("exceeded maximum remote calls"));
  }

  // ==================== AFC with Different Tools ====================

  @Test
  public void testAfc_withGoogleSearch() throws Exception {
    // Arrange
    Method weatherMethod =
        InteractionsAfcComprehensiveTest.class.getMethod("getWeather", String.class);

    Function functionTool = Function.fromMethod(weatherMethod);
    GoogleSearch googleSearch =
        GoogleSearch.builder().build();

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Search for weather and then get detailed forecast")
            .tools(ImmutableList.of(functionTool, googleSearch))
            .build();

    // Response with function call (not Google Search)
    String response1 =
        "{"
            + "\"id\":\"" + INTERACTION_ID_1 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"functionCall\":{\"name\":\"getWeather\",\"arguments\":{\"location\":\"Paris\"}}"
            + "  }]"
            + "}"
            + "}";

    String response2 =
        "{"
            + "\"id\":\"" + INTERACTION_ID_2 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"text\":\"Weather information retrieved.\""
            + "  }]"
            + "}"
            + "}";

    ResponseBody body1 = ResponseBody.create(response1, MediaType.get("application/json"));
    ResponseBody body2 = ResponseBody.create(response2, MediaType.get("application/json"));

    when(mockedResponse.getBody()).thenReturn(body1).thenReturn(body2);
    when(mockedClient.request(anyString(), anyString(), anyString(), any()))
        .thenReturn(mockedResponse);

    // Act
    Interaction result = client.interactions.create(config);

    // Assert
    assertNotNull(result);
    assertTrue(result.automaticFunctionCallingHistory().isPresent());
  }

  @Test
  public void testAfc_withCodeExecution() throws Exception {
    // Arrange
    Method addMethod = InteractionsAfcComprehensiveTest.class.getMethod("add", int.class, int.class);

    Function functionTool = Function.fromMethod(addMethod);
    CodeExecution codeExecution =
        CodeExecution.builder().build();

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Calculate 5+3 using the function")
            .tools(ImmutableList.of(functionTool, codeExecution))
            .build();

    String response1 =
        "{"
            + "\"id\":\"" + INTERACTION_ID_1 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"functionCall\":{\"name\":\"add\",\"arguments\":{\"a\":5,\"b\":3}}"
            + "  }]"
            + "}"
            + "}";

    String response2 =
        "{"
            + "\"id\":\"" + INTERACTION_ID_2 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"text\":\"The result is 8.\""
            + "  }]"
            + "}"
            + "}";

    ResponseBody body1 = ResponseBody.create(response1, MediaType.get("application/json"));
    ResponseBody body2 = ResponseBody.create(response2, MediaType.get("application/json"));

    when(mockedResponse.getBody()).thenReturn(body1).thenReturn(body2);
    when(mockedClient.request(anyString(), anyString(), anyString(), any()))
        .thenReturn(mockedResponse);

    // Act
    Interaction result = client.interactions.create(config);

    // Assert
    assertNotNull(result);
    assertTrue(result.automaticFunctionCallingHistory().isPresent());
  }

  @Test
  public void testAfc_withMultipleToolTypes() throws Exception {
    // Arrange
    Method weatherMethod =
        InteractionsAfcComprehensiveTest.class.getMethod("getWeather", String.class);

    Function functionTool = Function.fromMethod(weatherMethod);
    GoogleSearch googleSearch =
        GoogleSearch.builder().build();
    CodeExecution codeExecution =
        CodeExecution.builder().build();

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Use all available tools")
            .tools(ImmutableList.of(functionTool, googleSearch, codeExecution))
            .build();

    String response =
        "{"
            + "\"id\":\"" + INTERACTION_ID_1 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"text\":\"Using multiple tools.\""
            + "  }]"
            + "}"
            + "}";

    ResponseBody body = ResponseBody.create(response, MediaType.get("application/json"));
    when(mockedResponse.getBody()).thenReturn(body);
    when(mockedClient.request(anyString(), anyString(), anyString(), any()))
        .thenReturn(mockedResponse);

    // Act
    Interaction result = client.interactions.create(config);

    // Assert
    assertNotNull(result);
  }

  @Test
  public void testAfc_withCustomMethods() throws Exception {
    // Arrange
    Method taxMethod =
        InteractionsAfcComprehensiveTest.class.getMethod("calculateTax", double.class, double.class);

    Function functionTool = Function.fromMethod(taxMethod);

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Calculate tax on $100 at 8.5% rate")
            .tools(ImmutableList.of(functionTool))
            .build();

    String response1 =
        "{"
            + "\"id\":\"" + INTERACTION_ID_1 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"functionCall\":{\"name\":\"calculateTax\",\"arguments\":{\"amount\":100.0,\"rate\":0.085}}"
            + "  }]"
            + "}"
            + "}";

    String response2 =
        "{"
            + "\"id\":\"" + INTERACTION_ID_2 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"text\":\"The tax is $8.50.\""
            + "  }]"
            + "}"
            + "}";

    ResponseBody body1 = ResponseBody.create(response1, MediaType.get("application/json"));
    ResponseBody body2 = ResponseBody.create(response2, MediaType.get("application/json"));

    when(mockedResponse.getBody()).thenReturn(body1).thenReturn(body2);
    when(mockedClient.request(anyString(), anyString(), anyString(), any()))
        .thenReturn(mockedResponse);

    // Act
    Interaction result = client.interactions.create(config);

    // Assert
    assertNotNull(result);
    assertTrue(result.automaticFunctionCallingHistory().isPresent());
  }

  // ==================== AFC Error Handling ====================

  @Test
  public void testAfc_functionThrowsException() throws Exception {
    // This test would require a function that throws an exception
    // Implementation depends on how InteractionsAfcUtil handles function exceptions
    // Placeholder for now
  }

  @Test
  public void testAfc_invalidFunctionResponse() throws Exception {
    // Arrange
    Method weatherMethod =
        InteractionsAfcComprehensiveTest.class.getMethod("getWeather", String.class);

    Function functionTool = Function.fromMethod(weatherMethod);

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Get weather")
            .tools(ImmutableList.of(functionTool))
            .build();

    // Invalid JSON in function call response
    String invalidResponse =
        "{"
            + "\"id\":\"" + INTERACTION_ID_1 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"functionCall\":{\"name\":\"getWeather\",\"arguments\":\"invalid-not-object\"}"
            + "  }]"
            + "}"
            + "}";

    ResponseBody body = ResponseBody.create(invalidResponse, MediaType.get("application/json"));
    when(mockedResponse.getBody()).thenReturn(body);
    when(mockedClient.request(anyString(), anyString(), anyString(), any()))
        .thenReturn(mockedResponse);

    // Act & Assert - this might throw or handle gracefully depending on implementation
    // Placeholder assertion
  }

  @Test
  public void testAfc_functionNotFound() throws Exception {
    // Arrange
    Method weatherMethod =
        InteractionsAfcComprehensiveTest.class.getMethod("getWeather", String.class);

    Function functionTool = Function.fromMethod(weatherMethod);

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Call unknown function")
            .tools(ImmutableList.of(functionTool))
            .build();

    // Model requests a function that doesn't exist
    String response =
        "{"
            + "\"id\":\"" + INTERACTION_ID_1 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"functionCall\":{\"name\":\"unknownFunction\",\"arguments\":{}}"
            + "  }]"
            + "}"
            + "}";

    ResponseBody body = ResponseBody.create(response, MediaType.get("application/json"));
    when(mockedResponse.getBody()).thenReturn(body);
    when(mockedClient.request(anyString(), anyString(), anyString(), any()))
        .thenReturn(mockedResponse);

    // Act - should handle gracefully and return response without executing
    Interaction result = client.interactions.create(config);

    // Assert
    assertNotNull(result);
    // Should not have AFC history if no callable function was found
  }

  @Test
  public void testAfc_conversionFailure() throws Exception {
    // Arrange
    Method addMethod = InteractionsAfcComprehensiveTest.class.getMethod("add", int.class, int.class);

    Function functionTool = Function.fromMethod(addMethod);

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Add numbers")
            .tools(ImmutableList.of(functionTool))
            .build();

    // Model provides wrong argument types
    String response =
        "{"
            + "\"id\":\"" + INTERACTION_ID_1 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"functionCall\":{\"name\":\"add\",\"arguments\":{\"a\":\"not-a-number\",\"b\":3}}"
            + "  }]"
            + "}"
            + "}";

    ResponseBody body = ResponseBody.create(response, MediaType.get("application/json"));
    when(mockedResponse.getBody()).thenReturn(body);
    when(mockedClient.request(anyString(), anyString(), anyString(), any()))
        .thenReturn(mockedResponse);

    // Act & Assert - might throw or handle gracefully
    // Placeholder
  }

  // ==================== AFC History & State ====================

  @Test
  public void testAfc_historyTracking() throws Exception {
    // Arrange
    Method weatherMethod =
        InteractionsAfcComprehensiveTest.class.getMethod("getWeather", String.class);

    Function functionTool = Function.fromMethod(weatherMethod);

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Get weather")
            .tools(ImmutableList.of(functionTool))
            .build();

    String response1 =
        "{"
            + "\"id\":\"" + INTERACTION_ID_1 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"functionCall\":{\"name\":\"getWeather\",\"arguments\":{\"location\":\"Paris\"}}"
            + "  }]"
            + "}"
            + "}";

    String response2 =
        "{"
            + "\"id\":\"" + INTERACTION_ID_2 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"text\":\"Final answer.\""
            + "  }]"
            + "}"
            + "}";

    ResponseBody body1 = ResponseBody.create(response1, MediaType.get("application/json"));
    ResponseBody body2 = ResponseBody.create(response2, MediaType.get("application/json"));

    when(mockedResponse.getBody()).thenReturn(body1).thenReturn(body2);
    when(mockedClient.request(anyString(), anyString(), anyString(), any()))
        .thenReturn(mockedResponse);

    // Act
    Interaction result = client.interactions.create(config);

    // Assert
    assertTrue(result.automaticFunctionCallingHistory().isPresent());
    assertEquals(2, result.automaticFunctionCallingHistory().get().size());

    // Verify history contains both interactions
    assertEquals(
        INTERACTION_ID_1, result.automaticFunctionCallingHistory().get().get(0).id());
    assertEquals(
        INTERACTION_ID_2, result.automaticFunctionCallingHistory().get().get(1).id());
  }

  @Test
  public void testAfc_previousInteractionId() throws Exception {
    // Arrange
    Method weatherMethod =
        InteractionsAfcComprehensiveTest.class.getMethod("getWeather", String.class);

    Function functionTool = Function.fromMethod(weatherMethod);

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Continue from previous")
            .previousInteractionId("existing-conversation-id")
            .tools(ImmutableList.of(functionTool))
            .build();

    String response1 =
        "{"
            + "\"id\":\"" + INTERACTION_ID_1 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"functionCall\":{\"name\":\"getWeather\",\"arguments\":{\"location\":\"Paris\"}}"
            + "  }]"
            + "}"
            + "}";

    String response2 =
        "{"
            + "\"id\":\"" + INTERACTION_ID_2 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"text\":\"Continuing conversation.\""
            + "  }]"
            + "}"
            + "}";

    ResponseBody body1 = ResponseBody.create(response1, MediaType.get("application/json"));
    ResponseBody body2 = ResponseBody.create(response2, MediaType.get("application/json"));

    when(mockedResponse.getBody()).thenReturn(body1).thenReturn(body2);
    when(mockedClient.request(anyString(), anyString(), anyString(), any()))
        .thenReturn(mockedResponse);

    // Act
    Interaction result = client.interactions.create(config);

    // Assert
    assertNotNull(result);
    assertTrue(result.automaticFunctionCallingHistory().isPresent());

    // Verify that subsequent calls use previousInteractionId
    ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
    verify(mockedClient, times(2))
        .request(eq("post"), anyString(), bodyCaptor.capture(), any());

    // Second call should include previousInteractionId
    String secondCallBody = bodyCaptor.getAllValues().get(1);
    assertTrue(secondCallBody.contains(INTERACTION_ID_1));
  }

  @Test
  public void testAfc_historyNotReturnedForSingleCall() throws Exception {
    // Arrange
    Method weatherMethod =
        InteractionsAfcComprehensiveTest.class.getMethod("getWeather", String.class);

    Function functionTool = Function.fromMethod(weatherMethod);

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Simple question without function calls")
            .tools(ImmutableList.of(functionTool))
            .build();

    // Model responds without calling any functions
    String response =
        "{"
            + "\"id\":\"" + INTERACTION_ID_1 + "\","
            + "\"status\":\"completed\","
            + "\"output\":{"
            + "  \"contents\":[{"
            + "    \"text\":\"Direct answer without function calls.\""
            + "  }]"
            + "}"
            + "}";

    ResponseBody body = ResponseBody.create(response, MediaType.get("application/json"));
    when(mockedResponse.getBody()).thenReturn(body);
    when(mockedClient.request(anyString(), anyString(), anyString(), any()))
        .thenReturn(mockedResponse);

    // Act
    Interaction result = client.interactions.create(config);

    // Assert
    assertNotNull(result);
    // No AFC history for single call without function invocation
    assertFalse(result.automaticFunctionCallingHistory().isPresent());

    // Only one API call made
    verify(mockedClient, times(1)).request(eq("post"), anyString(), anyString(), any());
  }
}
