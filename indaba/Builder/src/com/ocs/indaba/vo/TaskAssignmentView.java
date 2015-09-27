/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.po.TaskAssignment;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class TaskAssignmentView {

    private String username = "";
    private String priority;
    private int inQueueDays;
    private boolean isDone;
    private int status;
    private String statusDisplay;
    private List<QueueUser> qualifiedUsers = null;//the users who can be assigned
    private TaskAssignment taskAssignment;

    public boolean isIsDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public TaskAssignment getTaskAssignment() {
        return taskAssignment;
    }

    public void setTaskAssignment(TaskAssignment taskAssignment) {
        this.taskAssignment = taskAssignment;
    }

    public int getInQueueDays() {
        return inQueueDays;
    }

    public void setInQueueDays(int inQueueDays) {
        this.inQueueDays = inQueueDays;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * @return the qualifiedUsers
     */
    public List<QueueUser> getQualifiedUsers() {
        return qualifiedUsers;
    }

    /**
     * @param qualifiedUsers the qualifiedUsers to set
     */
    public void setQualifiedUsers(List<QueueUser> qualifiedUsers) {
        this.qualifiedUsers = qualifiedUsers;
    }
}
