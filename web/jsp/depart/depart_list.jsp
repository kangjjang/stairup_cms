<%@page import="vo.DepartVO"%>
<%@page import="dao.DepartDao"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="util.StringUtil"%>
<%@page import="util.DateUtil"%>
<%@page import="util.Constant"%>
<%
	String menuCate = Constant.MENU_DEPART;
	String subMenuCate = Constant.SUBMENU_DEPART;

	int roles = Integer.parseInt(role);
	
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));
	String searchType = StringUtil.nchk(request.getParameter("searchType"),"");
	String keyword = StringUtil.nchk(request.getParameter("keyword"),"");
	int rowSize = 10;
	String board_cate = "1";

	////System.out.println("searchType = "+searchType);
	////System.out.println("roles = "+roles);
	////System.out.println("keyword = "+keyword);
	
	
	DepartDao dao = new DepartDao();
	final String UPLOAD_URL = "depart/";
	ArrayList<DepartVO> list = dao.departList(pageno, rowSize,searchType,keyword,roles);
	//dao = new DepartDao();
	int totalcnt = dao.departCnt(); 
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
		
		function departView(no){
			location.href = "depart_modify.jsp?departSeqNo=" + no + "&pageno="+<%=pageno%>;
		}
		
		</script>		
	</head>
	<body>
		<div id="wrap">
			<%@ include file="../include/inc_top_menu.jsp" %>
			<div id="contentWrap">  
				<%@ include file="../include/inc_left_menu.jsp" %>
				<div class="contentArea">
					<h2 class="contentTitle">부서 관리</h2>
					<div class="contentForm">
						<form name="frm" action="depart_list.jsp" method="post">
						<input type="hidden" name="pageno" value="1">
						<div class="searchArea">
							<table class="searchTable" >
								<tbody>
								<tr> 
								  	<td>
										<select name="searchType" id="searchType">
											<option value="1" <%if(searchType.equals("1")){ %>selected="selected"<%} %>>부서명</option>
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
											<th style="width: 10% ;text-align: center;">소속</th>
											<th style="width: 10% ;text-align: center;">부서명</th>
											<th style="width: 10% ;text-align: center;">이미지</th>
											<th style="width: 3% ;text-align: center;">인원수</th>
										</tr>
									</thead>
									<tbody>		
										<%
										if(list.size() > 0){
											for(int i=0; i<list.size(); i++){
												DepartVO vo = list.get(i);
												int ten =  i;
												int pagevalue = (pageno-1)*10;
												int rownum = totalcnt -pagevalue - ten ;
												
											%>
											<tr onclick="javascript:departView(<%=vo.getHomeSeqNo() %>);" style="cursor: pointer;" <%if((i % 2) == 1 ){%> class="even"<%}%>>
												<td><%=rownum %></td>
												<td><%=vo.getAffiliationName() %></td>
												<td><%=vo.getHomeName() %></td>
												<td height="60">
													<%if(vo.getHomePic() != null && !vo.getHomePic().equals("") ){%>
														<img src="../../upload/depart/<%=vo.getHomePic() %>" width="50" height="50" />
													<%}else{
														%> 이미지 없음<%} %>
												</td>
												<td><%=vo.getDepartPeople() %></td>
												
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
					      	<button type="button" OnClick="location.href='depart_add.jsp';">등록</button>
					    </div>							
					</div>
				</div>
			</div>
			<div id="bottomWrap"></div>
		</div>
		<div style="display: none;"></div>
	</body>
</html>
