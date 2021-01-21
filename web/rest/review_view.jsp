<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.util.ArrayList"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
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
	int rowCate = Integer.parseInt(StringUtil.nchk(request.getParameter("rowCate"),"1"));					//1:기부, 2:회원정보
	int pageNo = Integer.parseInt(StringUtil.nchk(request.getParameter("pageNo"),"1"));
	int rowSize = Integer.parseInt(StringUtil.nchk(request.getParameter("rowSize"),"10"));
	if(pageNo < 1) pageNo = 1;
	int limitGubun = 0;  // 메세지 리미트 3
	
	////System.out.println("memSeqNo = " + memSeqNo);
	////System.out.println("rowCate = " + rowCate);
	
	
	
	ReviewDao rdao = new ReviewDao();
	ArrayList<MemberVO> rvo = rdao.selectReview(rowCate, memSeqNo, limitGubun); 
	
	if(rvo!=null){
		for(int i=0; i< rvo.size();i++){
			obj2 = new JSONObject();
			MemberVO review = rvo.get(i);	
			obj2.put("rMemSeqNo", review.getMemSeqNo()); 
			obj2.put("rMemName", review.getMemName());
			obj2.put("content", review.getReviewVo().getReviewContent());
			obj2.put("doSeqNo", review.getReviewVo().getReviewSeqNo());
			if(review.getMemPic()!=null&&!review.getMemPic().equals("")){
				obj2.put("rMemPic", "upload/memberProfile/"+review.getMemPic());
				obj2.put("rMemThumbPic","upload/Thumb/memberProfile/"+review.getMemPic());
			}
			
			list2.add(i,obj2);
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
	
	if(rvo!=null){
		obj.put("reviewList", list2);
	}
	out.print(obj);
	out.flush();
	
	rdao.closeConn();
	
%>
