<%@ page language="java" contentType="application/vnd.ms-excel;charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="vo.StatisticsVO"%>
<%@page import="dao.StatisticsDao"%>
<%@page import="util.StringUtil"%>

<%
request.setCharacterEncoding("UTF-8");

response.setHeader("Content-Disposition","attachment;filename=MEMBER_STATIS.xls");
response.setHeader("Content-Description", "JSP Generated Data");

int date = Integer.parseInt(StringUtil.nchk(request.getParameter("date"),"0"));    // 선택한 날짜 타입
int stair = Integer.parseInt(StringUtil.nchk(request.getParameter("stair"),"1"));  //
String startDate = StringUtil.nchk(request.getParameter("startDate"),"");
String endDate = StringUtil.nchk(request.getParameter("endDate"),"");
int roles = Integer.parseInt(StringUtil.nchk(request.getParameter("roles"),"0"));  //

StatisticsDao sdao = new StatisticsDao();
ArrayList<StatisticsVO> alist = sdao.selectStatisticsExcelNew(startDate, endDate, date, stair, roles);

sdao.closeConn();

%>
<!DOCTYPE html>
<html lang="ko">
<head>
<title></title>
</head>
<body>
	<table border="1">
		<thead>
			<tr>
				<th style="width: 10% ;text-align: center;">성명</th>
				<th style="width: 15% ;text-align: center;">전화번호</th>
				<th style="width: 17% ;text-align: center;">소속명</th>
				<th style="width: 17% ;text-align: center;">부서명</th>
				<th style="width: 10% ;text-align: center;">계단 수</th>	
			</tr>
		</thead>
		<tbody>
			<% 
			if (alist.size() > 0) {
				for (int i=0; i<alist.size(); i++) {
					StatisticsVO vo = alist.get(i);
					
					%>
				<tr>
					<td><%=StringUtil.NVL(vo.getMemName()) %></td>
					<td><%=vo.getMemNumber() %></td>
					<td><%=StringUtil.NVL(vo.getAffiliationName()) %></td>
					<td><%=StringUtil.NVL(vo.getDepartName()) %></td>
					<td><%=vo.getTodaytotal() %></td>

				</tr>
				
				<%
				}
			} else {
				out.println("<tr><td colspan=5>조회 결과가 없습니다.</td></tr>");
			}
			%>
		</tbody>
	</table>
</body>
</html>
