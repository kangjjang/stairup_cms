<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.util.ArrayList"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.MemberDao" %>
<%@page import="vo.MemberVO" %>
<%
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

	/*2015-05-13 ksy 회원정보 운동기록 상세화면
	주간 : 오늘을 기준으로 7일전까지의 기록을 일간 단위로 보여줌
	월간 : 오늘을 기준으로 7주전까지의 기록을 주간 단위로 보여줌
	년간 : 해달 달을 기준으로 뒤로 12달의 기록을 월간 단위로 보여줌
	*/
	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = "0000";
	String resultDesc = "success";
	JSONObject obj = new JSONObject();
	JSONObject obj2 = null;
	ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	ArrayList<JSONObject> list2 = new ArrayList<JSONObject>();
	int memSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("memSeqNo"),"0"));
	int rowCate = Integer.parseInt(StringUtil.nchk(request.getParameter("rowCate"),"1"));					// 1:주, 2:월, 3:년
	int pageNo = Integer.parseInt(StringUtil.nchk(request.getParameter("pageNo"),"1"));
	int rowSize = Integer.parseInt(StringUtil.nchk(request.getParameter("rowSize"),"0"));
	if(pageNo < 1) pageNo = 1;
	double life =0;
	int minutes =0;
	int seconds =0;
	int average=0;			//평균 걸음수
	int sum=0;				//총 걸음수
	String toLife="";
	MemberDao dao = new MemberDao();
	ArrayList<MemberVO> rvo = dao.selectHealthList(rowCate, memSeqNo);	//총 걸은층, 오른층, 내린층, 날짜
	if(rvo!=null){
		for(int i=0; i< rvo.size();i++){
			obj2 = new JSONObject();
			MemberVO rank = rvo.get(i);
			if(rowCate==2){							// 월간일경우에는 1주 , 2주 형식으로 표시하기 위해서 함
				if(i==0){
					obj2.put("crtDate", "현재주");	
				}else{
					obj2.put("crtDate", 7-i+"주");	
				}
			}else{
				obj2.put("crtDate", rank.getCrtDate());			//날짜표시
			}
			obj2.put("cntHealth", rank.getToday());			//총걸음수
			life += Double.valueOf((rank.getUpCnt() * 92)+(rank.getDownCnt()*30.7));		// 건강수명을 합하기 위해서 함
			list2.add(i,obj2);
			sum += rank.getToday();
		}
		////System.out.println("===============================================" );
		////System.out.println("life 시간 : " +life);
		////System.out.println("===============================================" );
		switch(rowCate){
		case 1:							// 주
			if(life != 0){
	    		/* int time =0; 
	    		time = (int)life;	// double 형으로 저장된 건강수명을 소수점을 버리고 int 형으로 저장
	    		minutes = time / 60;
	    		seconds = time % 60;
	    		toLife = String.format("%02d분%02d초", minutes, seconds); */
				int time =0; 
	    		time = (int)life;	// double 형으로 저장된 건강수명을 소수점을 버리고 int 형으로 저장
	    		int day = time / (60*60*24);
	    		int hour = (time - day*60*60*24)/(60*60);
	    		minutes = (time - day*60*60*24 -hour*3600)/60;
	    		seconds = time %60;
	    		toLife = String.format("%d일 %d시간 %d분",day,hour,minutes);
	    	}
			average = sum/7;
			break;
		case 2:							//월
			if(life != 0){
	    	/* 	int time =0; 
	    		time = (int)life;	// double 형으로 저장된 건강수명을 소수점을 버리고 int 형으로 저장
	    		minutes = time / 60;
	    		seconds = time % 60;
	    		toLife = String.format("%02d분%02d초", minutes, seconds); */
				int time =0; 
	    		time = (int)life;	// double 형으로 저장된 건강수명을 소수점을 버리고 int 형으로 저장
	    		int day = time / (60*60*24);
	    		int hour = (time - day*60*60*24)/(60*60);
	    		minutes = (time - day*60*60*24 -hour*3600)/60;
	    		seconds = time %60;
	    		toLife = String.format("%d일 %d시간 %d분",day,hour,minutes);
	    	}
			average = sum/7;
			break;
		case 3:						//년
			if(life != 0){
	    		int time =0; 
	    		time = (int)life;	// double 형으로 저장된 건강수명을 소수점을 버리고 int 형으로 저장
	    		int day = time / (60*60*24);
	    		int hour = (time - day*60*60*24)/(60*60);
	    		minutes = (time - day*60*60*24 -hour*3600)/60;
	    		seconds = time %60;
	    		toLife = String.format("%d일 %d시간 %d분",day,hour,minutes);
	    	}
			average = sum/12;
			break;
	/* 	case 4:					//년
			if(life != 0){
	    		int time =0; 
	    		time = (int)life;	// double 형으로 저장된 건강수명을 소수점을 버리고 int 형으로 저장
	    		minutes = time / (60*60*24);
	    		toLife = String.format("건강수명 %d일 증가", minutes);
	    	}
			break; */
		}
	}else{
		resultCode = "9999";
		resultDesc = "FAIL";
	}

	obj.put("cntryCode",cntryCode);
	obj.put("langCode",langCode);
	obj.put("resultCode",resultCode);
	obj.put("resultDesc",resultDesc);
	obj.put("average", average);
	if(life != 0){
		obj.put("healthLife",toLife);
	}

	if(rvo!=null){
		obj.put("healthList", list2);
	}
	out.print(obj);
	out.flush();
	
	dao.closeConn();
	
%>