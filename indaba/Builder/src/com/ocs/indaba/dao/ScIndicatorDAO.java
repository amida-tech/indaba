/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.po.ScIndicator;
import com.ocs.util.ListUtils;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author yc06x
 */
public class ScIndicatorDAO extends SmartDaoMySqlImpl<ScIndicator, Integer> {

    private static final Logger log = Logger.getLogger(ScIndicatorDAO.class);
    private static final String SELECT_PRIVATE_INDICATORS =
            "SELECT sci.* FROM sc_indicator sci JOIN survey_config sc ON sci.survey_config_id=sc.id WHERE sc.visibility=?";
    private static final String SELECT_INDICATORS_BY_SC_ID =
            "SELECT sci.* FROM sc_indicator sci JOIN survey_config sc ON sci.survey_config_id=sc.id WHERE sc.id=?";
    private static final String SELECT_INDICATOR_IDS_BY_SC_ID =
            "SELECT survey_indicator_id FROM sc_indicator WHERE survey_config_id=?";
    private static final String EXISTS_BY_SURVEY_CONFIGID_AND_INDICATORID =
            "SELECT COUNT(1) FROM sc_indicator WHERE survey_config_id=? AND org_id=?";
    private static final String DELETE_INDICATOR_IDS_BY_SC_ID =
            "DELETE FROM sc_indicator WHERE survey_config_id=?";
    private static final String DELETE_INDICATOR_IDS_BY_SC_ID_AND_INDICATORIDS =
            "DELETE FROM sc_indicator WHERE survey_config_id=? AND survey_indicator_id IN ({0})";

    public boolean exists(int surveyConfigId, int indicatorId) {
        return super.count(EXISTS_BY_SURVEY_CONFIGID_AND_INDICATORID, surveyConfigId, indicatorId) > 0;
    }

    public List<ScIndicator> selectIndicatorsByVisibility(int visibility) {
        return super.find(SELECT_PRIVATE_INDICATORS, visibility);
    }

    public List<ScIndicator> selectIndicatorsBySurveyConfigId(int scId) {
        return super.find(SELECT_INDICATORS_BY_SC_ID, scId);
    }

    public List<Integer> selectIndicatorIdsBySurveyConfigId(int scId) {
        return super.getJdbcTemplate().query(SELECT_INDICATOR_IDS_BY_SC_ID, new Object[]{scId}, super.getIdRowMapper("survey_indicator_id"));
    }

    public void deleteByConfigId(int scId) {
        super.delete(DELETE_INDICATOR_IDS_BY_SC_ID, scId);
    }
    
    public void deleteByConfigIdAndIndicatorIds(int scId, List<Integer> siIds) {
        super.delete(MessageFormat.format(DELETE_INDICATOR_IDS_BY_SC_ID_AND_INDICATORIDS, ListUtils.listToString(siIds)), scId);
    }
}
