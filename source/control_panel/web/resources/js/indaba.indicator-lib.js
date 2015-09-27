
function initIndicatorLibToolbar(visibility, contextPath){
    var indicatorLibToolbarData = {
        parent: "indicatorLibToolbar",
        icon_path: contextPath + "/resources/images/",
        items: [
            {
                type: "buttonTwoState",
                id: "allIndLib",
                text: $.i18n.message('cp.tab2.indicator.all_lib'),
                img: "pub_endors_lib.png"
            },
            {
                type: "separator",
                id: "sep4"
            },
            {
                type: "buttonTwoState",
                id: "pubEndrsIndLib",
                text: $.i18n.message('cp.tab2.indicator.pub_endorsed_lib'),
                img: "pub_endors_lib.png"
            },
            {
                type: "separator",
                id: "sep3"
            },
            {
                type: "buttonTwoState",
                id: "pubExtIndLib",
                text: $.i18n.message('cp.tab2.indicator.pub_extended_lib'),
                img: "pub_ext_lib.png"
            },
            {
                type: "separator",
                id: "sep2"
            },
            {
                type: "buttonTwoState",
                id: "pubTestIndLib",
                text: $.i18n.message('cp.tab2.indicator.pub_test_lib'),
                img: "pub_endors_lib.png"
            },
            {
                type: "separator",
                id: "sep1"
            },
            {
                type: "buttonTwoState",
                id: "priIndLib",
                text: $.i18n.message('cp.tab2.indicator.priv_lib'),
                img: "private_lib.png"
            }
        ]
    };
    var toolbar = new dhtmlXToolbarObject(indicatorLibToolbarData, "dhx_blue");
                    
    fixDHTMLXTabbarHeight("content");
    if(visibility == '0') {
        toolbar.setItemState("allIndLib", true);
    }else if(visibility == '1') {
        toolbar.setItemState("pubEndrsIndLib", true);
    }else if(visibility == '2') {
        toolbar.setItemState("pubExtIndLib", true);
    }else if(visibility == '3') {
        toolbar.setItemState("pubTestIndLib", true);
    } else if(visibility == '4') {
        toolbar.setItemState("priIndLib", true);
    } else{
        toolbar.setItemState("pubExtIndLib", true);
    } 
    $('div.dhx_toolbar_btn').css("cursor", "pointer");
    toolbar.attachEvent("onStateChange", function(id, pressed){
        var clickPressedBar = false;
        var pressedBar = null;
        toolbar.forEachItem(function(itemId){
            if(itemId == id) {
                if(!pressed) {
                    clickPressedBar = true;
                    toolbar.setItemState(id, true);
                }
                return false;
            } else if(toolbar.getItemState(itemId)) {
                pressedBar = itemId;
            }
            return true;
        });
        if(!clickPressedBar) {
            var toVisibility = 0;
            if("allIndLib" == id) {
                toVisibility = 0;
            } else if("pubEndrsIndLib" == id) {
                toVisibility = 1;
            } else if("pubExtIndLib" == id) {
                toVisibility = 2;
            }else if("pubTestIndLib" == id) {
                toVisibility = 3;
            } else if("priIndLib" == id) {
                toVisibility = 4;
            } 
            toolbar.setItemState(id, true);
            toolbar.setItemState(pressedBar, false);
            window.location.href= contextPath + "/lib/indicator-lib?visibility=" + toVisibility;
        }
        return false;
    });
}

