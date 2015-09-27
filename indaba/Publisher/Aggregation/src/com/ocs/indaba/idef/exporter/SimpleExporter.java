/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.exporter;

import com.ocs.indaba.aggregation.dao.IdefDAO;
import com.ocs.indaba.aggregation.service.ScorecardService;
import com.ocs.indaba.aggregation.vo.HorseVO;
import com.ocs.indaba.idef.xo.DenormalizedSurveyAnswer;
import com.ocs.indaba.idef.xo.IndicatorChoices;
import com.ocs.indaba.po.ReferenceChoice;
import com.ocs.indaba.po.Target;
import com.ocs.ssu.SpreadsheetWriter;
import com.ocs.ssu.UnsupportedFileTypeException;
import com.ocs.ssu.WriterFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author yc06x
 */
public class SimpleExporter extends BaseExporter {

    private static final int FIELD_POS_PROJ_ID = 0;
    private static final int FIELD_POS_PROJ_NAME = 1;
    private static final int FIELD_POS_PROD_ID = 2;
    private static final int FIELD_POS_PROD_NAME = 3;
    private static final int FIELD_POS_QUESTION_ID = 4;
    private static final int FIELD_POS_QUESTION_TEXT = 5;
    private static final int FIELD_POS_QUESTION_WEIGHT = 6;
    private static final int FIELD_POS_QUESTION_TYPE = 7;
    private static final int FIELD_POS_CATEGORY_ID = 8;
    private static final int FIELD_POS_CATEGORY_NAME = 9;
    private static final int FIELD_POS_CATEGORY_WEIGHT = 10;
    private static final int FIELD_POS_TARGET_ID = 11;
    private static final int FIELD_POS_TARGET_NAME = 12;
    private static final int FIELD_POS_TARGET_DESC = 13;
    private static final int FIELD_POS_ANSWER_ID = 14;
    private static final int FIELD_POS_ANSWER_USER_ID = 15;
    private static final int FIELD_POS_ANSWER_USER_FIRST_NAME = 16;
    private static final int FIELD_POS_ANSWER_USER_LAST_NAME = 17;
    private static final int FIELD_POS_ANSWER_TIME = 18;
    private static final int FIELD_POS_ANSWER_VALUE = 19;
    private static final int FIELD_POS_ANSWER_SCORE = 20;
    private static final int FIELD_POS_ANSWER_COMMENTS = 21;
    private static final int FIELD_POS_ANSWER_SOURCES = 22;
    private static final int FIELD_POS_ANSWER_SOURCE_DESC = 23;
    private static final int FIELD_POS_REVIEW_USER_ID = 24;
    private static final int FIELD_POS_REVIEW_USER_FIRST_NAME = 25;
    private static final int FIELD_POS_REVIEW_USER_LAST_NAME = 26;
    private static final int FIELD_POS_REVIEW_OPINION = 27;
    private static final int FIELD_POS_REVIEW_ANSWER_VALUE = 28;
    private static final int FIELD_POS_REVIEW_COMMENTS = 29;
    private static final int FIELD_COUNT = 30;

    private static final String FIELD_NAME_PROJ_ID = "projectId";
    private static final String FIELD_NAME_PROJ_NAME = "projectName";
    private static final String FIELD_NAME_PROD_ID = "productId";
    private static final String FIELD_NAME_PROD_NAME = "productName";
    private static final String FIELD_NAME_QUESTION_ID = "questionId";
    private static final String FIELD_NAME_QUESTION_TEXT = "questionText";
    private static final String FIELD_NAME_QUESTION_WEIGHT = "questionWeight";
    private static final String FIELD_NAME_QUESTION_TYPE = "questionType";
    private static final String FIELD_NAME_CATEGORY_ID = "categoryId";
    private static final String FIELD_NAME_CATEGORY_NAME = "categoryName";
    private static final String FIELD_NAME_CATEGORY_WEIGHT = "categoryWeight";
    private static final String FIELD_NAME_TARGET_ID = "targetId";
    private static final String FIELD_NAME_TARGET_NAME = "targetName";
    private static final String FIELD_NAME_TARGET_DESC = "targetDescription";
    private static final String FIELD_NAME_ANSWER_ID = "answerId";
    private static final String FIELD_NAME_ANSWER_USER_ID = "answerUserId";
    private static final String FIELD_NAME_ANSWER_USER_FIRST_NAME = "answerUserFirstName";
    private static final String FIELD_NAME_ANSWER_USER_LAST_NAME = "answerUserLastName";
    private static final String FIELD_NAME_ANSWER_TIME = "answerTime";
    private static final String FIELD_NAME_ANSWER_VALUE = "answerValue";
    private static final String FIELD_NAME_ANSWER_SCORE = "answerScore";
    private static final String FIELD_NAME_ANSWER_COMMENTS = "answerComments";
    private static final String FIELD_NAME_ANSWER_SOURCES = "answerSources";
    private static final String FIELD_NAME_ANSWER_SOURCE_DESC = "answerSourceDescription";
    private static final String FIELD_NAME_REVIEW_USER_ID = "reviewUserId";
    private static final String FIELD_NAME_REVIEW_USER_FIRST_NAME = "reviewUserFirstName";
    private static final String FIELD_NAME_REVIEW_USER_LAST_NAME = "reviewUserLastName";
    private static final String FIELD_NAME_REVIEW_OPINION = "reviewOpinion";
    private static final String FIELD_NAME_REVIEW_ANSWER_VALUE = "reviewAnswerValue";
    private static final String FIELD_NAME_REVIEW_COMMENTS = "reviewComments";

