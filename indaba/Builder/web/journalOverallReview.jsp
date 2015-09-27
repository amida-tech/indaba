<%-- 
    Document   : journalOverallReview
    Created on : 2010-7-13, 10:34:25
    Author     : flyaway
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<%@page import="com.ocs.indaba.common.Constants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><indaba:msg key='jsp.journalOveralReview.title' /></title>
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
        <script type="text/javascript" src="js/journalReview.js"></script>
        <script type="text/javascript" src="js/contentGeneral.js"></script>
        <script type="text/javascript" src="js/button.js"></script>
        <script type="text/javascript" src="js/assignment.js"></script>
        <script type="text/javascript" src="ckeditor/ckeditor.js"></script>
        <script type="text/javascript" src="ckeditor/adapters/jquery.js"></script>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="yourcontent" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <script type="text/javascript">
                loadContentGeneral(${horseid}, ${assignid});
            </script>
            <div class="wrapper">
                <jsp:include page="discussion.jsp" flush="true" >
                    <jsp:param value="1" name="checkPermission"/>
                    <jsp:param value="1" name="syncStatus" />
                    <jsp:param value="1" name="folded" />
                </jsp:include>
            </div>
            <hr class="separator">
            <div class="wrapper">
                <!--c:if test="${journalBody != null && fn:length(fn:trim(journalBody)) > 0}"-->
                    <jsp:include page="notebookEditor.jsp" flush="true" >
                        <jsp:param value="false" name="showSubmitButton"/>
                        <jsp:param value="${superedit}" name="superedit"/>
                        <jsp:param name="requireAttach" value="true"/>
                    </jsp:include>
                <!--/c:if-->
                <script type="text/javascript">
                    var tempbutton = document.getElementById("nbesubmitbutton");
                    if (tempbutton){
                        tempbutton.parentNode.removeChild(tempbutton);
                        tempbutton = document.getElementById("nbesavebutton");
                        tempbutton.onclick = function(){
                            return submitform('overallsave');
                        }
                    }
                </script>

                <jsp:include page="staffDiscussion.jsp" flush="true" >
                    <jsp:param value="1" name="syncStatus"/>
                    <jsp:param value="0" name="folded" />
                </jsp:include>

                <jsp:include page="prReviews.jsp" flush="true" >
                    <jsp:param value="0" name="syncStatus" />
                    <jsp:param value="${param.assignid eq '0'}" name="superedit" />
                </jsp:include>

                <c:if test="${assignid != 0}">
                    <div align="center">
                        <button class="iamdone large button blue icon-check" onclick="submitJournalAssignment('${contextPath}', '${param.horseid}', '${param.assignid}');"><indaba:msg key='common.btn.donesubmit' /></button>
                    </div>
                </c:if>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
