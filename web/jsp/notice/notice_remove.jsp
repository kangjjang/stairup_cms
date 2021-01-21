<%@page import="dao.NoticeDao"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="util.StringUtil"%>
<%
	int no = Integer.parseInt(StringUtil.nchk(request.getParameter("no"), "0"));
	NoticeDao dao = new NoticeDao();
	int result = 0;
	result = dao.delete(no);
		
	dao.closeConn();
	//if(dao.delete(no) > 0){  
	if(result > 0){  
%>
		<script language=javascript>
			alert("삭제되었습니다.");  
			location.href = "notice_list.jsp";
		</script>
<%		
	}else{  
%>
		<script language=javascript>
			alert("삭제에 실패하였습니다.");  
			location.href = "notice_list.jsp";
		</script>
<%	
	}
	//dao.closeConn();
%>
