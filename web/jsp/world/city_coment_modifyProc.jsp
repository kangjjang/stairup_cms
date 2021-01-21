<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.io.*"%>
<%@page import="dao.CityDao"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%
	int result = 0;
	CityDao dao = new CityDao();
try{
		int cityComentSeq = Integer.parseInt(StringUtil.nchk(request.getParameter("comentSeqNo"), "1"));
		String cityComent = StringUtil.nchk(request.getParameter("cityComent"), "");
		
		////System.out.println("cityComentSeq = "+cityComentSeq);
		////System.out.println("cityComent = "+cityComent);
		
		result = dao.updateCityGreeting(cityComentSeq, cityComent);
	
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
