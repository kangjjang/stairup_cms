<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="util.CookieBox"%> 
<%
	response.setHeader("cache-control","no-cache"); 
	response.setHeader("expires","0");
	response.setHeader("pragma","no-cache"); 

	request.setCharacterEncoding("UTF-8");
	
	String role = "3";
	String addPath = null;
	String uploadFoler = null;
	uploadFoler = getServletContext().getRealPath("/") +  "files/";
	addPath = "";
%>

	