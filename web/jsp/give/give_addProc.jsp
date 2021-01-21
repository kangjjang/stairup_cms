<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.io.*"%>
<%@page import="java.io.File"%>
<%@page import="dao.DonationDao"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="java.awt.Image"%>
<%@page import="com.sun.jimi.core.Jimi"%>
<%@page import="com.sun.jimi.core.JimiUtils"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%
	int result = 0;
	String id = cookieBox.getValue("ID");
	String realPath = uploadFoler;
	int maxSize = Constant.MAX_SIZE;
	int fileSize = 0;
	int memSeqNo = 0;
	memSeqNo = Constant.ADMIN_MEM_SEQ;
	DonationDao dao = new DonationDao();
	//	try {
try{
		String uploadFolder = "give/";
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
		//File img_f = mul.getFile(mul.getFilesystemName("listImg"));
		
		
		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyymmdd_hhmmss");
		String str = dayTime.format(new Date(time));
		
		String giveTitle = StringUtil.nchk(mul.getParameter("giveTitle"), "");
		String giveContent = StringUtil.nchk(mul.getParameter("giveContent"), "");
		String stairs = StringUtil.nchk(mul.getParameter("stairs"), "");
		String endDate = StringUtil.nchk(mul.getParameter("endDate"), "");
		int stairsInt = Integer.parseInt(stairs);
		////System.out.println("giveTitle = "+giveTitle);
		////System.out.println("giveContent" + giveContent);
		////System.out.println("endDate" + endDate);
		////System.out.println("stairsInt" +stairsInt);
		
		//String listImg = "";
		String fileName = "";
		String imgAddre = "";
		f = mul.getFile("listImg");
		
		
		if (f !=null){
			fileName = StringUtil.nchk(f.getName(), "");
			imgAddre = fileName;
		}
		////System.out.println("f = " + f);
		////System.out.println("imgAddre = " + imgAddre);
		
		giveContent = giveContent.trim();
		giveContent = giveContent.replaceAll("&nbsp;", " ");
		giveContent = giveContent.replaceAll("&quot;", "/");
		giveContent = giveContent.replaceAll("&lt;", "<");
		giveContent = giveContent.replaceAll("&gt;", ">");
		giveContent = giveContent.replaceAll("</[pP]>", "\n");
		giveContent = giveContent.replaceAll("<(/)?[bB][rR](\\s)*(/)?>", "\n");
		giveContent = giveContent.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
		
		////System.out.println("giveContent = " + giveContent);
		
		result = dao.insert(giveTitle, giveContent, imgAddre,stairsInt,endDate);
		
 		 if (f !=null&&result>0) {
 			////System.out.println("이곳에 오는가");
 			//FileUtil.createProfileThumbnail(uploadPath, uploadThumbPath, memPhoto);
 			////System.out.println("imgAddre = " + imgAddre);
			Image image = JimiUtils.getThumbnail(uploadPath + imgAddre, 600 , 400 , Jimi.IN_MEMORY);
			imgAddre = result+"_"+str+".jpg";
			Jimi.putImage(image, uploadThumbPath + imgAddre);
			Jimi.putImage(image, uploadPath + imgAddre);
			dao.updateImage(imgAddre,result);
		}
		
	}catch(IOException e){
		System.out.println(e);
	}catch(Exception ex){
		System.out.println(ex);
	}finally{
		dao.closeConn();
	}
	
	if (result > 0) {
%>
		<script language=javascript>
			alert("등록되었습니다.");
			location.href = "give_list.jsp";
		</script>
<%
	} else {
%>
		<script language=javascript>
			alert("등록 실패했습니다.");
			location.href = "give_list.jsp";
		</script>
<%
	}
%>
