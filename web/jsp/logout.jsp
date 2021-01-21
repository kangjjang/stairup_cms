<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="util.CookieBox"%>
<%
	response.addCookie(CookieBox.createCookie("LOGIN", "", "/", 0));
	response.addCookie(CookieBox.createCookie("ID", "", "/", 0));
 	response.sendRedirect("../jsp/login.jsp");
%>
