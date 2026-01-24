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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.genai.types.Content;
import com.google.genai.types.FunctionDeclaration;
import com.google.genai.types.Modality;
import com.google.genai.types.Part;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import com.google.genai.types.interactions.CancelInteractionConfig;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.DeleteInteractionConfig;
import com.google.genai.types.interactions.DeleteInteractionResponse;
import com.google.genai.types.interactions.GenerationConfig;
import com.google.genai.types.interactions.GetInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.AudioContent;
import com.google.genai.types.interactions.content.DocumentContent;
import com.google.genai.types.interactions.content.ImageContent;
import com.google.genai.types.interactions.content.TextContent;
import com.google.genai.types.interactions.content.VideoContent;
import com.google.genai.types.interactions.tools.CodeExecution;
import com.google.genai.types.interactions.tools.Function;
import com.google.genai.types.interactions.tools.GoogleSearch;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Comprehensive async tests for Interactions API.
 *
 * <p>This test suite follows the same pattern as AsyncModelsTest.java, providing comprehensive
 * async coverage of all Interactions API features. All tests use CompletableFuture and .join()
 * for async operations, with CompletionException handling for errors.
 *
 * <p>To run these tests, set: export GOOGLE_GENAI_REPLAYS_DIRECTORY="/path/to/genai/replays"
 */
@EnabledIfEnvironmentVariable(
    named = "GOOGLE_GENAI_REPLAYS_DIRECTORY",
    matches = ".*genai/replays.*")
@ExtendWith(EnvironmentVariablesMockingExtension.class)
public class AsyncInteractionsComprehensiveTest {

  private static final String MODEL_ID = "gemini-2.5-flash";
  private static final String AGENT_ID = "deep-research-pro-preview-12-2025";

