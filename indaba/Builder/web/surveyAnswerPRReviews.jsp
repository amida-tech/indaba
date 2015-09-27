<%-- 
    Document   : surveyAnswerPRReviews.jsp
    Created on : Apr 24, 2010, 4:10:18 PM
    Author     : Tiger
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>


<indaba:view prjid="${prjid}" uid="${uid}" right="read survey peer reviews">
    <div class="box">
        <h3><indaba:msg key='common.label.reviews' /><a class="toggleVisible" href="#">
                <c:choose>
                    <c:when test="${param.folded eq 1}"><img src='images/expand_icon.png' alt='expand' /></c:when>
                    <c:otherwise><img src='images/collapse_icon.png' alt='collapse' /></c:otherwise>
                </c:choose></a></h3>
        <div id="prReviews" class="content<c:if test="${param.folded eq 1}"> show-then-hide</c:if>" <c:if test="${param.folded eq 1}">style="display:none;"</c:if>>
            </div>
        </div>
        <script type="text/javascript">
            $(document).ready(function() {
                var parameters = {};
                parameters.userId = '${uid}';
                parameters.userName = "${name}";
                parameters.containerName = 'prReviews';
                parameters.horseId = '${param.horseid}';
                parameters.questionId = '${param.questionid}';
                parameters.assignId = '${param.assignid}';
                parameters.ctxPath = "${contextPath}";
                parameters.checkPermission = '${param.checkPermission}';
                parameters.opinionEditable = "${param.opinionEditable}";
                parameters.commentsEditable = "${param.commentsEditable}";
                parameters.hideDiscussion = "${param.hideDiscussion}";
                parameters.answerEditable = "${param.answerEditable}";
                parameters.syncStatus = '${param.syncStatus}';
                parameters.folded = '${param.folded}';
                parameters.superedit = '${param.superedit}';
        <c:if test="${param.checkPermission eq '1'}">
                parameters.viewMode = "${param.viewMode}";
                parameters.saVerId = "${param.saVerId}";
        </c:if>
                loadSurveyPRReviews(parameters);
            });
    </script>
</indaba:view>