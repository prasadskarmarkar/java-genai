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
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsGoogleSearch"
 */
package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.GoogleSearchResult;
import com.google.genai.types.interactions.content.GoogleSearchCallContent;
import com.google.genai.types.interactions.content.GoogleSearchResultContent;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.TextContent;
import com.google.genai.types.interactions.tools.GoogleSearch;
import java.util.List;

/**
 * Example: Google Search Tool with the Interactions API
 *
 * <p>Demonstrates how to use the GoogleSearch to enable the model to search the web using
 * Google Search.
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsGoogleSearch {

  public static void main(String[] args) {
    // Instantiate the client. The client gets the API key from the environment variable
    // `GOOGLE_API_KEY`.
    //
    Client client = new Client();

    System.out.println("=== Interactions API: Google Search Tool Example ===\n");

    // ===== STEP 1: Create GoogleSearch =====
    System.out.println("STEP 1: Create GoogleSearch\n");

    GoogleSearch searchTool = GoogleSearch.builder().build();

    System.out.println("GoogleSearch created successfully\n");

    // ===== STEP 2: Create interaction with Google Search enabled =====
    System.out.println("---\n");
    System.out.println("STEP 2: Create interaction with Google Search enabled\n");

    String userQuestion = "What are the latest developments in quantum computing in 2025?";
    System.out.println("User: " + userQuestion + "\n");

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .input(userQuestion)
            .tools(searchTool)
            .build();

    // Print the request JSON
    System.out.println("=== REQUEST ===");
    System.out.println(config.toJson());
    System.out.println();

    Interaction response = client.interactions.create(config);

    // Print the response JSON
    System.out.println("=== RESPONSE ===");
    System.out.println(response.toJson());
    System.out.println();

    // ===== STEP 3: Extract and display the results =====
    System.out.println("---\n");
    System.out.println("STEP 3: Extract and display the results\n");

    System.out.println("Response received. Interaction ID: " + response.id());
    System.out.println();

    if (response.outputs().isPresent()) {
      for (Content content : response.outputs().get()) {
        if (content instanceof TextContent) {
          System.out.println("Text: " + ((TextContent) content).text().orElse("(empty)"));
          System.out.println();
        } else if (content instanceof GoogleSearchCallContent) {
          GoogleSearchCallContent searchCall = (GoogleSearchCallContent) content;
          System.out.println("Google Search Call:");
          System.out.println("  ID: " + searchCall.id());
          if (searchCall.arguments().isPresent()) {
            System.out.println("  Queries: " + searchCall.arguments().get().queries().orElse(java.util.List.of()));
          }
          System.out.println();
        } else if (content instanceof GoogleSearchResultContent) {
          GoogleSearchResultContent searchResult = (GoogleSearchResultContent) content;
          System.out.println("Google Search Result:");
          System.out.println("  Call ID: " + searchResult.callId().orElse("N/A"));
          System.out.println("  Is Error: " + searchResult.isError().orElse(false));

          if (searchResult.result().isPresent()) {
            List<GoogleSearchResult> results = searchResult.result().get();
            System.out.println("  Results: " + results.size() + " found");
            for (int i = 0; i < results.size(); i++) {
              GoogleSearchResult result = results.get(i);
              System.out.println("  [" + (i + 1) + "] URL: " + result.url().orElse("N/A"));
              System.out.println("      Title: " + result.title().orElse("N/A"));
              if (result.renderedContent().isPresent()) {
                String renderedContent = result.renderedContent().get();
                System.out.println("      Content (first 200 chars):");
                System.out.println("      " + (renderedContent.length() > 200 ? renderedContent.substring(0, 200) + "..." : renderedContent));
              }
            }
          }
          System.out.println();
        }
      }
    }

    System.out.println("\n=== Example completed ===");
  }

  private InteractionsGoogleSearch() {}
}
