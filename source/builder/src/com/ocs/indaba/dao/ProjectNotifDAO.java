/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.ProjectNotif;
import com.ocs.indaba.vo.ProjectNotifView;
import com.ocs.util.ListUtils;
import com.ocs.util.StringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author yc06x
 */
public class ProjectNotifDAO extends SmartDaoMySqlImpl<ProjectNotif, Integer> {

    private static final Logger logger = Logger.getLogger(ProjectNotifDAO.class);

    private static final String SELECT_NOTIFICATION_ITEM =
            "SELECT pn.* FROM project_notif pn, project_notif_role pnr, notification_type nt "
            + "WHERE nt.name=? AND pn.project_id=? AND pn.language_id=? AND pn.notification_type_id = nt.id "
            + "AND pnr.role_id=? AND pnr.project_notif_id=pn.id";


    public ProjectNotif selectNotificationItem(int projectId, int roleId, int languageId, String notificationTypeName) {
        Object[] values = new Object[]{notificationTypeName, projectId, languageId, roleId};
        return super.findSingle(SELECT_NOTIFICATION_ITEM, values);
    }


    public long countProjectNotifs(int projectId, int filterLangId, int filterRoleId, int filterTypeId, String searchCol, String searchTerm) {
        String selectSql = SELECT_PROJECT_NOTIFS_BASE + getFilterConditionSql(filterLangId, filterRoleId, filterTypeId, searchCol, searchTerm) +
                " GROUP BY pn.id ";

        String countSql = "SELECT COUNT(1) FROM (" + selectSql + ") xxx";

        return super.count(countSql, projectId);
    }


    private String getFilterConditionSql(int filteLangId, int filterRoleId, int filterTypeId, String searchCol, String searchTerm) {
        String langFilter = "";
        String typeFilter = "";
        String roleFiler = "";
        String search = "";

        if (filteLangId > 0) {
            langFilter = " AND pn.language_id=" + filteLangId;
        }
        if (filterTypeId > 0) {
            typeFilter = " AND pn.notification_type_id=" + filterTypeId;
        }
        if (filterRoleId >= 0) {
            roleFiler = " AND pnr.project_notif_id=pn.id AND pnr.role_id=" + filterRoleId;
        }

        if (!StringUtils.isEmpty(searchCol) && !StringUtils.isEmpty(searchTerm)) {
            if (searchCol.equalsIgnoreCase("name")) {
                search = " AND pn.name LIKE '%" + searchTerm + "%'";
            } else if (searchCol.equalsIgnoreCase("description")) {
                search = " AND pn.description LIKE '%" + searchTerm + "%'";
            } else if (searchCol.equalsIgnoreCase("subject")) {
                search = " AND pn.subject_text LIKE '%" + searchTerm + "%'";
            } else if (searchCol.equalsIgnoreCase("body")) {
                search = " AND pn.body_text LIKE '%" + searchTerm + "%'";
            }
        }

        return langFilter + typeFilter + roleFiler + search;
    }


    private static final String SELECT_PROJECT_NOTIFS_BASE =
            "SELECT pn.*, nt.name type_name, lang.language_desc lang_name, GROUP_CONCAT(r.name SEPARATOR ', ') role_names, GROUP_CONCAT(pnr.role_id SEPARATOR ', ') role_ids " +
            "FROM project_notif pn " +
            "JOIN language lang ON lang.id=pn.language_id " +
            "JOIN notification_type nt ON nt.id=pn.notification_type_id " +
            "JOIN project_notif_role pnr ON pnr.project_notif_id=pn.id " +
            "LEFT JOIN role r ON r.id=pnr.role_id " +
            "WHERE pn.project_id=?";

    public List<ProjectNotifView> findProjectNotifs(int projectId, int filterLangId, int filterRoleId, int filterTypeId, String searchCol, String searchTerm, String sortCol, String sortOrder, int offset, int count) {
        if ("typeName".equalsIgnoreCase(sortCol)) {
            sortCol = "type_name";
        } else if ("languageName".equalsIgnoreCase(sortCol)) {
            sortCol = "lang_name";
        }

        String sql = SELECT_PROJECT_NOTIFS_BASE + getFilterConditionSql(filterLangId, filterRoleId, filterTypeId, searchCol, searchTerm) +
                " GROUP BY pn.id " +
                " ORDER BY " + sortCol + " " + sortOrder +
                " LIMIT " + offset + ", " + count;

        return getJdbcTemplate().query(sql, new ProjectNotifViewRowMapper(), projectId);
    }


    public List<ProjectNotifView> findProjectNotifs(int projectId) {
        String sql = SELECT_PROJECT_NOTIFS_BASE + " GROUP BY pn.id";
        return getJdbcTemplate().query(sql, new ProjectNotifViewRowMapper(), projectId);
    }


    private static final String SELECT_PROJECT_NOTIFS_BY_IDS_BASE =
            "SELECT pn.*, nt.name type_name, lang.language_desc lang_name, GROUP_CONCAT(r.name SEPARATOR ', ') role_names, GROUP_CONCAT(pnr.role_id SEPARATOR ', ') role_ids " +
            "FROM project_notif pn " +
            "JOIN language lang ON lang.id=pn.language_id " +
            "JOIN notification_type nt ON nt.id=pn.notification_type_id " +
            "JOIN project_notif_role pnr ON pnr.project_notif_id=pn.id " +
            "LEFT JOIN role r ON r.id=pnr.role_id";

