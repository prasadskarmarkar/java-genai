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

package com.google.genai.examples.interactions_api_online_examples;

import com.google.genai.Client;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.TextContent;

/**
 * Basic Text Prompt with the Interactions API.
 *
 * <p>This example demonstrates the simplest way to send a text prompt and receive a response using
 * the Interactions API convenience overload.
 *
 * <p>Documentation: https://ai.google.dev/gemini-api/docs/interactions
 *
 * <p>To run this example:
 *
 * <ol>
 *   <li>Set the GOOGLE_API_KEY environment variable: {@code export GOOGLE_API_KEY=YOUR_API_KEY}
 *   <li>Compile the examples: {@code mvn clean compile}
 *   <li>Run: {@code mvn exec:java
 *       -Dexec.mainClass="com.google.genai.examples.interactions_api_online_examples.BasicTextPrompt"}
 * </ol>
 */
public final class BasicTextPrompt {

  public static void main(String[] args) throws Exception {
    // Initialize the client - API key is read from GOOGLE_API_KEY environment variable
    Client client = new Client();

    System.out.println("=== Basic Text Prompt Example ===\n");

    // Simple text prompt using the convenience overload
    Interaction response =
        client.interactions.create(
            "gemini-3-flash-preview", "Tell me a short joke about programming");

    // Print the response
    System.out.println("Status: " + response.status());
    System.out.println("Response: " + getTextOutput(response));


    System.out.println("\n=== Example completed ===");
  }

  /** Extracts the first text output from an interaction response. */
  private static String getTextOutput(Interaction interaction) {
    if (!interaction.outputs().isPresent() || interaction.outputs().get().isEmpty()) {
      return "(no output)";
    }

    for (Content output : interaction.outputs().get()) {
      if (output instanceof TextContent) {
        TextContent textContent = (TextContent) output;
        return textContent.text().orElse("(empty)");
      }
    }
    return "(no text output)";
  }

  private BasicTextPrompt() {}
}
