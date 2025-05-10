#!/bin/bash

GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
RESET='\033[0m'

SCRIPT_DIR=$(pwd)
LOG_FILE="$SCRIPT_DIR/scripts/start_script.log"
VERSION="1.4.0"

cd "$SCRIPT_DIR" || exit

LOG_ENABLED=true  # Default log enabled

log() {
    if $LOG_ENABLED; then
        echo -e "$(date '+%Y-%m-%d %H:%M:%S') $1" | tee -a "$LOG_FILE"
    else
        echo -e "$(date '+%Y-%m-%d %H:%M:%S') $1"
    fi
}

check_docker() {
    if ! command -v docker &> /dev/null; then
        log "${RED}❌ Docker is not installed or not in PATH.${RESET}"
        exit 1
    fi
    if ! docker compose version &> /dev/null; then
        log "${RED}❌ Docker Compose not available. Please install or update Docker.${RESET}"
        exit 1
    fi
    if ! docker info &> /dev/null; then
        log "${RED}❌ Docker is not running. Please start Docker first.${RESET}"
        exit 1
    fi
}

help() {
    log "${YELLOW}User executed help command${RESET}"
    echo -e "${YELLOW}Usage: $0 <environment> <command> [options]${RESET}"
    echo -e "  environment: Specify the environment to manage (e.g., development, production)"
    echo -e ""
    echo -e "Commands:"
    echo -e "  start [--build]        Start the specified environment, optionally rebuild images"
    echo -e "  stop [--volumes]       Stop the running environment, optionally remove volumes"
    echo -e "  restart                Restart the environment"
    echo -e "  status                 Show the status of Docker containers"
    echo -e "  logs                   Show logs from the environment"
    echo -e "  clean                  Clean old logs"
    echo -e "  help                   Show this help message"
    echo -e "  version                Show the script version"
    echo -e ""
    echo -e "Options:"
    echo -e "  --nolog                Disable log output"
    echo -e "  --volumes              Remove volumes when stopping the environment"
    echo -e "  --build                Rebuild the Docker images when starting"
    echo -e "  --tail <N>             Show the last N lines of logs"
    echo -e "  --volumes              Remove volumes when stopping the environment"
    echo -e ""
    echo -e "Examples:"
    echo -e "  $0 development start --build    # Start development with rebuild"
    echo -e "  $0 production stop --volumes    # Stop production and remove volumes"
    echo -e "  $0 development logs --tail 50   # Show last 50 log lines"
    echo -e "  $0 development clean            # Clean old logs"
    echo -e "  $0 --help                      # Show help message"
    echo -e "  $0 --version                   # Show script version"
    exit 0
}

manage_env() {
    local ENV=$1
    local ACTION=$2
    local PARAMS=$3
    local COMPOSE_FILE="${SCRIPT_DIR}/docker-compose-${ENV}.yml"
    if [ "${ACTION}" == "ps" ]; then
        local CMD="docker compose -f ./docker-compose-base.yml -f \"${COMPOSE_FILE}\" --env-file .env.${ENV} ps -a"
        log "${YELLOW}🔧 ${ACTION} environment: ${ENV}${RESET}"
        eval "${CMD}"
        exit 0
    fi
    if [ ! -f "${COMPOSE_FILE}" ]; then
        log "${RED}❌ Docker Compose file not found: ${COMPOSE_FILE}${RESET}"
        exit 1
    fi
    local CMD="docker compose -f ./docker-compose-base.yml -f \"${COMPOSE_FILE}\" --env-file .env.${ENV} ${ACTION}"
    if [ "${ACTION}" == "down" ] && [ "${STOP_VOLUMES}" == "true" ]; then
        CMD+=" --volumes"
        log "${YELLOW}🗑️  Stopping and removing volumes for environment: ${ENV}${RESET}"
    fi
    log "${YELLOW}🔧 ${ACTION} environment: ${ENV}${RESET}"
    eval "${CMD}"
}

