<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/jquery-ui/css/ui.multiselect.css">
<style type="text/css">
</style>
<div id="scCloneFormPopup" title="<indaba:msg key='cp.title.clone_survey_config'/>" class="hidden">
    <div id="scCloneFormBox" class="popup-container">
        <form id="scCloneForm" action="#" method="POST" >
            <fieldset id="sconfFs">
                <input type="hidden" name="visibility" id="visibility" value="${visibility}"/>
                <input type="hidden" name="srcSconfId" id="srcSconfId" value="-1" />
                <dl>
                    <dt><label for="scName"><indaba:msg key="cp.label.name"/></label></dt>
                    <dd>
                        <input id="scName" type="text" name="scName" class="long-input validate[required]"/>
                    </dd>
                </dl>
                <dl>
                    <dt><label for="organization"><indaba:msg key="cp.label.organization"/></label></dt>
                    <dd>
                        <select id="organization" name="organization" data-placeholder="<indaba:msg key='cp.text.choose_organization'/>" class="small-input validate[required]">
                            <option value=""></option>
                        </select>
                    </dd>
                    <dd class="loading"></dd>
                </dl>
            </fieldset>
        </form>
    </div>
</div>
<script type="text/javascript" charset="utf-8">
    var scCloneFormBox = $('#scCloneFormBox');
    var scCloneForm = $('#scCloneForm', scCloneFormBox);
    $(function(){
        $('input:text, input:radio, input:checkbox, textarea', scCloneFormBox).uniform();
        $("select",scCloneFormBox).chosen();
        $('a.ui-btn, button', scCloneFormBox).button();
        clearFormFields(scCloneFormBox);
        //scCloneForm.validationEngine({prettySelect: true,useSuffix: "_chzn",validationEventTrigger:"change"});
        
        // Dialog
        $('#scCloneFormPopup').dialog({
            autoOpen: false,
            width: 622,
            resizable: false,
            modal: true,
            open: function(){
                $('#scCloneFormPopup').find('#error-text').hide();
                $('form').validationEngine({prettySelect: true,useSuffix: "_chzn",validationEventTrigger:"change"});
            },
            beforeClose: function(){
                scCloneForm.find('select#organization option[value!=""]').remove();
                scCloneForm.find('select#organization').trigger("liszt:updated");
                clearFormFields(scCloneForm);
                $('form').validationEngine('hideAll');
            },
            close: function(){
                clearFormFields(scCloneForm);
            },
            buttons: {
                "Save": function() {
                    if(!$('form:first', scCloneFormBox).validationEngine('validate')) {
                        return false;
                    }
                    var reqData = extractFormDataToJson(scCloneForm);
                    //var loading=new ol.loading({id:"scCloneFormPopup", loadingText:"Waiting..."});
                    //loading.show();
                    var dlg = $(this);
                    $.ajax({
                        url:'${contextPath}/surveyconf/survey-config!clone',
                        type: 'POST',
                        dataType: 'json',
                        data: reqData,
                        success: function(data){
                            // loading.hide();
                            if(data.ret != 0) {
                                // $('#scCloneFormPopup').find('#error-text').text((data.desc)).show();
                                ocsError(data.desc);
                            } else {
                               $("#surveyConfig-flexgrid").flexReload({
                                    dataType: 'json'
                                });
                                ocsSuccess($.i18n.message('cp.text.success_clone_survey_config', [scCloneForm.find('#scName').val()]));
                                clearFormFields(scCloneFormBox);
                                dlg.dialog("close");
                            }
                        },
                        error: function(data) {
                            loading.hide();
                            defaultAjaxErrorHanlde(data);
                        }
                    });
                    return false;
                },
                "Cancel": function() {
                    clearFormFields(scCloneFormBox);
                    $(this).dialog("close");
                }
            }
        });
        
    });
</script>
