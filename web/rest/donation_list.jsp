<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.DonationDao" %>
<%@page import="vo.DonationVO" %>
<%@page import="java.util.ArrayList"%>
<% 
	/*
	2015-05-13 ksy 힘내라 기부 리스트
	*/
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");


	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = ResultCode.RS_SUCCESS;
	String resultDesc = "success";

	int memSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("memSeqNo"),"0"));
	int pageNo = Integer.parseInt(StringUtil.nchk(request.getParameter("pageNo"),"1"));
	int rowSize = Integer.parseInt(StringUtil.nchk(request.getParameter("rowSize"),"5"));
	int result =0;
	if(pageNo < 1) pageNo = 1;
	java.util.Date thisdate = new java.util.Date();	//오늘 날짜
	java.util.Date actdate;
	DonationDao dao = new DonationDao();
	ArrayList<DonationVO> vo = dao.selectDonationList(pageNo, rowSize);
	ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	if(vo!= null){
		for(int i = 0; i < vo.size(); i++){
			JSONObject obj2 = new JSONObject();
			DonationVO donation = vo.get(i);	
			
			int percent = (donation.getCnt() / donation.getAim()) * 100;		//진행된 계단수 / 목표 계단수 
			obj2.put("doSeqNo", donation.getGiveSeqNo()); 
			obj2.put("title", donation.getTitle());
			//skhero.kang 2015-12-12 이미지 URL 잘못된 부분 수정
			obj2.put("doPic", "upload/give/"+donation.getPic());
			obj2.put("doThumbPic", "upload/Thumb/give/"+donation.getPic());
			/* obj2.put("doPic", donation.getPic());
			obj2.put("doThumbPic", donation.getPic()); */
			obj2.put("doTotalFloor", donation.getAim());		//목표 계단수
			obj2.put("doNowFloor", donation.getCnt());			//진행된 계단수
			obj2.put("doReviewCnt", donation.getReviewCnt());	//메세지 개수
			obj2.put("doFightCnt", donation.getFightCnt());		//힘내요 개수
			obj2.put("percent", percent);						//진행 퍼센트
			obj2.put("endDate", donation.getEndDate());			//종료일
			obj2.put("startDate", donation.getCrtDate());
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
	if(vo != null){
		obj.put("donationList", list);
	}
	out.print(obj);
	out.flush();
	
	dao.closeConn();
	
%>
