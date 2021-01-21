<%@page import="vo.CityVO"%>
<%@page import="dao.CityDao"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%
	String menuCate = Constant.MENU_STAY;
	String subMenuCate = Constant.SUBMENU_CITY;
	
	int no = Integer.parseInt(StringUtil.nchk(request.getParameter("citySeqNo"), "1")); //요청하여 받은 값을 no변수에 저장 
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1")); //요청하여 받은 값을 pageno변수에 저장 

	CityDao dao = new CityDao();
	CityVO vo = new CityVO();
	vo = dao.cityView(no); //vo에 저장 
	int countSeq = vo.getCountrySeqNo();
	//dao = new CityDao();
	
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
			$('button.icon-trash').button({
				icons: {
	                primary: "ui-icon-trash"
	            },
	            text: false
			});
			$( "#notice_wrt_date" ).datepicker({
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
					<h2 class="contentTitle">도시 수정</h2>
					<form name="frm" action="city_modifyProc.jsp" method="post" enctype="multipart/form-data">
					<input type="hidden" name="citySeqNo" value="<%=no%>">
					<input type="hidden" name="pageno" value="<%=pageno%>">
					<div class="contentForm">
						<table class="detailInfoOneItem" style="margin-top: -20px;">
							<thead><tr><td colspan="2"  style="text-align:right;border-top:none;">* 필수입력</td></tr></thead>
							<tbody>
							<tr class="detailInfoOneItemFirst"> 
							  <th>국가명</th>
							  <td>
								 <%=vo.getCountryName() %>
							  </td>
							</tr>
							<tr class="detailInfoOneItemFirst"> 
							  <th>도시명</th>
							  <td><input type="text" name="cityName" value="<%=vo.getCityName() %>" size="50" class="box formfield" /></td>
							</tr>
							<tr>
							<th>낮 이미지</th>
							<td>
								<%if(!vo.getCityPic().equals("") ){%>
								<img src="../../upload/city/<%=vo.getCityPic() %>" width="150" height="100" />
								<%}else{%> 이미지 없음<%} %>
								<input type="file" name="daylistImg" size="50" class="box" /></td>
							</tr>
							<tr>
							<th>밤 이미지</th>
							<td>
								<%if(!vo.getCityNPic().equals("") ){%>
								<img src="../../upload/city/<%=vo.getCityNPic() %>" width="150" height="100" />
								<%}else{%> 이미지 없음<%} %>
								<input type="file" name="nightlistImg" size="50" class="box" /></td>
							</tr>
							<tr> 
							  <th>진행순서</th>
							  <td><%=vo.getOrderLy() %></td>
							</tr>
							<tr> 
							  <th>목표계단</th>
							  <td><input type="text" name="stair" value="<%=vo.getTotalStair() %>" size="50" class="box formfield" /></td>
							</tr>
							</tbody>
						</table>
					</div>
					<div class="detailInfoButton">
				      	<input type="submit" value="수정" />
				      	<input type="button" OnClick="location.href='city_list.jsp?pageno=<%=pageno%>';" value="취소" />
				    </div>
					</form>
				</div>
			</div>
			<div id="bottomWrap"></div>
		
		</div>
		<div style="display: none;"></div>
	</body>
</html>
