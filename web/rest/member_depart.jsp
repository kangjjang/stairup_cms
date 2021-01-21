<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.DepartDao" %>
<%@page import="vo.DepartVO" %>
<%@page import="java.util.ArrayList"%>
<%
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

	/* 회원 가입, 로그인 시 선택한 소속의 부서값을 리스트로 전달 함  */
	request.setCharacterEncoding("UTF-8");
	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = ResultCode.RS_SUCCESS;
	String resultDesc = "success";

	String affiliationSeqNo = URLDecoder.decode(StringUtil.nchk(request.getParameter("affiliationSeqNo"),""), "UTF-8"); //셀렉트 박스로 선택한 소속 시퀀스
	String departName = URLDecoder.decode(StringUtil.nchk(request.getParameter("schWord"),""), "UTF-8");  //검색어
	
	DepartDao dDao = new DepartDao();
	ArrayList<DepartVO> depart = dDao.MemberDepartList(affiliationSeqNo, departName);
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
	obj.put("departList",list);
	out.print(obj);
	out.flush();
	
	dDao.closeConn();
	
%>
