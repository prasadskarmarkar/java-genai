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
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsFunctionTools"
 */
package com.google.genai.examples;

import com.google.common.collect.ImmutableMap;
import com.google.genai.Client;
import com.google.genai.types.Schema;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.GetInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.FunctionCallContent;
import com.google.genai.types.interactions.content.FunctionResultContent;
import com.google.genai.types.interactions.content.TextContent;
import com.google.genai.types.interactions.content.ThoughtContent;
import com.google.genai.types.interactions.tools.FunctionTool;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Example: FunctionTool with Manual and AFC Configuration
 *
 * <p>Demonstrates two approaches to configuring FunctionTool:
 *
 * <ol>
 *   <li><b>Manual Configuration</b>: Define function with explicit Schema
 *   <li><b>AFC Configuration</b>: Create FunctionTool from Java method for automatic function
 *       calling
 * </ol>
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsFunctionTools {

  public static void main(String[] args) {
    try {
      // Instantiate the client. The client gets the API key from the environment variable
      // `GOOGLE_API_KEY`.
      //
      Client client = new Client();

      System.out.println("=== Interactions API: FunctionTool Example ===\n");

      // ===== PART 1: Manual FunctionTool Configuration =====
      System.out.println("--- PART 1: Manual FunctionTool Configuration ---\n");

      FunctionTool manualFunctionTool =
          FunctionTool.builder()
              .name("calculate_sum")
              .description("Calculate the sum of two numbers")
              .parameters(
                  Schema.builder()
                      .type("object")
                      .properties(
                          Map.of(
                              "a",
                              Schema.builder()
                                  .type("number")
                                  .description("First number to add")
                                  .build(),
                              "b",
                              Schema.builder()
                                  .type("number")
                                  .description("Second number to add")
                                  .build()))
                      .required("a", "b")
                      .build())
              .build();

      System.out.println("FunctionTool defined: calculate_sum\n");

      // ===== PART 2: AFC FunctionTool Configuration =====
      System.out.println("--- PART 2: AFC FunctionTool Configuration ---\n");

      Method weatherMethod =
          InteractionsFunctionTools.class.getMethod("getWeather", String.class);
      FunctionTool afcFunctionTool =
          FunctionTool.fromMethod("Get current weather for a city", weatherMethod);

      System.out.println("AFC FunctionTool created: " + afcFunctionTool.name().orElse("N/A"));
      System.out.println("AFC enabled: " + afcFunctionTool.method().isPresent() + "\n");

      // ===== PART 3: Manual Function Calling =====
      System.out.println("--- PART 3: Manual Function Calling (Create API) ---\n");

      String userInput = "Calculate the sum of 15 and 27";
      System.out.println("User: " + userInput + "\n");

      CreateInteractionConfig config =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .input(userInput)
              .tools(manualFunctionTool)
              .build();

      Interaction response1 = client.interactions.create(config);
      String interactionId = response1.id();

      System.out.println("CREATE Response:");
      System.out.println("  Interaction ID: " + interactionId);
      System.out.println("  Status: " + response1.status());
      printOutputsWithTypes(response1);

      // Extract function call
      FunctionCallContent functionCall = extractFunctionCall(response1);
      if (functionCall != null) {
        // Execute the function
        Map<String, Object> funcArgs = functionCall.arguments();
        double a = ((Number) funcArgs.getOrDefault("a", 0)).doubleValue();
        double b = ((Number) funcArgs.getOrDefault("b", 0)).doubleValue();
        double sum = a + b;
        Map<String, Object> result = ImmutableMap.of("result", sum);

        System.out.println("\nExecuting locally: calculate_sum(" + a + ", " + b + ") = " + sum);

        // ===== PART 4: Send Function Result Back =====
        System.out.println("\n--- PART 4: Send Function Result Back (Create API) ---\n");

        FunctionResultContent functionResult =
            FunctionResultContent.builder()
                .id(functionCall.id())
                .name(functionCall.name())
                .result(result)
                .build();

        CreateInteractionConfig config2 =
            CreateInteractionConfig.builder()
                .model("gemini-2.5-flash")
                .inputFromContents(functionResult)
                .previousInteractionId(interactionId)
                .tools(manualFunctionTool)
                .build();

        Interaction response2 = client.interactions.create(config2);

        System.out.println("CREATE Response:");
        System.out.println("  Interaction ID: " + response2.id());
        System.out.println("  Status: " + response2.status());
        printOutputsWithTypes(response2);

        // ===== PART 5: Get API to verify =====
        System.out.println("\n--- PART 5: Verify with Get API ---\n");

        GetInteractionConfig getConfig = GetInteractionConfig.builder().build();
        Interaction retrieved = client.interactions.get(response2.id(), getConfig);

        System.out.println("GET Response:");
        System.out.println("  Interaction ID: " + retrieved.id());
        System.out.println("  Status: " + retrieved.status());
        System.out.println("  Model: " + retrieved.model().orElse("N/A"));
        printOutputsWithTypes(retrieved);
      }

      // ===== PART 6: AFC Example =====
      System.out.println("\n--- PART 6: AFC Example (Automatic Function Calling) ---\n");

      String afcInput = "What's the weather like in San Francisco?";
      System.out.println("User: " + afcInput + "\n");

      CreateInteractionConfig afcConfig =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .input(afcInput)
              .tools(afcFunctionTool)
              .build();

      Interaction afcResponse = client.interactions.create(afcConfig);

      System.out.println("\nCREATE Response (after AFC):");
      System.out.println("  Interaction ID: " + afcResponse.id());
      System.out.println("  Status: " + afcResponse.status());
      printOutputsWithTypes(afcResponse);

      // Check AFC history
      if (afcResponse.automaticFunctionCallingHistory().isPresent()) {
        List<Interaction> history = afcResponse.automaticFunctionCallingHistory().get();
        System.out.println("\n  AFC History: " + history.size() + " intermediate interactions");
        for (int i = 0; i < history.size(); i++) {
          System.out.println("    [" + (i + 1) + "] ID: " + history.get(i).id());
        }
      }

      System.out.println("\n=== Example completed ===");

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Public static method for AFC - gets weather for a city.
   *
   * <p>This method must be public and static for AFC to work.
   */
  public static Map<String, Object> getWeather(String location) {
    System.out.println("[AFC] Executing getWeather(\"" + location + "\")");
    return ImmutableMap.of(
        "location", location,
        "temperature", "22",
        "unit", "celsius",
        "condition", "sunny",
        "humidity", "45%");
  }

  /** Extracts the first FunctionCallContent from an interaction response. */
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

  /** Prints all output content types with their details. */
  private static void printOutputsWithTypes(Interaction interaction) {
    System.out.println("  Output Content Types:");
    if (!interaction.outputs().isPresent() || interaction.outputs().get().isEmpty()) {
      System.out.println("    (none)");
      return;
    }

    for (Content output : interaction.outputs().get()) {
      String typeName = output.getClass().getSimpleName();

      if (output instanceof TextContent) {
        TextContent t = (TextContent) output;
        String text = t.text().orElse("(empty)");
        if (text.length() > 100) {
          text = text.substring(0, 100) + "...";
        }
        System.out.println("    - " + typeName + ": \"" + text + "\"");
      } else if (output instanceof FunctionCallContent) {
        FunctionCallContent fc = (FunctionCallContent) output;
        System.out.println("    - " + typeName + ":");
        System.out.println("        name: " + fc.name());
        System.out.println("        args: " + fc.arguments());
      } else if (output instanceof ThoughtContent) {
        ThoughtContent tc = (ThoughtContent) output;
        System.out.println("    - " + typeName + ":");
        System.out.println("        signature: " + tc.signature().orElse("(none)"));
        if (tc.summary().isPresent()) {
          System.out.println("        summaries: " + tc.summary().get().size());
        }
      } else {
        System.out.println("    - " + typeName);
      }
    }
  }

  private InteractionsFunctionTools() {}
}
