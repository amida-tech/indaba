/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.ProjectMembership;
import com.ocs.indaba.po.User;
import com.ocs.indaba.vo.ProjectMemberShipVO;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Types.INTEGER;
import static java.sql.Types.VARCHAR;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.drools.util.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author Jeff
 */
public class ProjectMembershipDAO extends SmartDaoMySqlImpl<ProjectMembership, Integer> {

    private static final Logger log = Logger.getLogger(ProjectMembershipDAO.class);
    private static final String SELECT_PROJECT_MEMBERSHIP =
            "SELECT * FROM project_membership WHERE project_id=? AND user_id=?";
    private static final String SELECT_PROJECT_MEMBERSHIPS_BY_PROJECT_ID =
            "SELECT pm.id id, u.id user_id, u.status user_status, u.first_name first_name, u.last_name last_name, u.email email, u.username username, r.id role_id, r.name role_name, o.id org_id, o.name org_name  "
            + "FROM project_membership pm, user u, role r, organization o "
            + "WHERE pm.user_id=u.id AND pm.role_id = r.id AND pm.project_id=? AND u.organization_id=o.id "
            + "ORDER BY u.first_name, u.last_name";
    private static final String SELECT_PAGED_PROJECT_MEMBERSHIPS_BY_PROJECT_ID =
            "SELECT pm.id id, u.id user_id, u.status user_status, u.first_name first_name, u.last_name last_name, u.email email, u.username username, r.id role_id, r.name role_name, o.id org_id, o.name org_name  "
            + "FROM project_membership pm, user u, role r, organization o "
            + "WHERE pm.user_id=u.id AND pm.role_id = r.id AND pm.project_id=? AND u.organization_id=o.id "
            + "ORDER BY {0} {1} LIMIT {2},{3}";
    private static final String SELECT_PROJECT_MEMBERSHIPS_BY_PROJECT_ID_AND_ROLE_ID =
            "SELECT pm.id id, u.id user_id, u.status user_status, u.first_name first_name, u.last_name last_name, u.email email, u.username username, r.id role_id, r.name role_name, o.id org_id, o.name org_name  "
            + "FROM project_membership pm, user u, role r, organization o "
            + "WHERE pm.user_id=u.id AND pm.role_id = r.id AND pm.project_id=? AND pm.role_id=? AND u.organization_id=o.id "
            + "ORDER BY {0} {1} LIMIT {2},{3}";
    private static final String SELECT_PROJECT_MEMBERSHIP_COUNT_BY_FILTER_PREFIX =
            "SELECT COUNT(*) count "
            + "FROM project_membership pm, user u, role r "
            + "WHERE pm.user_id=u.id AND pm.role_id = r.id ";
    private static final String SELECT_PROJECT_MEMBERSHIP_BY_FILTER_PREFIX =
            "SELECT pm.id id, u.id user_id, u.status user_status, u.first_name first_name, u.last_name last_name, u.email email, u.username username, r.id role_id, r.name role_name  "
            + "FROM project_membership pm, user u, role r "
            + "WHERE pm.user_id=u.id AND pm.role_id = r.id ";
    private static final String SELECT_PROJECT_MEMBERSHIP_BY_FILTER_SUFFIX = " ORDER BY {0} {1} LIMIT {2},{3}";
    private static final Map<String, String> filterName2Field = new HashMap<String, String>();
    private static final Map<String, Integer> filterName2Type = new HashMap<String, Integer>();

    static {
        filterName2Field.put("projectid", "pm.project_id");
        filterName2Type.put("projectid", new Integer(INTEGER));
        filterName2Field.put("roleid", "pm.role_id");
        filterName2Type.put("roleid", new Integer(INTEGER));
        filterName2Field.put("email", "u.email");
        filterName2Type.put("email", new Integer(VARCHAR));
        filterName2Field.put("firstname", "u.first_name");
        filterName2Type.put("firstname", new Integer(VARCHAR));
        filterName2Field.put("lastname", "u.last_name");
        filterName2Type.put("lastname", new Integer(VARCHAR));
    }
    private static final String SELECT_PROJECT_MEMBERSHIP_COUNT_BY_PROJECT_ID =
            "SELECT count(pm.id)  "
            + "FROM project_membership pm "
            + "WHERE pm.project_id=? ";
    private static final String SELECT_PROJECT_MEMBERSHIP_COUNT_BY_PROJECT_ID_AND_ROLE_ID =
            "SELECT count(pm.id)  "
            + "FROM project_membership pm "
            + "WHERE pm.project_id=? AND pm.role_id=?";
    private static final String SELECT_USERIDS_BY_PROJECT_ID =
            "SELECT user_id  "
            + "FROM project_membership "
            + "WHERE project_id=? ";
    private static final String SELECT_PROJECT_MEMBERSHIP_BY_PROJECT_AND_ROLE =
            "SELECT * FROM project_membership WHERE project_id=? AND role_id=?";
    private static final String SELECT_PROJECT_MEMBERSHIP_BY_ID =
            "SELECT * FROM project_membership WHERE id=?";

