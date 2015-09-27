/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.imp;

import com.ocs.indaba.idef.loader.IndicatorLoader;
import com.ocs.indaba.idef.loader.ProductLoader;
import com.ocs.indaba.idef.loader.ProjectLoader;
import com.ocs.indaba.idef.loader.ScorecardLoader;
import com.ocs.indaba.idef.loader.SurveyConfigLoader;
import com.ocs.indaba.idef.loader.UserLoader;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class ImportProcessor {

    private ImportValidator validator;
    private ImportMapper mapper;
    ProcessContext ctx;

    public ImportProcessor(int importId, String rootDir, ProcessContext ctx) {
        this.ctx = ctx;
        validator = new ImportValidator(importId, rootDir, ctx);
        mapper = new ImportMapper(rootDir, ctx);
    }


    public int getErrorCount() {
        return validator.getProcessContext().getErrorCount();
    }


    public List<String> getErrors() {
        return validator.getProcessContext().getErrors();
    }


    public ProcessContext getProcessContext() {
        return validator.getProcessContext();
    }

    public void process() {
        mapper.getMappings();

        if (mapper.getMappingCount() > 0) {
            ctx.setDoMapping(true);
            ctx.setAddImpPrefix(false);
        }
        
        validator.validate();

        if (ctx.getErrorCount() > 0) return;  // not valid

        // load users
        UserLoader userLoader = new UserLoader(ctx);
        userLoader.load();
        if (ctx.getErrorCount() > 0) return;  // not valid

        // load indicators
        IndicatorLoader indicatorLoader = new IndicatorLoader(ctx);
        indicatorLoader.load();
        if (ctx.getErrorCount() > 0) return;  // not valid

        // load surveyConfigs
        SurveyConfigLoader scLoader = new SurveyConfigLoader(ctx);
        scLoader.load();
        if (ctx.getErrorCount() > 0) return;  // not valid

        // load projects
        ProjectLoader projLoader = new ProjectLoader(ctx);
        projLoader.load();
        if (ctx.getErrorCount() > 0) return;  // not valid

        // load products
        ProductLoader prodLoader = new ProductLoader(ctx);
        prodLoader.load();
        if (ctx.getErrorCount() > 0) return;  // not valid

        // load scorecards
        ScorecardLoader scorecardLoader = new ScorecardLoader(ctx);
        scorecardLoader.load();
        if (ctx.getErrorCount() > 0) return;  // not valid
    }
}
