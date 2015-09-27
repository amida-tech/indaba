<%-- 
    Document   : people
    Created on : Mar 1, 2010, 8:38:56 PM
    Author     : menglong
--%>


<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title><indaba:msg key='jsp.people.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <link type="text/css" rel="stylesheet" href="css/button.css"/>
        <!--[if lt IE 7]>
            <link href="ie6.css" media="screen" rel="Stylesheet" type="text/css" />
        <![endif]-->
        <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/filter.js"></script>
    </head>
    <body>
        <div id="indaba">
            <!--c:set var="active" value="people" scope="request"/-->
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper">
                <div class="box">
                    <h3><indaba:msg key='jsp.people.shouldKnow' /><a href="#" class="toggleVisible"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
                    <div class="content">
                        <c:forEach items="${shouldKnowPeoples}" var="knowUser" varStatus="status">
                            <indaba:userDisplay prjid="${prjid}" subjectUid="${uid}" targetUid="${knowUser.userId}" showBio="true" />
                        </c:forEach>
                    </div>
                </div>
                <div class="box">
                    <h3><indaba:msg key='jsp.people.yourTeams' /><a href="#" class="toggleVisible"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
                    <div class="content">
                        <c:forEach items="${teams}" var="team" varStatus="status1">
                            <div>
                                <b>${team.name}:</b>
                                <c:forEach items="${team.userList}" var="teamUser" varStatus="status">
                                    <indaba:userDisplay prjid="${prjid}" subjectUid="${uid}" targetUid="${teamUser.id}" /><c:if test="${!status.last}">, </c:if>
                                </c:forEach>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <indaba:view prjid="${prjid}" uid="${uid}" right="manage all users">
                    <div class="box">
                        <h3><indaba:msg key='jsp.people.allUsers' />
                            <a href="#" class="toggleFilter"><indaba:msg key='common.filter.clickopen' />&nbsp; <img style="vertical-align:middle" src='images/expand_icon.png' alt='expand' /></a>
                        </h3>
                        <div class="content" style="display: none;">
                            <div id="filters-box" class="box">
                                <jsp:include page="peopleFilter.jsp" flush="true" />
                                <div align="middle">
                                    <input class='small button blue' type="reset" name="reset" value=" &nbsp;&nbsp;<indaba:msg key='common.btn.reset' />&nbsp;&nbsp; " onclick='$(".removeSelection a").click();resetRadio();'/>&nbsp;&nbsp;&nbsp;&nbsp;
                                    <input class='small button blue' type="button" name="apply" value=" &nbsp;&nbsp;<indaba:msg key='common.btn.apply' />&nbsp;&nbsp; " onclick="setFilters()"/>
                                </div>
                                <div id="waitInfo" style="text-align: center; color: red; display: none "><indaba:msg key='common.msg.pleasewait' /></div>
                            </div>
                        </div>
                        <div id="newList" style="display: none"></div>
                        <div id="userList">
                        <table id="userInfoList">
                            <thead>
                                <tr class="thead">
                                    <th><indaba:msg key='common.label.name' /></th>
                                    <th><indaba:msg key='common.label.role' /></th>
                                    <th><indaba:msg key='jsp.people.target' /></th>
                                    <th><indaba:msg key='common.label.teams' /></th>
                                    <th nowrap><indaba:msg key='common.label.opencases' /></th>
                                    <th><indaba:msg key='common.label.assignedcontent' /></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${allUsers!=ull}">
                                    <c:forEach items="${allUsers}" var="userListInfo" varStatus="iterStatus">
                                        <tr>
                                            <td nowrap>
                                                <indaba:userDisplay prjid="${prjid}" subjectUid="${uid}" targetUid="${userListInfo.id}" />
                                            </td>
                                            <td>${userListInfo.role}</td>
                                            <td>
                                                <c:forEach items="${userListInfo.targets}" var="tar" varStatus="status">
                                                    ${tar.name}<c:if test="${!status.last}">, </c:if>
                                                </c:forEach>
                                            </td>
                                            <td>
                                                <c:forEach items="${userListInfo.teams}" var="team" varStatus="status">
                                                    ${team.teamName}<c:if test="${!status.last}">, </c:if>
                                                </c:forEach>
                                            </td>
                                            <td>
                                                <c:forEach items="${userListInfo.openCases}" var="case" varStatus="status">
                                                    <a href="casedetail.do?caseid=${case.id}">#${case.id}</a><c:if test="${!status.last}">, </c:if>
                                                </c:forEach>
                                            </td>
                                            <td>
                                                <c:forEach items="${userListInfo.assignedTaskDisplay}" var="dis" varStatus="status">
                                                    <c:if test="${dis.horseId == -1}">
                                                        ${dis.content}
                                                    </c:if>
                                                    <c:if test="${dis.horseId != -1}">
                                                        <c:if test="${dis.contentType==1}">
                                                            <c:choose>
                                                                <c:when test="${dis.workflowObjectStatus == 1}">${dis.content}</c:when>
                                                                <c:otherwise>
                                                                    <a href="notebook.do?action=display&horseid=${dis.horseId}">${dis.content}</a>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:if>
                                                        <c:if test="${dis.contentType==0}">
                                                            <c:choose>
                                                                <c:when test="${dis.workflowObjectStatus == 1}">${dis.content}</c:when>
                                                                <c:otherwise>
                                                                    <a href="surveyDisplay.do?action=display&horseid=${dis.horseId}">${dis.content}</a>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:if>
                                                    </c:if>
                                                    <c:if test="${!status.last}">, </c:if>
                                                </c:forEach>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                            </tbody>
                        </table>
                        </div>
                    </div>
                </indaba:view>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
