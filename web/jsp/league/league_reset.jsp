<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.io.*"%>
<%@page import="java.io.File"%>
<%@page import="dao.DepartDao"%>
<%@page import="vo.DepartVO" %>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.*" %>
<%
	/*
	2015-05-28 ksy 부서 경기 결과 초기화
	*/
	DepartDao dao = new DepartDao();
	int result = 0;
	//	try {
try{
	
	result = dao.resetLeagueResult();
	
	}catch(Exception ex){
		System.out.println(ex);
	}finally{
		dao.closeConn();
	}
	if (result > 0) {
%>
		<script language=javascript>
			alert("초기화 되었습니다.");
			location.href = "league_list.jsp";
		</script>
<%
	} else{
%>
		<script language=javascript>
			alert("실패하였습니다.");
			location.href = "league_list.jsp";
		</script>
<%
	} 
%>
