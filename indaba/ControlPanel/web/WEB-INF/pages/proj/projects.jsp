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
        <title><indaba:msg key="cp.title.indaba_control_panel" /> - <indaba:msg key="cp.label.projects" /></title>
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
                    <jsp:param name="active" value="projects" />
                    <jsp:param name="content" value="projects" />
                </jsp:include>
            </div>
            <jsp:include page="../footer.jsp" flush="true" />
        </div>
        <div id="projects" class="hidden">
            <div id="content">
                <div id="projectsToolbar"></div>
                <jsp:include page="project-filter-inc.jsp" flush="true" />
                <div id="query-result">
                    <div id="project-flexgrid"></div>
                    <div id="freeow-br" class="freeow freeow-top-right"></div>
                </div>
                <!--<div class="spacing-line"></div>-->
            </div>
        </div>
        <!--jsp:include page="project-form-inc.jsp" flush="true" /-->
    </body>
</html>

<script type="text/javascript" charset="utf-8">
    function doOnLoad() {
        initProjectsToolbar('${visibility}', '${contextPath}');
        var curPage = 1;<c:if test="${page != null && page > 0}">curPage = ${page};</c:if>
        
        $("#project-filter-form").jqTransform();
            
        $('#submitFilter').click(function(){
            $("#project-flexgrid").flexReload({newp: 1,
                dataType: 'json'});
            return false;
        });
       

        $("#project-flexgrid").flexigrid({
            url: '${contextPath}/proj/projects!find',
            method: 'POST',
            dataType: 'json',
            colModel : [
                {display: 'ID', name : 'id', width : 0, sortable : false, hide: true},
                {display: '<indaba:msg key="cp.ch.name"/>', name : 'name', width : 220, sortable : true, align: 'left'},
                {display: '<indaba:msg key="cp.ch.primary_owner"/>', name : 'orgName', width : 220, sortable : true, align: 'left'},
                {display: '<indaba:msg key="cp.ch.secondary_owner"/>', name : 'secondaryOrgNames', width : 218, sortable : false, align: 'left'},
                {display: '<indaba:msg key="cp.ch.project_admin"/>', name : 'displayAdminUsers', width : 150, sortable : true, align: 'left'},
                {display: '<indaba:msg key="cp.ch.actions"/>', name : 'actions', width : 60, sortable : false, align: 'left'}
            ],
            preProcess: function(data) {
                $.each(data.rows, function(index, elem){
                    var projId = elem.id;
                    var orgName = elem.orgName;
                    var secondOrgNames = elem.secondaryOrgNames;
                    var filterOrgName = $('#project-filter-form #organization div.jqTransformSelectWrapper>div>span').text();
                    if(filterOrgName && filterOrgName != 'All') {
                       elem.orgName = orgName.replace(filterOrgName, '<span style="color: orangered; font-weight: bold">' +filterOrgName+ '</span>');
                       elem.secondaryOrgNames = secondOrgNames.replace(filterOrgName, '<span style="color: orangered; font-weight: bold">' +filterOrgName+ '</span>');
                    }
                    
                    var actions = '<a class="link" href="javascript:void(0)" onclick="return doEditProject(\'${contextPath}\','+ projId +');"><img height="14px" src="${contextPath}/resources/images/edit.png"><indaba:msg key="cp.btn.edit"/></a>';
                    elem.actions = actions;
                });
                return data;
            },
            buttons : [
    <c:if test="${user.siteAdmin}">
                    {name: '<indaba:msg key="cp.btn.add"/>', bclass: 'add', bimage: '${contextPath}/resources/images/add.png', attrs:[{name:'rel', value: '#editIndicator'}], onpress : function(){
                            var orgId = $('#project-filter-form select[name=filterOrgId]').val();
                            if(typeof(orgId) == "undefined") {
                                orgId = -1;
                            }
                            var page = $('#query-result .flexigrid .pGroup .pcontrol input').val();
                            if(typeof(page) == "undefined") {
                                page = 1;
                            }
                            window.location.href="${contextPath}/proj/projects!projectForm?visibility=${visibility}"+
                                "&projId=-1" +
                                "&filterOrgId="+ orgId+
                                "&page="+ page;
                            return false;
                        }
                    }
    </c:if>
                ],
                warnClass: 'warn',
                resizable: false,
                sortname: "iso",
                sortorder: "asc",
                usepager: true,
                title: 'Projects',
                useRp: true,
                rp: 50,
                rpOptions: [30, 50, 100, 200],
                showTableToggleBtn: false,
                showToggleBtn: false,
                width: 930,
                height: '100%',
                page: curPage,
                pagestat: 'Displaying {from} to {to} of {total} items',
                onError: function(){
                    fixDHTMLXTabbarHeight("content");
                },
                onNoData: function(){
                    fixDHTMLXTabbarHeight("content");
                },
                onSuccess: function(){
                    fixDHTMLXTabbarHeight("content");
                    if($('#flexgrid tbody tr td[abbr=actions]').length == 0) {
                    }
                    //$('.fbutton[rel]').overlay();
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
        }
        function doEditProject(contextPath, projId) {
            window.location.href= contextPath + "/proj/projects!projectForm?visibility=${visibility}" +
                "&projId=" + projId;
            return false;
        }
</script>
<script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/indaba.projects.js"></script>