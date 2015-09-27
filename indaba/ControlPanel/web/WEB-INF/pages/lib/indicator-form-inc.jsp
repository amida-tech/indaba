<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/jquery-ui/css/ui.multiselect.css">
<style type="text/css">
    #answerFs div.field, #answerFs div.field {
        display: inline-block;
        width: 350px;
    }

    fieldset#answerFs {
        border: 1px solid #A4BED4;
        padding: 10px;
        /*width: 499px;*/
    }

    fieldset#answerFs legend{
        margin-left: 20px;
        font-size: 14px;
        font-weight: bold;
    }

    .answer-added table {
        margin: 10px 0px;
        width: 100%;
        border: 1px solid #A4BED4;
    }
    .answer-added thead {
        font-weight: bold;
        background-color:#888;
        color: #fff;
    }

    .answer-added thead tr td{
        padding: 10px 0px;
    }
    .answer-added table tr td{
        padding: 5px;
    }

    thead td.label {
        border-right: 2px solid #EFEFEF;
    }
    .answer-added thead td.label {
        width: 70%;
    }
    .answer-added thead .value {
        width: 30%;
    }
    tr.item td {
        border-bottom: 1px solid #ccc;
    }
    .answer-added table tr td a.del {
        cursor:pointer;
        position: relative;
        top: 3px;
    }
    .answer-added table img {
        margin-right: 3px;
    }
    tr.nodata, tr.dnd-hint {
        color: #aaa;
        text-align: center;
    }
    tfoot tr.dnd-hint{
        background-color: #EFEFEF;
        display: none;
    }
    .drag {
        background-color: #A4BED4;
    }
    tr.odd {
        background-color: #EFEFEF;
    }
    #answerBox, #valueSettingBox {
        width:516px;
    }
    #answer-def-content{
        padding: 0px 5px 5px 5px;
        /*display: none;*/
    }
    div.button-row {
        float: right;
    }
    .multiselect {
        width: 407px;
        height: 120px;
    }
