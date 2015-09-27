/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff
 */
public class AssignedTask {
    private static final Logger log = Logger.getLogger(AssignedTask.class);
    
    private int assignmentId;
    private int assignedUserId;
    private String assignedUsername;
    private String displayUsername;
    private int taskId;
    private int taskType;
    private String data;
    private String taskName;
    private int productId;
    private String productName;
    private int targetId;
    private String targetName;
    private int horseId;
    private int workflowObjectStatus;
    private int status;
    private int toolId;
    private String toolName;
    private String action;
    private Date startTime;
    private int duration;
    private Date durTime;
    private Date deadline;
    private String displayDeadline = "--";
    private int utilDays;
    private String completeDisplay = "--";
    private float percentage;
    private String statusIcon;
    private String remarks = "unknown";
    private int completeStatus;
    private String instructions;
    private boolean clickable = false;
    private String taskStatus = "--";
    private short exitType;
    private TaskAssignmentStatusData tad;

    public AssignedTask() {
    }
    
    public int getUserId() {
        int l = data.indexOf(' ');
        if (l == -1)
            return Integer.parseInt(data);
        return Integer.parseInt(data.substring(0, l));
    }
    
    public String getContents() { 
        if (data == null)
            return "";
        int l = data.indexOf(' ');
        if (l == -1)
            return ""; 
        return data.substring(l+1);
    }
    
    public short getExitType() {
        return exitType;
    }

    public void setExitType(short exitType) {
        this.exitType = exitType;
    }
    
    public String getTaskStatus() {
        return taskStatus;
    }
    
    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getCompleteDisplay() {
        return completeDisplay;
    }

    public void setCompleteDisplay(String completeDisplay) {
        this.completeDisplay = completeDisplay;
    }

    public java.util.Date getDurTime() {
        return durTime;
    }

