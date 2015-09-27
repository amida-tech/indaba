/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Horse;
import com.ocs.indaba.util.ResultSetUtil;
import com.ocs.indaba.vo.ActiveHorseView;
import com.ocs.indaba.vo.AssignedTask;
import com.ocs.indaba.vo.HorseInfo;
import com.ocs.util.ListUtils;
import static java.sql.Types.INTEGER;
import com.ocs.util.StringUtils;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Operation on User table by accessing DB
 * 
 * @author Jeff
 * 
 */
public class HorseDAO extends SmartDaoMySqlImpl<Horse, Integer> {

    private static final Logger log = Logger.getLogger(HorseDAO.class);
    private static final String SELECT_USER_JOIN_IN_HORSE = "SELECT count(*) count FROM horse h, task_assignment ta "
            + "WHERE h.id=? AND ta.assigned_user_id=? AND ta.horse_id=h.id";
    private static final String SELECT_USERS_JOIN_IN_HORSE = "SELECT DISTINCT ta.assigned_user_id user_id FROM horse h, task_assignment ta, task t "
            + "WHERE h.id=? AND {0} AND ta.task_id=t.id AND t.product_id=h.product_id AND ta.target_id=h.target_id ";
    private static final String SELECT_USERS_JOIN_IN_HORSES = "SELECT DISTINCT h.id horse_id, ta.assigned_user_id user_id FROM horse h, task_assignment ta "
            + "WHERE ta.horse_id=h.id {0} {1} ";
    private static final String SELECT_ALL_DURATION_OF_HORSE = "SELECT SUM(g.duration) total_duration "
            + "FROM horse h, sequence_object seqobj, goal_object gobj, goal g "
            + "WHERE h.id=? AND h.workflow_object_id=seqobj.workflow_object_id AND "
            + "seqobj.id=gobj.sequence_object_id AND g.id=gobj.goal_id;";
    private static final String SELECT_ALL_DURATION =
            "SELECT SUM(g.duration) total_duration "
            + "FROM horse h, workflow_object wfo, workflow_sequence wfs, goal g "
            + "WHERE h.id=? AND h.workflow_object_id=wfo.id AND wfs.workflow_id=wfo.workflow_id AND g.workflow_sequence_id=wfs.id";
    private static final String SELECT_INCOMPLETED_DURATION =
            "SELECT SUM(g.duration) total_duration "
            + "FROM horse h, workflow_object wfo, sequence_object sqobj, goal g, goal_object gobj "
            + "WHERE h.id=? AND h.workflow_object_id=wfo.id AND sqobj.workflow_object_id=wfo.id AND "
            + "sqobj.id=gobj.sequence_object_id AND g.id=gobj.goal_id AND gobj.status!=?";
    private static final String SELECT_COMPLETED_DURATION =
            "SELECT SUM(g.duration) total_duration "
            + "FROM horse h, workflow_object wfo, sequence_object sqobj, goal g, goal_object gobj "
            + "WHERE h.id=? AND h.workflow_object_id=wfo.id AND sqobj.workflow_object_id=wfo.id AND "
            + "sqobj.id=gobj.sequence_object_id AND g.id=gobj.goal_id AND gobj.status=?";

    private static final String SELECT_ALL_ACTIVE_HORSES_BY_USERID =
            "SELECT DISTINCT h.id horse_id, h.start_time start_time, p.project_id project_id, trgt.id target_id, trgt.short_name target_name, p.id product_id, p.name product_name, "
            + "p.content_type, wfo.workflow_id workflow_id, wfo.id workflow_object_id, wfo.status workflow_object_status, wf.total_duration total_duration "
            + "FROM task_assignment ta, product p, horse h, target trgt, workflow_object wfo, workflow wf "
            + "WHERE p.project_id=? {0} AND h.id=ta.horse_id AND p.id=h.product_id AND "
            + "trgt.id=ta.target_id AND wfo.id=h.workflow_object_id AND "
            + "wfo.status!=? AND wfo.workflow_id=wf.id ORDER BY p.name, trgt.short_name";

