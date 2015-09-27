<%-- 
    Document   : notesSidebar
    Created on : 2010-5-9, 11:33:21
    Author     : Luke Shi
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
<div id="notes-box" class="box ${boxClass}" style="display: none;">
    <h3><span><indaba:msg key="common.label.notes" /></span><a href="#" class="toggleVisible"><img src='images/collapse_icon.png' alt="" /></a></h3>
    <div class="content commpanel"></div>
</div>

<link type="text/css" rel="stylesheet" href="plugins/qtip/jquery.qtip.css"/>
<script type="text/javascript" src="plugins/qtip/jquery.qtip.js"></script>
<script type="text/javascript">
    var $COMM_ERR_MSG = "Cannot communicate with server. Please restart your session and try again later.";

    $(document).ready(function() {
        var isPreversionPage = ${param.action eq 'preVersionDisplay'};
        var getNoteobjsAction = isPreversionPage ? 'getNoteobjVersions' : 'getNoteobjs';
        var getNoteobjAction = isPreversionPage ? 'getNoteobjVersion' : 'getNoteobj';
        $.ajax({
            type: 'POST',
            url: '${contextPath}/commPanel.do',
            data: {action: getNoteobjsAction, horseid: '${param.horseid}', questionid: '${param.questionid}', contentVersionId: '${contentVersionId}'},
            dataType: 'json',
            success: function(data) {
                var $commPanel = $('#notes-box div.commpanel');
                var $notesBox = $('#notes-box');
                if (!data || !data.noteobjs || data.noteobjs.length == 0) {
                    return;
                }
                $notesBox.show();
                $.each(data.noteobjs, function(index, elem) {
                    var $noteItem = renderNoteItem(elem.languages, elem, isPreversionPage);
                    $commPanel.show();
                    behaveToggleVisible($noteItem);
                    $noteItem.find('a.groupusers').qtip({
                        position: {
                            my: 'top center',
                            at: 'bottom center'
                        },
                        overwrite: true,
                        style: {
                            tip: {corner: 'top center'},
                            widget: true},
                        show: {solo: true},
                        hide: 'mouseleave',
                        content: {
                            title: $.i18n.message('common.title.groupmembers'),
                            button: "",                           
                            text: function(e, api) {
                                $.ajax({
                                    type: 'POST',
                                    url: '${contextPath}/commPanel.do',
                                    data: {action: 'getNoteobjMembers', noteobjId: $noteItem.attr('noteobjid')},
                                    dataType: 'json'
                                }).then(function(data) {
                                    var tipHtml = renderMembers($noteItem, data);
                                    if (tipHtml != null) api.set('content.text', tipHtml);
                                }, function(xhr, status, error) {
                                    // Upon failure... set the tooltip content to the status and error value
                                    api.set('content.text', status + ': ' + error);
                                });
                                return 'Loading...';
                            }
                        }
                    });
                    $noteItem.find('ul.buttons select.note-lang').change(function() {
                        loadNote($noteItem, elem.objId, $(this).val());
                        return false;
                    });
                    $noteItem.find('ul.buttons a.undo-note').click(function() {
                        var langId = $noteItem.find('select.note-lang').val();
                        loadNote($noteItem, elem.objId, langId);
                        return false;
                    });
                    $noteItem.find('ul.buttons button.save-note').click(function() {
                        var langId = $noteItem.find('select.note-lang').val();
                        var text = $noteItem.find('textarea.note-content').val();
                        saveNote($noteItem, elem.objId, langId, text);
                        return false;
                    });
                });
            },
            error: function() {
                alert($COMM_ERR_MSG);
            }
        });
        
        function renderNoteItem(languages, noteobjView) {
            var canTranslate = noteobjView.canTranslateNote;
            var canEdit = (noteobjView.canEditNote && !isPreversionPage);
            var $commpanel = $('#notes-box div.commpanel');
            var toggleVisible = $commpanel.find('div.note-item').length > 0 ? false : true;
            var $noteItem = $('<div class="note-item" id="noteobj-' + noteobjView.objId + '" noteobjId="' + noteobjView.objId + '"></div>');
            $commpanel.append($noteItem);
            var $noteHeader = $('<h3><div class="panel-title">' +
                    '<div class="label"><span class="icon-notes"></span>' + noteobjView.name + '</div>' +
                    (isPreversionPage ? '' : '<a href="#" class="groupusers">&nbsp;</a>') +
                    '<a href="#" class="toggleVisible"><img src="images/' + (toggleVisible ? 'collapse' : 'expand') + '_icon.png" /></a>' +                   
                    '</div></h3>');
            $noteItem.append($noteHeader);

            var $noteBody = $('<div class="content note-input clear" ' + (!toggleVisible ? 'style="display:none;"' : '') + '>' +
                    '<div class="desc clear">' + noteobjView.description + '</div>' +
                    '<textarea class="text note-content"></textarea></div>');

            $noteBody.append('<ul class="buttons">' +
                    '<li class="left"></li>' +
                    '<li class="right"><a href="#" class="undo-note">Undo</a>&nbsp;<button href="#" class="mini button blue save-note">' + $.i18n.message('common.btn.save') + '</button></li>' +
                    '<li class="clear"></li>' +
                    '</ul>');

            var $noteInput = $noteBody.find('textarea.note-content');
            var $saveButton = $noteBody.find('ul.buttons li button');
            var $undoLink = $noteBody.find('ul.buttons li .undo-note');
            if (!canEdit) {
                $saveButton.hide();
                $undoLink.hide();
                $noteInput.attr('disabled', 'disabled');
            } else {
                $undoLink.hide();
                $saveButton.hide();
            }
            $noteItem.append($noteBody);
            var langId = noteobjView.langId <= 0 ? 1 : noteobjView.langId;
           
            $noteInput.val(noteobjView.text);
            $noteInput.bind("propertychange keyup input paste", function(event){
                $noteInput.addClass('note-content-changed');
                $saveButton.show();
                $undoLink.show();
            });

            //if (noteobjView.canTranslateNote) {
            if (languages) {
                var $langSelect = $('<select class="note-lang" name="language"></select>');
                $.each(languages, function(index, elem) {
                    $langSelect.append('<option value="' + elem.id + '" ' + (langId === elem.id ? "selected" : "") + '>' + elem.languageDesc + '</option>');
                });
                $noteBody.find('li.left').append($langSelect);
            }
            //}
            return $noteItem;
        }

        function handleGeneralNoteResult(noteItem, data) {
            if (!data) {
                jAlert($.i18n.message('common.alert.invalidresponse'));
                return;
            }
            if (data.msg && data.msg.trim().length > 0) {
                jAlert(data.msg, $.i18n.message('common.js.alert.title.error'));
            }

            if (data.state == 2 || data.state == 3) {
                // remove this group
                noteItem.remove();

                var numNotes = $('.note-item').length;
                if (numNotes <= 0) {
                    // remove the note pannel
                    $('#notes-box').remove();
                }
            }
        }


        function renderMembers(noteItem, data) {
            handleGeneralNoteResult(noteItem, data);

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

        function loadNote(noteItem, noteobjId, langId) {
            $.ajax({
                type: 'POST',
                url: '${contextPath}/commPanel.do',
                data: {action: getNoteobjAction,
                    noteobjId: noteobjId,
                    contentVersionId: '${contentVersionId}',
                    langId: langId},
                dataType: 'json',
                success: function(data) {
                    handleNoteActionResult(noteItem, data);
                },
                error: function() {
                    alert($COMM_ERR_MSG);
                }
            });
        }

        function saveNote(noteItem, noteobjId, langId, text) {
            if (!text || text.trim() === '') {
                return;
            }
            $.ajax({
                type: 'POST',
                url: '${contextPath}/commPanel.do',
                data: {action: 'saveNoteobj', noteobjId: noteobjId, langId: langId, text: text},
                dataType: 'json',
                success: function(data) {                   
                    handleNoteActionResult(noteItem, data);                    
                },
                error: function() {
                    alert($COMM_ERR_MSG);
                }
            });
        }

        
        function handleNoteActionResult(noteItem, data) {
            handleGeneralNoteResult(noteItem, data);
            if (data.state == 1) {
                var $noteInput = noteItem.find('textarea.note-content');
                var $saveButton = noteItem.find('ul.buttons li button');
                var $undoLink = noteItem.find('ul.buttons li .undo-note');
                $noteInput.val(data.text);
                $noteInput.removeClass('note-content-changed');
                $undoLink.hide();
                $saveButton.hide();
            }
        }
    });
</script>