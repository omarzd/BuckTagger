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

import java.util.ArrayList;

public class TagResult {
	private static String tag = null;
	private static ArrayList<TagStem> secondaryTags = new ArrayList<TagStem>();  

	public String getTag() {
		return tag;
	}

	public static void setTag(String temp) {
		tag = temp;
		//System.out.println("TagResult.tag = "+ tag);
	}
	
	public static void appendSecondaryTag(String tag, String stem,String diacritizedStem) {
		secondaryTags.add(new TagStem(tag,stem,diacritizedStem));
		System.out.print("Appended: "+tag+": "+stem+"\n");
	}

}
