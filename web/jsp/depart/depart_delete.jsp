<%@page import="dao.DepartDao"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="util.StringUtil"%>
<%
	int departSeq = Integer.parseInt(StringUtil.nchk(request.getParameter("departSeq"), "0"));
	////System.out.println("departSeq = "+departSeq);
	
	DepartDao dao = new DepartDao();
	int result = 0;
	int person = 0;
	
	person = dao.selectDepartMember(departSeq);
	////System.out.println("person = "+person);
	
	if(person == 0){
		
		result = dao.departDelete(departSeq);
		
		dao.closeConn();

		if(result > 0){  
%>
			<script language=javascript>
				alert("해당 부서가 삭제되었습니다.");  
				location.href = "depart_list.jsp";
			</script>
<%		
		}else{  
%>
			<script language=javascript>
				alert("해당 부서 삭제에 실패하였습니다.");  
				location.href = "depart_list.jsp";
			</script>
<%	
		}
	}else{
		%>
		<script language=javascript>
			alert("해당 부서에 소속된 인원이 있습니다.");  
			location.href = "depart_list.jsp";
		</script>
<%	
	}
%>

