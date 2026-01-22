#!/bin/bash

# Script to validate the results from Interactions API examples
# Usage: ./validate_interactions_results.sh <log_directory>

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

if [ $# -eq 0 ]; then
    LOG_DIR="./interaction_examples_logs"
else
    LOG_DIR=$1
fi

if [ ! -d "$LOG_DIR" ]; then
    echo -e "${RED}Error: Log directory $LOG_DIR does not exist${NC}"
    exit 1
fi

echo "========================================="
echo "Validating Interactions API Example Results"
echo "========================================="
echo "Log Directory: $LOG_DIR"
echo ""

# Find the most recent log files
LATEST_LOGS=$(ls -t "$LOG_DIR"/*.log 2>/dev/null | head -n 19)

if [ -z "$LATEST_LOGS" ]; then
    echo -e "${RED}No log files found in $LOG_DIR${NC}"
    exit 1
fi

TOTAL=0
VALIDATED=0
ISSUES=0

# Function to get validation pattern for each example
get_validation_pattern() {
    local name=$1
    case "$name" in
        "InteractionsSimpleStringInput")
            echo "capital of France|Paris"
            ;;
        "InteractionsMultipleContents")
            echo "Eiffel Tower"
            ;;
        "InteractionsImageContent")
            echo "cake|dessert|Tiramisu"
            ;;
        "InteractionsAudioContent")
            echo "transcribe|audio|Pixel"
            ;;
        "InteractionsVideoContent")
            echo "video|Saeka Shimada|Tokyo|photographer"
            ;;
        "InteractionsDocumentContent")
            echo "Dummy PDF file|Hello World"
            ;;
        "InteractionsUrlContext")
            echo "Artificial intelligence|machine learning"
            ;;
        "InteractionsFunctionCalling")
            echo "weather|temperature|Paris|Tokyo"
            ;;
        "InteractionsCodeExecution")
            echo "Fibonacci|10945"
            ;;
        "InteractionsGoogleSearch")
            echo "quantum computing|2025"
            ;;
        "InteractionsMultiTurnConversation")
            echo "Paris|Eiffel Tower|Louvre|Notre Dame"
            ;;
        "InteractionsAsyncMultiTurn")
            echo "machine learning|real-world applications"
            ;;
        "InteractionsPreviousInteractionId")
            echo "quantum|entanglement|coins"
            ;;
        "InteractionsThoughtContent")
            echo "thought|reasoning"
            ;;
        "InteractionsTextAnnotations")
            echo "TextContent|annotation"
            ;;
        "InteractionsFileSearch")
            echo "Invalid input received|file search stores"
            ;;
        "InteractionsFileSearchCallContent")
            echo "Invalid input received|Serialization/Deserialization successful"
            ;;
        "InteractionsComputerUse")
            echo "Computer Use is not enabled"
            ;;
        "InteractionsMcpServer")
            echo "Internal server error|MCP server"
            ;;
        *)
            echo ""
            ;;
    esac
}

validate_log() {
    local log_file=$1
    local example_name=$(basename "$log_file" | sed 's/_[0-9]*_[0-9]*\.log$//')

    ((TOTAL++))

    echo -e "${BLUE}[$TOTAL] Validating: $example_name${NC}"

    if [ ! -f "$log_file" ]; then
        echo -e "${RED}  ✗ Log file not found${NC}"
        ((ISSUES++))
        return 1
    fi

    # Get validation pattern
    local pattern=$(get_validation_pattern "$example_name")

    if [ -z "$pattern" ]; then
        echo -e "${YELLOW}  ⚠ No validation pattern defined${NC}"
        return 0
    fi

    # Check if log contains expected pattern
    if grep -qiE "$pattern" "$log_file"; then
        echo -e "${GREEN}  ✓ Validation passed${NC}"

        # Show snippet of matched content
        echo -e "${BLUE}  Sample output:${NC}"
        grep -iE "$pattern" "$log_file" | head -n 2 | sed 's/^/    /'
        ((VALIDATED++))
        return 0
    else
        echo -e "${RED}  ✗ Validation failed - expected pattern not found${NC}"
        echo -e "${YELLOW}  Expected pattern: $pattern${NC}"

        # Check for BUILD SUCCESS
        if grep -q "BUILD SUCCESS" "$log_file"; then
            echo -e "${GREEN}  ℹ Build succeeded but output doesn't match expected pattern${NC}"
        fi

        # Check for errors
        if grep -qE "ERROR|Exception|FAILED" "$log_file"; then
            echo -e "${RED}  ℹ Errors found in log:${NC}"
            grep -E "ERROR|Exception" "$log_file" | head -n 3 | sed 's/^/    /'
        fi

        ((ISSUES++))
        return 1
    fi
}

# Validate each log file
for log_file in $LATEST_LOGS; do
    validate_log "$log_file"
    echo ""
done

# Print summary
echo "========================================="
echo "Validation Summary"
echo "========================================="
echo "Total Examples Checked: $TOTAL"
echo -e "${GREEN}Validated Successfully: $VALIDATED${NC}"
echo -e "${RED}Issues Found: $ISSUES${NC}"
echo "========================================="

# Exit with error if there are issues
if [ $ISSUES -gt 0 ]; then
    exit 1
fi

exit 0
