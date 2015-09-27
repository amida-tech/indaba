<%-- 
    Document   : Peer Reviews
    Created on : Apr 24, 2010, 4:10:18 PM
    Author     : Tiger
--%>

<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<un:useConstants var="Rights" className="com.ocs.indaba.common.Rights"/>

<indaba:view prjid="${prjid}" uid="${uid}" right="${Rights.READ_JOURNAL_PEER_REVIEWS}" ignore="${param.checkPermission ne '1'}" >
    <div id="prReviews" class="box">
        <h3><indaba:msg key='common.label.reviews' /><a class="toggleVisible" href="#">
                <c:choose>
                    <c:when test="${param.folded eq 1}"><img src='images/expand_icon.png' alt='expand' /></c:when>
                    <c:otherwise><img src='images/collapse_icon.png' alt='collapse' /></c:otherwise>
                </c:choose>
            </a></h3>
        <div id="prReviewsContent" class="content" <c:if test="${param.folded eq 1}">style="display:none;"</c:if>>
            </div>
        </div>
        <script type="text/javascript">
            var parameters = {};
            parameters.userId = '${uid}';
            parameters.userName = "${name}";
            parameters.containerName = 'prReviewsContent';
            parameters.horseId = '${param.horseid}';
            parameters.assignId = '${param.assignid}';
            parameters.ctxPath = "${contextPath}";
            parameters.checkPermission = '${param.checkPermission}';
            parameters.syncStatus = '0';
            parameters.folded = '${param.folded}';
            parameters.superedit = '${param.superedit}';
            loadPRReviews(parameters);
    </script>
</indaba:view>
