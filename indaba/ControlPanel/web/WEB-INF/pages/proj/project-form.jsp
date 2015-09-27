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
        <title><indaba:msg key="cp.title.indaba_control_panel" /> - <c:choose><c:when test="${(projId <= 0)}"><indaba:msg key="cp.title.add_project" /></c:when><c:otherwise><indaba:msg key="cp.title.edit_project" /></c:otherwise></c:choose></title>
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
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/upload/fileuploader.css">
        <link rel="stylesheet" type="text/css" href="${contextPath}/resources/plugins/validate/css/validationEngine.jquery.css" type="text/css"/>

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
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/ocs-common.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/plugins/upload/ajaxfileupload.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/jquery.i18n.js"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/jsI18nMessages"></script>
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/indaba.projects.js"></script>
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
                <div style="margin:10px;">
                    <a class="btn gray" href="${contextPath}/proj/projects?visibility=${visibility}">
                        <&nbsp;&nbsp;<indaba:msg key="cp.btn.back_to_proj_list"/>
                    </a>
                    <c:if test="${projId>0}">
                        <span style="border: 1px solid #ddd; padding: 5px; float: right; background-color: #fafafa">
                            <span><strong><indaba:msg key="cp.text.project"/>:</strong>
                                <label id="nameLabel"></label>
                            </span>
                        </span>
                    </c:if>
                </div>
                <div id="projectTabs" style="margin:10px; background: #f8f8f8">
                    <ul>
                        <li><a href="#projectBaseTab"><indaba:msg key="cp.tab2.project.base_info"  /></a></li>
                        <c:if test="${projId > 0}">
                            <c:if test="${isSA}">
                                <li><a href="#projectOrgsTab"><indaba:msg key="cp.tab2.project.organizations"  /></a></li>
                                <li><a href="#projectAdminsTab"><indaba:msg key="cp.tab2.project.administrators"  /></a></li>
                            </c:if>
                            <li><a href="#projectRolesTab"><indaba:msg key="cp.tab2.project.roles"  /></a></li>
                            <li><a href="#projectTargetsTab" onclick="loadTargetFlexGrid();"><indaba:msg key="cp.tab2.project.targets"  /></a></li>
                            <li><a href="#contributorsTab"><indaba:msg key="cp.tab2.project.contributors"  /></a></li>
                            <li><a href="#productsTab" onclick="loadProductsFlexGrid();"><indaba:msg key="cp.tab2.project.products"  /></a></li>
                            <li><a href="#userfindersTab" onclick="loadUserfinderFlexGrid();"><indaba:msg key="cp.tab2.project.userfinders"  /></a></li>
                            <li><a href="#notificationsTab" onclick="loadNotificationsFlexGrid();"><indaba:msg key="cp.tab2.project.notifications"  /></a></li>
                        </c:if>
                    </ul>
                    <jsp:include page="./tabs/baseinfo-inc.jsp" flush="true" />
                    <c:if test="${projId > 0}">
                        <c:if test="${isSA}">
                            <jsp:include page="./tabs/orgs-inc.jsp" flush="true" />
                            <jsp:include page="./tabs/admins-inc.jsp" flush="true" />
                        </c:if>
                        <jsp:include page="./tabs/roles-inc.jsp" flush="true" />
                        <jsp:include page="./tabs/targets-inc.jsp" flush="true" />
                        <jsp:include page="./tabs/contributors-inc.jsp" flush="true" />
                        <jsp:include page="./tabs/products-inc.jsp" flush="true" />
                        <jsp:include page="./tabs/userfinders-inc.jsp" flush="true" />
                        <jsp:include page="./tabs/notifications-inc.jsp" flush="true" />
                    </c:if>
                </div>
            </div>
        </div>
        <!--jsp:include page="../prod/product.jsp" flush="true" /-->
    </body>
</html>

<script type="text/javascript" charset="utf-8">
    function doOnLoad() {
        initProjectsToolbar('${visibility}', '${contextPath}');
        var tabIndex = 0;
        if(!'${tab}'.empty()) {
            if('${tab}' == 'org') {
                tabIndex = 1;
            } else  if('${tab}' == 'admin') {
                tabIndex = 2;
            } else  if('${tab}' == 'role') {
                tabIndex = 3;
            } else  if('${tab}' == 'target') {
                tabIndex = 4;
            } else  if('${tab}' == 'contributor') {
                tabIndex = 5;
            } else  if('${tab}' == 'prod') {
                tabIndex = 6;
                loadProductsFlexGrid();
            } else  if('${tab}' == 'uf') {
                tabIndex = 7;
            }

    <c:if test="${not isSA}">
                if('${tab}' == 'role') {
                    tabIndex = 1;
                } else  if('${tab}' == 'target') {
                    tabIndex = 2;
                } else  if('${tab}' == 'contributor') {
                    tabIndex = 3;
                } else  if('${tab}' == 'prod') {
                    tabIndex = 4;
                }
    </c:if>
            }
            $('#projectTabs').tabs({ selected: tabIndex,
                show: function(event, ui){fixDHTMLXTabbarHeight("content")},
                select: function(event, ui) {
                    var contributorTab = 5;

    <c:if test="${not isSA}">
                    contributorTab = 3;
    </c:if>
                        
                    var selected = ui.index;
                    switch (selected) {
                        case contributorTab:
                            initContributorTab();
                            break;
                    }
                }});
        }
</script>