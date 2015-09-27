/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.reader;

import com.ocs.indaba.idef.imp.ProcessContext;
import com.ocs.indaba.idef.xo.Product;
import com.ocs.indaba.idef.xo.Scorecard;
import com.ocs.indaba.idef.xo.User;
import com.ocs.indaba.po.Target;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class ScorecardReader extends Reader {

    static private final int COL_ID = 0;
    static private final int COL_PRODUCT_ID = 1;
    static private final int COL_TARGET = 2;
    static private final int COL_AUTHOR_ID = 3;
    static private final int COL_SUBMIT_TIME = 4;

    // required colums are in upper case; optional are in lower case
    static private final String[] COLUMNS = {
        "ID",
        "PRODUCT ID",
        "TARGET",
        "AUTHOR USER ID",
        "SUBMIT TIME"
    };

    public ScorecardReader(ProcessContext ctx, File file) {
        super.createReader(ctx, file, COLUMNS);
    }

    public void read() {
        String[] line;
        int numScorecards = 0;

        // process projects
        while ((line = readNext()) != null) {
            // validate the fields
            if (!super.checkRequiredColumns(line, COLUMNS)) continue;

            String scId = super.getColumn(line, COL_ID);
            String prodId = super.getColumn(line, COL_PRODUCT_ID);
            String targetName = super.getColumn(line, COL_TARGET);
            String authorId = super.getColumn(line, COL_AUTHOR_ID);
            String submitTimeStr = super.getColumn(line, COL_SUBMIT_TIME);

            int numErrs = 0;

            if (ctx.getScorecard(scId) != null) {
                super.addLineError("duplicate scorecard: " + prodId);
                numErrs++;
            }

            Product prod = ctx.getProduct(prodId);
            if (prod == null) {
                super.addLineError("referenced product does not exist: " + prodId);
                numErrs++;
            }

            Target target = ctx.getTarget(targetName);
            if (target == null) {
                super.addLineError("referenced target does not exist: " + targetName);
                numErrs++;
            }

            if (prod != null && target != null) {
                if (ctx.getProdTargetScorecard(prodId, target.getId()) != null) {
                    super.addLineError("duplicate scorecard for Product " + prodId + " and target " + target.getName());
                    numErrs++;
                }
            }
            
            User author = ctx.getUser(authorId);
            if (author == null) {
                super.addLineError("referenced author not exist: " + authorId);
                numErrs++;
            }

            Date submitTime = super.parseDate(submitTimeStr);
            if (submitTime == null) {
                super.addLineError("invalid submit time: " + submitTimeStr);
                numErrs++;
            }

            if (numErrs > 0) continue;

            // create project
            Scorecard card = new Scorecard();
            card.setId(scId);
            card.setProduct(prod);
            card.setSubmitTime(submitTime);
            card.setTarget(target);
            card.setUser(author);
            
            ctx.addScorecard(scId, card);
            ctx.addProdTargetScorecard(prodId, target.getId(), card);
            numScorecards++;
        }

        if (super.getErrorCount() == 0 && numScorecards == 0) {
            super.addError("No scorecards specified.");
        }
    }

}
