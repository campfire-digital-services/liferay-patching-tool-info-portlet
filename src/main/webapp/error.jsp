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

<%@ include file="/init.jsp" %>

<%
String portletToolErrorType = (String) portletSession.getAttribute( PortletKeys.REQUEST_KEY_PATCHING_TOOL_ERROR_TYPE );
String portletToolErrorMessage = (String) portletSession.getAttribute( PortletKeys.REQUEST_KEY_PATCHING_TOOL_ERROR_MESSAGE );
java.lang.Exception portletToolErrorException = (java.lang.Exception) portletSession.getAttribute( PortletKeys.REQUEST_KEY_PATCHING_TOOL_ERROR_EXCEPTION );
%>

<liferay-ui:error key="error" message="unable-to-run-patching-tool" />

<div class="portlet-msg-error"> 
<p>
<% if (portletToolErrorType != null) { %>
<b>Error Type:</b> <%= portletToolErrorType %>
<% } %>
</p>
<p>
<% if (portletToolErrorMessage != null) { %>
<b>Error Message:</b> <%= portletToolErrorMessage %>
<% } %>
</p>
<p>
<% if (portletToolErrorException != null) { %>
<b>Error Exception:</b> <%= portletToolErrorException %>
<% } %>
</p>
</div>

<hr>
