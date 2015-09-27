/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Role;
import com.ocs.indaba.po.Task;
import com.ocs.indaba.util.ResultSetUtil;
import static java.sql.Types.TINYINT;
import static java.sql.Types.INTEGER;

import com.ocs.indaba.vo.AssignedTask;
import com.ocs.indaba.vo.TaskAssignmentStatusData;
import com.ocs.indaba.vo.TaskVO;
import com.ocs.util.ListUtils;
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
public class TaskDAO extends SmartDaoMySqlImpl<Task, Integer> {

    private static final Logger log = Logger.getLogger(TaskDAO.class);
    public static final int TASK_ASSIGNMENT_METHOD_QUEUE = 2;
    private static final String SELECT_TASKS_BY_PRODUCT_ID_AND_TASK_NAME =
            "SELECT * from task WHERE product_id=? AND task_name=?";
    private static final String SELECT_TASK_BY_TOOL =
            "SELECT task.* from task, tool WHERE task.tool_id=tool.id AND tool.name=?";
    /*
    private static final String SELECT_ALL_ASSIGNED_TASKS_OF_PROJECT =
    "SELECT DISTINCT h.id horse_id, ta.id assignment_id, t.type task_type, ta.assigned_user_id, u.username, ta.percent percentage, ta.start_time, t.id task_id, t.task_name, t.tool_id, "
    + "prd.id product_id, prd.name product_name, trgt.id target_id, trgt.short_name target_name, g.id goal_id, g.name goal_name, "
    + "wo.status workflowObjectStatus, g.duration, ta.status, ta.due_time, "
    + "datediff(date_add(ta.start_time, interval g.duration day), NOW()) util_days "
    + "FROM task t, task_assignment ta, product prd, project prj, goal g, target trgt, horse h, user u, workflow_object wo "
    + "WHERE t.id=ta.task_id AND t.goal_id=g.id AND t.product_id=prd.id AND prd.project_id=prj.id AND "
    + "prj.id=? AND ta.target_id=trgt.id AND trgt.id=h.target_id AND prd.id=h.product_id AND "
    + "wo.id=h.workflow_object_id AND u.id=ta.assigned_user_id {0} ORDER BY ta.id";
     *
     */
    private static final String SELECT_ALL_ASSIGNED_TASKS_BY_USERID =
            "SELECT DISTINCT h.id horse_id, ta.id assignment_id, ta.exit_type, t.type task_type, ta.assigned_user_id, u.username, ta.percent percentage, ta.start_time, t.id task_id, t.task_name, t.tool_id, "
            + "prd.id product_id, prd.name product_name, trgt.id target_id, trgt.short_name target_name, g.id goal_id, g.name goal_name, "
            + "wo.status workflowObjectStatus, g.duration, ta.status, ta.due_time, ta.status_data tasd, "
            + "datediff(date_add(ta.start_time, interval g.duration day), NOW()) util_days "
            + "FROM task t, task_assignment ta, product prd, project prj, goal g, target trgt, horse h, user u, workflow_object wo, goal_object go "
            + "WHERE ta.assigned_user_id=? AND t.id=ta.task_id AND ta.goal_object_id=go.id AND go.goal_id=g.id "
            + "AND ta.horse_id=h.id AND prd.project_id=prj.id "
            + "AND prj.id=? AND ta.target_id=trgt.id AND trgt.id=h.target_id AND prd.id=h.product_id AND "
            + "wo.id=h.workflow_object_id AND u.id=ta.assigned_user_id {0} ORDER BY ta.due_time";

    private static final String SELECT_ASSIGNED_TASKS_OF_HORSE_BY_USERID =
            "SELECT DISTINCT h.id horse_id, ta.id assignment_id, ta.exit_type, t.type task_type, ta.assigned_user_id, u.username, ta.percent percentage, ta.start_time, t.id task_id, t.task_name, t.tool_id, "
            + "prd.id product_id, prd.name product_name, trgt.id target_id, trgt.short_name target_name, g.id goal_id, g.name goal_name, "
            + "wo.status workflowObjectStatus, g.duration, ta.status, ta.due_time, ta.status_data tasd, "
            + "datediff(date_add(ta.start_time, interval g.duration day), NOW()) util_days "
            + "FROM task t, task_assignment ta, product prd, goal g, target trgt, horse h, user u, workflow_object wo "
            + "WHERE h.id=? AND ta.assigned_user_id=? AND (ta.status=? OR ta.status=?) AND t.id=ta.task_id AND t.goal_id=g.id AND t.product_id=prd.id AND "
            + "ta.target_id=trgt.id AND trgt.id=h.target_id AND prd.id=h.product_id AND wo.id=h.workflow_object_id AND u.id=ta.assigned_user_id ORDER BY prd.name";

