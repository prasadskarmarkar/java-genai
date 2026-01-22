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
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsAsyncDelete"
 */
package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.DeleteInteractionConfig;
import com.google.genai.types.interactions.DeleteInteractionResponse;
import com.google.genai.types.interactions.GetInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import java.util.concurrent.CompletableFuture;

/**
 * Example: Async Delete Operations
 *
 * <p>Demonstrates async delete operations and bulk cleanup patterns:
 *
 * <ol>
 *   <li>Create interaction asynchronously
 *   <li>Chain create → delete using .thenCompose()
 *   <li>Bulk delete multiple interactions in parallel
 *   <li>Verify deletion with async get (should return 404)
 *   <li>Return CompletableFuture&lt;DeleteInteractionResponse&gt;
 * </ol>
 *
 * <p>This example demonstrates efficient cleanup of interactions using async operations.
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsAsyncDelete {

  public static void main(String[] args) {
    Client client = new Client();

    System.out.println("=== Interactions API: Async Delete Example ===\n");

    try {
      // ===== PART 1: Chained Create → Delete =====
      System.out.println("--- PART 1: Chained Create → Delete ---\n");

      CreateInteractionConfig createConfig =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .input("What is the capital of France?")
              .build();

      DeleteInteractionConfig deleteConfig = DeleteInteractionConfig.builder().build();
      GetInteractionConfig getConfig = GetInteractionConfig.builder().build();

      System.out.println("Creating interaction and chaining delete...");

      client.async.interactions.create(createConfig)
          .thenCompose(
              interaction -> {
                System.out.println("\nCreate completed:");
                System.out.println("  Interaction ID: " + interaction.id());
                System.out.println("  Status: " + interaction.status());
                System.out.println("\nNow deleting...");

                // Chain the delete operation
                return client.async.interactions.delete(interaction.id(), deleteConfig);
              })
          .thenAccept(
              deleteResponse -> {
                System.out.println("\nDelete completed:");
                System.out.println("  Response: " + deleteResponse);
              })
          .join();

      // ===== PART 2: Create → Delete → Verify =====
      System.out.println("\n--- PART 2: Create → Delete → Verify with Get ---\n");

      CreateInteractionConfig createConfig2 =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .input("What is quantum computing?")
              .build();

      System.out.println("Creating interaction, deleting, then verifying deletion...");

      client.async.interactions.create(createConfig2)
          .thenCompose(
              interaction -> {
                String interactionId = interaction.id();
                System.out.println("\nCreate completed: " + interactionId);

                // Delete and return the ID for verification
                return client.async.interactions.delete(interactionId, deleteConfig)
                    .thenApply(deleteResponse -> interactionId);
              })
          .thenCompose(
              interactionId -> {
                System.out.println("Delete completed: " + interactionId);
                System.out.println("\nVerifying deletion with get (should fail)...");

                // Try to get the deleted interaction
                return client.async.interactions.get(interactionId, getConfig);
              })
          .whenComplete(
              (result, exception) -> {
                if (exception != null) {
                  System.out.println("\nGet failed as expected:");
                  System.out.println("  Error: " + exception.getMessage());
                  System.out.println("  (Interaction was successfully deleted)");
                } else {
                  System.out.println("\nERROR: Get should have failed!");
                  System.out.println("  Status: " + result.status());
                }
              })
          .handle((result, exception) -> null)  // Consume the exception
          .join();

      // ===== PART 3: Bulk Delete Pattern =====
      System.out.println("\n--- PART 3: Bulk Delete Pattern ---\n");
      System.out.println("Creating 3 interactions...\n");

      // Create 3 interactions synchronously first
      Interaction i1 =
          client.interactions.create(
              CreateInteractionConfig.builder()
                  .model("gemini-2.5-flash")
                  .input("What is AI?")
                  .build());
      System.out.println("Created interaction 1: " + i1.id());

      Interaction i2 =
          client.interactions.create(
              CreateInteractionConfig.builder()
                  .model("gemini-2.5-flash")
                  .input("What is ML?")
                  .build());
      System.out.println("Created interaction 2: " + i2.id());

      Interaction i3 =
          client.interactions.create(
              CreateInteractionConfig.builder()
                  .model("gemini-2.5-flash")
                  .input("What is NLP?")
                  .build());
      System.out.println("Created interaction 3: " + i3.id());

      // Now delete all 3 in parallel
      System.out.println("\nDeleting all 3 interactions in parallel...");

      CompletableFuture<DeleteInteractionResponse> delete1 =
          client.async.interactions.delete(i1.id(), deleteConfig);
      CompletableFuture<DeleteInteractionResponse> delete2 =
          client.async.interactions.delete(i2.id(), deleteConfig);
      CompletableFuture<DeleteInteractionResponse> delete3 =
          client.async.interactions.delete(i3.id(), deleteConfig);

      // Attach callbacks
      delete1.thenAccept(r -> System.out.println("Deleted 1: " + i1.id()));
      delete2.thenAccept(r -> System.out.println("Deleted 2: " + i2.id()));
      delete3.thenAccept(r -> System.out.println("Deleted 3: " + i3.id()));

      // Wait for all deletes to complete
      CompletableFuture.allOf(delete1, delete2, delete3)
          .thenRun(() -> System.out.println("\nAll parallel deletes completed!"))
          .join();

      // Verify all deletions
      System.out.println("\nVerifying all deletions...");

      CompletableFuture<Void> verify1 =
          client.async.interactions.get(i1.id(), getConfig)
              .handle((r, e) -> {
                if (e != null) {
                  System.out.println("Verify 1: Successfully deleted (get failed)");
                }
                return null;
              });

      CompletableFuture<Void> verify2 =
          client.async.interactions.get(i2.id(), getConfig)
              .handle((r, e) -> {
                if (e != null) {
                  System.out.println("Verify 2: Successfully deleted (get failed)");
                }
                return null;
              });

      CompletableFuture<Void> verify3 =
          client.async.interactions.get(i3.id(), getConfig)
              .handle((r, e) -> {
                if (e != null) {
                  System.out.println("Verify 3: Successfully deleted (get failed)");
                }
                return null;
              });

      CompletableFuture.allOf(verify1, verify2, verify3)
          .thenRun(() -> System.out.println("\nAll verifications completed!"))
          .join();

      System.out.println("\n=== Example completed ===");

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private InteractionsAsyncDelete() {}
}
