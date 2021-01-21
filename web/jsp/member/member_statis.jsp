<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="vo.StatisticsVO"%>
<%@page import="dao.StatisticsDao"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="vo.AffiliationVO"%>
<%@page import="dao.AffiliationDao"%>

<%
	String menuCate = Constant.MENU_MEMBER;
	String subMenuCate = Constant.SUBMENU_STATIS;

	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
	String today = formatter.format(new java.util.Date());  // 오늘 날짜를 불러옴
	
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));
	int rowSize = Integer.parseInt(StringUtil.nchk(request.getParameter("rowSize"), "20"));
	int date = Integer.parseInt(StringUtil.nchk(request.getParameter("date"),"0"));    // 선택한 날짜 타입
	int stair = Integer.parseInt(StringUtil.nchk(request.getParameter("stair"),"1"));
	int total = 0;
	int roles = Integer.parseInt(StringUtil.nchk(request.getParameter("roles"),"0"));

	String ROLE = cookieBox.getValue("ROLE");
	int ckroles = Integer.parseInt(ROLE);

	if(ckroles > 0){
		roles = ckroles;
	}

	String startDate = StringUtil.nchk(request.getParameter("startDate"), today);
	String endDate = StringUtil.nchk(request.getParameter("endDate"), today);

	StatisticsDao sdao = new StatisticsDao();

	ArrayList<StatisticsVO> alist = sdao.selectStatisticsNew(startDate, endDate, date, pageno, rowSize, stair, roles);
	total = sdao.selectTotalCntNew(startDate, endDate, date, pageno, rowSize, stair, roles);
	
	sdao.closeConn();

	//소속 리스트 가져오기
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
		$(function(){
			$( "#startDate" ).datepicker();
			//옵션  : 매개변수값 2번째가 옵션의 타입이며 3번째 항목은 옵션에 대한 설정 값
		      $( "#startDate" ).datepicker( "option", "dateFormat", "yy-mm-dd" ); //데이터 포맷으로 날짜의 반환 타입을 지정
		      $( "#startDate" ).datepicker( "option", "showAnim", "slideDown" );      //달력의 표시 형태
		   	  $("#startDate").val("<%=startDate%>");
		});

        $(function(){
            $( "#endDate" ).datepicker();
            //옵션  : 매개변수값 2번째가 옵션의 타입이며 3번째 항목은 옵션에 대한 설정 값
            $( "#endDate" ).datepicker( "option", "dateFormat", "yy-mm-dd" ); //데이터 포맷으로 날짜의 반환 타입을 지정
            $( "#endDate" ).datepicker( "option", "showAnim", "slideDown" );      //달력의 표시 형태
            $("#endDate").val("<%=endDate%>");
        });
		
		
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
		
		function search(){
			/*
			if (document.getElementById("startDate").value.length == 0) {
				alert("날짜를 선택해 주세요.");
				return false;
			}else  if (document.getElementById("stair").value.length == 0) {
				alert("최소 계단 수를 입력해 주세요.");
				document.getElementById("stair").focus();
				return false;
			}
			*/

			document.frm.submit();
		}
		
		function fnc_excel(){
			document.frm.action = "member_statis_excel.jsp";
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
					<h2 class="contentTitle">회원 이벤트</h2>
					<div class="contentForm">
						<form name="frm" action="member_statis.jsp" method="post">
						<input type="hidden" name="pageno" value="1">
						<div class="searchArea">
							<table class="searchTable" >
								<tbody>
									<tr>
										<td>
											<div>
												<input type="text" id="startDate" name="startDate" class="form-control" placeholder="검색 시작일" size="10" readonly style="text-align:center; background:white; cursor: pointer;"/>
												~<input type="text" id="endDate" name="endDate" class="form-control" placeholder="검색 종료일" size="10" readonly style="text-align:center; background:white; cursor: pointer;"/>
												<%--<input type="radio" id="date" name="date" value="0" <%if(date==0){%>checked="checked"<%} %>>일&nbsp;&nbsp;
												<input type="radio" id="date" name="date" value="1" <%if(date==1){%>checked="checked"<%} %>>주&nbsp;&nbsp;
												<input type="radio" id="date" name="date" value="2" <%if(date==2){%>checked="checked"<%} %>>월&nbsp;&nbsp;
												<input type="radio" id="date" name="date" value="3" <%if(date==3){%>checked="checked"<%} %>>연&nbsp;&nbsp;
												<input type="text" id="stair" name="stair" size ="10" value="<%= stair%>" style="text-align: center">&nbsp;&nbsp;&nbsp;--%>
												<%if(ckroles == 0){%>
												<select name="roles" id="roles">
													<option value="0">소속 선택</option>
													<%
														if(list.size() > 0){
															for(int i=0; i<list.size(); i++){
																AffiliationVO vo = list.get(i);
													%>
													<option value="<%=vo.getSeqNo()%>" <%if(vo.getSeqNo() == roles){%>selected<%}%>><%=vo.getAffiliationName() %></option>
													<%
															}
														}
													%>
												</select>
												<%}else{%>
												<input type="hidden" name="roles" value="<%=ckroles%>">
												<%}%>
												<input type="button" value="검색" onclick="search();" />
												<input type="button" value="엑셀다운로드" onclick="fnc_excel();" />
											</div>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
						<div >
						&nbsp;
						</div>
						<!-- 
						<div class="searchButton">
				      		<input type="button" value="검색" onclick="search();" />
				    	</div>
						 -->
						<div class="listBody">
							<div class="listTable">
							<div class="listMenu"><p>검색된 날짜  : <%=startDate%>~<%=endDate%></p></div>
								<table class="hovertable" width="100%">
									<thead>
										<tr align="center">
											<th style="width: 10% ;text-align: center;">성명</th>
											<th style="width: 15% ;text-align: center;">전화번호</th>
											<th style="width: 17% ;text-align: center;">소속명</th>
											<th style="width: 17% ;text-align: center;">부서명</th>
											<th style="width: 10% ;text-align: center;">계단 수</th>									
										</tr>
									</thead>
									<tbody>
										 <% 
										if (alist.size() > 0) {
											for (int i=0; i<alist.size(); i++) {
												StatisticsVO vo = alist.get(i);
												
												%>
											<tr <%if((i % 2) == 1 ) {%> class="even"<%} %>>
												<td><%=StringUtil.NVL(vo.getMemName()) %></td>
												<td><%=vo.getMemNumber() %></td>
												<td><%=StringUtil.NVL(vo.getAffiliationName()) %></td>
												<td><%=StringUtil.NVL(vo.getDepartName()) %></td>
												<td><%=vo.getTodaytotal() %></td>
					
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
							<div class="listMenu"><p>total <%=total %>건</p></div>
							<div calss="listBottom">
								<div class="listBottomCenterOnly">
									<jsp:include page="../include/inc_paging.jsp">
										<jsp:param name="totalRecord" value="<%=total%>"/>
										<jsp:param name="pageNo" value="<%=pageno%>"/>
										<jsp:param name="rowCount" value="<%=rowSize %>"/> 
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
