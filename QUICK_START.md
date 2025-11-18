# BuckTagger - Quick Launch Guide

## One-Click Launch Options

### Option 1: Launch Script (Recommended)

**Linux/Mac:**
```bash
./launch.sh
```

**Windows:**
```bash
launch.bat
```

The script will:
1. Install dependencies automatically (if needed)
2. Start the development server
3. Open your browser to http://localhost:5173

### Option 2: Manual Launch

```bash
cd buck-tagger-modern
npm install    # Only needed first time
npm start      # Opens browser automatically
```

### Option 3: NPM Start

```bash
cd buck-tagger-modern
npm start
```

## What You'll See

Once launched, you'll see the BuckTagger interface where you can:
- Enter Arabic words with diacritics
- Automatically analyze morphological features
- View primary and secondary tags
- Try the example words provided

## Stopping the Server

Press `Ctrl+C` in the terminal to stop the development server.

## Troubleshooting

**Port already in use?**
```bash
# Kill the process on port 5173
kill -9 $(lsof -t -i:5173)
```

**Dependencies not installing?**
```bash
cd buck-tagger-modern
rm -rf node_modules package-lock.json
npm install
```

**Browser doesn't open automatically?**

Manually visit: http://localhost:5173

---

Enjoy using BuckTagger!
