<%-- 
    Document   : notebookDisplay
    Created on : 2010-3-24, 15:00:52
    Author     : Luke Shi
--%>

<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type">
        <title><indaba:msg key='jsp.notebookDisplay.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <!--[if lt IE 7]>
            <link href="ie6.css" media="screen" rel="Stylesheet" type="text/css" />
        <![endif]-->
        <link type="text/css" rel="stylesheet" href="css/style2.css"/>
        <link type="text/css" rel="stylesheet" href="css/button.css"/>
        <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="js/jquery.highlightFade.js"></script>
        <script type="text/javascript" src="js/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="js/discussions.js"></script>
        <script type="text/javascript" src="js/jquery.autogrow-textarea.js"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/json2.js"></script>
        <script type="text/javascript" src="js/journalReview.js"></script>
        <script type="text/javascript" src="js/contentGeneral.js"></script>
        <script type="text/javascript" src="ckeditor/ckeditor.js"></script>
        <script type="text/javascript" src="ckeditor/adapters/jquery.js"></script>
        <script>
            $(document).bind('commonWidgetReady', function(event, num){
                if (num == 0){
                    $('#cntgeneral').remove();
                    $('hr').each(function(index, hr){
                        if ($(hr).prev('.wrapper').children().length == 0){
                            $(hr).prev('.wrapper').remove();
                            $(hr).hide();
                        }
                    })
                }
            })
        </script>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="yourcontent" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <!--jsp:include page="contentGeneral.jsp" flush="true" /-->
            <script type="text/javascript">
                loadContentGeneral(${horseid}, ${assignid});
            </script>
            <div class="wrapper">
                <jsp:include page="discussion.jsp" flush="true" >
                    <jsp:param value="1" name="checkPermission"/>
                    <jsp:param value="1" name="folded"/>
                </jsp:include>
            </div>
            <hr/>
            <div class="wrapper">
                <jsp:include page="notebookView.jsp" flush="true" />
                <jsp:include page="staffDiscussion.jsp" flush="true">
                    <jsp:param value="1" name="checkPermission"/>
                    <jsp:param value="0" name="syncStatus"/>
                    <jsp:param name="folded" value="0"/>
                </jsp:include>
                <jsp:include page="prReviews.jsp" flush="true">
                    <jsp:param value="1" name="checkPermission"/>
                    <jsp:param value="0" name="syncStatus"/>
                    <jsp:param value="1" name="folded"/>
                </jsp:include>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>

