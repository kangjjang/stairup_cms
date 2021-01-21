<%@page import="vo.AffiliationVO"%>
<%@page import="dao.AffiliationDao"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="util.StringUtil"%>
<%@page import="util.DateUtil"%>
<%@page import="util.Constant"%>
<%
	String menuCate = Constant.MENU_DEPART;
	String subMenuCate = Constant.SUBMENU_AFFILIATION;

	CookieBox box = new CookieBox(request);
	String id = cookieBox.getValue("ID");
	////System.out.println("id = "+id);
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));
	String searchType = StringUtil.nchk(request.getParameter("searchType"),"");
	String keyword = StringUtil.nchk(request.getParameter("keyword"),"");
	int rowSize = 10;

	AffiliationDao dao = new AffiliationDao();
	ArrayList<AffiliationVO> list = dao.affiliationList(pageno, rowSize,searchType,keyword);/* selectList(memSeqNo, wallMemSeqNo, wallReqType, pageno, rowSize); */
	//dao = new DepartDao();
	int totalcnt = dao.affiliationCnt(); 
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
			
			$('input:submit, button').button();
			$('#radio').buttonset();
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
		
		function affiliationCntView(no){
			location.href = "affiliation_modify.jsp?affiliationSeqNo=" + no + "&pageno="+<%=pageno%>;
		}
		
		</script>		
	</head>
	<body>
		<div id="wrap">
			<%@ include file="../include/inc_top_menu.jsp" %>
			<div id="contentWrap">  
				<%@ include file="../include/inc_left_menu.jsp" %>
				<div class="contentArea">
					<h2 class="contentTitle">소속 관리</h2>
					<div class="contentForm">
						<form name="frm" action="affiliation_list.jsp" method="post">
						<input type="hidden" name="pageno" value="1">
						<div class="searchArea">
							<table class="searchTable" >
								<tbody>
								<tr> 
								  	<td>
										<select name="searchType" id="searchType">
											<option value="">선택하세요</option>
											<option value="1" <%if(searchType.equals("1")){ %>selected="selected"<%} %>>소속 명</option>
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
											<th style="width: 3% ;text-align: center;">번호</th>
											<th style="width: 10% ;text-align: center;">소속 명</th>
											<th style="width: 10% ;text-align: center;">이미지</th>
										</tr>
									</thead>
									<tbody>		
										<%
										if(list.size() > 0){
											for(int i=0; i<list.size(); i++){
												AffiliationVO vo = list.get(i);
												int ten =  i;
												int pagevalue = (pageno-1)*10;
												int rownum = totalcnt -pagevalue - ten ;
											%>
											<tr onclick="javascript:affiliationCntView(<%=vo.getSeqNo() %>);" style="cursor: pointer;" <%if((i % 2) == 1 ){%> class="even"<%}%>>
												<td><%=rownum %></td>
												<td><%=vo.getAffiliationName() %></td>
												<td height="60">
													<%if(vo.getAffiliationPic() != null && !vo.getAffiliationPic().equals("")){%>
														<img src="../../upload/affiliation/<%=vo.getAffiliationPic() %>" width="50" height="50" /><%}else{
														%> 이미지 없음<%} %>
												</td>												
											</tr>
											<%
											}
										}else{
											out.println("<tr><td colspan=4>해당게시판에 글이 없습니다.</td></tr>");
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
										<jsp:param name="rowCount" value="<%=Constant.DEFAULT_ROW_CNT %>"/> 
										<jsp:param name="pageGroup" value="<%=Constant.DEFAULT_BLOCK_SIZE %>"/>
									</jsp:include>  								
								</div>
							</div>
						</div>
						</form>
						<div class="alignRightButton">
					      	<button type="button" OnClick="location.href='affiliation_add.jsp';">등록</button>
					    </div>							
					</div>
				</div>
			</div>
			<div id="bottomWrap"></div>
		</div>
		<div style="display: none;"></div>
	</body>
</html>
