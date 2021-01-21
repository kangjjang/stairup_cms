<%@ page contentType="text/html; charset=utf-8" %>
<%@page import="util.Constant"%>

<div class="contentMenu">					
	<%
	if(menuCate.equals(Constant.MENU_NOTICE)){ %> 
  	<div class="submenuTitle">
		<h2>공지사항/이벤트</h2>
  	</div>
  	<div class="submenu">
		<div class="menuList">
			<ul>
				<li><a href="../notice/notice_list.jsp">공지 사항</a></li>
				<li><a href="../event/event_list.jsp">이벤트</a></li>
				<!-- <li><a href="../report/report_list.jsp">불편신고</a></li> -->
			</ul>
		</div>
		<div class="bottom"></div>
  	</div>	
  	<%}else if(menuCate.equals(Constant.MENU_CONTENT)){ %>	
  	<div class="submenuTitle">
		<h2>컨텐츠 관리</h2>
  	</div>
  	<div class="submenu">
		<div class="menuList">
			<ul>
			<!-- rnflrnfl_ehd 2014-12-23 help, share 추가 -->
			<!-- rnflrnfl_ehd 2015-01-08 play 추가 -->
			
				<li><a href="../give/give_list.jsp">힘내라 기부</a></li>
				<li><a href="../greeting/greeting_list.jsp">시장 인사말</a></li>					
			</ul>
		</div>
		<div class="bottom"></div>
  	</div>		
  	<%}else if(menuCate.equals(Constant.MENU_MEMBER)){ %>	
  	<div class="submenuTitle">
		<h2>회원 관리</h2>
  	</div>
  	<div class="submenu">
		<div class="menuList">
			<ul>
				<li><a href="../member/member_list.jsp">회원 정보</a></li>
				<li><a href="../member/member_statis.jsp">회원 통계</a></li>
			</ul>
		</div>
		<div class="bottom"></div>
  	</div>		
  	<%}else if(menuCate.equals(Constant.MENU_BEACON)){ %>	
  	<div class="submenuTitle">
		<h2>비콘 이벤트 관리</h2>
  	</div>
  	<div class="submenu">
		<div class="menuList">
			<ul>
				<li><a href="../beacon/beacon_list.jsp">팝업 이벤트 관리</a></li>
                <li><a href="../beacon/stamp_beacon_list.jsp">스탬프 이벤트 관리</a></li>
			    <li><a href="../beacon/beacon_sound_list.jsp">사운드 관리</a></li>
			</ul>
		</div>
		<div class="bottom"></div>
  	</div>		
  	<%}else if(menuCate.equals(Constant.MENU_STAY)){ %>	
  	<div class="submenuTitle">
		<h2>스테이지 관리</h2>
  	</div>
  	<div class="submenu">
		<div class="menuList">
			<ul>
				<li><a href="../world/country_list.jsp">국가 관리</a></li>
				<li><a href="../world/city_list.jsp">도시 관리</a></li>
				<li><a href="../world/city_coment_list.jsp">도시 인사말</a></li>
			</ul>
		</div>
		<div class="bottom"></div>
  	</div>		
  	<%}else if(menuCate.equals(Constant.MENU_TEAM)){ %>	
  	<div class="submenuTitle">
		<h2>팀별리그 관리</h2>
  	</div>
  	<div class="submenu">
		<div class="menuList">
			<ul>
				<li><a href="../league/league_list.jsp">팀별리그</a></li>
				<li><a href="../league/league_last_week.jsp">지난경기</a></li>
				<!-- <li><a href="../order_off/order_off_list.jsp">팀별랭킹</a></li> -->
			</ul>
		</div>
		<div class="bottom"></div>
  	</div>		
  	<%}else if(menuCate.equals(Constant.MENU_DEPART)){ %>	
  	<div class="submenuTitle">
		<h2>부서 관리</h2>
  	</div>
  	<div class="submenu">
		<div class="menuList">
			<ul>
				<li><a href="../depart/depart_list.jsp">부서 관리</a></li>
				<%if(role.equals("0")){%>
					<li><a href="../depart/affiliation_list.jsp">소속 관리</a></li>
				<%} %>
			</ul>
		</div>
		<div class="bottom"></div>
  	</div>		
  	<%}else if(menuCate.equals(Constant.MENU_ADMIN)){ %>	
  	<div class="submenuTitle">
		<h2>관리자 관리</h2>
  	</div>
  	<div class="submenu">
		<div class="menuList">
			<ul>
				<li><a href="../admin/admin_list.jsp">관리자 관리</a></li>
			</ul>
		</div>
		<div class="bottom"></div>
  	</div>		
  	<%} %>		
</div>				  	  
