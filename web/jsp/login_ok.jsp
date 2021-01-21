<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@page import="java.util.ArrayList"%>
<%@page import="vo.AdminVO"%>
<%@page import="dao.AdminDao"%>
<%@ page import="util.StringUtil"%>
<%@ page import="util.CookieBox"%>
<%@page import="util.HashUtil" %>
<%
	String id = request.getParameter("id");
	String password = request.getParameter("password");
	
	////System.out.println("id = "+id);
	////System.out.println("password = "+password);
	
	AdminDao dao = new dao.AdminDao();
	AdminVO vo = new vo.AdminVO();
	
	vo = dao.loginMember(id, password);
	////System.out.println("123 ");
	String memseq = String.valueOf(vo.getNo());
	////System.out.println("123 ");
	////System.out.println("로그인 : "+vo.getMemberRole());
	
	dao.closeConn();
	
	if( vo.getNo() == 0) {
%>
	<script language="javascript">
		alert("아이디와 비밀번호를 확인후 다시 로그인해주세요.");
		history.back();
	</script>	
<%	
	}else{
		response.addCookie(CookieBox.createCookie("LOGIN", "SUCCESS", "/", -1));
		response.addCookie(CookieBox.createCookie("ID", id, "/", -1));
		response.addCookie(CookieBox.createCookie("MEM_SEQ", memseq, "/", -1));
		/* response.addCookie(CookieBox.createCookie("ROLE", vo.getMemberRole(), "/", -1)); */
		response.addCookie(CookieBox.createCookie("ROLE", vo.getMemberRole(), "/", -1));
		//response.sendRedirect("../jsp/general/news_list.jsp");
		response.sendRedirect("../jsp/member/member_list.jsp");
}
%>

