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
String viewErrorType = (String) request.getAttribute("view-error-type");
String viewErrorMessage = (String) request.getAttribute("view-error-message");
java.lang.Exception viewErrorException = (java.lang.Exception) request.getAttribute("view-error-exception");
%>

<h3 class="portlet-msg-error">
	<liferay-ui:message key="unable-to-process-patching-tool-info-request" />
</h3>

<%--
<div>
	<liferay-ui:error key="view-error-message" />
	<hr>
	<liferay-ui:error key="view-error-exception" />
</div>
--%>

<div class="portlet-msg-error">
<p>
<% if (viewErrorType != null) { %>
<b>View Error Type:</b> <%= viewErrorType %>
<% } %>
</p>
<p>
<% if (viewErrorMessage != null) { %>
<b>View Error Message:</b> <%= viewErrorMessage %>
<% } %>
</p>
<%--
<pre>
<% if (viewErrorException != null) { %>
<%= viewErrorException.getMessage() %>
<% } %>
</pre>
--%>
</div>
