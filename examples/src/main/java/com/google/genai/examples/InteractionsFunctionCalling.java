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

package com.google.genai.examples;

import com.google.common.collect.ImmutableMap;
import com.google.genai.Client;
import com.google.genai.types.Schema;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.FunctionResultContent;
import com.google.genai.types.interactions.content.TextContent;
import com.google.genai.types.interactions.steps.FunctionCallStep;
import com.google.genai.types.interactions.steps.ModelOutputStep;
import com.google.genai.types.interactions.steps.Step;
import com.google.genai.types.interactions.tools.Function;
import java.util.List;
import java.util.Map;

/**
 * Example: Function Calling with the Interactions API.
 *
 * <p>In the step-based response model, function calls appear as {@link FunctionCallStep} in the
 * interaction's {@code steps} list (not inside {@code ModelOutputStep.content}). After receiving a
 * function call step, execute the function and send the result back as a
 * {@link FunctionResultContent} in the next request.
 *
 * <p>To run this example:
 * <ol>
 *   <li>Set: {@code export GOOGLE_API_KEY=YOUR_API_KEY}
 *   <li>Compile: {@code mvn clean compile}
 *   <li>Run: {@code mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsFunctionCalling"}
 * </ol>
 *
 * <p><b>Note:</b> The Interactions API is currently in beta.
 */
public final class InteractionsFunctionCalling {

  public static void main(String[] args) {
    Client client = new Client();

    System.out.println("=== Interactions API: Function Calling Example ===\n");

    try {
      // [START interactions_function_declaration]
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
                                  .description("The city name")
                                  .build()))
                      .required("location")
                      .build())
              .build();
      // [END interactions_function_declaration]

      // [START interactions_function_calling]
      // STEP 1: send the initial request
      CreateInteractionConfig config1 =
          CreateInteractionConfig.builder()
              .model("gemini-3-flash-preview")
              .input("What's the weather like in Paris?")
              .tools(weatherTool)
              .build();

      System.out.println("=== REQUEST 1 ===");
      System.out.println(config1.toJson());
      System.out.println();

      Interaction response1 = client.interactions.create(config1);

      System.out.println("=== RESPONSE 1 ===");
      System.out.println(response1.toJson());
      System.out.println();

      // STEP 2: extract the FunctionCallStep from the steps list
      FunctionCallStep functionCall = findFunctionCallStep(response1);
      if (functionCall == null) {
        System.out.println("No function call received — model responded directly:");
        printTextOutputs(response1);
        return;
      }

      System.out.println("Function call received:");
      System.out.println("  Name: " + functionCall.name().orElse("(unknown)"));
      System.out.println("  ID:   " + functionCall.id().orElse("(unknown)"));
      System.out.println("  Args: " + functionCall.arguments().orElse(Map.of()));

      // STEP 3: execute the function and send the result back
      Map<String, Object> weatherResult =
          executeGetWeather(functionCall.arguments().orElse(Map.of()));
      System.out.println("\nFunction result: " + weatherResult);

      FunctionResultContent functionResult =
          FunctionResultContent.builder()
              .id(functionCall.id().orElse(""))
              .name(functionCall.name().orElse(""))
              .result(weatherResult)
              .build();

      CreateInteractionConfig config2 =
          CreateInteractionConfig.builder()
              .model("gemini-3-flash-preview")
              .inputFromContents(functionResult)
              .previousInteractionId(response1.id())
              .tools(weatherTool)
              .build();

      System.out.println("\n=== REQUEST 2 ===");
      System.out.println(config2.toJson());
      System.out.println();

      Interaction response2 = client.interactions.create(config2);
      // [END interactions_function_calling]

      System.out.println("=== RESPONSE 2 ===");
      System.out.println(response2.toJson());
      System.out.println();

      System.out.println("Final answer:");
      printTextOutputs(response2);

      System.out.println("\n=== Example completed ===");

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /** Searches the interaction steps for the first FunctionCallStep. */
  private static FunctionCallStep findFunctionCallStep(Interaction interaction) {
    if (!interaction.steps().isPresent()) {
      return null;
    }
    for (Step step : interaction.steps().get()) {
      if (step instanceof FunctionCallStep) {
        return (FunctionCallStep) step;
      }
    }
    return null;
  }

  private static void printTextOutputs(Interaction interaction) {
    List<com.google.genai.types.interactions.content.Content> outputs =
        interaction.getModelOutputContents();
    if (outputs.isEmpty()) {
      System.out.println("  (no text output)");
      return;
    }
    for (com.google.genai.types.interactions.content.Content c : outputs) {
      if (c instanceof TextContent) {
        System.out.println("  " + ((TextContent) c).text().orElse("(empty)"));
      }
    }
  }

  private static Map<String, Object> executeGetWeather(Map<String, Object> arguments) {
    String location = (String) arguments.getOrDefault("location", "Unknown");
    return ImmutableMap.of(
        "location", location,
        "temperature", "22",
        "unit", "celsius",
        "condition", "sunny with a few clouds");
  }

  private InteractionsFunctionCalling() {}
}
