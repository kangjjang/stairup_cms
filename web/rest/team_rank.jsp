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
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = "0000";
	String resultDesc = "success";
	JSONObject obj = new JSONObject();
	int pageNo = Integer.parseInt(StringUtil.nchk(request.getParameter("pageNo"),"1"));
	if (pageNo < 1) pageNo = 1;
	int rowSize = Integer.parseInt(StringUtil.nchk(request.getParameter("rowSize"),"6"));
	int memSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("memSeqNo"),"0"));
	/* int rowCate = Integer.parseInt(StringUtil.nchk(request.getParameter("rowCate"),"6")); */
	DepartDao dao = new DepartDao();
	ArrayList<DepartVO> vo = dao.selectTeamRank(memSeqNo, pageNo, rowSize);
	ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	if(vo!=null){
		for(int i = 0; i< vo.size(); i++){
			JSONObject obj2 = new JSONObject();
			DepartVO rank = vo.get(i);	
			obj2.put("teamRank", rank.getDepartRank());
			obj2.put("teamPic", "upload/depart/"+rank.getHomePic());
			obj2.put("teamThumbPic", "upload/Thumb/depart/"+rank.getHomePic());
			obj2.put("teamName", rank.getDepartName());
			obj2.put("scoreWin", rank.getDepartWin());
			obj2.put("scoreLose",rank.getDepartLose());
			obj2.put("scoreDraw",rank.getDepartDraw());
			
			list.add(i,obj2);
		}

	}else{
		resultCode="9999";
		resultDesc="FAIL";
	}
	
	obj.put("cntryCode",cntryCode);
	obj.put("langCode",langCode);
	obj.put("resultCode",resultCode);
	obj.put("resultDesc",resultDesc);
	obj.put("teamRankList",list);
	out.print(obj);
	out.flush();
	
	dao.closeConn();
	
%>
