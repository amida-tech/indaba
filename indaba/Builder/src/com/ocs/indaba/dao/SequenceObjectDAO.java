/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Types.INTEGER;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.SequenceObject;
import com.ocs.indaba.vo.GoalObjectView;

/**
 * Operation on User table by accessing DB
 * 
 * @author Jeff
 * 
 */
public class SequenceObjectDAO extends SmartDaoMySqlImpl<SequenceObject, Integer> {

    private static final Logger log = Logger.getLogger(SequenceObjectDAO.class);
    private static final String SELECT_SEQUENCE_OBJECTS_BY_WORKFLOW_OBJECT_ID = "SELECT * FROM sequence_object WHERE workflow_object_id=? ORDER BY workflow_sequence_id";
    private static final String SELECT_GOAL_OBJECTS_BY_SEQUENCE_OBJECT_ID =
            "SELECT seqobj.id, seqobj.workflow_sequence_id, g.duration, gobj.enter_time, gobj.exit_time, "
            + "gobj.id goal_object_id, g.id goal_id, g.name goal_name, g.weight weight, gobj.status "
            + "FROM goal_object gobj, goal g, sequence_object seqobj "
            + "WHERE seqobj.id=? AND g.workflow_sequence_id=seqobj.workflow_sequence_id AND "
            + "gobj.sequence_object_id=seqobj.id AND g.id=gobj.goal_id ORDER BY g.weight";
    private static final String SELECT_PREGOAL_ID_BY_SEQUENCE_OBJECT_ID =
            "SELECT pre_goal_id "
            + "FROM pregoal "
            + "WHERE workflow_sequence_id = ?";

    /**
     * Select all duration of the specified horse id.
     * @param horseId
     * @return duration days
     */
    public List<GoalObjectView> selectGoalObjectsBySequenceObjectId(int sequenceObjectId) {
        log.debug("Select table sequence_object: " + SELECT_GOAL_OBJECTS_BY_SEQUENCE_OBJECT_ID + "[sequenceObjectId=" + sequenceObjectId + "].");

        RowMapper mapper = new GoalObjectViewRowMapper();
        List<GoalObjectView> list = getJdbcTemplate().query(SELECT_GOAL_OBJECTS_BY_SEQUENCE_OBJECT_ID,
                new Object[]{sequenceObjectId},
                new int[]{INTEGER},
                mapper);
        return list;
    }

    /**
     * Select all duration of the specified horse id.
     * @param horseId
     * @return duration days
     */
    public List<SequenceObject> selectSequenceObjectsByWorkflowObjectId(int workflowObjectId) {
        log.debug("Select table sequence object: " + SELECT_SEQUENCE_OBJECTS_BY_WORKFLOW_OBJECT_ID
                + "[workflowObjectId=" + workflowObjectId + "].");

        return super.find(SELECT_SEQUENCE_OBJECTS_BY_WORKFLOW_OBJECT_ID, workflowObjectId);
    }

    /**
     * Select all  of the specified horse id.
     * @param horseId
     * @return Pregoals Id
     */
    public List<Integer> selectPregoalIdBySequenceObjectId(int sequenceObjectId) {
        log.debug("Select table sequence_object: " + SELECT_PREGOAL_ID_BY_SEQUENCE_OBJECT_ID + "[sequenceObjectId=" + sequenceObjectId + "].");

        RowMapper mapper = new RowMapper(){
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("pre_goal_id");
            }
        };
        List<Integer> list = getJdbcTemplate().query(SELECT_PREGOAL_ID_BY_SEQUENCE_OBJECT_ID,
                new Object[]{sequenceObjectId},
                new int[]{INTEGER},
                mapper);
        return list;
    }
}

class GoalObjectViewRowMapper implements RowMapper {

    public GoalObjectView mapRow(ResultSet rs, int rowNum) throws SQLException {
        GoalObjectView goalObjView = new GoalObjectView();
        goalObjView.setGoalId(rs.getInt("goal_id"));
        goalObjView.setGoalObjId(rs.getInt("goal_object_id"));
        goalObjView.setGoalName(rs.getString("goal_name"));
        goalObjView.setWeight(rs.getInt("weight"));
        goalObjView.setDuration(rs.getInt("duration"));
        goalObjView.setStatus(rs.getInt("status"));
        goalObjView.setEnterTime(rs.getDate("enter_time"));
        goalObjView.setExitTime(rs.getDate("exit_time"));
        return goalObjView;
    }
}



