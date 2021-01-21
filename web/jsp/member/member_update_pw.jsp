<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="dao.MemberDao"%>
<%@page import="vo.MemberVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="util.HashUtil"%>
<%@page import="util.StringUtil"%>
<%@page import="java.net.URLDecoder"%>
<%
	int memSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("memSeqNo"), "0"));
	int pageNo = Integer.parseInt(StringUtil.nchk(request.getParameter("pageNo"), "1"));
	String memPw = URLDecoder.decode(StringUtil.nchk(request.getParameter("memPw"),""), "UTF-8");	//부서 seq


    int result =0;
	String memberPw = "";
	
	MemberDao dao = new dao.MemberDao(); 
	MemberVO vo = new MemberVO();
	
	vo = dao.SelectCmsMemInfo(memSeqNo);  //회원 아이디 검색
	memberPw = HashUtil.encryptPassword(vo.getMemId().trim(), memPw);  // 비밀번호 암호화

    result = dao.updateMemberPw(memSeqNo, memberPw);

	dao.closeConn();

	if(result > 0){ 
		
%>
		<script language=javascript>
			alert("변경되었습니다.."); 
			location.href = "member_list.jsp?pageno=<%=pageNo%>"; 
		</script>
<%		
	}else{  //데이터가 존재하지 않는다면 
%>
		<script language=javascript>
			alert("변경에 실패하였습니다."); 
			location.href = "member_list.jsp?pageno=<%=pageNo%>"; 
		</script>
<%	
	}
%>
