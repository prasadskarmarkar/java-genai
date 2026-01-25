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
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsGoogleSearchMultiContent"
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
import com.google.genai.types.interactions.content.ImageContent;
import com.google.genai.types.interactions.content.TextContent;
import com.google.genai.types.interactions.content.VideoContent;
import com.google.genai.types.interactions.tools.GoogleSearch;
import java.util.List;

/**
 * Example: GoogleSearch with Multiple Content Types
 *
 * <p>Demonstrates GoogleSearch with multiple content types as input:
 *
 * <ul>
 *   <li>3 TextContent items
 *   <li>1 ImageContent item
 *   <li>1 VideoContent item
 * </ul>
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsGoogleSearchMultiContent {

  public static void main(String[] args) {
    // Instantiate the client. The client gets the API key from the environment variable
    // `GOOGLE_API_KEY`.
    //
    Client client = new Client();

    System.out.println("=== Interactions API: GoogleSearch with Multiple Contents ===\n");

    // ===== Configure GoogleSearch =====
    GoogleSearch googleSearchTool = GoogleSearch.builder().build();

    // ===== Create Multiple Content Items =====
    System.out.println("--- Creating Multiple Content Items ---\n");

    TextContent textContent1 = TextContent.builder()
        .text("I'm showing you an image of a cake and a video about a phone.")
        .build();
    System.out.println("TextContent 1: Context about the media");

    TextContent textContent2 = TextContent.builder()
        .text("Please analyze both the image and video.")
        .build();
    System.out.println("TextContent 2: Analysis request");

    TextContent textContent3 = TextContent.builder()
        .text("Then use Google Search to find the latest news about similar products in 2025.")
        .build();
    System.out.println("TextContent 3: Search instruction");

    String imageUri = "https://storage.googleapis.com/generativeai-downloads/images/cake.jpg";
    ImageContent imageContent = ImageContent.fromUri(imageUri, "image/jpeg");
    System.out.println("ImageContent: cake.jpg");

    String videoUri = "https://storage.googleapis.com/cloud-samples-data/generative-ai/video/pixel8.mp4";
    VideoContent videoContent = VideoContent.fromUri(videoUri, "video/mp4");
    System.out.println("VideoContent: pixel8.mp4\n");

    // ===== Create Interaction =====
    System.out.println("--- Creating Interaction ---\n");

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .inputFromContents(
                textContent1,
                textContent2,
                imageContent,
                videoContent,
                textContent3)
            .tools(googleSearchTool)
            .build();

    Interaction response = client.interactions.create(config);
    String interactionId = response.id();

    System.out.println("Response received. Interaction ID: " + interactionId);
    System.out.println("Status: " + response.status());
    processOutputs(response);

    // ===== Verify with Get Interaction =====
    System.out.println("\n--- Verify with Get Interaction ---\n");

    GetInteractionConfig getConfig = GetInteractionConfig.builder().build();
    Interaction retrieved = client.interactions.get(interactionId, getConfig);

    System.out.println("Retrieved interaction ID: " + retrieved.id());
    System.out.println("Status: " + retrieved.status());
    if (retrieved.outputs().isPresent()) {
      System.out.println("Outputs count: " + retrieved.outputs().get().size());
      System.out.println("Output types:");
      for (Content output : retrieved.outputs().get()) {
        System.out.println("  - " + output.getClass().getSimpleName());
      }
    }

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
      } else if (output instanceof GoogleSearchResultContent) {
        GoogleSearchResultContent searchResult = (GoogleSearchResultContent) output;
        System.out.println("\nGoogleSearchResult: callId=" + searchResult.callId().orElse("N/A"));
        if (searchResult.result().isPresent()) {
          List<GoogleSearchResult> results = searchResult.result().get();
          System.out.println("  results count: " + results.size());
        }
      }
    }
  }

  private InteractionsGoogleSearchMultiContent() {}
}
