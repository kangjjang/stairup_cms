<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.DepartDao" %>
<%@page import="vo.DepartVO" %>
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
	int memSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("memSeqNo"),"6"));
	int rowCate = Integer.parseInt(StringUtil.nchk(request.getParameter("rowCate"),"6"));
/* 	String rowCate1 = URLDecoder.decode(StringUtil.nchk(request.getParameter("rowCate"),""), "UTF-8");	 */
	int hTeamSeq = Integer.parseInt(StringUtil.nchk(request.getParameter("hTeamSeq"),"6"));
	int aTeamSeq = Integer.parseInt(StringUtil.nchk(request.getParameter("aTeamSeq"),"6"));
	String startDay = URLDecoder.decode(StringUtil.nchk(request.getParameter("startDay"),""), "UTF-8");	
	String endDay = URLDecoder.decode(StringUtil.nchk(request.getParameter("endDay"),""), "UTF-8");
	/* ////System.out.println("rowCate1 : " +rowCate1); */
	
	int ato = 0;
	int hto = 0;
	int homeCnt=0;
	int homeToday=0;
	int awayCnt=0;
	int awayToday=0;
	
	DepartDao dao = new DepartDao();
	DepartVO hnvo = dao.selectDepartView(hTeamSeq);									//홈부서 기본 정보와,
	//dao = new DepartDao();
	DepartVO hvo = dao.selectTeamTotal(hTeamSeq, startDay, endDay);					//총 걸음수
	homeCnt = (hvo.getWorkCnt()*hvo.getWorkCnt())/hnvo.getDepartPeople();
	if(rowCate==1){
		//dao = new DepartDao();
		hto = dao.selectTeamToday(hTeamSeq);										//홈오늘 걸음수
		homeToday = (hto*hto)/hnvo.getDepartPeople();
	}
	//dao = new DepartDao();
	MemberVO hTop = dao.selectTeamTop(rowCate, hTeamSeq, startDay, endDay);			//홈mvp
	/*=======================================================================================================================*/
	//dao = new DepartDao();
	DepartVO anvo = dao.selectDepartView(aTeamSeq);									//어웨이 부서 기본 정보
	//dao = new DepartDao();
	DepartVO avo = dao.selectTeamTotal(aTeamSeq, startDay, endDay);					// 총 걸음수	
	awayCnt = (avo.getWorkCnt()*avo.getWorkCnt())/anvo.getDepartPeople();
	if(rowCate==1){
		//dao = new DepartDao();
		ato = dao.selectTeamToday(aTeamSeq);										//어웨이 오늘 걸음수
		awayToday = (ato*ato)/anvo.getDepartPeople();
	}
	//dao = new DepartDao();
	MemberVO aTop = dao.selectTeamTop(rowCate, aTeamSeq, startDay, endDay);					//어웨이 mvp
	if(hnvo != null && anvo != null){
		obj.put("hTeamPic", "upload/depart/"+hnvo.getHomePic());
		obj.put("hTeamThumbPic", "upload/Thumb/depart/"+hnvo.getHomePic());
		obj.put("hTeamName", hnvo.getDepartName());
		obj.put("aTeamPic", "upload/depart/"+anvo.getHomePic());
		obj.put("aTeamThumbPic", "upload/Thumb/depart/"+anvo.getHomePic());
		obj.put("aTeamName", anvo.getDepartName());
		if(rowCate==1){
			obj.put("hTeamToday", homeToday);
			obj.put("aTeamToday", awayToday);
		}
		obj.put("hTeamTotal", homeCnt);
		if(hTop.getMemPic()!=null&&!hTop.getMemPic().equals("")){
			obj.put("hTeamMVPpic", "upload/memberProfile/"+hTop.getMemPic());
			obj.put("hTeamMVPThumbPic", "upload/Thumb/memberProfile/"+hTop.getMemPic());
		}
		
		obj.put("hTeamMVPname",hTop.getMemName());
		obj.put("hTeamMVPseq",hTop.getMemSeqNo());
			
/* 			
		if(rowCate == 1){
			obj.put("aTeamToday", awayToday);
		} */
		obj.put("aTeamTotal", awayCnt);
		if(aTop.getMemPic()!=null&&!aTop.getMemPic().equals("")){
			obj.put("aTeamMVPpic", "upload/memberProfile/"+aTop.getMemPic());
			obj.put("aTeamMVPThumbPic", "upload/Thumb/memberProfile/"+aTop.getMemPic());
		}
		
		obj.put("aTeamMVPname",aTop.getMemName());
		obj.put("aTeamMVPseq",aTop.getMemSeqNo());
			

	}else{
		resultCode = "9999";
		resultDesc = "FAIL";
	}
	
	obj.put("cntryCode",cntryCode);
	obj.put("langCode",langCode);
	obj.put("resultCode",resultCode);
	obj.put("resultDesc",resultDesc);
	out.print(obj);
	out.flush();
	
	dao.closeConn();
	
%>
