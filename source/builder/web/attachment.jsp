<%-- 
    Document   : attachment.jsp
    Created on : Apr 24, 2011, 10:32:26 AM
    Author     : jiangjeff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<un:useConstants var="Rights" className="com.ocs.indaba.common.Rights"/>
<!--
<link type="text/css" rel="stylesheet" href="plugins/uniform/css/uniform.default.css"/>
<script type="text/javascript" src="plugins/uniform/js/jquery.uniform.js"></script>-->
<script src='js/ajaxfileupload.js' type="text/javascript" language="javascript"></script>
<script src='js/jquery.hashtable.js' type="text/javascript" language="javascript"></script>
<script src='js/json2.js' type="text/javascript" language="javascript"></script>
<!--<script type="text/javascript" src="http://getfirebug.com/releases/lite/1.2/firebug-lite-compressed.js"></script>-->
<script type="text/javascript">
    String.prototype.trim=function() {
        return this.replace(/(^\s*)|(\s*$)/g,"");
    }

    Date.prototype.format = function(formatStr)
    {
        var o = {

            "M+" : this.getMonth()+1,
            "d+" : this.getDate(),
            "h+" : this.getHours(),
            "m+" : this.getMinutes(),
            "s+" : this.getSeconds(),
            "q+" : Math.floor((this.getMonth()+3)/3),
            "S" : this.getMilliseconds()
        }

        if(/(y+)/.test(formatStr))
            formatStr = formatStr.replace(RegExp.$1,(this.getFullYear()+"").substr(4 - RegExp.$1.length));
        for(var k in o)
            if(new RegExp("("+ k +")").test(formatStr))
                formatStr = formatStr.replace(RegExp.$1,RegExp.$1.length==1 ? o[k] :("00"+ o[k]).substr((""+ o[k]).length));
        return formatStr;
    }
    
    var attachedFiles = new Array();
    var fileCount = 1;
    var fileSelectedMap = new Object();
    
    function getFilename(fullPath) {
        fullPath = fullPath.replace(/\\/g, "/");
        var arr = fullPath.split("/");
        return arr[arr.length - 1];
    }

    function showErrMsg(errmsg) {
        //var errmsgDiv = document.getElementById("fileupload_errmsg");
        //errmsgDiv.innerHTML = "<span style='color: red'><img src='images/warning.gif' /> " + errmsg + "</span>";
        //errmsgDiv.setAttribute("style", "display: block;");
        $('#fileupload_errmsg').empty();
        $('#fileupload_errmsg').append($("<span>" + errmsg + "!</span>"));
        $('#fileupload_errmsg').parent().parent().show();
    }

    function disableErrMsg() {
        //var errmsgDiv = document.getElementById("fileupload_errmsg");
        // errmsgDiv.innerHTML = "";
        //errmsgDiv.setAttribute("style", "display: none;");
        $('#fileupload_errmsg').empty();
        $('#fileupload_errmsg').parent().parent().hide();
    }

    function uploadAttachmentFile(horseId) {
        var fileItem = $('#attachFile');
        var filename = fileItem.attr("value");
        if(filename == null || filename == "") {
            showErrMsg($.i18n.message('common.js.alert.attachfile'));
            //fileItem.focus();
            return false;
        } else {
            disableErrMsg();
        }

        if(checkFileExisted(fileItem[0])) {
            return false;
        }

        // description
        var desc = $('#filedesc');
        var descTxt = desc.attr("value"); 
        if(descTxt == ""){
            var prompts = $.i18n.message('common.js.alert.attachdesc', ['\'<strong>'+filename+'</strong>\'']);
            jConfirm(prompts, $.i18n.message('common.js.alert.title.confirm'), function(choice) {
                if(!choice) { // let user add file description
                    desc.focus();
                } else { // ignore file description
                    handleUploadFile(horseId, descTxt);
                }
            });
        } else {
            handleUploadFile(horseId, descTxt);
        }
        return false;
    }

    function handleUploadFile(horseid, desc) {
        // IE8: input[type='file'] value will be null after submit,
        // and the file name is full path
        var filename = $('#attachFile').val();
        filename = filename.substr(filename.lastIndexOf("\\")+1);

        $("#uploading-img")
        .ajaxStart(function(){
            $(this).show();
        })
        .ajaxComplete(function(){
            $(this).hide();
        });
        $.ajaxFileUpload({
            url: 'attachment.do',
            secureuri: false,
            dataType: 'text',
            data: {
                "action": "add",
                "type": "${param.contentType}",
                "answerid": "${param.answerId}",
                "horseid": horseid,
                "filedesc": encodeURI(desc)
            },
            fileElementId: 'attachFile',
            success: function(data, status) {
                var json = JSON.parse(data);
                if(json.ret == 0) {
                    var i = addAttachedFile(filename); 
                    var currentTime = new Date().format('yyyy/MM/dd hh:mm:ss');
                    var attachedItem = addAttachFileItem(i, filename, json.fsize, desc, json.attachid, currentTime, "${name}");
                    attachedItem.attr("style", "background-color: #E3F2F2;");
                    if(attachedFiles.length == 1) {
                        attachedItem.appendTo($("#attachedlist"));
                    } else {
                        var items = $("#attachedlist tr");
                        items.attr("style", "");
                        switch(i) {
                            case 0:
                                $(items[0]).before(attachedItem);
                                break;
                            case attachedFiles.length -1:
                                $(items[items.length - 1]).after(attachedItem);
                                break;
                            default:
                                $(items[i]).before(attachedItem);
                        }
                    }

                    // clear fields: file and description
                    $("#attachFile").val("");
                    $('#filedesc').val("");
                    $('span.filename').text("");
                    $('#viewfile').val('');
                    $('#attachedlist tr.nodata').remove();
                } else {
                    var error = (json)?json.desc:"";
                    if(error == null || error == "") {
                        error = $.i18n.message('common.js.alert.attachfail', ['<strong>'+$("#attachFile").attr("value")+'</strong>']);
                    }
                    jAlert(error, "Error", null);
                }
            },
            error: function(data, status, e) {
                var json = JSON.parse(data);
                var error = (json)?json.desc:"";
                if(error == null || error == "") {
                    error = $.i18n.message('common.js.alert.attachfail', ['<strong>'+$("#attachFile").attr("value")+'</strong>']) + " <br><strong>" + e + "<strong>";
                }
                jAlert(error, "Error", null);
            }
        });
    }

    function addAttachedFile(filename) {
        attachedFiles.push(filename);
        attachedFiles = attachedFiles.sort();
        for(var i = 0; i < attachedFiles.length; ++i) {
            if(attachedFiles[i] == filename) {
                return i;
            }
        }
        return attachedFiles.length;
    }

    function addAttachFileItem(i, filename, filesize, filedesc, attachid, time, user) {
        var record =$("<tr class='attach' id='attachedlist-" + attachid + "' title='"+filedesc+"'></tr>");
        var filenameTd = $("<td align='left'><a id='attached-filename-" + attachid + "' href='attachment.do?action=download&attachid=" + attachid + "' title='" + filedesc + "'>" + filename + "</a></td>");
        filenameTd.appendTo(record);
        var size = (filesize >= 1024*1024) ? Math.floor(filesize / (1024*1024)) + " MB" :
            (filesize >= 1024) ? Math.floor(filesize / 1024) + " KB" : filesize + " B"
        var filesizeTd = $("<td align='left'>" + size + "</td>");
        filenameTd.after(filesizeTd);
        var timeTd = $("<td align='left'>" + time + "</td>");
        filesizeTd.after(timeTd);
        var userTd = $("<td align='left'><a href='profile.do?targetUid=${uid}'>" + user + "</a></td>");
        timeTd.after(userTd);
        var actionTd = $("<td class='journal-attachment-action'><a title='download' href='attachment.do?action=download&attachid=" + attachid+ "&answerid=${param.answerId}&horseid=${param.horseId}&preversion=${param.preversion}&type=${param.contentType}'><img alt='download' width='15px' src='images/down.png'/></a>&nbsp;&nbsp;&nbsp;<a title='delete' href='#' onclick='return deleteAttachment(\"" + attachid+ "\", \"${param.contentType}\", \"${param.answerId}\");'><img alt='delete' width='12px' src='images/delete.png'/></a></td>");
        userTd.after(actionTd);
        return record;
    }

    function checkFileExisted(fileItem) {
        var filename = fileItem.value;
        // IE8: the filename is full name
        filename = filename.substr(filename.lastIndexOf('\\')+1);
        var existed = false;
        for(var i = 0; i < attachedFiles.length; ++i) {
            if(attachedFiles[i] == filename){
                existed = true;
                break;
            }
        }
        if(existed) {
            //fileItem.value = "";
            $("#attachFile").attr("value", "");
            $('#filedesc').attr("value", "");
            $("#attachFile").val("");
            $('#filedesc').val("");
            showErrMsg($.i18n.message('common.js.alert.attachduplicated', ['\'<strong>'+ filename +'</strong>\'']));
            $('div.uploader span.filename').text('');
            $('#viewfile').val('');
        } else {
            $('#viewfile').val(filename);
            $('div.uploader span.filename').text(filename);
            disableErrMsg();
        }
        return existed;
    }

    function deleteAttachment(attachmentId, type, answerId) {
        var filename = $("#attached-filename-" + attachmentId).text();
        var prompts = $.i18n.message('common.js.alert.deleteattach', ['\'<strong>' +filename+'</strong>\'']);
        jConfirm(prompts, $.i18n.message('common.js.alert.title.confirm'), function(choice) {
            if(choice) { // let user add file description
                $.post('attachment.do', { action: 'delete',
                    type: type,
                    answerid: answerId,
                    attachid: attachmentId
                }, function(data) {
                    var json = JSON.parse(data);
                    if(json.ret == 0) {
                        var index = $.inArray(filename, attachedFiles);
                        attachedFiles.splice(index, 1);
                        var attachedList = document.getElementById("attachedlist");
                        var attachedRow = document.getElementById("attachedlist-" + attachmentId);
                        attachedList.removeChild(attachedRow);
                        if($('#attachedlist tr.attach').length == 0) {
                            $('#attachedlist').append($('<tr class="nodata"><td colspan="5">No <indaba:msg key="jsp.attachment.attachments" />.</td></tr>'));
                        }
                    } else {
                        jAlert($.i18n.message('common.js.alert.deleteattachfail'), $.i18n.message('common.js.alert.title.error'), null);
                    }
                });
            }
        });
        return false;
    }
