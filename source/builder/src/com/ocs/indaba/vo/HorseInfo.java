/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.po.Horse;


/**
 *
 * @author Luke
 */
public class HorseInfo extends Horse{
    private String targetName;
    private String targetShortName;
    private String productName;
    private int status;
    private String statusDisplay;

    public HorseInfo() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetShortName() {
        return targetShortName;
    }

    public void setTargetShortName(String name) {
        this.targetShortName = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }
}
