/**
 * BuckTagger - Modern Arabic Morphological Analyzer
 * Embeddable Web Component
 *
 * Usage:
 *   <script type="module" src="buck-tagger.es.js"></script>
 *   <buck-tagger></buck-tagger>
 *
 * @license GPL-3.0
 * @author Omar Alzuhaibi (original), Modernized 2025
 */

export { BuckTaggerElement } from './components/BuckTagger';
export { ArabicStem } from './core/ArabicStem';
export { analyze } from './core/rules/ruleEngine';
export * from './core/utils/arabic';
export type * from './types';

// Auto-register the custom element when imported
import './components/BuckTagger';
