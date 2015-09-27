<%--
    Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
    Document   : export_data_1
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
        <title>Indaba Publisher - Create a data export</title>
        <script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="js/jquery.uniform.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" charset="utf-8">
            $(function(){
                controlUnverifiedChkbox();
            });

            function controlUnverifiedChkbox() {
                if (${user.siteAdmin == 1} || "${user.organizationId}" == $("#" + $("#workingset").val()).val()) {
                    $("#includeUnverifiedDataLi").show();
                } else {
                    $("#includeUnverifiedDataLi").hide();
                }
            }

            function checkEntireForm(){
                $('#chooseWorkSet').attr("action", "createDataExport.do?step=5&exportEntireSet=1&ep=1");
                return true;
            }
        </script>
        <link rel="stylesheet" href="css/uniform.default.css" type="text/css" media="screen" />
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="dataexport" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <jsp:include page="dataexportinstructions.jsp" flush="true" />
            <div class="interactive">
                <div class="wrapper">
                    <h2>Create a data export</h2>
                    <ul id="steps-bar" class="seg-5">
                        <li class="current">
                            <dl>
                                <dt>Step 1:</dt>
                                <dd>Choose a working set</dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>Step 2:</dt>
                                <dd>Select indicators/datapoints</dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>Step 3:</dt>
                                <dd>Select targets</dd>
                            </dl>
                        </li>
                        <li>
                            <dl>
                                <dt>Step 4:</dt>
                                <dd>Save study periods</dd>
                            </dl>
                        </li>
                        <li class="last">
                            <dl>
                                <dt>Step 5:</dt>
                                <dd>Download your files</dd>
                            </dl>
                        </li>
                    </ul>
                    <div class="steps-bar-separator"></div>
                    <form id="chooseWorkSet" action="createDataExport.do?step=2&ep=1" method="post" >
                        <fieldset>
                            <legend> Choose a working set </legend>
                            <ul>
                                <li>
                                    <label for="workingset" style="position:relative; top:-7px;">Working Set:</label>
                                    <select class="middlebox" id="workingset" name="workingset">
                                        <c:forEach var="workset" items="${worksetList}">
                                            <option>${workset.name}</option>
                                        </c:forEach>
                                    </select>
                                    <br/>
                                    <span class="warn">Must choose a match mode!</span>
                                </li>
                                <li style="height: 5px;">
                                </li>
                                <li id="includeUnverifiedDataLi">
                                    <label for="includeUnverifiedData">Include unverified data?</label>
                                    <input id="includeUnverifiedData" type="checkbox" name="includeUnverifiedData" />
                                    You are elligible to export unverified, pre-publication data. Include this data?
                                </li>
                                <li style="height: 5px;">
                                </li>
                                <li>
                                    <input type="submit" value="Select a slice"/>&nbsp;&nbsp;&nbsp;&nbsp;
                                    <input type="submit" value="Export entire set"  onclick="return checkEntireForm();" />
                                </li>
                                <li style="height: 5px;">
                                </li>
                            </ul>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
