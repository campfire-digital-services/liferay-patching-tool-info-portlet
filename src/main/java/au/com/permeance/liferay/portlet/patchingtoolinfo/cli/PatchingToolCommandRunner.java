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

import au.com.permeance.liferay.portlet.patchingtoolinfo.util.StringHelper;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OSDetector;
import com.liferay.portal.kernel.util.StringPool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;


/**
 * Patching Tool Command Runner.
 * 
 * @author Tim Telcik <tim.telcik@permeance.com.au>
 * 
 * @see PatchingToolResults
 */
public class PatchingToolCommandRunner {

	private static final Log LOG = LogFactoryUtil.getLog(PatchingToolCommandRunner.class);
	
	private static final String MS_WINDOWS_SHELL_FILE_EXT = ".bat";
	
	private static final String UNIX_LINUX_SHELL_FILE_EXT = ".sh";
	
	private static final String MS_WINDOWS_SHELL_NAME = "cmd";
	
	private static final String MS_WINDOWS_SHELL_OPTION = "/c";

	private static final String UNIX_LINUX_SHELL_NAME = "/bin/sh";
	
	private static final String PATCHING_TOOL_HOME_FOLDER_NAME = "patching-tool";
	
	private static final String PATCHING_TOOL_SCRIPT_BASE_NAME = "patching-tool";
	
	private static final String SYS_PROP_KEY_LIFERAY_HOME = "liferay.home"; 
	
	private List<String> patchingToolOptions = new ArrayList<String>();
	
	private PatchingToolResults patchingToolResults = new PatchingToolResults();	
	

	public PatchingToolCommandRunner() {
	}
	
	
	public void setPatchingToolOptions(List<String> options) {
		if (options == null) {
			options = new ArrayList<String>();
		}
		this.patchingToolOptions = options;
	}
	

	public List<String> getPatchingToolOptions() {
		return patchingToolOptions;
	}
	
	
	public PatchingToolResults getPatchingToolResults() {
		return patchingToolResults;
	}


	public void runCommand() throws Exception {
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("running patching tool command ...");
		}

		try {
			
			ProcessBuilder processBuilder = configureProcessBuilder();
			
			// NOTE: ProcessBuilder#environent is initialised with System.getenv()
			// @see http://docs.oracle.com/javase/7/docs/api/java/lang/ProcessBuilder.html#environment%28%29
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("processBuilder : " + processBuilder);				
				List<String> commandList = processBuilder.command();
				LOG.debug("command environment : " + processBuilder.environment());
				LOG.debug("command list : " + commandList);
				LOG.debug("command directory : " + processBuilder.directory());
			}
			
			if (LOG.isDebugEnabled()) {
				List<String> commandList = processBuilder.command();
				String processCommandStr = StringHelper.flattenStringList( commandList );
				LOG.debug("running patching tool command : " + processCommandStr);
			}

