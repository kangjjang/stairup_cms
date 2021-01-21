<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="vo.AffiliationVO"%>
<%@ page import="dao.AffiliationDao" %>
<%
	String menuCate = Constant.MENU_BEACON;
	String subMenuCate = Constant.SUBMENU_BEACON;
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));

    AffiliationDao dao = new AffiliationDao();
    ArrayList<AffiliationVO> list = dao.affiliationList();
	
	dao.closeConn();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<%@ include file="../include/inc_top.jsp" %>
		<script language=javascript>

            function check() {

                if($("#stampEventTarget").val() == -1){
                    alert("이벤트 대상 소속을 선택해 주세요.");
                    $("#stampEventTarget").focus();
                    return;
                }

                if($("#stampEventName").val().length == 0){
                    alert("스탬프 이벤트 제목을 입력해 주세요.");
                    $("#stampEventName").focus();
                    return;
                }

                if($("#stampEventRegion").val().length == 0){
                    alert("이벤트 지역을 선택해 주세요.");
                    $("#stampEventRegion").focus();
                    return;
                }

                if($("#stampEventAddress").val().length == 0){
                    alert("주소를 입력해 주세요.");
                    $("#stampEventAddress").focus();
                    return;
                }

                if($("#stampEventCourseDistance").val().length == 0){
                    alert("코스길이를 입력해 주세요.");
                    $("#stampEventCourseDistance").focus();
                    return;
                }

                if($("#stampEventCourseTime").val().length == 0){
                    alert("코스시간을 입력해 주세요.");
                    $("#stampEventCourseTime").focus();
                    return;
                }

                if($("#stampEventCourseTime").val().length == 0){
                    alert("이벤트 기간을 입력해 주세요.");
                    $("#stampEventCourseTime").focus();
                    return;
                }

                if($("#stampBeaconMajor").val().length == 0){
                    alert("비콘 major 값을 입력해 주세요.");
                    $("#stampBeaconMajor").focus();
                    return;
                }

                if($("#stampBeaconMinor").val().length == 0){
                    alert("비콘 minor 값을 입력해 주세요.");
                    $("#stampBeaconMinor").focus();
                    return;
                }

                frm.submit();
			}
		

		$(function() {
			$( "#stampEventStartDate" ).datepicker();
			//옵션  : 매개변수값 2번째가 옵션의 타입이며 3번째 항목은 옵션에 대한 설정 값
			$( "#stampEventStartDate" ).datepicker( "option", "dateFormat", "yy-mm-dd" ); //데이터 포맷으로 날짜의 반환 타입을 지정
			$( "#stampEventStartDate" ).datepicker( "option", "showAnim", "slideDown" );      //달력의 표시 형태
		 });
		
		$(function() {
			$( "#stampEventEndDate" ).datepicker();
			//옵션  : 매개변수값 2번째가 옵션의 타입이며 3번째 항목은 옵션에 대한 설정 값
			$( "#stampEventEndDate" ).datepicker( "option", "dateFormat", "yy-mm-dd" ); //데이터 포맷으로 날짜의 반환 타입을 지정
			$( "#stampEventEndDate" ).datepicker( "option", "showAnim", "slideDown" );      //달력의 표시 형태
		 });

		
		</script>	
	</head>
	<body>
		<div id="wrap">
			<%@ include file="../include/inc_top_menu.jsp" %>		
			<div id="contentWrap">  
				<%@ include file="../include/inc_left_menu.jsp" %>	  
				<div class="contentArea">
					<h2 class="contentTitle">스탬프 이벤 등록</h2>
					<form name="frm" action="stamp_beacon_add_ok.jsp" method="post" enctype="multipart/form-data">
					<div class="contentForm">
						<table class="detailInfoOneItem" style="margin-top: -20px;">
							<thead><tr><td colspan="2"  style="text-align:right;border-top:none;">* 필수입력</td></tr></thead>
							<tbody>
								<tr class="detailInfoOneItemFirst"> 
								  <th>이벤트 참여 대상</th>
								  <td>
                                      <select name="stampEventTarget" id="stampEventTarget">
                                          <option value="-1">소속 선택</option>
                                          <option value="0">전체</option>
                                          <%
                                              if(list.size() > 0){
                                                  for(int i=0; i<list.size(); i++){
                                                      AffiliationVO vo = list.get(i);
                                          %>
                                          <option value="<%=vo.getSeqNo()%>"><%=vo.getAffiliationName() %></option>
                                          <%
                                                  }
                                              }
                                          %>
                                      </select>
								  </td>
								</tr>
                                <tr>
                                    <th>제목</th>
                                    <td><input type="text" id="stampEventName" name="stampEventName" size="50"  /></td>
                                </tr>
                                <tr>
                                    <th>지역</th>
                                    <td>
                                        <select name="stampEventRegion" id="stampEventRegion">
                                            <option value="">지역 선택</option>
                                            <option value="서울특별시">서울특별시</option>
                                            <option value="부산광역시">부산광역시</option>
                                            <option value="대구광역시">대구광역시</option>
                                            <option value="인천광역시">인천광역시</option>
                                            <option value="광주광역시">광주광역시</option>
                                            <option value="대전광역시">대전광역시</option>
                                            <option value="울산광역시">울산광역시</option>
                                            <option value="세종특별자치시">세종특별자치시</option>
                                            <option value="경기도">경기도</option>
                                            <option value="강원도">강원도</option>
                                            <option value="충청북도">충청북도</option>
                                            <option value="충청남도">충청남도</option>
                                            <option value="전라북도">전라북도</option>
                                            <option value="전라남도">전라남도</option>
                                            <option value="경상북도">경상북도</option>
                                            <option value="경상남도">경상남도</option>
                                            <option value="제주도">제주도</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th>주소</th>
                                    <td><input type="text" id="stampEventAddress" name="stampEventAddress" size="50" /></td>
                                </tr>
                                <tr>
								  <th>코스 길이</th>
								  <td><input type="text" id="stampEventCourseDistance" name="stampEventCourseDistance" size="50" /></td>
								</tr>
								<tr> 
								  <th>코스 시간</th>
								  <td><input type="text" id="stampEventCourseTime" name="stampEventCourseTime" size="50" /></td>
								</tr>
								<tr>
								  <th>이벤트 기간</th>
								  <td>
								  	  <input type="text" id="stampEventStartDate" name="stampEventStartDate" readonly style="background:white; cursor: pointer;"/>
								  	  부터 <input type="text" id="stampEventEndDate" name="stampEventEndDate" readonly style="background:white; cursor: pointer;"/>
								  </td>
								</tr>
								<tr>
								   <th>이벤트 이미지</th>
								  <td><input type="file" id="stampEventImage" name="stampEventImage" size="50" class="box" /></td>
								</tr>
                                <tr>
                                    <th>코스 이미지</th>
                                    <td><input type="file" id="stampEventCourseImage" name="stampEventCourseImage" size="50" class="box" /></td>
                                </tr>
                                <tr>
                                    <th>비콘 MAJOR</th>
                                    <td><input type="text" id="stampBeaconMajor" name="stampBeaconMajor" size="50" /></td>
                                </tr>
                                <tr>
                                    <th>비콘 MINOR</th>
                                    <td><input type="text" id="stampBeaconMinor" name="stampBeaconMinor" size="50" /> (쉼표,로 구분해서 넣어주세요)</td>
                                </tr>

                            </tbody>
						</table>
					</div>
					<div class="detailInfoButton">
						<input type="button" value="등록" onclick="check()">
				      	<input type="button" OnClick="location.href='stamp_beacon_list.jsp?pageno=<%=pageno%>';" value="취소" />
				    </div>
					</form>
				</div>
			</div>
			<div id="bottomWrap"></div>
		
		</div>
		<div style="display: none;"></div>
	</body>
</html>