function checkSelected() {
    var selected = $('tbody tr.trSelected', $('#indicator-flexgrid')).length;
    if(!selected) {
        $('#freeow-br').freeow($.i18n.message('cp.title.warning'), $.i18n.message('cp.error.no_indicator_selected'), {
            classes: ["gray"], 
            autoHideDelay:3000, 
            autoHide: true, 
            hideDuration: 500
        });
        return false;
    } else {
        return true;
    }
}
function doSelectIndicators(contextPath) {
    var elem = $('#query-result div.flexigrid div.tDiv2 a.fbutton span.select');
    var rowElems = $('#indicator-flexgrid tbody tr');
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

function doImportIndicators(visibility, contextPath){
    var ocsDlg = $('#indicatorImportPopup');
    initFileUpload(ocsDlg, contextPath+'/lib/indicator-lib!validateIndicatorImport', contextPath+'/lib/indicator-lib!importIndicators');
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

function doImportTranslations(visibility, contextPath){
    var ocsDlg = $('#indicatorImportPopup');
    initFileUpload(ocsDlg, contextPath+'/lib/indicator-lib!validateIndicatorTranslations', contextPath+'/lib/indicator-lib!importIndicatorTranslations');
    //$('input:file', ocsDlg).uniform();
    ocsDlg.dialog({
        width: 460,
        title: $.i18n.message('cp.btn.import_translations'),
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

function getSelectedIndicatorIds() {
    var idList = [];
    $('tbody tr.trSelected', $('#indicator-flexgrid')).each(function(){
        idList[idList.length] = $('td[abbr=id]>div', $(this)).text();
    });
    return idList;
}

function getSelectedIndicatorNames() {
    var nameList = [];
    $('tbody tr.trSelected', $('#indicator-flexgrid')).each(function(){
        nameList[nameList.length] = $('td[abbr=name]>div', $(this)).text();
    });
    return nameList;
}

function doGroupMoveIndicator(visibility, contextPath){
    if(!checkSelected()) {
        return false;
    }
    var idList = getSelectedIndicatorIds();
    var nameList = getSelectedIndicatorNames();
    doMoveIndicator(visibility, contextPath, idList, nameList);
    return false;
}

function doGroupDeleteIndicator(contextPath){
    if(!checkSelected()) {
        return false;
    }
    var idList = getSelectedIndicatorIds();
    var nameList = getSelectedIndicatorNames();
    doDeleteIndicator(contextPath, idList, nameList);
    return false;
}

function doGroupExportIndicator(contextPath, langOptions){
    if(!checkSelected()) {
        return false;
    }
    var idList = getSelectedIndicatorIds();
    var idOptions = "";
    $.each(idList, function(index, val){
        idOptions += '<input name="idList" style="display: none;" value='+val+' checked="checked" />'
    });
    var ocsDlg = $('<div id="groupExportDlg"><form id="exportForm" name="exportForm" method="POST" action="'+contextPath + '/lib/indicator-lib!export" target="_self">' +
        '<div id="ocsDlg" class="dlg">' +
        '<div style="line-height: 30px; font-weight: bold;">'+$.i18n.message('cp.text.export_indicators', [idList.length])+'</div><div>'+$.i18n.message('cp.text.choose_export_language')+'</div>'+
        '<div style="margin-top: 10px;"><select id="langId" name="langId" style="width: 200px" data-placeholder="'+$.i18n.message('cp.text.choose_language')+'">' +
        '<option value="0"></option>' + langOptions +'</select>'+
        '' +idOptions+ '<div style="margin: 10px 0px 0px 0px;" class="error-box hidden"></div>' +
        '<div><input type="checkbox" name="forTrans" value="yes">' +$.i18n.message('cp.text.for_text_trans')+ '</div>' +
        '</div></form></div>');
    ocsDlg.dialog({
        width: 460,
        title: $.i18n.message('cp.title.export_indicators'),
        resizable: false,
        modal: true,
        open: function(){
            $('select', ocsDlg).chosen();
            $('select', ocsDlg).parents('div.ui-dialog').css('overflow', 'visible');
            $('select', ocsDlg).change(function() {
                if($('#langId',ocsDlg).val() > 0) {
                    $('.error-box', ocsDlg).hide();
                    return false;
                }
                return false;
            });
        },
        close: function() {
            ocsDlg.parents('div.ui-dialog').remove();
            $('#groupExportDlg').remove();
        },
        buttons: {
            "Export": function() {
                if($('#langId',ocsDlg).val() <= 0) {
                    $('.error-box', ocsDlg).text($.i18n.message('cp.error.must_choose_language'));
                    $('.error-box', ocsDlg).show();
                    return false;
                }
                $('#exportForm', ocsDlg).submit();
                $(this).dialog("close");
                return false;
            },
            "Cancel": function() {
                $(this).dialog("close");
            }
        }
    });
    return false;
}

function doMoveIndicator(visibility, contextPath, idList, nameList){
    var ocsDlg = $('<div id="moveIndicatorDlg" class="dlg hidden"><div style="line-height: 30px; font-weight: bold;">'+$.i18n.message('cp.text.move_indicators', [idList.length])+'</div><div>'+$.i18n.message('cp.text.choose_target_lib')+'</div>'+
        '<div style="margin-top: 10px;"><select name="visibility" style="width: 200px" data-placeholder="'+$.i18n.message('cp.text.choose_lib')+'">' +
        '<option value="0"></option>' +
        '<option value="1">'+$.i18n.message('cp.tab2.indicator.pub_endorsed_lib')+'</option>' +
        '<option value="2">'+$.i18n.message('cp.tab2.indicator.pub_extended_lib')+'</option>' +
        '<option value="3">'+$.i18n.message('cp.tab2.indicator.pub_test_lib')+'</option>' +
        '</select></div><div style="margin: 10px 0px 0px 0px;" class="error-box hidden"></div></div>');
    if(visibility <= 0) {
        visibility = 1;
    }
    $('select option[value='+visibility+']', ocsDlg).remove();
    ocsDlg.dialog({
        width: 460,
        title: $.i18n.message('cp.title.move_indicator'),
        resizable: false,
        modal: true,
        open: function(){
            $('select', ocsDlg).chosen();
            $('select', ocsDlg).parents('div.ui-dialog').css('overflow', 'visible');
            $("select", ocsDlg).change(function(){
                $('.error-box', ocsDlg).hide();
            });
        },
        close: function() {
            ocsDlg.parents('div.ui-dialog').remove();
            $('#moveIndicatorDlg').remove();
        },
        buttons: {
            "OK": function() {
                var toLib = $('select', ocsDlg).val();
                if(toLib <=0){
                    $('.error-box', ocsDlg).text($.i18n.message('cp.error.must_choose_library'));
                    $('.error-box', ocsDlg).show();
                    return false;
                }
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: contextPath + '/lib/indicator-lib!move',
                    data: {
                        idList: idList,
                        toLib: toLib
                    },
                    success: function(resp){
                        if(resp.ret == 0) {
                            ocsDlg.dialog("close");
                            handleReports(resp.data);
                            $("#indicator-flexgrid").flexReload({
                                dataType: 'json'
                            });
                        } else {
                            ocsError(resp.desc);
                        }
                    }
                });
                return false;
            },
            "Cancel": function() {
                $(this).dialog("close");
            }
        }
    });
    return false;
}

function doDeleteIndicator(contextPath, idList, nameList){
    var msg = $.i18n.message('cp.text.confirm_delete_indicator', [idList.length]);

    msg += "\n";
    for (var i = 0; i < nameList.length; i++) {
        msg += nameList[i];
        if (i < nameList.length-1) {
            msg += ", ";
        }
    }
    ocsConfirm(msg, $.i18n.message('cp.title.confirm'),
    function(choice){
        if(choice){
            return doIndicatorAjaxRequest(contextPath, 'delete', {
                idList: idList
            });
        }
        return false;
    });
    return false;
}

function doCloneIndicator(visibility, contextPath, id, orgOptions){
    var visOptions =
        '<option value="1">' + $.i18n.message('cp.label.public') + '</option>' +
        '<option value="2">' + $.i18n.message('cp.label.private') + '</option>';

    var ocsDlg = $('<div id="cloneIndicatorDlg" class="dlg hidden">'+
        '<div>'+$.i18n.message('cp.text.specify_clone_indicator')+'</div>'+
        '<div style="margin-top: 10px;">'+
        '<div class="row"><label>'+$.i18n.message('cp.label.indicator_name')+'</label><div class="field" style="width: 320px;"><input name="name" title="Indicator name" required="required" type="text" style="width: 300px;"/></div></div>' +
        '<div class="row"><label>'+$.i18n.message('cp.label.organization')+'</label><select name="organization" title="Organization" data-placeholder="'+$.i18n.message('cp.text.choose_organization') +'" required="required" style="width: 200px;">' +
        '<option value="0"></option>' + orgOptions +
        '</select></div>'+
        '<div class="row"><label>'+$.i18n.message('cp.label.visibility')+'</label><select name="visibility" title="Visibility" data-placeholder="'+$.i18n.message('cp.text.choose_visibility') +'" required="required" style="width: 200px;">' +
        '<option value="0"></option>' + visOptions +
        '</select></div>'+
        '</div></div>');
    
    $('input:text', ocsDlg).uniform();
    
    ocsDlg.dialog({
        width: 460,
        title: $.i18n.message('cp.title.clone_indicator'),
        resizable: false,
        modal: true,
        open: function() {
            $('div.ui-dialog').css('overflow', 'visible');
            $('select', ocsDlg).chosen();
        },
        close: function() {
            ocsDlg.parents('div.ui-dialog').remove();
            $('#cloneIndicatorDlg').remove();
        },
        buttons: {
            "OK": function() {
                $('.error-box', ocsDlg).each(function(){
                    $(this).parent().remove();
                });
                var valid = true;
                var values = {};
                $('input[required]:text, select[required]', ocsDlg).each(function(){
                    if((typeof $(this).val() === "undefined") || $(this).val().empty() || $(this).val() == 0) {
                        var errTxt = $.i18n.message('cp.text.complete_field', [$(this).attr('title')]);
                        $(this).parents('div.row').after('<div class="row"><div style="width: 262px;margin-left:100px;" class="error-box"><span class="err-txt">'+errTxt+'</span></div></div>');
                        return (valid=false); 
                    }
                    values[$(this).attr('name')] = $(this).val();
                });
                if(!valid) {
                    return false;
                }
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: contextPath + '/lib/indicator-lib!clone',
                    data: {
                        visibility: values.visibility,
                        id: id,
                        name: values.name,
                        organization: values.organization
                    },
                    success: function(resp){
                        if(resp.ret == 0) {
                            ocsDlg.dialog("close");
                            
                            $("#indicator-flexgrid").flexReload({
                                dataType: 'json'
                            });
                            
                            var libname = (values.visibility == 1) ? $.i18n.message('cp.tab2.indicator.pub_test_lib') : $.i18n.message('cp.tab2.indicator.priv_lib');
                            ocsSuccess($.i18n.message('cp.text.indicator_created', [values.name, libname]));
                            return false;
                        } else {
                            ocsError(resp.desc);
                        }
                    }
                });
                return false;
            },
            "Cancel": function() {
                $(this).dialog("close");
            }
        }
    });
    return false;
}


var fetchingIndicatorI18n = false;
function doFetchIndicatorI18n(translateForm, contextPath, type, id, langId) {
    if (fetchingIndicatorI18n) return false;

    fetchingIndicatorI18n = true;
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: contextPath + '/lib/indicator-lib!get',
        data: {
            id:id,
            langId: langId
        },
        success: function(resp){
            fetchingIndicatorI18n = false;
            if(resp.ret == 0) {
                var data = resp.data;                
                initTranslateIndicatorForm(translateForm, type, data);
                if(type == 0) {
                    $('#translateFormPopup').dialog('option', 'title', $.i18n.message('cp.title.translate_indicator'));
                    $('#translateFormPopup').dialog('open');
                }
            }
        }
    });
    return false;
}

