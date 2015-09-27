/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
*/

package com.ocs.indaba.vo;

/**
 *
 * @author menglong
 */
public class Queue extends Object{
    private String contentName;
    private String milestone;
    private int inQueueDays;
    private String priority;

    private boolean showAssign;
    private String assignUser;
    
    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public int getInQueueDays() {
        return inQueueDays;
    }

    public void setInQueueDays(int inQueueDays) {
        this.inQueueDays = inQueueDays;
    }

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean getShowAssign() {
        return showAssign;
    }

    public void setShowAssign(boolean showAssign) {
        this.showAssign = showAssign;
    }

    public String getAssignUser() {
        return assignUser;
    }

    public void setAssignUser(String assignUser) {
        this.assignUser = assignUser;
    }

    
}
