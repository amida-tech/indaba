/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.TaskRole;
import com.ocs.indaba.util.ListUtils;
import com.ocs.util.StringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Jeff
 */
public class TaskRoleDAO extends SmartDaoMySqlImpl<TaskRole, Integer> {

    private static final Logger log = Logger.getLogger(TaskRoleDAO.class);
    public static final int TASK_ASSIGNMENT_METHOD_QUEUE = 2;
    private static final String SELECT_TASKROLES_BY_TASK_ID =
            "SELECT * FROM task_role WHERE task_id=?";
    private static final String DELETE_BY_TASK_ID =
            "DELETE FROM task_role WHERE task_id=?";
    private static final String EXISTS_BY_TASK_ID_AND_ROLE_ID =
            "SELECT * FROM task_role WHERE task_id=? AND role_id=?";
    private static final String EXISTS_BY_TASK_IDS_AND_ROLE_ID =
            "SELECT * FROM task_role WHERE task_id IN ({0}) AND role_id=?";

    public TaskRole getTaskRole(int taskId, int roleId) {
        return super.findSingle(EXISTS_BY_TASK_ID_AND_ROLE_ID, taskId, roleId);
    }

    public void deleteByTaskId(int taskId) {
        super.delete(DELETE_BY_TASK_ID, taskId);
    }

    public List<TaskRole> selectTaskRolesByTaskId(int taskId) {
        return super.find(SELECT_TASKROLES_BY_TASK_ID, taskId);
    }

    public Map<Integer, Integer> selectTaskRoleMap(List<Integer> taskIds, int roleId) {
        final Map<Integer, Integer> trMap = new HashMap<Integer, Integer>();
        if (taskIds == null || taskIds.isEmpty()) {
            return trMap;
        }
        RowMapper mapper = new RowMapper() {

            public Map<Integer, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
                trMap.put(rs.getInt("task_id"), rs.getInt("can_claim"));
                return trMap;
            }
        };
        getJdbcTemplate().query(MessageFormat.format(EXISTS_BY_TASK_IDS_AND_ROLE_ID, ListUtils.listToString(taskIds)), new Object[]{roleId}, mapper);
        return trMap;
    }
}
