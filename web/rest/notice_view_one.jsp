<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.NoticeDao" %>
<%@page import="vo.NoticeVO" %>
<%
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = ResultCode.RS_SUCCESS;
	String resultDesc = "success";
	int noticeSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("noticeSeqNo"),"1"));
	
	JSONObject obj2 = null;	
	
	NoticeDao dao = new NoticeDao();
	NoticeVO vo = dao.noticeView(noticeSeqNo);
	
	if(vo != null){
		obj2 = new JSONObject();
		
		obj2.put("noticeTitle", vo.getNotiTitle());
		obj2.put("noticePic", vo.getNoticePic()==null ? null : "notice/" + vo.getNoticePic()); 
		obj2.put("noticeContent", vo.getNotiContent());
		
	}
	
	JSONObject obj = new JSONObject();
	obj.put("cntryCode",cntryCode);
	obj.put("langCode",langCode);
	obj.put("resultCode",resultCode);
	obj.put("resultDesc",resultDesc);
	obj.put("notice",obj2);
	out.print(obj);
	out.flush();
	
	dao.closeConn();
	
%>
