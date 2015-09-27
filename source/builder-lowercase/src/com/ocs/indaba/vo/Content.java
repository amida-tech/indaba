/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.po.Cases;
import com.ocs.indaba.po.User;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Jeff
 */
public class Content {

    private long contentId;
    private String contentName;
    private int steps;
    private int curStep;
    private double percentage;
    private List<Cases> openCases;
    private List<User> assignedUsers;
    private String nextStepOwner;
    private String nextStepAction;
    private int dueDays;
    private Date deadline;
    private Date estimation;

    public Content() {
    }

    public Content(long contentId, String contentName, int steps, int curStep, String nextStepOwner, String nextStepAction, Date deadline, Date estimation) {
        this.contentId = contentId;
        this.contentName = contentName;
        this.steps = steps;
        this.curStep = curStep;
        this.nextStepOwner = nextStepOwner;
        this.nextStepAction = nextStepAction;
        this.deadline = deadline;
        this.estimation = estimation;
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

    public int getCurStep() {
        return curStep;
    }

    public void setCurStep(int curStep) {
        this.curStep = curStep;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getEstimation() {
        return estimation;
    }

    public void setEstimation(Date estimation) {
        this.estimation = estimation;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getNextStepAction() {
        return nextStepAction;
    }

    public void setNextStepAction(String nextStepAction) {
        this.nextStepAction = nextStepAction;
    }

    public String getNextStepOwner() {
        return nextStepOwner;
    }

    public void setNextStepOwner(String nextStepOwner) {
        this.nextStepOwner = nextStepOwner;
    }

    public List<Cases> getOpenCases() {
        return openCases;
    }

    public void setOpenCases(List<Cases> openCases) {
        this.openCases = openCases;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public int getDueDays() {
        return dueDays;
    }

    public void setDueDays(int dueDays) {
        this.dueDays = dueDays;
    }

    public List<User> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(List<User> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }
}
