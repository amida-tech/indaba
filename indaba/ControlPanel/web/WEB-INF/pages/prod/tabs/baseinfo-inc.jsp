<%--
    Document   : Product Tab - 'BaseInfo'
    Created on : May 20, 2012, 11:20:21 AM
    Author     : Jeff Jiang
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<div id="productBaseTab">
    <form id="baseinfoForm" action="#" method="POST">
        <c:if test="${prodId > 0}">
            <div class="form-buttons" style="margin-bottom: 10px;">
                <a id="save" style="display: none;" href="javascript:void(0)" class="btn save" id="save"><img src="${contextPath}/resources/images/save.png" /><indaba:msg key="cp.btn.save" /></a>
                <a id="cancel" style="display: none;" href="javascript:void(0)" class="btn cancel" id="cancel"><img src="${contextPath}/resources/images/undo.png" /><indaba:msg key="cp.btn.cancel" /></a>                    
                <a id="edit" href="javascript:void(0)" class="btn edit" id="edit"><img src="${contextPath}/resources/images/btn_edit.png" /><indaba:msg key="cp.btn.edit" /></a>
            </div>
        </c:if>
        <fieldset class="block">
            <input type="hidden" name="projId" id="projId" value="${projId}"/>
            <input type="hidden" name="prodId" id="prodId" value="${prodId}"/>
            <dl>
                <dt><label for="prodName"><indaba:msg key="cp.label.product_name"/></label></dt>
                <dd><input type="text" name="prodName" maxlength="100" id="prodName" value="0" class="long-input validate[required,ajax[ajaxValidateProductExists]]"/></dd>
            </dl>
            <dl>
                <dt><label for="description"><indaba:msg key="cp.label.description"  /></label></dt>
                <dd><textarea name="description" id="description" maxlength="255" class="long-input"></textarea></dd>
            </dl>
            <dl>
                <dt><label for="contentType"><indaba:msg key="cp.label.content_type"/></label></dt>
                <dd class="radio">
                    <input type="radio" name="contentType" value="0" class="validate[required]" /><span class="contype-txt"><indaba:msg key="cp.label.survey"/></span>
                    <input type="radio" name="contentType" value="1" class="validate[required]" /><span class="contype-txt"><indaba:msg key="cp.label.journal"/></span>
                </dd>
            </dl>
            <dl>
                <dt><label for="contentConfig"><indaba:msg key="cp.label.content_config"/></label></dt>
                <dd>
                    <select name="contentConfig" id="contentConfig" data-placeholder="<indaba:msg key='cp.text.placeholder_choose_config'/>" class="middle-input validate[required]">
                        <option value=""></option>
                        <c:forEach items="${journalConfigs}" var="config">
                            <option value="${config.id}" type="1">${config.name}</option>
                        </c:forEach>
                        <c:forEach items="${surveyConfigs}" var="config">
                            <option value="${config.id}" type="0">${config.name}</option>
                        </c:forEach>
                    </select>
                </dd>
            </dl>
            <dl>
                <dt><label for="workflow"><indaba:msg key="cp.label.workflow"/></label></dt>
                <dd>
                    <select name="workflow" id="workflow" data-placeholder="<indaba:msg key='cp.text.placeholder_choose_workflow'/>" class="middle-input validate[required]">
                        <option value=""></option>
                        <c:forEach items="${workflows}" var="wf">
                            <option value="${wf.id}">${wf.name}</option>
                        </c:forEach>
                    </select>
                </dd>
            </dl>
            <dl>
                <dt><label for="mode"><indaba:msg key="cp.label.mode"/></label></dt>
                <dd class="radio">
                    <input type="radio" name="mode" value="1" class="validate[required]" /><span class="mode-txt" style="color: #aaa"><indaba:msg key="cp.label.mode_config"/></span>
                    <input type="radio" name="mode" value="2" class="validate[required]" /><span class="mode-txt" style="color: #aaa"><indaba:msg key="cp.label.mode_test"/></span>
                    <input type="radio" name="mode" value="3" class="validate[required]" /><span class="mode-txt" style="color: #aaa"><indaba:msg key="cp.label.mode_launched"/></span>
                </dd>
            </dl>
        </fieldset>
        <div class="form-buttons" style="margin-bottom: 10px;">
            <a id="save" <c:if test="${prodId>0}">style="display: none;"</c:if> href="javascript:void(0)" class="btn save" id="save"><img src="${contextPath}/resources/images/save.png" /><indaba:msg key="cp.btn.save" /></a>
            <a id="cancel" <c:if test="${prodId>0}">style="display: none;"</c:if> href="javascript:void(0)" class="btn cancel" id="cancel"><img src="${contextPath}/resources/images/undo.png" /><indaba:msg key="cp.btn.cancel" /></a>
                <c:if test="${prodId>0}">
                <a id="edit" href="javascript:void(0)" class="btn edit" id="edit"><img src="${contextPath}/resources/images/btn_edit.png" /><indaba:msg key="cp.btn.edit" /></a>
                </c:if>
        </div>
    </form>
