<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="java.util.ArrayList" %>
<%@page import="util.StringUtil" %>
<%@page import="util.DateUtil" %>
<%@page import="java.net.URLDecoder" %>
<%@page import="dao.DepartDao" %>
<%@page import="vo.DepartVO" %>
<%@page import="util.Constant" %>
<%
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

    request.setCharacterEncoding("UTF-8");

    int affiliationSeq = Integer.parseInt(StringUtil.nchk(request.getParameter("affiliationSeq"), "0"));

    DepartDao dao = new DepartDao();
    ArrayList<DepartVO> list = dao.departListByAffiliation(affiliationSeq);
    dao.closeConn();
%>

<option value="0">부서 선택</option>
<%
    if (list.size() > 0) {
        for (int i = 0; i < list.size(); i++) {
            DepartVO vo = list.get(i);
%>
<option value="<%=vo.getDepartSeqNo()%>"><%=StringUtil.NVL(vo.getDepartName()) %>
</option>
<%
        }
    }
%>