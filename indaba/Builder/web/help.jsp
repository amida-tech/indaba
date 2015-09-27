<%--
    Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
    Document   : allcontent
    Created on : Jan 22, 2010, 11:26:15 PM
    Author     : Jeff Jiang
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title><indaba:msg key='jsp.help.title' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>

        <!--[if lt IE 7]>
            <link href="ie6.css" media="screen" rel="Stylesheet" type="text/css" />
        <![endif]-->
        <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="allcontent" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper">
                <div class="box icon-home-assignment">
                    <h3><indaba:msg key='jsp.header.menu.help' /></h3>
                    <div class="content" style="padding: 10px">
                        <indaba:msg key='jsp.help.start' />
                        <div style="padding:20px">
                        <ul class="help">
                            <li><indaba:msg key='jsp.help.tip1' /></li>
                            <li><indaba:msg key='jsp.help.tip2' /></li>
                            <li><indaba:msg key='jsp.help.tip3' /></li>
                        </ul>
                        </div>
                        <indaba:msg key='jsp.help.end' />
                    </div>
                </div>
            </div>            
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>