    public ProjectMembership selectProjectMembershipById(int pmId) {
        return super.findSingle(SELECT_PROJECT_MEMBERSHIP_BY_ID, pmId);
    }

    public List<Integer> selectUserIdsByProjectId(int projId) {
        return getJdbcTemplate().query(SELECT_USERIDS_BY_PROJECT_ID, new Object[]{projId}, new int[]{INTEGER}, new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("user_id");
            }
        });
    }

    public List<ProjectMemberShipVO> selectProjectMembershipsByProjectId(int projectId) {
        return super.getJdbcTemplate().query(SELECT_PROJECT_MEMBERSHIPS_BY_PROJECT_ID, new Object[]{projectId}, new ProjectMembershipMapper());
    }

    public ProjectMembership selectProjectMembership(int projectId, int userId) {
        log.debug("Select project: " + SELECT_PROJECT_MEMBERSHIP + "[projectId=" + projectId + ", userId=" + userId + "].");

        return super.findSingle(SELECT_PROJECT_MEMBERSHIP, projectId, userId);
    }

    public long selectProjectMembershipCountByProjectId(int projectId) {
        return super.count(SELECT_PROJECT_MEMBERSHIP_COUNT_BY_PROJECT_ID, projectId);
    }

    public long selectProjectMembershipCountByProjectIdAndRoleId(int projectId, int roleId) {
        if (roleId == 0) {
            // if role id equals to 0, it means selecting members of all roles
            return selectProjectMembershipCountByProjectId(projectId);
        } else {
            return super.count(SELECT_PROJECT_MEMBERSHIP_COUNT_BY_PROJECT_ID_AND_ROLE_ID, projectId, roleId);
        }
    }

    public List<ProjectMemberShipVO> selectProjectMembershipsByProjectIdAndRoleId(int projectId, int roleId, String sortName, String sortOrder, int offset, int count) {
        String sql = (roleId == 0) ? SELECT_PAGED_PROJECT_MEMBERSHIPS_BY_PROJECT_ID : SELECT_PROJECT_MEMBERSHIPS_BY_PROJECT_ID_AND_ROLE_ID;
        Object[] params = (roleId == 0) ? new Object[]{projectId} : new Object[]{projectId, roleId};
        int[] paramTypes = (roleId == 0) ? new int[]{INTEGER} : new int[]{INTEGER, INTEGER};
        return findProjectMemberships(sql, params, paramTypes, sortName, sortOrder, offset, count);
    }

    public List<ProjectMemberShipVO> selectProjectMembershipsByProjectId(int projectId, String sortName, String sortOrder, int offset, int count) {
        return selectProjectMembershipsByProjectIdAndRoleId(projectId, 0, sortName, sortOrder, offset, count);
    }

    public List<ProjectMembership> selectProjectMembershipByProjectAndRole(int projectId, int roleId) {
        return super.find(SELECT_PROJECT_MEMBERSHIP_BY_PROJECT_AND_ROLE, new Object[]{projectId, roleId});
    }

    private List<ProjectMemberShipVO> findProjectMemberships(String sql, Object[] params, int[] paramTypes, String sortName, String sortOrder, int offset, int count) {
        if ("USERNAME".equalsIgnoreCase(sortName)) {
            sortName = "last_name";
        } else if ("EMAIL".equalsIgnoreCase(sortName)) {
            sortName = "email";
        } else if ("status".equalsIgnoreCase(sortName)) {
            sortName = "user_status";
        } else {
            sortName = "last_name";
        }
        String sqlStr = MessageFormat.format(sql, sortName, sortOrder, offset, count);
        logger.debug("Select project memberships" + sqlStr);

        return getJdbcTemplate().query(sqlStr, params, paramTypes, new ProjectMembershipMapper());
    }

    private void preprocessFilter(Map<String, Object> filters) {
        boolean removeRoleFilter = false;
        for (Entry<String, Object> e : filters.entrySet()) {
            if ("roleid".equals(e.getKey().toLowerCase())) {
                try {
                    Integer roleId = (Integer) e.getValue();
                    if (roleId.intValue() <= 0) {
                        removeRoleFilter = true;
                    }
                } catch (Exception except) {
                    // not numeric, remove it
                    removeRoleFilter = true;
                }
            }
        }

        if (removeRoleFilter == true) {
            filters.remove("roleid");
        }
    }

    private String prepareDynamicSQL(Map<String, Object> filters) {
        StringBuilder sqlBuf = new StringBuilder();
        for (Entry<String, Object> e : filters.entrySet()) {
            if ("username".equals(e.getKey().toLowerCase())) {
                // this needs special treatment
                String value = (String) e.getValue();
                sqlBuf.append(" AND (first_name LIKE '%").append(value).append("%' OR last_name LIKE '%").append(value).append("%')");
            } else if (filterName2Field.containsKey(e.getKey().toLowerCase())) {
                Integer type = filterName2Type.get(e.getKey().toLowerCase());

                if (type.intValue() == VARCHAR) {
                    String value = (String) e.getValue();
                    sqlBuf.append(" AND ").append(filterName2Field.get(e.getKey().toLowerCase())).append(" LIKE '%").append(value).append("%'");
                } else {
                    sqlBuf.append(" AND ").append(filterName2Field.get(e.getKey().toLowerCase())).append("=").append(e.getValue().toString());
                }
            }
        }
        return sqlBuf.toString();
    }

    public long selectProjectMembershipCountByFilter(Map<String, Object> filters) {
        preprocessFilter(filters);
        // prepare SQL statement
        StringBuffer sqlBuf = new StringBuffer(SELECT_PROJECT_MEMBERSHIP_COUNT_BY_FILTER_PREFIX);
        sqlBuf.append(prepareDynamicSQL(filters));

        logger.debug("select project membership count SQL: " + sqlBuf.toString());
        // execute and return results
        return super.count(sqlBuf.toString());
    }

    public List<ProjectMemberShipVO> findProjectMembershipByFilter(Map<String, Object> filters, String sortName, String sortOrder, int offset, int count) {
        preprocessFilter(filters);
        // prepare SQL statement
        StringBuilder sqlBuf = new StringBuilder(SELECT_PROJECT_MEMBERSHIP_BY_FILTER_PREFIX);
        sqlBuf.append(prepareDynamicSQL(filters));

        if ("displayUserName".equalsIgnoreCase(sortName)) {
            sortName = "last_name";
        } else if ("EMAIL".equalsIgnoreCase(sortName)) {
            sortName = "email";
        } else if ("status".equalsIgnoreCase(sortName)) {
            sortName = "user_status";
        } else {
            sortName = "last_name";
        }

        sqlBuf.append(MessageFormat.format(SELECT_PROJECT_MEMBERSHIP_BY_FILTER_SUFFIX, sortName, sortOrder, offset, count));

        logger.debug("select project membership SQL: " + sqlBuf.toString());

        // execute and return results
        return getJdbcTemplate().query(sqlBuf.toString(), new ProjectMembershipMapper());
    }

    class ProjectMembershipMapper implements RowMapper {
        public ProjectMemberShipVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProjectMemberShipVO pm = new ProjectMemberShipVO();
            pm.setId(rs.getInt("id"));
            pm.setUserId(rs.getInt("user_id"));
            pm.setFirstName(rs.getString("first_name"));
            pm.setLastName(rs.getString("last_name"));
            pm.setRoleId(rs.getInt("role_id"));
            pm.setRoleName(rs.getString("role_name"));           
            StringBuilder sb = new StringBuilder();
            if (!StringUtils.isEmpty(pm.getFirstName())) {
                sb.append(pm.getFirstName());
            }
            if (!StringUtils.isEmpty(pm.getLastName())) {
                sb.append(' ').append(pm.getLastName());
            }
            pm.setDisplayUserName(sb.toString());
            pm.setEmail(rs.getString("email"));
            pm.setUserName(rs.getString("username"));
            pm.setStatus(rs.getShort("user_status"));
            return pm;
        }
    }
}