    private static final String SELECT_ASSIGNED_TASK_OF_HORSE_BY_USERID_AND_ASSIGNID =
            "SELECT DISTINCT h.id horse_id, ta.id assignment_id, ta.exit_type, ta.assigned_user_id, t.type task_type, u.username, ta.percent percentage, ta.start_time, t.id task_id, t.task_name, t.tool_id, "
            + "t.instructions instructions, prd.id product_id, prd.name product_name, trgt.id target_id, trgt.short_name target_name, g.id goal_id, g.name goal_name, "
            + "wo.status workflowObjectStatus, g.duration, ta.status, ta.due_time, ta.status_data tasd, "
            + "datediff(date_add(ta.start_time, interval g.duration day), NOW()) util_days "
            + "FROM task t, task_assignment ta, product prd, goal g, target trgt, horse h, user u, workflow_object wo "
            + "WHERE h.id=? AND ta.assigned_user_id=? AND ta.id=? {0} AND t.id=ta.task_id AND t.goal_id=g.id AND t.product_id=prd.id AND "
            + "ta.target_id=trgt.id AND trgt.id=h.target_id AND prd.id=h.product_id AND wo.id=h.workflow_object_id AND u.id=ta.assigned_user_id ORDER BY prd.name";
    private static final String SELECT_INSTRUCTIONS_BY_TASK_ASSIGNID =
            "SELECT t.instructions instructions "
            + "FROM task_assignment ta, task t "
            + "WHERE ta.id=? AND ta.task_id=t.id";
    private static final String SELECT_ASSIGNED_TASKS_OF_HORSE =
            "SELECT DISTINCT h.id horse_id, ta.id assignment_id, ta.exit_type, t.type task_type, ta.assigned_user_id, u.username, ta.percent percentage, ta.start_time, t.id task_id, t.task_name, t.tool_id, "
            + "prd.id product_id, prd.name product_name, trgt.id target_id, trgt.short_name target_name, g.id goal_id, g.name goal_name, "
            + "wo.status workflowObjectStatus, g.duration, ta.status, ta.due_time, ta.status_data tasd, "
            + "datediff(date_add(ta.start_time, interval g.duration day), NOW()) util_days "
            + "FROM task t, task_assignment ta, product prd, goal g, target trgt, horse h, workflow_object wo "
            + "LEFT JOIN user u on u.id=ta.assigned_user_id "
            + "WHERE h.id=? AND t.id=ta.task_id AND t.goal_id=g.id AND t.product_id=prd.id AND "
            + "ta.target_id=trgt.id AND trgt.id=h.target_id AND prd.id=h.product_id AND wo.id=h.workflow_object_id ORDER BY due_time, prd.name";
    private static final String SELECT_TASK_ASSIGNMENTS_OF_HORSE =
            "SELECT DISTINCT h.id horse_id, ta.id assignment_id, ta.exit_type, t.type task_type, ta.assigned_user_id, u.username, ta.percent percentage, ta.start_time, t.id task_id, t.task_name, t.tool_id, "
            + "prd.id product_id, prd.name product_name, trgt.id target_id, trgt.short_name target_name, g.id goal_id, g.name goal_name, "
            + "wo.status workflowObjectStatus, g.duration, ta.status, ta.due_time, ta.status_data tasd, "
            + "datediff(date_add(ta.start_time, interval g.duration day), NOW()) util_days "
            + "FROM task t, task_assignment ta, product prd, goal g, target trgt, horse h, user u, workflow_object wo "
            + "WHERE h.id=? AND t.id=ta.task_id AND t.goal_id=g.id AND t.product_id=prd.id AND ta.target_id=trgt.id AND "
            + "trgt.id=h.target_id AND prd.id=h.product_id AND wo.id=h.workflow_object_id AND u.id=ta.assigned_user_id ORDER BY due_time, prd.name";

    /*
    private static final String SELECT_TASK_ASSIGNMENTS_OF_HORSE_AND_GOAL =
    "SELECT DISTINCT h.id horse_id, ta.id assignment_id, ta.exit_type, t.type task_type, ta.assigned_user_id, u.username, ta.percent percentage, ta.start_time, t.id task_id, t.task_name, t.tool_id, "
    + "prd.id product_id, prd.name product_name, trgt.id target_id, trgt.short_name target_name, g.id goal_id, g.name goal_name, "
    + "wo.status workflowObjectStatus, g.duration, ta.status, ta.due_time, "
    + "datediff(date_add(ta.start_time, interval g.duration day), NOW()) util_days "
    + "FROM task t, task_assignment ta, product prd, goal g, target trgt, horse h, user u, workflow_object wo "
    + "WHERE h.id=? AND g.id=? AND t.id=ta.task_id AND t.goal_id=g.id AND t.product_id=prd.id AND ta.target_id=trgt.id AND "
    + "trgt.id=h.target_id AND prd.id=h.product_id AND wo.id=h.workflow_object_id AND u.id=ta.assigned_user_id ORDER BY due_time, prd.name";
     */
    // changed to get task info from goal object instead of goal, and product info thru horse instead of task. This is necessary to include dynamic task assignments
    // such as Survey Review Response, whose task doesn't belong to any goal nor any product.   YC.
    private static final String SELECT_TASK_ASSIGNMENTS_OF_HORSE_AND_GOAL =
            "SELECT DISTINCT h.id horse_id, ta.id assignment_id, ta.exit_type, t.type task_type, ta.assigned_user_id, u.username, ta.percent percentage, ta.start_time, t.id task_id, t.task_name, t.tool_id, "
            + "prd.id product_id, prd.name product_name, trgt.id target_id, trgt.short_name target_name, g.id goal_id, g.name goal_name, "
            + "wo.status workflowObjectStatus, g.duration, ta.status, ta.due_time, ta.status_data tasd, "
            + "datediff(date_add(ta.start_time, interval g.duration day), NOW()) util_days "
            + "FROM task t, task_assignment ta, product prd, goal g, goal_object go, target trgt, horse h, user u, workflow_object wo "
            + "WHERE h.id=? AND g.id=? AND ta.horse_id=h.id AND go.id=ta.goal_object_id AND go.goal_id=g.id AND t.id=ta.task_id AND ta.target_id=trgt.id AND "
            + "trgt.id=h.target_id AND prd.id=h.product_id AND wo.id=h.workflow_object_id AND u.id=ta.assigned_user_id ORDER BY due_time, prd.name";

