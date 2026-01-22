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
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsAsyncGet"
 */
package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.GetInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.TextContent;
import java.util.concurrent.CompletableFuture;

/**
 * Example: Async Get Operations
 *
 * <p>Demonstrates async get/retrieve operations and parallel fetching:
 *
 * <ol>
 *   <li>Async get single interaction by ID
 *   <li>Parallel fetch of multiple interactions using CompletableFuture.allOf()
 *   <li>Chaining create → get using .thenCompose()
 *   <li>Follow-up interaction retrieval with previousInteractionId
 * </ol>
 *
 * <p>This example shows how to use async operations to efficiently retrieve interactions without
 * blocking.
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsAsyncGet {

  public static void main(String[] args) {
    Client client = new Client();

    System.out.println("=== Interactions API: Async Get Example ===\n");

    try {
      // ===== PART 1: Chained Create → Get =====
      System.out.println("--- PART 1: Chained Create → Get ---\n");

      CreateInteractionConfig createConfig =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .input("What is the capital of Japan?")
              .build();

      System.out.println("Creating interaction and chaining get operation...");

      GetInteractionConfig getConfig = GetInteractionConfig.builder().build();

      client.async.interactions.create(createConfig)
          .thenCompose(
              interaction -> {
                System.out.println("\nCreate completed:");
                System.out.println("  Interaction ID: " + interaction.id());
                System.out.println("  Status: " + interaction.status());
                System.out.println("\nNow fetching with get...");

                // Chain the get operation
                return client.async.interactions.get(interaction.id(), getConfig);
              })
          .thenAccept(
              retrieved -> {
                System.out.println("\nGet completed:");
                System.out.println("  Interaction ID: " + retrieved.id());
                System.out.println("  Status: " + retrieved.status());
                System.out.println("  Model: " + retrieved.model().orElse("N/A"));
                System.out.print("  Output: ");
                printOutputs(retrieved);
              })
          .join();

      // ===== PART 2: Create Multiple, Then Fetch in Parallel =====
      System.out.println("\n--- PART 2: Create Multiple, Then Fetch in Parallel ---\n");
      System.out.println("Creating 3 interactions sequentially...\n");

      // Create first interaction
      Interaction i1 =
          client.interactions.create(
              CreateInteractionConfig.builder()
                  .model("gemini-2.5-flash")
                  .input("What is AI?")
                  .build());
      System.out.println("Created interaction 1: " + i1.id());

      // Create second interaction
      Interaction i2 =
          client.interactions.create(
              CreateInteractionConfig.builder()
                  .model("gemini-2.5-flash")
                  .input("What is ML?")
                  .build());
      System.out.println("Created interaction 2: " + i2.id());

      // Create third interaction
      Interaction i3 =
          client.interactions.create(
              CreateInteractionConfig.builder()
                  .model("gemini-2.5-flash")
                  .input("What is NLP?")
                  .build());
      System.out.println("Created interaction 3: " + i3.id());

      // Now fetch all 3 in parallel
      System.out.println("\nFetching all 3 interactions in parallel...");

      CompletableFuture<Interaction> fetch1 =
          client.async.interactions.get(i1.id(), getConfig);
      CompletableFuture<Interaction> fetch2 =
          client.async.interactions.get(i2.id(), getConfig);
      CompletableFuture<Interaction> fetch3 =
          client.async.interactions.get(i3.id(), getConfig);

      // Attach callbacks
      fetch1.thenAccept(
          r -> System.out.println("Fetched 1: " + r.id() + " - Status: " + r.status()));
      fetch2.thenAccept(
          r -> System.out.println("Fetched 2: " + r.id() + " - Status: " + r.status()));
      fetch3.thenAccept(
          r -> System.out.println("Fetched 3: " + r.id() + " - Status: " + r.status()));

      // Wait for all fetches to complete
      CompletableFuture.allOf(fetch1, fetch2, fetch3)
          .thenRun(() -> System.out.println("\nAll parallel fetches completed!"))
          .join();

      // ===== PART 3: Follow-up Interaction with previousInteractionId =====
      System.out.println("\n--- PART 3: Follow-up Interaction with previousInteractionId ---\n");

      CreateInteractionConfig initialConfig =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .input("Explain quantum computing")
              .build();

      System.out.println("Creating initial interaction...");

      client.async.interactions.create(initialConfig)
          .thenCompose(
              initial -> {
                System.out.println("\nInitial interaction created:");
                System.out.println("  ID: " + initial.id());

                // Create follow-up using previousInteractionId
                CreateInteractionConfig followUpConfig =
                    CreateInteractionConfig.builder()
                        .model("gemini-2.5-flash")
                        .input("Can you give me an example?")
                        .previousInteractionId(initial.id())
                        .build();

                System.out.println("\nCreating follow-up interaction...");
                return client.async.interactions.create(followUpConfig);
              })
          .thenCompose(
              followUp -> {
                System.out.println("\nFollow-up interaction created:");
                System.out.println("  ID: " + followUp.id());
                System.out.println("  Previous ID: "
                    + followUp.previousInteractionId().orElse("N/A"));

                // Get the follow-up to verify previousInteractionId
                return client.async.interactions.get(followUp.id(), getConfig);
              })
          .thenAccept(
              retrieved -> {
                System.out.println("\nRetrieved follow-up interaction:");
                System.out.println("  ID: " + retrieved.id());
                System.out.println("  Status: " + retrieved.status());
                System.out.println("  Previous Interaction ID: "
                    + retrieved.previousInteractionId().orElse("N/A"));
                System.out.print("  Output: ");
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

  private InteractionsAsyncGet() {}
}
