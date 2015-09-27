<%-- 
    Document   : contentGeneral
    Created on : 2010-3-23, 20:26:46
    Author     : Tiger
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:choose>
    <c:when test="${param.checkPermission eq '1'}">
        <indaba:view prjid="${prjid}" uid="${uid}" right="read content internal discussion">
            <div id="discussion" class="box discussions"></div>
            <script type="text/javascript">
                $(function(){
                    var parameters = {};
                    parameters.userId = '${uid}';
                    parameters.userName = "${name}";
                    parameters.horseId = '${param.horseid}';
                    parameters.assignId = '${param.assignid}';
                    parameters.ctxPath = "${contextPath}";
                    parameters.checkPermission = '${param.checkPermission}';
                    parameters.syncStatus = '${param.syncStatus}';
                    parameters.folded = '${param.folded}';
                    parameters.viewMode = '${param.viewMode}';
                    parameters.contentVersionId = '${contentVersionId}';
                    loadInternalDiscussions(parameters);
                });
            </script>
        </indaba:view>
    </c:when>
    <c:otherwise>
        <div id="discussion" class="box discussions"></div>
        <script type="text/javascript">
            $(function(){
                var parameters = {};
                parameters.userId = '${uid}';
                parameters.userName = "${name}";
                parameters.horseId = '${param.horseid}';
                parameters.assignId = '${param.assignid}';
                parameters.ctxPath = "${contextPath}";
                parameters.checkPermission = '${param.checkPermission}';
                parameters.syncStatus = '${param.syncStatus}';
                parameters.folded = '${param.folded}';
                parameters.viewMode = '${param.viewMode}';
                parameters.contentVersionId = '${contentVersionId}';
                loadInternalDiscussions(parameters);
            });
        </script>
    </c:otherwise>
</c:choose>