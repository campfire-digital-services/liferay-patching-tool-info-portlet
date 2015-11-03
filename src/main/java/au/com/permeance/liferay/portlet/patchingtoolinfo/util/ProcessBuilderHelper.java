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

import com.liferay.portal.kernel.util.StringPool;

import java.io.File;
import java.util.List;


/**
 * Process Builder Helper.
 * 
 * @author Tim Telcik <tim.telcik@permeance.com.au>
 * 
 * @see ProcessBuilder
 */
public class ProcessBuilderHelper {

	public static String buildProcessCommandString(ProcessBuilder processBuilder) throws Exception {
		
		String processCommandStr = StringPool.BLANK;
		
		if (processBuilder != null) {
			List<String> commandList = processBuilder.command();
			File commandDir = processBuilder.directory();
			String commandStr = StringHelper.flattenStringList(commandList);
			processCommandStr = commandDir + File.separator + commandStr;
		}
		
		return processCommandStr;
	}
}
