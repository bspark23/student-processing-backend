#!/bin/bash

echo "🚀 Deploying to Fly.io..."

# Check if fly CLI is installed
if ! command -v fly &> /dev/null; then
    echo "❌ Fly CLI not found. Please install it first:"
    echo "   curl -L https://fly.io/install.sh | sh"
    exit 1
fi

# Check if user is logged in
if ! fly auth whoami &> /dev/null; then
    echo "❌ Not logged in to Fly.io. Please run: fly auth login"
    exit 1
fi

# Check if app exists
if [ ! -f "fly.toml" ]; then
    echo "📋 Initializing Fly app..."
    fly launch --no-deploy
else
    echo "✅ Fly app configuration found"
fi

# Deploy
echo "🚀 Deploying application..."
fly deploy

# Check deployment status
echo "📊 Checking deployment status..."
fly status

echo "✅ Deployment complete!"
echo "🌐 Your API is available at: https://$(fly info --json | jq -r '.Hostname')/api"
echo "🏥 Health check: https://$(fly info --json | jq -r '.Hostname')/api/health"