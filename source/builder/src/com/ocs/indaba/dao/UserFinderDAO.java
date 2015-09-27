/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Userfinder;
import com.ocs.indaba.vo.UserfinderVO;
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
public class UserFinderDAO extends SmartDaoMySqlImpl<Userfinder, Integer> {

    private static final Logger log = Logger.getLogger(UserFinderDAO.class);

    private static final String SQL_SELECT_ALL_USERFINDERS =
            "SELECT uf.*, u.first_name, u.last_name, r.name role_name, prj.code_name, prod.name prod_name "
            + "FROM userfinder uf "
            + "JOIN user u ON uf.assigned_user_id=u.id "
            + "JOIN role r ON uf.role_id=r.id "
            + "JOIN project prj ON uf.project_id=prj.id "
            + "LEFT JOIN product prod ON uf.product_id=prod.id "
            + "ORDER BY uf.last_update_time DESC";
    
    private static final String SQL_SELECT_ALL_USERFINDERS_BY_STATUS = 
            "SELECT uf.*, u.first_name, u.last_name, r.name role_name, prj.code_name, prod.name prod_name "
            + "FROM userfinder uf "
            + "JOIN user u ON uf.assigned_user_id=u.id "
            + "JOIN role r ON uf.role_id=r.id "
            + "JOIN project prj ON uf.project_id=prj.id "
            + "LEFT JOIN product prod ON uf.product_id=prod.id "
            + "WHERE uf.status=? "
            + "ORDER BY uf.last_update_time DESC";

    private static final String SQL_SELECT_ALL_USERFINDERS_BY_ID_AND_STATUS = 
            "SELECT uf.*, u.first_name, u.last_name, r.name role_name, prj.code_name, prod.name prod_name "
            + "FROM userfinder uf "
            + "JOIN user u ON uf.assigned_user_id=u.id "
            + "JOIN role r ON uf.role_id=r.id "
            + "JOIN project prj ON uf.project_id=prj.id "
            + "LEFT JOIN product prod ON uf.product_id=prod.id "
            + "WHERE uf.id=? AND uf.status=? "
            + "ORDER BY uf.last_update_time DESC";

    private static final String COUNT_OF_ALL_USERFINDERS_BY_PROJECTID = "SELECT COUNT(1) FROM userfinder WHERE project_id=?";

    private static final String SQL_SELECT_ALL_USERFINDERS_BY_PROJECTID =
            "SELECT uf.*, u.first_name, u.last_name, r.name role_name, prj.code_name, prod.name prod_name "
            + "FROM userfinder uf "
            + "JOIN user u ON uf.assigned_user_id=u.id "
            + "JOIN role r ON uf.role_id=r.id "
            + "JOIN project prj ON uf.project_id=prj.id "
            + "LEFT JOIN product prod ON uf.product_id=prod.id "
            + "WHERE (uf.status=1 OR uf.status=2) AND uf.project_id={0} "
            + "ORDER BY {1} LIMIT {2,number,#},{3,number,#}";

    private static final String EXISTS_USERFINDER_BY_PROJECTID_AND_ROLE_ID = 
            "SELECT COUNT(1) FROM userfinder WHERE project_id=? AND product_id=? AND role_id=? AND id != ?";

    public boolean existsUserfinderByProjectIdAndRoleId(int projectId, int productId, int roleId, int excludeUserFinderId) {
        return super.count(EXISTS_USERFINDER_BY_PROJECTID_AND_ROLE_ID, projectId, productId, roleId, excludeUserFinderId) > 0;
    }

    public int countOfAllUserfindersByProjectId(int projectId) {
        return (int) super.count(COUNT_OF_ALL_USERFINDERS_BY_PROJECTID, projectId);
    }

    public List<UserfinderVO> selectAllUserfindersByProjectId(int projectId, String sortName, String sortOrder, int offset, int count) {
        String sortStr = "";

        if ("roleName".equals(sortName)) {
            sortStr = "role_name " + sortOrder;
        } else if ("productName".equals(sortName)) {
            sortStr = "prod_name " + sortOrder;
        } else if ("assignedUsername".equals(sortName)) {
            sortStr = "u.last_name " + sortOrder + ", u.last_name " + sortOrder;
        } else if ("status".equals(sortName)) {
            sortStr = "uf.status " + sortOrder;
        } else if ("caseSubject".equals(sortName)) {
            sortStr = "uf.case_subject " + sortOrder;
        } else {
            sortStr = "uf.last_update_time " + sortOrder;
        }
        String sqlStr = MessageFormat.format(SQL_SELECT_ALL_USERFINDERS_BY_PROJECTID, projectId, sortStr, offset, count);
        logger.debug("Find all user finders[project_id=" + projectId + ", sortStr=" + sortStr + ", offset=" + offset + ", count=" + count + "]\n\t" + sqlStr);

        return getJdbcTemplate().query(sqlStr, new UserFinderMapper());
    }

    public List<UserfinderVO> selectAllUserfindersByStatus(int status) {
        logger.debug("Find all user finders.");

        return getJdbcTemplate().query(SQL_SELECT_ALL_USERFINDERS_BY_STATUS, new Object[]{status}, new UserFinderMapper());
    }

    public List<UserfinderVO> selectAllUserfindersByIdAndStatus(int id, int status) {
        logger.debug("Find all user finders by ID.");
        return getJdbcTemplate().query(SQL_SELECT_ALL_USERFINDERS_BY_ID_AND_STATUS, new Object[]{id, status}, new UserFinderMapper());
    }

    public List<UserfinderVO> selectAllUserfinders() {
        logger.debug("Find all user finders.");
        return getJdbcTemplate().query(SQL_SELECT_ALL_USERFINDERS, new UserFinderMapper());
    }

    class UserFinderMapper implements RowMapper {

        public UserfinderVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserfinderVO userfinder = new UserfinderVO();
            userfinder.setId(rs.getInt("id"));
            userfinder.setDescription(rs.getString("description"));
            userfinder.setAssignedUserId(rs.getInt("assigned_user_id"));
            userfinder.setAttachContent(rs.getBoolean("attach_content"));
            userfinder.setAttachUser(rs.getBoolean("attach_user"));
            userfinder.setCaseBody(rs.getString("case_body"));
            userfinder.setCaseSubject(rs.getString("case_subject"));
            userfinder.setCasePriority(rs.getShort("case_priority"));
            userfinder.setDeleteTime(rs.getDate("delete_time"));
            userfinder.setCreateTime(rs.getDate("create_time"));
            userfinder.setLastUpdateTime(rs.getDate("last_update_time"));
            userfinder.setProjectId(rs.getInt("project_id"));
            userfinder.setProductId(rs.getInt("product_id"));
            userfinder.setRoleId(rs.getInt("role_id"));
            userfinder.setStatus(rs.getShort("status"));
            userfinder.setAssignedUsername(rs.getString("first_name") + " " + rs.getString("last_name"));
            userfinder.setRoleName(rs.getString("role_name"));
            userfinder.setProjectName(rs.getString("code_name"));
            userfinder.setProductName(rs.getString("prod_name"));
            return userfinder;
        }
    }
}
