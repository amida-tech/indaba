/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.imp;

import com.ocs.indaba.idef.common.Constants;
import com.ocs.indaba.idef.reader.IndicatorReader;
import com.ocs.indaba.idef.reader.ProductReader;
import com.ocs.indaba.idef.reader.ProjectReader;
import com.ocs.indaba.idef.reader.ScorecardReader;
import com.ocs.indaba.idef.reader.SurveyAnswerReader;
import com.ocs.indaba.idef.reader.SurveyCategoryReader;
import com.ocs.indaba.idef.reader.SurveyConfigReader;
import com.ocs.indaba.idef.reader.SurveyQuestionReader;
import com.ocs.indaba.idef.reader.UserReader;
import com.ocs.indaba.idef.xo.Scorecard;
import com.ocs.indaba.idef.xo.SurveyAnswer;
import com.ocs.indaba.idef.xo.SurveyConfig;
import com.ocs.indaba.idef.xo.SurveyQuestion;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class ImportValidator {

    private String rootDir;
    private ProcessContext ctx;

    public ImportValidator(int importId, String rootDir, ProcessContext ctx) {
        this.rootDir = rootDir;
        ctx.setImportId(importId);
        this.ctx = ctx;
    }


    public void validate() {
        File dir = new File(rootDir);

        if (dir == null) {
            ctx.addError("Bad root directory: " + rootDir);
            return;
        }

        File dirList[] = dir.listFiles();

        if (dirList == null) {
            ctx.addError("Empty root directory: " + rootDir);
            return;
        }

        List<File> answerFiles = new ArrayList<File>();
        List<File> indicatorFiles = new ArrayList<File>();
        List<File> surveyConfigFiles = new ArrayList<File>();
        List<File> surveyQuestionFiles = new ArrayList<File>();
        List<File> surveyCategoryFiles = new ArrayList<File>();
        List<File> scorecardFiles = new ArrayList<File>();
        List<File> projectFiles = new ArrayList<File>();
        List<File> productFiles = new ArrayList<File>();
        List<File> userFiles = new ArrayList<File>();

        for (File file : dirList) {
            String fileName = file.getName();

            if (fileName.endsWith((Constants.FILE_PATTERN_ANSWER))) {
                answerFiles.add(file);
            } else if (fileName.endsWith((Constants.FILE_PATTERN_INDICATOR))) {
                indicatorFiles.add(file);
            } else if (fileName.endsWith((Constants.FILE_PATTERN_SURVEY_CONFIG))) {
                surveyConfigFiles.add(file);
            } else if (fileName.endsWith((Constants.FILE_PATTERN_SURVEY_QUESTION))) {
                surveyQuestionFiles.add(file);
            } else if (fileName.endsWith((Constants.FILE_PATTERN_SURVEY_CATEGORY))) {
                surveyCategoryFiles.add(file);
            } else if (fileName.endsWith((Constants.FILE_PATTERN_SCORECARD))) {
                scorecardFiles.add(file);
            } else if (fileName.endsWith((Constants.FILE_PATTERN_PRODUCT))) {
                productFiles.add(file);
            } else if (fileName.endsWith((Constants.FILE_PATTERN_PROJECT))) {
                projectFiles.add(file);
            } else if (fileName.endsWith((Constants.FILE_PATTERN_USER))) {
                userFiles.add(file);
            } 
        }

        // process user files
        if (userFiles.isEmpty()) {
            ctx.addError("Missing user files.");
        }

        for (File f : userFiles) {
            UserReader reader = new UserReader(ctx, f);
            reader.read();
        }

        // indicator files
        if (indicatorFiles.isEmpty()) {
            ctx.addError("Missing indicator files.");
        }
        for (File f : indicatorFiles) {
            IndicatorReader reader = new IndicatorReader(ctx, f);
            reader.read();
        }

        // process survey config files
        if (surveyConfigFiles.isEmpty()) {
            ctx.addError("Missing indicator files.");
        }
        for (File f : surveyConfigFiles) {
            SurveyConfigReader reader = new SurveyConfigReader(ctx, f);
            reader.read();
        }

        // survey_category
        for (File f : surveyCategoryFiles) {
            SurveyCategoryReader reader = new SurveyCategoryReader(ctx, f);
            reader.read();
        }

        // survey_question
        if (surveyQuestionFiles.isEmpty()) {
            ctx.addError("Missing survey question files.");
        }
        for (File f : surveyQuestionFiles) {
            SurveyQuestionReader reader = new SurveyQuestionReader(ctx, f);
            reader.read();
        }

        // process project files
        if (projectFiles.isEmpty()) {
            ctx.addError("Missing project files.");
        }
        for (File f : projectFiles) {
            ProjectReader reader = new ProjectReader(ctx, f);
            reader.read();
        }

        // product files
        if (productFiles.isEmpty()) {
            ctx.addError("Missing product files.");
        }
        for (File f : productFiles) {
            ProductReader reader = new ProductReader(ctx, f);
            reader.read();
        }

        // scorecard files
        for (File f : scorecardFiles) {
            ScorecardReader reader = new ScorecardReader(ctx, f);
            reader.read();
        }

        // answer files
        for (File f : answerFiles) {
            SurveyAnswerReader reader = new SurveyAnswerReader(ctx, f);
            reader.read();
        }

        if (ctx.getErrors().size() > 0) return;

        // Further validation
        // make sure each scorecard is complete - all questions are answered
        List<Scorecard> scorecards = ctx.getScorecards();
        
        for (Scorecard sc : scorecards) {
            checkAnswerCompleteness(sc);
        }
        
    }


    private void checkAnswerCompleteness(Scorecard sc) {
        if (sc.getProduct() == null) return;
        SurveyConfig config = sc.getProduct().getSurveyConfig();
        if (config == null) return;

        List<SurveyQuestion> questions = config.getQuestions();
        int numQuestions = questions.size();
        List<SurveyAnswer> answers = sc.getSurveyAnswers();
        int numAnswers = answers.size();

        if (numAnswers < numQuestions) {
            ctx.addError("Scorecard [" + sc.getTarget().getName() + " - " + sc.getProduct().getName() + "] missing answers: "
                    + numQuestions + " questions but only " + numAnswers + " answers provided.");

            // try to find questions that miss answers
            // set sc to every question answered
            /*
            for (SurveyAnswer a : answers) {
                SurveyQuestion qst = a.getQuestion();
                qst.setScorecard(sc);
            }

            for (SurveyQuestion q : questions) {
                if (q.getScorecard() != sc) {
                    // this question is not answered by this scorecard!
                    ctx.addError("  Question Missed: " + q.getId());
                }
            }
             *
             */
        }
    }

    public ProcessContext getProcessContext() {
        return ctx;
    }

}
