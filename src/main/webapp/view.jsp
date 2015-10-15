<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<portlet:defineObjects />

<div id="patching_tool_info_details">
<%
String shell = System.getProperty("env.SHELL", "/bin/bash");
String command = System.getProperty("liferay.home") + "/patching-tool/patching-tool." + (shell.startsWith("/") ? "sh" : "bat") + " info";
Process p = Runtime.getRuntime().exec(command, new String[0], new java.io.File(System.getProperty("liferay.home")));
java.util.List<String> lines = org.apache.commons.io.IOUtils.readLines(p.getInputStream());
p.waitFor();
for (String line : lines) {
%>
<%= line %> <br>
<%
}
%>
</div>

