/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */

package com.ocs.indaba.vo;

/**
 *
 * @author Jeff
 */
public class Assignment implements java.io.Serializable {

    private int id;
    private int ownerId;
    private int horseId; // in prototype, it is notebook id.
    private int taskid;
    private long lastAssigned;

    public Assignment() {
    }

    public Assignment(int id, int ownerId, int horseId, int taskid, long lastAssigned) {
        this.id = id;
        this.ownerId = ownerId;
        this.horseId = horseId;
        this.taskid = taskid;
        this.lastAssigned = lastAssigned;
    }

    public long getLastAssigned() { 
    	return lastAssigned;
    }
    public void setLastAssigned(long lastAssigned) {
    	this.lastAssigned = lastAssigned;
    }
    public int getHorseId() {
        return horseId;
    }

    public void setHorseId(int horseId) {
        this.horseId = horseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getTaskid() {
        return taskid;
    }

    public void setTask(int taskid) {
        this.taskid = taskid;
    }
}
