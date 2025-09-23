@echo off
echo 🚀 Deploying to Fly.io...

REM Check if fly CLI is installed
fly version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Fly CLI not found. Please install it first:
    echo    iwr https://fly.io/install.ps1 -useb ^| iex
    pause
    exit /b 1
)

REM Check if user is logged in
fly auth whoami >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Not logged in to Fly.io. Please run: fly auth login
    pause
    exit /b 1
)

REM Check if app exists
if not exist "fly.toml" (
    echo 📋 Initializing Fly app...
    fly launch --no-deploy
) else (
    echo ✅ Fly app configuration found
)

REM Deploy
echo 🚀 Deploying application...
fly deploy

REM Check deployment status
echo 📊 Checking deployment status...
fly status

echo ✅ Deployment complete!
echo 🌐 Your API is available at: https://your-app-name.fly.dev/api
echo 🏥 Health check: https://your-app-name.fly.dev/api/health

pause