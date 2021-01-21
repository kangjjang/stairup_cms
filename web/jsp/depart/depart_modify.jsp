<%@page import="vo.DepartVO"%>
<%@page import="dao.DepartDao"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="vo.AffiliationVO"%>
<%@page import="dao.AffiliationDao"%>
<%
	String menuCate = Constant.MENU_DEPART; //Constant.MENU_GENERAL를 menuCate변수에 저장
	String subMenuCate = Constant.SUBMENU_DEPART; //Constant.SUBMENU_BOARD를 subMenuCate변수에 저장
	
	int roles = Integer.parseInt(role);
	
	int departSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("departSeqNo"), "1")); //요청하여 받은 값을 no변수에 저장 
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1")); //요청하여 받은 값을 pageno변수에 저장 
	
	AffiliationDao adao = new AffiliationDao();
	ArrayList<AffiliationVO> list = adao.affiliationList();
	
	DepartDao dao = new DepartDao();
	DepartVO vo = new DepartVO();
	
	
	vo = dao.departSelectView(departSeqNo); //vo에 저장 
	dao.closeConn();
	adao.closeConn();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<%@ include file="../include/inc_top.jsp" %>
		<script language=javascript>
		
		function departDelete(departSeq){
			if(confirm("해당 부서를 삭제 하시겠습니까 ?\n (부서를 삭제하시기 전에 해당 부서의 인원을 확인하세요.)") == true){
				location.href = "depart_delete.jsp?departSeq="+departSeq;
			}
		}
		
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
			$( "#endDate" ).datepicker({
				dateFormat: "yymmdd",
				defaultDate: "+1w",
				changeMonth: true,
				numberOfMonths: 3,
			});
			
			$('form[name="frm"]').submit(function(){
				var select = $('#affiliation').val();
				if(select == 'notting'){
					alert("소속을 선택해 주세요.");
				}
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
		
		function OriFileDel(oriImg, hidImg){
			$('#'+oriImg).hide('drop', { percent: 100 }, 500);
			$('#'+hidImg).val('');
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
					<h2 class="contentTitle">부서 수정</h2>
					<form name="frm" action="depart_modifyProc.jsp" method="post" enctype="multipart/form-data">
					<input type="hidden" name="departSeqNo" value="<%=vo.getHomeSeqNo()%>">
					<input type="hidden" name="pageno" value="<%=pageno%>">
					<div class="contentForm">
						<table class="detailInfoOneItem" style="margin-top: -20px;">
							<thead><tr><td colspan="2"  style="text-align:right;border-top:none;">* 필수입력</td></tr></thead>
							<tbody>
							<%if(roles == 0){%>
							<tr> 
								<th>소속</th>
							  	<td>
								  	<select name="affiliationSeq" id="affiliation">
								  		<option value="notting">소속 선택</option>
								  		<%
								  		if(list.size() > 0){
								  			for(int i=0; i<list.size(); i++){ 
								  				AffiliationVO avo = list.get(i);	
								  		%>
								  		<option value="<%=avo.getSeqNo()%>" <%if(avo.getAffiliationName().equals(vo.getAffiliationName())){%>selected<%} %>><%=avo.getAffiliationName() %></option>
								  		<%	
								  			}
								  		} 
								  		%>
								  	</select>
							    </td>
							</tr>
							<%} %>
							<tr> 
							  <th>부서명</th>
							  <td><input type="text" name="departName" value="<%=vo.getHomeName() %>" size="50" class="box formfield" /></td>
							  
							</tr>
							<tr>
							<th>이미지</th>
							<td>
								<%if(vo.getHomePic()!= null){%>
									<img src="../../upload/depart/<%=vo.getHomePic()%>" width="150" height="100" />
								<%}else{%>이미지 없음<%} %>
								<input type="file" name="listImg" size="50" class="box" />
							</td>
							</tr>
							<tr> 
							  <th>인원수</th>
							  <td><input type="text" name="departPeople" value="<%=vo.getDepartPeople() %>" size="50" class="box formfield" /></td>
							</tr>
							
							</tbody>
						</table>
					</div>
					<div class="detailInfoButton">
				      	<input type="submit" value="수정" />
				      	<input type="button" onclick="javascript:departDelete('<%=departSeqNo%>')" value="삭제"/>
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