/**
 * data - IndicatorI18nVO
 * type - 0: original; 1: translate to.
 */
function initTranslateIndicatorForm(translateForm, type, data) {
    if(type == 0) { // original
        $('input#indicatorId', translateForm).val(data.id);
        $('input#answerType', translateForm).val(data.answerType);
        $('input#answerTypeId', translateForm).val(data.answerTypeId);
        $('textarea#origQuestion', translateForm).val(data.question);
        $('textarea#origTip', translateForm).val(data.tip);
        var defaultLangId = data.language;

        $('#lang'+defaultLangId, translateForm).hide();
        $('select#transLanguage', translateForm).trigger("liszt:updated");
        $('select#origLanguage', translateForm).val(defaultLangId).trigger("liszt:updated");
        if(data.answerChoices) {
            $.each(data.answerChoices, function(i, elem){
                var choiceBox = $('<div class="choice-box">');
                var seqNum = (i+1);
                choiceBox.append('<div class="seq-num">' + $.i18n.message('cp.label.choice') + ' ' +seqNum+'.</div>');
                $('fieldset#translateFs', translateForm).append(choiceBox);
                choiceBox.append( '<input type="hidden" id="atcChoiceId' + seqNum + '" name="atcChoiceId' + seqNum + '" class="small-input" value="'+elem.id+'">');
                choiceBox.append( '<input type="hidden" id="atcChoiceIntlId' + seqNum + '" name="atcChoiceIntlId' + seqNum + '" class="small-input" value="-1">');
                choiceBox.append( '<input type="hidden" id="useScore' + seqNum + '" name="useScore' + seqNum + '" class="small-input" value="'+elem.useScore+'">');
                var labelElem = $('<dl>');
                labelElem.append('<dt><label for="label' + seqNum + '">' + $.i18n.message('cp.label.label') + '</label></dt>');
                labelElem.append('<dd></textarea><textarea maxlength="45" id="transLabel' + seqNum + '" name="transLabel' + seqNum + '" class="small-input"></textarea></dd>');
                labelElem.append('<dd>&nbsp;&nbsp;</dd>');
                labelElem.append('<dd><textarea id="origLabel' + seqNum + '" name="origLabel' + seqNum + '" class="small-input disabled" disabled>'+elem.label+'</textarea></dd>');
                choiceBox.append(labelElem);
                $('textarea', labelElem).uniform();
                var criteriaElem = $('<dl>');
                criteriaElem.append('<dt><label for="criteria' + seqNum + '">' + $.i18n.message('cp.label.criteria') + '</label></dt>');
                criteriaElem.append('<dd><textarea id="transCriteria' + seqNum + '" name="transCriteria' + seqNum + '" class="small-input"></textarea></dd>');
                criteriaElem.append('<dd>&nbsp;&nbsp;</dd>');
                criteriaElem.append('<dd><textarea id="origCriteria' + seqNum + '" name="origCriteria' + seqNum + '" class="small-input disabled" disabled>'+elem.criteria+'</textarea></dd>');
                choiceBox.append(criteriaElem);
                $('textarea', criteriaElem).uniform();
            });
        }
        $('textarea[name^=orig]', translateForm).each(function(){
            if(!$(this).val().empty()) {
                var transElemId = $(this).attr('id').replace('orig', 'trans');
                $('#' + transElemId, translateForm).addClass('validate[required]');
            }
        });
    } else { // translate to (init with the existed translated text if any)
        $('textarea[id^=trans], input[id^=trans]', translateForm).val('');
        //if(data.id > 0) {
        $('textarea#transQuestion', translateForm).val(data.question);
        $('textarea#transTip', translateForm).val(data.tip);
        $('input#indicatorIntlId', translateForm).val(data.id);
        if(data.answerChoices) {
            $.each(data.answerChoices, function(i, elem){
                var seqNum = (i+1);
                $('input#atcChoiceIntlId' + seqNum, translateForm).val(elem.id);
                $('textarea#transLabel' + seqNum, translateForm).val(elem.label);
                $('textarea#transCriteria' + seqNum, translateForm).val(elem.criteria);
                $('textarea#atcIntlId' + seqNum, translateForm).val(elem.id);
            });
        }
        //}
    }
}

