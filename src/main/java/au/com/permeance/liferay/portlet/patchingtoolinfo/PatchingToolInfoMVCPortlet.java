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

import au.com.permeance.liferay.portlet.patchingtoolinfo.cli.PatchingToolCommandRunner;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

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


/**
 * Patching Tool Info MVC Portlet.
 *
 * @author Terry Mueller <terry.mueller@permeance.com.au>
 * @author Tim Telcik <tim.telcik@permeance.com.au>
 * 
 * @see MVCPortlet
 */
public class PatchingToolInfoMVCPortlet extends MVCPortlet {
	
	private static final Log LOG = LogFactoryUtil.getLog(PatchingToolInfoMVCPortlet.class);

	private static final String ERROR_TEMPLATE_PATH = "/error.jsp";
	
	private static final String PATCHING_TOOL_INFO_CACHE_KEY = "patchingToolInfo";
	
	private static final String PATCHING_TOOL_INFO_ARG = "info";
	
	private static final String ACTION_NAME = "refreshAction";

	private Map<String,Object> patchingToolInfoCache = new HashMap<String,Object>();
	
	
	@Override
	public void doView(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {
		
		if (LOG.isInfoEnabled()) {
			LOG.info("do view ...");
		}
		
		try {
			
			processView( renderRequest, renderResponse );
			
		} catch (Exception e) {
			
			LOG.error("Error processing view: " + e.getMessage());
			
            // SessionErrors.add(request, e.getClass().getName());
            // SessionErrors.add(request, "view-error-message", e.getClass().getName());
			renderRequest.setAttribute("view-error-type", e.getClass().getName());
			renderRequest.setAttribute("view-error-message", e.getMessage());

            // HttpSession session = request.getSession();
            // session.setAttribute("view-error-exception", e);
            // SessionErrors.add(session, "view-error-exception", e);
            // SessionErrors.add(request, "view-error-exception", e);
			renderRequest.setAttribute("view-error-exception", e);
			
			this.doView( ERROR_TEMPLATE_PATH, renderRequest, renderResponse );
			
		}
		
		if (LOG.isInfoEnabled()) {
			LOG.info("dispatch to view ...");
		}
		
		super.doView(renderRequest, renderResponse);
	}	
	
	
	protected void processView(
			RenderRequest renderRequest, RenderResponse renderResponse)
			throws Exception {
		
		if (LOG.isInfoEnabled()) {
			LOG.info("process view ...");
		}
		
		List<String> infoLines = lookupPatchingToolInfoLines();

		if (LOG.isInfoEnabled()) {
			LOG.info("adding " + infoLines.size() + " info lines to portlet session");
		}

		PortletSession session = renderRequest.getPortletSession();
		
		session.setAttribute(PortletConstants.SESSION_KEY_PATCHING_TOOL_INFO_LINES, infoLines);
	}
	
	
	protected List<String> lookupPatchingToolInfoLines() throws Exception {
		
		@SuppressWarnings("unchecked")
		List<String> infoLines = (List<String>) patchingToolInfoCache.get(PATCHING_TOOL_INFO_CACHE_KEY);

		if (infoLines == null || infoLines.isEmpty()) {

			if (LOG.isInfoEnabled()) {
				LOG.info("cache is empty");
			}
			
			infoLines = queryPatchingToolInfo();
			
			if (LOG.isInfoEnabled()) {
				LOG.info("adding " + infoLines.size() + " info lines to cache");
			}
			
			patchingToolInfoCache.put(PATCHING_TOOL_INFO_CACHE_KEY, infoLines);
		}
			
		if (LOG.isInfoEnabled()) {
			LOG.info("cache contains " + infoLines.size() + " info lines");
		}

		return infoLines;
	}

	
	public void doView(
			String viewTemplate,
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		include( viewTemplate, renderRequest, renderResponse );
	}
	

	public void refreshAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortletException {
		
		if (LOG.isInfoEnabled()) {
			LOG.info("process action " + ACTION_NAME + " ...");
			LOG.info("clear current cache ...");
		}

		this.patchingToolInfoCache.clear();
	}
	
	
	private List<String> queryPatchingToolInfo() throws Exception {
		
		if (LOG.isInfoEnabled()) {
			LOG.info("query patching tool info ...");
		}
		
		List<String> outputLines = new ArrayList<String>();
		
		try {
			
			List<String> cmdOptions = new ArrayList<String>();
			
			cmdOptions.add( PATCHING_TOOL_INFO_ARG );
			
			PatchingToolCommandRunner cmd = new PatchingToolCommandRunner();
			
			cmd.setPatchingToolOptions( cmdOptions );
			
			cmd.runCommand();
			
			outputLines = cmd.getCommandOutputLines();
						
		} catch (Exception e) {
			
			String msg = "Error querying patching tool info : " + e.getMessage();
			LOG.error(msg, e);
			throw new Exception(msg, e);
		}
		
		if (LOG.isInfoEnabled()) {
			LOG.info("patching tool info query returned " + outputLines.size() + " lines");
		}
		
		return outputLines;
	}

}
