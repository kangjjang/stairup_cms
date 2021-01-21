<%@ page contentType="text/html; charset=utf-8" %>
<%
    if(role == null){
        response.sendRedirect("/jsp/login.jsp");
    }else{
%>
<div id="headerWrap">
    <div class="topLinkArea">
        <div class="topLinkImgOuter">
            <%if(!role.equals("-1")){ %>
            <a href="../logout.jsp">로그아웃</a>
            <%}else{ %>
            <a href="../login.jsp">로그인</a>
            <%} %>
        </div>
    </div>
    <div class="headerMenuWrap">
        <div class="websiteName">
            <a class="a_white">StairUP CMS</a>
        </div>
        <div class="topMenuOuter">
            <div class="topMenuInner">
                <div class="topMenuContent">
                    <ul class="topMenu">
                        <li id="topMenuNotice"><a href="../notice/notice_list.jsp">공지사항/이벤트</a></li>
                        <li id="topMenuGeneral"><a href="../give/give_list.jsp">컨텐츠 관리</a></li>
                        <li id="topMenuMember"><a href="../member/member_list.jsp">멤버 관리</a></li>
                        <li id="topMenuEvent"><a href="../world/country_list.jsp">스테이지 관리</a></li>
                        <li id="topMenuOrder"><a href="../beacon/beacon_list.jsp">비콘 관리</a></li>
                        <!-- <li id="topMenuOrder"><a href="../league/league_list.jsp">팀별리그</a></li> -->
                        <li id="topMenuSafe"><a href="../depart/depart_list.jsp">부서 관리</a></li>
                        <%if(role.equals("0")){%>
                        <li id="topMenuAdmin"><a href="../admin/admin_list.jsp">관리자 관리</a></li>
                        <%}%>

                    </ul>
                </div>
            </div>
        </div>
        <div class="topMenuToday"></div>
    </div>
</div>
<%
    }
%>
