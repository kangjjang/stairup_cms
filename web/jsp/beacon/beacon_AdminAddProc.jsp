<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.io.*"%>
<%@page import="java.io.File"%>
<%@page import="dao.BeaconEventDao"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="java.awt.Image"%>
<%@page import="com.sun.jimi.core.Jimi"%>
<%@page import="com.sun.jimi.core.JimiUtils"%>
<%

	String ROLE = cookieBox.getValue("ROLE");
	int roles = Integer.parseInt(ROLE);
	////System.out.println("roles = "+roles);
	
	BeaconEventDao dao = new BeaconEventDao();
	int result = 0;
	int minor = 0;
	try{		
		int beaconMajor = Integer.parseInt(StringUtil.nchk(request.getParameter("beaconMajor"), "0"));
		int startMinor = Integer.parseInt(StringUtil.nchk(request.getParameter("startMinor"), "0"));
		int endMinor = Integer.parseInt(StringUtil.nchk(request.getParameter("endMinor"), "0"));
		String beaconLocation = StringUtil.nchk(request.getParameter("beaconLocation"), "");
		String beaconPosition = StringUtil.nchk(request.getParameter("beaconPosition"), "");

		
		////System.out.println("beaconMajor = "+beaconMajor);
		////System.out.println("startMinor = "+startMinor);
		////System.out.println("endMinor = "+endMinor);
		////System.out.println("beaconLocation = "+beaconLocation);
		////System.out.println("beaconPosition = "+beaconPosition);
		for(int i = startMinor; i <= endMinor; i++){
			if(i == 0){
				;
			}else{
				if(i < 0){
					minor = i * -100;
				}else{
					minor = i;
				}
				////System.out.println("minor = "+minor);
				result = dao.insertBeaconEventAdmin(beaconMajor, minor, beaconLocation, beaconPosition);
			}
		}
		
		
	}catch(Exception ex){
		System.out.println(ex);
	}finally{
		dao.closeConn();
	}
	
	if (result > 0) {
%>
		<script language=javascript>
			alert("등록되었습니다.");
			location.href = "beacon_list.jsp";
		</script>
<%
	} else {
%>
		<script language=javascript>
			alert("등록 실패했습니다.");
			location.href = "beacon_list.jsp";
		</script>
<%
	}
%>
