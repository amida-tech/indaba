/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.vo.TaskAssignmentVO;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.GoalObject;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.indaba.util.ListUtils;
import com.ocs.indaba.util.ResultSetUtil;
import com.ocs.indaba.vo.TaskAssignmentAlertInfo;
import com.ocs.util.DateUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import static java.sql.Types.INTEGER;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;

/**
 *
 * @author luwb
 */
public class TaskAssignmentDAO extends SmartDaoMySqlImpl<TaskAssignment, Integer> {

    private static final Logger log = Logger.getLogger(TaskAssignmentDAO.class);
    private final String SELECT_QUEUE_TASKASSIGNMENT_BY_TASK_ID =
            "SELECT * from task_assignment where task_id=? order by q_priority desc";
    private final String SELECT_QUEUE_TASKASSIGNMENT_BY_ID =
            "SELECT * from task_assignment where id=?";
    private final String SELECT_TASKASSIGNMENT_BY_USER_ID =
            "SELECT * from task_assignment where assigned_user_id=? and task_id=?";
    private final String SELECT_TASKASSIGNMENT_BY_USER_TASK_TARGET =
            "SELECT * from task_assignment WHERE assigned_user_id=? AND task_id=? AND target_id=?";
    private final String SELECT_TASKASSIGNMENT_BY_USER_TASK_HORSE =
            "SELECT * from task_assignment WHERE assigned_user_id=? AND task_id=? AND horse_id=?";
    private final String SELECT_TASKASSIGNMENT_BY_TASK_HORSE =
            "SELECT * from task_assignment WHERE task_id=? AND horse_id=?";
    private final String SELECT_TASKASSIGNMENT_BY_TASK_TARGET =
            "SELECT COUNT(1) from task_assignment WHERE task_id=? AND target_id=?";
    private final String UPDATE_TASKASSIGNMENT_STATUS =
            "UPDATE task_assignment SET status=? WHERE id=?";
    private final String UPDATE_TASKASSIGNMENT_PERCENTAGE =
            "UPDATE task_assignment SET percent=? WHERE id=?";
    private final String UPDATE_TASKASSIGNMENT_STATUS_AND_PERCENTAGE =
            "UPDATE task_assignment SET status=?, percent=? WHERE id=?";
    private final String UPDATE_TASKASSIGNMENT_COMPLETION_TIME =
            "UPDATE task_assignment SET completion_time=? WHERE id=? AND completion_time IS NULL";
    private final String UPDATE_TASKASSIGNMENT_EVENT_LOG_ID =
            "UPDATE task_assignment SET event_log_id=? WHERE id=?";
    private final String UPDATE_APPLY_STAT_BY_ID =
            "UPDATE task_assignment tr set tr.q_last_assigned_uid=tr.assigned_user_id, tr.assigned_user_id=?, tr.q_last_assigned_time=now() "
            + "where tr.id=?";
    private final String UPDATE_RETURN_STAT_BY_ID =
            "UPDATE task_assignment tr set tr.assigned_user_id=0, tr.q_last_assigned_uid=?, "
            + "tr.q_last_return_time=now() where tr.id=?";
    private final String UPDATE_ASSIGN_AND_PRIORITY_BY_ID =
            "UPDATE task_assignment tr set tr.q_last_assigned_uid=tr.assigned_user_id, tr.assigned_user_id=?,  tr.q_last_assigned_time=now(), "
            + "tr.q_priority=? where tr.id=?";
    private final String UPDATE_PRIORITY_BY_ID =
            "UPDATE task_assignment tr set tr.q_priority=? where tr.id=?";
    private final String SELECT_INCOMPLETE_TASKASSIGNMENT_BY_GOALOBJECT_ID =
            "SELECT * FROM task_assignment WHERE goal_object_id=? AND status!='5' ";
    private final String SELECT_TASKASSIGNMENTS_BY_TASK_AND_GOALOBJECT =
            "SELECT task_assignment.* FROM task_assignment "
            + "WHERE task_id=? AND goal_object_id=?";
    private final String SELECT_MAX_DUE_TIME_BY_GOALOBJECTID =
            "SELECT MAX(due_time) due_time FROM task_assignment ta WHERE goal_object_id=?";
    private final String SELECT_TASKASSIGNMENTS_BY_GOALOBJECT =
            "SELECT task_assignment.* FROM task_assignment WHERE goal_object_id=?";
    private final String SELECT_UNFINISHED_TASKASSIGNMENTS_BY_GOALOBJECT =
            "SELECT * FROM task_assignment WHERE goal_object_id=? AND completion_time IS NULL";
    private final String SELECT_TASKASSIGNMENT_BY_GOALOBJECT_ID =
            "SELECT ta.* FROM task_assignment ta, task, goal_object go, goal, horse, sequence_object so"
            + " WHERE go.id=? AND go.goal_id=goal.id AND go.sequence_object_id=so.id AND goal.workflow_sequence_id=so.workflow_sequence_id"
            + " AND ta.task_id=task.id AND task.goal_id=goal.id AND ta.target_id=horse.target_id AND horse.workflow_object_id=so.workflow_object_id"
            + " AND task.product_id=horse.product_id AND task.assignment_method!=3";
    private final String SELECT_COMPLETED_TASKASSIGNMENT_BY_GOALOBJECT =
            "SELECT task_assignment.* FROM task_assignment WHERE goal_object_id=? AND status=5";
    private final String SELECT_TASKASSIGNMENT_ALERT_INFO_BY_TA_ID =
            "SELECT task.task_name, target.short_name, code_name, product.name, ta.due_time "
            + " FROM task, target, project, product, task_assignment ta, horse "
            + " WHERE ta.id=? AND ta.task_id=task.id AND ta.target_id=target.id AND horse.id=ta.horse_id AND horse.product_id=product.id AND product.project_id=project.id";
    private final String SELECT_TASKASSIGNMENT_ALERT_INFO_BY_GOALOBJECT_ID =
            "SELECT task_name, target.short_name, code_name, product.name, ta.due_time "
            + " FROM target, task, project, product, goal_object go, goal, horse, sequence_object so"
            + " WHERE go.id=? AND go.goal_id=goal.id AND go.sequence_object_id=so.id AND goal.workflow_sequence_id=so.workflow_sequence_id"
            + " AND task.goal_id=goal.id AND horse.workflow_object_id=so.workflow_object_id AND horse.target_id=target.id"
            + " AND task.product_id=horse.product_id AND task.product_id=product.id AND product.project_id=project.id";
    private final String SELECT_TA_BY_ASSIGNED_USER_ID_AND_TA_ID =
            "SELECT * FROM task_assignment WHERE assigned_user_id = ? AND id = ?";

