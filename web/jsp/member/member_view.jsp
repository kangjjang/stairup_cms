<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="vo.MemberVO"%>
<%@page import="dao.MemberDao" %>
<%@page import="vo.AffiliationVO"%>
<%@page import="dao.AffiliationDao"%>
<%@page import="vo.DepartVO"%>
<%@page import="dao.DepartDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%
	String menuCate = Constant.MENU_MEMBER;
	String subMenuCate = Constant.SUBMENU_MEMBER;
	
	String ROLE = cookieBox.getValue("ROLE");
	int roles = Integer.parseInt(ROLE);
	
	int no = Integer.parseInt(StringUtil.nchk(request.getParameter("no"), "1")); //요청하여 받은 값을 no변수에 저장 
	int pageno = Integer.parseInt(StringUtil.nchk(request.getParameter("pageno"), "1")); //요청하여 받은 값을 pageno변수에 저장 

	MemberDao dao = new MemberDao();
	MemberVO vo = new MemberVO();
	
	AffiliationDao adao = new AffiliationDao();
	ArrayList<AffiliationVO> list = adao.affiliationList();
	
	DepartDao dao2 = new DepartDao();
	ArrayList<DepartVO> list2 = dao2.departList();
	
	vo = dao.selectMemInfo(no);

	dao.closeConn();
	adao.closeConn();
	dao2.closeConn();
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
		});	
		
		function check() {
			
			if ($( "#memId" ).val().length == 0) {
				alert("아이디를 입력해주세요.");
				$('#memId').focus();
				return;
			}
			
			if ($( "#memName" ).val().length == 0) {
				alert("이름을 입력해주세요.");
				$('#memName').focus();
				return;
			}
			
			if($('#affiliationSeq').val() == 0){
				alert("소속을 선택해 주세요.");
				$('#affiliationSeq').focus();
				return;
			}
			
			if($('#departSeq').val() == 0){
				alert("부서를 선택해 주세요.");
				$('#departSeq').focus();
				return;
			}
			
			if ($( "#memId" ).val() != "<%=vo.getMemId()%>") {
				if($( "#hidCheckId" ).val() != "Y"){
					alert("아이디 중복확인을 해주세요.");
					return;
				}
			}
			
			
			frm.submit();
		}
		
		function getDepartSelectBox(){
			$("#departSeq option").remove();
			
			$.ajax({
					url: "/rest/depart_selectbox_ajax.jsp",
					data: "affiliationSeq=" + $( "#affiliationSeq" ).val(),
					dataType:"html",
					success:function(data){
						$("#departSeq").append(data);
						$("#departSeq").val("<%=vo.getDepartSeq()%>").prop("selected", true);
						if($("#departSeq").val() == null){
							$("#departSeq").val(0);
						}
					},
					error: function(err) {
				    	//alert(err.responseText);
				    }
				}); 
		}
		
		function id_check(){
			if($( "#memId" ).val().length == 0){
				alert("아이디를 입력해 주세요.");
				$( "#memId" ).focus();
				return;
			}
			
			$.ajax({
				url: "/rest/check_duplicate_id_ajax.jsp",
				data: "id=" + $( "#memId" ).val(),
				dataType:"html",
				success:function(data){
					if(data.trim() == 'Y'){
						alert("이미 등록된 아이디입니다.");
					    $("#hidCheckId").val("N");
						$( "#memPhoneNum" ).val("");
					}else{
						alert("가입이 가능한 아이디입니다.");
						$("#hidCheckId").val("Y");
					}
				},
				error: function(err) {
			    	//alert(err.responseText);
			    }
			}); 
		}
		
		//기업 셀렉트 박스 불러오기
		$(function() {
			getDepartSelectBox();
			
		})
		
		function delMem(memSeqNo,pageNo){
			if(confirm("그 동안 쌓은 모든 데이터가 날아갑니다.\n삭제하시겠습니까 ?") == true){
				location.href="member_remove.jsp?memSeqNo="+memSeqNo+"&pageNo="+pageNo;
			}else{
				return;
			}
			 
		}
		
		function updatePw(memSeqNo,pageNo){
			var returnValue = prompt("변경 할 비밀번호를 입력하세요", "");
			if(returnValue.length > 0){
				if(confirm("비밀번호를 변경하시겠습니까?") == true){
					location.href="member_update_pw.jsp?memSeqNo="+memSeqNo+"&pageNo="+pageNo+"&memPw="+returnValue;
				}
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
					<h2 class="contentTitle">회원 정보 수정</h2>
					<form name="frm" action="member_modifyProc.jsp" method="post" >
					<input type="hidden" name="no" value="<%=no%>">
					<input type="hidden" name="pageno" value="<%=pageno%>">
					<input type="hidden" id="hidCheckId" name="hidCheckId">
					<div class="contentForm">
						<table class="detailInfoOneItem" style="margin-top: -20px;">
							<thead><tr><td colspan="2"  style="text-align:right;border-top:none;">* 필수입력</td></tr></thead>
							<tbody>
							<tr class="detailInfoOneItemFirst"> 
							  <th style="text-align: center;">ID</th>
							  <td><input type="text" name="memId" id="memId" value="<%=vo.getMemId() %>" size="50" />&nbsp;<input type="button" value="중복확인" onclick="id_check();"></td>
							</tr>
							<tr> 
							  <th style="text-align: center;">이름</th>
							  <td><input type="text" name="memName" id="memName" value="<%=vo.getMemName() %>" size="30"  /></td>
							</tr>
							<tr> 
							  <th style="text-align: center;">소속</th>
							  <td>
							  	<select name="affiliationSeq" id="affiliationSeq" onchange="getDepartSelectBox()">
							  		<option value="0">소속 선택</option>
							  		<%
							  		if(list.size() > 0){
							  			for(int i=0; i<list.size(); i++){ 
							  				AffiliationVO avo = list.get(i);
							  		%>
							  		<option value="<%=avo.getSeqNo()%>" <%if(avo.getSeqNo() == vo.getAffiliationSeq()){%>selected<%} %>><%=avo.getAffiliationName() %></option>
							  		<%	
							  			}
							  		} 
							  		%>
								</select>
							  </td>
							</tr>
							<tr> 
							  <th style="text-align: center;">부서</th>
							  <td>
							  	<select name="departSeq" id="departSeq">
							  		<option value="0">부서 선택</option>
							  	</select>
							  </td>
							</tr>
							</tbody>
						</table>
					</div>
					<div class="detailInfoButton">
				      	<input type="button" value="수정" onclick="check();">
				      	<input type="button" value="삭제" onclick="javascript:delMem(<%=no%>,<%=pageno%>)">
				      	<input type="button" value="비밀번호 변경" onclick="javascript:updatePw(<%=no%>,<%=pageno%>)">
				      	<input type="button" value="취소" onclick="javascript:history.back();"/>
                        <%if(cookieBox.getValue("ID").equals("admin")){%>
                            <input type="button" value="계단로그보기" onclick="window.open('member_beacon_log_list.jsp?memSeq=<%=no%>', 'pop01', 'top=10, left=10, width=1200, height=600, status=no, menubar=no, toolbar=no, resizable=yes, scrollbars=yes');"/>
                        <%}%>
				    </div>
					</form>
				</div>
			</div>
			<div id="bottomWrap"></div>
		
		</div>
		<div style="display: none;"></div>
	</body>
</html>
