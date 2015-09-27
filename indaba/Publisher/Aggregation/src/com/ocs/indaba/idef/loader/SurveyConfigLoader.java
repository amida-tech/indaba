/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.loader;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.idef.imp.ProcessContext;
import com.ocs.indaba.idef.xo.SurveyCategory;
import com.ocs.indaba.idef.xo.SurveyConfig;
import com.ocs.indaba.idef.xo.SurveyQuestion;
import com.ocs.indaba.po.Organization;
import com.ocs.indaba.po.ScContributor;
import com.ocs.indaba.survey.tree.SurveyTree;
import java.util.Date;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class SurveyConfigLoader extends Loader {

    public SurveyConfigLoader(ProcessContext ctx) {
        super.createLoader(ctx);
    }


    private void loadOneSurveyConfig(SurveyConfig xo) {

        com.ocs.indaba.po.SurveyConfig po = new com.ocs.indaba.po.SurveyConfig();
        po.setCreateTime(new Date());
        po.setCreatorOrgId(xo.getOrgs().get(0).getId());
        po.setDescription(xo.getDescription());
        po.setImportId(ctx.getImportId());
        po.setInstructions(xo.getInstructions());
        po.setLanguageId(xo.getLanguage().getId());
        po.setName(getQualifiedName(xo.getName()));
        po.setOwnerOrgId(po.getCreatorOrgId());
        po.setVisibility(xo.getVisibility());
        po.setStatus((short)Constants.RESOURCE_STATUS_ACTIVE);

        List<Organization> orgs = xo.getOrgs();
        if (orgs.size() > 1) {
            for (int i = 1; i < orgs.size(); i++) {
                // add additional orgs
                ScContributor scc = new ScContributor();
                scc.setOrgId(orgs.get(i).getId());
                scc.setSurveyConfigId(po.getId());
                sccDao.create(scc);
            }
        }

        scDao.create(po);
        xo.setDboId(po.getId());

        // now load tree structure
        List<SurveyCategory> cats = xo.getRootCategories();
        for (SurveyCategory cat : cats) {
            loadCategory(po.getId(), cat);
        }

        // load questions
        List<SurveyQuestion> questions = xo.getQuestions();
        if (questions != null) {
            for (SurveyQuestion q : questions) {
                loadQuestion(po.getId(), q);
            }
        }

        // now set isTsc
        SurveyTree tree = surveyConfigService.buildTree(po.getId());
        po.setIsTsc(tree.isTraditional());
        scDao.update(po);
    }


    private void loadQuestion(int scId, SurveyQuestion xo) {
        com.ocs.indaba.po.SurveyQuestion po = new com.ocs.indaba.po.SurveyQuestion();
        po.setName(xo.getLabel());
        po.setPublicName(xo.getLabel());
        po.setSurveyCategoryId(xo.getParent().getDboId());
        po.setSurveyConfigId(scId);
        po.setSurveyIndicatorId(xo.getIndicator().getDboId());
        po.setWeight(xo.getWeight());
        qstDao.create(po);

        xo.setDboId(po.getId());
    }


    private void loadCategory(int scId, SurveyCategory xo) {
        com.ocs.indaba.po.SurveyCategory po = new com.ocs.indaba.po.SurveyCategory();
        po.setDescription(xo.getDescription());
        po.setLabel(xo.getLabel());
        po.setName(xo.getLabel());
        po.setSurveyConfigId(scId);
        po.setTitle(xo.getTitle());
        po.setWeight(xo.getWeight());

        SurveyCategory parent = xo.getParent();
        po.setParentCategoryId(parent == null ? 0 : parent.getDboId());
        
        catDao.create(po);
        xo.setDboId(po.getId());

        // load subcats
        List<SurveyCategory> children = xo.getChildren();
        if (children != null) {
            for (SurveyCategory child : children) {
                loadCategory(scId, child);
            }
        }
    }



    public void load() {
        List<SurveyConfig> list = ctx.getSurveyConfigs();

        if (list == null || list.isEmpty()) {
            ctx.addError("No Survey Configs to load!");
            return;
        }

        for (SurveyConfig sc : list) {
            loadOneSurveyConfig(sc);
        }
    }

}
