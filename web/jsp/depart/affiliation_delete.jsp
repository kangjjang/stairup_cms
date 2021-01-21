<%@page import="dao.DepartDao"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="util.StringUtil"%>
<%
	int affiliationSeq = Integer.parseInt(StringUtil.nchk(request.getParameter("affiliationSeq"), "0"));
	DepartDao dao = new DepartDao();
	int result = 0;
	result = dao.affiliationDelete(affiliationSeq);
		
	dao.closeConn();
	//if(dao.delete(no) > 0){  
	if(result > 0){  
%>
		<script language=javascript>
			alert("해당 소속과 하위 부서들이 삭제되었습니다.");  
			location.href = "affiliation_list.jsp";
		</script>
<%		
	}else{  
%>
		<script language=javascript>
			alert("소속 삭제에 실패하였습니다.");  
			location.href = "affiliation_list.jsp";
		</script>
<%	
	}
	//dao.closeConn();
%>
