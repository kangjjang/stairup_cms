<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.FightDao" %>
<%@page import="vo.EventVO" %>
<%@page import="vo.MemberVO" %>
<%
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = "0000";
	String resultDesc = "success";
	ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	
	int memSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("memSeqNo"),"1"));						//내 seq
	int boardCate = Integer.parseInt(StringUtil.nchk(request.getParameter("boardCate"),"6"));					//1: 기부, 2: 회원
	int boardSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("boardSeqNo"),"6"));					//상대혹은 기부 seq
	int gubunCode = Integer.parseInt(StringUtil.nchk(request.getParameter("gubunCode"),"1"));					// 1: 입력, 2: 삭제
	int result = 0;
	
	FightDao dao = new FightDao();
	try{
		switch(gubunCode){
		case 1:
			result = dao.insertFight(memSeqNo, boardCate, boardSeqNo);
			break;
		case 2:
			result = dao.deleteFight(memSeqNo, boardCate, boardSeqNo);
			break;
		}	
	}catch(Exception e){
		resultCode = "9999";
		resultDesc = "FAIL";
	}
	if(result<1){
		resultCode = "9999";
		resultDesc = "FAIL";
	}
	//dao = new FightDao();
	int countFingt = dao.selectFightCnt(boardCate, boardSeqNo);
	JSONObject obj = new JSONObject();
	obj.put("cntryCode",cntryCode);
	obj.put("langCode",langCode);
	obj.put("resultCode",resultCode);
	obj.put("resultDesc",resultDesc);
	obj.put("countFingt",countFingt);
	out.print(obj);
	out.flush();
	
	dao.closeConn();
	
%>
