<%-- 
    Document   : surveyAnswerPeerReview
    Created on : Apr 24, 2010, 4:10:18 PM
    Author     : Tiger
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<div id="prReviews" class="box">
    <h3><indaba:msg key='common.label.review' /><a class="toggleVisible" href="#"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
    <div class="content">
        <div class="box">
            <h3><indaba:msg key='jsp.surveryAnsPR.opinions' /><a class="toggleVisible" href="#"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
            <div class="content">
                <br>
                <b><indaba:msg key='jsp.surveryAnsPR.agree' /></b>
                <br>
                <table>
                    <tr>
                        <td nowrap=""><input type="radio" value="0" <c:if test="${peerReview.opinion eq 0}">checked="checked"</c:if> name="opinion" id="opinion_1"><label for="opinion_1"><indaba:msg key='jsp.surveryAnsPR.opinion.1' /></label></td>
                        </tr>
                        <tr>
                            <td nowrap=""><input type="radio" value="1" <c:if test="${peerReview.opinion eq 1}">checked="checked"</c:if> name="opinion" id="opinion_2"><label for="opinion_2"><indaba:msg key='common.msg.opinionagree' /></label></td>
                        </tr>
                        <tr>
                            <td nowrap=""><input type="radio" value="2" <c:if test="${peerReview.opinion eq 2}">checked="checked"</c:if> name="opinion" id="opinion_3"><label for="opinion_3"><indaba:msg key='common.msg.opiniondisagree' /></label></td>
                        </tr>
                        <tr>
                            <td nowrap=""><input type="radio" value="3" <c:if test="${peerReview.opinion eq 3}">checked="checked"</c:if> name="opinion" id="opinion_4"><label for="opinion_4"><indaba:msg key='common.msg.opinionnojudge' /></label></td>
                        </tr>
                        <tr class="opinion1" <c:if test="${peerReview.opinion ne 1}">style="display: none"</c:if>>
                            <td>
                            <indaba:msg key='jsp.surveryAnsPR.opinion1.desc' />
                            <textarea class="text reviewer-comment" rows="10">${peerReview.comments}</textarea>
                        </td>
                    </tr>
                    <tr class="opinion2 <c:if test="${peerReview.opinion ne 2}"> show-then-hide</c:if>">
                            <td>
                            <indaba:msg key='jsp.surveryAnsPR.opinion2.desc' />
                            <br/>
                            <textarea class="text reviewer-comment" rows="10">${peerReview.comments}</textarea>
                            <br/>
                            <b><indaba:msg key='common.msg.suggestedanswer' /></b><br/>
                            <c:choose>
                                <c:when test="${view.answerType eq 6}"> <%-- table question --%>
                                    <indaba:tableAnswerPeerReview peerReviewId="${peerReview.id}" 
                                                                  mainQuestionId="${questionid}" 
                                                                  disabled="false" />
                                </c:when>
                                <c:otherwise> <%-- simple question(int, float, single/multi choice, text) --%>
                                    <indaba:simpleAnswer answerType="${view.answerType}" 
                                                         answerTypeId="${view.answerTypeId}" 
                                                         name="selection" showAnswer="true" 
                                                         answerObjectId="${peerReview.suggestedAnswerObjectId}" 
                                                         disabled="false" />
                                </c:otherwise>
                            </c:choose>                    
                        </td>
                    </tr>
                    <tr>
                        <td nowrap="" align="center">
                            <div align="center">
                                <c:choose>
                                    <c:when test="${urlView.previousId == 0 || from == 'surveyReviewResponse.do'}">
                                        <a class="nav-link"><img src="images/icon_arrow_left_gray.png" alt=""/></a>
                                        </c:when>
                                        <c:otherwise>
                                        <a class="nav-link" href="#" onclick="location.href = 'surveyAnswerPeerReview.do?questionid=${urlView.previousId}&horseid=${param.horseid}&assignid=${param.assignid}&action=${from}&returl=${urlView.returnUrl}'"><img src="images/icon_arrow_left.png" alt=""/></a>
                                        </c:otherwise>
                                    </c:choose>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <button class="saveReview large button blue icon-check">&nbsp;<indaba:msg key="common.btn.save" />&nbsp;</button>
                                <span id="saveComplete"></span>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <c:choose>
                                    <c:when test="${urlView.nextId == 0 || from == 'surveyReviewResponse.do'}">
                                        <a class="nav-link"><img src="images/icon_arrow_right_gray.png" alt=""/></a>
                                        </c:when>
                                        <c:otherwise>
                                        <a class="nav-link" href="#" onclick="location.href = 'surveyAnswerPeerReview.do?questionid=${urlView.nextId}&horseid=${param.horseid}&assignid=${param.assignid}&action=${from}&returl=${urlView.returnUrl}'"><img src="images/icon_arrow_right.png" alt=""/></a>
                                        </c:otherwise>
                                    </c:choose>
                            </div>
                            <div class="clear"></div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
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
    parameters.questionId = '${param.questionid}';
    parameters.msgBoardId = '${peerReview.msgboardId}';
    parameters.ctxPath = "${contextPath}";
    parameters.checkPermission = '${param.checkPermission}';
    parameters.syncStatus = '${param.syncStatus}';
    parameters.folded = '${param.folded}';
    parameters.answerType = ${view.answerType};
    parameters.peerReviewId = ${peerReview.id};
    loadSurveyPeerReview(parameters);
</script>
