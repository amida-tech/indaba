/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function setChosenOptions(elem, options, select) {
    elem.empty();
    elem.append('<option value=""></option>');
    if (options) {
        $.each(options, function(i, item) {
            if (i == 0 && select) {
                elem.append('<option selected="selected" value="' + item.id + '">' + item.name + '</option>');
            } else {
                elem.append('<option value="' + item.id + '">' + item.name + '</option>');
            }
        });
        elem.trigger("liszt:updated");
    }
}
function extractFormDataToJson(formElem) {
    var data = {};
    $('input:text, input:hidden, input:radio:checked, textarea, select', formElem).each(function() {
        var name = $(this).attr('name');
        if (typeof name != 'undefined' && name != '' && name != 'undefined') {
            data[name] = $(this).val();
        }
    });
    return data;
}

function createSelect(name, options, keys, val, clazz) {
    var selectElem = $('<select name="' + name + '" disabled></select>');
    var kVal = 'val';
    var kTxt = 'txt';
    if (keys) {
        if (keys.kVal) {
            kVal = keys.kVal;
        }
        if (keys.kTxt) {
            kTxt = keys.kTxt;
        }
    }
    if (options && options.length > 0) {
        $.each(options, function(i, item) {
            var selected = (val === item[kVal]) ? "selected" : "";
            selectElem.append('<option value="' + item[kVal] + '" ' + selected + ' ' + (clazz ? 'class="' + clazz + '"' : '') + '>' + item[kTxt] + '</option>');
        });
    }
    if (selectElem.find('option').length === 0) {
        selectElem.append('<option value=""></option>');
    }
    selectElem.change(function() {
        fixAssginedToUserList($(this).parents('tr').attr('targetid'), $(this).parents('tr').attr('taid'));
    });
    return selectElem;
}
function resetForm(form) {
    $('input:radio, input:checkbox', form).each(function() {
        $(this).parent('span').removeClass('checked');
        $(this).removeAttr('checked');
    });
    $('select', form).each(function() {
        $('option', $(this)).removeAttr('selected');
    });
}

function selectChosenOption(selectElem, val) {
    selectElem.find('option[selected]').removeAttr('selected');
    selectElem.find('option[value=' + val + ']').attr('selected', 'selected');
    selectElem.trigger("liszt:updated");
}

function adjustDeletable(boxElem, taskId, targetId) {
    var taRows = boxElem.find('tr[taskId=' + taskId + '][targetId=' + targetId + ']');
    if (taRows.length <= 1) {
        taRows.find('td a.action.delete').hide();
    } else {
        taRows.find('td a.action.delete').show();
    }
}

function removeTaRow(boxElem, taId) {
    var taRow = boxElem.find('tr[taId=' + taId + ']');
    var taskId = taRow.attr('taskId');
    var targetId = taRow.attr('targetId');
    var firstTaRow = boxElem.find('tr[taskId=' + taskId + '][targetId=' + targetId + ']:first');

    if (taId === firstTaRow.attr('taId')) {
        var targetTd = firstTaRow.find('td:first').clone(true);
        taRow.remove();
        boxElem.find('tr[taskId=' + taskId + '][targetId=' + targetId + ']:first td:first').replaceWith(targetTd);
    } else {
        taRow.remove();
    }
}

