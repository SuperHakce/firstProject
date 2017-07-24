<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>删除学历信息</title>
</head>
<body>
<%  String historyId = request.getParameter("historyId").toString();
    String userId = request.getParameter("userId").toString();
    String userName = request.getParameter("userName").toString();
    String schoolName = request.getParameter("schoolName").toString();
    String startDate = request.getParameter("startDate").toString();
    String endDate = request.getParameter("endDate").toString();
%>
用户学历信息删除：
<form action="<c:url value="/deleteHistory.html" />" method="post">
    <table border="1px" width="35%">
        <tr>
            <td width="20%">学历ID</td>
            <td width="80%"><input type="text" name="historyId" value="<%=historyId%>"/></td>
        </tr>
        <tr>
            <td width="20%">用户ID</td>
            <td width="80%"><input type="text" name="userId" value="<%=userId%>"/></td>
        </tr>
        <tr>
            <td width="20%">用户姓名</td>
            <td width="80%"><input type="text" name="userName" value="<%=userName%>"/></td>
        </tr>
        <tr>
            <td width="20%">学校名称</td>
            <td width="80%"><input type="text" name="schoolName" value="<%=schoolName%>"></td>
        </tr>
        <tr>
            <td width="20%">开始时间</td>
            <td width="80%"><input type="text" name="startDate" value="<%=startDate%>"></td>
        </tr>
        <tr>
            <td width="20%">结束时间</td>
            <td width="80%"><input type="text" name="endDate" value="<%=endDate%>"></td>
        </tr>
        <tr>
            <td colspan="4">
                <input type="submit" value="删除学历信息">
            </td>
        </tr>
    </table>
</form>
</body>
</html>
