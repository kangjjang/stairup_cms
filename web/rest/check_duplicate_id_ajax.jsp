<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.MemberDao" %>
<%
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

	request.setCharacterEncoding("UTF-8");

	String id = URLDecoder.decode(StringUtil.nchk(request.getParameter("id"), ""),"UTF-8");
	
	MemberDao dao = new MemberDao();
	int result = dao.selectMemberInfoById(id);
	dao.closeConn();

	if(result > 0){
		out.print("Y");
	}else{
		out.print("N");
	}
%>
