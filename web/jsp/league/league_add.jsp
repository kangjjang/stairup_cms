<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="../include/inc_head.jsp" %>
<%@page import="java.io.*"%>
<%@page import="java.io.File"%>
<%@page import="dao.DepartDao"%>
<%@page import="vo.DepartVO" %>
<%@page import="util.StringUtil"%>
<%@page import="util.Constant"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.*" %>
<%
	/*
	2015-05-14 ksy 부서 경기 조인
	*/
	
	int result = 0;
	int departCnt = 0;
	int toWeek = 0;
	String id = cookieBox.getValue("ID");
	String realPath = uploadFoler;
	int memSeqNo = 0;
	memSeqNo = Constant.ADMIN_MEM_SEQ;
	ArrayList<DepartVO> list=null;
	DepartDao dao = new DepartDao();
	
	//	try {
try{
	
	toWeek = dao.checktoWeekLeague();			//이번주에 등록한 리그가 있는지 확인
		if(toWeek<1){
			//dao = new DepartDao();
			departCnt = dao.selectDepartVi();					//활성화된 부서 개수 가져오기
			Calendar today = Calendar.getInstance(); 
			int check = departCnt % 2;
			int day = today.get(Calendar.DAY_OF_WEEK);
			int plus =0;
			int a=0;
			switch(day){
			case 1:					//일
				plus = 0;
				break;
			case 2:					//월
				plus = 6;
				break;
			case 3:					//화
				plus = 5;
				break;
			case 4:					//수
				plus = 4;
				break;
			case 5:					//목
				plus = 3;
				break;
			case 6:					//금
				plus =2;
				break;
			case 7:					//토
				plus =1;
				break;
			}
			if(departCnt>1){
				if(check==0){	//활성화된 부서의 개수가 짝수일 경우
					//dao = new DepartDao();
					list = dao.selectTeamList(0);			//0은 의미 없는 값
				}else{	//활성화된 부서의 개수가 홀수일 경우
					//dao = new DepartDao();
					list = dao.selectTeamList(1);			//1은 gubun값으로 이노베이션 팀 제외하여 짝수 팀을 만듬
				}
				for(int i = 0; i < list.size(); i++){
					DepartVO tim = list.get(i);
					if((i % 2)==1){	//0,2,4 ..... 때 입력
						//dao = new DepartDao();
						result = dao.insertTim(a, tim.getDepartSeqNo(),plus);
					}else{	//1,3,5 ... 때의 seq 값을 저장
						a =tim.getDepartSeqNo();
					}
					
				}
			}
			/*==================== 지난주 경기 결과 저장 ==============================*/
			//dao = new DepartDao();
			ArrayList<DepartVO> lastWeekList = dao.selectCMSLeaguReulst();
			if(lastWeekList !=null){
				for(int i =0; i < lastWeekList.size(); i++){
					DepartVO lvo = lastWeekList.get(i);
					int homeCnt = (lvo.getHomeWalkCnt()*lvo.getHomeWalkCnt())/lvo.getDepartPeople();
					int awayCnt = (lvo.getAwayWalkCnt()*lvo.getAwayWalkCnt())/lvo.getAwayDepartPeople();
					if(homeCnt > awayCnt){
						//dao = new DepartDao();
						dao.departResultUpdate(1, lvo.getHomeSeqNo());
						//dao = new DepartDao();
						dao.departResultUpdate(2, lvo.getAwaySeqNo());
					}else if(homeCnt < awayCnt){
						//dao = new DepartDao();
						dao.departResultUpdate(2, lvo.getHomeSeqNo());
						//dao = new DepartDao();
						dao.departResultUpdate(1, lvo.getAwaySeqNo());
					}else if(homeCnt == awayCnt){
						//dao = new DepartDao();
						dao.departResultUpdate(3, lvo.getHomeSeqNo());
						//dao = new DepartDao();
						dao.departResultUpdate(3, lvo.getAwaySeqNo());
					}else{
						;
					}
				}
			}
		}
	}catch(Exception ex){
		System.out.println(ex);
	}finally{
		dao.closeConn();
	}
if(toWeek>1){
	%>
	<script language=javascript>
			alert("이번주 리그는 이미 등록 되었습니다.");
			location.href = "league_list.jsp";
		</script>
	<%
	}else{

	if (result > 0) {
%>
		<script language=javascript>
			alert("등록되었습니다.");
			location.href = "league_list.jsp";
		</script>
<%
	} else if(departCnt == 1) {
%>
		<script language=javascript>
			alert("활성화된 부서가 하나밖에 없습니다.");
			location.href = "league_list.jsp";
		</script>
<%
	} else {
%>
	<script language=javascript>
			alert("활성화된 부서가 없습니다.");
			location.href = "league_list.jsp";
		</script>
<%
	}
}
%>
