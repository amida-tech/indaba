<%--
    Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
    Document   : workset
    Created on : Feb 21, 2011, 11:26:15 PM
    Author     : Jeff Jiang
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
        <title>Indaba Builder - Your Content</title>
        <script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="js/jquery.uniform.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>

        <script type="text/javascript" charset="utf-8">
            $(function(){
                $('input, textarea, select, button').uniform();
            });
        </script>
        <link rel="stylesheet" href="css/uniform.default.css" type="text/css" media="screen" />
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="workset" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <jsp:include page="dataexportinstructions.jsp" flush="true" />
            <div class="wrapper">
                <form id="chooseWorkSet" action="workset.do" method="post" >
                    <fieldset>
                        <legend> Choose a working set </legend>
                        <ul>
                            <li>
                                <label for="workingset">Working Set:</label>
                                <select class="middlebox" id="workingset" name="workingset">
                                    <option value="" selected>Working set 1</option>
                                    <option value="1">Working set 2</option>
                                    <option value="2">Working set 3</option>
                                </select>
                                <br/>
                                <span class="warn">Must choose a match mode!</span>
                            </li>
                            <li style="height: 5px;">
                            </li>
                            <li>
                                <label for="includeUnverifiedData">Include unverified data?</label>
                                <input id="includeUnverifiedData" type="checkbox" name="includeUnverifiedData" />
                                You are elligible to export unverified, pre-publication data. Include this data?
                            </li>
                            <li style="height: 5px;">
                            </li>
                            <li>
                                <input type="submit" value="Select a slice" onclick="return checkForm();" />&nbsp;&nbsp;&nbsp;&nbsp;
                                <input type="submit" value="Export entire set"  onclick="return checkForm();" />
                            </li>
                            <li style="height: 5px;">
                            </li>
                        </ul>
                    </fieldset>
                </form>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
