<%-- 
    Document   : indicator-import-inc
    Created on : 2012-8-21, 10:11:17
    Author     : Jeff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/upload/fileuploader.css">
<div id="contributorImportPopup" class="dlg hidden">
    <div style="margin-top: 10px;">
        <div style="font-weight:bold; margin-bottom: 15px"><indaba:msg key="cp.text.specify_contributor_file"/></div>
        <form id="import-indicator" action="${contextPath}/proj/project-membership!validateCsv" enctype ="multipart/form-data" >
            <div id="fileuploadContrib">
                <noscript>
                    <p>Please enable JavaScript to use file uploader.</p>
                </noscript>
            </div>
        </form>
    </div>
</div>
<div id="contribValidateSuccessPopup" class="dlg hidden">
    <div id="contribValidateSuccessFormBox" class="popup-container">
        <form id="contribValidateSuccessForm">
            <fieldset>
                <input type="hidden" id="uploadedFilename" name="csvFilename" />
                <dl>
                    <dt><label for="newUserCount"><indaba:msg key="cp.label.new_user_num" /></label></dt>
                    <dd><input type="text" name="newUserCount" id="newUserCount" class="short-input" readonly="readonly"/></dd>
                </dl>
                <dl>
                    <dt><label for="existUserCount"><indaba:msg key="cp.label.exist_user_num" /></label></dt>
                    <dd><input type="text" name="existUserCount" id="existUserCount" class="short-input" readonly="readonly"/></dd>
               </dl>
               <dl>
                    <dt><label for="contributorNote"><indaba:msg key="cp.label.note" /></label></dt>
                    <dd><textarea id="contributorNote" name="note" class="long-input"></textarea></dd>
                </dl>                        
            </fieldset>
        </form>
    </div>
</div>
<div id="contribValidateFailurePopup" class="dlg hidden">
    <div id="contribValidateFailureFormBox" class="popup-container">
    <span><indaba:msg key="cp.label.below_lines_have_errors" />:</span><br/><br/>
    <ul id="contribValidateFailureErrorList"></ul>
    </div>
