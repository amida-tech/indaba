/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.vo;

import java.util.Date;

/**
 *
 * @author Luke
 */
public class WorkflowObjectView {
    private int wfoId;
    private int workflowId;
    private String workflowName;
    private int status;
    private String targetName;
    private Date startTime;

    public WorkflowObjectView() {
    }


    public int getWfoId() {
        return wfoId;
    }

    public void setWfoId(int wfoId) {
        this.wfoId = wfoId;
    }

    public int getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(int workflowId) {
        this.workflowId = workflowId;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

}