    public List<ProjectNotifView> findProjectNotifs(List<Integer> projectNotifIds) {
        String sql = SELECT_PROJECT_NOTIFS_BY_IDS_BASE + " WHERE pn.id IN (" + ListUtils.listToString(projectNotifIds) + ") GROUP BY pn.id";
        return getJdbcTemplate().query(sql, new ProjectNotifViewRowMapper());
    }


    private class ProjectNotifViewRowMapper implements RowMapper {

        public ProjectNotifView mapRow(ResultSet rs, int rowNum) throws SQLException {
                ProjectNotifView notif = new ProjectNotifView();

                notif.setId(rs.getInt("id"));
                notif.setProjectId(rs.getInt("project_id"));
                notif.setBodyText(rs.getString("body_text"));
                notif.setSubjectText(rs.getString("subject_text"));
                notif.setNotificationTypeId(rs.getInt("notification_type_id"));
                notif.setLanguageId(rs.getInt("language_id"));
                notif.setName(rs.getString("name"));
                notif.setDescription(rs.getString("description"));
                notif.setLanguageName(rs.getString("lang_name"));
                notif.setTypeName(rs.getString("type_name"));
                notif.setRoleNames(rs.getString("role_names"));

                String roleIdsStr = rs.getString("role_ids");
                if (StringUtils.isEmpty(roleIdsStr)) {
                    roleIdsStr = "0";
                }

                String roleId[] = roleIdsStr.split(",");
                List<Integer> roleIds = new ArrayList<Integer>();
                for (String rid : roleId) {
                    int roleIdValue = StringUtils.str2int(rid.trim());
                    roleIds.add(roleIdValue);
                }
                notif.setRoleIds(roleIds);

                return notif;
            }
    }


    private static final String FIND_CONFLICT_BASE =
            "SELECT pn.* FROM project_notif pn, project_notif_role pnr " +
            "WHERE pn.project_id=? AND pn.notification_type_id=? AND pn.language_id=? AND pnr.project_notif_id=pn.id ";

    public ProjectNotif findConflict(ProjectNotif notif, List<Integer> roles) {
        String exclusion = "";
        if (notif.getId() > 0) {
            exclusion = " AND pn.id!=" + notif.getId();
        }

        String sql = FIND_CONFLICT_BASE + " AND pnr.role_id IN (" + ListUtils.listToString(roles) + ")" + exclusion;

        return super.findSingle(sql, notif.getProjectId(), notif.getNotificationTypeId(), notif.getLanguageId());
    }


    private static final String REMOVE_NOTIF_ROLES =
            "DELETE FROM project_notif_role WHERE project_notif_id=?";

    private static final String INSERT_NOTIF_ROLES =
            "INSERT INTO project_notif_role (role_id, project_notif_id) VALUES (?, ?) ";

    public ProjectNotif saveProjectNotif(ProjectNotif notif, List<Integer> roles) {
        if (notif.getId() > 0) {
            // remove all PN roles of thie notif
            super.delete(REMOVE_NOTIF_ROLES, notif.getId());
            update(notif);
        } else {
            // create a new notif
            notif = super.create(notif);
        }

        // create roles
        for (Integer roleId : roles) {
            super.update(INSERT_NOTIF_ROLES, roleId, notif.getId());
        }

        return notif;
    }


    private static final String SELECT_PROJECT_NOTIF_BY_ID =
            "SELECT pn.*, nt.name type_name, lang.language_desc lang_name, GROUP_CONCAT(r.name SEPARATOR ', ') role_names, GROUP_CONCAT(pnr.role_id SEPARATOR ', ') role_ids " +
            "FROM project_notif pn " +
            "JOIN language lang ON lang.id=pn.language_id " +
            "JOIN notification_type nt ON nt.id=pn.notification_type_id " +
            "JOIN project_notif_role pnr ON pnr.project_notif_id=pn.id " +
            "LEFT JOIN role r ON r.id=pnr.role_id " +
            "WHERE pn.id=? GROUP BY pn.id";

    public ProjectNotifView getProjectNotifView(int notifId) {
        List<ProjectNotifView> notifs = getJdbcTemplate().query(SELECT_PROJECT_NOTIF_BY_ID, new ProjectNotifViewRowMapper(), notifId);

        return (notifs != null && !notifs.isEmpty()) ? notifs.get(0) : null;
    }


    private static final String REMOVE_NOTIF_ROLES_BY_NOTIFS =
            "DELETE FROM project_notif_role WHERE project_notif_id IN ({0})";

    private static final String REMOVE_NOTIFS_BY_IDS =
            "DELETE FROM project_notif WHERE id IN ({0})";

    public void deleteProjectNotif(List<Integer> notifIds) {
        String ids = ListUtils.listToString(notifIds);
        super.delete(MessageFormat.format(REMOVE_NOTIF_ROLES_BY_NOTIFS, ids));
        super.delete(MessageFormat.format(REMOVE_NOTIFS_BY_IDS, ids));
    }
}