</script>
<c:set var="viewRight" value="${Rights.NON_EXISTED_RIGHT}" />
<c:set var="manageRight" value="${Rights.NON_EXISTED_RIGHT}" />
<c:choose>
    <c:when test="${param.contentType eq 'journal'}">
        <c:set var="viewRight" value="${Rights.SEE_JOURNAL_FILE_ATTACHMENTS}" />
        <c:set var="uploadRight" value="${Rights.DOWNLOAD_AND_ATTACH_FILES_TO_JOURNAL}" />
        <c:set var="manageRight" value="${Rights.DELETE_FILES_FROM_JOURNAL}" />
    </c:when>
    <c:when test="${param.contentType eq 'survey'}">
        <c:set var="viewRight" value="${Rights.SEE_INDICATOR_FILE_ATTACHMENTS}" />
        <c:set var="uploadRight" value="${Rights.DOWNLOAD_AND_ATTACH_FILES_TO_SURVEY}" />
        <c:set var="manageRight" value="${Rights.DELETE_FILES_FROM_SURVEY}" />
    </c:when>
</c:choose>

<indaba:view prjid="${prjid}" uid="${uid}" right="${viewRight} | ${uploadRight} | ${manageRight} | ${Rights.SUPER_EDIT_CONTENT}" >
    <div class="box answer-part-attachment">
        <h3><a name="attachments"><indaba:msg key='jsp.attachment.attachments' /></a> <a href="#attachments" class="toggleVisible"><img src='images/collapse_icon.png' alt="" /></a></h3>
        <div class="content">
            <table id="journal-attachment-border">
                <thead>
                    <tr class="thead">
                        <th><indaba:msg key='jsp.attachment.file' /></th>
                <th><indaba:msg key='jsp.attachment.size' /></th>
                <th><indaba:msg key='common.label.time' /> </th>
                <th><indaba:msg key='jsp.attachment.by' /></th>
                <th class="journal-attachment-action"><indaba:msg key='common.label.action' /></th>
                </tr>
                </thead>

                <tbody id="attachedlist">
                    <c:choose>
                        <c:when test="${(empty attachments)}" >
                            <tr class="nodata"><td colspan="5">No <indaba:msg key='jsp.attachment.attachments' />.</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${attachments}" var="attach">
                            <script language="javascript" type="text/javascript">
                                attachedFiles.push("${attach.name}");
                            </script>
                            <tr class="attach" id="attachedlist-${attach.id}" title="${attach.note}" >
                                <td align="left">
                                    <a id="attached-filename-${attach.id}" href="attachment.do?action=download&attachid=${attach.id}&answerid=${param.answerId}&horseid=${param.horseId}&preversion=${param.preversion}&type=${param.contentType}" title="${attach.note}">${attach.name}</a>
                                </td>
                                <td align="left">
                                    <c:choose>
                                        <c:when test="${attach.size > 1024 * 1024}"><fmt:formatNumber var="fileSize" value="${attach.size / (1024 * 1024)}" maxFractionDigits="0" />${fileSize} <indaba:msg key='jsp.attachment.mb' /></c:when>
                                <c:when test="${attach.size > 1024}"><fmt:formatNumber var="fileSize" value="${attach.size / 1024}" maxFractionDigits="0" />${fileSize} <indaba:msg key='jsp.attachment.kb' /></c:when>
                                <c:otherwise><c:set var="fileSize" value="${attach.size}"/>${fileSize} <indaba:msg key='jsp.attachment.b' /></c:otherwise>
                            </c:choose>
                            </td>
                            <td align="left">
                                <fmt:formatDate pattern="yyyy/MM/dd hh:mm:ss" value="${attach.updateTime}" />
                            </td>
                            <td align="left">
                                <c:choose>
                                    <c:when test="${attach.userId == 0}">
                                        --
                                    </c:when>
                                    <c:otherwise>
                                    <indaba:userDisplay prjid="${prjid}" subjectUid="${uid}" targetUid="${attach.userId}" showBio="false" />
                                </c:otherwise>
                            </c:choose>
                            </td>
                            <td class="journal-attachment-action">
                                <a title="download" href="attachment.do?action=download&attachid=${attach.id}&answerid=${param.answerId}&horseid=${param.horseId}&preversion=${param.preversion}&type=${param.contentType}<c:if test="${view.preVersionMode}">&preVersion=true</c:if>"><img alt="download" width='15px' src="images/down.png"/></a>&nbsp;&nbsp;
                                    <c:if test="${not view.preVersionMode}">
                                <indaba:view flag="${(empty param.readonly) || !param.readonly}" ignore="${uid == attach.userId}" prjid="${prjid}" uid="${uid}" right="${manageRight} | ${Rights.SUPER_EDIT_CONTENT}" >
                                    <a title="delete" href="#" onclick='return deleteAttachment("${attach.id}", "${param.contentType}", "${param.answerId}");'><img alt="delete" width='12px' src="images/delete.png"/></a>
                                </indaba:view>
                            </c:if>
                            </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
            <c:if test="${not view.preVersionMode}">
                <indaba:view flag="${(empty param.readonly) || !param.readonly}" ignore="${!empty param.required && param.required}" prjid="${prjid}" uid="${uid}" right="${manageRight} | ${uploadRight} | ${Rights.SUPER_EDIT_CONTENT}" >
                    <div style="border: 1px solid #ddd; padding: 5px; margin-top: 10px;">
                        <div><span style="font-weight: bold"><indaba:msg key='jsp.attachment.addNewAttachments' /></span> &nbsp;&nbsp;(<span style="color: orange"><indaba:msg key='jsp.attachment.maxFileSize' />: <strong><indaba:msg key='jsp.attachment.maxFileSizeNum' /></strong></span>)</div>
                        <div class="fileuploadForm" style="padding-top: 15px;">
                            <dl>
                                <dt><label for="file"><indaba:msg key='common.label.file' /></label></dt>
                                <dd style="line-height: 26px;">
                                    <span style="float: left;"><input  type="text" name="viewfile" id="viewfile" class="viewfile" onclick="return false;" /></span>
                                    <span class="ocs-file-btn"><input type="file" name="file" id="attachFile" onchange="return checkFileExisted(this);"/><indaba:msg key='common.btn.browse' /></span>
                                </dd>
                            </dl>
                            <dl>
                                <dt><label for="filedesc"><indaba:msg key='common.label.desc' /></label></dt>
                                <dd style="line-height: 30px;">
                                    <input type="text" id="filedesc" name="filedesc" onchange="checkMaxWords(this)" onkeypress="checkMaxWords(this)" style="height: 22px; line-height: 22px; width: 360px; padding: 2px; position: relative; top:1px;border:1px solid #aaa;" />
                                    <input type="button" id="add_file_button" class="large button blue" name="add_file_button" value='  <indaba:msg debug="true" key="common.btn.add" />  ' style="margin-left: 10px; padding: 5px 10px; height: 28px; position: relative; top: 1px;" onclick="return uploadAttachmentFile(${param.horseid});"/>
                                    <img id="uploading-img" alt="uploading image" src="images/loading.gif" width="20px;" style="display: none; vertical-align: middle; margin-left: 15px;">
                                </dd>
                            </dl>
                            <dl style="display:none;">
                                <dt>&nbsp;</dt>
                                <dd><span id="fileupload_errmsg"></span></dd>
                            </dl>
                            <div class="clear"></div>
                        </div>
                    </div>
                </indaba:view>
            </c:if>
        </div>
    </div>

    <script type="text/javascript">
        $(function(){
        });
    </script>
</indaba:view>