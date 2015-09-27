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
        <title><indaba:msg key="cp.title.indaba_control_panel" /> - <indaba:msg key="cp.label.surveyConfig" /></title>
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
                    <jsp:param name="active" value="surveyConfig" />
                    <jsp:param name="content" value="surveyConfig" />
                </jsp:include>
            </div>
            <jsp:include page="../footer.jsp" flush="true" />
        </div>
        <div id="surveyConfig" class="hidden">
            <%--<div id="emptyToolbar"></div> --%>
            <div id="surveyConfigToolbar"></div>
            <div id="content">
                <jsp:include page="survey-config-filter-inc.jsp" flush="true" />
                <div id="query-result">
                    <div id="surveyConfig-flexgrid"></div>
                    <div id="freeow-br" class="freeow freeow-top-right"></div>
                </div>
            </div>
        </div>
        <jsp:include page="survey-config-clone-form-inc.jsp" flush="true" />
    </body>
</html>

<script type="text/javascript" charset="utf-8">
    function doOnLoad() {
        initSurveyConfigToolbar('${visibility}', '${contextPath}');
        /*
        $("#surveyConfig-filter-form").find('select').chosen();
        $("#surveyConfig-filter-form").find('select#filterOrgIds').change(function(args, changed){
            if(changed.selected && changed.selected == 0) {
                $(this).find('option[value!=0]').removeAttr('selected');
            } else {
                $(this).find('option[value=0]').removeAttr('selected');
            }
            $(this).trigger("liszt:updated");
            $("#surveyConfig-flexgrid").flexReload({newp: 1, dataType: 'json'});
        });
        */

        $("#surveyConfig-filter-form").jqTransform();
        $("#surveyConfig-filter-form").find('select#visibility').change(function(args, changed){
            $("#surveyConfig-flexgrid").flexReload({newp: 1,
                dataType: 'json'});
        });

        $('#submitFilter').click(function(){
            $("#surveyConfig-flexgrid").flexReload({newp: 1,
                dataType: 'json'});
            return false;
        });


        $("#surveyConfig-flexgrid").flexigrid({
            url: '${contextPath}/surveyconf/survey-config!find',
            method: 'POST',
            dataType: 'json',
            colModel : [
                {display: 'ID', name : 'id', width : 0, sortable : false, hide: true},
                {display: '<indaba:msg key="cp.ch.name"/>', name : 'name', width : 220, sortable : true, align: 'left'},
                {display: '<indaba:msg key="cp.ch.type"/>', name : 'type', width : 40, sortable : true, align: 'left'},
                {display: '<indaba:msg key="cp.ch.organizations"/>', name : 'orgNames', width : 222, sortable : false, align: 'left'},
                {display: '<indaba:msg key="cp.ch.products"/>', name : 'productNames', width : 256, sortable : true, align: 'left'},
                {display: '<indaba:msg key="cp.ch.actions"/>', name : 'actions', width : 160, sortable : false, align: 'left'}
            ],
            preProcess: function(data) {
                $.each(data.rows, function(index, elem){
                    elem.type=elem.tsc?'TSC':'BSC';
                    
                    var orgNames = elem.orgNames;
                    var filterOrgName = $('#surveyConfig-filter-form #organization div.jqTransformSelectWrapper>div>span').text();
                    if(filterOrgName && filterOrgName != 'All') {
                        elem.orgNames = orgNames.replace(filterOrgName, '<span style="color: orangered; font-weight: bold">' +filterOrgName+ '</span>');
                    }
                    
                    var actions = '';
                    if(elem.visibility == 1) { // public
                        actions += '<a class="link clone" href="javascript:void(0)" onclick="return doCloneSurveyConfig(\'${contextPath}\',' + elem.id + ',\'' + elem.name +'\');"><img height="14px" src="${contextPath}/resources/images/copy.png"><indaba:msg key="cp.btn.clone"/></a>';
                    } else if(elem.primaryOwner){
                        actions += '<a class="link clone" href="javascript:void(0)" onclick="return doCloneSurveyConfig(\'${contextPath}\',' + elem.id + ',\'' + elem.name +'\');"><img height="14px" src="${contextPath}/resources/images/copy.png"><indaba:msg key="cp.btn.clone"/></a>';
                    }

                    if(!elem.owned || elem.inActiveUse) {
                        actions+='<a class="link" href="${contextPath}/surveyconf/survey-config!edit?sconfId='+elem.id+'"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.view"/></a>';
                    } else {
                        actions+='<a class="link" href="${contextPath}/surveyconf/survey-config!edit?sconfId='+elem.id+'"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.edit"/></a>';
                    }
                    
                    if (elem.primaryOwner && !elem.inUse) {
                        actions +='<a class="link" href="#" onclick="doDeleteSurveyConfig(\'${contextPath}\','+elem.id+',\''+elem.name+'\'); return false;"><img height="14px" src="${contextPath}/resources/images/delete.png"><indaba:msg key="cp.btn.delete"/></a>';
                    }
                    elem.actions = actions;
                });
                return data;
            },
            buttons : [
                {name: '<indaba:msg key="cp.btn.add"/>', bclass: 'add', bimage: '${contextPath}/resources/images/add.png', attrs:[{name:'rel', value: '#editIndicator'}], onpress : function(){
                        window.location.href="${contextPath}/surveyconf/survey-config!create?visibility=${visibility}";
                        return false;
                    }
                }
            ],
            warnClass: 'warn',
            resizable: false,
            sortname: "iso",
            sortorder: "asc",
            usepager: true,
            title: 'Survey Configurations',
            useRp: true,
            rp: 50,
            rpOptions: [30, 50, 100],
            showTableToggleBtn: false,
            showToggleBtn: false,
            width: 930,
            height: '100%',
            page: 1,
            pagestat: 'Displaying {from} to {to} of {total} items',
            onError: function(){
                fixDHTMLXTabbarHeight("content");
            },
            onNoData: function(){
                fixDHTMLXTabbarHeight("content");
            },
            onSuccess: function(){
                fixDHTMLXTabbarHeight("content", 40);
                if($('#flexgrid tbody tr td[abbr=actions]').length == 0) {
                }
            },
            onSubmit: function(){
                var p = [];
                $('select, input', $('#surveyConfig-filter-form')).each(function(){
                    p[p.length] = {name: $(this).attr("name"), value: $(this).val()};
                });
                this.params = p;
                return true;
            }
        });
    }
</script>
<script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/indaba.surveyConfig.js"></script>