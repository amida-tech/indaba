
function initSurveyConfigToolbar(visibility, contextPath){
    var surveyConfigToolbarData = {
        parent: "surveyConfigToolbar",
        icon_path: contextPath + "/resources/images/",
        items: [
        {
            type: "buttonTwoState",
            id: "pubSurveyConfig",
            text: $.i18n.message('cp.tab2.pub_survey_config'),
            img: "pub_ext_lib.png"
        },
        {
            type: "separator",
            id: "sep1"
        },
        {
            type: "buttonTwoState",
            id: "privSurveyConfig",
            text: $.i18n.message('cp.tab2.priv_survey_config'),
            img: "private_lib.png"
        }
        ]
    };

    var toolbar = new dhtmlXToolbarObject(surveyConfigToolbarData, "dhx_blue");

    fixDHTMLXTabbarHeight("content");
    if(visibility == '1') {
        toolbar.setItemState("pubSurveyConfig", true);
    } else if(visibility == '2') {
        toolbar.setItemState("privSurveyConfig", true);
    } else{
        toolbar.setItemState("pubSurveyConfig", true);
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
            var toVisibility = 1;
            if("pubSurveyConfig" == id) {
                toVisibility = 1;
            } else if("privSurveyConfig" == id) {
                toVisibility = 2;
            }
            toolbar.setItemState(id, true);
            toolbar.setItemState(pressedBar, false);
            window.location.href= contextPath + "/surveyconf/survey-config?visibility=" + toVisibility;
        }
        return false;
    });
}

function initEmptyToolbar(contextPath){
    var emptyToolbarData = {
        parent: "emptyToolbar",
        icon_path: contextPath + "/resources/images/",
        items: [
    ]
    };
    var toolbar = new dhtmlXToolbarObject(emptyToolbarData, "dhx_blue");
    fixDHTMLXTabbarHeight("content");
}

function checkSelected(flexgridElem) {
    var selected = $('tbody tr.trSelected', flexgridElem).length;
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

function getSelectedIndicatorIds(flexgridElem) {
    var idList = [];
    $('tbody tr.trSelected', flexgridElem).each(function(){
        idList[idList.length] = $('td[abbr=indicatorId]>div', $(this)).text();
    });
    return idList;
}

function doSelectIndicators(contextPath) {
    var elem = $('#indicatorsFlexGrid div.flexigrid div.tDiv2 a.fbutton span.select');
    var rowElems = $('#indicators-flexgrid tbody tr');
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

function doAddGroupIndicators(contextPath, flexgridElem, sconfId) {
    if(!checkSelected(flexgridElem)) {
        return false;
    }
    var idList = getSelectedIndicatorIds(flexgridElem);
    $.ajax({
        url: contextPath+ '/surveyconf/survey-config!addIndicators',
        type: 'POST',
        dataType: 'json',
        data: {
            sconfId: sconfId,
            indicatorIds: idList
        },
        success: function(resp){
            if(resp.ret != 0) {
                ocsError(resp.desc);
            } else {
                ocsSuccess($.i18n.message('cp.text.success_add_scindicator'));
                checkSelected(flexgridElem);
                flexgridElem.flexReload({
                    newp: 1,
                    dataType: 'json'
                });
            }
        },
        error: function(data) {
            defaultAjaxErrorHanlde(data);
        }
    });
    return false;
}

function doDeleteGroupIndicators(contextPath, flexgridElem, sconfId, indicatorId) {
    ocsConfirm($.i18n.message('cp.text.confirm_delete_sc_indicator'),$.i18n.message('cp.title.confirm'), function(choice){
        if (choice){
            $.ajax({
                url: contextPath+ '/surveyconf/survey-config!deleteIndicators',
                type: 'POST',
                dataType: 'json',
                data: {
                    'sconfId': sconfId,
                    'indicatorIds[]': [indicatorId]
                },
                success: function(resp){
                    if(resp.ret != 0) {
                        ocsError(resp.desc);
                    } else {
                        // ocsSuccess($.i18n.message('cp.text.success_del_scindicator'));
                        flexgridElem.flexReload({
                            newp: 1,
                            dataType: 'json'
                        });
                    }
                },
                error: function(data) {
                    defaultAjaxErrorHanlde(data);
                }
            });
        }
    });
    return false;
}

function doDeleteSurveyConfig(contextPath, sconfId, sconfName) {
    ocsConfirm($.i18n.message('cp.text.confirm_delete_survey_config'),$.i18n.message('cp.title.confirm'), function(choice){
        if (choice){
            $.ajax({
                url: contextPath+ '/surveyconf/survey-config!delete',
                type: 'POST',
                dataType: 'json',
                data: {
                    sconfId:sconfId
                },
                success: function(resp){
                    if(resp.ret != 0) {
                        ocsError(resp.desc);
                    } else {
                        $("#surveyConfig-flexgrid").flexReload({
                            newp: 1,
                            dataType: 'json'
                        });
                    }
                },
                error: function(data) {
                    defaultAjaxErrorHanlde(data);
                }
            });
        }
    });
}

function doCloneSurveyConfig(contextPath, srcScId, srcScName){
    $('#srcSconfId').val(srcScId);
    $('#scCloneFormPopup').dialog('option', 'title', $.i18n.message('cp.title.clone_survey_config'));
    $('#scCloneFormPopup').dialog('open');
    $('#scCloneFormPopup dd.loading').show();
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: contextPath + '/surveyconf/survey-config!getOrganizations',
        data: {
            sconfId:srcScId
        },
        success: function(data){
            $('#scCloneFormPopup dd.loading').hide();
            if(data) {
                var orgElem = $('#scCloneFormPopup').find('select#organization');
                $.each(data, function(index, elem){
                    orgElem.append('<option value="'+elem.id+'">'+elem.name+'</option>');
                });
                orgElem.trigger("liszt:updated");
            }
        },
        error: function(){
            $('#scCloneFormPopup dd.loading').hide();
        }
    });

    return false;
}

