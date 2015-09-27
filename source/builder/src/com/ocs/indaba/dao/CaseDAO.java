/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Cases;
import com.ocs.indaba.vo.CaseInfo;
import com.ocs.indaba.vo.CaseStatus;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import static java.sql.Types.INTEGER;
import static java.sql.Types.BIGINT;
import static java.sql.Types.VARCHAR;

/**
 *
 * @author menglong
 */
public class CaseDAO extends SmartDaoMySqlImpl<Cases, Integer> {

    private static final Logger log = Logger.getLogger(CaseDAO.class);
    private static final String SQL_SELECT_CASE_BY_ID =
            " SELECT opened_by_user_id, assigned_user_id, opened_time, title, "
            + " description, priority, status, substatus, block_workflow, "
            + " block_publishing, project_id, product_id, horse_id, goal_id, "
            + " user_msgboard_id, staff_msgboard_id, last_updated_time  "
            + " FROM indaba.cases "
            + " WHERE id = ? ";
    private static final String SQL_SELECT_CASES_BY_HORSE_ID =
            "SELECT c.* FROM cases c " +
            "JOIN case_object co ON (c.id = co.cases_id AND co.object_type = 1) " +
            "JOIN content_header ch ON (ch.id = co.object_id) " +
            "JOIN horse h ON (h.id = ch.horse_id) " +
            "WHERE h.id = ? " +
            "ORDER BY c.id";
    private static final String SQL_SELECT_ALL_CASES_BY_RPJ_ID =
            " SELECT id, opened_by_user_id, assigned_user_id, opened_time, title, "
            + " description, priority, status, substatus, block_workflow, "
            + " block_publishing, project_id, product_id, horse_id, goal_id, "
            + " user_msgboard_id, staff_msgboard_id, last_updated_time  "
            + " FROM indaba.cases " 
            + " WHERE project_id = ? "
            + " ORDER BY status ASC, priority DESC";
    private static final String SQL_SELECT_OPEN_CASES_BY_OPEN_USER_ID =
            " SELECT id, assigned_user_id, opened_time, title, description, priority, block_workflow, "
            + " block_publishing, project_id, product_id, horse_id, goal_id,  user_msgboard_id,  "
            + " staff_msgboard_id, last_updated_time, substatus "
            + " FROM indaba.cases "
            + " WHERE  status = ? AND opened_by_user_id = ?"
            + " ORDER BY id";
    private static final String SQL_SELECT_OPEN_CASES_BY_ASSIGN_USER_ID =
            " SELECT id, opened_by_user_id, opened_time, title, description, priority, block_workflow, "
            + " block_publishing, project_id, product_id, horse_id, goal_id,  user_msgboard_id,  "
            + " staff_msgboard_id, last_updated_time, substatus "
            + " FROM indaba.cases "
            + " WHERE  status = ? AND assigned_user_id = ?"
            + " ORDER BY id";
    private static final String SQL_INSERT_CASE =
            " INSERT INTO cases "
            + " (opened_by_user_id, assigned_user_id, opened_time, title, "
            + " description, status, priority, block_workflow, block_publishing, "
            + " project_id, product_id, horse_id, last_updated_time) "
            + " VALUES "
            + " (?, ?, now(), ?, ?, ?, ?, ?, ?, ?, ?, ?, now()) ";
    private static final String SQL_UPDATE_CASE =
            "UPDATE cases "
            + " SET assigned_user_id =?, title = ?, "
            + "     description = ?, status = ?, priority = ?, block_workflow = ?, "
            + "     block_publishing = ?, last_updated_time = now() "
            + " WHERE id =? ";
    private static final String SQL_UPDATE_USER_MSG_BOARD_ID =
            "UPDATE cases SET user_msgboard_id = ? WHERE id = ? " ;
    private static final String SQL_UPDATE_STAFF_MSG_BOARD_ID =
            "UPDATE cases SET staff_msgboard_id = ? WHERE id = ? " ;
    private static final String SQL_SELECT_CASES_BY_USER_ID_AND_PRJ_ID =
            "SELECT distinct c.id, assigned_user_id, opened_by_user_id, opened_time, title, "
            + "description, priority, status, substatus, block_workflow, "
            + "block_publishing, project_id, product_id, horse_id, goal_id, "
            + "user_msgboard_id, staff_msgboard_id, last_updated_time  "
            + "FROM cases c LEFT JOIN case_object co ON (c.id = co.cases_id) "
            + "WHERE (assigned_user_id = ? "
            + "OR opened_by_user_id = ? "
            + "OR (co.object_id = ? AND co.object_type = 0)) "
            + "AND c.project_id = ? "
            + "ORDER BY status ASC, priority DESC";
    private static final String SQL_SELECT_OPEN_CASES_BY_USER_ID_AND_PRJ_ID =
            "SELECT distinct c.id, assigned_user_id, opened_by_user_id, opened_time, title, "
            + "description, priority, status, substatus, block_workflow, "
            + "block_publishing, project_id, product_id, horse_id, goal_id, "
            + "user_msgboard_id, staff_msgboard_id, last_updated_time  "
            + "FROM cases c LEFT JOIN case_object co ON (c.id = co.cases_id) "
            + "WHERE (assigned_user_id = ? "
            + "OR opened_by_user_id = ? "
            + "OR (co.object_id = ? AND co.object_type = 0)) "
            + "AND c.project_id = ? AND c.status=0 "
            + "ORDER BY status ASC, priority DESC";

