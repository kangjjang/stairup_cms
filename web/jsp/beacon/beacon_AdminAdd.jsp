<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="net.fckeditor.*"%>
<%@ taglib uri="http://java.fckeditor.net" prefix="FCK" %>
<%
	String menuCate = Constant.MENU_BEACON;
	String subMenuCate = Constant.SUBMENU_BEACON;
	
	String ROLE = cookieBox.getValue("ROLE");
	int roles = Integer.parseInt(ROLE);
	////System.out.println("roles = "+roles);
	
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));
%>
<%
	 FCKeditor fckEditor = new FCKeditor(request, "beaconContent", "700", "300", "Audi", "", addPath + "/fckeditor"); 
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
 				$('.beacon').remove();
				$('.formfield').each(function() {
					if(!$(this).val()) {
						$(this).addClass('highlight');
						$('<span />', {
							text: '필수 입력사항입니다.',
							'class': 'beacon',
							click: function() {
								alert('필수 입력사항입니다.');
							}
						}).appendTo($(this).parent());
					} else {
						$(this).removeClass('highlight');
					}
				});
				if($('.beacon').length)
					return false; 
			});			
		}); 
		
		
		
		function check() {
			
			if (document.getElementById("beaconMajor").value.length == 0) {
				alert("비콘 메이져 값을 입력해주세요.");
				document.getElementById("beaconMajor").focus();
				return false;
			}else if (document.getElementById("startMinor").value.length == 0) {
				alert("시작 마이너 값을 입력해주세요.");
				document.getElementById("startMinor").focus();
				return false;
			}else if (document.getElementById("endMinor").value.length == 0) {
				alert("종료 마이너 값을 입력해주세요.");
				document.getElementById("endMinor").focus();
				return false;
			}else if (document.getElementById("beaconPosition").value.length == 0) {
				alert("비콘 위치를 입력해주세요.");
				document.getElementById("beaconPosition").focus();
				return false;
			}
			
			/* var use = $("input[type=radio][name=beaconUse]:checked").val()
			if(use == "Y"){
				if (document.getElementById("title").value.length == 0) {
					alert("제목을 입력해주세요.");
					document.getElementById("title").focus();
					return false;
				}
				if(document.getElementById("listImg").value == ""){
					if (document.getElementById("url").value.length == 7 || document.getElementById("url").value.length == 0 ) {
						alert("이미지 또는 URL을 입력해 주세요!");
					
						return false;
					}
				}
					
			} */
			
			frm.submit();
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
					<h2 class="contentTitle">비콘 대량 등록</h2>
					<form name="frm" action="beacon_AdminAddProc.jsp" method="post">
					<div class="contentForm">
						<table class="detailInfoOneItem" style="margin-top: -20px;">
							<thead><tr><td colspan="2"  style="text-align:right;border-top:none;">* 필수입력</td></tr></thead>
							<tbody>
							<tr class="detailInfoOneItemFirst"> 
							  <th>비콘 번호(Major)*</th>
							  <td><input type="text" id="beaconMajor" name="beaconMajor" size="50" class="box formfield" /></td>
							</tr>
							<tr> 
							  <th>시작(Minor)*</th>
							  <td><input type="text" id="startMinor" name="startMinor" size="50" class="box formfield" /> <font color="red">- 지하3층은 300이 아닌 -3으로 표시</font></td>
							</tr>
							<tr> 
							  <th>종료(Minor)*</th>
							  <td><input type="text" id="endMinor" name="endMinor" size="50" class="box formfield" /></td>
							</tr>
							<tr> 
							  <th>팝업 위치*</th>
							  <td><input type="text" id="beaconPosition" name="beaconPosition" size="50" class="box formfield" /></td>
							</tr> 
							<tr> 
							  <th>위치</th>
							  <td><input type="radio" id="beaconLocation" name="beaconLocation" value="내부" checked="checked">내부&nbsp;&nbsp;
								  <input type="radio" id="beaconLocation" name="beaconLocation" value="외부">외부&nbsp;&nbsp;
							  </td>
							</tr>
							</tbody>
						</table>
					</div>
					<div class="detailInfoButton">
						<input type="button" value="등록" onclick="check()">
				      	<!-- <input type="submit" value="등록" onclick="check() /> -->
				      	<input type="button" OnClick="location.href='beacon_list.jsp?pageno=<%=pageno%>';" value="취소" />
				    </div>
					</form>
				</div>
			</div>
			<div id="bottomWrap"></div>
		
		</div>
		<div style="display: none;"></div>
	</body>
</html>