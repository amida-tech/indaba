/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.model;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.*;
import com.ocs.indaba.po.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author seanpcheng
 */
@Component
public class IndicatorValidation {
    
    private String name = null;
    private String question = null;
    private int type = -1;
    private int refId = -1;
    private String tip = null;
    private int creatorOrgId = -1;
    private int langId = -1;
    private int visibilityId = -1;
    private int stateId = Constants.RESOURCE_STATE_TEST;
    private int maxInt = -1;
    private int minInt = -1;
    private double maxFloat = -1.0;
    private double minFloat = -1.0; 
    private List<Integer> tagIDs = null;  
    private String criteria = null;
    private List<IndicatorChoice> choices = null;
    private int parentIndicatorId = 0;
    private String errorMsg = null;
    private IndicatorImportValidation importVal;

    private int answerTypeDbId = -1;
    
    static private AtcChoiceDAO atcChoiceDao = null;
    static private SurveyIndicatorDAO surveyIndicatorDao = null;
    static private AnswerTypeIntegerDAO answerTypeIntegerDao = null;
    static private AnswerTypeFloatDAO answerTypeFloatDao = null;
    static private AnswerTypeChoiceDAO answerTypeChoiceDao = null;
    static private AnswerTypeTextDAO answerTypeTextDao = null;
    static private IndicatorTagDAO indicatorTagDao = null;
    
    private static final Logger logger = Logger.getLogger(IndicatorValidation.class);

    
    @Autowired
    public void setAtcChoiceDAO(AtcChoiceDAO dao) {
        atcChoiceDao = dao;
    }
    
    @Autowired
    public void setIndicatorTagDAO(IndicatorTagDAO dao) {
        indicatorTagDao = dao;
    }
    
    @Autowired
    public void setAnswerTypeIntegerDAO(AnswerTypeIntegerDAO dao) {
        answerTypeIntegerDao = dao;
    }
    
    @Autowired
    public void setAnswerTypeFloatDAO(AnswerTypeFloatDAO dao) {
        answerTypeFloatDao = dao;
    }
    
    @Autowired
    public void setAnswerTypeTextDAO(AnswerTypeTextDAO dao) {
        answerTypeTextDao = dao;
    }
    
    @Autowired
    public void setAnswerTypeChoiceDAO(AnswerTypeChoiceDAO dao) {
        answerTypeChoiceDao = dao;
    }
    
    @Autowired
    public void setSurveyIndicatorDAO(SurveyIndicatorDAO surveyIndicatorDAO) {
        surveyIndicatorDao = surveyIndicatorDAO;
    }
    
    public IndicatorValidation(String name, IndicatorImportValidation importVal) {
        this.name = name;
        this.importVal = importVal;
    }
    
