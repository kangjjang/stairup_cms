<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.io.*"%>
<%@page import="dao.NoticeDao"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%
	int result = 0;
	String id = cookieBox.getValue("ID");
	int fileSize = 0;
	int memSeqNo = 0;
	memSeqNo = Constant.ADMIN_MEM_SEQ;
	NoticeDao dao = new NoticeDao();
	//	try {
try{
		String greetingContent = StringUtil.nchk(request.getParameter("greetingContent"), "");
		
		result = dao.insertGreeting(greetingContent);

	
	}catch(Exception ex){
		System.out.println(ex);
	}finally{
		dao.closeConn();
	}
	
	

	// 	} catch(Exception ex) {
	// 		System.out.println(ex);
	// 	}	

	if (result > 0) {
%>
		<script language=javascript>
			alert("등록되었습니다.");
			location.href = "greeting_list.jsp";
		</script>
<%
	} else {
%>
		<script language=javascript>
			alert("등록 실패했습니다.");
			location.href = "greeting_list.jsp";
		</script>
<%
	}
%>
