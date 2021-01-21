<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.MemberDao" %>
<%@page import="dao.CityDao" %>
<%@page import="vo.MemberVO" %>
<%@page import="vo.CityVO" %>
<%@page import="java.util.ArrayList"%>
<%@page import = "java.util.*"%>
<%
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

	request.setCharacterEncoding("UTF-8");
	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = ResultCode.RS_SUCCESS;
	String resultDesc = "success";
	int memberEmailCnt = 0;
	int memSeqNo = 0;
	String memId = URLDecoder.decode(StringUtil.nchk(request.getParameter("memId"),""), "UTF-8");	//회원 이메일
	String memPw = URLDecoder.decode(StringUtil.nchk(request.getParameter("memPw"),""), "UTF-8");	//부서 seq
	//String loginGubun = URLDecoder.decode(StringUtil.nchk(request.getParameter("loginGubun"),""), "UTF-8");	//로그인 구분
	int memAffiliation = Integer.parseInt(StringUtil.nchk(request.getParameter("memAffiliation"),"0"));	// 소속 시퀀스

    //skhero.kang 2019-01-07 공백 문자열 제거 ( 가끔 자동완성으로 입력되면 로그인이 안되는 경우가 있음 )
    memId = memId.trim();
    memPw = memPw.trim();
	
	String memberPw = "";

	memberPw = HashUtil.encryptPassword(memId, memPw);

    System.out.println(memId);
    System.out.println(memPw);
    System.out.println(memberPw);

	// check id : validate email
	JSONObject obj = new JSONObject();	
	MemberDao dao = new MemberDao();
	MemberVO vo = new MemberVO();
	
	vo = dao.selectMemberLogin(memId, memberPw, memAffiliation);	//이메일 중복 체크
	if(vo != null && vo.getMemSeqNo() > 0){   // 회원 아이디가 있으면
		obj.put("memSeqNo",vo.getMemSeqNo());
	}else{
		resultCode = "9999";
		resultDesc = "아이디나 비밀번호를 확인하세요.";
	}
	
	obj.put("cntryCode",cntryCode);
	obj.put("langCode",langCode);
	obj.put("resultCode",resultCode);
	obj.put("resultDesc",resultDesc);
	out.print(obj);
	out.flush();
	
	dao.closeConn();
	
%>
 