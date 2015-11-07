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
import au.com.permeance.liferay.portlet.patchingtoolinfo.cli.PatchingToolResults;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
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
 * @see PatchingToolCommandRunner
 * @see PatchingToolResults
 */
public class PatchingToolInfoMVCPortlet extends MVCPortlet {
	
	private static final Log LOG = LogFactoryUtil.getLog(PatchingToolInfoMVCPortlet.class);

	private static final String TEMPLATE_PAGE_PATH_ERROR = "/error.jsp";
	
	private static final String CACHE_KEY_PATCHING_TOOL_RESULTS = "patchingToolResults";
	
	private static final String PATCHING_TOOL_OPTION_INFO = "info";
	
	private static final String DEFAULT_PATCHING_OPTION_DEFAULT = PATCHING_TOOL_OPTION_INFO;
	
	private Map<String,Object> patchingToolInfoCache = new HashMap<String,Object>();
	
	
	@Override
	public void doView(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {
		
		if (LOG.isInfoEnabled()) {
			LOG.info("do view ...");
		}
		
		try {
			
			PatchingToolResults patchingToolResults = lookupPatchingToolResults();

			if (LOG.isInfoEnabled()) {
				LOG.info("adding patching tool results to portlet session : " + patchingToolResults);
			}
			
			SessionMessages.add(renderRequest, "success");

			PortletSession portletSession = renderRequest.getPortletSession();
			
			portletSession.setAttribute( PortletKeys.SESSION_KEY_PATCHING_TOOL_RESULTS, patchingToolResults );
			
			super.doView(renderRequest, renderResponse);
			
		} catch (Exception e) {
			
			LOG.error("Error processing view: " + e.getMessage());
			
			SessionErrors.add(renderRequest, "error");
			
			PortletSession portletSession = renderRequest.getPortletSession();
			
			portletSession.setAttribute( PortletKeys.REQUEST_KEY_PATCHING_TOOL_ERROR_TYPE, e.getClass().getName() );
			portletSession.setAttribute( PortletKeys.REQUEST_KEY_PATCHING_TOOL_ERROR_MESSAGE, e.getMessage() );
			portletSession.setAttribute( PortletKeys.REQUEST_KEY_PATCHING_TOOL_ERROR_EXCEPTION, e );
			
			include( TEMPLATE_PAGE_PATH_ERROR, renderRequest, renderResponse );
		}
		
		if (LOG.isInfoEnabled()) {
			LOG.info("dispatch to view ...");
		}
	}	
	
	
	protected PatchingToolResults lookupPatchingToolResults() throws Exception {
		
		@SuppressWarnings("unchecked")
		PatchingToolResults patchingToolResults = (PatchingToolResults) patchingToolInfoCache.get( CACHE_KEY_PATCHING_TOOL_RESULTS );

		if (patchingToolResults == null) {

			if (LOG.isInfoEnabled()) {
				LOG.info("cache is empty");
			}
			
			patchingToolResults = runPatchingTool();
			
			if (LOG.isInfoEnabled()) {
				LOG.info("adding patching tool results to cache: " + patchingToolResults);
			}
			
			patchingToolInfoCache.put( CACHE_KEY_PATCHING_TOOL_RESULTS, patchingToolResults );
		}
			
		if (LOG.isInfoEnabled()) {
			LOG.info("cache contains patching tool results: " + patchingToolResults);
		}

		return patchingToolResults;
	}

	
	public void refreshAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortletException {
		
		final String ACTION_NAME = "refreshAction";
		
		if (LOG.isInfoEnabled()) {
			LOG.info("process action " + ACTION_NAME + " ...");
			LOG.info("clear current cache ...");
		}

		this.patchingToolInfoCache.clear();
	}
	

	private PatchingToolResults runPatchingTool() throws Exception {
		
		List<String> commandOptions = new ArrayList<String>();
		commandOptions.add( DEFAULT_PATCHING_OPTION_DEFAULT );
		PatchingToolResults patchingToolResults = runPatchingTool( commandOptions );
		return patchingToolResults;
	}
	
	
	private PatchingToolResults runPatchingTool( List<String> commandOptions ) throws Exception {
		
		if (LOG.isInfoEnabled()) {
			LOG.info("run patching tool ...");
		}
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("patching tool command options : " + commandOptions);
		}
		
		if (commandOptions == null) {
			commandOptions = new ArrayList<String>();
		}
		
		if (commandOptions.isEmpty()) {
			commandOptions.add( DEFAULT_PATCHING_OPTION_DEFAULT );
		}
		
		PatchingToolResults patchingToolResults = new PatchingToolResults();
		
		try {
			
			PatchingToolCommandRunner commandRunner = new PatchingToolCommandRunner();
			
			commandRunner.setPatchingToolOptions( commandOptions );
			
			commandRunner.runCommand();
			
			patchingToolResults = commandRunner.getPatchingToolResults();
						
		} catch (Exception e) {
			
			String msg = "Error running patching tool : " + e.getMessage();
			LOG.error(msg, e);
			throw new Exception(msg, e);
		}
		
		if (LOG.isInfoEnabled()) {
			LOG.info("patching tool returned results : " + patchingToolResults);
		}
		
		return patchingToolResults;
	}

}
