/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Groupdef;
import com.ocs.indaba.po.GroupdefRole;
import com.ocs.indaba.po.GroupdefUser;
import com.ocs.indaba.vo.GroupdefRoleView;
import com.ocs.indaba.vo.GroupdefUserView;
import com.ocs.indaba.vo.GroupdefView;
import com.ocs.util.StringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author yc06x
 */
public class GroupdefDAO extends SmartDaoMySqlImpl<Groupdef, Integer>  {

    private static final String SELECT_ENABLED_GROUPDEFS_BY_HORSE =
            "SELECT def.* FROM groupdef def, horse WHERE horse.id=? AND def.enabled<>0 AND def.product_id=horse.product_id ORDER BY def.weight";

    public List<Groupdef> selectEnabledGroupdefsByHorseId(int horseId) {
        return super.find(SELECT_ENABLED_GROUPDEFS_BY_HORSE, horseId);
    }

    private static final String SELECT_GROUPDEFS_VIEW =
            /*
            "SELECT g.*," +
            "GROUP_CONCAT(DISTINCT gu.id SEPARATOR '|') as userIds," +
            "GROUP_CONCAT(DISTINCT CONCAT(u.first_name,' ',u.last_name) SEPARATOR '|') as usernames, "+
            "GROUP_CONCAT(DISTINCT gu.permissions SEPARATOR '|') as userPermissions," +
            "GROUP_CONCAT(DISTINCT gr.role_id SEPARATOR '|') as roleIds, "+
            "GROUP_CONCAT(DISTINCT r.name SEPARATOR '|') as roleNames, GROUP_CONCAT(DISTINCT gr.permissions SEPARATOR '|') rolePermissions " +
            "FROM groupdef g INNER JOIN groupdef_user gu ON g.id=gu.notedef_id INNER JOIN user u ON gu.user_id= u.id INNER JOIN groupdef_role gr ON g.id=gr.notedef_id INNER JOIN role r ON r.id = gr.role_id ";
            */
            "SELECT g.*, gu.userIds,gu.usernames,gu.userPermissions,gr.roleIds,gr.roleNames,gr.rolePermissions " +
            "FROM groupdef g LEFT JOIN ("
            + "SELECT gu.groupdef_id as groupdef_id,GROUP_CONCAT(gu.user_id SEPARATOR '|') as userIds,"
            + "GROUP_CONCAT( CONCAT(u.first_name,' ',u.last_name) SEPARATOR '|') as usernames,"
            + "GROUP_CONCAT( gu.permissions SEPARATOR '|') as userPermissions "
            + "FROM groupdef_user gu INNER JOIN user u ON gu.user_id= u.id GROUP BY groupdef_id"
            + ") gu ON g.id=gu.groupdef_id "
            + "LEFT JOIN ("
            + "SELECT gr.groupdef_id as groupdef_id,GROUP_CONCAT( gr.role_id SEPARATOR '|') as roleIds,"
            + "GROUP_CONCAT( r.name SEPARATOR '|') as roleNames,GROUP_CONCAT(gr.permissions SEPARATOR '|') rolePermissions "
            + "FROM groupdef_role gr INNER JOIN role r ON r.id = gr.role_id GROUP BY gr.groupdef_id"
            + ") gr ON g.id=gr.groupdef_id";
            
    public List<GroupdefView> selectGroupdefViewsByProductId(int productId, boolean enabled) {
        String sql = SELECT_GROUPDEFS_VIEW + " WHERE g.product_id =" + productId;
        if (enabled)
            sql +=" AND g.enabled=true";
        return getJdbcTemplate().query(sql, new GroupdefViewRowMapper());
    }

    public GroupdefView selectGroupdefViewByNotedefId(int notedefId) {
        String sql = SELECT_GROUPDEFS_VIEW + " WHERE g.id =" + notedefId;
        List<GroupdefView> list = getJdbcTemplate().query(sql, new GroupdefViewRowMapper());
        return (list == null)?null:list.get(0);
    }

    public List<GroupdefView> selectGroupdefViewsByProductId(int productId, boolean enabled, String sortName, String sortOrder, int offset, int count) {
        String sql = SELECT_GROUPDEFS_VIEW + " WHERE g.product_id =" + productId;
        if (enabled)
            sql += " AND g.enabled<>0";
        sql +=" GROUP BY g.id";
        if (sortName != null && sortName.trim().length()!=0 && sortOrder != null && sortOrder.trim().length()!=0) 
            sql +=" ORDER BY g." + sortName.trim() + " " + sortOrder;
        sql +=" LIMIT " + offset + ", " + count;
        return getJdbcTemplate().query(sql, new GroupdefDAO.GroupdefViewRowMapper());
    }
    
    
   private static final String COUNT_GROUPDEFS =
            "SELECT count(*) FROM groupdef g"; 
    
