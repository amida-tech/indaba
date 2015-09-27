<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<html>
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type">
        <title><indaba:msg key='jsp.surveyOverallRev.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <!--[if lt IE 7]>
            <link href="ie6.css" media="screen" rel="Stylesheet" type="text/css" />
        <![endif]-->
        <link type="text/css" rel="stylesheet" href="css/style2.css"/>
        <link type="text/css" rel="stylesheet" href="css/button.css"/>
        <link type="text/css" rel="stylesheet" href="css/tipTip.css"/>
        <link type="text/css" rel="stylesheet" href="plugins/jquery-ui/css/lightness/jquery-ui-1.10.4.custom.min.css" media="all" />
        <link type="text/css" rel="stylesheet" href="plugins/pnotify/jquery.pnotify.default.css"/>
        <link type="text/css" rel="stylesheet" href="plugins/pnotify/jquery.pnotify.default.icons.css"/>

        <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="js/jquery.tipTip.js"></script>
        <script type="text/javascript" src="js/jquery.highlightFade.js"></script>
        <script type="text/javascript" src="js/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="js/discussions.js"></script>
        <script type="text/javascript" src="js/jquery.autogrow-textarea.js"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/surveyReview.js"></script>
        <script type="text/javascript" src="js/json2.js"></script>
        <script type="text/javascript" src="js/button.js"></script>
        <script type="text/javascript" src="js/journalReview.js"></script>
        <script type="text/javascript" src="js/assignment.js"></script>
        <script type="text/javascript" src="js/contentGeneral.js"></script>
        <script type="text/javascript" src="ckeditor/ckeditor.js"></script>
        <script type="text/javascript" src="ckeditor/adapters/jquery.js"></script>
        <script type="text/javascript" src="js/scorecardNavigation.js"></script>
        <script type="text/javascript" src="js/indicatorSearch.js"></script>
        <script type="text/javascript" src="plugins/pnotify/jquery.pnotify.min.js"></script>
        <script type="text/javascript" src="js/ocs-notify.js"></script>
        <script type="text/javascript" src="plugins/jquery-ui/js/jquery-ui-1.10.4.custom.min.js"></script>
        <script type="text/javascript" src="plugins/dialog/ocs-dlg.js"></script>

        <script type="text/javascript">
            buttonDeffered = true;
        </script>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="yourcontent" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <c:if test="${assignid == null}">
                <c:set var="assignid" value="-1" />
            </c:if>
            <%--jsp:include page="contentGeneral.jsp" flush="true" /--%>
            <script type="text/javascript">
                loadContentGeneral(${horseid}, ${assignid});
            </script>
            <div class="wrapper">
                <jsp:include page="discussion.jsp" flush="true" >
                    <jsp:param name="checkPermission" value="1"/>
                    <jsp:param name="folded" value="1"/>
                </jsp:include>
                <jsp:include page="surveyStaffDiscussion.jsp" flush="true" >
                    <jsp:param value="1" name="syncStatus"/>
                    <jsp:param name="folded" value="1"/>
                </jsp:include>
            </div>
            <hr/>
            <div class="wrapper">
                <script type="text/javascript">
                    loadScorecardNavigation({horseid: "${horseid}", assignid: "${assignid}", action: "${action}", returl: "${returl}"});
                </script>
                <%--jsp:include page="scorecardNavigation.jsp" flush="true" /--%>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>