    public SimpleExporter(IdefDAO dao, ScorecardService srvc) {
        this.idefDao = dao;
        this.scorecardService = srvc;
    }
    
    static private class ChoiceAnswer {
        Float score;
        String text;
    }

    private ChoiceAnswer getChoiceAnswer(int indicatorId, Integer choices, Map<Integer, List<IndicatorChoices>> indicatorChoiceMap) {
        ChoiceAnswer result = new ChoiceAnswer();

        result.score = null;
        result.text = "";

        if (choices == null) return result;

        List<IndicatorChoices> icList = indicatorChoiceMap.get(indicatorId);

        if (icList == null) return result;

        StringBuilder sb = new StringBuilder("");
        boolean isFirst = true;
        for (IndicatorChoices ic : icList) {
            if (ic.getMask() != null && ((ic.getMask() & choices) != 0)) {
                if (!isFirst) sb.append(" | ");
                else result.score = ic.getScore();

                sb.append(ic.getLabel());
                isFirst = false;
            }
        }
        result.text = sb.toString();
        return result;
    }


    public void export(int productId, String outputPath) {
        try {
            doExport(productId, outputPath);
        } catch (Exception ex) {
            setError("Exception: " + ex);
        }
    }


    private void exportHorse(int productId, HorseVO horse, SpreadsheetWriter writer, Map<Integer, List<IndicatorChoices>> indicatorChoiceMap)
            throws FileNotFoundException, UnsupportedFileTypeException, UnsupportedEncodingException, IOException {

        List<DenormalizedSurveyAnswer> answers = idefDao.getAnswersByProduct(productId, horse.getTargetId());

        if (answers == null || answers.isEmpty()) {
            this.addMsg("Nothing to export for target " + horse.getTargetId() + " of product " + productId);
            return;
        }

        String[] row = new String[FIELD_COUNT];
        for (DenormalizedSurveyAnswer dsa : answers) {
            row[FIELD_POS_PROJ_ID] = "" + dsa.getProjectId();
            row[FIELD_POS_PROJ_NAME] = dsa.getProjectName();
            row[FIELD_POS_PROD_ID] = "" + dsa.getProductId();
            row[FIELD_POS_PROD_NAME] = dsa.getProductName();
            row[FIELD_POS_QUESTION_ID] = "" + dsa.getQuestionId();
            row[FIELD_POS_QUESTION_TEXT] = dsa.getQuestionLabel() + ": " + dsa.getIndicatorQuestion();
            row[FIELD_POS_QUESTION_WEIGHT] = "" + dsa.getQuestionWeight();
            row[FIELD_POS_QUESTION_TYPE] = "" + dsa.getIndicatorDataType();
            row[FIELD_POS_CATEGORY_ID] = "" + dsa.getCategoryId();
            row[FIELD_POS_CATEGORY_NAME] = dsa.getCategoryTitle();
            row[FIELD_POS_CATEGORY_WEIGHT] = "" + dsa.getCategoryWeight();
            row[FIELD_POS_TARGET_ID] = "" + dsa.getTargetId();
            row[FIELD_POS_TARGET_NAME] = dsa.getTargetName();
            row[FIELD_POS_TARGET_DESC] = dsa.getTargetDescription();
            row[FIELD_POS_ANSWER_ID] = "" + dsa.getAnswerId();
            row[FIELD_POS_ANSWER_USER_ID] = "" + dsa.getAnswerUserId();
            row[FIELD_POS_ANSWER_USER_FIRST_NAME] = dsa.getAnswerUserFirstName();
            row[FIELD_POS_ANSWER_USER_LAST_NAME] = dsa.getAnswerUserLastName();
            row[FIELD_POS_ANSWER_TIME] = "" + dsa.getAnswerTime();
            row[FIELD_POS_ANSWER_COMMENTS] = dsa.getAnswerComments();
            row[FIELD_POS_ANSWER_SOURCE_DESC] = dsa.getRefSourceDescription();
            row[FIELD_POS_REVIEW_USER_ID] = "" + dsa.getReviewUserId();
            row[FIELD_POS_REVIEW_USER_FIRST_NAME] = dsa.getReviewUserFirstName();
            row[FIELD_POS_REVIEW_USER_LAST_NAME] = dsa.getReviewUserLastName();
            row[FIELD_POS_REVIEW_OPINION] = "" + dsa.getReviewOpinion();
            row[FIELD_POS_REVIEW_COMMENTS] = dsa.getReviewComments();
            row[FIELD_POS_ANSWER_SOURCES] = "";

            switch (dsa.getIndicatorDataType()) {
                case 1:
                    // single-choice type
                    ChoiceAnswer ca = getChoiceAnswer(dsa.getIndicatorId(), dsa.getAnswerValueChoices(), indicatorChoiceMap);
                    row[FIELD_POS_ANSWER_VALUE] = ca.text;
                    row[FIELD_POS_ANSWER_SCORE] = "" + ca.score;

                    ca = getChoiceAnswer(dsa.getIndicatorId(), dsa.getReviewAnswerValueChoices(), indicatorChoiceMap);
                    row[FIELD_POS_REVIEW_ANSWER_VALUE] = ca.text;
                    break;

                case 2:
                    ca = getChoiceAnswer(dsa.getIndicatorId(), dsa.getAnswerValueChoices(), indicatorChoiceMap);
                    row[FIELD_POS_ANSWER_VALUE] = ca.text;
                    row[FIELD_POS_ANSWER_SCORE] = NULL;

                    ca = getChoiceAnswer(dsa.getIndicatorId(), dsa.getReviewAnswerValueChoices(), indicatorChoiceMap);
                    row[FIELD_POS_REVIEW_ANSWER_VALUE] = ca.text;
                    break;

                case 3:
                    // int
                    row[FIELD_POS_ANSWER_VALUE] = "" + dsa.getAnswerValueInt();
                    row[FIELD_POS_ANSWER_SCORE] = NULL;
                    row[FIELD_POS_REVIEW_ANSWER_VALUE] = "" + dsa.getReviewAnswerValueInt();
                    break;

                case 4:
                    // float
                    row[FIELD_POS_ANSWER_VALUE] = "" + dsa.getAnswerValueFloat();
                    row[FIELD_POS_ANSWER_SCORE] = NULL;
                    row[FIELD_POS_REVIEW_ANSWER_VALUE] = "" + dsa.getReviewAnswerValueFloat();
                    break;

                case 5:
                    // text
                    row[FIELD_POS_ANSWER_VALUE] = dsa.getAnswerValueText();
                    row[FIELD_POS_ANSWER_SCORE] = NULL;
                    row[FIELD_POS_REVIEW_ANSWER_VALUE] = dsa.getReviewAnswerValueText();
                    break;

                default:
                    row[FIELD_POS_ANSWER_VALUE] = "";
                    row[FIELD_POS_ANSWER_SCORE] = NULL;
                    row[FIELD_POS_REVIEW_ANSWER_VALUE] = "";
            }

            writer.writeNext(row);
            writer.flush();
        }
    }
    

