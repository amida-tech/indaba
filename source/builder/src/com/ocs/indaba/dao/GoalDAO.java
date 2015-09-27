/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Goal;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff
 */
public class GoalDAO extends SmartDaoMySqlImpl<Goal, Integer> {

    private static final Logger log = Logger.getLogger(GoalDAO.class);
    private static final String SELECT_GOAL_BY_TASK_ID =
            "SELECT g.* FROM task t, goal g WHERE t.id=? AND t.goal_id=g.id";
    private static final String SELECT_GOAL_BY_GOAL_OBJECT_ID =
            "SELECT g.* FROM goal_object go, goal g WHERE go.id=? AND go.goal_id=g.id";
    private static final String SELECT_PREGOAL_LIST_BY_SO =
            "SELECT g.* FROM pregoal pg, goal g, sequence_object so "
            + "WHERE pg.workflow_sequence_id=so.workflow_sequence_id AND pg.pre_goal_id=g.id AND so.id=?";

    private static final String SELECT_GOALS_BY_WORKFLOW_ID =
            "SELECT g.* from goal g, workflow_sequence ws WHERE ws.workflow_id=? AND g.workflow_sequence_id=ws.id ORDER BY g.name";


    public Goal selectGoalByTaskId(int taskId) {
        log.debug("Select project by task id: " + taskId + ". [" + SELECT_GOAL_BY_TASK_ID + "].");
        return super.findSingle(SELECT_GOAL_BY_TASK_ID, taskId);
    }

    public Goal selectGoalByGoalObjectId(int goalObjId) {
        log.debug("Select project by goal id: " + goalObjId + ". [" + SELECT_GOAL_BY_GOAL_OBJECT_ID + "].");
        return super.findSingle(SELECT_GOAL_BY_GOAL_OBJECT_ID, goalObjId);
    }

    public List<Goal> selectPreGoalListBySO(int soId) {
        log.debug("selectPreGoalListBySO: (" + soId + ") [" + SELECT_PREGOAL_LIST_BY_SO + "]");
        return super.find(SELECT_PREGOAL_LIST_BY_SO, soId);
    }

    public List<Goal> selectGoalsByWorkflowId(int workflowId) {
        return super.find(SELECT_GOALS_BY_WORKFLOW_ID, workflowId);
    }
}
