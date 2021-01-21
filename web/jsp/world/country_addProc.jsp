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

try{
		String uploadFolder = "country/";
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
		String countryName = StringUtil.nchk(mul.getParameter("countryName"), "");
		/* String orderLy = StringUtil.nchk(mul.getParameter("orderLy"), ""); */
		/* int order = Integer.parseInt(orderLy); */
		
		String listImg = "";
		String fileName = "";
		f = mul.getFile("listImg");
		if (f !=null){
			
			fileName = StringUtil.nchk(f.getName(), "");
		}
		
		/* int ordera = dao.selectOrderly(order,0); */
		
		int nameResult = dao.selectCountryName(countryName);
		int totalCountry = dao.selectLastOrder();
		int order = totalCountry +1;
		
		////System.out.println("nameResult = "+nameResult);
		if(nameResult <= 0){
			
			result = dao.countryInsert(countryName,order ,fileName);	
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
			location.href = "country_list.jsp";
		</script>
<%
	} else {
%>
		<script language=javascript>
			alert("이미 등록된 국가 입니다.");
			location.href = "country_list.jsp";
		</script>
<%
	}
%>
