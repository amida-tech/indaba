<%--
    Document   : Product Tab - 'Horses'
    Created on : May 20, 2012, 11:20:21 AM
    Author     : Jeff Jiang
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<div id="productHorsesTab">
    <div id="horsesFlexGrid" style="margin: 0px">
        <div id="horses-flexgrid"></div>
        <div id="freeow-br" class="freeow" style="top: 150px; right: 300px"></div>
    </div>
</div>

<script type="text/javascript" charset="utf-8">
    $(function(){
        $("#horses-flexgrid").flexigrid({
            autoload: false,
            //url: '${contextPath}/prod/horse!find?prodId=',
            method: 'POST',
            dataType: 'json',
            colModel : [
                {display: 'ID', name : 'id', width : 50, sortable : false, hide: false},
                {display: '', name : 'horseId', width : 0, sortable : true, hide: true}, // used for extracting ID when selected sortable must be true here!
                {display: '<indaba:msg key="cp.ch.target"/>', name : 'targetName', width : 400, sortable : true},
                {display: '<indaba:msg key="cp.ch.status"/>', name : 'statusDisplay', width : 77, sortable : true},
                {display: '<indaba:msg key="cp.ch.actions"/>', name : 'actions', width : 300, sortable : false}
            ],
            preProcess: function(data) {
                $.each(data.rows, function(index, elem){
                    var horseId = elem.id;
                    var status = elem.status;
                    // Start
                    var actions = '';
                    if(status == 1 || status == 4) {
                        actions += '<a class="link" href="javascript:void(0)" onclick="return doApplyAction(\'start\',['+horseId+']);"><img height="14px" src="${contextPath}/resources/images/start.png"><indaba:msg key="cp.btn.start"/></a>';
                    }
                    // Stop
                    if(status == 2) {
                        actions += '<a class="link" href="" onclick="return doApplyAction(\'stop\',['+ horseId +'],\''+ elem.name +'\');"><img height="14px" src="${contextPath}/resources/images/stop.png"><indaba:msg key="cp.btn.stop"/></a>';
                    }
                    // Cancel
                    if(status != 5) {
                        actions += '<a class="link" href="" onclick="return doApplyAction(\'cancel\',['+ horseId +'],\''+ elem.name +'\');"><img height="14px" src="${contextPath}/resources/images/cancel.png"><indaba:msg key="cp.btn.cancel"/></a>';
                    }
                    // Uncancel
                    if(status == 5) {
                        actions += '<a class="link" href="" onclick="return doApplyAction(\'uncancel\',['+ horseId +'],\''+ elem.name +'\');"><img height="14px" src="${contextPath}/resources/images/uncancel.png"><indaba:msg key="cp.btn.uncancel"/></a>';
                    }
                    // Reset
                    if(status != 1) {
                        actions += '<a class="link" href="" onclick="return doApplyAction(\'reset\',['+ horseId +'],\''+ elem.name +'\');"><img height="14px" src="${contextPath}/resources/images/reset.png"><indaba:msg key="cp.btn.reset"/></a>';
                    }
                    elem.actions = actions;
                    elem.horseId = horseId;
                });

                // reset to 'select all'
                var elem = $('.select');
                elem.empty();
                elem.append('<img src="${contextPath}/resources/images/uncheck.png">' + $.i18n.message('cp.btn.select_all'));
                
                return data;
            },
            buttons : [
                {name: '<indaba:msg key="cp.btn.start"/>', bclass: 'add', bimage: '${contextPath}/resources/images/start.png', onpress : function(){
                        handleBatchOfHorses('start');
                        return false;
                    }
                },
                {name: '<indaba:msg key="cp.btn.stop"/>', bclass: 'add', bimage: '${contextPath}/resources/images/stop.png', onpress : function(){
                        handleBatchOfHorses('stop');
                        return false;
                    }
                },
                {name: '<indaba:msg key="cp.btn.cancel"/>', bclass: 'add', bimage: '${contextPath}/resources/images/cancel.png', onpress : function(){
                        handleBatchOfHorses('cancel');
                        return false;
                    }
                },
                {name: '<indaba:msg key="cp.btn.uncancel"/>', bclass: 'add', bimage: '${contextPath}/resources/images/uncancel.png', onpress : function(){
                        handleBatchOfHorses('uncancel');
                        return false;
                    }
                },
                {name: '<indaba:msg key="cp.btn.reset"/>', bclass: 'add', bimage: '${contextPath}/resources/images/reset.png', onpress : function(){
                        handleBatchOfHorses('reset');
                        return false;
                    }
                },
                {separator: true},
                {name: '<indaba:msg key="cp.btn.select_all"/>', bclass: 'select', bimage: '${contextPath}/resources/images/uncheck.png', onpress : function(){
                        return doSelectHorses('${contextPath}');
                    }
                }
            ],
            warnClass: 'warn',
            resizable: false,
            sortname: "PRODNAME",
            sortorder: "asc",
            usepager: true,
            title: 'Manage Horses',
            useRp: true,
            rp: 15,
            showTableToggleBtn: false,
            showToggleBtn: false,
            width: 855,
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
                $('select, input', $('#project-filter-form')).each(function(){
                    p[p.length] = {name: $(this).attr("name"), value: $(this).val()};
                });
                this.params = p;
                return true;
            }
        });
        var horseForm = $('form#horseForm');
        // Dialog
        $('#horseFormPopup').dialog({
            autoOpen: false,
            width: 722,
            resizable: false,
            modal: true,
            open: function(){
                resetForm(horseForm);
                horseForm.validationEngine({prettySelect: true,useSuffix: "_chzn",validationEventTrigger:"change"});
            },
            beforeClose: function(){
                resetForm(horseForm);
                //$('select option', horseForm).show();
                horseForm.validationEngine('hideAll');
            },
            close: function(){
            },
            buttons: {
                "Save": function() {
                    if(!horseForm.validationEngine('validate')) {
                        return false;
                    }
                    var reqData = extractFormDataToJson(horseForm);
                    // var loading=new ol.loading({id:"indicatorFormPopup", loadingText:"Waiting..."});
                    // loading.show();
                    var dlg = $(this);
                    $.ajax({
                        url:'${contextPath}/prod/horse!save',
                        type: 'POST',
                        dataType: 'json',
                        data: reqData,
                        success: function(data){
                            // loading.hide();
                            if(data.ret != 0) {
                                ocsError(data.desc);
                            } else {
                                dlg.dialog("close");
                                ocsSuccess("Successfully save the horse!");
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
    });

    function doSelectHorses(contextPath) {
        var elem = $('#horsesFlexGrid div.flexigrid div.tDiv2 a.fbutton span.select');
        var rowElems = $('#horses-flexgrid tbody tr');
        var label = elem.text();
        elem.empty();
        if(label == $.i18n.message('cp.btn.select_all')) {
            elem.append('<img src="'+ contextPath + '/resources/images/check.png">' + $.i18n.message('cp.btn.unselect_all'));
            rowElems.attr('class', 'trSelected');
        } else {
            elem.append('<img src="'+ contextPath + '/resources/images/uncheck.png">' + $.i18n.message('cp.btn.select_all'));
            rowElems.attr('class', 'erow');
        }
        return false;
    }
    
    function loadHorsesFlexGrid(prodId) {
        $("#horses-flexgrid").flexReload({newp: 1,dataType: 'json',url: '${contextPath}/prod/horse!find?prodId='+prodId});
    }

    function doApplyAction(action, horseIds) {
        var confirmMsg;

        switch (action) {
            case "start" :
                confirmMsg = $.i18n.message('cp.text.confirm_start_horse');
                break;
            case "stop":
                confirmMsg = $.i18n.message('cp.text.confirm_stop_horse');
                break;
            case "cancel":
                confirmMsg = $.i18n.message('cp.text.confirm_cancel_horse');
                break;
            case "uncancel":
                confirmMsg = $.i18n.message('cp.text.confirm_uncancel_horse');
                break;
            case "reset":
                confirmMsg = $.i18n.message('cp.text.confirm_reset_horse');
                break;
            default:
                confirmMsg = $.i18n.message('cp.text.confirm_act_on_horse');
        }

        ocsConfirm(confirmMsg, $.i18n.message('cp.title.confirm'), function(choice){
            if(choice) {
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '${contextPath}/prod/horse!applyAction',
                    data: {
                        action: action,
                        horseIds: horseIds
                    },
                    success: function(resp){
                        if(resp.ret == 0) {
                            loadHorsesFlexGrid(${prodId});
                            var data = resp.data;
                            var ocsDlg = $('<div class="hidden">');
                            var resultBox = $('<div class="popup-container result">');
                            ocsDlg.append(resultBox);
                            if(!data || data.total <= 0) {
                                resultBox.append("<div>No horses data.</div>");
                            }else{
                                if(data.successes && data.successes.length > 0) {
                                    resultBox.append(gernerateResultBox(action, data.successes, true));
                                }
                                if(data.errors && data.errors.length > 0) {
                                    resultBox.append(gernerateResultBox(action, data.errors, false));
                                }
                            }
                            ocsDlg.dialog({
                                width: 622,
                                title: $.i18n.message('cp.title.result'),
                                resizable: false,
                                modal: true,
                                autoOpen: true,
                                open: function(){
                                },
                                close: function(){
                                    ocsDlg.remove();
                                },
                                buttons: {
                                    "OK": function() {
                                        ocsDlg.dialog("close");
                                    }
                                }
                            });
                        } else {
                            ocsError(resp.desc);
                        }
                    }
                });
            }
        });
        
        return false;
    }
    function handleBatchOfHorses(action) {
        var selectedElems = $('tbody tr.trSelected', $('#horses-flexgrid'));
        if(!selectedElems.length > 0) {
            $('#freeow-br').freeow($.i18n.message('cp.title.warning'), $.i18n.message('cp.error.no_horse_selected'), {
                classes: ["gray"],
                autoHideDelay:3000,
                autoHide: true,
                hideDuration: 500
            });
        } else {
            var idList = [];
            selectedElems.each(function(){
                idList[idList.length] = $('td[abbr=horseId]>div', $(this)).text(); // horseId must be sortable in colModel!
            });
            doApplyAction(action, idList);
        }
    }

    function gernerateResultBox(action, result, success) {
        var summary;
        if (success) {
            summary = $.i18n.message('cp.text.horse_action_success', [action.toUpperCase()]);
        } else {
            summary = $.i18n.message('cp.text.horse_action_fail', [action.toUpperCase()]);
        }
        var box = $('<div class="'+(success? 'successes': 'errors') + '"><div class="summary">'+summary+'</div></div>');
        var items = $('<ul></ul>');
        box.append(items);
        $.each(result, function(i,item){
            if(success) {
                items.append('<li>'+item.target+'</li>');
            } else {
                items.append('<li><strong>'+item.target+'</strong>: '+item.msg+'</li>');
            }
        });
        return box;
    }
</script>