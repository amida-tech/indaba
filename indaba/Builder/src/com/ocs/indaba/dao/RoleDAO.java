/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Role;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;

import static java.sql.Types.INTEGER;

/**
 *
 * @author menglong luwb
 */
public class RoleDAO extends SmartDaoMySqlImpl<Role, Integer> {

    private static final String SQL_SELECT_ROLE_BY_PROJECT_ID_AND_USER_ID =
            " SELECT r.name FROM role r "
            + " JOIN project_membership pm ON r.id = pm.role_id "
            + " JOIN user u ON u.id = pm.user_id "
            + " WHERE pm.project_id = ? "
            + " AND u.id = ?";
    private static final String SQL_SELECT_ROLE_BY_PROJECT_ID =
            "SELECT r.id, r.name FROM role r "
            + " INNER JOIN project_roles pr ON r.id = pr.role_id "
            + " WHERE pr.project_id = ? ORDER BY r.name";
    private final String SELECT_ROLEID_BY_PROJECT_ID_AND_USER_ID =
            "SELECT role_id from project_membership where project_id=? and user_id=?";
    private final String SELECT_ROLE_BY_PROJECT_ID_AND_USER_ID =
            "SELECT r.* FROM project_membership prjm, role r "
            + "WHERE prjm.project_id=? AND prjm.user_id=? AND prjm.role_id=r.id";
    private final String SELECT_ROLEID_BY_TASK_ID =
            "SELECT role_id from task_role where task_id=?";

    /*to find the intersection of roles that between the roles related to task id
    and the input role ids(in queue funtion, the input role ids are the roles user takes
    in the project)*/
    private final String SELECT_ROLEID_BY_TASK_ID_AND_ROLE_IDS =
            "SELECT role_id from task_role where task_id=? and role_id in";
    private final String SELECT_CLAIMABLE_ROLEID_BY_TASK_ID_AND_ROLE_IDS =
            "SELECT role_id from task_role where task_id=? AND can_claim != 0 AND role_id in";
    private final String SELECT_ROLES_BY_PRODUCT_ID =
            "SELECT DISTINCT r.* FROM role r JOIN task_role tr ON tr.role_id=r.id "
            + "JOIN task t ON t.id=tr.task_id WHERE t.product_id=?";

    public List<Role> selectRoleByProductId(int productId) {
        return super.find(SELECT_ROLES_BY_PRODUCT_ID, productId);
    }

    public Role selectRoleByProjectIdAndUserId(int projectId, int userId) {
        logger.debug("Select role in project.");
        return super.findSingle(SELECT_ROLE_BY_PROJECT_ID_AND_USER_ID, projectId, userId);
    }

    public String selectRoleNameByProjectIdAndUserId(Integer projectId, int userId) {
        logger.debug("Select users by team ID.");
        RowMapper mapper = new RowMapper() {

            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("name");
            }
        };

        List<String> list = getJdbcTemplate().query(
                SQL_SELECT_ROLE_BY_PROJECT_ID_AND_USER_ID,
                new Object[]{projectId, userId},
                new int[]{INTEGER, INTEGER},
                mapper);
        return (list.size() > 0) ? list.get(0) : "";

    }

    public List<Role> selectRolesByProjectId(int prjectid) {
        return find(SQL_SELECT_ROLE_BY_PROJECT_ID, prjectid);
    }

    public List<Integer> selectRoleIdByProjectIdAndUserId(int projectId, int userId) {
        logger.debug("Select Role Id by Project Id " + projectId + " and User Id " + userId);
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("role_id");
            }
        };

        List<Integer> list = getJdbcTemplate().query(
                SELECT_ROLEID_BY_PROJECT_ID_AND_USER_ID,
                new Object[]{projectId, userId},
                new int[]{INTEGER, INTEGER},
                mapper);
        return list;

    }

    public List<Integer> selectRoleIdByTaskId(int taskId) {
        logger.debug("Select Role Id by Task Id.");
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("role_id");
            }
        };

        List<Integer> list = getJdbcTemplate().query(
                SELECT_ROLEID_BY_TASK_ID,
                new Object[]{taskId},
                new int[]{INTEGER},
                mapper);
        return list;

    }

    public List<Integer> selectRoleIdByTaskIdAndRoleIds(int taskId, String strRoleIds) {
        logger.debug("Select Role Id by Task Id " + taskId + " Role Ids " + strRoleIds);
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("role_id");
            }
        };
        String sql = SELECT_ROLEID_BY_TASK_ID_AND_ROLE_IDS + "(" + strRoleIds + ")";
        List<Integer> list = getJdbcTemplate().query(
                sql,
                new Object[]{taskId},
                new int[]{INTEGER},
                mapper);
        return list;

    }

    public List<Integer> selectClaimableRoleIdByTaskIdAndRoleIds(int taskId, String strRoleIds) {
        logger.debug("Select Claimable Role Id by Task Id " + taskId + " Role Ids " + strRoleIds);
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("role_id");
            }
        };
        String sql = SELECT_CLAIMABLE_ROLEID_BY_TASK_ID_AND_ROLE_IDS + "(" + strRoleIds + ")";
        List<Integer> list = getJdbcTemplate().query(
                sql,
                new Object[]{taskId},
                new int[]{INTEGER},
                mapper);
        return list;

    }
}
