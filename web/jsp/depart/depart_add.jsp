<%@page contentType="text/html; charset=utf-8" %>
<%@include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>  
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="vo.AffiliationVO"%>
<%@page import="dao.AffiliationDao"%>
<%

	int roles = Integer.parseInt(role);

	String menuCate = Constant.MENU_DEPART; //Constant.MENU_GENERAL를 menuCate변수에 저장
	String subMenuCate = Constant.SUBMENU_DEPART; //Constant.SUBMENU_BOARD를 subMenuCate변수에 저장
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1")); //요청하여 받은 값을 page변수에 저장 
	
	AffiliationDao dao = new AffiliationDao();
	ArrayList<AffiliationVO> list = dao.affiliationList();
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
			
			$('form[name="frm"]').submit(function(){
				var select = $('#affiliation').val();
				if(select == 'notting'){
					alert("소속을 선택해 주세요.");
				}
 				$('.notice').remove();
				$('.formfield').each(function() {
					if(!$(this).val()) {
						$(this).addClass('highlight');
						$('<span />', {
							text: '필수 입력사항입니다.',
							'class': 'notice',
							click: function() {
								alert('필수 입력사항입니다.');
							}
						}).appendTo($(this).parent());
					} else {
						$(this).removeClass('highlight');
					}
				});
				if($('.notice').length)
					return false; 
			});			
		});
		
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
					<h2 class="contentTitle">부서 등록</h2>
					<form name="frm" action="depart_addProc.jsp" method="post" enctype="multipart/form-data">
					<div class="contentForm">
						<table class="detailInfoOneItem" style="margin-top: -20px;">
							<thead><tr><td colspan="2"  style="text-align:right;border-top:none;">* 필수입력</td></tr></thead>
							<tbody>
								<%if(roles == 0){%>
								<tr class="detailInfoOneItemFirst">
									<th>소속</th>
								    <td>
									  	<select name="affiliationSeq" id="affiliation">
									  		<option value="notting">소속 선택</option>
									  		<%
									  		if(list.size() > 0){
									  			for(int i=0; i<list.size(); i++){ 
									  				AffiliationVO vo = list.get(i);	
									  		%>
									  		<option value="<%=vo.getSeqNo()%>"><%=vo.getAffiliationName() %></option>
									  		<%	
									  			}
									  		} 
									  		%>
									  	</select>
								    </td>
								</tr>	
								<%} %>
								<tr class="detailInfoOneItemFirst">
									<th>부서명 *</th>
								  <td><input type="text" name="departName" size="50" class="box formfield" /></td>
								</tr>
								<tr > 
								   <th>이미지</th>
								  <td><input type="file" name="listImg" size="50" class="box" /></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="detailInfoButton">
				      	<input type="submit" value="등록" />
				      	<input type="button" OnClick="location.href='depart_list.jsp?pageno=<%=pageno%>';" value="취소" />
				    </div>
					</form>
				</div>
			</div>
			<div id="bottomWrap"></div>
		
		</div>
		<div style="display: none;"></div>
	</body>
</html>