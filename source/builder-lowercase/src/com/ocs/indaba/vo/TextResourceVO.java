/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

/**
 *
 * @author Jeff
 */
public class TextResourceVO {

    private int sourceFileTextResourceId;
    private int sourceFileId;
    private int textResourceId;
    private String resourceName;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getSourceFileId() {
        return sourceFileId;
    }

    public void setSourceFileId(int sourceFileId) {
        this.sourceFileId = sourceFileId;
    }

    public int getSourceFileTextResourceId() {
        return sourceFileTextResourceId;
    }

    public void setSourceFileTextResourceId(int sourceFileTextResourceId) {
        this.sourceFileTextResourceId = sourceFileTextResourceId;
    }

    public int getTextResourceId() {
        return textResourceId;
    }

    public void setTextResourceId(int textResourceId) {
        this.textResourceId = textResourceId;
    }
}
