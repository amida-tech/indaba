<%-- 
    Document   : indicator-importtbl-inc
    Created on : Jan 13, 2014, 8:02:45 PM
    Author     : ningshan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/upload/fileuploader.css">

<div id="indicatorTableImportPopup" class="dlg hidden">
    <div style="margin-top: 10px;">
        <div style="font-weight:bold; margin-bottom: 15px"><indaba:msg key="cp.text.specify_indicator_file"/></div>
        <form id="import-indicator" action="${contextPath}/lib/indicator-lib!validateIndicatorImport" enctype ="multipart/form-data" >
            <div id="fileuploader">
                <noscript>
                    <p>Please enable JavaScript to use file uploader.</p>
                </noscript>
            </div>
            <div id="uploadTableResult" style="margin-top: 10px;" class="error-box hidden"></div>
        </form>
    </div>
</div>
<script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/upload/fileuploader.js"></script>
<script type="text/javascript" charset="utf-8">
    var tblUUID ="";
    var tblFilename="";
    var tableDefineForm = $('tableFileSettingForm');
    var indicatorTableImportPopup = $('#indicatorTableImportPopup');
    var uploadTableResult = $('#uploadTableResult', indicatorTableImportPopup);
    
    function initTableFileUpload(ocsDlg, uploadActionUrl) {
        cleanupTableUploadBox();
        var uploader = new qq.FileUploader({
            element: $('#fileuploader', indicatorTableImportPopup)[0],
            action: uploadActionUrl,
            label: $.i18n.message('cp.btn.upload_file'),
            allowedExtensions: ['xls', 'xlsx'],
            debug: true,
            extraDropzones: [qq.getByClass(document, 'qq-upload-extra-drop-area')[0]],
            messages: {typeError: $.i18n.message('cp.error.file_extension', ['xls, xlsx'])},
            showMessage: function(message){
                ocsError(message);
            },
            onSubmit: function(id, filename) {
                cleanupTableUploadResultBox();
                $('.qq-uploader .qq-upload-list', indicatorTableImportPopup).empty();
            },
            onComplete: function(id, filename, respJSON) {
                
                if(respJSON.ret == 0) {
                    if(respJSON.data) {
                        var data = respJSON.data;
                        if(data.errCount == 0) {
                            showTableUploadResultBox(true);
                            uploadTableResult.append($('<div class="summary">'+$.i18n.message('cp.text.success_validate_file')+'</div>'));
                            uploadTableResult.append($('<div style="margin: 5px 0px;">'+$.i18n.message('cp.text.load_indicator_file', ['<input id="import" type="button" class="btn" value="Import" style="padding: 5px 10px;" />'])+'</div>'));
                            tblUUID =data.filePath;
                            tblFilename=data.uploadedFilename;
                           $('input:button#import', uploadTableResult).click(function() {
                                $('#tdfFileName').val(tblFilename);
                                $('#pathName').val(tblUUID);
                                cleanupUploadBox();
                                ocsDlg.dialog("close");
                                return false;
                            });
                        } else {
                            showTableUploadResultBox(false);
                            uploadTableResult.append($('<div class="summary">'+$.i18n.message("cp.text.fail_validate_file", [data.errCount])+'</div>'));
                            var errList = $('<ul class="err-list"></ul>');
                            if(data.errors){
                                $.each(data.errors, function(index, val){
                                    errList.append($('<li> - ' + val + '</li>'));
                                });
                            }
                            uploadTableResult.append(errList);
                        }
                    } else {
                    }
                } else {
                    showTableUploadResultBox(false, $.i18n.message('cp.err.server_error'));
                }
            }
        });
        $('<img width="16px" src="${contextPath}/resources/images/upload.png" style="position:relative; top: 2px; margin-right:2px;">').insertBefore($('.qq-uploader .qq-upload-button .upload-btn-text', indicatorTableImportPopup));
    }

    function showTableUploadResultBox(success, desc) {
        uploadTableResult.text((desc)?desc: '');
        uploadTableResult.show();
        if(success) {
            uploadTableResult.addClass('success-box');
            uploadTableResult.removeClass('error-box');
        } else {
            uploadTableResult.removeClass('success-box');
            uploadTableResult.addClass('error-box');
        }
    }
    function cleanupTableUploadResultBox() {
        uploadTableResult.text('');
        uploadTableResult.hide();
    }
    function cleanupTableUploadBox() {
        $('.qq-uploader', indicatorTableImportPopup).remove();
        cleanupTableUploadResultBox();
    }

    
    function doImportTableIndicator(contextPath){
        var ocsDlg = $('#indicatorTableImportPopup');
        initTableFileUpload(ocsDlg, contextPath+'/lib/indicator-lib!validateTableImport');
        //$('input:file', ocsDlg).uniform();
        ocsDlg.dialog({
            width: 460,
            title: $.i18n.message('cp.title.import_indicators'),
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

    function doDownloadTableIndicator(contextPath) {
        var tdfFileName = $('#tdfFileName').val();
        var pathName = $('#pathName').val();
        var url = contextPath+'/lib/indicator-lib!downloadTableDef?tdfFileName=' + encodeURIComponent(tdfFileName) + '&pathName=' + encodeURIComponent(pathName);
        window.location = url;
    }


</script>