    /*
    private static final String SELECT_ACTIVE_ASSIGNED_TASKS_OF_HORSE =
    "SELECT DISTINCT h.id horse_id, ta.id assignment_id, ta.exit_type, t.type task_type, ta.assigned_user_id, u.username,ta.percent percentage, ta.start_time, t.id task_id, t.task_name, t.tool_id, "
    + "prd.id product_id, prd.name product_name, trgt.id target_id, trgt.short_name target_name, g.id goal_id, g.name goal_name, "
    + "wo.status workflowObjectStatus, g.duration, ta.status, ta.due_time, "
    + "datediff(date_add(ta.start_time, interval g.duration day), NOW()) util_days "
    + "FROM task t, task_assignment ta, product prd, goal g, target trgt, horse h, user u, workflow_object wo "
    + "WHERE h.id=? AND t.id=ta.task_id AND t.goal_id=g.id AND t.product_id=prd.id AND ta.target_id=trgt.id AND "
    + "trgt.id=h.target_id AND prd.id=h.product_id AND wo.id=h.workflow_object_id AND u.id=ta.assigned_user_id {0} ORDER BY due_time, prd.name";
     */
    // changed to get product info thru horse instead of task. This is necessary to include dynamic task assignments
    // such as Survey Review Response, whose task doesn't belong to any product.   YC.
    private static final String SELECT_ACTIVE_ASSIGNED_TASKS_OF_HORSE =
            "SELECT DISTINCT h.id horse_id, ta.id assignment_id, ta.exit_type, t.type task_type, ta.assigned_user_id, u.username,ta.percent percentage, ta.start_time, t.id task_id, t.task_name, t.tool_id, "
            + "prd.id product_id, prd.name product_name, trgt.id target_id, trgt.short_name target_name, g.id goal_id, g.name goal_name, "
            + "wo.status workflowObjectStatus, g.duration, ta.status, ta.due_time, ta.status_data tasd, "
            + "datediff(date_add(ta.start_time, interval g.duration day), NOW()) util_days "
            + "FROM task t, task_assignment ta, product prd, goal g, goal_object go, target trgt, horse h, user u, workflow_object wo "
            + "WHERE h.id=? AND ta.horse_id=h.id AND t.id=ta.task_id AND go.id=ta.goal_object_id AND g.id=go.goal_id AND ta.target_id=trgt.id AND "
            + "trgt.id=h.target_id AND prd.id=h.product_id AND wo.id=h.workflow_object_id AND u.id=ta.assigned_user_id {0} ORDER BY due_time, prd.name";

    private static final String SELECT_QUEUE_TASKS = "SELECT * from task where assignment_method=? order by ta.assigned_user_id, ta.id";

    private static final String SELECT_QUEUE_TASKS_BY_PROJECT_ID =
            "SELECT t.id, t.task_name, t.description, t.goal_id, t.product_id, t.tool_id, t.assignment_method"
            + " from task t join product p on t.product_id=p.id"
            + " where t.assignment_method=? and p.project_id=?"
            + " order by t.task_name";

    private static final String SELECT_TASKS_BY_PRODUCT_ID =
            "SELECT t.* FROM task t WHERE t.product_id=? ORDER BY t.task_name";

    private static final String SELECT_USER_TASKS_BY_PRODUCT_ID =
            "SELECT DISTINCT t.* FROM task t, task_role tr, project_membership pm, product p "
            + "WHERE p.id=? AND t.product_id=p.id AND tr.task_id=t.id AND tr.can_claim!=0 AND pm.user_id=? AND pm.project_id=p.project_id AND pm.role_id=tr.role_id "
            + "ORDER BY t.task_name";

    private static final String SELECT_TASKS_BY_PRODUCT_ID_AND_ROLE_IDS =
            "SELECT DISTINCT t.* FROM task t JOIN task_role tr ON t.id=tr.task_id WHERE t.product_id=? AND tr.role_id IN ({0}) ORDER BY t.task_name";
    
    private static final String SELECT_TASKS_BY_PROJECT_ID =
            "SELECT t.* FROM task t JOIN product p ON t.product_id=p.id "
            + "WHERE p.project_id=? ORDER by t.task_name";

    private static final String SELECT_UNASSIGNED_TASKS_BY_PROJECT_ID =
            "SELECT DISTINCT t.* FROM task t, product p, task_assignment ta "
            + "WHERE p.project_id=? AND t.product_id=p.id AND ta.task_id=t.id AND ta.assigned_user_id=0 "
            + "ORDER by t.task_name";

    private static final String SELECT_USER_TASKS_BY_PROJECT_ID =
            "SELECT DISTINCT t.* FROM task t, task_role tr, project_membership pm, product p "
            + "WHERE p.project_id=? AND t.product_id=p.id AND tr.task_id=t.id AND tr.can_claim!=0 AND pm.user_id=? AND pm.project_id=p.project_id AND pm.role_id=tr.role_id "
            + "ORDER BY t.task_name";

    private static final String SELECT_USER_UNASSIGNED_TASKS_BY_PROJECT_ID =
            "SELECT DISTINCT t.* FROM task t, task_role tr, project_membership pm, product p, task_assignment ta  "
            + "WHERE p.project_id=? AND t.product_id=p.id AND tr.task_id=t.id AND tr.can_claim!=0 AND pm.user_id=? AND pm.project_id=p.project_id AND pm.role_id=tr.role_id "
            + "AND ta.task_id=t.id AND ta.assigned_user_id=0 "
            + "ORDER BY t.task_name";