function getNodeId(node, k) {
    var key = k ? k : 'id';
    return node[key].substr(1, node[key].length);
}
function initSurveyCategoryForm(catForm, data, nodeTId) {
    resetForm(catForm);
    $('input[name=action]', catForm).val('edit');
    $('input[name=nodeTId]', catForm).val(nodeTId);
    $('input[name=catId]', catForm).val(data.id);
    $('input[name=sconfId]', catForm).val(data.surveyConfigId);
    $('input[name=pCatId]', catForm).val(data.parentCategoryId);
    $('input[name=weight]', catForm).val(data.weight);
    $('input[name=name]', catForm).val(data.name);
    $('input[name=label]', catForm).val(data.label);
    $('textarea[name=title]', catForm).val(data.title);
    $('textarea[name=description]', catForm).val(data.description);
}
function initSurveyQuestionForm(qstnForm, data, nodeTId) {
    resetForm(qstnForm);
    var question = data.question;
    $('input[name=action]', qstnForm).val('edit');
    $('input[name=nodeTId]', qstnForm).val(nodeTId);
    $('input[name=qstnId]', qstnForm).val(question.id);
    $('input[name=sconfId]', qstnForm).val(question.surveyConfigId);
    $('input[name=pCatId]', qstnForm).val(question.surveyCategoryId);
    $('input[name=weight]', qstnForm).val(question.weight);
    $('input[name=name]', qstnForm).val(question.name);
    $('input[name=label]', qstnForm).val(question.publicName);
    var indicatorElem = $('select[name=indicatorId]', qstnForm);
    setOptions(indicatorElem, data.indicators, {
        sep:' - ',
        attrs:['name', 'question']
    }, {
        _attrs:['state']
    });
    selectChosenOptions(indicatorElem, [question.surveyIndicatorId]);
    if(question.surveyIndicatorId && question.surveyIndicatorId > 0) {
        var lib = indicatorElem.find('option[value='+question.surveyIndicatorId+']').attr('state');
        checkUniformChoiceV2(qstnForm, 'library', lib);
    }
    else {
        checkUniformChoiceV2(qstnForm, 'library', '0');
    }
}

