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

package au.com.permeance.liferay.portlet.patchingtoolinfo.cli;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Patching Tool Results.
 * 
 * @author Tim Telcik <tim.telcik@permeance.com.au>
 */
public class PatchingToolResults implements Serializable {
	
	private static final long serialVersionUID = 1183293828483589188L;
	
	private int exitValue = 0;
	private List<String> outputLines = Collections.emptyList();
	private List<String> errorLines = Collections.emptyList();
	

	public PatchingToolResults() {
	}
	

	public PatchingToolResults( int exitValue, List<String> outputLines, List<String> errorLines ) {
		
		setExitValue( exitValue );
		setOutputLines( outputLines );
		setErrorLines( errorLines );
	}
	
	
	public void setExitValue(int commandExitValue) {
		this.exitValue = commandExitValue;
	}

	
	public int getExitValue() {
		return this.exitValue;
	}

	
	public void setOutputLines(List<String> lines) {
		this.outputLines = new ArrayList<String>(lines);
	}

	
	public List<String> getOutputLines() {
		return this.outputLines;
	}


	public boolean hasOutputLines() {
		return (getOutputLines().size() > 0);
	}

	
	public void setErrorLines(List<String> lines) {
		this.errorLines = new ArrayList<String>(lines);
	}

	
	public List<String> getErrorLines() {
		return this.errorLines;
	}
	
	
	public boolean hasErrorLines() {
		return (getErrorLines().size() > 0);
	}


	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.getClass().getName());
		buffer.append("[");
		buffer.append("exitValue.size=" + exitValue);
		buffer.append(", ");
		buffer.append("outputLines.size=" + outputLines.size());
		buffer.append(", ");
		buffer.append("errorLines.size=" + errorLines.size());
		buffer.append("]");
		return buffer.toString();
	}

}
