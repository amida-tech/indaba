<%--
    Document   : Product Tab - 'Tasks'
    Created on : May 20, 2012, 11:20:21 AM
    Author     : Jeff Jiang
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<div id="productTasksTab">
    <div id="tasksFlexGrid" style="margin: 0px">
        <div id="tasks-flexgrid"></div>
    </div>
</div>
<div id="taskFormPopup" title="<indaba:msg key='cp.title.edit_task'/>" class="hidden">
    <div id="taskFormBox" class="popup-container" style="width: 650px; max-width: 650px">
        <form id="taskForm" action="#" method="POST" >
            <fieldset id="taskFs">
                <input type="hidden" name="projId" id="projId" value="-1" />
                <input type="hidden" name="prodId" id="prodId" value="-1" />
                <input type="hidden" name="taskId" id="taskId" value="-1" />
                <input type="hidden" name="type" id="type" value="-1" />
                <dl>
                    <dt><label for="taskName"><indaba:msg key='cp.title.task_name'/></label></dt>
                    <dd class="small-input"><input type="text" name="taskName" maxlength="100" id="taskName" class="long-input validate[required]"/></dd>
                </dl>
                <dl>
                    <dt><label for="description"><indaba:msg key='cp.label.description'/></label></dt>
                    <dd><textarea name="description" id="description" maxlength="255" class="long-input"></textarea></dd>
                </dl>
                <dl>
                    <dt><label for="goalId"><indaba:msg key='cp.label.goal'/></label></dt>
                    <dd>
                        <select id="goalId" name="goalId" data-placeholder="<indaba:msg key='cp.text.choose_goal'/>" class="long-input validate[required]">
                            <option value=""></option>
                            <c:forEach items="${goals}" var="goal">
                                <option value="${goal.id}">${goal.name}</option>
                            </c:forEach>
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt><label for="toolId"><indaba:msg key='cp.label.tool'/></label></dt>
                    <dd>
                        <select id="toolId" name="toolId" data-placeholder="<indaba:msg key='cp.text.choose_tool'/>" class="long-input validate[required]">
                            <option value=""></option>
                            <c:forEach items="${tools}" var="tool">
                                <option value="${tool.id}">${tool.label}</option>
                            </c:forEach>
                        </select>
                    </dd>
                </dl>
                <dl>
                    <dt><label for="instructions"><indaba:msg key='cp.label.instructions'/></label></dt>
                    <dd><textarea name="instructions" id="instructions" class="long-input"></textarea></dd>
                </dl>
                <dl>
                    <dt><label for="assignmentMethod"><indaba:msg key='cp.title.assignment_method'/></label></dt>
                    <dd class="radio">
                    <input type="radio" name="assignmentMethod" value="1"/><indaba:msg key='cp.label.manual'/>
                    <input type="radio" name="assignmentMethod" value="2"/><indaba:msg key='cp.label.queue'/>
                    </dd>
                </dl>
                <dl>
                    <dt><label for="roles"><indaba:msg key='cp.label.role'/></label></dt>
                    <dd>
                        <select id="roles" name="roles" data-placeholder="<indaba:msg key='cp.text.choose_task_role'/>" class="long-input validate[funcCall[checkRoles]]">
                            <option value=""></option>
                            <c:forEach items="${roles}" var="role">
                                <option value="${role.id}">${role.name}</option>
                            </c:forEach>
                        </select>
                        <h4 style="margin: 10px 0px; color: #444"><indaba:msg key="cp.text.tasks_selected"/></h4>
                        <table id="selectedRoles" class="selectedRoles" cellpadding="0" cellspacing="0" >
                            <thead>
                                <tr><th class="claim"><indaba:msg key="cp.title.claim"/></th><th class="role"><indaba:msg key="cp.label.role"/></th><th class="delete"><indaba:msg key="cp.btn.delete"/></th></tr>
                            </thead>
                            <tbody><tr class="nodata"><td colspan="3"><indaba:msg key="cp.text.no_task_data"/></td></tr></tbody>
                        </table>
                    </dd>
                </dl>
            </fieldset>
        </form>
    </div>
