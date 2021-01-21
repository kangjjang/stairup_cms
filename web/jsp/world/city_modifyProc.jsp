<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.io.*"%>
<%@page import="java.io.File"%>
<%@page import="dao.CityDao"%>
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
	CityDao dao = new CityDao();
	//	try {
try{
		String uploadFolder = "city/";
		String uploadRealPath = request.getRealPath("/upload/") + "/";
		String uploadPath = uploadRealPath + uploadFolder;
		String uploadThumbPath = uploadRealPath + "Thumb/" + uploadFolder;
		File f = new File(uploadPath); 
		File fa = new File(uploadPath); 
		
		if(!f.exists()){ 
			f.mkdirs(); 
		}
		if(!fa.exists()){ 
			fa.mkdirs(); 
		}
		f = new File(uploadThumbPath); 
		fa = new File(uploadThumbPath); 
		if(!f.exists()){
			f.mkdirs();
		}
		if(!fa.exists()){
			fa.mkdirs();
		}
		MultipartRequest mul = new MultipartRequest(request, uploadPath, maxSize, "UTF-8", new DefaultFileRenamePolicy());
		String cityName = StringUtil.nchk(mul.getParameter("cityName"), "");
		String stair = StringUtil.nchk(mul.getParameter("stair"), "");
		/* String country = StringUtil.nchk(mul.getParameter("country"), ""); */
		String citySeqNo = StringUtil.nchk(mul.getParameter("citySeqNo"), "");
		/* int citySeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("citySeqNo"), "1")); //요청하여 받은 값을 no변수에 저장  */
		int totalstair = Integer.parseInt(stair);
		/* int countrySeqNo = Integer.parseInt(country); */
		int citySeq = Integer.parseInt(citySeqNo);
		String listImg = "";
		String fileName = "";
		String listImga = "";
		String fileNamea = "";
		f = mul.getFile("daylistImg");
		fa = mul.getFile("nightlistImg");
		if (f !=null){
			
			fileName = StringUtil.nchk(f.getName(), "");			//낮 이미지
		}
		if (fa !=null){
			
			fileNamea = StringUtil.nchk(fa.getName(), "");			//밤 이미지
		}
		

		
		result = dao.cityUpdate(cityName, totalstair, fileName, fileNamea,citySeq);	
		
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
			location.href = "city_list.jsp";
		</script>
<%
	} else {
%>
		<script language=javascript>
			alert("이미 등록된 순서가 있습니다.");
			location.href = "city_list.jsp";
		</script>
<%
	}
%>
