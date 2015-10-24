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
	
	private static final String PATCHING_TOOL_FOLDER_NAME = "patching-tool";
	
	private static final String PATCHING_TOOL_SCRIPT_BASE_NAME = "patching-tool";
	
	private static final String PATCHING_TOOL_INFO_ARG = "info";

	private Map<String,Object> patchingToolInfoCache = new HashMap<String,Object>();
	
	
	@Override
	public void doView(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {
		
		if (LOG.isInfoEnabled()) {
			LOG.info("process view");
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
			
			String liferayHomePath = System.getProperty("liferay.home");

			String command = buildPatchingToolInfoQueryCommand();
			
			Runtime runtime = Runtime.getRuntime();
			
			Process process = runtime.exec(command, new String[0], new File(liferayHomePath));
			
			List<String> processLines = IOUtils.readLines(process.getInputStream());
			
			process.waitFor();
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("patching tool info process returned " + processLines.size() + " lines");
			}
			
			resultLines.addAll(processLines);
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("patching tool info result contains " + processLines.size() + " lines");
			}
			
		} catch (Exception e) {
			
			String msg = "Error querying patching tool info: " + e.getMessage();
			LOG.error(msg, e);
			throw new PortletException(msg, e);
			
		}
		
		if (LOG.isInfoEnabled()) {
			LOG.info("patching tool info query returned " + resultLines.size() + " lines");
		}
		
		return resultLines;
	}
	

	private String buildPatchingToolInfoQueryCommand() throws Exception {
		
		String liferayHomePath = System.getProperty("liferay.home");

		String shellScriptExt = StringPool.BLANK;
		
		if (OSDetector.isWindows()) {
			shellScriptExt = MS_WINDOWS_SHELL_FILE_EXT;
		} else {
			shellScriptExt = UNIX_LINUX_SHELL_FILE_EXT;			
		}
		
		String patchingToolScript = PATCHING_TOOL_SCRIPT_BASE_NAME + shellScriptExt;
		
		String patchingToolPath = liferayHomePath + File.separator + PATCHING_TOOL_FOLDER_NAME + File.separator + patchingToolScript;
		
		String patchingToolArg = PATCHING_TOOL_INFO_ARG;
		
		StringBuilder commandBuffer = new StringBuilder();
		
		commandBuffer.append(patchingToolPath);
		
		commandBuffer.append(StringPool.SPACE);
		
		commandBuffer.append(patchingToolArg);
		
		String command = commandBuffer.toString();
		
		return command;
	}

}
