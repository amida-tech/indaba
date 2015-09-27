/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.po.Product;

/**
 *
 * @author Jeff Jiang
 */
public class ProductVO extends Product {

    String configName;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }
}
