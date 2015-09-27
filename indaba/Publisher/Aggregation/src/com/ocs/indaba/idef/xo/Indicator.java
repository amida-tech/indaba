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
public class Indicator {

    private String id;
    private String name;
    private String question;
    private short type;
    private String hint = null;
    private int min = 0;
    private int max = 0;
    private double minf = 0;
    private double maxf = 0;
    private String criteria = null;
    private List<AnswerChoice> choices = null;
    private List<Organization> orgs;
    private short visibility;

    private Language language;
    private Ref ref;

    private int dboId;

    public void setChoices(List<AnswerChoice> list) {
        this.choices = list;
    }

    public List<AnswerChoice> getChoices() {
        return this.choices;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setType(short type) {
        this.type = type;
    }

    public short getType() {
        return type;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getHint() {
        return hint;
    }

    public void setCriteria(String hint) {
        this.criteria = hint;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setMinInt(int min) {
        this.min = min;
    }

    public int getMinInt() {
        return this.min;
    }

    public void setMaxInt(int max) {
        this.max = max;
    }

    public int getMaxInt() {
        return this.max;
    }

    public void setMinDouble(double min) {
        this.minf = min;
    }

    public double getMinDouble() {
        return this.minf;
    }

    public void setMaxDouble(double max) {
        this.maxf = max;
    }

    public double getMaxDouble() {
        return this.maxf;
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

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return this.question;
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

    public void setRef(Ref ref) {
        this.ref = ref;
    }

    public Ref getRef() {
        return this.ref;
    }
}
