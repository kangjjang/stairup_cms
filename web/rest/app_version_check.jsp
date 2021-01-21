<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.json.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLDecoder"%>    
<%@page import="util.StringUtil"%>
<%@page import="util.HashUtil"%>
<%@page import="util.ResultCode"%>
<%@page import="dao.DeviceDao" %>
<%
    request.setCharacterEncoding("UTF-8");

    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

    String cntryCode = "KR";
    String langCode = "ko";
    String resultCode = ResultCode.RS_SUCCESS;
    String resultDesc = "success";

    String deviceGbn = StringUtil.nchk(request.getParameter("deviceGbn"),"A");

    DeviceDao dao = new DeviceDao();
    String AppVersion = dao.selectAppVersion(deviceGbn);

    JSONObject obj = new JSONObject();
    obj.put("cntryCode",cntryCode);
    obj.put("langCode",langCode);
    obj.put("resultCode",resultCode);
    obj.put("resultDesc",resultDesc);
    obj.put("appVersion",AppVersion);

    out.print(obj);
    out.flush();

    dao.closeConn();
	
%>
