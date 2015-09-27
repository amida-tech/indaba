/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.vo;

import java.util.List;

/**
 *
 * @author jiangjeff
 */
public class WorksetForm {

    private int id;
    private int userId = -1;
    private int orgId = -1;
    private String name = null;
    private String notes = null;
    private short visibility = -1;
    private List<Integer> projectIds = null;
    private List<Integer> indicatorIds = null;
    private List<Integer> targetIds = null;
    private List<String> projectNames = null;
    private List<String> indicatorNames = null;
    private List<String> targetNames = null;
    private boolean changed = false;
    private boolean hasDatapoint = false;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Integer> getIndicatorIds() {
        return indicatorIds;
    }

    public void setIndicatorIds(List<Integer> indicatorIds) {
        this.indicatorIds = indicatorIds;
    }

    public List<String> getIndicatorNames() {
        return indicatorNames;
    }

    public void setIndicatorNames(List<String> indicatorNames) {
        this.indicatorNames = indicatorNames;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public short getVisibility() {
        return visibility;
    }

    public void setVisibility(short visibility) {
        this.visibility = visibility;
    }

    public List<Integer> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<Integer> projectIds) {
        this.projectIds = projectIds;
    }

    public List<String> getProjectNames() {
        return projectNames;
    }

    public void setProjectNames(List<String> projectNames) {
        this.projectNames = projectNames;
    }

    public List<Integer> getTargetIds() {
        return targetIds;
    }

    public void setTargetIds(List<Integer> targetIds) {
        this.targetIds = targetIds;
    }

    public List<String> getTargetNames() {
        return targetNames;
    }

    public void setTargetNames(List<String> targetNames) {
        this.targetNames = targetNames;
    }

    @Override
    public String toString() {
        return "WorksetForm{" + "userId=" + userId + "name=" + name + "notes=" + notes + "visibility=" + visibility + "projectIds=" + projectIds + "indicatorIds=" + indicatorIds + "targetIds=" + targetIds + "projectNames=" + projectNames + "indicatorNames=" + indicatorNames + "targetNames=" + targetNames + '}';
    }

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
     * @return the changed
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * @param changed the changed to set
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
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
