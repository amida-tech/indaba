/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.service;

import com.ocs.common.Config;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.dao.IdefDAO;
import com.ocs.indaba.builder.dao.ProductDAO;
import com.ocs.indaba.builder.dao.ProjectDAO;
import com.ocs.indaba.dao.ImportsDAO;
import com.ocs.indaba.dao.LanguageDAO;
import com.ocs.indaba.builder.dao.StudyPeriodDAO;
import com.ocs.indaba.builder.dao.TargetDAO;
import com.ocs.indaba.dao.NotedefDAO;
import com.ocs.indaba.dao.NoteobjIntlDAO;
import com.ocs.indaba.idef.exporter.GenericExporter;
import com.ocs.indaba.idef.exporter.SimpleExporter;
import com.ocs.indaba.idef.imp.ImportProcessor;
import com.ocs.indaba.idef.imp.ImportValidator;
import com.ocs.indaba.idef.imp.ProcessContext;
import com.ocs.indaba.idef.xo.Ref;
import com.ocs.indaba.idef.xo.RefChoice;
import com.ocs.indaba.po.Imports;
import com.ocs.indaba.po.Language;
import com.ocs.indaba.po.Organization;
import com.ocs.indaba.po.Reference;
import com.ocs.indaba.po.ReferenceChoice;
import com.ocs.indaba.po.StudyPeriod;
import com.ocs.indaba.po.Target;
import com.ocs.indaba.service.ReferenceService;
import com.ocs.indaba.service.SurveyConfigService;
import com.ocs.indaba.survey.table.SurveyTableService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author yc06x
 */
public class IdefService {

    @Autowired
    private ImportsDAO impDao = null;

    @Autowired
    private LanguageDAO langDao = null;

    @Autowired
    private TargetDAO targetDao = null;

    @Autowired
    private StudyPeriodDAO spDao = null;

    @Autowired
    private OrganizationService orgService = null;

    @Autowired
    private ReferenceService refService = null;

    @Autowired
    private IdefDAO idefDao = null;

    @Autowired
    private ProductDAO productDao = null;

    @Autowired
    private ProjectDAO projectDao = null;

    @Autowired
    private ScorecardService scorecardService = null;

    @Autowired
    SurveyConfigService surveyConfigService;

    @Autowired
    SurveyTableService surveyTableService;

    @Autowired
    private NotedefDAO notedefDao = null;

    @Autowired
    private NoteobjIntlDAO noteobjIntlDao = null;


    public Imports getImportById(int importId) {
        return impDao.get(importId);
    }


    public ProcessContext validateIdef(String rootDir) {
        ProcessContext ctx = createProcessContext();
        ImportValidator validator = new ImportValidator(0, rootDir, ctx);
        validator.validate();
        return ctx;
    }


    private ProcessContext createProcessContext() {
        // get all orgs
        List<Organization> orgs = orgService.getAllOrgs();
        List<Language> langs = langDao.selectAllLanguages();
        List<Target> targets = targetDao.findAll();
        List<StudyPeriod> sps = spDao.findAll();

        // get all references
        List<Reference> references = refService.getAllReferences();
        List<Ref> refs = new ArrayList<Ref>();

        for (Reference r : references) {
            Ref ref = new Ref();
            ref.setDboId(r.getId());
            ref.setDescription(r.getDescription());
            ref.setName(r.getName());
            ref.setType(r.getChoiceType());
            ref.setChoices(null);
            refs.add(ref);

            List<ReferenceChoice> choices = refService.getReferenceChoice(r.getId());
            if (choices == null || choices.isEmpty()) continue;

            List<RefChoice> rcList = new ArrayList<RefChoice>();
            for (ReferenceChoice c : choices) {
                RefChoice rc = new RefChoice();
                rc.setLabel(c.getLabel());
                rc.setMask((int)c.getMask());
                rc.setWeight(c.getWeight());
                rc.setSname(c.getSname());
                rcList.add(rc);
            }
            ref.setChoices(rcList);
        }

        ProcessContext ctx = new ProcessContext(orgs, refs, langs, sps, targets);
        return ctx;
    }

    public ProcessContext importIdef(Imports imp, String rootDir) {

        imp.setStartTime(new Date());

        ProcessContext ctx = createProcessContext();

        ImportProcessor processor = new ImportProcessor(imp.getId(), rootDir, ctx);

        processor.process();

        imp.setEndTime(new Date());
        imp.setResult(ctx.getErrorCount() > 0 ? Constants.IDEF_IMPORT_RESULT_FAILURE : Constants.IDEF_IMPORT_RESULT_SUCCESS);

        impDao.update(imp);

        return ctx;
    }


    public int deleteIdef(Imports imp) {
        imp.setStartTime(new Date());

        int rc = impDao.deleteImport(imp.getId());

        if (rc == 0) {
            imp.setResult(Constants.IDEF_IMPORT_RESULT_DELETED);
            imp.setEndTime(new Date());
            impDao.update(imp);
        }

        return rc;
    }


    public SimpleExporter export(int productId) {
        String expDir = Config.getString(Constants.KEY_IDEF_EXPORT_DIR);
        SimpleExporter exporter = new SimpleExporter(idefDao, scorecardService);
        exporter.export(productId, expDir);
        return exporter;
    }


    public GenericExporter exportGeneric(int productId) {
        try {
            List<Target> targets = targetDao.findAll();
            Map<Integer, Target> targetMap = new HashMap<Integer, Target>();
            for (Target target : targets) {
                targetMap.put(target.getId(), target);
            }

            String expDir = Config.getString(Constants.KEY_IDEF_EXPORT_DIR);
            GenericExporter exporter = new GenericExporter(idefDao, scorecardService, targetMap, 
                    projectDao, productDao, notedefDao, noteobjIntlDao, 
                    surveyConfigService, surveyTableService,
                    productId, expDir);
            exporter.export();
            return exporter;
        } catch (Throwable ex) {
            return null;
        }
    }

}
