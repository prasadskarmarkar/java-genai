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
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsAsyncCancel"
 */
package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.interactions.CancelInteractionConfig;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.GetInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.TextContent;
import java.util.concurrent.CompletableFuture;

/**
 * Example: Async Cancel Operations
 *
 * <p>Demonstrates async cancel operations for background interactions:
 *
 * <ol>
 *   <li>Create background interaction asynchronously
 *   <li>Chain create → cancel using .thenCompose()
 *   <li>Fire-and-forget cancel pattern
 *   <li>Verify cancellation status with async get
 *   <li>Error handling for cancel failures (API currently has 500 errors)
 * </ol>
 *
 * <p>Note: Only interactions created with background=true can be cancelled. The cancel API may
 * return 500 errors in some cases - this example demonstrates proper error handling.
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsAsyncCancel {

  public static void main(String[] args) {
    Client client = new Client();

    System.out.println("=== Interactions API: Async Cancel Example ===\n");

    try {
      // ===== PART 1: Chained Create → Cancel =====
      System.out.println("--- PART 1: Chained Create → Cancel ---\n");
      System.out.println("Note: background=true is REQUIRED for cancel to work.\n");

      CreateInteractionConfig createConfig =
          CreateInteractionConfig.builder()
              .agent("deep-research-pro-preview-12-2025")
              .input("Write a comprehensive essay about space exploration")
              .background(true)
              .build();

      CancelInteractionConfig cancelConfig = CancelInteractionConfig.builder().build();
      GetInteractionConfig getConfig = GetInteractionConfig.builder().build();

      System.out.println("Creating background interaction and chaining cancel...");

      client.async.interactions.create(createConfig)
          .thenCompose(
              interaction -> {
                System.out.println("\nCreate completed:");
                System.out.println("  Interaction ID: " + interaction.id());
                System.out.println("  Status: " + interaction.status());
                System.out.println("  Background: true");
                System.out.println("\nNow attempting to cancel...");

                // Chain the cancel operation
                return client.async.interactions.cancel(interaction.id(), cancelConfig);
              })
          .thenAccept(
              cancelled -> {
                System.out.println("\nCancel completed:");
                System.out.println("  Interaction ID: " + cancelled.id());
                System.out.println("  Status: " + cancelled.status());
              })
          .whenComplete(
              (result, exception) -> {
                if (exception != null) {
                  System.out.println("\nCancel failed (this is known behavior):");
                  System.out.println("  Error: " + exception.getMessage());
                  System.out.println("  (API may return 500 error for cancel operations)");
                }
              })
          .join();

      // ===== PART 2: Fire-and-Forget Cancel Pattern =====
      System.out.println("\n--- PART 2: Fire-and-Forget Cancel Pattern ---\n");

      CreateInteractionConfig createConfig2 =
          CreateInteractionConfig.builder()
              .agent("deep-research-pro-preview-12-2025")
              .input("Research quantum computing developments")
              .background(true)
              .build();

      System.out.println("Creating background interaction...");

      CompletableFuture<Interaction> createFuture =
          client.async.interactions.create(createConfig2);

      createFuture.thenAccept(
          interaction -> {
            System.out.println("\nCreate completed:");
            System.out.println("  Interaction ID: " + interaction.id());

            // Fire-and-forget cancel - don't wait for result
            System.out.println("\nFiring cancel request (not waiting for response)...");
            client.async.interactions.cancel(interaction.id(), cancelConfig)
                .whenComplete(
                    (cancelled, exception) -> {
                      if (exception != null) {
                        System.out.println("  Cancel error: " + exception.getMessage());
                      } else {
                        System.out.println("  Cancel succeeded: " + cancelled.id());
                      }
                    });
          });

      createFuture.join();

      // Give cancel request a moment to process
      Thread.sleep(1000);

      // ===== PART 3: Verify Cancellation with Get =====
      System.out.println("\n--- PART 3: Verify Cancellation with Get ---\n");

      CreateInteractionConfig createConfig3 =
          CreateInteractionConfig.builder()
              .agent("deep-research-pro-preview-12-2025")
              .input("Analyze machine learning trends")
              .background(true)
              .build();

      System.out.println("Creating background interaction, cancelling, then verifying...");

      client.async.interactions.create(createConfig3)
          .thenCompose(
              interaction -> {
                System.out.println("\nCreate completed: " + interaction.id());
                String interactionId = interaction.id();

                // Try to cancel
                return client.async.interactions.cancel(interactionId, cancelConfig)
                    .handle(
                        (cancelled, exception) -> {
                          if (exception != null) {
                            System.out.println("Cancel error: " + exception.getMessage());
                          } else {
                            System.out.println("Cancel succeeded: " + cancelled.id());
                          }
                          return interactionId;
                        });
              })
          .thenCompose(
              interactionId -> {
                System.out.println("\nVerifying final status with get...");
                return client.async.interactions.get(interactionId, getConfig);
              })
          .thenAccept(
              retrieved -> {
                System.out.println("\nGet completed:");
                System.out.println("  Interaction ID: " + retrieved.id());
                System.out.println("  Status: " + retrieved.status());
                System.out.println("  Agent: " + retrieved.agent().orElse("N/A"));
                printOutputs(retrieved);
              })
          .join();

      System.out.println("\n=== Example completed ===");

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /** Helper method to print interaction outputs. */
  private static void printOutputs(Interaction interaction) {
    if (interaction.outputs().isPresent() && !interaction.outputs().get().isEmpty()) {
      System.out.println("  Outputs:");
      for (Content output : interaction.outputs().get()) {
        if (output instanceof TextContent) {
          String text = ((TextContent) output).text().orElse("(empty)");
          if (text.length() > 100) {
            text = text.substring(0, 100) + "...";
          }
          System.out.println("    " + text);
        }
      }
    } else {
      System.out.println("  Outputs: (none - likely cancelled before completion)");
    }
  }

  private InteractionsAsyncCancel() {}
}
