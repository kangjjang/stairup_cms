<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="vo.DepartVO"%>
<%@page import="dao.DepartDao"%>
<%@page import="util.StringUtil"%>
<%@page import="util.DateUtil"%>
<%@page import="util.Constant"%>
<%
	String menuCate = Constant.MENU_TEAM;
	String subMenuCate = Constant.SUBMENU_TEAM_LIST;
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));
/* 	String notiType = StringUtil.nchk(request.getParameter("notiType"), "0"); */
	int gubun = Integer.parseInt(StringUtil.nchk(request.getParameter("gubun"),"0"));
	DepartDao dao = new DepartDao();
	ArrayList<DepartVO> list = dao.selectTimeLeagueList(pageno, 10,0);
	//dao = new DepartDao();
	int totalcnt = dao.leagueCnt();
	
	String today = DateUtil.getTime("yyyy.MM.dd");
	dao.closeConn();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<%@ include file="../include/inc_top.jsp" %>
		<script language=javascript>
		<%-- $(function(){
			$('div.topMenuContent ul li:nth-child(<%=menuCate%>)').addClass('topMenuOn');
			$('div.menuList ul li:nth-child(<%=subMenuCate%>)').addClass('leftMenuOn');
			
			$('input:submit, button').button();
			$('#radio').buttonset();
			
			$('#radio input:radio').click(function(){
				document.frm.notiType.value = $(this).val();
				pageLink(1);
			});
			
			bindFlag();
		});
		
		function bindFlag(){
			var no, flag;
			$('input:checkbox').change(function(){
				if($(this).is(":checked")){
					flag = "Y";
				}else{
					flag = "N";
				}
				no = $(this).attr("data-no");
				
				gfnc_Ajax({
				    type: "post",
				    url: "notice_flag_update_ajax.jsp",
				    data: {
				        no: no,
				        flag: flag
				    },
				    dataType: "text",
				    success: function(data){
				        if(data == 1){
				        	alert("변경되었습니다.");
				        }else{
				        	alert("변경 실패했습니다.");
				        }        
				    },
				    error: function(err) {
				    	alert(err.responseText);
				    }
				}); 
			});			
		}
		
		function pageLink(arg) {
			document.frm.pageno.value    = arg;
			document.frm.submit();
		} --%>
		function reset(){
			if(confirm("지금까지의 경기 결과가 초기화 하시겠습니까?") == true){
				location.href="league_reset.jsp";
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
					<h2 class="contentTitle">팀별리그 라이브 관리</h2>
					<div class="contentForm">
						<form name="frm" action="league_list.jsp" method="post">
						<input type="hidden" name="pageno" value="1">
				<%-- 		<input type="hidden" name="notiType" value="<%=notiType%>"> --%>
					
						<div class="listBody">
							<div class="listTable">
								<table class="hovertable" width="100%">
									<thead>
										<tr align="center">
											<th>번호</th>
											<th>홈팀</th>
											<th>어웨이팀</th>
											<th>시작일</th>
											<th>종료일</th>
										</tr>
									</thead>
									<tbody>		
										<%
										if(list.size() > 0){
											for(int i=0; i<list.size(); i++){
												DepartVO vo = list.get(i);
												String crtDate = vo.getCrtDate().substring(0,16);
												String endDate = vo.getEndDate().substring(0,16);
											%>
											<tr <%if((i % 2) == 1 ){%> class="even"<%}%>>
												<td><%=i+1%></td>
												<td><%=vo.getHomeName()%></td>
												<td><%=vo.getAwayName() %></td>
												<td><%=crtDate%></td>
												<td><%=endDate%></td>
											</tr>
											<%
											}
										}else{
											out.println("<tr><td colspan=8>조회된 목록이 없습니다.</td></tr>");
										}
										%>
									</tbody>
								</table>							
							</div>
							<div class="listMenu"><p>total <%=totalcnt %>건</p></div>
							<div class="listBottom">
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
					      	<button type="button" OnClick="location.href='league_add.jsp';">등록</button>
					      	<button type="button" OnClick="reset()">초기화</button>
					    </div>							
					</div>
				</div>
			</div>
			<div id="bottomWrap"></div>
		
		</div>
		<div style="display: none;"></div>
	</body>
</html>
