/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

//import com.ocs.indaba.common.dao.BaseDAO;
import com.ocs.indaba.common.Constants;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.User;
import com.ocs.indaba.vo.CommMemberInfo;
import com.ocs.indaba.vo.ProjectUserView;
import com.ocs.indaba.vo.QueueUser;
import com.ocs.util.ListUtils;
import com.ocs.util.StringUtils;
import static java.sql.Types.INTEGER;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Operation on User table by accessing DB
 *
 * @author Jeff
 *
 */
public class UserDAO extends SmartDaoMySqlImpl<User, Integer> {

    private static final Logger log = Logger.getLogger(UserDAO.class);
    /*
     * private static final String SELECT_USER_BY_NAME_AND_PWD = "SELECT u.uid,
     * u.username, u.email, u.password, u.last_password_change_time,
     * u.create_time, " + " u.last_logout_time, u.status, u.timezone,
     * u.language, u.msgboard_id, u.privacy_level, " + " u.forward_inbox_msg,
     * u.number_msgs_per_screen, u.first_name, u.last_name, u.phone, " + "
     * u.cell_phone, u.address, u.bio, u.photo, u.email_detail_level " + " FROM
     * user u" + " WHERE username=? AND password=?";
     */
    private static final String SQL_SELECT_USER_BY_NAME_AND_PWD =
            "SELECT * from user WHERE username=? AND password=? AND status=?";
    private static final String SQL_SELECT_USER_BY_NAME =
            "SELECT * from user WHERE username=?";
    private static final String SQL_SELECT_USERS_BY_TEAM_ID =
            "SELECT u.* FROM user u "
            + " JOIN team_user tu ON tu.user_id = u.id "
            + " WHERE tu.team_id =?";
    private static final String SQL_SELECT_OWER_AND_ADMIN_BY_PROJECT_ID =
            "SELECT DISTINCT u.* FROM user u, project p "
            + "WHERE p.id = ? AND (p.owner_user_id = u.id OR p.admin_user_id = u.id)";
    private static final String SQL_SELECT_ALL_USERS_BY_PROJECT_ID =
            "SELECT u.* FROM user u "
            + " JOIN project_membership pm ON u.id = pm.user_id "
            + " WHERE pm.project_id =? ORDER BY u.last_name";
    private static final String SQL_SELECT_USERS_BY_PROJECT_CONTACT =
            "SELECT u.* FROM user u "
            + " JOIN project_contact pc ON u.id = pc.user_id "
            + " WHERE pc.project_id =?";
    private static final String SELECT_ALL_ACTIVE_USERS =
            "SELECT * FROM user WHERE status=1 {0} ORDER BY first_name, last_name ASC";
    private static final String SQL_SELECT_USERS_BY_TASK_AND_PROJECT =
            "SELECT DISTINCT u.id, u.first_name, u.last_name FROM user u"
            + " JOIN project_membership pm ON u.id = pm.user_id"
            + " JOIN task_role tr ON pm.role_id= tr.role_id"
            + " WHERE tr.task_id=? AND pm.project_id=? AND u.status=? ORDER BY u.first_name, u.last_name";
    private static final String SQL_SELECT_USERS_BY_TASKS =
            "SELECT DISTINCT u.id, u.first_name, u.last_name, tr.task_id FROM user u"
            + " JOIN project_membership pm ON u.id = pm.user_id"
            + " JOIN task_role tr ON pm.role_id= tr.role_id"
            + " WHERE tr.task_id IN ({0}) AND pm.project_id=? AND u.status=? ORDER BY u.first_name, u.last_name";
    private static final String SELECT_ASSIGNED_USERS_BY_HORSE_ID =
            "SELECT DISTINCT u.* "
            + "FROM horse h, task_assignment ta, task t, user u "
            + "WHERE h.id=? AND h.product_id=t.product_id AND h.target_id=ta.target_id AND "
            + "t.id=ta.task_id AND ta.assigned_user_id=u.id ORDER BY u.first_name";
    private static final String UPDATE_USER_DEFAULT_PROJECT_ID =
            "UPDATE `user` SET default_project_id=? WHERE `id`=?";
    private static final String UPDATE_USER_MSGBOARD_ID =
            "UPDATE `user` SET msgboard_id=? WHERE `id`=?";
    private static final String SELECT_ALL_USERS_ORDER_BY_LASTNAME_AND_NOT_NULL =
            "SELECT DISTINCT user.* FROM user "
            + "JOIN project_membership pm ON (pm.user_id = user.id) "
            + "WHERE (status != 2 AND pm.project_id = ?) OR user.id = ? "
            + "ORDER BY user.last_name";
    private static final String SELECT_USERS_BY_TASK_IN_PROJECT =
            "SELECT user.* FROM user, project_membership, task_role "
            + "WHERE task_role.task_id=? AND task_role.role_id=project_membership.role_id AND project_membership.project_id=? AND user.id=project_membership.user_id";
    private static final String SELECT_AUTHOR_BY_HORSE_ID =
            "SELECT user.* FROM user, content_header "
            + "WHERE user.id=content_header.`author_user_id` AND content_header.`horse_id`=?";
    private static final String SQL_SELECT_SITE_ADMIN_USERS =
            "SELECT * FROM user WHERE site_admin=1 ORDER BY username";

