/**
 * ArabicStem - Main data model for Arabic word stems
 * Ported from VocStem.java
 */

import {
  removeDiacritics,
  storeDiacritics,
  restoreDiacritics,
  isVowelLetter,
  isHamza,
  FATHA,
  DAMMA,
  ALIF,
  SHADDA
} from './utils/arabic';
import type { MorphologicalFeatures, TagStem, StemClass, VerbSubClass } from '../types';

export class ArabicStem {
  // Instance variables
  private stem: string;
  private features: MorphologicalFeatures;
  private primaryTag: string | null = null;
  private modifiedStem: string;
  private diacritics: string[] = [];
  private secondaryTagStems: TagStem[] = [];

  constructor(stem: string) {
    this.stem = stem;
    this.modifiedStem = removeDiacritics(stem);

    // Initialize features with estimates
    this.features = {
      stemClass: this.estimateStemClass(),
      stemSubClass: this.estimateStemSubClass(),
      isTransitive: false,
      transitivity: this.estimateTransitivity(),
      isPassive: this.estimateIsPassive(),
      isImpYa: this.estimateIsImpYa(),
      isFirstHamza: this.estimateIsFirstHamza(),
      isBeforeLastDblOrVowel: this.estimateIsBeforeLastDblOrVowel(),
      isBeforeLastHamza: this.estimateIsBeforeLastHamza()
    };

    this.features.isTransitive = this.features.transitivity > 0;
    this.diacritics = storeDiacritics(stem);
  }

  // Getters
  getStem(): string {
    return this.stem;
  }

  getUnvocalized(): string {
    return removeDiacritics(this.stem);
  }

  getFeatures(): MorphologicalFeatures {
    return { ...this.features };
  }

  getPrimaryTag(): string | null {
    return this.primaryTag;
  }

  getSecondaryTagStems(): TagStem[] {
    return [...this.secondaryTagStems];
  }

  getModifiedStem(): string {
    return this.modifiedStem;
  }

  // Setters
  setPrimaryTag(tag: string): void {
    this.primaryTag = tag;
  }

  setFeature<K extends keyof MorphologicalFeatures>(
    feature: K,
    value: MorphologicalFeatures[K]
  ): void {
    this.features[feature] = value;
  }

  addSecondaryTag(tag: string, stem: string, diacritizedStem: string): void {
    this.secondaryTagStems.push({ tag, stem, diacritizedStem });
  }

  clearSecondaryTags(): void {
    this.secondaryTagStems = [];
  }

  // Morphological feature estimators
  private estimateStemClass(): StemClass {
    // TODO: Needs morphological analysis or POS tagging
    return 'verb';
  }

  private estimateStemSubClass(): VerbSubClass {
    // TODO: Needs morphological analysis or POS tagging
    return 'imperfect';
  }

  private estimateIsBeforeLastDblOrVowel(): boolean {
    return this.isBeforeLastDoubled() || this.isBeforeLastVowel();
  }

  private estimateIsBeforeLastHamza(): boolean {
    const beforeLast = this.getBeforeLast();
    return isHamza(beforeLast);
  }

  private estimateIsFirstHamza(): boolean {
    const firstOriginal = this.getFirstOriginal();
    return isHamza(firstOriginal);
  }

  private estimateIsImpYa(): boolean {
    return this.isImpDiac(FATHA);
  }

  private estimateIsPassive(): boolean {
    // Imperfect verb is passive if:
    // - Imperfective letter has damma AND
    // - Before-last has fatha OR is alif
    return (
      this.isImpDiac(DAMMA) &&
      (this.isBeforeLastDiac(FATHA) || this.getBeforeLast() === ALIF)
    );
  }

  private estimateTransitivity(): number {
    const length = this.getUnvocalized().length;

    if (this.isImpDiac(DAMMA) && !this.estimateIsPassive()) {
      return 1;
    } else if (length > 4) {
      return 0;
    } else {
      return 1;
    }
  }

  // Helper methods
  getLast(): string {
    const unvoc = this.getUnvocalized();
    return unvoc.charAt(unvoc.length - 1);
  }

  getBeforeLast(): string {
    const unvoc = this.getUnvocalized();
    return unvoc.charAt(unvoc.length - 2);
  }

  getFirstOriginal(): string {
    // Assuming first original letter is at index 1 (after imperfective letter)
    const unvoc = this.getUnvocalized();
    return unvoc.charAt(1) || '';
  }

  isLastDiac(diac: string): boolean {
    // Check if last letter has the specified diacritic
    const regex = new RegExp(`.+${diac}[ًٌٍَُِ]?$`);
    return regex.test(this.stem);
  }

  isBeforeLastDiac(diac: string): boolean {
    // Check if before-last letter has the specified diacritic
    const regex = new RegExp(`.+${diac}[ء-ي][ّ]?[ًٌٍَُِْ]?$`);
    return regex.test(this.stem);
  }

  isImpDiac(diac: string): boolean {
    // Check if imperfective letter has the specified diacritic
    const regex = new RegExp(`.${diac}.*`);
    return regex.test(this.stem);
  }

  isBeforeLastDoubled(): boolean {
    const length = this.getUnvocalized().length;

    if (this.isLastDiac(SHADDA)) {
      return true;
    }

    // Guessing logic
    if (length === 3) {
      return true;
    } else if (length >= 6) {
      return false;
    } else {
      return Math.random() >= 0.95;
    }
  }

  isBeforeLastVowel(): boolean {
    return isVowelLetter(this.getBeforeLast());
  }

  isLastHamza(): boolean {
    return isHamza(this.getLast());
  }

  // String manipulation methods for secondary tags
  replaceFirst(replacement: string): void {
    this.diacritics = storeDiacritics(this.stem);
    const unvoc = this.getUnvocalized();
    const length = unvoc.length;

    if (length > 2) {
      this.modifiedStem = unvoc.charAt(0) + replacement + unvoc.substring(2);
    } else {
      this.modifiedStem = unvoc.charAt(0) + replacement;
    }

    if (replacement === '') {
      this.diacritics.splice(0, 1);
    }
  }

  replaceLast(replacement: string): void {
    this.diacritics = storeDiacritics(this.stem);
    const unvoc = this.getUnvocalized();
    const length = unvoc.length;

    this.modifiedStem = unvoc.substring(0, length - 1) + replacement;

    if (replacement === '') {
      this.diacritics.pop();
    }
  }

  replaceBeforeLast(replacement: string): void {
    this.diacritics = storeDiacritics(this.stem);
    const unvoc = this.getUnvocalized();
    const length = unvoc.length;

    if (length > 2) {
      this.modifiedStem = unvoc.substring(0, length - 2) + replacement + unvoc.charAt(length - 1);
    } else {
      this.modifiedStem = replacement + unvoc.charAt(1);
    }

    if (replacement === '') {
      this.diacritics.splice(this.diacritics.length - 2, 1);
    }
  }

  restoreDiacritics(): string {
    return restoreDiacritics(this.modifiedStem, this.diacritics);
  }
}
