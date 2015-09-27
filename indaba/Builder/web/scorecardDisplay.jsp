<%-- 
    Document   : scoreboardDisplay
    Created on : 2010-3-24, 6:54:35
    Author     : Luke Shi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<html>
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type">
        <meta http-equiv="Pragma" content="no-cache" />
        <meta http-equiv="Cache-Control" content="no-cache" />
        <meta http-equiv="Expires" content="0" />
        <title><indaba:msg key='jsp.scorecardDisplay.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="plugins/chosen/chosen.css"/>
        <link type="text/css" rel="stylesheet" href="plugins/uniform/css/uniform.default.css"/>
        <link type="text/css" rel="stylesheet" href="css/jquery.fancybox-1.3.4.css" media="screen" />
        <link type="text/css" rel="stylesheet" href="plugins/jquery-ui/css/lightness/jquery-ui-1.10.4.custom.min.css" media="all" />
        <link type="text/css" rel="stylesheet" href="css/jquery.alerts.css"/>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>        
        <link type="text/css" rel="stylesheet" href="css/style2.css"/>
        <link type="text/css" rel="stylesheet" href="css/button.css"/>
        <link type="text/css" rel="stylesheet" href="css/tipTip.css"/>
        <link type="text/css" rel="stylesheet" href="plugins/pnotify/jquery.pnotify.default.css"/>
        <link type="text/css" rel="stylesheet" href="plugins/pnotify/jquery.pnotify.default.icons.css"/>

        <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="plugins/uniform/js/jquery.uniform.js"></script>
        <script type="text/javascript" src="plugins/chosen/jquery.chosen.js"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="js/jquery.tipTip.js"></script>
        <script type="text/javascript" src="js/jquery.highlightFade.js"></script>
        <script type="text/javascript" src="js/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="js/discussions.js"></script>
        <script type="text/javascript" src="js/jquery.autogrow-textarea.js"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/surveyReview.js"></script>
        <script type="text/javascript" src="js/json2.js"></script>
        <script type="text/javascript" src="js/button.js"></script>
        <script type="text/javascript" src="js/assignment.js"></script>
        <script type="text/javascript" src="js/contentGeneral.js"></script>
        <script type="text/javascript" src="ckeditor/ckeditor.js"></script>
        <script type="text/javascript" src="ckeditor/adapters/jquery.js"></script>
        <script type="text/javascript" src="js/scorecardNavigation.js"></script>
        <script type="text/javascript" src="js/indicatorSearch.js"></script>
        <script type="text/javascript" src="js/jquery.alerts.js"></script>
        <script type="text/javascript" src="plugins/jquery-ui/js/jquery-ui-1.10.4.custom.min.js"></script>
        <script type="text/javascript" src="js/jquery.fancybox-1.3.4.pack.js"></script>
        <script type="text/javascript" src="plugins/pnotify/jquery.pnotify.min.js"></script>
        <script type="text/javascript" src="js/ocs-notify.js"></script>
        <script type="text/javascript" src="plugins/dialog/ocs-dlg.js"></script>

        <script type="text/javascript">
            $(function() {   
                $('a.send-popup').each(function(){
                    var sendBox =  $('#send-box');
                    $(this).fancybox({
                        onStart: function(){
                            resetRespondentFilter(sendBox, '${target.id}');
                        }
                    })
                });
            });
            
            $(document).bind('commonWidgetReady', function(event, num){
                if (num === 0){
                    $('#cntgeneral').remove();
                    $('hr').each(function(index, hr){
                        if ($(hr).prev('.wrapper').children().length === 0){
                            $(hr).prev('.wrapper').remove();
                            $(hr).hide();
                        }
                    });
                }
            });
            
            buttonDeffered = true;
            
            function checkChecked() {
                var contents = "";
                $('input[name=content]:checked').each(function(){
                    contents += $(this).val();
                });
                
                if (contents === "") {
                    document.getElementById("submitBtn").style.visibility = 'hidden';
                } else {
                    document.getElementById("submitBtn").style.visibility = 'visible';
                }
            }
        </script>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="yourcontent" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <c:if test="${assignid == null}">
                <c:set var="assignid" value="-1" />
            </c:if>

            <!--jsp:include page="contentGeneral.jsp" flush="true" /-->
            <script type="text/javascript">
                loadContentGeneral(${horseid}, ${assignid});
            </script>    
            <div class="wrapper">
                <jsp:include page="discussion.jsp" flush="true" >
                    <jsp:param name="checkPermission" value="1"/>
                    <jsp:param value="1" name="folded"/>
                </jsp:include>
                <c:choose>
                    <c:when test="${action eq 'surveyDisplay.do' or action eq 'display'}">
                        <jsp:include page="surveyStaffDiscussion.jsp" flush="true" >
                            <jsp:param name="checkPermission" value="1"/>
                            <jsp:param value="1" name="folded"/>
                        </jsp:include>
                    </c:when>
                    <c:when test="${action eq 'surveyReview.do'}">
                        <jsp:include page="surveyStaffDiscussion.jsp" flush="true" >
                            <jsp:param name="checkPermission" value="0"/>
                            <jsp:param value="1" name="syncStatus"/>
                            <jsp:param value="1" name="folded"/>
                        </jsp:include>
                    </c:when>
                    <c:when test="${action eq 'surveyPRReview.do'}">
                        <jsp:include page="surveyStaffDiscussion.jsp" flush="true" >
                            <jsp:param name="checkPermission" value="0"/>
                            <jsp:param value="1" name="syncStatus"/>
                            <jsp:param value="1" name="folded"/>
                        </jsp:include>
                    </c:when>
                    <c:when test="${action eq 'surveyPeerReview.do'}">
                        <jsp:include page="surveyStaffDiscussion.jsp" flush="true" >
                            <jsp:param name="checkPermission" value="1"/>
                            <jsp:param value="1" name="syncStatus"/>
                            <jsp:param value="1" name="folded"/>
                            <jsp:param value="spr" name="tool"/>
                        </jsp:include>
                    </c:when>
                    <c:otherwise>
                        <jsp:include page="surveyStaffDiscussion.jsp" flush="true" >
                            <jsp:param value="1" name="syncStatus"/>
                            <jsp:param value="1" name="folded"/>
                        </jsp:include>
                    </c:otherwise>
                </c:choose>
            </div>

            <c:if test="${not empty problems}">
                <hr/>
                <div class="wrapper">
                    <div class="box">
                        <h3><a name="answerquestions"><indaba:msg key='jsp.scorecardNav.questionList' /></a>
                            <span id="respondent" style="color: #666">
                                <c:if test='${action == "surveyReview.do" || action == "surveyPRReview.do"}'>
                                    <c:if test="${not empty respondent}"> - <indaba:msg key='common.msg.assignedRespondent' />&nbsp;${respondent.displayUsername}</c:if>
                                </c:if>
                            </span>
                            <a href="#answerquestions" class="toggleVisible"><img src='images/collapse_icon.png' alt="" /></a></h3>
                        <div class="content">
                            <div id="questionlist">
                                <jsp:include page="surveyReviewQuestions.jsp" flush="true" />
                            </div>

                            <div id="submitButton" align="center">
                                <c:if test='${action == "surveyReview.do" || action == "surveyPRReview.do"}'>
                                    <jsp:include page="surveySendQuestions_inc.jsp" flush="true" >
                                        <jsp:param name="cntxtPath" value="${contextPath}" />
                                        <jsp:param name="targetId" value="${target.id}" />
                                        <jsp:param name="aUserId" value="${auserid}" />
                                        <jsp:param name="duedate" value="${duedate}" />
                                        <jsp:param name="contents" value="${contents}" />
                                        <jsp:param name="horseId" value="${horseid}" />
                                        <jsp:param name="assignId" value="${assignid}" />
                                    </jsp:include>
                                    <a class="send-popup" href="#send-box">
                                        <button class="large button blue icon-check"><indaba:msg key='jsp.scorecardNav.sendQuestions' /></button>
                                    </a>
                                </c:if>
                                <span id="submitDone"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>

            <hr/>
            <div class="wrapper">
                <script type="text/javascript">
                    loadScorecardNavigation({horseid: "${horseid}", assignid: "${assignid}", action: "${action}", returl: "${returl}"});
                    // loadPeerReviewDisagreementList("${contextPath}", "staffDiscussion", "${param.horseid}", "${param.assignid}", "${param.questionid}", "${param.answerid}", "${action}", "${returl}");
                    function sendQuestionsCallback(params) {
                        $('span#respondent').html(' - ' + $.i18n.message('common.msg.assignedRespondent') + '&nbsp;' + params.rusername);
                        $.fancybox.close();
                    }
                </script>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>