    private static final String SELECT_CLAIMABLE_USERS_BY_TASK_IN_PROJECT =
            "SELECT user.*, pm.project_id, pm.role_id FROM user, project_membership pm, task_role tr "
            + "WHERE tr.task_id=? AND tr.can_claim != 0 AND tr.role_id=pm.role_id AND pm.project_id=? AND user.id=pm.user_id";

    private static final String SELECT_SILENT_USERS_BY_TASK =
            "SELECT user.* FROM user, tasksub "
            + "WHERE tasksub.task_id=? AND tasksub.user_id=user.id AND tasksub.notify=0";

    private static final String SELECT_ORG_PRIMARY_ADMINS_BY_ORG_IDS = "SELECT * FROM user WHERE id IN ({0}) ORDER BY first_name, last_name";
    private static final String SELECT_USER_BY_EMAIL = "SELECT * FROM user WHERE email=?";
    private static final String SELECT_USER_BY_MSGBORAD_ID = "SELECT * FROM user WHERE msgboard_id=?";
    private static final String SELECT_USERS_BY_PROJECT_ID =
            "SELECT u.* FROM user u, project_membership pm WHERE pm.project_id=? AND pm.user_id=u.id ORDER BY u.first_name, u.last_name";
    private static final String SELECT_USERNAMES =
            "SELECT id, first_name, last_name FROM user WHERE id IN ({0})";

    private static final String FIND_SURVEY_REVIEW_RESPONDENT_USERS_BASE =
            "SELECT DISTINCT u.* FROM user u, project_membership pm WHERE pm.project_id={0,number,#} AND u.id=pm.user_id {1} {2} {3} ORDER BY u.last_name, u.first_name";

    private static final String FIND_SURVEY_REVIEW_RESPONDENT_USERS_TARGET =
            " AND u.id IN (SELECT DISTINCT ta.assigned_user_id FROM task_assignment ta, task t, product prod WHERE t.id=ta.task_id AND prod.id=t.product_id AND prod.project_id={0,number,#} AND ta.target_id={1,number,#})";

    public List<User> findSurveyReviewRespondentUsers(int projId, int roleId, int targetId, String username) {
        String roleCretia = (roleId > 0) ? " AND pm.role_id=" + roleId : "";
        String usernameCretia = StringUtils.isEmpty(username) ? "" : " AND (u.first_name LIKE '%" + username.replace('\'', '"').trim() + "%' OR u.last_name LIKE '%" + username.replace('\'', '"').trim() + "%')";
        String targetCretia = (targetId > 0) ? MessageFormat.format(FIND_SURVEY_REVIEW_RESPONDENT_USERS_TARGET, projId, targetId) : "";

        String sqlStr = MessageFormat.format(FIND_SURVEY_REVIEW_RESPONDENT_USERS_BASE, projId, roleCretia, targetCretia, usernameCretia);

        return super.find(sqlStr);
    }

