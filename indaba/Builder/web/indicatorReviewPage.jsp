<%-- 
    Document   : indicatorReviewPage
    Created on : 2010-5-10, 22:13:51
    Author     : Tiger
--%>

<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<%@page import="com.ocs.indaba.common.Constants"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type">
        <title><indaba:msg key='jsp.indicatorReviewPage.title' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <!--[if lt IE 7]>
            <link href="ie6.css" media="screen" rel="Stylesheet" type="text/css" />
        <![endif]-->
        <link type="text/css" rel="stylesheet" href="css/style2.css"/>
        <link type="text/css" rel="stylesheet" href="css/button.css"/>
        <link type="text/css" rel="stylesheet" href="css/tipTip.css"/>
        <link type="text/css" rel="stylesheet" href="plugins/scrollbar/css/jquery.mCustomScrollbar.css"/>
        <link type="text/css" rel="stylesheet" href="plugins/chosen/chosen.css"/>
        <link type="text/css" rel="stylesheet" href="plugins/uniform/css/uniform.default.css"/>
        <link type="text/css" rel="stylesheet" href="plugins/validate/css/validationEngine.jquery.css"/>
        <link type="text/css" rel="stylesheet" href="css/jquery.fancybox-1.3.4.css" media="screen" />
        <link type="text/css" rel="stylesheet" href="plugins/jquery-ui/css/lightness/jquery-ui-1.10.4.custom.min.css" media="all" />
        <link type="text/css" rel="stylesheet" href="css/jquery.alerts.css"/>
        <link type="text/css" rel="stylesheet" href="plugins/pnotify/jquery.pnotify.default.css"/>
        <link type="text/css" rel="stylesheet" href="plugins/pnotify/jquery.pnotify.default.icons.css"/>

        <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="plugins/scrollbar/js/jquery.mousewheel.js"></script>
        <script type="text/javascript" src="plugins/scrollbar/js/jquery.mCustomScrollbar.js"></script>
        <script type="text/javascript" src="plugins/scrollbar/js/jquery.mousewheel.js"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="plugins/uniform/js/jquery.uniform.js"></script>
        <script type="text/javascript" src="plugins/validate/js/languages/jquery.validationEngine-en.js"></script>
        <script type="text/javascript" src="plugins/validate/js/jquery.validationEngine.js"></script>
        <script type="text/javascript" src="plugins/chosen/jquery.chosen.js"></script>
        <script type="text/javascript" src="js/jquery.alerts.js"></script>
        <script type="text/javascript" src="plugins/jquery-ui/js/jquery-ui-1.10.4.custom.min.js"></script>
        <script type="text/javascript" src="js/jquery.fancybox-1.3.4.pack.js"></script>
        <script type="text/javascript" src="js/jquery.tipTip.js"></script>
        <script type="text/javascript" src="js/jquery.scrollTo-1.4.2-min.js"></script>
        <script type="text/javascript" src="js/jquery.localscroll-1.2.7-min.js"></script>
        <script type="text/javascript" src="plugins/pnotify/jquery.pnotify.min.js"></script>
        <script type="text/javascript" src="js/jquery.highlightFade.js"></script>
        <script type="text/javascript" src="js/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="js/discussions.js"></script>
        <script type="text/javascript" src="js/jquery.autogrow-textarea.js"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/json2.js"></script>
        <script type="text/javascript" src="js/button.js"></script>
        <script type="text/javascript" src="js/assignment.js"></script>
        <script type="text/javascript" src="js/surveyReview.js"></script>
        <script type="text/javascript" src="ckeditor/ckeditor.js"></script>
        <script type="text/javascript" src="ckeditor/adapters/jquery.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/ocs-notify.js"></script>
        <script type="text/javascript" src="plugins/dialog/ocs-dlg.js"></script>
        <script type="text/javascript" src="js/surveyTableQuestion.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                // load question list
                loadSurveyAnswerQuestionSidebar('${contextPath}', 'surveyAnswerReview.do', '${param.horseid}', '${param.assignid}', '0', '${view.surveyAnswerId}', '${param.returl}');
                // loadPeerReviewDisagreementList("${contextPath}", "staffDiscussion", "${param.horseid}", "${param.assignid}", "${param.questionid}", "${param.answerid}", "${action}", "${returl}");

                // fix tip's position
                $('.popup').hover(
                        function(e) {
                            $('#tipinfo').show();
                        },
                        function(e) {
                            $('#tipinfo').hide();
                        });

                 $('form').validationEngine();
            });
        </script>
    </head>
    <body>
        <c:set var="hidePRDiscussion" value="true" />
        <indaba:view prjid="${prjid}" uid="${uid}" right="read survey peer review discussions">
            <c:set var="hidePRDiscussion" value="false" />
        </indaba:view>

        <div id="indaba" style="position: relative;">
            <c:set var="active" value="yourcontent" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper">
                <div id="main-column">
                    <jsp:include page="indicatorDisplay.jsp" flush="true" >
                        <jsp:param name="editable" value="true" />
                        <jsp:param name="sourcesEditable" value="true" />
                        <jsp:param name="commentsEditable" value="true" />
                    </jsp:include>
                    <jsp:include page="indicatorAddTag.jsp" flush="true" />

                    <jsp:include page="surveyAnswerInternalDiscussion.jsp" flush="true" >
                        <jsp:param value="1" name="checkPermission" />
                        <jsp:param value="1" name="syncStatus"/>
                        <jsp:param value="1" name="folded"/>
                    </jsp:include>
                    <jsp:include page="surveyAnswerReview.jsp" flush="true" >
                        <jsp:param value="1" name="syncStatus"/>
                        <jsp:param value="0" name="folded"/>
                    </jsp:include>
                    <jsp:include page="surveyAnswerPRReviews.jsp" flush="true" >
                        <jsp:param value="1" name="syncStatus"/>
                        <jsp:param value="1" name="folded"/>
                        <jsp:param value="true" name="opinionEditable"/>
                        <jsp:param value="true" name="commentsEditable"/>
                        <jsp:param value="true" name="answerEditable"/>
                        <jsp:param value="${hidePRDiscussion}" name="hideDiscussion"/>
                    </jsp:include>
                    <div align="center">
                        <c:choose>
                            <c:when test="${urlView.previousId == 0 || param.action == 'surveyReviewResponse.do'}">
                                <a class="nav-link"><img src="images/icon_arrow_left_gray.png" alt=""/></a>
                                </c:when>
                                <c:otherwise>
                                <a class="nav-link" href="#" onclick="location.href = 'surveyAnswerReview.do?questionid=${urlView.previousId}&horseid=${param.horseid}&assignid=${param.assignid}&action=${param.action}&returl=${urlView.returnUrl}'"><img src="images/icon_arrow_left.png" alt=""/></a>
                                </c:otherwise>
                            </c:choose>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <button class="large button blue icon-check" onclick="submitIndicatorReview(this, '${contextPath}', '${param.horseid}', '${param.assignid}', '${param.questionid}', ${view.referType}, ${view.answerType});"><indaba:msg key='common.btn.save' /></button>
                        <span id="saveComplete"></span>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <c:choose>
                            <c:when test="${urlView.nextId == 0 || param.action == 'surveyReviewResponse.do'}">
                                <a class="nav-link"><img src="images/icon_arrow_right_gray.png" alt=""/></a>
                                </c:when>
                                <c:otherwise>
                                <a class="nav-link" href="#" onclick="location.href = 'surveyAnswerReview.do?questionid=${urlView.nextId}&horseid=${param.horseid}&assignid=${param.assignid}&action=${param.action}&returl=${urlView.returnUrl}'"><img src="images/icon_arrow_right.png" alt=""/></a>
                                </c:otherwise>
                            </c:choose>
                    </div>
                </div>
                <div id="sidebar">
                    <div id="sidebar_content">
                        <indaba:view prjid="${prjid}" uid="${uid}" right="see survey original answers">
                            <a class="origAnswer" href="surveyAnswerOriginal.do?horseid=${param.horseid}&answerid=${view.surveyAnswerId}" target="_new"><indaba:msg key='common.btn.origanswer' /></a>
                            <br/><br/>
                        </indaba:view>
                        <jsp:include page="surveyAnswerActionBar_inc.jsp" flush="true">
                            <jsp:param name="action" value="${param.action}"/>
                        </jsp:include>
                        <jsp:include page="surveyAnswerNavigation.jsp" flush="true">
                            <jsp:param name="action" value="surveyAnswerReview.do"/>
                            <jsp:param name="from" value="${param.action}"/>
                        </jsp:include>
                        <jsp:include page="targetSidebar.jsp" flush="true" />
                        <jsp:include page="notesSidebar.jsp" flush="true" >
                            <jsp:param value="${horseid}" name="horseid"/>
                            <jsp:param value="${questionid}" name="questionid"/>
                        </jsp:include>
                        <jsp:include page="groupsSidebar.jsp" flush="true">
                            <jsp:param value="${view.initialFlagGroupId}" name="initialFlagGroupId"/>
                            <jsp:param value="8" name="taskType"/>
                        </jsp:include>
                        <div id="problem_list"></div>
                    </div>
                </div>
            </div>
            
        </div>
        <jsp:include page="footer.jsp" flush="true" />
        <script type="text/javascript" src="js/ocs_scroll.js"></script>
        <script type="text/javascript" src="js/surveyTableQuestion.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                setQuestionTypeTable();
            });
        </script>
    </body>
</html>


