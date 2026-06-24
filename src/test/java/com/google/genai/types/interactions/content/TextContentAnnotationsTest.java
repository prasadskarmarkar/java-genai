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

package com.google.genai.types.interactions.content;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.genai.types.interactions.Annotation;
import com.google.genai.types.interactions.UrlCitation;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Tests for TextContent with annotations field. */
public class TextContentAnnotationsTest {

  @Test
  public void testTextContentWithoutAnnotations() {
    TextContent textContent = TextContent.of("This is a simple text without citations.");

    assertTrue(textContent.text().isPresent());
    assertEquals("This is a simple text without citations.", textContent.text().get());
    assertFalse(textContent.annotations().isPresent());
  }

  @Test
  public void testTextContentWithAnnotationsList() {
    UrlCitation annotation1 =
        UrlCitation.builder()
            .url("https://example.com/source1")
            .startIndex(0)
            .endIndex(25)
            .build();

    UrlCitation annotation2 =
        UrlCitation.builder()
            .url("https://en.wikipedia.org/wiki/AI")
            .startIndex(26)
            .endIndex(50)
            .build();

    List<Annotation> annotations = Arrays.asList(annotation1, annotation2);

    TextContent textContent =
        TextContent.builder()
            .text("This text has citations from multiple sources for attribution.")
            .annotations(annotations)
            .build();

    assertTrue(textContent.text().isPresent());
    assertTrue(textContent.annotations().isPresent());
    assertEquals(2, textContent.annotations().get().size());
    assertTrue(textContent.annotations().get().get(0) instanceof UrlCitation);
    assertTrue(textContent.annotations().get().get(1) instanceof UrlCitation);
    assertEquals(
        "https://example.com/source1",
        ((UrlCitation) textContent.annotations().get().get(0)).url().get());
  }

  @Test
  public void testTextContentWithAnnotationsVarargs() {
    UrlCitation annotation1 =
        UrlCitation.builder().url("https://source1.com").startIndex(0).endIndex(15).build();
    UrlCitation annotation2 =
        UrlCitation.builder().url("https://source2.com").startIndex(16).endIndex(30).build();

    TextContent textContent =
        TextContent.builder()
            .text("Annotated content using varargs syntax.")
            .annotations(annotation1, annotation2)
            .build();

    assertTrue(textContent.annotations().isPresent());
    assertEquals(2, textContent.annotations().get().size());
    assertEquals(
        "https://source1.com",
        ((UrlCitation) textContent.annotations().get().get(0)).url().get());
    assertEquals(
        "https://source2.com",
        ((UrlCitation) textContent.annotations().get().get(1)).url().get());
  }

  @Test
  public void testTextContentClearAnnotations() {
    UrlCitation annotation = UrlCitation.builder().url("https://example.com").build();
    TextContent.Builder builder =
        TextContent.builder().text("Text with annotations").annotations(annotation);

    builder.clearAnnotations();
    TextContent textContent = builder.build();

    assertFalse(textContent.annotations().isPresent());
  }

  @Test
  public void testTextContentJsonSerializationWithoutAnnotations() {
    TextContent textContent = TextContent.of("This is a simple text without citations.");

    String json = textContent.toJson();

    assertNotNull(json);
    assertTrue(json.contains("\"type\":\"text\""));
    assertTrue(json.contains("\"text\":\"This is a simple text without citations.\""));
    assertFalse(json.contains("\"annotations\""));
  }

  @Test
  public void testTextContentJsonSerializationWithAnnotations() {
    UrlCitation annotation1 =
        UrlCitation.builder()
            .url("https://example.com/source1")
            .startIndex(0)
            .endIndex(25)
            .build();
    UrlCitation annotation2 =
        UrlCitation.builder()
            .url("https://en.wikipedia.org/wiki/AI")
            .startIndex(26)
            .endIndex(50)
            .build();

    TextContent textContent =
        TextContent.builder()
            .text("This text has citations from multiple sources for attribution.")
            .annotations(Arrays.asList(annotation1, annotation2))
            .build();

    String json = textContent.toJson();

    assertNotNull(json);
    assertTrue(json.contains("\"type\":\"text\""));
    assertTrue(json.contains("\"text\":\"This text has citations"));
    assertTrue(json.contains("\"annotations\""));
    assertTrue(json.contains("\"start_index\":0"));
    assertTrue(json.contains("\"end_index\":25"));
    assertTrue(json.contains("\"url\":\"https://example.com/source1\""));
    assertTrue(json.contains("\"type\":\"url_citation\""));
  }

