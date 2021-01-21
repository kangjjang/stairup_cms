<%@page import="dao.DonationDao"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="util.StringUtil"%>
<%
	int giveSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("giveSeqNo"), "0"));
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "0"));
	int result=0;
	DonationDao dao = new DonationDao();
	result = dao.deleteGive(giveSeqNo);
	dao.closeConn();
	if(result > 0){  
%>
		<script language=javascript>
			alert("삭제되었습니다.");  
			location.href = "give_list.jsp";
		</script>
<%		
	}else{  
%>
		<script language=javascript>
			alert("삭제에 실패하였습니다.");  
			location.href = "give_view.jsp?giveSeqNo="+giveSeqNo+"&pageno="+pageno;
		</script>
<%	
	}
%>