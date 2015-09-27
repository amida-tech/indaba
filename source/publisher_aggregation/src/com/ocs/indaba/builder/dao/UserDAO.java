/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.builder.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.User;
import com.ocs.indaba.vo.UserView;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author luwb
 */
public class UserDAO extends SmartDaoMySqlImpl<User, Integer> {

    private static final Logger log = Logger.getLogger(UserDAO.class);
    private static final String SELECT_ALL_USERS_IN_PRODUCT =
            "SELECT u.id, u.first_name, u.last_name, u.username, u.email, r.name " +
            "FROM user u " +
            "LEFT JOIN project_membership pm ON pm.user_id = u.id " +
            "LEFT JOIN project proj ON pm.project_id = proj.id " +
            "LEFT JOIN product prod ON prod.project_id = proj.id " +
            "LEFT JOIN role r ON r.id = pm.role_id " +
            "WHERE prod.id = ?";

    public int selectOrgIdByUserId(int userId) {
        log.debug("select organization id by user id" + userId);
        return super.get(userId).getOrganizationId();
    }

    public Map<Integer, UserView> selectAllUsers(int productId) {

        List<UserView> users = getJdbcTemplate().query(SELECT_ALL_USERS_IN_PRODUCT,
                new Object[]{productId}, new RowMapper() {

            public UserView mapRow(ResultSet rs, int i) throws SQLException {
                UserView uv = new UserView();
                uv.setUserId(rs.getInt("id"));
                uv.setFirstName(rs.getString("first_name"));
                uv.setLastName(rs.getString("last_name"));
                uv.setUsername(rs.getString("username"));
                uv.setEmail(rs.getString("email"));
                uv.setRole(rs.getString("r.name"));
                return uv;
            }
        });
        Map<Integer, UserView> userMap = new HashMap<Integer, UserView>();
        if (users != null && !users.isEmpty()) {
            for (UserView uv : users) {
                userMap.put(uv.getUserId(), uv);
            }
        }
        return userMap;
    }
}
