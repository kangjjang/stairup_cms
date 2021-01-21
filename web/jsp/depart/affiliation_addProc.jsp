<%@ page contentType="text/html; charset=utf-8" %>
<%-- <%@page import="dao.PhotoDao"%> --%>
<%@page import="dao.AffiliationDao"%>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.io.*"%>
<%@page import="java.io.File"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="java.awt.Image"%>
<%@page import="com.sun.jimi.core.Jimi"%>
<%@page import="com.sun.jimi.core.JimiUtils"%>
<%
	String id = cookieBox.getValue("ID");
	String realPath = uploadFoler ;
	int maxSize = Constant.MAX_SIZE; 			
	int fileSize = 0;  
	int result = 0; 
	
	AffiliationDao dao = new AffiliationDao();
	
	try{
		String uploadFolder = "affiliation/";
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
		
		String listImg = "";
		String fileName = null;
		String affiliationtName = StringUtil.nchk(mul.getParameter("notiTitle"),""); 
		f = mul.getFile("listImg");
		if (f !=null){
			
			fileName = StringUtil.nchk(f.getName(), "");
		}
		
		
		result = dao.insertDepart(affiliationtName, fileName);
		
		if(f !=null&&result>0){
			Image image = JimiUtils.getThumbnail(uploadPath + fileName, 300 , 280 , Jimi.IN_MEMORY);
			Jimi.putImage(image, uploadThumbPath + fileName);
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
		<script language=javascript>
			alert("등록되었습니다.");
			location.href = "affiliation_list.jsp";
		</script>
<%
	}else{
%>
		<script language=javascript>
			alert("등록 실패했습니다."); 
			location.href = "affiliation_list.jsp"; 
		</script>
<%
	}
%>