    public long countGroupdefsByProductId(int productId, boolean enabled) {
        String sql = COUNT_GROUPDEFS + " WHERE g.product_id =" + productId;
        if (enabled)
            sql += " AND g.enabled<>0";
        return super.count(sql);
    }
    
    private static final String REMOVE_GROUPDEF_ROLES =
            "DELETE FROM groupdef_role WHERE groupdef_id=?";
    private static final String INSERT_GROUPDEF_ROLE =
            "INSERT INTO groupdef_role (groupdef_id, role_id, permissions) VALUES (?, ?, ?) ";
    private static final String REMOVE_GROUPDEF_USERS =
            "DELETE FROM groupdef_user WHERE groupdef_id=?";
    private static final String INSERT_GROUPDEF_USER =
            "INSERT INTO groupdef_user (groupdef_id, user_id, permissions) VALUES (?, ?, ?) ";
    public Groupdef save(GroupdefView groupdefview) {
        Groupdef groupdef = groupdefview;
        if (groupdefview.getId() > 0) {
            super.delete(REMOVE_GROUPDEF_ROLES, groupdefview.getId());
            super.delete(REMOVE_GROUPDEF_USERS, groupdefview.getId());
            update(groupdefview);
        }
        else {
            groupdef = create(groupdefview);
            groupdefview.setId(groupdef.getId());
        }
        for (GroupdefRoleView nr : groupdefview.getRoles()) {
            super.update(INSERT_GROUPDEF_ROLE,groupdefview.getId(), nr.getRoleId(), nr.getPermissions());
        }
        for (GroupdefUserView nu : groupdefview.getUsers()) {
            super.update(INSERT_GROUPDEF_USER, groupdefview.getId(), nu.getUserId(), nu.getPermissions());
        }
        return groupdef;
    }
    
    public Groupdef save(Groupdef groupdef, List<GroupdefUser> users, List<GroupdefRole> roles) {
        if (groupdef.getId() > 0) {
            super.delete(REMOVE_GROUPDEF_ROLES, groupdef.getId());
            super.delete(REMOVE_GROUPDEF_USERS, groupdef.getId());
            update(groupdef);
        }
        else {
            groupdef = create(groupdef);
        }
        for (GroupdefRole gr : roles) {
            super.update(INSERT_GROUPDEF_ROLE,groupdef.getId(), gr.getRoleId(), gr.getPermissions());
        }
        for (GroupdefUser gu : users) {
            super.update(INSERT_GROUPDEF_USER, groupdef.getId(), gu.getUserId(), gu.getPermissions());
        }
        return groupdef;
    }



    public void setEnabled(int groupdefId, boolean enabled) {
        short value = 0;
        if (enabled) value = 1;

        super.update("UPDATE groupdef SET enabled = ? WHERE id=?", value, groupdefId);
    }


    
    private class GroupdefViewRowMapper implements RowMapper {

        public GroupdefView mapRow(ResultSet rs, int rowNum) throws SQLException {
                GroupdefView gv = new GroupdefView();
                gv.setId(rs.getInt("id"));
                gv.setProductId(rs.getInt("product_id"));
                gv.setName(rs.getString("name"));
                gv.setDescription(rs.getString("description"));
                gv.setWeight(rs.getInt("weight"));
                gv.setEnabled(rs.getBoolean("enabled"));

                String str = rs.getString("userIds");
                if (str != null && str.trim().length() !=0) {
                    String[] userIds = str.split("\\|");
                    String[] userNames = rs.getString("usernames").split("\\|");
                    String[] userPermissions = rs.getString("userpermissions").split("\\|");
                    List<GroupdefUserView> users = new ArrayList<GroupdefUserView>();
                    for (int idx = 0; idx != userIds.length; idx ++) {
                        GroupdefUserView u = new GroupdefUserView();
                        u.setUserId(StringUtils.str2int(userIds[idx]));
                        u.setUserName(userNames[idx]);
                        u.setPermissions(StringUtils.str2int(userPermissions[idx]));
                        users.add(u);
                    }
                    gv.setUsers(users);
                }
                str = rs.getString("roleIds");
                if (str != null && str.trim().length() !=0) {
                    String[] roleIds = str.split("\\|");
                    String[] roleNames = rs.getString("rolenames").split("\\|");
                    String[] rolePermissions = rs.getString("rolepermissions").split("\\|");
                    List<GroupdefRoleView> roles = new ArrayList<GroupdefRoleView>();
                    for (int idx = 0; idx != roleIds.length; idx ++) {
                        GroupdefRoleView u = new GroupdefRoleView();
                        u.setRoleId(StringUtils.str2int(roleIds[idx]));
                        u.setRoleName(roleNames[idx]);
                        u.setPermissions(StringUtils.str2int(rolePermissions[idx]));
                        roles.add(u);
                    }
                    gv.setRoles(roles);
                }
                return gv;
            }
    }
    
    
}
