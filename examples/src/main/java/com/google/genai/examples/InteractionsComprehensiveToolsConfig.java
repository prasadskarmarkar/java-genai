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

/**
 * Comprehensive example demonstrating ALL available fields for tools in the Interactions API.
 *
 * <p>This example shows how to configure each tool type with all its optional fields, not just the
 * required ones.
 *
 * <p>Usage: 1. Set an API key environment variable: export GOOGLE_API_KEY=YOUR_API_KEY
 *
 * <p>2. Run the example: mvn exec:java
 * -Dexec.mainClass="com.google.genai.examples.InteractionsComprehensiveToolsConfig"
 */
package com.google.genai.examples;

import com.google.common.collect.ImmutableMap;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import com.google.genai.types.Schema;
import com.google.genai.types.ThinkingLevel;
import com.google.genai.types.interactions.AllowedTools;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.GenerationConfig;
import com.google.genai.types.interactions.ImageConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.SpeechConfig;
import com.google.genai.types.interactions.ThinkingSummaries;
import com.google.genai.types.interactions.ToolChoice;
import com.google.genai.types.interactions.ToolChoiceType;
import com.google.genai.types.interactions.tools.CodeExecution;
import com.google.genai.types.interactions.tools.ComputerUse;
import com.google.genai.types.interactions.tools.FileSearch;
import com.google.genai.types.interactions.tools.Function;
import com.google.genai.types.interactions.tools.GoogleSearch;
import com.google.genai.types.interactions.tools.McpServer;
import com.google.genai.types.interactions.tools.Tool;
import com.google.genai.types.interactions.tools.UrlContext;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InteractionsComprehensiveToolsConfig {

  public static void main(String[] args) {
    try {
      Client client = new Client();

      System.out.println("================================================================================");
      System.out.println("Interactions API: Comprehensive Tools Configuration Example");
      System.out.println("================================================================================");
      System.out.println();
      System.out.println("This example demonstrates ALL available fields for each tool type,");
      System.out.println("including both required and optional parameters.");
      System.out.println();

      // ========================================
      // Configure all tools with ALL fields
      // ========================================
      List<Tool> tools = configureAllTools();

      // ========================================
      // Configure GenerationConfig with ALL fields
      // ========================================
      GenerationConfig genConfig = configureComprehensiveGenerationConfig();

      // ========================================
      // Create interaction with comprehensive configuration
      // ========================================
      String userInput = "Demonstrate all tool capabilities with full configuration";

      CreateInteractionConfig config =
          CreateInteractionConfig.builder()
              .model("gemini-2.5-flash")
              .input(userInput)
              .tools(tools)
              .generationConfig(genConfig)
              .systemInstruction(
                  Content.fromParts(
                      Part.fromText("You are a helpful assistant demonstrating comprehensive tool usage.")))
              .build();

      System.out.println("================================================================================");
      System.out.println("REQUEST CONFIGURATION");
      System.out.println("================================================================================");
      System.out.println();
      System.out.println("Request JSON:");
      System.out.println(config.toJson());
      System.out.println();

      // Note: This example focuses on showing configuration.
      // Actual API call commented out to avoid hitting unsupported combinations
      // Uncomment below to test with individual tool types

      // Interaction response = client.interactions.create(config);
      // System.out.println("Response JSON:");
      // System.out.println(response.toJson());

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Configure all tools with ALL available fields (required and optional).
   */
  private static List<Tool> configureAllTools() throws Exception {
    List<Tool> tools = new ArrayList<>();

    System.out.println("--------------------------------------------------------------------------------");
    System.out.println("TOOL 1: Function (with ALL fields)");
    System.out.println("--------------------------------------------------------------------------------");

    // Function - Manual configuration with explicit parameters
    Function manualFunction =
        Function.builder()
            .name("calculate_sum")
            .description("Calculate the sum of two numbers with detailed parameter schema")
            .parameters(
                Schema.builder()
                    .type("object")
                    .properties(
                        Map.of(
                            "a",
                            Schema.builder()
                                .type("number")
                                .description("First number to add")
                                .build(),
                            "b",
                            Schema.builder()
                                .type("number")
                                .description("Second number to add")
                                .build()))
                    .required("a", "b")
                    .build())
            .build();
    tools.add(manualFunction);
    System.out.println("  ✓ Manual Function configured");
    System.out.println("    - name: calculate_sum");
    System.out.println("    - description: provided");
    System.out.println("    - parameters: full JSON schema with types and descriptions");
    System.out.println();

    // Note: The Interactions API does not support Automatic Function Calling (AFC).
    // All function calling must be handled manually by the application.
    System.out.println("  Note: AFC is not supported in Interactions API");
    System.out.println("    - Use manual function calling pattern instead");
    System.out.println();

    System.out.println("--------------------------------------------------------------------------------");
    System.out.println("TOOL 2: GoogleSearch (with ALL fields)");
    System.out.println("--------------------------------------------------------------------------------");

    // GoogleSearch - Only has type field (no optional fields in spec)
    GoogleSearch googleSearchTool = GoogleSearch.builder().build();
    tools.add(googleSearchTool);
    System.out.println("  ✓ GoogleSearch configured");
    System.out.println("    - type: google_search");
    System.out.println("    - NOTE: No additional optional fields available per OpenAPI spec");
    System.out.println();

    System.out.println("--------------------------------------------------------------------------------");
    System.out.println("TOOL 3: UrlContext (with ALL fields)");
    System.out.println("--------------------------------------------------------------------------------");

    // UrlContext - Only has type field (no optional fields in spec)
    UrlContext urlContextTool = UrlContext.builder().build();
    tools.add(urlContextTool);
    System.out.println("  ✓ UrlContext configured");
    System.out.println("    - type: url_context");
    System.out.println("    - NOTE: No additional optional fields available per OpenAPI spec");
    System.out.println();

    System.out.println("--------------------------------------------------------------------------------");
    System.out.println("TOOL 4: CodeExecution (with ALL fields)");
    System.out.println("--------------------------------------------------------------------------------");

    // CodeExecution - Only has type field (no optional fields in spec)
    CodeExecution codeExecutionTool = CodeExecution.builder().build();
    tools.add(codeExecutionTool);
    System.out.println("  ✓ CodeExecution configured");
    System.out.println("    - type: code_execution");
    System.out.println("    - NOTE: No additional optional fields available per OpenAPI spec");
    System.out.println();

    System.out.println("--------------------------------------------------------------------------------");
    System.out.println("TOOL 5: FileSearch (with ALL fields)");
    System.out.println("--------------------------------------------------------------------------------");

    // FileSearch - with ALL optional fields
    FileSearch fileSearchTool =
        FileSearch.builder()
            .fileSearchStoreNames(Arrays.asList("my-store-1", "my-store-2"))
            .topK(10)
            .metadataFilter("category:technical AND year:2025")
            .build();
    tools.add(fileSearchTool);
    System.out.println("  ✓ FileSearch configured with ALL fields:");
    System.out.println("    - type: file_search");
    System.out.println("    - file_search_store_names: [my-store-1, my-store-2]");
    System.out.println("    - top_k: 10 (number of semantic retrieval chunks)");
    System.out.println("    - metadata_filter: category:technical AND year:2025");
    System.out.println();

    System.out.println("--------------------------------------------------------------------------------");
    System.out.println("TOOL 6: ComputerUse (with ALL fields)");
    System.out.println("--------------------------------------------------------------------------------");

    // ComputerUse - with ALL optional fields
    ComputerUse computerUseTool =
        ComputerUse.builder()
            .environment("browser")
            .excludedPredefinedFunctions(Arrays.asList("navigate", "scroll"))
            .build();
    tools.add(computerUseTool);
    System.out.println("  ✓ ComputerUse configured with ALL fields:");
    System.out.println("    - type: computer_use");
    System.out.println("    - environment: browser");
    System.out.println("    - excluded_predefined_functions: [navigate, scroll]");
    System.out.println();

    System.out.println("--------------------------------------------------------------------------------");
    System.out.println("TOOL 7: McpServer (with ALL fields)");
    System.out.println("--------------------------------------------------------------------------------");

    // McpServer - with ALL optional fields
    McpServer mcpServerTool =
        McpServer.builder()
            .name("my-mcp-server")
            .url("https://api.example.com/mcp")
            .headers(
                ImmutableMap.of(
                    "Authorization", "Bearer token123",
                    "X-Custom-Header", "custom-value"))
            .allowedTools(
                Arrays.asList(
                    AllowedTools.builder()
                        .mode("auto")
                        .tools("tool1", "tool2")
                        .build()))
            .build();
    tools.add(mcpServerTool);
    System.out.println("  ✓ McpServer configured with ALL fields:");
    System.out.println("    - type: mcp_server");
    System.out.println("    - name: my-mcp-server");
    System.out.println("    - url: https://api.example.com/mcp");
    System.out.println("    - headers: {Authorization, X-Custom-Header}");
    System.out.println("    - allowed_tools: [{mode: AUTO, tools: [tool1, tool2]}]");
    System.out.println();

    System.out.println("Total tools configured: " + tools.size());
    System.out.println();

    return tools;
  }

  /**
   * Configure GenerationConfig with ALL available fields.
   */
  private static GenerationConfig configureComprehensiveGenerationConfig() {
    System.out.println("--------------------------------------------------------------------------------");
    System.out.println("GENERATION CONFIG (with ALL fields)");
    System.out.println("--------------------------------------------------------------------------------");

    // SpeechConfig with all fields
    SpeechConfig speechConfig =
        SpeechConfig.builder()
            .voice("en-US-Standard-A")
            .language("en-US")
            .speaker("Assistant")
            .build();

    // ImageConfig with all fields
    ImageConfig imageConfig =
        ImageConfig.builder()
            .aspectRatio("16:9")
            .imageSize("4K")
            .build();

    // ToolChoice configuration - using simple type-based control
    ToolChoice toolChoice = ToolChoice.fromType(ToolChoiceType.Known.AUTO);

    // GenerationConfig with ALL available fields
    GenerationConfig genConfig =
        GenerationConfig.builder()
            .temperature(0.7f)
            .topP(0.9f)
            .seed(12345)
            .stopSequences(Arrays.asList("STOP", "END"))
            .maxOutputTokens(2048)
            .toolChoice(toolChoice)
            .thinkingLevel(new ThinkingLevel(ThinkingLevel.Known.MEDIUM))
            .thinkingSummaries(new ThinkingSummaries(ThinkingSummaries.Known.AUTO))
            .speechConfig(speechConfig)
            .imageConfig(imageConfig)
            .build();

    System.out.println("  ✓ GenerationConfig configured with ALL fields:");
    System.out.println("    - temperature: 0.7");
    System.out.println("    - top_p: 0.9");
    System.out.println("    - seed: 12345 (for reproducibility)");
    System.out.println("    - stop_sequences: [STOP, END]");
    System.out.println("    - max_output_tokens: 2048");
    System.out.println("    - tool_choice: AUTO");
    System.out.println("    - thinking_level: MEDIUM");
    System.out.println("    - thinking_summaries: AUTO");
    System.out.println("    - speech_config: {voice: en-US-Standard-A, language: en-US, speaker: Assistant}");
    System.out.println("    - image_config: {aspect_ratio: 16:9, image_size: 4K}");
    System.out.println();

    return genConfig;
  }

  /**
   * Example function for AFC (Automatic Function Calling).
   */
  public static Map<String, Object> getWeather(String location) {
    return ImmutableMap.of(
        "location", location,
        "temperature", "22",
        "unit", "celsius",
        "condition", "sunny",
        "humidity", "45%");
  }
}
