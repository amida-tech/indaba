/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.xo;

import com.ocs.indaba.po.Language;
import com.ocs.indaba.po.Organization;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class SurveyConfig {

    private String id;
    private String name;
    private List<Organization> orgs;
    private String description = null;
    private String instructions = null;
    private short visibility;

    private Language language;
    private int dboId;
    private List<SurveyCategory> rootCats;
    private List<SurveyQuestion> questions;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void addOrg(Organization org) {
        if (orgs == null) orgs = new ArrayList<Organization>();
        orgs.add(org);
    }

    public List<Organization> getOrgs() {
        return orgs;
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

    public void setInstructions(String desc) {
        this.instructions = desc;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public void setVisibility(short v) {
        this.visibility = v;
    }

    public short getVisibility() {
        return this.visibility;
    }

    public void setLanguage(Language lang) {
        this.language = lang;
    }

    public Language getLanguage() {
        return this.language;
    }

    public void setDboId(int id) {
        this.dboId = id;
    }

    public int getDboId() {
        return this.dboId;
    }

    public void addCategory(SurveyCategory cat) {
        if (rootCats == null) rootCats = new ArrayList<SurveyCategory>();
        rootCats.add(cat);
    }

    public List<SurveyCategory> getRootCategories() {
        return rootCats;
    }

    public void addQuestion(SurveyQuestion q) {
        if (questions == null) questions = new ArrayList<SurveyQuestion>();
        questions.add(q);
    }

    public List<SurveyQuestion> getQuestions() {
        return questions;
    }
}