    private final String SELECT_ALL_COMPLETED_USERS_BY_PROJECT_AND_ROLE =
            "SELECT ta_a.user_id user_id"
            + "     FROM (SELECT ta.assigned_user_id user_id, COUNT(ta.status) count, ta.* "
            + "                     FROM task_assignment ta, project_membership pm, horse h, product prd, project prj "
            + "                         WHERE prd.project_id=? AND pm.role_id=? AND pm.project_id=prd.project_id AND pm.user_id=ta.assigned_user_id "
            + "                              AND ta.horse_id=h.id AND h.product_id=prd.id AND prj.id=prd.project_id AND (prj.status!=2 AND prj.status!=3 AND prj.status!=4)"
            + "                         GROUP BY ta.assigned_user_id) ta_a, "
            + "                (SELECT ta.assigned_user_id user_id, COUNT(ta.status) count "
            + "                     FROM task_assignment ta, project_membership pm, horse h, product prd, project prj "
            + "                         WHERE ta.status=5 AND prd.project_id=? AND pm.role_id=? AND pm.project_id=prd.project_id "
            + "                              AND pm.user_id=ta.assigned_user_id AND ta.horse_id=h.id AND h.product_id=prd.id AND prj.id=prd.project_id AND (prj.status!=2 AND prj.status!=3 AND prj.status!=4) "
            + "                         GROUP BY ta.assigned_user_id) ta_b"
            + "        WHERE ta_a.user_id=ta_b.user_id AND ta_a.count=ta_b.count ";

    private final String SELECT_ALL_COMPLETED_USERS_BY_PROJECT_AND_PRODUCT_AND_ROLE =
            "SELECT ta_a.user_id user_id"
            + "     FROM (SELECT ta.assigned_user_id user_id, COUNT(ta.status) count, ta.* "
            + "                     FROM task_assignment ta, project_membership pm, horse h, product prd, project prj "
            + "                         WHERE prd.project_id=? AND prd.id=? AND pm.role_id=? AND pm.project_id=prd.project_id AND pm.user_id=ta.assigned_user_id "
            + "                              AND ta.horse_id=h.id AND h.product_id=prd.id AND prj.id=prd.project_id AND (prj.status!=2 AND prj.status!=3 AND prj.status!=4)"
            + "                         GROUP BY ta.assigned_user_id) ta_a, "
            + "                (SELECT ta.assigned_user_id user_id, COUNT(ta.status) count "
            + "                     FROM task_assignment ta, project_membership pm, horse h, product prd, project prj "
            + "                         WHERE ta.status=5 AND prd.project_id=? AND prd.id=? AND pm.role_id=? AND pm.project_id=prd.project_id "
            + "                              AND pm.user_id=ta.assigned_user_id AND ta.horse_id=h.id AND h.product_id=prd.id AND prj.id=prd.project_id AND (prj.status!=2 AND prj.status!=3 AND prj.status!=4) "
            + "                         GROUP BY ta.assigned_user_id) ta_b"
            + "        WHERE ta_a.user_id=ta_b.user_id AND ta_a.count=ta_b.count ";


    private final String SELECT_LAST_COMPLETED_TASKS_BY_USER_AND_PROJECT_AND_ROLE =
            "SELECT ta_a.*  FROM "
            + "		(SELECT ta.* FROM task_assignment ta, project_membership pm, horse h, product prd, project prj "
            + "			WHERE ta.assigned_user_id=? AND prd.project_id=? AND pm.role_id=? AND ta.status=5 AND pm.project_id=prd.project_id AND pm.user_id=ta.assigned_user_id "
            + "			 		AND ta.horse_id=h.id AND h.product_id=prd.id AND prj.id=prd.project_id AND (prj.status!=2 AND prj.status!=3 AND prj.status!=4)) ta_a, "
            + "		(SELECT MAX(ta.completion_time) max_completion_time FROM task_assignment ta, project_membership pm, horse h, product prd, project prj "
            + "			WHERE ta.assigned_user_id=? AND prd.project_id=? AND pm.role_id=? AND ta.status=5 AND pm.project_id=prd.project_id AND pm.user_id=ta.assigned_user_id "
            + "					AND ta.horse_id=h.id AND h.product_id=prd.id AND prj.id=prd.project_id AND (prj.status!=2 AND prj.status!=3 AND prj.status!=4)) ta_b "
            + "	WHERE ta_a.completion_time=max_completion_time";


