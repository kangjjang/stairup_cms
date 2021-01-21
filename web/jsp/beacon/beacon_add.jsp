<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="net.fckeditor.*"%>
<%@page import="vo.BeaconSoundVO"%>
<%@page import="vo.AffiliationVO"%>
<%@page import="dao.BeaconEventDao"%>
<%
	String menuCate = Constant.MENU_BEACON;
	String subMenuCate = Constant.SUBMENU_BEACON;
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));
	
	BeaconEventDao dao = new BeaconEventDao();
	ArrayList<BeaconSoundVO> list = dao.selectBoxBeaconSoundList();
	
	ArrayList<AffiliationVO> alist = dao.selectAffiliationLogo();
	
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
			}else if (document.getElementById("beaconMinor").value.length == 0) {
				alert("비콘 마이너 값을 입력해주세요.");
				document.getElementById("beaconMinor").focus();
				return false;
			}else if (document.getElementById("beaconPosition").value.length == 0) {
				alert("비콘 위치를 입력해주세요.");
				document.getElementById("beaconPosition").focus();
				return false;
			}
			
			var use = $("input[type=radio][name=beaconUse]:checked").val()
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
					
			}
			
			frm.submit();
			}
		
		function changeView(a){
			if(a=='N'){
				document.all.urlmenu.style.display="none";
			}else{
				document.all.urlmenu.style.display="";
			}
		}
		
		$(function() {
			$( "#beaconPopFromDate" ).datepicker();
			//옵션  : 매개변수값 2번째가 옵션의 타입이며 3번째 항목은 옵션에 대한 설정 값
			$( "#beaconPopFromDate" ).datepicker( "option", "dateFormat", "yy-mm-dd" ); //데이터 포맷으로 날짜의 반환 타입을 지정
			$( "#beaconPopFromDate" ).datepicker( "option", "showAnim", "slideDown" );      //달력의 표시 형태
		 });
		
		$(function() {
			$( "#beaconPopToDate" ).datepicker();
			//옵션  : 매개변수값 2번째가 옵션의 타입이며 3번째 항목은 옵션에 대한 설정 값
			$( "#beaconPopToDate" ).datepicker( "option", "dateFormat", "yy-mm-dd" ); //데이터 포맷으로 날짜의 반환 타입을 지정
			$( "#beaconPopToDate" ).datepicker( "option", "showAnim", "slideDown" );      //달력의 표시 형태
		 });
		
		$(function() {
			$( "#beaconPopFromTime" ).timepicker();
		 });
		
		$(function() {
			$( "#beaconPopToTime" ).timepicker();
		 });
		
		
		</script>	
	</head>
	<body>
		<div id="wrap">
			<%@ include file="../include/inc_top_menu.jsp" %>		
			<div id="contentWrap">  
				<%@ include file="../include/inc_left_menu.jsp" %>	  
				<div class="contentArea">
					<h2 class="contentTitle">비콘 등록</h2>
					<form name="frm" action="beacon_addProc.jsp" method="post" enctype="multipart/form-data">
					<div class="contentForm">
						<table class="detailInfoOneItem" style="margin-top: -20px;">
							<thead><tr><td colspan="2"  style="text-align:right;border-top:none;">* 필수입력</td></tr></thead>
							<tbody>
								<tr class="detailInfoOneItemFirst"> 
								  <th>팝업 로고</th>
								  <td>
								  	<select name="logoImage">
								  	<option value="N">선택없음</option>
								  		<%
								  		for(int i=0; i<alist.size(); i++){
								  			AffiliationVO avo = alist.get(i);
								  		%>
								  			<option value="<%=avo.getAffiliationPic()%>"><%=avo.getAffiliationName() %></option>
								  		<%						  			
								  		}
								  		%>
								  	</select>
								  </td>
								</tr>
								<tr> 
								  <th>비콘 번호(Major)*</th>
								  <td><input type="text" id="beaconMajor" name="beaconMajor" size="50" class="box formfield" /></td>
								</tr>
								<tr> 
								  <th>비콘 번호(Minor)*</th>
								  <td><input type="text" id="beaconMinor" name="beaconMinor" size="50" class="box formfield" /></td>
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
								<tr> 
								  <th>팝업 사용</th>
								  <td><input type="radio" id="beaconUse" name="beaconUse" value="Y">사용&nbsp;&nbsp;
									  <input type="radio" id="beaconUse" name="beaconUse" value="N"  checked="checked">사용 안함&nbsp;&nbsp;
							      </td>
								</tr>
								<tr> 
								  <th>팝업 제목</th>
								  <td><input type="text" id="title" name="title" size="50"  /></td>
								</tr> 
								<tr> 
								  <th>팝업 기간</th>
								  <td>
								  	  <input type="text" id="beaconPopFromDate" name="beaconPopFromDate" readonly style="background:white; cursor: pointer;"/>
								  	  부터 <input type="text" id="beaconPopToDate" name="beaconPopToDate" readonly style="background:white; cursor: pointer;"/>
								  </td>
								</tr>
								<tr> 
								  <th>팝업 시간</th>
								  <td>
								  	 <input type="text" id="beaconPopFromTime" name="beaconPopFromTime" readonly style="background:white; cursor: pointer;"/>
								  	  부터 <input type="text" id="beaconPopToTime" name="beaconPopToTime" readonly style="background:white; cursor: pointer;"/>	
							      </td>
								</tr>
								<tr> 
								  <th>팝업 빈도</th>
								  <td><input type="radio" id="beaconPopCnt" name="beaconPopCnt" value="0" checked="checked">매번&nbsp;&nbsp;
									  <input type="radio" id="beaconPopCnt" name="beaconPopCnt" value="1">최초 1회&nbsp;&nbsp;
							      </td>
								</tr>
								<tr>   
								   <th>이미지</th>
								  <td><input type="file" id="listImg" name="listImg" size="50" class="box" /></td>
								</tr>
								<tr> 
								  <th>내용</th>
								  <td><textarea rows="10" id="beaconContent" name="beaconContent" cols="52"></textarea></td>
								</tr>
								<tr> 
								  <th>팝업 사운드 선택</th>
								  <td>
										<select name="beaconSound">
											<option value="0">선택 안함</option>
											<%
											if(list.size() > 0){
												for(int i=0; i < list.size(); i++){
													BeaconSoundVO vo = list.get(i);
												%>
													<option value="<%=vo.getSeqNo()%>"><%=vo.getSoundName() %></option>
												<%	
												}
											}
											%>
										</select>
								  </td>
								</tr>
								<tr> 
								  <th>연결 URL 사용</th>
								  <td>
								   	  <input type="radio" id="urlUse" name="urlUse" value="Y" onchange="javascript:changeView(this.value)">사용&nbsp;&nbsp;
									  <input type="radio" id="urlUse" name="urlUse" value="N" onchange="javascript:changeView(this.value)" checked="checked">사용 안함&nbsp;&nbsp;
							      </td>
								</tr>
								<tr id="urlmenu" style="display: none"> 
								  <th>URL</th>
								  <td><input type="text" id=url name="url" size="50" value="http://"/></td>
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