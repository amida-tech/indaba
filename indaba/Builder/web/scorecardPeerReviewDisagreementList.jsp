<%-- 
    Document   : scoreboardNavigation
    Created on : 2010-3-24, 6:52:48
    Author     : Luke Shi
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%--

THIS JSP IS NO LONGER USED. THE SURVEY TREE DISPLAY IS NOW HANDLED BY Survey Tree Tag Handler!!!  YC 2014/02/11

--%>

<c:if test="${not empty indicators}">
    <div class="box">
        <h3><a name="answerquestions"><indaba:msg key='jsp.scorecardPRDisagree.list' /></a><a href="#answerquestions" class="toggleVisible"><img src='images/collapse_icon.png' alt="" /></a></h3>
        <div class="content">
            <c:forEach var="indicator" items="${indicators}" >
                <c:choose>
                    <c:when test='${param.action == "surveyReview.do"}'>
                        <b>${indicator.publicName}:</b> <a href="surveyAnswerReview.do?answerid=${indicator.answerId}&questionid=${indicator.questionId}&horseid=${param.horseid}&assignid=${param.assignid}&action=${param.action}&returl=${param.returl}">${indicator.question}</a>
                		<c:if test="${indicator.staffReviewed}"><img src="images/check.png" alt=""/></c:if>
                    </c:when>
                    <c:otherwise>
                        <b>${indicator.publicName}:</b> <a href="surveyAnswerPRReview.do?answerid=${indicator.answerId}&questionid=${indicator.questionId}&horseid=${param.horseid}&assignid=${param.assignid}&action=${param.action}&returl=${param.returl}">${indicator.question}</a>
                        <c:if test="${indicator.prReviewed}"><img src="images/check.png" alt=""/></c:if>
                    </c:otherwise>
                </c:choose>
                <br/>
            </c:forEach>
        </div>
    </div>
</c:if>