/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.reader;

import com.ocs.indaba.idef.common.Constants;
import com.ocs.indaba.idef.imp.ProcessContext;
import com.ocs.indaba.idef.xo.SurveyConfig;
import com.ocs.indaba.po.Language;
import com.ocs.indaba.po.Organization;
import com.ocs.util.StringUtils;
import java.io.File;

/**
 *
 * @author yc06x
 */
public class SurveyConfigReader extends Reader {

    static private final int COL_ID = 0;
    static private final int COL_NAME = 1;
    static private final int COL_DESC = 2;
    static private final int COL_INSTRUCTIONS = 3;
    static private final int COL_LANGUAGE = 4;
    static private final int COL_ORG = 5;
    static private final int COL_VISIBILITY = 6;

    // required colums are in upper case; optional are in lower case
    static private final String[] COLUMNS = {
        "ID",
        "NAME",
        "description",
        "instructions",
        "LANGUAGE",
        "ORGANIZATION",
        "VISIBILITY"
    };

    public SurveyConfigReader(ProcessContext ctx, File file) {
        super.createReader(ctx, file, COLUMNS);
    }

    public void read() {
        String[] line;
        int scCount = 0;

        // process projects
        while ((line = readNext()) != null) {
            // validate the fields
            if (!super.checkRequiredColumns(line, COLUMNS)) continue;

            String scId = super.getColumn(line, COL_ID);
            String name = super.getColumn(line, COL_NAME);
            String desc = super.getColumn(line, COL_DESC);
            String instructions = super.getColumn(line, COL_INSTRUCTIONS);
            String lang = super.getColumn(line, COL_LANGUAGE);
            String orgNames = super.getColumn(line, COL_ORG);
            String visibility = super.getColumn(line, COL_VISIBILITY);

            String visOv = ctx.getMetaProperty(Constants.META_PROP_NAME_VISIBILITY_OVERRIDE);
            if (visOv != null) visibility = visOv;

            String langOv = ctx.getMetaProperty(Constants.META_PROP_NAME_LANGUAGE_OVERRIDE);
            if (langOv != null) lang = langOv;

            String orgOv = ctx.getMetaProperty(Constants.META_PROP_NAME_ORGANIZATION_OVERRIDE);
            if (orgOv != null) orgNames = orgOv;

            int numErrs = 0;

            short vis = super.parseVisibility(visibility);
            if (vis < 0) {
                super.addLineError("bad value for Visibility: " + visibility);
                numErrs++;
            }

            if (ctx.getSurveyConfig(scId) != null) {
                super.addLineError("duplicate survey config ID: " + scId);
                numErrs++;
            }
            
            if (numErrs > 0) continue;

            // create SC
            SurveyConfig sc = new SurveyConfig();
            sc.setId(scId);
            sc.setDescription(desc);
            sc.setInstructions(instructions);
            sc.setName(name);
            sc.setVisibility(vis);

            Language language = ctx.getLanguage(lang);
            if (language == null) {
                super.addLineError("nonexistent language: " + lang);
                numErrs++;
            }
            sc.setLanguage(language);

            String[] orgNameList = super.parseOrgNames(orgNames);
            for (String orgName : orgNameList) {
                if (!StringUtils.isEmpty(orgName)) {
                    Organization org = ctx.getOrg(orgName);
                    if (org == null) {
                        super.addLineError("nonexistent organization: " + orgName);
                        numErrs++;
                    } else {
                        sc.addOrg(org);
                    }
                }
            }

            ctx.addSurveyConfig(scId, sc);
            scCount++;
        }

        if (super.getErrorCount() == 0 && scCount == 0) {
            super.addError("No Survey Configs specified.");
        }
    }

   

}
