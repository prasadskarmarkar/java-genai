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
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsAsyncCreate"
 */
package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.TextContent;
import java.util.concurrent.CompletableFuture;

/**
 * Example: Async Create Operations
 *
 * <p>Demonstrates async create operations with various input types and parallel creation:
 *
 * <ol>
 *   <li>Basic async create with model
 *   <li>Async create with agent (background interaction)
 *   <li>Multiple parallel creates using CompletableFuture.allOf()
 *   <li>Callback handling with .thenAccept()
 *   <li>Error handling with .whenComplete()
 * </ol>
 *
 * <p>This example shows the power of async operations for creating multiple interactions
 * concurrently without blocking.
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsAsyncCreate {

  public static void main(String[] args) {
    Client client = new Client();

    System.out.println("=== Interactions API: Async Create Example ===\n");

    try {
      // ===== PART 1: Basic Async Create with Model =====
      System.out.println("--- PART 1: Basic Async Create with Model ---\n");

      CreateInteractionConfig config1 =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .input("What is the capital of France?")
              .build();

      System.out.println("Sending async request...");
      CompletableFuture<Interaction> future1 = client.async.interactions.create(config1);

      // Attach callback to handle response
      future1.thenAccept(
          interaction -> {
            System.out.println("\nRESPONSE (Model-based):");
            System.out.println("  Interaction ID: " + interaction.id());
            System.out.println("  Status: " + interaction.status());
            System.out.println("  Model: " + interaction.model().orElse("N/A"));
            System.out.print("  Output: ");
            printOutputs(interaction);
          });

      // Wait for completion
      future1.join();

      // ===== PART 2: Async Create with Agent (Background) =====
      System.out.println("\n--- PART 2: Async Create with Agent (Background) ---\n");

      CreateInteractionConfig config2 =
          CreateInteractionConfig.builder()
              .agent("deep-research-pro-preview-12-2025")
              .input("What are the latest AI developments?")
              .background(true)
              .build();

      System.out.println("Sending async background request with agent...");
      CompletableFuture<Interaction> future2 = client.async.interactions.create(config2);

      future2.thenAccept(
          interaction -> {
            System.out.println("\nRESPONSE (Agent-based, Background):");
            System.out.println("  Interaction ID: " + interaction.id());
            System.out.println("  Status: " + interaction.status());
            System.out.println("  Agent: " + interaction.agent().orElse("N/A"));
            System.out.println("  Background: true");
          });

      future2.join();

      // ===== PART 3: Multiple Parallel Creates =====
      System.out.println("\n--- PART 3: Multiple Parallel Creates ---\n");
      System.out.println("Creating 3 interactions in parallel...\n");

      CompletableFuture<Interaction> parallel1 =
          client.async.interactions.create(
              CreateInteractionConfig.builder()
                  .model("gemini-2.5-flash")
                  .input("What is machine learning?")
                  .build());

      CompletableFuture<Interaction> parallel2 =
          client.async.interactions.create(
              CreateInteractionConfig.builder()
                  .model("gemini-2.5-flash")
                  .input("What is quantum computing?")
                  .build());

      CompletableFuture<Interaction> parallel3 =
          client.async.interactions.create(
              CreateInteractionConfig.builder()
                  .model("gemini-2.5-flash")
                  .input("What is blockchain?")
                  .build());

      // Attach callbacks to each
      parallel1.thenAccept(
          i -> System.out.println("Parallel 1 completed: " + i.id()));
      parallel2.thenAccept(
          i -> System.out.println("Parallel 2 completed: " + i.id()));
      parallel3.thenAccept(
          i -> System.out.println("Parallel 3 completed: " + i.id()));

      // Wait for all to complete
      CompletableFuture.allOf(parallel1, parallel2, parallel3)
          .thenRun(
              () -> {
                System.out.println("\nAll parallel operations completed!");
                System.out.println("Total interactions created: 3");
              });

      CompletableFuture.allOf(parallel1, parallel2, parallel3).join();

      // ===== PART 4: Error Handling with whenComplete =====
      System.out.println("\n--- PART 4: Error Handling with whenComplete ---\n");

      CreateInteractionConfig config4 =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .input("Explain async programming in Java")
              .build();

      client.async.interactions.create(config4)
          .whenComplete(
              (interaction, exception) -> {
                if (exception != null) {
                  System.out.println("ERROR: " + exception.getMessage());
                } else {
                  System.out.println("SUCCESS:");
                  System.out.println("  Interaction ID: " + interaction.id());
                  System.out.println("  Status: " + interaction.status());
                }
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

  private InteractionsAsyncCreate() {}
}
