<%-- 
    Document   : libraries
    Created on : May 20, 2012, 11:20:21 AM
    Author     : jiangjeff
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC"-//W3C//DTD XHTML 1.0 Transitional//EN">

<!--
To change this template, choose Tools | Templates
and open the template in the editor.
-->
<!DOCTYPE html>
<html>
    <head>
        <title><indaba:msg key="cp.title.indaba_control_panel" /> - Indicator Library Settings</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link REL="SHORTCUT ICON" href="${contextPath}/resources/images/indaba_icon.ico"/>
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/css/style.css" />
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/uniform/uniform.default.css" />
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/jqtransform/jqtransform.css" />
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/dhtmlx/dhtmlx.css" />
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/chosen/chosen.css"/>
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/dhtmlx/themes/message_skyblue.css" />
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/flexgrid/jquery.flexigrid.css" />
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/jquery-ui/css/jquery-ui-1.8.21.custom.css" />
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/freeow/jquery.freeow.css" />
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/loading/loading.css" />
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/dialog/ocs-dlg.css" />
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/validate/css/validationEngine.jquery.css" type="text/css"/>

        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/ocs-common.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/json2.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/dhtmlx/dhtmlx.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/dhtmlx/message.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/jquery-1.7.2.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/jquery.tools.min.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/jquery.form.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/flexgrid/jquery.flexigrid.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/chosen/jquery.chosen.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/jquery.cookie.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/jquery-ui/js/jquery-ui-1.8.21.custom.min.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/jqtransform/jquery.jqtransform.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/uniform/jquery.uniform.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/freeow/jquery.freeow.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/loading/jquery.bgiframe.min.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/loading/loading.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/dialog/ocs-dlg.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/validate/js/languages/jquery.validationEngine-en.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/validate/js/jquery.validationEngine.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/jquery.i18n.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/jsI18nMessages"></script>
    </head>
    <body onload="doOnLoad();" >
        <div id="indaba">
            <div class="wrapper">
                <jsp:include page="../head.jsp" flush="true">
                    <jsp:param name="active" value="indicatorLib" />
                    <jsp:param name="content" value="indicatorLib" />
                </jsp:include>
            </div>
            <jsp:include page="../footer.jsp" flush="true" />
        </div>
        <div id="indicatorLib" class="hidden">
            <div id="content"> 
                <div id="indicatorLibToolbar"></div>
                <div class="note">
                    <div>
                        <img width="16px" src="${contextPath}/resources/images/warn.png"/>
                        <indaba:msg key="cp.text.change_notes"/>
                    </div>
                </div>
                <div class="clear"></div>
                <jsp:include page="indicator-filter-inc.jsp" flush="true">
                    <jsp:param name="boxId" value="indicator-filter-form" />
                </jsp:include>
                <div id="query-result">
                    <div id="indicator-flexgrid"></div>
                    <div id="freeow-br" class="freeow freeow-top-right"></div>
                </div>
                <!--<div class="spacing-line"></div>-->
            </div>
        </div>
        <jsp:include page="indicator-form-inc.jsp" flush="true" />
        <div id="translateFormPopup" title="<indaba:msg key='cp.label.translate_indicator'/>" class="hidden">
            <div id="translateFormBox" class="popup-container" style="width: 650px; max-width: 650px">
                <form id="translateForm" action="#" method="POST" >
                    <fieldset id="translateFs">
                        <input type="hidden" name="id" id="indicatorId" value="-1" />
                        <input type="hidden" name="intlId" id="indicatorIntlId" value="-1" />
                        <input type="hidden" name="answerType" id="answerType" value="-1" />
                        <input type="hidden" name="answerTypeId" id="answerTypeId" value="-1" />
                        <dl style="font-weight: bold;" >
                            <dt></dt>
                            <dd class="small-input"><indaba:msg key='cp.title.translate_to'/></dd>
                            <dd>&nbsp;&nbsp;&nbsp;&nbsp;</dd>
                            <dd class="small-input"><indaba:msg key='cp.title.original_text'/></dd>
                        </dl>
                        <dl>
                            <dt><label for="transLanguage"><indaba:msg key='cp.label.language'/></label></dt>
                            <dd>
                                <select id="transLanguage" name="transLanguage" data-placeholder="<indaba:msg key='cp.text.choose_language'/>" class="small-input-1 validate[required]">
                                    <option value=""></option>
                                    <c:forEach var="lang" items="${languages}">
                                        <option id="lang${lang.id}" value="${lang.id}">[${fn:toUpperCase(lang.language)}] ${lang.languageDesc}</option>
                                    </c:forEach>
                                </select>
                            </dd>
                            <dd>&nbsp;&nbsp;</dd>
                            <dd>
                                <select id="origLanguage" name="origLanguage" data-placeholder="<indaba:msg key='cp.text.choose_language'/>" class="small-input-1" disabled="disabled">
                                    <option value=""></option>
                                    <c:forEach var="lang" items="${languages}">
                                        <option id="origLang${lang.id}" value="${lang.id}">[${fn:toUpperCase(lang.language)}] ${lang.languageDesc}</option>
                                    </c:forEach>
                                </select>
                            </dd>
                        </dl>
                        <dl>
                            <dt><label for="transQuestion"><indaba:msg key='cp.label.question'/><span class="tiptxt" title="<indaba:msg key='cp.tip.indicator_question' />">&nbsp;</span></label></dt>
                            <dd><textarea id="transQuestion" name="transQuestion" class="small-input"></textarea></dd>
                            <dd>&nbsp;&nbsp;</dd>
                            <dd><textarea id="origQuestion" name="origQuestion" class="small-input disabled" disabled="disabled"></textarea></dd>
                        </dl>
                        <dl>
                            <dt><label for="transTip"><indaba:msg key="cp.label.tip"/><span class="tiptxt" title="<indaba:msg key='cp.tip.indicator_tip' />">&nbsp;</span></label></dt>
                            <dd><textarea id="transTip" name="transTip" class="small-input"></textarea></dd>
                            <dd>&nbsp;&nbsp;</dd>
                            <dd><textarea id="origTip" name="origTip" class="small-input disabled" disabled="disabled"></textarea></dd>
                        </dl>
                    </fieldset>
                </form>
            </div>
        </div>
        <jsp:include page="indicator-import-inc.jsp" flush="true" />
        <jsp:include page="indicator-importtbl-inc.jsp" flush="true" />
    </body>

