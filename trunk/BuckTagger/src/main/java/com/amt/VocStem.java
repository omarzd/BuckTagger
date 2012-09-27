/* 
 *  Copyright 2012 Omar Alzuhaibi and Ahmed Aman
 */

/* 
 *  This file is part of BuckTagger.
 *  
 *  BuckTagger is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *  
 *  BuckTagger is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with BuckTagger.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.amt;

import java.util.Arrays;
import java.util.ArrayList;

//import javax.swing.JOptionPane;
//import org.mvel2.ast.RegExMatch;

/* This class represents the input word after vocalization and stemming.
 * The word should appear like Buckwalter's original stems.
 * TODO: Possible subclasses should be: VocStemNoun, VocStemVerb for better handling.
 * TODO: In fact, this class has methods that are specific only to Imperfect Verbs. So they should be moved to a child class VocIV.
 */
public class VocStem {
	
	// CONSTANTS
	public static final String FATHA = "َ";
	public static final String DAMMA = "ُ";
	public static final String KASRA = "ِ";
	public static final String SHADDA = "ّ";
	public static final String ALIF = "ا";
	public static final String[] DIACRITICS = {"َ","ً","ُ","ٌ","ِ","ٍ","ّ","ْ"};
	public static final String[] IMP_LETTERS = {"ت", "ي", "ن", "أ"};
	
	// instance variables
	// each variable here stands for a CONDITION in the decision table except those conditions that are natively checked using built-in functions of the rules language
	private String stem				  = null; // vocalized and stemmed 
	private String stemClass          = null; // اسم ، فعل ، أداة
	private String stemSubClass       = null; // للأفعال: ماضي مضارع أمر، للأسماء علم إلخ
	private boolean isBeforeLastDblOrVowel = false; // عينه مضعفة أو معتلة
	private boolean isBeforeLastHamza = false;  // قبل آخره همزة
	private boolean isFirstHamza 	  = false; // فاؤه همزة
	private boolean isImpYa 		  = true; // حرف المضارعة مفتوح
	private boolean isPassive 		  = false; // مبني للمجهول
	private int transitivity          = -1; // for verbs; 0 = intransitive, 1 = tr to one object, 2 = tr to 2 obj.. etc
	private String primaryTag		  = null;
	private String modSStem			  = null; // a temporary variable to keep the modified word that fits a secondary tag
	private ArrayList<TagStem> secondaryTagStems = new ArrayList<TagStem>();
	
	private ArrayList<String> diacs = new ArrayList<String>();

	// Constructors
	public VocStem() {
		// this constructor is causing exceptions when used
		this("DEFAULT");
	}
	
	public VocStem(String stem) {
		this.stem = stem;
		estimStemClass();
		estimStemSubClass();
		estimIsBeforeLastDblOrVowel();
		estimIsBeforeLastHamza();
		estimIsFirstHamza();
		estimIsImpYa();
		estimIsPassive();
		estimTransitivity();
		modSStem = getUnvocalized();
		storeDiacritics();
	}
	
	
	// Getters and Setters
	public String getStem() {
		return stem;
	}
	public void setStem(String stem) {
		this.stem = stem;
	}
	public String getStemClass() {
		return stemClass;
	}	
	public void setStemClass(String stemClass) {
		this.stemClass = stemClass;
	}	
	public String getStemSubClass() {
		return stemSubClass;
	}	
	public void setStemSubClass(String stemSubClass) {
		this.stemSubClass = stemSubClass;
	}
	public boolean isBeforeLastDblOrVowel() {
		return isBeforeLastDblOrVowel;
	}	
	public void setBeforeLastDblOrVowel(boolean isBeforeLastDblOrVowel) {
		this.isBeforeLastDblOrVowel = isBeforeLastDblOrVowel;
	}
	public boolean isBeforeLastHamza() {
		return isBeforeLastHamza;
	}
	public void setBeforeLastHamza(boolean isBeforeLastHamza) {
		this.isBeforeLastHamza = isBeforeLastHamza;
	}
	public boolean isFirstHamza() {
		return isFirstHamza;
	}
	public void setFirstHamza(boolean isFirstHamza) {
		this.isFirstHamza = isFirstHamza;
	}
	public boolean isImpYa() {
		return isImpYa;
	}
	public void setImpYa(boolean isImpYa) {
		this.isImpYa = isImpYa;
	}
	public boolean isPassive() {
		return isPassive;
	}
	public void setPassive(boolean isPassive) {
		this.isPassive = isPassive;
	}
	public int getTransitivity() {
		return transitivity;
	}
	public void setTransitivity(int t) {
		this.transitivity = t;
	}
	public String getPrimaryTag() {
		return primaryTag;
	}
	public void setPrimaryTag(String primaryTag) {
		this.primaryTag = primaryTag;
	}
	public String getModSStem() {
		return modSStem;
	}
	public void setModSStem(String modSStem) {
		this.modSStem = modSStem;
	}
	public ArrayList<TagStem> getSecondaryTagStems() {
		return secondaryTagStems;
	}
	public void setSecondaryTagStems(ArrayList<TagStem> secondaryTagStems) {
		this.secondaryTagStems = secondaryTagStems;
	}
	// END Getters and Setters
	// END Class Setup
	// ========================================================================
	
