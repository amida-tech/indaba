/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.vo;

/**
 *
 * @author luwb
 */
public class WsProjectVO {
    private int projectId;
    private String projcetName;
    private boolean checked = false;

    /**
     * @return the projectId
     */
    public int getProjectId() {
        return projectId;
    }

    /**
     * @param projectId the projectId to set
     */
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    /**
     * @return the projcetName
     */
    public String getProjcetName() {
        return projcetName;
    }

    /**
     * @param projcetName the projcetName to set
     */
    public void setProjcetName(String projcetName) {
        this.projcetName = projcetName;
    }

    /**
     * @return the checked
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * @param checked the checked to set
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
