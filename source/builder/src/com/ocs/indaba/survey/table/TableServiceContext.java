/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.survey.table;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.AnswerTypeChoiceDAO;
import com.ocs.indaba.dao.AnswerTypeFloatDAO;
import com.ocs.indaba.dao.AnswerTypeIntegerDAO;
import com.ocs.indaba.dao.AnswerTypeTableDAO;
import com.ocs.indaba.dao.AnswerTypeTextDAO;
import com.ocs.indaba.dao.SurveyIndicatorDAO;
import com.ocs.indaba.dao.SurveyIndicatorIntlDAO;
import com.ocs.indaba.po.AnswerTypeTable;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.po.SurveyIndicatorIntl;
import com.ocs.indaba.service.SurveyService;
import com.ocs.indaba.vo.SurveyAnswerObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author yc06x
 */
public class TableServiceContext {

    private static final Logger logger = Logger.getLogger(TableServiceContext.class);

    private int mainSurveyQuestionId;
    private int langId;
    private List<List<Block>> blockGrid;
    private Map<Integer, IndicatorInfo> indicatorMap;
    private String error;

    static private AnswerTypeTableDAO atTableDao = null;
    static private AnswerTypeIntegerDAO atIntegerDao = null;
    static private AnswerTypeFloatDAO atFloatDao = null;
    static private AnswerTypeChoiceDAO atChoiceDao = null;
    static private AnswerTypeTextDAO atTextDao = null;
    static private SurveyIndicatorDAO surveyIndicatorDao = null;
    static private SurveyService surveyService = null;
    static private SurveyIndicatorIntlDAO surveyIndicatorIntlDao = null;

    static public void setDAOs(
            SurveyService surveyService,
            AnswerTypeTableDAO atTableDao,
            AnswerTypeIntegerDAO atIntegerDao,
            AnswerTypeFloatDAO atFloatDao,
            AnswerTypeChoiceDAO atChoiceDao,
            AnswerTypeTextDAO atTextDao,
            SurveyIndicatorDAO surveyIndicatorDao,
            SurveyIndicatorIntlDAO surveyIndicatorIntlDao) {
        TableServiceContext.surveyService = surveyService;
        TableServiceContext.atTableDao = atTableDao;
        TableServiceContext.atIntegerDao = atIntegerDao;
        TableServiceContext.atFloatDao = atFloatDao;
        TableServiceContext.atChoiceDao = atChoiceDao;
        TableServiceContext.atTextDao = atTextDao;
        TableServiceContext.surveyIndicatorDao = surveyIndicatorDao;
        TableServiceContext.surveyIndicatorIntlDao = surveyIndicatorIntlDao;
    }

    public TableServiceContext(int mainSurveyQuestionId, int langId) {
        this.mainSurveyQuestionId = mainSurveyQuestionId;
        this.langId = langId;

        SurveyIndicator mainIndicator = surveyService.selectSurveyIndicatorByQuesitonId(mainSurveyQuestionId, langId);

        if (mainIndicator == null) {
            error = "Can't find main indicator for survey question " + mainSurveyQuestionId;
            logger.error (error);
            return;
        }

        if (mainIndicator.getAnswerType() != Constants.SURVEY_ANSWER_TYPE_TABLE) {
            error = "Indicator " + mainIndicator.getId() + " of question " + mainSurveyQuestionId + " is not TABLE type.";
            logger.error (error);
            return;
        }

        AnswerTypeTable tableDef = atTableDao.get(mainIndicator.getAnswerTypeId());
        blockGrid = Block.fromJson(tableDef.getData());

        if (blockGrid == null) {
            error = "Indicator " + mainIndicator.getId() + " has no table definition.";
            logger.error (error);
            return;
        }

        makeIndicatorInfoMap(mainIndicator.getId(), langId);

    }

    private void makeIndicatorInfoMap(int mainIndicatorId, int langId) {
        List<SurveyIndicator> componentIndicators = surveyIndicatorDao.selectComponentIndicators(mainIndicatorId);

        if (componentIndicators == null) {
            error = "Table indicator " + mainIndicatorId + " has no components!";
            logger.error (error);
            return;
        }

        indicatorMap = new HashMap<Integer, IndicatorInfo>();

        for (SurveyIndicator ind : componentIndicators) {
            IndicatorInfo info = new IndicatorInfo();
            info.setIndicator(ind);
            info.setAnswerObject(null);
            indicatorMap.put(ind.getId(), info);
            Object answerTypeObj = null;

            switch (ind.getAnswerType()) {
                case Constants.SURVEY_ANSWER_TYPE_FLOAT:
                    answerTypeObj = atFloatDao.get(ind.getAnswerTypeId());
                    break;

                case Constants.SURVEY_ANSWER_TYPE_INTEGER:
                    answerTypeObj = atIntegerDao.get(ind.getAnswerTypeId());
                    break;

                case Constants.SURVEY_ANSWER_TYPE_TEXT:
                    answerTypeObj = atTextDao.get(ind.getAnswerTypeId());
                    break;

                case Constants.SURVEY_ANSWER_TYPE_SINGLE_CHOICE:
                case Constants.SURVEY_ANSWER_TYPE_MULTI_CHOICE:
                    answerTypeObj = atChoiceDao.getDefinition(ind.getAnswerTypeId(), langId);
                    break;

                default:
                    break;
            }
            info.setAnswerTypeObject(answerTypeObj);
        }

        // Try to set the best language for component indicators
        List<SurveyIndicatorIntl> compIntls = surveyIndicatorIntlDao.findComponentIndicatorIntl(mainIndicatorId, langId);
        if (compIntls != null) {
            for (SurveyIndicatorIntl intl : compIntls) {
                IndicatorInfo info = indicatorMap.get(intl.getSurveyIndicatorId());
                if (info == null) continue;
                SurveyIndicator ind = info.getIndicator();
                ind.setTip(intl.getTip());
                ind.setQuestion(intl.getQuestion());
            }
        }
    }


    public IndicatorInfo getIndicatorInfo(int indicatorId) {
        if (indicatorMap == null) return null;
        return indicatorMap.get(indicatorId);
    }


    public void setAnswerObject(SurveyAnswerObject obj) {
        IndicatorInfo info = getIndicatorInfo(obj.getIndicatorId());
        if (info != null) info.setAnswerObject(obj);
    }


    public void clearAnswerObjects() {
        if (indicatorMap == null) return;

        Iterator iterator = indicatorMap.entrySet().iterator();
	while (iterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) iterator.next();
            IndicatorInfo info = (IndicatorInfo)mapEntry.getValue();
            info.setAnswerObject(null);
        }
    }


    public List<List<Block>> getBlockGrid() {
        return blockGrid;
    }


    public String getError() {
        return error;
    }

    public int getLanguageId() {
        return this.langId;
    }


    public int getMainSurveyQuestionId() {
        return this.mainSurveyQuestionId;
    }

}
