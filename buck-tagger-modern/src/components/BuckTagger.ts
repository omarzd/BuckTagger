/**
 * BuckTagger Web Component
 * Embeddable Arabic morphological analyzer
 */

import { ArabicStem } from '../core/ArabicStem';
import { analyze } from '../core/rules/ruleEngine';
import { removeImperfectiveLetter } from '../core/utils/arabic';
import styles from './BuckTagger.css?inline';

export class BuckTaggerElement extends HTMLElement {
  private shadow: ShadowRoot;
  private stem: ArabicStem | null = null;

  // UI Elements
  private inputField!: HTMLInputElement;
  private checkboxes: Map<string, HTMLInputElement> = new Map();
  private submitButton!: HTMLButtonElement;
  private resultsSection!: HTMLElement;
  private primaryTagOutput!: HTMLElement;
  private secondaryTagsOutput!: HTMLElement;

  constructor() {
    super();
    this.shadow = this.attachShadow({ mode: 'open' });
  }

  connectedCallback() {
    this.render();
    this.attachEventListeners();
  }

  private render() {
    this.shadow.innerHTML = `
      <style>${styles}</style>
      <div class="container">
        <div class="card">
          <div class="header">
            <h1 class="title">واسم - BuckTagger</h1>
            <p class="subtitle">Arabic Morphological Analyzer - Buckwalter Tagger</p>
          </div>

          <div class="input-section">
            <label class="label" for="arabic-input">أدخل كلمة (Enter Arabic Word)</label>
            <input
              type="text"
              id="arabic-input"
              class="arabic-input"
              placeholder="يَخشَى"
              autocomplete="off"
            />
          </div>

          <div class="features-section">
            <label class="label">الميزات الصرفية (Morphological Features)</label>
            <div class="features-grid">
              <label class="checkbox-wrapper">
                <input type="checkbox" class="checkbox" id="transitive" />
                <span class="checkbox-label">متعدي (Transitive)</span>
              </label>

              <label class="checkbox-wrapper">
                <input type="checkbox" class="checkbox" id="passive" />
                <span class="checkbox-label">مبني للمجهول (Passive)</span>
              </label>

              <label class="checkbox-wrapper">
                <input type="checkbox" class="checkbox" id="imp-ya" />
                <span class="checkbox-label">حرف المضارعة مفتوح (Imp. Fatha)</span>
              </label>

              <label class="checkbox-wrapper">
                <input type="checkbox" class="checkbox" id="first-hamza" />
                <span class="checkbox-label">فاؤه همزة (First Hamza)</span>
              </label>

              <label class="checkbox-wrapper">
                <input type="checkbox" class="checkbox" id="before-last-dbl-vowel" />
                <span class="checkbox-label">عينه مضعفة أو معتلة (Weak/Doubled)</span>
              </label>

              <label class="checkbox-wrapper">
                <input type="checkbox" class="checkbox" id="before-last-hamza" />
                <span class="checkbox-label">عينه همزة (Before-last Hamza)</span>
              </label>
            </div>
          </div>

          <button class="submit-button" id="submit-button">
            تحليل (Analyze)
          </button>

          <div class="results-section" id="results-section">
            <div class="result-card">
              <h3 class="result-title">رئيس (Primary Tag)</h3>
              <div class="primary-tag" id="primary-tag"></div>
            </div>

            <div class="result-card">
              <h3 class="result-title">أفرع (Secondary Tags)</h3>
              <ul class="secondary-tags" id="secondary-tags"></ul>
            </div>
          </div>

          <p class="note">
            ملاحظة: يجب أن تكون الكلمة مجردة من الزوائد ومشكلة
            <br>
            Note: Word should be free of clitics and diacritized
          </p>
        </div>
      </div>
    `;

    // Get references to elements
    this.inputField = this.shadow.getElementById('arabic-input') as HTMLInputElement;
    this.submitButton = this.shadow.getElementById('submit-button') as HTMLButtonElement;
    this.resultsSection = this.shadow.getElementById('results-section') as HTMLElement;
    this.primaryTagOutput = this.shadow.getElementById('primary-tag') as HTMLElement;
    this.secondaryTagsOutput = this.shadow.getElementById('secondary-tags') as HTMLElement;

    // Store checkbox references
    this.checkboxes.set('transitive', this.shadow.getElementById('transitive') as HTMLInputElement);
    this.checkboxes.set('passive', this.shadow.getElementById('passive') as HTMLInputElement);
    this.checkboxes.set('imp-ya', this.shadow.getElementById('imp-ya') as HTMLInputElement);
    this.checkboxes.set('first-hamza', this.shadow.getElementById('first-hamza') as HTMLInputElement);
    this.checkboxes.set('before-last-dbl-vowel', this.shadow.getElementById('before-last-dbl-vowel') as HTMLInputElement);
    this.checkboxes.set('before-last-hamza', this.shadow.getElementById('before-last-hamza') as HTMLInputElement);
  }

