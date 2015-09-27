/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.AccessPermission;
import com.ocs.indaba.po.Rights;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import static java.sql.Types.INTEGER;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Jeff
 */
public class AccessPermissionDAO extends SmartDaoMySqlImpl<AccessPermission, Integer> {

    private static final Logger log = Logger.getLogger(AccessPermissionDAO.class);
    private static final String SQL_SELECT_PERMISSION_BY_GOAL_ID =
            "SELECT DISTINCT ap.* FROM project_membership pm, goal g, access_permission ap, rights r "
            + "WHERE pm.project_id=? AND pm.user_id=? AND pm.role_id=ap.role_id AND g.id=? AND r.id=? AND "
            + "r.id=ap.rights_id AND g.access_matrix_id=ap.access_matrix_id";
    private static final String SQL_SELECT_PERMISSION_BY_PRODUCT_ID =
            "SELECT DISTINCT ap.* FROM project_membership pm, product prd, access_permission ap, rights r "
            + "WHERE pm.project_id=? AND pm.user_id=? AND pm.role_id=ap.role_id AND prd.id=? AND r.id=? AND "
            + "r.id=ap.rights_id AND prd.access_matrix_id=ap.access_matrix_id";
    private static final String SQL_SELECT_PERMISSION_BY_PROJECT_ID =
            "SELECT DISTINCT ap.* FROM project prj, project_membership pm, access_permission ap, rights r "
            + "WHERE prj.id=? AND prj.id=pm.project_id AND pm.user_id=? AND pm.role_id=ap.role_id AND r.id=? AND "
            + "r.id=ap.rights_id AND prj.access_matrix_id=ap.access_matrix_id";
    private static final String SQL_SELECT_PERMISSION_BY_TOOL_AND_RIGHT =
            "SELECT DISTINCT ap.* FROM project_membership pm, access_permission ap, rights r, tool t "
            + "WHERE pm.project_id=? AND pm.user_id=? AND t.name=? AND t.access_matrix_id=ap.access_matrix_id "
            + "AND pm.role_id=ap.role_id AND r.name=? AND r.id=ap.rights_id";
    private static final String SQL_SELECT_PERMISSION_BY_RIGHT =
            "SELECT DISTINCT ap.* FROM project_membership pm, access_permission ap, rights r "
            + "WHERE pm.project_id=? AND pm.user_id=? AND pm.role_id=ap.role_id AND r.name=? AND r.id=ap.rights_id";
    private static final String SQL_SELECT_PERMISSION =
            "SELECT ap.* FROM access_permission ap, rights r "
            + "WHERE ap.access_matrix_id=? AND ap.role_id=? AND ap.rights_id=r.id AND r.name=?";
    private static final String SELECT_USER_RIGHTS_BY_PROJECT_ID =
            "SELECT r.* FROM project_membership pm, access_permission ap, rights r "
            + "WHERE pm.user_id=? AND pm.project_id=? AND pm.role_id=ap.role_id AND ap.rights_id=r.id";

    public List<Rights> selectUserRightsByProjectId(int userId, int projectId) {
        logger.debug("Select Role Id by Project Id " + projectId + " and User Id " + userId);
        RowMapper mapper = new RowMapper() {

            public Rights mapRow(ResultSet rs, int rowNum) throws SQLException {
                Rights r = new Rights();
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
                r.setLabel(rs.getString("label"));
                return r;
            }
        };

        List<Rights> list = getJdbcTemplate().query(
                SELECT_USER_RIGHTS_BY_PROJECT_ID,
                new Object[]{userId, projectId},
                new int[]{INTEGER, INTEGER},
                mapper);
        return list;
    }

    public List<AccessPermission> selectGoalAccessPermission(int projectId, int goalId, int rightId, int userId) {
        log.debug("Select goal access permission.");
        return super.find(SQL_SELECT_PERMISSION_BY_GOAL_ID, (Object) projectId, userId, goalId, rightId);
    }

    public List<AccessPermission> selectProductAccessPermission(int projectId, int productId, int rightId, int userId) {
        log.debug("Select product access permission.");
        return super.find(SQL_SELECT_PERMISSION_BY_PRODUCT_ID, (Object) projectId, userId, productId, rightId);
    }

    public List<AccessPermission> selectProjectAccessPermission(int projectId, int rightId, int userId) {
        log.debug("Select project access permission.");
        return super.find(SQL_SELECT_PERMISSION_BY_PROJECT_ID, (Object) projectId, userId, rightId);
    }

    public AccessPermission selectAccessPermission(int projectId, int userId, String rightName, String toolName) {
        log.debug("Select project access permission.");
        return super.findSingle(SQL_SELECT_PERMISSION_BY_TOOL_AND_RIGHT, projectId, userId, toolName, rightName);
    }

    public AccessPermission selectAccessPermission(int accessMatrixId, int roleId, String rightName) {
        log.debug("Select project access permission[accessMatrixId=" + accessMatrixId + ", roleId=" + roleId + ", rightName=" + rightName + "].");
        return super.findSingle(SQL_SELECT_PERMISSION, (Object) accessMatrixId, roleId, rightName);
    }

    public AccessPermission selectAccessPermissionByUser(int projectId, int userId, String rightName) {
        log.debug("Select project access permission[projectId=" + projectId + ", userId=" + userId + ", rightName=" + rightName + "].");
        return super.findSingle(SQL_SELECT_PERMISSION_BY_RIGHT, (Object) projectId, userId, rightName);
    }
}
