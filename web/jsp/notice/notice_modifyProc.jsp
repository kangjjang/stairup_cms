<%@page import="dao.NoticeDao"%>
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
<%
	String ROLE = cookieBox.getValue("ROLE");
	int roles = Integer.parseInt(ROLE);	

	
	String realPath = uploadFoler ; 		//파일이 업로드 될 경로
	int maxSize = Constant.MAX_SIZE; 			//파일 크기제한  1k * 1k * 10 = 10메가
	int fileSize = 0; //선언한 변수들을 0으로 초기화 
	int result = 0;	    
	int no = 0;			
	int pageno = 0;	  
	boolean photoChg = true;
	NoticeDao dao = new NoticeDao();
	
	try{
		String uploadFolder = "notice/";
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
		String notiTitle = StringUtil.nchk(mul.getParameter("notice_title"),"");  
		String notiContent = StringUtil.nchk(mul.getParameter("notiContent"),"");
		String affiliationSeq = StringUtil.nchk(mul.getParameter("affiliationSeq"),"");
		
		if(affiliationSeq.equals("")){
			affiliationSeq = String.valueOf(roles);
		}
		int affiliation = Integer.parseInt(affiliationSeq);
		
		f = mul.getFile("listImg");
		if (f !=null){  
			listImgName = StringUtil.nchk(f.getName(), "");   
			imgAddre = listImgName;
		}else{ 
			photoChg = false;
		}
		
		result = dao.update(no, notiTitle, notiContent,imgAddre, affiliation);
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
			alert("수정되었습니다.");
			location.href = "notice_view.jsp?no=<%=no%>";
		</script>
<%
	}else{
%>
		<script language=javascript>
			alert("수정에 실패했습니다.");
			location.href = "notice_list.jsp";
		</script>
<%
	}
%>