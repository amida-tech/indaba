/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.vo;

import com.ocs.indaba.po.Product;

/**
 *
 * @author Jeanbone
 */
public class ProductVO {

    private Product product;
    private String projectName;

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
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

}
