# Interactions API Examples - Test Results & Setup Guide

## Overview

This document provides information about the Interactions API examples, their test results, and setup requirements for examples that need additional configuration.

## Automated Test Scripts

Two shell scripts are provided to run and validate all examples:

1. **`run_all_interactions_examples.sh`** - Runs all 19 Interactions API examples sequentially
2. **`validate_interactions_results.sh`** - Validates the output from each example

### Usage

```bash
# Run all examples
./run_all_interactions_examples.sh

# Validate results (uses latest logs by default)
./validate_interactions_results.sh

# Validate specific log directory
./validate_interactions_results.sh ./interaction_examples_logs
```

### Log Files

- All logs are saved in `interaction_examples_logs/` directory
- Each example generates a separate log file with timestamp
- A summary file is created after each run

## Test Results Summary

### ✅ Successfully Executed (13 examples)

These examples run successfully without any additional setup:

1. **InteractionsSimpleStringInput** - Basic string input interaction
   - Tests: Simple question-answer interaction
   - Expected output: Answer about capital of France

2. **InteractionsMultipleContents** - Multiple content items in single request
   - Tests: Combining multiple text prompts
   - Expected output: Information about Eiffel Tower

3. **InteractionsImageContent** - Image analysis
   - Tests: Image from URI and inline base64 data
   - Expected output: Description of cake/dessert image

4. **InteractionsAudioContent** - Audio transcription and analysis
   - Tests: Audio file processing
   - Expected output: Transcription and summary of audio content

5. **InteractionsVideoContent** - Video analysis
   - Tests: Video understanding from URI
   - Expected output: Description of video content about Tokyo photographer

6. **InteractionsDocumentContent** - PDF document analysis
   - Tests: Document from URI and inline base64 data
   - Expected output: Document summary
   - **Note**: Updated to use `https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf` instead of the original Google Cloud Storage URL

7. **InteractionsUrlContext** - URL context tool for fetching web content
   - Tests: Fetching and summarizing web pages
   - Expected output: Summary of Wikipedia article on AI

8. **InteractionsFunctionCalling** - Function calling (manual and automatic)
   - Tests: Both manual function calling and Automatic Function Calling (AFC)
   - Expected output: Weather information for Paris and Tokyo

9. **InteractionsCodeExecution** - Code execution tool
   - Tests: Python code execution for calculating Fibonacci numbers
   - Expected output: First 20 Fibonacci numbers and their sum (10945)

10. **InteractionsGoogleSearch** - Google Search integration
    - Tests: Using Google Search tool to find current information
    - Expected output: Information about quantum computing in 2025

11. **InteractionsMultiTurnConversation** - Multi-turn conversations
    - Tests: Building conversation history manually
    - Expected output: Paris travel recommendations

12. **InteractionsAsyncMultiTurn** - Asynchronous multi-turn interactions
    - Tests: Async API usage with conversation history
    - Expected output: Machine learning applications

13. **InteractionsPreviousInteractionId** - Conversation continuity
    - Tests: Using previousInteractionId to link conversations
    - Expected output: Simplified explanation of quantum entanglement

### ⚠️ Expected Failures (4 examples - require additional setup)

These examples fail due to missing external dependencies or special permissions. This is expected behavior.