    private static final String SELECT_ASSIGNED_TASKS_BY_FILTER =
            "SELECT DISTINCT h.id horse_id, ta.id assignment_id, ta.exit_type, t.type task_type, ta.assigned_user_id, u.username, ta.percent percentage, ta.start_time, t.id task_id, t.task_name, t.tool_id, "
            + "prd.id product_id, prd.name product_name, trgt.id target_id, trgt.short_name target_name, g.id goal_id, g.name goal_name, "
            + "wo.status workflowObjectStatus, g.duration, ta.status, ta.due_time, ta.status_data tasd, "
            + "datediff(date_add(ta.start_time, interval g.duration day), NOW()) util_days "
            + "FROM task_assignment ta "
            + "JOIN task t ON (t.id=ta.task_id) "
            + "JOIN goal g ON (t.goal_id=g.id) "
            + "JOIN product prd ON (t.product_id=prd.id) "
            + "JOIN target trgt ON (ta.target_id=trgt.id) "
            //            + "JOIN horse h ON (ta.horse_id=h.id) "
            + "JOIN horse h ON (h.product_id=prd.id AND h.target_id=trgt.id) "
            + "JOIN user u ON (u.id=ta.assigned_user_id) "
            + "JOIN workflow_object wo ON (wo.id=h.workflow_object_id) "
            + "WHERE ta.assigned_user_id=? AND ta.status>=? AND ta.status<=? ";
    private static final String SELECT_TASK_TYPE_BY_ASSIGNID =
            "SELECT t.id task_id, t.tool_id tool_id, t.type task_type, t.task_name task_name, ta.data data, ta.horse_id horse_id "
            + " FROM task t, task_assignment ta WHERE ta.id=? AND t.id=ta.task_id";
    private static final String SELECT_TASKS_OF_GOAL =
            "SELECT task.* FROM task, horse WHERE task.goal_id=? AND horse.id=? AND task.product_id=horse.product_id";
    private static final String SELECT_TASKS_OF_GOALOBJECT =
            "SELECT task.* FROM goal_object, goal, workflow_object, sequence_object, horse, task "
            + "WHERE goal_object.id=? AND (task.assignment_method=1 OR task.assignment_method=2) "
            + "AND goal_object.goal_id=goal.id AND goal_object.sequence_object_id=sequence_object.id "
            + "AND sequence_object.workflow_object_id=workflow_object.id "
            + "AND horse.workflow_object_id=workflow_object.id "
            + "AND task.product_id=horse.product_id AND task.goal_id=goal.id";
    private static final String SELECT_TASK_COUNT_BY_PRODUCT_ID =
            "SELECT COUNT(1) FROM task WHERE product_id=?";
    private static final String SELECT_TASKS_BY_PRODUCT_ID_WITH_PAGINATION =
            "SELECT t.* FROM task t WHERE t.product_id=? ORDER BY {0} {1} LIMIT {2},{3}";
    private static final String SELECT_TASK_ROLES_BY_PRODUCT_ID =
            "SELECT t.*, GROUP_CONCAT(r.name SEPARATOR ', ') rolenames "
            + "FROM task t, task_role tr, role r "
            + "WHERE t.product_id=? AND tr.task_id=t.id AND tr.role_id=r.id "
            + "GROUP BY t.id";
    private static final String SELECT_TASK_ROLES_BY_TASK_IDS =
            "SELECT tr.task_id, r.* FROM task_role tr, role r WHERE r.id=tr.role_id AND tr.task_id IN ({0})";
    private static final String DELETE_TASKS_BY_PRODUCT_ID = "DELETE FROM task WHERE product_id=?";
    private static final String COUNT_TASKS_OF_GOAL_EXCEPT_TASK_ID = "SELECT COUNT(1) FROM task t1 JOIN task t2 ON t1.goal_id=t2.goal_id WHERE t2.id=? AND t1.id!=t2.id";
    private static final String SELECT_TASKS_BY_PRODUCT_AND_GOAL =
            "SELECT * FROM task WHERE product_id=? AND goal_id=?";
    private static final String COUNT_TASKS_BY_PRODUCT_AND_GOAL =
            "SELECT COUNT(*)  FROM task WHERE product_id=? AND goal_id=?";

    public long countTasksOfGoalExcpetTaskId(int taskId) {
        return super.count(COUNT_TASKS_OF_GOAL_EXCEPT_TASK_ID, taskId);
    }

    public List<Task> selectTasksOfGoal(int goalId, int horseId) {
        return super.find(SELECT_TASKS_OF_GOAL, (Object) goalId, (Object) horseId);
    }

    public List<Task> selectTasksOfGoalObject(int go_id) {
        return super.find(SELECT_TASKS_OF_GOALOBJECT, go_id);
    }

    public Task selectTaskByProductIdAndTaskName(int productId, String name) {
        Task task = findSingle(SELECT_TASKS_BY_PRODUCT_ID_AND_TASK_NAME, productId, name);
        return task;
    }

    public Task selectTaskByToolName(String toolName) {
        Task task = findSingle(SELECT_TASK_BY_TOOL, toolName);
        return task;
    }

    public AssignedTask selectTaskTypeAndDataByAssignId(int assignId) {
        log.debug("Select task type by assign id: " + assignId + ". [" + SELECT_TASK_TYPE_BY_ASSIGNID + "].");

        List<AssignedTask> list = getJdbcTemplate().query(SELECT_TASK_TYPE_BY_ASSIGNID,
                new Object[]{assignId},
                new int[]{INTEGER},
                new AssignedTaskRowMapper());

        return (list != null && list.size() > 0) ? list.get(0) : null;
    }

    public List<AssignedTask> selectAssignedTasksByUserId(int userId, int projectId) {
        return selectAssignedTasksByUserId(userId, projectId, false);
    }

