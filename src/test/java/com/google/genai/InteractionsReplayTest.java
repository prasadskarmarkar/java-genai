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

package com.google.genai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.genai.types.CancelInteractionConfig;
import com.google.genai.types.CreateInteractionConfig;
import com.google.genai.types.DeleteInteractionConfig;
import com.google.genai.types.DeleteInteractionResponse;
import com.google.genai.types.GetInteractionConfig;
import com.google.genai.types.Interaction;
import com.google.genai.types.InteractionStatus;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Replay-based tests for Interactions API.
 *
 * <p>These tests use recorded HTTP request/response pairs to test the full API flow without making
 * real API calls.
 *
 * <p>To run these tests, set: export
 * GOOGLE_GENAI_REPLAYS_DIRECTORY="/path/to/genai/replays"
 */
@EnabledIfEnvironmentVariable(
    named = "GOOGLE_GENAI_REPLAYS_DIRECTORY",
    matches = ".*genai/replays.*")
@ExtendWith(EnvironmentVariablesMockingExtension.class)
public class InteractionsReplayTest {

  private static final String MODEL_ID = "gemini-2.5-flash";

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCreateInteraction(boolean vertexAI) {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(vertexAI, "tests/interactions/create/test_create." + suffix + ".json");

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("What is the capital of France? Answer in one word.")
            .build();

    // Act
    Interaction interaction = client.interactions.create(config);

    // Assert
    assertNotNull(interaction);
    assertTrue(interaction.id().isPresent());
    assertTrue(interaction.status().isPresent());
    assertTrue(interaction.model().isPresent());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testGetInteraction(boolean vertexAI) {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(vertexAI, "tests/interactions/get/test_get." + suffix + ".json");

    // Use a known interaction ID from the replay
    String interactionId = vertexAI
        ? "projects/test-project/locations/us-central1/interactions/test-interaction-id"
        : "v1_test-interaction-id";

    GetInteractionConfig config = GetInteractionConfig.builder().build();

    // Act
    Interaction interaction = client.interactions.get(interactionId, config);

    // Assert
    assertNotNull(interaction);
    assertTrue(interaction.id().isPresent());
    assertEquals(interactionId, interaction.id().get());
    assertTrue(interaction.status().isPresent());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCreateAndGet(boolean vertexAI) {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI, "tests/interactions/end_to_end/test_create_and_get." + suffix + ".json");

    CreateInteractionConfig createConfig =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("What is 2+2?")
            .build();

    // Act - Create
    Interaction created = client.interactions.create(createConfig);

    // Act - Get
    GetInteractionConfig getConfig = GetInteractionConfig.builder().build();
    Interaction retrieved = client.interactions.get(created.id().get(), getConfig);

    // Assert
    assertNotNull(created);
    assertNotNull(retrieved);
    assertEquals(created.id(), retrieved.id());
    assertTrue(created.status().isPresent());
    assertTrue(retrieved.status().isPresent());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCancelInteraction(boolean vertexAI) {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI, "tests/interactions/cancel/test_cancel." + suffix + ".json");

    String interactionId = vertexAI
        ? "projects/test-project/locations/us-central1/interactions/test-interaction-id"
        : "v1_test-interaction-id";

    CancelInteractionConfig config = CancelInteractionConfig.builder().build();

    // Act
    Interaction interaction = client.interactions.cancel(interactionId, config);

    // Assert
    assertNotNull(interaction);
    assertTrue(interaction.id().isPresent());
    assertTrue(interaction.status().isPresent());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testDeleteInteraction(boolean vertexAI) {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI, "tests/interactions/delete/test_delete." + suffix + ".json");

    String interactionId = vertexAI
        ? "projects/test-project/locations/us-central1/interactions/test-interaction-id"
        : "v1_test-interaction-id";

    DeleteInteractionConfig config = DeleteInteractionConfig.builder().build();

    // Act
    DeleteInteractionResponse response = client.interactions.delete(interactionId, config);

    // Assert
    assertNotNull(response);
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testInteractionWithPreviousId(boolean vertexAI) {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI, "tests/interactions/create/test_with_previous_id." + suffix + ".json");

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Follow-up question")
            .previousInteractionId("previous-interaction-123")
            .build();

    // Act
    Interaction interaction = client.interactions.create(config);

    // Assert
    assertNotNull(interaction);
    assertTrue(interaction.id().isPresent());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false})  // Only test with MLDev, Vertex AI uses models
  public void testInteractionWithAgent(boolean vertexAI) {
    // Arrange
    String suffix = "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI, "tests/interactions/create/test_with_agent." + suffix + ".json");

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .agent("deep-research-pro-preview-12-2025")
            .input("Research quantum computing")
            .build();

    // Act
    Interaction interaction = client.interactions.create(config);

    // Assert
    assertNotNull(interaction);
    assertTrue(interaction.id().isPresent());
    assertTrue(interaction.agent().isPresent());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testInteractionStatusCompleted(boolean vertexAI) {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI, "tests/interactions/get/test_status_completed." + suffix + ".json");

    String interactionId = vertexAI
        ? "projects/test-project/locations/us-central1/interactions/completed-interaction"
        : "v1_completed-interaction";

    GetInteractionConfig config = GetInteractionConfig.builder().build();

    // Act
    Interaction interaction = client.interactions.get(interactionId, config);

    // Assert
    assertNotNull(interaction);
    assertTrue(interaction.status().isPresent());
    assertEquals(InteractionStatus.COMPLETED, interaction.status().get());
  }
}