function doIWantThis(elem, options) {
    var actions = '<a href="#" class="action iWantThis">' + $.i18n.message("common.js.i_want_this") + '</a>';
    elem.append(actions);
    $('.iWantThis', elem).click(function() {
        var $this = $(this);
        var pTr = $this.parents('tr');
        //var uidElem = pTr.find('td[uid]');
        var priority = pTr.find('td[priority]').attr('priority');
        var productId = $this.parents('div.box').attr('productId');
        //$('a.action.save, a.action.cancel', pTr).hide();
        $.ajax({
            url: 'queuesv2.do',
            type: 'POST',
            dataType: 'json',
            data: {
                action: 'updateQueue',
                productId: productId,
                taId: pTr.attr('taid'),
                priority: priority,
                assignUid: options.userId
            },
            success: function(resp) {
                if (resp.ret == 0) {
                    pTr.find('td[uid]').attr('uid', '' + resp.data.assignedUserId);
                    pTr.find('td[uid]').text(resp.data.assignedUsername);
                    elem.empty();
                    doReturnToQueue(elem, options);
                    $('tr[taskId=' + pTr.attr('taskId') + '][targetId=' + pTr.attr('targetId') + ']', pTr.parents('tbody')).each(function() {
                        var assignedToTd = $(this).find('td[uid]');
                        if (assignedToTd.attr('uid') == 0) {
                            $(this).find('td:last').empty();
                        }
                    });
                } else {
                    elem.find('span.loading').hide();
                    this.show();
                    ocsError(resp.desc);
                }
            },
            beforeSend: function() {
                elem.append('<span class="loading"></span>');
            },
            complete: function() {
                elem.find('span.loading').remove();
            },
            error: function() {
                elem.find('span.loading').remove();
                this.show();
            }
        });
        return false;
    });
}
function doReturnToQueue(elem, options) {
    var actions = '<a href="#" class="action returnToQueue">' + $.i18n.message("common.js.return_to_queue") + '</a>';
    elem.append(actions);
    $('.returnToQueue', elem).click(function() {
        var $this = $(this);
        var pTr = $this.parents('tr');
        //var uid = pTr.find('td[uid]').attr('uid');
        var priority = pTr.find('td[priority]').attr('priority');
        var productId = $this.parents('div.box').attr('productId');
        //$('a.action.save, a.action.cancel', pTr).hide();
        $.ajax({
            url: 'queuesv2.do',
            type: 'POST',
            dataType: 'json',
            data: {
                action: 'updateQueue',
                productId: productId,
                taId: pTr.attr('taid'),
                priority: priority,
                assignUid: 0
            },
            success: function(resp) {
                if (resp.ret == 0) {
                    pTr.find('td[uid]').attr('uid', '0');
                    pTr.find('td[uid]').text($.i18n.message("common.js.no_one"));
                    $('tr[taskId=' + pTr.attr('taskId') + '][targetId=' + pTr.attr('targetId') + ']', pTr.parents('tbody')).each(function() {
                        var assignedToTd = $(this).find('td[uid]');
                        if (assignedToTd.attr('uid') == 0) {
                            $(this).find('td:last').empty();
                            doIWantThis($(this).find('td:last'), options);
                        }
                    });
                } else {
                    elem.find('span.loading').hide();
                    this.show();
                    ocsError(resp.desc);
                }
            },
            beforeSend: function() {
                elem.append('<span class="loading"></span>');
            },
            complete: function() {
                elem.find('span.loading').remove();
            },
            error: function() {
                elem.find('span.loading').remove();
                this.show();
            }
        });
        return false;
    });
}
function doChangeTaskAssignment(productId, oldAssignUid, oldPriority, pTr, priorityElem, assignUserElem, elem) {
    $.ajax({
        url: 'queuesv2.do',
        type: 'POST',
        dataType: 'json',
        data: {
            action: 'updateQueue',
            productId: productId,
            taId: pTr.attr('taid'),
            priority: priorityElem.val(),
            assignUid: assignUserElem.val()
        },
        success: function(data) {
            if (data.ret === 0) {
                assignUserElem.parents('td').attr('uid', assignUserElem.val());
                priorityElem.parents('td').attr('priority', priorityElem.val());
                fixSelectableAssignedUserOfTarget(pTr.parents('tbody'), pTr.attr('targetId'));
            } else {
                ocsError(data.desc);
                assignUserElem.find("option").removeAttr('selected');
                assignUserElem.val(oldAssignUid);
                assignUserElem.find('option[value=' + oldAssignUid + ']').attr('selected', 'selected').trigger("liszt:updated");
                priorityElem.find("option").removeAttr('selected');
                priorityElem.val(oldPriority);
                priorityElem.find('option[value=' + oldPriority + ']').attr('selected', 'selected').trigger("liszt:updated");
            }
        },
        beforeSend: function() {
            elem.append('<span class="loading"></span>');
        },
        complete: function() {
            elem.find('span.loading').remove();
        },
        error: function() {
            elem.find('span.loading').remove();
            this.show();
        }
    });
}
function createActionsForNonPM(elem, options) {
    if (options.assignedMe) { // return to queue
        doReturnToQueue(elem, options);
    } else if (options.noAssigned && options.canClaim && !options.targetedTaskAssignedMe) {// I want this
        doIWantThis(elem, options);
    }
    return elem;
}

