<%@ page contentType="text/html; charset=utf-8" %>
<%@page import="util.PageUtil"%>
<%@page import="util.StringUtil" %>
<%
	response.setHeader("cache-control","no-cache"); 
	response.setHeader("expires","0");
	response.setHeader("pragma","no-cache"); 

	request.setCharacterEncoding("UTF-8");
%>
<%			
	/**
	*기본적으로 넘어와야 되는 변수
	* page번호 및 pageView, pageGroup, totalRecord	
	* @currentPage	 현재 페이지 번호   
	* @pageSize	한페이지에 나타나는 글목록수
	* @blockPage 한페이지에 보여주어야 하는 페이지수	
	* @totalRecord 총 글목록수	
	*/
	
	int currentPage		= 0; 
	int pageSize		= 0; 
	int blockPage		= 0; 
	int totalRecord		= 0; 
	
	//String actionPage	= null;  //링크되는 url	

	/**
	* 페이지를 위해 사용되는 변수선언
	* @totalPage	: 전체페이지 갯수
	* @startViewPage : 시작 페이지번호
	* @endViewPage : 마지막 페이지번호
	*/
	int totalPage		= 0;
	int startViewPage 	= 0;
	int endViewPage 	= 0;
	
	/**
	* 현재 페이지 request
	*/
	if(request.getParameter("pageNo")!=null){
		currentPage	= Integer.parseInt(request.getParameter("pageNo"));
	}else{
		currentPage	= 1;
	}

	/**
	* 글수/페이지수 request
	*/
	if(request.getParameter("rowCount")!=null){
		pageSize	= Integer.parseInt(request.getParameter("rowCount"));
	}else{
		pageSize	= 1; //default 처리
	}
		
	/**
	* 페이지/그룹 request	
	*/
	if(request.getParameter("pageGroup")!=null){
		blockPage	= Integer.parseInt(request.getParameter("pageGroup"));
	}else{
		blockPage	= 1; //default 처리
	}
		
	/**
	* 총레코드수
	*/
	if(request.getParameter("totalRecord")!=null){
		totalRecord	= Integer.parseInt(request.getParameter("totalRecord"));
	}else{
		totalRecord	= 1; //default 처리
	}		
		
	/**
	* com.rush.comm.PageUtil class 객체 사용 시작페이지와 마감페이지를 가져온다 
	*/
	
	PageUtil pageUtil = new PageUtil(pageSize, blockPage, currentPage, totalRecord);	
	pageUtil.pageViewProcess();
	startViewPage		= pageUtil.getStartViewPage(); 
	endViewPage			= pageUtil.getEndViewPage();
	totalPage			= pageUtil.getTotalPage();
	
	if (totalPage<endViewPage){
		endViewPage=totalPage;
	}
	
%>
		<div class="ks-pagination-links" align="center">
		<ul>
<%	
	/*
	* 현재페이지번호가 한페이지에 보여주어야 하는 페이지수 보다 클경우 나타내줄부분 
	*/
	if(blockPage < currentPage){
%>
			<li class="previous"><a href="javascript:pageLink('<%=startViewPage-1%>');">&lt; previous</a></li>
<%
	}
%>
<%
	/*
	* 한그룹에 보여질 페이지 부분
	*/
	for(int index=startViewPage;index<=endViewPage;index++){
		if (index==currentPage){
%>					
				<li class="current"><%=index%></li>
<%				
		} else {
%>			
				<li><a href="javascript:pageLink('<%=index%>');"><%=index%></a></li>
<%				
		}
		if(index<endViewPage) {
			out.println(" ");
		}
	}
%>
<%
	/*
	* 마지막 페이지가 토탈페이지보다 작을경우 보여지는 부분
	*/	
	if (totalPage>endViewPage){		
%>
			<li class="next"><a href="javascript:pageLink(<%=endViewPage+1%>);">Next &gt;</a></li>
<%				
	}	
%>
		</ul>
		</div>