</div>
<script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/upload/fileuploader.js"></script>
<script type="text/javascript" charset="utf-8">
    var contributorImportPopup = $('#contributorImportPopup');
    var uploadResult = $('#uploadResult', contributorImportPopup);
    function initContribFileUpload(ocsDlg, uploadActionUrl, importActionUrl) {
        cleanupContribUploadBox();
        var uploader = new qq.FileUploader({
            element: $('#fileuploadContrib', contributorImportPopup)[0],
            action: uploadActionUrl,
            label: $.i18n.message('cp.btn.upload_file'),
            allowedExtensions: ['csv', 'bcsv', 'xls', 'xlsx'],
            debug: true,
            extraDropzones: [qq.getByClass(document, 'qq-upload-extra-drop-area')[0]],
            messages: {typeError: $.i18n.message('cp.error.file_extension', ['csv, bcsv, xls, xlsx'])},
            showMessage: function(message){
                ocsError(message);
            },
            onSubmit: function(id, filename) {
                cleanupContribUploadResultBox();
                $('.qq-uploader .qq-upload-list', contributorImportPopup).empty();
                $('#uploadedFilename', contributorImportPopup).val();
            },
            onComplete: function(id, filename, respJSON) {
                if(respJSON.ret == 0) {
                    if(respJSON.data) {
                        var data = respJSON.data;
                        if(data.errCount == 0) {
                            var ocsDlg = $('#contributorImportPopup');
                            ocsDlg.dialog('close');
                            showContribValidateSuccessDlg(data, importActionUrl);
                        } else {
                            var ocsDlg = $('#contributorImportPopup');
                            ocsDlg.dialog('close');
                            showContribValidateFailureDlg(data);
                        }
                    } else {
                    }
                } else {
                    showContribUploadResultBox(false, $.i18n.message('cp.err.server_error'));
                }
            }
        });
        $('<img width="16px" src="${contextPath}/resources/images/upload.png" style="position:relative; top: 2px; margin-right:2px;">').insertBefore($('.qq-uploader .qq-upload-button .upload-btn-text', contributorImportPopup));
        uploader.setParams({projId: '${projId}'});
    }

    function showContribUploadResultBox(success, desc) {
        uploadResult.text((desc)?desc: '');
        uploadResult.show();
        if(success) {
            uploadResult.addClass('success-box');
            uploadResult.removeClass('error-box');
        } else {
            uploadResult.removeClass('success-box');
            uploadResult.addClass('error-box');
        }
    }
    function cleanupContribUploadResultBox() {
        uploadResult.text('');
        uploadResult.hide();
    }
    function cleanupContribUploadBox() {
        $('.qq-uploader', contributorImportPopup).remove();
        cleanupContribUploadResultBox();
    }
    function importContribUploadedFile(ocsDlg, importActionUrl) {
        var loading=new ol.loading({id:"contribValidateSuccessPopup", loadingText:"Waiting..."});
        $.ajax({
            url: importActionUrl,
            type: 'POST',
            dataType: 'json',
            data: {csvFilename: $('#uploadedFilename', ocsDlg).val(), projId: ${projId}, note: $('#contributorNote', ocsDlg).val()},
            success: function(respJSON){
                loading.hide();
                if(respJSON.ret != 0) {
                    ocsError(respJSON.desc);
                } else {
                    $("#contributors-flexgrid").flexReload({newp: 1,
                        dataType: 'json'
                    });
                    cleanupContribUploadBox();
                    ocsDlg.dialog("close");
                    ocsSuccess("Contributors imported: " + respJSON.count + ".");
                }
            },
            error: function(data) {
                loading.hide();
                defaultAjaxErrorHanlde(data);
            }
        });
    }
    function showContribValidateSuccessDlg(data, importActionUrl) {
        var dlg = $('#contribValidateSuccessPopup');
        var form = $('#contribValidateSuccessForm', dlg);
        $('#newUserCount', form).val(data.newcount);
        $('#existUserCount', form).val(data.existcount);
        $('#uploadedFilename', form).val(data.uploadedFilename);
        dlg.dialog({
            width: 662,
            title: $.i18n.message('cp.title.import_contributors'),
            resizable: false,
            modal: true,
            open: function(){
            },
            close: function(){
                resetForm($('#contribValidateSuccessForm'));
            },
            buttons: {
                "Import" : function() {
                    importContribUploadedFile(dlg, importActionUrl);
                },
                "Cancel": function() {
                    $(this).dialog("close");
                }
            }
        });
        return false;
    }

    function showContribValidateFailureDlg(data) {
        var errList = $('#contribValidateFailureErrorList');
        errList.empty();
        for(var i = 0; i < data.errcsv.length; ++i) {
            var html = $('<li> - ' + data.errcsv[i].line + '</li>');
            errList.append(html);
        }
        var dlg = $('#contribValidateFailurePopup');
        dlg.dialog({
            width: 662,
            title: $.i18n.message('cp.title.import_contributors'),
            resizable: false,
            modal: true,
            open: function(){
            },
            close: function(){
            },
            buttons: {
                "Close": function() {
                    $(this).dialog("close");
                }
            }
        });
        return false;
    }
    function doImportContributor() {
        var ocsDlg = $('#contributorImportPopup');
        initContribFileUpload(ocsDlg, '${contextPath}'+'/proj/project-membership!validateCsv', '${contextPath}'+'/proj/project-membership!bulkCreate');
        ocsDlg.dialog({
            width: 460,
            title: $.i18n.message('cp.title.import_contributors'),
            resizable: false,
            modal: true,
            open: function(){
            },
            close: function(){
            },
            buttons: {
                "Cancel": function() {
                    $(this).dialog("close");
                }
            }
        });
        return false;
    }
</script>