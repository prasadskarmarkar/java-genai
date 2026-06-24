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

package com.google.genai.types.interactions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Tests for Annotation discriminated union subtypes. */
public class AnnotationTest {

  @Test
  public void testUrlCitationBuilder() {
    UrlCitation citation =
        UrlCitation.builder()
            .url("https://example.com/source1")
            .title("Example Source")
            .startIndex(0)
            .endIndex(25)
            .build();

    assertTrue(citation.url().isPresent());
    assertEquals("https://example.com/source1", citation.url().get());
    assertTrue(citation.title().isPresent());
    assertEquals("Example Source", citation.title().get());
    assertTrue(citation.startIndex().isPresent());
    assertEquals(0, citation.startIndex().get());
    assertTrue(citation.endIndex().isPresent());
    assertEquals(25, citation.endIndex().get());
  }

  @Test
  public void testUrlCitationOptionalFields() {
    UrlCitation citation = UrlCitation.builder().url("https://example.com").build();

    assertTrue(citation.url().isPresent());
    assertFalse(citation.title().isPresent());
    assertFalse(citation.startIndex().isPresent());
    assertFalse(citation.endIndex().isPresent());
  }

  @Test
  public void testFileCitationBuilder() {
    FileCitation citation =
        FileCitation.builder()
            .documentUri("gs://bucket/doc.pdf")
            .fileName("doc.pdf")
            .source("Excerpt from document")
            .pageNumber(3)
            .startIndex(10)
            .endIndex(50)
            .build();

    assertTrue(citation.documentUri().isPresent());
    assertEquals("gs://bucket/doc.pdf", citation.documentUri().get());
    assertTrue(citation.fileName().isPresent());
    assertEquals("doc.pdf", citation.fileName().get());
    assertTrue(citation.pageNumber().isPresent());
    assertEquals(3, citation.pageNumber().get());
    assertTrue(citation.startIndex().isPresent());
    assertEquals(10, citation.startIndex().get());
  }

  @Test
  public void testPlaceCitationBuilder() {
    PlaceCitation citation =
        PlaceCitation.builder()
            .placeId("ChIJN1t_tDeuEmsRUsoyG83frY4")
            .name("Google Sydney")
            .url("https://maps.google.com/place/google-sydney")
            .startIndex(0)
            .endIndex(15)
            .build();

    assertTrue(citation.placeId().isPresent());
    assertEquals("ChIJN1t_tDeuEmsRUsoyG83frY4", citation.placeId().get());
    assertTrue(citation.name().isPresent());
    assertEquals("Google Sydney", citation.name().get());
    assertTrue(citation.startIndex().isPresent());
    assertEquals(0, citation.startIndex().get());
  }

  @Test
  public void testUrlCitationJsonSerialization() {
    UrlCitation citation =
        UrlCitation.builder()
            .url("https://example.com/source1")
            .startIndex(0)
            .endIndex(25)
            .build();

    String json = citation.toJson();

    assertNotNull(json);
    assertTrue(json.contains("\"url\":\"https://example.com/source1\""));
    assertTrue(json.contains("\"start_index\":0"));
    assertTrue(json.contains("\"end_index\":25"));
    assertTrue(json.contains("\"type\":\"url_citation\""));
  }

  @Test
  public void testUrlCitationClearMethods() {
    UrlCitation.Builder builder =
        UrlCitation.builder().url("https://example.com").startIndex(0).endIndex(10);

    builder.clearStartIndex();
    builder.clearEndIndex();
    UrlCitation citation = builder.build();

    assertTrue(citation.url().isPresent());
    assertFalse(citation.startIndex().isPresent());
    assertFalse(citation.endIndex().isPresent());
  }

  @Test
  public void testUrlCitationToBuilder() {
    UrlCitation original =
        UrlCitation.builder().url("https://example.com").startIndex(10).endIndex(50).build();

    UrlCitation modified = original.toBuilder().url("https://new-source.com").build();

    assertEquals("https://example.com", original.url().get());
    assertEquals("https://new-source.com", modified.url().get());
    assertEquals(original.startIndex(), modified.startIndex());
    assertEquals(original.endIndex(), modified.endIndex());
  }

  @Test
  public void testAnnotationIsInterface() {
    UrlCitation urlCitation = UrlCitation.builder().url("https://example.com").build();
    FileCitation fileCitation = FileCitation.builder().documentUri("gs://bucket/doc.pdf").build();
    PlaceCitation placeCitation = PlaceCitation.builder().placeId("place123").build();

    assertTrue(urlCitation instanceof Annotation);
    assertTrue(fileCitation instanceof Annotation);
    assertTrue(placeCitation instanceof Annotation);
  }
}
