/**
 * Primary Tag Rules
 * Based on Buckwalter morphological categories for Imperfect Verbs (IV)
 *
 * These rules classify the input word into its primary morphological tag.
 * Rules are based on the Drools decision table (BuckTagsRules.xls)
 */

import type { Rule } from './ruleEngine';

export const primaryTagRules: Rule[] = [
  // IV_0 - Basic imperfect verb, most common form
  {
    name: 'IV_0',
    conditions: (stem) => {
      const features = stem.getFeatures();
      return (
        features.stemSubClass === 'imperfect' &&
        !features.isPassive &&
        !features.isBeforeLastDblOrVowel &&
        !features.isBeforeLastHamza &&
        !features.isFirstHamza
      );
    },
    action: (stem) => {
      stem.setPrimaryTag('IV_0');
    }
  },

  // IV_Pass - Passive imperfect verb
  {
    name: 'IV_Pass',
    conditions: (stem) => {
      const features = stem.getFeatures();
      return (
        features.stemSubClass === 'imperfect' &&
        features.isPassive &&
        !features.isBeforeLastDblOrVowel &&
        !features.isBeforeLastHamza &&
        !features.isFirstHamza
      );
    },
    action: (stem) => {
      stem.setPrimaryTag('IV_Pass');
    }
  },

  // IV_Hwn - Imperfect verb with weak before-last letter (doubled or vowel)
  {
    name: 'IV_Hwn',
    conditions: (stem) => {
      const features = stem.getFeatures();
      return (
        features.stemSubClass === 'imperfect' &&
        !features.isPassive &&
        features.isBeforeLastDblOrVowel &&
        !features.isBeforeLastHamza &&
        !features.isFirstHamza
      );
    },
    action: (stem) => {
      stem.setPrimaryTag('IV_0');
    }
  },

  // IV with hamza in before-last position
  {
    name: 'IV_BeforeLastHamza',
    conditions: (stem) => {
      const features = stem.getFeatures();
      return (
        features.stemSubClass === 'imperfect' &&
        !features.isPassive &&
        features.isBeforeLastHamza &&
        !features.isFirstHamza
      );
    },
    action: (stem) => {
      stem.setPrimaryTag('IV_0');
    }
  },

  // IV with hamza in first position
  {
    name: 'IV_FirstHamza',
    conditions: (stem) => {
      const features = stem.getFeatures();
      return (
        features.stemSubClass === 'imperfect' &&
        !features.isPassive &&
        features.isFirstHamza &&
        !features.isBeforeLastDblOrVowel
      );
    },
    action: (stem) => {
      stem.setPrimaryTag('IV_0');
    }
  },

  // IV with both hamza and weak letter
  {
    name: 'IV_FirstHamza_Hwn',
    conditions: (stem) => {
      const features = stem.getFeatures();
      return (
        features.stemSubClass === 'imperfect' &&
        !features.isPassive &&
        features.isFirstHamza &&
        features.isBeforeLastDblOrVowel
      );
    },
    action: (stem) => {
      stem.setPrimaryTag('IV_0');
    }
  },

  // Default fallback for imperfect verbs
  {
    name: 'IV_Default',
    conditions: (stem) => {
      const features = stem.getFeatures();
      return features.stemSubClass === 'imperfect';
    },
    action: (stem) => {
      if (!stem.getPrimaryTag()) {
        stem.setPrimaryTag('IV_0');
      }
    }
  }
];
