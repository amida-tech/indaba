/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
*/

package com.ocs.indaba.vo;

/**
 *
 * @author luwb
 */
public class QueueSubmitResultView {
    private int assignedUserId;
    private String assignedUserName;
    private String priority;

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
}
