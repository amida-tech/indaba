<%-- 
    Document   : casePage
    Created on : 2010-7-5, 21:30:48
    Author     : Jeanbone
--%>

<%@page pageEncoding="UTF-8" contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <title>
            <c:choose>
                <c:when test='${action == "new"}'>
                    <indaba:msg key='jsp.casePage.title.newCase' />
                </c:when>
                <c:otherwise>
                    <indaba:msg key='jsp.casePage.title.caseDetail' />
                </c:otherwise>
            </c:choose>
        </title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <link type="text/css" rel="stylesheet" href="css/style2.css"/>
        <link type="text/css" rel="stylesheet" href="css/button.css"/>
        <script type="text/javascript" src="js/mootools-1.2.2.js"></script>
        <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript">
            var $j = jQuery.noConflict();
        </script>
        <script type="text/javascript" src="js/jquery.highlightFade.js"></script>
	<script type="text/javascript" src="js/jquery.tablesorter.js"></script>
	<script type="text/javascript" src="js/discussions.js"></script>
        <script type="text/javascript" src="js/jquery.autogrow-textarea.js"></script>
        <script type="text/javascript" src="js/toggleDisplayForCase.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/caseMsgBoard.js"></script>
        <script type="text/javascript" src="js/json2.js"></script>
        <script type="text/javascript" src="ckeditor/ckeditor.js"></script>
        <script type="text/javascript" src="ckeditor/adapters/jquery.js"></script>
        <script type="text/javascript" src="js/casedetail.js"></script>
        <script type="text/javascript" src="fancyupload/source/Swiff.Uploader.js"></script>
        <script type="text/javascript" src="fancyupload/source/Fx.ProgressBar.js"></script>
        <%--
        <script type="text/javascript" src="http://github.com/mootools/mootools-more/raw/master/Source/Core/Lang.js"></script>
        --%>
        <script type="text/javascript" src="fancyupload/source/FancyUpload2.js"></script>
        <script type="text/javascript">
            function checkTitle() {
                var title = document.getElementById("title");
                if (title.value.replace(/(^\s*)|(\s*$)/g, "").length == 0) {
                    document.getElementById('lbl_title').style.display="";
                    document.getElementById('submitDetail').disabled = true;
                } else {
                    document.getElementById('lbl_title').style.display="none";
                    document.getElementById('submitDetail').disabled = false;
                }
            }
        </script>
    </head>
    
    <body onload="alertMsg()">
        <div id="indaba">
            <c:set var="active" value="cases" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper cases">
                <div class="box">
                    <h3>
                        <c:choose>
                            <c:when test='${action == "new"}'>
                                <indaba:msg key='jsp.casePage.newCase' />
                            </c:when>
                            <c:otherwise>
                                <indaba:msg key='jsp.casePage.case' /> #${caseinfo.caseId}: ${caseinfo.title}
                                <c:if test="${uid == caseinfo.openedByUserId || uid == caseinfo.assignedUserId || canEditCase}">
                                    <a href="#" class="rightDisplay" onclick="onEditButtonClick();"><img id="editViewImg" height="14px" src="images/edit.png" /> <span id="editButton"><indaba:msg key='common.btn.edit' /></span></a>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </h3>
                    <br/>
                    <form id="caseForm" name="caseForm" action="
                        <c:choose>
                            <c:when test='${action == "new"}'>
                                ${contextPath}/createCase.do
                            </c:when>
                            <c:otherwise>
                                ${contextPath}/updateCase.do
                            </c:otherwise>
                        </c:choose>
                        " method="post">
                    <div id="fileNames" style="display: none"></div>

                    <table>
                        <tbody>
                        <c:if test='${action != "new"}'>
                            <tr>
                                <input id="caseid" name="caseid" value="${caseinfo.caseId}" type="hidden" />
                            </tr>
                        </c:if>
                        <tr>
                            <td align="right">
                                <b><indaba:msg key='jsp.casePage.createBy' />:</b>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test='${action == "new"}'>
                                        ${currUser.firstName}&nbsp;${currUser.lastName}
                                    </c:when>
                                    <c:otherwise>
                                        <c:choose>
                                            <c:when test='${canManageUsers}'>
                                                ${caseinfo.owner} on ${caseinfo.openedTime}
                                            </c:when>
                                            <c:otherwise>
                                                ${caseinfo.openedByUserName} on ${caseinfo.openedTime}
                                            </c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                <b><indaba:msg key='jsp.casePage.caseTitle' />:</b>
                            </td>
                            <td nowrap>
                                <c:choose>
                                    <c:when test='${action == "new"}'>
                                        <input class="text" type="text" id="title" name="title" size="100" onchange="checkTitle();" maxlength="100" />
                                        <label id="lbl_title" style="color: red;">&nbsp;*</label>
                                    </c:when>
                                    <c:otherwise>
                                        <input id="title" name="title" value="${fn:escapeXml(caseinfo.title)}" onchange="checkTitle();" disabled size="100"/>
                                        <input id="oldTitle" name="oldTitle" value="${fn:escapeXml(caseinfo.title)}" type="hidden" size="100"/>
                                        <label id="lbl_title" style="color: red; display: none">&nbsp;*</label>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <td align="right" valign="top">
                                <b><indaba:msg key='jsp.casePage.caseDescription' />:</b>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test='${action == "new"}'>
                                        <textarea name="description" id="description" class="text" rows="15" ></textarea>
                                    </c:when>
                                    <c:otherwise>
                                        <textarea name="description" id="description" class="text" rows="15" readonly>${fn:escapeXml(caseinfo.description)}</textarea>
                                        <input name="oldDescription" id="oldDescription" value="${fn:escapeXml(caseinfo.description)}" type="hidden" />
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <td nowrap align="right">
                                <b><indaba:msg key='jsp.casePage.casestatus' />:</b>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test='${action == "new"}'>
                                        <indaba:msg key='jsp.casePage.openNew' />
                                    </c:when>
                                    <c:otherwise>
                                        <select name="caseStatusSelect" id="caseStatusSelect" disabled>
                                            <c:choose>
                                                <c:when test='${canCloseAllCases}'>
                                                    <c:forEach items="${allCaseStatus}" var="caseStatus" varStatus="status">
                                                        <c:if test="${caseStatus.statusCode == caseinfo.statusCode}">
                                                            <option value="${caseStatus.statusCode}" selected><indaba:msg key='${caseStatus.transKey}' /></option>
                                                        </c:if>
                                                        <c:if test="${caseStatus.statusCode != caseinfo.statusCode}">
                                                            <option value="${caseStatus.statusCode}"><indaba:msg key='${caseStatus.transKey}' /></option>
                                                        </c:if>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach items="${allCaseStatus}" var="caseStatus" varStatus="status">
                                                        <c:if test="${caseStatus.statusCode == caseinfo.statusCode}">
                                                            <option value="${caseStatus.statusCode}" selected><indaba:msg key='${caseStatus.transKey}' /></option>
                                                        </c:if>
                                                        <c:if test="${caseStatus.statusCode != caseinfo.statusCode && uid == caseinfo.openedByUserId}">
                                                            <option value="${caseStatus.statusCode}"><indaba:msg key='${caseStatus.transKey}' /></option>
                                                        </c:if>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                        </select>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <td nowrap align="right">
                                <b><indaba:msg key='jsp.casePage.casePriority' />:</b>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test='${action == "new"}'>
                                        <select name="casePrioritySelect" id="casePrioritySelect" >
                                            <c:forEach items="${allCasePriority}" var="casePriority" varStatus="status">
                                                <option value="${casePriority.priorityCode}"><c:if test="${casePriority.priorityDesc == 'Low'}"><indaba:msg key='jsp.queues.low' /></c:if><c:if test="${casePriority.priorityDesc == 'Medium'}"><indaba:msg key='jsp.queues.medium' /></c:if><c:if test="${casePriority.priorityDesc == 'High'}"><indaba:msg key='jsp.queues.high' /></c:if></option>
                                            </c:forEach>
                                        </select>
                                    </c:when>
                                    <c:otherwise>
                                        <select name="casePrioritySelect" id="casePrioritySelect" disabled>
                                            <c:forEach items="${allCasePriority}" var="casePriority" varStatus="status">
                                                <c:if test="${casePriority.priorityCode == caseinfo.priorityCode}">
                                                    <option value="${casePriority.priorityCode}" selected><c:if test="${casePriority.priorityDesc == 'Low'}"><indaba:msg key='jsp.queues.low' /></c:if><c:if test="${casePriority.priorityDesc == 'Medium'}"><indaba:msg key='jsp.queues.medium' /></c:if><c:if test="${casePriority.priorityDesc == 'High'}"><indaba:msg key='jsp.queues.high' /></c:if></option>
                                                </c:if>
                                                <c:if test="${casePriority.priorityCode != caseinfo.priorityCode}">
                                                    <option value="${casePriority.priorityCode}"><c:if test="${casePriority.priorityDesc == 'Low'}"><indaba:msg key='jsp.queues.low' /></c:if><c:if test="${casePriority.priorityDesc == 'Medium'}"><indaba:msg key='jsp.queues.medium' /></c:if><c:if test="${casePriority.priorityDesc == 'High'}"><indaba:msg key='jsp.queues.high' /></c:if></option>
                                                </c:if>
                                            </c:forEach>
                                        </select>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <c:if test='${canBlockProgress}'>
                            <c:choose>
                                <c:when test='${action == "new"}'>
                                    <tr id="workFlowTr" style="display:none">
                                        <td align="right">
                                            <b><indaba:msg key='common.msg.blockworkflow' />:</b>
                                        </td>
                                        <td>
                                            <input type="checkbox" name="blockWorkFlowCheckBox" id="blockWorkFlowCheckBox" />
                                        </td>
                                    </tr>
                                    <tr id="publishingTr" style="display:none">
                                        <td valign="top" align="right">
                                            <b><indaba:msg key='common.msg.blockpublish' />:</b>
                                        </td>
                                        <td>
                                            <input type="checkbox" name="blockPublishingCheckBox" id="blockPublishingCheckBox" />
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <tr id="workFlowTr">
                                        <td align="right">
                                            <b><indaba:msg key='common.msg.blockworkflow' />:</b>
                                        </td>
                                        <td>
                                            <input type="checkbox" name="blockWorkFlowCheckBox" id="blockWorkFlowCheckBox" disabled
                                                <c:if test="${caseinfo.blockWorkFlow == true}">checked</c:if> />
                                        </td>
                                    </tr>
                                    <tr id="publishingTr">
                                        <td valign="top" align="right">
                                            <b><indaba:msg key='common.msg.blockpublish' />:</b>
                                        </td>
                                        <td>
                                            <input type="checkbox" name="blockPublishingCheckBox" id="blockPublishingCheckBox" disabled
                                               <c:if test="${caseinfo.blockPulishing == true}">checked</c:if> />
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                        <tr>
                            <td valign="top" align="right">
                                <b><indaba:msg key='jsp.casePage.assignTo' />:</b>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test='${action == "new"}'>
                                        <select name="assignUserSelect" id="assignUserSelect">
                                            <c:forEach items="${allUsers}" var="user" varStatus="status">
                                                <c:if test="${user.id == uid}"><option value="${user.id}" selected>${user.firstName}&nbsp;${user.lastName}</option></c:if>
                                                <c:if test="${user.id != uid}"><option value="${user.id}" >${user.firstName}&nbsp;${user.lastName}</option></c:if>
                                            </c:forEach>
                                        </select>
                                    </c:when>
                                    <c:otherwise>
                                        <select name="assignUserSelect" id="assignUserSelect" disabled>
                                            <c:forEach items="${allUsers}" var="user" varStatus="status">
                                                <c:if test="${user.id == caseinfo.assignedUserId}">
                                                    <option value="${user.id}" selected>${user.firstName}&nbsp;${user.lastName}</option>
                                                </c:if>
                                                <c:if test="${user.id != caseinfo.assignedUserId}">
                                                    <option value="${user.id}">${user.firstName}&nbsp;${user.lastName}</option>
                                                </c:if>
                                            </c:forEach>
                                        </select>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <td nowrap align="right" valign="top">
                                <b><indaba:msg key='jsp.casePage.attachUsers' />:</b>
                            </td>
                            <td valign="top">
                                <c:choose>
                                    <c:when test='${action == "new"}'>
                                        <select name="attachUserSelect" id="attachUserSelect">
                                            <c:forEach items="${allUsers}" var="user" varStatus="status">
                                                <option value="${user.id}">${user.firstName}&nbsp;${user.lastName}</option>
                                            </c:forEach>
                                        </select>
                                        <button type="button" onclick="addAttachUser(); return false;" class='small button blue icon-add'><indaba:msg key='common.btn.add' /></button>
                                        <div id="selectedUserFilters" class="selectedFilters">
                                        </div>

                                        <input id="attachUserIds" name="attachUserIds" value="" type="hidden"/>
                                        <input id="attachUserNames" value="" disabled size="90" type="hidden">
                                        <!--button onclick="clearAttachUsers()" type="button">Clear</button-->
                                    </c:when>
                                    <c:otherwise>
                                        <div id="oldAttachUserSelectIds" style="display:none" >
                                            <c:forEach items="${caseinfo.attachUsers}" var="user" varStatus="status">
                                                ${user.id}<c:if test="${!status.last}">; </c:if>
                                            </c:forEach>
                                        </div>
                                        <div id="oldAttachUserSelect" >
                                            <c:forEach items="${caseinfo.attachUsers}" var="user" varStatus="status">
                                                <indaba:userDisplay prjid="${prjid}" subjectUid="${uid}" targetUid="${user.id}" />
                                                <c:if test="${!status.last}">; </c:if>
                                            </c:forEach>
                                        </div>
                                        <div id="newAttachUserSelect" style="display:none">
                                            <select name="attachUserSelect" id="attachUserSelect">
                                                <c:forEach items="${allUsers}" var="user" varStatus="status">
                                                    <option value="${user.id}">${user.firstName}&nbsp;${user.lastName}</option>
                                                </c:forEach>
                                            </select>
                                            <button type="button" onclick="addAttachUser(); return false;" class='small button blue icon-add'><indaba:msg key='common.btn.add' /></button>
                                            <div id="selectedUserFilters" class="selectedFilters">
                                            </div>
                                            <c:forEach items="${caseinfo.attachUsers}" var="user" varStatus="status">
                                                <script type="text/javascript">
                                                    addSingleAttachUser("${user.id}");
                                                </script>
                                            </c:forEach>
                                        </div>
                                        <div id="newAttachUserInput" style="display:none">
                                            <input id="attachUserIds" name="attachUserIds" value="" type="hidden"/>
                                            <input id="attachUserNames" value="" disabled size="90" type="hidden"/>
                                            <!--button onclick="clearAttachUsers()" type="button">Clear</button-->
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <td nowrap align="right" valign="top">
                                <b><indaba:msg key='jsp.casePage.attachContent' />:</b>
                            </td>
                            <td valign="top">
                                <c:choose>
                                    <c:when test='${action == "new"}'>
                                        <select name="attachContentSelect" id="attachContentSelect">
                                            <c:forEach items="${allContents}" var="content" varStatus="status">
                                                <option value="${content.id}">${content.title}</option>
                                            </c:forEach>
                                        </select>
                                        <button type="button" onclick="addAttachContent(); return false;" class='small button blue icon-add'><indaba:msg key='common.btn.add' /></button>
                                        <div id="selectedContentFilters" class="selectedFilters">
                                        </div>
                                        <div id="attachContentInput">
                                            <input id="attachContentIds" name="attachContentIds" value="" type="hidden"/>
                                            <input id="attachContentNames" value="" disabled size="90" type="hidden"/>
                                            <!--button onclick="clearAttachContent()" type="button">Clear</button-->
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div id="oldAttachContentSelectIds" style="display: none">
                                            <c:forEach items="${caseinfo.attachContentIds}" var="id" varStatus="status">
                                                ${id}<c:if test="${!status.last}">; </c:if>
                                            </c:forEach>
                                        </div>
                                        <div id="oldAttachContentSelect">
                                            <c:forEach items="${caseinfo.attachContents}" var="content" varStatus="status">
                                                <c:choose>
                                                    <c:when test="${content.contentType == 1}">
                                                        <a href="notebook.do?action=display&horseid=${content.horseId}">${content.title}</a>
                                                        <c:if test="${!status.last}">; </c:if>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="surveyDisplay.do?action=display&horseid=${content.horseId}">${content.title}</a>
                                                        <c:if test="${!status.last}">; </c:if>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </div>
                                        <div id="newAttachContentSelect" style="float:left;display:none">
                                            <select name="attachContentSelect" id="attachContentSelect">
                                                <c:forEach items="${allContents}" var="content" varStatus="status">
                                                    <option value="${content.id}">${content.title}</option>
                                                </c:forEach>
                                            </select>
                                            <button type="button" onclick="addAttachContent(); return false;" class='small button blue icon-add'><indaba:msg key='common.btn.add' /></button>
                                        </div>
                                        <div id="selectedContentFilters" class="selectedFilters" style="display:none"/>
                                        <c:forEach items="${caseinfo.attachContents}" var="content" varStatus="status">
                                            <script type="text/javascript">
                                                addSingleAttachContent("${content.id}");
                                            </script>
                                        </c:forEach>
                                        <div id="attachContentInput" style="display:none">
                                            <input id="attachContentIds" name="attachContentIds" value="" type="hidden"/>
                                            <input id="attachContentNames" value="" disabled size="90" type="hidden"/>
                                            <!--button onclick="clearAttachContent()" type="button">Clear</button-->
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <c:if test='${fn:length(allCtags)>0}'>
                        <tr>
                            <td nowrap align="right" valign="top">
                                <b><indaba:msg key='jsp.casePage.attachTags' />:</b>
                            </td>
                            <td>
                            <c:choose>
                                <c:when test='${action == "new"}'>
                                        <select name="attachTagSelect" id="attachTagSelect">
                                            <c:forEach items="${allCtags}" var="ctag" varStatus="status">
                                                <option value="${ctag.id}">${ctag.term}</option>
                                            </c:forEach>
                                        </select>
                                        <button type="button" onclick="addAttachTags(); return false;" class='small button blue icon-add'><indaba:msg key='common.btn.add' /></button>
                                        <div id="selectedTagFilters" class="selectedFilters"/>
                                    <div id="attachTagInput">
                                        <input id="attachTagIds" name="attachTagIds" value="" type="hidden"/>
                                        <input id="attachTagNames" value="" disabled size="90" type="hidden"/>
                                        <!--button onclick="clearAttachTags()" type="button">Clear</button-->
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div id="oldTagSelectIds" style="display:none">
                                        <c:forEach items="${caseinfo.tags}" var="tag" varStatus="status">
                                            ${tag.id}<c:if test="${!status.last}">; </c:if>
                                        </c:forEach>
                                    </div>
                                    <div id="oldTagSelect" >
                                        <c:forEach items="${caseinfo.tags}" var="tag" varStatus="status">
                                            ${tag.term}<c:if test="${!status.last}">; </c:if>
                                        </c:forEach>
                                    </div>
                                    <div id="newTagSelect" style="display:none">
                                         <select name="attachTagSelect" id="attachTagSelect">
                                            <c:forEach items="${allCtags}" var="ctag" varStatus="status">
                                                <option value="${ctag.id}">${ctag.term}</option>
                                            </c:forEach>
                                        </select>
                                        <button type="button" onclick="addAttachTags(); return false;" class='small button blue icon-add'><indaba:msg key='common.btn.add' /></button>
                                        <div id="selectedTagFilters" class="selectedFilters" style="display:none"/>
                                        <c:forEach items="${caseinfo.tags}" var="tag" varStatus="status">
                                            <script type="text/javascript">
                                                addSingleAttachTag("${tag.term}");
                                            </script>
                                        </c:forEach>
                                    </div>
                                    <div id="attachTagInput" style="display:none">
                                        <input id="attachTagIds" name="attachTagIds" value="" type="hidden"/>
                                        <input id="attachTagNames" value="" disabled size="90" type="hidden"/>
                                        <!--button onclick="clearAttachTags()" type="button">Clear</button-->
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            </td>
                        </tr>
                        </c:if>
                        <tr>
                            <td align="right" valign="top" nowrap>
                                <b><indaba:msg key='jsp.casePage.attachFiles' />:</b>
                            </td>
                            <td>
                                <c:if test='${action != "new"}'>
                                    <table>
                                    <tr>
                                    <c:forEach items="${caseinfo.attachFiles}" var="file" varStatus="status">
                                        <td>
                                        <table id="attachFile${status.count}" style="display:block">
                                            <tr>
                                                <td>
                                                    <a href="#attachFile1" onclick="deleteAttachmentFile(${status.count},${file.id})" title="remove">
                                                        <img src ="images/icon-delete.png" id="fileDelete${status.count}"
                                                        alt="delete" width="12" height="12" style="display:none"/>
                                                    </a>
                                                </td>
                                                <td>
                                                <a href="#attachFile1" onclick="getCaseAttachment(${file.id});">${file.fileName}</a>
                                                </td>
                                            </tr>
                                        </table>
                                        </td>
                                    </c:forEach>
                                    </tr>
                                    </table>
                                </c:if>
                                <input id="deleteFileIds" name ="deleteFileIds" type="hidden" value ="">
                                <div id="attachFile" style="display:
                                    <c:choose>
                                        <c:when test='${action == "new"}'>block</c:when>
                                        <c:otherwise>none</c:otherwise>
                                    </c:choose>
                                 ">
                                    <jsp:include page="fileupload.jsp" flush="true" />
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <p align="center">
                                <input type="submit" onclick="getFiles();" value=" <indaba:msg key='common.btn.submit' /> " id="submitDetail" class='small button blue'
                                       style="display:
                                    <c:choose>
                                        <c:when test='${action == "new"}'>block</c:when>
                                        <c:otherwise>none</c:otherwise>
                                    </c:choose>
                                "
                                    <c:choose>
                                        <c:when test='${action == "new"}'>disabled</c:when>
                                    </c:choose>
                                />
                                </p>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <input value="${alertInfo}" id="alertInfoInput" type="hidden"/>
                </form>
                <br/>
                </div>
                
                <c:if test='${action != "new"}'>
                    <div>
                        <div id="caseUserNote" class="box discussions"></div>
                    </div>
                    <script type="text/javascript">
                        var parameters = {};
                        parameters.ctxPath = '${contextPath}';
                        parameters.userId = '${uid}';
                        parameters.userName = "${name}";
                        parameters.containerName = 'caseUserNote';
                        parameters.caseId = '${param.caseid}';
                        loadCaseUserMsgBoard(parameters);
                    </script>
                    <c:if test='${canAccessStaffNote}'>
                        <div>
                            <div id="caseStaffNote" class="box discussions"></div>
                        </div>
                        <script type="text/javascript">
                            var parameters = {};
                            parameters.ctxPath = '${contextPath}';
                            parameters.userId = '${uid}';
                            parameters.userName = "${name}";
                            parameters.containerName = 'caseStaffNote';
                            parameters.caseId = '${param.caseid}';
                            loadCaseStaffMsgBoard(parameters);
                        </script>
                    </c:if>
                </c:if>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
