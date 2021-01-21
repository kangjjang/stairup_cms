<%@page import="dao.BeaconEventDao"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="util.StringUtil"%>
<%
	int beaconMajor = Integer.parseInt(StringUtil.nchk(request.getParameter("beaconMajor"), "0"));
	BeaconEventDao dao = new BeaconEventDao();
	int result = 0;
	result = dao.BeaconAdmindelete(beaconMajor);
		
	dao.closeConn();
	//if(dao.delete(no) > 0){  
	if(result > 0){  
%>
		<script language=javascript>
			alert("삭제되었습니다.");  
			location.href = "beacon_list.jsp";
		</script>
<%		
	}else{  
%>
		<script language=javascript>
			alert("삭제에 실패하였습니다.");  
			location.href = "beacon_list.jsp";
		</script>
<%	
	}
	//dao.closeConn();
%>
