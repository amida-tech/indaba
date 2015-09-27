/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.vo;

import org.json.simple.JSONObject;

/**
 *
 * @author Jeff Jiang
 */
public class ProductForExportVO {
    //public  enum SORT_NAME{prodName, projName, orgName, studyPeriod};
    private int id;
    private String prodName;
    private int projId;
    private String projName;
    private int orgId;
    private String orgName;
    private int projVisibility;
    private String studyPeriod;
    private int isTsc;

    public JSONObject toJson() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("id", id);
        jsonObj.put("prodName", prodName);
        jsonObj.put("projName", projName);
        jsonObj.put("orgName", orgName);
        jsonObj.put("studyPeriod", studyPeriod);
        jsonObj.put("isTsc", isTsc);
        return jsonObj;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getStudyPeriod() {
        return studyPeriod;
    }

    public void setStudyPeriod(String studyPeriod) {
        this.studyPeriod = studyPeriod;
    }

    public int getProjId() {
        return projId;
    }

    public void setProjId(int projId) {
        this.projId = projId;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getProjVisibility() {
        return projVisibility;
    }

    public void setProjVisibility(int projVisibility) {
        this.projVisibility = projVisibility;
    }

    public int getIsTsc() {
        return this.isTsc;
    }

    public void setIsTsc(int v) {
        this.isTsc = v;
    }

    @Override
    public String toString() {
        return "ProductForExportVO{" + "id=" + id + "prodName=" + prodName + "projName=" + projName + "orgName=" + orgName + "studyPeriod=" + studyPeriod + "isTsc=" + isTsc + '}';
    }
}
