# BuckTagger Modern - ÙˆØØ³Ù…

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

Modern Arabic morphological analyzer as an embeddable web component. Classifies Arabic words into Buckwalter morphological categories.

##  Features

-  **Accurate Morphological Analysis** - Rule-based classification using Buckwalter tagset
-  **Zero Dependencies** - Pure TypeScript, no runtime dependencies
-  **Universal Embedding** - Works with any framework or no framework
-  **Responsive Design** - Works on desktop and mobile
-  **Dark Mode Support** - Automatic dark mode detection
-  **RTL Support** - Proper Right-to-Left text handling
-  **Customizable** - CSS custom properties for theming

##  Quick Start

### CDN (Easiest)

```html
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
</head>
<body>
  <buck-tagger></buck-tagger>

  <script type="module">
    import 'https://unpkg.com/buck-tagger/dist/buck-tagger.es.js';
  </script>
</body>
</html>
```

### NPM Installation

```bash
npm install buck-tagger
```

```javascript
import 'buck-tagger';

// Now you can use <buck-tagger></buck-tagger> in your HTML
```

### Local Development

```bash
# Install dependencies
npm install

# Start dev server
npm run dev

# Build for production
npm run build
```

##  Usage

### Basic Usage

Simply add the custom element to your HTML:

```html
<buck-tagger></buck-tagger>
```

### Programmatic Usage

```javascript
import { ArabicStem, analyze } from 'buck-tagger';

// Create a stem
const stem = new ArabicStem('ÙŠÙØ®Ø´ÙÙ‰');

// Analyze it
analyze(stem);

// Get results
console.log(stem.getPrimaryTag()); // "IV_0"
console.log(stem.getSecondaryTagStems()); // Array of secondary tags
```

### React Integration

```jsx
import 'buck-tagger';

function App() {
  return (
    <div>
      <h1>My Arabic App</h1>
      <buck-tagger></buck-tagger>
    </div>
  );
}
```

### Vue Integration

```vue
<template>
  <div>
    <h1>My Arabic App</h1>
    <buck-tagger></buck-tagger>
  </div>
</template>

<script>
import 'buck-tagger';

export default {
  name: 'App'
}
</script>
```

##  Customization

The component uses CSS custom properties for easy theming:

```css
buck-tagger {
  --bt-primary: #2563eb;
  --bt-primary-hover: #1d4ed8;
  --bt-bg: #ffffff;
  --bt-text: #1e293b;
  --bt-radius: 8px;
  --bt-font-arabic: 'Amiri', 'Traditional Arabic', serif;
}
```

##  How It Works

BuckTagger analyzes Arabic words (primarily imperfect verbs) using morphological rules:

1. **Input**: User enters an Arabic word with diacritics
2. **Feature Estimation**: Automatically detects morphological features:
   - Transitivity
   - Passive voice
   - Hamza positions
   - Weak/doubled letters
3. **Rule Engine**: Applies Buckwalter classification rules
4. **Output**: Returns primary tag and secondary (derived) forms

### Example Analysis

Input: `ÙŠÙØ®Ø´ÙÙ‰`

Output:
- **Primary Tag**: `IV_0: Ø®Ø´Ù‰`
- **Secondary Tags**:
  - `IV_Ann: Ø®Ø´ÙŠ`
  - `IV_0hwnyn: Ø®Ø´`
  - `IV_h: Ø®Ø´Ø`

##  Architecture

```
src/
 core/
‚    ArabicStem.ts          # Main data model
‚    rules/
‚   ‚    ruleEngine.ts      # Rule execution engine
‚   ‚    primaryTags.ts     # Primary tag rules
‚   ‚    secondaryTags.ts   # Secondary tag rules
‚    utils/
‚        arabic.ts          # Arabic text utilities
 components/
‚    BuckTagger.ts          # Web Component
‚    BuckTagger.css         # Styles
 types/
     index.ts               # TypeScript definitions
```

##  Morphological Features

### Primary Tags (IV - Imperfect Verbs)

- `IV_0` - Basic imperfect verb
- `IV_Pass` - Passive voice
- More tags for different morphological patterns

### Secondary Tags

- `IV_Ann` - Nunation form
- `IV_0hwnyn` - Doubled weakness
- `IV_h` - Alif/ha ending
- `IV_y` - Ya ending
- `IV_wA` - Waw-Alif form

##  Testing

Try these example words:

- `ÙŠÙÙ…Ø¶ÙÙŠ` - to go
- `ÙŠÙØØ¹Ù‰` - to shepherd
- `ÙŠÙØ³ØØ¹Ù…Ù` - to use
- `ÙŠÙØØ°Ù†` - to announce
- `ÙŠÙØ®Ø´Ù‰` - to fear
- `ÙŠÙØØ°ÙÙ†` - to permit
- `ÙŠÙØØ°ÙÙ†` - to be announced

##  Bundle Size

- Minified: ~15KB
- Gzipped: ~5KB

##  Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

##  License

GPL-3.0 License - see [LICENSE](../LICENSE) file for details.

This project is a modernization of the original BuckTagger by Omar Alzuhaibi and Ahmed Aman (2012).

##  Credits

- **Original Authors**: Omar Alzuhaibi, Ahmed Aman
- **Original Project**: [BuckTagger](https://github.com/omarzd/BuckTagger)
- **Based on**: Buckwalter's Arabic Morphological Analyzer (BAMA)
- **Modernization**: 2025

##  References

- [Buckwalter Arabic Morphological Analyzer](https://catalog.ldc.upenn.edu/LDC2004L02)
- [Original BuckTagger Technical Report](../BuckTaggerTechnicalReportArabic.pdf) (Arabic)

##  Issues

Found a bug? Please [create an issue](https://github.com/omarzd/BuckTagger/issues).

---

Made with  for Arabic NLP
