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
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsCreateExample"
 */
package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.GetInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.AudioContent;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.DocumentContent;
import com.google.genai.types.interactions.content.ImageContent;
import com.google.genai.types.interactions.content.TextContent;
import com.google.genai.types.interactions.content.ThoughtContent;
import com.google.genai.types.interactions.content.VideoContent;

/**
 * Example: Create Interaction with All Content Types
 *
 * <p>Demonstrates interactions.create() with all available INPUT content types:
 *
 * <ul>
 *   <li>TextContent - Plain text input
 *   <li>ImageContent - Image via URL
 *   <li>AudioContent - Audio via URL
 *   <li>VideoContent - Video via URL
 *   <li>DocumentContent - PDF document via URL
 * </ul>
 *
 * <p>Also shows all possible OUTPUT content types that may be returned.
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsCreateExample {

  public static void main(String[] args) {
    // Instantiate the client. The client gets the API key from the environment variable
    // `GOOGLE_API_KEY`.
    //
    Client client = new Client();

    System.out.println("=== Interactions API: All Content Types Example ===\n");

    try {
      // ===== PART 1: TextContent =====
      System.out.println("--- PART 1: TextContent (Create API) ---\n");

      TextContent textContent = TextContent.builder()
          .text("What is the meaning of life?")
          .build();

      System.out.println("Input Content Types:");
      System.out.println("  - TextContent: \"What is the meaning of life?\"\n");

      CreateInteractionConfig textConfig =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .inputFromContents(textContent)
              .build();

      Interaction textResponse = client.interactions.create(textConfig);
      printResponse("TextContent", textResponse);

      // ===== PART 2: ImageContent =====
      System.out.println("\n--- PART 2: ImageContent (Create API) ---\n");

      String imageUri = "https://storage.googleapis.com/generativeai-downloads/images/cake.jpg";
      ImageContent imageContent = ImageContent.fromUri(imageUri, "image/jpeg");
      TextContent imagePrompt = TextContent.builder()
          .text("Describe this image in detail.")
          .build();

      System.out.println("Input Content Types:");
      System.out.println("  - TextContent: \"Describe this image in detail.\"");
      System.out.println("  - ImageContent: cake.jpg (URL)\n");

      CreateInteractionConfig imageConfig =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .inputFromContents(imagePrompt, imageContent)
              .build();

      Interaction imageResponse = client.interactions.create(imageConfig);
      printResponse("ImageContent", imageResponse);

      // ===== PART 3: AudioContent =====
      System.out.println("\n--- PART 3: AudioContent (Create API) ---\n");

      String audioUri = "https://storage.googleapis.com/cloud-samples-data/generative-ai/audio/pixel.mp3";
      AudioContent audioContent = AudioContent.fromUri(audioUri, "audio/mp3");
      TextContent audioPrompt = TextContent.builder()
          .text("Transcribe and summarize this audio.")
          .build();

      System.out.println("Input Content Types:");
      System.out.println("  - TextContent: \"Transcribe and summarize this audio.\"");
      System.out.println("  - AudioContent: pixel.mp3 (URL)\n");

      CreateInteractionConfig audioConfig =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .inputFromContents(audioPrompt, audioContent)
              .build();

      Interaction audioResponse = client.interactions.create(audioConfig);
      printResponse("AudioContent", audioResponse);

      // ===== PART 4: VideoContent =====
      System.out.println("\n--- PART 4: VideoContent (Create API) ---\n");

      String videoUri = "https://storage.googleapis.com/cloud-samples-data/generative-ai/video/pixel8.mp4";
      VideoContent videoContent = VideoContent.fromUri(videoUri, "video/mp4");
      TextContent videoPrompt = TextContent.builder()
          .text("Describe what happens in this video.")
          .build();

      System.out.println("Input Content Types:");
      System.out.println("  - TextContent: \"Describe what happens in this video.\"");
      System.out.println("  - VideoContent: pixel8.mp4 (URL)\n");

      CreateInteractionConfig videoConfig =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .inputFromContents(videoPrompt, videoContent)
              .build();

      Interaction videoResponse = client.interactions.create(videoConfig);
      printResponse("VideoContent", videoResponse);

      // ===== PART 5: DocumentContent (PDF) =====
      System.out.println("\n--- PART 5: DocumentContent (Create API) ---\n");

      String pdfUri = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf";
      DocumentContent documentContent = DocumentContent.fromUri(pdfUri, "application/pdf");
      TextContent docPrompt = TextContent.builder()
          .text("Summarize the main points of this document.")
          .build();

      System.out.println("Input Content Types:");
      System.out.println("  - TextContent: \"Summarize the main points of this document.\"");
      System.out.println("  - DocumentContent: PDF (URL)\n");

      CreateInteractionConfig docConfig =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .inputFromContents(docPrompt, documentContent)
              .build();

      Interaction docResponse = client.interactions.create(docConfig);
      printResponse("DocumentContent", docResponse);

      // ===== PART 6: Multiple Content Types Combined =====
      System.out.println("\n--- PART 6: Multiple Content Types Combined (Create API) ---\n");

      TextContent multiPrompt = TextContent.builder()
          .text("Compare the cake image with what you see in the video. Are they related?")
          .build();

      System.out.println("Input Content Types:");
      System.out.println("  - TextContent: \"Compare the cake image...\"");
      System.out.println("  - ImageContent: cake.jpg (URL)");
      System.out.println("  - VideoContent: pixel8.mp4 (URL)\n");

      CreateInteractionConfig multiConfig =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .inputFromContents(multiPrompt, imageContent, videoContent)
              .build();

      Interaction multiResponse = client.interactions.create(multiConfig);
      printResponse("Multiple", multiResponse);

      // ===== PART 7: Verify with Get API =====
      System.out.println("\n--- PART 7: Verify Last Interaction (Get API) ---\n");

      GetInteractionConfig getConfig = GetInteractionConfig.builder().build();
      Interaction retrieved = client.interactions.get(multiResponse.id(), getConfig);

      System.out.println("GET Response:");
      System.out.println("  Interaction ID: " + retrieved.id());
      System.out.println("  Status: " + retrieved.status());
      System.out.println("  Model: " + retrieved.model().orElse("N/A"));
      printOutputsWithTypes(retrieved);

      System.out.println("\n=== Example completed ===");

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /** Prints response summary for each content type test. */
  private static void printResponse(String testName, Interaction interaction) {
    System.out.println("CREATE Response (" + testName + "):");
    System.out.println("  Interaction ID: " + interaction.id());
    System.out.println("  Status: " + interaction.status());
    printOutputsWithTypes(interaction);
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

  private InteractionsCreateExample() {}
}
