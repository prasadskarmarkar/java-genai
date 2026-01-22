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
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsAsyncMultiTurn"
 */
package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.Turn;
import com.google.genai.types.interactions.content.TextContent;
import java.util.concurrent.CompletableFuture;

/**
 * Example 5: Async Multi-Turn Interaction
 *
 * <p>Demonstrates the same multi-turn conversation pattern using the async API.
 *
 * <p>This example shows: - Using `client.async.interactions.create()` for async operations -
 * Multi-turn conversations with Turn in async mode - Using CompletableFuture to handle
 * async responses - Waiting for async operations to complete with `.join()`
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsAsyncMultiTurn {

  public static void main(String[] args) {
    // Instantiate the client. The client gets the API key from the environment variable
    // `GOOGLE_API_KEY`.
    //
    Client client = new Client();

    System.out.println("=== Example 5: Async Multi-Turn Interaction ===\n");

    // Build a multi-turn conversation config
    String turn1User = "What is machine learning?";
    String turn2Model =
        "Machine learning is a branch of artificial intelligence that"
            + " enables computers to learn from data without being"
            + " explicitly programmed.";
    String turn3User = "What are some real-world applications?";

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .inputFromTurns(
                // First user turn
                Turn.builder()
                    .role("user")
                    .content(TextContent.builder().text(turn1User).build())
                    .build(),
                // Model response (simulating previous conversation)
                Turn.builder()
                    .role("model")
                    .content(TextContent.builder().text(turn2Model).build())
                    .build(),
                // Follow-up user turn
                Turn.builder()
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
    System.out.println("  Execution: Async");
    System.out.println();

    // Execute the interaction asynchronously
    System.out.println("Sending async request...");
    CompletableFuture<Interaction> future = client.async.interactions.create(config);

    // Attach a callback to handle the response when it arrives
    future.thenAccept(
        interaction -> {
          System.out.println("\nRESPONSE:");
          System.out.println("  Status: " + interaction.status());
          System.out.println("  Interaction ID: " + interaction.id());
          System.out.print("  Output: ");
          printOutputs(interaction);
        });

    // Wait for the async operation to complete
    try {
      future.join();
    } catch (Exception e) {
      System.out.println("Async operation failed: " + e.getMessage());
      e.printStackTrace();
    }

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
