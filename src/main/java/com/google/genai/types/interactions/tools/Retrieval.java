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

package com.google.genai.types.interactions.tools;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.genai.JsonSerializable;
import com.google.genai.types.ExcludeFromGeneratedCoverageReport;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Retrieval tool for the Interactions API.
 *
 * <p>Supports retrieval from Vertex AI Search, RAG store, Exa AI search, and parallel AI search.
 * Valid values for {@code retrievalTypes}: {@code "vertex_ai_search"}, {@code "rag_store"},
 * {@code "exa_ai_search"}, {@code "parallel_ai_search"}.
 */
@AutoValue
@JsonDeserialize(builder = Retrieval.Builder.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("retrieval")
public abstract class Retrieval extends JsonSerializable implements Tool {

  @JsonProperty("retrieval_types")
  public abstract Optional<List<String>> retrievalTypes();

  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_Retrieval.Builder();
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    @JsonCreator
    private static Builder create() {
      return new AutoValue_Retrieval.Builder();
    }

    @JsonProperty("retrieval_types")
    public abstract Builder retrievalTypes(List<String> retrievalTypes);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder retrievalTypes(Optional<List<String>> retrievalTypes);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearRetrievalTypes() {
      return retrievalTypes(Optional.empty());
    }

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder retrievalTypes(String... retrievalTypes) {
      return retrievalTypes(Arrays.asList(retrievalTypes));
    }

    public abstract Retrieval build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static Retrieval fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, Retrieval.class);
  }
}
