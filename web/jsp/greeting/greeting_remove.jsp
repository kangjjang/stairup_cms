<%@page import="dao.NoticeDao"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="util.StringUtil"%>
<%
	int greetingSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("greetingSeqNo"), "0"));
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "0"));
	int result=0;
	NoticeDao dao = new NoticeDao();
	result = dao.deleteGreeting(greetingSeqNo);
	dao.closeConn();
	
	if(result > 0){  
%>
		<script language=javascript>
			alert("삭제되었습니다.");  
			location.href = "greeting_list.jsp";
		</script>
<%		
	}else{  
%>
		<script language=javascript>
			alert("삭제에 실패하였습니다.");  
			location.href = "greeting_list.jsp";
		</script>
<%	
	}
%>