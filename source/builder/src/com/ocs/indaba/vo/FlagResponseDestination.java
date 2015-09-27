/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

/**
 *
 * @author yc06x
 */
public class FlagResponseDestination extends GroupActionResult {

    private String destination;
    private int taskAssignmentId;

    public void setDestination(String url) {
        this.destination = url;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setTaskAssignmentId(int value) {
        taskAssignmentId = value;
    }

    public int getTaskAssignmentId() {
        return taskAssignmentId;
    }
}
