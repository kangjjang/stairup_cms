<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="vo.CityVO"%>
<%@page import="dao.CityDao"%>
<%@page import="util.StringUtil"%>
<%@page import="util.DateUtil"%>
<%@page import="util.Constant"%>
<%
	String menuCate = Constant.MENU_STAY;
	String subMenuCate = Constant.SUBMENU_CITY;
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));
	CityDao dao = new CityDao();
	int rowSize = 10;
	ArrayList<CityVO> list = dao.selectCityList(pageno, rowSize);
	//dao = new CityDao();
	int totalcnt = dao.countCity();
	
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
		}
		
		function cityView(no){
			location.href = "city_modify.jsp?citySeqNo=" + no + "&pageno="+<%=pageno%>;
		}
		
		</script>		
	</head>
	<body>
		<div id="wrap">
			<%@ include file="../include/inc_top_menu.jsp" %>
			<div id="contentWrap">  
				<%@ include file="../include/inc_left_menu.jsp" %>
				<div class="contentArea">
					<h2 class="contentTitle">도시 관리</h2>
					<div class="contentForm">
						<form name="frm" action="city_list.jsp" method="post">
						<input type="hidden" name="pageno" value="1">
						<div class="listBody">
							<div class="listTable">
								<table class="hovertable" width="100%">
									<thead>
										<tr align="center">
											<th style="text-align: center; width: 7%;">번호</th>
											<th style="text-align: center; width: 23%;">국가명</th>
											<th style="text-align: center; width: 20%;">도시명</th>
											<th style="text-align: center; width: 15%;">진행순서</th>
											<th style="text-align: center; width: 10%;">목표계단수</th>
										</tr>
									</thead>
									<tbody>		
										<%
										if(list.size() > 0){
											for(int i=0; i<list.size(); i++){
												CityVO vo = list.get(i);
												
												int ten =  i;
												int pagevalue = (pageno-1)*10;
												int rownum = totalcnt -pagevalue - ten ;
											%>
											<tr onclick="javascript:cityView(<%=vo.getCitySeqNo() %>);" style="cursor: pointer;" <%if((i % 2) == 1 ){%> class="even"<%}%>>
												<td><%=rownum%></td>
												<td><%=vo.getCountryName() %></td>
												<td><%=vo.getCityName() %></td>
												<td><%=vo.getOrderLy() %></td>
												<td><%=vo.getTotalStair() %></td>
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
					      	<button type="button" OnClick="location.href='city_add.jsp';">등록</button>
					    </div>							
					</div>
				</div>
			</div>
			<div id="bottomWrap"></div>
		
		</div>
		<div style="display: none;"></div>
	</body>
</html>
