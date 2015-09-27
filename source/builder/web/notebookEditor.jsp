<%-- 
    Document   : notebookGeneral
    Created on : 2010-3-23, 20:31:12
    Author     : Luke Shi
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="indaba" uri="indabaTaglib"%>

<link type="text/css" rel="stylesheet" href="css/button.css"/>
<script src='js/base_type.js' type="text/javascript" language="javascript"></script>
<script type="text/javascript">
    function checkMaxWords(obj) {
        if(obj.value.length >= 250) {
            jAlert($.i18n.message('common.js.alert.toolength'), $.i18n.message('common.js.alert.warn'), null);
            obj.value = obj.value.substr(1, 251);
            return false;
        }
        return true;
    }

    function submitform(action) {
        document.forms["upload"].elements["action"].value = action;
        var unAddedFilename = $("#attachFile").attr("value");
        if(unAddedFilename != null && unAddedFilename.length > 0) {
            var prompts = $.i18n.message('common.js.alert.filenotadded', ['<strong>'+unAddedFilename+'</strong>'])
            jConfirm(prompts, $.i18n.message('common.js.alert.confirm'), function(choice) {
                if(!choice) {
                    if ($.browser.msie){
                        $('#attachFile').remove();
                    }
                    else{
                        $("#attachFile").attr("value", "");
                        $('#file_description').attr("value", "");
                    }
                    document.forms["upload"].submit();
                }
            });
            return false;
        } else {
            return true;
        }
    }

    function openJournalContentVersion(versionId) {
        window.open('journalContentVersion.do?contentVersionId=' + versionId,'journalContentVersion','width=800,height=600,toolbar=no, resizable=yes, location=no, scrollbars=yes');
        return false;
    }

</script>

<div>
    <h2 class="pos-relative">
        ${journalTitle}
        <c:if test="${!empty contentVersionList}">
            <div id="previous-versions-div">
                <a id="previous-versions-link" href="#" onclick="return false;"><img src="images/previous-versions.png" height="15px"/>&nbsp;<indaba:msg key='jsp.notebookEditor.previousVersions' /></a>
                <ul id="previous-versions-list">
                    <c:forEach items="${contentVersionList}" var="cntVer" varStatus="status">
                        <li style="text-align: left;">
                            <fmt:formatDate value="${cntVer.createTime}" var="timestamp" pattern="yyyy-MM-dd HH:mm:ss"/>
                            <a href="#" onclick="return openJournalContentVersion(${cntVer.id});">
                                <c:set var="version_icon" value="old_version.png"/>
                                <c:if test="${status.index == 0}"><c:set var="version_icon" value ="latest_version.png"/></c:if>
                                <img src='images/${version_icon}' /> | <I><indaba:userDisplay prjid="${prjid}" subjectUid="${uid}" targetUid="${cntVer.userId}" pureText="true"/></I>
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>
    </h2>

    <form method="post" action="notebook.do" enctype="multipart/form-data" name="upload" accept-charset="UTF-8">
        <input type="hidden" name="task" value="${task}" id="taskHidden" />
        <input type="hidden" name="action" value="${action}" id="actionHidden" />
        <input type="hidden" name="tasktype" value="${tasktype}" id="tasktypeHidden" />
        <input type="hidden" name="returl" value="${returl}" />
        <input type="hidden" id="hiddenMinWords" name="minWords" value="${minWords}" />
        <input type="hidden" id="hiddenMaxWords" name="maxWords" value="${maxWords}" />
        <input type="hidden" name="horseid" value="${horseid}" />
        <input type="hidden" name="assignid" value="${assignid}" />
        <input type="hidden" name="toolid" value="${toolid}" />
        <input type="hidden" name="superedit" value="${superedit}" />
        <div class="box">
            <c:if test="${maxWords > 0}">
                <h3><a name="journalContent"><indaba:msg key='jsp.notebookEditor.notebookEditor' /> &nbsp;&nbsp;&nbsp;&nbsp;</a>
                    <img id="notebook-editor-tip" class="tipTip" src='images/hint_icon.png' title="${fn:replace(notebookinstr, "\"", '&quot;')}" alt="(Instructions)"/>
                    &nbsp;&nbsp;&nbsp;&nbsp;<span style="color: #5E5E5E; font-size: 12px;"><indaba:msg key='jsp.notebookEditor.words' />: ${minWords} ~ ${maxWords}</span>&nbsp;<a href="#journalContent" class="toggleVisible"><img src='images/collapse_icon.png' alt="" /></a>
                </h3>
                <script>$(".tipTip").tipTip();</script>
                <div class="content" style="padding-top: 5px;">
                    <div style="padding:10px 0 10px 0;">
                        <textarea class="text" name="body" id="journaleditor" style="width:100%;height:400px">
                            ${journalBody}
                        </textarea>
                        <script>
                            CKEDITOR.replace('journaleditor', {customConfig : '${contextPath}/js/ckeditor_config.js', resize_dir: 'vertical'});
                        </script>
                    </div>
                </div>
            </c:if>

            <div class='content'>
                <!-- include attachment widget -->
                <jsp:include page="attachment.jsp" flush="true" >
                    <jsp:param name="contentType" value="journal" />
                    <jsp:param name="uploaderWidth" value="582px" />
                    <jsp:param name="filenameWidth" value="385px" />
                    <jsp:param name="required" value="${param.requireAttach}" />
                    <jsp:param name="horseId" value="${horseid}" />
                </jsp:include>

                <div align="center">
                    <button class="large button blue icon-save" type="submit" id="nbesavebutton" name="nbesavebutton" onclick="return submitform('${(empty action) ? "save" : action}');"><indaba:msg key='common.btn.save' /></button>
                    <c:if test="${empty param.showSubmitButton || param.showSubmitButton}">
                        <button type="submit" class="iamdone large button blue icon-check" id="nbesubmitbutton" onclick="return submitform('save&done');"><indaba:msg key='common.btn.donesubmit' /></button>
                    </c:if>
                    <c:if test="${!empty superedit && superedit == 1}">
                        <button type="submit" class="large button blue icon-cancel" id="nbcancelbutton" onclick="javascript:history.back(1); return false;"><indaba:msg key='common.btn.cancel' /></button>
                    </c:if>
                </div>
            </div>
        </div>
    </form>
</div>
<%--
<script type="text/javascript" language="javascript">
    //addFileUploadItem(1);
</script>
--%>
