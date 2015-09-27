/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Workflow;
import com.ocs.indaba.vo.WorkflowView;
import com.ocs.indaba.vo.WorkflowObjectView;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Luke
 */
public class WorkflowDAO extends SmartDaoMySqlImpl<Workflow, Integer> {

    private static final Logger logger = Logger.getLogger(WorkflowDAO.class);
    private static final String SELECT_WORKFLOW_VIEW_LIST =
            "SELECT wfo.id, wf.name, wfo.status, wfs.name, so.status, g.name, go.status, task.task_name, ta.status, target.name, ta.assigned_user_id "
            + "FROM workflow wf, workflow_object wfo, workflow_sequence wfs, goal g, goal_object go, sequence_object so, task_assignment ta, task, target "
            + "WHERE wf.id=wfo.workflow_id AND wfs.workflow_id=wf.id AND so.workflow_object_id=wfo.id "
            + "AND so.workflow_sequence_id=wfs.id AND g.workflow_sequence_id=wfs.id AND go.goal_id=g.id "
            + "AND go.sequence_object_id=so.id AND go.id=ta.goal_object_id AND task.id=ta.task_id AND ta.target_id=target.id "
            + "ORDER BY wf.id, wfo.id";
    private static final String SELECT_ALL_WORKFLOWS =
            "SELECT * FROM workflow ORDER BY name";

    public List<Workflow> selectAllWorkflows() {
        return super.find(SELECT_ALL_WORKFLOWS);
    }

    public List<WorkflowView> getWorkflowViewList() {
        RowMapper mapper = new RowMapper() {

            public WorkflowView mapRow(ResultSet rs, int i) throws SQLException {
                WorkflowView workflowView = new WorkflowView();
                workflowView.setWfoId(rs.getInt("wfo.id"));
                workflowView.setWorkflowStatus(rs.getInt("wfo.status"));
                workflowView.setSequenceStatus(rs.getInt("so.status"));
                workflowView.setGoalStatus(rs.getInt("go.status"));
                workflowView.setWorkflowName(rs.getString("wf.name"));
                workflowView.setWorkflowSequenceName(rs.getString("wfs.name"));
                workflowView.setGoalName(rs.getString("g.name"));
                workflowView.setTaskName(rs.getString("task.task_name"));
                workflowView.setTaskStatus(rs.getInt("ta.status"));
                workflowView.setTargetName(rs.getString("target.name"));
                workflowView.setAssignedUserId(rs.getInt("ta.assigned_user_id"));
                return workflowView;
            }
        };

        List<WorkflowView> list = getJdbcTemplate().query(SELECT_WORKFLOW_VIEW_LIST,
                new Object[]{},
                new int[]{},
                mapper);
        return list;
    }
    private static final String SELECT_WORKFLOW_OBJECT_VIEW_LIST =
            "SELECT wfo.id, wf.name, wfo.status, wfo.start_time, wf.id, target.name "
            + "FROM workflow wf, workflow_object wfo, horse, target "
            + "WHERE wfo.workflow_id=wf.id AND wfo.id=horse.workflow_object_id AND horse.target_id=target.id "
            + "ORDER BY wf.id, wfo.id";

    public List<WorkflowObjectView> getWorkflowObjectViewList() {
        RowMapper mapper = new RowMapper() {

            public WorkflowObjectView mapRow(ResultSet rs, int i) throws SQLException {
                WorkflowObjectView workflowObjView = new WorkflowObjectView();
                workflowObjView.setWfoId(rs.getInt("wfo.id"));
                workflowObjView.setWorkflowId(rs.getInt("wf.id"));
                workflowObjView.setStatus(rs.getInt("wfo.status"));
                workflowObjView.setWorkflowName(rs.getString("wf.name"));
                workflowObjView.setStartTime(rs.getDate("wfo.start_time"));
                workflowObjView.setTargetName(rs.getString("target.name"));
                return workflowObjView;
            }
        };

        List<WorkflowObjectView> list = getJdbcTemplate().query(SELECT_WORKFLOW_OBJECT_VIEW_LIST,
                new Object[]{},
                new int[]{},
                mapper);
        return list;
    }
}