  private attachEventListeners() {
    // Input field events
    this.inputField.addEventListener('blur', () => this.onInputBlur());
    this.inputField.addEventListener('keypress', (e) => {
      if (e.key === 'Enter') {
        e.preventDefault();
        this.submitButton.click();
      }
    });

    // Checkbox events
    this.checkboxes.forEach((checkbox, id) => {
      checkbox.addEventListener('change', () => this.onCheckboxChange(id));
    });

    // Submit button
    this.submitButton.addEventListener('click', () => this.analyze());
  }

  private onInputBlur() {
    const text = this.inputField.value.trim();
    if (!text) return;

    // Create stem and estimate features
    this.stem = new ArabicStem(text);
    this.updateCheckboxesFromStem();
  }

  private updateCheckboxesFromStem() {
    if (!this.stem) return;

    const features = this.stem.getFeatures();
    this.checkboxes.get('transitive')!.checked = features.isTransitive;
    this.checkboxes.get('passive')!.checked = features.isPassive;
    this.checkboxes.get('imp-ya')!.checked = features.isImpYa;
    this.checkboxes.get('first-hamza')!.checked = features.isFirstHamza;
    this.checkboxes.get('before-last-dbl-vowel')!.checked = features.isBeforeLastDblOrVowel;
    this.checkboxes.get('before-last-hamza')!.checked = features.isBeforeLastHamza;
  }

  private onCheckboxChange(id: string) {
    if (!this.stem) return;

    const checkbox = this.checkboxes.get(id)!;
    const isChecked = checkbox.checked;

    switch (id) {
      case 'transitive':
        this.stem.setFeature('transitivity', isChecked ? 1 : 0);
        this.stem.setFeature('isTransitive', isChecked);
        break;
      case 'passive':
        this.stem.setFeature('isPassive', isChecked);
        if (isChecked) {
          this.checkboxes.get('imp-ya')!.checked = false;
          this.stem.setFeature('isImpYa', false);
        }
        break;
      case 'imp-ya':
        this.stem.setFeature('isImpYa', isChecked);
        break;
      case 'first-hamza':
        this.stem.setFeature('isFirstHamza', isChecked);
        break;
      case 'before-last-dbl-vowel':
        this.stem.setFeature('isBeforeLastDblOrVowel', isChecked);
        break;
      case 'before-last-hamza':
        this.stem.setFeature('isBeforeLastHamza', isChecked);
        break;
    }
  }

  private analyze() {
    if (!this.stem) {
      alert('أدخل كلمة رجاء (Please enter a word)');
      return;
    }

    const inputWord = this.inputField.value.trim();

    // Perform analysis
    analyze(this.stem);

    // Get results
    const primaryTag = this.stem.getPrimaryTag();
    const secondaryTags = this.stem.getSecondaryTagStems();

    // Remove imperfective letter from display (matching Java behavior)
    const features = this.stem.getFeatures();
    let displayStem = inputWord;
    if (features.stemSubClass === 'imperfect') {
      displayStem = removeImperfectiveLetter(inputWord);
    }

    // Display primary tag
    this.primaryTagOutput.textContent = `${primaryTag}: ${displayStem}`;

    // Display secondary tags
    this.secondaryTagsOutput.innerHTML = '';
    if (secondaryTags.length === 0) {
      this.secondaryTagsOutput.innerHTML = '<li class="secondary-tag-item">لا توجد أفرع (No secondary tags)</li>';
    } else {
      secondaryTags.forEach((tagStem) => {
        const li = document.createElement('li');
        li.className = 'secondary-tag-item';
        li.textContent = `${tagStem.tag}: ${tagStem.stem}`;
        this.secondaryTagsOutput.appendChild(li);
      });
    }

    // Show results
    this.resultsSection.classList.add('show');

    // Log to console (matching Java behavior)
    console.log('=======RESULTS========');
    console.log(`Primary Tag: ${primaryTag}: ${displayStem}`);
    console.log('Secondary Tags:', secondaryTags);
  }
}

// Define custom element
if (!customElements.get('buck-tagger')) {
  customElements.define('buck-tagger', BuckTaggerElement);
}
