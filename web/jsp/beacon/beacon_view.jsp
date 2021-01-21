<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList" %>
<%@page import="util.StringUtil" %>
<%@page import="util.Constant" %>
<%@page import="vo.BeaconSoundVO"%>
<%@page import="vo.AffiliationVO"%>
<%@page import="vo.MemberVO"%>
<%@page import="dao.BeaconEventDao"%>
<%@page import="vo.BeaconEventVO"%>
<%
	String menuCate = Constant.MENU_BEACON;
	String subMenuCate = Constant.SUBMENU_BEACON;
	int no = Integer.parseInt(StringUtil.nchk(request.getParameter("no"), "1"));
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));

	BeaconEventDao dao = new BeaconEventDao();
	BeaconEventVO vo = new BeaconEventVO();
	vo = dao.BeaconEventView(no);
	String crtDate = vo.getCrtDate().substring(0,16);
	String use ="";
	String url ="";
	if(vo.getBeaconUse().equals("Y")){
		use = "사용";
	}else if(vo.getBeaconUse().equals("N")){
		use = "사용 안함";
	}
	
	String urlUse = vo.getBeaconUrlYN();
	if(urlUse.equals("Y")){
		urlUse = "사용";
	}else{
		urlUse = "사용안함";
	}
	url = vo.getBeaconUrl();
	if(vo.getBeaconUrl() == null){
		url = "";
	}
	
	//최초인식자 정보 가져오기
	MemberVO vo2 = new MemberVO();
	vo2 = dao.selectBeaconEventLogTopMember(vo.getBeaconMajor(), vo.getBeaconMinor());
	
	dao.closeConn();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<title>공지 상세 정보</title>
	<%@ include file="../include/inc_top.jsp" %>
	<script language="javascript">
	$(function(){
		$('div.topMenuContent ul li:nth-child(<%=menuCate%>)').addClass('topMenuOn');
		$('div.menuList ul li:nth-child(<%=subMenuCate%>)').addClass('leftMenuOn');
		
		$('input:submit, input:button, button').button();
	});
	
	function beaconDel(no, pageno){
		if(confirm('삭제하시겠습니까?')){
			location.href = 'beacon_remove.jsp?no='+no+'&pageno='+pageno;
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
					<h2 class="contentTitle">비콘 상세 정보</h2>		
							
					<div class="contentForm">
						<form name="frm" action="member_addProc.jsp" method="post">
						<div class="listMenu"><p>공지 정보</p></div>	
						<table class="detailInfo">
							<tbody>
							<tr class="detailInfoOneItemFirst"> 
							  <th>팝업 로고</th>
							  <td>
							  	<img src="/upload/affiliation/<%=vo.getLogoImage()%>" width="150" height="100"/>
							  </td>
							</tr>
							<tr> 
								<th>비콘 번호(Major)</th>
							  	<td><%=vo.getBeaconMajor() %></td>
							  	<th>등록일자</th>
							  	<td><%=crtDate%></td>
							</tr>
							<tr> 
								<th>비콘 번호(Minor)</th>
							  	<td colspan="4"><%=vo.getBeaconMinor()%></td>
							</tr>
							<tr> 
								<th>팝업 위치</th>
							  	<td colspan="4"><%=vo.getBeaconPosition()%></td>
							</tr>
							<tr> 
								<th>위치</th>
							  	<td colspan="4"><%=vo.getBeaconLocation()%></td>
							</tr>
							<tr> 
								<th>팝업 사용</th>
							  	<td colspan="4"><%=use%></td>
							</tr>
							<tr> 
								<th>팝업 제목</th>
							  	<td colspan="4"><%=vo.getBeaconTitle()%></td>
							</tr>
							<tr>
							<th>이미지</th>
							<%if(vo.getBeaconImage() == null || vo.getBeaconImage().equals("")){ %>
								<td colspan="4">이미지 없음</td>
							<%}else{%>
								<td colspan="4"><img src="/upload/beacon/<%=vo.getBeaconImage()%>" width="150" height="100" /></td>
							<%}%>
							</tr>
							<tr>
								<th>팝업 내용</th>
							  	<td colspan="4"><%=vo.getBeaconContent()%></td>
							</tr>
							<tr> 
							  <th>팝업 기간</th>
							  <td colspan="4">
							  	<%if(vo.getBeaconPopFromDateTime() != null && vo.getBeaconPopFromDateTime().trim().length() > 0) {%>
							  	  <%=vo.getBeaconPopFromDateTime().substring(0, 16) %>
							  	  부터 <%=vo.getBeaconPopToDateTime().substring(0, 16) %>
							  	<%} %>
							  </td>
							</tr>
							<tr> 
							  <th>팝업 빈도</th>
							  <td colspan="4">
							    <%if(vo.getBeaconPopCnt() == 0) {%>
							  	  매번
							  	<%}else{ %>
							  	 최초 1회
							  	<%} %>
						      </td>
							</tr>
							<tr> 
							  <th>팝업 사운드</th>
							  <%
							  if(vo.getBeaconSoundName() == null){
							  %>
							  	  <td>선택 없음</td>
							  <%	  
							  }else{%>
								  <td><%=vo.getBeaconSoundName() %></td>
							  <%}
							  %>
							  
							</tr>
							<tr if="urlmenu"> 
								<th>URL 사용 여부</th>
							  	<td colspan="4"><%=urlUse%></td>
							</tr>
							<%if(vo.getBeaconUrlYN().equals("Y")){%>
								<tr if="urlmenu"> 
								<th>URL</th>
							  	<td colspan="4"><%=url%></td>
							</tr>
							<%}%>
							<tr> 
							  <th>최초1회팝업 당첨자</th>
							  <td colspan="4">
							    <%
							    if(vo2 != null && vo2.getMemSeqNo() > 0) {
							    	out.print("<a href='/jsp/member/member_view.jsp?no=" + vo2.getMemSeqNo() + "'>" + vo2.getMemName() + "</a>");	
								} 
							    %>
						      </td>
							</tr>
							
						  	</tbody>
						</table>
						</form>
					</div>
					<div class="alignRightButton">
						<input type="button" OnClick="location.href='beacon_modify.jsp?no=<%=no%>&pageno=<%=pageno%>';" value="수정" />
				      	<input type="button" OnClick="beaconDel('<%=no%>', '<%=pageno%>');" value="삭제" />
				      	<input type="button" OnClick="location.href='beacon_list.jsp?pageno=<%=pageno%>';" value="목록" />
				    </div>				    
				</div>
			</div>
		<div id="bottomWrap"></div>	
	</div>
	<div style="display: none;"></div>
</body>

