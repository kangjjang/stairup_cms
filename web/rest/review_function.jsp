<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.ReviewDao" %>
<%@page import="vo.EventVO" %>
<%@page import="vo.MemberVO" %>
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
	ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	
	int memSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("memSeqNo"),"1"));						//내 seq
	int boardCate = Integer.parseInt(StringUtil.nchk(request.getParameter("boardCate"),"6"));					//1: 기부, 2: 회원
	int boardSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("boardSeqNo"),"6"));					//상대혹은 기부 seq
	int gubunCode = Integer.parseInt(StringUtil.nchk(request.getParameter("gubunCode"),"1"));					// 1: 입력, 2: 수정, 3:삭제
	int conSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("conSeqNo"),"1"));
	int result = 0;
	
	String content = URLDecoder.decode(StringUtil.nchk(request.getParameter("content"),""), "UTF-8");			//내용
	////System.out.println("===================== content : " +content);
	ReviewDao dao = new ReviewDao();
	try{
		switch(gubunCode){
		case 1:
			result = dao.insertReview(memSeqNo, boardCate, boardSeqNo, content);
			break;
		case 2:
			result = dao.updateReview(memSeqNo, boardCate, boardSeqNo, content, conSeqNo);
			break;
		case 3:
			result = dao.deleteReview(memSeqNo, boardCate, boardSeqNo, conSeqNo);
			break;
		}	
	}catch(Exception e){
		resultCode = "9999";
		resultDesc = "FAIL";
	}
	
	//dao = new ReviewDao();
	ArrayList<MemberVO> vo = dao.selectReview(boardCate, boardSeqNo,0);
	if(vo!=null){
		for(int i = 0; i < vo.size(); i++){
			JSONObject obj2 = new JSONObject();
			MemberVO review = vo.get(i);
			obj2.put("doMemSeqNo", review.getMemSeqNo()); 
			if(review.getMemPic()!=null&&!review.getMemPic().equals("")){
				obj2.put("doMemPic", "upload/memberProfile/"+review.getMemPic());
				obj2.put("doMemThumbPic", "upload/Thumb/memberProfile/"+review.getMemPic());
			}
			obj2.put("doMemName", review.getMemName());
			obj2.put("doContent", review.getReviewVo().getReviewContent());
			obj2.put("doSeqNo", review.getReviewVo().getReviewSeqNo());
			obj2.put("fcCrtDate", review.getReviewVo().getCrtDate());
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
		obj.put("reviewList", list);
	}
	out.print(obj);
	out.flush();
	
	dao.closeConn();
	
%>
