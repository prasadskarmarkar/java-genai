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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.genai.types.interactions.content.InteractionContent;
import java.io.IOException;
import java.util.List;

/**
 * Custom serializer for InteractionInput that ensures proper polymorphic type information is
 * included when serializing lists of InteractionContent or InteractionTurn objects.
 *
 * <p>This is necessary because the @JsonValue annotation on a field typed as Object loses type
 * information for contained elements. This serializer explicitly handles the different cases
 * (String, List of InteractionContent, List of InteractionTurn) and ensures that polymorphic
 * content types include their "type" property.
 */
public class InteractionInputSerializer extends JsonSerializer<InteractionInput> {

  @Override
  public void serialize(InteractionInput value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    Object innerValue = value.getValue();

    if (innerValue == null) {
      gen.writeNull();
    } else if (innerValue instanceof String) {
      // String input - write directly
      gen.writeString((String) innerValue);
    } else if (innerValue instanceof InteractionContent) {
      // Single InteractionContent - serialize with type info
      serializers.defaultSerializeValue(innerValue, gen);
    } else if (innerValue instanceof List) {
      // List input - serialize with proper type handling
      List<?> list = (List<?>) innerValue;
      gen.writeStartArray();
      for (Object item : list) {
        // Use the default serializer which respects @JsonTypeInfo on the item's class
        serializers.defaultSerializeValue(item, gen);
      }
      gen.writeEndArray();
    } else {
      // Fallback for unknown types
      serializers.defaultSerializeValue(innerValue, gen);
    }
  }
}