</div>
<script type="text/javascript" charset="utf-8">
    //var prodBaseInfoForm = $('#baseinfoForm');
    var prodBaseInfoinitialized = false;
    $(function(){
        if(!prodBaseInfoinitialized) {
            prodBaseInfoinitialized = true;
            $('form').each(function(){
                if($(this).attr('id') != 'assignment-filter-form') {
                    resetForm($(this));
                }
            });
            if(${prodId} > 0) {
                setFormFieldEditable(false);
            }
            checkUniformChoice($('input[name=mode][value=1]', $('#baseinfoForm')));
            $('input:radio[name=mode]').attr('disabled', 'disabled');
            
            initProductForm(${projId}, ${prodId}, true);
            $('#baseinfoForm').validationEngine({prettySelect : true,useSuffix: "_chzn",validationEventTrigger:"change"});

            $('.tiptxt', $('#baseinfoForm')).tooltip({
                // place tooltip on the right edge
                position: "center right",
                // a little tweaking of the position
                offset: [-2, 10],
                // use the built-in fadeIn/fadeOut effect
                effect: "fade",
                // custom opacity setting
                opacity: 0.7
            });
            $('select#contentConfig option', $('#baseinfoForm')).each(function(){
                if($(this).attr('type')) {
                    $(this).css('display', 'none');
                }
            });
            $("select#contentConfig", $('#baseinfoForm')).trigger("liszt:updated");
            
            $('input[name=contentType]', $('#baseinfoForm')).change(function(){
                var val = $(this).val();
                $('select#contentConfig option').each(function(){
                    var type = $(this).attr('type');
                    if(!type){
                        $(this).attr('selected', 'selected');
                    } else{
                        $(this).removeAttr('selected');
                        if(type == val) {
                            $(this).css('display', 'block');
                        } else {
                            $(this).css('display', 'none');
                        }
                    }
                });
                $("select#contentConfig", $('#baseinfoForm')).trigger("liszt:updated");
            });

            $('div.form-buttons #edit',$('#baseinfoForm')).click(function(){
                setFormFieldEditable(true);
                return false;
            });
            $('div.form-buttons #save',$('#baseinfoForm')).click(function(){
                doSaveProduct();
                return false;
            });
            $('div.form-buttons #cancel',$('#baseinfoForm')).click(function(){
                ocsConfirm($.i18n.message('cp.text.confirm_cancel_product_base_info'), $.i18n.message('cp.title.confirm'),
                function(choice){
                    if(choice){
                        if(${prodId > 0}) {
                            initProductForm(${projId}, ${prodId});
                            setFormFieldEditable(false);
                        } else {
                            window.location.href="${contextPath}/proj/projects!projectForm?visibility=${visibility}&projId=${projId}&tab=prod";
                        }
                    }
                });
                return false;
            });
        }
    });
    
    function initProductForm(projId, prodId, updateOtherTabs){
        $.ajax({
            url:'${contextPath}/prod/product!getOptions',type: 'POST',dataType: 'json',
            data: {projId: projId, prodId: prodId},
            success: function(resp){
                if(resp.ret != 0) {
                    ocsError(resp.desc);
                } else {
                    initProductFormOptions(resp.data, updateOtherTabs);
                    if(updateOtherTabs) {
                        if(prodId<=0) {
                            // $('#productPopup').dialog('option', 'title', $.i18n.message('cp.title.add_product'));
                        } else {
                            initTaskForm(resp.data);
    <c:if test="${prodMode > 1}">
                                initAssignmentForm(resp.data);
    </c:if>
                            }
                        }
                    }
                }
            });
        }
        function initProductFormOptions(data, updateOtherTabs) {
            var form = $('#productBaseTab #baseinfoForm');
            if(!data) {
                return;
            }
            // init selection list
            $('select option', form).each(function(){
                if(!$(this).attr('value').empty()) {
                    $(this).remove();
                }
            });
            setOptions($('select#contentConfig', form), data.journalConfigs, null, {type:1});
            setOptions($('select#contentConfig', form), data.surveyConfigs, null, {type:0}, true);
            setOptions($('select#workflow', form), data.workflows);
            // init fields
            initProductBaseInfoForm(form, data.product);
            if(updateOtherTabs) {
                $('#projId', form).val(data.projId);
                $('#prodId', form).val(data.prodId);
            }
        }
        function initProductBaseInfoForm(form, prodData) {
            if(!prodData) {
                return;
            }

            var modeText;
            switch(prodData.mode) {
                case 3:
                    modeText = '<indaba:msg key="cp.label.mode_launched"/>';
                    break;
                case 2:
                    modeText = '<indaba:msg key="cp.label.mode_test"/>';
                    break;
                default:
                    modeText = '<indaba:msg key="cp.label.mode_config"/>';
                    break;
            }

            $('#prodNameLabel').text(prodData.name);
            $('#prodModeLabel').text(modeText);

            $('#prodName', form).val(prodData.name);
            $('#description', form).val(prodData.description);
            checkUniformChoiceV2(form, 'contentType', prodData.contentType);
            selectChosenOption($('select#workflow', form), prodData.workflowId);
            checkUniformChoiceV2(form, 'mode', prodData.mode);
        
            $('select#contentConfig option').each(function(){
                var type = $(this).attr('type');
                if(!type){
                    $(this).attr('selected', 'selected');
                } else{
                    $(this).removeAttr('selected');
                    if(type == prodData.contentType) {
                        $(this).css('display', 'block');
                    } else {
                        $(this).css('display', 'none');
                    }
                }
            });
            selectChosenOption($('select#contentConfig', form), prodData.productConfigId, [{k:'type', v:prodData.contentType}]);
            $("select#contentConfig", form).trigger("liszt:updated");
        }

        function doSaveProduct() {
            if(!$('#baseinfoForm').validationEngine('validate')) {
                return false;
            }
            var reqData = extractFormDataToJson($('#baseinfoForm'));
            $.ajax({
                url:'${contextPath}/prod/product!save',type: 'POST',dataType: 'json',data: reqData,_loading: {show:true,id:"projectTabs"},
                success: function(data){
                    if(data.ret != 0) {
                        ocsError(data.desc);
                    } else {
                        ocsSuccess($.i18n.message('cp.text.success_save_product'), $.i18n.message('cp.title.success'), function(){
                            if(${prodId > 0}) {
                                setFormFieldEditable(false);
                                initProductForm(${projId}, ${prodId},true);
                            } else {
                                window.location.href="${contextPath}/proj/projects!projectForm?visibility=${visibility}&projId=${projId}&tab=prod";
                            }
                        });
                    }
                },
                error: function(data) {
                    defaultAjaxErrorHanlde(data);
                }
            });
        }
        function setFormFieldEditable(editable) {
            if(editable) {
                $('div.form-buttons',$('#baseinfoForm')).find('#save, #cancel').show();
                $('div.form-buttons',$('#baseinfoForm')).find('#edit').hide();
                $('.choice-txt,.contype-txt,input[class!=default],textarea', $('#baseinfoForm')).css('color','#000');
                $('#baseinfoForm').find('input, textarea, select').removeAttr('disabled');
                $('input:radio[name=mode]',$('#baseinfoForm')).attr('disabled', 'disabled');

                if($('input:radio[name=mode][checked]',$('#baseinfoForm')).val() != 1) {// if the mode is not CONFIG
                    $('input:radio[name=contentType]',$('#baseinfoForm')).attr('disabled', 'disabled');
                    $('select[name=contentConfig]',$('#baseinfoForm')).attr('disabled', 'disabled');
                    $('select[name=workflow]',$('#baseinfoForm')).attr('disabled', 'disabled');
                    $('.contype-txt', $('#baseinfoForm')).css('color','#aaa');
                }
            } else {
                $('div.form-buttons',$('#baseinfoForm')).find('#save, #cancel').hide();
                $('div.form-buttons',$('#baseinfoForm')).find('#edit').show();
                $('#baseinfoForm').find('input, textarea, select').attr('disabled', 'disabled');
                $('.choice-txt,.contype-txt,input,textarea', $('#baseinfoForm')).css('color','#aaa');
            }
            $('select').trigger("liszt:updated");
        }
</script>