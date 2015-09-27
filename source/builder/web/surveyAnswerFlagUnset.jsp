<%-- 
    Document   : surveyAnswerFlagUnset
    Created on : Apr 8, 2014, 3:25:49 PM
    Author     : yc06x
--%>


<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title><indaba:msg key='jsp.surveryAnsDisp.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <!--[if lt IE 7]>
            <link href="ie6.css" media="screen" rel="Stylesheet" type="text/css" />
        <![endif]-->
        <link type="text/css" rel="stylesheet" href="css/style2.css"/>
        <link type="text/css" rel="stylesheet" href="plugins/jquery-ui/css/lightness/jquery-ui-1.10.4.custom.min.css" media="all" />
        <link type="text/css" rel="stylesheet" href="plugins/scrollbar/css/jquery.mCustomScrollbar.css"/>
        <link type="text/css" rel="stylesheet" href="css/button.css"/>
        <link type="text/css" rel="stylesheet" href="plugins/uniform/css/uniform.default.css"/>
        <link type="text/css" rel="stylesheet" href="plugins/pnotify/jquery.pnotify.default.css"/>
        <link type="text/css" rel="stylesheet" href="plugins/pnotify/jquery.pnotify.default.icons.css"/>

        <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="plugins/jquery-ui/js/jquery-ui-1.10.4.custom.min.js"></script>
        <script type="text/javascript" src="plugins/scrollbar/js/jquery.mousewheel.js"></script>
        <script type="text/javascript" src="plugins/scrollbar/js/jquery.mCustomScrollbar.js"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="plugins/uniform/js/jquery.uniform.js"></script>
        <script type="text/javascript" src="js/jquery.scrollTo-1.4.2-min.js"></script>
        <script type="text/javascript" src="js/jquery.localscroll-1.2.7-min.js"></script>
        <script type="text/javascript" src="js/jquery.highlightFade.js"></script>
        <script type="text/javascript" src="js/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="js/discussions.js"></script>
        <script type="text/javascript" src="js/jquery.autogrow-textarea.js"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="plugins/pnotify/jquery.pnotify.min.js"></script>
        <script type="text/javascript" src="js/ocs-notify.js"></script>
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
            function moveToFlag(ctx, horseId, flagId, assignId, exitUrl, dir) {
                var api = 'getNext';
                if (dir < 0) api = 'getPrev';

                $.ajax({
                    type: 'POST',
                    url: ctx + '/flagUnset.do',
                    data: {
                        api: api,
                        horseid: horseId,
                        flagid: flagId},
                        dataType: 'json',
                        success: function(data) {
                            if (data.flagId <= 0) {
                                var msg = (dir < 0) ? "No flags before this." : "No flags after this.";
                                ocsShowNotify({
                                    title: "Info",
                                    text: msg,
                                    type: 'info'
                                });
                            } else {
                                window.location.href = ctx + "/flagUnset.do?horseid=" + horseId + "&flagid=" + data.flagId +
                                    "&assignid=" + assignId + "&questionid=" + data.surveyQuestionId +
                                    "&initialFlagGroupId=" + data.groupobjId +
                                    "&fromurl=" + exitUrl;
                            }
                        }
                    });
            }

        </script>
    </head>
    <body>
        <c:set var="hideDiscussion" value="true" />
        <indaba:view prjid="${prjid}" uid="${uid}" right="read survey peer review discussions">
            <c:set var="hideDiscussion" value="false" />
        </indaba:view>
        <c:set var="foldPeerReviews" value="1" />
        <c:if test="${not empty contents && fn:contains(contents, 'F')}">
            <c:set var="hideDiscussion" value="false" />
            <c:set var="foldPeerReviews" value="0" />
        </c:if>
        <c:if test="${not empty contents && fn:contains(contents, 'G')}">
            <c:set var="foldPeerReviews" value="0" />
        </c:if>

        <c:set var="disableScore" value="true" />
        <c:set var="disableSource" value="true" />
        <c:set var="disableComment" value="true" />
        <c:set var="disableAttachment" value="true" />

        <div id="indaba">
            <c:set var="active" value="yourcontent" scope="request"/>
            <c:choose>
                <c:when test="${not contentOnly}">
                    <jsp:include page="header.jsp" flush="true" />
                </c:when>
                <c:otherwise>
                    <div style="margin-left:10px">
                        <img src="images/indaba-logo.gif" id="mainlogo" alt="Indaba Home"/>
                        <br/>
                        <h3><indaba:msg key='jsp.surveryAnsDisp.answerFrom' /> ${targetname}</h3>
                        <br/>
                    </div>
                </c:otherwise>
            </c:choose>
            <div class="wrapper">
                <div id="main-column">
                    <c:forEach items="${surveyAnswerView.categoryViewList}" var="category" varStatus="status">
                        <div class="content">
                            <div class="box" >
                                <h3>${category.label}&nbsp;&nbsp;&nbsp;&nbsp;${category.title}</h3>
                            </c:forEach>

                            <form id="uploadForm" <c:if test="${empty surveyAnswerView.categoryViewList}">class="survey-answer"</c:if>
                                  class="survey-answer" method="post" action="surveysubmit.do" enctype="multipart/form-data">
                                    <div class="content survey-answer-content">
                                    <c:if test="${surveyAnswerView.answerObjectId == 0}">
                                        <span style="color: red;"><indaba:msg key='common.alert.noanswer' /></span><br/><br/>
                                    </c:if>
                                   <div class="answer-part-score">
                                    <table>
                                        <tr>
                                            <td valign="top" width="1%" nowrap>
                                                <b>${surveyAnswerView.publicName}:</b>
                                            </td>
                                            <td>
                                                <b>${surveyAnswerView.question}</b>
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
                                        <c:when test="${surveyAnswerView.answerType eq 6}"> <%-- table question --%>
                                                    <indaba:tableAnswer horseId="${horseid}"
                                                                        mainQuestionId="${questionid}"
                                                                        disabled="${disableScore}" />
                                        </c:when>
                                        <c:otherwise> <%-- simple question(int, float, single/multi choice, text) --%>
                                            <indaba:simpleAnswer
                                                answerType="${surveyAnswerView.answerType}"
                                                answerTypeId="${surveyAnswerView.answerTypeId}"
                                                name="selection"
                                                showAnswer="true"
                                                answerObjectId="${surveyAnswerView.answerObjectId}"
                                                disabled="${disableScore}" />
                                        </c:otherwise>
                                    </c:choose>
                                    </div>
                                    <br/>
                                    <div class="answer-part-source">
                                    <b><indaba:msg key='common.title.sources' />:</b><br/>${surveyAnswerView.refdescrition}<br/>
                                    <indaba:source referenceType="${surveyAnswerView.referType}" referenceId="${surveyAnswerView.referId}" name="source" showAnswer="true" referenceObjectId="${surveyAnswerView.referenceObjectId}" disabled="${disableSource}"></indaba:source>
                                    </div>
                                    <br/><br/>

                                    <c:set var="preversion" value="0"/>

                                    <jsp:include page="attachment.jsp" flush="true" >
                                        <jsp:param name="contentType" value="survey" />
                                        <jsp:param name="preversion" value="${preversion}" />
                                        <jsp:param name="readonly" value="${disableAttachment == 'true'}" />
                                        <jsp:param name="answerId" value="${surveyAnswerView.surveyAnswerId}" />
                                        <jsp:param name="horseId" value="${param.horseid}" />
                                    </jsp:include>

                                    <div class="answer-part-comment">
                                    <b><indaba:msg key='common.title.comments' />:</b>
                                    <br/>
                                    <textarea NAME="comments"  class="text" ROWS="6"
                                              <c:if test="${disableComment}">
                                                READONLY="readonly"
                                              </c:if>
                                              onclick="resetStatus();">${surveyAnswerView.comments}</textarea>

                                    </div>
                                </div>
                            </form>
                            <c:forEach items="${surveyAnswerView.categoryViewList}" var="category">
                            </div>
                        </div>
                    </c:forEach>

                    <div class="clear"></div>
                    <c:set var="viewMode" value="" />

                    <c:if test="${surveyAnswerView.answerType eq 6}"><br/></c:if>
                    <jsp:include page="surveyAnswerPeerReviewsForFlagUnset.jsp" flush="true" >
                        <jsp:param value="1" name="syncStatus"/>
                        <jsp:param value="1" name="checkPermission"/>
                        <jsp:param value="${foldPeerReviews}" name="folded"/>
                        <jsp:param value="${hideDiscussion}" name="hideDiscussion"/>
                        <jsp:param value="${surveyAnswerView.surveyAnswerId}" name="saVerId" />
                        <jsp:param name="viewMode" value="${viewMode}"/>
                    </jsp:include>
                </div>
                <div id="sidebar">
                    <div id="sidebar_content">
                            <jsp:include page="surveyAnswerFlagUnsetNavigation.jsp" flush="true">
                                <jsp:param name="from" value="${param.action}"/>
                            </jsp:include>

                            <jsp:include page="notesSidebar.jsp" flush="true" >
                                <jsp:param value="${horseid}" name="horseid"/>
                                <jsp:param value="${questionid}" name="questionid"/>
                                <jsp:param value="21" name="taskType"/>
                            </jsp:include>
                            <jsp:include page="groupsSidebar.jsp" flush="true">
                                <jsp:param value="${surveyAnswerView.initialFlagGroupId}" name="initialFlagGroupId"/>
                                <jsp:param value="21" name="taskType"/>
                            </jsp:include>
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

                 <c:if test="${!empty contents}">
                     <c:if test="${fn:contains(contents, 'B')}">
                        // change score
                        $(".answer-part-score").addClass("answer-part-highlighted");
                     </c:if>

                     <c:if test="${fn:contains(contents, 'C')}">
                        // change source
                        $(".answer-part-source").addClass("answer-part-highlighted");
                     </c:if>

                     <c:if test="${fn:contains(contents, 'D')}">
                        // comment
                        $(".answer-part-comment").addClass("answer-part-highlighted");
                     </c:if>

                     <c:if test="${fn:contains(contents, 'E')}">
                        // attachment
                        $(".answer-part-attachment").addClass("answer-part-highlighted");
                     </c:if>

                 </c:if>
            });
        </script>
    </body>
</html>

