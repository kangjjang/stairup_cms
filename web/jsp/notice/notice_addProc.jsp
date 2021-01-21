<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.io.*"%>
<%@page import="java.io.File"%>
<%@page import="dao.NoticeDao"%>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="java.awt.Image"%>
<%@page import="com.sun.jimi.core.Jimi"%>
<%@page import="com.sun.jimi.core.JimiUtils"%>
<%
	String ROLE = cookieBox.getValue("ROLE");
	int roles = Integer.parseInt(ROLE);	

	int result = 0;

	String realPath = uploadFoler;
	int maxSize = Constant.MAX_SIZE;
	int fileSize = 0;
	int memSeqNo = 0;
	memSeqNo = Constant.ADMIN_MEM_SEQ;
	NoticeDao dao = new NoticeDao();
	//	try {
try{
		String uploadFolder = "notice/";
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
		String notiTitle = StringUtil.nchk(mul.getParameter("notiTitle"), "");
		String notiContent = StringUtil.nchk(mul.getParameter("notiContent"), "");
		String affiliationSeq = StringUtil.nchk(mul.getParameter("affiliationSeq"),"");
		if(affiliationSeq.equals("")){
			affiliationSeq = String.valueOf(roles);
		}
		int affiliation = Integer.parseInt(affiliationSeq);
		String listImg = "";
		String fileName = "";
		String imgAddre= "";
		f = mul.getFile("listImg");
		if (f !=null){
			
			fileName = StringUtil.nchk(f.getName(), "");
			imgAddre = fileName;
		}
		
		

		

		result = dao.insert(notiTitle, notiContent, imgAddre, affiliation);
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
		<script language=javascript>
			alert("등록되었습니다.");
			location.href = "notice_list.jsp";
		</script>
<%
	} else {
%>
		<script language=javascript>
			alert("등록 실패했습니다.");
			location.href = "notice_list.jsp";
		</script>
<%
	}
%>
