#!/bin/bash

# One-click launch script for BuckTagger Modern
# This script will install dependencies, build, and open the app in your browser

set -e

echo "========================================="
echo "  BuckTagger Modern - Quick Launch"
echo "========================================="
echo ""

cd "$(dirname "$0")/buck-tagger-modern"

# Check if node_modules exists
if [ ! -d "node_modules" ]; then
    echo "Installing dependencies..."
    npm install
    echo ""
fi

echo "Starting development server..."
echo ""
echo "The app will open in your browser automatically."
echo "If it doesn't, visit: http://localhost:5173"
echo ""
echo "Press Ctrl+C to stop the server"
echo ""

# Start the dev server and open browser
npm run dev -- --open
