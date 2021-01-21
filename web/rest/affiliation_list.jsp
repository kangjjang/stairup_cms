
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.DepartDao" %>
<%@page import="dao.MemberDao"%>
<%@page import="vo.MemberVO" %>
<%@page import="vo.AffiliationVO" %>
<%@page import="java.util.ArrayList"%>
<%
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

	/* 처음 앱 실행시 회원의 가입 유무를 디바이스 토큰 값으로 확인을 한다. 그리고 부서값을 리스트로 담아서 전달 */
	request.setCharacterEncoding("UTF-8");
	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = ResultCode.RS_SUCCESS;
	String resultDesc = "success";

	String affiliationImage;
	String dvcToken = URLDecoder.decode(StringUtil.nchk(request.getParameter("dvcToken"),""), "UTF-8");
	String searchKeyword = URLDecoder.decode(StringUtil.nchk(request.getParameter("schWord"),""), "UTF-8");  //검색어
	
	////System.out.println("멤버 리저트  토큰");
	////System.out.println("dvcToken : "+dvcToken);
	////System.out.println("서치 키워드 = "+searchKeyword);
	
	int result =0;
	MemberDao dao = new MemberDao();
	MemberVO vo = dao.memberResult(dvcToken);
	if(vo.getCnt() > 0){
		if(vo.getMemResult().equals("D")){
			result = 3;
		}else if(vo.getMemResult().equals("N")){
			result =1;
		}
	}else{
		result = 2;
	}
	
	DepartDao dDao = new DepartDao();
	ArrayList<AffiliationVO> depart = dDao.affiliationtList(searchKeyword);
	ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	if(depart != null) {
		for (int i=0; i<depart.size(); i++) {
			JSONObject obj2 = new JSONObject();	
			
			AffiliationVO de = depart.get(i);	
			if(de.getAffiliationPic() == null || de.getAffiliationPic().equals("")){
				affiliationImage = "upload/affiliation/avatar_df.png";
			}else{
				affiliationImage = "upload/affiliation/"+de.getAffiliationPic();
			}
			obj2.put("affiliationSeqNo", de.getSeqNo()); 
			obj2.put("affiliationName", de.getAffiliationName());
			obj2.put("affiliationPic", affiliationImage);
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
	obj.put("result",result);
	obj.put("affiliationList",list);
	out.print(obj);
	out.flush();
	
	dao.closeConn();
	dDao.closeConn();
%>
