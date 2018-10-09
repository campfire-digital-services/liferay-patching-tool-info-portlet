/**
* Copyright (C) 2015-present by Permeance Technologies
*
* This program is free software: you can redistribute it and/or modify it under the terms of the
* GNU General Public License as published by the Free Software Foundation, either version 3 of the
* License, or (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
* even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* General Public License for more details.
*
* You should have received a copy of the GNU General Public License along with this program. If
* not, see <http://www.gnu.org/licenses/>.
*/

package au.com.permeance.liferay.portlet.patchingtoolinfo.util;

import com.liferay.petra.string.StringPool;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * String Utils Helper.
 *
 * @author Tim Telcik <tim.telcik@permeance.com.au>
 * 
 * @see StringUtils
 */
public class StringHelper {
	
	/** 
	 * Flattens a List of String items into a single String.
	 * 
	 * @param list input List of String items
	 * @return flattened String with items separated by spaces
	 */
	public static String flattenStringList(List<String> list) {
		
		return flattenStringList(list, StringPool.SPACE);
	}

	
	/** 
	 * Flattens a List of String items into a single String.
	 * 
	 * @param list input List of String items
	 * @param itemSep list item seperator
	 * @return flattened String with items separated by spaces
	 */
	public static String flattenStringList(List<String> list, String itemSep) {
		
		if (itemSep == null) {
			itemSep = StringPool.SPACE;
		}
		
		StringBuilder sb = new StringBuilder();
		
		for (String item : list) {
			sb.append(item.toString());
			sb.append(itemSep);
		}
		
		return sb.toString();
	}	
	
	
	/** 
	 * Trims a List of String lines.
	 * 
	 * @param list input List of String lines
	 * @return List of trimmed String itlinesems
	 */
	public static List<String> trimStringLineList(List<String> lines) {
		
		List<String> newLines = new ArrayList<String>();
		
		if (lines != null) {
			for (String line : lines) {
				String newLine = line.trim();
				newLines.add(newLine);
			}
		}
		
		return newLines;
	}
	
	
	/** 
	 * Strips end of line markers from a List of String lines.
	 * 
	 * @param list input List of String lines
	 * @return List of trimmed String lines
	 */
	public static List<String> stripEolFromStringLineList(List<String> lines) {
		
		List<String> newLines = new ArrayList<String>();
		
		if (lines != null) {
			for (String line : lines) {
				String str = line;
				str = str.replace(StringPool.NEW_LINE, StringPool.BLANK);
				str = str.replace(StringPool.RETURN, StringPool.BLANK);
				String newLine = str;
				newLines.add(newLine);
			}
		}
		
		return newLines;
	}
	
	
	/**
	 * Strips chars from input List of String lines and returns new List of String lines.
	 * 
	 * @param lines input List of String lines
	 * @param stripChars chars to strip
	 * @return List of String lines with mathing chars stripped
	 * 
	 * @see StringUtils#strip(String,String)
	 */
	public static List<String> stripStringLineList(List<String> lines, String stripChars) {
		
		List<String> newLines = new ArrayList<String>();
		
		if (lines != null) {
			for (String line : lines) {
				String newLine = StringUtils.strip( line, stripChars );
				newLines.add(newLine);
			}
		}
		
		return newLines;
	}

}
