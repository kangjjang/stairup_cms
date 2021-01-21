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
    int result = 0;
    int maxSize = Constant.MAX_SIZE;
    int no = 0;

    BeaconEventDao dao = new BeaconEventDao();

    try {

        String uploadFolder = "beacon/";
        String uploadRealPath = request.getSession().getServletContext().getRealPath("/upload/") + "/";
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

        no = Integer.parseInt(StringUtil.nchk(mul.getParameter("no"),"0"));
        int stampEventTarget = Integer.parseInt(StringUtil.nchk(mul.getParameter("stampEventTarget"),"0"));
        String stampEventName = StringUtil.nchk(mul.getParameter("stampEventName"), "");
        String stampEventRegion = StringUtil.nchk(mul.getParameter("stampEventRegion"), "");
        String stampEventAddress = StringUtil.nchk(mul.getParameter("stampEventAddress"), "");
        String stampEventCourseDistance = StringUtil.nchk(mul.getParameter("stampEventCourseDistance"), "");
        String stampEventCourseTime = StringUtil.nchk(mul.getParameter("stampEventCourseTime"), "");
        String stampEventStartDate = StringUtil.nchk(mul.getParameter("stampEventStartDate"), "");
        String stampEventEndDate = StringUtil.nchk(mul.getParameter("stampEventEndDate"), "");
        String stampBeaconMajor = StringUtil.nchk(mul.getParameter("stampBeaconMajor"), "");
        String stampBeaconMinor = StringUtil.nchk(mul.getParameter("stampBeaconMinor"), "");

        String stampEventImage= "";
        String stampEventCourseImage= "";

        f = mul.getFile("stampEventImage");
        if (f !=null){
            stampEventImage = StringUtil.nchk(f.getName(), "");
        }

        f = mul.getFile("stampEventCourseImage");
        if (f !=null){
            stampEventCourseImage = StringUtil.nchk(f.getName(), "");
        }

        //skhero.kang 2019-04-05 스탬프 이벤트 등록
        int stampEventSeq = dao.updateStampEvent(no, stampEventTarget, stampEventName, stampEventRegion, stampEventAddress, stampEventCourseDistance, stampEventCourseTime,
                stampEventStartDate, stampEventEndDate, stampEventImage, stampEventCourseImage);


        //skhero.kang 2019-04-05 기존 스탬프 비콘 삭제하기
        dao.deleteStampEventBeacon(no);

        //skhero.kang 2019-04-05 스탬프 비콘 등록하기
        if(stampBeaconMinor != null && stampBeaconMinor.length() > 0){

            String[] arrStampBeaconMinor = stampBeaconMinor.split(",");

            for(int i = 0; i < arrStampBeaconMinor.length; i++){
                result = dao.insertStampEventBeacon(stampEventSeq, Integer.parseInt(stampBeaconMajor), Integer.parseInt(arrStampBeaconMinor[i]));
            }

        }
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
	<script type="text/javascript">
			alert("수정되었습니다.");
			location.href = "stamp_beacon_view.jsp?no=<%=no%>";
		</script>
<%
	}else{
%>
		<script type="text/javascript">
			alert("수정에 실패했습니다.");
			location.href = "stamp_beacon_list.jsp";
		</script>
<%
	}
%>