<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="indaba" uri="indabaTaglib"%>
<un:useConstants var="Rights" className="com.ocs.indaba.common.Rights"/>


<script type="text/javascript" src="js/endAssignment.js"></script>
<script type="text/javascript" src="js/taskList.js"></script>

<div style="text-align: center; margin: 10px 0px;"><h3>${cntName}</h3></div>
<table class="task-list" style="width: 900px">
    <thead>
        <tr class="thead">
            <th><indaba:msg key='common.label.owner' /></th>
            <th><indaba:msg key='common.label.task' /></th>
            <th><indaba:msg key='common.label.progress' /></th>
            <th><indaba:msg key='common.label.due' /></th>
            <th><indaba:msg key='common.label.status' /></th>
            <th><indaba:msg key='common.label.stop' /></th>
        </tr>
    </thead>
    <tbody>
        <c:choose>
            <c:when test="${allAssignedWithGrid != null && !empty allAssignedWithGrid}">
                <c:forEach items="${allAssignedWithGrid}" var="assignedTaskWithGrid">
                    <c:set var="assignedTask" value="${assignedTaskWithGrid.assignment}"/>   
                    <c:choose>
                        <c:when test="${assignedTask.durTime != null}">
                            <fmt:formatDate var="deadline" value="${assignedTask.durTime}" type="date" pattern="yyyy-MM-dd" />
                        </c:when>
                        <c:otherwise>
                            <c:set var="deadline" value='--'/>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${assignedTask.status == 3}">
                            <c:set var="statusIcon" value="eye.png"/> 
                            <c:set var="iconText" value="viewed"/>   
                        </c:when>
                        <c:when test="${assignedTask.status == 4}">
                            <c:set var="statusIcon" value="working.png"/>  
                            <c:set var="iconText" value="working"/>    
                        </c:when>
                        <c:when test="${assignedTask.status == 5}">
                            <c:set var="statusIcon" value="tick.png"/>   
                            <c:set var="iconText" value="done"/>   
                        </c:when>
                        <c:otherwise>
                            <c:set var="statusIcon" value=""/>
                            <c:set var="iconText" value="no activity"/>     
                        </c:otherwise>
                    </c:choose>
                    <tr>
                        <td><indaba:userDisplay prjid="${prjid}" subjectUid="${uid}" targetUid="${assignedTask.assignedUserId}" /></td>
                        <td>${assignedTask.taskName}</td>
                        <td style='padding: 5px 0px 0px 5px;background:url("images/${statusIcon}") no-repeat left center;vertical-align:middle'>
                            <span title="${iconText}" style="cursor:default;" >&nbsp;&nbsp;&nbsp;&nbsp;</span>
                            <span style="position: relative; bottom: 1px;">${assignedTaskWithGrid.progressDisplay}</span>
                        </td>
                        <td>${deadline}</td>
                        <td>${assignedTask.taskStatus}</td>
                        <td>
                            <c:if test="${hasEditDeadlineRight && assignedTask.status != 5 && assignedTask.status != 0}">
                                <a id='inline' href='#deadline' onclick='setAssignment(${assignedTask.assignmentId}, "${assignedTask.taskName}", "${deadline}", "${assignedTask.assignedUsername}");'>
                                    <%--indaba:msg key='common.btn.edit' --%>
                                    <img src="images/edit.png" title="<indaba:msg key='common.btn.editdeadline' />" />
                                </a>&nbsp;&nbsp;
                            </c:if>
                            <%--</td><td>
                            --%>
                            <c:if test="${hasEditDeadlineRight && assignedTask.status != 5 && assignedTask.status != 0}">
                                <c:choose>
                                    <c:when test="${assignedTask.taskType == 4 || assignedTask.taskType == 9}">
                                        <a href='#' onclick='endPeerReviewAssignment(${assignedTask.assignmentId}, "${assignedTask.assignedUsername}", "${assignedTask.taskName}", "${assignedTask.targetName}" + " " + "${assignedTask.productName}");'>
                                            <%--indaba:msg key='common.js.msg.exitassignment' /--%>
                                            <img src="images/stop.png" alt="Stop" />
                                        </a>
                                    </c:when>
                                     <c:when test="${assignedTask.taskType == 20 || assignedTask.taskType == 21}">
                                         <%-- these tasks cannot be forced exit --%>
                                     </c:when>
                                    <c:otherwise>
                                        <a href='#' onclick='endAssignment(${assignedTask.assignmentId}, "${assignedTask.assignedUsername}", "${assignedTask.taskName}", "${assignedTask.targetName}" + " " + "${assignedTask.productName}");'>
                                            <%--indaba:msg key='common.js.msg.exitassignment' /--%>
                                            <img src="images/stop.png"/>
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </td>
                    </tr>
                    <%---------- Display Grid View ----------%>
                    <indaba:view prjid="${prjid}" uid="${uid}" right="see task overview grid" >
                    <c:if test="${assignedTaskWithGrid.answerStatusList != null && !empty assignedTaskWithGrid.answerStatusList}">
                        <c:set var="GRID_SIZE_PER_LINE" value="34" />
                        <c:set var="size" value="${fn:length(assignedTaskWithGrid.answerStatusList)}" />
                        <c:set var="lineNum" value="${size/GRID_SIZE_PER_LINE + (size%GRID_SIZE_PER_LINE==0?0:1)}" />
                        <fmt:parseNumber var="lineNum" integerOnly="true" value="${lineNum}"/>
                        <tr><td colspan="6" style="padding: 0px;">
                                <ul class="grid-view">
                                    <c:forEach  var="surveyAnswerStatus" items="${assignedTaskWithGrid.answerStatusList}" varStatus="status">
                                        <c:choose>
                                            <%-- GREEN: ANSWER_STATUS_COMPLETE or ANSWER_STATUS_COMPLETE --%>
                                            <c:when test="${surveyAnswerStatus.status == 1 || surveyAnswerStatus.status == 11}">
                                                <c:set var="blockColor" value="green"/>  
                                            </c:when>
                                            <%-- ORANGE: ANSWER_STATUS_INCOMPLETE --%>
                                            <c:when test="${surveyAnswerStatus.status == 2 }">
                                                <c:set var="blockColor" value="orange "/>  
                                            </c:when>
                                            <%-- WHITE: ANSWER_STATUS_NOT_WROKED --%>
                                            <c:when test="${surveyAnswerStatus.status == 3}">
                                                <c:set var="blockColor" value="white"/>  
                                            </c:when>
                                            <%-- YELLOW: ANSWER_STATUS_AGREE_W_RESERVATION --%>
                                            <c:when test="${surveyAnswerStatus.status == 12}">
                                                <c:set var="blockColor" value="yellow"/>  
                                            </c:when>
                                            <%-- RED: ANSWER_STATUS_DISAGREE --%>
                                            <c:when test="${surveyAnswerStatus.status == 13}">
                                                <c:set var="blockColor" value="red"/>  
                                            </c:when>
                                            <%-- BLUE: ANSWER_STATUS_NOT_QUALIFIED --%>
                                            <c:when test="${surveyAnswerStatus.status == 14}">
                                                <c:set var="blockColor" value="blue"/>  
                                            </c:when>
                                            <%-- GRAY: ANSWER_STATUS_NOT_TO_BE_WORKED --%>
                                            <c:otherwise>
                                                <c:set var="blockColor" value="gray"/>  
                                            </c:otherwise>
                                        </c:choose>
                                        <fmt:parseNumber var="curLine" value="${(status.index/GRID_SIZE_PER_LINE + 1)}" integerOnly="true"/> 
                                        <c:set var="posClass" value=""/>
                                        <%--
                                        <c:if test="${status.index < GRID_SIZE_PER_LINE}"><c:set var="posClass" value="${posClass} first-row"/></c:if>
                                        <c:if test="${status.index % GRID_SIZE_PER_LINE == 0}"><c:set var="posClass" value="${posClass} first-col"/></c:if>
                                        --%>
                                        <c:if test="${(status.index + 1) % GRID_SIZE_PER_LINE == 0}"><c:set var="posClass" value="${posClass} last-col"/></c:if>
                                        <%-- last row --%>
                                        <c:if test="${status.index / GRID_SIZE_PER_LINE + 1 >= lineNum}"><c:set var="posClass" value="${posClass} last-row"/></c:if>
                                        <c:if test="${(curLine + 1 == lineNum) &&
                                                      (status.index%GRID_SIZE_PER_LINE>=size%GRID_SIZE_PER_LINE)}">
                                            <c:set var="posClass" value="${posClass} last-row"/></c:if>
                                        <c:if test="${(status.index + 1) == size}"><c:set var="posClass" value=" last-row last-col"/></c:if>
                                        <li class="${blockColor} ${posClass} <c:if test='${surveyAnswerStatus.flagCount > 0}'> flag </c:if>" title="${surveyAnswerStatus.questionText}" >${surveyAnswerStatus.index}</li>
                                        </c:forEach> 
                                </ul>
                            </td></tr>
                        </c:if>
                    </indaba:view>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${tasks}" var="task">
                    <tr>
                        <td>--</td>
                        <td>${task.taskName}</td>
                        <td><indaba:msg key='common.msg.taskstatus.unassigned' /></td>
                        <td>--</td>
                        <td><indaba:msg key='common.msg.taskstatus.unassigned' /></td>
                        <td></td>
                        <td></td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </tbody>
</table>