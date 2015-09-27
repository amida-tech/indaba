/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.aggregation.po.Scorecard;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author luwb
 */
public class ScorecardDAO extends SmartDaoMySqlImpl<Scorecard, Integer>{
    private static final Logger log = Logger.getLogger(ScorecardDAO.class);
    private static final String SELECT_SCORECARD_BY_PROJECT_AND_TARGET =
            "SELECT * FROM scorecard WHERE project_id=? AND target_id=?";

    private static final String SELECT_SCORECARD_BY_PROJECT_AND_TARGER_AND_STUDY_PERIOD =
            "SELECT * FROM scorecard WHERE study_period_id=? AND target_id=?  AND status>=? AND project_id IN ({0})";

    private static final String DELETE_ALL_SCORECARD = "TRUNCATE scorecard";

    public List<Scorecard> selectScorecardByProjectAndTarget(int projectId, int targetId){
        log.debug("select scorecard by project id:" + projectId + " target id:" + targetId);
        return super.find(SELECT_SCORECARD_BY_PROJECT_AND_TARGET, new Object[]{projectId, targetId});
    }

    public List<Scorecard> selectScorecardByProjectTargetAndStudyPeriod(int studyPeriodId, int targetId, List<Integer> projectIds, int status){
        String ids = projectIds.toString();
        String sqlStr = MessageFormat.format(SELECT_SCORECARD_BY_PROJECT_AND_TARGER_AND_STUDY_PERIOD, ids.substring(1, ids.length() - 1));
        return super.find(sqlStr, new Object[]{studyPeriodId, targetId, status});
    }

    public void deleteAll() {
        log.debug("delete all scorecard data.");
        super.delete(DELETE_ALL_SCORECARD);
    }
}
