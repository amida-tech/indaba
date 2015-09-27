/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.vo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author rocky
 */
public class JournalSummaryInfo {

    private int projectId;
    private int productId;
    private String productName;
    private String projectName;
    private String organizationName;
    private ArrayList<String> targetName;
    private String studyPeriod;
    private Date exportDate;

    @Override
    public String toString() {
        StringBuilder temp = new StringBuilder();
        temp.append(projectName).append("\\");
        temp.append(organizationName).append("\\");
        temp.append(productName).append("\\");
        temp.append(studyPeriod).append("\\");
        for (int i = 0; i < targetName.size(); i++) {
            if (i != targetName.size() - 1) {
                temp.append(targetName.get(i)).append("|");
            } else {
                temp.append(targetName.get(i)).append("\\");
            }
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        temp.append(df.format(exportDate));
        return temp.toString();
    }

    public List<String[]> toCsv() {
        List<String[]> csv = new ArrayList<String[]>();
        ArrayList<String> col1 = new ArrayList<String>();
        ArrayList<String> result = new ArrayList<String>();
//        StringBuilder temp = new StringBuilder();
        col1.add("Project");
        col1.add("Organization");
        col1.add("Product");
        col1.add("Study Period");
        col1.add("Targets( " + targetName.size() + ")");

        result.add(productName);
        result.add(organizationName);
        result.add(productName);
        result.add(studyPeriod);
        String targets = "";
        for (int i = 0; i < targetName.size(); i++) {
            if (i != targetName.size() - 1) {
                targets += targetName.get(i) + "|";
            } else {
                targets += targetName.get(i);
                result.add(targets);
            }
        }
        col1.add("Export Time");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        result.add(df.format(exportDate));
        csv.add(col1.toArray(new String[]{}));
        csv.add(result.toArray(new String[]{}));
        return csv;
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
     * @return the organizationName
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * @param organizationName the organizationName to set
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
     * @return the targetName
     */
    public ArrayList<String> getTargetName() {
        return targetName;
    }

    /**
     * @param targetName the targetName to set
     */
    public void setTargetName(ArrayList<String> targetName) {
        this.targetName = targetName;
    }

    /**
     * @return the studyPeriod
     */
    public String getStudyPeriod() {
        return studyPeriod;
    }

    /**
     * @param studyPeriod the studyPeriod to set
     */
    public void setStudyPeriod(String studyPeriod) {
        this.studyPeriod = studyPeriod;
    }

    /**
     * @return the exportDate
     */
    public Date getExportDate() {
        return exportDate;
    }

    /**
     * @param exportDate the exportDate to set
     */
    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }
}
