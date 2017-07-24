<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="baobaotao" tagdir="/WEB-INF/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>学校历史事件管理中心</title>
</head>
<body>
<tr>
    <a href="${pageContext.request.contextPath }/addSchoolEvent.jsp">点击增加学校历史事件</a></td>
</tr>
<br/>
<br/>
<form action="<c:url value="findEventByUserName.html"/>" method="post">
    <input type="text" name="findEventByUserName"/>
    <input type="submit" value="根据用户姓名模糊查询与其学历相关的历史事件（t_user -> t_history -> t_event）" />
</form>
<br/>
<br/>
<div>
    <table width="100%" border=1>
        <tr>
            <td>事件编号</td>
            <td>学校编号</td>
            <td>学校名字</td>
            <td>事件内容</td>
            <td>开始时间</td>
            <td>结束时间</td>
        </tr>
        <c:forEach items="${pagedTopic.result }" var="data">
            <tr>
                <td>${data.eventId }</td>
                <td>${data.schoolCode }</td>
                <td>${data.schoolName }</td>
                <td>${data.eventText }</td>
                <td>${data.startDate }</td>
                <td>${data.endDate }</td>
            </tr>
        </c:forEach>
    </table>
</div>
<baobaotao:PageBar
        pageUrl="changePage.html"
        pageAttrKey="pagedTopic"/>
</body>
</html>
