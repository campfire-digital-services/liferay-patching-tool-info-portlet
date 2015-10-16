<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

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
