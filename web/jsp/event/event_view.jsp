<%@page import="vo.PhotoVO"%>
<%@page import="dao.PhotoDao"%>
<%@page import="vo.EventVO"%>
<%@page import="dao.EventDao"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="util.StringUtil"%>
<%@ page import="util.Constant"%>
<%
	String menuCate = Constant.MENU_NOTICE; 
	String subMenuCate = Constant.SUBMENU_EVENT; 
	
	int no = Integer.parseInt(StringUtil.nchk(request.getParameter("no"), "0")); 
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));
	String boardCate = Constant.CATEGORY_EVENT;
	
	EventDao dao = new EventDao();
	EventVO vo = new EventVO();
	vo = dao.eventView(no);
	String crtDate = vo.getCrtDate().substring(0,16);
	dao.closeConn();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<%@ include file="../include/inc_top.jsp" %>
		<script language=javascript>
		$(function(){
			$('div.topMenuContent ul li:nth-child(<%=menuCate%>)').addClass('topMenuOn');
			$('div.menuList ul li:nth-child(<%=subMenuCate%>)').addClass('leftMenuOn');
			
			$('input:submit, input:button, button').button();
		});
		
		function BoardDel(no, pageno){
			if(confirm('게시물을 삭제하시겠습니까?')){  //삭제버튼을 눌렀을 경우 확인메시지 출력 
				location.href = 'event_remove.jsp?no='+no+'&pageno='+pageno;
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
					<h2 class="contentTitle">이벤트</h2>
					<div class="contentForm">
						<table class="detailInfoOneItem">
							<tbody>
							<tr class="detailInfoOneItemFirst">
							<th>제목</th>
							<td><%=vo.getEventTitle() %></td>
							</tr>
							<tr > 
							 <th>등록일</th>
							  <td><%=crtDate%><br/>
							  </td>
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
							 <td>
								<%if(!vo.getEventPic().equals("") ){%>
							 		<img src="/upload/event/<%=vo.getEventPic()%>" width="150" height="100" />
							 	<%}else{%> 이미지 없음<%} %>	
							  </td>
							</tr>
							<tr class="detailInfoOneItemLast"> 
							  <th>내용</th> 
							  <td><%=vo.getEventContent() %></td>
							</tr>
							
							</tbody>
						</table>
					</div>
					<div class="alignRightButton">
						<%-- <%if(memberId.equals(vo.getProfile().getMemName())){ %> bean 에서 가져온 ID 정보와 일치한다면 수정,삭제,기능을 제공 --%>
				      	<input type="button" OnClick="location.href='event_modify.jsp?no=<%=no%>&pageno=<%=pageno%>';" value="수정" />
				      	<input type="button" OnClick="BoardDel('<%=no%>', '<%=pageno%>');" value="삭제" />
				      	<%-- <%} %> --%>
				      	<input type="button" OnClick="location.href='event_list.jsp?pageno=<%=pageno%>';" value="목록" /> 
				    </div>
				</div>
			</div>
			<div id="bottomWrap"></div>
		
		</div>
		<div style="display: none;"></div>
	</body>
</html>

								