/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.loader;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.idef.imp.ProcessContext;
import com.ocs.indaba.idef.xo.AnswerChoice;
import com.ocs.indaba.idef.xo.Indicator;
import com.ocs.indaba.po.AnswerTypeChoice;
import com.ocs.indaba.po.AnswerTypeFloat;
import com.ocs.indaba.po.AnswerTypeInteger;
import com.ocs.indaba.po.AnswerTypeText;
import com.ocs.indaba.po.AtcChoice;
import java.util.Date;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class IndicatorLoader extends Loader {

    

    public IndicatorLoader(ProcessContext ctx) {
        super.createLoader(ctx);
    }

    private void loadOneIndicator(Indicator xo) {
        com.ocs.indaba.po.SurveyIndicator po = new com.ocs.indaba.po.SurveyIndicator();
        
        int surveyAnswerTypeId = 0;

        switch (xo.getType()) {
            case com.ocs.indaba.idef.common.Constants.INDICATOR_TYPE_SINGLE_CHOICE:
            case com.ocs.indaba.idef.common.Constants.INDICATOR_TYPE_MULTI_CHOICE:
                AnswerTypeChoice atc = new AnswerTypeChoice();
                atcDao.create(atc);
                surveyAnswerTypeId = atc.getId();

                // create all choices
                List<AnswerChoice> acList = xo.getChoices();
                for (AnswerChoice ac : acList) {
                    AtcChoice atcc = new AtcChoice();
                    atcc.setAnswerTypeChoiceId(surveyAnswerTypeId);
                    atcc.setCriteria(ac.getCriteria());
                    atcc.setDefaultSelected(false);
                    atcc.setLabel(ac.getOption());
                    atcc.setMask(ac.getMask());

                    if (ac.getScore() == null) {
                        atcc.setScore(0);
                        atcc.setUseScore(false);
                    } else {
                        atcc.setScore((int)(ac.getScore() * 10000));
                        atcc.setUseScore(true);
                    }

                    atcc.setWeight(ac.getWeight());
                    atccDao.create(atcc);
                }
                break;

            case com.ocs.indaba.idef.common.Constants.INDICATOR_TYPE_INTEGER:
                AnswerTypeInteger ati = new AnswerTypeInteger();
                ati.setCriteria(xo.getCriteria());
                ati.setMaxValue(xo.getMaxInt());
                ati.setMinValue(xo.getMinInt());
                atiDao.create(ati);
                surveyAnswerTypeId = ati.getId();
                break;

            case com.ocs.indaba.idef.common.Constants.INDICATOR_TYPE_FLOAT:
                AnswerTypeFloat atf = new AnswerTypeFloat();
                atf.setCriteria(xo.getCriteria());
                atf.setMaxValue((float)xo.getMaxDouble());
                atf.setMinValue((float)xo.getMinDouble());
                atfDao.create(atf);
                surveyAnswerTypeId = atf.getId();
                break;

            case com.ocs.indaba.idef.common.Constants.INDICATOR_TYPE_TEXT:
            default:
                AnswerTypeText att = new AnswerTypeText();
                att.setCriteria(xo.getCriteria());
                att.setMaxChars(xo.getMaxInt());
                att.setMinChars(xo.getMinInt());
                attDao.create(att);
                surveyAnswerTypeId = att.getId();
                break;

        }

        po.setAnswerType(xo.getType());
        po.setAnswerTypeId(surveyAnswerTypeId);
        po.setCreateTime(new Date());
        po.setCreateUserId(0);
        po.setCreatorOrgId(xo.getOrgs().get(0).getId());
        po.setImportId(ctx.getImportId());
        po.setLanguageId(xo.getLanguage().getId());
        po.setName(ctx.addImpPrefix() ? getQualifiedName(xo.getName()) : xo.getName());
        po.setOwnerOrgId(po.getCreatorOrgId());
        po.setQuestion(xo.getQuestion());
        po.setReferenceId(xo.getRef().getDboId());
        po.setVisibility(xo.getVisibility());
        po.setState((short)Constants.RESOURCE_STATE_ENDORSED);
        po.setStatus(Constants.INDICATOR_STATUS_ACTIVE);
        po.setTip(xo.getHint());

        indicatorDao.create(po);
        xo.setDboId(po.getId());
    }

    public void load() {
        List<Indicator> indicatorList = ctx.getIndicators();

        if (indicatorList == null || indicatorList.isEmpty()) {
            ctx.addError("No indicators to load!");
            return;
        }

        for (Indicator indicator : indicatorList) {
            loadOneIndicator(indicator);
        }
    }

}
