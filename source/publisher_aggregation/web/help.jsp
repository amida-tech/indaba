<%--
    Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
    Author     : Luke
--%>
<%@page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <link REL="SHORTCUT ICON" href="images/indaba_icon.ico"/>
        <meta content="HTML Tidy, see www.w3.org" name="generator"/>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
        <title>Indaba Publisher - Help</title>
        <script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="js/jquery.uniform.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>

        <link rel="stylesheet" href="css/uniform.default.css" type="text/css" media="screen" />
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <style type="text/css">
            .worksetNotes{
                font-size: 13px;
                font-weight: bold;
                margin-top: 0px;
                padding-left: 0px;
                margin-left: 0px;
                color: red;
            }
        </style>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="help" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <div class="help">
                Help
            </div>
        </div>
            Need help navigating Publisher? Contact Monika Shepard at Global Integrity: <a href="mailto:monika.shepard@globalintegrity.org?Subject=Help">monika.shepard@globalintegrity.org</a>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
