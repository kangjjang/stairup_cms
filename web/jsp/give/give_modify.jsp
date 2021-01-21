<%@page import="vo.DonationVO"%>
<%@page import="dao.DonationDao"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="net.fckeditor.*"%>
<%@ taglib uri="http://java.fckeditor.net" prefix="FCK" %>
<%
	String menuCate = Constant.MENU_CONTENT;
	String subMenuCate = Constant.SUBMENU_WALL_BOARD;;
	
	int giveSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("giveSeqNo"), "1")); //요청하여 받은 값을 no변수에 저장 
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1")); //요청하여 받은 값을 pageno변수에 저장 

	DonationDao dao = new DonationDao();
	DonationVO vo = new DonationVO();
	vo = dao.giveView(giveSeqNo); //vo에 저장 
	dao.closeConn();
%>
<%
	/* FCKeditor fckEditor = new FCKeditor(request, "notiDesc", "700", "300", "Audi", vo.getNotiContent(), addPath + "/fckeditor"); */
	FCKeditor fckEditor = new FCKeditor(request, "giveContent", "700", "300", "Audi", vo.getContent(), addPath + "/fckeditor");
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
			$( "#endDate" ).datepicker({
				dateFormat: "yymmdd",
				defaultDate: "+1w",
				changeMonth: true,
				numberOfMonths: 3,
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
					<h2 class="contentTitle">힘내라 기부 수정</h2>
					<form name="frm" action="give_modifyProc.jsp" method="post" enctype="multipart/form-data">
					<input type="hidden" name="giveSeqNo" value="<%=giveSeqNo%>">
					<input type="hidden" name="pageno" value="<%=pageno%>">
					<div class="contentForm">
						<table class="detailInfoOneItem" style="margin-top: -20px;">
							<thead><tr><td colspan="2"  style="text-align:right;border-top:none;">* 필수입력</td></tr></thead>
							<tbody>
							<tr class="detailInfoOneItemFirst"> 
							  <th>힘내라 기부 제목</th>
							  <td><input type="text" name="giveTitle" value="<%=vo.getTitle() %>" size="50" class="box formfield" /></td>
							  
							</tr>
							<tr>
							<th>이미지</th>
							<td>
								<%if(!vo.getPic().equals("")){%>
								<img src="/upload/Thumb/give/<%=vo.getPic() %>" width="150" height="100" />
								<%}else{%>이미지 없음<%} %>
								<input type="file" name="listImg" size="50" class="box" />
							</td>
							</tr>
							<tr> 
							  <th>상세 내용</th>
							  <td><%=fckEditor %></td>
							</tr>
							<tr>
							<th>목표계단</th>
							<td><input type="text" name="aim" value="<%=vo.getAim() %>" size="50" class="box formfield" /></td>
							</tr>
							<tr> 
							  <th>종료일</th>
							  <td><input type="text" id ="endDate" name="endDate" value="<%=vo.getEndDate()%>"class="box formfield" /></td>
							</tr>
							</tbody>
						</table>
					</div>
					<div class="detailInfoButton">
				      	<input type="submit" value="수정" />
				      	<input type="button" OnClick="location.href='give_list.jsp?pageno=<%=pageno%>';" value="취소" />
				    </div>
					</form>
				</div>
			</div>
			<div id="bottomWrap"></div>
		
		</div>
		<div style="display: none;"></div>
	</body>
</html>