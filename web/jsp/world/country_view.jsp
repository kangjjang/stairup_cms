<%@page import="dao.NoticeDao"%>
<%@page import="vo.NoticeVO"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList" %>
<%@page import="vo.CityVO" %>
<%@page import="dao.CityDao" %>
<%@page import="util.StringUtil" %>
<%@page import="util.Constant" %>

<%
	String menuCate = Constant.MENU_STAY;
	String subMenuCate = Constant.SUBMENU_COUNTRY;
	int countrySeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("countrySeqNo"), "1"));
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));

	CityDao dao = new CityDao();
	CityVO vo = new CityVO();
	vo = dao.countryView(countrySeqNo);
	//dao = new CityDao();
	ArrayList<CityVO> list = dao.selectCountryCity(countrySeqNo);
	dao.closeConn();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<title>국가 상세 정보</title>
	<%@ include file="../include/inc_top.jsp" %>
	<script language="javascript">
	$(function(){
		$('div.topMenuContent ul li:nth-child(<%=menuCate%>)').addClass('topMenuOn');
		$('div.menuList ul li:nth-child(<%=subMenuCate%>)').addClass('leftMenuOn');
		
		$('input:submit, input:button, button').button();
	});
	
	function memberDel(no, pageno){
		if(confirm('삭제하시겠습니까?')){
			location.href = 'notice_remove.jsp?no='+no+'&pageno='+pageno;
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
					<h2 class="contentTitle">국가 상세 정보</h2>		
							
					<div class="contentForm">
						<form name="frm" action="member_addProc.jsp" method="post">
						<div class="listMenu"><p>국가 상세 정보</p></div>	
						<table class="detailInfo">
							<tbody>
							<tr class="detailInfoFirst"> 
								<th>국가명</th>
							  	<td><%=vo.getCountryName() %></td>
							  	<th>진행순서</th>
							  	<td><%=vo.getOrderLy()%></td>
							</tr>
							<tr>
							<th>이미지</th>
							<td><img src="/upload/country/<%=vo.getCountryPic() %>" width="150" height="100" /></td>
							</tr>
							
						  	</tbody>
						</table>
						<table class="hovertable" width="100%">
						<thead>
						<tr align="center">
							<th>해당 도시명</th>
							<th>도시 진행순서</th>
						</tr>
						</thead>
						<tbody>
						<%if(list!=null){ 
									for(int i = 0; i < list.size(); i++){
										CityVO city = list.get(i);	
								%>
								<tr>
							  		<td><%=city.getCityName() %></td>
							  		<td><%=city.getOrderLy() %></td>
							  		</tr>
							  	<%}
								}%>
						</tbody>
						</table>
						</form>
					</div>
					<div class="alignRightButton">
						<input type="button" OnClick="location.href='notice_modify.jsp?countrySeqNo=<%=countrySeqNo%>&pageno=<%=pageno%>';" value="수정" />
				      	<input type="button" OnClick="memberDel('<%=countrySeqNo%>', '<%=pageno%>');" value="삭제" />
				      	<input type="button" OnClick="location.href='notice_list.jsp?pageno=<%=pageno%>';" value="목록" />
				    </div>				    
				</div>
			</div>
		<div id="bottomWrap"></div>	
	</div>
	<div style="display: none;"></div>
</body>
