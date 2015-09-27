<%--
    Document   : surveyAnswer
    Created on : 05 15, 2010, 4:34:20 PM
    Author     : luwb
--%>

<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title><indaba:msg key='jsp.surveyAnswer.title' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <link type="text/css" rel="stylesheet" href="css/style2.css"/>
        <link type="text/css" rel="stylesheet" href="css/button.css"/>
        <!--[if lt IE 7]>
            <link href="ie6.css" media="screen" rel="Stylesheet" type="text/css" />
        <![endif]-->
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
        <script type="text/javascript" src="plugins/scrollbar/js/jquery.mCustomScrollbar.js"></script>
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
        <script type="text/javascript" src="js/surveyReview.js"></script>
        <script type="text/javascript" src="js/json2.js"></script>
        <script type="text/javascript" src="js/button.js"></script>
        <script type="text/javascript" src="js/assignment.js"></script>
        <script type="text/javascript" src="js/contentGeneral.js"></script>
        <script type="text/javascript" src="ckeditor/ckeditor.js"></script>
        <script type="text/javascript" src="ckeditor/adapters/jquery.js"></script>
        <script type="text/javascript" src="js/scorecardNavigation.js"></script>
        <script type="text/javascript" src="js/indicatorSearch.js"></script>
        <script type="text/javascript" src="js/ocs-notify.js"></script>
        <script type="text/javascript" src="plugins/dialog/ocs-dlg.js"></script>
        <script type="text/javascript" src="js/surveyTableQuestion.js"></script>
        <script type="text/javascript">
            var sidebar, toppos;
            $(document).ready(function() {
                // load question list
                if (${type} === 17)
                    loadSurveyAnswerQuestionSidebarWithoutAction('${contextPath}', '${param.horseid}', '${param.assignid}', '0', '${view.surveyAnswerId}', '${param.returl}');

                $('.popup').hover(
                        function(e) {
                            $('#tipinfo').show();
                        },
                        function(e) {
                            $('#tipinfo').hide();
                        }
                );
                $('form').validationEngine();               
            });
        </script>
    </head>

    <body>
        <c:set var="hidePRDiscussion" value="true" />
    <indaba:view prjid="${prjid}" uid="${uid}" right="read survey peer review discussions">
        <c:set var="hidePRDiscussion" value="false" />
    </indaba:view>

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
                                <input type="hidden" name="horseid" value="${horseid}" />
                            <div class="content survey-answer-content">
                                <div class="answer-part-score">
                                <table>
                                    <tr>
                                        <td valign="top" width="1%" nowrap>
                                            <b>${surveyAnswer.publicName}:</b>
                                        </td>
                                        <td>
                                            <b>${surveyAnswer.question}</b>
                                        </td>
                                        <c:if test="${tipinfo != null && tipDisplayMethod == 1}">
                                            <td>
                                                <a id="indicator-tip" href="#" class="tipTip rightDisplay" title="${fn:replace(tipinfo, "\"", '&quot;')}" onclick="return false;"><img src="images/hint_icon.png" alt="TIP"/></a>
                                                <script>$(".tipTip").tipTip();</script>
                                            </td>
                                        </c:if>
                                    </tr>
                                </table>
                                <c:if test="${tipinfo != null && tipDisplayMethod == 2}">
                                    <div class="tipinfo">${tipinfo}</div>
                                </c:if>
                                <c:choose>
                                    <c:when test="${surveyAnswer.answerType eq 6}"> <%-- table question --%>
                                        <indaba:tableAnswer horseId="${horseid}" 
                                                            mainQuestionId="${questionid}"
                                                            contents="${contents}"
                                                            disabled="${param.action eq 'surveyEdit.do'}" />
                                    </c:when>
                                    <c:otherwise> <%-- simple question(int, float, single/multi choice, text) --%>
                                        <indaba:simpleAnswer answerType="${surveyAnswer.answerType}" 
                                                             answerTypeId="${surveyAnswer.answerTypeId}" 
                                                             name="selection" 
                                                             showAnswer="true" 
                                                             answerObjectId="${surveyAnswer.answerObjectId}" 
                                                             disabled="${param.action eq 'surveyEdit.do'}" 
                                                             showDefault="true" 
                                                             contents="${contents}" />
                                    </c:otherwise>
                                </c:choose>
                                </div>
                                <br/>
                                <div class="answer-part-source">
                                <b><indaba:msg key='common.title.sources' />:</b><br/>${surveyAnswer.refdescrition}<br/>
                                <indaba:source referenceType="${surveyAnswer.referType}" referenceId="${surveyAnswer.referId}" name="source" showAnswer="true" referenceObjectId="${surveyAnswer.referenceObjectId}" disabled="false" contents="${contents}"></indaba:source>
                                </div>
                                <br/><br/>

                                <c:if test="${empty contents || fn:contains(contents, 'E')}">
                                    <!-- include attachment widget -->
                                    <c:choose>
                                        <c:when test="${param.action eq 'surveyCreate.do' || param.action eq 'surveyEdit.do' || param.action eq 'surveyReviewResponse.do'}">
                                            <c:set var="requireAttach" value="true" />
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="requireAttach" value="false" />
                                        </c:otherwise>
                                    </c:choose>
                                    <div class="answer-part-attachment">
                                    <jsp:include page="attachment.jsp" flush="true" >
                                        <jsp:param name="contentType" value="survey" />
                                        <jsp:param name="answerId" value="${surveyAnswer.surveyAnswerId}" />
                                        <jsp:param name="uploaderWidth" value="322px" />
                                        <jsp:param name="filenameWidth" value="214px" />
                                        <jsp:param name="descWidth" value="312px" />
                                        <jsp:param name="required" value="${requireAttach}" />
                                        <jsp:param name="horseId" value="${param.horseid}" />
                                    </jsp:include>
                                    </div>
                                </c:if>

                                <div class="answer-part-comment">
                                <b><indaba:msg key='common.title.comments' />:</b>
                                <br/>
                                <textarea class="text" NAME="comments" ROWS="6" onclick="resetStatus();"
                                          <c:if test="${not empty contents && not fn:contains(contents, 'D')}">
                                              READONLY="readonly"
                                          </c:if>
                                          >${surveyAnswer.comments}</textarea>
                                </div>

                            </div>
                        </form>
                        <c:forEach items="${surveyAnswer.categoryViewList}" var="category">
                        </div>
                    </div>
                </c:forEach>
                <div id="addtag" class="content">
                    <jsp:include page="indicatorAddTag.jsp" flush="true"/>
                </div>

                    <div class="clear"></div>

                    <jsp:include page="surveyAnswerInternalDiscussion.jsp" flush="true">
                        <jsp:param name="checkPermission" value="1"/>
                        <jsp:param name="syncStatus" value="1"/>
                        <jsp:param value="1" name="folded"/>
                    </jsp:include>

                    <c:choose>
                        <c:when test="${not empty contents && not fn:contains(contents, 'A')}">
                            <jsp:include page="surveyAnswerReview.jsp" flush="true" >
                                <jsp:param name="checkPermission" value="1"/>
                                <jsp:param value="1" name="syncStatus"/>
                                <jsp:param value="0" name="folded"/>
                                <jsp:param value="disabled" name="disabled"/>
                            </jsp:include>
                        </c:when>
                        <c:otherwise>
                            <jsp:include page="surveyAnswerReview.jsp" flush="true" >
                                <jsp:param value="1" name="syncStatus"/>
                                <jsp:param value="0" name="folded"/>
                            </jsp:include>
                        </c:otherwise>
                    </c:choose>

                    <c:if test="${showPeer}">
                        <c:choose>
                            <c:when test="${param.action eq 'surveyOverallReview.do'}">
                                <jsp:include page="surveyAnswerPRReviews.jsp" flush="true" >
                                    <jsp:param value="1" name="syncStatus"/>
                                    <jsp:param value="1" name="folded" />
                                    <jsp:param value="true" name="opinionEditable"/>
                                    <jsp:param value="true" name="commentsEditable"/>
                                    <jsp:param value="true" name="answerEditable"/>
                                    <jsp:param value="${hidePRDiscussion}" name="hideDiscussion"/>
                                    <jsp:param value="${param.assignid eq '0'}" name="superedit"/>
                                </jsp:include>
                            </c:when>
                            <c:when test="${param.action eq 'surveyEdit.do'}">
                                <jsp:include page="surveyAnswerPRReviews.jsp" flush="true" >
                                    <jsp:param value="1" name="syncStatus"/>
                                    <jsp:param value="1" name="folded" />
                                    <jsp:param value="true" name="commentsEditable"/>
                                    <jsp:param value="${hidePRDiscussion}" name="hideDiscussion"/>
                                </jsp:include>
                            </c:when>
                            <c:when test="${param.action eq 'surveyReviewResponse.do'}">
                                <jsp:include page="surveyAnswerPeerReviewsForReviewReponse.jsp" flush="true" >
                                    <jsp:param value="1" name="syncStatus"/>
                                    <jsp:param value="1" name="folded" />
                                    <jsp:param value="${contents}" name="contents" />
                                    <jsp:param value="true" name="hideDiscussion"/>
                                </jsp:include>
                            </c:when>
                            <c:otherwise>
                                <jsp:include page="surveyAnswerPRReviews.jsp" flush="true" >
                                    <jsp:param value="1" name="syncStatus"/>
                                    <jsp:param value="1" name="folded" />
                                    <jsp:param value="${hidePRDiscussion}" name="hideDiscussion"/>
                                </jsp:include>
                            </c:otherwise>
                        </c:choose>
                    </c:if>

                    <div align="center" style="margin-bottom:10px;">
                        <c:if test="${from != 'surveyReviewResponse.do'}">
                            <c:choose>
                                <c:when test="${urlView.previousId == 0}">
                                    <a class="nav-link"><img src="images/icon_arrow_left_gray.png" alt=""/></a>
                                    </c:when>
                                    <c:otherwise>
                                    <a class="nav-link" href="#" onclick="location.href = 'surveyAnswer.do?questionid=${urlView.previousId}&horseid=${param.horseid}&assignid=${param.assignid}&action=${param.action}&returl=${urlView.returnUrl}'"><img src="images/icon_arrow_left.png" alt=""/></a>
                                    </c:otherwise>
                                </c:choose>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        </c:if>
                        <c:choose>
                            <c:when test="${param.action eq 'surveyOverallReview.do'}">
                                <button class="large button blue icon-check" onclick="submitIndicatorOverallReview(this, '${contextPath}', '${param.horseid}', '${param.assignid}', '${param.questionid}', ${surveyAnswer.answerType}, ${surveyAnswer.referType});">
                                    <indaba:msg key='common.btn.save' />
                                </button>
                            </c:when>
                            <c:when test="${param.action eq 'surveyReviewResponse.do'}">
                                <button class="large button blue icon-check"
                                        onclick="submitIndicatorReviewResponse(this, '${contextPath}', '${param.horseid}', '${param.assignid}', '${param.questionid}', ${surveyAnswer.answerType}, ${surveyAnswer.referType});
                                            loadSurveyAnswerQuestionSidebarWithoutAction('${contextPath}', '${param.horseid}', '${param.assignid}', '0', '${param.questionid}', '${param.returl}');">
                                    <indaba:msg key='common.btn.save' />
                                </button>
                            </c:when>
                            <c:when test="${param.action eq 'surveyEdit.do'}">
                                <button class="large button blue icon-check" onclick="submitIndicatorEdit(this, '${contextPath}', '${param.horseid}', '${param.assignid}', '${param.questionid}', ${surveyAnswer.answerType}, ${surveyAnswer.referType});">
                                    <indaba:msg key='common.btn.save' />
                                </button>
                            </c:when>
                            <c:otherwise>
                                <button class="large button blue icon-check" onclick="submitIndicatorAnswer(this, '${contextPath}', '${param.horseid}', '${param.assignid}', '${param.questionid}', ${surveyAnswer.answerType}, ${surveyAnswer.referType});">
                                    <indaba:msg key='common.btn.save' />
                                </button>
                            </c:otherwise>
                        </c:choose>
                        <%--<span id="saveComplete"></span>--%>
                        <c:if test="${from != 'surveyReviewResponse.do'}">
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <c:choose>
                                <c:when test="${urlView.nextId == 0}">
                                    <a class="nav-link"><img src="images/icon_arrow_right_gray.png" alt=""/></a>
                                    </c:when>
                                    <c:otherwise>
                                    <a class="nav-link" href="#" onclick="location.href = 'surveyAnswer.do?questionid=${urlView.nextId}&horseid=${param.horseid}&assignid=${param.assignid}&action=${param.action}&returl=${urlView.returnUrl}'"><img src="images/icon_arrow_right.png" alt=""/></a>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                    </div>              
            </div>

            <div id="sidebar">
                <div id="sidebar_content">                    
                    <c:if test="${showOriginal}">
                        <indaba:view prjid="${prjid}" uid="${uid}" right="see survey original answers">
                            <a class="origAnswer" href="surveyAnswerOriginal.do?horseid=${param.horseid}&answerid=${surveyAnswer.surveyAnswerId}" target="_new"><indaba:msg key='common.btn.origanswer'/></a>
                            <br><br/>
                        </indaba:view>
                    </c:if>                    
                    <jsp:include page="surveyAnswerActionBar_inc.jsp" flush="true">
                        <jsp:param name="action" value="${param.action}"/>
                    </jsp:include>
                    <jsp:include page="surveyAnswerNavigation.jsp" flush="true">
                        <jsp:param name="action" value="surveyAnswer.do"/>
                        <jsp:param name="from" value="${param.action}"/>
                    </jsp:include>
                    <jsp:include page="targetSidebar.jsp" flush="true" />
                    <jsp:include page="notesSidebar.jsp" flush="true" >
                        <jsp:param value="${horseid}" name="horseid"/>
                        <jsp:param value="${questionid}" name="questionid"/>
                    </jsp:include>
                    <jsp:include page="groupsSidebar.jsp" flush="true">
                        <jsp:param value="${surveyAnswer.initialFlagGroupId}" name="initialFlagGroupId"/>
                        <jsp:param value="7" name="taskType"/>
                    </jsp:include>
                    <div id="problem_list">
                        <%--
                        <jsp:include page="surveyAnswerQuestionSidebar.jsp" flush="true" />
                        --%>
                    </div>
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