    private static final String SELECT_ALL_ACTIVE_HORSES =
            "SELECT DISTINCT h.id horse_id, h.start_time start_time, trgt.id, trgt.short_name target_name, p.id, p.name product_name, p.project_id project_id, "
            + "p.content_type, wfo.workflow_id workflow_id, wfo.id wfo_id, wfo.status workflow_object_status, wf.total_duration total_duration "
            + "FROM horse h, product p, target trgt, workflow_object wfo, workflow wf "
            + "WHERE p.id=h.product_id AND h.target_id=trgt.id AND wfo.id=h.workflow_object_id AND "
            + "wfo.status!=? AND wfo.status!=? AND wfo.workflow_id=wf.id ORDER BY product_name, target_name";
    private static final String SELECT_ACTIVE_HORSES_BY_PARAMETERS =
            "SELECT h.id horse_id, h.start_time start_time, trgt.id target_id, trgt.short_name target_name, prd.id product_id, prd.name product_name, wfo.status workflow_object_status, "
            + "prd.content_type, wfo.workflow_id workflow_id, wfo.id workflow_object_id, prj.id project_id, wf.total_duration total_duration "
            + "FROM horse h, product prd, project prj, target trgt, workflow_object wfo, workflow wf "
            + "WHERE prd.id=h.product_id AND h.target_id=trgt.id AND wfo.id=h.workflow_object_id AND "
            + "prj.id=prd.project_id AND wfo.status!=? AND wfo.workflow_id=wf.id {0} ORDER BY product_name, target_name";
    private static final String SELECT_NEXT_TASK_BY_HORSE_ID = "SELECT ta.horse_id horse_id, ta.due_time due_time, g.weight weight, ta.id task_assingment_id, t.id task_id, t.task_name, ta.start_time, ta.assigned_user_id, ta.status "
            + "FROM task t, task_assignment ta, goal g "
            + "WHERE ta.horse_id=? AND t.id=ta.task_id AND g.id=t.goal_id {0} "
            + "ORDER BY ta.due_time, ta.id";
    // select horse by task id
    private static final String SELECT_HORSE_BY_TASK_ID = "SELECT h.* FROM task_assignment ta, task t, horse h "
            + "WHERE t.id=? AND t.id=ta.task_id AND ta.target_id=h.target_id AND t.product_id=h.product_id";
    private static final String SELECT_JOURNAL_INSTRUCTIONS_BY_HORSE_ID =
            "SELECT jc.instructions FROM journal_content_object jco, journal_config jc "
            + "WHERE jco.id=? AND jco.journal_config_id=jc.id";
    private static final String SELECT_SURVEY_INSTRUCTIONS_BY_HORSE_ID =
            "SELECT sc.instructions FROM survey_content_object sco, survey_config sc "
            + "WHERE sco.id=? AND sco.survey_config_id=sc.id";
    // select horse by content header id
    private static final String SELECT_HORSE_BY_CONTENT_HEADER_ID = "SELECT h.* FROM horse h "
            + "WHERE h.content_header_id = ? ";
    // select content type by horse id
    private static final String SELECT_CONTENT_TYPE_BY_HORSE_ID =
            "SELECT p.content_type FROM horse h, product p WHERE h.id=? AND h.product_id=p.id;";
    private static final String SELECT_HORSE_BY_WORKFLOW_OBJECT =
            "SELECT * FROM horse WHERE workflow_object_id=?;";
    private static final String SELECT_HORSE_BY_GOAL_OBJECT =
            "SELECT horse.*  FROM horse, goal_object, sequence_object "
            + "WHERE goal_object.id=? AND goal_object.sequence_object_id=sequence_object.id "
            + "AND sequence_object.workflow_object_id=horse.workflow_object_id";
    private static final String SELECT_HORSE_BY_PROD_ID =
            "SELECT * FROM horse WHERE product_id = ?";
    private static final String SELECT_WORKFLOW_OBJECT_ID_BY_HORSE_ID =
            "SELECT workflow_object_id FROM horse WHERE id = ?";
    private static final String SELECT_HORSE_BY_PROD_ID_TARGET_ID =
            "SELECT * FROM horse WHERE product_id = ? AND target_id = ?";
    private static final String SELECT_HORSE_INFO_BY_HORSE_ID =
            "SELECT product.name, target.short_name FROM product, target, horse "
            + "WHERE horse.id=? AND horse.product_id=product.id AND horse.target_id=target.id";
    private static final String SELECT_HORSE_OVERDUE_TASKS =
            "SELECT count(*) count FROM task_assignment WHERE horse_id=? AND (status!=0 AND status!=5) AND due_time < now()";
    private static final String SELECT_ALL_SCORECARD_HORSES =
            "SELECT h.* FROM horse h, product p WHERE h.product_id=p.id AND p.content_type=0";
    private static final String SELECT_ALL_NOT_CANCELLED_SCORECARD_HORSES =
            "SELECT h.* FROM horse h, product p, workflow_object wfo WHERE wfo.status!=5 AND h.workflow_object_id=wfo.id AND h.product_id=p.id AND p.content_type=0";
    private static final String SELECT_ALL_NOT_CANCELLED_TSC_SCORECARD_HORSES =
            "SELECT h.* FROM horse h, product p, survey_config sc, workflow_object wfo WHERE wfo.status!=5 AND h.workflow_object_id=wfo.id AND h.product_id=p.id AND p.content_type=0 AND sc.id=p.product_config_id AND sc.is_tsc=1";
    private static final String SELECT_HORSE_COUNT_BY_PRODUCT_ID =
            "SELECT COUNT(id) FROM horse WHERE product_id=?";
    private static final String SELECT_HORSES_BY_PRODUCT_ID_WITH_PAGINATION =
            "SELECT h.*, wfo.status status, t.name target_name, t.short_name target_short_name FROM horse h, workflow_object wfo, target t "
            + "WHERE h.product_id=? AND h.workflow_object_id=wfo.id AND h.target_id=t.id ORDER BY {0} {1} LIMIT {2},{3}";
    private static final String SELECT_TARGETS_BY_HORSE_IDS =
            "SELECT h.id, t.name FROM horse h, target t WHERE h.id IN ({0}) AND h.target_id=t.id";

