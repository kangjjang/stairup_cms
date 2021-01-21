<%@page import="vo.NoticeVO"%>
<%@page import="dao.NoticeDao"%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="net.fckeditor.*"%>
<%@page import="vo.AffiliationVO"%>
<%@page import="dao.AffiliationDao"%>
<%@ taglib uri="http://java.fckeditor.net" prefix="FCK" %>
<%
	String menuCate = Constant.MENU_NOTICE; //Constant.MENU_GENERAL를 menuCate변수에 저장
	String subMenuCate = Constant.SUBMENU_NOTICE; //Constant.SUBMENU_NEWS를 subMenuCate변수에 저장
	
	String ROLE = cookieBox.getValue("ROLE");
	int roles = Integer.parseInt(ROLE);
	
	int no = Integer.parseInt(StringUtil.nchk(request.getParameter("no"), "1")); //요청하여 받은 값을 no변수에 저장 
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1")); //요청하여 받은 값을 pageno변수에 저장 
	
	AffiliationDao adao = new AffiliationDao();
	ArrayList<AffiliationVO> list = adao.affiliationList();
	
	NoticeDao dao = new NoticeDao();
	NoticeVO vo = new NoticeVO();
	vo = dao.noticeView(no); //vo에 저장 
	dao.closeConn();
	adao.closeConn();
%>
<%
	/* FCKeditor fckEditor = new FCKeditor(request, "notiDesc", "700", "300", "Audi", vo.getNotiContent(), addPath + "/fckeditor"); */
	FCKeditor fckEditor = new FCKeditor(request, "notiContent", "700", "300", "Audi", vo.getNotiContent(), addPath + "/fckeditor");
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
			$( "#notice_wrt_date" ).datepicker({
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
					<h2 class="contentTitle">공지사항 수정</h2>
					<form name="frm" action="notice_modifyProc.jsp" method="post" enctype="multipart/form-data">
					<input type="hidden" name="no" value="<%=no%>">
					<input type="hidden" name="pageno" value="<%=pageno%>">
					<div class="contentForm">
						<table class="detailInfoOneItem" style="margin-top: -20px;">
							<thead><tr><td colspan="2"  style="text-align:right;border-top:none;">* 필수입력</td></tr></thead>
							<tbody>
							<tr class="detailInfoOneItemFirst"> 
							  <th>공지사항 제목</th>
							  <td><input type="text" name="notice_title" value="<%=vo.getNotiTitle() %>" size="50" class="box formfield" /></td>
							</tr>
							<%if(roles == 0){%>
							<tr> 
								<th>소속</th>
							  	<td>
								  	<select name="affiliationSeq" id="affiliation">
								  		<option value="notting">소속 선택</option>
								  		<option value="0">전체</option>
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
							<th>이미지</th>
							<td>
								<%if(!vo.getNoticePic().equals("") ){%>
									<img src="/upload/notice/<%=vo.getNoticePic() %>" width="150" height="100" />
								<%}else{%> 이미지 없음<%} %>
								<input type="file" name="listImg" size="50" class="box" /></td>
							</tr>
							<tr> 
							  <th>공지사항 내용</th>
							  <td><%=fckEditor %></td>
							</tr>
							<tr> 
							  <th>작성일</th>
							  <td><input type="text" id ="notice_wrt_date" name="notice_wrt_date" value="<%=vo.getCrtDate()%>"class="box formfield" /></td>
							</tr>
							</tbody>
						</table>
					</div>
					<div class="detailInfoButton">
				      	<input type="submit" value="수정" />
				      	<input type="button" OnClick="location.href='notice_list.jsp?pageno=<%=pageno%>';" value="취소" />
				    </div>
					</form>
				</div>
			</div>
			<div id="bottomWrap"></div>
		
		</div>
		<div style="display: none;"></div>
	</body>
</html>