start_env() {
    local ENV=$1
    local ACTION=$2
    local PARAMS=$3
    local COMPOSE_FILE="${SCRIPT_DIR}/docker-compose-${ENV}.yml"
    local CMD="docker compose -f ./docker-compose-base.yml -f \"${COMPOSE_FILE}\" --env-file .env.${ENV} up -d"
    log "${GREEN}🚀 Starting environment: ${ENV}${RESET}"
    if ! [ -x "${SCRIPT_DIR}/scripts/init_env.sh" ]; then
        log "${RED}❌ Initialization script not found or not executable. Exiting...${RESET}"
        exit 1
    fi
    if ! "${SCRIPT_DIR}/scripts/init_env.sh" "${ENV}"; then
        log "${RED}❌ Initialization failed. Exiting...${RESET}"
        exit 1
    fi
    if [ "$BUILD" == "true" ]; then
        CMD+=" --build"
        log "${YELLOW}🔄 Rebuilding Docker images...${RESET}"
    fi
    log "${GREEN}🐳 Starting Docker Compose...${RESET}"
    eval "$CMD" | tee -a "$LOG_FILE"
}

clean_logs() {
    log "${YELLOW}🧹 Cleaning old logs...${RESET}"
    find "$SCRIPT_DIR/scripts" -name "*.log" -mtime +7 -exec rm {} \;
    log "${GREEN}✅ Old logs cleaned.${RESET}"
}

BUILD=false
STOP=false
STOP_VOLUMES=false
RESTART=false
STATUS=false
CLEAN=false
ENV=""
TAIL=false
NUM_TAIL=0

check_docker

if [ -z "$1" ]; then
    log "${RED}❌ Environment not specified. Please provide an environment (e.g., development, production).${RESET}"
    help
fi

ENV=$1
shift

while [[ $# -gt 0 ]]; do
    case "$1" in
        start)
            ACTION="start"
            shift
            ;;
        stop)
            ACTION="stop"
            shift
            ;;
        restart)
            ACTION="restart"
            shift
            ;;
        status)
            ACTION="status"
            shift
            ;;
        logs)
            ACTION="logs"
            shift
            ;;
        clean)
            ACTION="clean"
            shift
            ;;
        -b|--b|build)
            BUILD=true
            shift
            ;;
        --volumes)
            STOP_VOLUMES=true
            shift
            ;;
        --nolog)
            LOG_ENABLED=false
            shift
            ;;
        --tail)
            TAIL=true
            NUM_TAIL=$2
            if ! [[ "$NUM_TAIL" =~ ^[0-9]+$ ]]; then
                log "${RED}❌ Invalid number of lines specified for --tail. Please provide a valid number.${RESET}"
                exit 1
            fi
            shift 2
            ;;
        -v|--v|version)
            log "${GREEN}Version: $VERSION${RESET}"
            exit 0
            ;;
        -h|--h|help)
            help
            ;;
        *)
            log "${RED}❌ Invalid action specified.${RESET}"
            help
            ;;
    esac
done

case "$ACTION" in
    start)
        start_env "$ENV" "$ACTION"
        ;;
    stop)
        manage_env "$ENV" "down" "$STOP_VOLUMES"
        ;;
    restart)
        manage_env "$ENV" "down" "$STOP_VOLUMES"
        start_env "$ENV" "$ACTION" "$BUILD"
        ;;
    status)
        manage_env "$ENV" "ps"
        ;;
    logs)
        if [ "$TAIL" == true ]; then
            log "${YELLOW}📝 Showing last $NUM_TAIL lines of logs...${RESET}"
            tail -n "$NUM_TAIL" "$LOG_FILE"
        else
            cat "$LOG_FILE"
        fi
        ;;
    clean)
        clean_logs
        ;;
    *)
        log "${RED}❌ Invalid action. Please use 'start', 'stop', 'restart', 'logs', or 'clean'.${RESET}"
        help
        ;;
esac