    private final String SELECT_LAST_COMPLETED_TASKS_BY_USER_AND_PROJECT_AND_PRODUCT_AND_ROLE =
            "SELECT ta_a.*  FROM "
            + "		(SELECT ta.* FROM task_assignment ta, project_membership pm, horse h, product prd, project prj "
            + "			WHERE ta.assigned_user_id=? AND prd.project_id=? AND prd.id=? AND pm.role_id=? AND ta.status=5 AND pm.project_id=prd.project_id AND pm.user_id=ta.assigned_user_id "
            + "			 		AND ta.horse_id=h.id AND h.product_id=prd.id AND prj.id=prd.project_id AND (prj.status!=2 AND prj.status!=3 AND prj.status!=4)) ta_a, "
            + "		(SELECT MAX(ta.completion_time) max_completion_time FROM task_assignment ta, project_membership pm, horse h, product prd, project prj "
            + "			WHERE ta.assigned_user_id=? AND prd.project_id=? AND prd.id=? AND pm.role_id=? AND ta.status=5 AND pm.project_id=prd.project_id AND pm.user_id=ta.assigned_user_id "
            + "					AND ta.horse_id=h.id AND h.product_id=prd.id AND prj.id=prd.project_id AND (prj.status!=2 AND prj.status!=3 AND prj.status!=4)) ta_b "
            + "	WHERE ta_a.completion_time=max_completion_time";


    private final String SELECT_COMPLETED_TASKS_BY_USER_AND_PROJECT_AND_ROLE =
            "SELECT DISTINCT ta.horse_id horse_id FROM task_assignment ta, project_membership pm, horse h, product prd, project prj "
            + "WHERE prd.project_id=? AND pm.role_id=? AND ta.assigned_user_id=? AND ta.status=5 AND pm.project_id=prd.project_id AND pm.user_id=ta.assigned_user_id "
            + "     AND ta.horse_id=h.id AND h.product_id=prd.id AND prj.id=prd.project_id AND (prj.status!=2 AND prj.status!=3 AND prj.status!=4) "
            + "ORDER BY ta.completion_time ASC";


    private final String SELECT_COMPLETED_TASKS_BY_USER_AND_PROJECT_AND_PRODUCT_AND_ROLE =
            "SELECT DISTINCT ta.horse_id horse_id FROM task_assignment ta, project_membership pm, horse h, product prd, project prj "
            + "WHERE prd.project_id=? AND prd.id=? AND pm.role_id=? AND ta.assigned_user_id=? AND ta.status=5 AND pm.project_id=prd.project_id AND pm.user_id=ta.assigned_user_id "
            + "     AND ta.horse_id=h.id AND h.product_id=prd.id AND prj.id=prd.project_id AND (prj.status!=2 AND prj.status!=3 AND prj.status!=4) "
            + "ORDER BY ta.completion_time ASC";


    private static final String SELECT_TASK_ASSIGNMENT_COUNT_BY_PRODUCT_ID =
            "SELECT COUNT(1) FROM task_assignment ta, task t WHERE t.product_id=? AND t.id=ta.task_id";
    private static final String DELETE_TASK_ASSINGMENTS_BY_TASK_ID = "DELETE FROM task_assignment WHERE task_id=?";
    private static final String DELETE_TASK_ASSIGNMENT_BY_ASSIGNMENT_ID = "DELETE FROM task_assignment WHERE id=?";

    // The ta.horse_id=h.id is needed to make sure the horse is still alive (not in dead_horse).
    private static final String SELECT_ASSIGNED_TASK_ASSIGNMENTS_BY_FILTER_WITH_PAGINATION_PREFIX =
            "SELECT ta.*, t.task_name task_name, tgt.name target_name, CONCAT(u.first_name,\" \", u.last_name) assigned_username "
            + "FROM product p, task_assignment ta, task t, target tgt, user u, horse h "
            + "WHERE p.id=? AND t.product_id=p.id AND ta.horse_id=h.id AND ta.task_id=t.id AND ta.target_id=tgt.id AND ta.assigned_user_id!=0 AND ta.assigned_user_id=u.id ";

    private static final String SELECT_UNASSIGNED_TASK_ASSIGNMENTS_BY_FILTER_WITH_PAGINATION_PREFIX =
            "SELECT ta.*, t.task_name task_name, tgt.name target_name, \"\" AS assigned_username "
            + "FROM product p, task_assignment ta, task t, target tgt, horse h "
            + "WHERE p.id=? AND t.product_id=p.id AND ta.horse_id=h.id AND ta.task_id=t.id AND ta.target_id=tgt.id AND ta.assigned_user_id=0 ";

    private static final String SELECT_ALL_TASK_ASSIGNMENTS_BY_FILTER_WITH_PAGINATION_PREFIX =
            "SELECT * FROM ("
            + SELECT_ASSIGNED_TASK_ASSIGNMENTS_BY_FILTER_WITH_PAGINATION_PREFIX
            + " UNION DISTINCT "
            + SELECT_UNASSIGNED_TASK_ASSIGNMENTS_BY_FILTER_WITH_PAGINATION_PREFIX
            + ") temp";

    private static final String SELECT_TASK_ASSIGNMENTS_BY_FILTER_WITH_PAGINATION_SUFFIX =
            " ORDER BY {0} {1} LIMIT {2},{3}";
    private static final String SELECT_TASK_ASSIGNMENT_COUNT_BY_FILTER_PREFIX =
            "SELECT COUNT(*) "
            + "FROM product p, task_assignment ta, task t "
            + "WHERE p.id=? AND t.product_id=p.id AND ta.task_id=t.id ";
    private static final String SELECT_TASK_ASSIGNMENTS_BY_PROJECT_ID =
            "SELECT ta.* FROM task_assignment ta JOIN task t ON ta.task_id=t.id JOIN product p ON t.product_id=p.id WHERE p.project_id=?";
    private static final String SELECT_TASK_ASSIGNMENTS_BY_PRODUCT_ID =
            "SELECT ta.* FROM task_assignment ta JOIN task t ON ta.task_id=t.id WHERE t.product_id=?";

    private static final String SELECT_TASK_ASSIGNMENTS_BY_TASK_ID =
            "SELECT ta.* FROM task_assignment ta WHERE ta.task_id=?";
    
