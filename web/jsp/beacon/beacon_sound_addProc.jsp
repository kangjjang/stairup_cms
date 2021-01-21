<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.io.*"%>
<%@page import="java.io.File"%>
<%@page import="dao.BeaconEventDao"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="java.awt.Image"%>
<%@page import="com.sun.jimi.core.Jimi"%>
<%@page import="com.sun.jimi.core.JimiUtils"%>
<%
	int result = 0;
	String id = cookieBox.getValue("ID");
	String realPath = uploadFoler;
	int maxSize = Constant.MAX_SIZE;
	int fileSize = 0;
	int memSeqNo = 0;
	memSeqNo = Constant.ADMIN_MEM_SEQ;
	BeaconEventDao dao = new BeaconEventDao();
	//	try {
try{
		String uploadFolder = "beaconStreaming/";
		String uploadRealPath = request.getRealPath("/upload/") + "/";
		String uploadPath = uploadRealPath + uploadFolder;
		String uploadThumbPath = uploadRealPath + "Thumb/" + uploadFolder;
		File f = new File(uploadPath); 
		
		if(!f.exists()){ 
			f.mkdirs(); 
		}
		
		f = new File(uploadThumbPath); 
		
		if(!f.exists()){
			f.mkdirs();
		}
		
		MultipartRequest mul = new MultipartRequest(request, uploadPath, maxSize, "UTF-8", new DefaultFileRenamePolicy());
		String soundName = StringUtil.nchk(mul.getParameter("soundName"), "");
		String soundLocation = StringUtil.nchk(mul.getParameter("soundLocation"), "");

		String fileName = "";
		String imgAddre= "";
		
		f = mul.getFile("listImg");
		if (f !=null){
			fileName = StringUtil.nchk(f.getName(), "");
			imgAddre = fileName;
		}
		
		////System.out.println("soundName : "+soundName);
		////System.out.println("soundLocation : "+soundLocation);
		////System.out.println("imgAddre : "+imgAddre);
		
		result = dao.insertBeaconSound(soundName, soundLocation, imgAddre);

	}catch(IOException e){
		System.out.println(e);
	}catch(Exception ex){
		System.out.println(ex);
	}finally{
		dao.closeConn();
	}
	
	

	// 	} catch(Exception ex) {
	// 		System.out.println(ex);
	// 	}	

	if (result > 0) {
%>
		<script language=javascript>
			alert("등록되었습니다.");
			location.href = "beacon_list.jsp";
		</script>
<%
	} else {
%>
		<script language=javascript>
			alert("등록 실패했습니다.");
			location.href = "beacon_list.jsp";
		</script>
<%
	}
%>