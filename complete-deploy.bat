@echo off
echo 🚀 Complete Fly.io Deployment Script
echo =====================================
echo.

REM Check if fly CLI is installed
fly version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Fly CLI not found!
    echo Please run install-fly-cli.bat first, then restart your terminal.
    pause
    exit /b 1
)

echo ✅ Fly CLI found
echo.

REM Check if user is logged in
fly auth whoami >nul 2>&1
if %errorlevel% neq 0 (
    echo 🔐 Please login to Fly.io first...
    fly auth login
    if %errorlevel% neq 0 (
        echo ❌ Login failed
        pause
        exit /b 1
    )
)

echo ✅ Logged in to Fly.io
echo.

REM Step 1: Launch app (if not exists)
if not exist "fly.toml" (
    echo 📋 Step 1: Launching new Fly.io app...
    echo.
    echo Please choose:
    echo - App name: student-processing-backend (or your preferred name)
    echo - Region: iad (US East) - recommended
    echo - Don't deploy yet when asked
    echo.
    pause
    fly launch --no-deploy
    if %errorlevel% neq 0 (
        echo ❌ App launch failed
        pause
        exit /b 1
    )
) else (
    echo ✅ Fly.toml already exists
)

echo.
echo 📊 Step 2: Creating PostgreSQL database...
echo.
echo Creating database: student-processing-db
fly postgres create --name student-processing-db --region iad
if %errorlevel% neq 0 (
    echo ⚠️  Database creation failed or already exists
    echo This might be okay if the database already exists
)

echo.
echo 🔗 Step 3: Attaching database to app...
echo.
REM Get app name from fly.toml
for /f "tokens=2 delims== " %%a in ('findstr "app =" fly.toml') do set APP_NAME=%%a
set APP_NAME=%APP_NAME:"=%

echo Attaching database to app: %APP_NAME%
fly postgres attach --app %APP_NAME% student-processing-db
if %errorlevel% neq 0 (
    echo ⚠️  Database attachment failed or already attached
    echo This might be okay if already attached
)

echo.
echo 🚀 Step 4: Deploying application...
echo.
fly deploy
if %errorlevel% neq 0 (
    echo ❌ Deployment failed
    pause
    exit /b 1
)

echo.
echo ✅ Deployment successful!
echo.

REM Get the app URL
for /f "tokens=2 delims== " %%a in ('findstr "app =" fly.toml') do set APP_NAME=%%a
set APP_NAME=%APP_NAME:"=%
set APP_URL=https://%APP_NAME%.fly.dev

echo 🌐 Your API is now live at: %APP_URL%/api
echo 🏥 Health check: %APP_URL%/api/health
echo.

echo 📊 Checking deployment status...
fly status

echo.
echo 🎉 Backend deployment complete!
echo.
echo Next steps:
echo 1. Test your API: %APP_URL%/api/health
echo 2. Update frontend environment.prod.ts with: %APP_URL%/api
echo 3. Deploy frontend to Netlify
echo.

pause