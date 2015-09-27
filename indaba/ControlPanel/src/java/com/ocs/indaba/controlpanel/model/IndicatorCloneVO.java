/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.controlpanel.model;

import com.ocs.util.ValueObject;

/**
 *
 * @author luwenbin
 */
public class IndicatorCloneVO extends ValueObject{
    private int indicatorId;
    private int userId;
    private int orgId;
    private String name;
    private int visibity;

    public IndicatorCloneVO(int indicatorId, int userId, int orgId, String name, int visibility){
        this.indicatorId = indicatorId;
        this.userId = userId;
        this.orgId = orgId;
        this.name = name;
        this.visibity = visibility;
    }

    /**
     * @return the indicatorId
     */
    public int getIndicatorId() {
        return indicatorId;
    }

    /**
     * @param indicatorId the indicatorId to set
     */
    public void setIndicatorId(int indicatorId) {
        this.indicatorId = indicatorId;
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return the orgId
     */
    public int getOrgId() {
        return orgId;
    }

    /**
     * @param orgId the orgId to set
     */
    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the visibity
     */
    public int getVisibity() {
        return visibity;
    }

    /**
     * @param visibity the visibity to set
     */
    public void setVisibity(int visibity) {
        this.visibity = visibity;
    }
}
