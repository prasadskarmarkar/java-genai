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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Base interface for citation annotations on model-generated content.
 *
 * <p>Annotations provide attribution for specific segments of text by mapping byte indices to their
 * source references. Each annotation has a discriminating {@code type} field that determines which
 * subtype it is.
 *
 * <p>Supported subtypes:
 *
 * <ul>
 *   <li>{@link UrlCitation} - A URL-based citation
 *   <li>{@link FileCitation} - A file-based citation
 *   <li>{@link PlaceCitation} - A place-based citation (Google Maps)
 * </ul>
 *
 * <p>The Interactions API is available in both Vertex AI and Gemini API.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = UrlCitation.class, name = "url_citation"),
  @JsonSubTypes.Type(value = FileCitation.class, name = "file_citation"),
  @JsonSubTypes.Type(value = PlaceCitation.class, name = "place_citation"),
})
public interface Annotation {}
