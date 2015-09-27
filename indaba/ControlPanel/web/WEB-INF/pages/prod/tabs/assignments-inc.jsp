<%--
    Document   : Product Tab - 'Assignments'
    Created on : May 20, 2012, 11:20:21 AM
    Author     : Jeff Jiang
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<div id="productAssignmentsTab">
    <div class="filter">
        <form id="assignment-filter-form" action="${contextPath}/prod/task-assignment!find?prodId=${prodId}" method="POST">
            <fieldset>
                <legend><img width="16px" src="${contextPath}/resources/images/search.png"><span><indaba:msg key="cp.label.task_assignment_filter"  /></span></legend>
                <input type="hidden" name="prodId" id="filterProdId" value="${prodId}" />
                <div id="filterTask" class="field" style="margin-top: 0px;">
                    <label><indaba:msg key="cp.label.task"  /></label>
                    <select name="taskId" id="filterTaskId" style="width: 200px; height: 15px;">
                        <option value="0"><indaba:msg key="cp.text.all"  /></option>
                        <c:forEach items="${tasks}" var="task">
                            <option value="${task.id}">${task.taskName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div id="filterTarget" class="field" style="margin-top: 0px;">
                    <label><indaba:msg key="cp.label.target"  /></label>
                    <select name="targetId" id="filterTargetId" style="width: 200px; height: 15px;" >
                        <option value="0"><indaba:msg key="cp.text.all"  /></option>
                        <c:forEach items="${targets}" var="target">
                            <option value="${target.id}">${target.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="field">
                    <a id="submitAssignmentFilter" class="button">&nbsp;&nbsp;&nbsp;<indaba:msg key="cp.btn.find"  />&nbsp;&nbsp;&nbsp;</a>
                    <a id="resetAssignmentFilter" class="button reset"><indaba:msg key="cp.btn.reset"  /></a>
                </div>
            </fieldset>
        </form>
    </div>
    <div id="assignmentsFlexGrid" style="margin: 10px">
        <div id="assignments-flexgrid"></div>
    </div>
</div>
<div id="assignmentFormPopup" title="<indaba:msg key='cp.label.edit_assignment'/>" class="hidden">
    <div id="assignmentFormBox" class="popup-container" style="width: 650px; max-width: 650px">
        <form id="assignmentForm" action="#" method="POST" >
            <fieldset id="assignmentFs">
                <input type="hidden" name="modeId" id="modeId" value="ADD" />
                <input type="hidden" name="prodId" id="prodId" value="${prodId}" />
                <input type="hidden" name="projId" id="projId" value="${projId}" />
                <input type="hidden" name="assignmentId" id="assignmentId" value="-1" />
                <dl>
                    <dt><label for="taskId"><indaba:msg key='cp.label.task'/></label></dt>
                    <dd>
                        <select id="taskId" name="taskId" data-placeholder="<indaba:msg key='cp.text.choose_task'/>" class="long-input validate[required]">
                            <option value=""></option>
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt><label for="targetId"><indaba:msg key='cp.label.target'/></label></dt>
                    <dd>
                        <select id="targetId" name="targetId" data-placeholder="<indaba:msg key='cp.text.choose_target'/>" class="long-input validate[required]">
                            <option value=""></option>
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt><label for="userId"><indaba:msg key='cp.label.user'/></label></dt>
                    <dd>
                        <select id="userId" name="userId" data-placeholder="<indaba:msg key='cp.text.choose_user'/>" class="long-input validate[required]">
                            <option value=""></option>
                        </select>
                    </dd>
                </dl>
            </fieldset>
        </form>
    </div>
</div>
<script type="text/javascript" charset="utf-8">
    var assignmentTabInitialized = false;
    $(function(){
        if(!assignmentTabInitialized) {
            assignmentTabInitialized = true;
            $('#assignment-filter-form').jqTransform();
            $('#submitAssignmentFilter').click(function(){
                $("#assignments-flexgrid").flexReload({newp: 1,dataType: 'json'});
                return false;
            });
            $('#resetAssignmentFilter').click(function(){
                $('#assignment-filter-form')[0].reset();
                $("#assignments-flexgrid").flexReload({newp: 1,dataType: 'json'});
                return false;
            });
            $("#assignments-flexgrid").flexigrid({
                autoload: false,
                url: '${contextPath}/prod/task-assignment!find',
                method: 'POST',
                dataType: 'json',
                colModel : [
                    {display: 'ID', name : 'id', width : 0, sortable : false, hide: true},
                    {display: '<indaba:msg key="cp.ch.task"/>', name : 'taskName', width : 191, sortable : true},
                    {display: '<indaba:msg key="cp.ch.target"/>', name : 'targetName', width : 120, sortable : true},
                    {display: '<indaba:msg key="cp.ch.user"/>', name : 'assignedUsername', width : 150, sortable : false},
                    {display: '<indaba:msg key="cp.ch.due_time"/>', name : 'duetimeDisplay', width : 140, sortable : true},
                    {display: '<indaba:msg key="cp.ch.status"/>', name : 'statusDisplay', width : 90, sortable : false},
                    {display: '<indaba:msg key="cp.ch.actions"/>', name : 'actions', width : 100, sortable : false}
                ],
                preProcess: function(data) {
                    $.each(data.rows, function(index, elem){
                        var prodId = data.prodId;
                        var assignmentId = elem.id;
                        // Edit
                        var actions = '<a class="link" href="javascript:void(0)" onclick="return openAssignmentEditForm( \'edit\','+assignmentId+');"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.edit"  /></a>';
                        // Delete
                        actions += '<a class="link" href="" onclick="return doDeleteAssignment(' +prodId + ',' + assignmentId + ');"><img height="14px" src="${contextPath}/resources/images/delete.png"><indaba:msg key="cp.btn.delete"  /></a>';
                        elem.actions = actions;
                    });
                    return data;
                },
                buttons : [
                    {name: '<indaba:msg key="cp.btn.add"  />', bclass: 'add', bimage: '${contextPath}/resources/images/add.png', attrs:[{name:'rel', value: '#'}], onpress : function(){
                            openAssignmentEditForm('add', -1);
                            return false;
                        }
                    }
                ],
                warnClass: 'warn',
                resizable: false,
                sortname: "PRODNAME",
                sortorder: "asc",
                usepager: true,
                title: '<indaba:msg key="cp.title.assignments"/>',
                useRp: true,
                rp: 15,
                showToggleBtn: false,
                width: 865,
                height: '100%',
                //page: 1,
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
                    $('select, input', $('#assignment-filter-form')).each(function(){
                        p[p.length] = {name: $(this).attr("name"), value: $(this).val()};
                    });
                    this.params = p;
                    return true;
                }
            });
            var assignmentForm = $('form#assignmentForm');
            // Dialog
            $('#assignmentFormPopup').dialog({
                autoOpen: false,
                width: 722,
                resizable: false,
                modal: true,
                open: function(){
                    // resetForm(assignmentForm);
                    assignmentForm.validationEngine({prettySelect: true,useSuffix: "_chzn",validationEventTrigger:"change"});
                },
                beforeClose: function(){
                    resetForm(assignmentForm);
                    $('#assignmentId', assignmentForm).val(-1);
                    assignmentForm.validationEngine('hideAll');
                },
                close: function(){
                },
                buttons: {
                    "Save": function() {
                        if(!assignmentForm.validationEngine('validate')) {
                            return false;
                        }
                        var reqData = extractFormDataToJson(assignmentForm);
                        // var loading=new ol.loading({id:"indicatorFormPopup", loadingText:"Waiting..."});
                        // loading.show();
                        var dlg = $(this);
                        var method = 'add' == $('#modeId').val() ? 'create' : 'save'
                        $.ajax({type: 'POST',dataType: 'json',url:'${contextPath}/prod/task-assignment!' + method,data: reqData,
                            success: function(data){
                                // loading.hide();
                                if(data.ret != 0) {
                                    ocsError(data.desc);
                                } else {
                                    dlg.dialog("close");
                                    $("#assignments-flexgrid").flexReload({newp:1, dataType:'json'});
                                    ocsSuccess($.i18n.message('cp.text.success_save_assignment'));
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
            assignmentForm.find('select#taskId').change(function(){
                var userElem = assignmentForm.find('select#userId');
                if (userElem) {
                    userElem.attr("disabled", "disabled").trigger("liszt:updated");
                    setOptions(userElem);
                }
                
                $.ajax({type: 'POST', dataType: 'json',url: '${contextPath}/prod/task-assignment!updateUserOptionsByTaskId',
                    data:{taskId: $(this).val(), projId: ${projId}},
                    success: function(resp){
                        if(resp.ret == 0 && resp.data) {
                            setOptions(userElem, resp.data.users, {id: 'userId', attrs: ['userName']});
                        }
                        userElem.removeAttr("disabled").trigger("liszt:updated");
                    },
                    complete: function(){
                        userElem.removeAttr("disabled").trigger("liszt:updated");
                    },
                    error: function(){
                        userElem.removeAttr("disabled").trigger("liszt:updated");
                    }
                });
            });
        }
    });
    function initAssignmentForm(options) {
        var assignmentForm = $('#assignmentFormBox #assignmentForm');
        if(!options) {
            return;
        }
        $('#projId', assignmentForm).val(options.projId);
        $('#prodId', assignmentForm).val(options.prodId);
    }
    function loadAssignmentsFlexGrid(prodId) {
        
    }
    function openAssignmentEditForm(action, assignmentId) {
        $.ajax({type: 'POST', dataType: 'json',url: '${contextPath}/prod/task-assignment!get',_loading: {show:true,id:"productPopup"},
            data: {
                projId: $('form#assignmentForm #projId').val(),
                prodId: $('form#assignmentForm #prodId').val(),
                assignmentId: assignmentId
            },
            success: function(resp){
                if(resp.ret == 0) {
                    initAssignmentFormWithData(resp.data);
                    $('#modeId').val(action);
                    if ('add' == action) {
                        $('#assignmentForm #targetId').removeAttr('disabled').trigger("liszt:updated");
                        $('#assignmentForm #taskId').removeAttr('disabled').trigger("liszt:updated");
                    }
                    else {
                        $('#assignmentForm #targetId').attr('disabled', 'disabled').trigger("liszt:updated");
                        $('#assignmentForm #taskId').attr('disabled', 'disabled').trigger("liszt:updated");
                    }
                    var title = ('add' == action) ? $.i18n.message('cp.title.add_task_assignment'):$.i18n.message('cp.title.edit_task_assignment');
                    $('#assignmentFormPopup').dialog('option', 'title', title);
                    $('#assignmentFormPopup').dialog('open');
                } else {
                    ocsError(resp.desc);
                }
            }
        });
        return false;
    }
    function doDeleteAssignment(prodId, assignmentId) {
        $.ajax({
            type: 'POST',dataType: 'json',url: '${contextPath}/prod/task-assignment!deleteable', data: { assignmentId: assignmentId },
            success: function(resp) {
                if (resp.ret == 0) {
                    ocsConfirm($.i18n.message('cp.text.confirm_delete_task_assignment'), $.i18n.message('cp.title.confirm'), function(confirmed){
                        if(confirmed) {
                            $.ajax({type: 'POST',dataType: 'json',url: '${contextPath}/prod/task-assignment!delete', data: { assignmentId: assignmentId },
                                success: function(resp){
                                    if(resp.ret == 0) {
                                        $("#assignments-flexgrid").flexReload({});
                                    } else {
                                        ocsError(resp.desc);
                                    }
                                }
                            });
                        }
                    });
                }
                else {
                    ocsError(resp.desc);
                }
            }
        });

        return false;
    }
    function initAssignmentFormWithData(data) {
        var assignmentForm = $('#assignmentFormBox #assignmentForm');
        if(!data) {
            return;
        }
        $('#projId', assignmentForm).val(data.projId);
        $('#prodId', assignmentForm).val(data.prodId);
        // init selection list
        setOptions($('select#taskId', assignmentForm), data.tasks, {attrs: ['taskName']});
        setOptions($('select#targetId', assignmentForm), data.targets);
        setOptions($('select#userId', assignmentForm), data.users, {id: 'userId', attrs: ['userName']});
        var assignment = data.assignment;
        if(assignment) {// init selection value
            $('#assignmentId', assignmentForm).val(assignment.id);
            selectChosenOption($('select#taskId', assignmentForm), assignment.taskId);
            selectChosenOption($('select#targetId', assignmentForm), assignment.targetId);
            selectChosenOption($('select#userId', assignmentForm), assignment.assignedUserId);
        }
    }
    
</script>