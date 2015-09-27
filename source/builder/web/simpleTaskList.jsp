<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="indaba" uri="indabaTaglib"%>


<script type="text/javascript" src="js/endAssignment.js"></script>
<script type="text/javascript" src="js/taskList.js"></script>

<div style="text-align: center;"> ${cntName} </div>
    <table class="task-list" style="width: 900px">
        <thead>
        <tr class="thead">
            <th><indaba:msg key='common.label.owner' /></th>
            <th><indaba:msg key='common.label.action' /></th>
            <th><indaba:msg key='common.label.engageStatus' /></th>
            <th><indaba:msg key='common.label.deadline' /></th>
            <th><indaba:msg key='common.label.taskStatus' /></th>
        </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${allAssignedOfHorse != null && !empty allAssignedOfHorse}">
                    <c:forEach items="${allAssignedOfHorse}" var="assignedTask">
                        <c:choose>
                            <c:when test="${assignedTask.durTime != null}">
                                <fmt:formatDate var="deadline" value="${assignedTask.durTime}" type="date" pattern="yyyy-MM-dd" />
                            </c:when>
                            <c:otherwise>
                                <c:set var="deadline" value='--'/>
                            </c:otherwise>
                        </c:choose>
                        <tr>
                            <td class="taskusername">
                                <indaba:userDisplay prjid="${prjid}" subjectUid="${uid}" targetUid="${assignedTask.assignedUserId}" />
                            </td>
                            <td class="simpletaskname">
                                <span class="tasktype_${assignedTask.taskType}">${assignedTask.taskName}</span>
                            </td>
                            <td style='padding: 5px 0px 0px 5px;background-image:url("images/${assignedTask.statusIcon}");background-repeat:no-repeat;
                                        color:yellow; font-size:9px; vertical-align:baseline;'>

                            <c:choose>
                            <c:when test="${assignedTask.taskId!=-3 && assignedTask.taskId!=-4}">
                                <b>${assignedTask.status}</b>
                                &nbsp;&nbsp;&nbsp;
                            </c:when>
                            <c:otherwise>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            </c:otherwise>
                            </c:choose>
                            <span style='color:black; font-size:12px'>${assignedTask.completeDisplay}</span>
                            </td>
                            <td>
                                ${deadline}
                            </td>
                            <td>
                                ${assignedTask.taskStatus}
                            </td>
                            <td>
                                <c:if test="${hasright && assignedTask.status != 5 && assignedTask.status != 0}">
                                    <a id='inline' href='#deadline' onclick='setAssignment(${assignedTask.assignmentId}, "${assignedTask.taskName}", "${deadline}", "${assignedTask.assignedUsername}");'>
                                        <indaba:msg key='common.btn.edit' />
                                    </a>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${hasright && assignedTask.status != 5 && assignedTask.status != 0}">
                                    <c:if test="${assignedTask.taskType == 4 || assignedTask.taskType == 9}">
                                        <a href='#' onclick='endPeerReviewAssignment(${assignedTask.assignmentId}, "${assignedTask.assignedUsername}", "${assignedTask.taskName}", "${assignedTask.targetName}" + " " + "${assignedTask.productName}");'>
                                            <indaba:msg key='common.js.msg.exitassignment' />
                                        </a>
                                    </c:if>
                                    <c:if test="${assignedTask.taskType != 4 && assignedTask.taskType != 9}">
                                        <a href='#' onclick='endAssignment(${assignedTask.assignmentId}, "${assignedTask.assignedUsername}", "${assignedTask.taskName}", "${assignedTask.targetName}" + " " + "${assignedTask.productName}");'>
                                            <indaba:msg key='common.js.msg.exitassignment' />
                                        </a>
                                    </c:if>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${tasks}" var="task">
                        <tr>
                            <td>
                                --
                            </td>
                            <td>
                                ${task.taskName}
                            </td>
                            <td>
                                <indaba:msg key='common.msg.taskstatus.unassigned' />
                            </td>
                            <td>
                                --
                            </td>
                            <td>
                                <indaba:msg key='common.msg.taskstatus.unassigned' />
                            </td>
                            <td>
                            </td>
                            <td>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>