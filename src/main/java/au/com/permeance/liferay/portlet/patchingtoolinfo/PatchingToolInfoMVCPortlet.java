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

package au.com.permeance.liferay.portlet.patchingtoolinfo;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OSDetector;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.util.bridges.mvc.MVCPortlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.io.IOUtils;


/**
 * Patching Tool Info MVC Portlet.
 *
 * @author Terry Mueller <terry.mueller@permeance.com.au>
 * @author Tim Telcik <tim.telcik@permeance.com.au>
 */
public class PatchingToolInfoMVCPortlet extends MVCPortlet {
	
	private static Log LOG = LogFactoryUtil.getLog(PatchingToolInfoMVCPortlet.class);
	
	private static final String PATCHING_TOOL_INFO_CACHE_KEY = "patchingToolInfo";
	
	private static final String MS_WINDOWS_SHELL_FILE_EXT = ".bat";
	
	private static final String UNIX_LINUX_SHELL_FILE_EXT = ".sh";
	
	private static final String PATCHING_TOOL_HOME_FOLDER_NAME = "patching-tool";
	
	private static final String PATCHING_TOOL_SCRIPT_BASE_NAME = "patching-tool";
	
	private static final String PATCHING_TOOL_INFO_ARG = "info";

	private Map<String,Object> patchingToolInfoCache = new HashMap<String,Object>();
	
	
	@Override
	public void doView(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {
		
		if (LOG.isInfoEnabled()) {
			LOG.info("process view ...");
		}

		@SuppressWarnings("unchecked")
		List<String> patchingToolInfoLines = (List<String>) patchingToolInfoCache.get(PATCHING_TOOL_INFO_CACHE_KEY);

		if (patchingToolInfoLines == null || patchingToolInfoLines.isEmpty()) {

			LOG.info("patching tool info cache is empty");
			patchingToolInfoLines = queryPatchingToolInfo();
			patchingToolInfoCache.put(PATCHING_TOOL_INFO_CACHE_KEY, patchingToolInfoLines);

		} else {
			
			if (LOG.isInfoEnabled()) {
				LOG.info("patching tool info cache contains " + patchingToolInfoLines.size() + " lines");
			}

		}
		
		if (LOG.isInfoEnabled()) {
			LOG.info("adding patching tool info with " + patchingToolInfoLines.size() + " lines to portlet session");
		}

		PortletSession session = renderRequest.getPortletSession();
		
		session.setAttribute("patchingToolInfoLines", patchingToolInfoLines);
		
		if (LOG.isInfoEnabled()) {
			LOG.info("displaying patching tool info");
		}
		
		super.doView(renderRequest, renderResponse);
	}	


	public void refreshPatchingToolInfoAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortletException {
		
		final String ACTION_NAME = "refreshPatchingToolInfoAction";
		
		if (LOG.isInfoEnabled()) {
			LOG.info("process action " + ACTION_NAME);
			LOG.info("clear current patching tool info cache ...");
		}

		this.patchingToolInfoCache.clear();
	}
	
	
	private List<String> queryPatchingToolInfo() throws PortletException {
		
		if (LOG.isInfoEnabled()) {
			LOG.info("query patching tool info ...");
		}
		
		List<String> resultLines = new ArrayList<String>();
		
		try {
			
			resultLines = runPatchingToolCommand();
						
		} catch (Exception e) {
			
			String msg = "Error querying patching tool info : " + e.getMessage();
			LOG.error(msg, e);
			throw new PortletException(msg, e);
		}
		
		if (LOG.isInfoEnabled()) {
			LOG.info("patching tool info query returned " + resultLines.size() + " lines");
		}
		
		return resultLines;
	}
	
	
	private List<String> runPatchingToolCommand() throws Exception {
		
		if (LOG.isInfoEnabled()) {
			LOG.info("running patching tool command ...");
		}
		
		List<String> resultLines = new ArrayList<String>();
		
		List<String> stdoutLines = Collections.emptyList();
		
		List<String> stderrLines = Collections.emptyList();	

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
				String processCommandStr = buildProcessCommandString(processBuilder);
				LOG.info("running command : " + processCommandStr);
			}

			Process process = processBuilder.start();
			
			process.waitFor();
			
			stdoutLines = IOUtils.readLines(process.getInputStream());
			
			stderrLines = IOUtils.readLines(process.getErrorStream());	

			if (LOG.isInfoEnabled()) {
				LOG.info("patching tool process returned " + stdoutLines.size() + " output lines");
				LOG.info("--- COMMAND OUTPUT ---");
				LOG.info(stdoutLines);
				LOG.info("patching tool process returned " + stderrLines.size() + " error lines");
				LOG.info("--- COMMAND ERROR ---");				
				LOG.info(stderrLines);
			}

			resultLines.addAll(stdoutLines);
			
		} catch (Exception e) {
			
			String msg = "Error running patching tool command : " + e.getMessage();
			LOG.error(msg, e);
			throw new Exception(msg, e);
			
		}
		
		if (LOG.isInfoEnabled()) {
			LOG.info("patching tool command returned " + resultLines.size() + " lines");
		}
		
		return resultLines;
	}	
	
	
	private ProcessBuilder configureProcessBuilder() throws Exception {
		
		String liferayHomePath = System.getProperty("liferay.home");
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("liferayHomePath : " + liferayHomePath);
		}
		
		String patchingToolHomePath = liferayHomePath + File.separator + PATCHING_TOOL_HOME_FOLDER_NAME;
		
		File patchingToolHomeDir = new File(patchingToolHomePath);
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("patchingToolHomeDir : " + patchingToolHomeDir);
		}
		
		String patchingToolScriptName = buildPatchingToolScriptName();
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("patchingToolScriptName : " + patchingToolScriptName);
		}
		
		ProcessBuilder pb = new ProcessBuilder(patchingToolScriptName, PATCHING_TOOL_INFO_ARG);
		
		pb.directory(patchingToolHomeDir);
		
		Map<String, String> processEnv = pb.environment();
		
		processEnv.clear();
		
		Map<String, String> systemEnvMap = System.getenv(); 
		
		processEnv.putAll(systemEnvMap);

		return pb;
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
	
	
	private String buildProcessCommandString(ProcessBuilder processBuilder) throws Exception {
		
		String processCommandStr = StringPool.BLANK;
		
		if (processBuilder != null) {
			List<String> commandList = processBuilder.command();
			File commandDir = processBuilder.directory();
			String commandStr = toString(commandList);
			processCommandStr = commandDir + File.separator + commandStr;
		}
		
		return processCommandStr;
	}

	private String toString(List<?> list) {
		return toString(list, StringPool.SPACE);
	}

	
	private String toString(List<?> list, String itemSep) {
		if (itemSep == null) {
		}
		StringBuilder sb = new StringBuilder();
		for (Object item : list) {
			sb.append(item.toString());
			sb.append(itemSep);
		}
		return sb.toString();
	}

}