function fixAssginedToUserList(targetId, taId) {
    var $tblElem = $('div.results table');
    var $selectionElems = $tblElem.find('tr[targetid=' + targetId + '] td select[name=assignUserId]');
    var assignedUsers = [];
    $selectionElems.each(function() {
        if ($(this).val() > 0) {
            assignedUsers[assignedUsers.length] = $(this).val();
        }
    });
    if (assignedUsers.length === 0) {
        $selectionElems.find('option').removeAttr('disabled');
        return;
    }
    $selectionElems.each(function() {
        var $selectElem = $(this);
        if ($selectElem.attr('disabled') === 'undefined' || $selectElem.attr('disabled') === false) {
            return;
        }
        $selectElem.find('option').each(function() {
            if ($.inArray($(this).attr('value'), assignedUsers) > -1) {
                if ($selectElem.val() !== $(this).attr('value'))
                    $(this).attr('disabled', 'true');
            } else {
                $(this).removeAttr('disabled');
            }
        });
    });
}

function createActionsForPM(elem, canDelete) {
    var actions = '<a href="#" class="action edit">' + $.i18n.message("common.btn.edit") + '</a>'; // edit
    actions += '<a href="#" class="action save">' + $.i18n.message("common.btn.save") + '</a>'; // save
    actions += '<a href="#" class="action cancel">' + $.i18n.message("common.btn.cancel") + '</a>'; // cancel
    actions += '<a href="#" class="action delete" style="' + (canDelete ? '' : 'display: none') + '">' + $.i18n.message("common.btn.delete") + '</a>'; // cancel

    elem.append(actions);

    $('.edit', elem).click(function() {
        var pTr = $(this).parents('tr');
        pTr.find('select').removeAttr('disabled');//.trigger("liszt:updated");
        fixAssginedToUserList(pTr.attr('targetid'), pTr.attr('taid'));
        $('a.action.save, a.action.cancel', pTr).show();
        $('a.action.edit', pTr).hide();
        return false;
    });
    $('.save', elem).click(function() {
        var $this = $(this);
        var pTr = $this.parents('tr');
        pTr.find('select').attr('disabled', 'disabled').trigger("liszt:updated");
        $('a.action.save, a.action.cancel', pTr).hide();
        $('a.action.edit', pTr).show();
        var oldAssignUid = pTr.find("td[uid]").attr('uid');
        var assignUserElem = pTr.find('select[name=assignUserId]');
        var oldPriority = pTr.find("td[priority]").attr('priority');
        var priorityElem = pTr.find('select[name=priority]');
        if (oldAssignUid !== assignUserElem.val() || oldPriority != priorityElem.val()) {
            var productId = $this.parents('div.box').attr('productId');
            doChangeTaskAssignment(productId, oldAssignUid, oldPriority, pTr, priorityElem, assignUserElem, elem);
        }
        return false;
    });
    $('.delete', elem).click(function() {
        var $this = $(this);
        var pTr = $this.parents('tr');
        var pBody = $this.parents('tbody');

        ocsConfirm($.i18n.message('common.js.alert.deleteassignment'), $.i18n.message('common.js.alert.title.confirm'), function(choice) {
            if (choice) {
                pTr.find('select').attr('disabled', 'disabled').trigger("liszt:updated");
                var productId = $this.parents('div.box').attr('productId');
                $.ajax({
                    url: 'queuesv2.do',
                    type: 'POST',
                    dataType: 'json',
                    data: {
                        action: 'delQueue',
                        productId: productId,
                        taId: pTr.attr('taid')
                    },
                    success: function(data) {
                        if (data.ret == 0) {
                            //pTr.remove();
                            removeTaRow(pBody, pTr.attr('taid'));
                            adjustDeletable(pBody, pTr.attr('taskId'), pTr.attr('targetId'));
                        } else {
                            ocsError(data.desc);
                        }
                    },
                    beforeSend: function() {
                        elem.append('<span class="loading"></span>');
                    },
                    complete: function() {
                        elem.find('span.loading').remove();
                    },
                    error: function() {
                        elem.find('span.loading').remove();
                        this.show();
                    }
                });
            }
        });
        return false;
    });
    $('.cancel', elem).click(function() {
        var pTr = $(this).parents('tr');
        pTr.find('select').attr('disabled', 'disabled').trigger("liszt:updated");
        $('a.action.save, a.action.cancel', pTr).hide();
        $('a.action.edit', pTr).show();
        var oldAssignUid = pTr.find("td[uid]").attr('uid');
        var assignUserElem = pTr.find('select[name=assignUserId]');
        if (oldAssignUid !== assignUserElem.val()) {
            selectChosenOption(assignUserElem, oldAssignUid);
        }
        var oldPriority = pTr.find("td[priority]").attr('priority');
        var priorityElem = pTr.find('select[name=priority]');
        if (oldPriority !== priorityElem.val()) {
            selectChosenOption(priorityElem, oldPriority);
        }
        return false;
    });
    return elem;
}

