<%@page import="vo.GreetingVO"%>
<%@page import="dao.NoticeDao"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="net.fckeditor.*"%>
<%@ taglib uri="http://java.fckeditor.net" prefix="FCK" %>
<%
String menuCate = Constant.MENU_CONTENT;
String subMenuCate = Constant.SUBMENU_TRAIN_BAORD;
	
	int greetingSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("greetingSeqNo"), "1")); //요청하여 받은 값을 no변수에 저장 
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1")); //요청하여 받은 값을 pageno변수에 저장 

	NoticeDao dao = new NoticeDao();
	GreetingVO vo = new GreetingVO();
	vo = dao.greetingModify(greetingSeqNo); //vo에 저장 
	dao.closeConn();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<%@ include file="../include/inc_top.jsp" %>
					
	</head>
	<body>
		<div id="wrap">
			<%@ include file="../include/inc_top_menu.jsp" %>		
			<div id="contentWrap">  
				<%@ include file="../include/inc_left_menu.jsp" %>	  
				<div class="contentArea">
					<h2 class="contentTitle">시장 인사말 수정</h2>
					<form name="frm" action="greeting_modifyProc.jsp" method="post">
					<input type="hidden" name="greetingSeqNo" value="<%=greetingSeqNo%>">
					<input type="hidden" name="pageno" value="<%=pageno%>">
					<div class="contentForm">
						<table class="detailInfoOneItem" style="margin-top: -20px;">
							<thead><tr><td colspan="2"  style="text-align:right;border-top:none;">* 필수입력</td></tr></thead>
							<tbody>
							<tr class="detailInfoOneItemFirst"> 
							  <th>시장 인사말</th>
							  <td><input type="text" name="greetingContent" value="<%=vo.getGreetingContent() %>" size="50" class="box formfield" /></td>
							</tr>
							</tbody>
						</table>
					</div>
					<div class="detailInfoButton">
				      	<input type="submit" value="수정" />
				      	<input type="button" OnClick="location.href='greeting_list.jsp?pageno=<%=pageno%>';" value="취소" />
				    </div>
					</form>
				</div>
			</div>
			<div id="bottomWrap"></div>
		
		</div>
		<div style="display: none;"></div>
	</body>
</html>