<%--
    Document   : Project Tab - 'Targets'
    Created on : May 20, 2012, 11:20:21 AM
    Author     : Jeff Jiang
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC"-//W3C//DTD XHTML 1.0 Transitional//EN">

<div id="projectTargetsTab">
    <div id="query-result">
        <div id="target-flexgrid"></div>
    </div>
    <div id="projectTargetFormPopup" title="<indaba:msg key='cp.label.target'/>" class="hidden">
        <div id="projectTargetFormBox" class="popup-container" style="width: 650px; max-width: 650px; padding-right: 20px;">
            <form id="projectTargetsForm" action="#" method="POST">  <fieldset class="block">
                    <h3 style="margin: 5px 10px; color: #666">Add targets to project: </h3>
                    <input type="hidden" name="projId" id="projId" value="${projId}"/>
                    <dl>
                        <dt><label for="targetIds"><indaba:msg key="cp.label.targets"  /></label></dt>
                        <dd>
                            <select name="targetIds" id="targetIds" multiple data-placeholder="<indaba:msg key='cp.text.add_target'  />" class="long-input validate[required]">
                                <option value=""></option>
                            </select>
                        </dd>
                    </dl>
                </fieldset>
                <div id="resultBox" style="background:#FFFFCC;" class="error-box hidden"></div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript" charset="utf-8">
    var targetsTabInitialized = false;
    $(function(){
        if(!targetsTabInitialized) {
            targetsTabInitialized = true;
            $("#target-flexgrid").flexigrid({
                // url: '${contextPath}/proj/project-target!find?projId=${projId}&visibility=${visibility}',
                method: 'POST',
                dataType: 'json',
                preProcess: function(data) {
                    if (data.canAddTargets != 'yes') {
                        $('.addtarget').parent().parent().hide();
                    }

                    $.each(data.rows, function(index, elem){
                        var targetId = elem.id;
                        var targetName = elem.name;
                        var actions = "";
                        actions += '<a class="link" href="javascript:void(0)" onclick="return doDeleteTarget(' + targetId + ',\''+targetName+'\');"><img height="14px" src="${contextPath}/resources/images/delete.png"><indaba:msg key="cp.btn.delete"/></a>';
                        elem.actions = actions;
                    });
                    return data;
                },
                colModel : [
                    {display: '<indaba:msg key="cp.ch.projectTargetId"/>', name : 'projectTargetId', width : 0, sortable : false, align: 'right', hide: true},
                    {display: '<indaba:msg key="cp.ch.id"/>', name : 'id', width : 0, sortable : false, align: 'right', hide: true},
                    {display: '<indaba:msg key="cp.ch.name"/>', name : 'name', width : 200, sortable : true, align: 'left'},
                    {display: '<indaba:msg key="cp.ch.shortname"/>', name : 'shortName', width : 200, sortable : true, align: 'left'},
                    {display: '<indaba:msg key="cp.ch.description"/>', name : 'description', width : 330, sortable : true, align: 'left'},
                    {display: '<indaba:msg key="cp.ch.actions"/>', name : 'actions', width : 83, sortable : false, align: 'left'}
                ],
                buttons : [
                    {name: '<indaba:msg key="cp.btn.add" />', bclass: 'add', bimage: '${contextPath}/resources/images/add.png',  onpress : function(){
                            doInitTargetDlg(true);
                            return false;
                        }
                    }
                ],
                searchitems : [
                    {display: '<indaba:msg key="cp.ch.name"/>', name : 'name', isdefault: true},
                    {display: '<indaba:msg key="cp.ch.shortname"/>', name : 'shortName'},
                    {display: '<indaba:msg key="cp.ch.description"/>', name : 'description'}
                ],
                warnClass: 'warn',
                resizable: false,
                sortname: "name",
                sortorder: "asc",
                usepager: true,
                title: '<indaba:msg key="cp.label.targets" />',
                useRp: true,
                rp: 30,
                rpOptions: [15, 30, 50, 100],
                showTableToggleBtn: false,
                showToggleBtn: false,
                width: "auto",
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
                    $('select, input', $('#target-filter-form')).each(function(){
                        p[p.length] = {name: $(this).attr("name"), value: $(this).val()};
                    });
                    this.params = p;
                    return true;
                }
            });
            var projectTargetsForm = $('#projectTargetsForm');
            var result = $('#resultBox', projectTargetsForm);
            projectTargetsForm.validationEngine({prettySelect : true,useSuffix: "_chzn",validationEventTrigger:"change"});
            $('#projectTargetFormPopup').dialog({
                autoOpen: false,
                width: 722,
                resizable: false,
                modal: true,
                open: function(){
                    result.text('');
                    result.hide();
                },
                beforeClose: function(){
                    var selectElem = $('#projectTargetsForm select#targetIds');
                    selectElem.empty();
                    selectElem.append('<option value="" selected="selected"></option>');
                    selectElem.trigger("liszt:updated");
                },
                close: function(){
                },
                buttons: {
                    "Save": function() {
                        if(!projectTargetsForm.validationEngine('validate')) {
                            return false;
                        }
                        var dlg = $(this);
                        var reqData = extractFormDataToJson(projectTargetsForm);
                        $.ajax({
                            url:'${contextPath}/proj/project-target!addTarget',
                            type: 'POST',
                            dataType: 'json',
                            data: reqData,
                            success: function(resp){
                                if(resp.ret != 0) {
                                    doInitTargetDlg();
                                    var data = resp.data;
                                    if(data.oks) {
                                        var success = 'Success to add '+data.oks.length+' target(s):';
                                        $.each(data.oks, function(index, val){
                                            success += (' "'+val.name+'"');
                                        });
                                        result.append('<h4 class="ok">'+success+'.</h4>');
                                    }
                                    var summary = $('<h4 class="err">Fail to add the follow target(s): </h4>');
                                    var errList = $('<ul class="err-list"></ul>');
                                    if(data.errors){
                                        $.each(data.errors, function(index, val){
                                            errList.append($('<li> - ' + val + '</li>'));
                                        });
                                    }
                                    result.append(summary);
                                    result.append(errList);
                                    result.show();
                                } else {
                                    ocsSuccess($.i18n.message('cp.text.target_added_to_poject'));
                                    loadTargetFlexGrid();
                                    dlg.dialog("close");
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
        }
    });
    function doInitTargetDlg(withOpen) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: '${contextPath}/proj/project-target!getAvailiabeTargets',
            data: {projId: ${projId}},
            success: function(resp){
                if(resp.ret == 0) {
                    if(resp.data.length > 0) {
                        var selectElem = $('#projectTargetsForm select#targetIds');
                        selectElem.empty();
                        selectElem.append('<option value="" selected="selected"></option>');
                        setOptions(selectElem, resp.data);
                        selectElem.trigger("liszt:updated");
                        if(withOpen) {
                            $('#projectTargetFormPopup').dialog('open');
                        }
                    }
                } else {
                    oscError($.i18n.message('cp.error.get_product_target'));
                }
            }
        });
    }
    function doDeleteTarget(targetId, targetName) {
        ocsConfirm($.i18n.message('cp.text.confirm_delete_product_target', [targetName]), $.i18n.message('cp.title.confirm'),
        function(confirmed){
            if(confirmed) {
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '${contextPath}/proj/project-target!deleteTarget',
                    data: {projId: ${projId},targetId: targetId},
                    success: function(data){
                        if(data.ret == 0) {
                            loadTargetFlexGrid();
                        } else {
                            ocsError(data.desc);
                        }
                    }
                });
            }
        });
    }

    function loadTargetFlexGrid() {
        $("#target-flexgrid").flexOptions({url: '${contextPath}/proj/project-target!find?projId=${projId}&visibility=${visibility}'}).flexReload({newp: 1,dataType: 'json'});
    }
</script>