/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
*/

package com.ocs.indaba.vo;

/**
 *
 * @author luwb
 */
public class QueueTaskAssignment {
    private int id;//the taskAssignment id
    private String TargetName;//the target name. eg. Argentina, China
    private int inQueueDays;//the days from active
    private String priority;//the priority of taskassignment
    private String assignedUserName;//the name of assignUser;
    private int assignedUserId;//the id of assignUser
    private boolean assigned = false;//whether this taskassignment is assigned;
    private boolean assignedToMe = false;//whether this taskassignment is assigned to me
    private boolean isDone = false;//whether this taskassignment is done

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the TargetName
     */
    public String getTargetName() {
        return TargetName;
    }

    /**
     * @param TargetName the TargetName to set
     */
    public void setTargetName(String TargetName) {
        this.TargetName = TargetName;
    }

    /**
     * @return the inQueueDays
     */
    public int getInQueueDays() {
        return inQueueDays;
    }

    /**
     * @param inQueueDays the inQueueDays to set
     */
    public void setInQueueDays(int inQueueDays) {
        this.inQueueDays = inQueueDays;
    }

    /**
     * @return the priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * @return the assignedUserName
     */
    public String getAssignedUserName() {
        return assignedUserName;
    }

    /**
     * @param assignedUserName the assignedUserName to set
     */
    public void setAssignedUserName(String assignedUserName) {
        this.assignedUserName = assignedUserName;
    }

    /**
     * @return the assignedUserId
     */
    public int getAssignedUserId() {
        return assignedUserId;
    }

    /**
     * @param assignedUserId the assignedUserId to set
     */
    public void setAssignedUserId(int assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    /**
     * @return the assigned
     */
    public boolean isAssigned() {
        return assigned;
    }

    /**
     * @param assigned the assigned to set
     */
    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    /**
     * @return the assignedToMe
     */
    public boolean isAssignedToMe() {
        return assignedToMe;
    }

    /**
     * @param assignedToMe the assignedToMe to set
     */
    public void setAssignedToMe(boolean assignedToMe) {
        this.assignedToMe = assignedToMe;
    }

    /**
     * @return the isDone
     */
    public boolean isIsDone() {
        return isDone;
    }

    /**
     * @param isDone the isDone to set
     */
    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

}
