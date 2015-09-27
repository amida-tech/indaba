<%-- 
    Document   : indicator-import-inc
    Created on : 2012-8-21, 10:11:17
    Author     : Jeff
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/upload/fileuploader.css">
<div id="indicatorImportPopup" class="dlg hidden">
    <div style="margin-top: 10px;">
        <div style="font-weight:bold; margin-bottom: 15px"><indaba:msg key="cp.text.specify_indicator_file"/></div>
        <form id="import-indicator" action="${contextPath}/lib/indicator-lib!validateIndicatorImport" enctype ="multipart/form-data" >
            <input type="hidden" id="uploadedFilename" name="uploadedFilename" />
            <div id="fileupload">
                <noscript>
                    <p>Please enable JavaScript to use file uploader.</p>
                </noscript>
            </div>
            <div id="uploadResult" style="margin-top: 10px;" class="error-box hidden"></div>
        </form>
    </div>
</div>
<script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/upload/fileuploader.js"></script>
<script type="text/javascript" charset="utf-8">
    var indicatorImportPopup = $('#indicatorImportPopup');
    var uploadResult = $('#uploadResult', indicatorImportPopup);
    function initFileUpload(ocsDlg, uploadActionUrl, importActionUrl) {
        cleanupUploadBox();
        var uploader = new qq.FileUploader({
            element: $('#fileupload', indicatorImportPopup)[0],
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
                $('.qq-uploader .qq-upload-list', indicatorImportPopup).empty();
                $('#uploadedFilename', indicatorImportPopup).val();
            },
            onComplete: function(id, filename, respJSON) {
                if(respJSON.ret == 0) {
                    if(respJSON.data) {
                        var data = respJSON.data;
                        if(data.errCount == 0) {
                            showUploadResultBox(true);
                            uploadResult.append($('<div class="summary">'+$.i18n.message('cp.text.success_validate_file')+'</div>'));
                            uploadResult.append($('<div style="margin: 5px 0px;">'+$.i18n.message('cp.text.load_indicator_file', ['<input id="import" type="button" class="btn" value="Import" style="padding: 5px 10px;" />'])+'</div>'));
                            $('#uploadedFilename', indicatorImportPopup).val(data.uploadedFilename);
                            $('input:button#import', uploadResult).click(function() {
                                importUploadedFile(ocsDlg, importActionUrl);
                                return false;
                            });
                        } else {
                            showUploadResultBox(false);
                            uploadResult.append($('<div class="summary">'+$.i18n.message("cp.text.fail_validate_file", [data.errCount])+'</div>'));
                            var errList = $('<ul class="err-list"></ul>');
                            if(data.genErrors){
                                $.each(data.genErrors, function(index, val){
                                    errList.append($('<li> - ' + val + '</li>'));
                                });
                            }
                            if(data.indicatorErrors){
                                $.each(data.indicatorErrors, function(index, val){
                                    errList.append($('<li> - ' + val + '</li>'));
                                });
                            }
                            uploadResult.append(errList);
                        }
                    } else {
                    }
                } else {
                    showUploadResultBox(false, $.i18n.message('cp.err.server_error'));
                }
            }
        });
        $('<img width="16px" src="${contextPath}/resources/images/upload.png" style="position:relative; top: 2px; margin-right:2px;">').insertBefore($('.qq-uploader .qq-upload-button .upload-btn-text', indicatorImportPopup));
        uploader.setParams({visibility: '${visibility}'});
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
        $('.qq-uploader', indicatorImportPopup).remove();
        cleanupUploadResultBox();
    }
    function importUploadedFile(ocsDlg, importActionUrl) {
        var loading=new ol.loading({id:"indicatorImportPopup", loadingText:"Waiting..."});
        $.ajax({
            url: importActionUrl,
            type: 'POST',
            dataType: 'json',
            data: {uploadedFilename: $('#uploadedFilename', indicatorImportPopup).val()},
            success: function(respJSON){
                loading.hide();
                if(respJSON.ret != 0) {
                    ocsError(respJSON.desc);
                } else {
                    $("#indicator-flexgrid").flexReload({
                        dataType: 'json'
                    });
                    cleanupUploadBox();
                    ocsDlg.dialog("close");
                    ocsSuccess("Indicators imported: " + respJSON.data.count +
                        ". Public indicators are placed in the Test Library, private indicators are placed in the Private Library");
                }
            },
            error: function(data) {
                loading.hide();
                defaultAjaxErrorHanlde(data);
            }
        });
    }
</script>