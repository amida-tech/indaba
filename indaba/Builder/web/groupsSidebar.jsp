<%-- 
    Document   : groupsSidebar.jsp
    Created on : 2010-5-9, 11:33:21
    Author     : Jeff Jiang
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<c:set var="boxClass" value=""/>
 <c:if test='${param.taskType == 20}'>
    <c:set var="boxClass" value="flag-response-box"/>
 </c:if>
 <c:if test='${param.taskType == 21}'>
    <c:set var="boxClass" value="flag-unset-box"/>
 </c:if>

<div id="groups-box" class="box ${boxClass}" style="display: none;">
    <h3><span><indaba:msg key="common.label.groupdiscussions" /></span><a href="#" class="toggleVisible"><img src='images/collapse_icon.png' alt="" /></a></h3>
    <div class="content commpanel"></div>
</div>
<div id="flag-question-form-box">
    <form  class="flag-question-form" action="${contextPath}/commPanel.do" method="POST">
        <div style="line-height: 16px;"><indaba:msg key='common.msg.reassignflagdesc' /></div>
        <input type="hidden" name="horseId" value="${param.horseid}" />
        <input type="hidden" name="taskAssignmentId" value="${param.assignid}" />
        <input type="hidden" name="taskType" value="${param.taskType}" />
        <table>
            <tbody>
                <tr>
                    <td class="label"><indaba:msg key="common.label.details" /><span class="required">*</span></td>
            <td><textarea name="issueDescription" class="text validate[required]" style="width: 95%" data-prompt-position="topLeft" ></textarea></td>
            </tr>
            <tr>
                <td><indaba:msg key="common.label.assign" /><span class="required">*</span></td>
            <td>
                <select class="srvset" name="assignedUserId" class="validate[required]" style="min-width: 40px; max-width: 230px;">
                    <option value="-1" disabled selected><indaba:msg key="common.label.chooseUser"/></option>
                </select>
            </td>
            </tr>
            <tr>
                <td><indaba:msg key="common.label.what" /><span class="required">*</span></td>
            <td>
                <ul class="what-options">
                    <li><span class="what-header"><indaba:msg key="common.label.content"/></span></li>
                    <li><span class="what-header"><indaba:msg key="common.label.review"/></span></li>
                    <li><input class="srvset" name="what" type="checkbox"  value="1"/> <indaba:msg key="common.label.scores"/></li>
                    <li><input class="srvset" name="what" type="checkbox"  value="16"/> <indaba:msg key="common.label.opinions"/></li>
                    <li><input class="srvset" name="what" type="checkbox"  value="2" /> <indaba:msg key="common.label.sources"/></li>
                    <li><input class="srvset" name="what" type="checkbox"  value="32" /> <indaba:msg key="common.label.discussions"/></li>
                    <li><input class="srvset" name="what" type="checkbox"  value="4" /> <indaba:msg key="common.label.comments"/></li>
                    <li><input name="" type="checkbox" value="0" style="visibility:hidden;"/></li>
                    <li><input class="srvset" name="what" type="checkbox"  value="8" /> <indaba:msg key="common.label.attachments"/></li>
                </ul>
            </td>
            </tr>
            <tr>
                <td><indaba:msg key="common.label.due2" /><span class="required">*</span></td>
            <td><input type="text" class="datepicker validate[required]" name="dueTime" /></td>
            </tr>
            <tr>
                <td colspan="2" style="text-align:right;padding-top:5px;">
                    <input class="mini button blue resolve-flag-question" style="display:none;" value="<indaba:msg key='common.btn.resolve' />" type="submit" />
                    <input class="mini button blue save-flag-question" style="display:none;" value="<indaba:msg key='common.btn.save' />" type="submit" />
                    <input class="mini button blue cancel-flag-question" value="<indaba:msg key='common.btn.cancel' />" type="reset">
                </td>
            </tr>
            </tbody>
        </table>
    </form>
