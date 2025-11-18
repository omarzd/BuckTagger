import { defineConfig } from 'vite';
import { resolve } from 'path';

export default defineConfig({
  build: {
    lib: {
      entry: resolve(__dirname, 'src/index.ts'),
      name: 'BuckTagger',
      formats: ['es', 'umd'],
      fileName: (format) => `buck-tagger.${format}.js`
    },
    rollupOptions: {
      output: {
        assetFileNames: 'buck-tagger.[ext]'
      }
    }
  },
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  }
});
