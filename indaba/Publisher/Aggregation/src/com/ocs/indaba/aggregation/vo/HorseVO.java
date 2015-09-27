/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.vo;

/**
 *
 * @author Jeff
 */
public class HorseVO {

    int horseId;
    int targetId;
    String targetName = null;
    boolean done;

    public HorseVO() {
    }

    public HorseVO(int horseId, int targetId, boolean done) {
        this.horseId = horseId;
        this.targetId = targetId;
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getHorseId() {
        return horseId;
    }

    public void setHorseId(int horseId) {
        this.horseId = horseId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    @Override
    public String toString() {
        return "HorseVO{" + "horseId=" + horseId + ", targetId=" + targetId + ", targetName=" + targetName + ", done=" + done + '}';
    }
}
