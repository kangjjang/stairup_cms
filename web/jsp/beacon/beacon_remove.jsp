<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="util.StringUtil"%>
<%@page import="dao.BeaconEventDao"%>
<%@page import="vo.BeaconEventVO"%>

<%
	int no = Integer.parseInt(StringUtil.nchk(request.getParameter("no"), "0"));
	int result = 0;
	
	BeaconEventDao dao = new BeaconEventDao();
	BeaconEventVO vo = new BeaconEventVO();
	vo = dao.BeaconEventView(no);
	
	result = dao.BeaconEventdelete(no);
	
	if(result > 0){
		dao.BeaconEventLogDelete(vo.getBeaconMajor(),vo.getBeaconMinor());
	}
	
	dao.closeConn();
	
	if(result > 0){  
%>
		<script type="text/javascript">
			alert("삭제되었습니다.");  
			location.href = "beacon_list.jsp";
		</script>
<%		
	}else{  
%>
		<script type="text/javascript">
			alert("삭제에 실패하였습니다.");  
			location.href = "beacon_list.jsp";
		</script>
<%	
	}
%>
