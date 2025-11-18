/**
 * Type definitions for BuckTagger
 */

/**
 * Morphological tag with its associated stem
 */
export interface TagStem {
  tag: string;
  stem: string;
  diacritizedStem: string;
}

/**
 * Stem class: noun, verb, particle
 */
export type StemClass = 'noun' | 'verb' | 'particle';

/**
 * Stem subclass for verbs
 */
export type VerbSubClass = 'perfect' | 'imperfect' | 'imperative';

/**
 * Result from morphological analysis
 */
export interface AnalysisResult {
  primaryTag: string;
  primaryStem: string;
  secondaryTags: TagStem[];
}

/**
 * Morphological features of an Arabic stem
 */
export interface MorphologicalFeatures {
  // General features
  stemClass: StemClass;
  stemSubClass?: VerbSubClass;

  // Verb-specific features
  isTransitive: boolean;
  transitivity: number; // 0 = intransitive, 1 = tr to one obj, 2 = tr to two objs
  isPassive: boolean;

  // Phonological features
  isImpYa: boolean; // Is imperfective letter open (fatha)?
  isFirstHamza: boolean; // Is first root letter hamza?
  isBeforeLastDblOrVowel: boolean; // Is before-last letter doubled or vowel?
  isBeforeLastHamza: boolean; // Is before-last letter hamza?
}
