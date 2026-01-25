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
 * <p>mvn exec:java
 * -Dexec.mainClass="com.google.genai.examples.InteractionsGenerationConfigComprehensive"
 */
package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.ThinkingLevel;
import com.google.genai.types.interactions.AllowedTools;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.GenerationConfig;
import com.google.genai.types.interactions.ImageConfig;
import com.google.genai.types.interactions.SpeechConfig;
import com.google.genai.types.interactions.ThinkingSummaries;
import com.google.genai.types.interactions.ToolChoice;
import com.google.genai.types.interactions.ToolChoiceConfig;
import com.google.genai.types.interactions.ToolChoiceType;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.TextContent;
import java.util.Arrays;

/**
 * Comprehensive demonstration of ALL GenerationConfig fields.
 *
 * <p>This example demonstrates all 10 configuration fields:
 *
 * <ul>
 *   <li>Basic controls: temperature, topP, seed, maxOutputTokens
 *   <li>Advanced controls: stopSequences, toolChoice
 *   <li>Thinking controls: thinkingLevel, thinkingSummaries
 *   <li>Multimodal output: speechConfig, imageConfig
 * </ul>
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsGenerationConfigComprehensive {

  private static final String MODEL = "gemini-2.5-flash";
  private static final Client client = new Client();

  public static void main(String[] args) {
    System.out.println("=== GenerationConfig Comprehensive Testing ===\n");

    // Run all demonstrations
    demonstrateBasicControls();
    demonstrateStopSequences();
    demonstrateToolChoice();
    demonstrateThinkingControls();
    demonstrateSpeechConfig();
    demonstrateImageConfig();
    demonstrateCombinedConfig();

    System.out.println("\n=== All demonstrations completed ===");
  }

  /**
   * PART 1: Basic Generation Controls
   *
   * <p>Demonstrates: temperature, topP, seed, maxOutputTokens
   */
  private static void demonstrateBasicControls() {
    System.out.println("=== PART 1: Basic Controls ===\n");

    // Example 1.1: Temperature control (0.0 to 2.0)
    // Higher temperature = more random/creative responses
    System.out.println("Example 1.1: Temperature Control");
    GenerationConfig config1 =
        GenerationConfig.builder()
            .temperature(0.9f) // Higher creativity
            .build();

    CreateInteractionConfig interactionConfig1 =
        CreateInteractionConfig.builder()
            .model(MODEL)
            .input("Tell me a creative story idea in one sentence.")
            .generationConfig(config1)
            .build();

    System.out.println("Config JSON:\n" + config1.toJson());
    Interaction response1 = client.interactions.create(interactionConfig1);
    System.out.println("Response: " + extractText(response1) + "\n");

    // Example 1.2: TopP (nucleus sampling)
    // Controls diversity via cumulative probability
    System.out.println("Example 1.2: TopP (Nucleus Sampling)");
    GenerationConfig config2 =
        GenerationConfig.builder()
            .topP(0.8f) // Consider only top 80% probability mass
            .build();

    CreateInteractionConfig interactionConfig2 =
        CreateInteractionConfig.builder()
            .model(MODEL)
            .input("What is 2 + 2?")
            .generationConfig(config2)
            .build();

    System.out.println("Config JSON:\n" + config2.toJson());
    Interaction response2 = client.interactions.create(interactionConfig2);
    System.out.println("Response: " + extractText(response2) + "\n");

    // Example 1.3: Seed for reproducibility
    // Same seed should produce similar outputs
    System.out.println("Example 1.3: Seed for Reproducibility");
    GenerationConfig config3 =
        GenerationConfig.builder()
            .seed(42)
            .temperature(1.0f)
            .build();

    CreateInteractionConfig interactionConfig3 =
        CreateInteractionConfig.builder()
            .model(MODEL)
            .input("Generate a random number.")
            .generationConfig(config3)
            .build();

    System.out.println("Config JSON:\n" + config3.toJson());
    Interaction response3 = client.interactions.create(interactionConfig3);
    System.out.println("Response: " + extractText(response3) + "\n");

    // Example 1.4: MaxOutputTokens
    // Limit the length of responses
    System.out.println("Example 1.4: MaxOutputTokens");
    GenerationConfig config4 =
        GenerationConfig.builder().maxOutputTokens(50).build();

    CreateInteractionConfig interactionConfig4 =
        CreateInteractionConfig.builder()
            .model(MODEL)
            .input("Explain quantum physics.")
            .generationConfig(config4)
            .build();

    System.out.println("Config JSON:\n" + config4.toJson());
    Interaction response4 = client.interactions.create(interactionConfig4);
    System.out.println("Response: " + extractText(response4) + "\n");

    System.out.println("=== PART 1 Complete ===\n");
  }

  /**
   * PART 2: Stop Sequences
   *
   * <p>Demonstrates: stopSequences field
   */
  private static void demonstrateStopSequences() {
    System.out.println("=== PART 2: Stop Sequences ===\n");

    // Example 2.1: Single stop sequence
    System.out.println("Example 2.1: Stop at specific sequence");
    GenerationConfig config =
        GenerationConfig.builder()
            .stopSequences(Arrays.asList(".", "!"))
            .build();

    CreateInteractionConfig interactionConfig =
        CreateInteractionConfig.builder()
            .model(MODEL)
            .input("Count from 1 to 10")
            .generationConfig(config)
            .build();

    System.out.println("Config JSON:\n" + config.toJson());
    Interaction response = client.interactions.create(interactionConfig);
    System.out.println("Response: " + extractText(response) + "\n");

    System.out.println("=== PART 2 Complete ===\n");
  }

  /**
   * PART 3: Tool Choice Configurations
   *
   * <p>Demonstrates: toolChoice with all types (AUTO, ANY, NONE, VALIDATED) and ToolChoiceConfig
   */
  private static void demonstrateToolChoice() {
    System.out.println("=== PART 3: Tool Choice ===\n");

    // Example 3.1: AUTO - Model decides whether to use tools
    System.out.println("Example 3.1: ToolChoice.AUTO");
    GenerationConfig config1 =
        GenerationConfig.builder()
            .toolChoice(ToolChoice.fromType(ToolChoiceType.Known.AUTO))
            .build();

    System.out.println("Config JSON:\n" + config1.toJson());
    System.out.println("ToolChoice type: AUTO (model decides)\n");

    // Example 3.2: ANY - Model must use a tool
    System.out.println("Example 3.2: ToolChoice.ANY");
    GenerationConfig config2 =
        GenerationConfig.builder()
            .toolChoice(ToolChoice.fromType(ToolChoiceType.Known.ANY))
            .build();

    System.out.println("Config JSON:\n" + config2.toJson());
    System.out.println("ToolChoice type: ANY (must use a tool)\n");

    // Example 3.3: NONE - Model must not use tools
    System.out.println("Example 3.3: ToolChoice.NONE");
    GenerationConfig config3 =
        GenerationConfig.builder()
            .toolChoice(ToolChoice.fromType(ToolChoiceType.Known.NONE))
            .build();

    System.out.println("Config JSON:\n" + config3.toJson());
    System.out.println("ToolChoice type: NONE (no tools allowed)\n");

    // Example 3.4: VALIDATED - Model uses validated tools
    System.out.println("Example 3.4: ToolChoice.VALIDATED");
    GenerationConfig config4 =
        GenerationConfig.builder()
            .toolChoice(ToolChoice.fromType(ToolChoiceType.Known.VALIDATED))
            .build();

    System.out.println("Config JSON:\n" + config4.toJson());
    System.out.println("ToolChoice type: VALIDATED (validated tools only)\n");

    // Example 3.5: ToolChoiceConfig with allowed tools
    System.out.println("Example 3.5: ToolChoiceConfig with AllowedTools");
    ToolChoiceConfig toolChoiceConfig =
        ToolChoiceConfig.builder()
            .allowedTools(
                AllowedTools.builder()
                    .mode("auto")
                    .tools(Arrays.asList("search_tool", "calculator"))
                    .build())
            .build();

    GenerationConfig config5 =
        GenerationConfig.builder()
            .toolChoice(ToolChoice.fromConfig(toolChoiceConfig))
            .build();

    System.out.println("Config JSON:\n" + config5.toJson());
    System.out.println("ToolChoiceConfig: Allows search_tool and calculator\n");

    System.out.println("=== PART 3 Complete ===\n");
  }

  /**
   * PART 4: Thinking Controls
   *
   * <p>Demonstrates: thinkingLevel (MINIMAL, LOW, MEDIUM, HIGH) and thinkingSummaries (AUTO, NONE)
   */
  private static void demonstrateThinkingControls() {
    System.out.println("=== PART 4: Thinking Controls ===\n");

    // Example 4.1: ThinkingLevel.MINIMAL
    System.out.println("Example 4.1: ThinkingLevel.MINIMAL");
    GenerationConfig config1 =
        GenerationConfig.builder()
            .thinkingLevel(new ThinkingLevel(ThinkingLevel.Known.MINIMAL))
            .build();

    System.out.println("Config JSON:\n" + config1.toJson());
    System.out.println("ThinkingLevel: MINIMAL (quick responses)\n");

    // Example 4.2: ThinkingLevel.LOW
    System.out.println("Example 4.2: ThinkingLevel.LOW");
    GenerationConfig config2 =
        GenerationConfig.builder()
            .thinkingLevel(new ThinkingLevel(ThinkingLevel.Known.LOW))
            .build();

    System.out.println("Config JSON:\n" + config2.toJson());
    System.out.println("ThinkingLevel: LOW (some reasoning)\n");

    // Example 4.3: ThinkingLevel.MEDIUM
    System.out.println("Example 4.3: ThinkingLevel.MEDIUM");
    GenerationConfig config3 =
        GenerationConfig.builder()
            .thinkingLevel(new ThinkingLevel(ThinkingLevel.Known.MEDIUM))
            .build();

    System.out.println("Config JSON:\n" + config3.toJson());
    System.out.println("ThinkingLevel: MEDIUM (balanced thinking)\n");

    // Example 4.4: ThinkingLevel.HIGH
    System.out.println("Example 4.4: ThinkingLevel.HIGH");
    GenerationConfig config4 =
        GenerationConfig.builder()
            .thinkingLevel(new ThinkingLevel(ThinkingLevel.Known.HIGH))
            .build();

    System.out.println("Config JSON:\n" + config4.toJson());
    System.out.println("ThinkingLevel: HIGH (deep reasoning)\n");

    // Example 4.5: ThinkingSummaries.AUTO
    System.out.println("Example 4.5: ThinkingSummaries.AUTO");
    GenerationConfig config5 =
        GenerationConfig.builder()
            .thinkingSummaries(new ThinkingSummaries(ThinkingSummaries.Known.AUTO))
            .build();

    System.out.println("Config JSON:\n" + config5.toJson());
    System.out.println("ThinkingSummaries: AUTO (automatic summaries)\n");

    // Example 4.6: ThinkingSummaries.NONE
    System.out.println("Example 4.6: ThinkingSummaries.NONE");
    GenerationConfig config6 =
        GenerationConfig.builder()
            .thinkingSummaries(new ThinkingSummaries(ThinkingSummaries.Known.NONE))
            .build();

    System.out.println("Config JSON:\n" + config6.toJson());
    System.out.println("ThinkingSummaries: NONE (no summaries)\n");

    System.out.println("=== PART 4 Complete ===\n");
  }

  /**
   * PART 5: Speech Configuration
   *
   * <p>Demonstrates: speechConfig (voice, language, speaker)
   */
  private static void demonstrateSpeechConfig() {
    System.out.println("=== PART 5: Speech Configuration ===\n");

    // Example 5.1: Speech config with all fields
    System.out.println("Example 5.1: Complete Speech Configuration");
    SpeechConfig speechConfig =
        SpeechConfig.builder()
            .voice("en-US-Studio-O")
            .language("en-US")
            .speaker("speaker1")
            .build();

    GenerationConfig config =
        GenerationConfig.builder().speechConfig(speechConfig).build();

    System.out.println("SpeechConfig JSON:\n" + speechConfig.toJson());
    System.out.println("Full Config JSON:\n" + config.toJson());
    System.out.println(
        "Speech settings: voice=en-US-Studio-O, language=en-US, speaker=speaker1\n");

    // Example 5.2: Speech config with only voice
    System.out.println("Example 5.2: Speech Config with Voice Only");
    SpeechConfig speechConfig2 =
        SpeechConfig.builder().voice("en-GB-Studio-B").build();

    GenerationConfig config2 =
        GenerationConfig.builder().speechConfig(speechConfig2).build();

    System.out.println("Config JSON:\n" + config2.toJson());
    System.out.println("Speech settings: voice=en-GB-Studio-B\n");

    System.out.println("=== PART 5 Complete ===\n");
  }

  /**
   * PART 6: Image Configuration
   *
   * <p>Demonstrates: imageConfig (aspectRatio, imageSize)
   */
  private static void demonstrateImageConfig() {
    System.out.println("=== PART 6: Image Configuration ===\n");

    // Test all supported aspect ratios
    String[] aspectRatios = {"1:1", "2:3", "3:2", "3:4", "4:3", "9:16", "16:9", "21:9"};
    for (String aspectRatio : aspectRatios) {
      System.out.println("Example 6.x: AspectRatio = " + aspectRatio);
      ImageConfig imageConfig =
          ImageConfig.builder().aspectRatio(aspectRatio).build();

      GenerationConfig config =
          GenerationConfig.builder().imageConfig(imageConfig).build();

      System.out.println("Config JSON:\n" + config.toJson());
    }
    System.out.println();

    // Test all supported image sizes
    String[] imageSizes = {"1K", "2K", "4K"};
    for (String imageSize : imageSizes) {
      System.out.println("Example 6.y: ImageSize = " + imageSize);
      ImageConfig imageConfig =
          ImageConfig.builder().imageSize(imageSize).build();

      GenerationConfig config =
          GenerationConfig.builder().imageConfig(imageConfig).build();

      System.out.println("Config JSON:\n" + config.toJson());
    }
    System.out.println();

    // Combined aspect ratio and size
    System.out.println("Example 6.z: Combined AspectRatio and ImageSize");
    ImageConfig imageConfig =
        ImageConfig.builder().aspectRatio("16:9").imageSize("4K").build();

    GenerationConfig config =
        GenerationConfig.builder().imageConfig(imageConfig).build();

    System.out.println("Config JSON:\n" + config.toJson());
    System.out.println("Image settings: 16:9 aspect ratio at 4K resolution\n");

    System.out.println("=== PART 6 Complete ===\n");
  }

  /**
   * PART 7: Combined Configuration
   *
   * <p>Demonstrates: Multiple fields used together in a realistic scenario
   */
  private static void demonstrateCombinedConfig() {
    System.out.println("=== PART 7: Combined Configuration ===\n");

    System.out.println("Example 7.1: All Basic + Advanced Controls");
    GenerationConfig config =
        GenerationConfig.builder()
            .temperature(0.7f)
            .topP(0.9f)
            .seed(12345)
            .maxOutputTokens(500)
            .stopSequences(Arrays.asList("\n\n", "---"))
            .toolChoice(ToolChoice.fromType(ToolChoiceType.Known.AUTO))
            .thinkingLevel(new ThinkingLevel(ThinkingLevel.Known.MEDIUM))
            .thinkingSummaries(new ThinkingSummaries(ThinkingSummaries.Known.AUTO))
            .speechConfig(
                SpeechConfig.builder()
                    .voice("en-US-Studio-O")
                    .language("en-US")
                    .build())
            .imageConfig(
                ImageConfig.builder().aspectRatio("16:9").imageSize("2K").build())
            .build();

    System.out.println("Combined Config JSON (All 10 Fields):");
    System.out.println(config.toJson());
    System.out.println();

    CreateInteractionConfig interactionConfig =
        CreateInteractionConfig.builder()
            .model(MODEL)
            .input("Explain the concept of neural networks in simple terms.")
            .generationConfig(config)
            .build();

    System.out.println("Full Request JSON:");
    System.out.println(interactionConfig.toJson());
    System.out.println();

    Interaction response = client.interactions.create(interactionConfig);
    System.out.println("Response Status: " + response.status());
    System.out.println("Response: " + extractText(response) + "\n");

    System.out.println("=== PART 7 Complete ===\n");
  }

  /**
   * Helper method to extract text from interaction response.
   *
   * @param interaction The interaction response
   * @return Extracted text or "(no text)"
   */
  private static String extractText(Interaction interaction) {
    if (interaction.outputs().isPresent() && !interaction.outputs().get().isEmpty()) {
      for (Content output : interaction.outputs().get()) {
        if (output instanceof TextContent) {
          return ((TextContent) output).text().orElse("(empty)");
        }
      }
    }
    return "(no text)";
  }
}
