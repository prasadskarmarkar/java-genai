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
 * <p>IMPORTANT: Computer Use tool requires special API access and only works with
 * gemini-3-pro-preview model.
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
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsComputerUseMultiContent"
 */
package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.GetInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.TextContent;
import com.google.genai.types.interactions.tools.ComputerUse;

/**
 * Example: ComputerUse with Multiple Content Types
 *
 * <p>Demonstrates ComputerUse with multiple text content items for browser automation tasks.
 *
 * <p>Note: Computer Use tool requires special API access and only works with gemini-3-pro-preview
 * model. The Interactions API is in beta and subject to change.
 */
public final class InteractionsComputerUseMultiContent {

  public static void main(String[] args) {
    try {
      // Instantiate the client. The client gets the API key from the environment variable
      // `GOOGLE_API_KEY`.
      //
      Client client = new Client();

      System.out.println("=== Interactions API: ComputerUse Example ===\n");
      System.out.println("Note: Computer Use tool requires special API access.\n");

      // ===== Configure ComputerUse =====
      System.out.println("--- Configuring ComputerUse ---\n");

      ComputerUse computerUseTool = ComputerUse.builder()
          .environment("browser")
          .build();
      System.out.println("ComputerUse configured with environment: browser");

      // Also demonstrate with excludedPredefinedFunctions
      ComputerUse computerUseToolWithExclusions = ComputerUse.builder()
          .environment("browser")
          .excludedPredefinedFunctions("screenshot", "click")
          .build();
      System.out.println("ComputerUse with exclusions: [screenshot, click]\n");

      // ===== Create Multiple Content Items =====
      System.out.println("--- Creating Multiple Content Items ---\n");

      TextContent textContent1 = TextContent.builder()
          .text("I need you to help me with a web task.")
          .build();
      System.out.println("TextContent 1: Task introduction");

      TextContent textContent2 = TextContent.builder()
          .text("Open a browser and go to https://www.google.com")
          .build();
      System.out.println("TextContent 2: Navigation instruction");

      TextContent textContent3 = TextContent.builder()
          .text("Then search for 'Java programming tutorial'")
          .build();
      System.out.println("TextContent 3: Search instruction\n");

      // ===== Create Interaction =====
      System.out.println("--- Creating Interaction ---\n");

      CreateInteractionConfig config =
          CreateInteractionConfig.builder()
              .model("gemini-3-pro-preview")
              .inputFromContents(
                  textContent1,
                  textContent2,
                  textContent3)
              .tools(computerUseTool)
              .build();

      Interaction response = client.interactions.create(config);
      String interactionId = response.id();

      System.out.println("Response received. Interaction ID: " + interactionId);
      System.out.println("Status: " + response.status());
      processOutputs(response);

      // ===== Verify with Get Interaction =====
      System.out.println("\n--- Verify with Get Interaction ---\n");

      GetInteractionConfig getConfig = GetInteractionConfig.builder().build();
      Interaction retrieved = client.interactions.get(interactionId, getConfig);

      System.out.println("Retrieved interaction ID: " + retrieved.id());
      System.out.println("Status: " + retrieved.status());
      if (retrieved.outputs().isPresent()) {
        System.out.println("Outputs count: " + retrieved.outputs().get().size());
        System.out.println("Output types:");
        for (Content output : retrieved.outputs().get()) {
          System.out.println("  - " + output.getClass().getSimpleName());
        }
      }

      System.out.println("\n=== Example completed ===");

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      System.err.println("\nNote: Computer Use tool requires special API access.");
      System.err.println("This error may be expected if your API key doesn't have");
      System.err.println("Computer Use permissions enabled.");
      e.printStackTrace();
    }
  }

  /** Processes and prints interaction outputs. */
  private static void processOutputs(Interaction interaction) {
    if (!interaction.outputs().isPresent()) {
      return;
    }

    for (Content output : interaction.outputs().get()) {
      if (output instanceof TextContent) {
        TextContent text = (TextContent) output;
        String textValue = text.text().orElse("(empty)");
        if (textValue.length() > 300) {
          textValue = textValue.substring(0, 300) + "... [truncated]";
        }
        System.out.println("\nModel: " + textValue);
      } else {
        // ComputerUse results come back as various content types
        System.out.println("\n" + output.getClass().getSimpleName());
      }
    }
  }

  private InteractionsComputerUseMultiContent() {}
}
