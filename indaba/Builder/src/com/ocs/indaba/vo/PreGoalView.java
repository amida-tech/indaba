/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

/**
 *
 * @author Jeff
 */
public class PreGoalView {

    private int id;
    private int workflowSequenceId;
    private int preWorkflowSequenceId;
    private int preGoalId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPreGoalId() {
        return preGoalId;
    }

    public void setPreGoalId(int preGoalId) {
        this.preGoalId = preGoalId;
    }

    public int getPreWorkflowSequenceId() {
        return preWorkflowSequenceId;
    }

    public void setPreWorkflowSequenceId(int preWorkflowSequenceId) {
        this.preWorkflowSequenceId = preWorkflowSequenceId;
    }

    public int getWorkflowSequenceId() {
        return workflowSequenceId;
    }

    public void setWorkflowSequenceId(int workflowSequenceId) {
        this.workflowSequenceId = workflowSequenceId;
    }
}
