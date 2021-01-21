<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="util.StringUtil" %>
<%@page import="util.Constant" %>
<%@page import="vo.AffiliationVO"%>
<%@page import="dao.BeaconEventDao"%>
<%@ page import="vo.StampEventVO" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dao.AffiliationDao" %>
<%@ page import="vo.StampEventBeaconVO" %>
<%
	String menuCate = Constant.MENU_BEACON;
	String subMenuCate = Constant.SUBMENU_BEACON;
	int no = Integer.parseInt(StringUtil.nchk(request.getParameter("no"), "1"));
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));

    AffiliationDao dao = new AffiliationDao();
    ArrayList<AffiliationVO> list = dao.affiliationList();

	BeaconEventDao dao2 = new BeaconEventDao();
	StampEventVO stampEventVO = dao2.stampEventView(no);

	//skhero.kang 2019-04-05 등록한 스탬프 비콘 가져오기
    int stampEventBeaconMajor = 0;
    String stampEventBeaconMinor = "";

    ArrayList<StampEventBeaconVO> list2 = dao2.selectStampEventBeaconList(no);

    for (int i = 0; i < list2.size(); i++){
        stampEventBeaconMajor = list2.get(i).getStampBeaconMajor();
        if(stampEventBeaconMinor.length() == 0){
            stampEventBeaconMinor = Integer.toString(list2.get(i).getStampBeaconMinor());
        }else{
            stampEventBeaconMinor = stampEventBeaconMinor + "," + Integer.toString(list2.get(i).getStampBeaconMinor());
        }
    }

	dao.closeConn();
	dao2.closeConn();

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<title>스탬프 이벤트 관리</title>
	<%@ include file="../include/inc_top.jsp" %>
	<script language="javascript">
	$(function(){
		$('div.topMenuContent ul li:nth-child(<%=menuCate%>)').addClass('topMenuOn');
		$('div.menuList ul li:nth-child(<%=subMenuCate%>)').addClass('leftMenuOn');
		
		$('input:submit, input:button, button').button();
	});

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
        $( "#stampEventStartDate" ).val("<%=stampEventVO.getStampEventStartDate().substring(0, 10)%>");
    });

    $(function() {
        $( "#stampEventEndDate" ).datepicker();
        //옵션  : 매개변수값 2번째가 옵션의 타입이며 3번째 항목은 옵션에 대한 설정 값
        $( "#stampEventEndDate" ).datepicker( "option", "dateFormat", "yy-mm-dd" ); //데이터 포맷으로 날짜의 반환 타입을 지정
        $( "#stampEventEndDate" ).datepicker( "option", "showAnim", "slideDown" );      //달력의 표시 형태
        $( "#stampEventEndDate" ).val("<%=stampEventVO.getStampEventEndDate().substring(0, 10)%>");
    });
	
	function stampEventDel(no, pageno){
		if(confirm('삭제하시겠습니까?')){
			location.href = 'stamp_beacon_delete.jsp?no='+no+'&pageno='+pageno;
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
                    <h2 class="contentTitle">스탬프 이벤트 수정</h2>
                    <form name="frm" action="stamp_beacon_update.jsp" method="post" enctype="multipart/form-data">
                        <input type="hidden" id="no" name="no" value="<%=no%>">
                        <div class="contentForm">
                            <table class="detailInfoOneItem" style="margin-top: -20px;">
                                <thead><tr><td colspan="2"  style="text-align:right;border-top:none;">* 필수입력</td></tr></thead>
                                <tbody>
                                <tr class="detailInfoOneItemFirst">
                                    <th>이벤트 참여 대상</th>
                                    <td>
                                        <select name="stampEventTarget" id="stampEventTarget">
                                            <option value="-1">소속 선택</option>
                                            <option value="0" <%if(stampEventVO.getStampEventTarget() == 0){%>selected<%}%>>전체</option>
                                            <%
                                                if(list.size() > 0){
                                                    for(int i=0; i<list.size(); i++){
                                                        AffiliationVO vo = list.get(i);
                                            %>
                                            <option value="<%=vo.getSeqNo()%>" <%if(stampEventVO.getStampEventTarget() == vo.getSeqNo()){%>selected<%}%>><%=vo.getAffiliationName() %></option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th>제목</th>
                                    <td><input type="text" id="stampEventName" name="stampEventName" size="50" value="<%=StringUtil.nchk(stampEventVO.getStampEventName())%>" /></td>
                                </tr>

                                <tr>
                                    <th>지역</th>
                                    <td>
                                        <select name="stampEventRegion" id="stampEventRegion">
                                            <option value="">지역 선택</option>
                                            <option value="서울특별시" <%if(stampEventVO.getStampEventRegion().equals("서울특별시")){%>selected<%}%> >서울특별시</option>
                                            <option value="부산광역시" <%if(stampEventVO.getStampEventRegion().equals("부산광역시")){%>selected<%}%> >부산광역시</option>
                                            <option value="대구광역시" <%if(stampEventVO.getStampEventRegion().equals("대구광역시")){%>selected<%}%> >대구광역시</option>
                                            <option value="인천광역시" <%if(stampEventVO.getStampEventRegion().equals("인천광역시")){%>selected<%}%> >인천광역시</option>
                                            <option value="광주광역시" <%if(stampEventVO.getStampEventRegion().equals("광주광역시")){%>selected<%}%> >광주광역시</option>
                                            <option value="대전광역시" <%if(stampEventVO.getStampEventRegion().equals("대전광역시")){%>selected<%}%> >대전광역시</option>
                                            <option value="울산광역시" <%if(stampEventVO.getStampEventRegion().equals("울산광역시")){%>selected<%}%> >울산광역시</option>
                                            <option value="세종특별자치시" <%if(stampEventVO.getStampEventRegion().equals("세종특별자치시")){%>selected<%}%> >세종특별자치시</option>
                                            <option value="경기도" <%if(stampEventVO.getStampEventRegion().equals("경기도")){%>selected<%}%> >경기도</option>
                                            <option value="강원도" <%if(stampEventVO.getStampEventRegion().equals("강원도")){%>selected<%}%> >강원도</option>
                                            <option value="충청북도" <%if(stampEventVO.getStampEventRegion().equals("충청북도")){%>selected<%}%> >충청북도</option>
                                            <option value="충청남도" <%if(stampEventVO.getStampEventRegion().equals("충청남도")){%>selected<%}%> >충청남도</option>
                                            <option value="전라북도" <%if(stampEventVO.getStampEventRegion().equals("전라북도")){%>selected<%}%> >전라북도</option>
                                            <option value="전라남도" <%if(stampEventVO.getStampEventRegion().equals("전라남도")){%>selected<%}%> >전라남도</option>
                                            <option value="경상북도" <%if(stampEventVO.getStampEventRegion().equals("경상북도")){%>selected<%}%> >경상북도</option>
                                            <option value="경상남도" <%if(stampEventVO.getStampEventRegion().equals("경상남도")){%>selected<%}%> >경상남도</option>
                                            <option value="제주도" <%if(stampEventVO.getStampEventRegion().equals("제주도")){%>selected<%}%> >제주도</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th>주소</th>
                                    <td><input type="text" id="stampEventAddress" name="stampEventAddress" size="50" value="<%=StringUtil.nchk(stampEventVO.getStampEventAddress())%>" /></td>
                                </tr>
                                <tr>
                                    <th>코스 길이</th>
                                    <td><input type="text" id="stampEventCourseDistance" name="stampEventCourseDistance" size="50" value="<%=StringUtil.nchk(stampEventVO.getStampEventCourseDistance())%>" /></td>
                                </tr>
                                <tr>
                                    <th>코스 시간</th>
                                    <td><input type="text" id="stampEventCourseTime" name="stampEventCourseTime" size="50" value="<%=StringUtil.nchk(stampEventVO.getStampEventCourseTime())%>" /></td>
                                </tr>
                                <tr>
                                    <th>이벤트 기간</th>
                                    <td>
                                        <input type="text" id="stampEventStartDate" name="stampEventStartDate" readonly style="background:white; cursor: pointer;" />
                                        부터 <input type="text" id="stampEventEndDate" name="stampEventEndDate" readonly style="background:white; cursor: pointer;" value="<%=StringUtil.nchk(stampEventVO.getStampEventEndDate().substring(0, 10))%>"/>
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
                                    <td><input type="text" id="stampBeaconMajor" name="stampBeaconMajor" size="50" value="<%=stampEventBeaconMajor%>" /></td>
                                </tr>
                                <tr>
                                    <th>비콘 MINOR</th>
                                    <td><input type="text" id="stampBeaconMinor" name="stampBeaconMinor" size="50" value="<%=stampEventBeaconMinor%>" /> (쉼표,로 구분해서 넣어주세요)</td>
                                </tr>

                                </tbody>
                            </table>
                        </div>
					<div class="alignRightButton">
                        <input type="button" onclick="check()" value="수정">
				      	<input type="button" onclick="stampEventDel('<%=no%>', '<%=pageno%>');" value="삭제" />
				      	<input type="button" onclick="location.href='stamp_beacon_list.jsp?pageno=<%=pageno%>';" value="목록" />
				    </div>
                    </form>
				</div>
			</div>
		<div id="bottomWrap"></div>	
	</div>
	<div style="display: none;"></div>
</body>

