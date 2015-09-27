/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.tag;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.common.Rights;
import com.ocs.indaba.dao.UserDAO;
import com.ocs.indaba.po.Tool;
import com.ocs.indaba.po.User;
import com.ocs.indaba.service.AccessPermissionService;
import com.ocs.indaba.service.TaskService;
import com.ocs.indaba.service.ToolService;
import com.ocs.indaba.util.DateUtils;
import com.ocs.indaba.util.SpringContextUtil;
import com.ocs.indaba.vo.AssignedTask;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author Jeff Jiang
 */
public class ManageAssignmentsTagHandler extends BaseTagHandler {

    private static final Logger logger = Logger.getLogger(ManageAssignmentsTagHandler.class);
    private static final String TASK_DISPLAY_PATTERN = "{0} - {1} {2}";
    
    private UserDAO userDAO = (UserDAO) SpringContextUtil.getBean("userDao");
    private TaskService taskService = (TaskService) SpringContextUtil.getBean("taskService");
    private ToolService toolService = (ToolService) SpringContextUtil.getBean("toolService");
    private AccessPermissionService accessPermissionService = (AccessPermissionService) SpringContextUtil.getBean("accessPermissionService");
    /*
     * attribute for access
     */
    private int prjid = 0;
    private int targetUid = 0;
    private int subjectUid = -1;
    private boolean hasMangeUsersRight = false;
    private boolean hasEditDeadlineRight = false;
    private String userName;

