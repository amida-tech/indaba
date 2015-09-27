<%--
    Document   : surveyAnswer
    Created on : 05 15, 2010, 4:34:20 PM
    Author     : luwb
--%>

<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <title><indaba:msg key='jsp.answerDetails.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <link type="text/css" rel="stylesheet" href="css/style2.css"/>
        <link type="text/css" rel="stylesheet" href="css/button.css"/>
        <link type="text/css" rel="stylesheet" href="css/jquery.alerts.css"/>
        <link type="text/css" rel="stylesheet" href="plugins/uniform/css/uniform.default.css"/>
        <link type="text/css" rel="stylesheet" href="plugins/validate/css/validationEngine.jquery.css"/>
        <!--[if lt IE 7]>
            <link href="ie6.css" media="screen" rel="Stylesheet" type="text/css" />
        <![endif]-->
        <link type="text/css" rel="stylesheet" href="css/tipTip.css"/>
        <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="plugins/uniform/js/jquery.uniform.js"></script>
        <script type="text/javascript" src="plugins/validate/js/languages/jquery.validationEngine-en.js"></script>
        <script type="text/javascript" src="plugins/validate/js/jquery.validationEngine.js"></script>
        <script type="text/javascript" src="js/jquery.alerts.js"></script>
        <script type="text/javascript" src="js/jquery.tipTip.js"></script>
        <script type="text/javascript" src="js/jquery.scrollTo-1.4.2-min.js"></script>
        <script type="text/javascript" src="js/jquery.localscroll-1.2.7-min.js"></script>
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
        <script type="text/javascript" src="js/surveyTableQuestion.js"></script>
        <script type="text/javascript">
            var sidebar, toppos;
            $(document).ready(function() {               
                // fix tip's position
                $('.popup').hover(
                        function(e) {
                            $('#tipinfo').show();
                        },
                        function(e) {
                            $('#tipinfo').hide();
                        }
                );
            });
        </script>
        <script type="text/javascript">
            function loadSurveyAnswerQuestionSidebarWithoutAction(ctx, horseid, assignid, answerid, disp_answerid, returl) {
                var parameters = new Object();
                parameters.horseid = horseid;
                parameters.assignid = assignid;
                parameters.answerid = answerid;
                parameters.disp_answerid = disp_answerid;
                parameters.returl = returl;
                parameters.reviewResponse = "TRUE";

                $.ajax({
                    type: "POST",
                    url: ctx + "/surveyProblemList.do",
                    data: parameters,
                    cache: false,
                    async: false,
                    success: function(result) {
                        $("#problem_list")[0].innerHTML = result;
                    },
                    error: function(result) {
                        alert(result);
                    }
                });
            }
        </script>
        <script type="text/javascript">
            function submitSurvey(surveyAnswerId) {
                var parameters = {};
                parameters.surveyAnswerId= surveyAnswerId;
                parameters.comments = $('textarea[name=comments]').val();

                parameters.answerType = ${surveyAnswer.answerType};
                parameters.referType = ${surveyAnswer.referType};

                switch (parameters.answerType) {
                    case ANSWER_TYPE_SINGLE_CHOICE:
                        {
                            parameters.selection = $('input[name=selection]:checked').val();
                            if (typeof(parameters.selection) === "undefined") {
                                //jAlert($.i18n.message('common.js.alert.askscoreoption'));
                                ocsShowNotify({
                                    title: $.i18n.message('common.js.alert.title.error'),
                                    text: $.i18n.message('common.js.alert.askscoreoption'),
                                    type: 'error'
                                });
                                return;
                            }
                        }
                        break;
                    case ANSWER_TYPE_MULTI_CHOICE:
                        {
                            parameters.selection = "";
                            $('input[name=selection]:checked').each(function() {
                                parameters.selection += $(this).val();
                                parameters.selection += ",";
                            });
                            if (parameters.selection === "") {
                                //jAlert($.i18n.message('common.js.alert.askscoreoption'));
                                ocsShowNotify({
                                    title: $.i18n.message('common.js.alert.title.error'),
                                    text: $.i18n.message('common.js.alert.askscoreoption'),
                                    type: 'error'
                                });
                                return;
                            }
                        }
                        break;
                    case ANSWER_TYPE_TEXT:
                        {
                            parameters.selection = $('textarea[name=selection]:visible').val();
                            if (parameters.selection === "") {
                                jAlert($.i18n.message('common.js.alert.inputanswer'));
                                return;
                            }
                        }
                        break;
                    case ANSWER_TYPE_INTEGER:
                    case ANSWER_TYPE_FLOAT:
                        {
                            parameters.selection = $('input[name=selection]:visible').val();
                            if (parameters.selection === "") {
                                jAlert($.i18n.message('common.js.alert.inputanswer'));
                                return;
                            }
                        }
                        break;
                    case ANSWER_TYPE_TABLE:
                        {
                            extractTableQuestionForm(parameters, $('table.qstn-type-tbl'));
                        }
                        break;
                    default:
                        break;
                }

                if (parameters.referType === 0) {
                    parameters.source = $('textarea[name=source]').val().trim();
                    if (parameters.source === "") {
                        jAlert($.i18n.message('common.js.alert.askinputsource'));
                        return;
                    }
                } else if (parameters.referType === 1) {
                    parameters.source = $('input[name=source]:checked').val();
                    if (typeof(parameters.source) === "undefined") {
                        jAlert($.i18n.message('common.js.alert.choicesource'));
                        return;
                    }
                } else {
                    parameters.source = "";
                    $('input[name=source]:checked').each(function() {
                        parameters.source += $(this).val();
                        parameters.source += ",";
                    });
                    if (parameters.source === "") {
                        jAlert($.i18n.message('common.js.alert.choicesource'));
                        return;
                    }
                }
                surveyAnswerId.assignId = ${param.assignid};
                surveyAnswerId.horseId = ${param.horseid};
                surveyAnswerId.type = ${type};

                $.ajax({
                    type: "POST",
                    url: "surveyAnswerSubmit.do",
                    data: parameters,
                    cache: false,
                    async: false,
                    success: function(result) {
                        var rc = JSON.parse(result);
                        jInfo(rc.msg, "Data Submitted", null);
                        if (rc.done) {
                            $('#show_done_button').show();
                        }
                        if (type === 17)
                            loadSurveyAnswerQuestionSidebarWithoutAction('${contextPath}', '${param.horseid}', '${param.assignid}', '0', surveyAnswerId, '${param.returl}');
                    }
                });
            }
        </script>
    </head>

    <body>
        <div id="indaba">
            <c:set var="active" value="yourcontent" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper">
                <div id="main-column">
                    <c:forEach items="${surveyAnswer.categoryViewList}" var="category" varStatus="status">
                        <div class="content">
                            <div class="box"<%--<c:if test="${status.last}">style="background-color:white;"</c:if>--%>>
                                <h3>${category.label}&nbsp;&nbsp;&nbsp;&nbsp;${category.title}</h3>
                            </c:forEach>
                            <form id="uploadForm" <c:if test="${empty surveyAnswer.categoryViewList}">class="survey-answer"</c:if>
                                  method="post" action="surveysubmit.do" enctype="multipart/form-data">
                                    <div class="content survey-answer-content">
                                        <table>
                                            <tr>
                                                <td valign="top" width="1%">
                                                    <b>${surveyAnswer.publicName}:</b>
                                            </td>
                                            <td>
                                                <b>${surveyAnswer.question}</b>
                                            </td>
                                            <c:if test="${tipinfo != null && tipDisplayMethod == 1}">
                                                <td>
                                                    <a id="indicator-tip" href="#" class="tipTip rightDisplay" title="${tipinfo}" onclick="return false;"><img src="images/hint_icon.png" alt="TIP"/></a>
                                                    <script>$(".tipTip").tipTip();</script>
                                                </td>
                                            </c:if>
                                        </tr>
                                    </table>
                                    <c:if test="${tipinfo != null && tipDisplayMethod == 2}">
                                        <div class="tipinfo">${tipinfo}</div>
                                    </c:if>
                                    <c:choose>
                                        <c:when test="${surveyAnswer.answerType eq 6}"> 
                                            <indaba:tableAnswer horseId="${horseid}" 
                                                                mainQuestionId="${questionid}" 
                                                                disabled="false" />
                                        </c:when>
                                        <c:otherwise>
                                            <indaba:simpleAnswer 
                                                answerType="${surveyAnswer.answerType}" 
                                                answerTypeId="${surveyAnswer.answerTypeId}" 
                                                name="selection" 
                                                showAnswer="true" 
                                                answerObjectId="${surveyAnswer.answerObjectId}" 
                                                disabled="false" 
                                                showDefault="true" />
                                        </c:otherwise>
                                    </c:choose>
                                    <br/><br/>
                                    <b><indaba:msg key='common.title.sources' />:</b><br/>${surveyAnswer.refdescrition}<br/>
                                    <indaba:source referenceType="${surveyAnswer.referType}" referenceId="${surveyAnswer.referId}" name="source" showAnswer="true" referenceObjectId="${surveyAnswer.referenceObjectId}" disabled="false"></indaba:source>
                                    <br/><br/>
                                    <!-- include attachment widget -->
                                    <jsp:include page="attachment.jsp" flush="true" >
                                        <jsp:param name="contentType" value="survey" />
                                        <jsp:param name="answerId" value="${surveyAnswer.surveyAnswerId}" />
                                        <jsp:param name="horseId" value="${param.horseid}" />
                                    </jsp:include>
                                    <b><indaba:msg key='common.title.sources' />:</b>
                                    <br/>
                                    <textarea NAME="comments" class="text" ROWS="6" onclick="resetStatus();">${surveyAnswer.comments}</textarea>
                                </div>

                            </form>
                            <c:forEach items="${surveyAnswer.categoryViewList}" var="category">
                            </div>
                        </div>
                    </c:forEach>

                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
