<%@page import="util.Constant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.BeaconEventDao" %>
<%@page import="vo.BeaconEventVO" %>
<%@page import="java.util.Date"%>
<%
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");


	int major = Integer.parseInt(StringUtil.nchk(request.getParameter("major"),"0"));			// 건물과 계단위치
	int minor = Integer.parseInt(StringUtil.nchk(request.getParameter("minor"),"0"));			// 층수
	int memSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("memSeqNo"),"0"));
 	
	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = ResultCode.RS_SUCCESS;
	String resultDesc = "success";

    /*System.out.println("##beacon_transfer");
    System.out.println("major : " + major);
    System.out.println("minor : " + minor);
    System.out.println("memSeqNo : " + memSeqNo);*/

    JSONObject obj = new JSONObject();

    if(false){  // skhero.kang 2019-01-03 임시방편으로 로직 중지
    //if(memSeqNo > 0 && major > 0 && minor > 0){  // 회원일 경우에만
		
		BeaconEventDao edao = new BeaconEventDao();
		BeaconEventVO evo = new BeaconEventVO();
		
		evo = edao.selectBeaconEvent(major, minor);
		
		String eventUse = evo.getBeaconUse();  // 비콘의 팝업 사용 여부 저장
		
		if(eventUse != null && eventUse.equals("Y")){

			//항상 뜨는 비콘일 경우(30분이 지났을 경우에만 팝업)
			if(evo.getBeaconPopCnt() == 0){
				
				String tranferDateTime = edao.selectTransTime(major , minor, memSeqNo);     // 비콘 팝업 전송 시간을 가져옴
				int transTime = 0;
				if(tranferDateTime.length() > 0){
					
					transTime = edao.selectTransferTime(tranferDateTime);   //해당 비콘의 팝업을 발송한 시간과 현재 시간을 비교
				}
				
				if(tranferDateTime.length() == 0 || transTime >= 60){
					
					String beaconImage = "";
					if(evo.getBeaconImage() != null){
						beaconImage = evo.getBeaconImage();
					}
					
					String beaconTitle = "";
					if(evo.getBeaconTitle() == null || evo.getBeaconTitle().equals("")){
						beaconTitle = "";
					}else{
						beaconTitle = evo.getBeaconTitle();
					}
					
					obj.put("beaconImage", "http://cms.stairup.io:8080/upload/beacon/" + beaconImage);
					obj.put("beaconUrl", evo.getBeaconUrl());
					obj.put("urlUse", evo.getBeaconUrlYN());   // URL 연결 링크를 사용할 것인지 여부 -> 앱에서 팝업 노출시 Y일 경우 연결링크 버튼 노출 
					obj.put("beaconContent", evo.getBeaconContent());
					obj.put("beaconTitle", beaconTitle);
					
					if(evo.getBeaconSoundSeq() > 0){
						obj.put("soundGubun", "http://cms.stairup.io:8080/upload/beaconStreaming/"+evo.getSoundUrl());
					}else{
						obj.put("soundGubun", "N");   // 사용 안함
					}
					
					if(evo.getLogoImage() != null && evo.getLogoImage().equals("N")){
						obj.put("imageLogo", "N");
					}else{
						obj.put("imageLogo", "http://cms.stairup.io:8080/upload/affiliation/"+evo.getLogoImage());
					}
					
					edao.insertEventLog(memSeqNo,major,minor);
					
				}else{
					
					resultCode = "8888";
					resultDesc = "조금전에 비콘 팝업이 뜬 사용자입니다.";
				}
				
			}else{		//처음 접근한 사용자 1회만 뜨는 비콘일 경우
				String tranferDateTime = edao.selectTransTime(major , minor, 0);     // 비콘 팝업 전송 시간을 가져옴
				
				if(tranferDateTime.length() == 0){
					
					String beaconImage = "";
					if(evo.getBeaconImage() != null){
						beaconImage = evo.getBeaconImage();
					}
					
					String beaconTitle = "";
					if(evo.getBeaconTitle() == null || evo.getBeaconTitle().equals("")){
						beaconTitle = "";
					}else{
						beaconTitle = evo.getBeaconTitle();
					}
					
					obj.put("beaconImage", "http://cms.stairup.io:8080/upload/beacon/" + beaconImage);
					obj.put("beaconUrl", evo.getBeaconUrl());
					obj.put("urlUse", evo.getBeaconUrlYN());   // URL 연결 링크를 사용할 것인지 여부 -> 앱에서 팝업 노출시 Y일 경우 연결링크 버튼 노출 
					obj.put("beaconContent", evo.getBeaconContent());
					obj.put("beaconTitle", beaconTitle);
					
					if(evo.getBeaconSoundSeq() > 0){
						obj.put("soundGubun", "http://cms.stairup.io:8080/upload/beaconStreaming/"+evo.getSoundUrl());
					}else{
						obj.put("soundGubun", "N");   // 사용 안함
					}
					
					if(evo.getLogoImage() != null && evo.getLogoImage().equals("N")){
						obj.put("imageLogo", "N");
					}else{
						obj.put("imageLogo", "http://cms.stairup.io:8080/upload/affiliation/"+evo.getLogoImage());
					}
					
					edao.insertEventLog(memSeqNo,major,minor);
				}else{
					
					resultCode = "5555";
					resultDesc = "이미 1회 뜬 비콘팝업 입니다.";
				}
				
			}
		}else{
			resultCode = "6666";
			resultDesc = "비콘 팝업이 등록되지 않은 비콘입니다.";
		}
		
		edao.closeConn();
		
	}else{  //비회원
		resultCode = "7777";
		resultDesc = "로그인을 안했습니다.";
	}
	
	obj.put("cntryCode",cntryCode);
	obj.put("langCode",langCode);
	obj.put("resultCode",ResultCode.RS_FAIL);
	obj.put("resultDesc",resultDesc);

    obj.put("beaconUrl", "");
    obj.put("beaconImage", "");
    obj.put("urlUse", "");
    obj.put("beaconContent", "");
    obj.put("beaconTitle", "");
    obj.put("soundGubun", "N");   // 사용 안함
    obj.put("imageLogo", "N");

    out.print(obj);
	out.flush();
%>