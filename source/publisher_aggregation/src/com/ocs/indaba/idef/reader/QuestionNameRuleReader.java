/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.reader;

import com.ocs.indaba.idef.imp.ProcessContext;
import java.io.File;

/**
 *
 * @author yc06x
 */
public class QuestionNameRuleReader extends Reader {

    static private final int COL_NAME = 0;
    static private final int COL_RULE = 1;

    // required colums are in upper case; optional are in lower case
    static private final String[] COLUMNS = {
        "NAME",
        "RULE"
    };

    public QuestionNameRuleReader(ProcessContext ctx, File file) {
        super.createReader(ctx, file, COLUMNS);
    }

    public void read() {
        String[] line;

        // process projects
        while ((line = readNext()) != null) {
            // validate the fields
            if (!super.checkRequiredColumns(line, COLUMNS)) continue;

            String name = super.getColumn(line, COL_NAME);
            String rule = super.getColumn(line, COL_RULE);

            ctx.setQuestionNameRule(name, rule);
        }
    }

}
