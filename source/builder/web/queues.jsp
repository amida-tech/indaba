<%-- 
    Document   : queues
    Created on : Feb 22, 2010, 9:34:20 PM
    Author     : menglong luwb
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
        <title><indaba:msg key='jsp.queues.pagetitle' /></title>
        <link type="text/css" rel="stylesheet" href="css/style.css"/>
        <!--[if lt IE 7]>
            <link href="ie6.css" media="screen" rel="Stylesheet" type="text/css" />
        <![endif]-->
        <script type="text/javascript" src="js/jquery-1.7.1.js"></script>
        <script type="text/javascript" src="js/jquery.i18n.js"></script>
        <script type="text/javascript" src="jsI18nMsg.do"></script>
        <script type="text/javascript" src="js/toggleDisplay.js"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript">
            <%--submit function of I want it--%>
            function assignToMe(taId){
                $.ajax({
		type: "POST",
		url: "queuesubmit.do",
		data: "type=1&taId=" + taId,
		cache: false,
		async: false,
		success: function(result) {
                    var json=eval("(" + result + ")");
                    if(json.code != 1)
                        alert(json.msg)
                    else{
                        var assign_td = "td_assign_" + taId;
                        //$("#"+priority_td+"").text(json.assignHtml);
                        document.getElementById(assign_td).innerHTML = json.assignHtml;
                    }
                }
                });
                return false;
            }
            <%--submit function of return to queue--%>
            function returnToQueue(taId){
                $.ajax({
		type: "POST",
		url: "queuesubmit.do",
		data: "type=2&taId=" + taId,
		cache: false,
		async: false,
		success: function(result) {
                    var json=eval("(" + result + ")");
                    if(json.code != 1)
                        alert(json.msg)
                    else{
                        var assign_td = "td_assign_" + taId;
                        //$("#"+priority_td+"").text(json.assignHtml);
                        document.getElementById(assign_td).innerHTML = json.assignHtml;
                    }
		}
                });
            }
            <%--submit function of update assignment--%>
            function updateAssignment(taId){
                var selectu = "selectu_" + taId;
                var selectp = "selectp_" + taId;
                var uid = $("#"+selectu+"").find("option:selected").val();
                var pid = $("#"+selectp+"").find("option:selected").val();
                $.ajax({
		type: "POST",
		url: "queuesubmit.do",
		data: "type=3&taId=" + taId + "&uid=" + uid + "&pid=" + pid,
		cache: false,
		async: false,
		success: function(result) {
                    var json=eval("(" + result + ")");
                    if(json.code != 1)
                        alert(json.msg)
                    else{
                        var assign_td = "td_assign_" + taId;
                        var priority_td = "td_priority_" + taId;
                        //$("#"+priority_td+"").text(json.assignHtml);
                        document.getElementById(priority_td).innerHTML = json.priorityHtml;
                        document.getElementById(assign_td).innerHTML = json.assignHtml;
                    }
		}
                });
            }
        </script>
        
    </head>
    <body>
        <div id="indaba">
            <!--c:set var="active" value="queues" scope="request"/-->
            <jsp:include page="header.jsp" flush="true" />
            <div class="wrapper">
                <c:choose>
                    <c:when test="${empty queueTaskList}">
                        <br/><br/>
                             <p align="center"><indaba:msg key='jsp.queues.noQuenes' /></p>
                        <br/>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${queueTaskList}" var="queueTask">
                            <div class="box">
                                <h3>${queueTask.taskName}<a href="#" class="toggleVisible"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>
                                <div class="content">
                                    <div class="queue-box">
                                        <h4 class="queue-box-assign-time"><indaba:msg key='jsp.queues.avgTime.assign' />: <c:if test="${queueTask.avgTimeToAssign>0}">${queueTask.avgTimeToAssign} <indaba:msg key='jsp.queues.days' /></c:if></h4>
                                        <h4 class="queue-box-complete-time"><indaba:msg key='jsp.queues.avgTimeComplete' />: <c:if test="${queueTask.avgTimeToComplete>0}">${queueTask.avgTimeToComplete} <indaba:msg key='jsp.queues.days' /></c:if></h4>
                                        <br/><br/>
                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                            <thead>
                                                <tr>
                                                    <th><indaba:msg key='jsp.queues.content' /></th>
                                                    <th><indaba:msg key='jsp.queues.inQueue' /></th>
                                                    <th><indaba:msg key='common.label.priority' /></th>
                                                    <c:choose>
                                                        <c:when test="${queueTask.isAdmin}">
                                                            <th><indaba:msg key='jsp.queues.assignment' /></th>
                                                            <th><indaba:msg key='jsp.queues.setAssignment' /></th>
                                                            <th><indaba:msg key='jsp.queues.setPriority' /></th>
                                                            <th></th>
                                                        </c:when>
                                                          <c:otherwise>
                                                             <th><indaba:msg key='jsp.queues.claimThis' /></th>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </tr>
                                            </thead>
                                            <tbody>
                                               <c:forEach items="${queueTask.taskAssignmentList}" var="queueTa">
                                                <tr id="${queueTa.id}">
                                                    <td>${queueTa.targetName} ${queueTask.productName}</td>
                                                    <td>${queueTa.inQueueDays} <indaba:msg key='jsp.queues.days' /></td>
                                                    <td id="td_priority_${queueTa.id}">${queueTa.priority}</td>
                                                    <c:choose>
                                                        <c:when test="${queueTask.isAdmin}"><%--is admin--%>
                                                            <c:choose>
                                                                <c:when test="${queueTa.assigned}"><%--is assigned--%>
                                                                    <td id="td_assign_${queueTa.id}"><indaba:msg key='jsp.queues.assignTo' /> <a href="profile.do?targetUid=${queueTa.assignedUserId}">${queueTa.assignedUserName}</a></td>
                                                                </c:when>
                                                                <c:otherwise><%--is not assigned--%>
                                                                    <td id="td_assign_${queueTa.id}"><indaba:msg key='jsp.queues.noAssign' /></td>
                                                                </c:otherwise>
                                                            </c:choose>
                                                            <td>
                                                                <c:if test="${!queueTa.isDone}">
                                                                    <select id="selectu_${queueTa.id}">
                                                                        <c:forEach items="${queueTask.qualifiedUsers}" var="user">
                                                                            <option value="${user.userId}">${user.userName}</option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </c:if>
                                                            </td>
                                                            <td>
                                                                <c:if test="${!queueTa.isDone}">
                                                                    <select id="selectp_${queueTa.id}">
                                                                        <option value="1"><indaba:msg key='jsp.queues.low' /></option>
                                                                        <option value="2"><indaba:msg key='jsp.queues.medium' /></option>
                                                                        <option value="3"><indaba:msg key='jsp.queues.high' /></option>
                                                                    </select>
                                                                </c:if>
                                                            </td>
                                                            <td>
                                                                <c:if test="${!queueTa.isDone}">
                                                                    <button onclick="updateAssignment(${queueTa.id})"><indaba:msg key='jsp.queues.update.assign' /></button>
                                                                </c:if>
                                                            </td>
                                                        </c:when>
                                                        <c:otherwise><%--is not admin--%>
                                                            <c:choose>
                                                                <c:when test="${queueTa.assigned}"><%--is assigned--%>
                                                                    <td id="td_assign_${queueTa.id}"><indaba:msg key='jsp.queues.assignTo' /> <a href="profile.do?targetUid=${queueTa.assignedUserId}">${queueTa.assignedUserName}</a> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<c:if test="${queueTa.assignedToMe && !queueTa.isDone}">(<a href="#" onclick="returnToQueue(${queueTa.id})"><indaba:msg key='jsp.queues.returnToQueue' /></a>)</c:if></td>
                                                                </c:when>
                                                                <c:otherwise><%--is not assigned--%>
                                                                    <td id="td_assign_${queueTa.id}"><a href="#" onclick="return assignToMe(${queueTa.id})"><indaba:msg key='jsp.queues.iWantThis' /></a></td>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="clear">
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <jsp:include page="footer.jsp" flush="true" />
    </body>
</html>
