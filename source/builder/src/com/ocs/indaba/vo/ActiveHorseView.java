/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.po.Cases;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Jeff
 */
public class ActiveHorseView {

    private int workflowId;
    private int workflowObjectId;
    private int workflowObjectStatus;
    private int projectId;
    private int horseId;
    private int targetId;
    private String targetName;
    private int productId;
    private String productName;
    private int contentType;
    private boolean userJoinedIn;
    private double completed;
    private int nextDue;
    private List<Cases> openCases;
    private List<UserDisplay> peopleAssigned;
    private Date startTime = null;
    private int totalDuration;
    private Date dueTime = null;//new Date();
    private Date estimationTime = null;//new Date();
    private AssignedTask activeTask;
    private List<Integer> nextTaskIds = null;
    //private List<AssignedTask> activeTasks;
    private List<SequenceObjectView> sequences;

    public ActiveHorseView() {
    }

    public ActiveHorseView(int workflowId, int workflowObjectId, int workflowObjectStatus, int projectId, int horseId, int targetId, String targetName, int productId, String productName, int contentType, boolean userJoinedIn, double completed, int nextDue, List<Cases> openCases, List<UserDisplay> peopleAssigned, AssignedTask activeTask, List<SequenceObjectView> sequences) {
        this.workflowId = workflowId;
        this.workflowObjectId = workflowObjectId;
        this.workflowObjectStatus = workflowObjectStatus;
        this.projectId = projectId;
        this.horseId = horseId;
        this.targetId = targetId;
        this.targetName = targetName;
        this.productId = productId;
        this.productName = productName;
        this.contentType = contentType;
        this.userJoinedIn = userJoinedIn;
        this.completed = completed;
        this.nextDue = nextDue;
        this.openCases = openCases;
        this.peopleAssigned = peopleAssigned;
        this.activeTask = activeTask;
        this.sequences = sequences;
    }

    public AssignedTask getActiveTask() {
        return activeTask;
    }

    public void setActiveTask(AssignedTask activeTask) {
        this.activeTask = activeTask;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(int workflowId) {
        this.workflowId = workflowId;
    }

    public int getWorkflowObjectId() {
        return workflowObjectId;
    }

    public void setWorkflowObjectId(int workflowObjectId) {
        this.workflowObjectId = workflowObjectId;
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

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public List<SequenceObjectView> getSequences() {
        return sequences;
    }

    public void setSequences(List<SequenceObjectView> sequences) {
        this.sequences = sequences;
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

    public Date getDueTime() {
        return dueTime;
    }

    public void setDueTime(Date durTime) {
        this.dueTime = durTime;
    }

    public Date getEstimationTime() {
        return estimationTime;
    }

    public void setEstimationTime(Date estimationTime) {
        this.estimationTime = estimationTime;
    }

    /*public List<AssignedTask> getActiveTasks() {
    return activeTasks;
    }

    public void setActiveTasks(List<AssignedTask> activeTasks) {
    this.activeTasks = activeTasks;
    }*/
    public double getCompleted() {
        return completed;
    }

    public void setCompleted(double completed) {
        this.completed = completed;
    }

    public int getNextDue() {
        return nextDue;
    }

    public void setNextDue(int nextDue) {
        this.nextDue = nextDue;
    }

    public List<Cases> getOpenCases() {
        return openCases;
    }

    public void setOpenCases(List<Cases> openCases) {
        this.openCases = openCases;
    }

    public List<UserDisplay> getPeopleAssigned() {
        return peopleAssigned;
    }

    public void setPeopleAssigned(List<UserDisplay> peopleAssigned) {
        this.peopleAssigned = peopleAssigned;
    }

    public boolean isUserJoinedIn() {
        return userJoinedIn;
    }

    public void setUserJoinedIn(boolean userJoinedIn) {
        this.userJoinedIn = userJoinedIn;
    }

    public List<Integer> getNextTaskIds() {
        return nextTaskIds;
    }

    public void setNextTaskIds(List<Integer> nextTaskIds) {
        this.nextTaskIds = nextTaskIds;
    }

    public void addNextTaskId(int taskId) {
        if (nextTaskIds == null) {
            nextTaskIds = new ArrayList<Integer>();
        }
        nextTaskIds.add(taskId);
    }

    public int getWorkflowObjectStatus() {
        return workflowObjectStatus;
    }

    public void setWorkflowObjectStatus(int workflowObjectStatus) {
        this.workflowObjectStatus = workflowObjectStatus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }
}
