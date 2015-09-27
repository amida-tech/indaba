/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.service;

import com.ocs.indaba.aggregation.dao.IndicatorDAO;
import com.ocs.indaba.aggregation.dao.ItagsDAO;
import com.ocs.indaba.aggregation.vo.PubIndicatorVO;
import com.ocs.indaba.aggregation.vo.QuestionDef;
import com.ocs.indaba.aggregation.vo.QuestionOption;
import com.ocs.indaba.dao.SurveyQuestionDAO;
import com.ocs.indaba.po.Itags;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.po.SurveyQuestion;
import com.ocs.indaba.vo.SurveyQuestionVO;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.dao.AnswerTypeFloatDAO;
import com.ocs.indaba.dao.AnswerTypeIntegerDAO;
import com.ocs.indaba.dao.AnswerTypeTextDAO;
import com.ocs.indaba.dao.AtcChoiceDAO;
import com.ocs.indaba.dao.AtcChoiceIntlDAO;
import com.ocs.indaba.dao.SurveyIndicatorIntlDAO;
import com.ocs.indaba.po.AnswerTypeFloat;
import com.ocs.indaba.po.AnswerTypeInteger;
import com.ocs.indaba.po.AnswerTypeText;
import com.ocs.indaba.po.AtcChoice;
import com.ocs.indaba.po.AtcChoiceIntl;
import com.ocs.indaba.po.SurveyIndicatorIntl;
import com.ocs.util.StringUtils;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Gerry
 */
public class IndicatorService {

    private IndicatorDAO pubIndicatorDao;
    private ItagsDAO pubItagsDao;
    private SurveyQuestionDAO qstDao;
    private AtcChoiceDAO atcChoiceDao;
    private AnswerTypeIntegerDAO atiDao;
    private AnswerTypeFloatDAO atfDao;
    private AnswerTypeTextDAO attDao;

    @Autowired
    private SurveyIndicatorIntlDAO siiDao = null;

    @Autowired
    private AtcChoiceIntlDAO atciDao = null;


    public List<PubIndicatorVO> getPubIndicatorVOByWorksetId(int worksetId) {
        return pubIndicatorDao.selectIndicatorVOByWorksetId(worksetId);
    }

    public List<PubIndicatorVO> getPubIndicatorVOsByWorksetId(int worksetId) {
        List<PubIndicatorVO> ret = this.getPubIndicatorVOByWorksetId(worksetId);
        for (int i = 0; i < ret.size(); i++) {
            String itags = "";
            List<Itags> itagsList = pubItagsDao.selectItagsByIndicatorId(ret.get(i).getIndicator().getId());
            for (int j = 0; j < itagsList.size(); j++) {
                itags += " " + itagsList.get(j).getTerm();
            }
            ret.get(i).setItags(itags);
        }
        return ret;
    }

    public String[] getIndicatorNamesByVO(List<PubIndicatorVO> indicatorList) {
        TreeSet<String> ts = new TreeSet<String>();
        for (PubIndicatorVO indicatorVO : indicatorList) {
            ts.add(indicatorVO.getIndicator().getName());
        }
        return ts.toArray(new String[]{});
    }

    public String[] getIndicatorQuestionsByVO(List<PubIndicatorVO> indicatorList) {
        TreeSet<String> ts = new TreeSet<String>();
        for (PubIndicatorVO indicatorVO : indicatorList) {
            ts.add(indicatorVO.getIndicator().getQuestion());
        }
        return ts.toArray(new String[]{});
    }

