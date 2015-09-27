/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.xo;

import com.ocs.indaba.po.Target;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class Scorecard {

    private String id;
    private Date submitTime;

    private Product product;
    private User user;
    private Target target;

    private List<SurveyAnswer> saList = new ArrayList<SurveyAnswer>();
    private int dboHorseId;
    private int dboContentHeaderId;
    private int dboSurveyContentObjectId;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setSubmitTime(Date t) {
        this.submitTime = t;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public Target getTarget() {
        return target;
    }

    public void setDboHorseId(int id) {
        this.dboHorseId = id;
    }

    public int getDboHorseId() {
        return dboHorseId;
    }

    public void setDboContentHeaderId(int id) {
        this.dboContentHeaderId = id;
    }

    public int getDboContentHeaderId() {
        return dboContentHeaderId;
    }

    public void setDboSurveyContentObjectId(int id) {
        this.dboSurveyContentObjectId = id;
    }

    public int getDboSurveyContentObjectId() {
        return dboSurveyContentObjectId;
    }

    public void addSurveyAnswer(SurveyAnswer sa) {
        saList.add(sa);
    }

    public List<SurveyAnswer> getSurveyAnswers() {
        return saList;
    }
}
