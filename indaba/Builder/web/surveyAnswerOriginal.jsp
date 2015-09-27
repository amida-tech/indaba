<%--
    Document   : surveyAnswerOriginal.jsp
    Created on : 05 15, 2010, 4:34:20 PM
    Author     : luwb
--%>

<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title><indaba:msg key='jsp.surveryAnsOrig.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <!--[if lt IE 7]>
            <link href="ie6.css" media="screen" rel="Stylesheet" type="text/css" />
        <![endif]-->
        <link type="text/css" rel="stylesheet" href="css/style2.css"/>
        <link type="text/css" rel="stylesheet" href="css/button.css"/>
        <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="js/jquery.scrollTo-1.4.2-min.js"></script>
        <script type="text/javascript" src="js/jquery.localscroll-1.2.7-min.js"></script>
        <script type="text/javascript" src="js/jquery.highlightFade.js"></script>
        <script type="text/javascript" src="js/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="js/discussions.js"></script>
        <script type="text/javascript" src="js/jquery.autogrow-textarea.js"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/surveyReview.js"></script>
        <script type="text/javascript" src="js/json2.js"></script>
        <script type="text/javascript" src="js/button.js"></script>
        <script type="text/javascript" src="js/assignment.js"></script>
        <script type="text/javascript" src="js/contentGeneral.js"></script>
        <script type="text/javascript" src="ckeditor/ckeditor.js"></script>
        <script type="text/javascript" src="ckeditor/adapters/jquery.js"></script>
        <script type="text/javascript" src="js/scorecardNavigation.js"></script>
        <script type="text/javascript" src="js/indicatorSearch.js"></script>
    </head>

    <body>
        <div id="indaba">
            <c:set var="active" value="yourcontent" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper">
                <div id="main">
                    <c:forEach items="${view.categoryViewList}" var="category" varStatus="status">
                        <div class="content">
                            <div class="box">
                                <c:if test="${status.first}">
                                    <h3><indaba:msg key='jsp.surveryAnsOrig.taskName' />: ${view.taskName}</h3>
                                    <h3><indaba:msg key='common.label.username' />: ${view.userName}</h3>
                                    <h3><indaba:msg key='common.label.createtime' />: ${view.createTime}</h3>
                                    <br/><br/>
                                </c:if>
                                <h3>${category.label}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${category.title}</h3>
                            </c:forEach>
                            <div class="content">
                                <b>${view.publicName}: ${view.question}</b>
                                <br/>
                                <c:choose>
                                    <c:when test="${view.answerType eq 6}">
                                        <indaba:tableAnswer horseId="${horseid}" 
                                                            mainQuestionId="${questionid}" 
                                                            disabled="true" />
                                    </c:when>
                                    <c:otherwise>
                                        <indaba:simpleAnswer answerType="${view.answerType}" 
                                                             answerTypeId="${view.answerTypeId}" 
                                                             name="selection" showAnswer="true" 
                                                             answerObjectId="${view.answerObjectId}" 
                                                             disabled="true" />
                                    </c:otherwise>
                                </c:choose>
                                <br/>
                                <b><indaba:msg key='common.title.sources' />:</b><br/>${view.refdescrition}<br/>
                                <indaba:source referenceType="${view.referType}" referenceId="${view.referId}" name="source" showAnswer="true" referenceObjectId="${view.referenceObjectId}" disabled="true"></indaba:source>
                                <br/><br/>
                                <!-- include attachment widget -->
                                <jsp:include page="attachment.jsp" flush="true" >
                                    <jsp:param name="contentType" value="survey" />
                                    <jsp:param name="preversion" value="1" />
                                    <jsp:param name="horseId" value="${horseId}" />
                                    <jsp:param name="readonly" value="true" />
                                </jsp:include>
                                <b><indaba:msg key='common.title.comments' />:</b>
                                <br/>
                                <textarea NAME="comments" class="text" ROWS="6" readonly>${view.comments}</textarea>
                            </div>
                            <c:forEach items="${view.categoryViewList}" var="category">
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
