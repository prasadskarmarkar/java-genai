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
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsAsyncGoogleSearch"
 */
package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.GoogleSearchResult;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.GoogleSearchCallContent;
import com.google.genai.types.interactions.content.GoogleSearchResultContent;
import com.google.genai.types.interactions.content.TextContent;
import com.google.genai.types.interactions.tools.GoogleSearch;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Example: Async Google Search Tool
 *
 * <p>Demonstrates async Google Search tool usage with chained operations:
 *
 * <ol>
 *   <li>Async create with GoogleSearch
 *   <li>Extract search results asynchronously
 *   <li>Chain multiple search operations
 *   <li>Handle GoogleSearchResultContent in async callbacks
 *   <li>Demonstrate multi-turn search refinement
 * </ol>
 *
 * <p>This example shows how to use async operations for web searches without blocking.
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsAsyncGoogleSearch {

  public static void main(String[] args) {
    Client client = new Client();

    System.out.println("=== Interactions API: Async Google Search Example ===\n");

    try {
      GoogleSearch searchTool = GoogleSearch.builder().build();

      // ===== PART 1: Basic Async Search =====
      System.out.println("--- PART 1: Basic Async Search ---\n");

      String query1 = "What are the latest AI developments in 2025?";
      System.out.println("User: " + query1 + "\n");

      CreateInteractionConfig config1 =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .input(query1)
              .tools(searchTool)
              .build();

      System.out.println("Sending async search request...");

      client.async.interactions.create(config1)
          .thenAccept(
              response -> {
                System.out.println("\nSearch completed:");
                System.out.println("  Interaction ID: " + response.id());
                System.out.println("  Status: " + response.status());
                System.out.println();

                extractAndPrintSearchResults(response);
                printOutputs(response);
              })
          .join();

      // ===== PART 2: Chained Search Operations =====
      System.out.println("\n--- PART 2: Chained Search Operations ---\n");

      String query2 = "Latest quantum computing breakthroughs";
      System.out.println("Query 1: " + query2);

      CreateInteractionConfig config2 =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .input(query2)
              .tools(searchTool)
              .build();

      System.out.println("\nSending first search...");

      client.async.interactions.create(config2)
          .thenCompose(
              firstResponse -> {
                System.out.println("\nFirst search completed:");
                System.out.println("  Interaction ID: " + firstResponse.id());
                extractAndPrintSearchResults(firstResponse);

                // Chain a follow-up search
                String followUpQuery = "Tell me more about quantum error correction";
                System.out.println("\nFollow-up query: " + followUpQuery);

                CreateInteractionConfig followUpConfig =
                    CreateInteractionConfig.builder()
                        .model("gemini-2.5-flash")
                        .input(followUpQuery)
                        .previousInteractionId(firstResponse.id())
                        .tools(searchTool)
                        .build();

                System.out.println("Sending follow-up search...");
                return client.async.interactions.create(followUpConfig);
              })
          .thenAccept(
              finalResponse -> {
                System.out.println("\nFollow-up search completed:");
                System.out.println("  Interaction ID: " + finalResponse.id());
                System.out.println("  Previous ID: "
                    + finalResponse.previousInteractionId().orElse("N/A"));
                extractAndPrintSearchResults(finalResponse);
                printOutputs(finalResponse);
              })
          .join();

      // ===== PART 3: Multiple Parallel Searches =====
      System.out.println("\n--- PART 3: Multiple Parallel Searches ---\n");
      System.out.println("Executing 3 searches in parallel...\n");

      CompletableFuture<Interaction> search1 =
          client.async.interactions.create(
              CreateInteractionConfig.builder()
                  .model("gemini-2.5-flash")
                  .input("Latest machine learning trends 2025")
                  .tools(searchTool)
                  .build());

      CompletableFuture<Interaction> search2 =
          client.async.interactions.create(
              CreateInteractionConfig.builder()
                  .model("gemini-2.5-flash")
                  .input("Climate change solutions 2025")
                  .tools(searchTool)
                  .build());

      CompletableFuture<Interaction> search3 =
          client.async.interactions.create(
              CreateInteractionConfig.builder()
                  .model("gemini-2.5-flash")
                  .input("Space exploration missions 2025")
                  .tools(searchTool)
                  .build());

      // Attach callbacks
      search1.thenAccept(
          r -> {
            System.out.println("Search 1 completed: " + r.id());
            extractAndPrintSearchResults(r);
          });

      search2.thenAccept(
          r -> {
            System.out.println("\nSearch 2 completed: " + r.id());
            extractAndPrintSearchResults(r);
          });

      search3.thenAccept(
          r -> {
            System.out.println("\nSearch 3 completed: " + r.id());
            extractAndPrintSearchResults(r);
          });

      // Wait for all searches to complete
      CompletableFuture.allOf(search1, search2, search3)
          .thenRun(() -> System.out.println("\n\nAll parallel searches completed!"))
          .join();

      // ===== PART 4: Search with Result Processing =====
      System.out.println("\n--- PART 4: Search with Result Processing ---\n");

      String query4 = "Best programming languages in 2025";
      System.out.println("User: " + query4 + "\n");

      CreateInteractionConfig config4 =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .input(query4)
              .tools(searchTool)
              .build();

      client.async.interactions.create(config4)
          .thenAccept(
              response -> {
                System.out.println("Search completed: " + response.id());
                System.out.println();

                // Process search results
                if (response.outputs().isPresent()) {
                  int searchCallCount = 0;
                  int searchResultCount = 0;
                  int totalResults = 0;

                  for (Content content : response.outputs().get()) {
                    if (content instanceof GoogleSearchCallContent) {
                      searchCallCount++;
                    } else if (content instanceof GoogleSearchResultContent) {
                      searchResultCount++;
                      GoogleSearchResultContent searchResult =
                          (GoogleSearchResultContent) content;
                      if (searchResult.result().isPresent()) {
                        totalResults += searchResult.result().get().size();
                      }
                    }
                  }

                  System.out.println("Search Statistics:");
                  System.out.println("  Search calls: " + searchCallCount);
                  System.out.println("  Search results: " + searchResultCount);
                  System.out.println("  Total URLs found: " + totalResults);
                  System.out.println();
                }

                extractAndPrintSearchResults(response);
                printOutputs(response);
              })
          .join();

      System.out.println("\n=== Example completed ===");

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /** Extracts and prints Google Search results from an interaction. */
  private static void extractAndPrintSearchResults(Interaction interaction) {
    if (!interaction.outputs().isPresent()) {
      return;
    }

    for (Content content : interaction.outputs().get()) {
      if (content instanceof GoogleSearchCallContent) {
        GoogleSearchCallContent searchCall = (GoogleSearchCallContent) content;
        System.out.println("  Google Search Call:");
        System.out.println("    ID: " + searchCall.id());
        if (searchCall.arguments().isPresent()) {
          System.out.println("    Queries: "
              + searchCall.arguments().get().queries().orElse(List.of()));
        }
      } else if (content instanceof GoogleSearchResultContent) {
        GoogleSearchResultContent searchResult = (GoogleSearchResultContent) content;
        System.out.println("  Google Search Result:");
        System.out.println("    Call ID: " + searchResult.callId().orElse("N/A"));
        System.out.println("    Is Error: " + searchResult.isError().orElse(false));

        if (searchResult.result().isPresent()) {
          List<GoogleSearchResult> results = searchResult.result().get();
          System.out.println("    Results: " + results.size() + " found");

          // Print first 3 results
          int limit = Math.min(3, results.size());
          for (int i = 0; i < limit; i++) {
            GoogleSearchResult result = results.get(i);
            System.out.println("    [" + (i + 1) + "] " + result.title().orElse("N/A"));
            System.out.println("        URL: " + result.url().orElse("N/A"));
          }
          if (results.size() > 3) {
            System.out.println("    ... and " + (results.size() - 3) + " more results");
          }
        }
      }
    }
  }

  /** Helper method to print interaction outputs. */
  private static void printOutputs(Interaction interaction) {
    if (interaction.outputs().isPresent() && !interaction.outputs().get().isEmpty()) {
      for (Content output : interaction.outputs().get()) {
        if (output instanceof TextContent) {
          String text = ((TextContent) output).text().orElse("(empty)");
          System.out.println("  Model Response:");
          if (text.length() > 200) {
            text = text.substring(0, 200) + "...";
          }
          System.out.println("    " + text);
          break;
        }
      }
    }
  }

  private InteractionsAsyncGoogleSearch() {}
}
