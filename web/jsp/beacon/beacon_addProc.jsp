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
		String uploadFolder = "beacon/";
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
		String logoImage = StringUtil.nchk(mul.getParameter("logoImage"), "");
		String beaconMajor = StringUtil.nchk(mul.getParameter("beaconMajor"), "");
		String beaconMonor = StringUtil.nchk(mul.getParameter("beaconMinor"), "");
		String beaconLocation = StringUtil.nchk(mul.getParameter("beaconLocation"), "");
		String beaconUse = StringUtil.nchk(mul.getParameter("beaconUse"), "");
		String beaconPosition = StringUtil.nchk(mul.getParameter("beaconPosition"), "");
		String urlUse = StringUtil.nchk(mul.getParameter("urlUse"), "");
		String beaconUrls = StringUtil.nchk(mul.getParameter("url"), "");
		String beaconContent = StringUtil.nchk(mul.getParameter("beaconContent"), "");
		String beaconTitle = StringUtil.nchk(mul.getParameter("title"),"");
		int beaconSound = Integer.parseInt(StringUtil.nchk(mul.getParameter("beaconSound"),"0"));
		
		//skhero.kang 2017-03-17 기간,변수 설정
		String beaconPopFromDate = StringUtil.nchk(mul.getParameter("beaconPopFromDate"), "");
		String beaconPopToDate = StringUtil.nchk(mul.getParameter("beaconPopToDate"), "");
		String beaconPopFromTime = StringUtil.nchk(mul.getParameter("beaconPopFromTime"), "");
		String beaconPopToTime = StringUtil.nchk(mul.getParameter("beaconPopToTime"), "");
		
		String beaconPopFromDateTime = beaconPopFromDate + " " + beaconPopFromTime;
		String beaconPopToDateTime = beaconPopToDate + " " + beaconPopToTime;
		
		beaconPopFromDateTime = beaconPopFromDateTime.trim();
		beaconPopToDateTime = beaconPopToDateTime.trim();
		
		if(beaconPopFromDateTime.length() == 0){
			beaconPopFromDateTime = "2017-01-31";
		}
		
		if(beaconPopToDateTime.length() == 0){
			beaconPopToDateTime = "2018-12-31";
		}
		
		int beaconPopCnt = Integer.parseInt(StringUtil.nchk(mul.getParameter("beaconPopCnt"),"-1"));
		
		
		
		int major = Integer.parseInt(beaconMajor);
		int minor = Integer.parseInt(beaconMonor);
		
		String listImg = "";
		String fileName = "";
		String imgAddre= "";
		
		f = mul.getFile("listImg");
		if (f !=null){
			
			fileName = StringUtil.nchk(f.getName(), "");
			imgAddre = fileName;
		}
		
		/* String beaconUrl ="";
		if(beaconUrls != null){
			beaconUrl ="http://"+beaconUrls;
		} */
		
		result = dao.insertBeaconEvent(major,minor, beaconLocation, beaconUse, beaconPosition, beaconUrls, beaconContent, imgAddre, beaconTitle, urlUse, beaconSound, logoImage, beaconPopFromDateTime, beaconPopToDateTime, beaconPopCnt);
		
		if(result > 0){
			dao.BeaconEventLogDelete(major,minor);
		}
		/* if(result > 0)
		{
			Image image = JimiUtils.getThumbnail(uploadPath + imgAddre, 200 , 200 , Jimi.IN_MEMORY);
    		Jimi.putImage(image, uploadThumbPath + imgAddre);
  
		} */
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
		<script type="text/javascript">
			alert("등록되었습니다.");
			location.href = "beacon_list.jsp";
		</script>
<%
	} else {
%>
		<script type="text/javascript">
			alert("등록 실패했습니다.");
			location.href = "beacon_list.jsp";
		</script>
<%
	}
%>
