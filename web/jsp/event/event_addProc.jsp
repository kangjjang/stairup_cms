<%@ page contentType="text/html; charset=utf-8" %>
<%@page import="dao.PhotoDao"%>
<%@page import="dao.EventDao"%>
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
<%@page import="dao.DeviceDao"%>
<%@page import="vo.DeviceVO" %>
<%@page import="java.util.ArrayList"%>
<%
	String ROLE = cookieBox.getValue("ROLE");
	int roles = Integer.parseInt(ROLE);
	
	String realPath = uploadFoler ;
	int maxSize = Constant.MAX_SIZE; 			
	int fileSize = 0;  
	int result = 0; 
	int memSeqNo = 0;
	memSeqNo = Constant.ADMIN_MEM_SEQ;
	EventDao dao = new EventDao();
	
	try{
		String uploadFolder = "event/";
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
		String fileName = "";
		String imgAddre = "";
		String wbContent = StringUtil.nchk(mul.getParameter("wallContent"),""); 
		String notiTitle = StringUtil.nchk(mul.getParameter("notiTitle"),""); 
		
		String wbPublicLevel = "1";
		String contentUrl = StringUtil.nchk(mul.getParameter("contentUrl"),"");
		
		String affiliationSeq = StringUtil.nchk(mul.getParameter("affiliationSeq"),"");
		if(affiliationSeq.equals("")){
			affiliationSeq = String.valueOf(roles);
		}
		int affiliation = Integer.parseInt(affiliationSeq);
		
		
		f = mul.getFile("listImg");
		if (f !=null){
			fileName = StringUtil.nchk(f.getName(), "");
			imgAddre = fileName;
		}
		
		
		result = dao.insert(wbContent, notiTitle,imgAddre, affiliation);
		/* if(result > 0){
			ArrayList<String> regidAndroid = new ArrayList<String>();
			ArrayList<String> regidAndroidSeper = new ArrayList<String>();
			ArrayList<String> regidIos = new ArrayList<String>();
			
			String index = "1";
			String ticker = "STAIR N 이벤트 메시지";
			String title = "STAIR N 이벤트 소식";
			String msg = "STAIR N 이벤트 소식이 도착하였습니다.";
			
	 		DeviceDao deviceDao = new DeviceDao();
			deviceDao.getDeviceToken(regidAndroid, regidIos);
			
			//안드로이드의 경우 배열에 대해서 발송
			if(regidAndroid.size()>0){
				PushServiceAndroid pushServiceAndroid = new PushServiceAndroid();
				for(int m=0; m < regidAndroid.size(); m++){
					regidAndroidSeper.add(regidAndroid.get(m));
					if( (m > 0 && (m%500 == 0)) || (m == regidAndroid.size() - 1)){	//m 이 500번째라면
						pushServiceAndroid.pushGcm(regidAndroidSeper, index, ticker, title, msg,Constant.CATEGORY_TRAINING,result);	// boardSeqNo 아직 확실치 않기에 우선 1로 지정
						//pushServiceAndroid.pushGcm(regidAndroidSeper, index, ticker, title, msg);
						regidAndroidSeper.clear();
					}
				}
			}
		} */
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
	
	
	if(result > 0){ 
%>
		<script language=javascript>
			alert("등록되었습니다.");
			location.href = "event_list.jsp";
		</script>
<%
	}else{
%>
		<script language=javascript>
			alert("등록 실패했습니다."); 
			location.href = "event_list.jsp"; 
		</script>
<%
	}
%>
