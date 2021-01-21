<%@page import="dao.EventDao"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="util.StringUtil"%>
<%@page import="java.util.ArrayList"%>
<%
	int no = Integer.parseInt(StringUtil.nchk(request.getParameter("no"), "0")); 
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1")); 
	int memSeqNo = 1;

	EventDao dao = new EventDao();  
		
	if(dao.delete(no) > 0){  
%>
		<script language=javascript>
			alert("삭제되었습니다."); 
			location.href = "event_list.jsp?pageno=<%=pageno%>";
		</script>
<%		
	}else{         
%>
		<script language=javascript>
			alert("삭제에 실패하였습니다."); 
			location.href = "event_list.jsp?pageno=<%=pageno%>";
		</script>
<%	
	}
	dao.closeConn();
%>