#### 14. InteractionsFileSearch
- **Status**: Expected failure
- **Reason**: Requires file search stores to be created
- **Error**: `400 . Invalid input received`
- **Setup Required**:
  - Create file search stores using the Files API
  - Upload documents to the stores
  - Update example with actual store names
  - See [File Search Setup](#file-search-setup) below

#### 15. InteractionsFileSearchCallContent
- **Status**: Expected failure (partial success)
- **Reason**: Requires file search stores to be created
- **Error**: `400 . Invalid input received` for API calls
- **Note**: Serialization/deserialization tests pass successfully
- **Setup Required**: Same as InteractionsFileSearch

#### 16. InteractionsComputerUse
- **Status**: Expected failure
- **Reason**: Requires special API permissions
- **Error**: `400 . Computer Use is not enabled for models/gemini-2.5-flash`
- **Setup Required**:
  - Request Computer Use API access from Google
  - Ensure your API key has Computer Use permissions
  - May require allowlist approval

#### 17. InteractionsMcpServer
- **Status**: Expected failure
- **Reason**: Requires a running MCP (Model Context Protocol) server
- **Error**: `500 . Internal server error`
- **Setup Required**:
  - Set up and run an MCP server
  - Update example with correct MCP server URL
  - Configure any required authentication headers
  - See [MCP Server Setup](#mcp-server-setup) below

### ✅ Test/Validation Examples (2 examples)

These examples are for testing specific features and run successfully:

#### 18. InteractionsThoughtContent
- **Status**: Success
- **Tests**: Model's internal reasoning capabilities
- **Expected output**: Step-by-step reasoning for math problems

#### 19. InteractionsTextAnnotations
- **Status**: Success
- **Tests**: Text annotations in responses
- **Expected output**: Annotation analysis for different types of queries

## Setup Guides

### File Search Setup

To run the File Search examples (InteractionsFileSearch and InteractionsFileSearchCallContent):

1. **Create File Search Stores**:
   ```bash
   # Use the Files API to create stores
   # See: https://ai.google.dev/gemini-api/docs/file-search
   ```

2. **Upload Documents**:
   ```bash
   # Upload your documents to the stores
   # Supported formats: PDF, TXT, HTML, etc.
   ```

3. **Update Example Code**:
   ```java
   // In InteractionsFileSearch.java, update store names:
   FileSearchTool fileSearchTool = FileSearchTool.builder()
       .fileSearchStoreNames(Arrays.asList("your-store-name-1", "your-store-name-2"))
       .topK(10)
       .build();
   ```

4. **Run the Example**:
   ```bash
   mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsFileSearch"
   ```

### MCP Server Setup

To run the InteractionsMcpServer example:

1. **Install and Run an MCP Server**:
   - Follow MCP server documentation
   - Ensure server is accessible at the configured URL

2. **Update Example Configuration**:
   ```java
   // In InteractionsMcpServer.java, update:
   McpServerTool mcpServerTool = McpServerTool.builder()
       .name("your-mcp-server-name")
       .url("http://your-server:port/mcp")
       .headers(Map.of("Content-Type", "application/json"))
       .build();
   ```

3. **Run the Example**:
   ```bash
   mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsMcpServer"
   ```

### Computer Use Setup

To run the InteractionsComputerUse example:

1. **Request Access**:
   - Contact Google to request Computer Use API access
   - Provide your use case and API key

2. **Wait for Approval**:
   - Computer Use may require allowlist approval
   - You'll receive notification when enabled

3. **Run the Example**:
   ```bash
   mvn exec:java -Dexec.mainClass="com.google.genai.examples.InteractionsComputerUse"
   ```

## TODO

- [ ] **Set up File Search Stores**: Create and configure file search stores for testing InteractionsFileSearch and InteractionsFileSearchCallContent examples
  - Create at least 2 file search stores
  - Upload sample documents (PDFs, text files, etc.)
  - Update example code with actual store names
  - Document the setup process

- [ ] **Request Computer Use API Access**: Apply for Computer Use API permissions to enable InteractionsComputerUse example
  - Submit access request to Google
  - Wait for approval notification
  - Test example once access is granted

- [ ] **Set up MCP Server**: Deploy and configure an MCP server for testing InteractionsMcpServer example
  - Choose MCP server implementation
  - Deploy server (local or remote)
  - Configure authentication if required
  - Update example with server endpoint
  - Test connectivity

- [ ] **Restore Original Document URL**: Investigate why the original Google Cloud Storage URL (`https://storage.googleapis.com/cloud-samples-data/generative-ai/pdf/2403.05530.pdf`) is not accessible and either:
  - Fix access to the original URL, or
  - Document the reason for using the alternative URL
  - Consider hosting example documents in a more reliable location

- [ ] **Create CI/CD Integration**: Add automated testing for all examples
  - Set up GitHub Actions or similar CI/CD
  - Run test script on each commit
  - Generate and publish test reports
  - Flag unexpected failures

- [ ] **Add Performance Metrics**: Track example execution times and API response times
  - Measure latency for each example
  - Compare different model performance
  - Document baseline metrics

## Important Notes

### Document Content Example Update

The `InteractionsDocumentContent.java` example was updated to use a different PDF URL:

- **Original URL**: `https://storage.googleapis.com/cloud-samples-data/generative-ai/pdf/2403.05530.pdf`
- **New URL**: `https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf`
- **Reason**: The original URL was returning `400 . Cannot fetch content from the provided URL`

### Expected Behavior

All examples that can run successfully do so. The failures for InteractionsFileSearch, InteractionsFileSearchCallContent, InteractionsComputerUse, and InteractionsMcpServer are **expected** and **normal** due to missing external dependencies or special permissions.

### Environment Variables

The test scripts automatically unset Vertex AI environment variables to use the Gemini API:

```bash
unset GOOGLE_CLOUD_PROJECT
unset GOOGLE_CLOUD_LOCATION
unset GOOGLE_GENAI_USE_VERTEXAI
```

Ensure you have `GOOGLE_API_KEY` or `GEMINI_API_KEY` set in your environment.

## Troubleshooting

### Common Issues

1. **API Key Not Set**:
   ```
   Error: API key not found
   ```
   Solution: Set `GOOGLE_API_KEY` or `GEMINI_API_KEY` environment variable

2. **Compilation Errors**:
   ```
   Error: Could not find or load main class
   ```
   Solution: Run `mvn clean compile` before executing examples

3. **Rate Limiting**:
   ```
   Error: 429 Too Many Requests
   ```
   Solution: Add delays between example runs or reduce concurrent requests

4. **Network Timeouts**:
   ```
   Error: Connection timeout
   ```
   Solution: Check internet connection, increase timeout values

## Support

For issues or questions:
- Check example source code comments for detailed documentation
- Review the main README.md for general setup instructions
- Open an issue on GitHub with example name and error details

## Last Updated

- Date: 2026-01-22
- Test Run: All 19 examples executed
- Success Rate: 13/13 executable examples (100%)
- Expected Failures: 4/4 (requires additional setup)
- Test Examples: 2/2 (100%)
