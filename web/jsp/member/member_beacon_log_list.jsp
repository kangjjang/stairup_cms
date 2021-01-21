<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="vo.MemberVO"%>
<%@page import="dao.MemberDao"%>
<%@page import="dao.AdminDao" %>
<%@page import="util.StringUtil"%>
<%@page import="util.DateUtil"%>
<%@page import="util.Constant"%>
<%@page import="util.HashUtil" %>
<%@ page import="dao.BeaconDao" %>
<%@ page import="vo.BeaconVO" %>

<%
	String ROLE = cookieBox.getValue("ROLE");
	int roles = Integer.parseInt(ROLE);

	String menuCate = Constant.MENU_MEMBER;
	String subMenuCate = Constant.SUBMENU_MEMBER;
	
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));
	int rowSize = Integer.parseInt(StringUtil.nchk(request.getParameter("rowSize"), "10"));
    int memSeq = Integer.parseInt(StringUtil.nchk(request.getParameter("memSeq"), "10"));

	BeaconDao dao = new BeaconDao();
	int totalcnt = dao.beaconLogCnt(memSeq);
	
	//dao = new MemberDao();
	ArrayList<BeaconVO> list = dao.beaconLogList(pageno, rowSize, memSeq);
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
			document.frm.pageno.value    = arg;
			document.frm.submit();
		}

		</script>
	</head>
	<body>
		<div id="wrap">
			<%@ include file="../include/inc_top_menu.jsp" %>			
			<div id="contentWrap">  
				<%@ include file="../include/inc_left_menu.jsp" %>
				<div class="contentArea">
					<h2 class="contentTitle">회원 계단 로그</h2>
					<div class="contentForm">
						<form name="frm" action="member_beacon_log_list.jsp" method="post">
						<input type="hidden" name="pageno" value="1">
                        <input type="hidden" name="memSeq" value="<%=memSeq%>">
						<div class="listBody">
							<div class="listTable">
								<table class="hovertable" width="100%">
									<thead>
										<tr align="center">
											<th style="width: 3% ;text-align: center;">no.</th>
											<th style="width: 7% ;text-align: center;">회원번호</th>
											<th style="width: 5% ;text-align: center;">건물비콘번호</th>
											<th style="width: 10% ;text-align: center;">시작비콘</th>
											<th style="width: 10% ;text-align: center;">끝비콘</th>
                                            <th style="width: 10% ;text-align: center;">처리값</th>
											<th style="width: 7% ;text-align: center;">등록일</th>
										</tr>
									</thead>
									<tbody>
										 <%
										if (list.size() > 0) {
											for (int i=0; i<list.size(); i++) {
												BeaconVO vo = list.get(i);
												String result ="";
												String crtDate = vo.getCrtDate().substring(0,16);
												int ten =  i;
												int pagevalue = (pageno-1)*10;
												int rownum = totalcnt -pagevalue - ten ;
												%>
											<tr>
												<td><%=vo.getBelogSeqNo() %></td>
												<td><%=vo.getMemSeqNo() %></td>
												<td><%=vo.getStairsPosition() %></td>
												<td><%=vo.getStartBeacon() %></td>
												<td><%=vo.getEndBeacon() %></td>
                                                <td><%=vo.getResultBeacon() %></td>
												<td><%=crtDate%></td>
											</tr>
											
											<%
											}
										} else {
											out.println("<tr><td colspan=6>조회 결과가 없습니다.</td></tr>");
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
