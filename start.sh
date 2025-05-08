#!/bin/bash

GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
RESET='\033[0m'

SCRIPT_DIR=.
cd "$SCRIPT_DIR" || exit

VERSION=$(cat VERSION 2>/dev/null || echo "1.3.0")
LOG_FILE="$SCRIPT_DIR/scripts/start_script.log"
LOG_ENABLED=true

log() {
    if $LOG_ENABLED; then
        echo -e "$1" | tee -a "$LOG_FILE"
    else
        echo -e "$1"
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

clean_logs() {
    if [ -f "$LOG_FILE" ]; then
        log "${YELLOW}🧹 Cleaning old logs...${RESET}"
        rm "$LOG_FILE"
    fi
}

usage() {
    log "${YELLOW}Usage: $0 [-s|--start <environment>] [-b|--build] [-x|--stop] [-r|--restart] [-t|--status] [-c|--clean] [-l|--nolog] [-h|--help] [-v|--version]${RESET}"
    log "  -s, --start <environment>  Start the environment (e.g., development, production)"
    log "  -b, --build                Force rebuild of Docker images"
    log "  -x, --stop                 Stop the running Docker Compose environment"
    log "  -r, --restart              Restart the Docker Compose environment"
    log "  -t, --status               Show the status of Docker containers"
    log "  -c, --clean                Clean old logs"
    log "  -l, --nolog                Disable log output"
    log "  -h, --help                 Show this help message"
    log "  -v, --version              Show the version"
    exit 1
}

manage_env() {
    local ACTION=$1
    local ENV=$2
    local COMPOSE_FILE="$SCRIPT_DIR/docker-compose-$ENV.yml"
    if [ ! -f "$COMPOSE_FILE" ]; then
        log "${RED}❌ Docker Compose file not found: $COMPOSE_FILE${RESET}"
        exit 1
    fi
    local CMD="docker compose -f $COMPOSE_FILE $ACTION"
    log "${YELLOW}🔧 $ACTION environment: $ENV${RESET}"
    eval "$CMD"
}

start_env() {
    local ENV=$1
    local BUILD=$2
    log "${GREEN}🚀 Starting environment: $ENV${RESET}"

    if ! [ -x "$SCRIPT_DIR/scripts/init_env.sh" ]; then
        log "${RED}❌ Initialization script not found or not executable. Exiting...${RESET}"
        exit 1
    fi

    if ! "$SCRIPT_DIR/scripts/init_env.sh" "$ENV"; then
        log "${RED}❌ Initialization failed. Exiting...${RESET}"
        exit 1
    fi

    local COMPOSE_FILE="$SCRIPT_DIR/docker-compose-$ENV.yml"
    local CMD="docker compose -f ./docker-compose-base.yml -f $COMPOSE_FILE --env-file .env.$ENV up -d"
    if [ "$BUILD" == "true" ]; then
        CMD+=" --build"
        log "${YELLOW}🔄 Rebuilding Docker images...${RESET}"
    fi
    log "${GREEN}🐳 Starting Docker Compose...${RESET}"
    eval "$CMD" | tee -a "$LOG_FILE"
}

BUILD=false
STOP=false
RESTART=false
STATUS=false
CLEAN=false

check_docker

while [[ $# -gt 0 ]]; do
    case "$1" in
        -s|--start)
            shift
            ENV=$1
            shift
            ;;
        -b|--build)
            BUILD=true
            shift
            ;;
        -x|--stop)
            STOP=true
            shift
            ;;
        -r|--restart)
            RESTART=true
            shift
            ;;
        -t|--status)
            STATUS=true
            shift
            ;;
        -c|--clean)
            CLEAN=true
            shift
            ;;
        -l|--nolog)
            LOG_ENABLED=false
            shift
            ;;
        -h|--help)
            usage
            ;;
        -v|--version)
            log "${GREEN}Version: $VERSION${RESET}"
            exit 0
            ;;
        *)
            log "${RED}❌ Unknown option: $1${RESET}"
            usage
            ;;
    esac
done

if $CLEAN; then
    clean_logs
fi

if $STOP; then
    manage_env "down" "$ENV"
elif $RESTART; then
    manage_env "restart" "$ENV"
elif $STATUS; then
    manage_env "ps" "$ENV"
elif [[ -n "$ENV" ]]; then
    start_env "$ENV" "$BUILD"
else
    log "${RED}❌ No environment specified. Use -h or --help for usage.${RESET}"
    exit 1
fi