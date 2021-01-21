<%@page import="dao.NoticeDao"%>
<%@page import="vo.NoticeVO"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList" %>
<%@page import="vo.NoticeVO" %>
<%@page import="dao.NoticeDao" %>
<%@page import="util.StringUtil" %>
<%@page import="util.Constant" %>

<%
	String menuCate = Constant.MENU_NOTICE;
	String subMenuCate = Constant.SUBMENU_NOTICE;
	int no = Integer.parseInt(StringUtil.nchk(request.getParameter("no"), "1"));
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));

	NoticeDao dao = new NoticeDao();
	NoticeVO vo = new NoticeVO();
	vo = dao.noticeView(no);
	String crtDate = vo.getCrtDate().substring(0,16);
	dao.closeConn();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<title>공지 상세 정보</title>
	<%@ include file="../include/inc_top.jsp" %>
	<script language="javascript">
	$(function(){
		$('div.topMenuContent ul li:nth-child(<%=menuCate%>)').addClass('topMenuOn');
		$('div.menuList ul li:nth-child(<%=subMenuCate%>)').addClass('leftMenuOn');
		
		$('input:submit, input:button, button').button();
	});
	
	function memberDel(no, pageno){
		if(confirm('삭제하시겠습니까?')){
			location.href = 'notice_remove.jsp?no='+no+'&pageno='+pageno;
		}			
	}	
	</script>
</head>

<body>
	<div id="wrap">
		<%@ include file="../include/inc_top_menu.jsp" %>		
		<div id="contentWrap">  
				<%@ include file="../include/inc_left_menu.jsp" %>	  
				<div class="contentArea">
					<h2 class="contentTitle">공지 상세 정보</h2>		
							
					<div class="contentForm">
						<form name="frm" action="member_addProc.jsp" method="post">
						<div class="listMenu"><p>공지 정보</p></div>	
						<table class="detailInfo">
							<tbody>
							<tr class="detailInfoFirst"> 
								<th>제목</th>
							  	<td><%=vo.getNotiTitle() %></td>
							  	<th>등록일자</th>
							  	<td><%=crtDate%></td>
							</tr>
							<%
							String affiliationName = "";
							if(vo.getAffiliationSeq() == 0){
								affiliationName = "전체";
							}else{
								affiliationName = vo.getAffiliationName();
							}
							%>
							<tr> 
								<th>소속</th>
							  	<td colspan="3"><%=affiliationName %></td>
							</tr>
							<tr>
							<th>이미지</th>
							<td colspan="3">
								<%if(!vo.getNoticePic().equals("") ){%>
									<img src="/upload/notice/<%=vo.getNoticePic() %>" width="150" height="100" />
								<%}else{%> 이미지 없음<%} %>
							</td>
							</tr>
							<tr>
								<th>공지내용</th>
							  	<td colspan="3"><%=vo.getNotiContent()%></td>
							</tr>
						  	</tbody>
						</table>
						</form>
					</div>
					<div class="alignRightButton">
						<input type="button" OnClick="location.href='notice_modify.jsp?no=<%=no%>&pageno=<%=pageno%>';" value="수정" />
				      	<input type="button" OnClick="memberDel('<%=no%>', '<%=pageno%>');" value="삭제" />
				      	<input type="button" OnClick="location.href='notice_list.jsp?pageno=<%=pageno%>';" value="목록" />
				    </div>				    
				</div>
			</div>
		<div id="bottomWrap"></div>	
	</div>
	<div style="display: none;"></div>
</body>