    private void doExport(int productId, String outputPath) throws FileNotFoundException, UnsupportedFileTypeException, UnsupportedEncodingException, IOException {
        // get all targets of the product
        List<HorseVO> horses = scorecardService.getHorsesByProductId(productId);
        if (horses == null || horses.isEmpty()) {
            this.addMsg("No exportable horses for product " + productId);
            return;
        }

        // get all indicator choices
        List<IndicatorChoices> indicatorChoices = idefDao.getIndicatorChoicesByProduct(productId);
        Map<Integer, List<IndicatorChoices>> indicatorChoiceMap = new HashMap<Integer, List<IndicatorChoices>>();

        if (indicatorChoices != null && !indicatorChoices.isEmpty()) {
            for (IndicatorChoices ic : indicatorChoices) {
                List<IndicatorChoices> choiceList = indicatorChoiceMap.get(ic.getIndicatorId());
                if (choiceList == null) {
                    choiceList = new ArrayList<IndicatorChoices>();
                    indicatorChoiceMap.put(ic.getIndicatorId(), choiceList);
                }
                choiceList.add(ic);
            }
        }

        // get all ref choices
        List<ReferenceChoice> refChoices = idefDao.getReferenceChoicesByProduct(productId);
        Map<Integer, List<ReferenceChoice>> refChoiceMap = new HashMap<Integer, List<ReferenceChoice>>();

        if (refChoices != null) {
            for (ReferenceChoice rc : refChoices) {
                List<ReferenceChoice> choiceList = refChoiceMap.get(rc.getReferenceId());
                if (choiceList == null) {
                    choiceList = new ArrayList<ReferenceChoice>();
                    refChoiceMap.put(rc.getReferenceId(), choiceList);
                }
                choiceList.add(rc);
            }
        }

        String outputFileName = outputPath + "/export.dnorm.prod." + productId + ".csv";
        this.setExportFileName(outputFileName);

        File outputFile = new File(outputPath, "export.dnorm.prod." + productId + ".csv");
	FileOutputStream fout = new FileOutputStream(outputFile);
        SpreadsheetWriter writer = WriterFactory.createWriter(fout, WriterFactory.FILE_TYPE_CSV);

        // write the header line
        String[] header = new String[FIELD_COUNT];
        header[FIELD_POS_PROJ_ID] = FIELD_NAME_PROJ_ID;
        header[FIELD_POS_PROJ_NAME] = FIELD_NAME_PROJ_NAME;
        header[FIELD_POS_PROD_ID] = FIELD_NAME_PROD_ID;
        header[FIELD_POS_PROD_NAME] = FIELD_NAME_PROD_NAME;
        header[FIELD_POS_QUESTION_ID] = FIELD_NAME_QUESTION_ID;
        header[FIELD_POS_QUESTION_TEXT] = FIELD_NAME_QUESTION_TEXT;
        header[FIELD_POS_QUESTION_WEIGHT] = FIELD_NAME_QUESTION_WEIGHT;
        header[FIELD_POS_QUESTION_TYPE] = FIELD_NAME_QUESTION_TYPE;
        header[FIELD_POS_CATEGORY_ID] = FIELD_NAME_CATEGORY_ID;
        header[FIELD_POS_CATEGORY_NAME] = FIELD_NAME_CATEGORY_NAME;
        header[FIELD_POS_CATEGORY_WEIGHT] = FIELD_NAME_CATEGORY_WEIGHT;
        header[FIELD_POS_TARGET_ID] = FIELD_NAME_TARGET_ID;
        header[FIELD_POS_TARGET_NAME] = FIELD_NAME_TARGET_NAME;
        header[FIELD_POS_TARGET_DESC] = FIELD_NAME_TARGET_DESC;
        header[FIELD_POS_ANSWER_ID] = FIELD_NAME_ANSWER_ID;
        header[FIELD_POS_ANSWER_USER_ID] = FIELD_NAME_ANSWER_USER_ID;
        header[FIELD_POS_ANSWER_USER_FIRST_NAME] = FIELD_NAME_ANSWER_USER_FIRST_NAME;
        header[FIELD_POS_ANSWER_USER_LAST_NAME] = FIELD_NAME_ANSWER_USER_LAST_NAME;
        header[FIELD_POS_ANSWER_TIME] = FIELD_NAME_ANSWER_TIME;
        header[FIELD_POS_ANSWER_VALUE] = FIELD_NAME_ANSWER_VALUE;
        header[FIELD_POS_ANSWER_SCORE] = FIELD_NAME_ANSWER_SCORE;
        header[FIELD_POS_ANSWER_COMMENTS] = FIELD_NAME_ANSWER_COMMENTS;
        header[FIELD_POS_ANSWER_SOURCES] = FIELD_NAME_ANSWER_SOURCES;
        header[FIELD_POS_ANSWER_SOURCE_DESC] = FIELD_NAME_ANSWER_SOURCE_DESC;
        header[FIELD_POS_REVIEW_USER_ID] = FIELD_NAME_REVIEW_USER_ID;
        header[FIELD_POS_REVIEW_USER_FIRST_NAME] = FIELD_NAME_REVIEW_USER_FIRST_NAME;
        header[FIELD_POS_REVIEW_USER_LAST_NAME] = FIELD_NAME_REVIEW_USER_LAST_NAME;
        header[FIELD_POS_REVIEW_OPINION] = FIELD_NAME_REVIEW_OPINION;
        header[FIELD_POS_REVIEW_ANSWER_VALUE] = FIELD_NAME_REVIEW_ANSWER_VALUE;
        header[FIELD_POS_REVIEW_COMMENTS] = FIELD_NAME_REVIEW_COMMENTS;

        writer.writeNext(header);
        writer.flush();

        for (HorseVO horse : horses) {
            exportHorse(productId, horse, writer, indicatorChoiceMap);
        }

        writer.close();
    }


}