    public Map<Integer, String> selectUsernameMap(List<Integer> userIds) {
        final Map<Integer, String> map = new HashMap<Integer, String>();
        if (userIds == null || userIds.isEmpty()) {
            return map;
        }
        RowMapper mapper = new RowMapper() {

            public Map<Integer, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String username = "";
                if (!StringUtils.isEmpty(firstName)) {
                    username = firstName;
                }
                if (!StringUtils.isEmpty(lastName)) {
                    if (username.length() > 0) {
                        username += " ";
                    }
                    username += lastName;
                }
                map.put(rs.getInt("id"), username);
                return map;
            }
        };
        getJdbcTemplate().query(MessageFormat.format(SELECT_USERNAMES, ListUtils.listToString(userIds)), mapper);
        return map;
    }

    public List<User> selectUsersByProjectId(int projId) {
        return super.find(SELECT_USERS_BY_PROJECT_ID, projId);
    }

    public List<User> selectUsersByIds(List<Integer> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return null;
        }
        return super.find(MessageFormat.format(SELECT_ORG_PRIMARY_ADMINS_BY_ORG_IDS, ListUtils.listToString(userIds)));
    }

    /**
     * SELECT USER
     *
     * @param username
     * @param password
     * @return
     */
    public List<User> selectUsersByTaskInProject(int taskId, int projectId) {
        return super.find(SELECT_USERS_BY_TASK_IN_PROJECT, (Object) taskId, projectId);
    }

    public List<ProjectUserView> selectClaimableUsersByTaskInProject(int taskId, int projectId) {

        List<ProjectUserView> claimableUsers = getJdbcTemplate().query(SELECT_CLAIMABLE_USERS_BY_TASK_IN_PROJECT,
                new Object[]{taskId, projectId},
                new int[]{INTEGER, INTEGER},
                new ProjectUserRowMapper());


        if (claimableUsers == null || claimableUsers.isEmpty()) {
            logger.debug("No claimable users for task " + taskId);
            return null;
        }

        logger.debug("Claimable Users for task " + taskId + ": " + claimableUsers.size());

        List<User> silentUsers = super.find(SELECT_SILENT_USERS_BY_TASK, (Object) taskId);

        if (silentUsers == null || silentUsers.isEmpty()) {
            logger.debug("No silent users for task " + taskId);
            return claimableUsers;
        }
        logger.debug("Silent Users for task " + taskId + ": " + silentUsers.size());

        // remove silent users
        for (User su : silentUsers) {
            // see if this user is in claimableUsers list
            for (ProjectUserView cu : claimableUsers) {
                int suid = su.getId();
                int cuid = cu.getId();
                if (suid == cuid) {
                    // remove this user
                    claimableUsers.remove(cu);
                    break;
                }
            }
        }

        logger.debug("Final Claimable Users for task " + taskId + ": " + claimableUsers.size());

        return claimableUsers;
    }

    public User selectUserByNameAndPwd(String username, String password) {
        log.debug("Select table User: " + SQL_SELECT_USER_BY_NAME_AND_PWD + "[username=" + username + ", password=" + password + "].");
        return super.findSingle(SQL_SELECT_USER_BY_NAME_AND_PWD, username, password, Constants.USER_STATUS_ACTIVE);
    }

    public User selectUserByNameAndPwd(String username, String password, short status) {
        log.debug("Select table User: " + SQL_SELECT_USER_BY_NAME_AND_PWD + "[username=" + username + ", password=" + password + ", status=" + status + "].");
        return super.findSingle(SQL_SELECT_USER_BY_NAME_AND_PWD, username, password, status);
    }

    public List<User> selectOwnerAndAdminByProjectId(int projectId) {
        return super.find(SQL_SELECT_OWER_AND_ADMIN_BY_PROJECT_ID, projectId);
    }

    /**
     * SELECT USER
     *
     * @param username
     * @param password
     * @return
     */
    public User selectUserByName(String username) {
        log.debug("Select table User: " + SQL_SELECT_USER_BY_NAME + "[username=" + username + "].");
        return super.findSingle(SQL_SELECT_USER_BY_NAME, username);

    }

    public User selectUserByMsgboradId(int msgboardId) {
        return super.findSingle(SELECT_USER_BY_MSGBORAD_ID, msgboardId);
    }

    public void updateUserDefaultProjectId(int userId, int projectId) {
        log.debug("Update user default project id: " + UPDATE_USER_DEFAULT_PROJECT_ID + ". [userId=" + userId + ", projectId=" + projectId + "].");
        update(UPDATE_USER_DEFAULT_PROJECT_ID, projectId, userId);
        log.debug("+++++ Updated default_project_id: " + get(userId).getDefaultProjectId());
    }

    public void updateUserMsgboardId(int userId, int msgBoardId) {
        update(UPDATE_USER_MSGBOARD_ID, msgBoardId, userId);
    }

    /**
     * SELECT USER
     *
     * @param username
     * @param password
     * @return
     */
    public User selectUserById(int userId) {
        log.debug("Select user by id: " + userId + ".");
        return super.get(userId);

    }

    /**
     * SELECT ALL USER
     *
     * @return
     */
    public List<User> selectAllUsers() {
        logger.debug("Select all User.");
        return super.findAll();

    }

    /**
     * SELECT ALL USER
     *
     * @return
     */
    public List<User> selectAllActiveUsers(List<Integer> excludes) {
        logger.debug("Select all User.");
        String conditions = "";
        if (excludes != null && !excludes.isEmpty()) {
            conditions = " AND id NOT IN (" + ListUtils.listToString(excludes) + ") ";
        }
        return super.find(MessageFormat.format(SELECT_ALL_ACTIVE_USERS, conditions));

    }

    public List<User> selectAllUsersOrderByLastName(int projectId, int userId) {

        return super.find(SELECT_ALL_USERS_ORDER_BY_LASTNAME_AND_NOT_NULL, new Object[]{projectId, userId});
    }

    /**
     * SELECT ALL USER
     *
     * @return
     */
    public List<User> selectAllUsersByProjectId(Integer projectId) {
        logger.debug("Select users by project ID: " + projectId);

        return super.find(SQL_SELECT_ALL_USERS_BY_PROJECT_ID, projectId);
    }

    public List<User> selectUsersByTeamId(Integer teamId) {
        logger.debug("Select users by team ID: " + teamId);

        return super.find(SQL_SELECT_USERS_BY_TEAM_ID, teamId);
    }

    public List<User> selectUsersByProjectContact(Integer projectId) {
        logger.debug("Select users by project contact: " + projectId);
        return super.find(SQL_SELECT_USERS_BY_PROJECT_CONTACT, projectId);

    }

    public void updateUser(User user) {
        update(user);
    }

    @SuppressWarnings("unchecked")
    public List<Integer> getUserIdsByProjectAndRoles(int prjid, int[] roleIds) {
        Integer[] args = new Integer[roleIds.length + 1];

        args[0] = prjid;
        for (int i = 0; i < roleIds.length; i++) {
            args[i + 1] = roleIds[i];
        }

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT u.id FROM user u INNER JOIN project_membership pm ON u.id = pm.user_id WHERE pm.project_id = ? AND (");

        boolean first = true;
        for (int i = 0; i < roleIds.length; i++) {
            if (!first) {
                sb.append(" OR ");
            } else {
                first = false;
            }

            sb.append(" pm.role_id = ? ");
        }

        sb.append(")");

        return getJdbcTemplate().query(sb.toString(), args, new RowMapper() {

            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("id");
            }
        });
    }

    @SuppressWarnings("unchecked")
    public List<Integer> getUserIdsByProjectAndTeams(int prjid, int[] teamIds) {
        Integer[] args = new Integer[teamIds.length];

        for (int i = 0; i < teamIds.length; i++) {
            args[i] = teamIds[i];
        }

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT u.id FROM user u INNER JOIN team_user tu ON u.id = tu.user_id WHERE ");

        boolean first = true;
        for (int i = 0; i < teamIds.length; i++) {
            if (!first) {
                sb.append(" OR ");
            } else {
                first = false;
            }

            sb.append(" tu.team_id = ? ");
        }

        return getJdbcTemplate().query(sb.toString(), args, new RowMapper() {

            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("id");
            }
        });
    }

    public List<QueueUser> selectUsersByTaskId(int taskId, int projectId) {
        logger.debug("Select users by task ID " + taskId);
        RowMapper mapper = new RowMapper() {

            public QueueUser mapRow(ResultSet rs, int rowNum) throws SQLException {
                QueueUser user = new QueueUser();
                user.setUserId(rs.getInt("id"));
                user.setUserName(rs.getString("first_name") + " " + rs.getString("last_name"));
                return user;
            }
        };

        return getJdbcTemplate().query(SQL_SELECT_USERS_BY_TASK_AND_PROJECT,
                new Object[]{taskId, projectId, Constants.USER_STATUS_ACTIVE},
                new int[]{INTEGER, INTEGER, INTEGER},
                mapper);
    }

    public Map<Integer, List<QueueUser>> selectUsersByTaskIds(List<Integer> taskIds, int projectId) {

        if (taskIds == null || taskIds.isEmpty()) {
            return null;
        }

        logger.debug("Select users by task ID " + taskIds);
        final Map<Integer, List<QueueUser>> queueUserMap = new HashMap<Integer, List<QueueUser>>();
        RowMapper mapper = new RowMapper() {

            public QueueUser mapRow(ResultSet rs, int rowNum) throws SQLException {
                QueueUser user = new QueueUser();
                user.setUserId(rs.getInt("id"));
                user.setUserName(rs.getString("first_name") + " " + rs.getString("last_name"));
                int taskId = rs.getInt("tr.task_id");
                List<QueueUser> queueUsers = queueUserMap.get(taskId);
                if (queueUsers == null) {
                    queueUsers = new ArrayList<QueueUser>();
                    queueUserMap.put(taskId, queueUsers);
                }
                queueUsers.add(user);
                return user;
            }
        };

        getJdbcTemplate().query(MessageFormat.format(SQL_SELECT_USERS_BY_TASKS, ListUtils.listToString(taskIds)),
                new Object[]{projectId, Constants.USER_STATUS_ACTIVE},
                mapper);

        return queueUserMap;
    }

    /**
     * Select all of the assigned users
     *
     * @param horseId
     * @return list of UserView
     */
    public List<User> selectAssignedUsers(int horseId) {
        log.debug("Select all the  by the horse: " + horseId + ". [" + SELECT_ASSIGNED_USERS_BY_HORSE_ID + "].");
        return super.find(SELECT_ASSIGNED_USERS_BY_HORSE_ID, horseId);
    }

    public User selectAuthorByHouseId(int horseId) {
        return super.findSingle(SELECT_AUTHOR_BY_HORSE_ID, horseId);
    }

    public List<User> selectSiteAdminUsers() {
        return super.find(SQL_SELECT_SITE_ADMIN_USERS);
    }

    public User selectUserByEmail(String email) {
        return super.findSingle(SELECT_USER_BY_EMAIL, email);
    }


    private static final String SELECT_PROJECT_USER_BY_ID =
            "SELECT user.*, pm.project_id, pm.role_id " +
            "FROM user " +
            "LEFT JOIN project_membership pm ON pm.project_id=? AND pm.user_id=user.id " +
            "WHERE user.id=?";

    public ProjectUserView selectProjectUser(int projectId, int userId) {
        List<ProjectUserView> list = getJdbcTemplate().query(SELECT_PROJECT_USER_BY_ID,
                new Object[]{projectId, userId},
                new int[]{INTEGER, INTEGER},
                new ProjectUserRowMapper());

        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }



    private class ProjectUserRowMapper implements RowMapper {

        public ProjectUserView mapRow(ResultSet rs, int rowNum) throws SQLException {
                ProjectUserView user = new ProjectUserView();
                user.setId(rs.getInt("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setLanguageId(rs.getInt("language_id"));
                user.setForwardInboxMsg(rs.getBoolean("forward_inbox_msg"));
                user.setEmailDetailLevel(rs.getShort("email_detail_level"));
                user.setMsgboardId(rs.getInt("msgboard_id"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));

                user.setProjectId(rs.getInt("project_id"));
                user.setRoleId(rs.getInt("role_id"));
                return user;
            }
    }


    private static final String SELECT_BY_NOTEOBJ_BASED_ON_ROLES =
            "SELECT DISTINCT u.* FROM user u, task_assignment ta, noteobj obj, notedef def, notedef_role ndr, project_membership pm, product prod " +
            "WHERE obj.id=? AND def.id=obj.notedef_id AND prod.id=def.product_id AND ndr.notedef_id=def.id AND pm.project_id=prod.project_id " +
            "AND pm.role_id=ndr.role_id AND u.id=pm.user_id AND ta.horse_id=obj.horse_id AND ta.assigned_user_id=u.id";

    public List<User> getUsersByNoteobjBasedOnRoles(int noteobjId) {
        return super.find(SELECT_BY_NOTEOBJ_BASED_ON_ROLES, noteobjId);
    }


    private static final String SELECT_BY_GROUPOBJ_BASED_ON_ROLES =
            "SELECT DISTINCT u.* FROM user u, task_assignment ta, groupobj obj, groupdef def, groupdef_role gdr, project_membership pm, product prod " +
            "WHERE obj.id=? AND def.id=obj.groupdef_id AND prod.id=def.product_id AND gdr.groupdef_id=def.id AND pm.project_id=prod.project_id " +
            "AND pm.role_id=gdr.role_id AND u.id=pm.user_id AND ta.horse_id=obj.horse_id AND ta.assigned_user_id=u.id";

    public List<User> getUsersByGroupobjBasedOnRoles(int groupobjId) {
        return super.find(SELECT_BY_GROUPOBJ_BASED_ON_ROLES, groupobjId);
    }

    private static final String SELECT_COMM_MEMBER_INFO_BY_UIDS =
            "SELECT DISTINCT u.id as uid, u.first_name, u.last_name, u.username, r.id rid, r.name rolename " +
            "FROM user u, role r, project_membership pm " +
            "WHERE u.id in ({0}) AND pm.project_id=? AND pm.user_id=u.id AND r.id=pm.role_id AND u.id!=?";

    private static final String SELECT_COMM_MEMBER_INFO_OF_PROJECT =
            "SELECT DISTINCT u.id as uid, u.first_name, u.last_name, u.username, r.id rid, r.name rolename " +
            "FROM user u, role r, project_membership pm " +
            "WHERE pm.project_id=? AND pm.user_id=u.id AND r.id=pm.role_id AND u.id!=?";

    public List<CommMemberInfo> getCommMemberInfo(int projectId, List<Integer> userIds, int excludeUserId) {
        String sql = (userIds != null && !userIds.isEmpty()) ? 
            MessageFormat.format(SELECT_COMM_MEMBER_INFO_BY_UIDS, ListUtils.listToString(userIds)) :
            SELECT_COMM_MEMBER_INFO_OF_PROJECT;

        return getJdbcTemplate().query(sql,
                new Object[]{projectId, excludeUserId},
                new int[]{INTEGER, INTEGER},
                new CommMemberRowMapper());
    }


    private class CommMemberRowMapper implements RowMapper {

        public CommMemberInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                CommMemberInfo user = new CommMemberInfo();
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setUserName(rs.getString("username"));
                user.setRoleName(rs.getString("rolename"));
                user.setRoleId(rs.getInt("rid"));
                user.setUserId(rs.getInt("uid"));

                return user;
            }
    }
}
