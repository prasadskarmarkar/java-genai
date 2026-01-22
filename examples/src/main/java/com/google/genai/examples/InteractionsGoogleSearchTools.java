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
 * <p>1. Set an API key environment variable. You can find a list of available API keys here:
 * https://aistudio.google.com/app/apikey
 *
 * <p>export GOOGLE_API_KEY=YOUR_API_KEY
 *
 * <p>2. Compile the java package and run the sample code.
 *
 * <p>mvn clean compile
 *
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsGoogleSearchTools"
 */
package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.GetInteractionConfig;
import com.google.genai.types.interactions.GoogleSearchResult;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.GoogleSearchCallContent;
import com.google.genai.types.interactions.content.GoogleSearchResultContent;
import com.google.genai.types.interactions.content.TextContent;
import com.google.genai.types.interactions.tools.GoogleSearchTool;
import java.util.List;

/**
 * Example: GoogleSearchTool with the Interactions API
 *
 * <p>Demonstrates how to use the GoogleSearchTool to enable the model to search the web.
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsGoogleSearchTools {

  public static void main(String[] args) {
    // Instantiate the client. The client gets the API key from the environment variable
    // `GOOGLE_API_KEY`.
    //
    Client client = new Client();

    System.out.println("=== Interactions API: GoogleSearchTool Example ===\n");

    // ===== Configure GoogleSearchTool =====
    GoogleSearchTool googleSearchTool = GoogleSearchTool.builder().build();
    System.out.println("GoogleSearchTool configured\n");

    // ===== First Query =====
    System.out.println("--- Query 1: Latest developments ---\n");

    String userInput = "What are the latest developments in quantum computing in 2025?";
    System.out.println("User: " + userInput + "\n");

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .input(userInput)
            .tools(googleSearchTool)
            .build();

    Interaction response = client.interactions.create(config);
    String interactionId = response.id();

    System.out.println("Response received. Interaction ID: " + interactionId);
    processOutputs(response);

    // ===== Verify with Get Interaction =====
    System.out.println("\n--- Verify with Get Interaction ---\n");

    GetInteractionConfig getConfig = GetInteractionConfig.builder().build();
    Interaction retrieved = client.interactions.get(interactionId, getConfig);

    System.out.println("Retrieved interaction ID: " + retrieved.id());
    System.out.println("Status: " + retrieved.status());
    if (retrieved.outputs().isPresent()) {
      System.out.println("Outputs count: " + retrieved.outputs().get().size());
    }

    // ===== Second Query =====
    System.out.println("\n--- Query 2: Weather query ---\n");

    String userInput2 = "What is the current weather in New York City?";
    System.out.println("User: " + userInput2 + "\n");

    CreateInteractionConfig config2 =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .input(userInput2)
            .tools(googleSearchTool)
            .build();

    Interaction response2 = client.interactions.create(config2);

    System.out.println("Response received. Interaction ID: " + response2.id());
    processOutputs(response2);

    System.out.println("\n=== Example completed ===");
  }

  /** Processes and prints interaction outputs. */
  private static void processOutputs(Interaction interaction) {
    if (!interaction.outputs().isPresent()) {
      return;
    }

    for (Content output : interaction.outputs().get()) {
      if (output instanceof TextContent) {
        TextContent text = (TextContent) output;
        String textValue = text.text().orElse("(empty)");
        if (textValue.length() > 300) {
          textValue = textValue.substring(0, 300) + "... [truncated]";
        }
        System.out.println("\nModel: " + textValue);
      } else if (output instanceof GoogleSearchCallContent) {
        GoogleSearchCallContent searchCall = (GoogleSearchCallContent) output;
        System.out.println("\nGoogleSearchCall: id=" + searchCall.id());
        if (searchCall.arguments().isPresent()) {
          System.out.println("  queries: " + searchCall.arguments().get().queries().orElse(List.of()));
        }
      } else if (output instanceof GoogleSearchResultContent) {
        GoogleSearchResultContent searchResult = (GoogleSearchResultContent) output;
        System.out.println("\nGoogleSearchResult: callId=" + searchResult.callId().orElse("N/A"));
        if (searchResult.result().isPresent()) {
          List<GoogleSearchResult> results = searchResult.result().get();
          System.out.println("  results count: " + results.size());
          int count = Math.min(2, results.size());
          for (int i = 0; i < count; i++) {
            GoogleSearchResult result = results.get(i);
            System.out.println("  [" + (i + 1) + "] " + result.title().orElse("N/A"));
          }
        }
      }
    }
  }

  private InteractionsGoogleSearchTools() {}
}
