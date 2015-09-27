<%-- 
    Document   : Peer Reviews
    Created on : Apr 24, 2010, 4:10:18 PM
    Author     : Tiger
--%>

<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<c:choose>
    <c:when test="${param.checkPermission eq '1'}">
        <indaba:view prjid="${prjid}" uid="${uid}" right="read journal peer reviews">
            <div id="prReviews" class="box">
                <h3><indaba:msg key='common.label.review' /><a class="toggleVisible" href="#"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
                <div class="content">
                    <table>
                        <tr>
                            <td align="left">
                                <h4><indaba:msg key='jsp.peerReview.peerReviewTips' />:</h4>
                                <textarea class="text" name="opinions" id="peer-review-opinion">${peerReview.opinions}</textarea>
                                <button style="float:right;" class="add medium button blue icon-save" name="SaveOpinions"><indaba:msg key='common.btn.save' /></button>
                                <div class="clear"></div>
                            </td>
                        </tr>
                    </table>
                </div>
                <div id="peerReviewContent" class="content">
                </div>
            </div>
            <script type="text/javascript">
                var parameters = {};
                parameters.userId = '${uid}';
                parameters.userName = "${name}";
                parameters.containerName = 'peerReviewContent';
                parameters.horseId = '${param.horseid}';
                parameters.assignId = '${param.assignid}';
                parameters.ctxPath = "${contextPath}";
                parameters.checkPermission = '${param.checkPermission}';
                parameters.syncStatus = '${param.syncStatus}';
                parameters.folded = '${param.folded}';
                loadPeerReviews(parameters);
            </script>
        </indaba:view>
    </c:when>
    <c:otherwise>
        <div id="prReviews" class="box">
            <h3><indaba:msg key='common.label.review' /><a class="toggleVisible" href="#"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
            <div class="content">
                <table>
                    <tr>
                        <td align="left">
                            <h4><indaba:msg key='common.label.reviewTips' />:</h4>
                            <textarea class="text" name="opinions" id="peer-review-opinion">${peerReview.opinions}</textarea>
                            <button style="float:right;" class="add medium button blue icon-save" name="SaveOpinions"><indaba:msg key='common.btn.save' /></button>
                            <div class="clear"></div>
                        </td>
                    </tr>
                </table>
            </div>
            <div id="peerReviewContent" class="content">
            </div>
        </div>

        <script type="text/javascript">
            var parameters = {};
            parameters.userId = '${uid}';
            parameters.userName = "${name}";
            parameters.containerName = 'peerReviewContent';
            parameters.horseId = '${param.horseid}';
            parameters.assignId = '${param.assignid}';
            parameters.ctxPath = "${contextPath}";
            parameters.checkPermission = '${param.checkPermission}';
            parameters.syncStatus = '${param.syncStatus}';
            parameters.folded = '${param.folded}';
            loadPeerReviews(parameters);
        </script>
    </c:otherwise>
</c:choose>