function doDeleteTreeNode(contextPath, node, isParent) {
    var api = isParent?'deleteSurveyCategory':'deleteSurveyQuestion';
    var data = isParent?{
        catId:  getNodeId(node),
        sconfId:  $('form#scForm input#sconfId').val()
    }:{
        qstnId:  getNodeId(node),
        sconfId:  $('form#scForm input#sconfId').val()
    }
    $.ajax({
        url: contextPath+ '/surveyconf/survey-config!' + api,
        type: 'POST',
        dataType: 'json',
        data: data,
        success: function(resp){
            if(resp.ret != 0) {
                ocsError(resp.desc);
            } else {
                zTree.removeNode(node);
            }
        },
        error: function(data) {
            defaultAjaxErrorHanlde(data);
        }
    });
}
function doEditSurveyData(contextPath, inlineFormBox, node) {
    var oldClass = enableLoading(node);
    var api, data;
    if(inlineFormBox.attr('id') == 'qstnFormBox'){
        api = 'getSurveyQuestion';
        data = {
            qstnId:  getNodeId(node),
            sconfId: $('#scForm input#sconfId').val()
        };
    } else{
        api='getSurveyCategory';
        data = {
            catId:  getNodeId(node)
        };
    }
    $.ajax({
        url: contextPath+ '/surveyconf/survey-config!'+api,
        type: 'POST',
        dataType: 'json',
        data: data,
        success: function(resp){
            disableLoadingAndRecover(node, oldClass);
            if(resp.ret != 0) {
                ocsError(resp.desc);
            } else {
                if((inlineFormBox.attr('id') == 'qstnFormBox')) {
                    initSurveyQuestionForm(inlineFormBox.find('form'), resp.data, node.tId);
                } else {
                    initSurveyCategoryForm(inlineFormBox.find('form'), resp.data, node.tId);
                }
                showInlineForm(inlineFormBox, node);
            }
        },
        error: function(data) {
            disableLoadingAndRecover(node, oldClass);
            defaultAjaxErrorHanlde(data);
        }
    });
}
function doAddSurveyCategory(inlineFormBox, pCatNode) {
    var newTreeNode = createTreeNode(pCatNode, $.i18n.message('cp.text.new_survey_category'), true);
    $('input[name=nodeTId]', inlineFormBox).val(newTreeNode.tId);
    $('input[name=name]', inlineFormBox).val($.i18n.message('cp.text.new_survey_category'));
    $('input[name=action]', inlineFormBox.find('form')).val('create');
    $('input[name=sconfId]', inlineFormBox).val($('form#scForm input#sconfId').val());
    $('input[name=pCatId]', inlineFormBox).val(getNodeId(pCatNode));
    $('input[name=name]', inlineFormBox).focus();
    showInlineForm(inlineFormBox, newTreeNode);
}

function doAddSurveyQuestion(contextPath, inlineFormBox, newTreeNode, pCatNode) {
    var oldClass = enableLoading(newTreeNode);
    resetForm(qstnForm);
    $.ajax({
        url: contextPath+ '/surveyconf/survey-config!getAllAvailableScIndicators',
        type: 'POST',
        dataType: 'json',
        data: {
            sconfId: $('#scForm input#sconfId').val()
        },
        success: function(resp){
            disableLoadingAndRecover(newTreeNode, oldClass);
            if(resp.ret != 0) {
                ocsError(resp.desc);
            } else {
                var qstnForm = inlineFormBox.find('form');
                $('input[name=nodeTId]', qstnForm).val(newTreeNode.tId);
                $('input[name=action]', qstnForm).val('create');
                $('input[name=name]', qstnForm).val($.i18n.message('cp.text.new_survey_question'));
                $('input[name=sconfId]', qstnForm).val($('form#scForm input#sconfId').val());
                $('input[name=pCatId]', qstnForm).val(getNodeId(pCatNode));
                var indicatorElem = $('select[name=indicatorId]', qstnForm);
                setOptions(indicatorElem, resp.data, {
                    sep:' - ',
                    attrs:['name', 'question']
                });
                checkUniformChoiceV2(qstnForm, 'library', '0');
                $("select", qstnForm).val('').chosen().trigger("liszt:updated");
                var parent = $("select", qstnForm).parent().find('div#' + $("select", qstnForm).attr('id') + '_chzn');
                parent.find('a span').width(parent.width() - 30);
                showInlineForm(inlineFormBox, newTreeNode);
            }
        },
        error: function(data) {
            disableLoadingAndRecover(newTreeNode, oldClass);
            defaultAjaxErrorHanlde(data);
        }
    });
}

