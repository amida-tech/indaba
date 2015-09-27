/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

/**
 *
 * @author Administrator
 */

public class ToolIntl {
    
    private int toolId;
    
    private int languageId;
    
    private String purpose;
    
    private String inactiveReason;

    public ToolIntl() {
    }
    
    public int getToolId() {
        return toolId;
    }

    public void setToolId(int toolId) {
        this.toolId = toolId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getInactiveReason() {
        return inactiveReason;
    }

    public void setInactiveReason(String inactiveReason) {
        this.inactiveReason = inactiveReason;
    }

    
}
