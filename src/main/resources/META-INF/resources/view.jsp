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
PatchingToolResults patchingToolResults = new PatchingToolResults();
int patchingToolExitValue = 0;
List<String> patchingToolOutputLines = new ArrayList<String>();
List<String> patchingToolErrorLines = new ArrayList<String>();

if (portletSession.getAttribute(PortletKeys.SESSION_KEY_PATCHING_TOOL_RESULTS) != null) {
	patchingToolResults = (PatchingToolResults) portletSession.getAttribute(PortletKeys.SESSION_KEY_PATCHING_TOOL_RESULTS);
	patchingToolExitValue = patchingToolResults.getExitValue();
	patchingToolOutputLines = patchingToolResults.getOutputLines();
	patchingToolErrorLines = patchingToolResults.getErrorLines();
}

request.setAttribute("patchingToolOutputLines", patchingToolOutputLines);
request.setAttribute("patchingToolErrorLines", patchingToolErrorLines);
%>

<portlet:actionURL name="refreshAction" var="refreshActionURL"></portlet:actionURL>

<div class="container-fluid-1280">
	<aui:form action="${refreshActionURL}" method="post" name="fm">
		<div class="card-horizontal main-content-card">
			<c:if test="${empty patchingToolOutputLines}">
				<liferay-ui:success key="success" message="patching-tool-has-no-results" />
			</c:if>
			
			<c:if test="${not empty patchingToolOutputLines}">
				<div id="patching_tool_output_lines">
					<p><pre><c:forEach items="${patchingToolOutputLines}" var="line"><c:out value="${line}"/><br></c:forEach></pre></p>
				</div>
			</c:if>
			
			<c:if test="${not empty patchingToolErrorLines}">
				<div id="patching_tool_error_lines">
					<p><pre><c:forEach items="${patchingToolErrorLines}" var="line"><c:out value="${line}"/><br></c:forEach></pre></p>
				</div>
			</c:if>
		</div>
		<div class="button-holder">
			<aui:button-row>
				<aui:button primary="true" type="submit" value="refresh" />
			</aui:button-row>
		</div>
	</aui:form>
</div>