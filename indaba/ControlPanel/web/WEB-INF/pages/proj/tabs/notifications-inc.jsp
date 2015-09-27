<%-- 
    Document   : notification-inc.jsp
    Created on : Dec 13, 2013, 10:34:29 AM
    Author     : ningshan
--%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC"-//W3C//DTD XHTML 1.0 Transitional//EN">

<div id="notificationsTab">
    <div class="filter">
        <form id="notificationFilterForm" action="#" onsubmit="$('#notifications-flexgrid').flexReload({newp: 1,dataType: 'json'});return false;" method="POST">
            <fieldset>
                <legend><img width="16px" src="${contextPath}/resources/images/search.png"><span><indaba:msg key="cp.label.notification_filter"  /></span></legend>
                <div id="filterLanguage" class="field" style="margin-top: 0px;">
                    <label><indaba:msg key="cp.label.language"  /></label>
                    <select name="filterLanguageId" id="filterLanguageId" style="width: 200px; height: 15px;" class="pretty dk" >
                        <option value="0"></option>                 
                    </select>
                </div>
                <div id="filterType" class="field" style="margin-top: 0px;">
                    <label><indaba:msg key="cp.label.type"  /></label>
                    <select name="filterTypeId" id="filterTypeId" style="width: 200px; height: 15px;" class="pretty dk" >
                        <option value="0"></option>                 
                    </select>
                </div>
                <div id="filterRole" class="field" style="margin-top: 0px;">
                    <label><indaba:msg key="cp.label.role"  /></label>
                    <select name="filterRoleId" id="filterRoleId"  style="width: 200px; height: 15px;" data-placeholder="<indaba:msg key='cp.text.choose_role' />"  class="pretty dk" >
                        <option value="-1"></option>                 
                    </select>
                </div>
            <div style="text-align: right;width: 200px; float: right;">
                <a id="submitNotifiFilter" class="button"><indaba:msg key="cp.btn.find"/></a>
                <a id="resetNotifiFilter" class="button reset"><indaba:msg key="cp.btn.reset"/></a>
            </div>
            </fieldset>
        </form>
    </div>
    <div id="notificationsFlexGrid" style="margin: 10px">
        <div id="notifications-flexgrid"></div>
    </div>
    <div id="notificationsFormPopup" title="<indaba:msg key='cp.title.add_notification' />" class="hidden">
        <div id="notificationsFormBox" class="popup-container">
            <form id="notificationsForm" action="#" method="POST" onsubmit="return false;">
                <fieldset>
                    <input type="hidden" name="mode" id="mode" value="ADD"/>
                    <input type="hidden" name="notifiId" id="notifiId" value="-1"/>
                    <dl>
                        <dt><label for="name"><indaba:msg key="cp.label.name" /></label></dt>
                        <dd><input type="text" name="name" maxlength="100" id="name" class="short-input validate[required]"/><span id="userHint" style="margin-left: 10px;color: green"></span></dd>
                    </dl>
                    <dl>
                        <dt><label for="notifidesc"><indaba:msg key="cp.label.description" /></label></dt>
                        <dd> <textarea id="notifidesc" name="notifidesc" class="long-input"></textarea></dd>
                    </dl>
                    <dl>
                        <dt><label for="type"><indaba:msg key="cp.label.type" /></label></dt>
                        <dd><select name="type" id="type" disabled="disabled" data-placeholder="<indaba:msg key='cp.text.choose_type'  />" class="short-input validate[required]" onchange="loadtokens(this.value)">
                            </select>
                        </dd>
                    </dl>
                    <dl>
                        <dt><label for="notifilanguage"><indaba:msg key="cp.label.language" /></label></dt>
                        <dd><select name="notifilanguage" id="notifilanguage" data-placeholder="<indaba:msg key='cp.text.choose_language'  />" class="short-input validate[required]">
                            </select>
                        </dd>
                    </dl>
                    <dl>
                        <dt><label for="notifiroles"><indaba:msg key="cp.label.role" /></label></dt>
                        <dd>
                            <select name="notifiroles" id="notifiroles" multiple data-placeholder="<indaba:msg key='cp.text.choose_role'  />" class="long-input validate[required]">
                            </select>
                        </dd>
                    </dl>
                    <dl>
                        <dt><label for="subject"><indaba:msg key="cp.label.subject" /></label></dt>
                        <dd><input type="text" name="subject" maxlength="200" id="subject" class="long-input validate[required]"/>
                        <span id="subjectHint" style="margin-left: 10px;color: red"></span></dd>
                    </dl>                    
                    <dl id="body-dl">
                        <dt><label for="notifibody"><indaba:msg key="cp.label.body" /></label></dt>
                        <dd>
                            <textarea id="notifibody" name="notifibody" class="long-input validate[required]"></textarea>
                        </dd>
                    </dl>
                    <dl>
                        <dt><label for="tokens"><indaba:msg key="cp.label.tokens" /></label></dt>
                        <dd><textarea id="tokens" class="tokenset long-input" disabled="disabled" rows="8"></textarea></dd>
                    </dl>
                </fieldset>
            </form>
        </div>
    </div>    
    <jsp:include page="notification-import-inc.jsp" flush="true" />
