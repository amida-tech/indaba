/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import static java.sql.Types.INTEGER;
import com.ocs.indaba.po.Pregoal;
import com.ocs.indaba.util.ResultSetUtil;
import com.ocs.indaba.vo.PreGoalView;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

public class PregoalDAO extends SmartDaoMySqlImpl<Pregoal, Integer> {

    private static final Logger log = Logger.getLogger(PregoalDAO.class);
    private static final String SELECT_PRE_GOALS =
            "SELECT pg.id id, pg.workflow_sequence_id, pg.pre_goal_id, g.workflow_sequence_id pre_workflow_sequence_id "
            + "FROM pregoal pg, goal g WHERE pg.workflow_sequence_id=? AND pg.pre_goal_id=g.id";

    public List<PreGoalView> selectPreGoals(int wfSeqId) {
        log.debug("Select pre-goals for workflow sequence id: " + wfSeqId);

        RowMapper mapper = new PreGoalViewRowMapper();

        return getJdbcTemplate().query(SELECT_PRE_GOALS,
                new Object[]{wfSeqId},
                new int[]{INTEGER},
                mapper);
    }
}

class PreGoalViewRowMapper implements RowMapper {

    public PreGoalView mapRow(ResultSet rs, int rowNum) throws SQLException {
        PreGoalView preGoal = new PreGoalView();
        preGoal.setId(ResultSetUtil.getInt(rs, "id"));
        preGoal.setWorkflowSequenceId(ResultSetUtil.getInt(rs, "workflow_sequence_id"));
        preGoal.setPreGoalId(ResultSetUtil.getInt(rs, "pre_goal_id"));
        preGoal.setPreWorkflowSequenceId(ResultSetUtil.getInt(rs, "pre_workflow_sequence_id"));

        return preGoal;
    }
}
