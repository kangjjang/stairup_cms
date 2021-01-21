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

	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = "0000";
	String resultDesc = "success";
	int size = 30 * 1024 * 1024;
	JSONObject obj = new JSONObject();
	// Photo 관련 변수
	String uploadFolder = "memberProfile/";
	// 공통코드 -- FIX-ME
	String uploadRealPath = request.getRealPath("/upload/") + "/";
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
	
    int memSeqNo = Integer.parseInt(StringUtil.nchk(multi.getParameter("memSeqNo"),"0"));
	//String memPic = URLDecoder.decode(StringUtil.nchk(multi.getParameter("memPic"), ""),"UTF-8");
	String memberBasicPw = URLDecoder.decode(StringUtil.nchk(multi.getParameter("memberBasicPw"), ""),"UTF-8");
	String memberPw = URLDecoder.decode(StringUtil.nchk(multi.getParameter("memberPw"), ""),"UTF-8");
	String memberPhone = URLDecoder.decode(StringUtil.nchk(multi.getParameter("memberPhone"), ""),"UTF-8");
	String memberName = URLDecoder.decode(StringUtil.nchk(multi.getParameter("memberName"), ""),"UTF-8");
	
	int result =0;
	int passwordCnt =0;
	f = multi.getFile("memPic"); 
	String filename = multi.getFilesystemName("memPic");
	if (f !=null){
		listImg = StringUtil.nchk(f.getName(), "");
	}
	
	////System.out.println("listImg : " +listImg);
	//////System.out.println("memPic : " +memPic);
	////System.out.println("memSeqNo : " +memSeqNo);
	////System.out.println("memberPw : " +memberPw);
	////System.out.println("memberBasicPw : " +memberBasicPw);
	////System.out.println("memberPhone : " +memberPhone);
	
	if(memberPw.equals("")){ // 비밀번호 변경 안할 때
		////System.out.println("비밀번호 변경 없음");
		result = dao.angularMemberUpdate(memSeqNo, memberName, memberPhone, listImg);
	}else{ // 비밀번호 변경 할 때
		// 비밀번호 비교
		////System.out.println("비밀번호 변경, 비밀번호 있는지 검사");
		passwordCnt = dao.angularSelectMemberPwUd(memSeqNo, memberBasicPw);
		if(passwordCnt > 0){ // 비밀번호가 일치 할 때 비밀번호와 같이 변경
			////System.out.println("비밀번호에 일치하는게 있음 비밀번호도 같이 변경");
			result = dao.angularMemberUpdate(memSeqNo, memberName, memberPhone, listImg, memberPw);
		}else{ // 비밀번호가 일치 하지 않을 때
			////System.out.println("비밀번호틀렸음");
			resultCode = "8888";
			resultDesc = "기존 비밀번호가 일치 하지 않습니다.";
		}
	}
	
	
	if(f !=null&&result>0){
		Image image = JimiUtils.getThumbnail(uploadPath + listImg, 300 , 280 , Jimi.IN_MEMORY);
		// 한글 파일명 decoder 로 정상적으로 보여지게 하여서 다시 파일을 서버에 저장한다. 2016-01-11 ksy
		//listImg = URLDecoder.decode(listImg);
		//listImg = listImg.replaceAll("\\s", "");
		listImg = memSeqNo+"_"+str+".jpg";
		dao.angularMemberUpdate(memSeqNo, memberName, memberPhone, listImg);
		Jimi.putImage(image, uploadThumbPath + listImg);	 
		Jimi.putImage(image, uploadPath + listImg);	 
	}
	
	/* if (result > 0) {
		;
	} else {
		resultCode = "9999";
		resultDesc = "정보 수정에 실패하였습니다.";
	} */
	
	obj.put("cntryCode", cntryCode);
	obj.put("langCode", langCode);
	obj.put("resultCode", resultCode);
	obj.put("resultDesc", resultDesc);
	obj.put("result", result);
	out.print(obj);
	out.flush();
	
	dao.closeConn();
%>
