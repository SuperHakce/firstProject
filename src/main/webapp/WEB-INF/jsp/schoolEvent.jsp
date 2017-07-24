<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="baobaotao" tagdir="/WEB-INF/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="pragma" content="public">
    <meta http-equiv="cache-control" content="public">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>学校历史事件管理中心</title>
</head>
<body>
<tr>
    <a href="${pageContext.request.contextPath }/addSchoolEvent.jsp">Please Add SchoolEvent</a></td>
</tr>
<br/>
<br/>
</form>
</div>
<baobaotao:PageBar
        pageUrl="changePage.html"
        pageAttrKey="pagedTopic"/>
</body>
</html>
