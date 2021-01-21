<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
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

	request.setCharacterEncoding("UTF-8");

	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = "0000";
	String resultDesc = "success";

	String memId = URLDecoder.decode(StringUtil.nchk(request.getParameter("memId"), ""),"UTF-8");
	String memPw = URLDecoder.decode(StringUtil.nchk(request.getParameter("memPw"), ""),"UTF-8");
	String memName = URLDecoder.decode(StringUtil.nchk(request.getParameter("memName"), ""),"UTF-8");
	String memNumber = URLDecoder.decode(StringUtil.nchk(request.getParameter("memNumber"), ""),"UTF-8");
	
	String dvcToken = URLDecoder.decode(StringUtil.nchk(request.getParameter("dvcToken"), ""),"UTF-8");
	String dvcOs = URLDecoder.decode(StringUtil.nchk(request.getParameter("dvcOs"), ""),"UTF-8");
	int memDepart = Integer.parseInt(StringUtil.nchk(request.getParameter("memDepart"),"0"));  // 부서
	int memAffiliation = Integer.parseInt(StringUtil.nchk(request.getParameter("memAffiliation"),"0"));
	String memberPw;

    //skhero.kang 2019-01-07 공백 문자열 제거 ( 가끔 자동완성으로 입력되면 로그인이 안되는 경우가 있음 )
	memId = memId.trim();
	memPw = memPw.trim();
	
	String memberPic = "avatar_df.png";
	
	MemberDao dao = new MemberDao();
	MemberVO vo;
	int memSeqNo = 0;
	
	memberPw = HashUtil.encryptPassword(memId, memPw);  // 비밀번호 암호화
	
	vo = dao.selectMemberCheck(memId,memberPw, memAffiliation);  //아이디 중복 체크
	
	if(vo.getMemId() == null){
		memSeqNo = dao.memberInsert(memberPic, memId, memberPw, memName, memNumber, dvcToken, dvcOs, memAffiliation, memDepart);
		
		
		if (memSeqNo > 0) {
			
			dao.insertMemberPosition(memSeqNo); 								//회원 처음스테이지 지정
			dao.memberDepartInsert(memAffiliation, memDepart, memSeqNo, 0); 	// 가입 시 기본 소속 값 추가
			dao.insertMasterRegist(memSeqNo, memAffiliation, memDepart);  		// master 인설트
		
		} else {
			resultCode = "9999";
			resultDesc = "존재하고 있는 계정입니다.";
		}
	}else{
		//System.out.println("중복된 아이디 입니다..");
		resultCode = "8888";
		resultDesc = "중복된 아이디 입니다.";
	}

    dao.closeConn();
	
	JSONObject obj = new JSONObject();
	obj.put("cntryCode", cntryCode);
	obj.put("langCode", langCode);
	obj.put("resultCode", resultCode);
	obj.put("resultDesc", resultDesc);
	obj.put("memSeqNo", memSeqNo);

	out.print(obj);
	out.flush();
%>
