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
		/* String orderLy = StringUtil.nchk(mul.getParameter("orderLy"), ""); */
		String stair = StringUtil.nchk(mul.getParameter("stair"), "");
		String country = StringUtil.nchk(mul.getParameter("country"), "");
		/* int order = Integer.parseInt(orderLy); */
		int totalstair = Integer.parseInt(stair);
		int countrySeqNo = Integer.parseInt(country);
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
		
		////System.out.println("cityName= "+cityName);
		////System.out.println("stair = "+stair);
		////System.out.println("country ="+country);
		
		
		
		int orderb = dao.selectCityOrder(countrySeqNo);
		////System.out.println("orderb = "+orderb);
		
		int countryCnt = dao.selectCountCnt(countrySeqNo);
		////System.out.println("countryCnt= "+countryCnt);
		
		
		int maxCity = dao.selectMaxCity();
		
		int order = maxCity +1;   // 새로 등록하는 국가의 도시일 때 제일 마지막 순번으로 

		////System.out.println("마지막 오더 : "+order);
		
/* 		int ordera = dao.selectOrderly(order,1); */
		
		if(countryCnt > 0){   
			////System.out.println("이미 도시가 있을때");
			order = orderb +1;
			dao.updateOrderList(order);
			////System.out.println("이미 도시가 있을때 오더 값 = " + order);
			result = dao.cityInsert(cityName, order, totalstair, fileName, fileNamea, countrySeqNo);	
		}else{
			////System.out.println("새 국가의 도시 입력");
			result = dao.cityInsert(cityName, order, totalstair, fileName, fileNamea, countrySeqNo);
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
			location.href = "city_list.jsp";
		</script>
<%
	} else {
%>
		<script language=javascript>
			alert("계단 수를 입력해 주세요.");
			location.href = "city_list.jsp";
		</script>
<%
	}
%>