    public List<AssignedTask> selectAssignedTasksByUserId(int userId, int projectId, boolean includeCompleted) {
        Integer[] workflowStatues = new Integer[]{
            Constants.WORKFLOW_OBJECT_STATUS_WAITING,
            Constants.WORKFLOW_OBJECT_STATUS_STARTED,
            Constants.WORKFLOW_OBJECT_STATUS_SUSPENDED,
            Constants.WORKFLOW_OBJECT_STATUS_DONE};
        Integer[] taskStatuses = null;
        if (includeCompleted) {
            taskStatuses = new Integer[]{
                        Constants.TASK_STATUS_INACTIVE,
                        Constants.TASK_STATUS_ACTIVE,
                        Constants.TASK_STATUS_AWARE,
                        Constants.TASK_STATUS_NOTICED,
                        Constants.TASK_STATUS_STARTED,
                        Constants.TASK_STATUS_DONE
                    };
        } else {
            taskStatuses = new Integer[]{
                        Constants.TASK_STATUS_ACTIVE,
                        Constants.TASK_STATUS_AWARE,
                        Constants.TASK_STATUS_NOTICED,
                        Constants.TASK_STATUS_STARTED};
        }

        return selectAllAssignedTasksByUserId(userId, projectId,
                workflowStatues,
                taskStatuses);
    }

    public List<AssignedTask> selectAllAssignedTasksByUserId(int userId, int projectId, Integer[] workflowStatuses, Integer[] taskStatuses) {
        StringBuffer sBuf = new StringBuffer();
        appendSQLParameters(sBuf, "wo.status", workflowStatuses);
        appendSQLParameters(sBuf, "ta.status", taskStatuses);
        String sqlStr = MessageFormat.format(SELECT_ALL_ASSIGNED_TASKS_BY_USERID, sBuf.toString());
        log.debug("Select assigened tasks for the user: " + userId + ". [" + sqlStr + "].");
        RowMapper mapper = new AssignedTaskRowMapper();

        List<AssignedTask> list = getJdbcTemplate().query(sqlStr,
                new Object[]{userId, projectId},
                new int[]{INTEGER, INTEGER},
                mapper);

        return list;
    }

    public List<AssignedTask> selectAssignedTasksOfHorseByUserId(int horseId, int userId) {
        log.debug("Select assigened tasks of horse for the user: " + userId + ". [" + SELECT_ASSIGNED_TASKS_OF_HORSE_BY_USERID + "].");
        RowMapper mapper = new AssignedTaskRowMapper();

        List<AssignedTask> list = getJdbcTemplate().query(SELECT_ASSIGNED_TASKS_OF_HORSE_BY_USERID,
                new Object[]{horseId, userId, Constants.TASK_STATUS_ACTIVE, Constants.TASK_STATUS_STARTED},
                new int[]{INTEGER, INTEGER, INTEGER, INTEGER},
                mapper);

        return list;
    }

    public String selectInstructionsByTaskAssignId(int assignId) {
        log.debug("Select instructions by task assign id: " + assignId + ". [" + SELECT_INSTRUCTIONS_BY_TASK_ASSIGNID + "].");

        List<String> list = getJdbcTemplate().query(SELECT_INSTRUCTIONS_BY_TASK_ASSIGNID,
                new Object[]{assignId},
                new int[]{INTEGER},
                new RowMapper() {

                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getString("instructions");
                    }
                });

