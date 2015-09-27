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
public class ProductNameMappingReader extends Reader {

    static private final int COL_OLD_NAME = 0;
    static private final int COL_NEW_NAME = 1;

    // required colums are in upper case; optional are in lower case
    static private final String[] COLUMNS = {
        "OLD NAME",
        "NEW NAME"
    };

    public ProductNameMappingReader(ProcessContext ctx, File file) {
        super.createReader(ctx, file, COLUMNS);
    }

    public void read() {
        String[] line;

        // process projects
        while ((line = readNext()) != null) {
            // validate the fields
            if (!super.checkRequiredColumns(line, COLUMNS)) continue;

            String oldName = super.getColumn(line, COL_OLD_NAME);
            String newName = super.getColumn(line, COL_NEW_NAME);

            ctx.setProductNameMapping(oldName, newName);
        }
    }

}
