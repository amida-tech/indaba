/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.aggregation.po.Scorecard;
import com.ocs.indaba.aggregation.po.ScorecardA;
import com.ocs.indaba.aggregation.vo.ScorecardHelper;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author luwb
 */
public class ScorecardADAO extends SmartDaoMySqlImpl<ScorecardA, Integer> {

    private static final Logger log = Logger.getLogger(ScorecardADAO.class);
    private static final String SELECT_SCORECARD_BY_PROJECT_AND_TARGET =
            "SELECT * FROM scorecard_a WHERE project_id=? AND target_id=?";
    private static final String SELECT_SCORECARD_BY_PROJECT_AND_TARGER_AND_STUDY_PERIOD =
            "SELECT * FROM scorecard_a WHERE study_period_id=? AND target_id=?  AND status>=? AND project_id IN ({0})";
    private static final String DELETE_ALL_SCORECARD = "TRUNCATE scorecard_a";
    private static final String DELETE_TDS_VALUE = "TRUNCATE tds_value_a";
    private static final String DELETE_OTIS_VALUE = "TRUNCATE otis_value_a";

    public List<Scorecard> selectScorecardByProjectAndTarget(int projectId, int targetId) {
        log.debug("select scorecard by project id:" + projectId + " target id:" + targetId);
        List<ScorecardA> listA = super.find(SELECT_SCORECARD_BY_PROJECT_AND_TARGET, new Object[]{projectId, targetId});
        List<Scorecard> list = new ArrayList<Scorecard>();
        if (listA != null) {
            for (ScorecardA scA : listA) {
                list.add(ScorecardHelper.scA2Sc(scA));
            }
        }
        return list;
    }

    public List<Scorecard> selectScorecardByProjectTargetAndStudyPeriod(int studyPeriodId, int targetId, List<Integer> projectIds, int status) {
        String ids = projectIds.toString();
        String sqlStr = MessageFormat.format(SELECT_SCORECARD_BY_PROJECT_AND_TARGER_AND_STUDY_PERIOD, ids.substring(1, ids.length() - 1));
        List<ScorecardA> listA = super.find(sqlStr, new Object[]{studyPeriodId, targetId, status});
        List<Scorecard> list = new ArrayList<Scorecard>();
        if (listA != null) {
            for (ScorecardA scA : listA) {
                list.add(ScorecardHelper.scA2Sc(scA));
            }
        }
        return list;
    }

    public void deleteAll() {
        log.debug("delete all scorecard_a data and tds_value_a and otis_value_a.");
        super.delete(DELETE_ALL_SCORECARD);
        super.delete(DELETE_TDS_VALUE);
        super.delete(DELETE_OTIS_VALUE);

    }
}
