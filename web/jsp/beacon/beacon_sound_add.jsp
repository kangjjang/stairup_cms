<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="net.fckeditor.*"%>
<%@ taglib uri="http://java.fckeditor.net" prefix="FCK" %>
<%
	String menuCate = Constant.MENU_BEACON;
	String subMenuCate = Constant.SUBMENU_BEACON;
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1"));
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
<script type="text/javascript">

function insert(mainSeq) {
	if(confirm("등록 하시겠습니까 ??")==true){
		if (Validator.isEmpty("#soundName", "팝업 사운드 제목을 입력해주세요.")) { document.getElementById("soundName").focus(); return; }
		if (Validator.isEmpty("#soundLocation", "팝업 사운드 위치를 입력해주세요.")) { document.getElementById("soundLocation").focus(); return; }
		if (Validator.isEmpty("#beaconSound", "파일을 선택해주세요.")) { document.getElementById("beaconSound").focus(); return; }
		document.frm.submit();
	}
}
</script>
		<title></title>
		<%@ include file="../include/inc_top.jsp" %>
	</head>
	<body>
		<div id="wrap">
			<%@ include file="../include/inc_top_menu.jsp" %>		
			<div id="contentWrap">  
				<%@ include file="../include/inc_left_menu.jsp" %>	  
				<div class="contentArea">
					<h2 class="contentTitle">팝업 사운드 등록</h2>
					<form name="frm" action="beacon_sound_addProc.jsp" method="post" enctype="multipart/form-data">
					<div class="contentForm">
						<table class="detailInfoOneItem" style="margin-top: -20px;">
							<thead><tr><td colspan="2"  style="text-align:right;border-top:none;">* 필수입력</td></tr></thead>
							<tbody>
							<tr class="detailInfoOneItemFirst"> 
							  <th>제목*</th>
							  <td><input type="text" name="soundName" id="soundName" size="50" class="box formfield" /></td>
							</tr>
							<tr> 
							  <th>위치*</th>
							  <td><input type="text" name="soundLocation" id="soundLocation" size="50" class="box formfield" /></td>
							</tr>
							<tr> 
							  <th>파일*</th>
							  <td><input type="file" name="listImg" id="beaconSound"/></td>
							</tr>
							</tbody>
						</table>
					</div>
					<div class="detailInfoButton">
				      	<input type="button" value="등록" onclick="insert()"/>
				      	<input type="button" OnClick="location.href='greeting_list.jsp?pageno=<%=pageno%>';" value="취소" />
				    </div>
					</form>
				</div>
			</div>
			<div id="bottomWrap"></div>
		
		</div>
		<div style="display: none;"></div>
	</body>
</html>