</div>
<script type="text/javascript" charset="utf-8">
    var tasksTabInitialized = false;
    $(function(){
        if(!tasksTabInitialized) {
            tasksTabInitialized = true;
            $("#tasks-flexgrid").flexigrid({
                autoload: false,
                //url: '${contextPath}/prod/task!find?prodId='+prodId,
                method: 'POST',
                dataType: 'json',
                colModel : [
                    {display: 'ID', name : 'id', width : 0, sortable : false, hide: true},
                    {display: '<indaba:msg key="cp.ch.name"/>', name : 'taskName', width : 300, sortable : true},
                    {display: '<indaba:msg key="cp.ch.state"/>', name : 'validity', width : 50, sortable : false},
                    {display: '<indaba:msg key="cp.ch.method"/>', name : 'assignMethod', width : 150, sortable : false},
                    {display: '<indaba:msg key="cp.ch.roles"/>', name : 'roleNames', width : 200, sortable : false},
                    {display: '<indaba:msg key="cp.ch.actions"/>', name : 'actions', width : 100, sortable : false}
                ],
                preProcess: function(data) {
                    $.each(data.rows, function(index, elem){
                        var taskId = elem.id;
                        var prodId = elem.productId;

                        // Edit
                        var actions = '<a class="link" href="javascript:void(0)" onclick="return openTaskEditForm('+taskId+');"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.edit"/></a>';
                        
                        // Delete
                        if (elem.sticky == 0) {
                            // only non-sticky tasks can be deleted
                            actions += '<a class="link" href="" onclick="return doDeleteTask(${prodMode},'+ prodId+ ','+ taskId +',\''+ elem.name +'\');"><img height="14px" src="${contextPath}/resources/images/delete.png"><indaba:msg key="cp.btn.delete"/></a>';
                        }
                        elem.actions = actions;

                        var icons;
                        switch(elem.validity) {
                            case 3:
                                // perfect
                                icons = '<img height="14px" width="14px" src="${contextPath}/resources/images/task3.gif" title="' + $.i18n.message('cp.text.task_well_defined') + '">';
                                break;
                            case 2:
                                // ok
                                icons = '<img height="14px" width="14px" src="${contextPath}/resources/images/task2.gif" title="' + $.i18n.message('cp.text.task_ok_defined') + '">';
                                break;
                            default:
                                // not good
                                icons = '<img height="14px" width="14px" src="${contextPath}/resources/images/task1.gif" title="' + $.i18n.message('cp.text.task_not_properly_defined') + '">';
                        }

                        switch(elem.userType) {
                            case 1:
                                // single user task
                                icons += '&nbsp;&nbsp;<img height="14px" width="14px" src="${contextPath}/resources/images/icon-user.png" title="' + $.i18n.message('cp.text.single_user_task') + '">';
                                break;
                            case 2:
                                // multi-user task
                                icons += '&nbsp;&nbsp;<img height="14px" width="14px" src="${contextPath}/resources/images/icon-team.png" title="' + $.i18n.message('cp.text.multi_user_task') + '">';
                                break;
                            default:
                                break;
                        }

                        elem.validity = icons;
                    });
                    return data;
                },
                buttons : [
                    {name: '<indaba:msg key="cp.btn.add"/>', bclass: 'add', bimage: '${contextPath}/resources/images/add.png', attrs:[{name:'rel', value: '#'}], onpress : function(){
                            $('#taskFormPopup').dialog('option', 'title', $.i18n.message('cp.title.add_task'));
                            $('#taskFormPopup').dialog('open');
                            return false;
                        }
                    }
                ],
                warnClass: 'warn',
                resizable: false,
                sortname: "PRODNAME",
                sortorder: "asc",
                usepager: true,
                title: 'Manage Tasks',
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
                    $('select, input', $('#project-filter-form')).each(function(){
                        p[p.length] = {name: $(this).attr("name"), value: $(this).val()};
                    });
                    this.params = p;
                    return true;
                }
            });
            var taskForm = $('form#taskForm');
            // Dialog
            $('#taskFormPopup').dialog({
                autoOpen: false,
                width: 722,
                resizable: false,
                modal: true,
                open: function(){
                    taskForm.validationEngine({prettySelect: true,useSuffix: "_chzn",validationEventTrigger:"change"});
                },
                beforeClose: function(){
                    resetForm(taskForm);
                    $('#taskId', taskForm).val(-1);
                    taskForm.validationEngine('hideAll');
                    $('#roles option').removeAttr('disabled');
                    $('#selectedRoles tr.item').remove();
                    $('select', taskForm).trigger("liszt:updated");
                },
                close: function(){
                },
                buttons: {
                    "Save": function() {
                        if(!taskForm.validationEngine('validate')) {
                            return false;
                        }
                        var reqData = extractFormDataToJson(taskForm);
                        var dlg = $(this);
                        $.ajax({type: 'POST',dataType: 'json',url:'${contextPath}/prod/task!save', data: $.extend(reqData, {roles: JSON.stringify(getRoles())}),
                            success: function(data){
                                if(data.ret != 0) {
                                    ocsError(data.desc);
                                } else {
                                    loadTasksFlexGrid(${prodId});
                                    ocsSuccess($.i18n.message('cp.text.success_save_task'));
                                    dlg.dialog("close");
                                }
                            },
                            error: function(data) {
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
            var selectBox = $('select#roles');
            selectBox.change(function(v){
                addRoleItem(selectBox.val());
            });
        }
    });
    function loadTasksFlexGrid(prodId) {
        $("#tasks-flexgrid").flexReload({url: '${contextPath}/prod/task!find?prodId='+prodId});
    }
    function initTaskForm(options) {
        var taskForm = $('#taskFormBox #taskForm');
        if(!options) {
            return;
        }

        $('#projId', taskForm).val(options.projId);
        $('#prodId', taskForm).val(options.prodId);

        // init selection list
        setOptions($('select#goalId', taskForm), options.goals);
        
        // setOptions($('select#toolId', taskForm), options.tools);
        // setOptions($('select#roles', taskForm), options.roles);
        
    }
   
    function doDeleteTask(prodMode, prodId, taskId) {
        var msg = $.i18n.message('cp.text.confirm_delete_task');

        if (prodMode != 1) {
            // not config
            msg = $.i18n.message('cp.text.confirm_delete_active_task');
        }

        ocsConfirm(msg, $.i18n.message('cp.title.confirm'), function(confirmed){
            if(confirmed) {
                $.ajax({type: 'POST',dataType: 'json',url: '${contextPath}/prod/task!delete',data: {taskId: taskId},
                    success: function(resp){
                        if(resp.ret == 0) {
                            loadTasksFlexGrid(prodId);
                        } else {
                            ocsError(resp.desc);
                        }
                    }
                });
            }
        });
        return false;
    }
    function openTaskEditForm(taskId) {
        $.ajax({type: 'POST',dataType: 'json',url: '${contextPath}/prod/task!get',data: {taskId: taskId},_loading: {show:true,id:"productPopup"},
            success: function(resp){
                if(resp.ret == 0) {
                    initTaskFormWithData(resp.data);                    
                    $('#taskFormPopup').dialog('option', 'title', $.i18n.message('cp.title.edit_task'));
                    $('#taskFormPopup').dialog('open');
                } else {
                    ocsError(resp.desc);
                }
            }
        });
        return false;
    }
    function initTaskFormWithData(data) {
        var taskForm = $('#taskFormBox #taskForm');
        if(!data) {
            return;
        }
        var task = data.task;
        $('#prodId', taskForm).val(task.productId);
        $('#taskId', taskForm).val(task.id);
        $('#type', taskForm).val(task.type);
        $('#taskName', taskForm).val(task.taskName);
        $('#description', taskForm).val(task.description);
        selectChosenOption($('select#goalId', taskForm), task.goalId);
        
        if (task.sticky) {
            // disable the goal selection list. The task's goal cannot be changed for sticky tasks.
            $('select#goalId', taskForm).attr("disabled", "disabled");
        } else {
            // enable goal selection list.
            $('select#goalId', taskForm).removeAttr("disabled");
        }
        $('select#goalId', taskForm).trigger("liszt:updated");
    
        selectChosenOption($('select#toolId', taskForm), task.toolId);
        $('#instructions', taskForm).val(task.instructions);
        checkUniformChoiceV2(taskForm, 'assignmentMethod', task.assignmentMethod);
        if(!data.taskRoles || data.taskRoles.length==0) {
            $('#selectedRoles tr.nodata').show();
        } else {
            $.each(data.taskRoles, function(i, item){
                var selectBox = $('select#roles');
                var option = $(('option[value=' + item.roleId + ']'),selectBox);
                option.attr('disabled', 'disabled');
                option.removeAttr('selected');
                addRoleItem(item.roleId, item.canClaim);
            });
        }
    }
    function checkRoles() {
        if($('#selectedRoles tbody tr.item').length == 0) {
            return '* This field is required';
        }
    }
    function addRoleItem(val, claim) {
        var selectBox = $('select#roles');
        var selectedOption = $(('option[value=' + val + ']'),selectBox);
        selectedOption.attr('disabled', 'disabled');
        selectedOption.removeAttr('selected')
        selectedOption.trigger("liszt:updated");
        var trElem = $('<tr rid="'+val+'" class="item"><td>&nbsp;<input '+((claim && claim==1)?'checked="checked"':'')+'type="checkbox" id="claim-'+val+'"></td><td>'+selectedOption.text()+'</td><td><a class="del" href="javascript:void(0)"><img width="12px" src="${contextPath}/resources/images/delete.png"/></a></td></tr>');
        $('input:checkbox', trElem).uniform();
        var selectedRoles = $('#selectedRoles');
        selectedRoles.append(trElem);
        $('tr.nodata', selectedRoles).hide();
        $('a.del', trElem).click(function(){
            $(this).parents('tr').remove();
            if($('tbody tr.item', selectedRoles).length == 0) {
                $('tr.nodata', selectedRoles).show();
            }
            selectedOption.removeAttr('disabled');
            selectedOption.trigger("liszt:updated");
        });
    }
    function getRoles() {
        var roles = [];
        $('#selectedRoles tbody tr.item').each(function(){
            var $this = $(this);
            var taskRole = {id: $this.attr('rid'), claim: ('checked' == $('input:checkbox', $this).attr('checked'))};
            roles[roles.length] =taskRole;
        });
        return roles;
    }
</script>