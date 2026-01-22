#!/bin/bash

# Script to run all Interactions API examples and capture results
# Usage: ./run_all_interactions_examples.sh

set -e  # Exit on error (can be disabled for collecting all results)

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Counters
TOTAL=0
SUCCESS=0
FAILED=0
SKIPPED=0

# Output directory for logs
LOG_DIR="./interaction_examples_logs"
mkdir -p "$LOG_DIR"

# Timestamp for this run
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
SUMMARY_FILE="$LOG_DIR/summary_${TIMESTAMP}.txt"

# Unset Vertex AI environment variables to use Gemini API
unset GOOGLE_CLOUD_PROJECT GOOGLE_CLOUD_LOCATION GOOGLE_GENAI_USE_VERTEXAI

echo "========================================" | tee "$SUMMARY_FILE"
echo "Interactions API Examples Test Run" | tee -a "$SUMMARY_FILE"
echo "Started at: $(date)" | tee -a "$SUMMARY_FILE"
echo "========================================" | tee -a "$SUMMARY_FILE"
echo "" | tee -a "$SUMMARY_FILE"

# Array of example class names
EXAMPLES=(
    "InteractionsSimpleStringInput"
    "InteractionsMultipleContents"
    "InteractionsImageContent"
    "InteractionsAudioContent"
    "InteractionsVideoContent"
    "InteractionsDocumentContent"
    "InteractionsUrlContext"
    "InteractionsFunctionCalling"
    "InteractionsCodeExecution"
    "InteractionsFileSearch"
    "InteractionsFileSearchCallContent"
    "InteractionsGoogleSearch"
    "InteractionsComputerUse"
    "InteractionsMcpServer"
    "InteractionsMultiTurnConversation"
    "InteractionsAsyncMultiTurn"
    "InteractionsPreviousInteractionId"
    "InteractionsThoughtContent"
    "InteractionsTextAnnotations"
)

# Expected failures (examples that require additional setup)
# Function to check if example is expected to fail
is_expected_failure() {
    local name=$1
    case "$name" in
        "InteractionsFileSearch"|"InteractionsFileSearchCallContent")
            echo "Requires file search stores"
            return 0
            ;;
        "InteractionsComputerUse")
            echo "Requires special API permissions"
            return 0
            ;;
        "InteractionsMcpServer")
            echo "Requires running MCP server"
            return 0
            ;;
        *)
            return 1
            ;;
    esac
}

# Function to run a single example
run_example() {
    local example_name=$1
    local example_class="com.google.genai.examples.${example_name}"
    local log_file="$LOG_DIR/${example_name}_${TIMESTAMP}.log"

    ((TOTAL++))

    echo -e "${BLUE}[${TOTAL}/${#EXAMPLES[@]}] Running: ${example_name}${NC}"

    # Run the example and capture output
    if mvn -q exec:java -Dexec.mainClass="$example_class" > "$log_file" 2>&1; then
        echo -e "${GREEN}✓ SUCCESS${NC}"
        echo "[SUCCESS] $example_name" >> "$SUMMARY_FILE"
        ((SUCCESS++))
        return 0
    else
        # Check if this is an expected failure
        local failure_reason
        if failure_reason=$(is_expected_failure "$example_name"); then
            echo -e "${YELLOW}⚠ EXPECTED FAILURE: ${failure_reason}${NC}"
            echo "[EXPECTED_FAILURE] $example_name - ${failure_reason}" >> "$SUMMARY_FILE"
            ((SKIPPED++))
            return 0
        else
            echo -e "${RED}✗ FAILED${NC}"
            echo "[FAILED] $example_name" >> "$SUMMARY_FILE"
            # Show last 20 lines of error
            echo "Last 20 lines of error:" | tee -a "$SUMMARY_FILE"
            tail -n 20 "$log_file" | tee -a "$SUMMARY_FILE"
            ((FAILED++))
            return 1
        fi
    fi
}

# Compile the project first
echo -e "${BLUE}Compiling the project...${NC}"
if mvn clean compile -q; then
    echo -e "${GREEN}✓ Compilation successful${NC}"
    echo ""
else
    echo -e "${RED}✗ Compilation failed${NC}"
    exit 1
fi

# Run all examples
for example in "${EXAMPLES[@]}"; do
    run_example "$example"
    echo ""
done

# Print summary
echo "========================================" | tee -a "$SUMMARY_FILE"
echo "Test Run Summary" | tee -a "$SUMMARY_FILE"
echo "========================================" | tee -a "$SUMMARY_FILE"
echo "Total Examples: $TOTAL" | tee -a "$SUMMARY_FILE"
echo -e "${GREEN}Successful: $SUCCESS${NC}" | tee -a "$SUMMARY_FILE"
echo -e "${YELLOW}Expected Failures (Skipped): $SKIPPED${NC}" | tee -a "$SUMMARY_FILE"
echo -e "${RED}Unexpected Failures: $FAILED${NC}" | tee -a "$SUMMARY_FILE"
echo "" | tee -a "$SUMMARY_FILE"
echo "Completed at: $(date)" | tee -a "$SUMMARY_FILE"
echo "Log files saved in: $LOG_DIR" | tee -a "$SUMMARY_FILE"
echo "Summary saved in: $SUMMARY_FILE" | tee -a "$SUMMARY_FILE"
echo "========================================" | tee -a "$SUMMARY_FILE"

# Exit with error if there are unexpected failures
if [ $FAILED -gt 0 ]; then
    exit 1
fi

exit 0
