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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;


/**
 * Patching Tool Command Runner.
 * 
 * @author Tim Telcik <tim.telcik@permeance.com.au>
 */
public class PatchingToolCommandRunner {

	private static final Log LOG = LogFactoryUtil.getLog(PatchingToolCommandRunner.class);
	
	private static final String MS_WINDOWS_SHELL_FILE_EXT = ".bat";
	
	private static final String UNIX_LINUX_SHELL_FILE_EXT = ".sh";
	
	private static final String MS_WINDOWS_SHELL_NAME = "cmd";
	
	private static final String MS_WINDOWS_SHELL_OPTION = "/c";
	
	private static final String UNIX_LINUX_SHELL_NAME = "/bin/sh";
	
	private static final String UNIX_LINUX_SHELL_OPTION = "-l";
	
	private static final String PATCHING_TOOL_HOME_FOLDER_NAME = "patching-tool";
	
	private static final String PATCHING_TOOL_SCRIPT_BASE_NAME = "patching-tool";
	
	private static final String SYS_PROP_KEY_LIFERAY_HOME = "liferay.home"; 
	
	// private static final String DEFAULT_PATCHING_TOOL_INFO_OPTION = "info";
	
	private List<String> commandOutputLines = Collections.emptyList();
	
	private List<String> commandErrorLines = Collections.emptyList();	
	
	private List<String> patchingToolOptions = new ArrayList<String>();
	

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
	
	
	public List<String> getCommandOutputLines() {
		return this.commandOutputLines;
	}
	
	
	public boolean hasOutputLines() {
		return (!getCommandOutputLines().isEmpty());
	}
	

	public List<String> getCommandErrorLines() {
		return this.commandErrorLines;
	}
	
	
	public boolean hasErrorLines() {
		return (!getCommandErrorLines().isEmpty());
	}

	
	public void runCommand() throws Exception {
		
		if (LOG.isInfoEnabled()) {
			LOG.info("running patching tool command ...");
		}

		int processExitValue = 0;
		
		this.commandOutputLines = Collections.emptyList();
		
		this.commandErrorLines = Collections.emptyList();	

		try {

			ProcessBuilder processBuilder = configureProcessBuilder();
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("processBuilder : " + processBuilder);				
				List<String> commandList = processBuilder.command();
				LOG.debug("command list : " + commandList);				
				LOG.debug("command directory : " + processBuilder.directory());				
				LOG.debug("command environment : " + processBuilder.environment());				
			}
			
			if (LOG.isInfoEnabled()) {
				// String processCommandStr = ProcessBuilderHelper.buildProcessCommandString(processBuilder);
				List<String> commandList = processBuilder.command();
				String processCommandStr = StringHelper.flattenStringList( commandList );
				LOG.info("running command : " + processCommandStr);
			}

			Process process = processBuilder.start();
			
			this.commandOutputLines = IOUtils.readLines(process.getInputStream());
			
			this.commandErrorLines = IOUtils.readLines(process.getErrorStream());	
			
			// NOTE: Java 1.8 supports Process#waitFor with a timeout
			// eg. boolean finished = iostat.waitFor(100, TimeUnit.MILLISECONDS);
			
			processExitValue = process.waitFor();
			
			if (LOG.isInfoEnabled()) {
				LOG.info("patching tool process returned exit code " + processExitValue );
				LOG.info("patching tool process returned " + commandOutputLines.size() + " output lines");
				LOG.info("--- COMMAND OUTPUT ---");
				LOG.info(commandOutputLines);
				LOG.info("patching tool process returned " + commandErrorLines.size() + " error lines");
				LOG.info("--- COMMAND ERROR ---");				
				LOG.info(commandErrorLines);
			}
			
			if ((processExitValue != 0) || (hasErrorLines())) {
				StringBuilder sb = new StringBuilder();
				String errorLine1 = getCommandErrorLines().get(0);
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
			
			LOG.error("patching tool process returned exit code " + processExitValue);
			LOG.error("patching tool process returned " + commandErrorLines.size() + " error lines");
			LOG.error("--- COMMAND ERROR ---");				
			LOG.error(getCommandErrorLines());
			
			String msg = "Error running patching tool command : " + e.getMessage();
			LOG.error(msg, e);
			throw new Exception(msg, e);
			
		}
		
		if (LOG.isInfoEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append("patching tool command returned " + getCommandOutputLines().size() + " output lines");
			sb.append(" and " + getCommandErrorLines().size() + " error lines");
			String msg = sb.toString();
			LOG.info(msg);
		}
	}
	
	
	private ProcessBuilder configureProcessBuilder() throws Exception {
		
		String liferayHomePath = System.getProperty( SYS_PROP_KEY_LIFERAY_HOME );
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("liferayHomePath : " + liferayHomePath);
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

		String patchingToolScriptPath = buildPatchingToolScriptName();
		
		// String patchingToolScriptPath = patchingToolHomePath + File.separator + buildPatchingToolScriptName();
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("patchingToolScriptPath : " + patchingToolScriptPath);
		}
		
		List<String> commandList = new ArrayList<String>();
		
		List<String> shellCommand = buildShellCommand();

		if (LOG.isDebugEnabled()) {
			LOG.debug("shellCommand : " + shellCommand);
		}

		commandList.addAll(shellCommand);
		
		commandList.add(patchingToolScriptPath);

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

		// ProcessBuilder#environent is initialised with System.getenv()
		// @see http://docs.oracle.com/javase/7/docs/api/java/lang/ProcessBuilder.html#environment%28%29

		Map<String, String> pbEnv = pb.environment();
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("pbEnv : " + pbEnv);
		}
		
		return pb;
	}
	
	
	private List<String> buildShellCommand() {
		
		List<String> commandList = new ArrayList<String>();
		
		if (OSDetector.isWindows()) {
			commandList.add( MS_WINDOWS_SHELL_NAME );
			commandList.add( MS_WINDOWS_SHELL_OPTION );
		} else {
			commandList.add( UNIX_LINUX_SHELL_NAME );
			// commandList.add( UNIX_LINUX_SHELL_OPTION );
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
