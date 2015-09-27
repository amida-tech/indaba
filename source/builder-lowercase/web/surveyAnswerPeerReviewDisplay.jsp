<%-- 
    Document   : surveyAnswerPeerReviewDisplay
    Created on : Apr 24, 2010, 4:10:18 PM
    Author     : Tiger
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<c:set var="opinionEditable" value="false" />
<c:set var="commentsEditable" value="false" />
<c:set var="answerEditable" value="false" />
<c:set var="hideDiscussion" value="false" />

<c:if test="${param.opinionEditable and peerReview.submitTime ne null}">
    <c:set var="opinionEditable" value="true" />
</c:if>
<c:if test="${param.commentsEditable and peerReview.submitTime ne null}">
    <c:set var="commentsEditable" value="true" />
</c:if>
<c:if test="${param.answerEditable and peerReview.submitTime ne null}">
    <c:set var="answerEditable" value="true" />
</c:if>
<c:if test="${param.hideDiscussion and peerReview.submitTime ne null}">
    <c:set var="hideDiscussion" value="true" />
</c:if>

<c:choose>
    <c:when test="${empty param.contents}">
        <c:set var="contents" value="" />
    </c:when>
    <c:otherwise>
        <c:set var="contents" value="${param.contents}" />
    </c:otherwise>
</c:choose>

<c:if test="${fn:contains(contents, 'G')}">
    <c:set var="opinionEditable" value="true" />
    <c:set var="commentsEditable" value="true" />
    <c:set var="answerEditable" value="true" />
</c:if>
<c:if test="${fn:contains(contents, 'F')}">
    <c:set var="hideDiscussion" value="false" />
</c:if>

<div class="box" id="reviewer-${peerReview.reviewer.userId}">
    <h3>
        ${peerReview.reviewer.displayUsername}
        <a class="toggleVisible" href="#"><img src='images/collapse_icon.png' alt='collapse' /></a>
    </h3>
    <div class="content">
        <div id="reviewOpinion_${peerReview.id}" class="box pr-part-score <c:if test='${param.highlightParts && opinionEditable}'>answer-part-highlighted</c:if>">
            <input type="hidden" name="answerType" value="${view.answerType}"/>
            <h3><indaba:msg key='jsp.surveryAnsPRDisp.opinion' /><a class="toggleVisible" href="#"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
            <div class="content">
                <form autocomplete="false">                    
                    <table>
                        <tr>
                            <td><input type="radio" value="0" <c:if test="${not opinionEditable}"> disabled="disabled"</c:if> <c:if test="${peerReview.opinion eq 0}">checked="true"</c:if> name="opinion" id="opinion_1"><label for="opinion_1"><indaba:msg key='jsp.surveryAnsPRDisp.opinion.1' /></label></td>
                            </tr>
                            <tr>
                                    <td><input type="radio" value="1" <c:if test="${not opinionEditable}"> disabled="disabled"</c:if> <c:if test="${peerReview.opinion eq 1}">checked="true"</c:if> name="opinion" id="opinion_2"><label for="opinion_2"><indaba:msg key='common.msg.opinionagree' /></label></td>
                            </tr>
                            <tr>
                                    <td><input type="radio" value="2" <c:if test="${not opinionEditable}"> disabled="disabled"</c:if> <c:if test="${peerReview.opinion eq 2}">checked="true"</c:if> name="opinion" id="opinion_3"><label for="opinion_3"><indaba:msg key='common.msg.opiniondisagree' /></label></td>
                            </tr>
                            <tr>
                                    <td><input type="radio" value="3" <c:if test="${not opinionEditable}"> disabled="disabled"</c:if> <c:if test="${peerReview.opinion eq 3}">checked="true"</c:if> name="opinion" id="opinion_4"><label for="opinion_4"><indaba:msg key='common.msg.opinionnojudge' /></label></td>
                            </tr>
                            <tr class="opinion1" <c:if test="${peerReview.opinion ne 1}">style="display: none"</c:if>>
                                <td>
                                <indaba:msg key='jsp.surveryAnsPRDisp.opinion1.desc' />
                                <textarea id="comment_${peerReview.id}" name="prcomments" class="text reviewer-comment" <c:if test="${not commentsEditable}">readonly</c:if> rows="10">${peerReview.comments}</textarea>
                                </td>
                            </tr>
                            <tr class="opinion2 <c:if test='${peerReview.opinion ne 2}'>show-then-hide</c:if>" >
                                <td>
                                <indaba:msg key='jsp.surveryAnsPRDisp.opinion2.desc' />
                                <textarea id="comment_${peerReview.id}" name="prcomments" class="text reviewer-comment" <c:if test="${not commentsEditable}">readonly</c:if> rows="10">${peerReview.comments}</textarea>
                                    <br/>
                                    <p>
                                        <b><indaba:msg key='common.msg.suggestedanswer' /></b>
                                    <br/>
                                    <c:choose>
                                        <c:when test="${view.answerType eq 6}">
                                            <c:choose>
                                                <c:when test="${peerReview.surveyPeerReviewVersionId ne 0}">
                                                    <indaba:tableAnswerPeerReviewVersion
                                                        peerReviewVersionId="${peerReview.surveyPeerReviewVersionId}"
                                                        mainQuestionId="${questionid}" />
                                                </c:when>
                                                <c:otherwise>
                                                    <indaba:tableAnswerPeerReview
                                                        peerReviewId="${peerReview.id}"
                                                        mainQuestionId="${questionid}" 
                                                        disabled="${not opinionEditable}" />
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <indaba:simpleAnswer
                                                answerType="${view.answerType}"
                                                answerTypeId="${view.answerTypeId}" 
                                                name="selection_${peerReview.id}" 
                                                showAnswer="true" 
                                                answerObjectId="${peerReview.suggestedAnswerObjectId}" 
                                                disabled="${not opinionEditable}" />
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </td>
                        </tr>
                    </table>                   
                </form>
            </div>
             </div>
        </div>
        <c:if test="${not hideDiscussion}">
            <div id="peerReview_${peerReview.id}" class="box discussion pr-part-discussion <c:if test='${param.highlightParts}'>answer-part-highlighted</c:if>">
            </div>
        </c:if>
    </div>
