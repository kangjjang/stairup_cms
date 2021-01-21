<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="dao.NoticeDao" %>
<%@page import="vo.NoticeVO" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>공지사항</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
<link rel="stylesheet" type="text/css" href="../css/mobile.css">
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
	window.addEventListener("load", function() {
		setTimeout(loaded, 100);

	}, false);

	function loaded() {
		window.scrollTo(0, 1);
	}
</script>

<script>
	function delform(n) {
		form1.mode.value = "d";
		form1.uid.value = n;
		form1.submit();
	}
</script>

</head>
<%
int noticeSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("noticeSeqNo"),"0"));
NoticeDao dao = new NoticeDao();
NoticeVO vo = dao.noticeView(noticeSeqNo);
dao.closeConn();
%>
<body id="webview">

	<form name="form1" method="post" action="/m/kor/notice.html">
		<input type="hidden" name="mode" value=""> <input
			type="hidden" name="uid" value="">
	</form>
	<!-- Contents -->
	<section id="contents">

		<!-- Notice List -->
		<div class="noticeSection">

			<div class="notice">
				<ul class="noticeBody">
				<li class="article">
						<p class="q">
							<a href="#" data-id="27"><%=vo.getNotiTitle() %></a>

						</p>
						<p class="a">
							
							<%if(!vo.getNoticePic().equals("")) {%>
							<%-- <img src="http://jerry393986.cafe24.com/upload/notice/<%=vo.getNoticePic() %>" width="320" height="320"><br><br><br> --%>
							<img src="http://ec2-52-78-198-250.ap-northeast-2.compute.amazonaws.com:8080/upload/notice/<%=vo.getNoticePic() %>" width="100%"><br><br><br>
							<%} %>
							<%=vo.getNotiContent() %>
						</p>
					</li>
					
				</ul>
			</div>
			<!-- <script type="text/javascript">
				jQuery(function($) {
					// Frequently Asked Question
					var article = $('.notice>.noticeBody>.article');
					article.addClass('hide');
					article.find('.a').hide();
					//article.eq(0).removeClass('hide');
					//article.eq(0).find('.a').show();
					$('.notice>.noticeBody>.article>.q>a').click(function() {
						var myArticle = $(this).parents('.article:first');
						if (myArticle.hasClass('hide')) {
							article.addClass('hide').removeClass('show');
							article.find('.a').slideUp(100);
							myArticle.removeClass('hide').addClass('show');
							myArticle.find('.a').slideDown(100);
						} else {
							myArticle.removeClass('show').addClass('hide');
							myArticle.find('.a').slideUp(100);
						}
						return false;
					});
					$('.notice>.noticeHeader>.showAll')
							.click(
									function() {
										var hidden = $('.notice>.noticeBody>.article.hide').length;
										if (hidden > 0) {
											article.removeClass('hide')
													.addClass('show');
											article.find('.a').slideDown(100);
										} else {
											article.removeClass('show')
													.addClass('hide');
											article.find('.a').slideUp(100);
										}
									});

					var uid = parseInt('');
					$("a[data-id=" + uid + "]").click();
				});
			</script> -->
			</div>
			<!-- Notice List //-->
	</section>
	<!-- Contents //-->


	<!-- Script -->
	<script type="text/javascript">
		//Msg Box
		$('.close').click(function() {
			$(this).parent('.result-msg').hide('fast');
		});
	</script>
	<!-- Script //-->

</body>
</html>