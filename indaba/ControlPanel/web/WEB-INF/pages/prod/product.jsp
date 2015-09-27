<%-- 
    Document   : Product
    Created on : May 20, 2012, 11:20:21 AM
    Author     : jiangjeff
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC"-//W3C//DTD XHTML 1.0 Transitional//EN">
<html>
    <head>
        <title><indaba:msg key="cp.title.indaba_control_panel" /> - <c:choose><c:when test="${(prodId <= 0)}"><indaba:msg key="cp.title.add_product" /></c:when><c:otherwise><indaba:msg key="cp.title.edit_product" /></c:otherwise></c:choose></title>
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
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/list-utils.js"></script>
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
                    <a class="btn gray" href="${contextPath}/proj/projects!projectForm?visibility=${visibility}&projId=${projId}&tab=prod"><&nbsp;&nbsp;<indaba:msg key="cp.btn.back_to_prod_list"/></a>
                    <c:if test="${prodId>0}">
                        <span style="border: 1px solid #ddd; padding: 5px; float: right; background-color: #fafafa">
                            <span>
                                <strong><indaba:msg key="cp.label.product"/>:</strong>
                                <label id="prodNameLabel"></label>
                            </span>
                            <span style="margin-left: 10px">
                                <strong><indaba:msg key="cp.label.mode"/>:</strong>
                                <label id="prodModeLabel"></label>
                            </span>
                        </span>
                    </c:if>
                </div>
                <div id="productTabs" style="margin:10px; background: #f8f8f8">
                    <ul>
                        <li><a href="#productBaseTab"><indaba:msg key="cp.tab2.project.base_info"  /></a></li>
                        <c:if test="${prodId > 0}">
                            <li><a href="#productTasksTab"><indaba:msg key="cp.tab2.product.tasks"  /></a></li>
                            <c:if test="${prodMode > 1}">
                                <li><a href="#productHorsesTab"><indaba:msg key="cp.tab2.product.horses"  /></a></li>
                                <li><a href="#productAssignmentsTab"><indaba:msg key="cp.tab2.product.assignments"  /></a></li>
                            </c:if>
                            <c:if test="${contentType == 0}">
                                <li><a href="#productNotedefsTab"><indaba:msg key="cp.tab2.product.notedefs"  /></a></li>
                                <li><a href="#productGroupdefsTab"><indaba:msg key="cp.tab2.product.groupdefs"  /></a></li>
                            </c:if>
                        </c:if>
                    </ul>
                    <jsp:include page="./tabs/baseinfo-inc.jsp" flush="true" />

                    <c:if test="${prodId > 0}">
                        <jsp:include page="./tabs/tasks-inc.jsp" flush="true" />
                        <c:if test="${prodMode > 1}">
                            <jsp:include page="./tabs/horses-inc.jsp" flush="true" />
                            <jsp:include page="./tabs/assignments-inc.jsp" flush="true" />
                        </c:if>
                        <c:if test="${contentType == 0}">
                            <jsp:include page="./tabs/notedefs-inc.jsp" flush="true" />
                            <jsp:include page="./tabs/groupdefs-inc.jsp" flush="true" />
                        </c:if>
                    </c:if>
                </div>
            </div>
        </div>
    </body>
</html>

<script type="text/javascript" charset="utf-8">
    function doOnLoad() {
        initProjectsToolbar('${visibility}', '${contextPath}');
        $( "#productTabs" ).tabs({ 
            selected: 0, 
            show: function(event, ui){fixDHTMLXTabbarHeight("content")},
            select: function(event, ui) {
                var baseInfoTab = 0;
                var taskTab = 1;
                var horseTab = 2;
                var assignmentTab = 3;
                var notedefTab = 4;
                var groupdefTab = 5;
                var selected = ui.index;
                switch (selected) {
                    case assignmentTab:
                        $("#assignments-flexgrid").flexReload({newp: 1, dataType: 'json'});
                        break;
                    case taskTab:
                        $("#tasks-flexgrid").flexReload({newp: 1,dataType: 'json',url: '${contextPath}/prod/task!find?prodId=${prodId}'});
                        break;
                    case horseTab:
                        $("#horses-flexgrid").flexReload({newp: 1,dataType: 'json',url: '${contextPath}/prod/horse!find?prodId=${prodId}'});
                        break;
                    case notedefTab:
                        $("#notedef-flexgrid").flexReload({newp: 1,dataType: 'json',url: '${contextPath}/prod/notedef!find?prodId=${prodId}'});
                        break;
                    case groupdefTab:
                        $("#groupdef-flexgrid").flexReload({newp: 1,dataType: 'json',url: '${contextPath}/prod/groupdef!find?prodId=${prodId}'});
                        break;
                    }
                }
            });
        }
</script>