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
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.DocumentContent;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.TextContent;

/**
 * Example demonstrating document analysis using DocumentContent with the Interactions API.
 *
 * <p>This example shows:
 * <ul>
 *   <li>Using DocumentContent.fromUri() to analyze a document from a URL
 *   <li>Using DocumentContent.fromData() to analyze base64-encoded documents
 *   <li>Combining DocumentContent with TextContent for extraction and analysis
 *   <li>Working with different document MIME types (PDF, plain text)
 * </ul>
 *
 * <p>To run this example:
 * <ol>
 *   <li>Set the GOOGLE_API_KEY environment variable: {@code export GOOGLE_API_KEY=YOUR_API_KEY}
 *   <li>Compile the examples: {@code mvn clean compile}
 *   <li>Run: {@code mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsDocumentContent"}
 * </ol>
 *
 * <p><b>Note:</b> The Interactions API is currently in beta.
 */
public final class InteractionsDocumentContent {

  public static void main(String[] args) {
    Client client = new Client();

    System.out.println("=== Example: Document Analysis with Interactions API ===\n");

    // ========================================
    // PART 1: Document from URI
    // ========================================
    System.out.println("PART 1: Document from URI\n");

    String documentUri =
        "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf";
    String promptText = "Summarize this document and extract the key information.";

    DocumentContent documentContent = DocumentContent.fromUri(documentUri, "application/pdf");
    TextContent textPrompt = TextContent.builder().text(promptText).build();

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .inputFromContents(documentContent, textPrompt)
            .build();

    System.out.println("REQUEST:");
    System.out.println("  Model: gemini-2.5-flash");
    System.out.println("  Document URI: " + documentUri);
    System.out.println("  Prompt: " + promptText);
    System.out.println("\nREQUEST JSON:");
    System.out.println(config.toJson());

    Interaction response = client.interactions.create(config);

    System.out.println("\nRESPONSE JSON:");
    System.out.println(response.toJson());
    System.out.println("\nRESPONSE:");
    System.out.println("  Status: " + response.status());
    System.out.println("  Interaction ID: " + response.id());
    System.out.print("  Output: ");
    printOutputs(response);

    // ========================================
    // PART 2: Document from Inline Data (base64)
    // ========================================
    System.out.println("\n\nPART 2: Document from Inline Data (Base64)\n");

    // Minimal valid PDF document (1 page with "Hello World")
    // To encode your own file: Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get("document.pdf")))
    String base64Data =
        "JVBERi0xLjQKJeLjz9MKMSAwIG9iago8PAovVHlwZSAvQ2F0YWxvZwovUGFnZXMgMiAwIFIKPj4KZW5kb2JqCjIgMCBvYmoKPDwKL1R5cGUgL1BhZ2VzCi9LaWRzIFszIDAgUl0KL0NvdW50IDEKL01lZGlhQm94IFswIDAgNjEyIDc5Ml0KPj4KZW5kb2JqCjMgMCBvYmoKPDwKL1R5cGUgL1BhZ2UKL1BhcmVudCAyIDAgUgovUmVzb3VyY2VzIDw8Ci9Gb250IDw8Ci9GMSA0IDAgUgo+Pgo+PgovQ29udGVudHMgNSAwIFIKPj4KZW5kb2JqCjQgMCBvYmoKPDwKL1R5cGUgL0ZvbnQKL1N1YnR5cGUgL1R5cGUxCi9CYXNlRm9udCAvSGVsdmV0aWNhCj4+CmVuZG9iago1IDAgb2JqCjw8Ci9MZW5ndGggNDQKPj4Kc3RyZWFtCkJUCi9GMSA0OCBUZgoxMCA3MDAgVGQKKEhlbGxvIFdvcmxkKSBUagpFVAplbmRzdHJlYW0KZW5kb2JqCnhyZWYKMCA2CjAwMDAwMDAwMDAgNjU1MzUgZgogCjAwMDAwMDAwMTUgMDAwMDAgbiAKMDAwMDAwMDA2NCAwMDAwMCBuIAowMDAwMDAwMTUxIDAwMDAwIG4gCjAwMDAwMDAyNjIgMDAwMDAgbiAKMDAwMDAwMDM0OSAwMDAwMCBuIAp0cmFpbGVyCjw8Ci9TaXplIDYKL1Jvb3QgMSAwIFIKPj4Kc3RhcnR4cmVmCjQ0MgolJUVPRgo=";
    String promptText2 = "What is the content of this document?";

    DocumentContent documentFromData = DocumentContent.fromData(base64Data, "application/pdf");
    TextContent textPrompt2 = TextContent.builder().text(promptText2).build();

    CreateInteractionConfig config2 =
        CreateInteractionConfig.builder()
            .model("gemini-2.5-flash")
            .inputFromContents(documentFromData, textPrompt2)
            .build();

    System.out.println("REQUEST:");
    System.out.println("  Model: gemini-2.5-flash");
    System.out.println("  Document: Inline base64 data (PDF format)");
    System.out.println("  Prompt: " + promptText2);
    System.out.println("\nREQUEST JSON:");
    System.out.println(config2.toJson());

    Interaction response2 = client.interactions.create(config2);

    System.out.println("\nRESPONSE JSON:");
    System.out.println(response2.toJson());
    System.out.println("\nRESPONSE:");
    System.out.println("  Status: " + response2.status());
    System.out.println("  Interaction ID: " + response2.id());
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
      for (Content output : interaction.outputs().get()) {
        if (output instanceof TextContent) {
          System.out.println(((TextContent) output).text().orElse("(empty)"));
          break;
        }
      }
    }
  }

  private InteractionsDocumentContent() {}
}
