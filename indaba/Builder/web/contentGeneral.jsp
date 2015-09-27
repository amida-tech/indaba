<%-- 
    Document   : contentGeneral
    Created on : Apr 24, 2010, 3:53:19 PM
    Author     : Jeff Jiang
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<link type="text/css" rel="stylesheet" href="css/style.css"/>
<script type="text/javascript" src="js/common.js">
</script>

<div id="cntgeneral" class="wrapper">
    <c:if test="${assignid > 0}">
        <div class="box">
            <h3><indaba:msg key='jsp.contentGeneral.instructions' /> <a href="#" class="toggleVisible"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
            <div class="content">
                ${instructions}
            </div>
        </div>
    </c:if>

    <indaba:view prjid="${prjid}" uid="${uid}" right="see content current tasks">
        <div class="box" >
            <h3><indaba:msg key='jsp.contentGeneral.currentTasks' /> <a href="#" class="toggleVisible"><img src='images/expand_icon.png' alt='expand' /></a></h3>
            <div class="content" style="display: none">
                <c:choose>
                    <c:when test='${fn:length(allAssignedOfHorse) == 0}'>
                        &nbsp;&nbsp;<indaba:msg key='common.msg.nodata' />
                    </c:when>
                    <c:otherwise>
                        <jsp:include page="simpleTaskList.jsp" flush="true" />
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </indaba:view>

    <indaba:view prjid="${prjid}" uid="${uid}" right="see content cases">
        <c:if test='${fn:length(allCases) > 0}'>
            <div class="box">
                <h3><indaba:msg key='common.label.cases' /> <a href="#" class="toggleVisible"><img src='images/expand_icon.png' alt='expand' /></a></h3>
                <div class="content" style="display: none;">
                    <table>
                        <thead>
                            <tr class="thead">
                                <th><indaba:msg key='jsp.contentGeneral.caseNO' /></th>
                                <th><indaba:msg key='common.label.title' /></th>
                                <th><indaba:msg key='common.label.status' /></th>
                                <th><indaba:msg key='jsp.contentGeneral.caseOpened' /></th>
                                <th><indaba:msg key='jsp.contentGeneral.caseLastUpdated' /></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${allCases}" var="case" varStatus="status">
                                <tr>
                                    <td>${case.caseId}</td>
                                    <td><a href="casedetail.do?caseid=${case.caseId}">${case.title}</a></td>
                                    <td>
                            <c:if test="${case.status == 'Open'}"><indaba:msg debug="true" key="common.label.open" /></c:if>
                            <c:if test="${case.status == 'Closed'}"><indaba:msg debug="true" key="common.label.closed" /></c:if>
                            </td>
                            <td>${case.openedTime}</td>
                            <td>${case.lastUpdateTime}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

            </div>
        </c:if>
    </indaba:view>
</div>