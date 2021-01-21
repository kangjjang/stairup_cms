<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.MemberDao" %>
<%@page import="dao.FightDao" %>
<%@page import="dao.ReviewDao" %>
<%@page import="dao.CityDao" %>
<%@page import="vo.EventVO" %>
<%@page import="vo.MemberVO" %>
<%@page import="vo.CityVO" %>
<%
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = "0000";
	String resultDesc = "success";
	ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	ArrayList<JSONObject> list2 = new ArrayList<JSONObject>();
	ArrayList<JSONObject> list3 = new ArrayList<JSONObject>();
	ArrayList<JSONObject> list4 = new ArrayList<JSONObject>();
	JSONObject obj = new JSONObject();
	int memSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("memSeqNo"),"1"));							//회원 seq
	int myMemSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("myMemSeqNo"),"1"));						//나의 seq
	int result = 0;
	int limitGubun = 1;  // 메세지 리미트 3
	
	MemberDao dao = new MemberDao();
	MemberVO vo = dao.selectMemberInfoBasic(memSeqNo,myMemSeqNo);	//회원번호, 부서, 회원이름, 오른층, 내려간층, 회원사진, 운동기록(일,주,월,년),총 힘내요개수
	FightDao fdao = new FightDao();
	ArrayList<MemberVO> fvo = fdao.selectFight(2,memSeqNo,1,4); //힘내요 마지막 4명
	ReviewDao rdao = new ReviewDao();
	ArrayList<MemberVO> rvo = rdao.selectReview(2, memSeqNo, limitGubun); //메세지 마지막 3명
	CityDao cdao = new CityDao();
	ArrayList<CityVO> cvo = cdao.selectWorldList(memSeqNo,1,3);	//세계일주
	//cdao = new CityDao();
	ArrayList<CityVO> mvo = cdao.selectMayorList(memSeqNo,1,3);	//내가 시장인 도시
	
	int kCal =(int)((vo.getUpCnt() * 3.45)+(vo.getDownCnt()*1.15));
	double life = Double.valueOf((vo.getLifeUp() * 92)+(vo.getLifeDown()*30.7));
	/* double lifeDou = Double.valueOf(life).doubleValue(); */	//계산된 건강수명을 시간으로 계산하기 위하여 double 형으로 저장
	String toLife ="";
	int minutes =0;
	int seconds =0;
	if(life != 0){
		/* int time =0; 
		time = (int)lifeDou;	// double 형으로 저장된 건강수명을 소수점을 버리고 int 형으로 저장
		minutes = time / 60; */
		int time =0; 
		time = (int)life;	// double 형으로 저장된 건강수명을 소수점을 버리고 int 형으로 저장
		int day = time / (60*60*24);
		int houra = (time - day*60*60*24)/(60*60);
		minutes = (time - day*60*60*24 -houra*3600)/60;
		seconds = time %60;
		toLife = String.format("%d일 %d시간 %d분",day,houra,minutes);
		/* seconds = time % 60;
		toLife = String.format("%02d분%02d초", minutes, seconds); */
	}
	
	if(vo != null && vo.getMemSeqNo() > 0){
		obj.put("memSeqNo", vo.getMemSeqNo());
		obj.put("memNumber", vo.getMemNumber());
		obj.put("memDepart", vo.getMemDepart());
		obj.put("memAffiliation", vo.getMemAffiliationName());
		obj.put("memName", vo.getMemName());
		obj.put("memKcal", kCal);
		obj.put("memLife", toLife);
		if(vo.getMemPic()!=null&&!vo.getMemPic().equals("")){
			obj.put("memPic", "upload/memberProfile/"+vo.getMemPic());
			obj.put("memThumbPic","upload/Thumb/memberProfile/"+vo.getMemPic());
		}
		obj.put("totalfight", vo.getFightCnt());
		obj.put("toDay", vo.getToday()); 
		obj.put("weekDay", vo.getWeek());
		obj.put("monDay", vo.getMon());
		obj.put("yearDay", vo.getYear());
		obj.put("frCnt", vo.getFrcnt());
		obj.put("likeSelect", vo.getLikeSelect());
		/*세계일주 리스트*/
		if(cvo!= null){
			for(int i = 0; i<cvo.size(); i++){
				JSONObject obj2 = new JSONObject();
				CityVO world = cvo.get(i);	
				obj2.put("worldNo", i+1+"번째");
				obj2.put("worldDay", world.getCityDay());
				list.add(i,obj2);
			}
		}
		/*내가 시장인 도시 리스트*/
		if(mvo!=null){
			for(int i = 0; i<mvo.size(); i++){
				JSONObject obj3 = new JSONObject();
				CityVO mayor = mvo.get(i);	
				obj3.put("cityName", mayor.getCityName());
				obj3.put("conPic", "upload/country/"+mayor.getCountryPic());
				list2.add(i,obj3);
			}
		}
		/*힘내요 리시트*/
		if(fvo!=null){
			for(int i = 0; i <fvo.size(); i++){
				JSONObject obj4 = new JSONObject();
				MemberVO fight = fvo.get(i);	
				obj4.put("fMemName", fight.getMemName());
				if(fight.getMemPic()!=null&&!fight.getMemPic().equals("")){
					obj4.put("fMemPic", "upload/memberProfile/"+fight.getMemPic());
					obj4.put("fMemThumbPic", "upload/Thumb/memberProfile/"+fight.getMemPic());
				}
				obj4.put("fMemSeqNo", fight.getMemSeqNo());
				obj4.put("fCrtDate", fight.getCrtDate());
				list3.add(i,obj4);
			}
		}
		/*메세지 리스트*/
		if(rvo!=null){
			for(int i= 0; i < rvo.size(); i++){
				JSONObject obj5 = new JSONObject();
				MemberVO review = rvo.get(i);	
				if(review.getMemPic()!=null&&!review.getMemPic().equals("")){
					obj5.put("fcMemPic","upload/memberProfile/"+review.getMemPic());
					obj5.put("fcMemThumbPic", "upload/Thumb/memberProfile/"+review.getMemPic());
				}
				obj5.put("fcMemName", review.getMemName());
				obj5.put("fcMemContent", review.getReviewVo().getReviewContent());
				obj5.put("fcMemSeqNo",review.getMemSeqNo());
				obj5.put("fcCrtDate", review.getReviewVo().getCrtDate());
				obj5.put("fcReviewSeqNo", review.getReviewVo().getReviewSeqNo());
				list4.add(i,obj5);
			}
		}
	}else{
		resultCode = "9999";
		resultDesc = "FAIL";
	}
	
	obj.put("cntryCode",cntryCode);
	obj.put("langCode",langCode);
	obj.put("resultCode",resultCode);
	obj.put("resultDesc",resultDesc);
	
	if(vo!=null){
		obj.put("world", list);
		obj.put("mayor", list2);
		obj.put("fight", list3);
		obj.put("fightContent", list4);
	}
	
	out.print(obj);
	out.flush();
	
	dao.closeConn();
	fdao.closeConn();
	rdao.closeConn();
	cdao.closeConn();
	
%>
