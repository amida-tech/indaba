/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Tool;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Jeff
 */
public class ToolDAO extends SmartDaoMySqlImpl<Tool, Integer> {

    private static final Logger log = Logger.getLogger(ToolDAO.class);
    private static final String SELECT_TOOL_BY_NAME =
            "SELECT * FROM tool WHERE name=?";
    private static final String SELECT_TOOL_BY_TASK_ASSIGNMENT_ID =
            "SELECT * FROM tool "
            + "JOIN task ON (task.tool_id = tool.id) "
            + "JOIN task_assignment ta ON (ta.task_id = task.id) "
            + "WHERE ta.id = ?";
    private static final String SELECT_TOOLS_OF_JOURNAL =
            "SELECT * FROM tool WHERE (content_types & 0x2 = 0x2) ORDER BY name";
    private static final String SELECT_TOOLS_OF_SURVEY_WITH_TSC =
            "SELECT * FROM tool WHERE (content_types & 0x1 = 0x1) ORDER BY name";
    private static final String SELECT_TOOLS_OF_SURVEY_WITHOUT_TSC =
            "SELECT * FROM tool WHERE (content_types & 0x1 = 0x1) AND (bsc_compatible=1) ORDER BY name";
    private static final String SELECT_TOOLID_MULTI_USER_MAP =
            "SELECT id, multi_user FROM tool";

    public Map<Integer, Integer> selectToolMultiUserMap() {
        final Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        RowMapper mapper = new RowMapper() {

            public Map<Integer, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
                map.put(rs.getInt("id"), rs.getInt("multi_user"));
                return map;
            }
        };
        getJdbcTemplate().query(SELECT_TOOLID_MULTI_USER_MAP,mapper);
        return map;
    }

    public List<Tool> selectToolsOfJournal() {
        return super.find(SELECT_TOOLS_OF_JOURNAL);
    }

    public List<Tool> selectToolsOfSurvey(boolean isTsc) {
        return isTsc ? super.find(SELECT_TOOLS_OF_SURVEY_WITH_TSC) : super.find(SELECT_TOOLS_OF_SURVEY_WITHOUT_TSC);
    }

    public Tool selectToolById(int toolId) {
        log.debug("Select tool by id: " + toolId + ".");
        return super.get(toolId);
    }

    public Tool selectToolByName(String toolName) {
        log.debug("Select tool by name: " + toolName + ".");
        return super.findSingle(SELECT_TOOL_BY_NAME, toolName);
    }

    public List<Tool> selectTools() {
        log.debug("Select all of the tools. ");
        return super.findAll();
    }

    public Tool selectToolByTaskAssignmentId(int taskAssignmentId) {
        return findSingle(SELECT_TOOL_BY_TASK_ASSIGNMENT_ID, taskAssignmentId);
    }
}
