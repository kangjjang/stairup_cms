<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="vo.GreetingVO"%>
<%@page import="dao.NoticeDao"%>
<%@page import="util.StringUtil"%>
<%@page import="util.DateUtil"%>
<%@page import="util.Constant"%>
<%
	String ROLE = cookieBox.getValue("ROLE");
	int roles = Integer.parseInt(ROLE);

	String menuCate = Constant.MENU_CONTENT;
	String subMenuCate = Constant.SUBMENU_TRAIN_BAORD;
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));

	NoticeDao dao = new NoticeDao();
	ArrayList<GreetingVO> list = dao.selectGreetingList(pageno, 10);
	//dao = new NoticeDao();
	int totalcnt = dao.greetingCnt();
	
	String today = DateUtil.getTime("yyyy.MM.dd");
	dao.closeConn();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<%@ include file="../include/inc_top.jsp" %>
		<script language=javascript>
		function delGreeting(greetingSeqNo,pageNo){
			location.href="greeting_remove.jsp?greetingSeqNo="+greetingSeqNo+"&pageNo="+pageNo; 
		}
		
		$(function(){
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
					<h2 class="contentTitle">인사말 관리</h2>
					<div class="contentForm">
						<form name="frm" action="greeting_list.jsp" method="post">
						<input type="hidden" name="pageno" value="1">
				<%-- 		<input type="hidden" name="notiType" value="<%=notiType%>"> --%>
					
						<div class="listBody">
							<div class="listTable">
								<table class="hovertable" width="100%">
									<thead>
										<tr align="center">
											<th style="width: 2% ;text-align: center;">번호</th>
											<th style="width: 15% ;text-align: center;">인사말</th>
											<th style="width: 5% ;text-align: center;">입력일</th>
											<th style="width: 3% ;text-align: center;">삭제</th>
										</tr>
									</thead>
									<tbody>		
										<%
										if(list.size() > 0){
											for(int i=0; i<list.size(); i++){
												GreetingVO vo = list.get(i);
												String crtDate = vo.getCrtDate().substring(0,16);
												
												int ten =  i;
												int pagevalue = (pageno-1)*10;
												int rownum = totalcnt -pagevalue - ten ;
											%>
											<tr <%if((i % 2) == 1 ){%> class="even"<%}%>>
												<td><%=rownum%></td>
												<td><a href="greeting_modify.jsp?greetingSeqNo=<%=vo.getGreetingSeqNo()%>"><%=vo.getGreetingContent() %></a></td>
												<td><%=crtDate%></td>
												<td><input type=button value="삭제" onClick="delGreeting(<%=vo.getGreetingSeqNo()%>,<%=pageno%>)"/></td>
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
					      	<button type="button" OnClick="location.href='greeting_add.jsp';">등록</button>
					    </div>							
					</div>
				</div>
			</div>
			<div id="bottomWrap"></div>
		
		</div>
		<div style="display: none;"></div>
	</body>
</html>
