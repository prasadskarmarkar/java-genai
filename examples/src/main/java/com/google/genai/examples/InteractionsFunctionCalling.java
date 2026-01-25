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
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.Schema;
import com.google.genai.types.interactions.content.FunctionCallContent;
import com.google.genai.types.interactions.content.FunctionResultContent;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.TextContent;
import com.google.genai.types.interactions.tools.Function;
import java.util.Map;

/**
 * Example: Manual Function Calling with the Interactions API
 *
 * <p>Demonstrates how to handle function calling manually with the Interactions API:
 *
 * <ol>
 *   <li>Define function tools with Schema
 *   <li>Send initial request with function declarations
 *   <li>Extract function calls from the response
 *   <li>Execute functions in your application code
 *   <li>Send function results back using previousInteractionId
 *   <li>Receive the final response
 * </ol>
 *
 * <p>Note: The Interactions API does not implement Automatic Function Calling (AFC).
 * All function execution must be handled manually by the application.
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsFunctionCalling {

  public static void main(String[] args) throws Exception {
    // Instantiate the client. The client gets the API key from the environment variable
    // `GOOGLE_API_KEY`.
    //
    Client client = new Client();

    System.out.println("=== Interactions API: Function Calling Example ===\n");

    manualFunctionCallingExample(client);

    System.out.println("\n=== Example completed ===");
  }

  /**
   * Demonstrates manual function calling with the Interactions API.
   *
   * <p>In manual mode, you define the function schema and handle function calls yourself.
   */
  private static void manualFunctionCallingExample(Client client) {
    // ===== STEP 1: Define a Function with Schema =====
    System.out.println("STEP 1: Define the get_weather function using Schema\n");

    // Define the function tool with parameters schema
    // Note: Using lowercase type strings ("object", "string") for API compatibility
    Function weatherTool =
        Function.builder()
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

    System.out.println("Function defined: " + weatherTool.name().orElse("N/A") + "\n");

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

    System.out.println("Response received. Interaction ID: " + response1.id());
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
    System.out.println("  ID: " + functionCall.id());
    System.out.println("  Name: " + functionCall.name());
    System.out.println("  Arguments: " + functionCall.arguments());
    System.out.println();

    // Execute the function (simulated)
    Map<String, Object> weatherResult =
        executeGetWeather(functionCall.arguments());
    System.out.println("Function result: " + weatherResult);
    System.out.println();

    // ===== STEP 4: Send function result back to the model =====
    System.out.println("---\n");
    System.out.println("STEP 4: Send function result back to the model\n");

    FunctionResultContent functionResult =
        FunctionResultContent.builder()
            .id(functionCall.id())
            .name(functionCall.name())
            .result(weatherResult)
            .build();

    CreateInteractionConfig config2 =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .inputFromContents(functionResult)
            .previousInteractionId(response1.id())
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
      for (Content output : interaction.outputs().get()) {
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
      for (Content output : interaction.outputs().get()) {
        if (output instanceof TextContent) {
          System.out.println(((TextContent) output).text().orElse("(empty)"));
        } else if (output instanceof FunctionCallContent) {
          FunctionCallContent fc = (FunctionCallContent) output;
          System.out.println("[Function Call: " + fc.name() + "]");
        }
      }
    }
  }

  private InteractionsFunctionCalling() {}
}
