/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.TeamUser;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Types.INTEGER;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;


/**
 *
 * @author Jeff
 */
public class TeamUserDAO extends SmartDaoMySqlImpl<TeamUser, Integer> {

    private static final Logger logger = Logger.getLogger(TeamUserDAO.class);
    private static final String SELECT_TEAM_USER_BY_ID =
            "SELECT * FROM team_user WHERE user_id = ? ";
    private static final String SELECT_TEAM_USERS_BY_USERID =
            "SELECT a.user_id FROM team_user a, team_user b WHERE b.user_id = ? AND a.team_id = b.team_id";

    public TeamUser selectTeamUserByUserId(long userId) {
        logger.debug("Select table team_user by user: " + userId + ". [" + SELECT_TEAM_USER_BY_ID + "].");

        return super.findSingle(SELECT_TEAM_USER_BY_ID, userId);
    }

    public List<Integer> selectTeamUsersByUserId(long userId) {
        logger.debug("Select table team_user by user: " + userId + ". [" + SELECT_TEAM_USERS_BY_USERID + "].");
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getInt("user_id");
            }
        };

        List<Integer> list = getJdbcTemplate().query(SELECT_TEAM_USERS_BY_USERID,
                new Object[]{userId},
                new int[]{INTEGER},
                mapper);
        return list;
    }
}
