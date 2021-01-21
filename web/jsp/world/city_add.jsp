<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="net.fckeditor.*"%>
<%@ taglib uri="http://java.fckeditor.net" prefix="FCK" %>
<%@page import="dao.CityDao" %>
<%@page import="vo.CityVO" %>
<%
	String menuCate = Constant.MENU_STAY;
	String subMenuCate = Constant.SUBMENU_CITY;
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1")); 
	int countrySeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("countrySeqNo"), "1")); 
	CityDao dao = new CityDao();
	ArrayList<CityVO> country = dao.selectCountryList(1, 100);
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
			/* $( "#notiDate" ).datepicker({
				dateFormat: "yymmdd",
				defaultDate: "+1w",
				changeMonth: true,
				numberOfMonths: 3,
			}); */
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
					<h2 class="contentTitle">도시 입력</h2>
					<form name="frm" action="city_addProc.jsp" method="post" enctype="multipart/form-data">
					<div class="contentForm">
						<table class="detailInfoOneItem" style="margin-top: -20px;">
							<thead><tr><td colspan="2"  style="text-align:right;border-top:none;">* 필수입력</td></tr></thead>
							<tbody>
							<tr class="detailInfoOneItemFirst"> 
							  <th>도시명 *</th>
							  <td><input type="text" name="cityName" size="50" class="box formfield" /></td>
							</tr>
							<tr > 
							   <th>국가</th>
							  <td>
							  <select name ="country">
							  <%for(int i =0; i<country.size(); i++){ 
							  CityVO cvo = country.get(i);
							  %>
							 <option value="<%=cvo.getCountrySeqNo()%>" <%if(cvo.getCountrySeqNo()==(countrySeqNo)){%> selected<%}%>><%=cvo.getCountryName()%></option>
							 <%} %>
							  </select>
							  </td>
							</tr>
							<tr > 
							   <th>낮 이미지</th>
							  <td><input type="file" name="daylistImg" size="50" class="box" /></td>
							</tr>
							<tr > 
							   <th>밤 이미지</th>
							  <td><input type="file" name="nightlistImg" size="50" class="box" /></td>
							</tr>
							<tr > 
							   <th>목표계단수</th>
							  <td><input type="text" name="stair" size="50" class="box formfield" /></td>
							</tr>
							</tbody>
						</table>
					</div>
					<div class="detailInfoButton">
				      	<input type="submit" value="등록" />
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