    private static final String SELECT_HORSES_BY_PROJECT_ID =
            "SELECT DISTINCT h.id horse_id, prd.id prod_id, prd.name prod_name, t.id target_id, t.name target_name, t.short_name target_short_name, h.workflow_object_id workflow_object_id "
            + "FROM horse h, product prd, target t "
            + "WHERE h.product_id=prd.id AND prd.project_id=? AND t.id=h.target_id {0}";

    private static final String SELECT_HORSES_BY_PRODUCT_ID =
            "SELECT DISTINCT h.id horse_id, prd.id prod_id, prd.name prod_name, t.id target_id, t.name target_name, t.short_name target_short_name, h.workflow_object_id workflow_object_id "
            + "FROM horse h JOIN product prd ON h.product_id=prd.id JOIN target t ON t.id=h.target_id "
            + "JOIN workflow_object wfo ON wfo.id=h.workflow_object_id "
            + "WHERE h.product_id=? {0} ORDER BY target_name";


    static private class CommonHorseInfoMapper implements RowMapper {

        public HorseInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                HorseInfo horse = new HorseInfo();
                horse.setId(rs.getInt("horse_id"));
                horse.setProductId(rs.getInt("prod_id"));
                horse.setProductName(rs.getString("prod_name"));
                horse.setTargetId(rs.getInt("target_id"));
                horse.setTargetName(rs.getString("target_name"));

                try {
                    String shortName = rs.getString("target_short_name");
                    horse.setTargetShortName(shortName);
                } catch (Exception e) {
                    horse.setTargetShortName(horse.getTargetName());
                }
                
