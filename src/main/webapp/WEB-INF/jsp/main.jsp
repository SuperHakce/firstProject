<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>网信论坛</title>
</head>
<body>
<%
    String findBySchoolName = request.getParameter("findBySchoolName");
%>
    ${user.userName},欢迎您进入网信论坛，您当前积分为${user.credits}。
    <br/>
    <br/>
<form action="<c:url value="testIdempotentForEvent.html"/>" method="post">
    <input type="text" name="testIdempotentForEvent" size="5" value="幂等模拟"  disabled = disabled/>
    <input type="submit" value="幂等模拟" />
</form>
    <br/>
    <form action="<c:url value="changePage.html"/>" method="post">
          <input type="text" name="changePage" size="5" value="历史管理"  disabled = disabled/>
          <input type="submit" value="点击进入" />
    </form>
    <br/>
    <tr>
        <a href="${pageContext.request.contextPath }/addHistory.jsp?userId=${user.userId}&userName=${user.userName}">点击增加学历信息</a></td>
    </tr>
    <br/>
<br/>

    <form action="<c:url value="findBySchoolName.html"/>" method="post">
    <input type="text" name="findBySchoolName"/>
    <input type="submit" value="学校名称模糊查询" />
    </form>

    <br/>
    <br/>
    </table>
    您的学习经历:
    <br/>
    <table width="100%" border=1>
        <tr>
            <td>学历编号</td>
            <td>用户编号</td>
            <td>用户姓名</td>
            <td>学校名字</td>
            <td>开始时间</td>
            <td>结束时间</td>
            <td>操作</td>
        </tr>
        <c:forEach items="${histories }" var="data">
        <tr>
            <td>${data.historyId }</td>
            <td>${data.userId }</td>
            <td>${data.userName }</td>
            <td>${data.schoolName }</td>
            <td>${data.startDate }</td>
            <td>${data.endDate }</td>
            <td><a href="${pageContext.request.contextPath }/modifyHistory.jsp?historyId=${data.historyId}&userId=${data.userId}&userName=${data.userName}&schoolName=${data.schoolName}&startDate=${data.startDate}&endDate=${data.endDate}">修改</a>
                <a href="${pageContext.request.contextPath }/deleteHistory.jsp?historyId=${data.historyId}&userId=${data.userId}&userName=${data.userName}&schoolName=${data.schoolName}&startDate=${data.startDate}&endDate=${data.endDate}">删除</a>
        </tr>
        </c:forEach>
</body>
</html>