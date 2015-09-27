<%-- 
    Document   : contentVersionDropdown
    Created on : Aug 28, 2010, 7:59:41 PM
    Author     : Jeff Jiang
--%>

<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type">

        <fmt:formatDate value="${journalContentVersion.contentVersion.createTime}" var="timestamp" pattern="yyyy-MM-dd HH:mm:ss"/>
        <title>
            <indaba:msg key='jsp.journalContentVersion.pagetitle' >
                <indaba:arg value='${timestamp}' />
            </indaba:msg>
            <indaba:userDisplay prjid="${prjid}" subjectUid="${uid}" targetUid="${journalContentVersion.contentVersion.userId}" pureText="true"/></title>
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
    </head>
    <body>
        <c:if test="${!empty journalContentVersion}" >
            <div class="wrapper">
                <br/>
                <h2>${journalContentVersion.contentHeader.title} (${timestamp} by <indaba:userDisplay prjid="${prjid}" subjectUid="${uid}" targetUid="${journalContentVersion.contentVersion.userId}" pureText="true"/>)</h2>
                <div class="box">
                    <h3><a name="journalDescription"><indaba:msg key='common.label.desc' /></a> <a href="#journalDescription" class="toggleVisible"><img src='images/collapse_icon.png' alt="" /></a></h3>
                    <div class="content">
                        <c:choose>
                            <c:when test="${empty journalContentVersion.contentVersion.description}">
                                --
                            </c:when>
                            <c:otherwise>
                                ${journalContentVersion.contentVersion.description}
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="box">
                    <h3><a name="journalContent"><indaba:msg key='jsp.journalContentVersion.content' /></a> <a href="#journalContent" class="toggleVisible"><img src='images/collapse_icon.png' alt="" /></a></h3>
                    <div class="content">
                        <div id="journal-content-readonly-border">
                            <div <c:if test="${empty journalContentVersion.journalContentVersion.body}">style="height: 40px;"</c:if> >
                                <c:if test="${!empty journalContentVersion.journalContentVersion}">
                                    ${journalContentVersion.journalContentVersion.body}
                                </c:if>
                            </div>
                        </div>
                    </div>
                    <!--
                    <script>
                        CKEDITOR.replace('journaleditor', {customConfig : '${contextPath}/js/ckeditor_readonly_config.js', resize_dir: 'vertical'});
                    </script>
                    -->
                </div>

                <!-- include attachment widget -->
                <jsp:include page="attachment.jsp" flush="true" >
                    <jsp:param name="contentType" value="journal" />
                    <jsp:param name="preversion" value="1" />
                    <jsp:param name="readonly" value="true" />
                </jsp:include>

            </div>
        </c:if>
    </body>
</html>