    private static final String SELECT_TASK_ASSIGNMENTS_BY_TASK_IDS =
            "SELECT ta.* FROM task_assignment ta WHERE ta.task_id IN ({0})";

    private static final String COUNT_TASK_ASSIGNMENT_BY_TASKID_TARGETID_ASSIGNED_USER =
            "SELECT COUNT(1) FROM task_assignment ta WHERE ta.task_id=? AND ta.target_id=? AND ta.assigned_user_id=?";

    private static Map<String, String> filterName2Field = new HashMap<String, String>();
    private static Map<String, Integer> filterName2Type = new HashMap<String, Integer>();

    static {
        filterName2Field.put("taskid", "task_id");
        filterName2Field.put("targetid", "target_id");
        filterName2Type.put("taskid", new Integer(INTEGER));
        filterName2Type.put("targetid", new Integer(INTEGER));
    }

    // ONLY keep valid filters
    private Map<String, Object> preprocessFilter(Map<String, Object> filters) {
        Map<String, Object> finalFilters = new HashMap<String, Object>();
        for (Entry<String, Object> e : filters.entrySet()) {
            if (filterName2Field.containsKey(e.getKey().toLowerCase())
                    && (Integer) e.getValue() > 0) {
                finalFilters.put(e.getKey().toLowerCase(), e.getValue());
            }
        }
        return finalFilters;
    }

    private String prepareDynamicSQL(Map<String, Object> filters) {
        StringBuffer sqlBuf = new StringBuffer();
        boolean isFirst = true;

        for (Entry<String, Object> e : filters.entrySet()) {
            if (!isFirst) {
                sqlBuf.append(" AND ");
            }
            sqlBuf.append(filterName2Field.get(e.getKey()) + "=?");
            isFirst = false;
        }
        return sqlBuf.toString();
    }

    public List<TaskAssignment> selectTaskAssignmentsByProjectId(int projectId) {
        return super.find(SELECT_TASK_ASSIGNMENTS_BY_PROJECT_ID, projectId);
    }

    public List<TaskAssignment> selectTaskAssignmentsByProductId(int productId) {
        return super.find(SELECT_TASK_ASSIGNMENTS_BY_PRODUCT_ID, productId);
    }

    public List<TaskAssignment> selectTaskAssignmentsByTaskId(int taskId) {
        return super.find(SELECT_TASK_ASSIGNMENTS_BY_TASK_ID, taskId);
    }

    public List<TaskAssignment> selectTaskAssignmentsByTaskIds(List<Integer> taskIds) {
        return super.find(MessageFormat.format(SELECT_TASK_ASSIGNMENTS_BY_TASK_IDS, ListUtils.listToString(taskIds)));
    }

    public TaskAssignment createDefaultTaskAssignment(int horseId, int taskId, GoalObject goalObj, int targetId, Date dueTime) {
        TaskAssignment ta = new TaskAssignment();
        ta.setAssignedUserId(0);
        ta.setCompletionTime(null);
        ta.setData("");
        ta.setDueTime(dueTime);
        ta.setEventLogId(0);
        ta.setExitType((short) 0);
        ta.setGoalObjectId(goalObj.getId());
        ta.setHorseId(horseId);
        ta.setPercent(-1f);
        ta.setQEnterTime(new Date());
        ta.setQLastAssignedTime(null);
        ta.setQLastAssignedUid(0);
        ta.setQLastReturnTime(null);
        ta.setQPriority(Constants.TASK_ASSIGNMENT_PRIORITY_LOW); // 'set to low'
        ta.setStartTime(null);

        switch (goalObj.getStatus()) {
            case Constants.GOAL_OBJECT_STATUS_WAITING:
            case Constants.GOAL_OBJECT_STATUS_STARTING:
                ta.setStatus(Constants.TASK_ASSIGNMENT_STATUS_INACTIVE); // set to 'inactive'
                break;

            default:
                ta.setStatus(Constants.TASK_ASSIGNMENT_STATUS_ACTIVE);
                ta.setQPriority(Constants.TASK_ASSIGNMENT_PRIORITY_HIGH);
                break;
        }

        ta.setTargetId(targetId);
        ta.setTaskId(taskId);
        return super.create(ta);
    }

    public boolean exists(int taskId, int targetId, int assignedUserId) {
        return super.count(COUNT_TASK_ASSIGNMENT_BY_TASKID_TARGETID_ASSIGNED_USER, taskId, targetId, assignedUserId) > 0;
    }

    public long countOfTaskAssignmentCountByFilter(int productId, Map<String, Object> filters) {
        Map<String, Object> processedFilters = preprocessFilter(filters);
        StringBuffer sqlBuf = new StringBuffer(SELECT_TASK_ASSIGNMENT_COUNT_BY_FILTER_PREFIX);

        if (processedFilters != null && !processedFilters.isEmpty()) {
            sqlBuf.append(" AND ").append(prepareDynamicSQL(processedFilters));
        }

        logger.debug("Execute SQL: " + sqlBuf.toString());
        Object[] params = new Object[processedFilters.size() + 1];
        params[0] = productId;

        int idx = 1;
        for (Entry<String, Object> e : processedFilters.entrySet()) {
            params[idx] = e.getValue();
            ++idx;
        }
        return super.count(sqlBuf.toString(), params);
    }

