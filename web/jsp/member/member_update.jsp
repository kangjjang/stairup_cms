<%@page import="util.ResultCode"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head_member.jsp" %>
<%@page import="dao.MemberDao"%>
<%@page import="dao.DeviceDao"%>
<%@page import="vo.DeviceVO" %>
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil" %>
<%
	
	int memSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("memSeqNo"), "0"));
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageNo"), "1"));
	
	MemberDao dao = new MemberDao();
	int result = dao.memberApprove(memSeqNo);
	dao.closeConn();
	if(result > 0){ 
		//##########################push메세지 보내기#############################
				
				/* DeviceDao deviceDao = new DeviceDao();
				DeviceVO list = deviceDao.getDeviceToken(memSeqNo);
				String phoneSerial = list.getDeviceToken();
				//안드로이드의 경우 배열에 대해서 발송
				PushServiceAndroid pushServiceAndroid = new PushServiceAndroid();
				pushServiceAndroid.pushGcm(phoneSerial,memSeqNo);
				deviceDao.closeConn(); */
				//	pushServiceAndroid.pushGcm(regidAndroidSeper, index, ticker, title, msg);
%>
			<script language=javascript>
			alert("승인이 완료되었습니다."); 
			location.href = "member_list.jsp?pageno=<%=pageno%>"; 
		</script>
<%
	}else{  //필수 입력해야 하는 데이터가  입력이 되지 않았다면 
%>
		<script language=javascript>
			alert("승인에 실패하였습니다."); 
			location.href = "member_list.jsp?pageno=<%=pageno%>"; 
		</script>
<%
	}
%>
