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
        <indaba:view prjid="${prjid}" uid="${uid}" right="read journal staff-author discussion">
            <div id="staffDiscussion" class="box"></div>
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
                    loadStaffDiscussions(parameters);
                });
            </script>
        </indaba:view>
    </c:when>
    <c:otherwise>
        <div id="staffDiscussion" class="box"></div>
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
                loadStaffDiscussions(parameters);
            });
        </script>
    </c:otherwise>
</c:choose>