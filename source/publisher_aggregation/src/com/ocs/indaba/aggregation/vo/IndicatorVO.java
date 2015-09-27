/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.vo;

import com.ocs.indaba.po.Organization;
import com.ocs.indaba.po.SurveyIndicator;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jiangjeff
 */
public class IndicatorVO {

    private int id;
    private String name;
    private String questionTitle;
    private int questionId;
    private String questionName;
    private int projectId;
    private int orgId;
    private String orgName;
    private int answerTypeNum;
    private String answerType;
    private List<String> scoreCriterias;
    private List<Organization> orgList;
    private boolean checked = false;
    private String tag;
    protected static final String COLUMN_SEPARATOR = ",";
    protected static final String OBJECT_SEPARATOR = "|";
    protected static final String PART_SEPARATOR = ":";

    public IndicatorVO(SurveyIndicator si) {
        this.id = si.getId();
        this.name = si.getName();
        this.questionTitle = si.getQuestion();
        this.answerTypeNum = si.getAnswerType();
    }

    public IndicatorVO() {
    }

    public String[] toCSVLine() {
        List<String> csvCols = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        csvCols.add("I" + id);
        csvCols.add(name);
        csvCols.add(questionTitle);
        csvCols.add(answerType);

        int i, size;
        for (i = 0, size = scoreCriterias.size(); i < size - 1; i++) {
            sb.append(scoreCriterias.get(i)).append(OBJECT_SEPARATOR);
        }
        if (i < size) {
            sb.append(scoreCriterias.get(i));
        }
        csvCols.add(sb.toString());

        if (orgList.size() > 1) {
            sb.setLength(0);
            Organization org;
            for (i = 0, size = orgList.size(); i < size - 1; i++) {
                org = orgList.get(i);
                sb.append(org.getName()).append(PART_SEPARATOR).append(org.getId()).append(OBJECT_SEPARATOR);
            }
            org = orgList.get(i);
            sb.append(org.getName()).append(PART_SEPARATOR).append(org.getId());
            csvCols.add(sb.toString());
        }

        return csvCols.toArray(new String[]{});
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("I").append(id).append(COLUMN_SEPARATOR).append(name).append(COLUMN_SEPARATOR).append(questionTitle).append(COLUMN_SEPARATOR).append(answerType).append(COLUMN_SEPARATOR);

        int i, size;
        for (i = 0, size = scoreCriterias.size(); i < size - 1; i++) {
            sb.append(scoreCriterias.get(i)).append(OBJECT_SEPARATOR);
        }
        sb.append(scoreCriterias.get(i));

        if (orgList.size() > 1) {
            sb.append(COLUMN_SEPARATOR);
            Organization org;
            for (i = 0, size = orgList.size(); i < size - 1; i++) {
                org = orgList.get(i);
                sb.append(org.getName()).append(PART_SEPARATOR).append(org.getId()).append(OBJECT_SEPARATOR);
            }
            org = orgList.get(i);
            sb.append(org.getName()).append(PART_SEPARATOR).append(org.getId());
        }
        return sb.append("\r\n").toString();
    }

    /**
     * Get the value of orgName
     *
     * @return the value of orgName
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * Set the value of orgName
     *
     * @param orgName new value of orgName
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * Get the value of organizationId
     *
     * @return the value of organizationId
     */
    public int getOrgId() {
        return orgId;
    }

    /**
     * Set the value of organizationId
     *
     * @param organizationId new value of organizationId
     */
    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    /**
     * Get the value of projectId
     *
     * @return the value of projectId
     */
    public int getProjectId() {
        return projectId;
    }

    /**
     * Set the value of projectId
     *
     * @param projectId new value of projectId
     */
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    /**
     * Get the value of questionId
     *
     * @return the value of questionId
     */
    public int getQuestionId() {
        return questionId;
    }

    /**
     * Set the value of questionId
     *
     * @param questionId new value of questionId
     */
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    /**
     * Get the value of questionName
     *
     * @return the value of questionName
     */
    public String getQuestionName() {
        return questionName;
    }

    /**
     * Set the value of questionName
     *
     * @param questionName new value of questionName
     */
    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    /**
     * Get the value of question_title
     *
     * @return the value of question_title
     */
    public String getQuestionTitle() {
        return questionTitle;
    }

    /**
     * Set the value of question_title
     *
     * @param question_title new value of question_title
     */
    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public int getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the answerTypeNum
     */
    public int getAnswerTypeNum() {
        return answerTypeNum;
    }

    /**
     * @param answerTypeNum the answerTypeNum to set
     */
    public void setAnswerTypeNum(int answerTypeNum) {
        this.answerTypeNum = answerTypeNum;
    }

    /**
     * @return the answerType
     */
    public String getAnswerType() {
        return answerType;
    }

    /**
     * @param answerType the answerType to set
     */
    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    /**
     * @return the scoreCriterias
     */
    public List<String> getScoreCriterias() {
        return scoreCriterias;
    }

    /**
     * @param scoreCriterias the scoreCriterias to set
     */
    public void setScoreCriterias(List<String> scoreCriterias) {
        this.scoreCriterias = scoreCriterias;
    }

    /**
     * @return the orgList
     */
    public List<Organization> getOrgList() {
        return orgList;
    }

    /**
     * @param orgList the orgList to set
     */
    public void setOrgList(List<Organization> orgList) {
        this.orgList = orgList;
    }

    /**
     * @return the checked
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * @param checked the checked to set
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    /**
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(String tag) {
        this.tag = tag;
    }
}
