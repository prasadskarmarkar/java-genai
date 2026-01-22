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
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsCancelExample"
 */
package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.interactions.CancelInteractionConfig;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.GetInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.TextContent;
import com.google.genai.types.interactions.content.ThoughtContent;

/**
 * Example: Cancel Interaction API
 *
 * <p>Demonstrates the interactions.cancel() API:
 *
 * <ol>
 *   <li>Create a background interaction (background=true required for cancel)
 *   <li>Cancel the interaction while it's in progress
 *   <li>Get the interaction to verify cancelled status
 * </ol>
 *
 * <p>Note: Only interactions created with background=true and in IN_PROGRESS status can be
 * cancelled. The Interactions API is in beta and subject to change.
 */
public final class InteractionsCancelExample {

  public static void main(String[] args) {
    // Instantiate the client. The client gets the API key from the environment variable
    // `GOOGLE_API_KEY`.
    //
    Client client = new Client();

    System.out.println("=== Interactions API: Cancel Example ===\n");

    try {
      // ===== STEP 1: Create Background Interaction =====
      System.out.println("--- STEP 1: Create Background Interaction (Create API) ---\n");
      System.out.println("Note: background=true is REQUIRED for cancel to work.\n");

      CreateInteractionConfig createConfig =
          CreateInteractionConfig.builder()
              .agent("deep-research-pro-preview-12-2025")
              .input("Write an essay about space exploration.")
              .background(true) // IMPORTANT: Must be true to cancel later
              .build();

      Interaction interaction = client.interactions.create(createConfig);
      String interactionId = interaction.id();

      System.out.println("CREATE Response:");
      System.out.println("  Interaction ID: " + interactionId);
      System.out.println("  Status: " + interaction.status());
      System.out.println("  Background: true");
      printOutputsWithTypes(interaction);

      // ===== STEP 2: Cancel Interaction =====
      System.out.println("\n--- STEP 2: Cancel Interaction (Cancel API) ---\n");

      // Attempt to cancel the interaction
      try {
        CancelInteractionConfig cancelConfig = CancelInteractionConfig.builder().build();
        Interaction cancelled = client.interactions.cancel(interactionId, cancelConfig);

        System.out.println("CANCEL Response:");
        System.out.println("  Interaction ID: " + cancelled.id());
        System.out.println("  Status: " + cancelled.status());
        printOutputsWithTypes(cancelled);
      } catch (Exception e) {
        System.out.println("Cancel failed: " + e.getMessage());
        System.out.println("(This may happen if the interaction completed too quickly)");
      }

      // ===== STEP 3: Verify with Get =====
      System.out.println("\n--- STEP 3: Verify Final State (Get API) ---\n");

      try {
        GetInteractionConfig getConfig = GetInteractionConfig.builder().build();
        Interaction retrieved = client.interactions.get(interactionId, getConfig);

        System.out.println("GET Response:");
        System.out.println("  Interaction ID: " + retrieved.id());
        System.out.println("  Status: " + retrieved.status());
        System.out.println("  Model: " + retrieved.model().orElse("N/A"));
        System.out.println("  Agent: " + retrieved.agent().orElse("N/A"));
        printOutputsWithTypes(retrieved);
      } catch (Exception e) {
        System.out.println("Get interaction failed: " + e.getMessage());
        System.out.println("(API may be experiencing issues)");
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

  private InteractionsCancelExample() {}
}
