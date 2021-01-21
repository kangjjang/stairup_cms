<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.util.ArrayList"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.FightDao" %>
<%@page import="dao.ReviewDao" %>
<%@page import="vo.MemberVO" %>
<%
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

	/*2015-05-13 ksy 힘내요, 메세지 상세화면*/
	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = ResultCode.RS_SUCCESS;
	String resultDesc = "success";
	JSONObject obj2 = null;
	ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	ArrayList<JSONObject> list2 = new ArrayList<JSONObject>();
	int memSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("memSeqNo"),"0"));
/* 	int pageNo = Integer.parseInt(StringUtil.nchk(request.getParameter("pageNo"),"1"));
	int rowSize = Integer.parseInt(StringUtil.nchk(request.getParameter("rowSize"),"0"));
	if(pageNo < 1) pageNo = 1; */
	FightDao fdao = new FightDao();
	ArrayList<MemberVO> fvo = fdao.selectFight(2,memSeqNo,1,4); //힘내요 마지막 4명
	ReviewDao rdao = new ReviewDao();
	ArrayList<MemberVO> rvo = rdao.selectReview(2, memSeqNo,0); //메세지 마지막 3명
	if(fvo!=null){
		for(int i=0; i< fvo.size();i++){
			obj2 = new JSONObject();
			MemberVO fight = fvo.get(i);	
			obj2.put("memSeqNo", fight.getMemSeqNo()); 
			obj2.put("memName", fight.getMemName());
			if(fight.getMemPic()!=null&&!fight.getMemPic().equals("")){
				obj2.put("memPic", "upload/memberProfile/"+fight.getMemPic());
				obj2.put("memThumbPic","upload/Thumb/memberProfile/"+fight.getMemPic());
			}
			list.add(i,obj2);
		}
	}
	if(rvo!=null){
		for(int i=0; i< rvo.size();i++){
			obj2 = new JSONObject();
			MemberVO review = rvo.get(i);	
			obj2.put("rMemSeqNo", review.getMemSeqNo()); 
			obj2.put("rMemName", review.getMemName());
			obj2.put("content", review.getReviewVo().getReviewContent());
			obj2.put("rCrtDate", review.getReviewVo().getCrtDate());
			if(review.getMemPic()!=null&&!review.getMemPic().equals("")){
				obj2.put("rMemPic", "upload/memberProfile/"+review.getMemPic());
				obj2.put("rMemThumbPic","upload/Thumb/memberProfile/"+review.getMemPic());
			}
			list2.add(i,obj2);
		}
	}
	
	JSONObject obj = new JSONObject();
	
	obj.put("cntryCode",cntryCode);
	obj.put("langCode",langCode);
	obj.put("resultCode",resultCode);
	obj.put("resultDesc",resultDesc);
	if(fvo!=null){
		obj.put("fightList", list);
	}
	if(rvo!=null){
		obj.put("reviewList", list2);
	}
	
	
	out.print(obj);
	out.flush();
	
	fdao.closeConn();
	rdao.closeConn();
	
%>