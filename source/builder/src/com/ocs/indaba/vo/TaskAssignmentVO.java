/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.po.TaskAssignment;

/**
 *
 * @author Jeff
 */
public class TaskAssignmentVO extends TaskAssignment {

    private String taskName;
    private String targetName;
    private String assignedUsername;
    private String statusDisplay;
    private String duetimeDisplay;

    public String getAssignedUsername() {
        return assignedUsername;
    }

    public void setAssignedUsername(String assignedUsername) {
        this.assignedUsername = assignedUsername;
    }

    public String getDuetimeDisplay() {
        return duetimeDisplay;
    }

    public void setDuetimeDisplay(String duetimeDisplay) {
        this.duetimeDisplay = duetimeDisplay;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }
}
