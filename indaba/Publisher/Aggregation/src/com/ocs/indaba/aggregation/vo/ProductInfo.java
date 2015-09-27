/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.vo;

import com.ocs.indaba.util.Tree;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jeff
 */
public class ProductInfo {

    private int surveyConfigId;
    private String surveyName = null;
    private int productId;
    private String prodouctName = null;
    private String prodouctDesc = null;
    private int studyPeriodId;
    private String studyPeriod = null;
    List<HorseVO> horses = null;
    private List<Tree<ScorecardBaseNode>> rootCategories = null;

    public String getProdouctName() {
        return prodouctName;
    }

    public void setProdouctName(String prodouctName) {
        this.prodouctName = prodouctName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getStudyPeriod() {
        return studyPeriod;
    }

    public void setStudyPeriod(String studyPeriod) {
        this.studyPeriod = studyPeriod;
    }

    public int getStudyPeriodId() {
        return studyPeriodId;
    }

    public void setStudyPeriodId(int studyPeriodId) {
        this.studyPeriodId = studyPeriodId;
    }

    public int getSurveyConfigId() {
        return surveyConfigId;
    }

    public void setSurveyConfigId(int surveyConfigId) {
        this.surveyConfigId = surveyConfigId;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getProdouctDesc() {
        return prodouctDesc;
    }

    public void setProdouctDesc(String prodouctDesc) {
        this.prodouctDesc = prodouctDesc;
    }

    public List<HorseVO> getHorses() {
        return horses;
    }

    public void setHorses(List<HorseVO> horses) {
        this.horses = horses;
    }

    public void addRootCategory(Tree<ScorecardBaseNode> rootCategory) {
        if (rootCategories == null) {
            rootCategories = new ArrayList<Tree<ScorecardBaseNode>>();
        }
        rootCategories.add(rootCategory);
    }

    public List<Tree<ScorecardBaseNode>> getRootCategories() {
        return rootCategories;
    }

    public void setRootCategories(List<Tree<ScorecardBaseNode>> rootCategories) {
        this.rootCategories = rootCategories;
    }

    @Override
    public String toString() {
        return "ProductInfo{surveyConfigId=" + surveyConfigId + ", surveyName=" + surveyName + ", productId=" + productId + ", prodouctName=" + prodouctName + ", studyPeriodId=" + studyPeriodId + ", studyPeriod=" + studyPeriod + ", horses=" + horses + ", rootCategories=" + rootCategories + '}';
    }
}
