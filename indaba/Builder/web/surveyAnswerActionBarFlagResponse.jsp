<%--
    Document   : surveyAnswerActionBarFlagResponse
    Created on : 2014-04-03
    Author     : yc06x
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<div id="scorecard-actionbar-box" class="box flag-response-box">
    <h3><indaba:msg key="common.msg.scorecardActionBar" /></h3>
    <ul class="actionbar">
        <li class="tipTip actionbar-save" title="<indaba:msg key='common.msg.saveYourChanges'/>"><indaba:msg key='common.btn.save' /></li>
    </ul>
</div>

<script type="text/javascript">
    $(function(){
        bindSideBarSaveClickEvent();
        fixActionBarHeight();
    });


    function fixActionBarHeight() {
        $('#scorecard-actionbar-box').css('height', (35 + $('#scorecard-actionbar-box ul.actionbar li:visible').length * 50) + 'px');
    }

    function bindSideBarSaveClickEvent() {
        var $saveChanges = $('#scorecard-actionbar-box ul.actionbar li.actionbar-save');
        $saveChanges.unbind('click');
        $saveChanges.bind('click', function (){
            submitIndicatorFlagResponse(this, '${contextPath}', '${param.horseid}', '${assignid}', '${param.questionid}', ${surveyAnswerView.answerType}, ${surveyAnswerView.referType});
            return false;
        });
    }
    
</script>