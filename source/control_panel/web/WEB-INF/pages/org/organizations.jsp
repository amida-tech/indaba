<%--
    Document   : organizations
    Created on : May 20, 2012, 11:20:21 AM
    Author     : rick
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
    <body onload="doOnLoad();">
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
                <div class="clear"></div>
                <div class="container">
                    <div id="orgs-grid"></div>
                </div>
                <!--<div class="spacing-line"></div>-->
            </div>
        </div>
        <c:if test="${siteAdmin==true}">
            <jsp:include page="org-add-dialog.jsp" flush="true" />
        </c:if>
    </body>
</html>

<script type="text/javascript" charset="utf-8">
    function doOnLoad() {
        $('#orgs-grid').flexigrid({
            url: '${contextPath}/organizations!find',
            method: 'POST',
            dataType: 'json',
            preProcess: function(data) {
                if (data.siteAdmin != 'yes') {
                    $('.addOrg').parent().parent().hide();
                }

                for (var i = 0; i < data.rows.length; ++i) {
                    data.rows[i].actions =
                        '<a class="link" href="javascript:void(0)" onclick="return showOrgDetail(\'${contextPath}\',' +
                        data.rows[i].id + ');"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.edit"/></a>';
                }
                return data;
            },
            colModel: [
                {display:'ID', name:'id', width:33, sortable:false, align:'left'},
                {display:'<indaba:msg key="cp.ch.name"/>', name:'name', width:200, sortable:true, align:'left'},
                {display:'<indaba:msg key="cp.ch.address"/>', name:'address', width:410, sortable:true, align:'left'},
                {display:'<indaba:msg key="cp.ch.administrator"/>', name:'superAdmin', width:160, sortable:false, align:'left'},
                {display:'<indaba:msg key="cp.ch.actions"/>', name:'actions', width:112, sortable:false, align:'left'}
            ],
            buttons: [
                {name: '<indaba:msg key="cp.btn.add"/>', bclass: 'addOrg', bimage: '${contextPath}/resources/images/add.png', onpress : function(){
                        $('#org-add-popup').dialog('option', 'title', $.i18n.message('cp.label.add_organization'));
                        $('#org-add-popup').dialog('open');
                        return false;
                    }
                }
            ],
            warnClass: 'warn',
            resizable: false,
            sortname: "name",
            sortorder: "asc",
            usepager: true,
            title: '<indaba:msg key="cp.label.organizations"/>',
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
            }
        });
    }

    function showOrgDetail(contextPath, orgId) {
        window.location.href="${contextPath}/organizations!detail?id=" + orgId;
    }
</script>