                horse.setWorkflowObjectId(rs.getInt("workflow_object_id"));
                return horse;
        }
    }

     private static final String SELECT_HORSES_BY_PROJECT_ID_AND_TASKS =
            "SELECT DISTINCT h.id horse_id, prd.id prod_id, prd.name prod_name, t.id target_id, t.name target_name, t.short_name target_short_name, h.workflow_object_id workflow_object_id "
            + "FROM horse h, product prd, target t, task "
            + "WHERE h.product_id=prd.id AND prd.project_id=? AND t.id=h.target_id AND task.product_id=prd.id AND task.id IN ({0}) {1} "
            + "ORDER BY target_name";

    public List<HorseInfo> selectHorsesByProjectIdAndTasksAndTargetIds(int projectId, List<Integer> taskIds, List<Integer> targetIds) {
        String taskIdList = ListUtils.listToString(taskIds);
        logger.debug("Get all horses by project id " + projectId + " and Tasks: " + taskIdList);
        String condStr = (targetIds == null || targetIds.isEmpty()) ? "" : " AND target_id IN (" + ListUtils.listToString(targetIds) + ")";
        String sqlStr = MessageFormat.format(SELECT_HORSES_BY_PROJECT_ID_AND_TASKS, taskIdList, condStr);
        logger.debug("SQL: " + sqlStr);
        return getJdbcTemplate().query(sqlStr, new Object[]{projectId}, new int[]{INTEGER},
                new CommonHorseInfoMapper());
    }


    public List<HorseInfo> selectHorsesByProjectIdAndTargetIds(int projectId, List<Integer> targetIds) {
        logger.debug("Get all horses by project id: " + projectId);
        String condStr = (targetIds == null || targetIds.isEmpty()) ? "" : " AND target_id IN (" + ListUtils.listToString(targetIds) + ")";
        String sqlStr = MessageFormat.format(SELECT_HORSES_BY_PROJECT_ID, condStr);
        logger.debug("SQL: " + sqlStr);
        return getJdbcTemplate().query(sqlStr, new Object[]{projectId}, new int[]{INTEGER},
                new CommonHorseInfoMapper());
    }


    public List<HorseInfo> selectHorsesByProjectId(int projectId) {
        return selectHorsesByProjectIdAndTargetIds(projectId, null);
    }

    public List<HorseInfo> selectHorsesByProductIdAndTargetIds(int productId, List<Integer> targetIds) {
        logger.debug("Get all horses by product id: " + productId);
        String condStr = (targetIds == null || targetIds.isEmpty()) ? "" : "AND target_id IN (" + ListUtils.listToString(targetIds) + ")";
        String sqlStr = MessageFormat.format(SELECT_HORSES_BY_PRODUCT_ID, condStr);
        logger.debug("SQL: " + sqlStr);
        return getJdbcTemplate().query(sqlStr, new Object[]{productId}, new int[]{INTEGER}, new CommonHorseInfoMapper());
    }


    private static final String SELECT_NOT_CANCELLED_HORSES_BY_PRODUCT_ID =
            "SELECT DISTINCT h.id horse_id, prd.id prod_id, prd.name prod_name, t.id target_id, t.name target_name, t.short_name target_short_name, h.workflow_object_id workflow_object_id "
            + "FROM horse h, target t, product prd, workflow_object wfo "
            + "WHERE h.product_id=prd.id AND t.id=h.target_id AND wfo.id=h.workflow_object_id AND wfo.status!=5 AND prd.id=? "
            + "ORDER BY target_name";

    public List<HorseInfo> selectNotCancelledHorsesByProductId(int productId) {
        logger.debug("Get all not-cancelled horses by product id: " + productId);
        return getJdbcTemplate().query(SELECT_NOT_CANCELLED_HORSES_BY_PRODUCT_ID, new Object[]{productId}, new int[]{INTEGER}, new CommonHorseInfoMapper());
    }



    public long countOfHorseCountByProductId(int projectId) {
        return super.count(SELECT_HORSE_COUNT_BY_PRODUCT_ID, projectId);
    }

    public Map<Integer, String> selectTargetsByHorseIds(List<Integer> horseIds) {
        final Map<Integer, String> map = new HashMap<Integer, String>();
        RowMapper mapper = new RowMapper() {

            public Map<Integer, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
                map.put(rs.getInt("h.id"), rs.getString("t.name"));
                return map;
            }
        };
        String sqlStr = MessageFormat.format(SELECT_TARGETS_BY_HORSE_IDS, StringUtils.list2Str(horseIds));
        getJdbcTemplate().query(sqlStr, null, null, mapper);
        return map;
    }

    private static final String SELECT_HORSES_BY_HORSE_IDS = "SELECT * FROM horse WHERE id IN ({0})";

    public List<Horse> selectHorsesByIds (List<Integer> horseIds) {
        if (horseIds == null || horseIds.isEmpty()) return null;

        String sqlStr = MessageFormat.format(SELECT_HORSES_BY_HORSE_IDS, StringUtils.list2Str(horseIds));

        return super.find(sqlStr);
    }


    public List<HorseInfo> selectHorsesByProductId(int productId, String sortName, String sortOrder, int offset, int count) {
        if ("TARGETNAME".equalsIgnoreCase(sortName)) {
            sortName = "target_name";
        } else if ("STATUS".equalsIgnoreCase(sortName)) {
            sortName = "status";
        } else {
            sortName = "target_name";
        }
        String sqlStr = MessageFormat.format(SELECT_HORSES_BY_PRODUCT_ID_WITH_PAGINATION, sortName, sortOrder, offset, count);
        logger.debug("Select project memberships of project[projId=" + productId + "]: " + sqlStr);
        return getJdbcTemplate().query(sqlStr,
                new Object[]{productId},
                new int[]{INTEGER},
                new HorseInfoRowMapper());
    }

    public HorseInfo selectHorseInfo(int horseId) {
        RowMapper mapper = new HorseInfoMapper();
        List<HorseInfo> list = getJdbcTemplate().query(SELECT_HORSE_INFO_BY_HORSE_ID,
                new Object[]{horseId},
                new int[]{INTEGER},
                mapper);
        return (list != null && list.size() > 0) ? list.get(0) : null;
    }

    public List<Horse> selectHorseByProdId(int prodId) {
        return super.find(SELECT_HORSE_BY_PROD_ID, prodId);
    }

    public Horse selectHorseByGoalObject(int goId) {
        return findSingle(SELECT_HORSE_BY_GOAL_OBJECT, goId);
    }

    public Horse selectHorseByWorkflowObjectId(int wfoId) {
        return findSingle(SELECT_HORSE_BY_WORKFLOW_OBJECT, wfoId);
    }

    /**
     * Select horse by task id.
     * @param taskId
     * @return Horse
     */
    public Horse selectHorseByTaskId(int taskId) {
        log.debug("Select horse by task id: " + taskId + ". [" + SELECT_HORSE_BY_TASK_ID + "].");
        return findSingle(SELECT_HORSE_BY_TASK_ID, taskId);
    }

    public Horse selectHorseByProductIdAndTargetId(int productId, int targetId) {
        return findSingle(SELECT_HORSE_BY_PROD_ID_TARGET_ID, productId, targetId);
    }

    /**
     * Select horse by content header id.
     * @param contentHeaderId
     * @return Horse
     */
    public Horse selectHorseByContentHeaderId(Long contentHeaderId) {
        log.debug("Select horse by content header id: " + contentHeaderId + ". [" + SELECT_HORSE_BY_TASK_ID + "].");
        return findSingle(SELECT_HORSE_BY_CONTENT_HEADER_ID, contentHeaderId);
    }

    public boolean selectUserJoinInHorse(int userId, int horseId) {
        log.debug("Check if user joins in the specified horse: " + horseId + "[userId=" + userId + "] ==> [" + SELECT_USER_JOIN_IN_HORSE + "].");
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("count");
            }
        };
        List<Integer> list = getJdbcTemplate().query(SELECT_USER_JOIN_IN_HORSE,
                new Object[]{horseId, userId},
                new int[]{INTEGER, INTEGER},
                mapper);
        return (list != null && list.get(0).intValue() > 0);
    }

    public List<Integer> selectUsersJoinInHorse(Integer[] userIds, int horseId) {
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("user_id");
            }
        };
        String users = super.appendSQLInParameters("assigned_user_id", userIds);
        String sql = MessageFormat.format(SELECT_USERS_JOIN_IN_HORSE, users);
        log.debug("Check if users join in the specified horse: " + horseId + "[userIds=" + userIds + "] ==> [" + sql + "].");
        List<Integer> list = getJdbcTemplate().query(sql,
                new Object[]{horseId},
                new int[]{INTEGER},
                mapper);
        return list;
    }

    public Map<Integer, List<Integer>> selectUsersJoinInHorses(Integer[] userIds, Integer[] horseIds) {
        Map<Integer, List<Integer>> userJoinHorseMap = new HashMap<Integer, List<Integer>>();
        RowMapper mapper = new RowMapper() {

            public UserJoinHorse mapRow(ResultSet rs, int i) throws SQLException {
                UserJoinHorse obj = new UserJoinHorse();
                obj.setHorseId(rs.getInt("horse_id"));
                obj.setUserId(rs.getInt("user_id"));
                return obj;
            }
        };
        String users = super.appendSQLInParameters("assigned_user_id", userIds);
        String horses = super.appendSQLInParameters("h.id", horseIds);
        String sql = MessageFormat.format(SELECT_USERS_JOIN_IN_HORSES, horses, users);
        log.debug("Check if users join in the specified horse: " + horseIds + "[userIds=" + userIds + "] ==> [" + sql + "].");
        List<UserJoinHorse> list = getJdbcTemplate().query(sql,
                new Object[]{},
                new int[]{},
                mapper);
        if (list != null) {
            for (UserJoinHorse o : list) {
                List<Integer> uids = userJoinHorseMap.get(o.getHorseId());
                if (uids == null) {
                    uids = new ArrayList<Integer>(2);
                    userJoinHorseMap.put(o.getHorseId(), uids);
                }
                if (!uids.contains(o.getUserId())) {
                    uids.add(o.getUserId());
                }
            }
        }
        return userJoinHorseMap;
    }

    public int selectAllDuratinOfHorse(int horseId) {
        log.debug("Select all duration of the horse: " + horseId + ". [" + SELECT_ALL_DURATION_OF_HORSE + "].");
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("total_duration");
            }
        };
        List<Integer> list = getJdbcTemplate().query(SELECT_ALL_DURATION_OF_HORSE,
                new Object[]{horseId},
                new int[]{INTEGER},
                mapper);
        return (list != null && list.size() > 0) ? list.get(0) : 0;
    }

    /**
     * Select all duration of the specified horse id.
     * @param horseId
     * @return duration days
     */
    /*public int selectAllDurtion(int horseId) {
    log.debug("Select table all duraton of the horse: " + horseId + ". [" + SELECT_ALL_DURATION + "].");
    RowMapper mapper = new DurationRowMapper();
    List<Integer> list = getJdbcTemplate().query(SELECT_ALL_DURATION,
    new Object[]{horseId},
    new int[]{INTEGER},
    mapper);
    if (list != null && list.size() > 0) {
    return list.get(0);
    }
    return 0;
    }*/
    /**
     * Select all duration of the specified horse id.
     * @param horseId
     * @return duration days
     */
    public int selectIncompletedDurtion(int horseId) {
        log.debug("Select the complted duration of the horse: " + horseId + ". [" + SELECT_INCOMPLETED_DURATION + "].");
        RowMapper mapper = new DurationRowMapper();
        List<Integer> list = getJdbcTemplate().query(SELECT_INCOMPLETED_DURATION,
                new Object[]{horseId, Constants.GOAL_OBJECT_STATUS_DONE},
                new int[]{INTEGER, INTEGER},
                mapper);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return 0;
    }

    /**
     * Select all duration of the specified horse id.
     * @param horseId
     * @return duration days
     */
    public int selectCompletedDurtion(int horseId) {
        log.debug("Select the complted duration of the horse: " + horseId + ". [" + SELECT_COMPLETED_DURATION + "].");
        RowMapper mapper = new DurationRowMapper();
        List<Integer> list = getJdbcTemplate().query(SELECT_COMPLETED_DURATION,
                new Object[]{horseId, Constants.GOAL_OBJECT_STATUS_DONE},
                new int[]{INTEGER, INTEGER},
                mapper);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return 0;
    }

    /**
     * Select all active horses.
     * @param horseId
     * @return duration days
     */
    public List<ActiveHorseView> selectAllActiveHorses() {
        log.debug("Select all of the active horses. [" + SELECT_ALL_ACTIVE_HORSES + "].");
        RowMapper mapper = new ActiveHorseRowMapper();
        List<ActiveHorseView> list = getJdbcTemplate().query(SELECT_ALL_ACTIVE_HORSES,
                new Object[]{Constants.WORKFLOW_OBJECT_STATUS_DONE, Constants.WORKFLOW_OBJECT_STATUS_CANCELLED},
                new int[]{INTEGER, INTEGER},
                mapper);
        return list;
    }

    public List<Horse> selectAllScorecardHorses() {
        log.debug("Select all of the scorecard horses. [" + SELECT_ALL_SCORECARD_HORSES + "].");

        return super.find(SELECT_ALL_SCORECARD_HORSES);
    }

    public List<Horse> selectAllNotCancelledScorecardHorses() {
        log.debug("Select all of the not cancelled scorecard horses. [" + SELECT_ALL_NOT_CANCELLED_SCORECARD_HORSES + "].");

        return super.find(SELECT_ALL_NOT_CANCELLED_SCORECARD_HORSES);
    }

    public List<Horse> selectAllNotCancelledTscScorecardHorses() {
        log.debug("Select all of the not cancelled TSC scorecard horses. [" + SELECT_ALL_NOT_CANCELLED_TSC_SCORECARD_HORSES + "].");

        return super.find(SELECT_ALL_NOT_CANCELLED_TSC_SCORECARD_HORSES);
    }

    /**
     * Select all active horses.
     * @param horseId
     * @return duration days
     */
    public List<ActiveHorseView> selectAllActiveHorses(int projId, int targetId, int productId, int status) {
        StringBuffer sb = new StringBuffer();
        // Combine SQL statement with parameters
        sb.append(" AND prd.project_id=").append(projId);
        if (targetId != Constants.PARAM_ALL_TARGETS) {
            sb.append(" AND trgt.id=").append(targetId);
        }
        if (productId != Constants.PARAM_ALL_PRODUCTS) {
            sb.append(" AND prd.id=").append(productId);
        }
        switch (status) {
            case Constants.PARAM_ALL_STATUSES:
                break;
            case Constants.PARAM_STATUS_NOT_OVERDUE:
                break;
            case Constants.PARAM_STATUS_OVERDUE:
                break;
        }
        sb.append(" ORDER BY product_name");
        String sql = MessageFormat.format(SELECT_ACTIVE_HORSES_BY_PARAMETERS, sb.toString());
        log.debug("Select all of the active horses. [" + sql + "].");
        RowMapper mapper = new ActiveHorseRowMapper();
        List<ActiveHorseView> list = getJdbcTemplate().query(sql,
                new Object[]{Constants.WORKFLOW_OBJECT_STATUS_CANCELLED},
                new int[]{INTEGER},
                mapper);
        return list;
    }

    /**
     * Select all of the active horses in which the specified user took part.
     * @param horseId
     * @return duration days
     */
    public List<ActiveHorseView> selectActiveHorsesByUsers(Integer[] userIds, int projectId) {
        String params = super.appendSQLInParameters("assigned_user_id", userIds);
        String sql = MessageFormat.format(SELECT_ALL_ACTIVE_HORSES_BY_USERID, params);
        log.debug("Select all of the active horses of users: [" + sql + "].");
        RowMapper mapper = new ActiveHorseRowMapper();
        List<ActiveHorseView> list = getJdbcTemplate().query(sql,
                new Object[]{projectId, Constants.WORKFLOW_OBJECT_STATUS_CANCELLED},
                new int[]{INTEGER, INTEGER},
                mapper);
        return list;
    }

    /**
     * Select all of the active horses in which the specified user took part.
     * @param horseId
     * @return duration days
     */
    public List<ActiveHorseView> selectActiveHorsesByUserId(int userId, int projectId) {
        return selectActiveHorsesByUsers(new Integer[]{userId}, projectId);
    }

    /**
     * Select all active horses.
     * @param horseId
     * @return duration days
     */
    public List<ActiveHorseView> selectAllActiveHorses(int projId, List<Integer> targetIds, List<Integer> productIds, int status) {
        StringBuffer sb = new StringBuffer();
        // Combine SQL statement with parameters
        sb.append(" AND prd.project_id=").append(projId);
        if (targetIds != null && targetIds.size() > 0) {
            //if (targetIds != Constants.PARAM_ALL_TARGETS) {
            sb.append(" AND ");
            for (int i = 0, size = targetIds.size(); i < size; ++i) {
                if (i == 0) {
                    sb.append("(");
                }
                sb.append("trgt.id=").append(targetIds.get(i));
                if (i != size - 1) {
                    sb.append(" OR ");
                } else {
                    sb.append(")");
                }
            }
        } else {
            sb.append(" AND trgt.id=" + Constants.INVALID_INT_ID);
        }
        if (productIds != null && productIds.size() > 0) {
            //if (targetIds != Constants.PARAM_ALL_TARGETS) {
            sb.append(" AND ");
            for (int i = 0, size = productIds.size(); i < size; ++i) {
                if (i == 0) {
                    sb.append("(");
                }
                sb.append("prd.id=").append(productIds.get(i));
                if (i != size - 1) {
                    sb.append(" OR ");
                } else {
                    sb.append(")");
                }
            }
        } else {
            sb.append(" AND prd.id=" + Constants.INVALID_INT_ID);
        }

        String sql = MessageFormat.format(SELECT_ACTIVE_HORSES_BY_PARAMETERS, sb.toString());
        log.debug("Select all of the active horses. [" + sql + "].");
        RowMapper mapper = new ActiveHorseRowMapper();
        List<ActiveHorseView> list = getJdbcTemplate().query(sql,
                new Object[]{Constants.WORKFLOW_OBJECT_STATUS_CANCELLED},
                new int[]{INTEGER},
                mapper);

        if (list == null) {
            return list;
        }
        List<ActiveHorseView> resultList = null;
        switch (status) {
            case Constants.PARAM_ALL_STATUSES:
                resultList = list;
                break;
            case Constants.PARAM_STATUS_NOT_OVERDUE:
                resultList = new ArrayList<ActiveHorseView>();
                for (ActiveHorseView horse : list) {
                    /*int duration = selectAllDuratinOfHorse(horse.getHorseId());
                    long now = System.currentTimeMillis();
                    Date startTime = horse.getStartTime();
                    if (startTime == null) {
                    startTime = new Date();
                    }
                    if ((startTime.getTime() + duration * Constants.MILLSECONDS_PER_DAY) >= now) {
                    resultList.add(horse);
                    }*/
                    if (!isHorseOverDue(horse.getHorseId())) {
                        resultList.add(horse);
                    }
                }
                break;
            case Constants.PARAM_STATUS_OVERDUE:
                resultList = new ArrayList<ActiveHorseView>();
                for (ActiveHorseView horse : list) {
                    /*int duration = selectAllDuratinOfHorse(horse.getHorseId());
                    long now = System.currentTimeMillis();
                    Date startTime = horse.getStartTime();
                    if (startTime == null) {
                    startTime = new Date();
                    }
                    if ((startTime.getTime() + duration * Constants.MILLSECONDS_PER_DAY) < now) {
                    resultList.add(horse);
                    }*/

                    if (isHorseOverDue(horse.getHorseId())) {
                        resultList.add(horse);
                    }
                }
                break;
        }
        return resultList;
    }

    /**
     * Select next tasks
     *
     * @param horseId
     * @return List of AssignedTask
     */
    public AssignedTask selectNextTask(int horseId) {
        int[] activeStatuses = {
            Constants.TASK_STATUS_ACTIVE,
            Constants.TASK_STATUS_AWARE,
            Constants.TASK_STATUS_NOTICED,
            Constants.TASK_STATUS_STARTED
        };
        StringBuffer sBuf = new StringBuffer(" AND (");
        for (int i = 0, length = activeStatuses.length; i < length; ++i) {
            if (i != 0) {
                sBuf.append(" OR ");
            }
            sBuf.append("ta.status=" + activeStatuses[i]);
        }
        sBuf.append(")");
        String sql = MessageFormat.format(SELECT_NEXT_TASK_BY_HORSE_ID, sBuf.toString());
        log.debug("Select next tasks by the horse: "
                + horseId + ". [" + sql + "].");
        RowMapper mapper = new TaskRowMapper();
        List<AssignedTask> list = getJdbcTemplate().query(sql,
                new Object[]{horseId},
                new int[]{INTEGER},
                mapper);
        return (list != null && list.size() > 0) ? list.get(0) : null;
    }

    /**
     * Select next tasks
     *
     * @param horseId
     * @return List of AssignedTask
     */
    public int selectContentTypeByHorseId(int horseId) {
        log.debug("Select next tasks by the horse: " + horseId + ". [" + SELECT_CONTENT_TYPE_BY_HORSE_ID + "].");
        // RowMapper mapper = new TaskRowMapper();
        List<Integer> list = getJdbcTemplate().query(SELECT_CONTENT_TYPE_BY_HORSE_ID,
                new Object[]{horseId},
                new int[]{INTEGER},
                new RowMapper() {

                    public Integer mapRow(ResultSet rs, int i) throws SQLException {
                        return rs.getInt("content_type");
                    }
                });
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return Constants.INVALID_INT_ID;
    }

    public String selectJournalInstructionsByCntObjId(int cntObjId) {
        log.debug("Select journal instructions by the horse: " + cntObjId + ". [" + SELECT_JOURNAL_INSTRUCTIONS_BY_HORSE_ID + "].");
        // RowMapper mapper = new TaskRowMapper();
        List<String> list = getJdbcTemplate().query(SELECT_JOURNAL_INSTRUCTIONS_BY_HORSE_ID,
                new Object[]{cntObjId},
                new int[]{INTEGER},
                new RowMapper() {

                    public String mapRow(ResultSet rs, int i) throws SQLException {
                        return rs.getString("instructions");
                    }
                });
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public String selectSurveyInstructionsByCntObjId(int cntObjId) {
        log.debug("Select survey instructions by the horse: " + cntObjId + ". [" + SELECT_SURVEY_INSTRUCTIONS_BY_HORSE_ID + "].");
        // RowMapper mapper = new TaskRowMapper();
        List<String> list = getJdbcTemplate().query(SELECT_SURVEY_INSTRUCTIONS_BY_HORSE_ID,
                new Object[]{cntObjId},
                new int[]{INTEGER},
                new RowMapper() {

                    public String mapRow(ResultSet rs, int i) throws SQLException {
                        return rs.getString("instructions");
                    }
                });
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public int selectWorkflowObjectIdByHorseId(int horseId) {
        log.debug("Select next tasks by the horse: " + horseId + ". [" + SELECT_WORKFLOW_OBJECT_ID_BY_HORSE_ID + "].");
        // RowMapper mapper = new TaskRowMapper();
        List<Integer> list = getJdbcTemplate().query(SELECT_WORKFLOW_OBJECT_ID_BY_HORSE_ID,
                new Object[]{horseId},
                new int[]{INTEGER},
                new RowMapper() {

                    public Integer mapRow(ResultSet rs, int i) throws SQLException {
                        return rs.getInt("workflow_object_id");
                    }
                });
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return Constants.INVALID_INT_ID;
    }

    public boolean isHorseOverDue(int horseId) {
        List<Integer> list = getJdbcTemplate().query(SELECT_HORSE_OVERDUE_TASKS,
                new Object[]{horseId},
                new int[]{INTEGER},
                new RowMapper() {

                    public Integer mapRow(ResultSet rs, int i) throws SQLException {
                        return rs.getInt("count");
                    }
                });
        return (list != null && list.size() != 0 && list.get(0) > 0);
    }
}
/*public String getInstructions(int horseId) {
}*/

class DurationRowMapper implements RowMapper {

    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("total_duration");
    }
}

