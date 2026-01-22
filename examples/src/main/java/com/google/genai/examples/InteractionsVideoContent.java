/*
 * Copyright 2025 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.CreateInteractionConfig;
import com.google.genai.types.Interaction;
import com.google.genai.types.interactions.content.InteractionContent;
import com.google.genai.types.interactions.content.TextContent;
import com.google.genai.types.interactions.content.VideoContent;

/**
 * Example demonstrating video analysis using VideoContent with the Interactions API.
 *
 * <p>This example shows:
 * <ul>
 *   <li>Using VideoContent.fromUri() to analyze a video from a URL
 *   <li>Combining VideoContent with TextContent for analysis and summarization
 * </ul>
 *
 * <p><b>Note:</b> Inline base64-encoded video is not shown in this example because video files
 * are typically large and not suitable for inline embedding in code. Use URI-based references
 * for video content.
 *
 * <p>To run this example:
 * <ol>
 *   <li>Set the GOOGLE_API_KEY environment variable: {@code export GOOGLE_API_KEY=YOUR_API_KEY}
 *   <li>Compile the examples: {@code mvn clean compile}
 *   <li>Run: {@code mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsVideoContent"}
 * </ol>
 *
 * <p><b>Note:</b> The Interactions API is currently in beta.
 */
public final class InteractionsVideoContent {

  public static void main(String[] args) {
    Client client = new Client();

    System.out.println("=== Example: Video Analysis with Interactions API ===\n");

    // ========================================
    // PART 1: Video from URI
    // ========================================
    System.out.println("PART 1: Video from URI\n");

    String videoUri =
        "https://storage.googleapis.com/cloud-samples-data/generative-ai/video/pixel8.mp4";
    String promptText = "Describe what happens in this video and summarize the key events.";

    VideoContent videoContent = VideoContent.fromUri(videoUri, "video/mp4");
    TextContent textPrompt = TextContent.builder().text(promptText).build();

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .inputFromContents(videoContent, textPrompt)
            .build();

    System.out.println("REQUEST:");
    System.out.println("  Model: gemini-2.5-flash");
    System.out.println("  Video URI: " + videoUri);
    System.out.println("  Prompt: " + promptText);
    System.out.println("\nREQUEST JSON:");
    System.out.println(config.toJson());

    Interaction response = client.interactions.create(config);

    System.out.println("\nRESPONSE JSON:");
    System.out.println(response.toJson());
    System.out.println("\nRESPONSE:");
    System.out.println("  Status: " + response.status());
    System.out.println("  Interaction ID: " + response.id().orElse("N/A"));
    System.out.print("  Output: ");
    printOutputs(response);

    System.out.println("\n=== Example completed ===");
  }

  /**
   * Helper method to extract and print text outputs from an interaction.
   *
   * @param interaction The interaction containing outputs
   */
  private static void printOutputs(Interaction interaction) {
    if (interaction.outputs().isPresent() && !interaction.outputs().get().isEmpty()) {
      for (InteractionContent output : interaction.outputs().get()) {
        if (output instanceof TextContent) {
          System.out.println(((TextContent) output).text().orElse("(empty)"));
          break;
        }
      }
    }
  }

  private InteractionsVideoContent() {}
}
