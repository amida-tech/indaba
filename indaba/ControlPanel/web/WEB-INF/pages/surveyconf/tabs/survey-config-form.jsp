<%--
    Document   : SurveyConfig - 'General Information'
    Created on : May 20, 2012, 11:20:21 AM
    Author     : Jeff Jiang
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<div id="surveyConfigFormTab">
    <form id="scForm" action="#" method="POST">
        <div class="form-buttons" style="margin-bottom: 10px;">
            <a id="save" <c:if test="${sconfId>0}">style="display: none;"</c:if> href="#" onclick="return doSaveSurveyConfig();" class="btn save"><img src="${contextPath}/resources/images/save.png" /><indaba:msg key="cp.btn.save" /></a>
            <a id="cancel" <c:if test="${sconfId>0}">style="display: none;"</c:if> href="#" onclick="return doCancelForm();" class="btn cancel"><img src="${contextPath}/resources/images/undo.png" /><indaba:msg key="cp.btn.cancel" /></a>
                <c:if test="${sconfId>0 and owned}">
                <a id="edit" href="#" onclick="return setFormFieldEditable(true);" class="btn edit"><img src="${contextPath}/resources/images/btn_edit.png" /><indaba:msg key="cp.btn.edit" /></a>
                </c:if>
        </div>
        <fieldset class="block">
            <input type="hidden" name="sconfId" id="sconfId" value="${sconfId}"/>
            <dl>
                <dt><label for="scName"><indaba:msg key="cp.label.name"/></label></dt>
                <dd><input type="text" maxlength="100" name="scName" id="scName" class="long-input validate[required,ajax[ajaxValidateSurveyConfigExists]]"/></dd>
            </dl>
            <dl>
                <dt><label for="description"><indaba:msg key="cp.label.description"  /></label></dt>
                <dd><textarea name="description" maxlength="255" id="description" class="long-input"></textarea></dd>
            </dl>
            <dl>
                <dt><label for="instructions"><indaba:msg key="cp.label.instructions"  /></label></dt>
                <dd><textarea name="instructions" id="instructions" class="long-input"></textarea></dd>
            </dl>
            <dl>
                <dt><label for="visibility"><indaba:msg key="cp.label.visibility"  /></label></dt>
                <dd class="radio">
                    <input type="radio" name="visibility" value="1" class="validate[required] notEditable" <c:if test="${visibility == 1}"> checked="checked"</c:if> disabled="true" /><span style="color: #666"><indaba:msg key="cp.label.public"/></span>
                    <input type="radio" name="visibility" value="2" class="validate[required] notEditable" <c:if test="${visibility != 1}"> checked="checked"</c:if> disabled="true" /><span style="color: #666"><indaba:msg key="cp.label.private"/></span>
                </dd>
            </dl>
            <dl>
                <dt><label for="primaryOrg"><indaba:msg key="cp.label.primary_owner"  /></label></dt>
                <dd>
                    <select name="primaryOrg" id="primaryOrg" data-placeholder="<indaba:msg key='cp.text.choose_primary_owner' />" class="short-input validate[required] noChange">
                        <option value=""></option>
                        <c:forEach items="${primaryorgs}" var="org">
                            <option value="${org.id}">${org.name}</option>
                        </c:forEach>
                    </select>
                </dd>
            </dl>
            <dl>
                <dt><label for="secondaryOrgs"><indaba:msg key="cp.label.secondary_owner"  /></label></dt>
                <dd>
                    <select name="secondaryOrgs" id="secondaryOrgs" data-placeholder="<indaba:msg key='cp.text.choose_secondary_admin' />" class="long-input noChange" multiple>
                        <option value=""></option>
                        <c:forEach items="${secondaryorgs}" var="org">
                            <option value="${org.id}">${org.name}</option>
                        </c:forEach>
                    </select>
                </dd>
            </dl>
            <dl>
                <dt><label for="tipDisplayMethod">Tip Display Method</label></dt>
                <dd class="radio">
                    <input type="radio" name="tipDisplayMethod" value="1" class="validate[required] editable" <c:if test="${tipDisplayMethod == 1}"> checked="checked"</c:if> disabled="true" /><span style="color: #666">Tip/Guidance</span>
                    <input type="radio" name="tipDisplayMethod" value="2" class="validate[required] editable" <c:if test="${tipDisplayMethod != 1}"> checked="checked"</c:if> disabled="true" /><span style="color: #666">Plain Text</span>
                </dd>
            </dl>
        </fieldset>
        <div class="form-buttons" style="margin-bottom: 10px;">
            <a id="save" <c:if test="${sconfId>0}">style="display: none;"</c:if> href="#" class="btn save"><img src="${contextPath}/resources/images/save.png" /><indaba:msg key="cp.btn.save" /></a>
            <a id="cancel" <c:if test="${sconfId>0}">style="display: none;"</c:if> href="#" class="btn cancel"><img src="${contextPath}/resources/images/undo.png" /><indaba:msg key="cp.btn.cancel" /></a>
                <c:if test="${sconfId>0 and owned}">
                <a id="edit" href="#" class="btn edit"><img src="${contextPath}/resources/images/btn_edit.png" /><indaba:msg key="cp.btn.edit" /></a>
                </c:if>
        </div>
    </form>
