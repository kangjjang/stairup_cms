<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.EventDao" %>
<%@page import="vo.EventVO" %>
<%
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = "0000";
	String resultDesc = "success";
	int pageNo = Integer.parseInt(StringUtil.nchk(request.getParameter("pageNo"),"1"));
	if (pageNo < 1) pageNo = 1;
	int rowSize = Integer.parseInt(StringUtil.nchk(request.getParameter("rowSize"),"6"));
	int affiliationSeq = Integer.parseInt(StringUtil.nchk(request.getParameter("affiliationSeq"),"0"));
	
	EventDao dao = new EventDao();
	ArrayList<EventVO> vo = dao.eventList(pageNo, rowSize, affiliationSeq);
	ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	if(vo!=null){
		for(int i = 0; i< vo.size(); i++){
			JSONObject obj2 = new JSONObject();
			EventVO event = vo.get(i);	
			int eventSeqNo = event.getEventSeqNo();
			obj2.put("eventSeqNo", eventSeqNo); 
			obj2.put("title", event.getEventTitle());
			obj2.put("crtDate", event.getCrtDate());
			
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
	obj.put("eventList",list);
	out.print(obj);
	out.flush();
	
	dao.closeConn();
	
%>
