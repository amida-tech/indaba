/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.xo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class SurveyCategory {

    private String id;
    private String parentId;
    private String description = null;
    private String label;
    private String title;
    private int weight;

    private SurveyConfig surveyConfig;
    private SurveyCategory parent;
    private List<SurveyCategory> children;
    private List<SurveyQuestion> questions;

    private int dboId;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setSurveyConfig(SurveyConfig sc) {
        this.surveyConfig = sc;
    }

    public SurveyConfig getSurveyConfig() {
        return this.surveyConfig;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParent(SurveyCategory parent) {
        this.parent = parent;
    }

    public SurveyCategory getParent() {
        return parent;
    }

    public void setWeight(int w) {
        this.weight = w;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setDboId(int id) {
        this.dboId = id;
    }

    public int getDboId() {
        return this.dboId;
    }

    public void addChild(SurveyCategory cat) {
        if (children == null) children = new ArrayList<SurveyCategory>();
        children.add(cat);
    }

    public List<SurveyCategory> getChildren() {
        return children;
    }

    public void addQuestion(SurveyQuestion qst) {
        if (questions == null) questions = new ArrayList<SurveyQuestion>();
        questions.add(qst);
    }

    public List<SurveyQuestion> getQuestions() {
        return questions;
    }
    
}