    public String getItagsByIndicatorId(int indicatorId) {
        List<Itags> itagList = pubItagsDao.selectItagsByIndicatorId(indicatorId);
        StringBuilder sb = new StringBuilder();
        for (Itags itags : itagList) {
            sb.append(itags.getTerm()).append(",");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    public String[] getDistinctItagsByWorksetId(int worksetId) {
        TreeSet<String> ts = new TreeSet<String>();
        List<Itags> itagList = pubItagsDao.selectDistinctItagsByWorksetId(worksetId);
        for (Itags itags : itagList) {
            ts.add(itags.getTerm());
        }
        return ts.toArray(new String[]{});
    }


    private List<QuestionDef> getQuestionDefinitions(int surveyConfigId) {
        // get all indicators
        List<SurveyQuestionVO> vos = qstDao.selectSurveyQuestions(surveyConfigId);

        if (vos == null) return null;

        // get all choices. Map answerTypeId to list of choices
        HashMap<Integer, List<QuestionOption>> atcMap = new HashMap<Integer, List<QuestionOption>>();
        List<AtcChoice> choices = atcChoiceDao.getAtcChoicesOfSurveyConfig(surveyConfigId);
        if (choices != null) {
            for (AtcChoice ac : choices) {
                List<QuestionOption> opts = atcMap.get(ac.getAnswerTypeChoiceId());
                if (opts == null) {
                    opts = new ArrayList<QuestionOption>();
                    atcMap.put(ac.getAnswerTypeChoiceId(), opts);
                }
                QuestionOption opt = new QuestionOption();
                opt.setId(ac.getId());
                opt.setCriteria(ac.getCriteria());
                opt.setLabel(ac.getLabel());
                opt.setMask(((int) ac.getMask()));
                opt.setScore(ac.getScore());
                opt.setUseScore(ac.getUseScore());
                opt.setWeight(ac.getWeight());
                opt.setHint("");
                opts.add(opt);
            }
        }

        // build ATI map
        HashMap<Integer, AnswerTypeInteger> atiMap = new HashMap<Integer, AnswerTypeInteger>();
        List<AnswerTypeInteger> atiList = atiDao.getTypeDefsOfSurveyConfig(surveyConfigId);
        if (atiList != null) {
            for (AnswerTypeInteger ati : atiList) {
                atiMap.put(ati.getId(), ati);
            }
        }

        // build ATF map
        HashMap<Integer, AnswerTypeFloat> atfMap = new HashMap<Integer, AnswerTypeFloat>();
        List<AnswerTypeFloat> atfList = atfDao.getTypeDefsOfSurveyConfig(surveyConfigId);
        if (atfList != null) {
            for (AnswerTypeFloat atf : atfList) {
                atfMap.put(atf.getId(), atf);
            }
        }

        // build ATT map
        HashMap<Integer, AnswerTypeText> attMap = new HashMap<Integer, AnswerTypeText>();
        List<AnswerTypeText> attList = attDao.getTypeDefsOfSurveyConfig(surveyConfigId);
        if (attList != null) {
            for (AnswerTypeText att : attList) {
                attMap.put(att.getId(), att);
            }
        }


        List<QuestionDef> defs = new ArrayList<QuestionDef>();

        for (SurveyQuestionVO vo : vos) {
            SurveyQuestion qst = vo.getQuestion();
            SurveyIndicator indicator = vo.getIndicator();
            List<QuestionOption> options = null;
            short type = indicator.getAnswerType();
            String criteria = null;

            switch(type) {
                case Constants.ANSWER_TYPE_SINGLE:
                case Constants.ANSWER_TYPE_MULTI:
                    // get options
                    options = atcMap.get(indicator.getAnswerTypeId());
                    break;

                case Constants.ANSWER_TYPE_FLOAT:
                    AnswerTypeFloat atf = atfMap.get(indicator.getAnswerTypeId());
                    if (atf != null) criteria = atf.getCriteria();
                    break;

                case Constants.ANSWER_TYPE_INTEGER:
                    AnswerTypeInteger ati = atiMap.get(indicator.getAnswerTypeId());
                    if (ati != null) criteria = ati.getCriteria();
                    break;

                case Constants.ANSWER_TYPE_TEXT:
                    AnswerTypeText att = attMap.get(indicator.getAnswerTypeId());
                    if (att != null) criteria = att.getCriteria();
                    break;

            }

            QuestionDef def = new QuestionDef(indicator.getId(), qst.getId(), indicator.getAnswerType(),
                    qst.getName(), qst.getPublicName(), indicator.getQuestion(), indicator.getTip(), criteria, options);

            defs.add(def);
        }

        return defs;
    }



    public void getTextMaps(int surveyConfigId, int langId, Map<Integer, SurveyIndicatorIntl> siiMap, Map<Integer, AtcChoiceIntl> atciMap) {
        List<SurveyIndicatorIntl> siiList = siiDao.findByConfigIdAndLanguage(surveyConfigId, langId);
        if (siiList != null) {
            for (SurveyIndicatorIntl sii : siiList) {
                siiMap.put(sii.getSurveyIndicatorId(), sii);
            }
        }


        List<AtcChoiceIntl> atciList = atciDao.selectByConfigIdAndLanguage(surveyConfigId, langId);
        if (atciList != null) {
            for (AtcChoiceIntl atci : atciList) {
                atciMap.put(atci.getAtcChoiceId(), atci);
            }
        }
    }


    public Map<Integer, Integer> getQuestionToIndicatorIdMap(int surveyConfigId) {
        List<SurveyQuestionVO> qsts = qstDao.selectSurveyQuestions(surveyConfigId);
        if (qsts == null) return null;

        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (SurveyQuestionVO q : qsts) {
            map.put(q.getQuestion().getId(), q.getIndicator().getId());
        }

        return map;
    }


    public List<QuestionDef> getQuestionDefinitionsByLanguage(int surveyConfigId, int langId) {
        List<QuestionDef> defs = getQuestionDefinitions(surveyConfigId);
        if (langId <= 0 || defs == null) return defs;

        // replace text with right language
        // get intl resource map
        HashMap<Integer, SurveyIndicatorIntl> siiMap = new HashMap<Integer, SurveyIndicatorIntl>();
        HashMap<Integer, AtcChoiceIntl> atciMap = new HashMap<Integer, AtcChoiceIntl>();
        getTextMaps(surveyConfigId, langId, siiMap, atciMap);

        if (siiMap.isEmpty() && atciMap.isEmpty()) return defs;

        for (QuestionDef def : defs) {
            SurveyIndicatorIntl sii = siiMap.get(def.getIndicatorId());
            if (sii != null) {
                if (!StringUtils.isEmpty(sii.getQuestion())) def.setText(sii.getQuestion());
                if (!StringUtils.isEmpty(sii.getTip())) def.setTip(sii.getTip());
            }

            if (def.getType() == Constants.ANSWER_TYPE_SINGLE || def.getType() == Constants.ANSWER_TYPE_MULTI) {
                List<QuestionOption> options = def.getOptions();
                if (options != null) {
                    for (QuestionOption opt : options) {
                        AtcChoiceIntl atci = atciMap.get(opt.getId());
                        if (atci != null) {
                            if (!StringUtils.isEmpty(atci.getCriteria())) opt.setCriteria(atci.getCriteria());
                            if (!StringUtils.isEmpty(atci.getLabel())) opt.setLabel(atci.getLabel());
                        }
                    }
                }
            }
        }

        return defs;
    }


    @Autowired
    private void setIndicatordao(IndicatorDAO indicatorDao) {
        this.pubIndicatorDao = indicatorDao;
    }

    @Autowired
    private void setItagsdao(ItagsDAO itagsDao) {
        this.pubItagsDao = itagsDao;
    }

    @Autowired
    private void setSurveyQuestionDao(SurveyQuestionDAO dao) {
        this.qstDao = dao;
    }

    @Autowired
    private void setAtcChoiceDao(AtcChoiceDAO dao) {
        this.atcChoiceDao = dao;
    }

    @Autowired
    private void setAnswerTypeIntegerDao(AnswerTypeIntegerDAO dao) {
        this.atiDao = dao;
    }

    @Autowired
    private void setAnswerTypeFloatDao(AnswerTypeFloatDAO dao) {
        this.atfDao = dao;
    }

    @Autowired
    private void setAnswerTypeTextDao(AnswerTypeTextDAO dao) {
        this.attDao = dao;
    }
}
