/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.vo;

/**
 *
 * @author Luke
 */
public class WorkflowView {
    private int wfoId;
    private String workflowName;
    private int workflowStatus;
    private String workflowSequenceName;
    private int sequenceStatus;
    private String goalName;
    private int goalStatus;
    private String taskName;
    private int taskStatus;
    private String targetName;
    private int assignedUserId;

    public WorkflowView() {
    }

    public WorkflowView(int wfoId, String workflowName, int workflowStatus,
                                   String workflowSequenceName, int sequenceStatus,
                                   String goalName, int goalStatus) {
        this.wfoId = wfoId;
        this.workflowName = workflowName;
        this.workflowStatus = workflowStatus;
        this.workflowSequenceName = workflowSequenceName;
        this.sequenceStatus = sequenceStatus;
        this.goalName = goalName;
        this.goalStatus = goalStatus;
    }

    public int getWfoId() {
        return wfoId;
    }

    public void setWfoId(int wfoId) {
        this.wfoId = wfoId;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public int getWorkflowStatus() {
        return workflowStatus;
    }

    public void setWorkflowStatus(int workflowStatus) {
        this.workflowStatus = workflowStatus;
    }

    public String getWorkflowSequenceName() {
        return workflowSequenceName;
    }

    public void setWorkflowSequenceName(String workflowSequenceName) {
        this.workflowSequenceName = workflowSequenceName;
    }

    public int getSequenceStatus() {
        return sequenceStatus;
    }

    public void setSequenceStatus(int sequenceStatus) {
        this.sequenceStatus = sequenceStatus;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public int getGoalStatus() {
        return goalStatus;
    }

    public void setGoalStatus(int goalStatus) {
        this.goalStatus = goalStatus;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public int getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(int assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

}
