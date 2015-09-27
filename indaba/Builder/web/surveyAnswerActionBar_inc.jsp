<%--
    Document   : surveyAnswerActionBar
    Created on : 2010-6-14, 1:00:45
    Author     : luwb
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<div id="scorecard-actionbar-box" class="box">
    <%--
    ${param.action}, ${param.assignid}
    --%>
    <h3><indaba:msg key="common.msg.scorecardActionBar" /></h3>
    <ul class="actionbar">        
        <li class="tipTip actionbar-save" title="<indaba:msg key='common.msg.saveYourChanges'/>"><indaba:msg key='common.btn.save' /></li>
        <c:if test="${(param.action ne 'surveyOverallReview.do') or (param.assignid != 0)}">
            <c:if test="${(param.action eq 'surveyReview.do') or (param.action eq 'surveyPRReview.do')}">
                <li class="tipTip actionbar-send-questions" title="<indaba:msg key='common.msg.sendYourQuestions'/>"><indaba:msg key='common.btn.sendQuestions' /></li>
            </c:if>
            <c:choose>
                <c:when test="${!showIamDone}">
                    <li class="tipTip actionbar-complete-assignment-disabled" title="${toolIntl.inactiveReason}"><indaba:msg key='common.btn.completeAssignment' /></li>
                </c:when>
                <c:otherwise>
                    <li class="tipTip actionbar-complete-assignment" title="${toolIntl.purpose}"><indaba:msg key='common.btn.completeAssignment' /></li>
                </c:otherwise>
            </c:choose>
        </c:if>
    </ul>
</div>
<c:if test="${(param.action eq 'surveyReview.do') or (param.action eq 'surveyPRReview.do')}">
    <jsp:include page="surveySendQuestions_inc.jsp" flush="true" >
        <jsp:param name="cntxtPath" value="${contextPath}" />
        <jsp:param name="targetId" value="${target.id}" />
        <jsp:param name="aUserId" value="${auserid}" />
        <jsp:param name="dueDate" value="${duedate}" />
        <jsp:param name="contents" value="${contents}" />
        <jsp:param name="horseId" value="${param.horseid}" />
        <jsp:param name="assignId" value="${param.assignid}" />
    </jsp:include>
</c:if>

