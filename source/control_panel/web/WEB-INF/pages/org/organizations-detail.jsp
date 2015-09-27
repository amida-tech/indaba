<%-- 
    Document   : libraries
    Created on : May 20, 2012, 11:20:21 AM
    Author     : jiangjeff
--%>

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
    <body>
        <div id="indaba">
            <div class="wrapper">
                <jsp:include page="../head.jsp" flush="true">
                    <jsp:param name="active" value="organizations" />
                    <jsp:param name="content" value="organizations" />
                </jsp:include>
            </div>
            <jsp:include page="../footer.jsp" flush="true" />
        </div>
        <div id="organizations" class="hidden">
            <div id="content">
                <div class="container">
                    <div id="org-detail-tabs" style="margin:10px; padding-bottom: 30px; background: #eaeaea">
                        <ul>
                            <li><a href="#basic-info-tab"><indaba:msg key="cp.label.base_information" /></a></li>
                            <li><a href="#oa-tab"><indaba:msg key="cp.label.administrators" /></a></li>
                            <li><a href="#key-tab"><indaba:msg key="cp.label.security_keys" /></a></li>
                        </ul>

                        <div id="basic-info-tab">
                            <div id="org-basic" style="background-color: white;padding: 10px;">
                                <jsp:include page="org-basic-form.jsp" flush="true" />
                                <br/>
                                <div style="margin-left: auto;margin-right: auto;width:200px">
                                    <a href="javascript:void(0)" id="save-org-btn" onclick='return saveOrgBasic("${contextPath}", "${orgid}")'><indaba:msg key="cp.btn.save" /></a>
                                    <span style="margin-left: 10px;margin-right: 10px"></span>
                                    <a href="javascript:void(0)" id="reload-org-btn" onclick='return loadOrgBasic("${contextPath}", "${orgid}")'><indaba:msg key="cp.btn.reset" /></a>
                                    <span style="margin-left: 10px;margin-right: 10px"></span>
                                </div>
                            </div>
                        </div>
                        <div id="oa-tab">
                            <div id="org-oa"  style="width:834px">
                                <div id="oa-grid"></div>
                            </div>                            
                        </div>
                        <div id="key-tab">
                            <div id="org-key" style="width:868px">
                                <div id="key-grid"></div>
                            </div>                            
                        </div>
                    </div>
                </div>
                <!--<div class="spacing-line"></div>-->
            </div>
        </div>
        <jsp:include page="org-add-oa-dialog.jsp" flush="true">
            <jsp:param name="orgid" value="${orgid}"/>
        </jsp:include>
        <jsp:include page="org-add-key-dialog.jsp" flush="true">
            <jsp:param name="orgid" value="${orgid}"/>
        </jsp:include>
        <jsp:include page="org-key-pwd-dialog.jsp" flush="true">
            <jsp:param name="orgid" value="${orgid}"/>
        </jsp:include>
        <jsp:include page="org-revoke-key-dialog.jsp" flush="true">
            <jsp:param name="orgid" value="${orgid}"/>
        </jsp:include>
        <jsp:include page="org-edit-key-dialog.jsp" flush="true">
            <jsp:param name="orgid" value="${orgid}"/>
        </jsp:include>
    </body>
    <script type="text/javascript">
        $(function(){
            $('input').uniform();
            //$('select').uniform();
            $('select').chosen();
            $('textarea').uniform();
            $('#save-org-btn').button(); 
            $('#reload-org-btn').button();
            $('#back-btn').button();
            $("#org-detail-tabs").tabs({ selected: 0, 
                show: function(event, ui){fixDHTMLXTabbarHeight("content", 40)},
                select: function(event, ui){
                    // clear security key data anyway
                     $('#key-grid tbody').empty();
                    // handle new selection
                    var selected = ui.index;
                    if(selected == 1) {
                        $('#oa-grid').flexReload({dataType:'json'});
                    }
                    else if(selected == 2){
                        $('#key-pwd-popup').dialog('option', 'title', 'Please Enter Your Password');
                        $('#key-pwd-popup').dialog('open');
                    }
                }});
            load_org_basic_info("${contextPath}", "${orgid}");
            $('#oa-grid').flexigrid({
                url: '${contextPath}/organizations!getOAs?id=${orgid}',
                method: 'POST',
                dataType: 'json',
                preProcess: function(data) {
                    if (data.poa != "yes") {
                        $('.addOA').parent().parent().hide();
                    }

                    for (var i = 0; i < data.rows.length; ++i) {
                        
                        if (data.rows[i].actions == "delete") {                
                            data.rows[i].links =
                                '<a class="link" href="javascript:void(0)" onclick="return doDeleteOA(\'${contextPath}\',' +
                                data.rows[i].id + ',\''+ data.rows[i].userName +'\');"><img height="14px" src="${contextPath}/resources/images/delete.png"><indaba:msg key="cp.btn.delete" /></a>';
                        } else {
                            data.rows[i].links = ' ';
                        }
                        
                        if (data.rows[i].actions == "poa") {
                            data.rows[i].type = '<font color="red"><indaba:msg key="cp.oatype.primary"/></font>';
                        } else {
                            data.rows[i].type = '<indaba:msg key="cp.oatype.secondary" />';
                        }
                    }
                    return data;
                },
                colModel: [ {display:'<indaba:msg key="cp.ch.first_name" />', name:'firstName', width:120, sortable:false, align:'left'},
                            {display:'<indaba:msg key="cp.ch.last_name" />', name:'lastName', width:120, sortable:false, align:'left'},
                            {display:'<indaba:msg key="cp.ch.user_name" />', name:'userName', width:120, sortable:false, align:'left'},
                            {display:'<indaba:msg key="cp.ch.email" />', name:'email', width:220, sortable:false, align:'left'},
                            {display:'<indaba:msg key="cp.ch.type" />', name:'type', width:90, sortable:false, align:'left'},
                            {display:'<indaba:msg key="cp.ch.actions" />', name:'links', width:90, sortable:false, align:'left'}],
                buttons: [ {name: 'Add', bclass: 'addOA', bimage: '${contextPath}/resources/images/add.png', onpress: function() {
                        $('#add-oa-popup').dialog('option', 'title', '<indaba:msg key="cp.label.add_admin" />');
                        $('#add-oa-popup').dialog('open');
                    }}],
                warnClass: 'warn',
                resizable: 'false',
                usepager: false,
                title: '<indaba:msg key="cp.label.administrators" />',
                useRp: false,
                rp: 10,
                showTableToggleBtn: false,
                showToggleBtn : false,
                width: 'auto',
                height: '100%',
                autoload: false,
                pagestat: 'Displaying {from} to {to} of {total} items',
                onError: function(){
                    fixDHTMLXTabbarHeight("content");
                },
                onNoData: function(){
                    fixDHTMLXTabbarHeight("content");
                },
                onSuccess: function(){
                    fixDHTMLXTabbarHeight("content");
                }
            });
            
            $('#key-grid').flexigrid({
                url: '${contextPath}/organizations!getKeys?id=${orgid}',
                method: 'POST',
                dataType: 'json',
                preProcess: function(data) {
                    for (var i = 0; i < data.rows.length; ++i) {
                        if (data.rows[i].statusId == 1) {
                            data.rows[i].actions =
                                '<a class="link" href="javascript:void(0)" onclick="return doRevokeKey(\'${contextPath}\',' +
                                data.rows[i].id + ');"><img height="14px" src="${contextPath}/resources/images/revoke.png"><indaba:msg key="cp.btn.revoke"/></a>' +
                                '<a class="link" href="javascript:void(0)" onclick="return doEditKey(\'${contextPath}\',' + 
                                data.rows[i].id + ');"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.edit"/></a>';
                        } else {
                            data.rows[i].actions = ' ';
                        }
                    }
                    return data;
                },
                colModel: [ {display:'<indaba:msg key="cp.ch.version" />', name:'version', width:55, sortable:false, align:'left'},
                            {display:'<indaba:msg key="cp.ch.algorithm" />', name:'hashAlgorithm', width:70, sortable:false, align:'left'},
                            {display:'<indaba:msg key="cp.ch.issue_date" />', name:'issueTime', width:90, sortable:false, align:'left'},
                            {display:'<indaba:msg key="cp.ch.effective_date" />', name:'effectiveTime', width:90, sortable:false, align:'left'},
                            {display:'<indaba:msg key="cp.ch.expirry_date" />', name:'expiryTime', width:90, sortable:false, align:'left'},
                            {display:'<indaba:msg key="cp.ch.status" />', name:'status', width:60, sortable:false, align:'left'},
                            {display:'<indaba:msg key="cp.ch.data" />', name:'data', width:200, sortable:false, align:'left'},
                            {display:'<indaba:msg key="cp.ch.actions" />', name:'actions', width:115, sortable:false, align:'left'}],
                buttons: [ {name: '<indaba:msg key="cp.btn.add" />', bclass: 'addKey', bimage: '${contextPath}/resources/images/add.png', onpress: function() {
                        $('#add-key-popup').dialog('option', 'title', '<indaba:msg key="cp.label.add_security_key" />');
                        $('#add-key-popup').dialog('open');
                    }}],
                warnClass: 'warn',
                resizable: 'false',
                usepager: false,
                title: '<indaba:msg key="cp.label.security_keys" />',
                useRp: false,
                rp: 10,
                showToggleBtn: false,
                showTableToggleBtn: false,
                width: 'auto',
                height: '100%',
                autoload: false,
                pagestat: 'Displaying {from} to {to} of {total} items',
                onError: function(){
                    fixDHTMLXTabbarHeight("content");
                },
                onNoData: function(){
                    fixDHTMLXTabbarHeight("content");
                },
                onSuccess: function(){
                    fixDHTMLXTabbarHeight("content");
                }
            });
            
        });
        /*
        function doRevokeKey(contextPath, keyId) {
                $('#revoke-key-popup').dialog('option', 'title', 'Revoke Security Key');
                $('#revoke-key-popup').dialog({buttons:[{
                    text: 'Revoke2',
                    click: function(){revoke_org_key(contextPath, keyId, function(){ $('#key-grid').flexReload();$(this).dialog('close');}, function(){});}
                },{
                    text: 'Cancel2',
                    click: function(){$(this).dialog('close');}
                }]});
                $('#revoke-key-popup').dialog('open');
                return false; 
        }
        
        function doEditKey(contextPath, keyId) {
            
        }*/
        
        function doDeleteOA(contextPath, oaId, userName) {
                ocsConfirm($.i18n.message('cp.text.confirm_delete_orgadmin', [userName]), $.i18n.message('cp.title.confirm'),
                function(choice){
                    if(choice){
                        remove_org_oa(oaId, function(){$('#oa-grid').flexReload({dataType:'json'});}, function(){});
                    }
                });
                return false;            
        }
        
        function saveOrgBasic(contextPath, orgId) {
            update_org_basic(contextPath, orgId);
        }
        
        function loadOrgBasic(contextPath, orgId) {
            load_org_basic_info(contextPath, orgId);
        }
        
        function back2Orgs() { window.location.href="${contextPath}/organizations"; }
    </script>
</html>