    private static final String SELECT_USER_FINDER_CASES = 
            "SELECT * FROM cases WHERE horse_id=? AND project_id=? AND product_id=? "
            + "AND goal_id=? AND opened_by_user_id=? AND assigned_user_id=? AND "
            + "title=? AND description=?";

    public Cases selectCase(int horseId, int projectId, int productId, int goalId,
            int openedUserId, int assignedUserId, String title, String description) {
            return super.findSingle(SELECT_USER_FINDER_CASES,
                    new Object[]{horseId, projectId, productId, goalId, openedUserId, assignedUserId, title, description});
    }

    public Cases selectCaseById(final int caseId) {
        log.debug("Select case by case ID.");
        RowMapper mapper = new RowMapper() {

            public Cases mapRow(ResultSet rs, int rowNum) throws SQLException {
                Cases caseTmp = new Cases();
                caseTmp.setId(caseId);
                caseTmp.setOpenedByUserId(rs.getInt("opened_by_user_id"));
                caseTmp.setAssignedUserId(rs.getInt("assigned_user_id"));
                caseTmp.setOpenedTime(rs.getTimestamp("opened_time"));
                caseTmp.setTitle(rs.getString("title"));
                caseTmp.setDescription(rs.getString("description"));
                caseTmp.setStatus(rs.getShort("status"));
                caseTmp.setSubstatus(rs.getShort("substatus"));
                caseTmp.setPriority(rs.getShort("priority"));
                caseTmp.setBlockWorkflow(rs.getBoolean("block_workflow"));
                caseTmp.setBlockPublishing(rs.getBoolean("block_publishing"));
                caseTmp.setProjectId(rs.getInt("project_id"));
                caseTmp.setProductId(rs.getInt("product_id"));
                caseTmp.setHorseId(rs.getInt("horse_id"));
                caseTmp.setGoalId(rs.getInt("goal_id"));
                caseTmp.setUserMsgboardId(rs.getInt("user_msgboard_id"));
                caseTmp.setStaffMsgboardId(rs.getInt("staff_msgboard_id"));
                caseTmp.setLastUpdatedTime(rs.getTimestamp("last_updated_time"));
                return caseTmp;

            }
        };

        List<Cases> list = getJdbcTemplate().query(SQL_SELECT_CASE_BY_ID,
                new Object[]{caseId},
                new int[]{BIGINT},
                mapper);

        if (list != null && list.size() == 1) {
            return list.get(0);
        }

        return null;

    }

    public List<Cases> selectCasesByHorseId(final Integer horseId) {
        log.debug("Select cases by horseId: " + horseId + ". [" + SQL_SELECT_CASES_BY_HORSE_ID + "].");

        return super.find(SQL_SELECT_CASES_BY_HORSE_ID, horseId);
    }

    public List<Cases> selectAllCasesByProjectId(int projectId) {
        return super.find(SQL_SELECT_ALL_CASES_BY_RPJ_ID, projectId);
    }

