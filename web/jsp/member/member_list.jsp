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

<%
	String ROLE = cookieBox.getValue("ROLE");
	int roles = Integer.parseInt(ROLE);

	String menuCate = Constant.MENU_MEMBER;
	String subMenuCate = Constant.SUBMENU_MEMBER;
	
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));
	int rowSize = Integer.parseInt(StringUtil.nchk(request.getParameter("rowSize"), "10"));
	String searchType = StringUtil.nchk(request.getParameter("searchType"),"");
	String keyword = StringUtil.nchk(request.getParameter("keyword"),"");

	MemberDao dao = new MemberDao();
	int totalcnt = dao.memberCnt(searchType, keyword, roles);
	
	//dao = new MemberDao();
	ArrayList<MemberVO> list = dao.selectCmsMemberList(pageno, rowSize, searchType, keyword, roles);
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
		
		function search(){
			if($('#keyword').val() != "" && $('#searchType').val() == ""){
				alert("검색 타입을 선택해 주세요.");
				return false;
			}else{
				document.frm.submit();
			}
		}
		
		function memberView(no){
			location.href = "member_view.jsp?no=" + no + "&pageno="+<%=pageno%>;
		}
		
		</script>
	</head>
	<body>
		<div id="wrap">
			<%@ include file="../include/inc_top_menu.jsp" %>			
			<div id="contentWrap">  
				<%@ include file="../include/inc_left_menu.jsp" %>
				<div class="contentArea">
					<h2 class="contentTitle">회원 정보</h2>
					<div class="contentForm">
						<form name="frm" action="member_list.jsp" method="post">
						<input type="hidden" name="pageno" value="1">
						
						<div class="searchArea">
							<table class="searchTable" >
								<tbody>
								<tr> 
								  	<td>
										<select name="searchType" id="searchType">
											<option value="1" <%if(searchType.equals("1")){ %>selected="selected"<%} %>>성명</option>
											<option value="2" <%if(searchType.equals("2")){ %>selected="selected"<%} %>>전화번호</option>
											<option value="3" <%if(searchType.equals("3")){ %>selected="selected"<%} %>>소속</option>
											<option value="4" <%if(searchType.equals("4")){ %>selected="selected"<%} %>>부서</option>										
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
											<th style="width: 7% ;text-align: center;">ID</th>
											<th style="width: 5% ;text-align: center;">성명</th>
											<th style="width: 10% ;text-align: center;">소속명</th>
											<th style="width: 10% ;text-align: center;">부서명</th>
											<th style="width: 7% ;text-align: center;">등록일</th>
										</tr>
									</thead>
									<tbody>
										 <%
										if (list.size() > 0) {
											for (int i=0; i<list.size(); i++) {
												MemberVO vo = list.get(i);
												String result ="";
												String crtDate = vo.getCrtDate().substring(0,16);
												int ten =  i;
												int pagevalue = (pageno-1)*10;
												int rownum = totalcnt -pagevalue - ten ;
												%>
											<tr onclick="javascript:memberView(<%=vo.getMemSeqNo() %>);" style="cursor: pointer;" <%if((i % 2) == 1 ) {%> class="even"<%} %>>
												<td><%=rownum %></td>
												<td><%=vo.getMemId() %></td>
												<td><%=StringUtil.NVL(vo.getMemName()) %></td>
												<td><%=StringUtil.NVL(vo.getMemAffiliationName()) %></td>
												<td><%=StringUtil.NVL(vo.getMemDepart()) %></td>
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