  @Test
  public void testTextContentJsonDeserializationWithAnnotations() {
    String json =
        "{\"type\":\"text\",\"text\":\"Sample text with citations.\","
            + "\"annotations\":["
            + "{\"type\":\"url_citation\",\"url\":\"https://source1.com\","
            + "\"start_index\":0,\"end_index\":15},"
            + "{\"type\":\"url_citation\",\"url\":\"https://source2.com\","
            + "\"start_index\":16,\"end_index\":30}"
            + "]}";

    TextContent textContent = TextContent.fromJson(json);

    assertTrue(textContent.text().isPresent());
    assertEquals("Sample text with citations.", textContent.text().get());
    assertTrue(textContent.annotations().isPresent());
    assertEquals(2, textContent.annotations().get().size());

    UrlCitation first = (UrlCitation) textContent.annotations().get().get(0);
    assertEquals(0, first.startIndex().get());
    assertEquals(15, first.endIndex().get());
    assertEquals("https://source1.com", first.url().get());

    UrlCitation second = (UrlCitation) textContent.annotations().get().get(1);
    assertEquals(16, second.startIndex().get());
    assertEquals(30, second.endIndex().get());
    assertEquals("https://source2.com", second.url().get());
  }

  @Test
  public void testTextContentRoundTripSerializationWithAnnotations() {
    UrlCitation annotation1 =
        UrlCitation.builder().url("https://example.org").startIndex(0).endIndex(20).build();
    UrlCitation annotation2 =
        UrlCitation.builder()
            .url("https://researchpaper.org")
            .startIndex(21)
            .endIndex(40)
            .build();

    TextContent original =
        TextContent.builder()
            .text("Text with multiple citation sources.")
            .annotations(annotation1, annotation2)
            .build();

    String json = original.toJson();
    TextContent deserialized = TextContent.fromJson(json);

    assertEquals(original.text(), deserialized.text());
    assertTrue(deserialized.annotations().isPresent());
    assertEquals(2, deserialized.annotations().get().size());

    List<Annotation> deserializedAnnotations = deserialized.annotations().get();
    assertEquals(
        "https://example.org", ((UrlCitation) deserializedAnnotations.get(0)).url().get());
    assertEquals(
        "https://researchpaper.org", ((UrlCitation) deserializedAnnotations.get(1)).url().get());
  }

  @Test
  public void testTextContentToBuilder() {
    UrlCitation originalAnnotation =
        UrlCitation.builder().url("https://source.com").startIndex(0).endIndex(10).build();
    TextContent original =
        TextContent.builder().text("Original text").annotations(originalAnnotation).build();

    UrlCitation newAnnotation =
        UrlCitation.builder().url("https://new-source.com").startIndex(0).endIndex(15).build();
    TextContent modified =
        original.toBuilder().text("Modified text").annotations(newAnnotation).build();

    assertEquals("Original text", original.text().get());
    assertEquals(
        "https://source.com",
        ((UrlCitation) original.annotations().get().get(0)).url().get());

    assertEquals("Modified text", modified.text().get());
    assertEquals(
        "https://new-source.com",
        ((UrlCitation) modified.annotations().get().get(0)).url().get());
  }

  @Test
  public void testTextContentWithEmptyAnnotationsList() {
    TextContent textContent =
        TextContent.builder()
            .text("Text with empty annotations list")
            .annotations(Arrays.asList())
            .build();

    assertTrue(textContent.annotations().isPresent());
    assertTrue(textContent.annotations().get().isEmpty());
  }

  @Test
  public void testAnnotationSubtypesAreAnnotationInterface() {
    UrlCitation urlCitation = UrlCitation.builder().url("https://example.com").build();

    assertTrue(urlCitation instanceof Annotation);
  }
}
