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
 * @author jiangjeff
 */
public class ScorecardInfo {

    private int id;
    private int horseId;
    private int contentHeaderId;
    private int projectId;
    private int orgId;
    private int surveyConfigId;
    private String surveyName;
    private int productId;
    private String prodouctName;
    private String prodouctDesc;
    private int targetId;
    private String targetName;
    private String targetShortName;
    private int studyPeriodId;
    private String studyPeriod;
    private String title;
    private int status;
    private double moe;
    private double legalFramework;
    private double implementation;
    private double implementationGap;
    private double overall;
    private int legalFrameworkCount = 0;
    private int implementationCount = 0;
    private int usedScoreCount = 0;
    private List<Tree<ScorecardBaseNode>> rootCategories = null;

    public ScorecardInfo() {
    }

    public ScorecardInfo(int id, int horseId, int productId, int targetId, int studyPeriodId) {
        this.id = id;
        this.horseId = horseId;
        this.productId = productId;
        this.targetId = targetId;
        this.studyPeriodId = studyPeriodId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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

    public int getHorseId() {
        return horseId;
    }

    public void setHorseId(int horseId) {
        this.horseId = horseId;
    }

    public int getStudyPeriodId() {
        return studyPeriodId;
    }

    public void setStudyPeriodId(int studyPeriodId) {
        this.studyPeriodId = studyPeriodId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getProdouctName() {
        return prodouctName;
    }

    public void setProdouctName(String prodouctName) {
        this.prodouctName = prodouctName;
    }

    public String getStudyPeriod() {
        return studyPeriod;
    }

    public void setStudyPeriod(String studyPeriod) {
        this.studyPeriod = studyPeriod;
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

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getImplementation() {
        return implementation;
    }

    public void setImplementation(double implementation) {
        this.implementation = implementation;
    }

    public double getImplementationGap() {
        return implementationGap;
    }

    public void setImplementationGap(double implementationGap) {
        this.implementationGap = implementationGap;
    }
    
    public double getLegalFramework() {
        return legalFramework;
    }

    public void setLegalFramework(double legalFramework) {
        this.legalFramework = legalFramework;
    }

    public double getMoe() {
        return moe;
    }

    public int getImplementationCount() {
        return implementationCount;
    }

    public void addImplementationCount(int implementationCount) {
        this.implementationCount += implementationCount;
    }

    public void incrementImplementationCount() {
        ++this.implementationCount;
    }

    public void setImplementationCount(int implementationCount) {
        this.implementationCount = implementationCount;
    }

    public int getLegalFrameworkCount() {
        return legalFrameworkCount;
    }

    public void incrementLegalFrameworkCount() {
        ++this.legalFrameworkCount;
    }

    public void addLegalFrameworkCount(int legalFrameworkCount) {
        this.legalFrameworkCount += legalFrameworkCount;
    }

    public void setLegalFrameworkCount(int legalFrameworkCount) {
        this.legalFrameworkCount = legalFrameworkCount;
    }

    public void setMoe(double moe) {
        this.moe = moe;
    }

    public double getOverall() {
        return overall;
    }

    public void setOverall(double overall) {
        this.overall = overall;
    }

    public int getContentHeaderId() {
        return contentHeaderId;
    }

    public void setContentHeaderId(int contentHeaderId) {
        this.contentHeaderId = contentHeaderId;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProdouctDesc() {
        return prodouctDesc;
    }

    public void setProdouctDesc(String prodouctDesc) {
        this.prodouctDesc = prodouctDesc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUsedScoreCount() {
        return usedScoreCount;
    }

    public void setUsedScoreCount(int usedScoreCount) {
        this.usedScoreCount = usedScoreCount;
    }

    public void incrementUsedScoreCount() {
        ++this.usedScoreCount;
    }
    
    public void increaseUsedScoreCount(int count) {
        this.usedScoreCount += count;
    }

    public String getTargetShortName() {
        return targetShortName;
    }

    public void setTargetShortName(String targetShortName) {
        this.targetShortName = targetShortName;
    }

    @Override
    public String toString() {
        return "ScorecardInfo{" + "id=" + id + ", horseId=" + horseId + ", contentHeaderId=" + contentHeaderId + ", projectId=" + projectId + ", orgId=" + orgId + ", surveyConfigId=" + surveyConfigId + ", surveyName=" + surveyName + ", productId=" + productId + ", prodouctName=" + prodouctName + ", prodouctDesc=" + prodouctDesc + ", targetId=" + targetId + ", targetName=" + targetName + ", targetShortName=" + targetShortName + ", studyPeriodId=" + studyPeriodId + ", studyPeriod=" + studyPeriod + ", title=" + title + ", status=" + status + ", moe=" + moe + ", legalFramework=" + legalFramework + ", implementation=" + implementation + ", implementationGap=" + implementationGap + ", overall=" + overall + ", legalFrameworkCount=" + legalFrameworkCount + ", implementationCount=" + implementationCount + ", usedScoreCount=" + usedScoreCount + ", rootCategories=" + rootCategories + '}';
    }

}