    public List<TaskAssignmentVO> selectTaskAssignmentsByFilter(int productId, Map<String, Object> filters, String sortName, String sortOrder, int offset, int count) {
        if ("TASKNAME".equalsIgnoreCase(sortName)) {
            sortName = "task_name";
        } else if ("TARGETNAME".equalsIgnoreCase(sortName)) {
            sortName = "target_name";
        } else if ("USER".equalsIgnoreCase(sortName)) {
            sortName = "assigned_username";
        } else if ("duetimeDisplay".equalsIgnoreCase(sortName)) {
            sortName = "due_time";
        } else {
            sortName = "task_name";
        }

        Map<String, Object> processedFilters = preprocessFilter(filters);
        StringBuffer sqlBuf = new StringBuffer(SELECT_ALL_TASK_ASSIGNMENTS_BY_FILTER_WITH_PAGINATION_PREFIX);

        if (processedFilters != null && !processedFilters.isEmpty()) {
            sqlBuf.append(" WHERE ").append(prepareDynamicSQL(processedFilters));
        }

        sqlBuf.append(MessageFormat.format(SELECT_TASK_ASSIGNMENTS_BY_FILTER_WITH_PAGINATION_SUFFIX, sortName, sortOrder, offset, count));
        logger.debug("Execute SQL: " + sqlBuf.toString());

        Object[] params = new Object[processedFilters.size() + 2];
        int[] paramTypes = new int[processedFilters.size() + 2];

        params[0] = params[1] = productId;
        int idx = 2;
        for (Entry<String, Object> e : processedFilters.entrySet()) {
            params[idx] = e.getValue();
            paramTypes[idx] = filterName2Type.get(e.getKey()).intValue();
            ++idx;
        }

        return getJdbcTemplate().query(sqlBuf.toString(),
                params,
                new TaskAssignmentRowMapper());
    }

    public void deleteByTaskId(int taskId) {
        super.delete(DELETE_TASK_ASSINGMENTS_BY_TASK_ID, taskId);
    }

    public void deleteByTaskAssignmentId(int assignmentId) {
        super.delete(DELETE_TASK_ASSIGNMENT_BY_ASSIGNMENT_ID, assignmentId);
    }

    public long countOfTaskAssignmentCountByProductId(int producctId) {
        return super.count(SELECT_TASK_ASSIGNMENT_COUNT_BY_PRODUCT_ID, producctId);
    }