	// ==========================
	// CONDITION Value Estimators 
	// ==========================
	// In this section, all the hard work is done.. actually waiting to be done
	// this is a big TODO for future developers
	// stemClass: اسم ، فعل ، أداة
	// stemSubClass: للأفعال: ماضي مضارع أمر، للأسماء علم إلخ 
	private void estimStemClass() {
		// TODO needs morphological analysis or simply POS tagging, perhaps from AlKhalil
		setStemClass("verb");
	}	
	
	private void estimStemSubClass() {
		// TODO needs morphological analysis or simply POS tagging, perhaps from AlKhalil
		setStemSubClass("imperfect");
	}
	
	// returns true if letter before the last is either doubled or a vowel
	private void estimIsBeforeLastDblOrVowel() {
		setBeforeLastDblOrVowel(isBeforeLastDoubled() || isBeforeLastVowel());
	}
	
	// returns true if letter before last (before last in root as well) is a Hamza
	private void estimIsBeforeLastHamza() {
		setBeforeLastHamza(getBeforeLast().matches("[ء-ئ]"));
	}	
	
	// returns true if first original letter is a Hamza
	private void estimIsFirstHamza() {
		// TODO needs morphological analysis or root extraction to get first original letter
		setFirstHamza(getFirstOriginal().matches("[ء-ئ]"));		
	}
	
	// returns true if imperfection letter حرف المضارعة carries a Fatha
	private void estimIsImpYa() {
		// TODO should be adjusted to return an intelligent guess if no diacritic found
		// TODO Auto Diacritization needed here
		setImpYa( isImpDiac(FATHA));
	}
	
	// returns a diacritic-driven guess of whether it is active or passive, true if passive.
	private void estimIsPassive() {
		// assuming this is an imperfect verb
		// return true if imperfection letter carries Damma AND ( Letter before last carries Fatha OR is Alif )
		setPassive(isImpDiac(DAMMA) && (isBeforeLastDiac(FATHA) || getBeforeLast().equals(ALIF)));
	}
	
	// returns a diacritic-driven guess of whether it is transitive or not  
	private void estimTransitivity() {
		// assuming this is an imperfect verb
		// TODO needs a database driven method. Morphological Analysis would be more than enough.
		// this is just an intelligent guess
		int length = getUnvocalized().length();
		if (isImpDiac(DAMMA) && !isPassive())
			setTransitivity(1);
		else if (length > 4) 
			setTransitivity(0);
		else
			setTransitivity(1);
	}
	// ======================================================================
	// ==============
	// Helper Methods
	// ==============
	
	// returns the word unvocalized (undiacritized)
	public String getUnvocalized() {
		return stem.replaceAll("[ًٌٍَُِّْ]", "");
	}
	
	// restores the diacritics on the unvocalized stems
	public String restoreDiacritics(){
		String result = "";
		int length = modSStem.length();
			for(int i=0;i<length;i++){
				result += modSStem.charAt(i) + diacs.get(i);
			}
		return result;
	}
	
	// stores the diacratized stems before unvocalizing them
	public void storeDiacritics(){
		diacs.clear();
		int modSStemLength = getUnvocalized().length();
		for(int i=0;i<modSStemLength;i++){
			diacs.add(i,"");
		}
		int length=stem.length();
		int pos=-1;
		for(int i=0;i<length;i++){
			if("ًٌٍَُِّْ".indexOf(stem.charAt(i)) > -1)
				diacs.set(pos, diacs.get(pos).concat(stem.charAt(i)+""));
			else
				pos++;
		}
	}
	
	// returns last letter as a string
	public String getLast () {
		String unvoc = getUnvocalized();
		int length = unvoc.length();
		return unvoc.substring(length-1);
	}
	
	// is parameter the last letter ?
	public boolean isLast (String s) {
		return getLast().equals(s);
	}
	
	public String getBeforeLast () {
		String unvoc = getUnvocalized();
		int length = unvoc.length();
		return unvoc.substring(length-2, length-1);
	}
	
