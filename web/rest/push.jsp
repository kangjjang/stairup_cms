<%@page import="util.Constant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="service.FcmPushService"%>
<%
request.setCharacterEncoding("UTF-8");

String title = "stairup";
String msg = "app awake";
String topic = "/topics/pushToAll";
int msgFlag = Constant.PUSH_AWAKE; //awake

////System.out.println("#############pushawake");

FcmPushService.pushFCMNotification(title, msg, topic, msgFlag);
%>