function showInlineForm(inlineFormBox, treeNode) {
    inlineFormBox.insertAfter($('#' + treeNode.tId + '_a'));
    inlineFormBox.show();
    setTimeout(function(){
        fixDHTMLXTabbarHeight("content");
    }, 500);
}

function initSurveyConfigTree(contextPath, sconfId, tree, data) {
    tree.loadJSON(contextPath+ '/surveyconf/survey-config!getTree?sconfId='+sconfId);
/*
    $.ajax({
        url:contextPath+ '/surveyconf/survey-config!getTree',
        type: 'POST', 
        dataType: 'json',
        data: {
            sconfId: sconfId
        },
        success: function(data){
            //$.fn.zTree.init($("#scTree"), settings, data);
            tree.loadJSONObject(data);
        }
    });
    */
}

function updateTreeNode(selectedNode, id, nodeName) {
    selectedNode.id = 'c' + id;
    selectedNode.name = nodeName;
    zTree.updateNode(selectedNode);
}

function createTreeNode(selectedNode, nodeName, isParent) {
    var newTreeNode = {
        name: nodeName,
        isParent: isParent,
        dropInner: isParent
    };
    var addedNodes;
    if (zTree.getSelectedNodes()[0]) {
        addedNodes = zTree.addNodes(selectedNode, newTreeNode);
        zTree.cancelSelectedNode(selectedNode);
        zTree.selectNode(addedNodes[0]);
    } else {
        addedNodes = zTree.addNodes(null, newTreeNode);
    }
    return addedNodes[0];
}

function doSaveSurveyData(contextPath, inlineFormBox) {
    var loading=new ol.loading({
        id:inlineFormBox.find('form').attr('id'),
        loadingText:$.i18n.message('cp.text.waiting')
    });
    var api = (inlineFormBox.attr('id') == 'qstnFormBox')?'saveSurveyQuestion':'saveSurveyCategory';
    var form = $(inlineFormBox).find('form');
    if(!form.validationEngine('validate')) {
        return false;
    }
    var data = extractFormDataToJson(form);
    $.ajax({
        url:contextPath+'/surveyconf/survey-config!' + api,
        type: 'POST',
        dataType: 'json',
        data: data,
        success: function(resp){
            loading.hide();
            if(resp.ret != 0) {
                ocsError(resp.desc);
            } else {
                var selectedNode = zTree.getNodeByTId($('input[name=nodeTId]', form).val());
                var nodeName = (inlineFormBox.attr('id') == 'qstnFormBox')?(resp.data.publicName + '. ' + resp.data.text):(resp.data.label + '. ' + resp.data.title);
                updateTreeNode(selectedNode, resp.data.id, nodeName);
                closeInlineFormBox(inlineFormBox);
            }
        },
        error: function(data) {
            loading.hide();
            defaultAjaxErrorHanlde(data);
        }
    });
    return false;
}
function enableLoading(selectedNode){
    var icon = $('#'+selectedNode.tId+'_ico');
    var oldClass = icon.attr('class');
    if(icon.hasClass('ico_docu')){
        oldClass='ico_docu';
    } else if(icon.hasClass('ico_open')){
        oldClass='ico_open';
    } else  if(icon.hasClass('ico_close')){
        oldClass='ico_close';
    }
    icon.removeClass(oldClass);
    icon.removeClass('button');
    icon.addClass('loading');
    return oldClass;
}

function disableLoadingAndRecover(selectedNode, oldClass){
    var icon = $('#'+selectedNode.tId+'_ico');
    icon.addClass('button');
    icon.addClass(oldClass);
}

