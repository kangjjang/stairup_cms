<%@ page contentType="text/html; charset=utf-8" %>
<%@page import="vo.PhotoVO"%>
<%@page import="dao.PhotoDao"%>
<%@page import="vo.EventVO"%>
<%@page import="dao.EventDao"%>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="net.fckeditor.*"%>
<%@page import="vo.AffiliationVO"%>
<%@page import="dao.AffiliationDao"%>
<%@ taglib uri="http://java.fckeditor.net" prefix="FCK" %>
<%
	String menuCate = Constant.MENU_NOTICE; 
	String subMenuCate = Constant.SUBMENU_EVENT; 
	
	String ROLE = cookieBox.getValue("ROLE");
	int roles = Integer.parseInt(ROLE);
	
	int no = Integer.parseInt(StringUtil.nchk(request.getParameter("no"), "1")); 
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));
	String boardCate = Constant.CATEGORY_EVENT;
	String url = "";
	PhotoVO photovo = null;
	EventDao dao = new EventDao();
	EventVO vo = new EventVO();
	
	AffiliationDao adao = new AffiliationDao();
	ArrayList<AffiliationVO> list = adao.affiliationList();

	vo = dao.eventView(no);
	dao.closeConn();
	adao.closeConn();
%>
<%
	FCKeditor fckEditor = new FCKeditor(request, "wallContent", "700", "300", "Audi", vo.getEventContent(), addPath + "/fckeditor"); //FCKeditor 생성 
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
			$('button.icon-trash').button({
				icons: {
	                primary: "ui-icon-trash"
	            },
	            text: false
			});
			
			$('form[name="frm"]').submit(function(){
 				$('.notice').remove();
				$('.formfield').each(function() {
					if(!$(this).val()) {
						$(this).addClass('highlight');
						$('<span />', {
							text: '필수 입력사항입니다.',
							'class': 'notice',
							click: function() {
								alert('필수 입력사항입니다.');
							}
						}).appendTo($(this).parent());
					} else {
						$(this).removeClass('highlight');
					}
				});
				if($('.notice').length)
					return false; 
			});
		});	
		
		function OriFileDel(oriImg, hidImg){
			$('#'+oriImg).hide('drop', { percent: 100 }, 500);
			$('#'+hidImg).val('');
		}
		</script>
		<script type="text/javascript">
			function FCKeditor_OnComplete(editorInstance) {
				window.status = editorInstance.Description;
			}
		</script>			
	</head>
	<body>
		<div id="wrap">
			<%@ include file="../include/inc_top_menu.jsp" %>		
			<div id="contentWrap">  
				<%@ include file="../include/inc_left_menu.jsp" %>	  
				<div class="contentArea">
					<h2 class="contentTitle">이벤트 수정</h2>
					<form name="frm" action="event_modifyProc.jsp" method="post" enctype="multipart/form-data">
					<input type="hidden" name="no" value="<%=no%>">
					<input type="hidden" name="pageno" value="<%=pageno%>">
					<div class="contentForm">
						<table class="detailInfoOneItem" style="margin-top: -20px;">
							<thead><tr><td colspan="2"  style="text-align:right;border-top:none;">* 필수입력</td></tr></thead>
							<tbody>
							<tr>
							  <th>이벤트 제목 </th>
							  <td><input type="text" name="event_title" value="<%=vo.getEventTitle() %>" size="50" class="box formfield" /></td>
							  
							</tr>
							<%if(roles == 0){%>
							<tr> 
								<th>소속</th>
							  	<td>
								  	<select name="affiliationSeq" id="affiliation">
								  		<option value="notting">소속 선택</option>
								  		<option value="0">전체</option>
								  		<%
								  		if(list.size() > 0){
								  			for(int i=0; i<list.size(); i++){ 
								  				AffiliationVO avo = list.get(i);	
								  		%>
								  		<option value="<%=avo.getSeqNo()%>" <%if(avo.getAffiliationName().equals(vo.getAffiliationName())){%>selected<%} %>><%=avo.getAffiliationName() %></option>
								  		<%	
								  			}
								  		} 
								  		%>
								  	</select>
							    </td>
							</tr>
							<%} %>
							<tr>
							<th>이미지</th>
							<td>
								<%if(!vo.getEventPic().equals("") ){%>
							 		<img src="/upload/event/<%=vo.getEventPic()%>" width="150" height="100" />
							 	<%}else{%> 이미지 없음<%} %>	
								<input type="file" name="listImg" size="50" class="box" />
								</td>
							</tr>
							<tr> 
							  <th>내용</th>
							  <td><%=fckEditor%></td>
							</tr>
							</tbody>
						</table>
					</div>
					<div class="detailInfoButton">
				      	<input type="submit" value="수정" />
				      	<input type="button" OnClick="location.href='event_list.jsp?pageno=<%=pageno%>';" value="취소" />
				    </div>
					</form>
				</div>
			</div>
			<div id="bottomWrap"></div>
		</div>
		<div style="display: none;"></div>
	</body>
</html>