			Process process = processBuilder.start();
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("process : " + process);				
			}			

			// NOTE: Java 1.8 supports Process#waitFor with a timeout
			// eg. boolean finished = iostat.waitFor(100, TimeUnit.MILLISECONDS);

			int processExitValue = process.waitFor();

			List<String> processOutputLines = IOUtils.readLines( process.getInputStream() );

			List<String> processErrorLines = IOUtils.readLines( process.getErrorStream() );
			
			this.patchingToolResults = new PatchingToolResults( processExitValue, processOutputLines, processErrorLines );
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("patchingToolResults: " + patchingToolResults);
			}
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("patching tool returned exit code " + this.patchingToolResults.getExitValue() );
				LOG.debug("patching tool returned " + this.patchingToolResults.getOutputLines().size() + " output lines");
				LOG.debug("--- COMMAND OUTPUT ---");
				LOG.debug(processOutputLines);
				LOG.debug("patching tool returned " + this.patchingToolResults.getErrorLines().size() + " error lines");
				LOG.debug("--- COMMAND ERROR ---");				
				LOG.debug(processErrorLines);
			}
			
			// NOTE: Command shell may return lines in the error stream that are warning messages, not errors.
			// Hence, we cannot rely upon content in the error stream as a valid error.
			
			if (this.patchingToolResults.getExitValue() != 0) {
				StringBuilder sb = new StringBuilder();
				String errorLine1 = null;
				if (this.patchingToolResults.hasErrorLines()) {
					errorLine1 = this.patchingToolResults.getErrorLines().get(0);
				}
				if (errorLine1 == null) {
					sb.append("Error running patching tool command.");
					sb.append(" See portal logs for more details.");
				} else {
					sb.append("Error running patching tool command : ");
					sb.append(errorLine1);			
				}
				String errMsg = sb.toString();
				throw new Exception(errMsg);
			}

		} catch (Exception e) {
			
			String msg = "Error executing patching tool command : " + e.getMessage();
			LOG.error(msg, e);
			throw new Exception(msg, e);
		}
	}
	
	
	private ProcessBuilder configureProcessBuilder() throws Exception {
		
		String liferayHomePath = System.getProperty( SYS_PROP_KEY_LIFERAY_HOME );
		
		if (LOG.isDebugEnabled()) {
			LOG.debug(SYS_PROP_KEY_LIFERAY_HOME + " : " + liferayHomePath);
		}
		
		if ( liferayHomePath == null ) {
			String msg = "Liferay Home property is undefined";
			LOG.error( msg );
			throw new Exception( msg );
		}
		
		String patchingToolHomePath = liferayHomePath + File.separator + PATCHING_TOOL_HOME_FOLDER_NAME;
		
		File patchingToolHomeDir = new File( patchingToolHomePath );
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("patchingToolHomeDir : " + patchingToolHomeDir);
		}
		
		if (!patchingToolHomeDir.exists()) {
			String msg = "Patching tool home folder does not exist : " + patchingToolHomeDir.getAbsolutePath();
			LOG.error(msg);
			throw new Exception(msg);
		}

		String patchingToolScriptName = buildPatchingToolScriptName();

		if (LOG.isDebugEnabled()) {
			LOG.debug("patchingToolScriptName : " + patchingToolScriptName);
		}

		String patchingToolScriptPath = patchingToolHomePath + File.separator + buildPatchingToolScriptName();
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("patchingToolScriptPath : " + patchingToolScriptPath);
		}
		
		File patchingToolScriptFile = new File(patchingToolScriptPath);
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("patchingToolScriptFile : " + patchingToolScriptFile);
		}
		
		if (!patchingToolScriptFile.exists()) {
			String msg = "Patching tool script does not exist : " + patchingToolScriptFile.getAbsolutePath();
			LOG.error(msg);
			throw new Exception(msg);
		}
		
		List<String> commandList = new ArrayList<String>();
		
		List<String> shellCommand = buildShellCommand();

		if (LOG.isDebugEnabled()) {
			LOG.debug("shellCommand : " + shellCommand);
		}

		commandList.addAll( shellCommand );
		
		commandList.add( patchingToolScriptName );

		if (LOG.isDebugEnabled()) {
			LOG.debug("patchingToolOptions : " + getPatchingToolOptions());
		}

		if (!getPatchingToolOptions().isEmpty()) {
			commandList.addAll( getPatchingToolOptions() );			
		}
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("commandList : " + commandList);
		}
		
		ProcessBuilder pb = new ProcessBuilder( commandList );
		
		pb.directory( patchingToolHomeDir );

		return pb;
	}
	
	
	private List<String> buildShellCommand() {
		
		List<String> commandList = new ArrayList<String>();
		
		if (OSDetector.isWindows()) {
			commandList.add( MS_WINDOWS_SHELL_NAME );
			commandList.add( MS_WINDOWS_SHELL_OPTION );
		} else {
			commandList.add( UNIX_LINUX_SHELL_NAME );
		}
		
		return commandList;
	}
	
	
	private String buildPatchingToolScriptName() throws Exception {
		
		String shellScriptExt = StringPool.BLANK;
		
		if (OSDetector.isWindows()) {
			shellScriptExt = MS_WINDOWS_SHELL_FILE_EXT;
		} else {
			shellScriptExt = UNIX_LINUX_SHELL_FILE_EXT;			
		}
		
		String patchingToolScriptName = PATCHING_TOOL_SCRIPT_BASE_NAME + shellScriptExt;
		
		String command = patchingToolScriptName;
		
		return command;
	}

}
