<%--
    Document   : surveyAnswer
    Created on : 05 15, 2010, 4:34:20 PM
    Author     : luwb
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
    </head>
    <body>
        <c:set var="hideDiscussion" value="true" />
        <indaba:view prjid="${prjid}" uid="${uid}" right="read survey peer review discussions">
            <c:set var="hideDiscussion" value="false" />
        </indaba:view>
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
                    <c:if test="${action eq 'preVersionDisplay'}"><div class="cnt-ver" style="width: 640px;"><indaba:msg key="common.msg.contentVersion" />&nbsp;<span>${contentVersion.description}</span></div></c:if>
                    <c:forEach items="${view.categoryViewList}" var="category" varStatus="status">
                        <div class="content">
                            <div class="box"<%--<c:if test="${status.last}">style="background-color:white;"</c:if>--%> >
                                <h3>${category.label}&nbsp;&nbsp;&nbsp;&nbsp;${category.title}</h3>
                            </c:forEach>

                            <form id="uploadForm" <c:if test="${empty surveyAnswer.categoryViewList}">class="survey-answer"</c:if>
                                  class="survey-answer" method="post" action="surveysubmit.do" enctype="multipart/form-data">
                                    <div class="content survey-answer-content">
                                    <c:if test="${view.answerObjectId == 0}">
                                        <span style="color: red;"><indaba:msg key='common.alert.noanswer' /></span><br/><br/>
                                    </c:if>
                                   <div class="answer-part-score">
                                    <table>
                                        <tr>
                                            <td valign="top" width="1%" nowrap>
                                                <b>${view.publicName}:</b>
                                            </td>
                                            <td>
                                                <b>${view.question}</b>
                                            </td>
                                        </tr>
                                    </table>
                                    <c:choose>
                                        <c:when test="${view.answerType eq 6}"> <%-- table question --%>
                                            <c:choose>
                                                <c:when test="${action eq 'preVersionDisplay'}">
                                                    <indaba:tableAnswerVersion contentVersionId="${param.contentVersionId}" mainQuestionId="${questionid}" />
                                                </c:when>
                                                <c:otherwise>
                                                    <indaba:tableAnswer horseId="${horseid}"
                                                                        mainQuestionId="${questionid}"
                                                                        disabled="true" />
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise> <%-- simple question(int, float, single/multi choice, text) --%>
                                            <indaba:simpleAnswer 
                                                answerType="${view.answerType}" 
                                                answerTypeId="${view.answerTypeId}" 
                                                name="selection" 
                                                showAnswer="true" 
                                                answerObjectId="${view.answerObjectId}" 
                                                disabled="true" />
                                        </c:otherwise>
                                    </c:choose>
                                    </div>
                                    <br/>
                                    <div class="answer-part-source">
                                    <b><indaba:msg key='common.title.sources' />:</b><br/>${view.refdescrition}<br/>
                                    <indaba:source referenceType="${view.referType}" referenceId="${view.referId}" name="source" showAnswer="true" referenceObjectId="${view.referenceObjectId}" disabled="true"></indaba:source>
                                    </div>
                                    <br/><br/>
                                        <!-- include attachment widget -->

                                    <c:set var="preversion" value="0"/>
                                    <c:if test="${action eq 'preVersionDisplay'}"><c:set var="preversion" value="1"/></c:if>
                                    <div class="answer-part-attachment">
                                    <jsp:include page="attachment.jsp" flush="true" >
                                        <jsp:param name="contentType" value="survey" />                                     
                                        <jsp:param name="preversion" value="${preversion}" />
                                        <jsp:param name="readonly" value="true" />
                                        <jsp:param name="horseId" value="${param.horseid}" />
                                    </jsp:include>
                                    </div>

                                    <div class="answer-part-comment">
                                    <b><indaba:msg key='common.title.comments' />:</b>
                                    <br/>
                                    <textarea NAME="comments"  class="text" ROWS="6" readonly onclick="resetStatus();">${view.comments}</textarea>
                                    </div>
                                </div>
                            </form>
                            <c:forEach items="${view.categoryViewList}" var="category">
                            </div>
                        </div>
                    </c:forEach>
                    <c:if test="${not contentOnly and not view.preVersionMode}">
                        <div id="addtag" class="content">
                            <jsp:include page="indicatorAddTag.jsp" flush="true" />
                        </div>
                    </c:if>

                    <c:if test="${not contentOnly}">
                        <div class="clear"></div>
                        <c:set var="viewMode" value="" />
                        <c:if test="${view.preVersionMode}"><c:set var="viewMode" value="preVersion" /></c:if>
                        <jsp:include page="surveyAnswerInternalDiscussion.jsp" flush="true">
                            <jsp:param value="0" name="syncStatus"/>
                            <jsp:param name="viewMode" value="${viewMode}"/>
                        </jsp:include>
                        <jsp:include page="surveyAnswerReview.jsp" flush="true" >
                            <jsp:param value="1" name="syncStatus"/>
                            <jsp:param value="1" name="checkPermission"/>
                            <jsp:param name="viewMode" value="${viewMode}"/>
                        </jsp:include>
                        <jsp:include page="surveyAnswerPRReviews.jsp" flush="true" >
                            <jsp:param value="1" name="syncStatus"/>
                            <jsp:param value="1" name="checkPermission"/>
                            <jsp:param value="1" name="folded"/>
                            <jsp:param value="${hideDiscussion}" name="hideDiscussion"/>
                            <jsp:param value="${view.surveyAnswerId}" name="saVerId" />
                            <jsp:param name="viewMode" value="${viewMode}"/>
                        </jsp:include>
                    </c:if>
                </div>
                <c:if test="${not contentOnly}">
                    <div id="sidebar">
                        <div id="sidebar_content">                            
                            <c:if test="${not view.preVersionMode and showOriginal}">
                                <indaba:view prjid="${prjid}" uid="${uid}" right="see survey original answers">
                                    <a class="origAnswer" href="surveyAnswerOriginal.do?horseid=${param.horseid}&answerid=${view.surveyAnswerId}" target="_new"><indaba:msg key='common.btn.origanswer' /></a>
                                    <br/><br/>
                                </indaba:view>
                            </c:if>                            
                            <c:if test="${view.preVersionMode}">
                                <indaba:view prjid="${prjid}" uid="${uid}" right="see survey versions" >
                                    <c:if test="${!empty contentVersionList}">
                                        <div id="previous-versions-div" style="float:left; position: relative; margin: 10px 5px 35px 0px;">
                                            <a id="previous-versions-link" href="#" onclick="return false;"><img src="images/previous-versions.png" height="15px"/>&nbsp;<indaba:msg key='jsp.notebookEditor.previousVersions' /></a>
                                            <ul id="previous-versions-list" style="position: fixed">
                                                <c:forEach items="${contentVersionList}" var="cntVer" varStatus="status">
                                                    <c:set var="newCntVer" value="contentVersionId=${cntVer.id}" />
                                                    <c:set var="returl" value="${indaba:replaceAll(param.returl, 'contentVersionId=[\\\\d]+', newCntVer)}" />
                                                    <c:if test="${param.contentVersionId != cntVer.id}">
                                                        <li style="text-align: left;">
                                                            <fmt:formatDate value="${cntVer.createTime}" var="timestamp" pattern="yyyy-MM-dd HH:mm:ss"/>
                                                            <a href="surveyAnswerDisplay.do?questionid=${param.questionid}&assignid=${param.assignid}&horseid=${param.horseid}&action=preVersionDisplay&contentVersionId=${cntVer.id}&returl=<indaba:url action='encode' value='${returl}' />" target="_blank">
                                                                <c:set var="version_icon" value="old_version.png"/>
                                                                <c:if test="${status.index == 0}"><c:set var="version_icon" value ="latest_version.png"/></c:if>
                                                                <img src='images/${version_icon}' /> | <I>${cntVer.description}</I>
                                                            </a>
                                                        </li>
                                                    </c:if>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                    </c:if>
                                </indaba:view>
                                <div style="clear:both"></div>
                            </c:if>
                            <jsp:include page="surveyAnswerNavigation.jsp" flush="true">
                                <jsp:param name="action" value="surveyAnswerDisplay.do"/>
                                <jsp:param name="from" value="${param.action}"/>
                            </jsp:include>
                            <c:if test="${not view.preVersionMode}">
                                <jsp:include page="targetSidebar.jsp" flush="true" />
                            </c:if>
                            <jsp:include page="notesSidebar.jsp" flush="true" >
                                <jsp:param value="${horseid}" name="horseid"/>
                                <jsp:param value="${questionid}" name="questionid"/>
                                <jsp:param value="${param.contentVersionId}" name="contentVersionId"/>
                            </jsp:include>
                            <c:if test="${action ne 'preVersionDisplay'}">
                                <jsp:include page="groupsSidebar.jsp" flush="true">
                                    <jsp:param value="${view.initialFlagGroupId}" name="initialFlagGroupId"/>
                                    <jsp:param value="10" name="taskType"/>
                                </jsp:include>
                            </c:if>
                        </div>
                    </div>
                    
                </c:if>
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
