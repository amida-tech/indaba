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
        <title><indaba:msg key="cp.title.indaba_control_panel" /> - Target Library Settings</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link REL="SHORTCUT ICON" href="${contextPath}/resources/images/indaba_icon.ico"/>
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/css/style.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/uniform/uniform.default.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/jqtransform/jqtransform.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/dhtmlx/dhtmlx.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/chosen/chosen.css"/>
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/flexgrid/jquery.flexigrid.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/jquery-ui/css/jquery-ui-1.8.21.custom.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/freeow/jquery.freeow.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/loading/loading.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/dialog/ocs-dlg.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/validate/css/validationEngine.jquery.css" type="text/css"/>


        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/ocs-common.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/json2.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/dhtmlx/dhtmlx.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/jquery-1.7.2.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/jquery.tools.min.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/jquery.form.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/jquery.cookie.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/jquery-ui/js/jquery-ui-1.8.21.custom.min.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/jqtransform/jquery.jqtransform.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/flexgrid/jquery.flexigrid.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/chosen/jquery.chosen.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/uniform/jquery.uniform.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/freeow/jquery.freeow.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/loading/jquery.bgiframe.min.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/loading/loading.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/dialog/ocs-dlg.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/validate/js/languages/jquery.validationEngine-en.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/validate/js/jquery.validationEngine.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/jquery.i18n.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/jsI18nMessages"></script>
    </head>
    <body onload="doOnLoad();">
        <div id="indaba">
            <div class="wrapper">
                <jsp:include page="../head.jsp" flush="true">
                    <jsp:param name="active" value="targetLib" />
                    <jsp:param name="content" value="targetLib" />
                </jsp:include>
            </div>
            <jsp:include page="../footer.jsp" flush="true" />
        </div>
        <div id="targetLib" class="hidden">
            <div id="content">
                <div id="targetLibToolbar"></div>
                <div class="note">
                    <div>
                        <img width="16px" src="${contextPath}/resources/images/warn.png"/>
                        <indaba:msg key="cp.text.change_notes"/>
                    </div>
                </div>
                <jsp:include page="target-filter-inc.jsp" flush="true" />
                <div id="query-result">
                    <div id="target-flexgrid"></div>
                    <div id="freeow-br" class="freeow freeow-top-right"></div>
                </div>
                <!--<div class="spacing-line"></div>-->
            </div>
        </div>
        <jsp:include page="target-form-inc.jsp" flush="true" />
    </body>
</html>

<script type="text/javascript" charset="utf-8">
    var ctxPath;

    function _doEditTarget(targetId, viewonly) {
        doEditTarget(ctxPath, targetId, viewonly);
    }

    function doOnLoad() {
         ctxPath = '${contextPath}';

        initTargetLibToolbar('${visibility}', '${contextPath}');
        $("#target-filter-form").jqTransform();
            
        $('#submitFilter').click(function(){
            $('#target-flexgrid').flexReload({newp: 1,dataType: 'json'});
            return false;
        });
        $('#resetFilter').click(function(){
            $("form.target-filter-form")[0].reset();
            $("#target-flexgrid").flexReload({newp: 1, dataType: 'json'});
            return false;
        });

        $("#target-flexgrid").flexigrid({
            url: '${contextPath}/lib/target-lib!find',
            method: 'POST',
            dataType: 'json',
            preProcess: function(data) {
                if (data.canAddTargets != 'yes') {
                    $('.addtarget').parent().parent().hide();
                }

                $.each(data.rows, function(index, elem){
                    var targetId = elem.id;
                    var actions = "";

                    if (elem.editable) {
                        actions += '<a class="link" href="javascript:void(0)" onclick="return _doEditTarget(' + targetId + ', false' + ');"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.edit"/></a>';
                    } else {
                        actions += '<a class="link" href="javascript:void(0)" onclick="return _doEditTarget(' + targetId + ', true' + ');"><img height="14px" src="${contextPath}/resources/images/view.png"><indaba:msg key="cp.btn.view"/></a>';
                    }

                    elem.actions = actions;
                });
                return data;
            },
            colModel : [
                {display: '<indaba:msg key="cp.ch.id"/>', name : 'id', width : 0, sortable : false, align: 'right', hide: true},
                {display: '<indaba:msg key="cp.ch.name"/>', name : 'name', width : 150, sortable : true, align: 'left'},
                {display: '<indaba:msg key="cp.ch.shortname"/>', name : 'shortName', width : 150, sortable : true, align: 'left'},
                {display: '<indaba:msg key="cp.ch.description"/>', name : 'description', width : 200, sortable : true, align: 'left'},
                {display: '<indaba:msg key="cp.ch.organization"/>', name : 'orgname', width : 200, sortable : true, align: 'left'},
                {display: '<indaba:msg key="cp.ch.actions"/>', name : 'actions', width : 50, sortable : false, align: 'left'}
            ],
            buttons : [
                {name: '<indaba:msg key="cp.btn.add" />', bclass: 'addtarget', bimage: '${contextPath}/resources/images/add.png', /*attrs:[{name:'rel', value: '#editIndicator'}],*/ onpress : function(){
                        setViewMode(false);
                        $('#targetFormPopup').dialog('option', 'title', 'Add Target');

                        var p = $('#targetForm');
                        $('input[name=id]', p).val(-1);

                        $('#targetFormPopup').dialog('open');
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
            rp: 50,
            rpOptions: [30, 50, 100, 200, 500],
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
    }
</script>
<script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/indaba.target-lib.js"></script>