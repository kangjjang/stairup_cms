<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="java.util.ArrayList"%>
<%@page import="dao.CityDao" %>
<%@page import="vo.CityVO" %>
<% 
/*
2015-05-13 ksy 도시별 시장 정보
*/
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = "0000";
	String resultDesc = "success";
	
	int memSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("memSeqNo"),"0"));
	int pageNo = Integer.parseInt(StringUtil.nchk(request.getParameter("pageNo"),"1"));
	int rowSize = Integer.parseInt(StringUtil.nchk(request.getParameter("rowSize"),"10"));
	int affiliationSeq = Integer.parseInt(StringUtil.nchk(request.getParameter("affiliationSeq"),"0"));
	
	if(pageNo < 1){
		pageNo = 1;
	}
	
	CityDao dao = new CityDao();
	ArrayList<CityVO> vo = dao.selectCityMayorList(pageNo, rowSize, affiliationSeq);			//도시명, 회원명, 회원seq, 도시사진, 재임기간
	ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	
	if(vo != null){
		for(int i = 0; i < vo.size(); i++){
			JSONObject obj2 = new JSONObject();
			
			CityVO city = vo.get(i);	
			
			obj2.put("cityName", city.getCityName()); 
			obj2.put("memSeqNo", city.getMemSeqNo());
			obj2.put("cityMem", city.getCityMem());
			obj2.put("cityDay", city.getCityDay());
			obj2.put("countryPic", "upload/country/"+city.getCountryPic());
			obj2.put("departName", city.getDepartVo().getDepartName());
			
			if(city.getMemPic() != null && !city.getMemPic().equals("")){
				obj2.put("memPic", "upload/memberProfile/"+city.getMemPic());
				obj2.put("memThumbPic", "upload/Thumb/memberProfile/"+city.getMemPic());
			}
			
			list.add(i,obj2);
		}
	}else{
		resultCode ="9999";
		resultDesc ="FAIL";
	}
	
	JSONObject obj = new JSONObject();
	obj.put("cntryCode",cntryCode);
	obj.put("langCode",langCode);
	obj.put("resultCode",resultCode);
	obj.put("resultDesc",resultDesc);
	obj.put("mayorList",list);
	out.print(obj);
	out.flush();
	
	dao.closeConn();
%>
