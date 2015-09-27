/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.vo;

/**
 *
 * @author Jeff
 */
public class ProductBriefVO {

    private int productId;
    private String productName;
    private String projectName;
    private int visibility;
    private int projectOrgId;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getProjectOrgId() {
        return projectOrgId;
    }

    public void setProjectOrgId(int projectOrgId) {
        this.projectOrgId = projectOrgId;
    }

    @Override
    public String toString() {
        return "ProductBriefVO{" + "productId=" + productId + ", productName=" + productName + ", projectName=" + projectName + ", visibility=" + visibility + ", projectOrgId=" + projectOrgId + '}';
    }

}
