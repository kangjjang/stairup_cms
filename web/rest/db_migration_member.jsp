<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="java.util.ArrayList"%>
<%@page import="dao.MigrationDao" %>
<%@page import="dao.DepartDao" %>
<%@page import="dao.MemberDao" %>
<%@page import="vo.MemberVO" %>
<%@page import="vo.MigrationVO" %>
<%@page import="vo.DepartVO" %>
<%

	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

	request.setCharacterEncoding("UTF-8");
	
	int affiliationSeq = 143;
	MigrationDao dao = new MigrationDao();
	MemberDao dao2 = new MemberDao();
	
	ArrayList<MemberVO> vo = dao.selectMemberList();
	
	if(vo!=null){
		for(int i = 0; i< vo.size(); i++){
			String memberPw = HashUtil.encryptPassword(vo.get(i).getMemNumber(), vo.get(i).getMemNumber());  // 비밀번호 암호화
			
			//System.out.println(StringUtil.NVL(vo.get(i).getMemPic()) + StringUtil.NVL(vo.get(i).getMemNumber()) + StringUtil.NVL(vo.get(i).getMemName()) 
			//+ StringUtil.NVL(vo.get(i).getMemNumber()) + "" + "" + affiliationSeq + vo.get(i).getDepartSeq());
			
			int memberSeq = dao2.memberInsert(StringUtil.NVL(vo.get(i).getMemPic()), StringUtil.NVL(vo.get(i).getMemNumber()), memberPw, StringUtil.NVL(vo.get(i).getMemName()), StringUtil.NVL(vo.get(i).getMemNumber()), "", "", affiliationSeq, vo.get(i).getDepartSeq());
			
			dao2.memberDepartInsert(affiliationSeq, vo.get(i).getDepartSeq(), memberSeq, 0);
			
			//dao2.insertMemberPosition(memberSeq); 	
			//dao2.insertMasterRegist(memberSeq, affiliationSeq, vo.get(i).getDepartSeq());  		// master 인설트
			
			dao.updateBeaconLog(vo.get(i).getMemSeqNo(), memberSeq);
			dao.updateMaster(vo.get(i).getMemSeqNo(), memberSeq);
			dao.updateMemStay(vo.get(i).getMemSeqNo(), memberSeq);
			dao.updateWorld(vo.get(i).getMemSeqNo(), memberSeq);
			dao.updateFriend(vo.get(i).getMemSeqNo(), memberSeq);
			dao.updateFriend2(vo.get(i).getMemSeqNo(), memberSeq);
			dao.updateLike(vo.get(i).getMemSeqNo(), memberSeq);
			dao.updateLike2(vo.get(i).getMemSeqNo(), memberSeq);
		}
	}else{
		out.println("실패");
	}
	
	dao.closeConn();
	dao2.closeConn();
	
%>
