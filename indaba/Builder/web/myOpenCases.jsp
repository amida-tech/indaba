<%-- 
    Document   : myOpenCases
    Created on : Sep 1, 2010, 15:53:18 PM
    Author     :  Jeff Jiang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<c:if test='${fn:length(myCase) > 0}'>
<div class="box icon-home-content">
    <h3><indaba:msg key='jsp.myOpenCases.yourOpenCases' />
        <a href="#" class="toggleVisible"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
    <div class="content">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <thead>
                <tr class="thead">
                    <th width="40"><indaba:msg key='common.label.case' /> #</th>
                    <th><indaba:msg key='common.label.title' /></th>
                    <th><indaba:msg key='common.label.status' /></th>
                    <th><indaba:msg key='common.label.priority' /></th>
                    <th><indaba:msg key='common.label.tags' /></th>
                    <th><indaba:msg key='common.label.owner' /></th>
                    <th><indaba:msg key='common.label.attachedcontent' /></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${myCase}" var="case" varStatus="status1">
                    <tr>
                        <td><a href="casedetail.do?caseid=${case.caseId}">#${case.caseId}</a></td>
                        <td><a href="casedetail.do?caseid=${case.caseId}">${case.title}</a></td>
                        <td>
                            <c:if test="${case.status == 'Open'}"><indaba:msg key='common.label.open' /></c:if>
                            <c:if test="${case.status == 'Closed'}"><indaba:msg key='common.label.closed' /></c:if>
                        </td>
                        <td>
                            <c:if test="${case.priority == 'Low'}"><indaba:msg key='jsp.queues.low' /></c:if>
                            <c:if test="${case.priority == 'Medium'}"><indaba:msg key='jsp.queues.medium' /></c:if>
                            <c:if test="${case.priority == 'High'}"><indaba:msg key='jsp.queues.high' /></c:if>
                        </td>
                        <td>
                            <c:forEach items="${case.tags}" var="tag" varStatus="status">
                                ${tag.term}<c:if test="${!status.last}">, </c:if>
                            </c:forEach>
                        </td>
                        <td><a href="profile.do?targetUid=${case.assignedUserId}">${case.owner}</a></td>
                        <td>
                            <c:forEach items="${case.attachContents}" var="content" varStatus="status">
                                <c:choose>
                                    <c:when test="${content.contentType == 1}">
                                        <a href="notebook.do?action=display&horseid=${content.horseId}">${content.title}</a>
                                        <c:if test="${!status.last}">, </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="surveyDisplay.do?action=display&horseid=${content.horseId}">${content.title}</a>
                                        <c:if test="${!status.last}">, </c:if>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</c:if>
