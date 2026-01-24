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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The resolution of the media (images and videos).
 *
 * <p>Controls the quality/resolution of media content in interactions.
 */
public enum MediaResolution {
  /** Low resolution. */
  @JsonProperty("low")
  LOW,

  /** Medium resolution. */
  @JsonProperty("medium")
  MEDIUM,

  /** High resolution. */
  @JsonProperty("high")
  HIGH,

  /** Ultra high resolution. */
  @JsonProperty("ultra_high")
  ULTRA_HIGH
}
