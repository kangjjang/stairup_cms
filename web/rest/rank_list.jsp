<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.RankDao" %>
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
	JSONObject obj = new JSONObject();
	int pageNo = Integer.parseInt(StringUtil.nchk(request.getParameter("pageNo"),"1"));
	if (pageNo < 1) pageNo = 1;
	int rowSize = Integer.parseInt(StringUtil.nchk(request.getParameter("rowSize"),"10"));
	int memSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("memSeqNo"),"0"));
	int rowCate = Integer.parseInt(StringUtil.nchk(request.getParameter("rowCate"),"1"));  // 일 , 주 , 월 , 연 ( 일 = 1)
	int groupSeq = Integer.parseInt(StringUtil.nchk(request.getParameter("groupSeq"),"0")); //안드로이드 통합버전에서 전체 랭킹과 소속 랭킹을 구별하는 소속 값 - 0 이면 전체값 
	
	if(rowCate < 1){
		rowCate = 1;
	}
	RankDao dao = new RankDao();
	ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	
	if(pageNo == 1){
		MemberVO vo2  = dao.selectPrivateRankMy(memSeqNo, rowCate, groupSeq);
		if(vo2 != null && vo2.getMemSeqNo() > 0){
			JSONObject obj3 = new JSONObject();
			obj3.put("memSeqNo", vo2.getMemSeqNo());
			if(vo2.getMemPic()!=null&&!vo2.getMemPic().equals("")){
				obj3.put("memPic", "upload/memberProfile/"+vo2.getMemPic());
				obj3.put("memThumbPic", "upload/Thumb/memberProfile/"+vo2.getMemPic());	
			}
			obj3.put("memName", vo2.getMemName());
			obj3.put("memDepart", vo2.getMemDepart());
			obj3.put("memCity", vo2.getCityVo().getCityName());
			obj3.put("memWalk", vo2.getToday());
			obj3.put("memRank", vo2.getRank());	
			obj3.put("memWorld", vo2.getWorldCnt());
			obj3.put("memAffliation", vo2.getMemAffiliationName());
			obj3.put("countryPic", "upload/country/"+vo2.getCityVo().getCountryPic());
			list.add(obj3);
		}
	}
	
	try{
		
		if(rowCate >0){
			ArrayList<MemberVO> vo  = dao.selectPrivateRank(memSeqNo, rowCate, pageNo, rowSize, groupSeq);	
			if(vo != null){
				for(int i = 0; i< vo.size(); i++){
					JSONObject obj2 = new JSONObject();
					MemberVO mvo = vo.get(i);
					obj2.put("memSeqNo", mvo.getMemSeqNo());
					if(mvo.getMemPic()!=null&&!mvo.getMemPic().equals("")){
						obj2.put("memPic", "upload/memberProfile/"+mvo.getMemPic());
						obj2.put("memThumbPic", "upload/Thumb/memberProfile/"+mvo.getMemPic());	
					}
					obj2.put("memName", mvo.getMemName());
					obj2.put("memDepart", mvo.getMemDepart());
					obj2.put("memCity", mvo.getCityVo().getCityName());
					obj2.put("memWalk", mvo.getToday());
					obj2.put("memRank", mvo.getRank());	
					obj2.put("memWorld", mvo.getWorldCnt());
					obj2.put("memAffliation", mvo.getMemAffiliationName());
					obj2.put("countryPic", "upload/country/"+mvo.getCityVo().getCountryPic());
					list.add(obj2);
				}
			}
		}else{
			resultCode = "9999";
			resultDesc = "FAIL";
		}
	}catch(Exception e){
		resultCode = "9999";
		resultDesc = "FAIL";
	}
	
	obj.put("cntryCode",cntryCode);
	obj.put("langCode",langCode);
	obj.put("resultCode",resultCode);
	obj.put("resultDesc",resultDesc);
	obj.put("rankList", list);

	out.print(obj);
	out.flush();
	
	dao.closeConn();
	
%>
