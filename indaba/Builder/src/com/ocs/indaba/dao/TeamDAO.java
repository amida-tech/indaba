/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Team;
import com.ocs.indaba.po.TeamUser;
import com.ocs.indaba.vo.UserTeamInfo;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Types.BIGINT;
import static java.sql.Types.INTEGER;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Jeff
 */
public class TeamDAO extends SmartDaoMySqlImpl<Team, Integer> {

    private static final Logger log = Logger.getLogger(TeamDAO.class);
    private TeamUserDAO teamUserDao = null;
    private static final String SELECT_TEAM_BY_ID =
            "SELECT * FROM team WHERE id = ? ";
    private static final String SQL_SELECT_TEAMS_BY_USER_ID =
            "SELECT t.id team_id, t.project_id, t.team_name, t.description "
            + " FROM team t, team_user tu "
            + " WHERE tu.user_id = ? AND t.id = tu.team_id";
    private static final String SQL_SELECT_TEAMS_BY_USER_AND_PROJECT =
            "SELECT t.id team_id, t.project_id, t.team_name, t.description "
            + " FROM team t, team_user tu "
            + " WHERE tu.user_id = ? AND t.project_id = ? AND t.id = tu.team_id";
    private static final String SQL_SELECT_TEAMUSERS_OF_USERID_AND_PROJECTID =
            "SELECT tub.user_id user_id, t.id team_id, t.team_name, t.project_id, u.username, u.first_name, u.last_name "
            + "FROM team_user tua, team_user tub, team t, user u "
            + "WHERE tua.user_id=? AND tua.team_id=tub.team_id  AND t.project_id=? AND tub.user_id=u.id AND tub.team_id=t.id "
            + "ORDER BY tub.user_id, t.team_name";
    private static final String SQL_SELECT_USERTEAM_BY_USERID_AND_PROJECTID =
            "SELECT t.id team_id, t.team_name, t.project_id, u.username, u.first_name, u.last_name "
            + "FROM team_user tu, team t, user u "
            + "WHERE tu.user_id=? "
            + " AND t.project_id=? "
            + " AND tu.user_id=u.id "
            + " AND tu.team_id=t.id "
            + " ORDER BY t.team_name ";

    public Team selectTeamById(int teamId) {
        log.debug("Select table team by id: " + teamId + ". [" + SELECT_TEAM_BY_ID + "].");

        return super.findSingle(SELECT_TEAM_BY_ID, teamId);
    }

    public List<Integer> selectTeamUsersByUserId(long userId) {
        return teamUserDao.selectTeamUsersByUserId(userId);
    }

    public List<UserTeamInfo> selectTeamsOfUserIdAndProjectId(final long userId, final int projectId) {
        logger.debug("Select table team_users of user: " + userId + ". ["
                + SQL_SELECT_TEAMUSERS_OF_USERID_AND_PROJECTID + "].");

        RowMapper mapper = new RowMapper() {

            public UserTeamInfo mapRow(ResultSet rs, int i) throws SQLException {
                UserTeamInfo userTeamInfo = new UserTeamInfo();
                userTeamInfo.setTeamId(rs.getInt("team_id"));
                userTeamInfo.setUserid(rs.getInt("user_id"));
                userTeamInfo.setTeamName(rs.getString("team_name"));
                userTeamInfo.setFirstName(rs.getString("first_name"));
                userTeamInfo.setLastName(rs.getString("last_name"));
                userTeamInfo.setUserName(rs.getString("username"));
                return userTeamInfo;
            }
        };

        List<UserTeamInfo> list = getJdbcTemplate().query(SQL_SELECT_TEAMUSERS_OF_USERID_AND_PROJECTID,
                new Object[]{userId, projectId},
                new int[]{INTEGER, INTEGER},
                mapper);
        return list;
    }