</div>
<script type="text/javascript" charset="utf-8">
    var scFormInitialized = false;
    $(function(){
        if(!scFormInitialized) {
            scFormInitialized = true;
            resetForm('form#scForm');

            checkUniformChoiceV2($('#scForm'), 'visibility', '${visibility}');
            checkUniformChoiceV2($('#scForm'), 'tipDisplayMethod', '${tipDisplayMethod}');

            /*
            if(${sconfId>0 && used}) {
                $('div.form-buttons').hide();
            }
            */
            
            initSurveyConfig(${sconfId}, ${used});
            $('#scForm').validationEngine({prettySelect : true,useSuffix: "_chzn",validationEventTrigger:"change"});

            if(${sconfId} > 0) {
                setFormFieldEditable(false);
            }
            $('div.form-buttons #edit', $('#scForm')).click(function(){
                setFormFieldEditable(true);
                return false;
            });

            $('div.form-buttons #save', $('#scForm')).click(function(){
                doSaveSurveyConfig();
                return false;
            });
            $('div.form-buttons #cancel', $('#scForm')).click(function(){
                ocsConfirm($.i18n.message('cp.text.confirm_cancel_survey_config'), $.i18n.message('cp.title.confirm'),
                function(choice){
                    if(choice){
                        if(${sconfId > 0}) {
                            initSurveyConfig(${sconfId});
                            setFormFieldEditable(false);
                        } else {
                            window.location.href="${contextPath}/surveyconf/survey-config";
                        }
                    }
                });
                return false;
            });
        }
    });

    function initSurveyConfig(sconfId, inUse){
        if(sconfId <= 0) {
            return;
        }
        $.ajax({
            url:'${contextPath}/surveyconf/survey-config!get',
            type: 'POST',dataType: 'json',data: {sconfId: sconfId},
            success: function(resp){
                if(resp.errCode != 0) {
                    ocsError(resp.errMsg);
                } else {
                    initSurveyConfigForm(resp, inUse);
                }
                $('#scForm').validationEngine('hideAll');
            }
        });
    }
    function initSurveyConfigForm(scData, inUse) {
        if(!scData) {
            return;
        }
        if(scData.sc) {
            $('#nameLabel').text(scData.sc.name);
            $('#scName').val(scData.sc.name);
            $('#description', $('#scForm')).val(scData.sc.description);
            $('#instructions', $('#scForm')).val(scData.sc.instructions);
            checkUniformChoiceV2($('#scForm'), 'visibility', scData.sc.visibility);
            checkUniformChoiceV2($('#scForm'), 'tipDisplayMethod', scData.sc.tipDisplayMethod);
            selectChosenOption($('select#primaryOrg', $('#scForm')), scData.sc.ownerOrgId);
        }
        if(scData.orgs){
            selectChosenOptions($('select#secondaryOrgs', $('#scForm')), scData.orgs);
        }
    }

    function doSaveSurveyConfig() {
        if(!$('#scForm').validationEngine('validate')) {
            return false;
        }
        var reqData = extractFormDataToJson($('#scForm'));
        $.ajax({
            url:'${contextPath}/surveyconf/survey-config!save',
            type: 'POST',dataType: 'json',data: reqData,_loading: {show:true,id:"projectTabs"},
            success: function(data){
                if(data.ret != 0) {
                    ocsError(data.desc);
                } else {
                    ocsSuccess($.i18n.message('cp.text.success_save_survey_config'), $.i18n.message('cp.title.success'), function(){
                        if(${sconfId > 0}) {
                            setFormFieldEditable(false);
                        } else {
                            window.location.href="${contextPath}/surveyconf/survey-config";
                        }
                    });
                }
            },
            error: function(data) {
                defaultAjaxErrorHanlde(data);
            }
        });
        return false;
    }
    function setFormFieldEditable(editable) {
        if(editable) {
            $('div.form-buttons', $('#scForm')).find('#save, #cancel').show();
            $('div.form-buttons', $('#scForm')).find('#edit').hide();
            $('.choice-txt,.contype-txt,input[class!=default],textarea', $('#scForm')).css('color','#000');
            $('input, textarea, select', $('#scForm')).removeAttr('disabled');
            $('.notEditable', $('#scForm')).attr('disabled', 'disabled');
            $('.editable', $('#scForm')).removeAttr('disabled');
        } else {
            $('div.form-buttons', $('#scForm')).find('#save, #cancel').hide();
            $('div.form-buttons', $('#scForm')).find('#edit').show();
            $('input, textarea, select', $('#scForm')).attr('disabled', 'disabled');
            $('.choice-txt,.contype-txt,input,textarea', $('#scForm')).css('color','#666');
        }

        if (${used}) {
            $('.noChange', $('#scForm')).attr('disabled', true);
        }
        $('select', $('#scForm')).trigger("liszt:updated");
        return false;
    }
</script>