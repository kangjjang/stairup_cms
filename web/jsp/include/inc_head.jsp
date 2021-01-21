<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="util.CookieBox"%> 
<%
	response.setHeader("cache-control","no-cache"); 
	response.setHeader("expires","0");
	response.setHeader("pragma","no-cache"); 

	request.setCharacterEncoding("UTF-8");
	
	CookieBox cookieBox = new CookieBox(request);
	boolean login = cookieBox.exists("LOGIN")&& cookieBox.getValue("LOGIN").equals("SUCCESS");
	String role = cookieBox.getValue("ROLE");
	String memberId = cookieBox.getValue("ID");
	
	String addPath = null;
	String uploadFoler = null;
	uploadFoler = getServletContext().getRealPath("/") +  "files/";
	addPath = "";
%>
<%
	if(!login){
%>
		<script language="javascript">
			alert("로그인해주세요.");
			location = "../login.jsp";
		</script>
<%	} %>	
