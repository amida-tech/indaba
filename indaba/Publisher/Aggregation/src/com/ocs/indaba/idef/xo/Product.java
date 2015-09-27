/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.xo;

/**
 *
 * @author yc06x
 */
public class Product {

    private String id;
    private String name;
    private String description = null;
    private short contentType;
    private short mode;

    private Project project;
    private SurveyConfig surveyConfig;

    private int dboId;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return this.description;
    }

    public void setContentType(short type) {
        this.contentType = type;
    }

    public short getContentType() {
        return this.contentType;
    }

    public void setMode(short mode) {
        this.mode = mode;
    }

    public short getMode() {
        return this.mode;
    }

    public void setDboId(int id) {
        this.dboId = id;
    }

    public int getDboId() {
        return this.dboId;
    }

    public void setProject(Project proj) {
        this.project = proj;
    }

    public Project getProject() {
        return this.project;
    }

    public void setSurveyConfig(SurveyConfig sc) {
        this.surveyConfig = sc;
    }

    public SurveyConfig getSurveyConfig() {
        return this.surveyConfig;
    }
}
