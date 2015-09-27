/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Jeff
 */
public class GoalObjectView implements Comparable<GoalObjectView> {

    private int indexOfSeq;
    private int indexOfGoal;
    private int goalObjId;
    private int goalId;
    private String goalName;
    private int weight;
    private int status;
    private int duration;
    private Date enterTime;
    private Date exitTime;
    private double workload;       // WLS
    private Date completionTime;
    private List<GoalObjectView> parent;
    private List<GoalObjectView> children;

    public GoalObjectView() {
    }

    public void addChild(GoalObjectView child) {
        if (getChildren() == null) {
            setChildren(new ArrayList<GoalObjectView>(1));
        }
        getChildren().add(child);
    }

    public void addParent(GoalObjectView p) {
        if (getParent() == null) {
            setParent(new ArrayList<GoalObjectView>(1));
        }
        getParent().add(p);
    }

    public int compareTo(GoalObjectView o) {
        if (o.getCompletionTime() == null) {
            return -1;
        } else if (this.getCompletionTime() == null) {
            return 1;
        } else if (this.getCompletionTime().compareTo(o.getCompletionTime()) <= 0) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * @return the indexOfSeq
     */
    public int getIndexOfSeq() {
        return indexOfSeq;
    }

    /**
     * @param indexOfSeq the indexOfSeq to set
     */
    public void setIndexOfSeq(int indexOfSeq) {
        this.indexOfSeq = indexOfSeq;
    }

    /**
     * @return the indexOfGoal
     */
    public int getIndexOfGoal() {
        return indexOfGoal;
    }

    /**
     * @param indexOfGoal the indexOfGoal to set
     */
    public void setIndexOfGoal(int indexOfGoal) {
        this.indexOfGoal = indexOfGoal;
    }

    /**
     * @return the goalObjId
     */
    public int getGoalObjId() {
        return goalObjId;
    }

    /**
     * @param goalObjId the goalObjId to set
     */
    public void setGoalObjId(int goalObjId) {
        this.goalObjId = goalObjId;
    }

    /**
     * @return the goalId
     */
    public int getGoalId() {
        return goalId;
    }

    /**
     * @param goalId the goalId to set
     */
    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    /**
     * @return the goalName
     */
    public String getGoalName() {
        return goalName;
    }

    /**
     * @param goalName the goalName to set
     */
    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    /**
     * @return the weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * @return the enterTime
     */
    public Date getEnterTime() {
        return enterTime;
    }

    /**
     * @param enterTime the enterTime to set
     */
    public void setEnterTime(Date enterTime) {
        this.enterTime = enterTime;
    }

    /**
     * @return the exitTime
     */
    public Date getExitTime() {
        return exitTime;
    }

    /**
     * @param exitTime the exitTime to set
     */
    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }

    /**
     * @return the workload
     */
    public double getWorkload() {
        return workload;
    }

    /**
     * @param workload the workload to set
     */
    public void setWorkload(double workload) {
        this.workload = workload;
    }

    /**
     * @return the completionTime
     */
    public Date getCompletionTime() {
        return completionTime;
    }

    /**
     * @param completionTime the completionTime to set
     */
    public void setCompletionTime(Date completionTime) {
        this.completionTime = completionTime;
    }

    /**
     * @return the parent
     */
    public List<GoalObjectView> getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(List<GoalObjectView> parent) {
        this.parent = parent;
    }

    /**
     * @return the children
     */
    public List<GoalObjectView> getChildren() {
        return children;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(List<GoalObjectView> children) {
        this.children = children;
    }
}

    