function doOpenIndicatorEditForm(contextPath, id, viewonly){
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: contextPath + '/lib/indicator-lib!get',
        data: {
            id:id,
            langId: 0
        },
        success: function(resp){
            if(resp.ret == 0) {
                if (viewonly) {
                    // get here via the VIEW button - no need to show ban reasons, if any
                    resp.desc = null;
                } else if (!resp.data.writable) {
                    // got here via the EDIT button, but there is ban on editing
                    viewonly = true;
                }
                
                initIndicatorForm(resp.data, resp.desc, viewonly);
                if (viewonly) {
                    $('#indicatorFormPopup').dialog('option', 'title', $.i18n.message('cp.title.view_indicator'));
                } else {
                    $('#indicatorFormPopup').dialog('option', 'title', $.i18n.message('cp.title.edit_indicator'));
                }
                $('#indicatorFormPopup').dialog('open');
            }
        }
    });
    
                        
    return false;
}


function doIndicatorAjaxRequest(contextPath, handler, reqData, success, error) {
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: contextPath + '/lib/indicator-lib!'+handler,
        data: reqData,
        success: function(resp){
            if(resp.ret == 0) {
                handleReports(resp.data);
                $("#indicator-flexgrid").flexReload({
                    dataType: 'json'
                });
            }
        }
    });
    return false;
}

