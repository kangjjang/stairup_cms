<%@page import="vo.AdminVO"%>
<%@page import="dao.AdminDao" %>
<%@page import="vo.AffiliationVO"%>
<%@page import="dao.AffiliationDao"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="net.fckeditor.*"%>
<%@ taglib uri="http://java.fckeditor.net" prefix="FCK" %>
<%
	String menuCate = Constant.MENU_ADMIN;
	String subMenuCate = Constant.SUBMENU_ADMIN;
	
	String ROLE = cookieBox.getValue("ROLE");
	int roles = Integer.parseInt(ROLE);
	////System.out.println("roles = "+roles);
	
	int no = Integer.parseInt(StringUtil.nchk(request.getParameter("no"), "1")); //요청하여 받은 값을 no변수에 저장 
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1")); //요청하여 받은 값을 pageno변수에 저장 

	AdminDao dao = new AdminDao();
	AdminVO vo = new AdminVO();
	
	AffiliationDao adao = new AffiliationDao();
	ArrayList<AffiliationVO> list = adao.affiliationList();
	
	
	vo = dao.selectMember(no);

	dao.closeConn();
	adao.closeConn();
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
			$('button.icon-trash').button({
				icons: {
	                primary: "ui-icon-trash"
	            },
	            text: false
			});
			$( "#beacon_wrt_date" ).datepicker({
				dateFormat: "yymmdd",
				defaultDate: "+1w",
				changeMonth: true,
				numberOfMonths: 3,
			});
			
			
		});	
		
		function check() {
			if (document.getElementById("title").value.length == 0) {
				alert("제목을 입력해주세요.");
				document.getElementById("title").focus();
				return false;
			}
			
			if($('#affiliation').val() == "notting"){
				alert("소속을 선택해 주세요.");
				return false;
			}else{
				document.frm.submit();
			}
			frm.submit();
		}
		
		function OriFileDel(oriImg, hidImg){
			$('#'+oriImg).hide('drop', { percent: 100 }, 500);
			$('#'+hidImg).val('');
		}
		
		function adminDelete(no){
			if(confirm("해당 관리자를 삭제하시겠습니까 ?") == true){
				location.href = "admin_remove.jsp?no="+no;
			}
		}
		
		</script>
		<script type="text/javascript">
			function FCKeditor_OnComplete(editorInstance) {
				window.status = editorInstance.Description;
			}
		</script>			
	</head>
	<body>
		<div id="wrap">
			<%@ include file="../include/inc_top_menu.jsp" %>		
			<div id="contentWrap">  
				<%@ include file="../include/inc_left_menu.jsp" %>	  
				<div class="contentArea">
					<h2 class="contentTitle">관리자 정보 수정</h2>
					<form name="frm" action="admin_modifyProc.jsp" method="post" >
					<input type="hidden" name="no" value="<%=no%>">
					<input type="hidden" name="pageno" value="<%=pageno%>">
					<div class="contentForm">
						<table class="detailInfoOneItem" style="margin-top: -20px;">
							<thead><tr><td colspan="2"  style="text-align:right;border-top:none;">* 필수입력</td></tr></thead>
							<tbody>
							<tr class="detailInfoOneItemFirst"> 
							  <th style="text-align: center;">ID</th>
							  <td><input type="text" name="adminId" value="<%=vo.getMemberId() %>" size="50" class="box formfield" readonly /></td>
							</tr>
							<tr> 
							  <th style="text-align: center;">소속</th>
							  <td>
							  	<%if(vo.getMemberRole().equals("0")){%>
							  		<input type="text" value="전체 관리자"size="30" readonly />
							  	<%}else{%>
							  		<select name="affiliationSeq" id="affiliation">
							  		<option value="notting">소속 선택</option>
							  		<%
							  		if(list.size() > 0){
							  			for(int i=0; i<list.size(); i++){ 
							  				AffiliationVO avo = list.get(i);
							  		%>
							  		<option value="<%=avo.getSeqNo()%>" <%if(avo.getAffiliationName().equals(vo.getAdminAffiliation())){%>selected<%} %>><%=avo.getAffiliationName() %></option>
							  		<%	
							  			}
							  		} 
							  		%>
								</select>
							  	<%} %>
							  	
							  </td>
							</tr>
							<tr> 
							  <th style="text-align: center;">이름</th>
							  <td><input type="text" name="adminName" value="<%=vo.getMemberName() %>" size="30"  /></td>
							</tr>
							<tr> 
							  <th style="text-align: center;">전화번호</th>
							  <td><input type="text" id="title" name="adminPhone" value="<%=vo.getMemberPhone() %>" size="30" /></td>
							</tr>
							<tr>
							</tbody>
						</table>
					</div>
					<div class="detailInfoButton">
				      	<input type="button" value="수정" onclick="check()">
				      	<input type="button" value="삭제" onclick="javascript:adminDelete('<%=no%>')">
				      	<input type="button" OnClick="location.href='admin_list.jsp?pageno=<%=pageno%>';" value="취소" />
				    </div>
					</form>
				</div>
			</div>
			<div id="bottomWrap"></div>
		
		</div>
		<div style="display: none;"></div>
	</body>
</html>