<script type="text/javascript">
    $(function(){
        $('#scorecard-actionbar-box .tipTip').tipTip({'defaultPosition': 'bottom'});
        bindSideBarSaveClickEvent();
        bindSideBarSendQuestionsClickEvent();
        bindSideBarCompleteAssignmentClickEvent();
        fixActionBarHeight();
    });
    
    function enableCompleteAssignment() {
        var completedAssignment = $('#scorecard-actionbar-box ul li.actionbar-complete-assignment-disabled');
        if(completedAssignment) {
            completedAssignment.removeClass('actionbar-complete-assignment-disabled').addClass('actionbar-complete-assignment').attr('title', '${toolIntl.purpose}');
            completedAssignment.tipTip({'defaultPosition': 'bottom'});
            bindSideBarCompleteAssignmentClickEvent();
        }
    }

    function fixActionBarHeight() {
        $('#scorecard-actionbar-box').css('height', (35 + $('#scorecard-actionbar-box ul.actionbar li:visible').length * 50) + 'px');
    }
    function loadProblemListCallback() {
        var sendQustnBtn = $('#scorecard-actionbar-box ul.actionbar li.actionbar-send-questions');
        if($('#questions-box .question-box-item').length > 0) {
            sendQustnBtn.show();
        } else {
            sendQustnBtn.hide();
        }
        fixActionBarHeight();
    }
    function bindSideBarSaveClickEvent() {
        var $saveChanges = $('#scorecard-actionbar-box ul.actionbar li.actionbar-save');
        $saveChanges.unbind('click');
        $saveChanges.bind('click', function (){
    <c:choose>
        <c:when test="${param.action eq 'surveyCreate.do'}">
                    submitIndicatorAnswer(this, '${contextPath}', '${param.horseid}', '${param.assignid}', '${param.questionid}', ${surveyAnswer.answerType}, ${surveyAnswer.referType});
        </c:when>
        <c:when test="${param.action eq 'surveyEdit.do'}">
                    submitIndicatorEdit(this, '${contextPath}', '${param.horseid}', '${param.assignid}', '${param.questionid}', ${surveyAnswer.answerType}, ${surveyAnswer.referType});
        </c:when>
        <c:when test="${param.action eq 'surveyReview.do'}">
                    submitIndicatorReview(this, '${contextPath}', '${param.horseid}', '${param.assignid}', '${param.questionid}', ${view.referType}, ${view.answerType});
        </c:when>
        <c:when test="${param.action eq 'surveyPeerReview.do'}">
                    saveSurveyAnswerPeerReview({ctxPath:'${contextPath}', horseId:${param.horseid}, assignId:${param.assignid}, questionId: ${param.questionid}, answerType: ${view.answerType}, containerName: 'peerReviewContent'});
        </c:when>
        <c:when test="${param.action eq 'surveyPRReview.do'}">
                    submitIndicatorPRReview(this, '${contextPath}', '${param.horseid}', '${param.assignid}', '${param.questionid}', ${view.referType}, ${view.answerType});
        </c:when>
        <c:when test="${param.action eq 'surveyOverallReview.do'}">
                    submitIndicatorOverallReview(this, '${contextPath}', '${param.horseid}', '${param.assignid}', '${param.questionid}', ${surveyAnswer.answerType}, ${surveyAnswer.referType});
        </c:when>
        <c:when test="${param.action eq 'surveyReviewResponse.do'}">
                    submitIndicatorReviewResponse(this, '${contextPath}', '${param.horseid}', '${param.assignid}', '${param.questionid}', ${surveyAnswer.answerType}, ${surveyAnswer.referType});
                    loadSurveyAnswerQuestionSidebarWithoutAction('${contextPath}', '${param.horseid}', '${param.assignid}', '0', '${param.questionid}', '${param.returl}');
        </c:when>
        <c:when test="${param.action eq 'surveyFlagResponse.do'}">
                    submitIndicatorFlagResponse(this, '${contextPath}', '${param.horseid}', '${param.assignid}', '${param.questionid}', ${surveyAnswer.answerType}, ${surveyAnswer.referType});
        </c:when>
        <c:otherwise>
                    submitIndicatorAnswer(this, '${contextPath}', '${param.horseid}', '${param.assignid}', '${param.questionid}', ${surveyAnswer.answerType}, ${surveyAnswer.referType});
        </c:otherwise>
    </c:choose>
                return false;
            });
        }
        function bindSideBarSendQuestionsClickEvent() {
            var $sendQuestions = $('#scorecard-actionbar-box ul.actionbar li.actionbar-send-questions');
            var sendBox =  $('#send-box');
            $sendQuestions.fancybox({
                href:"#send-box",
                onStart: function(){
                    resetRespondentFilter(sendBox, '${target.id}');
                }
            });
        }
        function bindSideBarCompleteAssignmentClickEvent() {
            var $completeAssignment = $('#scorecard-actionbar-box ul.actionbar li.actionbar-complete-assignment');
            $completeAssignment.unbind('click');
            $completeAssignment.bind('click',function(){
    <c:choose>
        <c:when test="${param.action eq 'surveyCreate.do'}">
                    submitDone('${contextPath}', '${param.horseid}', '${param.assignid}', '${returl}');
        </c:when>
        <c:when test="${param.action eq 'surveyReview.do'}">
                    trySubmitSurveyReview('${contextPath}', '${param.horseid}', '${param.assignid}', '${returl}');
        </c:when>
        <c:when test="${param.action eq 'surveyPeerReview.do'}">
                    submitSurveyPeerReviewAssignment('${contextPath}', '${param.horseid}', '${param.assignid}', '${returl}');
        </c:when>
        <c:when test="${param.action eq 'surveyPRReview.do'}">
                    trySubmitSurveyPRReview('${contextPath}', '${param.horseid}', '${param.assignid}');
        </c:when>
        <c:when test="${param.action eq 'surveyOverallReview.do'}">
                    submitDone('${contextPath}', '${param.horseid}', '${param.assignid}', '${returl}');
        </c:when>
        <c:otherwise>
                    submitDone('${contextPath}', '${param.horseid}', '${param.assignid}', '${returl}');
        </c:otherwise>
    </c:choose>
                return false;
            });
        }
        function sendQuestionsCallback(params) {
            $.fancybox.close();
        }
</script>