    public List<Cases> selectOpenCasesByOpenUserId(final int userId) {
        logger.debug("Select open case by open user id.");
        RowMapper mapper = new RowMapper() {

            public Cases mapRow(ResultSet rs, int rowNum) throws SQLException {
                Cases caseTmp = new Cases();
                caseTmp.setId(rs.getInt("id"));
                caseTmp.setOpenedByUserId(userId);
                caseTmp.setAssignedUserId(rs.getInt("assigned_user_id"));
                caseTmp.setOpenedTime(rs.getTimestamp("opened_time"));
                caseTmp.setTitle(rs.getString("title"));
                caseTmp.setDescription(rs.getString("description"));
                caseTmp.setStatus(CaseStatus.OPENNEW.getStatusCode());
                caseTmp.setSubstatus(rs.getShort("substatus"));
                caseTmp.setPriority(rs.getShort("priority"));
                caseTmp.setBlockWorkflow(rs.getBoolean("block_workflow"));
                caseTmp.setBlockPublishing(rs.getBoolean("block_publishing"));
                caseTmp.setProjectId(rs.getInt("project_id"));
                caseTmp.setProductId(rs.getInt("product_id"));
                caseTmp.setHorseId(rs.getInt("horse_id"));
                caseTmp.setGoalId(rs.getInt("goal_id"));
                caseTmp.setUserMsgboardId(rs.getInt("user_msgboard_id"));
                caseTmp.setStaffMsgboardId(rs.getInt("staff_msgboard_id"));
                caseTmp.setLastUpdatedTime(rs.getTimestamp("last_updated_time"));
                return caseTmp;

            }
        };

        return getJdbcTemplate().query(SQL_SELECT_OPEN_CASES_BY_OPEN_USER_ID,
                new Object[]{CaseStatus.OPENNEW.getStatusCode(), userId},
                new int[]{INTEGER, BIGINT},
                mapper);

    }

    public List<Cases> selectOpenCasesByAssignUserId(final int userId) {
        logger.debug("Select open case by assign user id.");

        RowMapper mapper = new RowMapper() {

            public Cases mapRow(ResultSet rs, int rowNum) throws SQLException {
                Cases caseTmp = new Cases();
                caseTmp.setId(rs.getInt("id"));
                caseTmp.setOpenedByUserId(rs.getInt("opened_by_user_id"));
                caseTmp.setAssignedUserId(userId);
                caseTmp.setOpenedTime(rs.getTimestamp("opened_time"));
                caseTmp.setTitle(rs.getString("title"));
                caseTmp.setDescription(rs.getString("description"));
                caseTmp.setStatus(CaseStatus.OPENNEW.getStatusCode());
                caseTmp.setSubstatus(rs.getShort("substatus"));
                caseTmp.setPriority(rs.getShort("priority"));
                caseTmp.setBlockWorkflow(rs.getBoolean("block_workflow"));
                caseTmp.setBlockPublishing(rs.getBoolean("block_publishing"));
                caseTmp.setProjectId(rs.getInt("project_id"));
                caseTmp.setProductId(rs.getInt("product_id"));
                caseTmp.setHorseId(rs.getInt("horse_id"));
                caseTmp.setGoalId(rs.getInt("goal_id"));
                caseTmp.setUserMsgboardId(rs.getInt("user_msgboard_id"));
                caseTmp.setStaffMsgboardId(rs.getInt("staff_msgboard_id"));
                caseTmp.setLastUpdatedTime(rs.getTimestamp("last_updated_time"));
                return caseTmp;

            }
        };

        return getJdbcTemplate().query(SQL_SELECT_OPEN_CASES_BY_ASSIGN_USER_ID,
                new Object[]{CaseStatus.OPENNEW.getStatusCode(), userId},
                new int[]{INTEGER, BIGINT},
                mapper);

    }

