/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.service.impl;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.controlpanel.model.IndicatorChoiceTrans;
import com.ocs.indaba.controlpanel.model.IndicatorTransValidation;
import com.ocs.indaba.controlpanel.model.TransValidation;
import com.ocs.indaba.dao.*;
import com.ocs.indaba.po.*;
import com.ocs.ssu.SpreadsheetWriter;
import com.ocs.ssu.UnsupportedFileTypeException;
import com.ocs.ssu.WriterFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;

/**
 *
 * @author seanpcheng
 */
@Component
public class IndicatorTextExporter {

    private static final Logger logger = Logger.getLogger(IndicatorTextExporter.class);

    private List<Integer> indicatorIDs = null;
    private int langId = Constants.LANG_EN;
    private SpreadsheetWriter writer = null;
    private HashMap<Integer, String> langMap = null;
    private IndicatorTransValidation inds = null;

    private static SurveyIndicatorDAO surveyIndicatorDao = null;
    private static SurveyIndicatorIntlDAO surveyIndicatorIntlDao = null;
    private static AnswerTypeDAO answerTypeDao = null;
    private static AtcChoiceDAO atcChoiceDao = null;
    private static AtcChoiceIntlDAO atcIntlDao = null;
    private static LanguageDAO langDao = null;

    @Autowired
    public void setSurveyIndicatorIntlDAO(SurveyIndicatorIntlDAO dao) {
        surveyIndicatorIntlDao = dao;
    }

    @Autowired
    public void setAtcChoiceIntlDAO(AtcChoiceIntlDAO dao) {
        atcIntlDao = dao;
    }

    @Autowired
    public void setAnswerTypeDAO(AnswerTypeDAO dao) {
        answerTypeDao = dao;
    }

    @Autowired
    public void setAtcChoiceDAO(AtcChoiceDAO dao) {
        atcChoiceDao = dao;
    }

    @Autowired
    public void setSurveyIndicatorDAO(SurveyIndicatorDAO dao) {
        logger.debug("Got Survey Indicator DAO!");
        surveyIndicatorDao = dao;
    }

    @Autowired
    public void setLanguageDAO(LanguageDAO langDAO) {
        langDao = langDAO;
    }

    public IndicatorTextExporter() {
    }

    public IndicatorTextExporter(List<Integer> indicatorIDs, int langId, OutputStream output) throws IOException, UnsupportedFileTypeException {
        this.indicatorIDs = indicatorIDs;
        writer = WriterFactory.createWriter(output, WriterFactory.FILE_TYPE_XLS);
        this.langId = langId;
    }

    public void setLanguage(int langId) {
        this.langId = langId;
    }

    public int export() throws IOException {
        if (indicatorIDs == null || indicatorIDs.isEmpty()) {
            return 0;
        }
        inds = new IndicatorTransValidation();

        int count = 0;
        for (Integer id : indicatorIDs) {
            if (exportIndicator(id)) {
                count++;
            }
        }
        langMap = buildLanguageMap();

        createCSV();
        writer.flush();
        writer.close();

        return count;
    }

    private boolean exportIndicator(int id) {
        SurveyIndicator si;
        AnswerTypeChoice atc;

        si = surveyIndicatorDao.selectSurveyIndicatorById(id);

        if (si == null) {
            return false;
        }

        TransValidation val = new TransValidation(inds);
        inds.addTransValidation(val);

        val.setIndicatorId(id);
        val.setName(si.getName());
        val.setIndicatorLangId(si.getLanguageId());
        val.setLangId(si.getLanguageId());
        val.setQuestion(si.getQuestion());
        val.setTip(si.getTip());

        // try to get record from survey_indicator_intl that matches the requested lang
        SurveyIndicatorIntl indIntl = surveyIndicatorIntlDao.findByIndicatorIdAndLanguage(id, langId);
        if (indIntl != null) {
            val.setLangId(langId);
            val.setQuestion(indIntl.getQuestion());
            val.setTip(indIntl.getTip());
        }

        switch (si.getAnswerType()) {
            case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
            case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
                atc = answerTypeDao.getAnswerTypeChoice(si.getAnswerTypeId());
                List<AtcChoice> choices = atcChoiceDao.getAtcChoicesByAnswerTypeChoiceId(atc.getId());
                for (AtcChoice choice : choices) {
                    IndicatorChoiceTrans ict = new IndicatorChoiceTrans();
                    ict.setCriteria(choice.getCriteria());
                    ict.setLabel(choice.getLabel());
                    ict.setAtcChoiceId(choice.getId());
                    ict.setLanguageId(val.getIndicatorLangId());

                    AtcChoiceIntl chIntl = atcIntlDao.findByChoiceIdAndLanguage(choice.getId(), langId);
                    if (chIntl != null) {
                        ict.setCriteria(chIntl.getCriteria());
                        ict.setLabel(chIntl.getLabel());
                        ict.setLanguageId(langId);
                    }

                    val.addChoice(ict);
                }
                break;

            default:
                break;

        }

        return true;
    }

