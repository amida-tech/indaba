<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC"-//W3C//DTD XHTML 1.0 Transitional//EN">

<!--
To change this template, choose Tools | Templates
and open the template in the editor.
-->
<!DOCTYPE html>
<html>
    <head>
        <title><indaba:msg key="cp.title.indaba_control_panel" /> - Organizations Settings</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link REL="SHORTCUT ICON" href="${contextPath}/resources/images/indaba_icon.ico"/>
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/css/style.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/uniform/uniform.default.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/jqtransform/jqtransform.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/dhtmlx/dhtmlx.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/dhtmlx/themes/message_skyblue.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/flexgrid/jquery.flexigrid.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/jquery-ui/css/jquery-ui-1.8.21.custom.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/freeow/jquery.freeow.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/loading/loading.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/dialog/ocs-dlg.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/validate/css/validationEngine.jquery.css" type="text/css"/>
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/chosen/chosen.css"/>

        <!--<link rel="stylesheet" type="text/css" href="${contextPath}/resources/css/orgs.css">-->

        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/ocs-common.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/json2.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/dhtmlx/dhtmlx.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/dhtmlx/message.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/jquery-1.7.2.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/jquery.tools.min.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/jquery.form.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/flexgrid/jquery.flexigrid.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/jquery.cookie.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/jquery-ui/js/jquery-ui-1.8.21.custom.min.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/jqtransform/jquery.jqtransform.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/uniform/jquery.uniform.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/freeow/jquery.freeow.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/loading/jquery.bgiframe.min.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/loading/loading.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/dialog/ocs-dlg.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/validate/js/languages/jquery.validationEngine-en.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/validate/js/jquery.validationEngine.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/chosen/jquery.chosen.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/jquery.i18n.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/jsI18nMessages"></script>
    </head>
    <body onload="doOnLoad();">
        <div id="indaba">
            <div class="wrapper">
                <jsp:include page="../head.jsp" flush="true">
                    <jsp:param name="active" value="notifications" />
                    <jsp:param name="content" value="notifications" />
                </jsp:include>
            </div>
            <jsp:include page="../footer.jsp" flush="true" />
        </div>
        <div id="notifications" class="hidden">
            <div id="content">
                <div class="clear"></div>
                <jsp:include page="notifications-filter-inc.jsp" flush="true" />
                <div id="notificationsGrid" class="container">
                    <div id="notifications-grid"></div>
                    <div id="freeow-br" class="freeow freeow-top-right"></div>
                </div>
                <!--<div class="spacing-line"></div>-->
            </div>
        </div>
        <jsp:include page="notifications-form-inc.jsp" flush="true" />
        <jsp:include page="notif-import-inc.jsp" flush="true" />
        <jsp:include page="notifications-view-inc.jsp" flush="true" />
    </body>
