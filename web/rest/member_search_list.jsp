<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.util.ArrayList"%>
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
	String resultCode = ResultCode.RS_SUCCESS;
	String resultDesc = "success";

	////System.out.println("memberName");
	String memberName = URLDecoder.decode(StringUtil.nchk(request.getParameter("schWord"),""), "UTF-8");
	int memAffiliation = Integer.parseInt(StringUtil.nchk(request.getParameter("groupSeq"),"0"));	//부서 seq
	
	////System.out.println("memberName = " + memberName);
	////System.out.println("memAffiliation = " + memAffiliation);
	int totalCount=0;
	
	ArrayList<MemberVO> vo =null;
	ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	MemberDao dao = new MemberDao();
    // check id : validate email
	if(memberName != null){
		
		vo = dao.searchMemberList(memberName,memAffiliation);
		
		totalCount = dao.searchMemberListCount(memberName, memAffiliation);
		if(vo!=null){
			for(int i = 0; i< vo.size(); i++){
				JSONObject obj2 = new JSONObject();
				MemberVO search = vo.get(i);	
				int memSeqNo = search.getMemSeqNo();
				obj2.put("memSeqNo", memSeqNo); 
				obj2.put("memName", search.getMemName());
				obj2.put("memDepart", search.getMemDepart());
				if(search.getMemPic()!=null&&!search.getMemPic().equals("")){
					obj2.put("memPic", "upload/memberProfile/"+search.getMemPic());
					obj2.put("memThumbPic","upload/Thumb/memberProfile/"+search.getMemPic());
				}
				
				list.add(i,obj2);
			}
		}
	}else{
		resultCode = "9999";
		resultDesc = "검색어 입력이 안되었습니다.";
	}
	
	JSONObject obj = new JSONObject();
	obj.put("cntryCode",cntryCode);
	obj.put("langCode",langCode);
	obj.put("resultCode",resultCode);
	obj.put("resultDesc",resultDesc);
	obj.put("totalCount", totalCount);
	if(vo!=null){
		obj.put("memberList",list);
	}
	out.print(obj);
	out.flush();
	
	dao.closeConn();
	
%>
