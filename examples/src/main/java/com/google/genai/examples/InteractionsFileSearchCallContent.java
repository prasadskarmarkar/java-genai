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
 * <p>IMPORTANT: This example requires file search stores to be created beforehand.
 *
 * <p>Setup Instructions:
 *
 * <p>1. Create a file search store using the FileSearchStores API or gcloud CLI
 *
 * <p>2. Upload documents to your file search store
 *
 * <p>3. Set an API key environment variable:
 *
 * <p>export GOOGLE_API_KEY=YOUR_API_KEY
 *
 * <p>4. Update the file search store name in this example to match your created store.
 *
 * <p>5. Compile and run:
 *
 * <p>mvn clean compile
 *
 * <p>mvn exec:java
 * -Dexec.mainClass="com.google.genai.examples.InteractionsFileSearchCallContent"
 */
package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.FileSearchResult;
import com.google.genai.types.interactions.content.FileSearchCallContent;
import com.google.genai.types.interactions.content.FileSearchResultContent;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.TextContent;
import com.google.genai.types.interactions.tools.FileSearch;

/**
 * FileSearchCallContent Testing Example
 *
 * <p>This example demonstrates how to work with FileSearchCallContent in the Interactions API.
 * FileSearchCallContent represents the model's file search operation call, containing the search
 * query and call ID.
 *
 * <p>Structure: FileSearchCallContent contains:
 * - type: Literal "file_search_call"
 * - id: Optional unique identifier for the file search call
 *
 * <p>Note: Unlike other tool calls, FileSearchCallContent has no arguments field.
 * All search configuration (stores, top_k, filters) is defined in the FileSearch.
 * The content type appears in the interaction outputs when the model decides to
 * search through file stores.
 *
 * <p>Test Cases:
 * 1. Single Query - Basic file search with one query
 * 2. Multiple Queries - Testing file search across multiple stores
 * 3. JSON Serialization - Testing FileSearchCallContent creation and serialization
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsFileSearchCallContent {

  public static void main(String[] args) {
    Client client = new Client();

    System.out.println("=== FileSearchCallContent Testing Example ===\n");
    System.out.println("Testing FileSearchCallContent in the Interactions API\n");

    // Test Case 1: Basic File Search
    testBasicFileSearch(client);

    // Test Case 2: Complex Query
    testComplexQuery(client);

    // Test Case 3: JSON Serialization/Deserialization
    testJsonSerialization();

    System.out.println("\n=== All tests completed ===");
  }

  /**
   * Test Case 1: Basic File Search
   *
   * <p>Tests FileSearchCallContent with a basic query to search through file stores.
   */
  private static void testBasicFileSearch(Client client) {
    System.out.println("\n--- Test Case 1: Basic File Search ---\n");

    String prompt = "Search the documentation and tell me about the API authentication methods.";

    // NOTE: Update "my-document-store" to match your actual file search store name
    FileSearch fileSearchTool =
        FileSearch.builder()
            .fileSearchStoreNames("my-document-store")
            .topK(5)
            .build();

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .input(prompt)
            .tools(fileSearchTool)
            .build();

    System.out.println("REQUEST:");
    System.out.println("  Prompt: " + prompt);
    System.out.println("  File Search Store: my-document-store");
    System.out.println();

    try {
      Interaction response = client.interactions.create(config);

      System.out.println("FULL RESPONSE JSON:");
      System.out.println(response.toJson());
      System.out.println();

      System.out.println("PARSED RESPONSE:");
      System.out.println("  Status: " + response.status());
      System.out.println("  Interaction ID: " + response.id());
      System.out.println();

      analyzeContent(response);

    } catch (Exception e) {
      System.err.println("ERROR in Test Case 1:");
      System.err.println("  Exception: " + e.getClass().getName());
      System.err.println("  Message: " + e.getMessage());
      System.err.println();
      System.err.println("NOTE: This example requires file search stores to be created.");
      System.err.println("Please check:");
      System.err.println("  1. File search store exists with the name specified");
      System.err.println("  2. Files have been uploaded to the store");
      System.err.println("  3. Your API key has access to the store");
      e.printStackTrace();
    }
  }

  /**
   * Test Case 2: Complex Query
   *
   * <p>Tests FileSearchCallContent with a more complex query that may trigger multiple searches.
   */
  private static void testComplexQuery(Client client) {
    System.out.println("\n--- Test Case 2: Complex Multi-Document Query ---\n");

    String prompt =
        "Search through the uploaded documentation and provide a comprehensive summary of:\n"
            + "1. System requirements\n"
            + "2. Installation procedures\n"
            + "3. Configuration options\n"
            + "Please cite specific sections from the documents.";

    // NOTE: Update store names to match your actual file search stores
    FileSearch fileSearchTool =
        FileSearch.builder()
            .fileSearchStoreNames("my-document-store-1", "my-document-store-2")
            .topK(10)
            .build();

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .input(prompt)
            .tools(fileSearchTool)
            .build();

    System.out.println("REQUEST:");
    System.out.println("  Prompt: " + prompt);
    System.out.println("  File Search Stores: my-document-store-1, my-document-store-2");
    System.out.println();

    try {
      Interaction response = client.interactions.create(config);

      System.out.println("FULL RESPONSE JSON:");
      System.out.println(response.toJson());
      System.out.println();

      System.out.println("PARSED RESPONSE:");
      System.out.println("  Status: " + response.status());
      System.out.println("  Interaction ID: " + response.id());
      System.out.println();

      analyzeContent(response);

    } catch (Exception e) {
      System.err.println("ERROR in Test Case 2:");
      System.err.println("  Exception: " + e.getClass().getName());
      System.err.println("  Message: " + e.getMessage());
      System.err.println();
      System.err.println("NOTE: This example requires file search stores to be created.");
      e.printStackTrace();
    }
  }

  /**
   * Test Case 3: JSON Serialization/Deserialization
   *
   * <p>Tests FileSearchCallContent creation, serialization, and deserialization.
   */
  private static void testJsonSerialization() {
    System.out.println("\n--- Test Case 3: JSON Serialization/Deserialization ---\n");

    try {
      // Create FileSearchCallContent programmatically
      FileSearchCallContent content =
          FileSearchCallContent.of("file-search-call-123");

      System.out.println("CREATED FileSearchCallContent:");
      System.out.println("  ID: " + content.id());
      System.out.println();

      // Serialize to JSON
      String json = content.toJson();
      System.out.println("SERIALIZED JSON:");
      System.out.println("  " + json);
      System.out.println();

      // Deserialize from JSON
      FileSearchCallContent deserialized = FileSearchCallContent.fromJson(json);
      System.out.println("DESERIALIZED FileSearchCallContent:");
      System.out.println("  ID: " + deserialized.id());
      System.out.println();

      // Verify they match
      boolean matches = content.id().equals(deserialized.id());
      System.out.println("VERIFICATION:");
      System.out.println("  Serialization/Deserialization successful: " + matches);
      System.out.println();

    } catch (Exception e) {
      System.err.println("ERROR in Test Case 3:");
      System.err.println("  Exception: " + e.getClass().getName());
      System.err.println("  Message: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Analyzes interaction content to identify and display FileSearchCallContent and related content.
   *
   * <p>This method:
   * - Iterates through all outputs in the interaction
   * - Identifies FileSearchCallContent instances
   * - Displays file search call details (query, call ID)
   * - Shows corresponding FileSearchResultContent
   * - Compares with expected structure
   */
  private static void analyzeContent(Interaction interaction) {
    System.out.println("CONTENT ANALYSIS:");

    if (!interaction.outputs().isPresent() || interaction.outputs().get().isEmpty()) {
      System.out.println("  No outputs found in response");
      return;
    }

    int fileSearchCallCount = 0;
    int fileSearchResultCount = 0;
    int textCount = 0;
    int otherCount = 0;

    System.out.println("\n  Analyzing " + interaction.outputs().get().size() + " output(s):");

    for (Content content : interaction.outputs().get()) {
      System.out.println("\n    Content Type: " + content.getClass().getSimpleName());

      if (content instanceof FileSearchCallContent) {
        fileSearchCallCount++;
        FileSearchCallContent fileSearchCall = (FileSearchCallContent) content;

        System.out.println("    >>> FILE SEARCH CALL CONTENT FOUND <<<");
        System.out.println("      Call ID: " + fileSearchCall.id());
        System.out.println("      Full FileSearchCallContent JSON:");
        System.out.println("      " + fileSearchCall.toJson());

      } else if (content instanceof FileSearchResultContent) {
        fileSearchResultCount++;
        FileSearchResultContent resultContent = (FileSearchResultContent) content;

        System.out.println("    >>> FILE SEARCH RESULT CONTENT FOUND <<<");

        if (resultContent.result().isPresent()) {
          for (FileSearchResult result : resultContent.result().get()) {
            System.out.println("      Title: " + result.title().orElse("N/A"));
            System.out.println(
                "      File Search Store: " + result.fileSearchStore().orElse("N/A"));

            if (result.text().isPresent()) {
              String text = result.text().get();
              String preview = text.length() > 200 ? text.substring(0, 200) + "..." : text;
              System.out.println("      Text Preview: " + preview);
              System.out.println("      Total text length: " + text.length() + " chars");
            }
          }
        } else {
          System.out.println("      Result: (none)");
        }

      } else if (content instanceof TextContent) {
        textCount++;
        TextContent text = (TextContent) content;
        String textValue = text.text().orElse("(empty)");
        String preview =
            textValue.length() > 150 ? textValue.substring(0, 150) + "..." : textValue;
        System.out.println("      Text Preview: " + preview);

      } else {
        otherCount++;
        System.out.println("      Content Class: " + content.getClass().getName());
      }
    }

    System.out.println("\n  SUMMARY:");
    System.out.println("    FileSearchCallContent instances: " + fileSearchCallCount);
    System.out.println("    FileSearchResultContent instances: " + fileSearchResultCount);
    System.out.println("    TextContent instances: " + textCount);
    System.out.println("    Other content instances: " + otherCount);

    if (fileSearchCallCount == 0) {
      System.out.println(
          "\n  NOTE: No FileSearchCallContent found. This may indicate:");
      System.out.println("    - The model didn't trigger a file search for this query");
      System.out.println("    - The file search store may not exist or is empty");
      System.out.println("    - The model decided to answer without searching files");
    }

    // Display final text output
    if (interaction.outputs().isPresent() && !interaction.outputs().get().isEmpty()) {
      System.out.println("\n  FINAL TEXT OUTPUT:");
      for (Content output : interaction.outputs().get()) {
        if (output instanceof TextContent) {
          String text = ((TextContent) output).text().orElse("(empty)");
          String displayText = text.length() > 500 ? text.substring(0, 500) + "..." : text;
          System.out.println("    " + displayText);
        }
      }
    }
  }

  private InteractionsFileSearchCallContent() {}
}
