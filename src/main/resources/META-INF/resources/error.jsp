<%--
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
--%>

<%@ include file="init.jsp" %>

<%
String portletToolErrorMessage = (String) portletSession.getAttribute( PortletKeys.REQUEST_KEY_PATCHING_TOOL_ERROR_MESSAGE );
%>
<portlet:actionURL name="refreshAction" var="refreshActionURL"></portlet:actionURL>

<div class="container-fluid-1280">
	<aui:form action="${refreshActionURL}" method="post" name="fm">
	
		<liferay-ui:error key="error" message="unable-to-run-patching-tool" />
		
		<% if (portletToolErrorMessage != null) { %>
			<div class="portlet-msg-error"> 
				<p>
					<b>Error Message:</b> <%= portletToolErrorMessage %>
				</p>
			</div>
		<% } %>
		<div class="button-holder">
			<aui:button-row>
				<aui:button primary="true" type="submit" value="refresh" />
			</aui:button-row>
		</div>
	</aui:form>
</div>