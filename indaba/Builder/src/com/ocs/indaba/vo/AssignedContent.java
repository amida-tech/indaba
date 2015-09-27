/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.vo;

import java.util.Date;

/**
 * 
 * @author Jeff
 */
public class AssignedContent {

    private long contentId;
    private String contentName;
    private String actionName;
    private double completed;
    private int days;
    private Date deadline;

    public AssignedContent() {
    }

    public AssignedContent(long contentId, String contentName, String actionName, double completed, Date deadline) {
        this.contentId = contentId;
        this.contentName = contentName;
        this.actionName = actionName;
        this.completed = completed;
        this.deadline = deadline;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public double getCompleted() {
        return completed;
    }

    public void setCompleted(double completed) {
        this.completed = completed;
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

}
