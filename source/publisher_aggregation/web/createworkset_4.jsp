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
        <title>Indaba Publisher - Create Working Set</title>
        <script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="js/jquery.uniform.js" charset="utf-8"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script src="js/jquery.alerts.js" type="text/javascript"></script>
        <link href="css/jquery.alerts.css" rel="stylesheet" type="text/css" />

        <script type="text/javascript" charset="utf-8">
            $(function(){
                //$('input, textarea, select, button').uniform();
            });
            var worksetId = ${worksetId};
            function saveWorkset(){
                var data = "step=5";
                $.ajax({
                    type: "POST",
                    url: "createworkset.do",
                    data: data,
                    cache: false,
                    async: false,
                    success: function(result) {
                        var json = eval("(" + result + ")");
                        if(json.code == 0){
                            jAlert("", "Save Working Set Falied", null);
                        }else{
                            //jAlert("", "Save Working Set Succeed", null);
                            worksetId = json.worksetId;
                            location.href = "manageworkset.do";
                        }
                    }
                });
                return false;
            }

            function quitWorkset(){
                jConfirm("Your work has not been saved. Are you sure to quit", "Confirm Quit Workset", function(r) {
                    if (r){
                        var toUrl = "quitworkset.do";
                        location.href = toUrl;
                    }
                });
                return false;
            }
        </script>
        <link rel="stylesheet" href="css/uniform.default.css" type="text/css" media="screen" />
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
    </head>
    <body>
        <div id="indaba">
            <c:set var="active" value="createworkset" scope="request"/>
            <jsp:include page="header.jsp" flush="true" />

            <div class="interactive">
                <div class="wrapper">
                    <h2>Create a Working Set &nbsp;&nbsp;&nbsp;&nbsp;<small>- <a href="#" onclick="quitWorkset();">Quit this Working Set</a></small></h2>
                    <ul id="steps-bar" class="seg-4">
                        <li class="done">
                            <dl>
                                <dt>Step 1:</dt>
                                <dd>Select name and projects</dd>
                            </dl>
                        </li>
                        <li class="done">
                            <dl>
                                <dt>Step 2:</dt>
                                <dd>Select indicators</dd>
                            </dl>
                        </li>
                        <li class="lastDone">
                            <dl>
                                <dt>Step 3:</dt>
                                <dd>Select targets</dd>
                            </dl>
                        </li>
                        <li class="last current">
                            <dl>
                                <dt>Step 4:</dt>
                                <dd>Save Working Set</dd>
                            </dl>
                        </li>
                    </ul>
                    <div class="steps-bar-separator"></div>
                    <div id="nameProjects" align="left" valign="top" style="display:block; margin-top: 10px;" >
                        <div class="preview">Name / Projects<a href="createworkset.do?step=1">(change this)</a></div>
                        <div class="line" ></div>
                        <div>
                            <ul>
                                <li>
                                    <span class="preview-name">Workset name:</span>
                                    <span class="preview-cnt">${workset.name}</span>
                                </li>
                                <li>
                                    <span class="preview-name">Projects Included:</span>
                                    <c:forEach items="${workset.projectNames}" var="projectName" varStatus="status">
                                        <span class="preview-cnt">${projectName}<c:if test="${!status.last}">,</c:if></span>
                                    </c:forEach>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div id="nameProjects" align="left" valign="top" style="display:block; margin-top: 10px;" >
                        <div class="preview">Your Indicator Selection<a href="createworkset.do?step=2">(change this)</a></div>
                        <div class="line" ></div>
                        <div>
                            <c:forEach items="${workset.indicatorNames}" var="indicatorName" varStatus="status">
                                <span class="preview-cnt">${indicatorName}<c:if test="${!status.last}">,</c:if></span>
                            </c:forEach>
                        </div>
                    </div>
                    <div id="preview" align="left" valign="top" style="display:block; margin-top: 10px;" >
                        <div class="preview">Your Target Selection<a href="createworkset.do?step=3">(change this)</a></div>
                        <div class="line" ></div>
                        <div>
                            <c:forEach items="${workset.targetNames}" var="targetName" varStatus="status">
                                <span class="preview-cnt">${targetName}<c:if test="${!status.last}">,</c:if></span>
                            </c:forEach>
                        </div>
                    </div>
                    <!--<c:if test="${worksetId > 0}">
                        <div id="preview" align="left" valign="top" style="display:block; margin-top: 10px;" >
                            <c:choose>
                                <c:when test="${showManageDatapint}">
                                <div class="preview"><a href="manageaggregation.do?worksetId=${worksetId}">Manage Aggregations</a></div>
                                </c:when>
                                <c:otherwise>
                                <div class="preview"><a href="createaggregation.do?worksetId=${worksetId}">Create Aggregation</a></div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:if>-->
                    <c:if test="${showSave}">
                        <div id="saveWorksetDiv"  class="submitButtons">
                            <input type="submit" value="Save this workset" onclick="return saveWorkset();" />
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
