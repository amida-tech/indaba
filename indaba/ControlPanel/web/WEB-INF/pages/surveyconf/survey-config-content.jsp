<%-- 
    Document   : Survey config
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
        <title><indaba:msg key="cp.title.indaba_control_panel" /> - <c:choose><c:when test="${(surveyConfigId <= 0)}"><indaba:msg key="cp.title.add_surveyConfig" /></c:when><c:otherwise><indaba:msg key="cp.title.edit_surveyConfig" /></c:otherwise></c:choose></title>
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
        <script type="text/javascript" charset="utf-8" src="${contextPath}/resources/js/indaba.surveyConfig.js"></script>
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
            <div id="content">
                <div id="emptyToolbar"></div>
                <div style="margin:10px;">
                    <a class="btn gray" href="${contextPath}/surveyconf/survey-config?visibility=${visibility}">
                        &nbsp;&nbsp;<indaba:msg key="cp.btn.back_to_survey_config_list"/>
                    </a>
                    <c:if test="${sconfId>0}">
                        <span style="border: 1px solid #ddd; padding: 5px; float: right; background-color: #fafafa">
                            <span><strong><indaba:msg key="cp.label.surveyConfig"/>:</strong>
                                <label id="nameLabel"></label>
                            </span>
                            <c:if test="${used}"><span class="lock"><indaba:msg key="cp.label.view_only"/></span></c:if>
                        </span>
                    </c:if>
                    </div>
                    <div id="surveyConfigTabs" style="margin:10px; background: #f8f8f8">
                        <ul>
                            <li><a href="#surveyConfigFormTab"><indaba:msg key="cp.label.base_information"/></a></li>
                        <c:if test="${sconfId > 0}">
                            <li><a href="#scIndicatorsTab" onclick="loadScIndicatorsFlexGrid();"><indaba:msg key="cp.label.indicators"/></a></li>
                            <li><a href="#surveyTreeTab"><indaba:msg key="cp.tab2.surveyConfig.indicator_tree"/></a></li>
                        </c:if>
                    </ul>
                    <jsp:include page="./tabs/survey-config-form.jsp" flush="true" />

                    <c:if test="${sconfId > 0}">
                        <jsp:include page="./tabs/indicators.jsp" flush="true" />
                        <jsp:include page="./tabs/survey-tree.jsp" flush="true" />
                    </c:if>
                </div>
            </div>
        </div>
    </body>
</html>

<script type="text/javascript" charset="utf-8">
    function doOnLoad() {
        initEmptyToolbar('${contextPath}');
        $( "#surveyConfigTabs" ).tabs({
            selected: 0,
            show: function(event, ui){fixDHTMLXTabbarHeight("content")},
            select: function(event, ui) {
                /*
                var surveyConfigFormTab = 0;
                var surveyTreeTab = 1;
                var selected = ui.index;
                switch (selected) {
                    case surveyConfigFormTab:
                        //$("#tasks-flexgrid").flexReload({newp: 1,dataType: 'json',url: '${contextPath}/prod/task!find?surveyConfigId=${surveyConfigId}'});
                        break;
                    }
                 */
            }
        });
    }
</script>