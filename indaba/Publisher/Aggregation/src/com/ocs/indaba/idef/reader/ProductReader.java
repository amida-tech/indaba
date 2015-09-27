/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.reader;

import com.ocs.indaba.idef.common.Constants;
import com.ocs.indaba.idef.imp.ProcessContext;
import com.ocs.indaba.idef.xo.Product;
import com.ocs.indaba.idef.xo.Project;
import com.ocs.indaba.idef.xo.SurveyConfig;
import java.io.File;

/**
 *
 * @author yc06x
 */
public class ProductReader extends Reader {

    static private final int COL_ID = 0;
    static private final int COL_NAME = 1;
    static private final int COL_PROJECT_ID = 2;
    static private final int COL_DESC = 3;
    static private final int COL_CONTENT_TYPE = 4;
    static private final int COL_CONFIG_ID = 5;
    static private final int COL_MODE = 6;

    // required colums are in upper case; optional are in lower case
    static private final String[] COLUMNS = {
        "ID",
        "NAME",
        "PROJECT ID",
        "description",
        "CONTENT TYPE",
        "CONFIG ID",
        "MODE"
    };

    public ProductReader(ProcessContext ctx, File file) {
        super.createReader(ctx, file, COLUMNS);
    }

    public void read() {
        String[] line;
        int numProducts = 0;

        // process projects
        while ((line = readNext()) != null) {
            // validate the fields
            if (!super.checkRequiredColumns(line, COLUMNS)) continue;

            String prodId = super.getColumn(line, COL_ID);
            String name = super.getColumn(line, COL_NAME);
            String projId = super.getColumn(line, COL_PROJECT_ID);
            String desc = super.getColumn(line, COL_DESC);
            String ctype = super.getColumn(line, COL_CONTENT_TYPE);
            String scId = super.getColumn(line, COL_CONFIG_ID);
            String mode = super.getColumn(line, COL_MODE);

            if (ctx.doMapping()) {
                String newName = ctx.getProductNameMapping(name);
                if (newName != null) name = newName;
            }

            int numErrs = 0;

            Project project = ctx.getProject(projId);
            if (project == null) {
                super.addLineError("referenced project does not exist: " + projId);
                numErrs++;
            }

            SurveyConfig sc = ctx.getSurveyConfig(scId);
            if (sc == null) {
                super.addLineError("referenced survey config does not exist: " + scId);
                numErrs++;
            }

            if (ctx.getProduct(prodId) != null) {
                super.addLineError("duplicate product ID: " + prodId);
                numErrs++;
            }

            short ctypeValue = -1;
            if (ctype.equalsIgnoreCase("survey")) {
                ctypeValue = Constants.CONTENT_TYPE_SURVEY;
            } else if (ctype.equalsIgnoreCase("journal")) {
                ctypeValue = Constants.CONTENT_TYPE_JOURNAL;
            } else {
                super.addLineError("invalid content type: " + ctype);
                numErrs++;
            }

            short modeValue = -1;
            if (mode.equalsIgnoreCase("config")) {
                modeValue = Constants.PRODUCT_MODE_CONFIG;
            } else if (mode.equalsIgnoreCase("test")) {
                modeValue = Constants.PRODUCT_MODE_TEST;
            } else if (mode.equalsIgnoreCase("prod") || mode.equalsIgnoreCase("production")) {
                modeValue = Constants.PRODUCT_MODE_PROD;
            } else {
                super.addLineError("invalid product mode: " + mode);
                numErrs++;
            }

            if (numErrs > 0) continue;

            // create project
            Product product = new Product();
            product.setContentType(ctypeValue);
            product.setDescription(desc);
            product.setId(prodId);
            product.setMode(modeValue);
            product.setName(name);
            product.setProject(project);
            product.setSurveyConfig(sc);

            ctx.addProduct(prodId, product);
            numProducts++;

            if (project.getVisibility() != sc.getVisibility()) {
                super.addLineError("The visibility of the survey config " + sc.getName() + " is different from that of the project " + project.getName());
            }
        }

        if (super.getErrorCount() == 0 && numProducts == 0) {
            super.addError("No products specified.");
        }
    }

}
