<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="dao.ReviewDao" %>
<%
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

	request.setCharacterEncoding("UTF-8");
	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = "0000";
	String resultDesc = "success";

	int reviewSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("reviewSeqNo"),"0"));
	int result =0;
	
	ReviewDao dao = new ReviewDao();
	result = dao.reviewDelete(reviewSeqNo);
	if(result>0){
		;
	}else{
		resultCode = "9999";
		resultDesc = "리뷰 삭제에 실패하였습니다.";
	}
	JSONObject obj = new JSONObject();
	obj.put("cntryCode", cntryCode);
	
	obj.put("langCode", langCode);
	obj.put("resultCode", resultCode);
	obj.put("resultDesc", resultDesc);
	obj.put("result", result);

	out.print(obj);
	out.flush();
	
	dao.closeConn();
	
%>