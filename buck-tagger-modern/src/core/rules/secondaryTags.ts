/**
 * Secondary Tag Rules
 * Generate derived forms and inflections based on primary tag
 *
 * Secondary tags represent modified versions of the word (different inflections,
 * moods, etc.) that fall under the same primary tag family.
 */

import type { Rule } from './ruleEngine';
import { removeDiacritics } from '../utils/arabic';

export const secondaryTagRules: Rule[] = [
  // IV_Ann - Nunation form (التنوين)
  {
    name: 'IV_Ann',
    conditions: (stem) => {
      const primaryTag = stem.getPrimaryTag();
      const features = stem.getFeatures();
      return (
        primaryTag === 'IV_0' &&
        features.stemSubClass === 'imperfect' &&
        features.isBeforeLastDblOrVowel
      );
    },
    action: (stem) => {
      // For nunation, last vowel is typically converted to ي
      stem.replaceLast('ي');
      const newStem = stem.getModifiedStem();
      const diacritized = stem.restoreDiacritics();
      stem.addSecondaryTag('IV_Ann', removeDiacritics(newStem), diacritized);
    }
  },

  // IV_0hwnyn - Doubled form weakness (الإدغام)
  {
    name: 'IV_0hwnyn',
    conditions: (stem) => {
      const primaryTag = stem.getPrimaryTag();
      const features = stem.getFeatures();
      return (
        primaryTag === 'IV_0' &&
        features.stemSubClass === 'imperfect' &&
        features.isBeforeLastDblOrVowel
      );
    },
    action: (stem) => {
      // For doubled weakness, remove last letter
      stem.replaceLast('');
      const newStem = stem.getModifiedStem();
      const diacritized = stem.restoreDiacritics();
      stem.addSecondaryTag('IV_0hwnyn', removeDiacritics(newStem), diacritized);
    }
  },

  // IV_h - Alif/ha ending form
  {
    name: 'IV_h',
    conditions: (stem) => {
      const primaryTag = stem.getPrimaryTag();
      const features = stem.getFeatures();
      return (
        primaryTag === 'IV_0' &&
        features.stemSubClass === 'imperfect' &&
        stem.getLast() === 'ى'
      );
    },
    action: (stem) => {
      // Replace last letter with alif
      stem.replaceLast('ا');
      const newStem = stem.getModifiedStem();
      const diacritized = stem.restoreDiacritics();
      stem.addSecondaryTag('IV_h', removeDiacritics(newStem), diacritized);
    }
  },

  // IV_y - Ya ending (for weak verbs)
  {
    name: 'IV_y',
    conditions: (stem) => {
      const primaryTag = stem.getPrimaryTag();
      const features = stem.getFeatures();
      const last = stem.getLast();
      return (
        primaryTag === 'IV_0' &&
        features.stemSubClass === 'imperfect' &&
        features.isBeforeLastDblOrVowel &&
        last === 'ى'
      );
    },
    action: (stem) => {
      // Keep as is but mark as different inflection
      const newStem = stem.getModifiedStem();
      const diacritized = stem.restoreDiacritics();
      stem.addSecondaryTag('IV_y', removeDiacritics(newStem), diacritized);
    }
  },

  // IV_wA - Waw-Alif ending (for hollow verbs)
  {
    name: 'IV_wA',
    conditions: (stem) => {
      const primaryTag = stem.getPrimaryTag();
      const features = stem.getFeatures();
      const beforeLast = stem.getBeforeLast();
      return (
        primaryTag === 'IV_0' &&
        features.stemSubClass === 'imperfect' &&
        (beforeLast === 'و' || beforeLast === 'ا')
      );
    },
    action: (stem) => {
      // Replace before-last with long vowel
      stem.replaceBeforeLast('ا');
      const newStem = stem.getModifiedStem();
      const diacritized = stem.restoreDiacritics();
      stem.addSecondaryTag('IV_wA', removeDiacritics(newStem), diacritized);
    }
  }
];