</div>
<script type="text/javascript" charset="utf-8">
    var categories = {};
    var type2cat = {};
    var langOptions = "";
    var notificationTabInitilized = false;
    $(function(){
        if(!notificationTabInitilized) {
            notificationTabInitilized = true;
            initNotificationOptions();
        }

        //////////////////////////////////////////////////////////////////////////
        //
        // 'Notifications' Flexgrid
        //
        //////////////////////////////////////////////////////////////////////////
        var notificationsForm = $('#notificationsForm');
        //$('input, textarea', notificationsForm).uniform();
        //$("select",notificationsForm).chosen();
        //$('a.ui-btn, button').button();
        //notificationsForm.validationEngine({prettySelect : true,useSuffix: "_chzn",validationEventTrigger:"change"});
        notificationsForm.validationEngine({prettySelect : true,useSuffix: "_chzn",validationEventTrigger:"change"});
        // Dialog
        $('#notificationsFormPopup').dialog({
            autoOpen: false,
            width: 622,
            resizable: false,
            minHeight: 370,
            modal: true,
            open: function(){
                  
            },
            beforeClose: function(){
                resetProjectNotificationForm();
            },
            buttons: {
                "Save": function() {
                    doSaveEditedNotification();
                },
                "Cancel": function() {
                    $('#notificationsFormPopup').dialog('close');
                }
            }
        });
        
        
        $('#submitNotifiFilter').click(function(){
            $('#notifications-flexgrid').flexReload({newp: 1,dataType: 'json'});
            return false;
        });
        $('#resetNotifiFilter').click(function(){
            selectChosenOption($('select#filterLanguageId'), 0);
            selectChosenOption($('select#filterRoleId'), -1);
            selectChosenOption($('select#filterTypeId'), 0);
            $("#notifications-flexgrid").flexReload({newp: 1, dataType: 'json'});
            return false;
        });

        
        
        $("#notifications-flexgrid").flexigrid({
            url: '${contextPath}/proj/notification!find?projId=' + ${projId},
            method: 'POST',
            dataType: 'json',
            colModel : [
                {display: 'ID', name : 'notifid', width : 0, sortable : true, hide: true},
                {display: '<indaba:msg key="cp.ch.type"/>', name : 'typeName', width : 120, sortable : true, align: 'left'},
                {display: '<indaba:msg key="cp.ch.name"/>', name : 'name', width : 100, sortable : true, align: 'left'},
                {display: '<indaba:msg key="cp.ch.description"/>', name : 'description', width : 280, sortable : false, align: 'left'},
                {display: '<indaba:msg key="cp.ch.roles"/>', name : 'roleNames', width : 100, sortable : false, align: 'left'},
                {display: '<indaba:msg key="cp.ch.language"/>', name : 'languageName', width : 70, sortable : true, align: 'left'},
                {display: '<indaba:msg key="cp.ch.actions"  />', name : 'actions', width : 150, sortable : false, align: 'left'}
            ],
            preProcess: function(data) {
                $.each(data.rows, function(index, elem){
                    
                    var notifiId = elem.id;
                    elem.notifid = notifiId;
                     var actions = '<a class="link" href="javascript:void(0)" onclick="return doEditNotification(\''+ notifiId + '\');"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.edit"  /></a>'
                        + '<a class="link" href="" onclick="return doDeleteNotification('+notifiId +', \''+elem.name+'\''+');"><img height="14px" src="${contextPath}/resources/images/delete.png"><indaba:msg key="cp.btn.delete"  /></a>'
                        + '<a class="link clone" href="javascript:void(0)" onclick="return doCloneNotification(\''+ ${projId} + '\',\'' + notifiId +'\');"><img height="14px" src="${contextPath}/resources/images/copy.png"><indaba:msg key="cp.btn.clone"/></a>';
                   
                    elem.actions = actions;
                    if (elem.status == 1) {
                        // active
                        elem.status = '<img height="14px" width="14px" src="${contextPath}/resources/images/task3.gif" title="' + $.i18n.message('cp.text.active_user') + '">';
                    } else {
                        elem.status = '<img height="14px" width="14px" src="${contextPath}/resources/images/task1.gif" title="' + $.i18n.message('cp.text.inactive_user') + '">';
                    }
                });
                return data;
            },
            buttons : [
                {name: '<indaba:msg key="cp.btn.add"  />', bclass: 'add', bimage: '${contextPath}/resources/images/add.png', attrs:[{name:'rel', value: '#'}], onpress : function(){
                        doAddNotification();
                        return false;
                    }
                },
                {name: '<indaba:msg key="cp.btn.delete"/>', bclass: 'massdelete', bimage: '${contextPath}/resources/images/delete.png', attrs:[{name:'rel', value: '#'}], onpress : function(){
                        doGroupDeleteNotification('${contextPath}');
                        return false;
                    }
                },
                {separator: true},
                {name: '<indaba:msg key="cp.btn.import"  />', bclass: 'import', bimage: '${contextPath}/resources/images/import.png', attrs:[{name:'rel', value: '#'}], onpress : function(){
                        doImportNotification();
                        return false;
                    }
                },
                {name: '<indaba:msg key="cp.btn.export"/>', bclass: 'export', bimage: '${contextPath}/resources/images/export.png', attrs:[{name:'rel', value: '#'}], onpress : function(){
                        doGroupExportNotification('${contextPath}');
                        return false;
                    }
                },
                {separator: true},
                {name: '<indaba:msg key="cp.btn.select_all"/>', bclass: 'select', bimage: '${contextPath}/resources/images/uncheck.png', attrs:[{name:'rel', value: '#'}],  onpress : function(){
                        doSelectNotifications('${contextPath}');
                        return false;
                    }
                }
            ],
            warnClass: 'warn',
            resizable: false,
            sortname: "type",
            sortorder: "asc",
            usepager: true,
            title: '<indaba:msg key="cp.title.project_notifications"  />',
            useRp: true,
            rp: 50,
            rpOptions: [30, 50, 100, 200, 500],
            showToggleBtn: false,
            autoload: false,
            width: 865,
            height: '100%',
            //page: 1,
            pagestat: 'Displaying {from} to {to} of {total} items',
            searchitems: [
                {display: '<indaba:msg key="cp.ch.name"/>', name: 'name'},
                {display: '<indaba:msg key="cp.ch.description"/>', name: 'description'}
            ],
            doSearch: function() {
            },
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
                $('select, input', $('#notificationFilterForm')).each(function(){
                    p[p.length] = {name: $(this).attr("name"), value: $(this).val()};
                });
                this.params = p;
                return true;
            }
        });      
    });
        
    function doSaveEditedNotification()
    {
        var mode = $('#mode');
        if(!$('#notificationsForm').validationEngine('validate')){
            return false;
        }            
        $.ajax({
            url: '${contextPath}/proj/notification!save',
            data: { notifiId: $('#notifiId').val(), 
                    projId: ${projId}, 
                    name: $('#name').val(),
                    desc: $('#notifidesc').val(),
                    typeId: $('#type').val(),
                    languageId: $('#notifilanguage').val(),
                    roleIds: $('#notifiroles').val(),
                    subject: $('#subject').val(),
                    mode : $('#mode').val(),
                    body: $('#notifibody').val()
            },
            type: 'POST',
            dataType: 'json',
            success: function(data, textStatus, jqXHR) {
                if (data['ret'] == 0) {
                    $('#notificationsFormPopup').dialog('close');
                    loadNotificationsFlexGrid();
                }
                else {
                    ocsError(data['desc']);
                }
            }
        });
    }
    
    function resetProjectNotificationForm() {
        resetForm($('#notificationsForm'));
        $('#notificationsForm').validationEngine('hideAll');
        //$("#tokens").attr("disabled", "disabled");
    }
    
    function doAddNotification() {
        $('#notificationsForm input').removeAttr('disabled');
        $('#mode').val('ADD');
        selectChosenOptions($('#notifilanguage'), "");
//        $('select#notifilanguage').removeattr("disabled");
//        $('select#notifilanguage').trigger("liszt:updated");
        selectChosenOptions($('#type'), "");
        $('#tokens').val("");
//        $('select#type').removeattr("disabled","");
//        $('select#type').trigger("liszt:updated");
        selectChosenOptions($('#notifiroles'),[-1]);
        $('#notificationsFormPopup').dialog('option', 'title', "<indaba:msg key='cp.title.add_notification' />");
        $('#notificationsFormPopup').dialog('open');
        return false;
    }
    function loadtokens(value) {
        $('#tokens').val(categories[type2cat[value].category].tokens);
        $("#tokens").attr("disabled", true);
        // $.uniform.update();
    }
    
    function initialFormValue(data, mode) {  
        $('#notifiId').val(data.id);
        $('#name').val(data.name);
        $('#notifidesc').val(data.description);
        $('#subject').val(data.subjectText);
        $('#notifibody').val(data.bodyText);

        selectChosenOptions($('#type'), [data.notificationTypeId]);
        loadtokens(data.notificationTypeId);
        // $('select#type').attr("disabled","disabled");
        $('select#type').trigger("liszt:updated");
        selectChosenOptions($('#notifilanguage'), [data.languageId]);
        // $('select#notifilanguage').attr("disabled","disabled");
        $('select#notifilanguage').trigger("liszt:updated");
        if (mode == "EDIT") {
            selectChosenOptions($('#notifiroles'), data.roleIds);
            // $('select#notifiroles').attr("disabled","disabled");
            // $('select#notifiroles').trigger("liszt:updated");
        }
        else if (mode == "CLONE") {
            selectChosenOptions($('#notifiroles'), [-1]);
        }
    }
    
    function doEditNotification(notifiId) {
     
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url:'${contextPath}/proj/notification!get',
            data: {
                notifiId:notifiId
            },
            success: function(resp){
                if(resp.ret == 0) {
                    initialFormValue(resp.data, "EDIT");
                    $('#mode').val('EDIT');
                    //$('#notificationsForm input').attr('disabled', 'disabled');
                    $('#notificationsFormPopup').dialog('option', 'title', "<indaba:msg key='cp.title.edit_notification' />");
                    $('#notificationsFormPopup').dialog('open');
                }
            }
        });
        return false;        
    }
    
    function doDeleteNotification(id, name) {
        var idList = []; idList[0]=id;
        var nameList = []; nameList[0]=name;
        doDeleteSetNotifications('${contextPath}', idList, nameList);
        return false;
    }
    
    function removeNotification() {
        var checked = ('checked' == $('#rmNotify').attr('checked')) ? true : false;
        $.ajax({
            url: '${contextPath}/proj/notification!delete',
            data: {notifiId: $('#rmNotifiId').val(), rmNotify: checked, removeNote: $('#rmNote').val()},
            type: 'POST',
            dataType: 'json',
            success: function(data, textStatus, jqXHR) {
                if (data['ret'] != 0) {
                    ocsError(data.desc);
                }
                else {
                    loadNotificationsFlexGrid();
                }
            }
        });
        return false;
    }
    //
    function doNotificationAjaxRequest(contextPath, handler, reqData, success, error) {
       $.ajax({
           type: 'POST',
           dataType: 'json',
           url: contextPath + '/proj/notification!'+handler,
           data: reqData,
           success: function(resp){
               if(resp.ret == 0) {
                   handleRetReports(resp.data);
                   $("#notifications-flexgrid").flexReload({
                       dataType: 'json'
                   });
               }
           }
       });
       return false;
    }   
    function handleRetReports(reportList) {
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
    function getSelectedNotificationNames() {
        var nameList = [];
        $('tbody tr.trSelected', $('#notifications-flexgrid')).each(function(){
            nameList[nameList.length] = $('td[abbr=name]>div', $(this)).text();
        });
        return nameList;
    }
    function getSelectedNotificationIds() {
        var idList = [];
        $('tbody tr.trSelected', $('#notifications-flexgrid')).each(function(){
            idList[idList.length] = $('td[abbr=notifid]>div', $(this)).text();
        });
        return idList;
    }
    
    function doGroupDeleteNotification(contextPath){
        if(!checkNotificationSelected()) {
            return false;
        }
        var idList = getSelectedNotificationIds();
        var nameList = getSelectedNotificationNames();
        doDeleteSetNotifications(contextPath, idList, nameList);
        return false;
    }    
    
    function doDeleteSetNotifications(contextPath, idList, nameList){
       var msg = $.i18n.message('cp.text.confirm_delete_notification');

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
               return doNotificationAjaxRequest(contextPath, 'groupdelete', {
                   idList: idList
               });
           }
           return false;
       });
       return false;
   }
    
    function doSelectNotifications(contextPath) {
        var elem = $('#notificationsFlexGrid div.flexigrid div.tDiv2 a.fbutton span.select');
        var rowElems = $('#notifications-flexgrid tbody tr');
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
    function checkNotificationSelected() {
        var selected = $('tbody tr.trSelected', $('#notifications-flexgrid')).length;
        if(!selected) {
            $('#freeow-br').freeow($.i18n.message('cp.title.warning'), $.i18n.message('cp.error.no_notification_selected'), {
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
       
    function doGroupExportNotification(contextPath){
        if(!checkNotificationSelected()) {
            return false;
        }
        var idList = getSelectedNotificationIds();
        var idOptions = "";
        $.each(idList, function(index, val){
            idOptions += '<input name="idList" style="display: none;" value='+val+' checked="checked" />'
        });

        var ocsDlg = $('<div id="groupExportDlg">' +
            'Are you sure to export the selected notifications?' +
            '<form id="exportForm" name="exportForm" method="POST" action="'+contextPath + '/proj/notification!export" target="_self">' +
            '<div id="ocsDlg" class="dlg">' + idOptions +
            '</div></form></div>');

        ocsDlg.dialog({
            width: 460,
            title: $.i18n.message('cp.title.export_notifications'),
            resizable: false,
            modal: true,
            open: function(){               
            },
            close: function() {
                ocsDlg.parents('div.ui-dialog').remove();
                $('#groupExportDlg').remove();
            },
            buttons: {
                "Export": function() {                    
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

    function doCloneNotification(projId, notifiId) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url:'${contextPath}/proj/notification!get',
            data: {
                notifiId:notifiId
            },
            success: function(resp){
                if(resp.ret == 0) {
                    initialFormValue(resp.data, "CLONE");
                    $('#mode').val('CLONE');
                    //$('#notificationsForm input').attr('disabled', 'disabled');
                    $('#notificationsFormPopup').dialog('option', 'title', "<indaba:msg key='cp.title.clone_notification' />");
                    $('#notificationsFormPopup').dialog('open');
                }
            }
        });
        return false;        

    }
    
    function initNotificationOptions() {
        $.ajax({
            url: '${contextPath}/proj/notification!getAllOptions',
            data: {projId: ${projId}},
            type: 'POST',
            dataType: 'json',
            success: function(data, textStatus, jqXHR) {
                var form = $('#notificationFilterForm');
                langOptions = "";
                if(data.languages) {
                    setOptions($('#notificationsForm select#notifilanguage'), data.languages);
                    var filterLangs = [{id: 0, name: '<indaba:msg key="cp.text.all"  />'}];
                    for(var i = 0; i < data.languages.length; ++i) {
                        filterLangs.push(data.languages[i]);
                        langOptions += '<option value="' + data.languages[i].id +'"> ' + data.languages[i].name + '</option>';
                    }
                    setOptions($('#notificationFilterForm select#filterLanguageId'), filterLangs, null, null, false, true);
                    selectChosenOption($('#notificationFilterForm select#filterLanguageId'), 0);
                }
                if (data.ntypes) {
                    setOptions($('#notificationsForm select#type'), data.ntypes);
                    var filterTypes = [{id: 0, name: '<indaba:msg key="cp.text.all"  />'}];
                    for(var i = 0; i < data.ntypes.length; ++i) {
                        filterTypes.push(data.ntypes[i]);
                        type2cat[data.ntypes[i].id]= { category : data.ntypes[i].category };
                    }
                    setOptions($('#notificationFilterForm select#filterTypeId'), filterTypes, null, null, false, true);
                    selectChosenOption($('#notificationFilterForm select#filterTypeId'), 0);
                }
                if(data.roles) {
                    setOptions($('#notificationsForm select#notifiroles'), data.roles);
                    var filterRoles = [{id: -1, name: '<indaba:msg key="cp.text.all"  />'}];
                    for(var i = 0; i < data.roles.length; ++i) {
                        filterRoles.push(data.roles[i]);
                    }
                    setOptions($('#notificationFilterForm select#filterRoleId'), filterRoles, null, null, false, true);
                    selectChosenOption($('#notificationFilterForm select#filterRoleId'), -1);
               }
               
               if (data.categories) {
                    for(var i = 0; i < data.categories.length; ++i) {
                        categories[data.categories[i].category] = { 
                            id : data.categories[i].id,
                            tokens : data.categories[i].tokens };
                    }
               }
               
               loadNotificationsFlexGrid();
            }
        });
    }

    function loadNotificationsFlexGrid() {
        $("#notifications-flexgrid").flexReload({newp: 1,dataType: 'json'});
    }
</script>