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
	int pageNo = Integer.parseInt(StringUtil.nchk(request.getParameter("pageNo"),"1"));
	if (pageNo < 1) pageNo = 1;
	int rowSize = Integer.parseInt(StringUtil.nchk(request.getParameter("rowSize"),"6"));
	int affiliationSeq = Integer.parseInt(StringUtil.nchk(request.getParameter("affiliationSeq"),"0"));
	
	NoticeDao dao = new NoticeDao();
	ArrayList<NoticeVO> vo = dao.noticeList(pageNo, rowSize,affiliationSeq);
	ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	if(vo!=null){
		for(int i = 0; i< vo.size(); i++){
			JSONObject obj2 = new JSONObject();
			NoticeVO notice = vo.get(i);	
			int noticeSeqNo = notice.getNotiSeqNo();
			obj2.put("noticeSeqNo", noticeSeqNo); 
			obj2.put("title", notice.getNotiTitle());
			obj2.put("crtDate", notice.getCrtDate());
			
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
	if(vo!=null){
		obj.put("noticeList",list);
	}
	
	out.print(obj);
	out.flush();
	
	dao.closeConn();
	
%>
