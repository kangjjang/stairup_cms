<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="java.net.*"%>
<%@page import="java.io.*" %>
<%@page import="org.json.*"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.MemberDao" %>
<%@page import="dao.MemberDao"%>
<%@page import="vo.MemberVO" %>
<%@page import="java.awt.Image"%>
<%@page import="com.sun.jimi.core.Jimi"%>
<%@page import="com.sun.jimi.core.JimiUtils"%>
<%@page import="util.Constant" %>
<%@page import="com.oreilly.servlet.MultipartRequest" %>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%@page import="java.text.ParseException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>

<%
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

    System.out.println("member update");

	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = "0000";
	String resultDesc = "success";
	int size = 30 * 1024 * 1024;
	JSONObject obj = new JSONObject();
	// Photo 관련 변수
	String uploadFolder = "memberProfile/";
	// 공통코드 -- FIX-ME
	String uploadRealPath = request.getSession().getServletContext().getRealPath("/upload/") + "/";
	String uploadPath = uploadRealPath + uploadFolder;
	String uploadThumbPath = uploadRealPath + "Thumb/" +"memberProfile/";
	// -----------------------------------------------------
	
	File f = new File(uploadPath); 

	if (!f.exists()) {
		f.mkdirs();
	}
	
	f = new File(uploadThumbPath); 
	
	if(!f.exists()){
		f.mkdirs();
	}
	
	MemberDao dao = new MemberDao();
	long time = System.currentTimeMillis(); 
	SimpleDateFormat dayTime = new SimpleDateFormat("yyyymmdd_hhmmss");
	String str = dayTime.format(new Date(time));
    MultipartRequest multi = new MultipartRequest(request, uploadPath, size,"UTF-8", new DefaultFileRenamePolicy());  
    String listImg = "";
    String imgAddre ="";
	
	int memDepart = Integer.parseInt(StringUtil.nchk(multi.getParameter("memDepart"),"0"));
	int memSeqNo = Integer.parseInt(StringUtil.nchk(multi.getParameter("memSeqNo"),"0"));
	int memaffiliation = Integer.parseInt(StringUtil.nchk(multi.getParameter("affiliationSeq"),"0"));

    System.out.println(memDepart);
    System.out.println(memSeqNo);
    System.out.println(memaffiliation);
	
	int result =0;
	f = multi.getFile("memPic"); 
	
	if (f != null){
		listImg = StringUtil.nchk(f.getName(), "");
	}

	int nowaffiliation = dao.selectMemberAffiliation(memSeqNo);
	
	dao.memberMainaffilUpdate(memSeqNo, memaffiliation, memDepart);   				// 1. - 2. MEM_AFFILIATION  - 메인 소속 변경
	dao.updateMasterInfo(memSeqNo, memaffiliation, memDepart);						// 3. master Table 변경// 4. 마스터 테이블 변경
	dao.memberMayorUpdate(memSeqNo, nowaffiliation); 								// 5. 시장 정보 삭제
	
	result = dao.memberUpdate(memDepart, listImg, memSeqNo, memaffiliation);   //1. 회원 정보 소속 변경
	
	if(f != null && result > 0){
		Image image = JimiUtils.getThumbnail(uploadPath + listImg, 300 , 280 , Jimi.IN_MEMORY);
		listImg = memSeqNo+"_"+str+".jpg";
		dao.memberUpdate(memDepart, listImg, memSeqNo, memaffiliation);
		Jimi.putImage(image, uploadThumbPath + listImg);
		Jimi.putImage(image, uploadPath + listImg);
	}
	
	if (result > 0) {
		;
	} else {
		resultCode = "9999";
		resultDesc = "정보 수정에 실패하였습니다.";
	}
	obj.put("cntryCode", cntryCode);
	obj.put("langCode", langCode);
	obj.put("resultCode", resultCode);
	obj.put("resultDesc", resultDesc);
	obj.put("result", result);

	out.print(obj);
	out.flush();

	dao.closeConn();
	
%>