class ActiveHorseRowMapper implements RowMapper {

    public ActiveHorseView mapRow(ResultSet rs, int rowNum) throws SQLException {
        ActiveHorseView horseView = new ActiveHorseView();
        horseView.setHorseId(rs.getInt("horse_id"));
        horseView.setStartTime(rs.getDate("start_time"));
        horseView.setProjectId(rs.getInt("project_id"));
        horseView.setProductId(rs.getInt("product_id"));
        horseView.setProductName(rs.getString("product_name"));
        horseView.setContentType(rs.getInt("content_type"));
        horseView.setTargetId(rs.getInt("target_id"));
        horseView.setTargetName(rs.getString("target_name"));
        horseView.setWorkflowId(rs.getInt("workflow_id"));
        horseView.setWorkflowObjectId(rs.getInt("workflow_object_id"));
        horseView.setWorkflowObjectStatus(rs.getInt("workflow_object_status"));
        horseView.setTotalDuration(rs.getInt("total_duration"));
        return horseView;
    }
}

class TaskRowMapper implements RowMapper {

    public AssignedTask mapRow(ResultSet rs, int rowNum) throws SQLException {
        AssignedTask assignedTask = new AssignedTask();
        assignedTask.setAssignmentId(ResultSetUtil.getInt(rs, "task_assingment_id"));
        assignedTask.setHorseId(ResultSetUtil.getInt(rs, "horse_id"));
        assignedTask.setStartTime(ResultSetUtil.getDate(rs, "start_time"));
        assignedTask.setDurTime(ResultSetUtil.getDate(rs, "due_time"));
        assignedTask.setStatus(ResultSetUtil.getInt(rs, "status"));
        assignedTask.setTaskId(ResultSetUtil.getInt(rs, "task_id"));
        assignedTask.setTaskName(ResultSetUtil.getString(rs, "task_name"));
        assignedTask.setAssignedUserId(ResultSetUtil.getInt(rs, "assigned_user_id"));
        return assignedTask;
    }
}


