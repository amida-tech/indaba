/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.vo;

/**
 *
 * @author luwb
 */
public class WorksetVO {
    private int id;
    private String name;
    private int orgId;
    private String orgName;
    private short visibility;
    private boolean isActive;
    private boolean hasDatapoint;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
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
     * @return the orgName
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * @param orgName the orgName to set
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * @return the visibility
     */
    public short getVisibility() {
        return visibility;
    }

    /**
     * @param visibility the visibility to set
     */
    public void setVisibility(short visibility) {
        this.visibility = visibility;
    }

    /**
     * @return the isActive
     */
    public boolean isIsActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return the hasDatapoint
     */
    public boolean isHasDatapoint() {
        return hasDatapoint;
    }

    /**
     * @param hasDatapoint the hasDatapoint to set
     */
    public void setHasDatapoint(boolean hasDatapoint) {
        this.hasDatapoint = hasDatapoint;
    }
}
