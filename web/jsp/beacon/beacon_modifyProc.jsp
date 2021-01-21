<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.io.*"%>
<%@page import="java.io.File"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.sun.jimi.core.Jimi"%>
<%@page import="com.sun.jimi.core.JimiUtils"%>
<%@page import="java.awt.Image"%>
<%@page import="dao.BeaconEventDao"%>
<%
	String realPath = uploadFoler ; 		//파일이 업로드 될 경로
	int maxSize = Constant.MAX_SIZE; 			//파일 크기제한  1k * 1k * 10 = 10메가
	int fileSize = 0; //선언한 변수들을 0으로 초기화 
	int result = 0;	    
	int no = 0;			
	int pageno = 0;	  
	boolean photoChg = true;
	BeaconEventDao dao = new BeaconEventDao();
	
	try{
		String uploadFolder = "beacon/";
		String uploadRealPath = request.getRealPath("/upload/") + "/";
		String uploadPath = uploadRealPath + uploadFolder;
		String uploadThumbPath = uploadRealPath + Constant.PHOTO_UPLOAD_URL_THUMBNAIL + uploadFolder;
		MultipartRequest mul = new MultipartRequest(request, uploadPath, maxSize, "UTF-8", new DefaultFileRenamePolicy()); //파일 업로드를 함
		File f = new File(uploadPath); //파일의 경로를 f에 저장 
		
		if(!f.exists()){ //만약 폴더가 존재하지 않으면 폴더를 생성 
			f.mkdirs();
		}
		
		f = new File(uploadThumbPath); 
		
		if(!f.exists()){
			f.mkdirs();
		}
		
		String listImgName = ""; /*String 변수로 선언된 변수들을 초기화*/
		String imgAddre ="";
		String stDate = "";
		String edDate = "";
		String contentUrl="";
		int memSeqNo = Constant.ADMIN_MEM_SEQ;
		no = Integer.parseInt(StringUtil.nchk(mul.getParameter("no"), "0")); 
		/* pageno = Integer.parseInt(StringUtil.nchk(mul.getParameter("pageno"), "1")); */
		String beaconMajor = StringUtil.nchk(mul.getParameter("beaconMajor"),"");
		String beaconMinor = StringUtil.nchk(mul.getParameter("beaconMinor"),"");
		String beaconLocation = StringUtil.nchk(mul.getParameter("beaconLocation"),"");
		String beaconUse = StringUtil.nchk(mul.getParameter("beaconUse"),"");
		String beaconPosition = StringUtil.nchk(mul.getParameter("beaconPosition"),"");
		String urlUse = StringUtil.nchk(mul.getParameter("urlUse"),"");
		String beaconUrl1 = StringUtil.nchk(mul.getParameter("Url"),"");
		String beaconContent = StringUtil.nchk(mul.getParameter("beaconContent"),"");
		String beaconTitle = StringUtil.nchk(mul.getParameter("title"),"");
		int beaconSound = Integer.parseInt(StringUtil.nchk(mul.getParameter("beaconSound"),"0"));
		String logoImage = StringUtil.nchk(mul.getParameter("logoImage"), "");
		
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
			beaconPopFromDateTime = "2017-01-31 00:00:00";
		}
		
		if(beaconPopToDateTime.length() == 0){
			beaconPopToDateTime = "2018-12-31 23:59:59";
		}
		
		int beaconPopCnt = Integer.parseInt(StringUtil.nchk(mul.getParameter("beaconPopCnt"),"-1"));
		
		int major = Integer.parseInt(beaconMajor);
		int minor = Integer.parseInt(beaconMinor);
		
		f = mul.getFile("listImg");
		if (f !=null){  
			/* listImgName = StringUtil.nchk(f.getName(), ""); */
			listImgName = StringUtil.nchk(f.getName(), "");   
			imgAddre = listImgName;
		}
		
		result = dao.BeaconEventupdate(no, major, minor, beaconLocation, beaconUse, beaconPosition, beaconUrl1, beaconContent, imgAddre, beaconTitle, urlUse, beaconSound, logoImage, beaconPopFromDateTime, beaconPopToDateTime, beaconPopCnt);
	
		if(result > 0){
			dao.BeaconEventLogDelete(major,minor);
		}
		
	}catch(IOException e){
		System.out.println(e);
	}catch(Exception ex){
		System.out.println(ex);
	}finally{
		dao.closeConn();
	}
	
	if(result > 0){
%>
	<script type="text/javascript">
			alert("수정되었습니다.");
			location.href = "beacon_view.jsp?no=<%=no%>";
		</script>
<%
	}else{
%>
		<script type="text/javascript">
			alert("수정에 실패했습니다.");
			location.href = "beacon_list.jsp";
		</script>
<%
	}
%>