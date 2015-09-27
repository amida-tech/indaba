<%-- 
    Document   : workflowConsole
    Created on : 2010-5-20, 11:22:55
    Author     : Luke Shi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<indaba:view prjid="${prjid}" uid="${uid}" right="use indaba admin">
    <html>
        <head>
            <c:if test="${reloadTime > 0}">
                <meta HTTP-EQUIV="REFRESH" CONTENT="${reloadTime}" />
            </c:if>
            <!--
            <meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
            -->
            <title><indaba:msg key='jsp.workflowConsole.pagetitle' /></title>
            <link type="text/css" rel="stylesheet" href="css/style.css"/>
            <!--[if lt IE 7]>
                <link href="ie6.css" media="screen" rel="Stylesheet" type="text/css" />
            <![endif]-->
            <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
            <script type="text/javascript" src="js/jquery.i18n.js"></script>
            <script type="text/javascript" src="jsI18nMsg.do"></script>
            <script type="text/javascript" src="js/toggleDisplay.js"></script>
            <script type="text/javascript" src="js/json2.js"></script>
            <c:if test="${reloadTime > 0}">
                <script type="text/javascript" language="javascript">
                    var count=0;
                    var timer;

                    function startCount(){
                        document.getElementById('counter').value=count;
                        count=count+1;
                        timer=window.setTimeout("startCount()",1000);
                    }

                    function pauseCount(){
                        window.clearTimeout(timer);
                    }

                    function stopCount(){
                        count=0;
                        document.getElementById('counter').value=0;
                        clearTimeout(timer);
                    }
                </script>
            </c:if>
        </head>
        <body onload="startCount();">
            <div id="indaba">
                <c:set var="active" value="yourcontent" scope="request"/>
                <jsp:include page="header.jsp" flush="true" />
                <div class="wrapper">
                    <c:if test="${reloadTime > 0}">
                        <div align="right">Reload Every ${reloadTime} Seconds... &nbsp; <input type="text" id="counter" size="1" readonly style="text-align: right;"></div>
                        </c:if>
                    <br/>
                    <div class="box" id="workflow">
                        <h3><a href="workflowConsole.do"><indaba:msg key='jsp.workflowConsole.workflows' /></a>
                            <a href="runWorkflow.do?disp=1" class="rightDisplay"><img src='images/fire.png' alt=""/> <font color="red" size="2"><indaba:msg key='jsp.workflowConsole.workflows.runAll' /></font> <img src='images/fire.png' alt=""/></a>
                        </h3>
                        <div class="content" style="padding: 1px" align="center">
                            <table width="100%" style="font-size: 12px;">
                                <tr class="thead">
                                    <th><indaba:msg key='jsp.workflowConsole.workflows.id' /></th>
                                    <th><indaba:msg key='common.label.name' /></th>
                                    <th><indaba:msg key='jsp.workflowConsole.workflows.description' /></th>
                                    <th><indaba:msg key='jsp.workflowConsole.workflows.createdAt' /></th>
                                    <th><indaba:msg key='jsp.workflowConsole.workflows.createdBy' /></th>
                                    <th><indaba:msg key='jsp.workflowConsole.workflows.duration' /></th>
                                </tr>
                                <c:forEach items="${workflowList}" var="workflow">
                                    <tr class="highlight_row">
                                        <td>${workflow.id}</td>
                                        <td><a href="runWorkflow.do?disp=1&id=${workflow.id}"><img src='images/fire.png' alt=""/> ${workflow.name}</a></td>
                                        <td>${workflow.description}</td>
                                        <td>${workflow.createdTime}</td>
                                        <td><a href="profile.do?targetUid=${workflow.createdByUserId}">User${workflow.createdByUserId}</a></td>
                                        <td>${workflow.totalDuration}</td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </div>
                        <br>
                    </div>
                    ${returnStr}
                    <div class="box" id="workflowObject">
                        <h3><a href="workflowConsole.do"><indaba:msg key='jsp.workflowConsole.workflowObjects' /></a><a href="#" class="toggleVisible"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
                        <div class="content" style="padding: 1px" align="center">
                            <table width="100%" style="font-size: 12px;">
                                <tr class="thead">
                                    <th><indaba:msg key='jsp.workflowConsole.workflowObjects.target' /></th>
                                    <th><indaba:msg key='jsp.workflowConsole.workflowObjects.object' /></th>
                                    <th><indaba:msg key='common.label.status' /></th>
                                    <th><indaba:msg key='jsp.workflowConsole.workflowObjects.startTime' /></th>
                                </tr>
                                <c:forEach items="${workflowObjectList}" var="wo">
                                    <tr class="highlight_row">
                                        <td><a href="runWorkflow.do?disp=1&wfoid=${wo.wfoId}">${wo.workflowName} - ${wo.targetName}</a></td>
                                        <td><a href="runWorkflow.do?disp=1&wfoid=${wo.wfoId}">${wo.workflowId} - ${wo.wfoId}</a></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${wo.status == 1}">
                                                    <font color="gray">WAITING</font>
                                                </c:when>
                                                <c:when test="${wo.status == 2}">
                                                    <font color="orange">STARTED</font>
                                                </c:when>
                                                <c:when test="${wo.status == 3}">
                                                    <font color="green">DONE</font>
                                                </c:when>
                                                <c:when test="${wo.status == 4}">
                                                    <font color="red">SUSPENDED</font>
                                                </c:when>
                                                <c:when test="${wo.status == 5}">
                                                    <font color="blue">CANCELED</font>
                                                </c:when>
                                            </c:choose>
                                        </td>
                                        <td>${wo.startTime}</td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </div>
                        <br/>
                    </div>
                    <br/>
                    <div class="box" id="workflow">
                        <h3><a href="workflowConsole.do"><indaba:msg key='jsp.workflowConsole.goals' /></a><a href="#" class="toggleVisible"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
                        <div class="content" style="padding: 1px">
                            <table width="100%" style="font-size: 12px;">
                                <tr class="thead">
                                    <th><indaba:msg key='jsp.workflowConsole.goals.target' /></th>
                                    <th><indaba:msg key='jsp.workflowConsole.goals.sequence' /></th>
                                    <th><indaba:msg key='common.label.status' /></th>
                                    <th><indaba:msg key='common.label.goal' /></th>
                                    <th><indaba:msg key='common.label.status' /></th>
                                    <th><indaba:msg key='jsp.workflowConsole.goals.task' /></th>
                                    <th><indaba:msg key='common.label.status' /></th>
                                    <th><indaba:msg key='jsp.workflowConsole.goals.user' /></th>
                                </tr>
                                <c:forEach items="${workflowViewList}" var="workflowView">
                                    <tr class="highlight_row">
                                        <td>
                                            <a href="runWorkflow.do?disp=1&wfoid=${workflowView.wfoId}">
                                                ${workflowView.workflowName} - ${workflowView.targetName} / ${workflowView.wfoId}</a>
                                        </td>
                                        <td>${workflowView.workflowSequenceName}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${workflowView.sequenceStatus == 0}">
                                                    <font color="gray">WAITING</font>
                                                </c:when>
                                                <c:when test="${workflowView.sequenceStatus == 1}">
                                                    <font color="orange">STARTED</font>
                                                </c:when>
                                                <c:when test="${workflowView.sequenceStatus == 2}">
                                                    <font color="green">DONE</font>
                                                </c:when>
                                            </c:choose>
                                        </td>
                                        <td>${workflowView.goalName}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${workflowView.goalStatus == 0}">
                                                    <font color="gray">WAITING</font>
                                                </c:when>
                                                <c:when test="${workflowView.goalStatus == 1}">
                                                    <font color="red">STARTING</font>
                                                </c:when>
                                                <c:when test="${workflowView.goalStatus == 2}">
                                                    <font color="orange">STARTED</font>
                                                </c:when>
                                                <c:when test="${workflowView.goalStatus == 3}">
                                                    <font color="green">DONE</font>
                                                </c:when>
                                                <c:when test="${workflowView.goalStatus == 9}">
                                                    <font color="red">OVERDUE</font>
                                                </c:when>
                                            </c:choose>
                                        </td>
                                        <td>${workflowView.taskName}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${workflowView.taskStatus == 0}">
                                                    <font color="gray">INACTIVE</font>
                                                </c:when>
                                                <c:when test="${workflowView.taskStatus == 1}">
                                                    <font color="blue">ACTIVE</font>
                                                </c:when>
                                                <c:when test="${workflowView.taskStatus == 2}">
                                                    <font color="brown">AWARE</font>
                                                </c:when>
                                                <c:when test="${workflowView.taskStatus == 3}">
                                                    <font color="red">NOTICED</font>
                                                </c:when>
                                                <c:when test="${workflowView.taskStatus == 4}">
                                                    <font color="orange">STARTED</font>
                                                </c:when>
                                                <c:when test="${workflowView.taskStatus == 5}">
                                                    <font color="green">DONE</font>
                                                </c:when>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:if test="${workflowView.assignedUserId != 0}">
                                                <a href="profile.do?targetUid=${workflowView.assignedUserId}">${workflowView.assignedUserId}</a>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </div>
                        <br>
                    </div>
                </div>
            </div>
        <jsp:include page="footer.jsp" flush="true" />
        </body>
    </html>
</indaba:view>
