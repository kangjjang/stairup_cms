<%@ page contentType="text/html; charset=utf-8" %>
<%@page import="dao.AdminDao"%>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>

<%
	String id = cookieBox.getValue("ID");
	int result = 0; 
	int memSeqNo = 0;
	memSeqNo = Constant.ADMIN_MEM_SEQ;
	AdminDao dao = new AdminDao();
	
	try{
		int affiliationSeq = Integer.parseInt(StringUtil.nchk(request.getParameter("affiliationSeq"),"0"));
		String adminId = StringUtil.nchk(request.getParameter("adminId"),"");
		String adminPw = StringUtil.nchk(request.getParameter("adminPw"),"");
		String adminName = StringUtil.nchk(request.getParameter("adminName"),"");
		String adminPhone = StringUtil.nchk(request.getParameter("adminPhone"),"");

		String affiliationName = dao.selectAdminAffiliation(affiliationSeq);
		if(affiliationSeq == 0){
			affiliationName = "전체 관리자";
		}

		result = dao.insertMember(affiliationSeq, adminId, adminPw, adminName, adminPhone, affiliationName);

	}catch(Exception ex){
		System.out.println(ex);
	}finally{
		dao.closeConn();
	}
	
	
	if(result > 0){ 
%>
		<script language=javascript>
			alert("등록되었습니다.");
			location.href = "admin_list.jsp";
		</script>
<%
	}else{
%>
		<script language=javascript>
			alert("등록 실패했습니다."); 
			location.href = "admin_list.jsp"; 
		</script>
<%
	}
%>
