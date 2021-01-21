<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.util.ArrayList"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.MemberDao" %>
<%@page import="dao.CityDao" %>
<%@page import="vo.MemberVO" %>
<%@page import="vo.CityVO" %>
<%@page import = "java.util.*"%>
<%
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = ResultCode.RS_SUCCESS;
	String resultDesc = "success";
	
	Calendar today = Calendar.getInstance(); 
	int hour = today.get(Calendar.HOUR);
	
	int memSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("memSeqNo"),"0"));
	int memAffiliation = Integer.parseInt(StringUtil.nchk(request.getParameter("groupSeq"),"0"));	//부서 seq
	int percent = 0;
	int nFloor = 0;
	String gubunPic = "";
	String memResult ="";
	
	////System.out.println("퍼스트 로그인 ");
	////System.out.println("memSeqNo시작하기 : " +memSeqNo);
	////System.out.println("memAffiliation : "+memAffiliation);

	JSONObject obj = new JSONObject();
	JSONObject obj2 = null;
	JSONObject obj3 = null;
	ArrayList<JSONObject> list2 = new ArrayList<JSONObject>();
	ArrayList<JSONObject> jsonList = new ArrayList<JSONObject>();
    MemberDao dao = new MemberDao();
    CityDao cdao = new CityDao();
    
    //MemberVO vo = dao.memberInfo(memSeqNo, memAffiliation);	//회원번호, 부서, 회원이름, 오른층, 내려간층, 랭킹, 회원사진, 도시seq
    MemberVO vo2 = dao.jehozzang(memSeqNo);	//총 오름수 , 상위 몇퍼센트 인지    
    MemberVO vo3 = dao.mainMemberInfo(memSeqNo);		//회원 정보
    MemberVO vo4 = dao.mainMemberCityInfo(memSeqNo);					//회원 도시정보
    MemberVO vo5 = dao.mainMemberStairInfo(memSeqNo, memAffiliation);	//회원 계단정보
    
    double ratio1 = Double.valueOf(vo2.getRank()*1.0/vo2.getCnt()*100);
    int ratio = (int)ratio1;   //회원이 상위 몇 퍼센트 인지
    obj.put("ratio", ratio);		//상위 %
    obj.put("totalStair", vo2.getToday());		//누적 걸음수
    
    if(vo3!=null&&vo3.getMemResult().equals("N")){
    	obj2 = new JSONObject();
    	//dao = new MemberDao();
    	MemberVO mvo = dao.cityMayorMem(vo4.getCityVo().getCitySeqNo(), memAffiliation);			//해당 도시의 시장 정보
    	//dao = new MemberDao();
    	ArrayList<MemberVO> list = dao.cityMemberList(vo4.getCityVo().getCitySeqNo(), memAffiliation, memSeqNo);		//해당 도시에 있는 유저 리스트
    	if(hour > 18){
    		gubunPic = "upload/city/"+vo4.getCityVo().getCityNPic();
    	}else{
    		gubunPic = "upload/city/"+vo4.getCityVo().getCityPic();
    	}
    	int kCal =(int)((vo5.getUpCnt() * 3.45)+(vo5.getDownCnt()*1.15));
    	double life = Double.valueOf((vo5.getLifeUp() * 92)+(vo5.getLifeDown()*30.7));
    	/* double lifeDou = Double.valueOf(life).doubleValue(); */	//계산된 건강수명을 시간으로 계산하기 위하여 double 형으로 저장
    	String toLife ="";
    	int minutes =0;
    	int seconds =0;
    	if(life != 0){
    		/* int time =0; 
    		time = (int)lifeDou;	// double 형으로 저장된 건강수명을 소수점을 버리고 int 형으로 저장
    		minutes = time/ 60; */
    		int time =0; 
    		time = (int)life;	// double 형으로 저장된 건강수명을 소수점을 버리고 int 형으로 저장
    		int day = time / (60*60*24);
    		int houra = (time - day*60*60*24)/(60*60);
    		minutes = (time - day*60*60*24 -houra*3600)/60;
    		seconds = time %60;
    		toLife = String.format("%d일 %d시간 %d분",day,houra,minutes);
    		/* seconds = time % 60;
    		toLife = String.format("%02d분", minutes, seconds); */
     	}
    	obj2.put("memSeqNo",vo3.getMemSeqNo());
    	obj2.put("memDepart", vo3.getMemDepart());
    	obj2.put("departSeq", vo3.getDepartSeq());
    	obj2.put("memAffiliation", vo3.getMemAffiliationName());
    	obj2.put("memName", vo3.getMemName());
    	if(vo3.getMemPic()!=null&&!vo3.getMemPic().equals("")){
    		obj2.put("memPic", "upload/memberProfile/"+vo3.getMemPic());
        	obj2.put("memThumbPic","upload/Thumb/memberProfile/"+vo3.getMemPic());
    	}
    	obj2.put("citySeqNo", vo4.getCityVo().getCitySeqNo());
    	obj2.put("cityPic",gubunPic);
    	obj2.put("cityNum", vo4.getCityVo().getOrderLy());
    	obj2.put("countryName", vo4.getCityVo().getCountryName());
    	obj2.put("countryPic", "upload/country/"+vo4.getCityVo().getCountryPic());
    	obj2.put("cityName", vo4.getCityVo().getCityName());
    	obj2.put("memKcal",kCal);
    	obj2.put("memLife",toLife);
    	obj2.put("toDayFloor", vo5.getDownCnt()+vo5.getUpCnt());
    	obj2.put("toDayRank", vo5.getRank());
    	
    	obj2.put("mayorName", mvo.getMemName());
    	obj2.put("mayorSeqNo", mvo.getMemSeqNo());
    	obj2.put("mayorDay", mvo.getCityVo().getCityDay());
    	obj2.put("mayorContent", mvo.getCityVo().getContent());
    	if(mvo.getMemPic()!=null&&!mvo.getMemPic().equals("")){
    		obj2.put("mayorPic", "upload/memberProfile/"+mvo.getMemPic());
        	obj2.put("mayorThumbPic", "upload/Thumb/memberProfile/"+mvo.getMemPic());
    	}
    	if(list.size()>0){
    		for(int i =0; i < list.size(); i++){ 
        		obj3 = new JSONObject();
        		 MemberVO mber = list.get(i);	
     			int memSeq = mber.getMemSeqNo();
     			obj3.put("uMemSeqNo", memSeq); 
     			if(mber.getMemPic()!=null&&!mber.getMemPic().equals("")){
     				obj3.put("uMemPic", "upload/memberProfile/"+mber.getMemPic());
         			obj3.put("uMemThumbPic", "upload/Thumb/memberProfile/"+mber.getMemPic());
     			}
     			list2.add(i,obj3);
        	}
    	}else{
    		obj3 = new JSONObject();
    		obj3.put("uMemSeqNo",vo3.getMemSeqNo());
    		if(vo3.getMemPic()!=null && !vo3.getMemPic().equals("")){
    			obj3.put("uMemPic","upload/memberProfile/"+vo3.getMemPic());
        		obj3.put("uMemThumbPic","upload/Thumb/memberProfile/"+vo3.getMemPic());
    		}
    		list2.add(0,obj3);
    	}
    	
    	/*===================================================다음 도시 정보======================================================================*/
    	
    	CityVO cvo = cdao.selectMemCityInfo(memSeqNo, vo4.getCityVo().getFstartDate());		//클리어 도시, 클리어 국가, 총 국가, 총 도시   
    	CityVO civo = cdao.selectNextCity(memSeqNo, vo4.getCityVo().getOrderLy());			//다음도시 정보를 가져오기 위한 것
    	
    	////System.out.println("내가 현재 머물고 있는 도시 순서 = "+ vo4.getCityVo().getOrderLy());
    	////System.out.println("civo.getCityComent() 다음도시 코멘트 = "+ civo.getCityComent());
    	////System.out.println("civo.getCityName() 다음 도시 이름 = "+ civo.getCityName());
    	
    	ArrayList<CityVO> lvo = cdao.selectClearCityList(memSeqNo, vo4.getCityVo().getFstartDate());	//거처온 국가
    	if(cvo!=null){
    		if(cvo.getClearCity() >0)percent = (int)(((double)(cvo.getClearCity()) / (double)(cvo.getTotalCity()))*100);	//(다녀온도시 - 총도시)*100 
    		
    		nFloor = civo.getTotalStair() - (civo.getTotalWork() - (civo.getAllStair() * vo5.getWorldCnt()));		//지금까지온 도시의 목표계단 총수 - 현재까지의 나의 총 층수
    		
    		/* ////System.out.println("nFloor = "+ nFloor);
    		////System.out.println("civo.getTotalStair()다음 순번까지의 모든 도시 층 = "+ civo.getTotalStair());
    		////System.out.println("civo.getTotalWork() 회원의 총 걸음 수  = "+ civo.getTotalWork());
    		////System.out.println("civo.getAllStair() 세계일주 하는데 필요한 계단 수 = "+ civo.getAllStair());
    		////System.out.println("vo.getWorldCnt() 세계 일주 횟수 = "+ vo.getWorldCnt()); */
    		
    		obj.put("countWorld", cvo.getClearCountry());		//거처간 국가
    		obj.put("countCity", cvo.getClearCity());			//거처간 나라
    		obj.put("percent", percent);						//세계일주 진행률
    		obj.put("totalCountry", cvo.getTotalCountry());		//총 국가 수
    		obj.put("totalCity", cvo.getTotalCity());			//총 도시 수
    		if(civo.getCountryName()!=null){
    			obj.put("nCountry", civo.getCountryName());			//다음 국가
        		obj.put("nCity", civo.getCityComent()+" "+civo.getCityName());				//다음 도시
        		obj.put("nCountryPic", "upload/country/"+civo.getCountryPic());
    		}else{
    			
    			CityVO ncity = cdao.selectfirstCity();
    			obj.put("nCountry", ncity.getCountryName());			//다음 국가
        		obj.put("nCity", ncity.getCityName());				//다음 도시
        		obj.put("nCountryPic", "upload/country/"+ncity.getCountryPic());
    		}
    		
    		obj.put("nFloor",nFloor);							//남은층
    		if(lvo!=null){
    			for(int i= 0; i < lvo.size()-1; i++){	//마지막은 현재 진행중인것으로 -1을 한다.
    				JSONObject obj4 = new JSONObject();
    				CityVO citylist = lvo.get(i);
    				obj4.put("countName", citylist.getCountryName());		//국가 이름
    				obj4.put("countSeqNo", citylist.getCountrySeqNo());		//국가 seq
    				
    				jsonList.add(i,obj4);
    			}
    		}
    	}
    }else{
    	resultCode = "9999";
    	resultDesc = "없는 회원 입니다.";
    }
  
    obj.put("cntryCode",cntryCode);
	obj.put("langCode",langCode);
	obj.put("resultCode",resultCode);
	obj.put("resultDesc",resultDesc);
	obj.put("memInfo", obj2);
	obj.put("memList",list2);
	obj.put("countryList",jsonList);
	out.print(obj);
	out.flush();
	
	dao.closeConn();
    cdao.closeConn();
	
%>
