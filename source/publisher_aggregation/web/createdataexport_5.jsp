<%-- 
    Document   : export_data_5
    Created on : 2011-2-27, 21:28:40
    Author     : Jeanbone
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

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
                $('input, textarea, select, button').uniform();
            });
        </script>
        <link rel="stylesheet" href="css/uniform.default.css" type="text/css" media="screen" />
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
    </head>
    <body onload="refresh_process_content('createDataExport.do', 6)">
        <div id="indaba">
            <c:set var="active" value="dataexport" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />
            <jsp:include page="dataexportinstructions.jsp" flush="true" />
            <div class="interactive">
                <div class="wrapper">
                    <h2>Create a data export</h2>
                    <ul id="steps-bar" class="seg-5">
                        <li class="done">
                            <dl>
                                <dt>Step 1:</dt>
                                <dd>Choose a working set</dd>
                            </dl>
                        </li>
                        <li class="done">
                            <dl>
                                <dt>Step 2:</dt>
                                <dd>Select indicators/datapoints</dd>
                            </dl>
                        </li>
                        <li class="done">
                            <dl>
                                <dt>Step 3:</dt>
                                <dd>Select targets</dd>
                            </dl>
                        </li>
                        <li class="lastDone">
                            <dl>
                                <dt>Step 4:</dt>
                                <dd>Save study periods</dd>
                            </dl>
                        </li>
                        <li class="last current">
                            <dl>
                                <dt>Step 5:</dt>
                                <dd>Download your files</dd>
                            </dl>
                        </li>
                    </ul>
                    <div class="steps-bar-separator"></div>
                    <div id="preview" align="left" valign="top" style="display:block; margin-top: 10px;" >
                        <div class="preview">Download your files:</div>
                        <div style="border-top:2px solid #cccccc;margin-top: 5px; height: 5px;overflow:hidden; margin-right: 70px;"></div>
                        <c:if test="${process_flag == null}">
                            <div align ="center">
                                <div class="preview-cnt" style="width: 220px; text-align: left">
                                    <img id="uploading-img" alt="uploading image" src="images/error.gif" width="20px;" style="vertical-align: middle;">
                                    Please access page follow guidance.
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${process_flag != null}">
                            <div id="export_Processing" >
                                <jsp:include page="export_processing.jsp" flush="true">
                                    <jsp:param name="zipFolder" value="data_export_zip" />
                                </jsp:include>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
