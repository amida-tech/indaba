/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.vo;

import com.ocs.indaba.po.Target;

/**
 *
 * @author Jeanbone
 */
public class TargetVO {
    private Target target;
    private String tags;
    private int wsTargetId;

    /**
     * @return the target
     */
    public Target getTarget() {
        return target;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(Target target) {
        this.target = target;
    }

    /**
     * @return the tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getWsTargetId() {
        return wsTargetId;
    }

    public void setWsTargetId(int wsTargetId) {
        this.wsTargetId = wsTargetId;
    }
}