    public List<Integer> selectCompletedTasksByProjectAndRoleAndUserId(int projectId, int roleId, int userId) {
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("horse_id");
            }
        };

        return getJdbcTemplate().query(SELECT_COMPLETED_TASKS_BY_USER_AND_PROJECT_AND_ROLE,
                new Object[]{projectId, roleId, userId},
                mapper);
    }

    public List<Integer> selectCompletedTasksByProjectAndProductAndRoleAndUserId(int projectId, int productId, int roleId, int userId) {
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("horse_id");
            }
        };

        return getJdbcTemplate().query(SELECT_COMPLETED_TASKS_BY_USER_AND_PROJECT_AND_PRODUCT_AND_ROLE,
                new Object[]{projectId, productId, roleId, userId},
                mapper);
    }

    public TaskAssignment selectLastCompletedTaskByUserAndProjectAndRole(int projectId, int roleId, int userId) {
        return super.findSingle(SELECT_LAST_COMPLETED_TASKS_BY_USER_AND_PROJECT_AND_ROLE, new Object[]{userId, projectId, roleId, userId, projectId, roleId});
    }

    public TaskAssignment selectLastCompletedTaskByUserAndProjectAndProductAndRole(int projectId, int productId, int roleId, int userId) {
        return super.findSingle(SELECT_LAST_COMPLETED_TASKS_BY_USER_AND_PROJECT_AND_PRODUCT_AND_ROLE, new Object[]{userId, projectId, productId, roleId, userId, projectId, productId, roleId});
    }

    public List<Integer> selectAllCompletedUsersProjectAndRole(int projectId, int roleId) {
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("user_id");
            }
        };

        return getJdbcTemplate().query(SELECT_ALL_COMPLETED_USERS_BY_PROJECT_AND_ROLE,
                new Object[]{projectId, roleId, projectId, roleId},
                mapper);
    }

    public List<Integer> selectAllCompletedUsersProjectProductAndRole(int projectId, int productId, int roleId) {
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("user_id");
            }
        };

        return getJdbcTemplate().query(SELECT_ALL_COMPLETED_USERS_BY_PROJECT_AND_PRODUCT_AND_ROLE,
                new Object[]{projectId, productId, roleId, projectId, productId, roleId},
                mapper);
    }

    public TaskAssignment selectIncompleteAssignment(int goalObjectId) {
        return super.findSingle(SELECT_INCOMPLETE_TASKASSIGNMENT_BY_GOALOBJECT_ID, goalObjectId);
    }

    public List<TaskAssignment> selectTaskAssignmentsByTaskAndGoalObject(int taskId, int goalObjectId) {
        return super.find(SELECT_TASKASSIGNMENTS_BY_TASK_AND_GOALOBJECT, (Object) taskId, goalObjectId);
    }

    public List<TaskAssignment> selectTaskAssignmentsByGoalObject(int goalObjectId) {
        return super.find(SELECT_TASKASSIGNMENTS_BY_GOALOBJECT, goalObjectId);
    }

    public List<TaskAssignment> selectUnfinishedTaskAssignmentsByGoalObject(int goalObjectId) {
        return super.find(SELECT_UNFINISHED_TASKASSIGNMENTS_BY_GOALOBJECT, goalObjectId);
    }

    public List<TaskAssignment> selectCompletedTaskAssignmentsByGoalObject(int goalObjectId) {
        return super.find(SELECT_COMPLETED_TASKASSIGNMENT_BY_GOALOBJECT, goalObjectId);
    }

    public List<TaskAssignment> selectTaskAssignmentsByGoalObjectId(int goalObjectId) {
        log.debug("Select TaskAssignment Id by Goal Object Id " + goalObjectId);
        RowMapper mapper = new RowMapper() {

            public TaskAssignment mapRow(ResultSet rs, int rowNum) throws SQLException {
                TaskAssignment ta = new TaskAssignment();
                ta.setId(rs.getInt("id"));
                ta.setTaskId(rs.getInt("task_id"));
                ta.setTargetId(rs.getInt("target_id"));
                ta.setAssignedUserId(rs.getInt("assigned_user_id"));
                ta.setDueTime(rs.getTimestamp("due_time"));
                ta.setStatus(rs.getShort("status"));
                ta.setStartTime(rs.getTimestamp("start_time"));
                ta.setCompletionTime(rs.getTimestamp("completion_time"));
                ta.setEventLogId(rs.getInt("event_log_id"));
                ta.setQEnterTime(rs.getTimestamp("q_enter_time"));
                ta.setQLastAssignedTime(rs.getTimestamp("q_last_assigned_time"));
                ta.setQLastAssignedUid(rs.getInt("q_last_assigned_uid"));
                ta.setQLastReturnTime(rs.getTimestamp("q_last_return_time"));
                ta.setQPriority(rs.getShort("q_priority"));
                return ta;
            }
        };
        return getJdbcTemplate().query(SELECT_TASKASSIGNMENT_BY_GOALOBJECT_ID,
                new Object[]{goalObjectId},
                mapper);
    }

    public List<TaskAssignment> selectTaskAssignmentByAssignedUserIdAndTaskId(int assignedUserId, int taskId) {
        log.debug("Select TaskAssignment Id by Task Id " + taskId);
        return super.find(SELECT_TASKASSIGNMENT_BY_USER_ID, (Object) assignedUserId, taskId);
    }

    public TaskAssignment selectTaskAssignmentByUserTaskTarget(int assignedUserId, int taskId, int targetId) {
        log.debug("===== Select TaskAssignment Id by User " + assignedUserId + " Task " + taskId + " TARGET " + targetId);
        return super.findSingle(SELECT_TASKASSIGNMENT_BY_USER_TASK_TARGET, assignedUserId, taskId, targetId);
    }

    public TaskAssignment selectTaskAssignmentByUserTaskHorse(int assignedUserId, int taskId, int horseId) {
        log.debug("===== Select TaskAssignment Id by User " + assignedUserId + " Task " + taskId + " HORSE " + horseId);
        return super.findSingle(SELECT_TASKASSIGNMENT_BY_USER_TASK_HORSE, assignedUserId, taskId, horseId);
    }

    public TaskAssignment selectTaskAssignmentByTaskHorse(int taskId, int horseId) {
        log.debug("===== Select TaskAssignment Id by Task " + taskId + " HORSE " + horseId);
        return super.findSingle(SELECT_TASKASSIGNMENT_BY_TASK_HORSE, taskId, horseId);
    }

    public long countTaskAssignmentByTaskTarget(int taskId, int taretId) {
        log.debug("===== Select TaskAssignment Id by Task " + taskId + " and Target " + taretId);
        return super.count(SELECT_TASKASSIGNMENT_BY_TASK_TARGET, taskId, taretId);
    }

    public Date selectMaxDuetimeByGoalObjectId(int goalObjectId) {
        RowMapper mapper = new RowMapper() {

            public Date mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getDate("due_time");
            }
        };
        List<Date> list = getJdbcTemplate().query(SELECT_MAX_DUE_TIME_BY_GOALOBJECTID,
                new Object[]{goalObjectId},
                new int[]{INTEGER},
                mapper);
        return (list == null || list.size() <= 0) ? null : list.get(0);
    }

    public List<TaskAssignment> selectQueueTAByTaskId(int taskId) {
        log.debug("Select TaskAssignment Id by Task Id " + taskId);
        RowMapper mapper = new RowMapper() {

            public TaskAssignment mapRow(ResultSet rs, int rowNum) throws SQLException {
                TaskAssignment ta = new TaskAssignment();
                ta.setId(rs.getInt("id"));
                ta.setTaskId(rs.getInt("task_id"));
                ta.setTargetId(rs.getInt("target_id"));
                ta.setAssignedUserId(rs.getInt("assigned_user_id"));
                ta.setDueTime(rs.getTimestamp("due_time"));
                ta.setStatus(rs.getShort("status"));
                ta.setStartTime(rs.getTimestamp("start_time"));
                ta.setCompletionTime(rs.getTimestamp("completion_time"));
                ta.setEventLogId(rs.getInt("event_log_id"));
                ta.setQEnterTime(rs.getTimestamp("q_enter_time"));
                ta.setQLastAssignedTime(rs.getTimestamp("q_last_assigned_time"));
                ta.setQLastAssignedUid(rs.getInt("q_last_assigned_uid"));
                ta.setQLastReturnTime(rs.getTimestamp("q_last_return_time"));
                ta.setQPriority(rs.getShort("q_priority"));
                ta.setHorseId(rs.getInt("horse_id"));
                return ta;
            }
        };
        return getJdbcTemplate().query(SELECT_QUEUE_TASKASSIGNMENT_BY_TASK_ID,
                new Object[]{taskId},
                new int[]{INTEGER},
                mapper);
    }

    public TaskAssignment get(int id) {
        log.debug("Select TaskAssignment Id by TaskAssignment Id: " + id);
        RowMapper mapper = new RowMapper() {

            public TaskAssignment mapRow(ResultSet rs, int rowNum) throws SQLException {
                TaskAssignment ta = new TaskAssignment();
                ta.setId(rs.getInt("id"));
                ta.setTaskId(rs.getInt("task_id"));
                ta.setTargetId(rs.getInt("target_id"));
                ta.setAssignedUserId(rs.getInt("assigned_user_id"));
                ta.setDueTime(rs.getTimestamp("due_time"));
                ta.setStatus(rs.getShort("status"));
                ta.setStartTime(rs.getTimestamp("start_time"));
                ta.setCompletionTime(rs.getTimestamp("completion_time"));
                ta.setEventLogId(rs.getInt("event_log_id"));
                ta.setQEnterTime(rs.getTimestamp("q_enter_time"));
                ta.setQLastAssignedTime(rs.getTimestamp("q_last_assigned_time"));
                ta.setQLastAssignedUid(rs.getInt("q_last_assigned_uid"));
                ta.setQLastReturnTime(rs.getTimestamp("q_last_return_time"));
                ta.setQPriority(rs.getShort("q_priority"));
                ta.setData(rs.getString("data"));
                ta.setGoalObjectId(rs.getInt("goal_object_id"));
                ta.setPercent(rs.getFloat("percent"));
                ta.setHorseId(rs.getInt("horse_id"));

                return ta;
            }
        };
        List<TaskAssignment> taList = getJdbcTemplate().query(SELECT_QUEUE_TASKASSIGNMENT_BY_ID,
                new Object[]{id},
                new int[]{INTEGER},
                mapper);

        return (taList != null && taList.size() > 0) ? taList.get(0) : null;
    }

    public TaskAssignmentAlertInfo selectTAAlertInfo(int taId) {
        RowMapper mapper = new RowMapper() {

            public TaskAssignmentAlertInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                TaskAssignmentAlertInfo taaInfo = new TaskAssignmentAlertInfo();
                taaInfo.setTaskName(rs.getString("task_name"));
                taaInfo.setTargetName(rs.getString("target.short_name"));
                taaInfo.setProjectName(rs.getString("code_name"));
                taaInfo.setProductName(rs.getString("product.name"));
                taaInfo.setDueTime(rs.getDate("ta.due_time"));
                return taaInfo;
            }
        };
        List<TaskAssignmentAlertInfo> list = getJdbcTemplate().query(SELECT_TASKASSIGNMENT_ALERT_INFO_BY_TA_ID,
                new Object[]{taId},
                new int[]{INTEGER},
                mapper);
        return (list != null && list.size() > 0) ? list.get(0) : null;
    }

    public TaskAssignmentAlertInfo selectTAAlertInfoByGoalObjectId(int goId) {
        RowMapper mapper = new RowMapper() {

            public TaskAssignmentAlertInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                TaskAssignmentAlertInfo taaInfo = new TaskAssignmentAlertInfo();
                taaInfo.setTaskName(rs.getString("task_name"));
                taaInfo.setTargetName(rs.getString("target.short_name"));
                taaInfo.setProjectName(rs.getString("code_name"));
                taaInfo.setProductName(rs.getString("product.name"));
                taaInfo.setDueTime(rs.getDate("ta.due_time"));
                return taaInfo;
            }
        };
        List<TaskAssignmentAlertInfo> list = getJdbcTemplate().query(SELECT_TASKASSIGNMENT_ALERT_INFO_BY_GOALOBJECT_ID,
                new Object[]{goId},
                new int[]{INTEGER},
                mapper);
        return (list != null && list.size() > 0) ? list.get(0) : null;
    }

    public void updateApplyStatId(int taskAssignmentId, int userId) throws SQLException {
        logger.debug("Update Apply Stat TaskAssignment Id " + taskAssignmentId + " userId " + userId);

        Object[] values = new Object[]{userId, taskAssignmentId};
        this.getJdbcTemplate().update(UPDATE_APPLY_STAT_BY_ID, values);
    }

    public void updateReturnStatId(int taskAssignmentId, int userId) throws SQLException {
        logger.debug("Update Apply Stat TaskAssignment Id " + taskAssignmentId + " userId " + userId);

        Object[] values = new Object[]{userId, taskAssignmentId};
        this.getJdbcTemplate().update(UPDATE_RETURN_STAT_BY_ID, values);
    }

    public void updateAssignAndPriorityId(int taskAssignmentId, int userId, int priority) throws SQLException {
        logger.debug("Update Apply Stat TaskAssignment Id " + taskAssignmentId + " userId " + userId);

        Object[] values = new Object[]{userId, priority, taskAssignmentId};
        this.getJdbcTemplate().update(UPDATE_ASSIGN_AND_PRIORITY_BY_ID, values);
    }

    public void updatePriority(int taskAssignmentId, int priority) throws SQLException {

        Object[] values = new Object[]{priority, taskAssignmentId};
        this.getJdbcTemplate().update(UPDATE_PRIORITY_BY_ID, values);
    }

    public void updateTaskAssignmentStatus(int assignId, int status) {
        logger.debug("Update task assignment status: " + UPDATE_TASKASSIGNMENT_STATUS + ". [assignId=" + assignId + ", status=" + status + "].");
        super.update(UPDATE_TASKASSIGNMENT_STATUS, status, assignId);
    }

    public void updateTaskAssignment(int assignId, float percentage) {
        logger.debug("Update task assignment percentage: " + UPDATE_TASKASSIGNMENT_PERCENTAGE + ". [assignId=" + assignId + ", percentage=" + percentage + "].");
        super.update(UPDATE_TASKASSIGNMENT_PERCENTAGE, percentage, assignId);
    }

    public void updateTaskAssignment(int assignId, int status, float percentage) {
        logger.debug("Update task assignment status and percentage: " + UPDATE_TASKASSIGNMENT_STATUS_AND_PERCENTAGE + ". [assignId=" + assignId + ", status=" + status + ", perentage=" + percentage + "].");
        super.update(UPDATE_TASKASSIGNMENT_STATUS_AND_PERCENTAGE, status, percentage, assignId);
    }

    public void updateTaskAssignment(int assignId, int status, float percentage, Date completionTime) {
        // update status and percentage first.  YC
        logger.debug("Update task assignment status, percentage: " + UPDATE_TASKASSIGNMENT_STATUS_AND_PERCENTAGE + ". [assignId=" + assignId + ", status=" + status + ", perentage=" + percentage + "].");
        super.update(UPDATE_TASKASSIGNMENT_STATUS_AND_PERCENTAGE, status, percentage, assignId);

        // Then update the completion_time. Note it's updated only if completion_time is not set in DB
        logger.debug("Update task completion time: " + UPDATE_TASKASSIGNMENT_COMPLETION_TIME + ". [assignId=" + assignId + "].");
        super.update(UPDATE_TASKASSIGNMENT_COMPLETION_TIME, completionTime, assignId);
    }

    public void updateTaskAssignmentEventLog(int assignId, int eventLogId) {
        logger.debug("Update task assignment event_log_id: " + UPDATE_TASKASSIGNMENT_EVENT_LOG_ID + ". [assignId=" + assignId + ", eventLogId=" + eventLogId + "].");
        super.update(UPDATE_TASKASSIGNMENT_EVENT_LOG_ID, eventLogId, assignId);
    }

    public TaskAssignment selectTAByAssignedUserIdAndTAId(int assignedUserId, int assignid) {
        return super.findSingle(SELECT_TA_BY_ASSIGNED_USER_ID_AND_TA_ID, assignedUserId, assignid);
    }



    public int create(int taskId, int prodId, int targetId, int userId) {
        return call("add_assignment", taskId, prodId, targetId, userId);
    }

    public int update(int assignmentId, int userId) {
        return call("update_assignment", assignmentId, userId);
    }


    static private final String SELECT_ACTIVE_ASSIGNMENTS_USER_HORSE_TASK =
            "SELECT * FROM task_assignment WHERE assigned_user_id=? AND horse_id=? AND task_id=? AND status != 5";

    public TaskAssignment selectActiveAssignmentsByUserHorseAndTask(int assignedUserId, int horseId, int taskId) {
        return super.findSingle(SELECT_ACTIVE_ASSIGNMENTS_USER_HORSE_TASK,
                new Object[]{assignedUserId, horseId, taskId});
    }


    static private final String SELECT_ACTIVE_ASSIGNMENT_BY_HORSE =
            "SELECT * FROM task_assignment WHERE horse_id=? AND status != 5";

    public TaskAssignment selectActiveAssignmentByHorse(int horseId) {
        return super.findSingle(SELECT_ACTIVE_ASSIGNMENT_BY_HORSE, horseId);
    }

    static private final String SELECT_ACTIVE_FLAG_ASSIGNMENTS =
            "SELECT ta.* FROM task_assignment ta, horse " +
            "WHERE (task_id=-3 OR task_id=-4) AND status != 0 AND status != 5 AND horse.id=ta.horse_id";

    public List<TaskAssignment> selectActiveFlagAssignments() {
        return super.find(SELECT_ACTIVE_FLAG_ASSIGNMENTS);
    }

}

