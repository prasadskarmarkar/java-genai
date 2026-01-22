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
 * <p>mvn exec:java
 * -Dexec.mainClass="com.google.genai.examples.InteractionsMultipleContents"
 */
package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.CreateInteractionConfig;
import com.google.genai.types.Interaction;
import com.google.genai.types.interactions.content.InteractionContent;
import com.google.genai.types.interactions.content.TextContent;

/**
 * Example 2: Single Turn with Multiple Content Items
 *
 * <p>Demonstrates using InteractionContent objects to send multiple content items in a single turn.
 *
 * <p>This example shows: - Using `.inputFromContents(InteractionContent...)` for multiple content
 * items - Creating multiple TextContent objects - Sending multiple pieces of information in one
 * request
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsMultipleContents {

  public static void main(String[] args) {
    // Instantiate the client. The client gets the API key from the environment variable
    // `GOOGLE_API_KEY`.
    //
    // Note: Interactions API is currently only available in Gemini Developer API (not Vertex AI).
    Client client = new Client();

    System.out.println("=== Example 2: Single Turn with Multiple Content Items ===\n");

    // Create an interaction with multiple content items in a single turn
    String content1 = "Tell me about the Eiffel Tower.";
    String content2 = "Keep it brief, under 50 words.";
    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .inputFromContents(
                TextContent.builder().text(content1).build(),
                TextContent.builder().text(content2).build())
            .build();

    // Print request details
    System.out.println("REQUEST:");
    System.out.println("  Model: gemini-2.5-flash");
    System.out.println("  Content 1: " + content1);
    System.out.println("  Content 2: " + content2);
    System.out.println();
    System.out.println("REQUEST JSON:");
    System.out.println(config.toJson());
    System.out.println();

    // Execute the interaction
    Interaction response = client.interactions.create(config);

    // Print response details
    System.out.println("RESPONSE JSON:");
    System.out.println(response.toJson());
    System.out.println();
    System.out.println("RESPONSE:");
    System.out.println("  Status: " + response.status());
    System.out.println("  Interaction ID: " + response.id().orElse("N/A"));
    System.out.print("  Output: ");
    printOutputs(response);

    System.out.println("\n=== Example completed ===");
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
          break;
        }
      }
    }
  }
}
