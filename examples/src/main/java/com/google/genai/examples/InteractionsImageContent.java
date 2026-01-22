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
import com.google.genai.types.interactions.content.ImageContent;
import com.google.genai.types.interactions.content.InteractionContent;
import com.google.genai.types.interactions.content.TextContent;

/**
 * Example demonstrating image analysis using ImageContent with the Interactions API.
 *
 * <p>This example shows:
 * <ul>
 *   <li>Using ImageContent.fromUri() to analyze an image from a URL
 *   <li>Using ImageContent.fromData() to analyze a base64-encoded image
 *   <li>Combining ImageContent with TextContent for context/questions
 * </ul>
 *
 * <p>To run this example:
 * <ol>
 *   <li>Set the GOOGLE_API_KEY environment variable: {@code export GOOGLE_API_KEY=YOUR_API_KEY}
 *   <li>Compile the examples: {@code mvn clean compile}
 *   <li>Run: {@code mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsImageContent"}
 * </ol>
 *
 * <p><b>Note:</b> The Interactions API is currently in beta.
 */
public final class InteractionsImageContent {

  public static void main(String[] args) {
    Client client = new Client();

    System.out.println("=== Example: Image Analysis with Interactions API ===\n");

    // ========================================
    // PART 1: Image from URI
    // ========================================
    System.out.println("PART 1: Image from URI\n");

    String imageUri = "https://storage.googleapis.com/generativeai-downloads/images/cake.jpg";
    String promptText = "Describe what you see in this image.";

    ImageContent imageContent = ImageContent.fromUri(imageUri, "image/jpeg");
    TextContent textPrompt = TextContent.builder().text(promptText).build();

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .inputFromContents(imageContent, textPrompt)
            .build();

    System.out.println("REQUEST:");
    System.out.println("  Model: gemini-2.5-flash");
    System.out.println("  Image URI: " + imageUri);
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

    // ========================================
    // PART 2: Image from Inline Data (base64)
    // ========================================
    System.out.println("\n\nPART 2: Image from Inline Data (Base64)\n");

    // Small 1x1 red pixel PNG encoded as base64
    // To encode your own file: Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get("image.png")))
    String base64Data =
        "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8DwHwAFBQIAX8jx0gAAAABJRU5ErkJggg==";
    String promptText2 = "What color is this image?";

    ImageContent imageFromData = ImageContent.fromData(base64Data, "image/png");
    TextContent textPrompt2 = TextContent.builder().text(promptText2).build();

    CreateInteractionConfig config2 =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .inputFromContents(imageFromData, textPrompt2)
            .build();

    System.out.println("REQUEST:");
    System.out.println("  Model: gemini-2.5-flash");
    System.out.println("  Image: Inline base64 data (1x1 pixel PNG)");
    System.out.println("  Prompt: " + promptText2);
    System.out.println("\nREQUEST JSON:");
    System.out.println(config2.toJson());

    Interaction response2 = client.interactions.create(config2);

    System.out.println("\nRESPONSE JSON:");
    System.out.println(response2.toJson());
    System.out.println("\nRESPONSE:");
    System.out.println("  Status: " + response2.status());
    System.out.println("  Interaction ID: " + response2.id().orElse("N/A"));
    System.out.print("  Output: ");
    printOutputs(response2);

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

  private InteractionsImageContent() {}
}