function fixSelectableAssignedUserOfAllTargets(boxElem) {
    var targetIds = [];
    boxElem.find('tbody tr').each(function() {
        var targetId = $(this).attr('targetId');
        if ($.inArray(targetId, targetIds) === -1) {
            targetIds[targetIds.length] = targetId;
        }
    });
    $.each(targetIds, function(i, elem) {
        fixSelectableAssignedUserOfTarget(boxElem, elem);
    });
}

function fixSelectableAssignedUserOfTarget(boxElem, targetId) {
    $('tr[targetId=' + targetId + '] td[uid] select[name=assignUserId] option', boxElem).removeAttr('disabled');
    $('tr[targetId=' + targetId + ']', boxElem).each(function() {
        var $this = $(this);
        // var targetId = $this.attr('targetId');
        var taId = $this.attr('taId');
        var userId = $this.find('td[uid]').attr('uid');
        if (userId !== '0') {
            boxElem.find('tr[targetId=' + targetId + '][taId!=' + taId + '] td[uid]').each(function() {
                $(this).find('select[name=assignUserId] option[value=' + userId + ']').attr('disabled', 'disabled').trigger("liszt:updated");
            });
        }
    });
}

function createTaskItem(userId, isPM, productId, data) {
    // alert("data.targetedTaskList.length=" + data.targetedTaskList.length);

    if (!data.targetedTaskList || data.targetedTaskList.length == 0) {
        return;
    }
    var isMultiUser = data.multiUser;
    var taskItem = $('<div class="box" style="margin: 5px;background: #fafafa;border-radius: 4px" productId="' + productId + '">' +
            '   <h3><span class="taskname">' + data.taskName + '</span><span class="tasksub"></span><a href="#" class="toggleVisible"><img src="images/collapse_icon.png" alt="collapse"/></a></h3>' +
            '   <div class="content">' +
            '      <table width="100%" border="0" cellspacing="0" cellpadding="0">' +
            '         <thead>' +
            '            <tr>' +
            '               <th class="target">' + $.i18n.message("common.msg.target") + '</th>' +
            '               <th class="inQueue">' + $.i18n.message("common.js.inqueue") + '</th>' +
            '               <th class="status">' + $.i18n.message("common.label.status") + '</th>' +
            '               <th class="priority">' + $.i18n.message("common.label.priority") + '</th>' +
            '               <th class="assign">' + $.i18n.message("common.js.assignment") + '</th>' +
            '               <th>' + $.i18n.message(isPM ? "common.label.action" : "common.label.claim_this") + '</th>' +
            '             </tr>' +
            '         </thead>' +
            '         <tbody>' +
            '         </tbody>' +
            '       </table>' +
            '      <div class="clear"> </div>' +
            '   </div>' +
            '</div>');

    $('div.results').append(taskItem);
    // alert("taskItem appended");

    var taskItemBody = $('tbody', taskItem);
    $.each(data.targetedTaskList, function(i, item) {
        if (item.taList && item.taList.length > 0) {
            var targetedTaskAssignedMe;
            $.each(item.taList, function(j, taItem) {
                if (userId > 0 && userId === taItem.assignedUserId) {
                    targetedTaskAssignedMe = true;
                    return;
                }
            });
            $.each(item.taList, function(j, taItem) {
                taskItemBody.append(createTaskAssignmentRow(userId, isPM, isMultiUser, data.taskId, item.targetId, taItem, {
                    withTargetColumn: (j === 0),
                    targetName: item.targetName,
                    hasMultiTAs: (item.taList.length > 1),
                    canClaim: data.canClaim,
                    targetedTaskAssignedMe: targetedTaskAssignedMe,
                    goalObjStatus: item.goalObjStatus
                }));
            });
        }
    });


    if (!isPM) {
        doTaskSubscribe($('h3 span.tasksub', taskItem), data.subscribed ? 'unsubscribe' : 'subscribe');
    }

    // alert("Check 2");
    function doTaskSubscribe(elem, action) {
        var actionElem = $((action == 'subscribe') ?
                '<a href="#" class="action subscribe">' + $.i18n.message("common.js.subscribe_queue") + '</a>' :
                '<a href="#" class="action unsubscribe">' + $.i18n.message("common.js.unsubscribe_queue") + '</a>');
        elem.empty();
        elem.append(actionElem);
        actionElem.click(function() {
            $.ajax({
                url: 'queuesv2.do',
                type: 'POST',
                dataType: 'json',
                data: {
                    action: action,
                    userId: userId,
                    taskId: data.taskId
                },
                success: function(resp) {
                    if (resp.ret === 0) {
                        if (action === 'subscribe') {
                            doTaskSubscribe(elem, 'unsubscribe');
                        } else {
                            doTaskSubscribe(elem, 'subscribe');
                        }
                    } else {
                        ocsError(resp.desc);
                    }
                },
                beforeSend: function() {
                    elem.append('<span class="loading"></span>');
                },
                complete: function() {
                    elem.find('span.loading').remove();
                },
                error: function() {
                    elem.find('span.loading').remove();
                }
            });
            return false;
        });
    }

    function createTaskAssignmentRow(userId, isPM, isMultiUser, taskId, targetId, taItem, options) {
        var row = $('<tr targetId="' + targetId + '" taId="' + taItem.taId + '" taskId="' + taskId + '" mu="' + isMultiUser + '"></tr>');
        var goalObjDone = false;

        if (options && options.goalObjStatus && options.goalObjStatus === 3) {
            // the goal object is done - no change is allowed to TAs
            goalObjDone = true;
        }

        if (options && options.withTargetColumn) {
            if (isPM && isMultiUser && !goalObjDone) {
                row.append('<td><span>' + options.targetName + '&nbsp;<a href="#" class="action add" title="Click to add a new task assignment"></a></span></td>');
            } else {
                row.append('<td><span>' + options.targetName + '</span></td>');
            }
        } else {
            row.append('<td></td>');
        }
        row.append('<td>' + taItem.inQueueDays + ' days</td>');
        row.append('<td>' + taItem.statusDisplay + '</td>');
        var priorityElem;
        var qualifiedUserElem;

        if (isPM) {
            priorityElem = createSelect('priority', [{
                    txt: $.i18n.message("common.js.priority.low"),
                    val: 1
                }, {
                    txt: $.i18n.message("common.js.priority.medium"),
                    val: 2
                }, {
                    txt: $.i18n.message("common.js.priority.high"),
                    val: 3
                }], {
                kVal: 'val',
                kTxt: 'txt'
            }, taItem.priority, 'mini-input');
            var quList = taItem.quList;
            if (!quList) {
                quList = [];
            }
            quList[quList.length] = {
                uid: 0,
                uname: $.i18n.message("common.js.no_one")
            };
            qualifiedUserElem = createSelect('assignUserId', quList, {
                kVal: 'uid',
                kTxt: 'uname'
            }, taItem.assignedUserId, 'small-input');
        } else {
            priorityElem = taItem.priorityDisplay;
            qualifiedUserElem = (taItem.assignedUserId === 0) ? $.i18n.message("common.js.no_one") : taItem.username;
        }
        row.append($('<td priority="' + taItem.priority + '"></td>').append(priorityElem));
        row.append($('<td uid="' + taItem.assignedUserId + '"></td>').append(qualifiedUserElem));

        if (goalObjDone) {
            row.append($('<td>' + $.i18n.message("common.js.goal_done") + '</td>'));
        } else if (taItem.status === 5) { // DONE
            row.append($('<td></td>'));
        } else if (isPM) {
            row.append(createActionsForPM($('<td></td>'), isMultiUser && options.hasMultiTAs));
        } else {
            row.append(createActionsForNonPM($('<td></td>'), {
                userId: userId,
                assignedMe: (userId > 0 && userId === taItem.assignedUserId),
                noAssigned: (taItem.assignedUserId === 0),
                canClaim: options.canClaim,
                targetedTaskAssignedMe: options.targetedTaskAssignedMe
            }));
        }

        return row;
    }

    $('.add', taskItem).click(function() {
        var $this = $(this);
        var pTr = $this.parents('tr');
        var targetId = pTr.attr('targetId');
        var taskId = pTr.attr('taskId');
        var pBody = pTr.parents('tbody');
        $.ajax({
            url: 'queuesv2.do',
            type: 'POST',
            dataType: 'json',
            data: {
                action: 'addQueue',
                targetId: targetId,
                taskId: taskId
            },
            success: function(data) {
                if (data) {
                    var newRow = createTaskAssignmentRow(-1, true, true, taskId, targetId, data, {
                        hasMultiTAs: true
                    });
                    pBody.find('tr[targetId=' + targetId + ']:last').after(newRow);
                    adjustDeletable(pBody, taskId, targetId);
                    // fixSelectableAssignedUserOfTarget(pBody, targetId, taskId);
                    // $('select', newRow).attr('disabled', 'disabled').chosen();
                }
            },
            beforeSend: function() {
                $this.parents('td').append('<span class="loading"></span>');
                $this.hide();
            },
            complete: function() {
                $this.parents('td').find('span.loading').remove();
                $this.show();
            },
            error: function() {
                $this.parents('td').find('span.loading').remove();
                $this.show();
            }
        });
        return false;
    });

//    $('div.results').append(taskItem);
//    alert("taskItem appended");
    // fixSelectableAssignedUserOfAllTargets(taskItem);
    // alert("Skip fixSelectableAssignedUserOfAllTargets - Check 4");

    // $('div.results select').attr('disabled', 'disabled').chosen();
    // $('select', taskItem).chosen();

    // alert("Before toggle");
    behaveToggleVisible();
    // alert("Skipped disabled - Check 5");

}