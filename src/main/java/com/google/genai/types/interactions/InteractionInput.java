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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.genai.JsonSerializable;
import java.util.Arrays;
import java.util.List;

/**
 * Union type for interaction input. Can be a string, list of InteractionContent objects, or list
 * of InteractionTurn objects.
 */
@JsonSerialize(using = InteractionInputSerializer.class)
public final class InteractionInput extends JsonSerializable {
  private final Object value;

  private InteractionInput(Object value) {
    this.value = value;
  }

  /**
   * Creates an InteractionInput from a string.
   *
   * @param text The input text
   * @return An InteractionInput instance wrapping the string
   */
  public static InteractionInput fromString(String text) {
    return new InteractionInput(text);
  }

  /**
   * Creates an InteractionInput from a single InteractionContent object.
   *
   * @param content The interaction content object
   * @return An InteractionInput instance wrapping the content
   */
  public static InteractionInput fromContent(InteractionContent content) {
    return new InteractionInput(content);
  }

  /**
   * Creates an InteractionInput from a list of InteractionContent objects.
   *
   * @param contents The list of interaction content objects
   * @return An InteractionInput instance wrapping the contents
   */
  public static InteractionInput fromContents(List<InteractionContent> contents) {
    return new InteractionInput(contents);
  }

  /**
   * Creates an InteractionInput from InteractionContent objects (varargs).
   *
   * @param contents The interaction content objects
   * @return An InteractionInput instance wrapping the contents
   */
  public static InteractionInput fromContents(InteractionContent... contents) {
    return new InteractionInput(Arrays.asList(contents));
  }

  /**
   * Creates an InteractionInput from a list of InteractionTurn objects.
   *
   * @param turns The list of conversation turns
   * @return An InteractionInput instance wrapping the turns
   */
  public static InteractionInput fromTurns(List<InteractionTurn> turns) {
    return new InteractionInput(turns);
  }

  /**
   * Creates an InteractionInput from InteractionTurn objects (varargs).
   *
   * @param turns The conversation turns
   * @return An InteractionInput instance wrapping the turns
   */
  public static InteractionInput fromTurns(InteractionTurn... turns) {
    return new InteractionInput(Arrays.asList(turns));
  }

  /**
   * Gets the underlying value.
   *
   * @return The wrapped value (String, List&lt;InteractionContent&gt;, or
   *     List&lt;InteractionTurn&gt;)
   */
  public Object getValue() {
    return value;
  }
}