</html>
<script type="text/javascript" charset="utf-8">
    function doOnLoad() {
        $("#notification_filter-form").jqTransform();
        $('#submitFilter').click(function(){
            $('#notifications-grid').flexReload({newp: 1,dataType: 'json'});
            return false;
        });
        $('#resetFilter').click(function(){
            $("form.notification_filter-form")[0].reset();
            $("#notifications-grid").flexReload({newp: 1, dataType: 'json'});
            return false;
        });
        
        $('#notifications-grid').flexigrid({
            url: '${contextPath}/notif/notifications!find',
            method: 'POST',
            dataType: 'json',
            preProcess: function(data) {
                
                for (var i = 0; i < data.rows.length; ++i) {
                    
                    data.rows[i].actions = '<a class=" link edit" href="javascript:void(0)" onclick="return doViewNotification(' + data.rows[i].id +');"><img height="14px" src="${contextPath}/resources/images/view.png"><indaba:msg key="cp.btn.view"/></a>' +    
                        '<a class="link" href="javascript:void(0)" onclick="return doEditCloneNotificationItem('+ data.rows[i].id + ', false);"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.edit"/></a>';
                        if (data.rows[i].languageName != 'English') {
                            data.rows[i].actions += '<a class="link" href="" onclick="return doDeleteNotificationItem('+data.rows[i].id +', \''+data.rows[i].name+'\''+');"><img height="14px" src="${contextPath}/resources/images/delete.png"><indaba:msg key="cp.btn.delete"  /></a>'
                        }
                        data.rows[i].actions +='<a class="link clone" href="javascript:void(0)" onclick="return doEditCloneNotificationItem('+ data.rows[i].id + ', true);"><img height="14px" src="${contextPath}/resources/images/copy.png"><indaba:msg key="cp.btn.clone"/></a>';
                }
                return data;
            },
            colModel: [
                {display: 'ID', name : 'id', width : 0, sortable : true, hide: true},
                {display: '<indaba:msg key="cp.ch.type"/>', name : 'typeName', width : 240, sortable : true, align: 'left'},
                {display: '<indaba:msg key="cp.ch.name"/>', name : 'name', width : 100, sortable : true, align: 'left'},
                {display: '<indaba:msg key="cp.ch.subject"/>', name : 'subjectText', width : 280, sortable : false, align: 'left'},
                {display: '<indaba:msg key="cp.ch.language"/>', name : 'languageName', width : 70, sortable : true, align: 'left'},
                {display: '<indaba:msg key="cp.ch.actions"  />', name : 'actions', width : 150, sortable : false, align: 'left'}
            ],
            buttons: [
                {name: '<indaba:msg key="cp.btn.add"  />', bclass: 'add', bimage: '${contextPath}/resources/images/add.png', attrs:[{name:'rel', value: '#'}], onpress : function(){
                        try {
                        $('#notifications-popup').dialog('option', 'title', $.i18n.message('cp.label.add_notification'));
                        $('#mode', $('#notifications-popup')).val("ADD");
                        $('#notifications-popup').dialog('open');
                        return false;
                        }
                        catch(e) {
                            alert(e);
                        }
                    }
                },
                {name: '<indaba:msg key="cp.btn.delete"/>', bclass: 'massdelete', bimage: '${contextPath}/resources/images/delete.png', attrs:[{name:'rel', value: '#'}], onpress : function(){
                        var nameList = [];
                        var idList = [];
                        $('tbody tr.trSelected', $('#notifications-grid')).each(function(){
                            if ($('td[abbr=languageName]>div', $(this)).text() != "English") {
                                nameList[nameList.length] = $('td[abbr=name]>div', $(this)).text();
                                idList[idList.length] = $('td[abbr=id]>div', $(this)).text();
                            }
                        });
                        if (nameList.length == 0) {
                            $('#freeow-br').freeow($.i18n.message('cp.title.warning'), $.i18n.message('cp.error.no_selected_notifications'), {
                                classes: ["gray"], 
                                autoHideDelay:3000, 
                                autoHide: true, 
                                hideDuration: 500
                            });
                            return false;
                        }
                        doDeleteNotificationItems(idList, nameList);
                        return false;
                    }
                },
                {separator: true},
                {name: '<indaba:msg key="cp.btn.import"  />', bclass: 'import', bimage: '${contextPath}/resources/images/import.png', attrs:[{name:'rel', value: '#'}], onpress : function(){
                        doImportNotificationItems();
                        return false;
                    }
                },
                {name: '<indaba:msg key="cp.btn.export"/>', bclass: 'export', bimage: '${contextPath}/resources/images/export.png', attrs:[{name:'rel', value: '#'}], onpress : function(){
                        var idList =[];
                        $('tbody tr.trSelected', $('#notifications-grid')).each(function(){
                                idList[idList.length] = $('td[abbr=id]>div', $(this)).text();
                        });
                        if (idList.length == 0) {
                            $('#freeow-br').freeow($.i18n.message('cp.title.warning'), $.i18n.message('cp.error.no_selected_notifications'), {
                                classes: ["gray"], 
                                autoHideDelay:3000, 
                                autoHide: true, 
                                hideDuration: 500
                            });
                            return false;
                        }
                        var idOptions = "";
                        $.each(idList, function(index, val){
                            idOptions += '<input name="idList" style="display: none;" value='+val+' checked="checked" />'
                        });

                        var ocsDlg = $('<div id="groupExportDlg">' +
                            'Are you sure to export the selected notifications?' +
                            '<form id="exportForm" name="exportForm" method="POST" action="'+'${contextPath}/notif/notifications!export" target="_self">' +
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
                },
                {separator: true},
                {name: '<indaba:msg key="cp.btn.select_all"/>', bclass: 'select', bimage: '${contextPath}/resources/images/uncheck.png', attrs:[{name:'rel', value: '#'}],  onpress : function(){
                        var elem = $('#notificationsGrid div.flexigrid div.tDiv2 a.fbutton span.select');
                        var rowElems = $('#notifications-grid tbody tr');
                        var label = elem.text();
                        elem.empty();
                        if(label == $.i18n.message('cp.btn.select_all')) {
                            elem.append('<img src="'+ '${contextPath}/resources/images/check.png">' + $.i18n.message('cp.btn.unselect_all'));
                            rowElems.attr('class', 'trSelected');
                        } else {
                            elem.append('<img src="'+ '${contextPath}/resources/images/uncheck.png">' + $.i18n.message('cp.btn.select_all'));
                            rowElems.attr('class', 'erow');
                        }
                        return false;
                    }
                }
            ],
            warnClass: 'warn',
            resizable: false,
            sortname: "name",
            sortorder: "asc",
            usepager: true,
            title: '<indaba:msg key="cp.label.notifications"/>',
            useRp: true,
            rp: 50,
            rpOptions: [30, 50, 100, 200],
            showTableToggleBtn: false,
            showToggleBtn: false,
            width: 'auto',
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
                $('select, input', $('#notification_filter-form')).each(function(){
                    p[p.length] = {name: $(this).attr("name"), value: $(this).val()};
                });
                this.params = p;
                return true;
            }
        });
    }

    function doEditCloneNotificationItem(notifiId, bClone) {
        
        var mode = "EDIT";
        if (bClone)
            mode = "CLONE";
        
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url:'${contextPath}/notif/notifications!getById',
            data: {
                notifiId:notifiId
            },
            success: function(resp){
                if(resp.ret == 0) {
                    $('#name',$('#notifications-popup')).val(resp.data.name);
                    $('#subject',$('#notifications-popup')).val(resp.data.subjectText);
                    $('#notifibody',$('#notifications-popup')).val(resp.data.bodyText);
                    $('#notifiId',$('#notifications-popup')).val(notifiId)
                    selectChosenOptions($('select#notifitype',$('#notifications-form')), [resp.data.notificationTypeId]);
                    $('select#notifitype').trigger("liszt:updated");
                    loadtokens(resp.data.notificationTypeId);
                    selectChosenOptions($('select#notifilanguage',$('#notifications-form')),[resp.data.languageId]);
                    $('#mode',$('#notifications-popup')).val(mode);
                    if (mode == 'EDIT')
                        excludeDisableChosenOption(resp.data.languageId);
                    if (bClone)
                        $('#notifications-popup').dialog('option', 'title', "<indaba:msg key='cp.title.clone_notification' />");
                    else
                        $('#notifications-popup').dialog('option', 'title', "<indaba:msg key='cp.title.edit_notification' />");
                    $('#notifications-popup').dialog('open');
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

    function doNotificationItemAjaxRequest(handler, reqData, success, error) {
       $.ajax({
           type: 'POST',
           dataType: 'json',
           url: '${contextPath}/notif/notifications!'+handler,
           data: reqData,
           success: function(resp){
               if(resp.ret == 0) {
                   handleRetReports(resp.data);
                   $("#notifications-grid").flexReload({
                       dataType: 'json'
                   });
               }
           }
       });
       return false;
    }   
    
    function doDeleteNotificationItem(id, name) {
        var idList = []; idList[0]=id;
        var nameList = []; nameList[0]=name;
        doDeleteNotificationItems(idList, nameList);
        return false;
    }

    function doDeleteNotificationItems(idList, nameList){
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
               return doNotificationItemAjaxRequest('delete', {
                   idList: idList
               });
           }
           return false;
       });
       return false;
    }

    function chkSelectedItems() {
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
    
    
    function doViewNotification(notifiId) {
        
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url:'${contextPath}/notif/notifications!getById',
            data: {
                notifiId:notifiId
            },
            success: function(resp){
                if(resp.ret == 0) {
                    $('#vwName',$('#notif-view-popup')).val(resp.data.name);
                    $('#vwType',$('#notif-view-popup')).val(resp.data.typeName);
                    $('#vwLanguage',$('#notif-view-popup')).val(resp.data.languageName);
                    $('#vwSubject',$('#notif-view-popup')).val(resp.data.subjectText);
                    $('#vwNotifibody',$('#notif-view-popup')).val(resp.data.bodyText);
                    $('#notif-view-popup').dialog('option', 'title', "<indaba:msg key='cp.title.view_notification' />");
                    $('#notif-view-popup').dialog('open');
                }
            }
        });
    }
    
</script>

