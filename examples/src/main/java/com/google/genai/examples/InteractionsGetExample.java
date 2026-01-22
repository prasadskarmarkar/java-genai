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
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsGetExample"
 */
package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.GetInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.TextContent;
import com.google.genai.types.interactions.content.ThoughtContent;

/**
 * Example: Get Interaction API with Model and Agent
 *
 * <p>Demonstrates the interactions.get() API with both model-based and agent-based interactions:
 *
 * <ol>
 *   <li>Create a model-based interaction (using .model())
 *   <li>Get the interaction by ID
 *   <li>Create a follow-up interaction with previousInteractionId
 *   <li>Create an agent-based interaction (using .agent())
 * </ol>
 *
 * <p>Available agents:
 * <ul>
 *   <li>deep-research-pro-preview-12-2025 - Gemini Deep Research Agent
 * </ul>
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsGetExample {

  public static void main(String[] args) {
    // Instantiate the client. The client gets the API key from the environment variable
    // `GOOGLE_API_KEY`.
    //
    Client client = new Client();

    System.out.println("=== Interactions API: Get Example ===\n");

    try {
      // ===== STEP 1: Create Model-based Interaction =====
      System.out.println("--- STEP 1: Create Model-based Interaction (Create API) ---\n");

      CreateInteractionConfig createConfig =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .input("Explain quantum computing in 2-3 sentences.")
              .build();

      Interaction interaction = client.interactions.create(createConfig);
      String interactionId = interaction.id();

      System.out.println("CREATE Response:");
      System.out.println("  Interaction ID: " + interactionId);
      System.out.println("  Status: " + interaction.status());
      System.out.println("  Model: " + interaction.model().orElse("N/A"));
      System.out.println("  Agent: " + interaction.agent().orElse("(none - model-based)"));
      printOutputsWithTypes(interaction);

      // ===== STEP 2: Get Interaction =====
      System.out.println("\n--- STEP 2: Get Interaction by ID (Get API) ---\n");

      GetInteractionConfig getConfig = GetInteractionConfig.builder().build();
      Interaction retrieved = client.interactions.get(interactionId, getConfig);

      System.out.println("GET Response:");
      System.out.println("  Interaction ID: " + retrieved.id());
      System.out.println("  Status: " + retrieved.status());
      System.out.println("  Model: " + retrieved.model().orElse("N/A"));
      System.out.println("  Agent: " + retrieved.agent().orElse("(none - model-based)"));

      if (retrieved.previousInteractionId().isPresent()) {
        System.out.println("  Previous Interaction ID: " + retrieved.previousInteractionId().get());
      }

      printOutputsWithTypes(retrieved);

      // ===== STEP 3: Create Follow-up Interaction =====
      System.out.println("\n--- STEP 3: Create Follow-up Interaction ---\n");

      CreateInteractionConfig followUpConfig =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .input("Can you give me a real-world example?")
              .previousInteractionId(interactionId)
              .build();

      Interaction followUp = client.interactions.create(followUpConfig);

      System.out.println("CREATE Response (Follow-up):");
      System.out.println("  Interaction ID: " + followUp.id());
      System.out.println("  Status: " + followUp.status());
      System.out.println("  Previous Interaction ID: " + interactionId);
      printOutputsWithTypes(followUp);

      // ===== STEP 4: Get Follow-up Interaction =====
      System.out.println("\n--- STEP 4: Get Follow-up Interaction (Get API) ---\n");

      Interaction retrievedFollowUp = client.interactions.get(followUp.id(), getConfig);

      System.out.println("GET Response (Follow-up):");
      System.out.println("  Interaction ID: " + retrievedFollowUp.id());
      System.out.println("  Status: " + retrievedFollowUp.status());
      System.out.println("  Model: " + retrievedFollowUp.model().orElse("N/A"));
      System.out.println("  Previous Interaction ID: "
          + retrievedFollowUp.previousInteractionId().orElse("N/A"));
      printOutputsWithTypes(retrievedFollowUp);

      // ===== STEP 5: Agent-based Interaction =====
      System.out.println("\n--- STEP 5: Agent-based Interaction (Create API) ---\n");
      System.out.println("Using agent: deep-research-pro-preview-12-2025 (Gemini Deep Research)");
      System.out.println("Note: Agent-based interactions use .agent() instead of .model()\n");

      CreateInteractionConfig agentConfig =
          CreateInteractionConfig.builder()
              .agent("deep-research-pro-preview-12-2025")
              .input("What are the latest breakthroughs in quantum computing in 2025?")
	      .background(true)
              .build();

      Interaction agentInteraction = client.interactions.create(agentConfig);

      System.out.println("CREATE Response (Agent):");
      System.out.println("  Interaction ID: " + agentInteraction.id());
      System.out.println("  Status: " + agentInteraction.status());
      System.out.println("  Model: " + agentInteraction.model().orElse("(none - agent-based)"));
      System.out.println("  Agent: " + agentInteraction.agent().orElse("N/A"));
      printOutputsWithTypes(agentInteraction);

      // ===== STEP 6: Get Agent Interaction =====
      System.out.println("\n--- STEP 6: Get Agent Interaction (Get API) ---\n");

      Interaction retrievedAgent = client.interactions.get(agentInteraction.id(), getConfig);

      System.out.println("GET Response (Agent):");
      System.out.println("  Interaction ID: " + retrievedAgent.id());
      System.out.println("  Status: " + retrievedAgent.status());
      System.out.println("  Model: " + retrievedAgent.model().orElse("(none - agent-based)"));
      System.out.println("  Agent: " + retrievedAgent.agent().orElse("N/A"));
      printOutputsWithTypes(retrievedAgent);

      // Summary
      System.out.println("\n--- Summary: Model vs Agent ---\n");
      System.out.println("Model-based interaction:");
      System.out.println("  .model(\"gemini-2.5-flash\") - Direct model access");
      System.out.println();
      System.out.println("Agent-based interaction:");
      System.out.println("  .agent(\"deep-research-pro-preview-12-2025\") - Gemini Deep Research Agent");
      System.out.println("  - Agents are specialized for specific tasks (e.g., deep research)");
      System.out.println("  - May return different output types and have longer processing times");

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

  private InteractionsGetExample() {}
}
