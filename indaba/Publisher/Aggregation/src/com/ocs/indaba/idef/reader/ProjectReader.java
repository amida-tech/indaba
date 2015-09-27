/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.reader;

import com.ocs.indaba.idef.common.Constants;
import com.ocs.indaba.idef.imp.ProcessContext;
import com.ocs.indaba.idef.xo.Project;
import com.ocs.indaba.idef.xo.User;
import com.ocs.indaba.po.Organization;
import com.ocs.indaba.po.StudyPeriod;
import com.ocs.util.StringUtils;
import java.io.File;
import java.util.Date;

/**
 *
 * @author yc06x
 */
public class ProjectReader extends Reader {

    static private final int COL_ID = 0;
    static private final int COL_ORG = 1;
    static private final int COL_NAME = 2;
    static private final int COL_DESC = 3;
    static private final int COL_START_TIME = 4;
    static private final int COL_STUDY_PERIOD = 5;
    static private final int COL_ADMIN_USER = 6;
    static private final int COL_CLOSE_TIME = 7;
    static private final int COL_VISIBILITY = 8;

    // required colums are in upper case; optional are in lower case
    static private final String[] COLUMNS = {
        "ID",
        "ORGANIZATION",
        "NAME",
        "description",
        "START TIME",
        "STUDY PERIOD",
        "admin uid",
        "close time",
        "VISIBILITY"
    };


    public ProjectReader(ProcessContext ctx, File file) {
        super.createReader(ctx, file, COLUMNS);
    }

    public void read() {
        String[] line;
        int numProjects = 0;

        // process projects
        while ((line = readNext()) != null) {
            int numErrs = 0;

            // validate the fields
            if (!super.checkRequiredColumns(line, COLUMNS)) continue;

            String projId = super.getColumn(line, COL_ID);
            String orgNames = super.getColumn(line, COL_ORG);
            String projName = super.getColumn(line, COL_NAME);
            String desc = super.getColumn(line, COL_DESC);
            String startTime = super.getColumn(line, COL_START_TIME);
            String studyPeriodName = super.getColumn(line, COL_STUDY_PERIOD);
            String adminUserId = super.getColumn(line, COL_ADMIN_USER);
            String closeTime = super.getColumn(line, COL_CLOSE_TIME);
            String visibility = super.getColumn(line, COL_VISIBILITY);

            if (ctx.doMapping()) {
                String newName = ctx.getProjectNameMapping(projName);
                if (newName != null) projName = newName;
            }

            String visOv = ctx.getMetaProperty(Constants.META_PROP_NAME_VISIBILITY_OVERRIDE);
            if (visOv != null) visibility = visOv;

            String spOv = ctx.getMetaProperty(Constants.META_PROP_NAME_STUDY_PERIOD_OVERRIDE);
            if (spOv != null) studyPeriodName = spOv;

            String orgOv = ctx.getMetaProperty(Constants.META_PROP_NAME_ORGANIZATION_OVERRIDE);
            if (orgOv != null) orgNames = orgOv;


            // parse org names
            String[] orgNameList = super.parseOrgNames(orgNames);

            Date startDate = super.parseDate(startTime);
            if (startDate == null) {
                super.addLineError("bad value for Start Time: " + startTime);
                numErrs++;
            }

            Date closeDate = null;
            if (!StringUtils.isEmpty(closeTime)) {
                closeDate = super.parseDate(closeTime);
                if (closeDate == null) {
                    super.addLineError("bad value for Close Time: " + closeTime);
                    numErrs++;
                }
            }

            short vis = super.parseVisibility(visibility);
            if (vis < 0) {
                super.addLineError("bad value for Visibility: " + visibility);
                numErrs++;
            }

            User adminUser = null;
            if (!StringUtils.isEmpty(adminUserId)) {
                adminUser = ctx.getUser(adminUserId);

                if (adminUser == null) {
                    super.addLineError("admin user " + adminUserId + " is not defined in USER table.");
                    numErrs++;
                }
            }

            if (ctx.getProject(projId) != null) {
                super.addLineError("duplicate project ID: " + projId);
                numErrs++;
            }

            StudyPeriod sp = ctx.getStudyPeriod(studyPeriodName);
            if (sp == null) {
                super.addLineError("nonexistent study period: " + studyPeriodName);
                numErrs++;
            }

            if (numErrs != 0) continue;  // don't create project object

            // create project
            Project proj = new Project();
            proj.setId(projId);
            proj.setAdminUser(adminUser);
            proj.setCloseTime(closeDate);
            proj.setDescription(desc);
            proj.setName(projName);
            proj.setStartTime(startDate);
            proj.setVisibility(vis);

            for (String orgName : orgNameList) {
                if (!StringUtils.isEmpty(orgName)) {
                    Organization org = ctx.getOrg(orgName);
                    if (org == null) {
                        super.addLineError("nonexistent organization: " + orgName);
                        numErrs++;
                    } else {
                        proj.addOrg(org);
                    }
                }
            }

            proj.setStudyPeriod(sp);

            ctx.addProject(projId, proj);
            numProjects++;
        }

        if (super.getErrorCount() == 0 && numProjects == 0) {
            super.addError("No projcts specified.");
        }
    }

}
