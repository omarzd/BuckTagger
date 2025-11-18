@echo off
REM One-click launch script for BuckTagger Modern (Windows)
REM This script will install dependencies, build, and open the app in your browser

echo =========================================
echo   BuckTagger Modern - Quick Launch
echo =========================================
echo.

cd "%~dp0buck-tagger-modern"

REM Check if node_modules exists
if not exist "node_modules\" (
    echo Installing dependencies...
    call npm install
    echo.
)

echo Starting development server...
echo.
echo The app will open in your browser automatically.
echo If it doesn't, visit: http://localhost:5173
echo.
echo Press Ctrl+C to stop the server
echo.

REM Start the dev server and open browser
call npm run dev -- --open
