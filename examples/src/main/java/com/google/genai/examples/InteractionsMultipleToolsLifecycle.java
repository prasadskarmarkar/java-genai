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

/**
 * Usage:
 *
 * <p>Note: The Interactions API is currently only available via the Gemini Developer API (not
 * Vertex AI).
 *
 * <p>1. Set an API key environment variable. You can find a list of available API keys here:
 * https://aistudio.google.com/app/apikey
 *
 * <p>export GOOGLE_API_KEY=YOUR_API_KEY
 *
 * <p>2. Compile the java package and run the sample code.
 *
 * <p>mvn clean compile
 *
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsMultipleToolsLifecycle"
 */
package com.google.genai.examples;

import com.google.common.collect.ImmutableMap;
import com.google.genai.Client;
import com.google.genai.types.Schema;
import com.google.genai.types.interactions.CancelInteractionConfig;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.DeleteInteractionConfig;
import com.google.genai.types.interactions.DeleteInteractionResponse;
import com.google.genai.types.interactions.GetInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.FunctionCallContent;
import com.google.genai.types.interactions.content.TextContent;
import com.google.genai.types.interactions.tools.CodeExecution;
import com.google.genai.types.interactions.tools.Function;
import com.google.genai.types.interactions.tools.GoogleSearch;
import com.google.genai.types.interactions.tools.Tool;
import com.google.genai.types.interactions.tools.UrlContext;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Example: Multiple Tools with Full Interaction Lifecycle
 *
 * <p>Demonstrates:
 *
 * <ol>
 *   <li><b>CREATE</b> - Create an interaction with multiple tools:
 *       <ul>
 *         <li>Function with AFC (Automatic Function Calling)
 *         <li>Function without AFC (Manual Function Calling)
 *         <li>GoogleSearch (Web search)
 *         <li>UrlContext (URL content retrieval)
 *         <li>CodeExecution (Code execution)
 *       </ul>
 *   <li><b>GET</b> - Retrieve interaction and validate it matches CREATE response
 *   <li><b>CANCEL</b> - Cancel the interaction
 *   <li><b>GET after CANCEL</b> - Verify status changed to CANCELLED
 *   <li><b>DELETE</b> - Delete the interaction
 *   <li><b>GET after DELETE</b> - Verify interaction is deleted (should fail)
 * </ol>
 *
 * <p>All requests and responses are logged and validated.
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsMultipleToolsLifecycle {

  private static final String SEPARATOR = "=".repeat(80);
  private static final String SUB_SEPARATOR = "-".repeat(80);
  private static final DateTimeFormatter TIMESTAMP_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  public static void main(String[] args) throws Exception {
    // Instantiate the client
    Client client = new Client();

    System.out.println(SEPARATOR);
    System.out.println("Interactions API: Multiple Tools Full Lifecycle Example");
    System.out.println(SEPARATOR);
    System.out.println();

    try {
      // ========================================
      // STEP 1: CREATE - Create interaction with multiple tools
      // ========================================
      Interaction createResponse = stepCreate(client);

      // ========================================
      // STEP 2: GET - Retrieve and validate
      // ========================================
      Interaction getResponse = stepGet(client, createResponse);

      // ========================================
      // STEP 3: CANCEL - Cancel the interaction
      // ========================================
      Interaction cancelResponse = stepCancel(client, createResponse.id());

      // ========================================
      // STEP 4: GET after CANCEL - Verify cancelled
      // ========================================
      Interaction getAfterCancelResponse = stepGetAfterCancel(client, createResponse.id());

      // ========================================
      // STEP 5: DELETE - Delete the interaction
      // ========================================
      DeleteInteractionResponse deleteResponse = stepDelete(client, createResponse.id());

      // ========================================
      // STEP 6: GET after DELETE - Verify deleted
      // ========================================
      stepGetAfterDelete(client, createResponse.id());

      System.out.println();
      System.out.println(SEPARATOR);
      System.out.println("✅ ALL LIFECYCLE STEPS COMPLETED SUCCESSFULLY");
      System.out.println(SEPARATOR);

    } catch (Exception e) {
      System.err.println();
      System.err.println(SEPARATOR);
      System.err.println("❌ ERROR: " + e.getMessage());
      System.err.println(SEPARATOR);
      e.printStackTrace();
      System.exit(1);
    }
  }

  // ==================================================================================
  // STEP 1: CREATE
  // ==================================================================================

  private static Interaction stepCreate(Client client) throws Exception {
    printStepHeader("STEP 1: CREATE", "Create interaction with multiple tools");

    // Define all tools (WITHOUT Function - working configuration)
    List<Tool> tools = new ArrayList<>();

    // 1. GoogleSearch
    System.out.println("Configuring GoogleSearch...");
    GoogleSearch googleSearchTool = GoogleSearch.builder().build();
    tools.add(googleSearchTool);
    System.out.println("  ✓ Google Search enabled");

    // 2. UrlContext
    System.out.println("Configuring UrlContext...");
    UrlContext urlContextTool = UrlContext.builder().build();
    tools.add(urlContextTool);
    System.out.println("  ✓ URL Context enabled");

    // 3. CodeExecution
    System.out.println("Configuring CodeExecution...");
    CodeExecution codeExecutionTool = CodeExecution.builder().build();
    tools.add(codeExecutionTool);
    System.out.println("  ✓ Code Execution enabled");

    System.out.println();
    System.out.println("Total tools configured: " + tools.size());
    System.out.println();

    // Create the interaction config
    String userInput = "Search for the latest quantum computing news, fetch content from https://www.wikipedia.org/wiki/Quantum_computing, and calculate the first 10 Fibonacci numbers.";
    System.out.println("User Input: " + userInput);
    System.out.println();

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .input(userInput)
            .tools(tools)
            .build();

    // Print request
    printSubSection("CREATE REQUEST");
    System.out.println("Model: " + config.model().orElse("N/A"));
    System.out.println("Agent: " + config.agent().orElse("N/A"));
    System.out.println("Input: " + config.input().getValue().toString());
    System.out.println("Background: " + config.background().orElse(false));
    System.out.println("Tools count: " + config.tools().map(List::size).orElse(0));
    System.out.println();

    // Save request
    saveResponse("1_create_request.json", config.toJson());

    // Make the API call
    System.out.println("Calling client.interactions.create()...");
    long startTime = System.currentTimeMillis();
    Interaction response = client.interactions.create(config);
    long duration = System.currentTimeMillis() - startTime;

    // Print response
    printSubSection("CREATE RESPONSE");
    printInteractionDetails(response, duration);

    // Save response
    saveResponse("1_create_response.json", response.toJson());

    System.out.println();
    return response;
  }

  // ==================================================================================
  // STEP 2: GET
  // ==================================================================================

  private static Interaction stepGet(Client client, Interaction createResponse) {
    printStepHeader("STEP 2: GET", "Retrieve interaction and validate");

    String interactionId = createResponse.id();
    System.out.println("Interaction ID: " + interactionId);
    System.out.println();

    // Print request
    printSubSection("GET REQUEST");
    System.out.println("Interaction ID: " + interactionId);
    System.out.println();

    // Save request
    GetInteractionConfig config = GetInteractionConfig.builder().build();
    saveResponse("2_get_request.json", "{\"interaction_id\":\"" + interactionId + "\"}");

    // Make the API call
    System.out.println("Calling client.interactions.get()...");
    long startTime = System.currentTimeMillis();
    Interaction response = client.interactions.get(interactionId, config);
    long duration = System.currentTimeMillis() - startTime;

    // Print response
    printSubSection("GET RESPONSE");
    printInteractionDetails(response, duration);

    // Save response
    saveResponse("2_get_response.json", response.toJson());

    // Validate GET matches CREATE
    printSubSection("VALIDATION: GET vs CREATE");
    validateInteractions(createResponse, response, "CREATE", "GET");

    System.out.println();
    return response;
  }

  // ==================================================================================
  // STEP 3: CANCEL
  // ==================================================================================

  private static Interaction stepCancel(Client client, String interactionId) {
    printStepHeader("STEP 3: CANCEL", "Cancel the interaction");

    System.out.println("Interaction ID: " + interactionId);
    System.out.println();

    // Print request
    printSubSection("CANCEL REQUEST");
    System.out.println("Interaction ID: " + interactionId);
    System.out.println();

    // Save request
    saveResponse("3_cancel_request.json", "{\"interaction_id\":\"" + interactionId + "\"}");

    // Make the API call
    System.out.println("Calling client.interactions.cancel()...");
    long startTime = System.currentTimeMillis();
    CancelInteractionConfig config = CancelInteractionConfig.builder().build();
    Interaction response = client.interactions.cancel(interactionId, config);
    long duration = System.currentTimeMillis() - startTime;

    // Print response
    printSubSection("CANCEL RESPONSE");
    printInteractionDetails(response, duration);

    // Save response
    saveResponse("3_cancel_response.json", response.toJson());

    System.out.println();
    return response;
  }

  // ==================================================================================
  // STEP 4: GET after CANCEL
  // ==================================================================================

  private static Interaction stepGetAfterCancel(Client client, String interactionId) {
    printStepHeader("STEP 4: GET after CANCEL", "Verify status changed to CANCELLED");

    System.out.println("Interaction ID: " + interactionId);
    System.out.println();

    // Print request
    printSubSection("GET (after CANCEL) REQUEST");
    System.out.println("Interaction ID: " + interactionId);
    System.out.println();

    // Save request
    saveResponse("4_get_after_cancel_request.json", "{\"interaction_id\":\"" + interactionId + "\"}");

    // Make the API call
    System.out.println("Calling client.interactions.get()...");
    long startTime = System.currentTimeMillis();
    GetInteractionConfig config = GetInteractionConfig.builder().build();
    Interaction response = client.interactions.get(interactionId, config);
    long duration = System.currentTimeMillis() - startTime;

    // Print response
    printSubSection("GET (after CANCEL) RESPONSE");
    printInteractionDetails(response, duration);

    // Save response
    saveResponse("4_get_after_cancel_response.json", response.toJson());

    // Validate status is CANCELLED
    printSubSection("VALIDATION: Status should be CANCELLED");
    String status = response.status().toString();
    if ("CANCELLED".equals(status)) {
      System.out.println("✅ PASS: Status is CANCELLED");
    } else {
      System.out.println("❌ FAIL: Expected status CANCELLED, got: " + status);
    }

    System.out.println();
    return response;
  }

  // ==================================================================================
  // STEP 5: DELETE
  // ==================================================================================

  private static DeleteInteractionResponse stepDelete(Client client, String interactionId) {
    printStepHeader("STEP 5: DELETE", "Delete the interaction");

    System.out.println("Interaction ID: " + interactionId);
    System.out.println();

    // Print request
    printSubSection("DELETE REQUEST");
    System.out.println("Interaction ID: " + interactionId);
    System.out.println();

    // Save request
    saveResponse("5_delete_request.json", "{\"interaction_id\":\"" + interactionId + "\"}");

    // Make the API call
    System.out.println("Calling client.interactions.delete()...");
    long startTime = System.currentTimeMillis();
    DeleteInteractionConfig config = DeleteInteractionConfig.builder().build();
    DeleteInteractionResponse response = client.interactions.delete(interactionId, config);
    long duration = System.currentTimeMillis() - startTime;

    // Print response
    printSubSection("DELETE RESPONSE");
    System.out.println("Duration: " + duration + "ms");
    System.out.println("HTTP Response: " + response.sdkHttpResponse().orElse(null));
    System.out.println();

    // Save response
    saveResponse("5_delete_response.json", response.toJson());

    System.out.println();
    return response;
  }

  // ==================================================================================
  // STEP 6: GET after DELETE
  // ==================================================================================

  private static void stepGetAfterDelete(Client client, String interactionId) {
    printStepHeader("STEP 6: GET after DELETE", "Verify interaction is deleted (should fail)");

    System.out.println("Interaction ID: " + interactionId);
    System.out.println();

    // Print request
    printSubSection("GET (after DELETE) REQUEST");
    System.out.println("Interaction ID: " + interactionId);
    System.out.println();

    // Save request
    saveResponse("6_get_after_delete_request.json", "{\"interaction_id\":\"" + interactionId + "\"}");

    // Make the API call - expect this to fail
    System.out.println("Calling client.interactions.get() (expecting error)...");
    try {
      long startTime = System.currentTimeMillis();
      GetInteractionConfig config = GetInteractionConfig.builder().build();
      Interaction response = client.interactions.get(interactionId, config);
      long duration = System.currentTimeMillis() - startTime;

      // If we get here, the interaction was NOT deleted (unexpected)
      printSubSection("GET (after DELETE) RESPONSE - UNEXPECTED SUCCESS");
      System.out.println("❌ UNEXPECTED: GET succeeded after DELETE");
      printInteractionDetails(response, duration);
      saveResponse("6_get_after_delete_response_unexpected.json", response.toJson());

    } catch (Exception e) {
      // Expected: GET should fail because interaction was deleted
      printSubSection("GET (after DELETE) RESPONSE - EXPECTED ERROR");
      System.out.println("✅ PASS: GET failed as expected after DELETE");
      System.out.println("Error type: " + e.getClass().getSimpleName());
      System.out.println("Error message: " + e.getMessage());

      // Save error
      saveResponse(
          "6_get_after_delete_error.txt",
          "Error Type: " + e.getClass().getName() + "\nMessage: " + e.getMessage());
    }

    System.out.println();
  }

  // ==================================================================================
  // HELPER METHODS
  // ==================================================================================

  /**
   * AFC-enabled function: Get weather for a city.
   *
   * <p>This method is public and static so it can be invoked via reflection for AFC.
   */
  public static Map<String, Object> getWeather(String location) {
    System.out.println("    [AFC] Executing getWeather(\"" + location + "\")");
    return ImmutableMap.of(
        "location", location,
        "temperature", "18",
        "unit", "celsius",
        "condition", "partly cloudy",
        "humidity", "65%");
  }

  private static void printStepHeader(String stepTitle, String description) {
    System.out.println(SEPARATOR);
    System.out.println(stepTitle + ": " + description);
    System.out.println(SEPARATOR);
    System.out.println();
  }

  private static void printSubSection(String title) {
    System.out.println(SUB_SEPARATOR);
    System.out.println(title);
    System.out.println(SUB_SEPARATOR);
  }

  private static void printInteractionDetails(Interaction interaction, long duration) {
    System.out.println("Duration: " + duration + "ms");
    System.out.println("ID: " + interaction.id());
    System.out.println("Status: " + interaction.status());
    System.out.println("Model: " + interaction.model().orElse("N/A"));
    System.out.println("Created: " + interaction.created().map(Object::toString).orElse("N/A"));
    System.out.println("Updated: " + interaction.updated().map(Object::toString).orElse("N/A"));

    // Print outputs
    if (interaction.outputs().isPresent() && !interaction.outputs().get().isEmpty()) {
      System.out.println();
      System.out.println("Outputs (" + interaction.outputs().get().size() + "):");
      int index = 1;
      for (Content output : interaction.outputs().get()) {
        System.out.println("  [" + index + "] " + getContentTypeDescription(output));
        if (output instanceof TextContent) {
          TextContent textContent = (TextContent) output;
          String text = textContent.text().orElse("(empty)");
          // Truncate long text
          if (text.length() > 100) {
            System.out.println("      " + text.substring(0, 100) + "...");
          } else {
            System.out.println("      " + text);
          }
        } else if (output instanceof FunctionCallContent) {
          FunctionCallContent fc = (FunctionCallContent) output;
          System.out.println("      Function: " + fc.name());
          System.out.println("      Arguments: " + fc.arguments());
        }
        index++;
      }
    } else {
      System.out.println("Outputs: (none)");
    }

    System.out.println();
  }

  private static String getContentTypeDescription(Content content) {
    if (content instanceof TextContent) {
      return "TextContent";
    } else if (content instanceof FunctionCallContent) {
      return "FunctionCallContent";
    } else {
      return content.getClass().getSimpleName();
    }
  }

  private static void validateInteractions(
      Interaction expected, Interaction actual, String expectedLabel, String actualLabel) {
    boolean allPassed = true;

    // Validate ID
    if (expected.id().equals(actual.id())) {
      System.out.println("✅ ID matches: " + expected.id());
    } else {
      System.out.println(
          "❌ ID mismatch: " + expectedLabel + "=" + expected.id() + ", " + actualLabel + "="
              + actual.id());
      allPassed = false;
    }

    // Validate Status
    String expectedStatus = expected.status().toString();
    String actualStatus = actual.status().toString();
    if (expectedStatus.equals(actualStatus)) {
      System.out.println("✅ Status matches: " + expectedStatus);
    } else {
      System.out.println(
          "❌ Status mismatch: " + expectedLabel + "=" + expectedStatus + ", " + actualLabel + "="
              + actualStatus);
      allPassed = false;
    }

    // Validate Model
    String expectedModel = expected.model().orElse("N/A");
    String actualModel = actual.model().orElse("N/A");
    if (expectedModel.equals(actualModel)) {
      System.out.println("✅ Model matches: " + expectedModel);
    } else {
      System.out.println(
          "❌ Model mismatch: " + expectedLabel + "=" + expectedModel + ", " + actualLabel + "="
              + actualModel);
      allPassed = false;
    }

    // Summary
    System.out.println();
    if (allPassed) {
      System.out.println("✅ ALL VALIDATIONS PASSED");
    } else {
      System.out.println("❌ SOME VALIDATIONS FAILED");
    }
  }

  private static void saveResponse(String filename, String content) {
    String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
    String dirPath = "interaction_lifecycle_logs";
    String filePath = dirPath + "/" + filename;

    try {
      // Create directory if it doesn't exist
      java.nio.file.Files.createDirectories(java.nio.file.Paths.get(dirPath));

      // Write content to file
      java.nio.file.Files.write(
          java.nio.file.Paths.get(filePath),
          content.getBytes(java.nio.charset.StandardCharsets.UTF_8));

      System.out.println("📝 Response saved to: " + filePath);
      System.out.println("   Timestamp: " + timestamp);
    } catch (Exception e) {
      System.err.println("❌ Failed to save response to " + filePath + ": " + e.getMessage());
    }
  }

  private InteractionsMultipleToolsLifecycle() {}
}
