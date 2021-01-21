<%@page import="dao.NoticeDao"%>
<%@page import="vo.NoticeVO"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList" %>
<%@page import="vo.DonationVO" %>
<%@page import="vo.MemberVO" %>
<%@page import="dao.DonationDao" %>
<%@page import="util.StringUtil" %>
<%@page import="util.Constant" %>

<%
	String menuCate = Constant.MENU_CONTENT;
	String subMenuCate = Constant.SUBMENU_WALL_BOARD;;
	int giveSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("no"), "1"));
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));
	String crtDate = StringUtil.nchk(request.getParameter("crtDate"),"");
	String endDate = StringUtil.nchk(request.getParameter("endDate"),"");
	
	DonationDao dao = new DonationDao();
	DonationVO vo = new DonationVO();
	vo = dao.giveView(giveSeqNo);
	//dao = new DonationDao();
	ArrayList<MemberVO> mvo = dao.selectGiveTop(endDate, crtDate);
	crtDate = vo.getCrtDate().substring(0,16);
	dao.closeConn();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<title>힘내라 기부 상세화면</title>
	<%@ include file="../include/inc_top.jsp" %>
	<script language="javascript">
	$(function(){
		$('div.topMenuContent ul li:nth-child(<%=menuCate%>)').addClass('topMenuOn');
		$('div.menuList ul li:nth-child(<%=subMenuCate%>)').addClass('leftMenuOn');
		
		$('input:submit, input:button, button').button();
	});
	
	function memberDel(no, pageno){
		if(confirm('삭제하시겠습니까?')){
			location.href = 'give_remove.jsp?giveSeqNo='+no+'&pageno='+pageno;
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
					<h2 class="contentTitle">힘내라 기부 상세화면</h2>		
							
					<div class="contentForm">
						<form name="frm" action="member_addProc.jsp" method="post">
						<table class="detailInfo">
							<tbody>
							<tr class="detailInfoFirst"> 
								<th>제목</th>
							  	<td><%=vo.getTitle() %></td>
							  	<th>등록일자</th>
							  	<td><%=crtDate%></td>
							</tr>
							<tr>
							<th>이미지</th>
							<td>
								<%if(!vo.getPic().equals("")){%>
								<img src="/upload/Thumb/give/<%=vo.getPic() %>" width="150" height="100" />
								<%}else{%>이미지 없음<%} %>
							</td>
							</tr>
							<tr>
								<th>상세내용</th>
							  	<td colspan="4"><%=vo.getContent()%></td>
							</tr>
							<tr>
							<th rowspan=<%=mvo.size()+1 %>>기부천사 top3</th>
							
							<%
							if(mvo!=null){
								for(int i =0; i <mvo.size(); i++){
									MemberVO giveAngel = mvo.get(i);
									%>
									<tr>
									<td colspan="4">
										<%if(giveAngel.getMemPic() != null){%>
											<img src="/upload/memberProfile/<%=giveAngel.getMemPic() %>" width="100" height="50" /><%}else{%> 이미지 없음 - <%} %><%=giveAngel.getMemName() %>
									</td>
									</tr>
									<%
								}
							}
							%>
							</tr>
							<tr>
								<th>목표계단</th>
							  	<td colspan="4"><%=vo.getAim()%></td>
							</tr>
							<tr>
								<th>종료일</th>
							  	<td colspan="4"><%=vo.getEndDate()%></td>
							</tr>
						  	</tbody>
						</table>
						</form>
					</div>
					<div class="alignRightButton">
						<input type="button" OnClick="location.href='give_modify.jsp?giveSeqNo=<%=giveSeqNo%>&pageno=<%=pageno%>';" value="수정" />
				      	<input type="button" OnClick="memberDel('<%=giveSeqNo%>', '<%=pageno%>');" value="삭제" />
				      	<input type="button" OnClick="location.href='give_list.jsp?pageno=<%=pageno%>';" value="목록" />
				    </div>				    
				</div>
			</div>
		<div id="bottomWrap"></div>	
	</div>
	<div style="display: none;"></div>
</body>

