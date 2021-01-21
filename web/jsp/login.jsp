<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="./include/inc_top.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>StairUP CMS</title>
<link href="../css/login.css" rel="stylesheet" type="text/css" />
<script language="JavaScript"> 
<!--
	//$.preloadImages("../img/bt_login.gif"); svn 연동
 
	$(document).ready(function() {
		$("#id").focus();
	});
 
	function checkForm() {
		if (Validator.isEmpty("#id", "아이디를 입력해주세요.")) { return; }
		if (Validator.isEmpty("#password", "비밀번호를 입력해주세요.")) {	return; }
 		
		loginForm.submit();
	}
	
	function hitEnterKey(e){
	  if(e.keyCode == 13){
		  checkForm();
	  }else{
		  e.keyCode == 0;
	 	  return;
	  }
	} 
//-->
</script>
 
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<div id="contentWrap">
	<div id="content">
		<h2 class="loginTitle"><img src="../img/t_a_login.gif" alt="Audi Administrator"/></h2>
		<div class="formBody">
			<form action="login_ok.jsp" id="loginForm" method="post">
			<input type="hidden" name="returnUrl" value="L2FkbWluL21haW4vbWFpbi5kbw"/>
				<fieldset>
				<legend class="hiddenText">로그인 폼</legend>
				<div class="formGuide"><img src="../img/t_4.gif"/></div>
				<div class="formInput">
					<table>
						<tr>
							<td>
								<ul>
									<li>
										<label for="id"><img src="../img/id.gif"/></label>
										<input type="text" id="id" name="id" class="box" tabindex="1"/>
									</li>
									<li>
										<label for="password"><img src="../img/pw.gif"/></label>
										<input type="password" id="password" name="password" class="box" tabindex="2" onKeypress="hitEnterKey(event)"/>
									</li>
								</ul>
							</td>
							<td>
								<button type="img" src="../img/bt_login.gif" id="loginButton" tabindex="3" onclick="checkForm();"/>
							</td>
						</tr>
					</table>
				</div>
				</fieldset>
			</form>
		</div>
	</div>
</div>
</body>
</html>