    private void createCSV() throws IOException {
        // create CREATOR ORG
        String[] line = new String[IndicatorCsvDefs.TRANS_COL_COUNT];
        line[IndicatorCsvDefs.TRANS_COL_POS_NAME] = IndicatorCsvDefs.COL_LABEL_NAME;
        line[IndicatorCsvDefs.TRANS_COL_POS_ITEM_TYPE] = IndicatorCsvDefs.COL_LABEL_ITEM_TYPE;
        line[IndicatorCsvDefs.TRANS_COL_POS_ITEM_KEY] = IndicatorCsvDefs.COL_LABEL_ITEM_KEY;
        line[IndicatorCsvDefs.TRANS_COL_POS_LANGUAGE] = IndicatorCsvDefs.COL_LABEL_LANGUAGE;
        line[IndicatorCsvDefs.TRANS_COL_POS_ITEM_TEXT] = IndicatorCsvDefs.COL_LABEL_ITEM_TEXT;

        writer.writeNext(line);
              
        for (TransValidation val : inds.getIndicatorValidations()) {
            // write the main line
            line[IndicatorCsvDefs.TRANS_COL_POS_NAME] = val.getName();
            line[IndicatorCsvDefs.TRANS_COL_POS_ITEM_TYPE] = IndicatorCsvDefs.TRANS_ITEM_TYPE_IQ;
            line[IndicatorCsvDefs.TRANS_COL_POS_ITEM_KEY] = Integer.toString(val.getIndicatorId());
            line[IndicatorCsvDefs.TRANS_COL_POS_LANGUAGE] = langMap.get(val.getLangId());
            line[IndicatorCsvDefs.TRANS_COL_POS_ITEM_TEXT] = val.getQuestion();
            writer.writeNext(line);

            line[IndicatorCsvDefs.TRANS_COL_POS_NAME] = "";
            line[IndicatorCsvDefs.TRANS_COL_POS_ITEM_TYPE] = IndicatorCsvDefs.TRANS_ITEM_TYPE_IH;
            line[IndicatorCsvDefs.TRANS_COL_POS_ITEM_KEY] = Integer.toString(val.getIndicatorId());
            line[IndicatorCsvDefs.TRANS_COL_POS_LANGUAGE] = langMap.get(val.getLangId());
            line[IndicatorCsvDefs.TRANS_COL_POS_ITEM_TEXT] = val.getTip();
            writer.writeNext(line);

            if (val.getChoices() != null) {
                for (IndicatorChoiceTrans ict : val.getChoices()) {
                    line[IndicatorCsvDefs.TRANS_COL_POS_NAME] = "";
                    line[IndicatorCsvDefs.TRANS_COL_POS_ITEM_TYPE] = IndicatorCsvDefs.TRANS_ITEM_TYPE_OL;
                    line[IndicatorCsvDefs.TRANS_COL_POS_ITEM_KEY] = Integer.toString(ict.getAtcChoiceId());
                    line[IndicatorCsvDefs.TRANS_COL_POS_LANGUAGE] = langMap.get(ict.getLanguageId());
                    line[IndicatorCsvDefs.TRANS_COL_POS_ITEM_TEXT] = ict.getLabel();
                    writer.writeNext(line);

                    line[IndicatorCsvDefs.TRANS_COL_POS_NAME] = "";
                    line[IndicatorCsvDefs.TRANS_COL_POS_ITEM_TYPE] = IndicatorCsvDefs.TRANS_ITEM_TYPE_OC;
                    line[IndicatorCsvDefs.TRANS_COL_POS_ITEM_KEY] = Integer.toString(ict.getAtcChoiceId());
                    line[IndicatorCsvDefs.TRANS_COL_POS_LANGUAGE] = langMap.get(ict.getLanguageId());
                    line[IndicatorCsvDefs.TRANS_COL_POS_ITEM_TEXT] = ict.getCriteria();
                    writer.writeNext(line);
                }
            }
        }
    }

    
    private HashMap<Integer, String> buildLanguageMap() {
        HashMap<Integer, String> langmap = new HashMap<Integer, String>();
        List<Language> langs = langDao.selectAllLanguages();

        for (Language lang : langs) {
            String str = lang.getLanguageDesc();
            langmap.put(lang.getId(), str);
        }

        return langmap;
    }

}
