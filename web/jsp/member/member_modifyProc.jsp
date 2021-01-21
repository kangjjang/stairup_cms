<%@ page contentType="text/html; charset=utf-8" %>
<%@page import="java.io.*"%>
<%@page import="dao.MemberDao" %>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="java.net.URLDecoder"%> 
<%@page import="util.HashUtil"%>
<%
	request.setCharacterEncoding("UTF-8");

	int result = 0;
	
	MemberDao dao = new MemberDao();
	
	int memSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("no"), "0"));
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "0"));
	
	String memId = URLDecoder.decode(StringUtil.nchk(request.getParameter("memId"), ""), "UTF-8");
	String memName = URLDecoder.decode(StringUtil.nchk(request.getParameter("memName"), ""), "UTF-8");
	int affiliationSeq = Integer.parseInt(StringUtil.nchk(request.getParameter("affiliationSeq"), "0"));
	int departSeq = Integer.parseInt(StringUtil.nchk(request.getParameter("departSeq"), "0"));
	
	int nowaffiliation = dao.selectMemberAffiliation(memSeqNo);
	
	dao.memberMainaffilUpdate(memSeqNo, affiliationSeq, departSeq);   				// 1. - 2. MEM_AFFILIATION  - 메인 소속 변경
	dao.updateMasterInfo(memSeqNo, affiliationSeq, departSeq);						// 3. master Table 변경// 4. 마스터 테이블 변경
	dao.memberMayorUpdate(memSeqNo, nowaffiliation); 								// 5. 시장 정보 삭제
	
	String memberPw = HashUtil.encryptPassword(memId, memId);  // 비밀번호 암호화
	
	result = dao.memberUpdate(memId, memberPw, memName, affiliationSeq, departSeq, memSeqNo);   //1. 회원 정보 변경
	
	dao.closeConn();
	
	if (result > 0) {
%>
		<script type="text/javascript">
			alert("수정 되었습니다.");
			location.href = "member_list.jsp?pageno=<%=pageno%>";
		</script>
<%
	} else {
%>
		<script type="text/javascript">
			alert("수정 실패했습니다.");
			location.href = "member_list.jsp?pageno=<%=pageno%>";
		</script>
<%
	}
%>