function doAddIndicator(contextPath, visibility){
    //window.location.href=contextPath + '/lib/indicator-lib!addpage?visibility='+visibility;
    var dhxWins = new dhtmlXWindows();
    //dhxWins.setSkin("dhx_web");
    dhxWins.enableAutoViewport(false);
    dhxWins.attachViewportTo("indaba");
    dhxWins.setImagePath("../dhtmlx/imgs/");
    var w1 = dhxWins.createWindow("w1", 0, 0, 720, 580);
    w1.setText("Confirmation");
    w1.allowResize();
    w1.button("minmax1").hide();
    w1.button("park").hide();
    w1.center();
    w1.setModal(true);
    w1.attachHTMLString($('#indicatorFormPopup').html());
    w1.keepInViewport(true);
    //w1.setDimension($('.popup-box').width, $('.popup-box').height());
    $(".popup-box a.close").click(function(){
        w1.close();
        return false;
    });
    
    $('input,select,button,textarea', $('#add-indicator')).uniform();
    $('.question-mark', $('#add-indicator')).tooltip({
        // place tooltip on the right edge
        position: "center right",
        // a little tweaking of the position
        offset: [-2, 10],
        // use the built-in fadeIn/fadeOut effect
        effect: "fade",
        // custom opacity setting
        opacity: 0.7
    });
    $('input:radio[name=answerType]', $('#add-indicator')).click(function(){
        if($(this).val() == 1 || $(this).val() == 2) {
            $('#indicator-def').show();
        }
        else{
            $('#indicator-def').hide();
        }
    });
    return false;
}

function handleReports(reportList) {
    if(reportList) {
        var success = 0;
        var reports =('<ul class="reports">');
        $.each(reportList, function(index, val){
            var cssClass = "fail";
            if(val.errCode == 0) {
                success++;
                cssClass = "success";
            }
            reports +=('<li class="'+cssClass+'">' + val.errMsg + '</li>');
        });
        reports += '</ul>';
        var result = '<div style="font-size: 14px; margin-bottom: 5px;">' + $.i18n.message('cp.text.operation_result', ['<span style="font-weight: bold">' + success+'</span>', '<span style="font-weight: bold">' + (reportList.length-success) + '</span>']) + '</div>';
        ocsInfo(result+reports, $.i18n.message('cp.title.result'));
    }
}