    public List<UserTeamInfo> selectTeamsByUserIdAndProjectId(final long userId, final int projectId) {
        logger.debug("Select table team_user by user: " + userId + ". ["
                + SQL_SELECT_USERTEAM_BY_USERID_AND_PROJECTID + "].");

        RowMapper mapper = new RowMapper() {

            public UserTeamInfo mapRow(ResultSet rs, int i) throws SQLException {
                UserTeamInfo userTeamInfo = new UserTeamInfo();
                userTeamInfo.setTeamId(rs.getInt("team_id"));
                userTeamInfo.setTeamName(rs.getString("team_name"));
                userTeamInfo.setFirstName(rs.getString("first_name"));
                userTeamInfo.setLastName(rs.getString("last_name"));
                userTeamInfo.setUserName(rs.getString("username"));
                return userTeamInfo;
            }
        };

        List<UserTeamInfo> list = getJdbcTemplate().query(SQL_SELECT_USERTEAM_BY_USERID_AND_PROJECTID,
                new Object[]{userId, projectId},
                new int[]{INTEGER, INTEGER},
                mapper);
        return list;
    }

    public List<Team> selectTeamsByUserId(final long userId) {
        logger.debug("Select all teams by user id.");

        RowMapper mapper = new RowMapper() {

            public Team mapRow(ResultSet rs, int rowNum) throws SQLException {
                Team team = new Team();
                team.setId(rs.getInt("team_id"));
                team.setProjectId(rs.getInt("project_id"));
                team.setTeamName(rs.getString("team_name"));
                team.setDescription(rs.getString("description"));
                return team;

            }
        };

        return getJdbcTemplate().query(SQL_SELECT_TEAMS_BY_USER_ID,
                new Object[]{userId},
                new int[]{BIGINT},
                mapper);

    }

    public List<Team> selectTeamsByUserAndProject(final long userId, int projectId) {
        logger.debug("Select all teams by user id.");

        RowMapper mapper = new RowMapper() {

            public Team mapRow(ResultSet rs, int rowNum) throws SQLException {
                Team team = new Team();
                team.setId(rs.getInt("team_id"));
                team.setProjectId(rs.getInt("project_id"));
                team.setTeamName(rs.getString("team_name"));
                team.setDescription(rs.getString("description"));
                return team;

            }
        };

        return getJdbcTemplate().query(SQL_SELECT_TEAMS_BY_USER_AND_PROJECT,
                new Object[]{userId, projectId},
                new int[]{INTEGER, INTEGER},
                mapper);

    }

    public List<Team> selectTeamsByFilter(Integer userId, List<Integer> teamFilter) {

        RowMapper mapper = new RowMapper() {

            public Team mapRow(ResultSet rs, int rowNum) throws SQLException {
                Team team = new Team();
                team.setId(rs.getInt("team_id"));
                team.setProjectId(rs.getInt("project_id"));
                team.setTeamName(rs.getString("team_name"));
                team.setDescription(rs.getString("description"));
                return team;
            }
        };

        List<Team> retList = getJdbcTemplate().query(SQL_SELECT_TEAMS_BY_USER_ID,
                new Object[]{userId},
                new int[]{BIGINT},
                mapper);

        if (retList.isEmpty()) {
            return (teamFilter.get(0) == 1) ? null : retList;
        }

        int i;
        String op1 = (teamFilter.get(0) == 1) ? " AND " : " OR ";
        String op2 = (teamFilter.get(0) == 1) ? " <> " : " = ";
        StringBuffer sb = new StringBuffer(SQL_SELECT_TEAMS_BY_USER_ID);
        if (teamFilter.size() > 1) {
            sb.append(" AND (");
            for (i = 1; i < teamFilter.size() - 1; i++) {
                sb.append("tu.team_id" + op2 + teamFilter.get(i) + op1);
            }
            sb.append("tu.team_id" + op2 + teamFilter.get(i) + ")");
        } else if (teamFilter.get(0).equals(0)) {
            return null;
        }

        List<Team> retList1 = getJdbcTemplate().query(sb.toString(),
                new Object[]{userId},
                new int[]{BIGINT},
                mapper);
        if (teamFilter.get(0) == 1) {
            return retList1.isEmpty() ? null : retList;
        } else {
            return (retList1.size() < retList.size()) ? null : retList;
        }
    }

    public List<Team> selectTeamsByProjectId(Integer prjid) {
        return find("SELECT t.id, t.team_name FROM team t WHERE project_id = ? ", prjid);
    }

    @Autowired
    public void setTeamUserDao(TeamUserDAO teamUserDao) {
        this.teamUserDao = teamUserDao;
    }
}
