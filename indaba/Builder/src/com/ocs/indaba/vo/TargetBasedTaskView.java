/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jeff Jiang
 */
public class TargetBasedTaskView {

    private short goalObjectStatus;
    private int targetId;
    private String targetName;
    private List<TaskAssignmentView> taskAssigns;

    public void addTaskAssignment(TaskAssignmentView view) {
        if (taskAssigns == null) {
            taskAssigns = new ArrayList<TaskAssignmentView>();
        }
        taskAssigns.add(view);
    }

    public List<TaskAssignmentView> getTaskAssigns() {
        return taskAssigns;
    }

    public void setTaskAssigns(List<TaskAssignmentView> taskAssigns) {
        this.taskAssigns = taskAssigns;
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

    public short getGoalObjectStatus() {
        return this.goalObjectStatus;
    }

    public void setGoalObjectStatus(short status) {
        this.goalObjectStatus = status;
    }
}