class TaskAssignmentRowMapper implements RowMapper {

    public TaskAssignmentVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        TaskAssignmentVO ta = new TaskAssignmentVO();
        ta.setId(ResultSetUtil.getInt(rs, "id"));
        ta.setAssignedUserId(ResultSetUtil.getInt(rs, "assignedUserId"));
        ta.setTargetId(ResultSetUtil.getInt(rs, "target_id"));
        ta.setCompletionTime(ResultSetUtil.getDate(rs, "completion"));
        ta.setData(ResultSetUtil.getString(rs, "data"));
        ta.setAssignedUsername(ResultSetUtil.getString(rs, "assigned_username"));
        ta.setTargetName(ResultSetUtil.getString(rs, "target_name"));
        ta.setTaskName(ResultSetUtil.getString(rs, "task_name"));
        ta.setStatus(ResultSetUtil.getShort(rs, "status"));
        ta.setDueTime(ResultSetUtil.getDate(rs, "due_time"));
        ta.setEventLogId(ResultSetUtil.getInt(rs, "event_log_id"));
        ta.setExitType(ResultSetUtil.getShort(rs, "exit_type"));
        ta.setGoalObjectId(ResultSetUtil.getInt(rs, "goal_object_id"));
        ta.setHorseId(ResultSetUtil.getInt(rs, "horse_id"));
        ta.setPercent(ResultSetUtil.getFloat(rs, "percent"));
        ta.setQEnterTime(ResultSetUtil.getDate(rs, "q_enter_time"));
        ta.setQLastAssignedTime(ResultSetUtil.getDate(rs, "q_last_assigned_time"));
        ta.setQLastAssignedUid(ResultSetUtil.getInt(rs, "q_last_assigned_uid"));
        ta.setQLastReturnTime(ResultSetUtil.getDate(rs, "q_last_return_time"));
        ta.setQPriority(ResultSetUtil.getShort(rs, "q_priority"));
        ta.setStartTime(ResultSetUtil.getDate(rs, "start_time"));
        if (ta.getDueTime() == null) {
            ta.setDuetimeDisplay("N/A");
        } else {
            ta.setDuetimeDisplay(DateUtils.format(ta.getDueTime(), DateUtils.DATE_FORMAT_3));
        }
        switch (ta.getStatus()) {
            case Constants.TASK_ASSIGNMENT_STATUS_INACTIVE:
                ta.setStatusDisplay("INACTIVE");
                break;
            case Constants.TASK_ASSIGNMENT_STATUS_ACTIVE:
                ta.setStatusDisplay("ACTIVE");
                break;
            case Constants.TASK_ASSIGNMENT_STATUS_AWARE:
                ta.setStatusDisplay("AWARE");
                break;
            case Constants.TASK_ASSIGNMENT_STATUS_NOTICED:
                ta.setStatusDisplay("NOTICED");
                break;
            case Constants.TASK_ASSIGNMENT_STATUS_STARTED:
                ta.setStatusDisplay("STARTED");
                break;
            case Constants.TASK_ASSIGNMENT_STATUS_DONE:
                ta.setStatusDisplay("DONE");
                break;
            default:
                ta.setStatusDisplay("INITIAL");
        }
        return ta;
    }
}
