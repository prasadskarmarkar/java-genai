# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```sh
# Build and run all tests
mvn clean test -Dtest='**/*Test' -Djacoco.skip=true

# Run a single test class
mvn clean test -Dtest='InteractionsTest' -Djacoco.skip=true

# Run with a specific JDK version (tests require JDK 11 or 17 — check .github/workflows/unit-tests.yml)
JAVA_HOME=$(/usr/libexec/java_home -v 21) mvn clean test -Dtest='**/*Test' -Djacoco.skip=true

# Build without tests
mvn clean package -DskipTests
```

## Architecture

The SDK wraps both the **Gemini Developer API** and **Gemini Enterprise Agent Platform (Vertex AI)** under a single `Client` class. The client detects the backend from constructor args: `apiKey` → Gemini API, `project`+`location` → Vertex AI, `enterprise(true)` → Enterprise Agent Platform.

### Module structure

- `src/main/java/com/google/genai/` — SDK core
- `src/test/java/com/google/genai/` — unit + replay tests
- `examples/` — standalone Maven module with runnable examples (separate `pom.xml`)

### Key classes

- **`Client`** — entry point; exposes `models`, `chats`, `files`, `interactions`, `fileSearchStores`, etc. as public fields. Each field is a service class (e.g. `Models`, `Interactions`). Async variants live under `client.async.*`.
- **`ApiClient`** (abstract) / **`HttpApiClient`** — handles HTTP transport, auth, retries.
- **`ReplayApiClient`** — used in tests to replay recorded HTTP interactions from JSON files in `src/test/resources/`.
- **`Interactions`** / **`AsyncInteractions`** — the Interactions API service added on `feat/interactions-api`.

### Type system conventions

All request/response types follow a strict pattern:

1. **`@AutoValue`** immutable value classes with a nested `Builder` — every field uses `Optional<T>` for nullable fields, with generated `clear*()` methods.
2. **Enum wrapper pattern** — enums like `HarmCategory`, `InteractionStatus` wrap a `Known` inner enum for forward compatibility. Unknown API values fall back to an `UNSPECIFIED` sentinel rather than throwing.
3. **Discriminated unions** — polymorphic types (e.g. `Content`, `Tool`, `Step`, `InteractionSseEvent`) use Jackson `@JsonTypeInfo` + `@JsonSubTypes` with a `type` discriminator property. Base types are interfaces or abstract classes; concrete subtypes use `@JsonTypeName`.
4. **`@ExcludeFromGeneratedCoverageReport`** — applied to constructors/methods that Jacoco should skip. The annotation name contains "generated" which triggers Jacoco exclusion automatically.

### Interactions API (`feat/interactions-api`)

Types live under `com.google.genai.types.interactions.*`:
- `content/` — discriminated `Content` subtypes (TextContent, ImageContent, etc.)
- `streaming/` — SSE event types; `delta/` contains streaming delta types
- `tools/` — Tool subtypes (GoogleSearch, FileSearch, Function, etc.)

The canonical spec is at `https://ai.google.dev/static/api/interactions.openapi.json`. When adding or modifying types, validate field names and discriminator values against this spec.
