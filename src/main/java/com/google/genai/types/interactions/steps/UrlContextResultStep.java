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

package com.google.genai.types.interactions.steps;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.genai.JsonSerializable;
import com.google.genai.types.ExcludeFromGeneratedCoverageReport;
import com.google.genai.types.interactions.UrlContextResult;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/** URL context result step. */
@AutoValue
@JsonDeserialize(builder = UrlContextResultStep.Builder.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("url_context_result")
public abstract class UrlContextResultStep extends JsonSerializable implements Step {

  @JsonProperty("call_id")
  public abstract Optional<String> callId();

  @JsonProperty("result")
  public abstract Optional<List<UrlContextResult>> result();

  @JsonProperty("is_error")
  public abstract Optional<Boolean> isError();

  @JsonProperty("signature")
  public abstract Optional<String> signature();

  @ExcludeFromGeneratedCoverageReport
  public static Builder builder() {
    return new AutoValue_UrlContextResultStep.Builder();
  }

  public abstract Builder toBuilder();

  @AutoValue.Builder
  public abstract static class Builder {
    @JsonCreator
    private static Builder create() {
      return new AutoValue_UrlContextResultStep.Builder();
    }

    @JsonProperty("call_id")
    public abstract Builder callId(String callId);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder callId(Optional<String> callId);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearCallId() {
      return callId(Optional.empty());
    }

    @JsonProperty("result")
    public abstract Builder result(List<UrlContextResult> result);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder result(Optional<List<UrlContextResult>> result);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearResult() {
      return result(Optional.empty());
    }

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder result(UrlContextResult... result) {
      return result(Arrays.asList(result));
    }

    @JsonProperty("is_error")
    public abstract Builder isError(Boolean isError);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder isError(Optional<Boolean> isError);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearIsError() {
      return isError(Optional.empty());
    }

    @JsonProperty("signature")
    public abstract Builder signature(String signature);

    @ExcludeFromGeneratedCoverageReport
    abstract Builder signature(Optional<String> signature);

    @ExcludeFromGeneratedCoverageReport
    @CanIgnoreReturnValue
    public Builder clearSignature() {
      return signature(Optional.empty());
    }

    public abstract UrlContextResultStep build();
  }

  @ExcludeFromGeneratedCoverageReport
  public static UrlContextResultStep fromJson(String jsonString) {
    return JsonSerializable.fromJsonString(jsonString, UrlContextResultStep.class);
  }
}
