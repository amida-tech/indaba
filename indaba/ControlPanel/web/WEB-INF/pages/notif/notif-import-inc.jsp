<%-- 
    Document   : notif-import-inc
    Created on : Dec 18, 2013, 4:58:54 PM
    Author     : ningshan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/upload/fileuploader.css">
<div id="nt-import-popup" class="dlg hidden">
    <div style="margin-top: 10px;">
        <div style="font-weight:bold; margin-bottom: 15px"><indaba:msg key="cp.text.specify_notification_file"/></div>
        <form id="nt-import" action="${contextPath}/notif/notifications!validateCsv" enctype ="multipart/form-data" >
            <div id="fileupload">
                <noscript>
                    <p>Please enable JavaScript to use file uploader.</p>
                </noscript>
            </div>
        </form>
    </div>
</div>
<div id="validateNotifSuccessPopup" class="dlg hidden">
    <div id="validateNotifSuccessFormBox" class="popup-container">
        The file is valid. Are you sure you want to import?
        <form id="validateNotifSuccessForm">
            <fieldset>
                <input type="hidden" id="uploadedFilename" name="csvFilename" />                                      
            </fieldset>
        </form>
    </div>
</div>
<div id="validateNotifFailurePopup" class="dlg hidden">
    <div id="validateNotifFailureFormBox" class="popup-container">
    <span><indaba:msg key="cp.label.below_lines_have_errors" />:</span><br/><br/>
    <ul id="validateNotifFailureErrorList"></ul>
    </div>
</div>
<script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/upload/fileuploader.js"></script>
<script type="text/javascript" charset="utf-8">
    var nntImpPopup = $('#nt-import-popup');
    var uploadResult = $('#uploadResult', nntImpPopup);
    function initFileUpload(ocsDlg, uploadActionUrl, importActionUrl) {
        cleanupUploadBox();
        var uploader = new qq.FileUploader({
            element: $('#fileupload', nntImpPopup)[0],
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
                cleanupUploadResultBox();
                $('.qq-uploader .qq-upload-list', nntImpPopup).empty();
                $('#uploadedFilename', nntImpPopup).val();
            },
            onComplete: function(id, filename, respJSON) {
                if(respJSON.ret == 0) {
                    if(respJSON.data) {
                        var data = respJSON.data;
                        if(data.errCount == 0) {
                            var ocsDlg = $('#nt-import-popup');
                            ocsDlg.dialog('close');
                            showValidateSuccessDlg(data, importActionUrl);
                        } else {
                            var ocsDlg = $('#nt-import-popup');
                            ocsDlg.dialog('close');
                            showValidateFailureDlg(data);
                        }
                    } else {
                    }
                } else {
                    showUploadResultBox(false, $.i18n.message('cp.err.server_error'));
                }
            }
        });
        $('<img width="16px" src="${contextPath}/resources/images/upload.png" style="position:relative; top: 2px; margin-right:2px;">').insertBefore($('.qq-uploader .qq-upload-button .upload-btn-text', nntImpPopup));
        uploader.setParams({projId: '${projId}'});
    }

    function showUploadResultBox(success, desc) {
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
    function cleanupUploadResultBox() {
        uploadResult.text('');
        uploadResult.hide();
    }
    function cleanupUploadBox() {
        $('.qq-uploader', nntImpPopup).remove();
        cleanupUploadResultBox();
    }
    function importUploadedFile(ocsDlg, importActionUrl) {
        var loading=new ol.loading({id:"validateNotifSuccessPopup", loadingText:"Waiting..."});
        $.ajax({
            url: importActionUrl,
            type: 'POST',
            dataType: 'json',
            data: {csvFilename: $('#uploadedFilename', ocsDlg).val()},
            success: function(respJSON){
                loading.hide();
                if(respJSON.ret != 0) {
                    ocsError(respJSON.desc);
                } else {
                    $("#notifications-flexgrid").flexReload({newp: 1,
                        dataType: 'json'
                    });
                    cleanupUploadBox();
                    ocsDlg.dialog("close");
                    ocsSuccess("Notifications imported: " + respJSON.data.count);
                }
            },
            error: function(data) {
                loading.hide();
                defaultAjaxErrorHanlde(data);
            }
        });
    }

    function showValidateSuccessDlg(data, importActionUrl) {
        var dlg = $('#validateNotifSuccessPopup');
        var form = $('#validateNotifSuccessForm', dlg);
        $('#uploadedFilename', form).val(data.uploadedFilename);
        dlg.dialog({
            width: 662,
            title: $.i18n.message('cp.title.import_notifications'),
            resizable: false,
            modal: true,
            open: function(){
            },
            close: function(){
                resetForm($('#validateNotifSuccessForm'));
            },
            buttons: {
                "Import" : function() {
                    importUploadedFile(dlg, importActionUrl);
                },
                "Cancel": function() {
                    $(this).dialog("close");
                }
            }
        });
        return false;
    }

    function showValidateFailureDlg(data) {
        var errList = $('#validateNotifFailureErrorList');
        errList.empty();
        for(var i = 0; i < data.errors.length; ++i) {
            var html = $('<li> - ' + data.errors[i] + '</li>');
            errList.append(html);
        }
        var dlg = $('#validateNotifFailurePopup');
        dlg.dialog({
            width: 662,
            title: $.i18n.message('cp.title.import_notifications'),
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

    function doImportNotificationItems() {
        var ocsDlg = $('#nt-import-popup');
        initFileUpload(ocsDlg, '${contextPath}'+'/notif/notifications!validateCsv', '${contextPath}'+'/notif/notifications!importNotifications');
        ocsDlg.dialog({
            width: 460,
            title: $.i18n.message('cp.title.import_notifications'),
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