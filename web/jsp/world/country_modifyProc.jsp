<%@page import="dao.CityDao"%>
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
	String realPath = uploadFoler ; 		//파일이 업로드 될 경로
	int maxSize = Constant.MAX_SIZE; 			//파일 크기제한  1k * 1k * 10 = 10메가
	int fileSize = 0; //선언한 변수들을 0으로 초기화 
	int result = 0;	    
	int no =0;	
	int pageno = 0;	  
	boolean photoChg = true;
	CityDao dao = new CityDao();
	try{
		String uploadFolder = "country/";
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
		String listImgNamea = ""; /*String 변수로 선언된 변수들을 초기화*/
		String stDate = "";
		String edDate = "";
		String contentUrl="";
		int memSeqNo = Constant.ADMIN_MEM_SEQ;
		String countrySeqNo = StringUtil.nchk(mul.getParameter("countrySeqNo"),"");  
		/* pageno = Integer.parseInt(StringUtil.nchk(mul.getParameter("pageno"), "1")); */
		String countryName = StringUtil.nchk(mul.getParameter("countryName"),"");  
		no = Integer.parseInt(countrySeqNo);
		f = mul.getFile("listImg");
		if (f !=null){  
			listImgName = StringUtil.nchk(f.getName(), "");   
		}else{ 
			photoChg = false;
		}
	
		
		result = dao.countryUpdate(no, countryName, listImgName);
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
			location.href = "country_list.jsp?no=<%=no%>";
	</script>
<%
	}else{
%>
		<script language=javascript>
			alert("수정에 실패했습니다.");
			location.href = "country_list.jsp";
		</script>
<%
	}
%>