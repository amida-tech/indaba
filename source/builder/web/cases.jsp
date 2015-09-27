<%-- 
    Document   : cases
    Created on : Feb 21, 2010, 8:23:13 PM
    Author     : menglong
--%>

<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
    <head>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <title><indaba:msg key='jsp.cases.title' /></title>
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
            <!--c:set var="active" value="cases" scope="request"/-->
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper">
                <indaba:view prjid="${prjid}" uid="${uid}" right="open cases">
                    <a id="open-new-case" href="newcase.do"><indaba:msg key='jsp.cases.openNewCase' /></a>
                </indaba:view>
                <jsp:include page="myOpenCases.jsp" flush="true" />
                <indaba:view prjid="${prjid}" uid="${uid}" right="manage all cases">
                    <div class="box">
                        <h3><indaba:msg key='jsp.cases.allCases' />
                            <a href="#" class="toggleFilter"><indaba:msg key='common.filter.clickopen' />&nbsp; <img style="vertical-align:middle" src='images/expand_icon.png' alt='expand' /></a>
                        </h3>
                        <div class="content" style="display: none;">
                            <div id="filters-box" class="box">
                                <form action="cases.do">
                                    <jsp:include page="casefilter.jsp" flush="true" />
                                    <div align="middle">
                                        <input type="reset" name="reset" value=" &nbsp;&nbsp;<indaba:msg key='common.btn.reset' />&nbsp;&nbsp; " onclick='$(".removeSelection a").click();' class='small button blue'/>&nbsp;&nbsp;&nbsp;&nbsp;
                                        <input type="button" name="apply" value=" &nbsp;&nbsp;<indaba:msg key='common.btn.apply' />&nbsp;&nbsp; " onclick="setCaseFilters()" class='small button blue'/>
                                    </div>
                                </form>
                                <div id="waitInfo" style="text-align: center; color: red; display: none "><indaba:msg key='common.msg.pleasewait' /></div>
                            </div>
                        </div>
                        <hr/>
                        <div id="newList" style="display: none"></div>
                        <table id="casesList" width="100%" border="0" cellspacing="0" cellpadding="0">
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
                                <c:forEach items="${filterCases}" var="case" varStatus="status1">
                                    <tr>
                                        <td>
                                            <a href="casedetail.do?caseid=${case.caseId}">#${case.caseId}</a>
                                       <!--indaba:access prjid="${prjid}" uid="${uid}" right="see all cases"
                                      link="casedetail.do?caseid=${case.caseId}" text="#${case.caseId}" /-->
                                        </td>
                                        <td>
                                            <a href="casedetail.do?caseid=${case.caseId}">${case.title}</a>
                                        </td>
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
                                            <%--
                                             <indaba:access prjid="${prjid}" uid="${uid}" right="see all content"
                                          link="notebook.do?action=display&horseid=1" text="${title}" />
                                            --%>
                                        </td>

                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </indaba:view>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
