<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.util.ArrayList"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.BeaconEventDao" %>
<%@page import="vo.BeaconEventVO" %>
<%
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

	/*2015-05-13 ksy 힘내요, 메세지 상세화면*/
	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = ResultCode.RS_SUCCESS;
	String resultDesc = "success";
	JSONObject obj = new JSONObject();
	
	/* String memSeqNo = StringUtil.nchk(request.getParameter("memSeqNo"),""); */
	String majorNo = StringUtil.nchk(request.getParameter("major"),"");
	String minorNo = StringUtil.nchk(request.getParameter("minor"),"");
	
	/* int memSeq = Integer.parseInt(memSeqNo); */
	int major = Integer.parseInt(majorNo);
	int minor = Integer.parseInt(minorNo);
	
	////System.out.println("beaconEvent.jsp★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
	////System.out.println("major"+major);
	////System.out.println("minor"+minor);
	////System.out.println("beaconEvent.jsp★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
	
	BeaconEventDao dao = new BeaconEventDao();
	BeaconEventVO vo = new BeaconEventVO();
		
	if(major>0){
		vo = dao.selectBeaconEvent(major, minor);
		////System.out.println("vo.getSoundUrl() = "+vo.getSoundUrl());
		////System.out.println("vo.getLogoImage() : "+vo.getLogoImage());
		String beaconImage = "";
		if(vo.getBeaconImage()!=null){
			beaconImage = "/upload/beacon/"+vo.getBeaconImage();
		}
		String beaconTitle = "";
		if(vo.getBeaconTitle() == null || vo.getBeaconTitle().equals("")){
			beaconTitle = "";
		}else{
			beaconTitle = vo.getBeaconTitle();
		}
		obj.put("beaconImage", beaconImage);
		obj.put("beaconUrl", vo.getBeaconUrl());
		obj.put("urlUse", vo.getBeaconUrlYN());   // URL 연결 링크를 사용할 것인지 여부 -> 앱에서 팝업 노출시 Y일 경우 연결링크 버튼 노출 
		obj.put("beaconContent", vo.getBeaconContent());
		obj.put("beaconTitle", beaconTitle);
		if(vo.getBeaconSoundSeq() > 0){
			obj.put("soundGubun", "http://ec2-52-78-198-250.ap-northeast-2.compute.amazonaws.com:8080/upload/beaconStreaming/"+vo.getSoundUrl());
		}else{
			obj.put("soundGubun", "N");   // 사용 안함
		}
		
		if(vo.getLogoImage().equals("N")){
			obj.put("imageLogo", "N");
		}else{
			obj.put("imageLogo", "http://ec2-52-78-198-250.ap-northeast-2.compute.amazonaws.com:8080/upload/affiliation/"+vo.getLogoImage());
		}
		
		////System.out.println("★★★★beaconTitle★★★★"+beaconTitle);
		////System.out.println("★★★★beaconUrl★★★★"+vo.getBeaconUrl());
		
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
