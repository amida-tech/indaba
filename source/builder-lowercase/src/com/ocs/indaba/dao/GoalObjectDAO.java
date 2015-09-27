/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import java.text.MessageFormat;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.GoalObject;
import com.ocs.indaba.util.ListUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import static java.sql.Types.INTEGER;

/**
 *
 * @author Jeff
 */
public class GoalObjectDAO extends SmartDaoMySqlImpl<GoalObject, Integer> {

    private static final Logger log = Logger.getLogger(GoalObjectDAO.class);
    private static final String SELECT_GOALOBJECT_BY_ID =
            "SELECT * FROM goal_object WHERE id= ?";
    private static final String SELECT_GOALOBJECT_BY_SO_AND_STATUS =
            "SELECT goal_object.* FROM goal_object, goal WHERE sequence_object_id=? AND status=? AND goal_object.goal_id=goal.id ORDER BY goal.weight";
    private static final String SELECT_ACTIVE_GOALOBJECT_BY_SO =
            "SELECT * FROM goal_object WHERE (status=1 OR status=2) AND sequence_object_id=?";
    private static final String SELECT_GOALOBJECT_BY_GOAL_AND_SO =
            "SELECT * FROM goal_object WHERE goal_id=? AND sequence_object_id=?";
    private static final String SELECT_GOAL_OBJECTS_BY_WORKFLOW_OBJECT_ID =
            "SELECT go.* FROM workflow_object wo, sequence_object so, goal_object go "
            + "WHERE wo.id=? AND wo.id=so.workflow_object_id AND so.id=go.sequence_object_id AND go.status=?";

    private static final String SELECT_GOAL_OBJECT_BY_TASK_ASSIGNMENT_ID =
            "SELECT go.* FROM task_assignment ta, goal_object go WHERE ta.id=? AND go.id=ta.goal_object_id";

    private static final String SELECT_HORSE_START_TIME_BY_GOAL_OBJECT_ID =
            "SELECT h.start_time FROM horse h "
            + "JOIN workflow_object wo ON (h.workflow_object_id = wo.id) "
            + "JOIN sequence_object so ON (wo.id = so.workflow_object_id) "
            + "JOIN goal_object go ON (so.id = go.sequence_object_id) "
            + "WHERE go.id = ?";
    private static final String UPDATE_EVENT_LOG_ID_BY_GOAL_OBJECT_ID =
            "UPDATE goal_object go "
            + "SET go.event_log_id = ? "
            + "WHERE go.id = ?";
    private static final String SELECT_PRE_GOAL_OBJECT_BY_WORKFLOW_OBJECT =
            "SELECT go.* FROM pregoal pg, goal g, sequence_object so, goal_object go "
            + "WHERE pg.workflow_sequence_id=? AND pg.pre_goal_id=g.id AND go.sequence_object_id=so.id "
            + "AND g.id=go.goal_id AND so.workflow_sequence_id=? AND so.workflow_object_id=?";

    private static final String SELECT_GOAL_OBJECT_BY_GOAL_ID_AND_WORKFLOW_OBJECT =
            "SELECT go.*, g.id gid, wo.id woid " +
            "FROM goal_object go, goal g, workflow_object wo, sequence_object so " +
            "WHERE go.goal_id=g.id AND so.workflow_object_id=wo.id AND go.sequence_object_id=so.id " +
            "AND g.id IN ({0}) AND wo.id IN ({1})";

    public List<GoalObject> selectGoalObjectListByWorkflowObject(int pregoalSeqId, int soSeqId, int soWfObjId) {
        log.debug("selectGoalObjectListByWorkflowObject: (" + pregoalSeqId + "," + soSeqId + "," + soWfObjId
                + ") [" + SELECT_PRE_GOAL_OBJECT_BY_WORKFLOW_OBJECT + "]");
        return super.find(SELECT_PRE_GOAL_OBJECT_BY_WORKFLOW_OBJECT, (Object) pregoalSeqId, soSeqId, soWfObjId);
    }
    /*
    public GoalObject selectGoalObjectByGoalIdAndWorkflowObjectId(int goalId, int workflowObjectId) {
    log.debug("Get goal object by goalId[=" + goalId + "] and workflowObjectId[=" + workflowObjectId + "]: " + SELECT_GOAL_OBJECT_BY_GOAL_ID_AND_WORKFLOW_OBJECT);
    return super.findSingle(SELECT_GOAL_OBJECT_BY_GOAL_ID_AND_WORKFLOW_OBJECT, goalId, workflowObjectId);
    }
     */

