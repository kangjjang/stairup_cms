<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="util.StringUtil"%>
<%@page import="dao.BeaconEventDao"%>

<%
	int no = Integer.parseInt(StringUtil.nchk(request.getParameter("no"), "0"));
	int result;
	
	BeaconEventDao dao = new BeaconEventDao();

	result = dao.deleteStampEvent(no);
	
	if(result > 0){
		dao.deleteStampEventBeacon(no);
	}
	
	dao.closeConn();
	
	if(result > 0){  
%>
		<script type="text/javascript">
			alert("삭제되었습니다.");  
			location.href = "stamp_beacon_list.jsp";
		</script>
<%		
	}else{  
%>
		<script type="text/javascript">
			alert("삭제에 실패하였습니다.");  
			location.href = "stamp_beacon_list.jsp";
		</script>
<%	
	}
%>
