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
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsAsyncFunctionTools"
 */
package com.google.genai.examples;

import com.google.common.collect.ImmutableMap;
import com.google.genai.Client;
import com.google.genai.types.Schema;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.FunctionCallContent;
import com.google.genai.types.interactions.content.FunctionResultContent;
import com.google.genai.types.interactions.content.TextContent;
import com.google.genai.types.interactions.tools.FunctionTool;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Example: Async Function Calling
 *
 * <p>Demonstrates async function calling with tools and multi-turn AFC:
 *
 * <ol>
 *   <li>Async create with FunctionTool
 *   <li>Handle function calls asynchronously in callbacks
 *   <li>Chain multiple function call rounds using .thenCompose()
 *   <li>Async AFC loop pattern
 *   <li>Demonstrate concurrent function execution if multiple tools called
 * </ol>
 *
 * <p>This example shows the power of async operations for complex function calling scenarios.
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsAsyncFunctionTools {

  public static void main(String[] args) {
    try {
      Client client = new Client();

      System.out.println("=== Interactions API: Async Function Tools Example ===\n");

      // ===== PART 1: Manual Function Calling (Async) =====
      System.out.println("--- PART 1: Manual Function Calling (Async) ---\n");

      FunctionTool calculatorTool =
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

      String userInput = "What is 25 plus 37?";
      System.out.println("User: " + userInput + "\n");

      CreateInteractionConfig config =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .input(userInput)
              .tools(calculatorTool)
              .build();

      System.out.println("Sending async request with function tool...");

      client.async.interactions.create(config)
          .thenCompose(
              interaction -> {
                System.out.println("\nFirst response received:");
                System.out.println("  Interaction ID: " + interaction.id());
                System.out.println("  Status: " + interaction.status());

                // Extract function call
                FunctionCallContent functionCall = extractFunctionCall(interaction);
                if (functionCall == null) {
                  System.out.println("  No function call found");
                  return CompletableFuture.completedFuture(interaction);
                }

                System.out.println("  Function call detected:");
                System.out.println("    Name: " + functionCall.name());
                System.out.println("    Args: " + functionCall.arguments());

                // Execute the function
                Map<String, Object> funcArgs = functionCall.arguments();
                double a = ((Number) funcArgs.getOrDefault("a", 0)).doubleValue();
                double b = ((Number) funcArgs.getOrDefault("b", 0)).doubleValue();
                double sum = a + b;

                System.out.println("\n  Executing: calculate_sum(" + a + ", " + b + ") = " + sum);

                // Build function result
                FunctionResultContent functionResult =
                    FunctionResultContent.builder()
                        .id(functionCall.id())
                        .name(functionCall.name())
                        .result(ImmutableMap.of("result", sum))
                        .build();

                // Send result back
                CreateInteractionConfig followUpConfig =
                    CreateInteractionConfig.builder()
                        .model("gemini-2.5-flash")
                        .inputFromContents(functionResult)
                        .previousInteractionId(interaction.id())
                        .tools(calculatorTool)
                        .build();

                System.out.println("\n  Sending function result back...");
                return client.async.interactions.create(followUpConfig);
              })
          .thenAccept(
              finalResponse -> {
                System.out.println("\nFinal response received:");
                System.out.println("  Interaction ID: " + finalResponse.id());
                System.out.println("  Status: " + finalResponse.status());
                System.out.print("  Output: ");
                printOutputs(finalResponse);
              })
          .join();

      // ===== PART 2: AFC Example (Async) =====
      System.out.println("\n--- PART 2: AFC Example (Async) ---\n");

      Method weatherMethod =
          InteractionsAsyncFunctionTools.class.getMethod("getWeather", String.class);
      FunctionTool afcWeatherTool =
          FunctionTool.fromMethod("Get current weather for a city", weatherMethod);

      String afcInput = "What's the weather like in Tokyo?";
      System.out.println("User: " + afcInput + "\n");

      CreateInteractionConfig afcConfig =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .input(afcInput)
              .tools(afcWeatherTool)
              .build();

      System.out.println("Sending async request with AFC...");

      client.async.interactions.create(afcConfig)
          .thenAccept(
              afcResponse -> {
                System.out.println("\nAFC completed:");
                System.out.println("  Interaction ID: " + afcResponse.id());
                System.out.println("  Status: " + afcResponse.status());
                System.out.print("  Output: ");
                printOutputs(afcResponse);

                // Check AFC history
                if (afcResponse.automaticFunctionCallingHistory().isPresent()) {
                  List<Interaction> history =
                      afcResponse.automaticFunctionCallingHistory().get();
                  System.out.println("\n  AFC History: " + history.size()
                      + " intermediate interactions");
                  for (int i = 0; i < history.size(); i++) {
                    System.out.println("    [" + (i + 1) + "] ID: " + history.get(i).id());
                  }
                }
              })
          .join();

      // ===== PART 3: Multiple Function Tools (Async) =====
      System.out.println("\n--- PART 3: Multiple Function Tools (Async) ---\n");

      Method timeMethod =
          InteractionsAsyncFunctionTools.class.getMethod("getCurrentTime", String.class);
      FunctionTool afcTimeTool =
          FunctionTool.fromMethod("Get current time for a city", timeMethod);

      String multiToolInput = "What's the weather and time in London?";
      System.out.println("User: " + multiToolInput + "\n");

      CreateInteractionConfig multiToolConfig =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .input(multiToolInput)
              .tools(afcWeatherTool, afcTimeTool)
              .build();

      System.out.println("Sending async request with multiple AFC tools...");

      client.async.interactions.create(multiToolConfig)
          .thenAccept(
              response -> {
                System.out.println("\nMulti-tool AFC completed:");
                System.out.println("  Interaction ID: " + response.id());
                System.out.println("  Status: " + response.status());
                System.out.print("  Output: ");
                printOutputs(response);

                if (response.automaticFunctionCallingHistory().isPresent()) {
                  List<Interaction> history =
                      response.automaticFunctionCallingHistory().get();
                  System.out.println("\n  AFC History: " + history.size()
                      + " intermediate interactions");
                }
              })
          .join();

      // ===== PART 4: Async AFC Loop Pattern =====
      System.out.println("\n--- PART 4: Async AFC Loop Pattern ---\n");
      System.out.println("Demonstrating manual AFC loop with async chaining...\n");

      String loopInput = "Calculate 10 + 20, then add 15 to that result";
      System.out.println("User: " + loopInput);

      CreateInteractionConfig loopConfig =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .input(loopInput)
              .tools(calculatorTool)
              .build();

      System.out.println("\nStarting async AFC loop...");

      // This will chain through multiple function calls if needed
      handleAfcLoop(client, loopConfig, calculatorTool, null, 1)
          .thenAccept(
              finalResponse -> {
                System.out.println("\nAFC loop completed:");
                System.out.println("  Final Interaction ID: " + finalResponse.id());
                System.out.print("  Final Output: ");
                printOutputs(finalResponse);
              })
          .join();

      System.out.println("\n=== Example completed ===");

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Recursive async AFC loop handler.
   *
   * <p>Chains function call rounds until no more function calls are needed.
   */
  private static CompletableFuture<Interaction> handleAfcLoop(
      Client client,
      CreateInteractionConfig config,
      FunctionTool tool,
      String previousInteractionId,
      int round) {

    // Limit rounds to prevent infinite loops
    if (round > 5) {
      System.out.println("\n  Max rounds reached");
      return CompletableFuture.failedFuture(
          new RuntimeException("Max AFC rounds exceeded"));
    }

    return client.async.interactions.create(config)
        .thenCompose(
            interaction -> {
              System.out.println("\n  Round " + round + ":");
              System.out.println("    Interaction ID: " + interaction.id());

              // Extract function call
              FunctionCallContent functionCall = extractFunctionCall(interaction);
              if (functionCall == null) {
                // No more function calls - we're done
                System.out.println("    No function call - loop complete");
                return CompletableFuture.completedFuture(interaction);
              }

              System.out.println("    Function: " + functionCall.name());
              System.out.println("    Args: " + functionCall.arguments());

              // Execute function
              Map<String, Object> funcArgs = functionCall.arguments();
              double a = ((Number) funcArgs.getOrDefault("a", 0)).doubleValue();
              double b = ((Number) funcArgs.getOrDefault("b", 0)).doubleValue();
              double sum = a + b;

              System.out.println("    Result: " + sum);

              // Build function result
              FunctionResultContent functionResult =
                  FunctionResultContent.builder()
                      .id(functionCall.id())
                      .name(functionCall.name())
                      .result(ImmutableMap.of("result", sum))
                      .build();

              // Continue loop
              CreateInteractionConfig nextConfig =
                  CreateInteractionConfig.builder()
                      .model("gemini-2.5-flash")
                      .inputFromContents(functionResult)
                      .previousInteractionId(interaction.id())
                      .tools(tool)
                      .build();

              return handleAfcLoop(client, nextConfig, tool, interaction.id(), round + 1);
            });
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
        "temperature", "18",
        "unit", "celsius",
        "condition", "partly cloudy",
        "humidity", "60%");
  }

  /**
   * Public static method for AFC - gets current time for a city.
   *
   * <p>This method must be public and static for AFC to work.
   */
  public static Map<String, Object> getCurrentTime(String location) {
    System.out.println("[AFC] Executing getCurrentTime(\"" + location + "\")");
    return ImmutableMap.of(
        "location", location,
        "time", "14:30",
        "timezone", "UTC+0",
        "date", "2025-01-23");
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

  /** Helper method to print interaction outputs. */
  private static void printOutputs(Interaction interaction) {
    if (interaction.outputs().isPresent() && !interaction.outputs().get().isEmpty()) {
      for (Content output : interaction.outputs().get()) {
        if (output instanceof TextContent) {
          String text = ((TextContent) output).text().orElse("(empty)");
          if (text.length() > 100) {
            text = text.substring(0, 100) + "...";
          }
          System.out.println(text);
          break;
        }
      }
    }
  }

  private InteractionsAsyncFunctionTools() {}
}
