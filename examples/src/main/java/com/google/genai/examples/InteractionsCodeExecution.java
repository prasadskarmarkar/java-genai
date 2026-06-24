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
 * <p>export GOOGLE_API_KEY=YOUR_API_KEY
 *
 * <p>mvn clean compile
 *
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsCodeExecution"
 */
package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.TextContent;
import com.google.genai.types.interactions.steps.CodeExecutionCallStep;
import com.google.genai.types.interactions.steps.CodeExecutionResultStep;
import com.google.genai.types.interactions.steps.ModelOutputStep;
import com.google.genai.types.interactions.steps.Step;
import com.google.genai.types.interactions.tools.CodeExecution;

/**
 * Example: Code Execution Tool with the Interactions API.
 *
 * <p>In the step-based response model, code execution calls appear as {@link CodeExecutionCallStep}
 * and results as {@link CodeExecutionResultStep} in the interaction's {@code steps} list. The final
 * model text output arrives in a {@link ModelOutputStep}.
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsCodeExecution {

  public static void main(String[] args) {
    Client client = new Client();

    System.out.println("=== Interactions API: Code Execution Tool Example ===\n");

    CodeExecution codeTool = CodeExecution.builder().build();

    String userQuestion =
        "Calculate the first 20 Fibonacci numbers and find their sum. "
            + "Show me the code and the result.";

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model("gemini-3-flash-preview")
            .input(userQuestion)
            .tools(codeTool)
            .build();

    System.out.println("=== REQUEST ===");
    System.out.println(config.toJson());
    System.out.println();

    Interaction response = client.interactions.create(config);

    System.out.println("=== RESPONSE ===");
    System.out.println(response.toJson());
    System.out.println();

    System.out.println("Interaction ID: " + response.id());
    System.out.println("Status: " + response.status());
    System.out.println();

    if (!response.steps().isPresent() || response.steps().get().isEmpty()) {
      System.out.println("No steps in response.");
      return;
    }

    for (Step step : response.steps().get()) {
      if (step instanceof CodeExecutionCallStep) {
        CodeExecutionCallStep callStep = (CodeExecutionCallStep) step;
        System.out.println("[CodeExecutionCall] id=" + callStep.id().orElse("?"));
        callStep.arguments().ifPresent(codeArgs -> {
          System.out.println("  Language: " + codeArgs.language().orElse("python"));
          System.out.println("  Code:");
          System.out.println("  ---");
          System.out.println("  " + codeArgs.code().orElse("(empty)"));
          System.out.println("  ---");
        });
        System.out.println();

      } else if (step instanceof CodeExecutionResultStep) {
        CodeExecutionResultStep resultStep = (CodeExecutionResultStep) step;
        System.out.println("[CodeExecutionResult] callId=" + resultStep.callId().orElse("?"));
        System.out.println("  Is Error: " + resultStep.isError().orElse(false));
        System.out.println("  Output:");
        System.out.println("  ---");
        System.out.println("  " + resultStep.result().orElse("(empty)"));
        System.out.println("  ---");
        System.out.println();

      } else if (step instanceof ModelOutputStep) {
        ModelOutputStep outputStep = (ModelOutputStep) step;
        System.out.println("[ModelOutput]");
        outputStep.content().ifPresent(contents -> {
          for (com.google.genai.types.interactions.content.Content c : contents) {
            if (c instanceof TextContent) {
              System.out.println("  " + ((TextContent) c).text().orElse("(empty)"));
            }
          }
        });
        System.out.println();
      }
    }

    System.out.println("=== Example completed ===");
  }

  private InteractionsCodeExecution() {}
}
