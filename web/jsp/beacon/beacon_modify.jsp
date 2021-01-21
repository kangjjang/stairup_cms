<%@page import="vo.BeaconEventVO"%>
<%@page import="dao.BeaconEventDao"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="net.fckeditor.*"%>
<%@ taglib uri="http://java.fckeditor.net" prefix="FCK" %>
<%@page import="vo.BeaconSoundVO"%>
<%@page import="vo.AffiliationVO"%>
<%
	String menuCate = Constant.MENU_BEACON; //Constant.MENU_GENERAL를 menuCate변수에 저장
	String subMenuCate = Constant.SUBMENU_BEACON; //Constant.SUBMENU_NEWS를 subMenuCate변수에 저장
	
	int no = Integer.parseInt(StringUtil.nchk(request.getParameter("no"), "1")); //요청하여 받은 값을 no변수에 저장 
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1")); //요청하여 받은 값을 pageno변수에 저장 

	BeaconEventDao dao = new BeaconEventDao();
	BeaconEventVO vo = new BeaconEventVO();
	vo = dao.BeaconEventView(no);
	
	String url = "";
	
	if(vo.getBeaconUrl().equals("") || vo.getBeaconUrl().equals("http://")){
		url = "http://";
	}else{
		url = vo.getBeaconUrl();
	}
	
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
			$('button.icon-trash').button({
				icons: {
	                primary: "ui-icon-trash"
	            },
	            text: false
			});
			$( "#beacon_wrt_date" ).datepicker({
				dateFormat: "yymmdd",
				defaultDate: "+1w",
				changeMonth: true,
				numberOfMonths: 3,
			});
			
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
			var use = $("input[type=radio][name=beaconUse]:checked").val()
			if(use == "Y"){
				if (document.getElementById("title").value.length == 0) {
					alert("제목을 입력해주세요.");
					document.getElementById("title").focus();
					return false;
				}
				if(document.getElementById("listImg").value == ""){
					if (document.getElementById("Url").value.length == 0 ) {
						alert("이미지 또는 URL을 입력해 주세요!");
					
						return false;
					}
				}
			}
			
			frm.submit();
			}
		
		function OriFileDel(oriImg, hidImg){
			$('#'+oriImg).hide('drop', { percent: 100 }, 500);
			$('#'+hidImg).val('');
		}
		
		function changeView(a){
			if(a=='N'){
				document.all.urlmenu.style.display="none";
			}else{
				document.all.urlmenu.style.display="";
			}
		}

	   $(function() {
	       var a = '<%=vo.getBeaconUrlYN()%>';
	       if (a == 'Y') {
               document.all.urlmenu.style.display = "";
           }else{
        	   document.all.urlmenu.style.display = "none";
           }
	       
	   });
	   
	   $(function() {
			$( "#beaconPopFromDate" ).datepicker();
			//옵션  : 매개변수값 2번째가 옵션의 타입이며 3번째 항목은 옵션에 대한 설정 값
			$( "#beaconPopFromDate" ).datepicker( "option", "dateFormat", "yy-mm-dd" ); //데이터 포맷으로 날짜의 반환 타입을 지정
			$( "#beaconPopFromDate" ).datepicker( "option", "showAnim", "slideDown" );      //달력의 표시 형태
			$( "#beaconPopFromDate" ).val("<%=vo.getBeaconPopFromDateTime().substring(0, 10)%>");
		 });
		
		$(function() {
			$( "#beaconPopToDate" ).datepicker();
			//옵션  : 매개변수값 2번째가 옵션의 타입이며 3번째 항목은 옵션에 대한 설정 값
			$( "#beaconPopToDate" ).datepicker( "option", "dateFormat", "yy-mm-dd" ); //데이터 포맷으로 날짜의 반환 타입을 지정
			$( "#beaconPopToDate" ).datepicker( "option", "showAnim", "slideDown" );      //달력의 표시 형태
			$( "#beaconPopToDate" ).val("<%=vo.getBeaconPopToDateTime().substring(0, 10)%>");
		 });
		
		$(function() {
			$( "#beaconPopFromTime" ).timepicker();
			$( "#beaconPopFromTime" ).val("<%=vo.getBeaconPopFromDateTime().substring(11, 16)%>");
		 });
		
		$(function() {
			$( "#beaconPopToTime" ).timepicker();
			$( "#beaconPopToTime" ).val("<%=vo.getBeaconPopToDateTime().substring(11, 16)%>");
		 });
		
		</script>
	</head>
	<body>
		<div id="wrap">
			<%@ include file="../include/inc_top_menu.jsp" %>		
			<div id="contentWrap">  
				<%@ include file="../include/inc_left_menu.jsp" %>	  
				<div class="contentArea">
					<h2 class="contentTitle">비콘 정보 수정</h2>
					<form name="frm" action="beacon_modifyProc.jsp" method="post" enctype="multipart/form-data">
					<input type="hidden" name="no" value="<%=no%>">
					<input type="hidden" name="pageno" value="<%=pageno%>">
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
							  			<option value="<%=avo.getAffiliationPic()%>" <%if(vo.getLogoImage().equals(avo.getAffiliationPic())){
							  			%>
							  			 selected="selected"
							  			<%
							  			}
							  			%>
							  			><%=avo.getAffiliationName() %></option>
							  		<%						  			
							  		}
							  		%>
							  	</select>
							  </td>
							</tr>
							<tr> 
							  <th>비콘 번호(Major)</th>
							  <td><input type="text" name="beaconMajor" value="<%=vo.getBeaconMajor() %>" size="50" class="box formfield" /></td>
							</tr>
							<tr> 
							  <th>비콘 번호(Minor)</th>
							  <td><input type="text" name="beaconMinor" value="<%=vo.getBeaconMinor() %>" size="50" class="box formfield" /></td>
							</tr>
							<tr> 
							  <th>팝업 위치</th>
							  <td><input type="text" name="beaconPosition" value="<%=vo.getBeaconPosition() %>" size="50" class="box formfield" /></td>
							</tr>
							<tr> 
							  <th>위치</th>
							  <td><input type="radio" id="beaconLocation" name="beaconLocation" value="내부" <%if(vo.getBeaconLocation().equals("내부")){%>checked="checked"<%} %>>내부&nbsp;&nbsp;
								  <input type="radio" id="beaconLocation" name="beaconLocation" value="외부" <%if(vo.getBeaconLocation().equals("외부")){%>checked="checked"<%} %>>외부&nbsp;&nbsp;
							  </td>
							</tr>
							<tr> 
							  <th>팝업 사용</th>
							  <td><input type="radio" id="beaconUse" name="beaconUse" value="Y"    <%if(vo.getBeaconUse().equals("Y")){%>checked="checked"<%} %>>사용&nbsp;&nbsp;
								  <input type="radio" id="beaconUse" name="beaconUse" value="N" <%if(vo.getBeaconUse().equals("N")){%>checked="checked"<%} %>>사용 안함&nbsp;&nbsp;
						      </td>
							</tr>
							<tr> 
							  <th>팝업 제목</th>
							  <td><input type="text" id="title" name="title" value="<%=vo.getBeaconTitle() %>" size="50" /></td>
							</tr>
							<tr> 
							  <th>팝업 기간3313</th>
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
							  <td><input type="radio" id="beaconPopCnt" name="beaconPopCnt" value="0" <%if(vo.getBeaconPopCnt() == 0){ %>checked<%} %>>매번&nbsp;&nbsp;
								  <input type="radio" id="beaconPopCnt" name="beaconPopCnt" value="1" <%if(vo.getBeaconPopCnt() == 1){ %>checked<%} %>>최초 1회&nbsp;&nbsp;
						      </td>
							</tr>
							<tr>
							<th>이미지</th>
							<td>
								<%if(!vo.getBeaconImage().equals("")){ %>
									<img src="/upload/beacon/<%=vo.getBeaconImage() %>" width="150" height="100" />
								<%}else{%>이미지 없음<%} %>
							<input type="file" id="listImg" name="listImg" size="50" class="box" />
							</td>
							</tr>
							<tr> 
							  <th>팝업 내용</th>
							  <td><textarea rows="10" id="beaconContent" name="beaconContent" cols="52"><%=vo.getBeaconContent() %></textarea></td>
							</tr>
							<tr> 
							  <th>팝업 사운드 선택</th>
							  <td>
									<select name="beaconSound">
										<option value="0">선택 안함</option>
										<%
										if(list.size() > 0){
											for(int i=0; i < list.size(); i++){
												BeaconSoundVO vo2 = list.get(i);
												////System.out.println("vo2.getSeqNo() = "+ vo2.getSeqNo());
												////System.out.println("vo.getBeaconSoundSeq() = "+ vo.getBeaconSoundSeq());
											%>
												<option value="<%=vo2.getSeqNo()%>"<% if(vo2.getSeqNo()== vo.getBeaconSoundSeq()){%> selected="selected" <% } %>> <%=vo2.getSoundName() %></option>
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
							   	  <input type="radio" id="urlUse" name="urlUse" value="Y" <%if(vo.getBeaconUrlYN().equals("Y")){ %> checked="checked" <%}%> onchange="javascript:changeView(this.value)">사용&nbsp;&nbsp;
								  <input type="radio" id="urlUse" name="urlUse" value="N" <%if(vo.getBeaconUrlYN().equals("N")){ %> checked="checked" <%}%> onchange="javascript:changeView(this.value)">사용 안함&nbsp;&nbsp;
						      </td>
							</tr>
							<tr id="urlmenu"> 
							  <th>URL</th>
							  <td><input type="text" id="Url" name="Url" value="<%=url%>" size="50" /></td>
							</tr>
							
							</tbody>
						</table>
					</div>
					<div class="detailInfoButton">
				      	<input type="button" value="수정 " onclick="check()">
				      	<!-- <input type="submit" value="등록" /> -->
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