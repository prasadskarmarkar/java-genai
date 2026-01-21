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
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsMultiTurnConversation"
 */
package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.CreateInteractionConfig;
import com.google.genai.types.Interaction;
import com.google.genai.types.interactions.InteractionContent;
import com.google.genai.types.interactions.InteractionTurn;
import com.google.genai.types.interactions.TextContent;

/**
 * Example 3: Multi-Turn Conversation with InteractionTurn
 *
 * <p>Demonstrates a multi-turn conversation using InteractionTurn objects with proper role
 * assignment. This shows how context from previous turns influences the response.
 *
 * <p>This example shows: - Using `.inputFromTurns(InteractionTurn...)` for multi-turn
 * conversations - Creating InteractionTurn objects with "user" and "model" roles - Building
 * conversation history - How context from previous turns affects the model's response
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsMultiTurnConversation {

  public static void main(String[] args) {
    // Instantiate the client. The client gets the API key from the environment variable
    // `GOOGLE_API_KEY`.
    //
    // Note: Interactions API is currently only available in Gemini Developer API (not Vertex AI).
    Client client = new Client();

    System.out.println("=== Example 3: Multi-Turn Conversation ===\n");

    // Build a conversation history with multiple turns
    String turn1User = "I'm planning a trip to Paris.";
    String turn2Model =
        "That's wonderful! Paris is a beautiful city with rich history and"
            + " culture. What would you like to know about your trip?";
    String turn3User = "What are the top 3 must-see attractions?";

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .inputFromTurns(
                // First user turn
                InteractionTurn.builder()
                    .role("user")
                    .content(TextContent.builder().text(turn1User).build())
                    .build(),
                // Model response (simulating previous conversation)
                InteractionTurn.builder()
                    .role("model")
                    .content(TextContent.builder().text(turn2Model).build())
                    .build(),
                // Follow-up user turn (benefits from context)
                InteractionTurn.builder()
                    .role("user")
                    .content(TextContent.builder().text(turn3User).build())
                    .build())
            .build();

    // Print request details
    System.out.println("REQUEST:");
    System.out.println("  Model: gemini-2.5-flash");
    System.out.println("  Turn 1 (user): " + turn1User);
    System.out.println("  Turn 2 (model): " + turn2Model);
    System.out.println("  Turn 3 (user): " + turn3User);
    System.out.println();

    // Execute the interaction
    Interaction response = client.interactions.create(config);

    // Print response details
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