</div>
<link type="text/css" rel="stylesheet" href="plugins/qtip/jquery.qtip.css"/>
<link type="text/css" rel="stylesheet" href="plugins/validate/css/validationEngine.jquery.css"/>

<script type="text/javascript" src="plugins/qtip/jquery.qtip.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>
<script type="text/javascript" src="plugins/validate/js/languages/jquery.validationEngine-en.js"></script>
<script type="text/javascript" src="plugins/validate/js/jquery.validationEngine.js"></script>
<script type="text/javascript">
    var $COMM_ERR_MSG = "Cannot communicate with server. Please restart your session and try again later.";

    $(function() {
        $.ajax({
            type: 'POST',
            url: '${contextPath}/commPanel.do',
            data: {action: 'getGroupSummary', horseid: '${param.horseid}', questionid: '${param.questionid}'},
            dataType: 'json',
            success: function(data) {
                var $commPanel = $('#groups-box div.commpanel');
                var $groupsBox = $('#groups-box');
                if (!data || !data.groups || data.groups.length === 0) {
                    return;
                }
                $groupsBox.show();
                $.each(data.groups, function(index, elem) {
                    var $groupItem = renderGroupItem(elem);
                    $commPanel.show();
                    updateWidgetWithUiState($groupItem, elem.uistate, elem.timestamp);
                    loadGroupComments($groupItem);
                });

                updateToggle();
            },
            error: function() {
                alert($COMM_ERR_MSG);
            }
        });

        function renderGroupItem(groupobjView) {
            var $commpanel = $('#groups-box div.commpanel');
            var $groupItem = $('<div class="group-item" id="groupobj-' + groupobjView.objId +
                    '" groupobjId="' + groupobjView.objId + '" canManageComments="' +
                    groupobjView.canManageComments + '"></div>');
            $commpanel.append($groupItem);
            var $groupHeader = $('<h3><div class="panel-title">' +
                    '<div class="label"><span></span>' + groupobjView.name + '</div>' +
                    '<a href="#" class="groupusers">&nbsp;</a>' +
                    '<a href="#" class="toggleVisible"><img /></a>' +                   
                    '</div></h3>');
            $groupItem.append($groupHeader);

            var $groupBody = $('<div class="content clear">' +
                    '<div class="desc clear">' + groupobjView.description + '</div>' +
                    '<ul class="group-comments"></ul>' +
                    '<div class="comment-input">' +                  
                    '<table>' +
                    '<tr valign="top">' +
                    '<td rowspan="2">' +
                    '<textarea class="text group-content" textBoxState="' + groupobjView.uistate.textBoxState + '"></textarea>' +
                    '</td>' +
                    '<td>' +
                    '<input type="image" width="25" height="25" class="discussion-flag">' +
                    '</td>' +                    
                    '</tr>' +
                    '<tr>' + 
                    '<td>' +
                    '<input type="image" width="25" height="25" class="discussion-add">' +
                    '</td>' +
                    '</tr>' +
                    '</table>' +
                    '</div>');
            $groupItem.append($groupBody);
            setupGroupMemberPopup($groupItem);
            setupFlagDiscussionFormPopup($groupItem);
            setupSaveComment($groupItem);
            $('ul.group-comments', $groupBody).mCustomScrollbar({
                theme: 'dark-thick', horizontalScroll: false, advanced: {updateOnContentResize: true}
            });

            return $groupItem;
        }
        function getTitleBarClass(titleBarState) {
            switch (titleBarState) {
                case 1:
                    return 'icon-groups';
                    break;
                case 2:
                default:
                    return 'icon-groups-with-flag';
                    break;
            }
        }
        function setupGroupMemberPopup(groupItem) {
            groupItem.find('a.groupusers').click(function(e) {
                e.preventDefault();
                return false;
            }).qtip({
                position: {
                    my: 'top center',
                    at: 'bottom center'
                },
                suppress: false,
                style: {tip: {corner: 'top center'}, widget: true},
                show: {solo: true},
                hide: 'mouseleave',
                events: {
                    show: function(event, api) {
                        api.set('content.text', $.i18n.message('common.msg.loading'));
                        $.ajax({
                            type: 'POST',
                            url: '${contextPath}/commPanel.do',
                            data: {action: 'getGroupMembers', groupobjId: groupItem.attr('groupobjId'), ts: groupItem.attr('ts')},
                            dataType: 'json'
                        }).then(function(data) {
                            var tipHtml = renderMembers(groupItem, data);
                            api.set('content.text', tipHtml);
                            // return tipHtml;
                        }, function(xhr, status, error) {
                            // Upon failure... set the tooltip content to the status and error value
                            api.set('content.text', status + ': ' + error);
                        });
                    }
                },
                content: {
                    title: $.i18n.message('common.title.groupmembers'),
                    button: "",
                    text: function(e, api) {
                        return $.i18n.message('common.msg.loading');
                    }
                }
            });
        }


        function _handleGeneralResult(groupItem, data) {
            if (!data) {
                jAlert($.i18n.message('common.alert.invalidresponse'));
                return;
            }
            
            if (data.msg && data.msg.trim().length > 0) {
                jAlert(data.msg, $.i18n.message('common.js.alert.title.error'));
            }

            var groupobjId = groupItem.attr('groupobjId');
            if (data.state == 2 || data.state == 3) {
                // remove this group
                groupItem.remove();
                
                var numGroups = $('.group-item').length;
                if (numGroups <= 0) {
                    // remove the group pannel 
                    $('#groups-box').remove();
                }

            } else if (data.state === 4) {
                loadGroupComments(groupItem);
            }
        }
        

        function setupFlagDiscussionFormPopup(groupItem) {
            var $flagButton = groupItem.find('input.discussion-flag');
            var qtipId = 'flag-disucssion-qtip-' + groupItem.attr('id');
            $flagButton.click(function() {
                var $flagState = $flagButton.attr("state");
                if ($flagState != 3) return false;

                // call the API and handle redirect
                var apiAction = 'getFlagResponseDestination';
                var groupobjId = groupItem.attr('groupobjId');
                var flagId = $('#groupobj-' + groupobjId).attr('flagId');
                $.ajax({
                    type: 'POST',
                    url: '${contextPath}/commPanel.do',
                    data: {action: apiAction, horseid: '${param.horseid}', taskAssignmentId: ${param.assignid}, groupobjId: groupobjId, flagId: flagId},
                    dataType: 'json',
                    success: function(data) {
                        _handleGeneralResult(groupItem, data);
                        if (data.state != 1) return;
                            window.location.href = "${contextPath}/flagResponse.do?horseid=${param.horseid}&flagid=" + flagId +
                            "&assignid=" + data.taskAssignmentId + "&questionid=${param.questionid}&initialFlagGroupId=" + groupobjId +
                            "&fromurl=${requrl}";
                    },
                    error: function() {
                        alert($COMM_ERR_MSG);
                    }
                });
            }).qtip({
                id: qtipId,
                position: {
                    my: 'top right',
                    at: 'bottom right'
                },               
                events: {                   
                    visible: function(event, api) {
                        var $form = $('#qtip-' + qtipId + ' .flag-question-form');
                        var $flagId = parseInt(groupItem.attr('flagId'));

                        initializeFlagForm($form, $flagId);
                        $form.validationEngine({prettySelect: true, useSuffix: "_chzn", validationEventTrigger: "change"});
                        $form.validationEngine('detach');
                        $form.find(".datepicker").datepicker();

                        $form.find('input.cancel-flag-question').click(function() {
                            //$(this).parents('div.qtip').qtip('hide');
                            api.hide();
                        });

                        $form.find('input.save-flag-question').click(function() {
                            $form.validationEngine('attach');
                            var valid = $form.validationEngine('validate');
                            $form.validationEngine('detach');
                            if (!valid) {
                                return false;
                            }
                            var $action = "raiseFlag";
                            var $groupobjId = groupItem.attr('groupobjId');
                            var $canManage = groupItem.attr('canManageComments');
                            if ($flagId && $flagId > 0) $action = "reassignFlag";

                            $form.ajaxSubmit({
                                data: {action: $action, horseid: '${param.horseid}', groupobjId: $groupobjId, ts: groupItem.attr('ts'), flagId: $flagId, canManageComments: $canManage},
                                dataType: 'json',
                                success: function(data) {
                                    //$("div.qtip").hide();
                                    api.hide();
                                    _handleGeneralResult(groupItem, data);
                                    
                                    if (data.state == 1) {
                                        groupItem.attr('flagId', data.flagId);
                                        handleGroupContentResult(groupItem, data);
                                    }
                                },
                                error: function() {
                                    api.hide();
                                    alert($COMM_ERR_MSG);
                                }
                            });
                            return false;
                        });

                        $form.find('input.resolve-flag-question').click(function() {
                            $form.validationEngine('attach');
                            $form.validationEngine('hideAll');
                            var invalid = $form.find('textarea').validationEngine('validate');
                            $form.validationEngine('detach');
                            if (invalid) {
                                return false;
                            }
                            var $groupobjId = groupItem.attr('groupobjId');
                            var $canManage = groupItem.attr('canManageComments');
                            var $action = "unsetFlag";
                            $form.ajaxSubmit({
                                data: {action: $action, flagId: $flagId, horseid: '${param.horseid}', groupobjId: $groupobjId, ts: groupItem.attr('ts'), canManageComments: $canManage},
                                dataType: 'json',
                                success: function(data) {
                                    //$("div.qtip").hide();
                                    api.hide();
                                    _handleGeneralResult(groupItem, data);

                                    if (data.state == 1) {
                                        groupItem.attr('flagId', data.flagId);
                                        handleGroupContentResult(groupItem, data);
                                    }
                                },
                                error: function() {
                                    api.hide();
                                    alert($COMM_ERR_MSG);
                                }
                            });
                            return false;
                        });

                        // prepare data
                        var apiAction = '';
                        var $flagState = $flagButton.attr("state");
                        if ($flagState == 1) {
                            apiAction = 'getNewFlagInfo';
                        } else {
                            apiAction = 'getFlagInfo';
                        }

                        var groupobjId = groupItem.attr('groupobjId');
                        $.ajax({
                            type: 'POST',
                            url: '${contextPath}/commPanel.do',
                            data: {action: apiAction, horseid: '${param.horseid}', taskAssignmentId: ${param.assignid}, groupobjId: groupobjId, flagId: $('#groupobj-' + groupobjId).attr('flagId')},
                            dataType: 'json',
                            success: function(data) {
                                _handleGeneralResult(groupItem, data);

                                if (data.state != 1) {
                                    // $('#qtip-' + qtipId).qtip('hide');
                                    api.hide();
                                    return;
                                }
                                setupFlagForm($form, data, parseInt(groupItem.attr('flagId')));
                            },
                            error: function() {
                                //$('#qtip-' + qtipId).qtip('hide');
                                api.hide();
                                alert($COMM_ERR_MSG);
                            }
                        });
                    }
                },
                style: {tip: {corner: 'top right'}, widget: true},
                show: {event: 'click', modal: {on: true, blur: false, escape: false}, solo: true},
                hide: false,
                suppress: false,
                content: {
                    title: $.i18n.message('common.title.flagquestion'),
                    button: true,
                    text: function(e, api) {
                        return $('#flag-question-form-box').html();
                    }
                }
            });
        }


        function setupFlagForm(form, data, flagId) {
            var $select = form.find('select[name=assignedUserId]');
            $select.find('option[value!=-1]').remove();
            if (data.candidates) {
                $.each(data.candidates, function(i, elem) {
                    $select.append('<option value="' + elem.userId + '">' + elem.displayName + ' - ' + elem.roleName + '</option>');
                });
            }

            // form.clearForm();
            /*
            if (data.description && data.description.trim() !== '') {
                form.find('textarea[name=issueDescription]').val(data.description);
            }
            */
            if (data.assignedUserId) {
                $select.find('option[value=' + data.assignedUserId + ']').attr('selected', 'selected');
            }
            if (data.permissions) {
                $.each(form.find('input[name=what]'), function(i, elem) {                    
                    if ((data.permissions & parseInt(elem.value)) > 0) {
                        elem.checked = true;
                    }
                    
                });
            }
            if (data.dueTimeStr && data.dueTimeStr.trim() !== '') {
                form.find('input[name=dueTime]').val(data.dueTimeStr);
            }

            form.find('input.save-flag-question').show();
            if (flagId > 0) {
                form.find('input.resolve-flag-question').show();
            }

            form.find('.srvset').prop('disabled', false);
        }

        function initializeFlagForm(form, flagId) {
            form.clearForm();
            var $select = form.find('select[name=assignedUserId]');
            $select.find('option[value!=-1]').remove();
            form.find('input.save-flag-question').hide();
            form.find('input.resolve-flag-question').hide();

            // disable
            form.find('.srvset').prop('disabled', true);
        }


        function setupSaveComment(groupItem) {
            var $addButton = groupItem.find('input.discussion-add');

            $addButton.click(function() {
                var $textarea = groupItem.find('textarea.group-content');
                if ($textarea.val().trim() === '') {
                    return;
                }
                var groupobjId = groupItem.attr('groupobjId');
                var reqData = {
                    taskType: '${param.taskType}',
                    horseid: ${param.horseid},
                    groupobjId: groupobjId,
                    ts: groupItem.attr('ts'),
                    canManageComments: groupItem.attr('canManageComments'),
                    text: $textarea.val()
                };
                if ($addButton.attr('state') === '1') { // reqular comment
                    reqData = $.extend(reqData, {
                        action: 'saveRegularComment'
                    });
                } else {
                    reqData = $.extend(reqData, {
                        action: 'saveFlagResponse',
                        flagId: groupItem.attr('flagId')
                    });
                }
                $.ajax({
                    type: 'POST',
                    url: '${contextPath}/commPanel.do',
                    data: reqData,
                    dataType: 'json',
                    success: function(data) {
                        _handleGeneralResult(groupItem, data);
                        if (data.state != 1) return;

                        if (handleGroupContentResult(groupItem, data) !== 0) {
                            return;
                        }
                        if (data.code === 0) { // success to add
                            groupItem.find('div.comment-input textarea.group-content').val('');
                        }
                    },
                    error: function() {
                        alert($COMM_ERR_MSG);
                    }
                });
            });
        }
        
        function renderMembers(groupItem, data) {
            _handleGeneralResult(groupItem, data);

            if (data.state != 1) return null;

            var members = data.members;
            if (!members || members.length === 0) {
                return $.i18n.message('common.msg.nomember');
            }
            var tipHtml = '<ul class="members">';
            $.each(members, function(i, elem) {
                tipHtml += '<li><span class="uname">' + elem.displayName + '</span> <span class="rname">' + elem.roleName + '</span></li>';
            });
            return tipHtml + '</ul>';
        }

        function loadGroupComments(groupItem) {
            var taskType = '${param.taskType}';
            var timestamp = groupItem.attr('ts');
            var canManageComments = groupItem.attr('canManageComments');
            var groupobjId = groupItem.attr('groupobjId');

            $.ajax({
                type: 'POST',
                url: '${contextPath}/commPanel.do',
                data: {action: 'getGroupComments', taskType: taskType, groupobjId: groupobjId, timestamp: timestamp, canManageComments: canManageComments},
                dataType: 'json',
                ajax: false,
                success: function(data) {
                    _handleGeneralResult(groupItem, data);
                    if (data.state == 1) {
                        handleGroupContentResult(groupItem, data);
                    }
                },
                error: function() {
                    alert($COMM_ERR_MSG);
                }
            });
        }
        function renderGroupComments($groupItem, comments) {
            var $comments = $groupItem.find('ul.group-comments');
            var groupobjId = $groupItem.attr('groupobjId');
            var canmanagecomments = $groupItem.attr('canmanagecomments') === 'true';
            $.each(comments, function(i, elem) {
                createGroupComment($('div.mCSB_container', $comments), elem, canmanagecomments, groupobjId);
            });
            $comments.find('li.last').removeClass('last');
            $comments.find('li:last').addClass('last');

            updateCommentsScrollbar($comments);

            if ($comments.find('li').length === 0) {
                $comments.hide();
            } else {
                $comments.show();
            }
        }

        function manageComment($linkElem, commentId, taskType, groupobjId) {
            var groupItem = $('#groupobj-' + groupobjId);
            var action = ($linkElem.attr('class') === 'hide-comment') ? 'hideComment' : 'unhideComment';
            $.ajax({
                type: 'POST',
                url: '${contextPath}/commPanel.do',
                data: {action: action, taskType: taskType, groupobjId: groupobjId, ts: groupItem.attr('ts'), commentId: commentId},
                dataType: 'json',
                success: function(data) {
                    _handleGeneralResult(groupItem, data);
                    if (handleGroupContentResult(groupItem, data) === 0) {
                        if (action === 'hideComment') {
                            $linkElem.attr('class', 'unhide-comment');
                        } else {
                            $linkElem.attr('class', 'hide-comment');
                        }
                    }
                },
                error: function() {
                    alert($COMM_ERR_MSG);
                }
            });
        }

        function createGroupComment(commentsBox, comment, canmanagecomments, groupobjId) {
            var $comment = $('<li></li>');
            var $header = $('<div class="comment-header" commentId="' + comment.commentId + '"></div>');
            var stateIconClass = '';
            switch (comment.type) {
                case 1:
                    stateIconClass = 'setflag';
                    break;
                case 2:
                    stateIconClass = 'response';
                    break;
                case 3:
                    stateIconClass = 'unset';
                    break;
                default:
                    stateIconClass = 'regular';
                    break;
            }
            var manageCommentLink = (canmanagecomments) ? '<a href="#" class="' + (comment.state === 0 ? 'hide' : 'unhide') + '-comment"></a>' : "";
            $header.append('<span class="user ' + stateIconClass + '">' + comment.userDisplayName + '</span>');
            $header.append('<span class="right">' + comment.timeStr + manageCommentLink + '</span>');
            $comment.append($header).append('<div class="comment-body">' + comment.text + '</div>');
            commentsBox.append($comment);

            if (canmanagecomments) {
                $header.find('span.right a').click(function() {
                    manageComment($(this), $(this).parents('.comment-header').attr('commentId'),
                            '${param.taskType}', groupobjId);
                    return false;
                });
            }
        }


        function handleGroupContentResult(groupItem, result) {
            if (!result) {
                jAlert($.i18n.message('common.alert.invalidresponse'));
                return -1;
            }
            if (result.code !== 0) {               
                return result.code;
            }
            if (result.state != 1) return -1;
            updateWidgetWithUiState(groupItem, result.uistate, result.timestamp, result.flagId);
            renderGroupComments(groupItem, result.comments);
            return 0;               
        }
        
        function updateToggle() {
            var $groupItems = $('#groups-box div.commpanel div.group-item');
            var toggle;
            $.each($groupItems, function(i, elem) {
                var $groupItem = $(elem);
                var $open = true;

                if (${param.initialFlagGroupId} > 0) {
                    var $groupId = $groupItem.attr('groupobjId');
                    if ($groupId == ${param.initialFlagGroupId}) {
                        $open = true;
                    } else {
                        $open = false;
                    }
                } else {
                    if (i === 0) {
                        $open = true;
                    } else {
                        $open = false;
                    }
                }

                if ($open) {
                    toggle = 'collapse';
                } else {
                    toggle = 'expand';
                    $groupItem.find('div.content:first').css('display', 'none');
                }

                $groupItem.find('a.toggleVisible img').attr('src', 'images/' + toggle + '_icon.png');

                behaveToggleVisible($groupItem, function() {
                    updateCommentsScrollbar($groupItem.find('ul.group-comments'));
                });
            });
        }


        function updateCommentsScrollbar($comments) {
            $comments.css('height', '');
            var maxHeight = $comments.css('max-height');
            maxHeight = maxHeight.substring(0, maxHeight.indexOf('px'));
            var height = $comments.height();
            if (height > maxHeight) {
                height = maxHeight + 'px';
            }

            $comments.animate({height: height}, 'slow', function() {
                $(this).mCustomScrollbar('update');
                $(this).mCustomScrollbar("scrollTo", "bottom");
            });
        }
        
        function updateWidgetWithUiState(groupElem, uistate, timestamp, flagId) {
            if (timestamp != 0) groupElem.attr('ts', timestamp);
            groupElem.attr('flagId', flagId);

            // update title bar
            groupElem.find('div.label>span').attr('class', getTitleBarClass(uistate.titleBarState));

            // update box state
            // var boxStateClass = (uistate.textBoxState === 1) ? 'regular' : 'flag';
            // $('div.comment-input span.textbox-state-icon', groupElem).addClass(boxStateClass);

            // update flag button
            updateAddButton(groupElem.find('input.discussion-add'), uistate.textBoxState);
            updateFlagButton(groupElem, uistate.flagButtonState);
        }


        function updateAddButton($addButton, textBoxState) {
            $addButton.attr('state', textBoxState);
            switch (textBoxState) {
                case 1: // regular
                    $addButton.attr('src', 'images/discussPanel_button_addComment.png');
                    $addButton.attr('title', 'Add a comment');
                    break;
                case 2: // set by me
                    $addButton.attr('src', 'images/discussPanel_button_addResponse.png');
                    $addButton.attr('title', 'Enter the response to the flag');
                    break;
            }

        }

        function updateFlagButton(groupItem, flagButtonState) {
            var $flagButton = groupItem.find('input.discussion-flag');
            var $addButton = groupItem.find('input.discussion-add');
            var $buttonState = $addButton.attr('state');

            $flagButton.attr('state', flagButtonState);

            switch (flagButtonState) {
                case 1: // unset
                    $flagButton.attr('src', 'images/discussPanel_button_flagUnset.png');
                    $flagButton.attr('title', 'There is no flag standing. Click to raise a new flag');
                    break;
                case 2: // set by me
                    $flagButton.attr('src', 'images/discussPanel_button_flagByMe.png');
                    $flagButton.attr('title', 'The flag was raised by you. Click to resolve or reassign the flag');
                    break;
                case 3: // assigned to me
                    $flagButton.attr('src', 'images/discussPanel_button_flagToMe.png');

                    if (${param.taskType == 20}) {
                        $flagButton.attr('disabled', 'disabled');

                        if ($buttonState == 1) {
                            $flagButton.attr('title', 'The flag is assigned to you. You already responded.');
                        } else {
                            $flagButton.attr('title', 'The flag is assigned to you. Please fix identified issues and enter a response.');
                        }
                    } else {
                        if ($buttonState == 1) {
                            $flagButton.attr('title', 'The flag is assigned to you. You already responded.');
                        } else {
                            $flagButton.attr('title', 'The flag is assigned to you. Click to respond to the flag.');
                        }
                    }
                    break;
                case 4: // set others
                    $flagButton.attr('src', 'images/discussPanel_button_flagByOthers.png');
                    $flagButton.attr('disabled', 'disabled');
                    $flagButton.attr('title', 'The flag is set by others.');
                    break;
                case 5: // hide
                default:
                    $('textarea.group-content', $flagButton.parent()).addClass('no-flagbtn');
                    $flagButton.hide();
                    return null;
            }
        }
    });
</script>