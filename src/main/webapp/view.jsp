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

<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
 
<portlet:defineObjects />

<%
List<String> patchingToolInfoLines = new ArrayList<String>();

if (portletSession.getAttribute("patchingToolInfoLines") != null) {
	patchingToolInfoLines = (List<String>) portletSession.getAttribute("patchingToolInfoLines");
}
%>

<div id="patching_tool_info_controls">
<portlet:actionURL name="refreshPatchingToolInfoAction" var="refreshPatchingToolInfoActionURL"></portlet:actionURL>

<aui:form action="<%= refreshPatchingToolInfoActionURL %>" method="post" name="fm">
	<aui:button type="submit" value="Refresh" />
</aui:form>
</div>

<hr>

<div id="patching_tool_info_details">
<%
for (String line : patchingToolInfoLines) {
%>
<%= line %> <br>
<%
}
%>
</div>

<hr>

<br>
