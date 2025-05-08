#!/bin/bash

create_env_file() {
    local env=$1
    local env_file=".env.$env"
    local DB_PASSWORD

    if [ -f "$env_file" ]; then
        echo "⚠️  $env_file already exists, skipping environment init."
        exit 0
    fi

    read -r -p "Enter new database password for $env_file (generate): " DB_PASSWORD

    if [ -z "$DB_PASSWORD" ]; then
        if command -v openssl &> /dev/null; then
            DB_PASSWORD=$(openssl rand -base64 12)
            echo "🔑  Password generated with openssl: $DB_PASSWORD"
        else
            echo "❌  OpenSSL not found on your OS. Please enter a password manually."

            while [ -z "$DB_PASSWORD" ]; do
                read -r -sp "Enter password: " DB_PASSWORD
                echo
                if [ -z "$DB_PASSWORD" ]; then
                    echo "❗️  Password cannot be empty. Please enter a valid password."
                fi
            done
        fi
    fi

    cat << EOF > "$env_file"
DB_HOST: db
DB_USER: PQUser
DB_PASSWORD: $DB_PASSWORD
DB_NAME: GRDB_$env
DB_PORT: 5432

BACKEND_PORT=8080
FRONTEND_PORT=3000

SPRING_PROFILE=$env
EOF

    echo "✅  $env_file was created with the following content:"
    cat "$env_file"
}

if [ -z "$1" ]; then
    echo "❌  You must specify an environment (e.g., development or production)"
    exit 1
fi

case "$1" in
    "development")
        create_env_file "development"
        ;;
    "production")
        create_env_file "production"
        ;;
    *)
        echo "❌  Invalid environment specified. Use 'development' or 'production'."
        exit 1
        ;;
esac