    public Map<String, GoalObject> selectGoalObjectsByGoalAndWorkflowObjectIdMap(List<Integer> goalIds, List<Integer> workflowObjectIds) {
        final Map<String, GoalObject> goalObjectMap = new HashMap<String, GoalObject>();

        if (goalIds == null || goalIds.isEmpty() || workflowObjectIds == null || workflowObjectIds.isEmpty()) {
            return goalObjectMap;
        }

        RowMapper mapper = new RowMapper() {

            public Map<String, GoalObject> mapRow(ResultSet rs, int rowNum) throws SQLException {
                GoalObject goalObj = new GoalObject();
                goalObj.setId(rs.getInt("go.id"));
                goalObj.setGoalId(rs.getInt("go.goal_id"));
                goalObj.setStatus(rs.getShort("go.status"));
                int goalId = rs.getInt("gid");
                int wfoId = rs.getInt("woid");
                goalObjectMap.put(goalId + "-" + wfoId, goalObj);
                return goalObjectMap;
            }
        };

        String sqlStr = MessageFormat.format(SELECT_GOAL_OBJECT_BY_GOAL_ID_AND_WORKFLOW_OBJECT, 
                ListUtils.listToString(goalIds), ListUtils.listToString(workflowObjectIds));

        logger.debug(sqlStr);
        getJdbcTemplate().query(sqlStr, mapper);
        return goalObjectMap;
    }

    public GoalObject selectGoalObjectsByGoalIdAndWorkflowObjectId(int goalId, int wfoId) {       
        return super.findSingle(MessageFormat.format(SELECT_GOAL_OBJECT_BY_GOAL_ID_AND_WORKFLOW_OBJECT, ""+goalId, ""+wfoId));
    }

    public Date selectHorseStartTimeByGoalObjectId(int goalObjectId) {
        RowMapper mapper = new RowMapper() {

            public Date mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getDate("start_time");
            }
        };

        List<Date> retVal = getJdbcTemplate().query(
                SELECT_HORSE_START_TIME_BY_GOAL_OBJECT_ID,
                new Object[]{goalObjectId},
                new int[]{INTEGER},
                mapper);
        return (retVal.isEmpty()) ? null : retVal.get(0);
    }

    public GoalObject selectGoalObjectById(int id) {
        log.debug("Select GoalObject by id: " + id + ". [" + SELECT_GOALOBJECT_BY_ID + "].");
        return super.findSingle(SELECT_GOALOBJECT_BY_ID, id);
    }

    public GoalObject selectActiveGoalObjectBySo(int so_id) {
        return super.findSingle(SELECT_ACTIVE_GOALOBJECT_BY_SO, so_id);
    }

    public GoalObject selectGoalObjectBySoAndStatus(int so_id, int status) {
        log.debug("Select Active GoalObject by SO id: " + so_id + ". [" + SELECT_GOALOBJECT_BY_SO_AND_STATUS + "].");
        return super.findSingle(SELECT_GOALOBJECT_BY_SO_AND_STATUS, so_id, status);
    }

    public List<GoalObject> selectGoalObjectListBySoAndStatus(int so_id, int status) {
        log.debug("Select Active GoalObject List by SO id: " + so_id + ". [" + SELECT_GOALOBJECT_BY_SO_AND_STATUS + "].");
        return super.find(SELECT_GOALOBJECT_BY_SO_AND_STATUS, (Object) so_id, status);
    }

    public GoalObject selectGoalObjectByGoalAndSO(int goal_id, int so_id) {
        return super.findSingle(SELECT_GOALOBJECT_BY_GOAL_AND_SO, goal_id, so_id);
    }

    public List<GoalObject> selectActiveGoalObjectsByWorkflowObjectId(int workflowObjectId) {
        log.debug("Select active goal bojects by workflow object id: " + workflowObjectId + ". [" + SELECT_GOAL_OBJECTS_BY_WORKFLOW_OBJECT_ID + "].");

        return super.find(SELECT_GOAL_OBJECTS_BY_WORKFLOW_OBJECT_ID, (Object) workflowObjectId, Constants.GOAL_OBJECT_STATUS_STARTED);
    }

    public GoalObject selectGoalObjectByTaskAssignmentId(int taskAssignmentId) {
        log.debug("Select goal boject by task assignment id: " + taskAssignmentId + ". [" + SELECT_GOAL_OBJECT_BY_TASK_ASSIGNMENT_ID + "].");

        return super.findSingle(SELECT_GOAL_OBJECT_BY_TASK_ASSIGNMENT_ID, taskAssignmentId);
    }
    private static final String UPDATE_STATUS = "update goal_object go set go.status=? where id=?";

    public void updateStatus(GoalObject go) {
        logger.debug("Update Goal Object status");

        Object[] values = new Object[]{go.getStatus(), go.getId()};
        this.getJdbcTemplate().update(this.UPDATE_STATUS, values);
    }

    public void updateEventLogId(GoalObject go) {
        logger.debug("Update Goal Object event_log_id:" + go.getEventLogId());
        getJdbcTemplate().update(UPDATE_EVENT_LOG_ID_BY_GOAL_OBJECT_ID,
                new Object[]{go.getEventLogId(), go.getId()});
    }


    private static final String SELECT_ACTIVE_GOAL_OBJECTS_BY_HORSE =
            "SELECT go.* FROM goal_object go, sequence_object so, workflow_object wo, horse h " +
            "WHERE h.id=? AND wo.id=h.workflow_object_id AND so.workflow_object_id=wo.id AND go.sequence_object_id=so.id " +
            "AND wo.status != 5 AND wo.status != 3 AND go.status != 3 " +
            "ORDER BY go.status DESC";

    public List<GoalObject> getActiveGoalObjectsOfHorse(int horseId) {
        return super.find(SELECT_ACTIVE_GOAL_OBJECTS_BY_HORSE, horseId);
    }
}