        return (list != null && list.size() > 0) ? list.get(0) : null;
    }

    public AssignedTask selectAssignedTasksOfHorseByUserIdAndAssignId(int horseId, int userId, int assignId) {
        log.debug("Select assigened tasks of horse for the user[userid=" + userId + ". horseId=" + horseId + ", assignId=" + assignId + "]==>[" + SELECT_ASSIGNED_TASK_OF_HORSE_BY_USERID_AND_ASSIGNID + "].");

        StringBuffer sBuf = new StringBuffer(50);
        sBuf.append(" AND (");
        sBuf.append("ta.status=").append(Constants.TASK_STATUS_ACTIVE).append(" OR ");
        sBuf.append("ta.status=").append(Constants.TASK_STATUS_AWARE).append(" OR ");
        sBuf.append("ta.status=").append(Constants.TASK_STATUS_NOTICED).append(" OR ");
        sBuf.append("ta.status=").append(Constants.TASK_STATUS_STARTED).append(") ");

        String sql = MessageFormat.format(SELECT_ASSIGNED_TASK_OF_HORSE_BY_USERID_AND_ASSIGNID, sBuf.toString());

        RowMapper mapper = new AssignedTaskRowMapper();

        List<AssignedTask> list = getJdbcTemplate().query(sql,
                new Object[]{horseId, userId, assignId},
                new int[]{INTEGER, INTEGER, INTEGER},
                mapper);

        return (list == null || list.size() < 1) ? null : list.get(0);
    }

    public List<AssignedTask> selectAssignedTasksOfHorse(int horseId) {
        log.debug("Select assigened tasks of horse: [" + SELECT_ASSIGNED_TASKS_OF_HORSE + "].");
        RowMapper mapper = new AssignedTaskRowMapper();

        List<AssignedTask> list = getJdbcTemplate().query(SELECT_ASSIGNED_TASKS_OF_HORSE,
                new Object[]{horseId},
                new int[]{INTEGER},
                mapper);

        return list;
    }

    public List<AssignedTask> selectTaskAssignmentsOfHorseAndGoal(int horseId, int goalId) {
        RowMapper mapper = new AssignedTaskRowMapper();
        List<AssignedTask> list;

        if (goalId == 0) {
            list = getJdbcTemplate().query(SELECT_TASK_ASSIGNMENTS_OF_HORSE,
                    new Object[]{horseId},
                    new int[]{INTEGER},
                    mapper);
        } else {
            list = getJdbcTemplate().query(SELECT_TASK_ASSIGNMENTS_OF_HORSE_AND_GOAL,
                    new Object[]{horseId, goalId},
                    new int[]{INTEGER, INTEGER},
                    mapper);
        }
        return list;
    }

    public List<AssignedTask> selectTaskAssignmentsOfHorse(int horseId) {
        return selectTaskAssignmentsOfHorseAndGoal(horseId, 0);
    }

    public List<AssignedTask> selectActiveAssignedTasksOfHorse(int horseId) {
        Integer[] workflowStatues = new Integer[]{
            Constants.WORKFLOW_OBJECT_STATUS_WAITING,
            Constants.WORKFLOW_OBJECT_STATUS_STARTED,
            Constants.WORKFLOW_OBJECT_STATUS_SUSPENDED,
            Constants.WORKFLOW_OBJECT_STATUS_DONE};
        Integer[] taskStatuses = new Integer[]{
            Constants.TASK_STATUS_ACTIVE,
            Constants.TASK_STATUS_AWARE,
            Constants.TASK_STATUS_NOTICED,
            Constants.TASK_STATUS_STARTED};
        StringBuffer sBuf = new StringBuffer();
        appendSQLParameters(sBuf, "wo.status", workflowStatues);
        appendSQLParameters(sBuf, "ta.status", taskStatuses);
        String sql = MessageFormat.format(SELECT_ACTIVE_ASSIGNED_TASKS_OF_HORSE, sBuf.toString());
        //log.info("======= Select assigened tasks of horse:" + horseId + " [" + sql + "].");

        RowMapper mapper = new AssignedTaskRowMapper();
        List<AssignedTask> list = getJdbcTemplate().query(sql,
                new Object[]{horseId},
                new int[]{INTEGER},
                mapper);

        return list;
    }

    public List<AssignedTask> selectAssignedTasksByFilter(int userId, int project, int statusFilter,
            List<Integer> targetFilter, List<Integer> productFilter) {

        RowMapper mapper = new AssignedTaskRowMapper();

        List<AssignedTask> retList = selectAssignedTasksByUserId(userId, project, true);

        if (retList.isEmpty()) {
            return (targetFilter.get(0) == 1 || (productFilter.get(0) == 1)) ? null : retList;
        }

        StringBuffer sb = new StringBuffer(SELECT_ASSIGNED_TASKS_BY_FILTER);
        switch (statusFilter) {
            case 0:
                break;
            case 1:
                sb.append(" AND (ta.due_time IS NULL OR datediff(ta.due_time, NOW()) >= 0) OR ta.status = 5");
                break;
            case 2:
                sb.append(" AND datediff(ta.due_time, NOW()) < 0 AND ta.status < 5");
                break;
        }

        List<AssignedTask> retList1 = getJdbcTemplate().query(sb.toString(),
                new Object[]{userId, Constants.TASK_STATUS_INACTIVE, Constants.TASK_STATUS_DONE},
                new int[]{INTEGER, INTEGER, INTEGER},
                mapper);
        if (retList1.isEmpty()) {
            return null;
        }

        int i;
        sb = new StringBuffer(SELECT_ASSIGNED_TASKS_BY_FILTER);
        String op1 = (targetFilter.get(0) == 1) ? " AND " : " OR ";
        String op2 = (targetFilter.get(0) == 1) ? " <> " : " = ";
        if (targetFilter.size() > 1) {
            sb.append(" AND (");
            for (i = 1; i < targetFilter.size() - 1; i++) {
                sb.append("trgt.id" + op2 + targetFilter.get(i) + op1);
            }
            sb.append("trgt.id" + op2 + targetFilter.get(i) + ")");
        } else if (targetFilter.get(0).equals(0)) {
            return null;
        }
        retList1 = getJdbcTemplate().query(sb.toString(),
                new Object[]{userId, Constants.TASK_STATUS_INACTIVE, Constants.TASK_STATUS_DONE},
                new int[]{INTEGER, INTEGER, INTEGER},
                mapper);
        if (targetFilter.get(0) == 1 && retList1.isEmpty()) {
            return null;
        }
        if (targetFilter.get(0) == 0 && retList1.size() < retList.size()) {
            return null;
        }

        sb = new StringBuffer(SELECT_ASSIGNED_TASKS_BY_FILTER);
        op1 = (productFilter.get(0) == 1) ? " AND " : " OR ";
        op2 = (productFilter.get(0) == 1) ? " <> " : " = ";
        if (productFilter.size() > 1) {
            sb.append(" AND (");
            for (i = 1; i < productFilter.size() - 1; i++) {
                sb.append("prd.id" + op2 + productFilter.get(i) + op1);
            }
            sb.append("prd.id" + op2 + productFilter.get(i) + ") ORDER BY prd.name");
        } else if (productFilter.get(0).equals(0)) {
            return null;
        }
        retList1 = getJdbcTemplate().query(sb.toString(),
                new Object[]{userId, Constants.TASK_STATUS_INACTIVE, Constants.TASK_STATUS_DONE},
                new int[]{INTEGER, INTEGER, INTEGER},
                mapper);
        if (productFilter.get(0) == 1) {
            return retList1.isEmpty() ? null : retList;
        } else {
            return retList1.size() < retList.size() ? null : retList;
        }
    }

    public List<Task> selectQueueTask() {
        log.debug("Select Queue Task");
        return super.find(SELECT_QUEUE_TASKS, TASK_ASSIGNMENT_METHOD_QUEUE);
    }

    public List<Task> selectTasksByProjectId(int projectId) {
        log.debug("Select Queue Task By Project Id " + projectId);
        return super.find(SELECT_TASKS_BY_PROJECT_ID, projectId);
    }

    public List<Task> selectUserTasksByProjectId(int projectId, int userId) {
        log.debug("Select User Task By Project Id " + projectId);
        return super.find(SELECT_USER_TASKS_BY_PROJECT_ID, (Object)projectId, userId);
    }


    public List<Task> selectUnassignedTasksByProjectId(int projectId) {
        log.debug("Select Starting Task By Project Id " + projectId);
        return super.find(SELECT_UNASSIGNED_TASKS_BY_PROJECT_ID, projectId);
    }

    public List<Task> selectUserUnassignedTasksByProjectId(int projectId, int userId) {
        log.debug("Select User Task By Project Id " + projectId);
        return super.find(SELECT_USER_UNASSIGNED_TASKS_BY_PROJECT_ID, (Object)projectId, userId);
    }


    public List<Task> selectTasksByProductId(int productId) {
        log.debug("Select Queue Task By Product Id " + productId);
        return super.find(SELECT_TASKS_BY_PRODUCT_ID, productId);
    }

    public List<Task> selectUserTasksByProductId(int productId, int userId) {
        log.debug("Select User Task By Product Id " + productId);
        return super.find(SELECT_USER_TASKS_BY_PRODUCT_ID, (Object)productId, userId);
    }

    public List<Task> selectTasksByProductId(int productId, List<Integer> roleIds) {
        log.debug("Select Queue Task By Product Id " + productId);
        String sqlStr = MessageFormat.format(SELECT_TASKS_BY_PRODUCT_ID_AND_ROLE_IDS, ListUtils.listToString(roleIds));
        return super.find(sqlStr, productId);
    }

    public List<Task> selectQueueTaskByProjectId(int projectId) {
        log.debug("Select Queue Task By Project Id " + projectId);
        RowMapper mapper = new RowMapper() {

            public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setTaskName(rs.getString("task_name"));
                task.setDescription(rs.getString("description"));
                task.setProductId(rs.getInt("product_id"));
                task.setGoalId(rs.getInt("goal_id"));
                task.setToolId(rs.getInt("tool_id"));
                task.setAssignmentMethod(rs.getShort("assignment_method"));
                return task;
            }
        };
        return getJdbcTemplate().query(SELECT_QUEUE_TASKS_BY_PROJECT_ID,
                new Object[]{TASK_ASSIGNMENT_METHOD_QUEUE, projectId},
                new int[]{TINYINT, INTEGER},
                mapper);
    }

    public long countOfTaskCountByProductId(int projectId) {
        return super.count(SELECT_TASK_COUNT_BY_PRODUCT_ID, projectId);
    }

    public List<TaskVO> selectTasksByProductId(int productId, String sortName, String sortOrder, int offset, int count) {
        sortName = "t.task_name";
        /* if ("TASKNAME".equalsIgnoreCase(sortName)) {
        sortName = "t.name";
        } else {
        sortName = "name";
        }*/
        String sqlStr = MessageFormat.format(SELECT_TASKS_BY_PRODUCT_ID_WITH_PAGINATION, sortName, sortOrder, offset, count);
        logger.debug("Select tasks of product[prodId=" + productId + "]: " + sqlStr);

        List<TaskVO> tasks = getJdbcTemplate().query(sqlStr,
                new Object[]{productId},
                new int[]{INTEGER},
                new TaskRoleRowMapper());

        if (tasks == null || tasks.isEmpty()) {
            return tasks;
        }


        logger.debug("Select task roles: " + SELECT_TASK_ROLES_BY_PRODUCT_ID);

        List<TaskVO> rtasks = getJdbcTemplate().query(SELECT_TASK_ROLES_BY_PRODUCT_ID,
                new Object[]{productId},
                new int[]{INTEGER},
                new TaskRoleRowMapper());

        if (rtasks == null || rtasks.isEmpty()) {
            logger.debug("No roles selected!");
            return tasks;
        }

        for (TaskVO t : tasks) {
            for (TaskVO rt : rtasks) {
                int tid = t.getId();
                int rtid = rt.getId();
                if (tid == rtid) {
                    t.setRoleNames(rt.getRoleNames());
                    break;
                }
            }
        }

        return tasks;
    }

    public Map<Integer, Role> selectRolesByProductId(List<Integer> taskIds) {
        final Map<Integer, Role> map = new HashMap<Integer, Role>();
        if (taskIds != null) {
            String sqlStr = MessageFormat.format(SELECT_TASK_ROLES_BY_TASK_IDS, StringUtils.list2Str(taskIds));
            logger.debug("Select roles of tasks[taskIds=" + taskIds + "]: " + sqlStr);
            RowMapper mapper = new RowMapper() {

                public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Role role = new Role();
                    role.setId(rs.getInt("r.id"));
                    role.setName(rs.getString("r.name"));
                    map.put(rs.getInt("tr.task_id"), role);
                    return -1;
                }
            };
            getJdbcTemplate().query(sqlStr, null, null, mapper);
        }
        return map;
    }

    public void deleteTasksByProductId(int prodId) {
        super.delete(DELETE_TASKS_BY_PRODUCT_ID, prodId);
    }

    public List<Task> selectTaskByProductAndGoal(int productId, int goalId) {
        Object[] values = new Object[]{productId, goalId};
        return super.find(SELECT_TASKS_BY_PRODUCT_AND_GOAL, values);
    }

    public long countTaskByProductAndGoal(int productId, int goalId) {
        Object[] values = new Object[]{productId, goalId};
        return super.count(COUNT_TASKS_BY_PRODUCT_AND_GOAL, values);
    }
    private static final String SELECT_TASK_BY_PROJ_PROD_TASK_ID =
            "SELECT * FROM task t, project proj, product prod WHERE proj.id=? AND prod.id=? AND t.id=? "
            + "AND t.product_id=prod.id AND prod.project_id=proj.id";

    public Task validateTaskRelation(int projId, int prodId, int taskId) {
        return super.findSingle(SELECT_TASK_BY_PROJ_PROD_TASK_ID, projId, prodId, taskId);
    }


    private static final String SELECT_CLAIMABLE_TASKS_BY_PROJECT_AND_ROLE =
            "SELECT * FROM task, project proj, product prod, task_role tr "
            + "WHERE proj.id=? AND prod.project_id=proj.id AND task.product_id=prod.id "
            + "AND tr.role_id=? AND tr.task_id=task.id AND tr.can_claim=1";

    public List<Task> selectClaimableTasks(int projectId, int roleId) {
        return super.find(SELECT_CLAIMABLE_TASKS_BY_PROJECT_AND_ROLE, (long)projectId, roleId);
    }
}

