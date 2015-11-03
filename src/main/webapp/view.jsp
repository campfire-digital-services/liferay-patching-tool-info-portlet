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
List<String> patchingToolInfoLines = new ArrayList<String>();

if (portletSession.getAttribute(PortletConstants.SESSION_KEY_PATCHING_TOOL_INFO_LINES) != null) {
	patchingToolInfoLines = (List<String>) portletSession.getAttribute(PortletConstants.SESSION_KEY_PATCHING_TOOL_INFO_LINES);	
}

/*
String lineSep = System.lineSeparator();
List<String> newPatchingToolInfoLines = StringUtilsHelper.strip(patchingToolInfoLines, lineSep); 
patchingToolInfoLines = newPatchingToolInfoLines;
*/

request.setAttribute("patchingToolInfoLines", patchingToolInfoLines);
%>

<div id="patching_tool_info_controls">
<portlet:actionURL name="refreshAction" var="refreshActionURL"></portlet:actionURL>

<aui:form action="<%= refreshActionURL %>" method="post" name="fm">
	<aui:button type="submit" value="Refresh" />
</aui:form>
</div>

<hr>

<%--
<div id="patching_tool_info_details">
<pre>
<% for (String line : patchingToolInfoLines) { %>
<%= line %>
<% } %>
<br>
</pre>
</div>
 --%>

<%--
<div id="patching_tool_info_details">
<pre>
<c:forEach items="${patchingToolInfoLines}" var="line">
   <c:out value="${line}"/>
</c:forEach>
</pre>
</div>
--%>

<div id="patching_tool_info_details">
<c:forEach items="${patchingToolInfoLines}" var="line">
   <c:out value="${line}"/> <br>
</c:forEach>
</div>

<hr>

<br>