    @Override
    public int doStartTag() {
        logger.debug("########################### <indaba:yourassignments> Tag Start###########################");
        logger.debug("Your assignments: [projectId=" + prjid + ", targetUid=" + targetUid + ", subjectUid=" + subjectUid + "].");
        JspWriter out = pageContext.getOut();
        List<AssignedTask> assignedTasks = null;

        // first check if the user has the right 'manage all users'
        hasMangeUsersRight = accessPermissionService.checkProjectPermission(prjid, subjectUid, Rights.MANAGE_ALL_USERS);
        if (hasMangeUsersRight) {
            assignedTasks = taskService.getAssignedTasksByUserId(targetUid, prjid, hasMangeUsersRight);
        }

        hasEditDeadlineRight = accessPermissionService.checkProjectPermission(prjid, subjectUid, Rights.EDIT_ASSIGNMENT_DEADLINES);

        if (hasEditDeadlineRight) {
            User user = userDAO.get(targetUid);
            userName = user.getFirstName() + " " + user.getLastName();
        }

        if (assignedTasks != null && assignedTasks.size() > 0) {
            try {
                out.print("<tr class='thead'>");
                out.print("<th></th>");
                out.print("<th>" + getI18nMessage(Messages.KEY_COMMON_TH_STATUS) + "</th>");
                out.print("<th>" + getI18nMessage(Messages.KEY_COMMON_TH_DEADLINE) + "</th>");
                out.print("<th>" + getI18nMessage(Messages.KEY_COMMON_TH_TASKSTATUS) + "</th>");
                out.print("<th></th>");
                out.print("</tr>");

                int langId = this.getLanguageId();

                for (AssignedTask task : assignedTasks) {
                    task.computeTaskStatus(langId);
                    
                    Tool tool = toolService.getToolById(task.getToolId());
                    if (tool != null) {
                        task.setToolName(tool.getName());
                        task.setAction(tool.getAction());
                    }

                    if (subjectUid == task.getAssignedUserId() && task.getStatus() == Constants.TASK_STATUS_ACTIVE) {
                        taskService.updateTaskAssignmentStatus(task.getAssignmentId(), Constants.TASK_STATUS_AWARE);
                        task.setStatus(Constants.TASK_STATUS_AWARE);
                    }
                    task.setClickable(true);                   
                    task.setTaskStatusIcon(langId);

                    // If the user is not the assigned owner, disable the task link.
                    if (targetUid != task.getAssignedUserId()) {
                        task.setClickable(false);
                    }

                    outputTask(out, task);
                }
            } catch (IOException ex) {
                logger.error("Error Occurs", ex);
            }
        } else {
            try {
                out.print("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + getI18nMessage(Messages.KEY_COMMON_MSG_NODATA));
            } catch (IOException ex) {
                logger.error("Error Occurs", ex);
            }
        }
        return Tag.SKIP_BODY;
    }

    /*
     * private void setDueTime(AssignedTask task) { Date startTime =
     * task.getStartTime(); if (startTime != null) { Date dueTime = new Date();
     * long milliseconds = startTime.getTime() + Constants.MILLSECONDS_PER_DAY *
     * task.getDuration(); dueTime.setTime(milliseconds); int utilDays = (int)
     * ((milliseconds - System.currentTimeMillis()) /
     * Constants.MILLSECONDS_PER_DAY); task.setUtilDays(utilDays > 0 ? utilDays
     * : 0); task.setDurTime(dueTime); } }
     */
    @Override
    public int doEndTag() {
        logger.debug("########################### <indaba:yourassignments> Tag End ###########################");
        prjid = 0;
        targetUid = 0;
        subjectUid = -1;
        return Tag.EVAL_PAGE;

    }

    private void outputTask(JspWriter out, AssignedTask task) throws IOException {
        String taskClass = "tasktype_" + task.getTaskType();

        out.print("<tr>");
        // output task name
        out.print("<td class='taskname'>");
        out.print("&nbsp;&nbsp;&nbsp;&nbsp;");
        String taskDisplay = MessageFormat.format(TASK_DISPLAY_PATTERN, task.getTaskName(), task.getTargetName(), task.getProductName());
        String taskSpan = "<span class='" + taskClass + "'>" + taskDisplay + "</span>";

        if (task.isClickable()) {
            StringBuffer sBuf = new StringBuffer();
            logger.debug("subjectUid: " + subjectUid + ", targetUid: " + targetUid + ", assignedUid: " + task.getAssignedUserId());
            // if the assigned use id is not the subjectUid, don't display the task link.
            if (subjectUid != task.getAssignedUserId()) {
                //&& !accessPermissionService.checkProjectPermission(prjid, subjectUid, Rights.READ_CONTENT_DETAILS_OF_OTHERS)) {
                sBuf.append(taskSpan);
            } else {
                //if (hasMangeUsersRight) {
                //}
                sBuf.append("<a href='").append(task.getAction()).append('?').append("toolid=").append(task.getToolId()).append("&tasktype=").append(task.getTaskType()).append("&horseid=").append(task.getHorseId()).append("&assignid=").append(task.getAssignmentId()).
                        append((task.getTaskType() == Constants.TASK_TYPE_SURVEY_REVIEW_RESPONSE) ? "#answerquestions" : "").append("'>").append(taskSpan).append("</a>");
            }
            out.print(sBuf.toString());
        } else {
            out.print(taskSpan);
        }

        out.print("</td>");

        StringBuffer sBuf = new StringBuffer();
        // output status
        sBuf.append("\n<td class='status' style='background-repeat: no-repeat; background-image:url(images/").append(task.getStatusIcon()).append(")'>");
        //outputStatus(out, task);
        int status = (task.getWorkflowObjectStatus() > 0) ? task.getStatus() : (task.getStatus() * -1);
        //sBuf.append("<span style='font-weight: bold; font-size: 10px; color: orange;'>").append(status).append("</span>&nbsp;");
        //sBuf.append("<img src='").append(imgbase).append("/").append(task.getStatusIcon()).append("' width='").
        //      append(width).append("'  alt='").append(task.getRemarks()).append("' ").
        //    append("title='").append(task.getRemarks()).append("'").append("/>"); 
        sBuf.append("\n\t<span class='percentage_txt'>\n&nbsp;&nbsp;").append(task.getCompleteDisplay());

        if (task.getTaskId() == Constants.TASK_ID_FLAG_RESPONSE || task.getTaskId() == Constants.TASK_ID_UNSET_FLAG) {
            sBuf.append("\n\t\t<span class='status_txt' title='").append(task.getRemarks()).append("'>").append("</span>\n\t").append("</span>");
        } else {
            sBuf.append("\n\t\t<span class='status_txt' title='").append(task.getRemarks()).append("'>").append(status).append("</span>\n\t").append("</span>");
        }

        out.print(sBuf.toString());
        out.print("\n</td>");

        // output deadline
        out.print("<td>");
        if (task.getDurTime() == null) {
            task.setDisplayDeadline("--");
        } else {
            task.setDisplayDeadline(DateUtils.date2Str(task.getDurTime(), DateUtils.DEFAULT_DATE_FORMAT_2));
        }

        out.print(task.getDisplayDeadline());
        out.print("</td>");

        // output task status
        out.print("<td>");
        out.print(task.getTaskStatus());
        out.print("</td>");

        // output action options
        out.print("<td>");

//      if (hasEditDeadlineRight && task.getDurTime() != null && task.getStatus() != Constants.TASK_STATUS_DONE) {
        if (hasEditDeadlineRight && task.getStatus() != Constants.TASK_STATUS_DONE && task.getStatus() != Constants.TASK_STATUS_INACTIVE) {
            out.print("&nbsp;&nbsp;&nbsp;<a id='inline' href='#deadline' title='"
                    + getI18nMessage(Messages.KEY_COMMON_BUTTON_EDITDEADLINE)
                    + "' onclick='setAssignment(\"" + task.getAssignmentId() + "\",\"" + taskDisplay + "\",\"" + task.getDisplayDeadline() + "\", \"" + StringEscapeUtils.escapeXml(userName) + "\");'>"
                    + getI18nMessage(Messages.KEY_COMMON_BUTTON_EDIT) + "</a>");
        }
  
        if (hasEditDeadlineRight && task.getStatus() != Constants.TASK_STATUS_DONE && task.getStatus() != Constants.TASK_STATUS_INACTIVE) {
            if (task.getTaskType() == 4 || task.getTaskType() == 9) {  // these are peer review task types.
                out.print("<td><a href='#' title='End Assignment' onclick='return endPeerReviewAssignment(\""
                        + task.getAssignmentId() + "\", \"" + StringEscapeUtils.escapeXml(userName) + "\", \"" + task.getTaskName() + "\", \"" + task.getTargetName() + " " + task.getProductName() + "\");'>"
                        + getI18nMessage(Messages.KEY_COMMON_JS_MSG_END_ASSIGNMENT) + "</a></td>");
            } else {
                out.print("<td><a href='#' onclick='endAssignment(\""
                        + task.getAssignmentId() + "\", \"" + StringEscapeUtils.escapeXml(userName) + "\", \"" + task.getTaskName() + "\", \"" + task.getTargetName() + " " + task.getProductName() + "\");'>"
                        + getI18nMessage(Messages.KEY_COMMON_JS_MSG_END_ASSIGNMENT) + "</a></td>");
            }
        }

        out.print("</tr>");
        out.flush();
    }

    public void setBodyContent() {
    }

    /**
     * @param prjid the prjid to set
     */
    public void setPrjid(Object prjid) {
        try {
            this.prjid = (Integer) ExpressionEvaluatorManager.evaluate("prjid", prjid.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set prjid value error" + ex);
        }
    }

    /**
     * @param targetUid the targetUid to set
     */
    public void setTargetUid(Object targetUid) {
        try {
            this.targetUid = (Integer) ExpressionEvaluatorManager.evaluate("targetUid", targetUid.toString(), Integer.class, this, pageContext);
        } catch (JspException ex) {
            logger.error("set targetUid value error" + ex);
        }
    }

    /**
     * @param targetUid the targetUid to set
     */
    public void setSubjectUid(Object subjectUid) {
        try {
            if (subjectUid != null) {
                this.subjectUid = (Integer) ExpressionEvaluatorManager.evaluate("subjectUid", subjectUid.toString(), Integer.class, this, pageContext);
            }
        } catch (JspException ex) {
            logger.error("set uid value error" + ex);
        }
    }
}
