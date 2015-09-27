/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.SurveyIndicatorIntl;
import com.ocs.util.StringUtils;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author luwb
 */
public class SurveyIndicatorIntlDAO extends SmartDaoMySqlImpl<SurveyIndicatorIntl, Integer> {

    private static final Logger log = Logger.getLogger(SurveyIndicatorIntlDAO.class);
    //private static final String SELECT_ALL_INDICATORS = "SELECT * FROM survey_indicator_intl WHERE language_id=?";

    private static final String SELECT_BY_INDICATOR_ID_AND_LANG_ID = "SELECT * FROM survey_indicator_intl "
            + "WHERE survey_indicator_id=? AND language_id=?";

    private static final String UPDATE_SURVEY_INDICATOR_INTL_BY_INDICATOR_ID_AND_LANG_ID = "UPDATE survey_indicator_intl SET question=?, tip=?,"
            + " WHERE survey_indicator_id=? AND language_id=?";
/*
    public List<SurveyIndicatorIntl> selectAllIndicators(int languageId) {
        return super.find(SELECT_ALL_INDICATORS, languageId);
    }
*/
    public SurveyIndicatorIntl findByIndicatorIdAndLanguage(int indicatorId, int languageId) {
        return super.findSingle(SELECT_BY_INDICATOR_ID_AND_LANG_ID, indicatorId, languageId);
    }

    public void updateByIndicatorIdAndLaunguage(String question, String tip, int indicatorId, int languageId) {
        Object[] values = {question, tip, indicatorId, languageId};
        super.update(UPDATE_SURVEY_INDICATOR_INTL_BY_INDICATOR_ID_AND_LANG_ID, values);
    }

    private static final String SELECT_BY_INDICATOR_IDS_AND_LANG_ID = "SELECT * FROM survey_indicator_intl "
            + "WHERE survey_indicator_id in ({0}) AND language_id=?";

    public List<SurveyIndicatorIntl> findByIndicatorIdsAndLanguage(List<Integer> indicatorIds, int languageId) {
        String sql = MessageFormat.format(SELECT_BY_INDICATOR_IDS_AND_LANG_ID, StringUtils.list2Str(indicatorIds));

        return super.find(sql, languageId);
    }


    private static final String SELECT_BY_CONFIG_ID_AND_LANG_ID =
            "SELECT sii.* FROM survey_indicator_intl sii, survey_indicator si, survey_question sq "
            + "WHERE sq.survey_config_id=? and si.id=sii.survey_indicator_id AND sq.survey_indicator_id=si.id "
            + "AND sii.language_id=?";

    public List<SurveyIndicatorIntl> findByConfigIdAndLanguage(int surveyConfigId, int languageId) {
        return super.find(SELECT_BY_CONFIG_ID_AND_LANG_ID, (long)surveyConfigId, languageId);
    }


    private static final String SELECT_COMPONENTS =
            "SELECT sii.* FROM survey_indicator_intl sii, survey_indicator si "
            + "WHERE si.parent_indicator_id = ? AND sii.survey_indicator_id = si.id AND sii.language_id = ?";

    public List<SurveyIndicatorIntl> findComponentIndicatorIntl(int parentIndicatorId, int languageId) {
        return super.find(SELECT_COMPONENTS, (long)parentIndicatorId, languageId);
    }

}
