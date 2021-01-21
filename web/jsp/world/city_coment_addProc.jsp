<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.io.*"%>
<%@page import="dao.CityDao"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%
	int result = 0;
	String id = cookieBox.getValue("ID");
	CityDao dao = new CityDao();
	//	try {
try{
		int	citySeq = Integer.parseInt(StringUtil.nchk(request.getParameter("citySeq"),"0"));
		String cityComent = StringUtil.nchk(request.getParameter("cityComent"), "");
		int orderly = dao.selectCityOrderly(citySeq);
		
		////System.out.println("orderly = "+orderly);
		result = dao.insertCityGreeting(citySeq, cityComent, orderly);

	
	}catch(Exception ex){
		System.out.println(ex);
	}finally{
		dao.closeConn();
	}
	
	if (result > 0) {
%>
		<script language=javascript>
			alert("등록되었습니다.");
			location.href = "city_coment_list.jsp";
		</script>
<%
	} else {
%>
		<script language=javascript>
			alert("등록 실패했습니다.");
			location.href = "city_coment_list.jsp";
		</script>
<%
	}
%>