    public IndicatorValidation() {
        
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
    
    public void setRefId(int refId) {
        this.refId = refId;
    }
    
    public void setTip (String tip) {
        this.tip = tip;
    }
    
    public void setCreatorOrgId(int creatorOrgId) {
        this.creatorOrgId = creatorOrgId;
    }
      
    public void setLangId(int langId) {
        this.langId = langId;
    }
    
    public void setVisibility(int visibility) {
        this.visibilityId = visibility;
    }
    
    public void setState(int state) {
        this.stateId = state;
    }
    
    public void setMaxInt (int maxInt) {
        this.maxInt = maxInt;
    }
    
    public void setMinInt (int minInt) {
        this.minInt = minInt;
    }
    
    public void setMaxFloat (double maxFloat) {
        this.maxFloat = maxFloat;
    }
    
    public void setMinFloat (double minFloat) {
        this.minFloat = minFloat;
    }

    public void setParentIndicatorId(int value) {
        this.parentIndicatorId = value;
    }
       
    public void addTagId(Integer tagId) {
        if (this.tagIDs == null) {
            this.tagIDs = new ArrayList<Integer>();
        }
        this.tagIDs.add(tagId);
    }
    
    public void addChoice(IndicatorChoice choice) {
        if (this.choices == null) {
            this.choices = new ArrayList<IndicatorChoice>();
        }
        this.choices.add(choice);
    }
    
    public void setErrorMsg(int lineNum, String msg) {
        logger.debug("Setting error: " + msg);
        if (errorMsg == null) {
            errorMsg = "Line " + lineNum + ": " + msg;
            importVal.incrementError();
        }
    }
    
    //Getters
    
    public String getName() {
        return this.name;
    }
    
    public String getQuestion() {
        return this.question;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getRefId() {
        return this.refId;
    }
    
    public String getTip() {
        return this.tip;
    }
    
    public int getCreatorOrgId() {
        return this.creatorOrgId;
    }
    
    public int getLangId() {
        return this.langId;
    }
    
    public int getVisibility() {
        return this.visibilityId;
    }
    
    public int getState() {
        return this.stateId;
    }
    
    public int getMaxInt() {
        return this.maxInt;
    }
    
    public int getMinInt() {
        return this.minInt;
    }
    
    public double getMaxFloat() {
        return this.maxFloat;
    }
    
    public double getMinFloat() {
        return this.minFloat;
    }
    
    public List<Integer> getTagIDs() {
        return this.tagIDs;
    }
    
    public List<IndicatorChoice> getChoices() {
        if (choices == null) {
            this.choices = new ArrayList<IndicatorChoice>();
        }
        return this.choices;
    }
    
    public String getErrorMsg() {
        return this.errorMsg;
    }

    public int load() {
        return load(name);
    }

    
    public int load(String indicatorName) {
        int answerTypeId = 0;

        if (this.answerTypeDbId < 0) {
            switch (this.type) {
                case Constants.SURVEY_ANSWER_TYPE_INTEGER:
                    AnswerTypeInteger dbo = new AnswerTypeInteger();
                    if (this.criteria != null) {
                        dbo.setCriteria(this.criteria);
                    }
                    dbo.setMinValue(minInt);
                    dbo.setMaxValue(maxInt);

                    dbo = answerTypeIntegerDao.create(dbo);
                    answerTypeId = dbo.getId();
                    break;

                case Constants.SURVEY_ANSWER_TYPE_FLOAT:
                    AnswerTypeFloat atf = new AnswerTypeFloat();
                    if (this.criteria != null) {
                        atf.setCriteria(this.criteria);
                    }
                    atf.setMinValue((float) this.minFloat);
                    atf.setMaxValue((float) this.maxFloat);

                    atf = answerTypeFloatDao.create(atf);
                    answerTypeId = atf.getId();
                    break;

                case Constants.SURVEY_ANSWER_TYPE_TEXT:
                    AnswerTypeText text = new AnswerTypeText();
                    if (this.criteria != null) {
                        text.setCriteria(this.criteria);
                    }
                    text.setMinChars(minInt);
                    text.setMaxChars(maxInt);

                    text = answerTypeTextDao.create(text);
                    answerTypeId = text.getId();

                    break;

                case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
                case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:

                    AnswerTypeChoice mc = new AnswerTypeChoice();
                    mc = answerTypeChoiceDao.create(mc);
                    answerTypeId = mc.getId();

                    int seq = 0;

                    for (IndicatorChoice choice : this.getChoices()) {
                        AtcChoice atcchoice = new AtcChoice();
                        atcchoice.setAnswerTypeChoiceId(answerTypeId);
                        atcchoice.setCriteria(choice.getCriteria());
                        atcchoice.setLabel(choice.getLabel());
                        atcchoice.setScore((int) (choice.getScore() * 10000));
                        atcchoice.setUseScore(choice.getUseScore());

                        long mask = 1 << seq;
                        seq++;
                        atcchoice.setWeight(seq);
                        atcchoice.setMask(mask);
                        atcChoiceDao.create(atcchoice);
                    }
                    break;

                default:
                    break;
            }
            this.answerTypeDbId = answerTypeId;
        } else {
            answerTypeId = this.answerTypeDbId;
        }
        
        // create survey indicator
        SurveyIndicator si = new SurveyIndicator();
        si.setAnswerType((short)this.type);
        si.setAnswerTypeId(answerTypeId);
        si.setCreateTime(new Date());
        si.setCreateUserId(importVal.getUserId());
        si.setParentIndicatorId(parentIndicatorId);
        
        int creatorOrg = (this.creatorOrgId > 0) ? this.creatorOrgId : importVal.getDefaultCreatorOrgId();        
        si.setCreatorOrgId(creatorOrg);
        
        int lang = (this.langId > 0) ? this.langId : importVal.getDefaultLangId();
        si.setLanguageId(lang);

        short visibility = (short) ((this.visibilityId > 0) ? this.visibilityId : importVal.getDefaultVisibility());
        si.setVisibility(visibility);

        // Note that state is determined by visibility, regardless the value specified in import
        int state = (visibility == Constants.VISIBILITY_PUBLIC) ? Constants.RESOURCE_STATE_TEST : 0;
        si.setState((short)state);
                        
        si.setOwnerOrgId(creatorOrg);
        
        si.setName(indicatorName);
        si.setQuestion(question);
        si.setTip(tip);
        si.setReferenceId(refId);
        si.setStatus(Constants.INDICATOR_STATUS_ACTIVE);
        
        si = surveyIndicatorDao.create(si);
               
        IndicatorTag it = new IndicatorTag();
        it.setSurveyIndicatorId(si.getId());
        if (this.getTagIDs() != null) {        
            for (Integer tid : this.getTagIDs()) {
                it.setId(null);                
                it.setItagsId(tid);
                indicatorTagDao.create(it);
            }
        }

        return si.getId();
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }
    
    public String getCriteria() {
        return this.criteria;
    }

    public int getParentIndicatorId() {
        return this.parentIndicatorId;
    }
    
}
