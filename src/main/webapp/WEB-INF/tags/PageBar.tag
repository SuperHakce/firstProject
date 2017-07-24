<%@ tag import="org.hibernate.Session" %>
<%@ tag pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="pageUrl" required="true" rtexprvalue="true" description="分页页面对应的URl" %>
<%@ attribute name="pageAttrKey" required="true" rtexprvalue="true" description="Page对象在Request域中的键名称" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/dwr/engine.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/dwr/interface/CheckBox.js"></script>
<c:set var="pageUrl" value="${pageUrl}" />
<%
    String myTest = request.getParameter("findEventByUserName");
    String setPageSize = request.getParameter("setPageSize");
%>
<%
    String separator = pageUrl.indexOf("?") > -1?"&":"?";
    jspContext.setAttribute("pageResult", request.getAttribute(pageAttrKey));
    jspContext.setAttribute("pageUrl", pageUrl);
    jspContext.setAttribute("separator", separator);
    jspContext.setAttribute("myTest",myTest);
    jspContext.setAttribute("setPageSize",setPageSize);
%>
<script>
    function switchSelectBox(eventId){
        //alert(eventId);
        CheckBox.updateCheckBox(eventId,function(data){
            //alert(data);
        });
    }
</script>
<form action="<c:url value="findEventByUserName.html"/>" method="post">
    <input type="submit" value="Please Set PageSize">
    <input type="text" name="setPageSize" size="2" value=${pageResult.pageSize}>
    <input type="submit" value="Please Set ID Search">
    <input type="text" name="findEventByUserName" size="8" value=${pageResult.searchName}>
    <input type="submit" value="Submit"/>
    <input type="submit" name="checkAll" value="Check All"/>
    <input type="submit" name="cancelCheckAll" value="Cancel Check All"/>
    <input type="submit" name="invertCheck" value="Invert Check"/>
    <input type="submit" name="checkAllThisPage" value="Check All ThisPage"/>
    <input type="submit" name="cancelCheckAllThisPage" value="Cancel Check All ThisPage"/>
    <input type="submit" name="invertCheckThisPage" value="Invert Check ThisPage"/>
    <br/>
    <br/>
    <input type="submit" name="deleteAllChecked" value="Delete All Checked"/>
    <input type="text" name="setPage" size="1" style="DISPLAY:none" value=${pageResult.currentPageNo}>
</form>
<div>
    <table width="100%" border=1>
        <tr>
            <td>EventId</td>
            <td>SchoolCode</td>
            <td>SchoolName</td>
            <td>EventText</td>
            <td>StartDate</td>
            <td>EndDate</td>
            <td>Choose</td>
        </tr>
        <c:forEach items="${pageResult.result }" var="data" varStatus="idEvent">
            <tr>
                <td>${data.eventId }</td>
                <td>${data.schoolCode }</td>
                <td>${data.schoolName }</td>
                <td>${data.eventText }</td>
                <td>${data.startDate }</td>
                <td>${data.endDate }</td>
                <c:if test="${data.isCheck == 0}">
                <td width="4%" height="26" align="center">
                    <input type="checkbox" name="checkbox" onclick=switchSelectBox("${data.eventId }")>
                </td>
                </c:if>
                <c:if test="${data.isCheck == 1}">
                    <td width="4%" height="26" align="center">
                        <input type="checkbox" name="checkbox" checked ="true" onclick=switchSelectBox("${data.eventId }")>
                    </td>
                </c:if>
            </tr>
        </c:forEach>
    </table>
</div>
<div style="font:12px;background-color:#DDDDDD">
    <c:if test="${pageResult.totalPageCount <= 1}"></c:if>
    <c:if test="${pageResult.totalPageCount > 1}">
        <form action="<c:url value="/showPage.html" />" method="post">
            Skip To <input type="text" name="showPage" size="4"> Page
            <input type="text" name="findEventByUserName" size="1" style="DISPLAY:none" value=${myTest}>
            <input type="text" name="setPageSize" size="1" style="DISPLAY:none" value=${setPageSize}>
            <input type="submit" name="submit" value="Skip">
            All ${pageResult.totalPageCount} Page，The ${pageResult.currentPageNo} Page
            <c:if test="${pageResult.currentPageNo <=1}">
                FirstPage&nbsp;&nbsp;
            </c:if>
            <c:if test="${pageResult.currentPageNo >1 }">
                <a href="<c:url value="${pageUrl}"/>${separator}pageNo=1&findEventByUserName=${myTest }&setPageSize=${setPageSize}">FirstPage</a>&nbsp;&nbsp;
            </c:if>
            <c:if test="${pageResult.hasPreviousPage}">
                <a href="<c:url value="${pageUrl}"/>${separator}pageNo=${pageResult.currentPageNo -1 }&findEventByUserName=${myTest }&setPageSize=${setPageSize}">PrevPage</a>&nbsp;&nbsp;
            </c:if>
            <c:if test="${!pageResult.hasPreviousPage}">
                PrevPage&nbsp;&nbsp;
            </c:if>
            <c:if test="${pageResult.hasNextPage}">
                <a href="<c:url value="${pageUrl}"/>${separator}pageNo=${pageResult.currentPageNo +1 }&findEventByUserName=${myTest }&setPageSize=${setPageSize}">NextPage</a>&nbsp;&nbsp;
            </c:if>
            <c:if test="${!pageResult.hasNextPage}">
                NextPage&nbsp;&nbsp;
            </c:if>
            <c:if test="${pageResult.currentPageNo >= pageResult.totalPageCount}">
                LastPage&nbsp;&nbsp;
            </c:if>
            <c:if test="${pageResult.currentPageNo < pageResult.totalPageCount}">
                <a href="<c:url value="${pageUrl}"/>${separator}pageNo=${pageResult.totalPageCount }&findEventByUserName=${myTest }&setPageSize=${setPageSize}">LastPage</a>&nbsp;&nbsp;
            </c:if>
        </form>
    </c:if>
</div>