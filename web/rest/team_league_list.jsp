<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.DepartDao" %>
<%@page import="vo.DepartVO" %>
<%
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	response.setHeader("Access-Control-Max-Age", "3600");
	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = "0000";
	String resultDesc = "success";
	JSONObject obj = new JSONObject();
	int pageNo = Integer.parseInt(StringUtil.nchk(request.getParameter("pageNo"),"1"));
	if (pageNo < 1) pageNo = 1;
	int rowSize = Integer.parseInt(StringUtil.nchk(request.getParameter("rowSize"),"6"));
	int memSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("memSeqNo"),"6"));
	int rowCate = Integer.parseInt(StringUtil.nchk(request.getParameter("rowCate"),"6"));
	int homeWalkCnt= 0;			//홈팀 걸음수
	int awayWalkCnt= 0;			//어웨이팀 걸음수
	int teamWin=0;				//홈팀을 기준으로 1:win, 2:lost, 3:draw
	DepartDao dao = new DepartDao();
	ArrayList<DepartVO> vo = dao.selectTeamLeagueList(memSeqNo, rowCate, pageNo, rowSize);			// rowCate 1 : live , 2: 지난주 리스트 불러옴
/* 	dao = new DepartDao();
	DepartVO myvo = dao.selectMyTeamLeague(memSeqNo, rowCate);  */
	ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	if(vo!=null){
		for(int i = 0; i< vo.size(); i++){
			JSONObject obj2 = new JSONObject();
			DepartVO team = vo.get(i);	
			obj2.put("homeName", team.getHomeName());
			obj2.put("homePic", "upload/depart/"+team.getHomePic());
			obj2.put("homeThumbPic", "upload/Thumb/depart/"+team.getHomePic());
			obj2.put("homeSeq", team.getHomeSeqNo());
			obj2.put("awayName", team.getAwayName());
			obj2.put("awayPic", "upload/depart/"+team.getAwayPic());
			obj2.put("awayThumbPic", "upload/Thumb/depart/"+team.getAwayPic());
			obj2.put("awaySeq",team.getAwaySeqNo());
			obj2.put("crtDate", team.getCrtDate());
			obj2.put("endDate",team.getEndDate());
			obj2.put("leagueSeq",team.getLeagueSeqNo());
			if(rowCate ==2){					// 지난주 경기
				homeWalkCnt = (team.getHomeWalkCnt()*team.getHomeWalkCnt())/team.getAwayDepartPeople();
				awayWalkCnt = (team.getAwayWalkCnt()*team.getAwayWalkCnt())/team.getAwayDepartPeople();
				if(homeWalkCnt > awayWalkCnt){								// 홈팀을 기준으로 홈팀이 이겻을 경우 는 1을 반환
					teamWin =1;
				}else if(homeWalkCnt < awayWalkCnt){						// 홈팀을 기준으로 홈팀이 졋을 경우는 2를 반환
					teamWin = 2;
				}else if(homeWalkCnt == awayWalkCnt){						// 무승부일경우 3을 반환
					teamWin = 3;
				}else{														// 기타 다른 상황일 경우 -1을 반환
					teamWin = -1;
				}
				obj2.put("homeWalkCnt",homeWalkCnt);
				obj2.put("awayWalkCnt", awayWalkCnt);
				obj2.put("teamWin", teamWin);
			}
			list.add(i,obj2);
		}

	}else{
		resultCode = "9999";
		resultDesc = "FAIL";
	}
	
	obj.put("cntryCode",cntryCode);
	obj.put("langCode",langCode);
	obj.put("resultCode",resultCode);
	obj.put("resultDesc",resultDesc);
	obj.put("teamList",list);
	out.print(obj);
	out.flush();
	
	dao.closeConn();
	
%>
