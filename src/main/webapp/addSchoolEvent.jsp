<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>新增事件信息</title>
</head>
<body>
学校事件信息增加：
<form action="<c:url value="/addSchoolEvent.html" />" method="post">
    <table border="1px" width="35%">
        <tr>
            <td width="20%">学校编码</td>
            <td width="80%"><input type="text" name="schoolCode"/></td>
        </tr>
        <tr>
            <td width="20%">学校名称</td>
            <td width="80%"><input type="text" name="schoolName"></td>
        </tr>
        <tr>
            <td width="20%">事件内容</td>
            <td width="80%"><input type="text" name="eventText"></td>
        </tr>
        <tr>
            <td width="20%">开始时间</td>
            <td width="80%"><input type="text" name="startDate"></td>
        </tr>
        <tr>
            <td width="20%">结束时间</td>
            <td width="80%"><input type="text" name="endDate"></td>
        </tr>
        <tr>
            <td colspan="4">
                <input type="submit" value="保存事件信息">
                &nbsp&nbsp
                <input type="reset" value="重置事件信息">
            </td>
        </tr>
    </table>
</form>
</body>
</html>
