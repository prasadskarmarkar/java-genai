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
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsDeleteExample"
 */
package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.DeleteInteractionConfig;
import com.google.genai.types.interactions.DeleteInteractionResponse;
import com.google.genai.types.interactions.GetInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.TextContent;
import com.google.genai.types.interactions.content.ThoughtContent;

/**
 * Example: Delete Interaction API
 *
 * <p>Demonstrates the interactions.delete() API:
 *
 * <ol>
 *   <li>Create an interaction
 *   <li>Get the interaction to verify it exists
 *   <li>Delete the interaction
 *   <li>Try to get it again (should fail)
 * </ol>
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsDeleteExample {

  public static void main(String[] args) {
    // Instantiate the client. The client gets the API key from the environment variable
    // `GOOGLE_API_KEY`.
    //
    Client client = new Client();

    System.out.println("=== Interactions API: Delete Example ===\n");

    try {
      // ===== STEP 1: Create Interaction =====
      System.out.println("--- STEP 1: Create Interaction (Create API) ---\n");

      CreateInteractionConfig createConfig =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .input("What is the capital of France?")
              .build();

      Interaction interaction = client.interactions.create(createConfig);
      String interactionId = interaction.id();

      System.out.println("CREATE Response:");
      System.out.println("  Interaction ID: " + interactionId);
      System.out.println("  Status: " + interaction.status());
      printOutputsWithTypes(interaction);

      // ===== STEP 2: Get Interaction =====
      System.out.println("\n--- STEP 2: Verify with Get API ---\n");

      GetInteractionConfig getConfig = GetInteractionConfig.builder().build();
      Interaction retrieved = client.interactions.get(interactionId, getConfig);

      System.out.println("GET Response:");
      System.out.println("  Interaction ID: " + retrieved.id());
      System.out.println("  Status: " + retrieved.status());
      System.out.println("  Model: " + retrieved.model().orElse("N/A"));
      printOutputsWithTypes(retrieved);

      // ===== STEP 3: Delete Interaction =====
      System.out.println("\n--- STEP 3: Delete Interaction (Delete API) ---\n");

      DeleteInteractionConfig deleteConfig = DeleteInteractionConfig.builder().build();
      DeleteInteractionResponse deleteResponse =
          client.interactions.delete(interactionId, deleteConfig);

      System.out.println("DELETE Response:");
      System.out.println("  Deleted interaction: " + interactionId);
      System.out.println("  Response: " + deleteResponse);

      // ===== STEP 4: Try to Get Deleted Interaction =====
      System.out.println("\n--- STEP 4: Try to Get Deleted Interaction ---\n");

      try {
        Interaction deleted = client.interactions.get(interactionId, getConfig);
        System.out.println("ERROR: Should have thrown an exception!");
        System.out.println("  Status: " + deleted.status());
      } catch (Exception e) {
        System.out.println("GET Response (Expected Error):");
        System.out.println("  Error: " + e.getMessage());
        System.out.println("  (This is expected - interaction was deleted)");
      }

      System.out.println("\n=== Example completed ===");

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /** Prints all output content types with their details. */
  private static void printOutputsWithTypes(Interaction interaction) {
    System.out.println("  Output Content Types:");
    if (!interaction.outputs().isPresent() || interaction.outputs().get().isEmpty()) {
      System.out.println("    (none)");
      return;
    }

    for (Content output : interaction.outputs().get()) {
      String typeName = output.getClass().getSimpleName();

      if (output instanceof TextContent) {
        TextContent t = (TextContent) output;
        String text = t.text().orElse("(empty)");
        if (text.length() > 100) {
          text = text.substring(0, 100) + "...";
        }
        System.out.println("    - " + typeName + ": \"" + text + "\"");
      } else if (output instanceof ThoughtContent) {
        ThoughtContent tc = (ThoughtContent) output;
        System.out.println("    - " + typeName + ":");
        System.out.println("        signature: " + tc.signature().orElse("(none)"));
        if (tc.summary().isPresent()) {
          System.out.println("        summaries: " + tc.summary().get().size());
        }
      } else {
        System.out.println("    - " + typeName);
      }
    }
  }

  private InteractionsDeleteExample() {}
}
