/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.reader;

import com.ocs.indaba.idef.imp.ProcessContext;
import com.ocs.indaba.idef.xo.SurveyCategory;
import com.ocs.indaba.idef.xo.SurveyConfig;
import com.ocs.util.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class SurveyCategoryReader extends Reader {

    static private final int COL_ID = 0;
    static private final int COL_CONFIG_ID = 1;
    static private final int COL_PARENT_ID = 2;
    static private final int COL_DESC = 3;
    static private final int COL_LABEL = 4;
    static private final int COL_TITLE = 5;
    static private final int COL_WEIGHT = 6;

    // required colums are in upper case; optional are in lower case
    static private final String[] COLUMNS = {
        "ID",
        "SURVEY CONFIG ID",
        "parent category id",
        "description",
        "LABEL",
        "TITLE",
        "WEIGHT"
    };

    public SurveyCategoryReader(ProcessContext ctx, File file) {
        super.createReader(ctx, file, COLUMNS);        
    }

    public void read() {
        String[] line;
        List<SurveyCategory> categories = new ArrayList<SurveyCategory>();
        ctx.setCategories(categories);

        // process projects
        while ((line = readNext()) != null) {
            // validate the fields
            if (!super.checkRequiredColumns(line, COLUMNS)) continue;

            String catId = super.getColumn(line, COL_ID);
            String scId = super.getColumn(line, COL_CONFIG_ID);
            String parentId = super.getColumn(line, COL_PARENT_ID);
            String desc = super.getColumn(line, COL_DESC);
            String label = super.getColumn(line, COL_LABEL);
            String title = super.getColumn(line, COL_TITLE);
            String weight = super.getColumn(line, COL_WEIGHT);

            int numErrs = 0;

            if (ctx.getCategory(catId) != null) {
                super.addLineError("duplicate survey category: " + scId);
                numErrs++;
            }

            SurveyConfig sc = ctx.getSurveyConfig(scId);
            if (sc == null) {
                super.addLineError("referenced survey config does not exist: " + scId);
                numErrs++;
            }

            int weightValue = StringUtils.str2int(weight);
            if (weightValue == Integer.MIN_VALUE) {
                super.addLineError("invalid weight value: " + weight);
                numErrs++;
            }

            if (numErrs > 0) continue;

            // create project
            SurveyCategory cat = new SurveyCategory();
            cat.setDescription(desc);
            cat.setId(catId);
            cat.setLabel(label);
            cat.setParentId(parentId);
            cat.setTitle(title);
            cat.setSurveyConfig(sc);
            cat.setWeight(weightValue);

            categories.add(cat);
            ctx.addCategory(catId, cat);
        }

        if (super.getErrorCount() == 0) {
            if (categories.isEmpty()) {
                super.addError("No categories specified.");
            } else {
                // check for missing references
                for (SurveyCategory c : categories) {
                    String parentId = c.getParentId();
                    if (StringUtils.isEmpty(parentId)) {
                        // this category has no parent
                        c.setParent(null);
                        
                        // add it as a root category in the SC
                        c.getSurveyConfig().addCategory(c);
                    } else {
                        SurveyCategory p = ctx.getCategory(parentId);
                        if (p == null) {
                            super.addError("In category " + c.getId() + " referenced parent category doesn't exist: " + parentId);
                        } else if (c.getSurveyConfig() != p.getSurveyConfig()) {
                            // parent and child don't belong to the same SC
                             super.addError("Category " + c.getId() + " and its parent " + parentId + " don't belong to the same survey config.");                           
                        } else {
                            c.setParent(p);
                            p.addChild(c);
                        }
                    }
                }

                // now check for cyclic references
                for (SurveyCategory c : categories) {
                    String cycle = checkCycle(c);
                    if (cycle != null) {
                        super.addError("Cyclic reference detected: " + cycle);
                    }
                }
            }
        }
    }


    private String checkCycle(SurveyCategory cat) {
        SurveyCategory p = cat;
        StringBuilder sb = new StringBuilder();

        while (p.getParent() != null) {
            sb.append(p.getId()).append(":");
            p = p.getParent();

            if (p == cat) {
                sb.append(p.getId());
                return sb.toString();
            }
        }

        return null;
    }

}