    public long insertCase(CaseInfo caseInfo) {
        logger.debug("insert case.");
        final int openedUserId = caseInfo.getOpenedByUserId();
        final int assignUserId = caseInfo.getAssignedUserId();
        final String title = caseInfo.getTitle();
        final String description = caseInfo.getDescription();
        final Short statusCode = caseInfo.getStatusCode();
        final Short priorityCode = caseInfo.getPriorityCode();
        final Boolean blockWorkFlow = caseInfo.getBlockWorkFlow();
        final Boolean blockPulishing = caseInfo.getBlockPulishing();
        final Integer projectId = caseInfo.getProjectId();
        final Integer productId = caseInfo.getProductId();
        final Integer horseId = caseInfo.getHorseId();

        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement prepareStatement =
                        conn.prepareStatement(SQL_INSERT_CASE, Statement.RETURN_GENERATED_KEYS);
                if (openedUserId != 0) {
                    prepareStatement.setLong(1, openedUserId);
                } else {
                    prepareStatement.setNull(1, BIGINT);
                }
                if (assignUserId != 0) {
                    prepareStatement.setLong(2, assignUserId);
                } else {
                    prepareStatement.setNull(2, BIGINT);
                }
                if (title != null) {
                    prepareStatement.setString(3, title);
                } else {
                    prepareStatement.setNull(3, VARCHAR);
                }
                if (description != null) {
                    prepareStatement.setString(4, description);
                } else {
                    prepareStatement.setNull(4, VARCHAR);
                }
                prepareStatement.setShort(5, statusCode);
                prepareStatement.setShort(6, priorityCode);
                prepareStatement.setBoolean(7, blockWorkFlow);
                prepareStatement.setBoolean(8, blockPulishing);
                prepareStatement.setInt(9, projectId);
                if (productId != null) {
                    prepareStatement.setInt(10, productId);
                } else {
                    prepareStatement.setNull(10, INTEGER);
                }

                if (horseId != null) {
                    prepareStatement.setInt(11, horseId);
                } else {
                    prepareStatement.setNull(11, INTEGER);
                }
                return prepareStatement;
            }
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public int updateCase(CaseInfo caseInfo) {
        logger.debug("update case.");

        return getJdbcTemplate().update(
                SQL_UPDATE_CASE,
                new Object[]{caseInfo.getAssignedUserId(), caseInfo.getTitle(),
                    caseInfo.getDescription(), caseInfo.getStatusCode(),
                    caseInfo.getPriorityCode(), caseInfo.getBlockWorkFlow(),
                    caseInfo.getBlockPulishing(), caseInfo.getCaseId()},
                new int[]{BIGINT, VARCHAR,
                    VARCHAR, INTEGER,
                    INTEGER, INTEGER,
                    INTEGER, BIGINT});
    }

