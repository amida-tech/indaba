<%-- 
    Document   : staffReview
    Created on : Apr 24, 2010, 4:10:18 PM
    Author     : Tiger
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<c:choose>
    <c:when test="${param.checkPermission eq '1'}">
        <c:set var="rightName" value="read survey staff-author discussion" />
        <c:if test="${param.tool eq 'spr'}">
            <c:set var="rightName" value="read survey staff-author discussion on peer review" />
        </c:if>

        <indaba:view prjid="${prjid}" uid="${uid}" right="${rightName}">
            <div id="staffDiscussion" class="box"></div>
            <script type="text/javascript">
                var parameters = {};
                parameters.userId = '${uid}';
                parameters.userName = "${name}";
                parameters.horseId = '${param.horseid}';
                parameters.assignId = '${param.assignid}';
                parameters.ctxPath = "${contextPath}";
                parameters.checkPermission = '${param.checkPermission}';
                parameters.syncStatus = '${param.syncStatus}';
                parameters.viewMode = '${param.viewMode}';
                parameters.contentVersionId = '${contentVersionId}';
                parameters.folded = '${param.folded}';
                parameters.tool = '${param.tool}';
                loadSurveyStaffDiscussions(parameters);
            </script>
        </indaba:view>
    </c:when>
    <c:otherwise>
        <div id="staffDiscussion" class="box"></div>
        <script type="text/javascript">
            var parameters = {};
            parameters.userId = '${uid}';
            parameters.userName = "${name}";
            parameters.horseId = '${param.horseid}';
            parameters.assignId = '${param.assignid}';
            parameters.ctxPath = "${contextPath}";
            parameters.checkPermission = '${param.checkPermission}';
            parameters.syncStatus = '${param.syncStatus}';
            parameters.viewMode = '${param.viewMode}';
            parameters.contentVersionId = '${contentVersionId}';
            parameters.folded = 0;
            loadSurveyStaffDiscussions(parameters);
        </script>
    </c:otherwise>
</c:choose>