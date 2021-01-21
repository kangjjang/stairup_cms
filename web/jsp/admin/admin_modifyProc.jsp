<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.io.*"%>
<%@page import="vo.AdminVO"%>
<%@page import="dao.AdminDao" %>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%
	int result = 0;
	String id = cookieBox.getValue("ID");
	int fileSize = 0;
	int memSeqNo = 0;
	memSeqNo = Constant.ADMIN_MEM_SEQ;
	AdminDao dao = new AdminDao();
	try{	
		int adminSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("no"), "0"));
		String memberName = StringUtil.nchk(request.getParameter("adminName"), "");
		String memberPhone = StringUtil.nchk(request.getParameter("adminPhone"), "");
		int affiliationSeq = Integer.parseInt(StringUtil.nchk(request.getParameter("affiliationSeq"), "0"));
		
		////System.out.println("adminSeqNo = "+adminSeqNo);
		////System.out.println("memberName = "+memberName);
		////System.out.println("memberPhone = "+memberPhone);
		////System.out.println("affiliationSeq = "+affiliationSeq);
		
		String affiliationName = dao.selectAdminAffiliation(affiliationSeq);
		if(affiliationSeq == 0){
			affiliationName = "전체 관리자";
		}
		result = dao.updateAdmin(adminSeqNo, memberName, memberPhone, affiliationName, affiliationSeq);

	
	}catch(Exception ex){
		System.out.println(ex);
	}finally{
		dao.closeConn();
	}

	if (result > 0) {
%>
		<script language=javascript>
			alert("수정 되었습니다.");
			location.href = "admin_list.jsp";
		</script>
<%
	} else {
%>
		<script language=javascript>
			alert("수정 실패했습니다.");
			location.href = "admin_list.jsp";
		</script>
<%
	}
%>
