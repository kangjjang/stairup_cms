<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.util.ArrayList"%>
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
	String resultCode = "0000";
	String resultDesc = "success";
	int memSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("memSeqNo"),"1"));
	MemberDao dao = new MemberDao();
	ArrayList<MemberVO> vo = dao.friendList(memSeqNo);
	//dao = new MemberDao();
	ArrayList<MemberVO> bvo = dao.bestFriendList(memSeqNo);
	ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	ArrayList<JSONObject> list2 = new ArrayList<JSONObject>();
	try{
		if(vo!=null){
			for(int i = 0; i< vo.size(); i++){
				JSONObject obj2 = new JSONObject();
				MemberVO mem = vo.get(i);	
				int fmemSeqNo = mem.getMemSeqNo();
				obj2.put("fmemSeqNo", fmemSeqNo); 
				if(mem.getMemPic()!=null&&!mem.getMemPic().equals("")){
					obj2.put("fmemPic", "upload/memberProfile/"+mem.getMemPic());
					obj2.put("fmemThumbPic","upload/Thumb/memberProfile/"+mem.getMemPic());
				}
				obj2.put("fworldCnt",mem.getWorldCnt());
				obj2.put("fcountryPic","upload/country/"+mem.getCityVo().getCountryPic());
				/* obj2.put("fcountryThumbPic","upload/Thumb/country/"+mem.getCityVo().getCountryPic()); */
				obj2.put("fcityName",mem.getCityVo().getCityName());
				obj2.put("fRank",mem.getRank());
				obj2.put("fmemName", mem.getMemName());
				obj2.put("fmemToday", mem.getFloorCnt());
				
				list.add(i,obj2);
			}
		}
		if(bvo!=null){
			for(int i = 0; i< bvo.size(); i++){
				JSONObject obj3 = new JSONObject();
				MemberVO bmem = bvo.get(i);	
				int fmemSeqNo = bmem.getMemSeqNo();
				obj3.put("fbmemSeqNo", fmemSeqNo); 
				if(bmem.getMemPic()!=null&&!bmem.getMemPic().equals("")){
					obj3.put("fbmemPic", "upload/memberProfile/"+bmem.getMemPic());
					obj3.put("fbmemThumbPic", "upload/Thumb/memberProfile/"+bmem.getMemPic());
				}
				obj3.put("fbworldCnt",bmem.getWorldCnt());
				obj3.put("fbcountryPic","upload/country/"+bmem.getCityVo().getCountryPic());
				/* obj3.put("fbcountryThumbPic","upload/Thumb/country/"+bmem.getCityVo().getCountryPic()); */
				obj3.put("fbcityName",bmem.getCityVo().getCityName());
				obj3.put("fbRank",bmem.getRank());
				obj3.put("fbmemName", bmem.getMemName());
				obj3.put("fbmemToday", bmem.getFloorCnt());
				
				list2.add(i,obj3);
			}
		}
	}catch(Exception e){
		resultCode = "9999";
		resultDesc = "FAIL";
	}
	JSONObject obj = new JSONObject();
	obj.put("cntryCode",cntryCode);
	obj.put("langCode",langCode);
	obj.put("resultCode",resultCode);
	obj.put("resultDesc",resultDesc);
	obj.put("friendList",list);
	obj.put("friendBestList",list2);
	out.print(obj);
	out.flush();
	
	dao.closeConn();
	
%>
