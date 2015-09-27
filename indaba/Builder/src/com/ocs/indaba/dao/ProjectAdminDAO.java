/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.ProjectAdmin;
import com.ocs.indaba.vo.ProjectAdminVO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Jeff
 */
public class ProjectAdminDAO extends SmartDaoMySqlImpl<ProjectAdmin, Integer> {

    private static final Logger log = Logger.getLogger(ProjectAdminDAO.class);
    private static final String SELECT_PROJECT_ADMINS =
            "SELECT pa.project_id project_id, pa.user_id user_id, u.first_name first_name, u.last_name last_name "
            + "FROM project_admin pa, user u "
            + "WHERE pa.project_id in {0} AND pa.user_id=u.id "
            + "ORDER BY pa.id ASC";

    private static final String DELETE_BY_PROJECT_ID =
            "DELETE FROM project_admin WHERE project_id=?";
    
     private static final String DELETE_BY_PROJECT_ID_AND_USER_ID =
            "DELETE FROM project_admin WHERE project_id=? AND user_id=?";

    private static final String EXISTS_PROJECT_ADMIN_BY_USERID_AND_PROJID =
            "SELECT COUNT(1) FROM project p "
            + "WHERE (p.owner_user_id=? AND p.id=?) OR (SELECT COUNT(1) FROM project_admin pa WHERE (pa.user_id=? AND pa.project_id=?)) > 0";

    private static final String SELECT_BY_PROJECT_ID =
            "SELECT * FROM project_admin WHERE project_id=?";

    private static final String SELECT_PROJECT_ADMIN_BY_PROJECT_AND_USER = "SELECT * FROM project_admin WHERE project_id=? AND user_id=?";

    public boolean existsByUserIdAndOrgId(int userId, int projId) {
        return count(EXISTS_PROJECT_ADMIN_BY_USERID_AND_PROJID, userId, projId, userId, projId) > 0;
    }

    public void deleteByProjectId(int projId) {
        super.delete(DELETE_BY_PROJECT_ID, projId);
    }

    public List<ProjectAdminVO> selectProjectAdmins(List<Integer> projectIds) {
        if (projectIds == null || projectIds.isEmpty()) {
            return null;
        }
        RowMapper mapper = new RowMapper() {

            public ProjectAdminVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                ProjectAdminVO padmin = new ProjectAdminVO();
                padmin.setProjId(rs.getInt("project_id"));
                padmin.setUserId(rs.getInt("user_id"));
                padmin.setFirstName(rs.getString("first_name"));
                padmin.setLastName(rs.getString("last_name"));
                return padmin;
            }
        };
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (int prjId : projectIds) {
            sb.append(prjId).append(',');
        }
        sb.setLength(sb.length() - 1);
        sb.append(')');
        logger.debug(MessageFormat.format(SELECT_PROJECT_ADMINS, sb.toString()));
        return getJdbcTemplate().query(MessageFormat.format(SELECT_PROJECT_ADMINS, sb.toString()), null, null, mapper);
    }

    public List<ProjectAdmin> selectProjectAdminByProjectId(int projectId){
        return super.find(SELECT_BY_PROJECT_ID, projectId);
    }
    
    public ProjectAdmin selectProjectAdminByProjectAndUser(int projectId, int userId){
        return super.findSingle(SELECT_PROJECT_ADMIN_BY_PROJECT_AND_USER, projectId, userId);
    }

    public void deleteProjectAdmin(int projectId, int userId) {
        super.delete(DELETE_BY_PROJECT_ID_AND_USER_ID, projectId, userId);
    }
}
