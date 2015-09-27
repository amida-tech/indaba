/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.po.ScContributor;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author yc06x
 */
public class ScContributorDAO extends SmartDaoMySqlImpl<ScContributor, Integer> {

    private static final Logger log = Logger.getLogger(ScContributorDAO.class);
    private static final String SELECT_PRIVATE_CONTRIBUTORS =
            "SELECT scc.* FROM sc_contributor scc JOIN survey_config sc ON scc.survey_config_id=sc.id WHERE sc.visibility=?";
    private static final String SELECT_CONTRIBUTORS_BY_SC_ID =
            "SELECT scc.* FROM sc_contributor scc JOIN survey_config sc ON scc.survey_config_id=sc.id WHERE sc.id=?";
    private static final String SELECT_ORG_IDS_BY_SC_ID =
            "SELECT org_id FROM sc_contributor WHERE survey_config_id=?";
    private static final String EXISTS_BY_SURVEY_CONFIGID_AND_ORGID =
            "SELECT COUNT(1) FROM sc_contributor WHERE survey_config_id=? AND org_id=?";
    private static final String DELETE_BY_SC_ID =
            "DELETE FROM sc_contributor WHERE survey_config_id=?";

    public boolean exists(int surveyConfigId, int orgId) {
        return super.count(EXISTS_BY_SURVEY_CONFIGID_AND_ORGID, surveyConfigId, orgId) > 0;
    }

    public List<ScContributor> selectContributorsByVisibility(int visibility) {
        return super.find(SELECT_PRIVATE_CONTRIBUTORS, visibility);
    }

    public List<ScContributor> selectContributorsBySurveyConfigId(int scId) {
        return super.find(SELECT_CONTRIBUTORS_BY_SC_ID, scId);
    }

    public List<Integer> selectOrgIdsBySurveyConfigId(int scId) {
        return super.getJdbcTemplate().query(SELECT_ORG_IDS_BY_SC_ID, new Object[]{scId}, super.getIdRowMapper("org_id"));
    }

    public void deleteByConfigId(int scId) {
        super.delete(DELETE_BY_SC_ID, scId);
    }
}
