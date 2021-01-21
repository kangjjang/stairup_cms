<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="vo.DonationVO"%>
<%@page import="dao.DonationDao"%>
<%@page import="util.StringUtil"%>
<%@page import="util.DateUtil"%>
<%@page import="util.Constant"%>
<%@page import="java.net.URLDecoder"%>
<%
	String menuCate = Constant.MENU_CONTENT;
	String subMenuCate = Constant.SUBMENU_WALL_BOARD;;
	int totalcnt =0;
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));
	int searchType = Integer.parseInt(StringUtil.nchk(request.getParameter("searchType"),"1"));
	/* int gubun = Integer.parseInt(StringUtil.nchk(request.getParameter("ragubun"),"1"));	 */	
	String keyword = URLDecoder.decode(StringUtil.nchk(request.getParameter("keyword"),""), "UTF-8");
	DonationDao dao = new DonationDao();
	ArrayList<DonationVO> list = dao.giveList(pageno, 10, searchType, keyword);
	//dao = new DonationDao();
	totalcnt = dao.giveCnt();
	String today = DateUtil.getTime("yyyy.MM.dd");
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
			$('#radio input:radio').click(function(){
				document.frm.notiType.value = $(this).val();
				pageLink(1);
			});
		});
		
		function pageLink(arg) {
			document.frm.pageno.value    = arg;
			document.frm.submit();
		}
		
/* 		function search(){
			var kw = document.all.keyword.value;
			var st = document.all.searchType.value;
		
			location.href="app_list.jsp?searchType="+st+"&keyword="+kw;
		}
		function serachGubun(n){
			location.href="app_list.jsp?gubun="+n;
		} */
		function search(){
			
 	 	 	if($('#keyword').val() != "" && $('#searchType').val() == ""){
				alert("검색 타입을 선택해 주세요.");
				return false;
			}else{  
				document.frm.submit();
		  	 }  
		}
		
		function giveView(no,crtDate,endDate){
			location.href = "give_view.jsp?no=" + no + "&pageno="+<%=pageno%> + "&crtDate=" + crtDate + "&endDate=" + endDate;
		}
		
		
	</script>
	</head>
	<body>
		<div id="wrap">
			<%@ include file="../include/inc_top_menu.jsp" %>			
			<div id="contentWrap">  
				<%@ include file="../include/inc_left_menu.jsp" %>
				<div class="contentArea">
					<h2 class="contentTitle">힘내라 기부 리스트</h2>
					<div class="contentForm">
						<form name="frm" action="give_list.jsp" method="post">
						<input type="hidden" name="pageno" value="1">	
						<div class="searchArea">
							<table class="searchTable" >
								<tbody>
								<tr> 
								  	<td>
										<select name="searchType" id="searchType">
											<option value="">선택하세요</option>
											<option value="1" <%if(searchType==1){ %>selected="selected"<%} %>>제목</option>
										</select>	
										<input type="text" name="keyword" id="keyword" value="<%=keyword %>" size="50"/>
										<input type="submit" value="검색" onClick="search();" />							  
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
											<th style="width: 15% ;text-align: center;">제목</th>
											<th style="width: 5% ;text-align: center;">목표계단</th>
											<th style="width: 5% ;text-align: center;">입력일</th>
											<th style="width: 7% ;text-align: center;">종료일</th>
										</tr>
									</thead>
									<tbody>
										<%
										  if (list.size() > 0) {
									
											for (int i=0; i<list.size(); i++) {
												DonationVO vo = list.get(i);  
												String crtDate = vo.getCrtDate().substring(0,16);
												String endDate = vo.getEndDate().substring(0,16);
												int ten =  i;
												int pagevalue = (pageno-1)*10;
												int rownum = totalcnt -pagevalue - ten ;
										%>
												
											 <tr onclick="javascript:giveView('<%=vo.getGiveSeqNo()%>','<%=vo.getCrtDate()%>','<%=vo.getEndDate() %>');" style="cursor: pointer;" <%if((i % 2) == 1 ) {%> class="even"<%} %>>
												<td><%=rownum%></td>
												<td><%=vo.getTitle()%></td>
												<td><%=vo.getAim()%></td>
												<td><%=crtDate%></td>
												<td><%=endDate%></td>
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
										<jsp:param name="rowCount" value="<%=Constant.DEFAULT_ROW_CNT %>"/> 
										<jsp:param name="pageGroup" value="<%=Constant.DEFAULT_BLOCK_SIZE %>"/>
									</jsp:include>  								
								</div>
							</div>
						</div>
						</form>
						<div class="alignRightButton">
					      	<button type="button" OnClick="location.href='give_add.jsp';">등록</button>
					    </div>		
					</div>
				</div>
			</div>
			<div id="bottomWrap"></div>		
		</div>
		<div style="display: none;"></div>
	</body>
</html>
