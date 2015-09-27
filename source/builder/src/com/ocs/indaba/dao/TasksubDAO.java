/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;

import com.ocs.indaba.po.Tasksub;
import com.ocs.indaba.util.ListUtils;
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
public class TasksubDAO extends SmartDaoMySqlImpl<Tasksub, Integer> {

    private static final Logger log = Logger.getLogger(TasksubDAO.class);
    private static final String SELECT_BY_TASK_IDS_AND_USER_ID =
            "SELECT * FROM tasksub WHERE task_id IN ({0}) AND user_id=?";
    private static final String SELECT_BY_TASK_ID_AND_USER_ID =
            "SELECT * FROM tasksub WHERE task_id=? AND user_id=?";

    public Tasksub selectTasksub(int taskId, int userId) {
        return super.findSingle(SELECT_BY_TASK_ID_AND_USER_ID, taskId, userId);
    }

    public Map<Integer, Integer> selectTasksubMap(List<Integer> taskIds, int userId) {
        final Map<Integer, Integer> trMap = new HashMap<Integer, Integer>();
        if (taskIds == null || taskIds.isEmpty()) {
            return trMap;
        }
        RowMapper mapper = new RowMapper() {

            public Map<Integer, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
                trMap.put(rs.getInt("task_id"), rs.getInt("notify"));
                return trMap;
            }
        };
        getJdbcTemplate().query(MessageFormat.format(SELECT_BY_TASK_IDS_AND_USER_ID, ListUtils.listToString(taskIds)), new Object[]{userId}, mapper);
        return trMap;
    }
}
