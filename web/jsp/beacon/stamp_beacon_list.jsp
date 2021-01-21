<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="vo.StampEventVO"%>
<%@page import="dao.BeaconEventDao"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>

<%
	String menuCate = Constant.MENU_BEACON;
	String subMenuCate = Constant.SUBMENU_BEACON;

	String ROLE = cookieBox.getValue("ROLE");
	int roles = Integer.parseInt(ROLE);

	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));
	int rowSize = Integer.parseInt(StringUtil.nchk(request.getParameter("rowSize"), "10"));
	String keyword = StringUtil.nchk(request.getParameter("keyword"),"");

	BeaconEventDao dao = new BeaconEventDao();

	int totalcnt = dao.stampEventCnt();

	ArrayList<StampEventVO> list = dao.selectStampEventList(pageno, rowSize, keyword);
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
			$( "#radio" ).buttonset();
		});

		function pageLink(arg) {
			document.frm.pageno.value = arg;
			document.frm.submit();
		}

		function stampEventView(no){
			location.href = "stamp_beacon_view.jsp?no=" + no + "&pageno="+<%=pageno%>;
		}

		function stampRank(seq) {
            alert("순위 통계는 관리자에게 요청해 주세요.");
        }

		function search(){
			if($('#keyword').val().length == 0){
				alert("검색어를 입력 주세요.");
				return false;
			}else{
				document.frm.submit();
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
					<h2 class="contentTitle">스탬프 이벤트 관리</h2>
					<div class="contentForm">
						<form name="frm" action="stamp_beacon_list.jsp" method="post">
						<input type="hidden" name="pageno" value="1">

						<div class="searchArea">
							<table class="searchTable" >
								<tbody>
								<tr>
								  	<td>
										<input type="text" name="keyword" id="keyword" value="<%=keyword %>" size="50" placeholder="검색어를 입력해 주세요." />
								  		<input type="button" value="검색" onclick="search();" />
								  </td>
								  </tr>
								</tbody>
							</table>
						</div>

						<div class="listBody">
							<div class="listTable">
								<table class="hovertable" width="100%">
									<thead>
										<tr align="center">
											<th style="width: 3% ;text-align: center;">no.</th>
											<th style="width: 7% ;text-align: center;">이벤트명</th>
											<th style="width: 7% ;text-align: center;">이벤트 시작일</th>
											<th style="width: 5% ;text-align: center;">이벤트 종료일</th>
											<th style="width: 7% ;text-align: center;">등록일</th>
                                            <th style="width: 7% ;text-align: center;">순위보기</th>
										</tr>
									</thead>
									<tbody>
										 <%
										if (list.size() > 0) {
											for (int i=0; i<list.size(); i++) {
												StampEventVO vo = list.get(i);

												int ten =  i;
												int pagevalue = (pageno-1) * 10;

												int rownum = totalcnt -pagevalue - ten ;

												%>

											<tr style="cursor: pointer;" <%if((i % 2) == 1 ) {%> class="even"<%} %>>
												<td style="text-align:center;"><%=rownum %></td>
												<td onclick="javascript:stampEventView(<%=vo.getSeqNo() %>);" style="text-align:center;"><%=vo.getStampEventName() %></td>
												<td style="text-align:center;"><%=vo.getStampEventStartDate().substring(0,10) %></td>
												<td style="text-align:center;"><%=vo.getStampEventEndDate().substring(0,10) %></td>
												<td style="text-align:center;"><%=vo.getCrtDate().substring(0,10) %></td>
												<td style="text-align:center;"><input type="button" id="btnRank" name="btnRank" value="순위보기" onclick="javascript:stampRank(<%=vo.getSeqNo() %>)"></td>
											</tr>

											<%
											}
										} else {
											out.println("<tr><td colspan=7>조회 결과가 없습니다.</td></tr>");
										}
										%>
									</tbody>
								</table>
							</div>
							<div class="listMenu"><p>total <%=totalcnt %>건</p></div>
							<div calss="listBottom">
								<div class="listBottomCenterOnly">
									<jsp:include page="../include/inc_paging.jsp">
										<jsp:param name="totalRecord" value="<%=totalcnt%>"/>
										<jsp:param name="pageNo" value="<%=pageno%>"/>
										<jsp:param name="rowCount" value="10"/>
										<jsp:param name="pageGroup" value="10"/>
									</jsp:include>
									<div class="alignRightButton">
								      	<button type="button" OnClick="location.href='stamp_beacon_add.jsp';">등록</button>
								    </div>
								</div>
							</div>
						</div>
						</form>

					</div>
				</div>
			</div>
			<div id="bottomWrap"></div>
		</div>
		<div style="display: none;"></div>
	</body>
</html>