  // ==================== Async Core Operations ====================

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCreateAsync_withBasicModel(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/create_basic_model." + suffix + ".json");

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("What is the capital of France?")
            .build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
    assertNotNull(interaction.status());
    assertTrue(interaction.model().isPresent());
    assertEquals(MODEL_ID, interaction.model().get());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testGetAsync_withValidId(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI, "tests/interactions/comprehensive_async/get_valid_id." + suffix + ".json");

    String interactionId =
        vertexAI
            ? "projects/test-project/locations/us-central1/interactions/test-interaction-id"
            : "v1_test-interaction-id";

    GetInteractionConfig config = GetInteractionConfig.builder().build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.get(interactionId, config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertEquals(interactionId, interaction.id());
    assertNotNull(interaction.status());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCancelAsync_backgroundInteraction(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/cancel_background." + suffix + ".json");

    String interactionId =
        vertexAI
            ? "projects/test-project/locations/us-central1/interactions/background-id"
            : "v1_background-id";

    CancelInteractionConfig config = CancelInteractionConfig.builder().build();

    // Act
    CompletableFuture<Interaction> future =
        client.async.interactions.cancel(interactionId, config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
    assertNotNull(interaction.status());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testDeleteAsync_existingInteraction(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/delete_existing." + suffix + ".json");

    String interactionId =
        vertexAI
            ? "projects/test-project/locations/us-central1/interactions/delete-id"
            : "v1_delete-id";

    DeleteInteractionConfig config = DeleteInteractionConfig.builder().build();

    // Act
    CompletableFuture<DeleteInteractionResponse> future =
        client.async.interactions.delete(interactionId, config);
    DeleteInteractionResponse response = future.join();

    // Assert
    assertNotNull(response);
  }

  // ==================== Async Model-Based Interactions ====================

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCreateAsync_withGenerationConfig(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/create_generation_config."
                + suffix
                + ".json");

    GenerationConfig generationConfig =
        GenerationConfig.builder()
            .temperature(0.7f)
            .topP(0.9f)
            .maxOutputTokens(1024)
            .stopSequences(ImmutableList.of("END", "STOP"))
            .build();

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Write a creative story.")
            .generationConfig(generationConfig)
            .build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
    assertTrue(interaction.model().isPresent());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCreateAsync_withSystemInstruction(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/create_system_instruction."
                + suffix
                + ".json");

    Content systemInstruction =
        Content.fromParts(Part.fromText("You are a helpful assistant specialized in science."));

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Explain quantum mechanics.")
            .systemInstruction(systemInstruction)
            .build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCreateAsync_withResponseFormat(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/create_response_format." + suffix + ".json");

    Schema responseSchema =
        Schema.builder()
            .type(new Type(Type.Known.OBJECT))
            .properties(ImmutableMap.of(
                "name", Schema.builder().type(new Type(Type.Known.STRING)).build(),
                "age", Schema.builder().type(new Type(Type.Known.INTEGER)).build()))
            .required("name", "age")
            .build();

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Generate a person's information.")
            .responseFormat(responseSchema)
            .build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCreateAsync_withResponseMimeType(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/create_response_mime_type."
                + suffix
                + ".json");

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Return JSON with user data.")
            .responseMimeType("application/json")
            .build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCreateAsync_withResponseModalities(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/create_response_modalities."
                + suffix
                + ".json");

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Describe a sunset.")
            .responseModalities(Modality.Known.TEXT, Modality.Known.IMAGE)
            .build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCreateAsync_withStoreTrue(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/create_store_true." + suffix + ".json");

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Remember this conversation.")
            .store(true)
            .build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCreateAsync_withBackgroundTrue(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/create_background_true." + suffix + ".json");

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Process this in the background.")
            .background(true)
            .build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCreateAsync_withPreviousInteractionId(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/create_previous_interaction_id."
                + suffix
                + ".json");

    String previousId =
        vertexAI
            ? "projects/test-project/locations/us-central1/interactions/previous-id"
            : "v1_previous-id";

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Continue the conversation.")
            .previousInteractionId(previousId)
            .build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
  }

  // ==================== Async Agent-Based Interactions ====================

  @ParameterizedTest
  @ValueSource(booleans = {false}) // Agent is MLDev only
  public void testCreateAsync_withAgent(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/create_with_agent." + suffix + ".json");

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .agent(AGENT_ID)
            .input("Research the history of quantum computing.")
            .build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
    assertTrue(interaction.agent().isPresent());
    assertEquals(AGENT_ID, interaction.agent().get());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false}) // Agent is MLDev only
  public void testCreateAsync_withAgentConfig(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/create_with_agent_config."
                + suffix
                + ".json");

    com.google.genai.types.AgentConfig agentConfig =
        com.google.genai.types.AgentConfig.builder().type("research").build();

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .agent(AGENT_ID)
            .input("Deep research on AI ethics.")
            .agentConfig(agentConfig)
            .build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false}) // Agent is MLDev only
  public void testCreateAsync_agentWithBackground(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/create_agent_background." + suffix + ".json");

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .agent(AGENT_ID)
            .input("Long-running research task.")
            .background(true)
            .build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
  }

  // ==================== Async Tools & Function Calling ====================

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCreateAsync_withFunction(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/create_function_tool." + suffix + ".json");

    Function functionTool = Function.builder()
        .name("get_weather")
        .description("Get the current weather for a location")
        .parameters(
            Schema.builder()
                .type(new Type(Type.Known.OBJECT))
                .properties(ImmutableMap.of(
                    "location", Schema.builder().type(new Type(Type.Known.STRING)).build()))
                .required("location")
                .build())
        .build();

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("What's the weather in San Francisco?")
            .tools(ImmutableList.of(functionTool))
            .build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCreateAsync_withGoogleSearch(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/create_google_search." + suffix + ".json");

    GoogleSearch googleSearch = GoogleSearch.builder().build();

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Search for latest AI news.")
            .tools(ImmutableList.of(googleSearch))
            .build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCreateAsync_withCodeExecution(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/create_code_execution." + suffix + ".json");

    CodeExecution codeExecution = CodeExecution.builder().build();

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Calculate the factorial of 10.")
            .tools(ImmutableList.of(codeExecution))
            .build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCreateAsync_withMultipleTools(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/create_multiple_tools." + suffix + ".json");

    Function functionTool = Function.builder()
        .name("get_weather")
        .description("Get weather")
        .parameters(
            Schema.builder()
                .type(new Type(Type.Known.OBJECT))
                .properties(ImmutableMap.of(
                    "location", Schema.builder().type(new Type(Type.Known.STRING)).build()))
                .build())
        .build();
    GoogleSearch googleSearch = GoogleSearch.builder().build();
    CodeExecution codeExecution = CodeExecution.builder().build();

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .input("Use multiple tools to answer my question.")
            .tools(ImmutableList.of(functionTool, googleSearch, codeExecution))
            .build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
  }

  // ==================== Async Multimodal Content Tests ====================

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCreateAsync_withImageContent(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/create_image_content." + suffix + ".json");

    URL resourceUrl = getClass().getClassLoader().getResource("google.png");
    Path filePath = Paths.get(resourceUrl.toURI());

    ImageContent imageContent =
        ImageContent.builder().uri(filePath.toAbsolutePath().toString()).build();

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .inputFromContents(
                ImmutableList.of(
                    TextContent.builder().text("Describe this image.").build(), imageContent))
            .build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCreateAsync_withAudioContent(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/create_audio_content." + suffix + ".json");

    AudioContent audioContent =
        AudioContent.builder().uri("gs://test-bucket/audio.mp3").build();

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .inputFromContents(
                ImmutableList.of(
                    TextContent.builder().text("Transcribe this audio.").build(), audioContent))
            .build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCreateAsync_withVideoContent(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/create_video_content." + suffix + ".json");

    VideoContent videoContent =
        VideoContent.builder().uri("gs://test-bucket/video.mp4").build();

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .inputFromContents(
                ImmutableList.of(
                    TextContent.builder().text("Analyze this video.").build(), videoContent))
            .build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCreateAsync_withDocumentContent(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/create_document_content." + suffix + ".json");

    DocumentContent documentContent =
        DocumentContent.builder().uri("gs://test-bucket/document.pdf").build();

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .inputFromContents(
                ImmutableList.of(
                    TextContent.builder().text("Summarize this document.").build(),
                    documentContent))
            .build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCreateAsync_withMixedMediaContent(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/create_mixed_media." + suffix + ".json");

    URL resourceUrl = getClass().getClassLoader().getResource("google.png");
    Path filePath = Paths.get(resourceUrl.toURI());

    ImageContent imageContent =
        ImageContent.builder().uri(filePath.toAbsolutePath().toString()).build();

    AudioContent audioContent =
        AudioContent.builder().uri("gs://test-bucket/audio.mp3").build();

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .inputFromContents(
                ImmutableList.of(
                    TextContent.builder().text("Analyze these media files.").build(),
                    imageContent,
                    audioContent))
            .build();

    // Act
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    Interaction interaction = future.join();

    // Assert
    assertNotNull(interaction);
    assertNotNull(interaction.id());
  }

  // ==================== Async Error Handling ====================

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCreateAsync_validationError_completionException(boolean vertexAI)
      throws Exception {
    // Arrange
    Client client = TestUtils.createClient(vertexAI, "unused");

    CreateInteractionConfig config =
        CreateInteractionConfig.builder()
            .model(MODEL_ID)
            .agent(AGENT_ID)
            .input("Invalid config")
            .build();

    // Act & Assert
    CompletableFuture<Interaction> future = client.async.interactions.create(config);
    assertThrows(CompletionException.class, () -> future.join());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testGetAsync_notFound_completionException(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/get_not_found." + suffix + ".json");

    String invalidId = "invalid-interaction-id";
    GetInteractionConfig config = GetInteractionConfig.builder().build();

    // Act & Assert
    CompletableFuture<Interaction> future = client.async.interactions.get(invalidId, config);
    assertThrows(CompletionException.class, () -> future.join());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testCancelAsync_alreadyCancelled_completionException(boolean vertexAI)
      throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/cancel_already_cancelled."
                + suffix
                + ".json");

    String cancelledId =
        vertexAI
            ? "projects/test-project/locations/us-central1/interactions/cancelled-id"
            : "v1_cancelled-id";

    CancelInteractionConfig config = CancelInteractionConfig.builder().build();

    // Act & Assert
    CompletableFuture<Interaction> future =
        client.async.interactions.cancel(cancelledId, config);
    assertThrows(CompletionException.class, () -> future.join());
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  public void testDeleteAsync_notFound_completionException(boolean vertexAI) throws Exception {
    // Arrange
    String suffix = vertexAI ? "vertex" : "mldev";
    Client client =
        TestUtils.createClient(
            vertexAI,
            "tests/interactions/comprehensive_async/delete_not_found." + suffix + ".json");

    String notFoundId =
        vertexAI
            ? "projects/test-project/locations/us-central1/interactions/not-found-id"
            : "v1_not-found-id";

    DeleteInteractionConfig config = DeleteInteractionConfig.builder().build();

    // Act & Assert
    CompletableFuture<DeleteInteractionResponse> future =
        client.async.interactions.delete(notFoundId, config);
    assertThrows(CompletionException.class, () -> future.join());
  }
}
