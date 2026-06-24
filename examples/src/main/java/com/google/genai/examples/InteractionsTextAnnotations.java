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
 * <p>1. Set an API key environment variable:
 *
 * <p>export GOOGLE_API_KEY=YOUR_API_KEY
 *
 * <p>2. Compile and run:
 *
 * <p>mvn clean compile
 *
 * <p>mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsTextAnnotations"
 */
package com.google.genai.examples;

import com.google.genai.Client;
import com.google.genai.types.interactions.Annotation;
import com.google.genai.types.interactions.FileCitation;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.PlaceCitation;
import com.google.genai.types.interactions.UrlCitation;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.content.TextContent;
import java.util.List;

/**
 * Example: Text Annotations (Citations) with the Interactions API.
 *
 * <p>Demonstrates how to access citation annotations on TextContent responses. Each annotation is a
 * discriminated union: {@link UrlCitation}, {@link FileCitation}, or {@link PlaceCitation}.
 *
 * <p>Note: The Interactions API is in beta and subject to change.
 */
public final class InteractionsTextAnnotations {

  private static final String MODEL = "gemini-3-flash-preview";

  public static void main(String[] args) {
    Client client = new Client();

    System.out.println("=== Interactions API: Text Annotations Example ===\n");

    testAnnotations(client, "Factual question", "What is the capital of France?");

    testAnnotations(
        client,
        "Citation request",
        "Tell me about climate change and cite your sources.");

    testAnnotations(
        client,
        "Research question",
        "What are the latest advancements in quantum computing? Provide citations.");

    System.out.println("\n=== All test cases completed ===");
  }

  private static void testAnnotations(Client client, String label, String prompt) {
    System.out.println("\n--- " + label + " ---");
    try {
      Interaction response = client.interactions.create(MODEL, prompt);
      analyzeAnnotations(response);
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  private static void analyzeAnnotations(Interaction interaction) {
    List<Content> outputs = interaction.getModelOutputContents();
    if (outputs.isEmpty()) {
      System.out.println("  No model output.");
      return;
    }

    int totalAnnotations = 0;
    for (Content content : outputs) {
      if (!(content instanceof TextContent)) {
        continue;
      }
      TextContent text = (TextContent) content;
      System.out.println("  Text: " + text.text().orElse("(empty)"));

      if (!text.annotations().isPresent() || text.annotations().get().isEmpty()) {
        System.out.println("  (no annotations)");
        continue;
      }

      List<Annotation> annotations = text.annotations().get();
      totalAnnotations += annotations.size();
      System.out.println("  Annotations (" + annotations.size() + "):");

      for (int i = 0; i < annotations.size(); i++) {
        Annotation ann = annotations.get(i);
        System.out.println("    [" + (i + 1) + "] " + describeAnnotation(ann, text));
      }
    }

    System.out.println("  Total annotations: " + totalAnnotations);
  }

  private static String describeAnnotation(Annotation ann, TextContent textContent) {
    if (ann instanceof UrlCitation) {
      UrlCitation url = (UrlCitation) ann;
      StringBuilder sb = new StringBuilder("UrlCitation");
      url.url().ifPresent(u -> sb.append(" url=").append(u));
      url.title().ifPresent(t -> sb.append(" title=\"").append(t).append("\""));
      appendRange(sb, url.startIndex().orElse(null), url.endIndex().orElse(null), textContent);
      return sb.toString();
    } else if (ann instanceof FileCitation) {
      FileCitation file = (FileCitation) ann;
      StringBuilder sb = new StringBuilder("FileCitation");
      file.fileName().ifPresent(f -> sb.append(" file=").append(f));
      file.documentUri().ifPresent(u -> sb.append(" uri=").append(u));
      appendRange(sb, file.startIndex().orElse(null), file.endIndex().orElse(null), textContent);
      return sb.toString();
    } else if (ann instanceof PlaceCitation) {
      PlaceCitation place = (PlaceCitation) ann;
      StringBuilder sb = new StringBuilder("PlaceCitation");
      place.name().ifPresent(n -> sb.append(" name=").append(n));
      place.placeId().ifPresent(p -> sb.append(" placeId=").append(p));
      appendRange(sb, place.startIndex().orElse(null), place.endIndex().orElse(null), textContent);
      return sb.toString();
    }
    return ann.getClass().getSimpleName();
  }

  private static void appendRange(
      StringBuilder sb, Integer start, Integer end, TextContent textContent) {
    if (start == null || end == null) {
      return;
    }
    sb.append(" [").append(start).append(",").append(end).append(")");
    textContent.text().ifPresent(t -> {
      if (start >= 0 && end <= t.length() && start < end) {
        sb.append(" \"").append(t, start, end).append("\"");
      }
    });
  }

  private InteractionsTextAnnotations() {}
}
