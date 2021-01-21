<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="java.util.ArrayList"%>
<%@page import="dao.MigrationDao" %>
<%@page import="dao.DepartDao" %>
<%@page import="dao.MemberDao" %>
<%@page import="vo.MigrationVO" %>
<%@page import="vo.DepartVO" %>
<%

	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

	request.setCharacterEncoding("UTF-8");
	
	int affiliationSeq = 143;
	MigrationDao dao = new MigrationDao();
	DepartDao dao2 = new DepartDao();
	
	ArrayList<DepartVO> vo = dao.selectDepartList();
	
	if(vo!=null){
		for(int i = 0; i< vo.size(); i++){
			out.println(vo.get(i).getDepartSeqNo() + " " + vo.get(i).getDepartName() + "<br>");
			
			int departSeq = dao2.insertDepart(vo.get(i).getDepartName(), 1000, "", affiliationSeq);
			
			dao.updateMemberDepart(vo.get(i).getDepartSeqNo(), departSeq);
		}
	}else{
		out.println("실패");
	}
	
	dao.closeConn();
	dao2.closeConn();
	
%>
