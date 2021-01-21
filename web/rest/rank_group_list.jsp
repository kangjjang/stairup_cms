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
	int groupSeq = Integer.parseInt(StringUtil.nchk(request.getParameter("groupSeq"),"0")); //KT 버전에서 그룹랭킹에사용됨 - 0 일경우에는 모든 소속들 간의 랭킹 0이 아니면 소속안의 부서끼리의 랭킹
	int departSeq = Integer.parseInt(StringUtil.nchk(request.getParameter("departSeq"),"0")); // KT 버전에서 소속안의 부서들간 랭킹에 사용됨
	int mode = Integer.parseInt(StringUtil.nchk(request.getParameter("mode"),"0")); // KT 버전에서 구룹랭킹의 모드 구분
	
	////System.out.println("###########pageNo : " + pageNo);
	////System.out.println("###########rowSize : " + rowSize);
	////System.out.println("###########memSeqNo : " + memSeqNo);
	////System.out.println("###########rowCate : " + rowCate);
	////System.out.println("###########groupSeq : " + groupSeq);
	////System.out.println("###########111departSeq : " + departSeq);
	////System.out.println("###########mode : " + mode);
	
	if(rowCate < 1){
		rowCate = 1;
	}
	RankDao dao = new RankDao();
	ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	if(pageNo == 1){
		MemberVO vo2  = dao.selectPrivateGroupRankMy(rowCate, groupSeq, departSeq, mode);
		if(vo2 != null && vo2.getMemSeqNo() > 0){
			JSONObject obj3 = new JSONObject();
			obj3.put("memSeqNo", vo2.getMemSeqNo());  // 부서 , 소속 시퀀스
			if(vo2.getMemPic()!=null&&!vo2.getMemPic().equals("")){
				obj3.put("memPic", "upload/memberProfile/"+vo2.getMemPic());   // 부서 , 소속 사진
				obj3.put("memThumbPic", "upload/affiliation/"+vo2.getMemPic());	 // 부서 , 소속 사진
			}
			obj3.put("memName", vo2.getMemAffiliationName());  // 부서 , 소속 이름
			obj3.put("memWalk", vo2.getToday());  // 부서 , 소속 걸음수
			obj3.put("memRank", vo2.getRank());	  // 부서 , 소속 랭킹
			list.add(obj3);
		}
	}
	
	try{
		if(rowCate >0){
			ArrayList<MemberVO> vo  = dao.selectPrivateGroupRank(rowCate, pageNo, rowSize, groupSeq, departSeq, mode);	
			if(vo != null){
				for(int i = 0; i< vo.size(); i++){
					JSONObject obj2 = new JSONObject();
					MemberVO mvo = vo.get(i);
					obj2.put("memSeqNo", mvo.getMemSeqNo());  // 부서 , 소속 시퀀스
					if(mvo.getMemPic()!=null&&!mvo.getMemPic().equals("")){
						obj2.put("memPic", "upload/memberProfile/"+mvo.getMemPic());   // 부서 , 소속 사진
						obj2.put("memThumbPic", "upload/affiliation/"+mvo.getMemPic());	 // 부서 , 소속 사진
					}
					obj2.put("memName", mvo.getMemAffiliationName());  // 부서 , 소속 이름
					obj2.put("memWalk", mvo.getToday());  // 부서 , 소속 걸음수
					obj2.put("memRank", mvo.getRank());	  // 부서 , 소속 랭킹
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
