/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.service.impl;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.controlpanel.model.IndicatorChoice;
import com.ocs.indaba.controlpanel.model.IndicatorImportValidation;
import com.ocs.indaba.controlpanel.model.IndicatorValidation;
import com.ocs.indaba.controlpanel.model.Utilities;
import com.ocs.indaba.dao.*;
import com.ocs.indaba.po.*;
import com.ocs.ssu.SpreadsheetWriter;
import com.ocs.ssu.UnsupportedFileTypeException;
import com.ocs.ssu.WriterFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author seanpcheng
 */
public class IndicatorExporter {

    private static final Logger logger = Logger.getLogger(IndicatorExporter.class);

    private List<Integer> indicatorIDs = null;
    private int langId = Constants.LANG_EN;
    private SpreadsheetWriter writer = null;
    private IndicatorImportValidation inds = null;
    private HashMap<Integer, String> orgMap = null;
    private HashMap<Integer, String> visMap = null;
    private HashMap<Integer, String> stateMap = null;
    private HashMap<Integer, String> langMap = null;
    private HashMap<Integer, String> tagMap = null;
    private HashMap<Integer, String> refMap = null;
    private HashMap<Integer, String> typeMap = null;

    private static SurveyIndicatorDAO surveyIndicatorDao = null;
    private static SurveyIndicatorIntlDAO surveyIndicatorIntlDao = null;
    private static AnswerTypeDAO answerTypeDao = null;
    private static AtcChoiceDAO atcChoiceDao = null;
    private static AtcChoiceIntlDAO atcIntlDao = null;
    private static ItagsDAO tagDao = null;

