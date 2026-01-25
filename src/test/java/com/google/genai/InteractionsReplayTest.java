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

import com.google.genai.types.interactions.CancelInteractionConfig;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.DeleteInteractionConfig;
import com.google.genai.types.interactions.DeleteInteractionResponse;
import com.google.genai.types.interactions.GetInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.InteractionStatus;
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
    assertNotNull(interaction.id());
    assertNotNull(interaction.status());
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
    assertNotNull(interaction.id());
    assertEquals(interactionId, interaction.id());
    assertNotNull(interaction.status());
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
    Interaction retrieved = client.interactions.get(created.id(), getConfig);

    // Assert
    assertNotNull(created);
    assertNotNull(retrieved);
    assertEquals(created.id(), retrieved.id());
    assertNotNull(created.status());
    assertNotNull(retrieved.status());
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
    assertNotNull(interaction.id());
    assertNotNull(interaction.status());
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
    assertNotNull(interaction.id());
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
    assertNotNull(interaction.id());
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
    assertNotNull(interaction.status());
    assertEquals(InteractionStatus.COMPLETED, interaction.status());
  }

  // ==================== End-to-End Flows ====================

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testFullConversationFlow(boolean vertexAI) {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI, "tests/interactions/e2e/full_conversation_flow." + suffix + ".json");

    // Act - Create initial interaction
    CreateInteractionConfig createConfig =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("What is the capital of France?")
            .build();
    Interaction initial = client.interactions.create(createConfig);

    // Act - Get the interaction
    GetInteractionConfig getConfig = GetInteractionConfig.builder().build();
    Interaction retrieved = client.interactions.get(initial.id(), getConfig);

    // Act - Create follow-up interaction
    CreateInteractionConfig followUpConfig =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("What's its population?")
            .previousInteractionId(initial.id())
            .build();
    Interaction followUp = client.interactions.create(followUpConfig);

    // Act - Get follow-up
    Interaction retrievedFollowUp = client.interactions.get(followUp.id(), getConfig);

    // Act - Delete the interactions
    DeleteInteractionConfig deleteConfig = DeleteInteractionConfig.builder().build();
    client.interactions.delete(initial.id(), deleteConfig);

    // Assert
    assertNotNull(initial);
    assertNotNull(retrieved);
    assertEquals(initial.id(), retrieved.id());
    assertNotNull(followUp);
    assertTrue(followUp.previousInteractionId().isPresent());
    assertEquals(initial.id(), followUp.previousInteractionId().get());
    assertNotNull(retrievedFollowUp);
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testBackgroundInteractionFlow(boolean vertexAI) {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI, "tests/interactions/e2e/background_flow." + suffix + ".json");

    // Act - Create background interaction
    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Long running task")
            .background(true)
            .build();
    Interaction background = client.interactions.create(config);

    // Act - Poll status
    GetInteractionConfig getConfig = GetInteractionConfig.builder().build();
    Interaction polled = client.interactions.get(background.id(), getConfig);

    // Act - Cancel the background task
    CancelInteractionConfig cancelConfig = CancelInteractionConfig.builder().build();
    Interaction cancelled = client.interactions.cancel(background.id(), cancelConfig);

    // Assert
    assertNotNull(background);
    assertNotNull(background.id());
    assertNotNull(polled);
    assertNotNull(cancelled);
    assertNotNull(cancelled.status());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testMultiTurnWithTools(boolean vertexAI) {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI, "tests/interactions/e2e/multi_turn_with_tools." + suffix + ".json");

    com.google.genai.types.interactions.tools.GoogleSearch googleSearch =
        com.google.genai.types.interactions.tools.GoogleSearch.builder().build();

    // Act - First turn with tools
    CreateInteractionConfig firstConfig =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Search for recent AI news")
            .tools(com.google.common.collect.ImmutableList.of(googleSearch))
            .build();
    Interaction first = client.interactions.create(firstConfig);

    // Act - Second turn following up
    CreateInteractionConfig secondConfig =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Summarize the top 3 articles")
            .previousInteractionId(first.id())
            .tools(com.google.common.collect.ImmutableList.of(googleSearch))
            .build();
    Interaction second = client.interactions.create(secondConfig);

    // Assert
    assertNotNull(first);
    assertNotNull(second);
    assertTrue(second.previousInteractionId().isPresent());
    assertEquals(first.id(), second.previousInteractionId().get());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false}) // Agent is MLDev only
  public void testAgentResearchFlow(boolean vertexAI) {
    // Arrange
    String suffix = "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI, "tests/interactions/e2e/agent_research_flow." + suffix + ".json");

    // Act - Create agent-based research interaction
    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .agent("deep-research-pro-preview-12-2025")
            .input("Research the latest developments in quantum computing")
            .background(true)
            .build();
    Interaction research = client.interactions.create(config);

    // Act - Poll for completion
    GetInteractionConfig getConfig = GetInteractionConfig.builder().build();
    Interaction polled = client.interactions.get(research.id(), getConfig);

    // Assert
    assertNotNull(research);
    assertTrue(research.agent().isPresent());
    assertNotNull(polled);
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testToolChaining(boolean vertexAI) {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI, "tests/interactions/e2e/tool_chaining." + suffix + ".json");

    com.google.genai.types.interactions.tools.GoogleSearch googleSearch =
        com.google.genai.types.interactions.tools.GoogleSearch.builder().build();
    com.google.genai.types.interactions.tools.CodeExecution codeExecution =
        com.google.genai.types.interactions.tools.CodeExecution.builder().build();

    // Act - Create interaction with multiple tools
    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Search for weather data and calculate the average temperature")
            .tools(com.google.common.collect.ImmutableList.of(googleSearch, codeExecution))
            .build();
    Interaction result = client.interactions.create(config);

    // Assert
    assertNotNull(result);
    assertNotNull(result.id());
    assertTrue(result.outputs().isPresent());
  }

  // ==================== Platform Comparison Tests ====================

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testSameModelAcrossPlatforms(boolean vertexAI) {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI, "tests/interactions/e2e/same_model_platforms." + suffix + ".json");

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("What is 2+2?")
            .build();

    // Act
    Interaction result = client.interactions.create(config);

    // Assert
    assertNotNull(result);
    assertNotNull(result.id());
    assertTrue(result.model().isPresent());
    assertEquals(MODEL_ID, result.model().get());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testResponseFormat(boolean vertexAI) {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI, "tests/interactions/e2e/response_format." + suffix + ".json");

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Generate a person's data")
            .responseMimeType("application/json")
            .build();

    // Act
    Interaction result = client.interactions.create(config);

    // Assert
    assertNotNull(result);
    assertNotNull(result.id());
    assertTrue(result.outputs().isPresent());
  }

  // ==================== Performance & Scale ====================

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testLargeInput(boolean vertexAI) {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI, "tests/interactions/e2e/large_input." + suffix + ".json");

    // Create a large input (simulating approaching token limits)
    StringBuilder largeInput = new StringBuilder();
    for (int i = 0; i < 100; i++) {
      largeInput.append("This is sentence number ").append(i).append(". ");
    }

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input(largeInput.toString())
            .build();

    // Act
    Interaction result = client.interactions.create(config);

    // Assert
    assertNotNull(result);
    assertNotNull(result.id());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testMultipleSequential(boolean vertexAI) {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI, "tests/interactions/e2e/multiple_sequential." + suffix + ".json");

    String previousId = null;

    // Act - Create 3 sequential interactions
    for (int i = 1; i <= 3; i++) {
      CreateInteractionConfig.Builder configBuilder =
          CreateInteractionConfig.builder()
              .model(MODEL_ID)
              .input("Question " + i);

      if (previousId != null) {
        configBuilder.previousInteractionId(previousId);
      }

      Interaction result = client.interactions.create(configBuilder.build());

      // Assert each interaction
      assertNotNull(result);
      assertNotNull(result.id());
      if (previousId != null) {
        assertTrue(result.previousInteractionId().isPresent());
        assertEquals(previousId, result.previousInteractionId().get());
      }

      previousId = result.id();
    }
  }
}
