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
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsFunctionCalling"
 */
package com.google.genai.examples;

import com.google.common.collect.ImmutableMap;
import com.google.genai.Client;
import com.google.genai.types.CreateInteractionConfig;
import com.google.genai.types.Interaction;
import com.google.genai.types.Schema;
import com.google.genai.types.interactions.FunctionCallContent;
import com.google.genai.types.interactions.FunctionResultContent;
import com.google.genai.types.interactions.InteractionContent;
import com.google.genai.types.interactions.TextContent;
import com.google.genai.types.interactions.tools.FunctionTool;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Example: Function Calling with the Interactions API
 *
 * <p>Demonstrates two approaches to function calling:
 *
 * <ol>
 *   <li><b>Manual Function Calling</b>: Define function tools with Schema, handle function calls
 *       manually
 *   <li><b>Automatic Function Calling (AFC)</b>: Define function tools from Java methods, SDK
 *       auto-executes functions
 * </ol>
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsFunctionCalling {

  public static void main(String[] args) throws Exception {
    // Instantiate the client. The client gets the API key from the environment variable
    // `GOOGLE_API_KEY`.
    //
    // Note: Interactions API is currently only available in Gemini Developer API (not Vertex AI).
    Client client = new Client();

    System.out.println("=== Interactions API: Function Calling Example ===\n");

    // ========================================
    // PART 1: Manual Function Calling
    // ========================================
    System.out.println("========================================");
    System.out.println("PART 1: Manual Function Calling");
    System.out.println("========================================\n");

    manualFunctionCallingExample(client);

    // ========================================
    // PART 2: Automatic Function Calling (AFC)
    // ========================================
    System.out.println("\n========================================");
    System.out.println("PART 2: Automatic Function Calling (AFC)");
    System.out.println("========================================\n");

    automaticFunctionCallingExample(client);

    System.out.println("\n=== Example completed ===");
  }

  /**
   * Demonstrates manual function calling with the Interactions API.
   *
   * <p>In manual mode, you define the function schema and handle function calls yourself.
   */
  private static void manualFunctionCallingExample(Client client) {
    // ===== STEP 1: Define a FunctionTool with Schema =====
    System.out.println("STEP 1: Define the get_weather function using Schema\n");

    // Define the function tool with parameters schema
    // Note: Using lowercase type strings ("object", "string") for API compatibility
    FunctionTool weatherTool =
        FunctionTool.builder()
            .name("get_weather")
            .description("Get the current weather for a location")
            .parameters(
                Schema.builder()
                    .type("object")
                    .properties(
                        Map.of(
                            "location",
                            Schema.builder()
                                .type("string")
                                .description("The city name, e.g., 'Paris' or 'Tokyo'")
                                .build()))
                    .required("location")
                    .build())
            .build();

    System.out.println("FunctionTool defined: " + weatherTool.name().orElse("N/A") + "\n");

    // ===== STEP 2: First interaction - Ask about weather =====
    System.out.println("---\n");
    System.out.println("STEP 2: First interaction - Ask about weather\n");

    String userQuestion = "What's the weather like in Paris?";
    System.out.println("User: " + userQuestion + "\n");

    CreateInteractionConfig config1 =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .input(userQuestion)
            .tools(weatherTool)
            .build();

    Interaction response1 = client.interactions.create(config1);

    System.out.println("Response received. Interaction ID: " + response1.id().orElse("N/A"));
    System.out.println();

    // ===== STEP 3: Extract the function call from the response =====
    System.out.println("---\n");
    System.out.println("STEP 3: Extract and execute the function call\n");

    FunctionCallContent functionCall = extractFunctionCall(response1);
    if (functionCall == null) {
      System.out.println("ERROR: Expected a function call but didn't receive one.");
      System.out.println("Response outputs: " + response1.outputs());
      return;
    }

    System.out.println("Function call received:");
    System.out.println("  ID: " + functionCall.id().orElse("N/A"));
    System.out.println("  Name: " + functionCall.name().orElse("N/A"));
    System.out.println("  Arguments: " + functionCall.arguments().orElse(Map.of()));
    System.out.println();

    // Execute the function (simulated)
    Map<String, Object> weatherResult =
        executeGetWeather(functionCall.arguments().orElse(Map.of()));
    System.out.println("Function result: " + weatherResult);
    System.out.println();

    // ===== STEP 4: Send function result back to the model =====
    System.out.println("---\n");
    System.out.println("STEP 4: Send function result back to the model\n");

    FunctionResultContent functionResult =
        FunctionResultContent.builder()
            .id(functionCall.id().get())
            .name(functionCall.name().get())
            .result(weatherResult)
            .build();

    CreateInteractionConfig config2 =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .inputFromContents(functionResult)
            .previousInteractionId(response1.id().get())
            .tools(weatherTool)
            .build();

    Interaction response2 = client.interactions.create(config2);

    // ===== STEP 5: Display the final response =====
    System.out.println("---\n");
    System.out.println("STEP 5: Final response from the model\n");

    System.out.println("Model: ");
    printOutputs(response2);
  }

  /**
   * Demonstrates Automatic Function Calling (AFC) with the Interactions API.
   *
   * <p>In AFC mode, you define function tools from Java methods and the SDK automatically executes
   * the functions when the model requests them.
   */
  private static void automaticFunctionCallingExample(Client client) throws Exception {
    // ===== STEP 1: Get Java Method reference =====
    System.out.println("STEP 1: Create FunctionTool from Java Method\n");

    // Get the Java Method reference
    Method weatherMethod =
        InteractionsFunctionCalling.class.getMethod("getWeatherForCity", String.class);

    // Create FunctionTool from the method - this enables AFC
    FunctionTool weatherTool =
        FunctionTool.fromMethod("Get the current weather for a city", weatherMethod);

    System.out.println(
        "FunctionTool created from method: " + weatherTool.name().orElse("N/A") + "\n");
    System.out.println("AFC enabled: method() is set = " + weatherTool.method().isPresent() + "\n");

    // ===== STEP 2: Create interaction with AFC =====
    System.out.println("---\n");
    System.out.println("STEP 2: Create interaction - SDK will automatically handle function calls\n");

    String userQuestion = "What's the weather like in Tokyo?";
    System.out.println("User: " + userQuestion + "\n");

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .input(userQuestion)
            .tools(weatherTool)
            .build();

    // The SDK automatically:
    // 1. Sends the request to the model
    // 2. Detects function calls in the response
    // 3. Executes the Java method with the arguments from the model
    // 4. Sends the result back to the model
    // 5. Returns the final text response
    Interaction response = client.interactions.create(config);

    // ===== STEP 3: Display the final response =====
    System.out.println("---\n");
    System.out.println("STEP 3: Final response (AFC handled automatically)\n");

    System.out.println("Model: ");
    printOutputs(response);

    // Check AFC history
    if (response.automaticFunctionCallingHistory().isPresent()) {
      List<Interaction> history = response.automaticFunctionCallingHistory().get();
      System.out.println("\nAFC History (" + history.size() + " interactions):");
      for (int i = 0; i < history.size(); i++) {
        Interaction hist = history.get(i);
        System.out.println("  [" + (i + 1) + "] ID: " + hist.id().orElse("N/A"));
      }
    }
  }

  /**
   * Public static method for AFC - gets weather for a city.
   *
   * <p>This method must be public and static for AFC to work. The SDK uses reflection to invoke
   * it.
   *
   * @param location The city name
   * @return A map with weather information
   */
  public static Map<String, Object> getWeatherForCity(String location) {
    System.out.println("  [AFC] Executing getWeatherForCity(\"" + location + "\")");
    return ImmutableMap.of(
        "location", location,
        "temperature", "18",
        "unit", "celsius",
        "condition", "partly cloudy",
        "humidity", "62%");
  }

  /**
   * Simulates executing the get_weather function (for manual mode).
   *
   * <p>In a real application, this would call an actual weather API.
   */
  private static Map<String, Object> executeGetWeather(Map<String, Object> arguments) {
    String location = (String) arguments.getOrDefault("location", "Unknown");
    return ImmutableMap.of(
        "location", location,
        "temperature", "22",
        "unit", "celsius",
        "condition", "sunny with a few clouds",
        "humidity", "45%");
  }

  /**
   * Extracts the first FunctionCallContent from an interaction response.
   *
   * @param interaction The interaction response
   * @return The first FunctionCallContent, or null if none found
   */
  private static FunctionCallContent extractFunctionCall(Interaction interaction) {
    if (interaction.outputs().isPresent()) {
      for (InteractionContent output : interaction.outputs().get()) {
        if (output instanceof FunctionCallContent) {
          return (FunctionCallContent) output;
        }
      }
    }
    return null;
  }

  /**
   * Helper method to print interaction outputs.
   *
   * <p>Extracts and displays TextContent from the interaction response.
   */
  private static void printOutputs(Interaction interaction) {
    if (interaction.outputs().isPresent() && !interaction.outputs().get().isEmpty()) {
      for (InteractionContent output : interaction.outputs().get()) {
        if (output instanceof TextContent) {
          System.out.println(((TextContent) output).text().orElse("(empty)"));
        } else if (output instanceof FunctionCallContent) {
          FunctionCallContent fc = (FunctionCallContent) output;
          System.out.println("[Function Call: " + fc.name().orElse("unknown") + "]");
        }
      }
    }
  }

  private InteractionsFunctionCalling() {}
}