    public void setDurTime(java.util.Date durTime) {
        this.durTime = durTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    
    public int getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(int assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getAssignedUsername() {
        return assignedUsername;
    }

    public void setAssignedUsername(String assignedUsername) {
        this.assignedUsername = assignedUsername;
    }

    public String getDisplayUsername() {
        return displayUsername;
    }

    public void setDisplayUsername(String displayUsername) {
        this.displayUsername = displayUsername;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getHorseId() {
        return horseId;
    }

    public void setHorseId(int horseId) {
        this.horseId = horseId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getUtilDays() {
        return utilDays;
    }

    public void setUtilDays(int utilDays) {
        this.utilDays = utilDays;
    }

    public int getToolId() {
        return toolId;
    }

    public void setToolId(int toolId) {
        this.toolId = toolId;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatus(int completeStatus) {
        this.completeStatus = completeStatus;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getDisplayDeadline() {
        return displayDeadline;
    }

    public void setDisplayDeadline(String displayDeadline) {
        this.displayDeadline = displayDeadline;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public int getWorkflowObjectStatus() {
        return workflowObjectStatus;
    }

    public void setWorkflowObjectStatus(int workflowObjectStatus) {
        this.workflowObjectStatus = workflowObjectStatus;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatusIcon() {
        return statusIcon;
    }

    public void setStatusIcon(String statusIcon) {
        this.statusIcon = statusIcon;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public TaskAssignmentStatusData getTaskAssignmentData() {
        return tad;
    }

    public void setTaskAssignmentStatusData(TaskAssignmentStatusData tad) {
        this.tad = tad;
    }

    public void computeTaskStatus(int langId) {
        if (assignedUserId <= 0) {
            setTaskStatus(getI18nMessage(Messages.KEY_COMMON_MSG_TASKSTATUS_UNASSIGNED, langId));
        } else if (status == Constants.TASK_STATUS_DONE) {
            if (exitType == 0) {
                setTaskStatus(getI18nMessage(Messages.KEY_COMMON_MSG_TASKSTATUS_DONE, langId));
            } else {
                setTaskStatus(getI18nMessage(Messages.KEY_COMMON_MSG_TASKSTATUS_FORCED_EXIT, langId));
            }
        } else if (durTime != null && new Date().compareTo(durTime) > 0) {
            setTaskStatus(getI18nMessage(Messages.KEY_COMMON_MSG_TASKSTATUS_OVERDUE, langId));
        } else if (status == Constants.TASK_STATUS_INACTIVE) {
            setTaskStatus(getI18nMessage(Messages.KEY_COMMON_MSG_TASKSTATUS_INACTIVE, langId));
        } else {
            setTaskStatus(getI18nMessage(Messages.KEY_COMMON_MSG_TASKSTATUS_INFLIGHT, langId));
        }
    }


    private String getI18nMessage(String key, int languageId) {
        return Messages.getInstance().getMessage(key, languageId);
    }


    public void setTaskStatusIcon(int languageId) {
        if (this.getWorkflowObjectStatus() < 0) {
            this.setRemarks(getI18nMessage(Messages.KEY_COMMON_REMARK_SUSPENDED, languageId));
            this.setClickable(false);
            this.setStatusIcon(Constants.TASK_STATUS_ICON_STOPPED);
        } else if (Constants.WORKFLOW_OBJECT_STATUS_DONE == this.getWorkflowObjectStatus()
                || Constants.TASK_STATUS_DONE == this.getStatus()) {
            this.setRemarks(getI18nMessage(Messages.KEY_COMMON_REMARK_DONE, languageId));
            this.setStatusIcon(Constants.TASK_STATUS_ICON_STARTED);
        } else if (Constants.WORKFLOW_OBJECT_STATUS_WAITING == this.getWorkflowObjectStatus()
                || Constants.TASK_STATUS_INACTIVE == this.getStatus()) {
            this.setRemarks(getI18nMessage(Messages.KEY_COMMON_REMARK_INACTIVE, languageId));
            this.setStatusIcon(Constants.TASK_STATUS_ICON_IDLE);
        } else {
            switch (this.getStatus()) {
                case Constants.TASK_STATUS_INACTIVE: {
                    this.setRemarks(getI18nMessage(Messages.KEY_COMMON_REMARK_INACTIVE, languageId));
                    this.setStatusIcon(Constants.TASK_STATUS_ICON_IDLE);
                    break;
                }
                case Constants.TASK_STATUS_ACTIVE: {
                    this.setRemarks(getI18nMessage(Messages.KEY_COMMON_REMARK_ACTIVE, languageId));
                    this.setStatusIcon(Constants.TASK_STATUS_ICON_IDLE);
                    break;
                }
                case Constants.TASK_STATUS_AWARE: {
                    this.setRemarks(getI18nMessage(Messages.KEY_COMMON_REMARK_AWARE, languageId));
                    this.setStatusIcon(Constants.TASK_STATUS_ICON_IDLE);
                    break;
                }
                case Constants.TASK_STATUS_NOTICED: {
                    this.setRemarks(getI18nMessage(Messages.KEY_COMMON_ALT_NOTICED, languageId));
                    this.setStatusIcon(Constants.TASK_STATUS_ICON_IDLE);
                    break;
                }
                case Constants.TASK_STATUS_STARTED: {
                    this.setRemarks(getI18nMessage(Messages.KEY_COMMON_REMARK_STARTED, languageId));
                    this.setStatusIcon(Constants.TASK_STATUS_ICON_STARTED);
                    break;
                }
                case Constants.TASK_STATUS_DONE: {
                    this.setDisplayDeadline(getI18nMessage(Messages.KEY_COMMON_REMARK_DONE, languageId));
                    this.setRemarks(getI18nMessage(Messages.KEY_COMMON_REMARK_DONE, languageId));
                    this.setStatusIcon(Constants.TASK_STATUS_ICON_STARTED);
                    break;
                }
            }                  
        }

        // flag tasks
        if (this.getTaskId() == Constants.TASK_ID_FLAG_RESPONSE) {
            this.setStatusIcon(Constants.TASK_STATUS_ICON_FLAG_TO_ME);
        } else if (this.getTaskId() == Constants.TASK_ID_UNSET_FLAG) {
            this.setStatusIcon(Constants.TASK_STATUS_ICON_FLAG_BY_ME);
        }

        if (tad == null) {
            // compute the complete display
            int p = (int) ((percentage + 0.0001) * 100);
            if (p < 0) {
                this.setCompleteDisplay("--");
            } else {
                this.setCompleteDisplay(String.valueOf(p) + "%");
            }
        } else {
            this.setCompleteDisplay("" + tad.getCompletedItems() + "/" + tad.getTotalItems());
        }
    }


    @Override
    public String toString() {
        StringBuffer sBuf = new StringBuffer("AssignedTask: ");
        sBuf.append("assignmentId=").append(assignmentId);
        sBuf.append(", assignedUserId=").append(assignedUserId);
        sBuf.append(", assignedUsername=").append(assignedUsername);
        sBuf.append(", displayUsername=").append(displayUsername);
        sBuf.append(", taskId=").append(taskId);
        sBuf.append(", taskType=").append(taskType);
        sBuf.append(", taskData=").append(data);
        sBuf.append(", taskName=").append(taskName);
        sBuf.append(", productId=").append(productId);
        sBuf.append(", productName=").append(productName);
        sBuf.append(", targetId=").append(targetId);
        sBuf.append(", targetName=").append(targetName);
        sBuf.append(", horseId=").append(horseId);
        sBuf.append(", workflowObjectStatus=").append(workflowObjectStatus);
        sBuf.append(", status=").append(status);
        sBuf.append(", toolId=").append(toolId);
        sBuf.append(", toolName=").append(toolName);
        sBuf.append(", action=").append(action);
        sBuf.append(", startTime=").append(startTime);
        sBuf.append(", duration=").append(duration);
        sBuf.append(", durTime=").append(durTime);
        sBuf.append(", deadline=").append(deadline);
        sBuf.append(", displayDeadline=").append(displayDeadline);
        sBuf.append(", utilDays=").append(utilDays);
        sBuf.append(", completeDisplay=").append(completeDisplay);
        sBuf.append(", percentage=").append(percentage);
        sBuf.append(", statusIcon=").append(statusIcon);
        sBuf.append(", remarks=").append(remarks);
        sBuf.append(", completeStatus=").append(completeStatus);
        sBuf.append(", instructions=").append(instructions);
        sBuf.append(", clickable=").append(clickable);
        return sBuf.toString();
    }
}
