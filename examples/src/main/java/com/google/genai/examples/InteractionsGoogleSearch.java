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

package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.GoogleSearchResult;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.TextContent;
import com.google.genai.types.interactions.steps.GoogleSearchCallStep;
import com.google.genai.types.interactions.steps.GoogleSearchResultStep;
import com.google.genai.types.interactions.steps.ModelOutputStep;
import com.google.genai.types.interactions.steps.Step;
import com.google.genai.types.interactions.tools.GoogleSearch;
import java.util.List;

/**
 * Example: Google Search Tool with the Interactions API.
 *
 * <p>In the step-based response model, Google Search calls appear as {@link GoogleSearchCallStep}
 * and results as {@link GoogleSearchResultStep} in the interaction's {@code steps} list. The final
 * model text output arrives in a {@link ModelOutputStep}.
 *
 * <p>To run this example:
 * <ol>
 *   <li>Set: {@code export GOOGLE_API_KEY=YOUR_API_KEY}
 *   <li>Compile: {@code mvn clean compile}
 *   <li>Run: {@code mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsGoogleSearch"}
 * </ol>
 *
 * <p><b>Note:</b> The Interactions API is currently in beta.
 */
public final class InteractionsGoogleSearch {

  public static void main(String[] args) {
    Client client = new Client();

    System.out.println("=== Interactions API: Google Search Tool Example ===\n");

    try {
      GoogleSearch searchTool = GoogleSearch.builder().build();

      CreateInteractionConfig config =
          CreateInteractionConfig.builder()
              .model("gemini-3-flash-preview")
              .input("What are the latest developments in quantum computing in 2025?")
              .tools(searchTool)
              .build();

      System.out.println("=== REQUEST ===");
      System.out.println(config.toJson());
      System.out.println();

      Interaction response = client.interactions.create(config);

      System.out.println("=== RESPONSE ===");
      System.out.println(response.toJson());
      System.out.println();

      printResults(response);

      System.out.println("\n=== Example completed ===");

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private static void printResults(Interaction interaction) {
    System.out.println("Interaction ID: " + interaction.id());
    System.out.println("Status: " + interaction.status());

    if (!interaction.steps().isPresent() || interaction.steps().get().isEmpty()) {
      System.out.println("No steps in response.");
      return;
    }

    for (Step step : interaction.steps().get()) {
      if (step instanceof GoogleSearchCallStep) {
        GoogleSearchCallStep callStep = (GoogleSearchCallStep) step;
        System.out.println("\n[GoogleSearchCall] id=" + callStep.id().orElse("?"));
        callStep.arguments().ifPresent(args ->
            System.out.println("  queries: " + args.queries().orElse(List.of())));

      } else if (step instanceof GoogleSearchResultStep) {
        GoogleSearchResultStep resultStep = (GoogleSearchResultStep) step;
        System.out.println("\n[GoogleSearchResult] callId=" + resultStep.callId().orElse("?"));
        resultStep.result().ifPresent(results -> {
          List<GoogleSearchResult> items = (List<GoogleSearchResult>) results;
          System.out.println("  " + items.size() + " result(s):");
          for (int i = 0; i < Math.min(3, items.size()); i++) {
            System.out.println("  [" + (i + 1) + "] " + items.get(i).title().orElse("N/A")
                + " — " + items.get(i).url().orElse(""));
          }
        });

      } else if (step instanceof ModelOutputStep) {
        ModelOutputStep outputStep = (ModelOutputStep) step;
        System.out.println("\n[ModelOutput]");
        outputStep.content().ifPresent(contents -> {
          for (com.google.genai.types.interactions.content.Content c : contents) {
            if (c instanceof TextContent) {
              System.out.println("  " + ((TextContent) c).text().orElse("(empty)"));
            }
          }
        });
      }
    }
  }

  private InteractionsGoogleSearch() {}
}
