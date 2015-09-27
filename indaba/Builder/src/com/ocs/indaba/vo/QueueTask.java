/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
*/

package com.ocs.indaba.vo;

import java.util.List;

/**
 *
 * @author luwb
 */
public class QueueTask extends Object{
    private String taskName;
    private String taskDes;//description of the task
    private String productName;//name of the product this task belong to

    private boolean isAdmin= false;//normal user and admin show different view
    private int avgTimeToAssign;//average time from active to assign
    private int avgTimeToComplete;//average time from start to complete

    private List<QueueUser> qualifiedUsers = null;//the users who can be assigned
    private List<QueueTaskAssignment> taskAssignmentList = null;//the task_assignment list relate to the QueueTask

    /**
     * @return the taskName
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * @param taskName the taskName to set
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * @return the taskName
     */
    public String getTaskDes() {
        return taskDes;
    }

    /**
     * @param taskName the taskName to set
     */
    public void setTaskDes(String taskDes) {
        this.taskDes = taskDes;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the avgTimeToAssign
     */
    public int getAvgTimeToAssign() {
        return avgTimeToAssign;
    }

    /**
     * @param avgTimeToAssign the avgTimeToAssign to set
     */
    public void setAvgTimeToAssign(int avgTimeToAssign) {
        this.avgTimeToAssign = avgTimeToAssign;
    }

    /**
     * @return the avgTimeToComplete
     */
    public int getAvgTimeToComplete() {
        return avgTimeToComplete;
    }

    /**
     * @param avgTimeToComplete the avgTimeToComplete to set
     */
    public void setAvgTimeToComplete(int avgTimeToComplete) {
        this.avgTimeToComplete = avgTimeToComplete;
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

    /**
     * @return the isAdmin
     */
    public boolean isIsAdmin() {
        return isAdmin;
    }

    /**
     * @param isAdmin the isAdmin to set
     */
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * @return the taskAssignmentList
     */
    public List<QueueTaskAssignment> getTaskAssignmentList() {
        return taskAssignmentList;
    }

    /**
     * @param taskAssignmentList the taskAssignmentList to set
     */
    public void setTaskAssignmentList(List<QueueTaskAssignment> taskAssignmentList) {
        this.taskAssignmentList = taskAssignmentList;
    }

}
