/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

import java.util.List;

/**
 *
 * @author yc06x
 */
public class TaskAssignmentWithGrid {

    private AssignedTask assignment;
    private String progressDisplay;
    private List<SurveyAnswerStatus> answerStatusList;

    public String getProgressDisplay() {
        return this.progressDisplay;
    }

    public void setProgressDisplay(String value) {
        this.progressDisplay = value;
    }

    public List<SurveyAnswerStatus> getAnswerStatusList() {
        return this.answerStatusList;
    }

    public void setAnswerStatusList(List<SurveyAnswerStatus> value) {
        this.answerStatusList = value;
    }

    public AssignedTask getAssignment() {
        return this.assignment;
    }

    public void setAssignment(AssignedTask value) {
        this.assignment = value;
    }
}
