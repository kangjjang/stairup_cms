<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="dao.MemberDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="util.StringUtil"%>
<%
	int memSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("memSeqNo"), "0"));
	int pageNo = Integer.parseInt(StringUtil.nchk(request.getParameter("pageNo"), "1"));
	int result =0;
	MemberDao dao = new dao.MemberDao(); 
	result = dao.delectMember(memSeqNo);
	dao.closeConn();
	if(result > 0){ 
		
%>
		<script language=javascript>
			alert("삭제되었습니다."); 
			location.href = "member_list.jsp?pageno=<%=pageNo%>"; 
		</script>
<%		
	}else{  //데이터가 존재하지 않는다면 
%>
		<script language=javascript>
			alert("삭제에 실패하였습니다."); 
			location.href = "member_list.jsp?pageno=<%=pageNo%>"; 
		</script>
<%	
	}
%>
