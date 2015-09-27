<%-- 
    Document   : notebookEdit
    Created on : 2010-3-23, 22:30:33
    Author     : Luke Shi
--%>

<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="content-Type" />
        <meta http-equiv="Pragma" content="no-cache" />
        <meta http-equiv="Cache-Control" content="no-cache" />
        <meta http-equiv="Expires" content="0" />
        <title><indaba:msg key='jsp.notebookEdit.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <!--[if lt IE 7]>
            <link href="ie6.css" media="screen" rel="Stylesheet" type="text/css" />
        <![endif]-->
        <link type="text/css" rel="stylesheet" href="css/style2.css"/>
        <link type="text/css" rel="stylesheet" href="css/button.css"/>
        <link type="text/css" rel="stylesheet" href="css/tipTip.css"/>
        <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="js/jquery.tipTip.js"></script>
        <script type="text/javascript" src="js/jquery.highlightFade.js"></script>
        <script type="text/javascript" src="js/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="js/discussions.js"></script>
        <script type="text/javascript" src="js/jquery.autogrow-textarea.js"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/json2.js"></script>
        <script type="text/javascript" src="js/button.js"></script>
        <script type="text/javascript" src="js/assignment.js"></script>
        <script type="text/javascript" src="js/journalReview.js"></script>
        <script type="text/javascript" src="js/contentGeneral.js"></script>
        <script type="text/javascript" src="ckeditor/ckeditor.js"></script>
        <script type="text/javascript" src="ckeditor/adapters/jquery.js"></script>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="yourcontent" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <!--jsp:include page="/cntgeneral.do?horseid=${horseid}" flush="true" /-->
            <script type="text/javascript">
                loadContentGeneral(${horseid}, ${assignid});
            </script>
            <!--jsp:include page="contentGeneral.jsp" flush="true" /-->
            <div class="wrapper">
                <jsp:include page="discussion.jsp" flush="true" >
                    <jsp:param value="1" name="checkPermission"/>
                    <jsp:param value="1" name="folded" />
                </jsp:include>
                <jsp:include page="staffDiscussion.jsp" flush="true" >
                    <jsp:param value="1" name="syncStatus"/>
                    <jsp:param value="0" name="folded" />
                </jsp:include>
            </div>
            <hr style="border: 0px solid red;" />
            <div class="wrapper">
                <jsp:include page="notebookEditor.jsp" flush="true">
                    <jsp:param name="requireAttach" value="true"/>
                </jsp:include>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