class HorseInfoMapper implements RowMapper {

    public HorseInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        HorseInfo horseInfo = new HorseInfo();
        horseInfo.setProductName(rs.getString("product.name"));
        horseInfo.setTargetName(rs.getString("target.short_name"));
        return horseInfo;
    }
}

class HorseInfoRowMapper implements RowMapper {

    public HorseInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        HorseInfo horse = new HorseInfo();
        horse.setId(ResultSetUtil.getInt(rs, "h.id"));
        horse.setProductId(ResultSetUtil.getInt(rs, "h.product_id"));
        horse.setTargetId(ResultSetUtil.getInt(rs, "h.target_id"));
        horse.setCompletionTime(ResultSetUtil.getDate(rs, "h.completion"));
        horse.setContentHeaderId(ResultSetUtil.getInt(rs, "h.content_header_id"));
        horse.setStartTime(ResultSetUtil.getDate(rs, "h.start_time"));
        horse.setTargetName(ResultSetUtil.getString(rs, "target_name"));
        horse.setStatus(ResultSetUtil.getInt(rs, "status"));
        switch (horse.getStatus()) {
            case Constants.WORKFLOW_OBJECT_STATUS_WAITING:
                horse.setStatusDisplay("INITIAL");
                break;
            case Constants.WORKFLOW_OBJECT_STATUS_STARTED:
                horse.setStatusDisplay("RUNNING");
                break;
            case Constants.WORKFLOW_OBJECT_STATUS_DONE:
                horse.setStatusDisplay("DONE");
                break;
            case Constants.WORKFLOW_OBJECT_STATUS_SUSPENDED:
                horse.setStatusDisplay("STOPPED");
                break;
            case Constants.WORKFLOW_OBJECT_STATUS_CANCELLED:
                horse.setStatusDisplay("CANCELLED");
                break;
            default:
                horse.setStatusDisplay("INITIAL");
        }
        return horse;
    }
}

class UserJoinHorse {

    private int userId;
    private int horseId;

    public int getHorseId() {
        return horseId;
    }

    public void setHorseId(int horseId) {
        this.horseId = horseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