<script type="text/javascript" charset="utf-8">
    var langOptions = "";
    var orgOptions = "";
    var ctxPath;
    var visibility;
    var translateFormBox;
    var translateForm;

    function _doOpenIndicatorEditForm(indicatorId, viewonly) {
        doOpenIndicatorEditForm(ctxPath, indicatorId, viewonly);
        return false;
    }

    function _doCloneIndicator(indicatorId) {
        doCloneIndicator(visibility, ctxPath, indicatorId, orgOptions);
        return false;
    }

    function _doDeleteIndicator(indicatorId) {
        doDeleteIndicator(ctxPath, [indicatorId], []);
        return false;
    }

    function _doMoveIndicator(indicatorId) {
        doMoveIndicator(visibility, ctxPath, [indicatorId], []);
        return false;
    }

    function _doFetchIndicatorI18n(indicatorId, langId) {
        doFetchIndicatorI18n(translateForm, ctxPath, 0, indicatorId, langId);
        return false;
    }

    function doOnLoad() {
        initIndicatorLibToolbar('${visibility}', '${contextPath}');
           
        $("#indicator-filter-form").jqTransform();
            
        $('#submitFilter').click(function(){
            $("#indicator-flexgrid").flexReload({newp: 1, dataType: 'json'});
            return false;
        });
        $('#resetFilter').click(function(){
            $("form.indicator-filter-form")[0].reset();
            $("#indicator-flexgrid").flexReload({newp: 1, dataType: 'json'});
            return false;
        });

        ctxPath = '${contextPath}';
        visibility = '${visibility}';

        <c:forEach var="lang" items="${languages}">
            langOptions += '<option value="${lang.id}">[${fn:toUpperCase(lang.language)}] ${lang.languageDesc}</option>';
    </c:forEach>
        <c:forEach var="org" items="${orgs}">
            orgOptions +='<option value="${org.id}">${org.name}</option>';
    </c:forEach>

            $("#indicator-flexgrid").flexigrid({
                url: '${contextPath}/lib/indicator-lib!find',
                method: 'POST',
                dataType: 'json',
                preProcess: function(data) {
                    if (data.massdelete != 'yes') {
                        $('.massdelete').parent().parent().hide();
                    }
                    if (data.massmove != 'yes') {
                        $('.massmove').parent().parent().hide();
                    }
                    if (data.addindicator != 'yes') {
                        $('.add').parent().parent().hide();
                    }
                    if (data.importindicators != 'yes') {
                        $('.import').parent().parent().hide();
                    }
                        
                    $.each(data.rows, function(index, elem){
                        var indicatorId = elem.id;
                        var actions = "";

                        if (elem.editable) {
                            actions += '<a class=" link edit" href="javascript:void(0)" onclick="return _doOpenIndicatorEditForm(' + indicatorId + ', false' + ');"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.edit"/></a>';
                        } else {
                            actions += '<a class=" link edit" href="javascript:void(0)" onclick="return _doOpenIndicatorEditForm(' + indicatorId + ', true' +');"><img height="14px" src="${contextPath}/resources/images/view.png"><indaba:msg key="cp.btn.view"/></a>';
                        }
                        if (elem.deleteable) {
                            actions += '<a class="link delete" href="javascript:void(0)" onclick="return _doDeleteIndicator(' + indicatorId + ');"><img height="14px" src="${contextPath}/resources/images/delete.png"><indaba:msg key="cp.btn.delete"/></a>';
                        }
                        if (elem.moveable) {
                            actions += '<a class="link move" href="javascript:void(0)" onclick="return _doMoveIndicator(' + indicatorId + ');"><img height="14px" src="${contextPath}/resources/images/move.png"><indaba:msg key="cp.btn.move"/></a>';
                        }
                        if (elem.translateable) {
                            actions += '<a class="link trans" href="javascript:void(0)" onclick="return _doFetchIndicatorI18n(' + indicatorId + ', ' +elem.langId+ ');"><img height="14px" src="${contextPath}/resources/images/translation.png"><indaba:msg key="cp.btn.translate"/></a>';
                        }

                        actions += '<a class="link clone" href="javascript:void(0)" onclick="return _doCloneIndicator(' + indicatorId + ');"><img height="14px" src="${contextPath}/resources/images/copy.png"><indaba:msg key="cp.btn.clone"/></a>';
                        elem.actions = actions;
                    });

                    // reset to 'select all'
                    var elem = $('.select');
                    elem.empty();
                    elem.append('<img src="${contextPath}/resources/images/uncheck.png">' + $.i18n.message('cp.btn.select_all'));
                    return data;
                },
                nowrap: false,
                colModel : [
                    {display: '<indaba:msg key="cp.ch.id"/>', name : 'id', width: 0, sortable : true, align: 'left', hide:true},
                    {display: '<indaba:msg key="cp.ch.name"/>', name : 'name', width: 100, sortable : true, align: 'left'},
                    {display: '<indaba:msg key="cp.ch.type"/>', name : 'typeName', width: 50, sortable : true, align: 'left'},
                    {display: '<indaba:msg key="cp.ch.question"/>', name : 'question', width: <c:choose><c:when test='${visibility==0}'>251</c:when><c:otherwise>201</c:otherwise></c:choose>, sortable : true, align: 'left'},
                    {display: '<indaba:msg key="cp.ch.organization"/>', name : 'orgName', width: 140, sortable : true, align: 'left'},
    <c:if test="${visibility==0}">
                    {display: '<indaba:msg key="cp.ch.library"/>', name : 'libName', width : 50, sortable : false, align: 'left'},
    </c:if>
                    {display: '<indaba:msg key="cp.ch.actions"/>', name : 'actions', width : 265, sortable : false, align: 'left'}
                ],
                buttons : [
                    {name: '<indaba:msg key="cp.btn.add"/>', bclass: 'add', bimage: '${contextPath}/resources/images/add.png', /*attrs:[{name:'rel', value: '#editIndicator'}],*/ onpress : function(){
                            setViewMode(false);
                            $('#indicatorFormPopup').dialog('option', 'title', $.i18n.message('cp.title.add_indicator'));
                            var p = $('#indicatorFs');
                            $('input[name=id]', p).val(-1);
                            $('#downloadTdf').hide();
                            $('#uploadTdf').html('&nbsp;&nbsp;upload');
                            $('#indicatorFormPopup').dialog('open');
                            return false;
                        }},
                    {name: '<indaba:msg key="cp.btn.import"/>', bclass: 'import', bimage: '${contextPath}/resources/images/import.png', onpress : function(){return doImportIndicators('${visibility}','${contextPath}');}},
                    {name: '<indaba:msg key="cp.btn.import_translations"/>', bclass: 'grptranslate', bimage: '${contextPath}/resources/images/translation.png', onpress : function(){return doImportTranslations('${visibility}','${contextPath}');}},
                    {separator: true},
                    {name: '<indaba:msg key="cp.btn.move"/>', bclass: 'massmove', bimage: '${contextPath}/resources/images/move.png', onpress : function(){return doGroupMoveIndicator('${visibility}', '${contextPath}');}},
                    {name: '<indaba:msg key="cp.btn.delete"/>', bclass: 'massdelete', bimage: '${contextPath}/resources/images/delete.png', onpress : function(){return doGroupDeleteIndicator('${contextPath}');}},
                    {name: '<indaba:msg key="cp.btn.export"/>', bclass: 'export', bimage: '${contextPath}/resources/images/export.png', onpress : function(){return doGroupExportIndicator('${contextPath}', langOptions);}},
                    {separator: true},
                    {name: '<indaba:msg key="cp.btn.select_all"/>', bclass: 'select', bimage: '${contextPath}/resources/images/uncheck.png', onpress : function(){return doSelectIndicators('${contextPath}');}}
                ],
                searchitems : [
                    {display: '<indaba:msg key="cp.ch.name"/>', name : 'name', isdefault: true},
                    {display: '<indaba:msg key="cp.ch.question"/>', name : 'question'}
                ],
                warnClass: 'warn',
                resizable: false,
                sortname: "name",
                sortorder: "asc",
                usepager: true,
                title: '<indaba:msg key="cp.title.indicators"/>',
                useRp: true,
                rp: 50,
                rpOptions: [30, 50, 100, 200, 500],
                showTableToggleBtn: false,
                showToggleBtn: false,
                width: 'auto',
                height: '100%',
                pagestat: 'Displaying {from} to {to} of {total} items',
                onError: function(){
                    fixDHTMLXTabbarHeight("content");
                },
                onNoData: function(){
                    fixDHTMLXTabbarHeight("content");
                },
                onSuccess: function(){
                    fixDHTMLXTabbarHeight("content");
                },
                onSubmit: function(){
                    var p = [];
                    $('select, input', $('#indicator-filter-form')).each(function(){
                        p[p.length] = {name: $(this).attr("name"), value: $(this).val()};
                    });
                    this.params = p;
                    return true;
                }
            });
        
            translateFormBox = $('#translateFormBox');
            translateForm = $('#translateForm', translateFormBox);
            $('input:text, input:radio, input:checkbox, textarea', translateForm).uniform();
            $("select",translateForm).chosen();
            // Dialog
            $('#translateFormPopup').dialog({
                autoOpen: false,
                width: 722,
                resizable: false,
                modal: true,
                open: function(){
                    $('select[name^=orig], textarea[name^=orig]', translateForm).attr('disabled', 'disabled');
                    $('select#origLanguage', translateForm).trigger("liszt:updated");
                    $("select",translateForm).chosen();
                    $('select#transLanguage', translateForm).change(function(){
                        doFetchIndicatorI18n(translateForm, '${contextPath}', 1,  $('input#indicatorId', translateForm).val(), $(this).val());
                    });
                    translateForm.validationEngine({prettySelect: true,useSuffix: "_chzn",validationEventTrigger:"change"});
                },
                beforeClose: function(){
                    clearFormFields(translateFormBox);
                    $('select option', translateForm).show();

                    translateForm.validationEngine('hideAll');
                    $('select#transLanguage', translateForm).removeClass('chzn-done');
                    $('div#transLanguage_chzn', $('select#transLanguage', translateForm).parent()).remove();
                },
                close: function(){
                    $('div.choice-box',translateForm).remove();
                    $('textarea[name^=trans]',translateForm).removeClass('validate[required]');
                },
                buttons: {
                    "Save": function() {
                        if(!$('form:first', translateFormBox).validationEngine('validate')) {
                            return false;
                        }
                        var data = {};
                        data.indicatorId = $('#indicatorId', translateForm).val();
                        data.indicatorIntlId = $('#indicatorIntlId', translateForm).val();
                        data.answerType = $('#answerType', translateForm).val();
                        data.answerTypeId = $('#answerTypeId', translateForm).val();
                        data.langId = $('#transLanguage', translateForm).val();
                        data.question = $('#transQuestion', translateForm).val();
                        data.tip = $('#transTip', translateForm).val();
                        var choices = [];
                        $('div.choice-box', translateForm).each(function(){
                            var choice = {};
                            choice.atcChoiceIntlId = $('input[id^=atcChoiceIntlId]', $(this)).val();
                            choice.atcChoiceId =$('input[id^=atcChoiceId]', $(this)).val();
                            choice.useScore = ('true' == $('input[id^=useScore]', $(this)).val());
                            choice.label = $('textarea[id^=transLabel]', $(this)).val();
                            choice.criteria = $('textarea[id^=transCriteria]', $(this)).val();
                            choices[choices.length] = choice;
                        });
                        data.choices=JSON.stringify(choices);
                        // var loading=new ol.loading({id:"indicatorFormPopup", loadingText:"Waiting..."});
                        // loading.show();
                        var dlg = $(this);
                        $.ajax({
                            url:'${contextPath}/lib/indicator-lib!translate',
                            type: 'POST',
                            dataType: 'json',
                            data: data,
                            success: function(data){
                                // loading.hide();
                                if(data.ret != 0) {
                                    ocsError(data.desc);
                                } else {
                                    dlg.dialog("close");
                                    ocsSuccess("Translation saved!");
                                }
                            },
                            error: function(data) {
                                // loading.hide();
                                defaultAjaxErrorHanlde(data);
                            }
                        });
                        return false;
                    },
                    "Cancel": function() {
                        $(this).dialog("close");
                    }
                }
            });
        }
</script>
<script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/indaba.indicator-lib.js"></script>
</html>