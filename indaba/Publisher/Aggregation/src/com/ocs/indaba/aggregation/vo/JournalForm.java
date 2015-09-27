/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.vo;

import com.ocs.indaba.aggregation.common.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jeanbone
 */
public class JournalForm {

    private int projectId;
    private String projectName;
    private int productId;
    private String productName;
    private List<Integer> targetIds;
    private List<String> targetNames;           // useless

    public JournalForm() {
        projectId = Constants.INVALID_INT_ID;
        productId = Constants.INVALID_INT_ID;
        projectName = null;
        productName = null;
        targetIds = new ArrayList<Integer>();
        targetNames = new ArrayList<String>();
    }

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
     * @return the projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName the projectName to set
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * @return the productId
     */
    public int getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the targetIds
     */
    public List<Integer> getTargetIds() {
        return targetIds;
    }

    /**
     * @param targetIds the targetIds to set
     */
    public void setTargetIds(List<Integer> targetIds) {
        this.targetIds = targetIds;
    }

    /**
     * @return the targetNames
     */
    public List<String> getTargetNames() {
        return targetNames;
    }

    /**
     * @param targetNames the targetNames to set
     */
    public void setTargetNames(List<String> targetNames) {
        this.targetNames = targetNames;
    }
}
