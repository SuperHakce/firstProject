<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>新增学历信息</title>
</head>
<body>
用户学历信息：
<form action="<c:url value="/addHistory.html" />" method="post">
    <table border="1px" width="60%">
        <tr>
            <td width="20%">用户名</td>
            <td width="80%">
                <input type="text" name="userName"/>
            </td>
        </tr>
        <tr>
            <td width="20%">密码</td>
            <td width="80%"><input type="password" name="password"/></td>
        </tr>
        <tr>
            <td width="20%">密码确认</td>
            <td width="80%"><input type="password" name="again"></td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="保存">
                <input type="reset" value="重置">
            </td>
        </tr>
    </table>
</form>
</body>
</html>