    public List<Cases> searchCasesByFilter(int projectId, List<Integer> statusFilter) {
        
        RowMapper mapper = new RowMapper() {

            public Cases mapRow(ResultSet rs, int rowNum) throws SQLException {
                Cases caseTmp = new Cases();
                caseTmp.setId(rs.getInt("id"));
                caseTmp.setOpenedByUserId(rs.getInt("opened_by_user_id"));
                caseTmp.setAssignedUserId(rs.getInt("assigned_user_id"));
                caseTmp.setOpenedTime(rs.getTimestamp("opened_time"));
                caseTmp.setTitle(rs.getString("title"));
                caseTmp.setDescription(rs.getString("description"));
                caseTmp.setStatus(rs.getShort("status"));
                caseTmp.setSubstatus(rs.getShort("substatus"));
                caseTmp.setPriority(rs.getShort("priority"));
                caseTmp.setBlockWorkflow(rs.getBoolean("block_workflow"));
                caseTmp.setBlockPublishing(rs.getBoolean("block_publishing"));
                caseTmp.setProjectId(rs.getInt("project_id"));
                caseTmp.setProductId(rs.getInt("product_id"));
                caseTmp.setHorseId(rs.getInt("horse_id"));
                caseTmp.setGoalId(rs.getInt("goal_id"));
                caseTmp.setUserMsgboardId(rs.getInt("user_msgboard_id"));
                caseTmp.setStaffMsgboardId(rs.getInt("staff_msgboard_id"));
                caseTmp.setLastUpdatedTime(rs.getTimestamp("last_updated_time"));
                return caseTmp;
            }
        };
        
        if (statusFilter.size() == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT DISTINCT c.* FROM cases c WHERE c.project_id = ? AND (");

        int i;
        for (i = 0; i < statusFilter.size() - 1; i ++) {
            sb.append("c.status = " + statusFilter.get(i) + " OR ");
        }
        sb.append("c.status = " + statusFilter.get(i) + ")");
        
        return getJdbcTemplate().query(sb.toString(),
                new Object[]{projectId}, new int[]{INTEGER},
                mapper);
    }

    public int updateUserMsgBoardId(Integer userMsgBoardId, Long caseId) {
        return getJdbcTemplate().update(
                SQL_UPDATE_USER_MSG_BOARD_ID,
                new Object[]{userMsgBoardId, caseId},
                new int[]{INTEGER, BIGINT});
    }

    public int updateStaffMsgBoardId(Integer staffMsgBoardId, Long caseId) {
        return getJdbcTemplate().update(
                SQL_UPDATE_STAFF_MSG_BOARD_ID,
                new Object[]{staffMsgBoardId, caseId},
                new int[]{INTEGER, BIGINT});
    }
    
    public List<Cases> selectCasesByAssociatedUserIdAndProjectId(final int userId, final int projectId) {

        RowMapper mapper = new RowMapper() {

            public Cases mapRow(ResultSet rs, int rowNum) throws SQLException {
                Cases caseTmp = new Cases();
                caseTmp.setId(rs.getInt("id"));
                caseTmp.setAssignedUserId(rs.getInt("assigned_user_id"));
                caseTmp.setOpenedByUserId(rs.getInt("opened_by_user_id"));
                caseTmp.setOpenedTime(rs.getTimestamp("opened_time"));
                caseTmp.setTitle(rs.getString("title"));
                caseTmp.setDescription(rs.getString("description"));
                caseTmp.setStatus(rs.getShort("status"));
                caseTmp.setSubstatus(rs.getShort("substatus"));
                caseTmp.setPriority(rs.getShort("priority"));
                caseTmp.setBlockWorkflow(rs.getBoolean("block_workflow"));
                caseTmp.setBlockPublishing(rs.getBoolean("block_publishing"));
                caseTmp.setProjectId(rs.getInt("project_id"));
                caseTmp.setProductId(rs.getInt("product_id"));
                caseTmp.setHorseId(rs.getInt("horse_id"));
                caseTmp.setGoalId(rs.getInt("goal_id"));
                caseTmp.setUserMsgboardId(rs.getInt("user_msgboard_id"));
                caseTmp.setStaffMsgboardId(rs.getInt("staff_msgboard_id"));
                caseTmp.setLastUpdatedTime(rs.getTimestamp("last_updated_time"));
                return caseTmp;
            }
        };

        List<Cases> list = getJdbcTemplate().query(SQL_SELECT_CASES_BY_USER_ID_AND_PRJ_ID,
                new Object[]{userId, userId, userId, projectId},
                new int[]{BIGINT, BIGINT, BIGINT, BIGINT},
                mapper);
//        if (list == null || list.isEmpty())
//            list= getJdbcTemplate().query(SQL_SELECT_CASES_BY_USER,
//                new Object[]{userId, userId},
//                new int[]{BIGINT, BIGINT},
//                mapper);
        return list;
    }

    public List<Cases> selectOpenCasesByUserIdAndProjectId(final int userId, final int projectId) {

        RowMapper mapper = new RowMapper() {

            public Cases mapRow(ResultSet rs, int rowNum) throws SQLException {
                Cases caseTmp = new Cases();
                caseTmp.setId(rs.getInt("id"));
                caseTmp.setAssignedUserId(rs.getInt("assigned_user_id"));
                caseTmp.setOpenedByUserId(rs.getInt("opened_by_user_id"));
                caseTmp.setOpenedTime(rs.getTimestamp("opened_time"));
                caseTmp.setTitle(rs.getString("title"));
                caseTmp.setDescription(rs.getString("description"));
                caseTmp.setStatus(rs.getShort("status"));
                caseTmp.setSubstatus(rs.getShort("substatus"));
                caseTmp.setPriority(rs.getShort("priority"));
                caseTmp.setBlockWorkflow(rs.getBoolean("block_workflow"));
                caseTmp.setBlockPublishing(rs.getBoolean("block_publishing"));
                caseTmp.setProjectId(rs.getInt("project_id"));
                caseTmp.setProductId(rs.getInt("product_id"));
                caseTmp.setHorseId(rs.getInt("horse_id"));
                caseTmp.setGoalId(rs.getInt("goal_id"));
                caseTmp.setUserMsgboardId(rs.getInt("user_msgboard_id"));
                caseTmp.setStaffMsgboardId(rs.getInt("staff_msgboard_id"));
                caseTmp.setLastUpdatedTime(rs.getTimestamp("last_updated_time"));
                return caseTmp;
            }
        };

        List<Cases> list = getJdbcTemplate().query(SQL_SELECT_OPEN_CASES_BY_USER_ID_AND_PRJ_ID,
                new Object[]{userId, userId, userId, projectId},
                new int[]{BIGINT, BIGINT, BIGINT, BIGINT},
                mapper);
//        if (list == null || list.isEmpty())
//            list= getJdbcTemplate().query(SQL_SELECT_CASES_BY_USER,
//                new Object[]{userId, userId},
//                new int[]{BIGINT, BIGINT},
//                mapper);
        return list;
    }
}