class TaskRoleRowMapper implements RowMapper {

    public TaskVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        TaskVO task = new TaskVO();
        task.setId(ResultSetUtil.getInt(rs, "t.id"));
        task.setTaskName(ResultSetUtil.getString(rs, "t.task_name"));
        task.setDescription(ResultSetUtil.getString(rs, "t.description"));
        task.setInstructions(ResultSetUtil.getString(rs, "t.instructions"));
        task.setGoalId(ResultSetUtil.getInt(rs, "t.goal_id"));
        task.setProductId(ResultSetUtil.getInt(rs, "t.product_id"));
        task.setToolId(ResultSetUtil.getInt(rs, "t.tool_id"));
        task.setAssignmentMethod(ResultSetUtil.getShort(rs, "t.assignment_method"));
        task.setType(ResultSetUtil.getShort(rs, "t.type"));
        task.setSticky(ResultSetUtil.getShort(rs, "t.sticky"));
        task.setRoleNames(ResultSetUtil.getString(rs, "rolenames"));

        if (task.getGoalId() > 0 && task.getDescription() != null && task.getDescription().length() > 0
                && task.getToolId() > 0 && task.getInstructions() != null && task.getInstructions().length() > 0) {
            task.setValidity(Constants.TASK_VALIDITIY_FULL);
        } else if (task.getGoalId() > 0 && task.getToolId() > 0 && task.getInstructions() != null && task.getInstructions().length() > 0) {
            task.setValidity(Constants.TASK_VALIDITIY_HALF);
        } else {
            task.setValidity(Constants.TASK_VALIDITIY_NONE);
        }

