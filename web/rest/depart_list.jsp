<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.DepartDao" %>
<%@page import="vo.DepartVO" %>

<% 
/* 2015-05-06 ksy
비콘 로그를 저장하기 위한곳
앱으로 결과값을 전달할 필요는 없다.*/

	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = "0000";
	String resultDesc = "success";
	
	DepartDao dDao = new DepartDao();
	ArrayList<DepartVO> depart = dDao.departList();
	ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	if(depart != null) {
		for (int i=0; i<depart.size(); i++) {
			JSONObject obj2 = new JSONObject();
			
			DepartVO de = depart.get(i);	
			int deSeqNo = de.getDepartSeqNo();
			obj2.put("departSeqNo", deSeqNo); 
			obj2.put("departName", de.getDepartName());
			
			list.add(i,obj2);
			
		}
	}else{
		resultCode = "9999";
		resultDesc = "FAIL";
	}
	
	JSONObject obj = new JSONObject();
	
	obj.put("cntryCode",cntryCode);
	obj.put("langCode",langCode);
	obj.put("resultCode",resultCode);
	obj.put("resultDesc",resultDesc);
	
	out.print(obj);
	out.flush();
	
	dDao.closeConn();
%>
