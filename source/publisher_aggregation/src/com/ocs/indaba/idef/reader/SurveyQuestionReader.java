/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.reader;

import com.ocs.indaba.idef.common.MappingRule;
import com.ocs.indaba.idef.xo.Indicator;
import com.ocs.indaba.idef.imp.ProcessContext;
import com.ocs.indaba.idef.xo.SurveyCategory;
import com.ocs.indaba.idef.xo.SurveyConfig;
import com.ocs.indaba.idef.xo.SurveyQuestion;
import com.ocs.util.StringUtils;
import java.io.File;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class SurveyQuestionReader extends Reader {

    static private final int COL_ID = 0;
    static private final int COL_CONFIG_ID = 1;
    static private final int COL_LABEL = 2;
    static private final int COL_INDICATOR_ID = 3;
    static private final int COL_PARENT_ID = 4;
    static private final int COL_WEIGHT = 5;

    // required colums are in upper case; optional are in lower case
    static private final String[] COLUMNS = {
        "ID",
        "SURVEY CONFIG ID",
        "LABEL",
        "INDICATOR ID",
        "parent category id",
        "WEIGHT"
    };

    public SurveyQuestionReader(ProcessContext ctx, File file) {
        super.createReader(ctx, file, COLUMNS);
    }

    public void read() {
        String[] line;
        int qstCount = 0;

        // process questions
        while ((line = readNext()) != null) {
            // validate the fields
            if (!super.checkRequiredColumns(line, COLUMNS)) continue;

            String qstId = super.getColumn(line, COL_ID);
            String scId = super.getColumn(line, COL_CONFIG_ID);
            String parentId = super.getColumn(line, COL_PARENT_ID);
            String indId = super.getColumn(line, COL_INDICATOR_ID);
            String label = super.getColumn(line, COL_LABEL);
            String weight = super.getColumn(line, COL_WEIGHT);

             if (ctx.doMapping()) {
                String ruleSpec = ctx.getQuestionNameRule(label);
                if (ruleSpec == null) {
                    // No indicator specific rule defined - try to find general rule
                    ruleSpec = ctx.getQuestionNameRule("*");
                }

                if (ruleSpec != null) {
                    // apply rule
                    MappingRule rule = MappingRule.parseRule(ruleSpec);
                    if (rule == null) {
                        super.addError("bad question name rule: " + ruleSpec);
                    } else {
                        label = rule.evaluate(label);
                    }
                }
            }

            int numErrs = 0;

            if (ctx.getQuestion(qstId) != null) {
                super.addLineError("duplicate question: " + qstId);
                numErrs++;
            }

            SurveyConfig sc = ctx.getSurveyConfig(scId);
            if (sc == null) {
                super.addLineError("referenced survey config does not exist: " + scId);
                numErrs++;
            }

            SurveyCategory cat = null;
            if (!StringUtils.isEmpty(parentId)) {
                cat = ctx.getCategory(parentId);
                if (cat == null) {
                    super.addLineError("referenced parent category does not exist: " + parentId);
                    numErrs++;
                }
            }

            if (sc != null && cat != null) {
                // make sure they belong to the same SC
                if (cat.getSurveyConfig() != sc) {
                    super.addLineError("parent category " + cat.getId() + " and the question don't belong to the same survey config.");
                    numErrs++;
                }
            }

            Indicator indicator = null;
            indicator = ctx.getIndicatorById(indId);
            if (indicator == null) {
                super.addLineError("referenced indicator does not exist: " + indId);
                numErrs++;
            }

            int weightValue = StringUtils.str2int(weight);
            if (weightValue == Integer.MIN_VALUE) {
                super.addLineError("invalid weight value: " + weight);
                numErrs++;
            }


            if (numErrs > 0) continue;

            // create question
            SurveyQuestion qst = new SurveyQuestion();

            qst.setId(qstId);
            qst.setIndicator(indicator);
            qst.setLabel(label);
            qst.setParent(cat);
            qst.setSurveyConfig(sc);
            qst.setWeight(weightValue);
            sc.addQuestion(qst);

            // they must have the same visibility
            if (sc.getVisibility() != indicator.getVisibility()) {
                super.addLineError("Indicator's visibility is different that of the survey config " + sc.getName());
            }

            ctx.addQuestion(qstId, qst);
            qstCount++;
        }

        if (super.getErrorCount() == 0 && qstCount == 0) {
            super.addError("No questions specified.");
        }

        if (super.getErrorCount() == 0) {
            // check for empty categories that don't have any questions
            List<SurveyCategory> cats = ctx.getCategories();

            if (cats != null) {
                for (SurveyCategory cat : cats) {
                    if (cat.getChildren() == null && cat.getQuestions() == null) {
                        String catPath = categoryPath(cat);
                        super.addError("Empty categories: " + catPath);
                    }
                }
            }
        }
    }


    private String categoryPath(SurveyCategory cat) {
        if (cat.getParent() == null) {
            return cat.getId();
        } else {
            return categoryPath(cat.getParent()) + " => " + cat.getId();
        }
    }

}
