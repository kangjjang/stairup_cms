<%@page import="vo.AffiliationVO"%>
<%@page import="dao.AffiliationDao"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%
	String menuCate = Constant.MENU_DEPART; //Constant.MENU_GENERAL를 menuCate변수에 저장
	String subMenuCate = Constant.SUBMENU_AFFILIATION; //Constant.SUBMENU_BOARD를 subMenuCate변수에 저장
	
	int affiliationSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("affiliationSeqNo"), "1")); //요청하여 받은 값을 no변수에 저장 
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1")); //요청하여 받은 값을 pageno변수에 저장 
	
	AffiliationDao dao = new AffiliationDao();
	AffiliationVO vo = new AffiliationVO();
	vo = dao.affiliationSelectView(affiliationSeqNo); //vo에 저장 
	dao.closeConn();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<%@ include file="../include/inc_top.jsp" %>
		<script language=javascript>
		
		function departDelete(affiliationSeq){
			if(confirm("해당 소속을 삭제 하시겠습니까 ?\n (소속을 삭제하시기 전에 해당 소속의 인원을 확인하세요.)") == true){
				location.href = "affiliation_delete.jsp?affiliationSeq="+affiliationSeq;
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
					<h2 class="contentTitle">소속 수정</h2>
					<form name="frm" action="affiliation_modifyProc.jsp" method="post" enctype="multipart/form-data">
					<input type="hidden" name="affiliationSeqNo" value="<%=vo.getSeqNo()%>">
					<input type="hidden" name="pageno" value="<%=pageno%>">
					<div class="contentForm">
						<table class="detailInfoOneItem" style="margin-top: -20px;">
							<thead><tr><td colspan="2"  style="text-align:right;border-top:none;">* 필수입력</td></tr></thead>
							<tbody>
							<tr class="detailInfoOneItemFirst"> 
							  <th>부서명</th>
							  <td><input type="text" name="affiliationName" value="<%=vo.getAffiliationName() %>" size="50" class="box formfield" /></td>
							  
							</tr>
							<tr>
							<th>이미지</th>
							<td>
								<%if(vo.getAffiliationPic()!= null){%>
									<img src="../../upload/affiliation/<%=vo.getAffiliationPic()%>" width="150" height="100" />
								<%}else{%>이미지 없음<%} %>
								<input type="file" name="listImg" size="50" class="box" />
							</td>
							</tr>
							</tbody>
						</table>
					</div>
					<div class="detailInfoButton">
				      	<input type="submit" value="수정" />
				      	<input type="button" onclick="javascript:departDelete('<%=affiliationSeqNo%>')" value="삭제"/>
				      	<input type="button" OnClick="location.href='affiliation_list.jsp?pageno=<%=pageno%>';" value="취소" />
				    </div>
					</form>
				</div>
			</div>
			<div id="bottomWrap"></div>
		
		</div>
		<div style="display: none;"></div>
	</body>
</html>