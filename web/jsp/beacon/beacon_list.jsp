<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="vo.BeaconEventVO"%>
<%@page import="dao.BeaconEventDao"%>
<%@page import="dao.AdminDao" %>
<%@page import="util.StringUtil"%>
<%@page import="util.DateUtil"%>
<%@page import="util.Constant"%>
<%@page import="util.HashUtil" %>

<%
	String menuCate = Constant.MENU_BEACON;
	String subMenuCate = Constant.SUBMENU_BEACON;
	
	String ROLE = cookieBox.getValue("ROLE");
	int roles = Integer.parseInt(ROLE);
	////System.out.println("roles = "+roles);
	
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));
	int rowSize = Integer.parseInt(StringUtil.nchk(request.getParameter("rowSize"), "10"));
	String searchType = StringUtil.nchk(request.getParameter("searchType"),"");
	String keyword = StringUtil.nchk(request.getParameter("keyword"),"");

	BeaconEventDao dao = new BeaconEventDao();
	int totalcnt = dao.BeaconEventCnt();
	
	int rownum = 0;
	//dao = new MemberDao();
	ArrayList<BeaconEventVO> list = dao.selectCmsBeaconEventList(pageno, rowSize, searchType, keyword);
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
		
		function beaconView(no){
			location.href = "beacon_view.jsp?no=" + no + "&pageno="+<%=pageno%>;
		}
		
		function search(){
			if($('#keyword').val() != "" && $('#searchType').val() == ""){
				alert("검색 타입을 선택해 주세요.");
				return false;
			}else{
				document.frm.submit();
			}
		}
		
		function delMem(memSeqNo,pageNo){
			if(confirm("정말 삭제하시겠습니까??") == true){
				location.href="member_remove.jsp?memSeqNo="+memSeqNo+"&pageNo="+pageNo;
			}else{
				return;
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
					<h2 class="contentTitle">비콘 관리</h2>
					<div class="contentForm">
						<form name="frm" action="beacon_list.jsp" method="post">
						<input type="hidden" name="pageno" value="1">
						
						<div class="searchArea">
							<table class="searchTable" >
								<tbody>
								<tr> 
								  	<td>
										<select name="searchType" id="searchType">
											<option value="">선택하세요</option>
											<option value="0" <%if(searchType.equals("0")){ %>selected="selected"<%} %>>비콘번호(Major)</option>
											<option value="1" <%if(searchType.equals("1")){ %>selected="selected"<%} %>>비콘번호(Minor)</option>
											<option value="2" <%if(searchType.equals("2")){ %>selected="selected"<%} %>>팝업 위치</option>	
											<option value="3" <%if(searchType.equals("3")){ %>selected="selected"<%} %>>팝업사용</option>										
									  	</select>
									    <input type="text" name="keyword" id="keyword" value="<%=keyword %>" size="50"/>									  
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
											<th style="width: 7% ;text-align: center;">비콘번호(Major)</th>
											<th style="width: 7% ;text-align: center;">비콘번호(Minor)</th>
											<th style="width: 5% ;text-align: center;">위치</th>
											<th style="width: 7% ;text-align: center;">팝업위치</th>
											<th style="width: 7% ;text-align: center;">팝업사용</th>
											<th style="width: 7% ;text-align: center;">등록일</th>
										</tr>
									</thead>
									<tbody>
										 <%
										if (list.size() > 0) {
											for (int i=0; i<list.size(); i++) {
												BeaconEventVO vo = list.get(i);
												String result ="";
												String crtDate = vo.getCrtDate().substring(0,16);
												String use = "";
												if(vo.getBeaconUse().equals("Y")){
													use = "사용" ;	
												}else if(vo.getBeaconUse().equals("N")){
													use = "미사용" ;	
												}
												int ten =  i;
												
												int pagevalue = (pageno-1)*10;
												
												rownum = totalcnt -pagevalue - ten ;
												
												
												
												%>
												 
											<tr onclick="javascript:beaconView(<%=vo.getSeqNo() %>);" style="cursor: pointer;" <%if((i % 2) == 1 ) {%> class="even"<%} %>>
												<td style="text-align:center;"><%=rownum %></td>
												<td style="text-align:center;"><%=vo.getBeaconMajor() %></td>
												<td style="text-align:center;"><%=vo.getBeaconMinor() %></td>
												<td style="text-align:center;"><%=vo.getBeaconLocation() %></td>
												<td style="text-align:center;"><%=StringUtil.NVL(vo.getBeaconPosition()) %></td>
												<td style="text-align:center;"><%=use%></td>
												<td style="text-align:center;"><%=crtDate %></td>
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
										<%
										if(roles == 0){%>
											<button type="button" OnClick="location.href='beacon_AdminAdd.jsp';">대량 등록</button>
											<button type="button" OnClick="location.href='beacon_AdminRemove.jsp';">대량 삭제</button>
										<%}%>
								      	<button type="button" OnClick="location.href='beacon_add.jsp';">등록</button>
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