    @Autowired
    public void setItagsDAO(ItagsDAO tagDAO) {
        tagDao = tagDAO;
    }

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
        surveyIndicatorDao = dao;
    }

    public IndicatorExporter() {
    }

    
    public IndicatorExporter(List<Integer> indicatorIDs, int langId, OutputStream output) throws IOException, UnsupportedFileTypeException {
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

        inds = new IndicatorImportValidation();

        int count = 0;
        for (Integer id : indicatorIDs) {
            if (exportIndicator(id)) {
                count++;
            }
        }
        orgMap = Utilities.buildOrgMapI2N();
        visMap = Utilities.buildVisibilityMapI2N();
        stateMap = Utilities.buildResourceStateMapI2N();
        langMap = Utilities.buildLanguageMapI2N();
        refMap = Utilities.buildRefMapI2N();
        tagMap = Utilities.buildTagMapI2N();
        typeMap = Utilities.buildIndicatorTypeMapI2N();

        createCSV();
        writer.flush();
        writer.close();

        return count;

    }

    private boolean exportIndicator(int id) {
        SurveyIndicator si;
        AnswerTypeInteger ati;
        AnswerTypeFloat atf;
        AnswerTypeText att;
        AnswerTypeChoice atc;
        AtcChoice atcc;

        si = surveyIndicatorDao.selectSurveyIndicatorById(id);

        if (si == null) {
            return false;
        }

        if (inds.getDefaultLangId() < 0) {
            inds.setDefaultLang(langId);
            inds.setDefaultOrgID(si.getCreatorOrgId());
            inds.setDefaultState(si.getState());
            inds.setDefaultVisibility(si.getVisibility());
        }

        IndicatorValidation val = new IndicatorValidation(si.getName(), inds);        
        inds.addIndicatorValidation(val);
        val.setCreatorOrgId(si.getCreatorOrgId());

        // set language
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

        val.setState(si.getState());
        val.setVisibility(si.getVisibility());
        val.setType(si.getAnswerType());
        val.setRefId(si.getReferenceId());

        switch (si.getAnswerType()) {
            case Constants.SURVEY_ANSWER_TYPE_INTEGER:
                ati = answerTypeDao.getAnswerTypeInteger(si.getAnswerTypeId());
                val.setMaxInt(ati.getMaxValue());
                val.setMinInt(ati.getMinValue());
                val.setCriteria(ati.getCriteria());

                break;

            case Constants.SURVEY_ANSWER_TYPE_FLOAT:
                atf = answerTypeDao.getAnswerTypeFloat(si.getAnswerTypeId());
                val.setMaxFloat(atf.getMaxValue());
                val.setMinFloat(atf.getMinValue());
                val.setCriteria(atf.getCriteria());

                break;

            case Constants.SURVEY_ANSWER_TYPE_TEXT:
                att = answerTypeDao.getAnswerTypeText(si.getAnswerTypeId());
                val.setMaxInt(att.getMaxChars());
                val.setMinInt(att.getMinChars());
                val.setCriteria(att.getCriteria());

                break;

            default: // choice type
                atc = answerTypeDao.getAnswerTypeChoice(si.getAnswerTypeId());
                List<AtcChoice> choices = atcChoiceDao.getAtcChoicesByAnswerTypeChoiceId(atc.getId());
                for (AtcChoice choice : choices) {
                    IndicatorChoice ch = new IndicatorChoice();
                    ch.setCriteria(choice.getCriteria());
                    ch.setLabel(choice.getLabel());
                    ch.setScore((double) choice.getScore() / 10000.0);
                    ch.setUseScore(choice.getUseScore());

                    AtcChoiceIntl chIntl = atcIntlDao.findByChoiceIdAndLanguage(choice.getId(), langId);
                    if (chIntl != null) {
                        ch.setCriteria(chIntl.getCriteria());
                        ch.setLabel(chIntl.getLabel());
                    }

                    val.addChoice(ch);
                }
                break;

        }

        // get all tags
        List<Itags> itags = tagDao.getIndicatorTagsByIndicatorId(id);
        logger.debug("Number of tags of indicator " + id + ": " + ((itags == null) ? 0 : itags.size()));
        
        if (itags != null && !itags.isEmpty()) {
            for (Itags itag : itags) {
                val.addTagId(itag.getId());
                logger.debug("ITAGS ID: " + itag.getId());
            }
        }

        return true;
    }

    private void createCSV() throws IOException {
        // create CREATOR ORG
        String[] line = new String[2];
        line[0] = IndicatorCsvDefs.COL_LABEL_CREATOR_ORG;
        line[1] = orgMap.get(inds.getDefaultCreatorOrgId());
        writer.writeNext(line);

        line[0] = IndicatorCsvDefs.COL_LABEL_VISIBILITY;
        line[1] = visMap.get(inds.getDefaultVisibility());
        writer.writeNext(line);

        line[0] = IndicatorCsvDefs.COL_LABEL_STATE;
        line[1] = stateMap.get(inds.getDefaultState());
        writer.writeNext(line);

        line[0] = IndicatorCsvDefs.COL_LABEL_LANGUAGE;
        line[1] = langMap.get(inds.getDefaultLangId());
        writer.writeNext(line);

        //Insert blank line
        String[] blankline = new String[1];
        blankline[0] = "";
        writer.writeNext(blankline);

        String[] indline = new String[15];
        indline[IndicatorCsvDefs.COL_POS_NAME] = IndicatorCsvDefs.COL_LABEL_NAME;
        indline[IndicatorCsvDefs.COL_POS_QUESTION] = IndicatorCsvDefs.COL_LABEL_QUESTION;
        indline[IndicatorCsvDefs.COL_POS_TYPE] = IndicatorCsvDefs.COL_LABEL_TYPE;
        indline[IndicatorCsvDefs.COL_POS_REFERENCE] = IndicatorCsvDefs.COL_LABEL_REFERENCE;
        indline[IndicatorCsvDefs.COL_POS_HINT] = IndicatorCsvDefs.COL_LABEL_HINT;
        indline[IndicatorCsvDefs.COL_POS_MIN] = IndicatorCsvDefs.COL_LABEL_MIN;
        indline[IndicatorCsvDefs.COL_POS_MAX] = IndicatorCsvDefs.COL_LABEL_MAX;
        indline[IndicatorCsvDefs.COL_POS_OPTION] = IndicatorCsvDefs.COL_LABEL_OPTION;
        indline[IndicatorCsvDefs.COL_POS_SCORE] = IndicatorCsvDefs.COL_LABEL_SCORE;
        indline[IndicatorCsvDefs.COL_POS_CRITERIA] = IndicatorCsvDefs.COL_LABEL_CRITERIA;
        indline[IndicatorCsvDefs.COL_POS_TAGS] = IndicatorCsvDefs.COL_LABEL_TAGS;
        indline[IndicatorCsvDefs.COL_POS_CREATOR_ORG] = IndicatorCsvDefs.COL_LABEL_CREATOR_HEADER;
        indline[IndicatorCsvDefs.COL_POS_VISIBILITY] = IndicatorCsvDefs.COL_LABEL_VISIBILITY_HEADER;
        indline[IndicatorCsvDefs.COL_POS_MATURITY_STATE] = IndicatorCsvDefs.COL_LABEL_STATE;
        indline[IndicatorCsvDefs.COL_POS_LANGUAGE] = IndicatorCsvDefs.COL_LABEL_LANGUAGE_HEADER;
        writer.writeNext(indline);

        for (IndicatorValidation val : inds.getIndicatorValidations()) {
            writeFullRow(indline, val);
            writeSecondaryRow(indline, val);
        }

    }
   

    private void clear(String[] line) {
        for (int i = 0; i < line.length; i++) {
            line[i] = "";
        }
    }

    private void writeFullRow(String[] indline, IndicatorValidation val) throws IOException {
        clear(indline);

        indline[IndicatorCsvDefs.COL_POS_NAME] = val.getName();
        indline[IndicatorCsvDefs.COL_POS_QUESTION] = val.getQuestion();
        indline[IndicatorCsvDefs.COL_POS_TYPE] = typeMap.get(val.getType());
        indline[IndicatorCsvDefs.COL_POS_REFERENCE] = refMap.get(val.getRefId());
        indline[IndicatorCsvDefs.COL_POS_HINT] = val.getTip();
        indline[IndicatorCsvDefs.COL_POS_CRITERIA] = val.getCriteria();

        StringBuilder tags = new StringBuilder();
        String tag;
        if (val.getTagIDs() != null) {
            boolean first = true;
            for (Integer tagid : val.getTagIDs()) {
                tag = tagMap.get(tagid);

                if (!first) tags.append(", ");
                tags.append(tag);
                first = false;
            }
        }
        indline[IndicatorCsvDefs.COL_POS_TAGS] = tags.toString();
        logger.debug("TAGS: " + tags);
        
        indline[IndicatorCsvDefs.COL_POS_CREATOR_ORG] = orgMap.get(val.getCreatorOrgId());
        indline[IndicatorCsvDefs.COL_POS_VISIBILITY] = visMap.get(val.getVisibility());
        indline[IndicatorCsvDefs.COL_POS_MATURITY_STATE] = stateMap.get(val.getState());
        indline[IndicatorCsvDefs.COL_POS_LANGUAGE] = langMap.get(val.getLangId());

        switch (val.getType()) {
            case Constants.SURVEY_ANSWER_TYPE_INTEGER:
            case Constants.SURVEY_ANSWER_TYPE_TEXT:
                indline[IndicatorCsvDefs.COL_POS_MIN] = Integer.toString(val.getMinInt());
                indline[IndicatorCsvDefs.COL_POS_MAX] = Integer.toString(val.getMaxInt());
                break;
            case Constants.SURVEY_ANSWER_TYPE_FLOAT:
                indline[IndicatorCsvDefs.COL_POS_MIN] = Double.toString(val.getMinFloat());
                indline[IndicatorCsvDefs.COL_POS_MAX] = Double.toString(val.getMaxFloat());
                break;
        }

        writer.writeNext(indline);
    }

    private void writeSecondaryRow(String[] indline, IndicatorValidation val) throws IOException {
        switch (val.getType()) {
            case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
            case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
                break;
            default:
                return;
        }

        clear(indline);

        for (IndicatorChoice choice : val.getChoices()) {
            indline[IndicatorCsvDefs.COL_POS_OPTION] = choice.getLabel();
            if (choice.getUseScore()) {
                indline[IndicatorCsvDefs.COL_POS_SCORE] = Double.toString(choice.getScore());
            } 
            indline[IndicatorCsvDefs.COL_POS_CRITERIA] = choice.getCriteria();
            writer.writeNext(indline);
        }
    }

    
}
