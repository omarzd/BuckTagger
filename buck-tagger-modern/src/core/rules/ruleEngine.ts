/**
 * Rule Engine - Converts Drools decision table logic to JavaScript
 * Handles both primary and secondary tag classification
 */

import type { ArabicStem } from '../ArabicStem';
import { primaryTagRules } from './primaryTags';
import { secondaryTagRules } from './secondaryTags';

export interface Rule {
  name: string;
  conditions: (stem: ArabicStem) => boolean;
  action: (stem: ArabicStem) => void;
}

/**
 * Execute primary tag rules to determine the main morphological classification
 */
export function executePrimaryTagRules(stem: ArabicStem): void {
  for (const rule of primaryTagRules) {
    if (rule.conditions(stem)) {
      rule.action(stem);
      // In Drools, once a primary tag is set, we typically stop
      // But multiple rules might match, so we continue
    }
  }
}

/**
 * Execute secondary tag rules to generate derived forms
 */
export function executeSecondaryTagRules(stem: ArabicStem): void {
  // Clear any existing secondary tags
  stem.clearSecondaryTags();

  for (const rule of secondaryTagRules) {
    if (rule.conditions(stem)) {
      rule.action(stem);
      // Multiple secondary tags can apply, so we continue
    }
  }
}

/**
 * Main analysis function - performs two-pass rule execution
 * First pass: Determine primary tag
 * Second pass: Generate secondary tags based on primary tag
 */
export function analyze(stem: ArabicStem): void {
  // First pass: Primary tags
  executePrimaryTagRules(stem);

  // Second pass: Secondary tags
  executeSecondaryTagRules(stem);
}
