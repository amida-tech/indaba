/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.model;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.*;
import com.ocs.indaba.po.AtcChoiceIntl;
import com.ocs.indaba.po.SurveyIndicatorIntl;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author seanpcheng
 */
@Component
public class TransValidation {

    private String name = null;
    private String question = null;
    private int indicatorId = -1;
    private int indicatorLangId = -1;
    private int type = -1;
    private String tip = null;
    private int langId = -1;
    private int answerTypeId = -1;
    private List<IndicatorChoiceTrans> choices = null;
    private String errorMsg = null;
    private IndicatorTransValidation transval = null;

    static private SurveyIndicatorIntlDAO surveyIndicatorIntlDao = null;
    static private AtcChoiceIntlDAO atcChoiceIntlDao = null;
    private static final Logger logger = Logger.getLogger(TransValidation.class);

    // dummy constructor to make autowire work
    public TransValidation() {
    }

    public TransValidation(IndicatorTransValidation transval) {
        this.transval = transval;
    }

    @Autowired
    public void setAtcChoiceIntlDAO(AtcChoiceIntlDAO dao) {
        atcChoiceIntlDao = dao;
    }

    @Autowired
    public void setSurveyIndicatorIntlDAO(SurveyIndicatorIntlDAO dao) {
        surveyIndicatorIntlDao = dao;
    }

    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setLangId(int langId) {
        this.langId = langId;
    }

    public void setIndicatorLangId(int langId) {
        this.indicatorLangId = langId;
    }

    public void setAnswerTypeId(int id) {
        this.answerTypeId = id;
    }

    public void setIndicatorId(int id) {
        this.indicatorId = id;
    }

    public void addChoice(IndicatorChoiceTrans choice) {
        if (this.choices == null) {
            this.choices = new ArrayList<IndicatorChoiceTrans>();
        }
        this.choices.add(choice);
    }

    public void setErrorMsg(int lineNum, String msg) {
        if (errorMsg == null) {
            errorMsg = "Line " + lineNum + ": " + msg;
            transval.incrementError();
        }
    }

    //Getters
    public int getLangId() {
        return this.langId;
    }

    public int getIndicatorLangId() {
        return this.indicatorLangId;
    }

    public int getAnswerTypeId() {
        return this.answerTypeId;
    }

    public String getName() {
        return this.name;
    }

    public String getQuestion() {
        return this.question;
    }

    public String getTip() {
        return this.tip;
    }

    public int getType() {
        return this.type;
    }

    public int getIndicatorId() {
        return this.indicatorId;
    }

    public List<IndicatorChoiceTrans> getChoices() {
        if (this.choices == null) {
            this.choices = new ArrayList<IndicatorChoiceTrans>();
        }
        return this.choices;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public int load() {
        SurveyIndicatorIntl si = null;
        if ((si = surveyIndicatorIntlDao.findByIndicatorIdAndLanguage(indicatorId, langId)) != null) {
            si.setQuestion(question);
            si.setTip(tip);
            surveyIndicatorIntlDao.update(si);
        } else {
            si = new SurveyIndicatorIntl();
            si.setLanguageId(langId);
            si.setQuestion(question);
            si.setTip(tip);
            si.setSurveyIndicatorId(indicatorId);
            si = surveyIndicatorIntlDao.create(si);
        }

        if (this.type == Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE || this.type == Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE) {
            if (this.getChoices() != null) {
                for (IndicatorChoiceTrans ic : this.getChoices()) {
                    logger.debug(">>> criteria: " + ic.getCriteria() + ", label: " + ic.getLabel() + ", atcChoiceId: " + ic.getAtcChoiceId() + ", langId: " + langId);
                    AtcChoiceIntl atcchoiceintl = null;

                    if ((atcchoiceintl = atcChoiceIntlDao.findByChoiceIdAndLanguage(ic.getAtcChoiceId(), langId)) == null) {
                        atcchoiceintl = new AtcChoiceIntl();
                        atcchoiceintl.setCriteria(ic.getCriteria());
                        atcchoiceintl.setLabel(ic.getLabel());
                        atcchoiceintl.setAtcChoiceId(ic.getAtcChoiceId());
                        atcchoiceintl.setLanguageId(langId);
                        atcChoiceIntlDao.create(atcchoiceintl);
                    } else {
                        atcchoiceintl.setCriteria(ic.getCriteria());
                        atcchoiceintl.setLabel(ic.getLabel());
                        atcChoiceIntlDao.update(atcchoiceintl);
                    }
                }
            }
        }
        return (si == null) ? -1 : si.getId();
    }
}
