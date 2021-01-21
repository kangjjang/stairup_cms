<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.DonationDao" %>
<%@page import="dao.ReviewDao" %>
<%@page import="vo.DonationVO" %>
<%@page import="vo.MemberVO" %>
<%@page import="java.util.ArrayList"%>
<% 
	String cntryCode = "KR";
	String langCode = "ko";
	String resultCode = ResultCode.RS_SUCCESS;
	String resultDesc = "success";
	JSONObject obj = new JSONObject();
	int memSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("memSeqNo"),"0"));
	int doSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("doSeqNo"),"1"));
	String endDate = URLDecoder.decode(StringUtil.nchk(request.getParameter("endDate"),""), "UTF-8");
	String startDate = URLDecoder.decode(StringUtil.nchk(request.getParameter("startDate"),""), "UTF-8");
	
	int pageNo = Integer.parseInt(StringUtil.nchk(request.getParameter("pageNo"),"1"));
	int rowSize = Integer.parseInt(StringUtil.nchk(request.getParameter("rowSize"),"5"));
	DonationDao dao = new DonationDao();
	DonationVO vo = dao.selectDonationView(doSeqNo);	//힘내라 기부 상세화면(제목, 내용, 사진)
	
	
	ArrayList<MemberVO> mvo = dao.selectGiveTop(endDate,startDate);		// 기부천사 top3
	ReviewDao rdao = new ReviewDao();
	ArrayList<MemberVO> rvo = rdao.selectReview(1,doSeqNo);		//댓글 리스트
	ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	ArrayList<JSONObject> list2 = new ArrayList<JSONObject>();
	if(vo.getTitle()!= null){
		//skhero.kang 2015-12-12 이미지 URL 잘못된 부분 수정
		obj.put("doPic", "upload/give/"+vo.getPic());
		obj.put("doThumbPic", "upload/Thumb/give/"+vo.getPic());
		/* obj.put("doPic", vo.getPic());
		obj.put("doThumbPic", vo.getPic()); */
		obj.put("doContent", vo.getContent());
		obj.put("doTitle", vo.getTitle());
		if(mvo!=null){
			for(int i = 0; i < mvo.size(); i++){
				JSONObject obj2 = new JSONObject();
				MemberVO angel = mvo.get(i);
				obj2.put("doAMemSeq", angel.getMemSeqNo()); 
				if(angel.getMemPic()!=null&&!angel.getMemPic().equals("")){
					obj2.put("doAMemPic", "upload/memberProfile/"+angel.getMemPic());
					obj2.put("doAMemThumbPic","upload/Thumb/memberProfile/"+angel.getMemPic());
				}
				
				obj2.put("doAMemName", angel.getMemName());
				obj2.put("doAMemDepart", angel.getMemDepart());
				list.add(i,obj2);
			}
		}
		if(rvo!=null){
			for(int i = 0; i < rvo.size(); i++){
				JSONObject obj3 = new JSONObject();
				MemberVO review = rvo.get(i);
				obj3.put("doMemSeqNo", review.getMemSeqNo()); 
				if(review.getMemPic()!=null&&!review.getMemPic().equals("")){
					obj3.put("doMemPic", "upload/memberProfile/"+review.getMemPic());
					obj3.put("doMemThumbPic", "upload/Thumb/memberProfile/"+review.getMemPic());
				}
				
				obj3.put("doMemName", review.getMemName());
				obj3.put("doContent", review.getReviewVo().getReviewContent());
				obj3.put("fcCrtDate", review.getReviewVo().getCrtDate());

				list2.add(i,obj3);
			}
		}
	}else{
		resultCode = "9999";
		resultDesc = "FAIL";
	}
	dao.closeConn();
	rdao.closeConn();
	
	obj.put("cntryCode",cntryCode);
	obj.put("langCode",langCode);
	obj.put("resultCode",resultCode);
	obj.put("resultDesc",resultDesc);
	if(mvo != null){
		obj.put("doAngel", list);
	}
	if(rvo != null){
		obj.put("doReviewList", list2);
	}

	out.print(obj);
	out.flush();
%> --%>