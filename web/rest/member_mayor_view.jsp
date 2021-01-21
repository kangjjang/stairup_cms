<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.util.ArrayList"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.MemberDao" %>
<%@page import="dao.CityDao" %>
<%@page import="vo.MemberVO" %>
<%@page import="vo.CityVO" %>
<%
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
	int rowSize = Integer.parseInt(StringUtil.nchk(request.getParameter("rowSize"),"10"));
	if(pageNo < 1) pageNo = 1;
	String memResult ="";
	JSONObject obj2 = null;
	ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	CityDao cdao = new CityDao();
	ArrayList<CityVO> mvo = cdao.selectMayorList(memSeqNo,pageNo,rowSize);	//내가 시장인 도시
	if(mvo != null){
		for(int i =0; i<mvo.size(); i++){
			obj2 = new JSONObject();
			CityVO mayor = mvo.get(i);	
			obj2.put("worldName", mayor.getCountryName()); 
			obj2.put("cityName", mayor.getCityName());
			obj2.put("countryPic","upload/country/"+mayor.getCountryPic());
			obj2.put("affiliationName",mayor.getAffiliationVo().getAffiliationName());
			obj2.put("affiliationSeq",mayor.getAffiliationVo().getSeqNo());
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
	if(mvo != null){
		obj.put("mayorList", list);
	}
	
	
	out.print(obj);
	out.flush();
	
	cdao.closeConn();
	
%>
