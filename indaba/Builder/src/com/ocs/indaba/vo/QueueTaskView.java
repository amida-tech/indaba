/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jeff Jiang
 */
public class QueueTaskView {

    private int productId;
    private int taskId;
    private String taskName;
    private int isMultiUser;
    private boolean canClaim;
    private boolean subscribed;
    private List<TargetBasedTaskView> targetBasedTasks;

    public int getIsMultiUser() {
        return isMultiUser;
    }

    public void setIsMultiUser(int isMultiUser) {
        this.isMultiUser = isMultiUser;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void addTargetBasedTask(TargetBasedTaskView view) {
        if (targetBasedTasks == null) {
            targetBasedTasks = new ArrayList<TargetBasedTaskView>();
        }
        targetBasedTasks.add(view);
    }

    public List<TargetBasedTaskView> getTargetBasedTasks() {
        return targetBasedTasks;
    }

    public void setTargetBasedTasks(List<TargetBasedTaskView> targetBasedTasks) {
        this.targetBasedTasks = targetBasedTasks;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public boolean isCanClaim() {
        return canClaim;
    }

    public void setCanClaim(boolean canClaim) {
        this.canClaim = canClaim;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
}
