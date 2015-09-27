package com.ocs.indaba.vo;

public class AssignedTaskDisplay {

    private String content;
    private int horseId;
    private String productName;
    private int workflowObjectStatus;
    private int contentType;

    public AssignedTaskDisplay(String content, int horseId, String productName, int workflowObjectStatus) {
        this.content = content;
        this.horseId = horseId;
        this.productName = productName;
        this.workflowObjectStatus = workflowObjectStatus;
    }

    /**
     * @return the contents
     */
    public String getContent() {
        return content;
    }

    /**
     * @param contents the contents to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the horseId
     */
    public int getHorseId() {
        return horseId;
    }

    /**
     * @param horseId the horseId to set
     */
    public void setHorseId(int horseId) {
        this.horseId = horseId;
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

    public int getWorkflowObjectStatus() {
        return workflowObjectStatus;
    }

    public void setWorkflowObjectStatus(int workflowObjectStatus) {
        this.workflowObjectStatus = workflowObjectStatus;
    }
    
    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

}