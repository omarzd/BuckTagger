/**
 * Arabic Text Utilities
 * Ported from VocStem.java
 */

// Arabic diacritics and characters
export const FATHA = 'َ';
export const DAMMA = 'ُ';
export const KASRA = 'ِ';
export const SHADDA = 'ّ';
export const SUKUN = 'ْ';
export const TANWEEN_FATH = 'ً';
export const TANWEEN_DAM = 'ٌ';
export const TANWEEN_KASR = 'ٍ';
export const ALIF = 'ا';

// Diacritics array and string
export const DIACRITICS = [FATHA, TANWEEN_FATH, DAMMA, TANWEEN_DAM, KASRA, TANWEEN_KASR, SHADDA, SUKUN];
export const DIACRITICS_STR = 'ًٌٍَُِّْ';
export const DIACRITICS_REGEX = /[ًٌٍَُِّْ]/g;

// Imperfective letters (حروف المضارعة)
export const IMP_LETTERS = ['ت', 'ي', 'ن', 'أ'];

// Vowel letters
export const VOWEL_LETTERS = ['ا', 'و', 'ي'];

// Hamza variations
export const HAMZA_CHARS = ['ء', 'أ', 'إ', 'آ', 'ؤ', 'ئ'];

/**
 * Remove all diacritics from Arabic text
 */
export function removeDiacritics(text: string): string {
  return text.replace(DIACRITICS_REGEX, '');
}

/**
 * Check if a character is a diacritic
 */
export function isDiacritic(char: string): boolean {
  return DIACRITICS_STR.includes(char);
}

/**
 * Check if a character is a vowel letter
 */
export function isVowelLetter(char: string): boolean {
  return VOWEL_LETTERS.includes(char);
}

/**
 * Check if a character is a hamza (any variation)
 */
export function isHamza(char: string): boolean {
  return HAMZA_CHARS.some(h => char.includes(h));
}

/**
 * Check if a character is an imperfective letter
 */
export function isImperfectiveLetter(char: string): boolean {
  return IMP_LETTERS.includes(char);
}

/**
 * Remove the imperfective letter from the beginning of the word
 * Assumes the word is an imperfect verb
 */
export function removeImperfectiveLetter(word: string): string {
  let result = word;

  for (const letter of IMP_LETTERS) {
    if (result.startsWith(letter)) {
      result = result.substring(1);
      break;
    }
  }

  // Remove fatha or damma if it follows
  if (result.charAt(0) === FATHA || result.charAt(0) === DAMMA) {
    result = result.substring(1);
  }

  return result;
}

/**
 * Store diacritics positions from vocalized text
 * Returns an array where each index corresponds to a letter in the unvocalized text
 */
export function storeDiacritics(text: string): string[] {
  const unvocalized = removeDiacritics(text);
  const diacritics: string[] = new Array(unvocalized.length).fill('');

  let pos = -1;
  for (let i = 0; i < text.length; i++) {
    const char = text.charAt(i);
    if (isDiacritic(char)) {
      diacritics[pos] += char;
    } else {
      pos++;
    }
  }

  return diacritics;
}

/**
 * Restore diacritics to unvocalized text
 */
export function restoreDiacritics(unvocalized: string, diacritics: string[]): string {
  let result = '';
  for (let i = 0; i < unvocalized.length; i++) {
    result += unvocalized.charAt(i);
    if (i < diacritics.length && diacritics[i]) {
      result += diacritics[i];
    }
  }
  return result;
}
