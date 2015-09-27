/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.reader;

import com.ocs.indaba.idef.common.Constants;
import com.ocs.indaba.idef.common.MappingRule;
import com.ocs.indaba.idef.xo.AnswerChoice;
import com.ocs.indaba.idef.xo.Indicator;
import com.ocs.indaba.idef.imp.ProcessContext;
import com.ocs.indaba.idef.xo.Ref;
import com.ocs.indaba.po.Language;
import com.ocs.indaba.po.Organization;
import com.ocs.util.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class IndicatorReader extends Reader {

    static private final int COL_ID = 0;
    static private final int COL_NAME = 1;
    static private final int COL_QUESTION = 2;
    static private final int COL_TYPE = 3;
    static private final int COL_REFERENCE = 4;
    static private final int COL_HINT = 5;
    static private final int COL_MIN = 6;
    static private final int COL_MAX = 7;
    static private final int COL_CRITERIA = 8;
    static private final int COL_OPTION = 9;
    static private final int COL_SCORE = 10;
    static private final int COL_LANGUAGE = 11;
    static private final int COL_ORG = 12;
    static private final int COL_VISIBILITY = 13;


    // required colums are in upper case; optional are in lower case
    static private final String[] COLUMNS = {
        "ID",
        "NAME",
        "QUESTION",
        "TYPE",
        "REFERENCE",
        "hint",
        "min",
        "max",
        "criteria",
        "option",
        "score",
        "LANGUAGE",
        "ORGANIZATION",
        "VISIBILITY"
    };

    
    public IndicatorReader(ProcessContext ctx, File file) {
        super.createReader(ctx, file, COLUMNS);
    }


    private AnswerChoice parseAnswerChoice(String[] line) {
        String opt = super.getColumn(line, COL_OPTION);
        String criteria = super.getColumn(line, COL_CRITERIA);
        String score = super.getColumn(line, COL_SCORE);
        boolean hasScore = false;

        if (StringUtils.isEmpty(opt)) {
            super.addLineError("missing option label.");
            return null;
        }

        double scoreValue = 0.0;
        if (!StringUtils.isEmpty(score)) {
            scoreValue = StringUtils.str2double(score, Constants.DOUBLE_ERROR);
            if (scoreValue == Constants.DOUBLE_ERROR) {
                super.addLineError("invalid score value: " + score);
                return null;
            }
            hasScore = true;
        }

        AnswerChoice ac = new AnswerChoice();
        ac.setCriteria(criteria);
        ac.setOption(opt);

        if (hasScore) ac.setScore(scoreValue);

        return ac;
    }


    public void read() {
        String[] line;
        int numIndicators = 0;

        // process projects
        while ((line = readNext()) != null) {
            // validate the fields
            if (!super.checkRequiredColumns(line, COLUMNS)) continue;

            String indId = super.getColumn(line, COL_ID);
            String name = super.getColumn(line, COL_NAME);
            String qst = super.getColumn(line, COL_QUESTION);
            String type = super.getColumn(line, COL_TYPE);
            String refName = super.getColumn(line, COL_REFERENCE);
            String hint = super.getColumn(line, COL_HINT);
            String min = super.getColumn(line, COL_MIN);
            String max = super.getColumn(line, COL_MAX);
            String criteria = super.getColumn(line, COL_CRITERIA);
            String lang = super.getColumn(line, COL_LANGUAGE);
            String orgNames = super.getColumn(line, COL_ORG);
            String vis = super.getColumn(line, COL_VISIBILITY);

            if (ctx.doMapping()) {
                String newRefName = ctx.getIndicatorRefMapping(name);
                if (newRefName != null) refName = newRefName;

                String ruleSpec = ctx.getIndicatorNameRule(name);
                if (ruleSpec == null) {
                    // No indicator specific rule defined - try to find general rule
                    ruleSpec = ctx.getIndicatorNameRule("*");
                }

                if (ruleSpec != null) {
                    // apply rule
                    MappingRule rule = MappingRule.parseRule(ruleSpec);
                    if (rule == null) {
                        super.addError("bad indicator name rule: " + ruleSpec);
                    } else {
                        name = rule.evaluate(name);
                    }
                }
            }

            String visOv = ctx.getMetaProperty(Constants.META_PROP_NAME_VISIBILITY_OVERRIDE);
            if (visOv != null) vis = visOv;

            String langOv = ctx.getMetaProperty(Constants.META_PROP_NAME_LANGUAGE_OVERRIDE);
            if (langOv != null) lang = langOv;

            String orgOv = ctx.getMetaProperty(Constants.META_PROP_NAME_ORGANIZATION_OVERRIDE);
            if (orgOv != null) orgNames = orgOv;

            int numErrs = 0;

            if (ctx.getIndicatorById(indId) != null) {
                super.addLineError("duplicate indicator ID: " + indId);
                numErrs++;
            }

            if (ctx.getIndicatorByName(name) != null) {
                super.addLineError("duplicate indicator name: " + name);
                numErrs++;
            }

            short ansType;
            if ((ansType = super.parseAnswerType(type)) < 0) {
                super.addLineError("invalid indicator type: " + type);
                numErrs++;
            }

            short visibility;
            if ((visibility = super.parseVisibility(vis)) < 0) {
                super.addLineError("invalid visibility: " + vis);
                numErrs++;
            }

            Language language = ctx.getLanguage(lang);
            if (language == null) {
                super.addLineError("nonexistent language: " + lang);
                numErrs++;
            }

            Ref ref = ctx.getRefByName(refName);
            if (ref == null) {
                super.addLineError("nonexistent reference: " + refName);
                numErrs++;
            }

            List<AnswerChoice> acList = null;
            int mini = 0;
            int maxi = 0;
            double minf = 0;
            double maxf = 0;

            switch(ansType) {
                case Constants.INDICATOR_TYPE_MULTI_CHOICE:
                case Constants.INDICATOR_TYPE_SINGLE_CHOICE:
                    acList = new ArrayList<AnswerChoice>();
                    AnswerChoice ac = null;

                    String opt = super.getColumn(line, COL_OPTION);
                    if (!StringUtils.isEmpty(opt)) {
                        ac = parseAnswerChoice(line);
                        if (ac != null) acList.add(ac);
                        else numErrs++;
                    }

                    while ((line = readNext()) != null) {
                        String idStr = super.getColumn(line, COL_ID);
                        if (StringUtils.isEmpty(idStr)) {
                            ac = parseAnswerChoice(line);
                            if (ac != null) acList.add(ac);
                            else numErrs++;
                        } else {
                            super.returnLine(line);
                            break;
                        }
                    }
                    if (acList.isEmpty()) {
                        super.addLineError("missing choices.");
                        numErrs++;
                    }
                    break;

                case Constants.INDICATOR_TYPE_INTEGER:
                case Constants.INDICATOR_TYPE_TEXT:
                    if (StringUtils.isEmpty(min)) {
                        super.addLineError("missing Min value.");
                        numErrs++;
                    } else {
                        mini = StringUtils.str2int(min);
                        if (mini == Integer.MIN_VALUE) {
                            super.addLineError("invalid Min value: " + min);
                            numErrs++;
                        }
                    }

                    if (StringUtils.isEmpty(max)) {
                        super.addLineError("missing Max value.");
                        numErrs++;
                    } else {
                        maxi = StringUtils.str2int(max);
                        if (maxi == Integer.MIN_VALUE) {
                            super.addLineError("invalid Max value: " + max);
                            numErrs++;
                        }
                    }
                    break;

                default:
                    // float type
                    if (StringUtils.isEmpty(min)) {
                        super.addLineError("missing Min value.");
                        numErrs++;
                    } else {
                        minf = StringUtils.str2double(min, Constants.DOUBLE_ERROR);
                        if (minf == Constants.DOUBLE_ERROR) {
                            super.addLineError("invalid Min value: " + min);
                            numErrs++;
                        }
                    }

                    if (StringUtils.isEmpty(max)) {
                        super.addLineError("missing Max value.");
                        numErrs++;
                    } else {
                        maxf = StringUtils.str2double(max, Constants.DOUBLE_ERROR);
                        if (maxf == Constants.DOUBLE_ERROR) {
                            super.addLineError("invalid Max value: " + max);
                            numErrs++;
                        }
                    }
                    break;
            }

            
            if (numErrs > 0) continue;

            // create indicator
            Indicator indicator = new Indicator();
            indicator.setCriteria(criteria);
            indicator.setHint(hint);
            indicator.setId(indId);
            indicator.setLanguage(language);
            indicator.setMaxDouble(maxf);
            indicator.setMaxInt(maxi);
            indicator.setMinDouble(minf);
            indicator.setMinInt(mini);
            indicator.setName(name);
            indicator.setQuestion(qst);
            indicator.setType(ansType);
            indicator.setVisibility(visibility);
            indicator.setChoices(acList);
            indicator.setRef(ref);

            // compute weight and mask for choices
            if (acList != null && !acList.isEmpty()) {
                int pos = 0;
                for (AnswerChoice ac : acList) {
                    ac.setWeight(pos+1);
                    ac.setMask(1 << pos);
                    pos++;
                }
            }
           
            String[] orgNameList = super.parseOrgNames(orgNames);
            for (String orgName : orgNameList) {
                if (!StringUtils.isEmpty(orgName)) {
                    Organization org = ctx.getOrg(orgName);
                    if (org == null) {
                        super.addLineError("nonexistent organization: " + orgName);
                        numErrs++;
                    } else {
                        indicator.addOrg(org);
                    }
                }
            }

            ctx.addIndicatorById(indId, indicator);
            ctx.addIndicatorByName(name, indicator);
            numIndicators++;
        }

        if (super.getErrorCount() == 0 && numIndicators == 0) {
            super.addError("No indicators specified.");
        }
    }

}