</style>
<div id="indicatorFormPopup" title="Add Indicator" class="hidden">
    <div id="error-text" class="error-box"style="margin: 10px; width:555px"></div>
    <div id="indicatorFormBox" class="popup-container">
        <form id="indicatorForm" action="#" method="POST" >
            <fieldset id="indicatorFs">
                <input type="hidden" name="visibility" id="visibility" value="${visibility}"/>
                <input type="hidden" name="id" id="indicatorId" value="-1" />

                <dl>
                    <dt><label for="language"><indaba:msg key="cp.label.language"/></label></dt>
                    <dd>
                        <select id="language" name="language" data-placeholder="<indaba:msg key='cp.text.choose_language'/>" class="small-input validate[required]">
                            <option value=""></option>
                            <c:forEach var="lang" items="${languages}">
                                <option value="${lang.id}">[${fn:toUpperCase(lang.language)}] ${lang.languageDesc}</option>
                            </c:forEach>
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt><label for="indName"><indaba:msg key="cp.label.name"/><span class="tiptxt" title="<indaba:msg key='cp.tip.indicator_name' />">&nbsp;</span></label></dt>
                    <dd>
                        <input id="indName" type="text" name="name" maxlength="255" class="long-input validate[required]"/>
                    </dd>
                </dl>
                <dl id="orgname-item">
                    <dt><label for="orgName"><indaba:msg key="cp.label.organization"/></label></dt>
                    <dd>
                        <input id="orgName" type="text" readonly="readonly" name="orgname" />
                    </dd>
                </dl>
                <dl id="org-item">
                    <dt><label for="organization"><indaba:msg key="cp.label.organization"/></label></dt>
                    <dd>
                        <select id="organization" name="organization" data-placeholder="<indaba:msg key='cp.text.choose_organization'/>" class="small-input validate[required]">
                            <option value=""></option>
                            <c:forEach var="org" items="${ownOrgs}">
                                <option value="${org.id}">${org.name}</option>
                            </c:forEach>
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt><label for="question"><indaba:msg key="cp.label.question"/><span class="tiptxt" title="<indaba:msg key='cp.tip.indicator_question' />">&nbsp;</span></label></dt>
                    <dd>
                        <textarea id="question" name="question" class="long-input validate[required]"></textarea>
                    </dd>
                </dl>
                <dl>
                    <dt><label for="tip"><indaba:msg key="cp.label.tip"/><span class="tiptxt" title="<indaba:msg key='cp.tip.indicator_tip' />">&nbsp;</span></label></dt>
                    <dd>
                        <textarea id="tip" name="tip" class="long-input"></textarea>
                    </dd>
                </dl>
                <dl>
                    <dt><label for="reference"><indaba:msg key="cp.label.reference"/></label></dt>
                    <dd>
                        <select id="reference" name="reference" data-placeholder="<indaba:msg key='cp.text.choose_reference'/>" class="small-input validate[required]">
                            <option value=""></option>
                            <c:forEach var="ref" items="${references}">
                                <option value="${ref.id}">${ref.name}</option>
                            </c:forEach>
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt><label for="itags"><indaba:msg key="cp.label.indicator_tags"/></label></dt>
                    <dd>
                        <select id="itags" name="itags" multiple data-placeholder="<indaba:msg key='cp.text.choose_indicator_tags'/>" class="long-input">
                            <option value=""></option>
                            <c:forEach var="tag" items="${itags}">
                                <option value="${tag.id}">${tag.term}</option>
                            </c:forEach>
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt><label for="anserType"><indaba:msg key="cp.label.answer_type"/></label></dt>
                    <dd class="radio">
                        <input type="radio" name="answerType" value="1" class="validate[required]" /><span class="choice-txt"><indaba:msg key="cp.label.single_choice"/></span>
                        <input type="radio" name="answerType" value="2" class="validate[required]" /><span class="choice-txt"><indaba:msg key="cp.label.multi_choice"/></span>
                        <input type="radio" name="answerType" value="3" class="validate[required]" /><span class="choice-txt"><indaba:msg key="cp.label.integer"/></span>
                        <input type="radio" name="answerType" value="4" class="validate[required]" /><span class="choice-txt"><indaba:msg key="cp.label.float"/></span>
                        <input type="radio" name="answerType" value="5" class="validate[required]" /><span class="choice-txt"><indaba:msg key="cp.label.text"/></span>
                        <input type="radio" name="answerType" value="6" class="validate[required]" /><span class="choice-txt"><indaba:msg key="cp.label.table"/></span>
                    </dd>
                </dl>
            </fieldset>
        </form>
        <br>
        <div id="valueSettingBox" class="hidden ui-accordion ui-widget ui-helper-reset ui-accordion-icons">
            <h3 class="ui-accordion-header ui-helper-reset ui-state-active ui-corner-top"><span class="ui-icon ui-icon-triangle-1-s "></span><a href="javascript:void(0)"><indaba:msg key="cp.label.numberic_choice_setting"/></a></h3>
            <div class='ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active'>
                <form id="numberSettingForm" action="#" method="POST">
                    <fieldset id="numberSettingFs">
                        <dl>
                            <dt><label for="minVal"><indaba:msg key="cp.label.min_value"/></label></dt>
                            <dd>
                                <input id="valueSettingId" type="hidden" name="id" value="-1"/>
                                <input id="minVal" type="text" name="minVal" class="mini-input validate[required,funcCall[checkNumbericMaxMin]]"/>
                            </dd>
                        </dl>
                        <dl>
                            <dt><label for="maxVal"><indaba:msg key="cp.label.max_value"/></label></dt>
                            <dd>
                                <input id="maxVal" type="text" name="maxVal" class="mini-input validate[required,funcCall[checkNumbericMaxMin]]"/>
                            </dd>
                        </dl>
                        <dl>
                            <dt><label for="defaultVal"><indaba:msg key="cp.label.default_value"/></label></dt>
                            <dd>
                                <input id="defaultVal" type="text" name="defaultVal" class="mini-input validate[required,funcCall[checkNumbericMaxMin]]"/>
                            </dd>
                        </dl>
                        <dl>
                            <dt><label for="criteria"><indaba:msg key="cp.label.criteria"/></label></dt>
                            <dd>
                                <textarea id="criteria" name="criteria" class="middle-input"></textarea>
                            </dd>
                        </dl>
                    </fieldset>
                </form>
            </div>
        </div>
        <div id="answerBox" class="hidden ui-accordion ui-widget ui-helper-reset ui-accordion-icons">
            <h3 class="ui-accordion-header ui-helper-reset ui-state-active ui-corner-top"><span class="ui-icon ui-icon-triangle-1-s "></span><a href="javascript:void(0)"><indaba:msg key="cp.label.answer_definition"/></a></h3>
            <div id="answer-def-content" class='ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active'>
                <div class="answer-added">
                    <table id="answerTbl" cellspacing="0">
                        <thead>
                            <tr><td class="label"><indaba:msg key="cp.ch.label"/></td><td class="value"><indaba:msg key="cp.ch.value"/></td></tr>
                        </thead>
                        <tbody>
                            <tr class="nodata"><td colspan="2"><indaba:msg key="cp.text.no_answer_defined"/></td></tr>
                        </tbody>
                        <tfoot>
                            <tr class="dnd-hint"><td colspan="2"><img style="position:relative;top:3px;padding-right: 2px;" src="${contextPath}/resources/images/updown.png"><indaba:msg key="cp.text.click_drag_notes"/></td></tr>
                        </tfoot>
                    </table>
                </div>
                <div class="row">
                    <div id="showAnswerFormBtn" style="margin: 0px 40px; float: right;">
                        <a class="ui-btn" href="javascript:void(0)"><indaba:msg key="cp.btn.add_answer"/></a>
                    </div>
                </div>
                <div id="answerFormBox" class="hidden" style="width: 504px;">
                    <form id="answerForm" action="#" method="POST">
                        <fieldset id="answerFs">
                            <legend><indaba:msg key="cp.btn.add_answer"/>r</legend>
                            <input id="answerId" type="hidden" name="id" />
                            <dl>
                                <dt><label for="label"><indaba:msg key="cp.label.label"/><span class="tiptxt" title="<indaba:msg key='cp.tip.answer_label' />">&nbsp;</span></label></dt>
                                <dd>
                                    <%--
                                    <input id="label" type="text" name="Label" class="middle-input validate[required,funcCall[checkAnswerLabel]]"/>
                                    --%>
                                    <input id="label" type="text" name="Label" maxlength="100" class="middle-input validate[required]"/>
                                </dd>
                            </dl>
                            <dl>
                                <dt><label for="label"><indaba:msg key="cp.label.value"/><span class="tiptxt" title="<indaba:msg key='cp.tip.answer_value' />">&nbsp;</span></label></dt>
                                <dd>
                                    <input id="value" type="text" name="value" class="mini-input validate[required, custom[number]]"/>&nbsp;&nbsp;
                                    <div style="display:inline-block;position:relative;top:-2px;"><input id="useScore" type="checkbox" name="useScore"/><indaba:msg key="cp.text.not_available"/></div>
                                </dd>
                            </dl>
                            <dl>
                                <dt><label for="criteria"><indaba:msg key="cp.label.criteria"/><span class="tiptxt" title="<indaba:msg key='cp.tip.answer_criteria' />">&nbsp;</span></label></dt>
                                <dd>
                                    <textarea id="criteria" name="criteria" class="middle-input"></textarea>
                                </dd>
                            </dl>
                            <dl>
                                <dt></dt>
                                <dd>
                                    <div class="button-row">
                                        <span id="save-btns" style="display: none;">
                                            <button id="saveAnswerBtn"><indaba:msg key="cp.btn.save"/></button>
                                        </span>
                                        <span id="add-btns">
                                            <button id="addAnswerBtn"><indaba:msg key="cp.btn.add"/></button>
                                        </span>
                                        &nbsp;&nbsp;<button id="cancelAnswerBtn"><indaba:msg key="cp.btn.cancel"/></button>
                                        &nbsp;&nbsp;
                                    </div>
                                </dd>
                            </dl>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>

        <div id="tableSettingBox" class="hidden ui-accordion ui-widget ui-helper-reset ui-accordion-icons">
            <h3 class="ui-accordion-header ui-helper-reset ui-state-active ui-corner-top"><span class="ui-icon ui-icon-triangle-1-s "></span><a href="javascript:void(0)"><indaba:msg key="cp.label.numberic_choice_setting"/></a></h3>
            <div class='ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content-active'>

                <form id="tableFileSettingForm" action="#" method="POST">
                    <input type="hidden" id="pathName" name="pathName"/>
                    <input id="tableTypeId" type="hidden" name="tableTypeId" value="-1"/>
                    <fieldset id="tableSettingFs">
                        <dl>
                            <dt><label for="minVal"><indaba:msg key="cp.ch.file_name"/></label></dt>
                            <dd>
                                <input type="text" id="tdfFileName" name="tdfFileName" class="short-input validate[required]" disabled="disabled"/>
                            </dd>
                            <dd>
                                <a id="uploadTdf" class="slink" href="javascript:void(0)">upload</a>
                                <a id="downloadTdf" class="slink" href="javascript:void(0)">download</a>
                            </dd>
                        </dl>
                    </fieldset>                    
                </form>
            </div>
        </div>                                    
    </div>
