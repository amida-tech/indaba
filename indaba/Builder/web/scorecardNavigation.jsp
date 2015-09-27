<%-- 
    Document   : scorecardNavigation
    Created on : 2010-3-24, 6:52:48
    Author     : Luke Shi
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--un:useConstants var="Rights" className="com.ocs.indaba.common.Rights"/--%>

<script type="text/javascript" src="js/common.js"></script>

<c:if test="${showIamdoneButton}">
    <div align="center">
        <c:choose>
            <c:when test='${action == "surveyReview.do"}'>
                <button class="large button orange icon-check" onclick="trySubmitSurveyReview('${contextPath}', '${horseid}', '${assignid}', '${returl}');">
                    <indaba:msg key='common.btn.completeAssignment' /></button>
                </c:when>
                <c:when test='${action == "surveyPeerReview.do"}'>
                <button class="large button orange icon-check" onclick="submitSurveyPeerReviewAssignment('${contextPath}', '${horseid}', '${assignid}', '${returl}');">
                    <indaba:msg key='common.btn.completeAssignment' /></button>
                </c:when>
                <c:when test='${action == "surveyPRReview.do"}'>
                <button class="large button orange icon-check" onclick="trySubmitSurveyPRReview('${contextPath}', '${horseid}', '${assignid}');">
                    <indaba:msg key='common.btn.completeAssignment' /></button>
                </c:when>
                <c:when test='${action == "surveyReviewResponse.do"}'>
                <button class="large button orange icon-check"
                        onclick="submitDone('${contextPath}', '${horseid}', '${assignid}', '${returl}');">
                    <indaba:msg key='common.btn.sendFeedback' /></button>
                </c:when>
                <c:otherwise>
                <button class="large button orange icon-check"
                        onclick="submitDone('${contextPath}', '${horseid}', '${assignid}', '${returl}');">
                    <indaba:msg key='common.btn.completeAssignment' /></button>
                </c:otherwise>
            </c:choose>
        <hr class="inbox-separator">
    </div>
</c:if>
<c:if test="${action eq 'preVersionDisplay'}"><div class="cnt-ver"><indaba:msg key="common.msg.contentVersion" />&nbsp;<span>${contentVersion.description}</span></div></c:if>
<h2 class="pos-relative">${surveyTitle}
    <c:if test='${(action eq "surveyDisplay.do") or (action eq "preVersionDisplay")}'><indaba:msg key='jsp.scorecardNav.readOnly' /></c:if>
    </h2>
<c:if test='${(action eq "surveyDisplay.do") or (action eq "preVersionDisplay")}'>
    <div style="position:relative;margin-right: 5px;">
        <c:if test="${action ne 'preVersionDisplay'}"> <%-- For Preious Version view mode, don't display 'Indicator Tags' --%>
            <indaba:view prjid="${prjid}" uid="${uid}" right="super edit content">
                <div id="super-edit-div" style="float: right; position: relative; width: 250px">
                    <c:choose>
                        <c:when test="${(not empty contentHeader.submitTime)}">
                            <a id="super-edit-link" href="surveyOverallReview.do?toolid=19&tasktype=19&&horseid=${horseid}&assignid=0"><img src="images/edit.png" alt=""/>&nbsp;<indaba:msg key='jsp.notebookView.editThis' /></a>
                            </c:when>
                            <c:otherwise>
                                <indaba:msg key='common.btn.editdisabled' />
                            </c:otherwise>
                        </c:choose>
                </div>
            </indaba:view>
        </c:if>
        <indaba:view prjid="${prjid}" uid="${uid}" right="see survey versions" >
            <c:if test="${!empty contentVersionList}">
                <div id="previous-versions-div" style="float: right; margin-right: 5px;">
                    <a id="previous-versions-link" href="#" onclick="return false;"><img src="images/previous-versions.png" height="15px"/>&nbsp;<indaba:msg key='jsp.notebookEditor.previousVersions' /></a>
                    <ul id="previous-versions-list">
                        <c:forEach items="${contentVersionList}" var="cntVer" varStatus="status">
                            <c:if test="${contentVersionId != cntVer.id}">
                                <li style="text-align: left;">
                                    <fmt:formatDate value="${cntVer.createTime}" var="timestamp" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    <a href="surveyPreVersionDisplay.do?contentVersionId=${cntVer.id}&action=display&horseid=${horseid}" target="_blank">
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
    </div>
    <div style="clear:both;"></div>
</c:if>
<c:if test='${action == "surveyReview.do" || action == "surveyPRReview.do"}'>
    <div style="float: right"><a href="#" onclick="addAllQuestions('${horseid}', '${assignid}');
        return false;">
            <indaba:msg key='jsp.surveryAnsBar.add.question'/></a> &nbsp;&nbsp;
    </div>
</c:if>
<br/>

<c:if test='${action != "surveyReviewResponse.do"}'>
    <div class="box">
        <h3><indaba:msg key='jsp.scorecardNav.nav' />&nbsp;&nbsp;&nbsp;&nbsp;
            <img id="indicator-navigator-tip" class="tipTip" src='images/hint_icon.png' title="${fn:replace(indicatorinstr, "\"", '&quot;')}" alt="(Instructions)"/>
            <script type="text/javascript">$(".tipTip").tipTip();</script>
        </h3>
        <indaba:surveyTree userid="${uid}" horseid="${horseid}" assignid="${assignid}" action="${action}" cntverid="${contentVersionId}" returl="${returl}"/>
    </div>
</c:if>

<c:if test="${action ne 'preVersionDisplay'}"> <%-- For Preious Version view mode, don't display 'Indicator Tags' --%>
    <indaba:view prjid="${prjid}" uid="${uid}" right="tag indicators">
        <div class="box" id="indicatorSearchBox">
            <h3><indaba:msg key='jsp.scorecardNav.indicatorTags' /><a href="#" class="toggleVisible"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
            <div class="content" style="padding: 15px;" align="center">
                <div id="indicatorSearchBar">
                </div>
            </div>
        </div>
    </indaba:view>
</c:if>

