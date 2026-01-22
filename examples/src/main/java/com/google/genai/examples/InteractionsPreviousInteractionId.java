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
 * -Dexec.mainClass="com.google.genai.examples.InteractionsPreviousInteractionId"
 */
package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.GetInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.TextContent;

/**
 * Example 4: Conversation Continuity with previousInteractionId
 *
 * <p>Demonstrates how to link interactions using previousInteractionId to maintain conversation
 * continuity across separate API calls.
 *
 * <p>This example shows: - Making a first interaction and retrieving its ID - Creating a follow-up
 * interaction using `.previousInteractionId()` - How the API maintains conversation context across
 * separate requests
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsPreviousInteractionId {

  public static void main(String[] args) {
    // Instantiate the client. The client gets the API key from the environment variable
    // `GOOGLE_API_KEY`.
    //
    Client client = new Client();

    System.out.println("=== Example 4: Conversation Continuity with previousInteractionId ===\n");

    // ===== FIRST INTERACTION =====
    String input1 = "Tell me an interesting fact about quantum physics.";
    System.out.println("FIRST INTERACTION:");
    System.out.println("REQUEST:");
    System.out.println("  Model: gemini-2.5-flash");
    System.out.println("  Input: " + input1);
    System.out.println();

    CreateInteractionConfig config1 =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .input(input1)
            .build();

    System.out.println("REQUEST JSON:");
    System.out.println(config1.toJson());
    System.out.println();

    Interaction firstResponse = client.interactions.create(config1);

    System.out.println("RESPONSE JSON:");
    System.out.println(firstResponse.toJson());
    System.out.println();
    System.out.println("RESPONSE:");
    System.out.println("  Status: " + firstResponse.status());
    System.out.println("  Interaction ID: " + firstResponse.id());
    System.out.print("  Output: ");
    printOutputs(firstResponse);

    // ===== SECOND INTERACTION (LINKED TO FIRST) =====
    System.out.println("\n---\n");
    String input2 = "Can you explain that in simpler terms?";
    System.out.println("SECOND INTERACTION (LINKED):");
    System.out.println("REQUEST:");
    System.out.println("  Model: gemini-2.5-flash");
    System.out.println("  Input: " + input2);
    System.out.println("  Previous Interaction ID: " + firstResponse.id());
    System.out.println();

    CreateInteractionConfig config2 =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .input(input2)
            .previousInteractionId(firstResponse.id()) // Link to previous interaction
            .build();

      System.out.println("REQUEST JSON:");
      System.out.println(config2.toJson());
      System.out.println();

      Interaction secondResponse = client.interactions.create(config2);

      System.out.println("RESPONSE JSON:");
      System.out.println(secondResponse.toJson());
      System.out.println();
      System.out.println("RESPONSE:");
      System.out.println("  Status: " + secondResponse.status());
      System.out.println("  Interaction ID: " + secondResponse.id());
      System.out.print("  Output: ");
      printOutputs(secondResponse);

    // ===== GET INTERACTION (RETRIEVE SECOND INTERACTION BY ID) =====
    System.out.println("\n---\n");
    System.out.println("GET INTERACTION (RETRIEVE BY ID):");
    System.out.println("REQUEST:");
    System.out.println("  Interaction ID: " + secondResponse.id());
    System.out.println();

    Interaction retrievedInteraction =
        client.interactions.get(secondResponse.id(), GetInteractionConfig.builder().build());

    System.out.println("RESPONSE JSON:");
    System.out.println(retrievedInteraction.toJson());
    System.out.println();
    System.out.println("RESPONSE:");
    System.out.println("  Status: " + retrievedInteraction.status());
    System.out.println("  Interaction ID: " + retrievedInteraction.id());
    System.out.println(
        "  Previous Interaction ID: " + retrievedInteraction.previousInteractionId().orElse("N/A"));
    System.out.print("  Output: ");
    printOutputs(retrievedInteraction);

    System.out.println("\n=== Example completed ===");
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
          break;
        }
      }
    }
  }
}
