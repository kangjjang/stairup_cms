<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.MemberDao" %>
<%@page import="vo.MemberVO" %>
<%
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = ResultCode.RS_SUCCESS;
	String resultDesc = "success";
	
	int memSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("memSeqNo"),"0"));	// 내 memSeqNo
	int matMemSeq = Integer.parseInt(StringUtil.nchk(request.getParameter("matMemSeq"),"0"));	// 상대 memSeqNo
	int gubun = Integer.parseInt(StringUtil.nchk(request.getParameter("gubun"),"1"));	// 1: 친구 등록, 2: 친구 취소
	int result = 0;
    // check id : validate email
	
	MemberDao dao = new MemberDao();
	if(gubun == 1){
		result = dao.friendAdd(memSeqNo, matMemSeq);
	}else if(gubun == 2){
		result = dao.friendDelete(memSeqNo, matMemSeq);
	}
	if(result >0){
		;
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
	
	dao.closeConn();
	
%>