<%-- 
    Document   : surveyAnswerQuestionSidebar
    Created on : 2010-7-9, 11:33:21
    Author     : Luke Shi
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<style type="text/css">
    a#tip {line-height: 2em;}
    a#tip:hover {text-decoration:underline; color: red;}
</style>

<div id="ihavequestions">
    <c:if test="${displayHaveQuestions}" >
        <button class="large button yellow icon-question" onclick="loadSurveyAnswerQuestionSidebar('${contextPath}', '${action}', '${param.horseid}', '${param.assignid}', '${param.disp_answerid}', '${param.disp_answerid}', '${param.returl}');">
            <indaba:msg key='jsp.surveryAnsBar.add.question' /> </button>
        <br/><br/>
    </c:if>
</div>
<c:if test="${problemList!=null && (!empty problemList)}">
    <div id="questions-box" class="box">
        <h3><indaba:msg key='jsp.surveryAnsBar.question.list' /></h3>
        <div class="question-box-items">
            <c:forEach items="${problemList}" var="p">
                <div class="question-box-item">
                    <c:choose>
                        <c:when test="${reviewResponse}">
                            &nbsp;
                            <c:choose>
                                <c:when test="${p.responded}"><img src="images/check.png" alt=""/></c:when>
                                <c:otherwise>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</c:otherwise>
                            </c:choose>
                            <a href="surveyAnswer.do?questionid=${p.questionId}&horseid=${param.horseid}&answerid=${p.answerId}&assignid=${param.assignid}&action=surveyReviewResponse.do&returl=${returl}"
                               class="tipTip" title="${p.question}">
                                <b>${p.publicName}</b></a>
                            </c:when>
                            <c:otherwise>
                            &nbsp;
                            <a href="#" onclick="return loadSurveyAnswerQuestionSidebar('${contextPath}', '${action}', '${param.horseid}', '${param.assignid}', '-${p.answerId}', '${param.disp_answerid}', '${param.returl}');">
                                <img src="images/delete.png" alt="" width="12px" /></a>
                            <a href="${action}?questionid=${p.questionId}&horseid=${param.horseid}&answerid=${p.answerId}&assignid=${param.assignid}&action=surveyReview.do&returl=${returl}"
                               class="tipTip" title="${p.question}">
                                <b>${p.publicName}</b></a>
                            <c:if test="${p.responded}"><img src="images/check.png" alt=""/></c:if>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:forEach>
        </div>
    </div>
    <script type="text/javascript">
            $(document).ready(function() {
                $('div.question-box-items').mCustomScrollbar({
                    horizontalScroll: true,
                    theme: 'dark-thick',
                    autoDraggerLength: true,
                    scrollButtons: {enable: true, scrollType: 'continuous'}
                });
            });
    </script>
</c:if>