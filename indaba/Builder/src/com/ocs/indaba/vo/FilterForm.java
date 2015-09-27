/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import java.util.List;

/**
 *
 * @author Jeff
 */
public class FilterForm {

    private List<Integer> targetIds;
    private boolean targetSelectionInclude;
    private List<Integer> productIds;
    private boolean prodSelectionInclude;
    private List<Integer> roleIds;
    private boolean roleSelectionInclude;
    private List<Integer> teamIds;
    private boolean teamSelectionInclude;
    private int status;
    private int caseStatus;

    public FilterForm() {
    }

    public FilterForm(List<Integer> targetIds, boolean targetSelectionInclude,
            List<Integer> productIds, boolean prodSelectionInclude,
            List<Integer> roleIds, boolean roleSelectionInclude,
            List<Integer> teamIds, boolean teamSelectionInclude,
            int status) {
        this.targetIds = targetIds;
        this.targetSelectionInclude = targetSelectionInclude;
        this.productIds = productIds;
        this.prodSelectionInclude = prodSelectionInclude;
        this.roleIds = roleIds;
        this.roleSelectionInclude = roleSelectionInclude;
        this.teamIds = teamIds;
        this.teamSelectionInclude = teamSelectionInclude;
        this.status = status;
    }

    public FilterForm(List<Integer> targetIds, boolean targetSelectionInclude,
            List<Integer> productIds, boolean prodSelectionInclude,
            List<Integer> roleIds, boolean roleSelectionInclude,
            List<Integer> teamIds, boolean teamSelectionInclude,
            int status, int caseStatus) {
        this.targetIds = targetIds;
        this.targetSelectionInclude = targetSelectionInclude;
        this.productIds = productIds;
        this.prodSelectionInclude = prodSelectionInclude;
        this.roleIds = roleIds;
        this.roleSelectionInclude = roleSelectionInclude;
        this.teamIds = teamIds;
        this.teamSelectionInclude = teamSelectionInclude;
        this.status = status;
        this.caseStatus = caseStatus;
    }

    public boolean isProdSelectionInclude() {
        return prodSelectionInclude;
    }

    public void setProdSelectionInclude(boolean prodSelectionInclude) {
        this.prodSelectionInclude = prodSelectionInclude;
    }

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Integer> getTargetIds() {
        return targetIds;
    }

    public void setTargetIds(List<Integer> targetIds) {
        this.targetIds = targetIds;
    }

    public boolean isTargetSelectionInclude() {
        return targetSelectionInclude;
    }

    public void setTargetSelectionInclude(boolean targetSelectionInclude) {
        this.targetSelectionInclude = targetSelectionInclude;
    }

    public int getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(int caseStatus) {
        this.caseStatus = caseStatus;
    }

    @Override
    public String toString() {
        StringBuffer strBuf = new StringBuffer("FilterForm[");
        strBuf.append("\ntargetIds: ").append(targetIds);
        strBuf.append(", targetSelectionInclude: ").append(targetSelectionInclude);
        strBuf.append("\nproductIds: ").append(productIds);
        strBuf.append(", prodSelectionInclude: ").append(prodSelectionInclude);
        strBuf.append("\nroleIds: ").append(getRoleIds());
        strBuf.append(", roleSelectionInclude: ").append(isRoleSelectionInclude());
        strBuf.append("\nteamuctIds: ").append(getTeamIds());
        strBuf.append(", teamSelectionInclude: ").append(isTeamSelectionInclude());
        strBuf.append("\nstatus: ").append(status);
        strBuf.append(", caseStatus: ").append(caseStatus);
        strBuf.append(']');
        return strBuf.toString();
    }

    /**
     * @return the roleIds
     */
    public List<Integer> getRoleIds() {
        return roleIds;
    }

    /**
     * @return the roleSelectionInclude
     */
    public boolean isRoleSelectionInclude() {
        return roleSelectionInclude;
    }

    /**
     * @param roleSelectionInclude the roleSelectionInclude to set
     */
    public void setRoleSelectionInclude(boolean roleSelectionInclude) {
        this.roleSelectionInclude = roleSelectionInclude;
    }

    /**
     * @return the teamIds
     */
    public List<Integer> getTeamIds() {
        return teamIds;
    }

    /**
     * @return the teamSelectionInclude
     */
    public boolean isTeamSelectionInclude() {
        return teamSelectionInclude;
    }

    /**
     * @param teamSelectionInclude the teamSelectionInclude to set
     */
    public void setTeamSelectionInclude(boolean teamSelectionInclude) {
        this.teamSelectionInclude = teamSelectionInclude;
    }

    /**
     * @param roleIds the roleIds to set
     */
    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    /**
     * @param teamIds the teamIds to set
     */
    public void setTeamIds(List<Integer> teamIds) {
        this.teamIds = teamIds;
    }
}
