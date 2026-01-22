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
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsCodeExecutionMultiContent"
 */
package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.GetInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.CodeExecutionCallContent;
import com.google.genai.types.interactions.content.CodeExecutionResultContent;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.ImageContent;
import com.google.genai.types.interactions.content.TextContent;
import com.google.genai.types.interactions.tools.CodeExecutionTool;

/**
 * Example: CodeExecutionTool with Multiple Content Types
 *
 * <p>Demonstrates CodeExecutionTool with multiple content types as input:
 *
 * <ul>
 *   <li>3 TextContent items
 *   <li>1 ImageContent item
 * </ul>
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsCodeExecutionMultiContent {

  public static void main(String[] args) {
    // Instantiate the client. The client gets the API key from the environment variable
    // `GOOGLE_API_KEY`.
    //
    Client client = new Client();

    System.out.println("=== Interactions API: CodeExecutionTool with Multiple Contents ===\n");

    // ===== Configure CodeExecutionTool =====
    CodeExecutionTool codeExecutionTool = CodeExecutionTool.builder().build();

    // ===== Create Multiple Content Items =====
    System.out.println("--- Creating Multiple Content Items ---\n");

    TextContent textContent1 = TextContent.builder()
        .text("I'm showing you an image of a cake.")
        .build();
    System.out.println("TextContent 1: Context about the image");

    TextContent textContent2 = TextContent.builder()
        .text("Analyze the image to estimate the cake dimensions and calculate its approximate volume.")
        .build();
    System.out.println("TextContent 2: Analysis request");

    TextContent textContent3 = TextContent.builder()
        .text("Write Python code to perform the calculation and show the result.")
        .build();
    System.out.println("TextContent 3: Code execution instruction");

    String imageUri = "https://storage.googleapis.com/generativeai-downloads/images/cake.jpg";
    ImageContent imageContent = ImageContent.fromUri(imageUri, "image/jpeg");
    System.out.println("ImageContent: cake.jpg\n");

    // ===== Create Interaction =====
    System.out.println("--- Creating Interaction ---\n");

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .inputFromContents(
                textContent1,
                textContent2,
                imageContent,
                textContent3)
            .tools(codeExecutionTool)
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
      } else if (output instanceof CodeExecutionCallContent) {
        CodeExecutionCallContent codeCall = (CodeExecutionCallContent) output;
        System.out.println("\nCodeExecutionCall: id=" + codeCall.id());
        if (codeCall.arguments().isPresent()) {
          System.out.println("  language: " + codeCall.arguments().get().language().orElse("N/A"));
          String code = codeCall.arguments().get().code().orElse("(empty)");
          if (code.length() > 200) {
            code = code.substring(0, 200) + "... [truncated]";
          }
          System.out.println("  code: " + code);
        }
      } else if (output instanceof CodeExecutionResultContent) {
        CodeExecutionResultContent codeResult = (CodeExecutionResultContent) output;
        System.out.println("\nCodeExecutionResult: callId=" + codeResult.callId().orElse("N/A"));
        System.out.println("  isError: " + codeResult.isError().orElse(false));
        String result = codeResult.result().orElse("(empty)");
        if (result.length() > 200) {
          result = result.substring(0, 200) + "... [truncated]";
        }
        System.out.println("  result: " + result);
      }
    }
  }

  private InteractionsCodeExecutionMultiContent() {}
}
