<%-- 
    Document   : surveyAnswerPeerReviewsForFlagUnset
    Created on : Apr 8, 2014, 4:08:05 PM
    Author     : yc06x
--%>


<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<c:set var="opinionEditable" value="false" />
<c:set var="commentsEditable" value="false" />
<c:set var="answerEditable" value="false" />
<c:set var="hideDiscussion" value="true" />

<c:if test="${fn:contains(contents, 'F')}">
    <c:set var="hideDiscussion" value="false" />
</c:if>

    <div class="box">
        <h3><indaba:msg key='common.label.reviews' /><a class="toggleVisible" href="#">
                <c:choose>
                    <c:when test="${param.folded eq 1}"><img src='images/expand_icon.png' alt='expand' /></c:when>
                    <c:otherwise><img src='images/collapse_icon.png' alt='collapse' /></c:otherwise>
                </c:choose></a></h3>
        <div id="prReviews" class="content" <c:if test="${param.folded eq 1}">style="display:none;"</c:if>>
        </div>
    </div>
    <script type="text/javascript">
        $(document).ready(function(){
            var parameters = {};
            parameters.userId = '${uid}';
            parameters.userName = "${name}";
            parameters.containerName = 'prReviews';
            parameters.horseId = '${param.horseid}';
            parameters.questionId = '${param.questionid}';
            parameters.assignId = '${param.assignid}';
            parameters.ctxPath = "${contextPath}";
            parameters.checkPermission = 'false';

            parameters.opinionEditable = '${opinionEditable}';
            parameters.commentsEditable = '${commentsEditable}';
            parameters.hideDiscussion = '${hideDiscussion}';
            parameters.answerEditable = '${answerEditable}';

            parameters.syncStatus = '${param.syncStatus}';
            parameters.folded = '${param.folded}';
            parameters.contents = '${param.contents}';
            parameters.superedit = '${param.superedit}';
            parameters.highlightParts = true;

            parameters.checkRights = 1;
            loadSurveyPRReviews(parameters);
        });
    </script>