	// returns true if the imperfect verb has its second and third original letters identical AND if they are likely to unfold under the جزم case.   
	public boolean isBeforeLastDoubled() {
		int length = getUnvocalized().length();
		if(isLastDiac(SHADDA)) return true;
		//else if(isBeforeLastDiac(SHADDA) || isBeforeLastDup()) return false;
		
		// now we are unsure, so we're just guessing. The purpose is to reduce user involvement. 
		else if (length == 3) return true;
		else if (length >= 6) return false;  
		else return Math.random() >= 0.95; // TODO: simple ML algorithm can suffice here. 
	}
	
	// return true if last letter is the same as before the last
	public boolean isBeforeLastDup() {
		return getBeforeLast().equals(getLast());
	}
	
	// returns true if the specified diacritical mark is on the last letter
	public boolean isLastDiac(String diac) {
		// TODO needs a clearer implementation. Perhaps we should keep an unvocalized string and a separate array of characters carrying diacritics in their corresponding positions.
		// TODO Auto Diacritization needed here
		return stem.matches(".+"+diac+"[ًٌٍَُِ]?");
	}
	
	// returns true if the specified diacritical mark is on the letter before last
	public boolean isBeforeLastDiac(String diac) {
		// TODO needs a clearer implementation. Perhaps we should keep an unvocalized string and a separate array of characters carrying diacritics in their corresponding positions.
		// TODO Auto Diacritization needed here
		return stem.matches(".+"+diac+"[ء-ي][ّ]?[ًٌٍَُِْ]?");
	}
	
	// returns true if the letter before last is one of the three vowels
	public boolean isBeforeLastVowel() {
		return getBeforeLast().matches("[اوي]");
	}
	
	// return true if last letter exists in the specified array
	public boolean lastIn(String [] a) {
		Arrays.sort(a);
		return Arrays.binarySearch(a, getLast()) >= 0;
	}
	
	// returns true if last letter (last in root as well) is a Hamza
	public boolean isLastHamza() {
		return getLast().matches("[ء-ئ]");
	}	
	
	// returns first original (first-in-root) letter
	public String getFirstOriginal() {
		// This definitely needs a try catch.. in fact, all calls to substring do 
		// TODO needs morphological analysis or root extraction to get first original letter
		return getUnvocalized().substring(1, 2);
	}
	
	// returns true if imperfection letter حرف المضارعة carries the specified diacritic
	public boolean isImpDiac(String diac) {
		// assuming this is an imperfect verb
		// TODO Auto Diacritization needed here
		return stem.matches("."+diac+".*");
	}
	
	// returns true if imperfection letter حرف المضارعة carries a Damma
	public boolean isImpYu() {
		// TODO should be adjusted to return an intelligent guess if no diacritic found
		// TODO Auto Diacritization needed here
		return isImpDiac(DAMMA);
	}
	
	// TODO: implement transitive to more than one object by introducing the appropriate tags to Aramorph 
	public boolean isTransitive() {
		return transitivity != 0;
	}
	
	// =======================================================================
	// =====================
	// Secondary Tag Actions
	// =====================
	
	// replace first letter with string s
	public void replaceFirst(String s) {
		storeDiacritics();
		String unvoc = getUnvocalized();
		int length = unvoc.length();
		if (length > 2)
			modSStem = unvoc.substring(0,1)+s+unvoc.substring(2,length);
		else 
			modSStem = unvoc.substring(0,1)+s;
		
		if(s.isEmpty())
			diacs.remove(0);
	}

	// replace last letter with string s
	public void replaceLast(String s) {
		storeDiacritics();
		String unvoc = getUnvocalized();
		int length = unvoc.length();
		modSStem = unvoc.substring(0,length-1)+s;
		
		if(s.isEmpty())
			diacs.remove(diacs.size()-1);
	}

	// replace before-last letter with string s
	public void replaceBeforeLast(String s) {
		storeDiacritics();
		String unvoc = getUnvocalized();
		int length = unvoc.length();
		if (length > 2)
			modSStem = unvoc.substring(0,length-2)+s+unvoc.substring(length-1,length);
		else 
			modSStem = s+unvoc.substring(1,length);
		
		if(s.isEmpty())
			diacs.remove(diacs.size()-2);
	}
	
    public static String getMeaning(String input){
    	// TODO: put translator here.
    	return "defaultMeaning";
    }

	public static String removeDiacritics(String input){
    	String result = input;
    	for(String  d : DIACRITICS){
    		result = result.replace(d, "");
    	}
    	return result;
    }
}