</div>
<script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/jquery.tablednd.js"></script>
<script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/jquery-ui/js/jquery.localisation.js"></script>
<script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/jquery-ui/js/jquery.scrollTo.js"></script>
<script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/jquery-ui/js/ui.multiselect.js"></script>
<script type="text/javascript" charset="utf-8">
    var answerId = 0;
    var indicatorFormBox = $('#indicatorFormBox');
    var indicatorForm = $('#indicatorForm', indicatorFormBox);
    var answerFormBox = $('#answerFormBox');
    $(function(){
        $('input:text, input:radio, input:checkbox, textarea', indicatorFormBox).uniform();
        $("select",indicatorFormBox).chosen();
        $('a.ui-btn, button', indicatorFormBox).button();
        clearFormFields(indicatorFormBox);
        //indicatorForm.validationEngine({prettySelect: true,useSuffix: "_chzn",validationEventTrigger:"change"});
        $('.tiptxt').tooltip({
            // place tooltip on the right edge
            position: "center right",
            // a little tweaking of the position
            offset: [-2, 10],
            // use the built-in fadeIn/fadeOut effect
            effect: "fade",
            // custom opacity setting
            opacity: 0.7
        });
        $('h3.ui-accordion-header').click(function(){
            if($(this).hasClass('ui-state-active')) {
                $(this).attr('class', 'ui-accordion-header ui-helper-reset ui-state-default ui-corner-all');
                $('span', $(this)).attr('class','ui-icon ui-icon-triangle-1-e');
                $(this).siblings('div').hide();
            } else {
                $(this).attr('class', 'ui-accordion-header ui-helper-reset ui-state-active ui-corner-top');
                $('span', $(this)).attr('class','ui-icon ui-icon-triangle-1-s');
                $(this).siblings('div').show();
            }
            return false;
        });
        // Dialog
        $('#indicatorFormPopup').dialog({
            autoOpen: false,
            width: 622,
            resizable: false,
            //maxHeight: $(window).height(),
            modal: true,
            open: function(){
                $('#answerFormBox').hide();
                $('#tdfFileName').attr("disabled", true);
                $('form').validationEngine({prettySelect: true,useSuffix: "_chzn",validationEventTrigger:"change"});
            },
            beforeClose: function(){
                clearFormFields(indicatorFormBox);
                $('form').validationEngine('hideAll');
            },
            close: function(){
                resetIndicatorForm($('#indicatorFs'));
            },
            buttons: {
                "Save": function() {
                    if(!$('form:first', indicatorFormBox).validationEngine('validate')) {
                        return false;
                    }
                    var numberSettingForm = $('#valueSettingBox form#numberSettingForm', indicatorFormBox);
                    var choiceType = parseInt($('input:radio[name=answerType][checked]', indicatorFormBox).val());
                    if(choiceType < 3) {
                        if($('#answerTbl tbody tr.item').length <= 0) {
                            $('#answerBox h3 a').validationEngine('showPrompt', $.i18n.message('cp.error.must_define_answer'), 'error', 'topLeft', true);
                            return false;
                        }
                    } 
                    else if (choiceType < 6 ) {
                        if(!numberSettingForm.validationEngine('validate')) {
                            return false;
                        }
                    }
                    else if ( choiceType == 6 ) {
                        if ($('#pathName').val() == "" ) {
                            $('#tableSettingBox h3 a').validationEngine('showPrompt', $.i18n.message('cp.error.must_define_tablefile'), 'error', 'topLeft', true);
                            return false;
                        }
                    }
                    var reqData = generateIndicatorJsonData();
                    //var loading=new ol.loading({id:"indicatorFormPopup", loadingText:"Waiting..."});
                    //loading.show();
                    var dlg = $(this);
                    $.ajax({
                        url:'${contextPath}/lib/indicator-lib!create',
                        type: 'POST',
                        dataType: 'json',
                        data: reqData,
                        success: function(data){
                            //loading.hide();
                            if(data.ret != 0) {
                                ocsError(data.desc);
                            } else {
                                $("#indicator-flexgrid").flexReload({
                                    dataType: 'json'
                                });
                                clearFormFields(indicatorFormBox);
                                dlg.dialog("close");
                                ocsSuccess(data.desc);
                            }
                        },
                        error: function(data) {
                            //loading.hide();
                            defaultAjaxErrorHanlde(data);
                        }
                    });
                    return false;
                },
                "Cancel": function() {
                    clearFormFields(indicatorFormBox);
                    $(this).dialog("close");
                }
            }
        });
        $('input:radio[name=answerType]', indicatorFormBox).click(function(){
            showChoiceSettingBox(parseInt($(this).val()));
        });
        
        function showChoiceSettingBox(choiceType) {
            $('div.ui-accordion', indicatorFormBox).hide();
            switch(choiceType) {
                case 1:
                case 2:
                    {
                        $('#answerBox', indicatorFormBox).show();
                        break;
                    }
                case 3:
                    {
                        initValueSettingBox(choiceType);
                        $('#valueSettingBox', indicatorFormBox).show();
                        break;
                    }
                case 4:
                    {
                        initValueSettingBox(choiceType);
                        $('#valueSettingBox', indicatorFormBox).show();
                        break;
                    }
                case 5:
                    {
                        initValueSettingBox(choiceType);
                        $('#valueSettingBox', indicatorFormBox).show();
                        break;
                    }
                case 6:
                    {
                        $('#tableSettingBox', indicatorFormBox).show();
                    }
            }
        }
        $('#showAnswerFormBtn a').click(function(){
            switchAnswerFormStatus('add');
            return false;
        });
        // Add answer item
        $('#addAnswerBtn').click(function(){
            if(!$('form#answerForm', answerFormBox).validationEngine('validate')) {
                return false;
            }
            var data = generateAnswerFormData();
            addAnswerTableRow(data, false);
            $('#showAnswerFormBtn').parent('div.row').show();
            $('#answerFormBox').hide();
            clearUniformFields(answerFormBox);
            return false;
        });
        
        // Save answer item
        $('#saveAnswerBtn').click(function(){
            if(!$('form', answerFormBox).validationEngine('validate')) {
                return false;
            }
            var data = generateAnswerFormData();
            saveAnswerTableRow(data);
            //clearUniformFields();
            $('#showAnswerFormBtn').parent('div.row').show();
            $('#answerFormBox').hide();
            return false;
        });
        // Cancel answer
        $('#cancelAnswerBtn').click(function(){
            $('#showAnswerFormBtn').parent('div.row').show();
            $('#answerFormBox').hide();
            return false;
        });

        $('#uploadTdf').click(function(){
            doImportTableIndicator('${contextPath}');
            return false;
        });
        $('#downloadTdf').click(function(){
            doDownloadTableIndicator('${contextPath}');
            return false;
        });

    });

    function generateIndicatorJsonData() {
        var p = $('#indicatorFs');
        var data = extractFormDataToJson(p);
        if(data['answerType'] == 1 || data['answerType'] == 2) {
            data['answerChoices']= JSON.stringify(generateAnswerListJsonData());
        } 
        else if (data['answerType'] == 6 ){
            data['answerTableSetting'] = JSON.stringify(extractFormDataToJson($('#tableFileSettingForm')));
        } else {
            data['answerValueSetting'] = JSON.stringify(extractFormDataToJson($('#numberSettingForm')));
        }
        
        return data;
    }
        
    function generateAnswerListJsonData() {
        var answerTbl = $('#answerTbl');
        var answerList = [];
        $('tbody tr.item', answerTbl).each(function(){
            answerList[answerList.length] = generateDataFromTableRow($(this));
        });
        return answerList;
    }
        
    function generateAnswerFormData() {
        var fs = $('#answerFs');
        return {
            id: $('#answerId', fs).val(),
            label: $('#label', fs).val(),
            value: $('#value', fs).val(),
            useScore: !($('#useScore', fs).is(':checked')),
            criteria: $('#criteria', fs).val()
        };
    }
    function generateDataFromTableRow(tr) {
        return {
            id: tr.attr('id'),
            label: $('td.label span.lbl', tr).text(),
            value: $('td.value', tr).text(),
            useScore: $('td.useScore', tr).text(),
            criteria: $('td.criteria', tr).text()
        };
    }
    function initAnswerForm(data) {
        var fs = $('#answerFs');
        $('#answerId', fs).val(data.id);
        $('#label', fs).val(data.label);
        $('#value', fs).val(data.value);
        $('#useScore', fs).val(data.useScore);
        $('#criteria', fs).val(data.criteria);
        var uf = $('#uniform-useScore>span', fs);
        if(data.useScore == 'true') {
            uf.removeClass('checked');
        }else{
            uf.addClass('checked');
        }
        return true;
    }
    function setViewMode(viewonly, notEditableReason) {
        var p = $('#indicatorFs');

        if(viewonly) {
            if(notEditableReason) {
                $('#indicatorFormPopup div#error-text').text(notEditableReason);
                $('#indicatorFormPopup div#error-text').show();
            } else {
                $('#indicatorFormPopup div#error-text').hide();
            }

            $('input, select, button, textarea', p).attr("disabled","disabled");
            $('input, select, button, textarea', p).css("background-color","#fafafa");
            $('#answerBox #showAnswerFormBtn').hide();
            $('div.ui-dialog-buttonset button span', p.parents('div.ui-dialog')).each(function(){
                if($(this).text() == 'Save') {
                    $(this).parent().hide();
                    return false;
                }
            });
            $('dl#orgname-item', p).show();
            $('dl#org-item', p).hide();
            $('#answer-def-content div.row').hide();
            $('#answerTbl tfoot').hide();
        } else {
            $('#indicatorFormPopup div#error-text').text('');
            $('#indicatorFormPopup div#error-text').hide();
            $('input, select, button, textarea', p).removeAttr("disabled");
            $('input, select, button, textarea', p).css("background-color","#fff");
            $('#answerBox #showAnswerFormBtn').show();
            $('div.ui-dialog-buttonset button', p.parents('div.ui-dialog')).show();
            $('#showAnswerFormBtn').parent('div.row').show();
            $('dl#orgname-item', p).hide();
            $('dl#org-item', p).show();
            $('#answer-def-content div.row').show();
            $('#answerTbl tfoot').show();
        }
    }
    
    function resetIndicatorForm(elem) {
        var f = $('#indicatorFs');
        $('option', f).prop('selected', false);
        $('select', f).trigger('liszt:updated');

        $('#valueSettingBox').hide();
        $('#tableSettingBox').hide();
        var p = $('#answerBox');
        p.hide();
        $('#answerTbl tr.item', p).remove();
        $('#answerTbl tr.nodata', p).show();
        $('#answerTbl tr.dnd-hint', p).hide();
        //$('select#itags>option').removeAttr('selected');
        clearFormFields(elem);
        
        // Make sure the indicator ID is set to -1!
        var p = $('#indicatorFs');
        $('input[name=id]', p).val(-1);
    }

    function initIndicatorForm(data, notEditableReason, viewonly) {
        var p = $('#indicatorFs');
        setViewMode(viewonly, notEditableReason);
        $('input[name=id]', p).val(data.id);
        $('input:text[name=name]', p).val(data.name);
        $('input:text[name=orgname]', p).val(data.orgname);
        $('textarea[name=question]', p).val(data.question);
        $('textarea[name=tip]', p).val(data.tip);
        checkUniformChoice($('input:radio[name=answerType][value='+data.answerType+']', p));
        selectChosenOption($('select[name=language]', p), data.language);

        if (viewonly) {
            // Done by Jeff: hide the organization select field, and show the orgname text field
            // NO NEED TO HANDLE HERE. IT'S ALREADY DONE IN setViewMode(...)
        } else {
            selectChosenOption($('select[name=organization]', p), data.organization);
            // Done by Jeff: hide the orgname text field
            // NO NEED TO HANDLE HERE. IT'S ALREADY DONE IN setViewMode(...)
        }

        selectChosenOption($('select[name=reference]', p), data.reference);
        if(data.itags) {
            for(var i = 0; i < data.itags.length; ++i) {
                selectChosenOption($('select[name=itags]', p), data.itags[i]);
            }
        }
        switch(data.answerType) {
            case 1:
            case 2:
                {
                    $('#answerTbl tr.item').remove();
                    if(data.answerChoices) {
                        $.each(data.answerChoices, function(index, value){
                            addAnswerTableRow(value, viewonly);
                        });
                    }
                    $('#answerBox').show();
                }
                break;
            case 3:
            case 4:
            case 5:
                {
                    initValueSettingBox(data.answerType, data.answerValueSetting);
                    $('#valueSettingBox').show();
                }
                break;
            case 6: 
                initTableSettingBox(data.answerTableSetting);
                $('#tableSettingBox').show();
                break;
            default:
                break;
        }
    }
    function initValueSettingBox(choiceType, valueSettingVo) {
        var numberSettingForm = $('#valueSettingBox form#numberSettingForm', indicatorFormBox);
        numberSettingForm.validationEngine('hideAll');

        switch(choiceType) {
            case 3:
                $('dt label', $('#minVal', numberSettingForm).parent().parent()).text('Min Value');
                $('dt label', $('#maxVal', numberSettingForm).parent().parent()).text('Max Value');
                $('#minVal, #maxVal', numberSettingForm).attr('class', 'mini-input validate[required,funcCall[checkNumbericMaxMin]] text');
                $('#defaultVal', numberSettingForm).parent().parent().show();
                break;
            case 4:
                $('dt label', $('#minVal', numberSettingForm).parent().parent()).text('Min Value');
                $('dt label', $('#maxVal', numberSettingForm).parent().parent()).text('Max Value');
                $('#minVal, #maxVal', numberSettingForm).attr('class', 'mini-input validate[required,funcCall[checkNumbericMaxMin]] text');
                $('#defaultVal', numberSettingForm).parent().parent().show();
                break;
            case 5:
                $('dt label', $('#minVal', numberSettingForm).parent().parent()).text('Min Chars');
                $('dt label', $('#maxVal', numberSettingForm).parent().parent()).text('Max Chars');
                $('#minVal, #maxVal', numberSettingForm).attr('class', 'mini-input validate[required,funcCall[checkNumbericMaxMin]] text');
                $('#defaultVal', numberSettingForm).parent().parent().hide();
                break;
        }
        clearUniformFields(numberSettingForm);
        if(valueSettingVo) {
            $('#valueSettingId', numberSettingForm).val(valueSettingVo.id);
            $('#minVal', numberSettingForm).val(valueSettingVo.minVal);
            $('#maxVal', numberSettingForm).val(valueSettingVo.maxVal);
            $('#defaultVal', numberSettingForm).val(valueSettingVo.defaultVal);
            $('#criteria', numberSettingForm).val(valueSettingVo.criteria);
        }
        numberSettingForm.validationEngine({prettySelect: true,useSuffix: "_chzn",validationEventTrigger:"change"});
    }
    function initTableSettingBox(tableSettingVo) {
        var settingForm = $('#tableSettingBox form#tableFileSettingForm', indicatorFormBox);
        settingForm.validationEngine('hideAll');
        clearUniformFields(settingForm);
        if(tableSettingVo) {
            $('#tableTypeId', settingForm).val(tableSettingVo.id);
            $('#pathName', settingForm).val(tableSettingVo.pathName);
            $('#tdfFileName', settingForm).val(tableSettingVo.tdfFileName);
            $('#uploadTdf', settingForm).html('&nbsp;&nbsp;change');
            $('#downloadTdf', settingForm).show();
        }
        $('#tdfFileName', settingForm).attr("disabled", true);
        settingForm.validationEngine({prettySelect: true,useSuffix: "_chzn",validationEventTrigger:"change"});
    }
    function switchAnswerFormStatus(addOrEdit) {
        clearUniformFields($('#answerFs'));
        $('#answerFormBox').show();
        $('#label', $('#answerFormBox')).focus();
        if(addOrEdit == 'add') {
            $('legend', $('#answerFs')).text('Add Answer');
            $('#save-btns').hide();
            $('#add-btns').show();
            $('#showAnswerFormBtn').parent('div.row').hide();
        } else if (addOrEdit == 'edit'){
            $('legend', $('#answerFs')).text('Edit Answer');
            $('#save-btns').show();
            $('#add-btns').hide();
        } else if (addOrEdit == 'view'){
            $('legend', $('#answerFs')).text('View Answer');
            $('#save-btns').hide();
            $('#add-btns').hide();
            $('input, textarea',$('#answerFs')).attr('disabled', 'disabled');
        }
    }
    function addAnswerTableRow(data, viewonly) {
        var answerTbl = $('#answerTbl');
        var newtr = $('<tr class="item" id="'+((data.id>0)?data.id:(--answerId))+'">');
        var lblTd = $('<td class="label"></td>');
        if(viewonly) {
            lblTd.append('<a class="view" href="javascript:void(0)"><img title="view" style="position: relative; top:0px;width: 16px;" src="${contextPath}/resources/images/view.png" /></a>');
        } else {
            lblTd.append('<a class="del" href="javascript:void(0)"><img title="delete" style="position: relative; top:-4px; width: 10px;" src="${contextPath}/resources/images/delete.png" /></a>');
            lblTd.append('<a class="edit" href="javascript:void(0)"><img title="edit" style="position: relative; top:0px;width: 12px;" src="${contextPath}/resources/images/edit.png" /></a>');
        }
        lblTd.append('<span class="lbl">'+data.label+'</span>');
        newtr.append(lblTd);
        newtr.append($('<td class="value">'+data.value+'</td>'));
        newtr.append($('<td class="useScore hidden">'+data.useScore+'</td>'));
        newtr.append($('<td class="criteria hidden">'+data.criteria+'</td>'));
        // Add a new row
        newtr.appendTo($('tbody', answerTbl));
        if(viewonly) {
            // view answer
            $('a.view',newtr).click(function(){ // Bind delete click event
                switchAnswerFormStatus('view');
                var tr = $(this).parent().parent();
                initAnswerForm(generateDataFromTableRow(tr));
                return false;
            });
        } else {
            $('a.del',newtr).click(function(){ // Bind delete click event
                var trElm = $(this).parent().parent();
                ocsConfirm($.i18n.message('cp.text.confirm_delete_answer'), $.i18n.message('cp.title.confirm'), function(choice){
                    if(choice){
                        trElm.remove();
                        var count = $('tbody tr.item', answerTbl).length;
                        if(count == 0) {
                            $('tbody tr.nodata', answerTbl).show();
                            $('#answerBox h3 a').validationEngine('showPrompt', $.i18n.message('cp.error.must_define_answer'), 'error', 'topLeft', true);
                        }
                        if(count <= 1) {
                            $('tfoot tr.dnd-hint', answerTbl).hide();
                        }
                    }
                });
                return false;
            });
            // edit answer
            $('a.edit',newtr).click(function(){ // Bind delete click event
                switchAnswerFormStatus('edit');
                var tr = $(this).parent().parent();
                initAnswerForm(generateDataFromTableRow(tr));
                return false;
            });
        }
        var itemCount = $('tbody tr.item', answerTbl).length;
        if(itemCount > 0) { // Remove nodata row if any
            $('tbody tr.nodata', answerTbl).hide();
        }
        if(itemCount > 1) {
            $('tfoot tr.dnd-hint', answerTbl).css('display', 'table-row');
        }
        if(viewonly) {
            $('#answerTbl tfoot').hide();
        } else {
            answerTbl.tableDnD({onDragClass:'drag',
                onDrop:function(){
                    var i = 0;
                    $('tbody tr', answerTbl).each(function(){
                        //if(i++ % 2 == 1){$(this).toggleClass('odd')}
                    })
                }});
            $('#answerTbl tfoot').show();
        }
        $('div.undefinedformError', $('#answerBox h3 a').parents('h3')).remove();
    }
    function saveAnswerTableRow(data) {
        var tr = $('tr[id='+data.id+']', $('#answerTbl'));
        $('td.label span.lbl', tr).text(data.label);
        $('td.value', tr).text(data.value);
        $('td.useScore', tr).text(data.useScore);
        $('td.criteria', tr).text(data.criteria);
            
    }
    function checkAnswerLabel(elem, rules, i, options){
        var existed = false;
        $('#answerTbl tbody td.label span.lbl').each(function(){
            if($(this).text() == elem.val()) {
                existed = true;
                return false;
            } else {
                return true;
            }
        });
        if(existed) {
            return $.i18n.message('cp.error.anwer_label_exists');
        }
    }
    function checkNumbericMaxMin(elem, rules, i, options){
        var choiceType = parseInt($('input:radio[name=answerType][checked]', indicatorFormBox).val());
        if(choiceType == 3) {// INTEGER
            var re = /^[\-]?[0-9]+$/;
            if (!re.test(elem.val())) {
                return '* Not a valid integer';
            }
        } else if(choiceType == 4) { // FLOAT
            if(isNaN(elem.val())) {
                return '* Not a valid float';
            }
        } else if(choiceType == 5) {// TEXT
            var re = /^[0-9]+$/;
            if (!re.test(elem.val())) {
                return '* Not a valid positive integer';
            }
        }
        var min = $('#numberSettingForm input#minVal').val();
        var max = $('#numberSettingForm input#maxVal').val();
        if(isNaN(min) || isNaN(max)) {
            return;
        }
        if(choiceType == 3) {// INTEGER
            if(parseInt(min) > parseInt(max)) {
                return (elem.attr('id') == 'minVal')?$.i18n.message('cp.error.must_less_than_maxval'):$.i18n.message('cp.error.must_greater_than_minval');
            }
        } else if(choiceType == 4) { // FLOAT
            if(parseFloat(min) > parseFloat(max)) {
                return (elem.attr('id') == 'minVal')?$.i18n.message('cp.error.must_less_than_maxval'):$.i18n.message('cp.error.must_greater_than_minval');
            }
        } else if(choiceType == 5) {// Text
            if(parseInt(min) > parseInt(max)) {
                return (elem.attr('id') == 'minVal')?$.i18n.message('cp.error.must_less_than_maxchars'):$.i18n.message('cp.error.must_greater_than_minchars');
            }
        }
    }
</script>