function initialize(contextPath, catFormBox, qstnFormBox) {
    resetForm(catFormBox);
    resetForm(qstnFormBox);
    catFormBox.find('form').validationEngine({
        prettySelect: true,
        useSuffix: "_chzn",
        validationEventTrigger:"change"
    });
    qstnFormBox.find('form').validationEngine({
        prettySelect: true,
        useSuffix: "_chzn",
        validationEventTrigger:"change"
    });
    $('li#m_del a').click(function() {
        hideRMenu();
        var nodes = zTree.getSelectedNodes();
        if (nodes && nodes.length>0) {
            var confirmMsg;
            if (nodes[0].children) {
                confirmMsg = $.i18n.message('cp.text.confirm_delete_survey_category');
            } else {
                confirmMsg = $.i18n.message('cp.text.confirm_delete_survey_question');
            }

            ocsConfirm(confirmMsg,"Confirm", function(choice){
                if (choice){
                    var nodes = zTree.getSelectedNodes();
                    if (nodes && nodes.length>0) {
                        doDeleteTreeNode(contextPath, nodes[0], nodes[0].isParent);
                    }
                }
            });
        }
        return false;
    });
    $('li#m_edit a').click(function() {
        hideRMenu();
        var nodes = zTree.getSelectedNodes();
        if (nodes && nodes.length>0) {
            if(nodes[0].isParent) {
                doEditSurveyData(contextPath, catFormBox,  nodes[0]);
            } else {
                doEditSurveyData(contextPath, qstnFormBox,  nodes[0]);
            }
        } else {
            
        }
        return false;
    });
    $('li#m_add_qstn a').click(function() {
        hideRMenu();
        var nodes = zTree.getSelectedNodes();
        if(nodes && nodes.length > 0) {
            var newTreeNode = createTreeNode(nodes[0], $.i18n.message('cp.text.new_survey_question'), false);
            doAddSurveyQuestion(contextPath, qstnFormBox, newTreeNode, nodes[0]);
        }
        return false;
    });
    $('li#m_add_cat a').click(function() {
        hideRMenu();
        var nodes = zTree.getSelectedNodes();
        if(nodes && nodes.length > 0) {
            doAddSurveyCategory(catFormBox, nodes[0]);
        }
        return false;
    });
    $('a.save', catFormBox).click(function(){
        doSaveSurveyData(contextPath, catFormBox);
        return false;
    });
    $('a.cancel', catFormBox).click(function() {
        if($('form input[name=action]', catFormBox).val() == 'create') {
            var nodeTId = catFormBox.parent('li').attr('id');
            var treeNode = zTree.getNodeByTId(nodeTId);
            var parentNode = zTree.getNodeByTId(treeNode.parentTId);
            catFormBox.hide().appendTo('body');
            zTree.removeNode(treeNode);
            zTree.selectNode(parentNode);
            window.scrollTo($('#'+parentNode.tId).offset().left, $('#'+parentNode.tId).offset().top);
        }
        closeInlineFormBox(catFormBox);
    });
    $('a.save', qstnFormBox).click(function() {
        doSaveSurveyData(contextPath, qstnFormBox);
        return false;
    });
    $('a.cancel', qstnFormBox).click(function() {
        if($('form input[name=action]', qstnFormBox).val() == 'create') {
            var nodeTId = qstnFormBox.parent('li').attr('id');
            var treeNode = zTree.getNodeByTId(nodeTId);
            var parentNode = zTree.getNodeByTId(treeNode.parentTId);
            qstnFormBox.hide().appendTo('body');
            zTree.removeNode(treeNode);
            zTree.selectNode(parentNode);
            window.scrollTo($('#'+parentNode.tId).offset().left, $('#'+parentNode.tId).offset().top);
        }
        closeInlineFormBox(qstnFormBox);
    });
}

function closeInlineFormBox(formElemBox) {
    formElemBox.hide();
    var form = $(formElemBox).find('form');
    clearFormFields(form, true);
    form.validationEngine('hideAll');
    setTimeout(function(){
        fixDHTMLXTabbarHeight("content");
    }, 100);
}

function moveNode(contextPath, sconfId, origNodeId, origNodeIsCat, targetNodeId, targetParentId, targetNodeIsCat, moveType) {
    $.ajax({
        url:contextPath+'/surveyconf/survey-config!move',
        type: 'POST',
        dataType: 'json',
        data: {
            sconfId: sconfId,
            origNodeId:origNodeId,
            origNodeIsCat:origNodeIsCat,
            targetNodeId:targetNodeId,
            targetParentId: targetParentId,
            targetNodeIsCat:targetNodeIsCat,
            moveType:moveType
        },
        success: function(resp){
            if(resp.ret != 0) {
                ocsError(resp.desc);
            }
        },
        error: function(data) {
            defaultAjaxErrorHanlde(data);
        }
    });
}
