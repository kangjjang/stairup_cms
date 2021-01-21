<%@page import="vo.AdminVO"%>
<%@page import="dao.AdminDao" %>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="util.StringUtil"%>
<%
	int adminSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("no"), "0"));
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "0"));
	int result=0;
	AdminDao dao = new AdminDao();
	result = dao.deleteMember(adminSeqNo);
	dao.closeConn();
	
	if(result > 0){  
%>
		<script language=javascript>
			alert("삭제되었습니다.");  
			location.href = "admin_list.jsp";
		</script>
<%		
	}else{  
%>
		<script language=javascript>
			alert("삭제에 실패하였습니다.");  
			location.href = "admin_list.jsp";
		</script>
<%	
	}
%>