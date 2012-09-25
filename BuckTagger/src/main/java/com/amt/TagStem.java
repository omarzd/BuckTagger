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

public class TagStem {
	private String secondaryTag = null;
	private String secondaryStem= null;
	private String diacritizedStem= null;
	
	public TagStem() {
		secondaryTag = "";
		secondaryStem= "";
		diacritizedStem= "";
	}
	
	public TagStem(String tag, String stem) {
		secondaryTag = tag;
		secondaryStem= stem;
	}
	
	public TagStem(String tag, String stem, String diacritizedStem) {
		this(tag,stem);
		this.diacritizedStem = diacritizedStem;
	}
	
	public String getDiacritizedStem(){
		return diacritizedStem;
	}
	
	public String getSecondaryTag() {
		return secondaryTag;
	}

	public void setSecondaryTag(String secondaryTag) {
		this.secondaryTag = secondaryTag;
	}

	public String getSecondaryStem() {
		return secondaryStem;
	}

	public void setSecondaryStem(String secondaryStem) {
		this.secondaryStem = secondaryStem;
	}
    
	public String toString() {
		//return secondaryStem + "\t" + diacritizedStem + "\t" + secondaryTag+"\t"+VocStem.getMeaning(secondaryStem) ;
		return secondaryTag+":\t"+diacritizedStem+"\n";
	}
}