        switch (task.getAssignmentMethod()) {
            case Constants.TASK_ASSIGNMENT_METHOD_MANUAL:
                task.setAssignMethod("MANUAL");
                break;
            case Constants.TASK_ASSIGNMENT_METHOD_QUEUE:
                task.setAssignMethod("QUEUE");
                break;
            case Constants.TASK_ASSIGNMENT_METHOD_DYNAMIC:
                task.setAssignMethod("DYNAMIC");
                break;
            default:
                task.setAssignMethod("");
        }
        return task;
    }
}

class AssignedTaskRowMapper implements RowMapper {

    public AssignedTask mapRow(ResultSet rs, int rowNum) throws SQLException {
        AssignedTask assignedTask = new AssignedTask();
        assignedTask.setAssignmentId(ResultSetUtil.getInt(rs, "assignment_id"));
        assignedTask.setDuration(ResultSetUtil.getInt(rs, "duration"));
        assignedTask.setDurTime(ResultSetUtil.getDate(rs, "due_time"));
        assignedTask.setUtilDays(ResultSetUtil.getInt(rs, "util_days"));
        assignedTask.setHorseId(ResultSetUtil.getInt(rs, "horse_id"));
        assignedTask.setProductId(ResultSetUtil.getInt(rs, "product_id"));
        assignedTask.setProductName(ResultSetUtil.getString(rs, "product_name"));
        assignedTask.setWorkflowObjectStatus(ResultSetUtil.getInt(rs, "workflowObjectStatus"));
        assignedTask.setPercentage(ResultSetUtil.getFloat(rs, "percentage"));
        assignedTask.setStartTime(ResultSetUtil.getDate(rs, "start_time"));
        assignedTask.setStatus(ResultSetUtil.getInt(rs, "status"));
        assignedTask.setTargetId(ResultSetUtil.getInt(rs, "target_id"));
        assignedTask.setTargetName(ResultSetUtil.getString(rs, "target_name"));
        assignedTask.setTaskId(ResultSetUtil.getInt(rs, "task_id"));
        assignedTask.setTaskName(ResultSetUtil.getString(rs, "task_name"));
        assignedTask.setTaskType(ResultSetUtil.getInt(rs, "task_type"));
        assignedTask.setData(ResultSetUtil.getString(rs, "data"));
        assignedTask.setAssignedUserId(ResultSetUtil.getInt(rs, "assigned_user_id"));
        assignedTask.setAssignedUsername(ResultSetUtil.getString(rs, "username"));
        assignedTask.setToolId(ResultSetUtil.getInt(rs, "tool_id"));
        assignedTask.setExitType(ResultSetUtil.getShort(rs, "exit_type"));

        String data = ResultSetUtil.getString(rs, "tasd");
        if (data != null && !StringUtils.isEmpty(data)) {
            assignedTask.setTaskAssignmentStatusData(TaskAssignmentStatusData.decode(data));
        }

        return assignedTask;
    }
}
