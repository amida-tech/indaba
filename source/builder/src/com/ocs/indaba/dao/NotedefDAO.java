/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Notedef;
import com.ocs.indaba.po.NotedefRole;
import com.ocs.indaba.po.NotedefUser;
import com.ocs.indaba.vo.NotedefRoleView;
import com.ocs.indaba.vo.NotedefUserView;
import com.ocs.indaba.vo.NotedefView;
import com.ocs.util.StringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author yc06x
 */
public class NotedefDAO extends SmartDaoMySqlImpl<Notedef, Integer>  {
    private static final Logger logger = Logger.getLogger(NotedefDAO.class);
    private static final String SELECT_ENABLED_NOTEDEFS_BY_HORSE =
                "SELECT notedef.* FROM notedef, horse WHERE horse.id=? AND notedef.enabled<>0 AND notedef.product_id=horse.product_id ORDER BY notedef.weight";

    private static final String SELECT_ENABLED_NOTEDEFS_BY_PRODUCT =
                "SELECT notedef.* FROM notedef WHERE notedef.enabled<>0 AND notedef.product_id=? ORDER BY notedef.weight";

    public List<Notedef> selectEnabledNotedefsByHorseId(int horseId) {
        return super.find(SELECT_ENABLED_NOTEDEFS_BY_HORSE, horseId);
    }

    public List<Notedef> selectEnabledNotedefsByProductId(int productId) {
        return super.find(SELECT_ENABLED_NOTEDEFS_BY_PRODUCT, productId);
    }
  
    private static final String SELECT_NOTEDEFS_VIEW =
            /*
            "SELECT n.*," +
            "GROUP_CONCAT(DISTINCT nu.id SEPARATOR '|') as userIds," +
            "GROUP_CONCAT(DISTINCT CONCAT(u.first_name,' ',u.last_name) SEPARATOR '|') as usernames, "
            + "GROUP_CONCAT(nu.permissions SEPARATOR '|') as userPermissions," +
            "GROUP_CONCAT(DISTINCT nr.role_id SEPARATOR '|') as roleIds, "
            + "GROUP_CONCAT(DISTINCT r.name SEPARATOR '|') as roleNames, "
            + "GROUP_CONCAT(DISTINCT nr.permissions SEPARATOR '|') rolePermissions " +
            "FROM notedef n LEFT JOIN notedef_user nu ON n.id=nu.notedef_id LEFT JOIN user u ON nu.user_id= u.id LEFT JOIN notedef_role nr ON n.id=nr.notedef_id LEFT JOIN role r ON r.id = nr.role_id ";
            */
            "SELECT n.*, nu.userIds,nu.usernames,nu.userPermissions,nr.roleIds,nr.roleNames,nr.rolePermissions " +
            "FROM notedef n LEFT JOIN ("
            + "SELECT nu.notedef_id as notedef_id,GROUP_CONCAT(nu.user_id SEPARATOR '|') as userIds,"
            + "GROUP_CONCAT( CONCAT(u.first_name,' ',u.last_name) SEPARATOR '|') as usernames,"
            + "GROUP_CONCAT( nu.permissions SEPARATOR '|') as userPermissions "
            + "FROM notedef_user nu INNER JOIN user u ON nu.user_id= u.id GROUP BY notedef_id"
            + ") nu ON n.id=nu.notedef_id "
            + "LEFT JOIN ("
            + "SELECT nr.notedef_id as notedef_id,GROUP_CONCAT( nr.role_id SEPARATOR '|') as roleIds,"
            + "GROUP_CONCAT( r.name SEPARATOR '|') as roleNames,GROUP_CONCAT(nr.permissions SEPARATOR '|') rolePermissions "
            + "FROM notedef_role nr INNER JOIN role r ON r.id = nr.role_id GROUP BY nr.notedef_id"
            + ") nr ON n.id=nr.notedef_id";

    public List<NotedefView> selectEnabledNotedefViewsByProductId(int productId) {
        String sql = SELECT_NOTEDEFS_VIEW + " WHERE n.enabled<>0 AND n.product_id =" + productId + " GROUP BY n.id";
        return getJdbcTemplate().query(sql, new NotedefViewRowMapper());
    }
    
    public List<NotedefView> selectNotedefViewsByProductId(int productId, boolean enabled) {
        
        String sql = SELECT_NOTEDEFS_VIEW + " WHERE n.product_id =" + productId;
        if (enabled)
            sql += " AND n.enabled<>0";
        sql +=" GROUP BY n.id";
        
        return getJdbcTemplate().query(sql, new NotedefViewRowMapper());
    }

    public List<NotedefView> selectNotedefViewsByProductId(int productId, boolean enabled, String sortName, String sortOrder, int offset, int count) {
        String sql = SELECT_NOTEDEFS_VIEW + " WHERE n.product_id =" + productId;
        if (enabled)
            sql += " AND n.enabled<>0";
        sql +=" GROUP BY n.id";
        if (sortName != null && sortName.trim().length()!=0 && sortOrder != null && sortOrder.trim().length()!=0) 
            sql +=" ORDER BY n." + sortName.trim() + " " + sortOrder;
        sql +=" LIMIT " + offset + ", " + count;
        return getJdbcTemplate().query(sql, new NotedefViewRowMapper());
    }
    
    public NotedefView selectNotedefViewByNotedefId(int notedefId) {
        String sql = SELECT_NOTEDEFS_VIEW + " WHERE n.id =" + notedefId;
        List<NotedefView> list = getJdbcTemplate().query(sql, new NotedefViewRowMapper());
        return (list == null)?null:list.get(0);
    }
    
   private static final String COUNT_NOTEDEFS =
            "SELECT count(*) FROM notedef n ";
    
    public long countNotedefsByProductId(int productId, boolean enabled) {
        String sql = COUNT_NOTEDEFS + " WHERE n.product_id =" + productId;
        if (enabled)
            sql += " AND n.enabled<>0";
        return super.count(sql);
    }
    
    private static final String REMOVE_NOTEDEF_ROLES =
            "DELETE FROM notedef_role WHERE notedef_id=?";
    private static final String INSERT_NOTEDEF_ROLE =
            "INSERT INTO notedef_role (notedef_id, role_id, permissions) VALUES (?, ?, ?) ";
    private static final String REMOVE_NOTEDEF_USERS =
            "DELETE FROM notedef_user WHERE notedef_id=?";
    private static final String INSERT_NOTEDEF_USER =
            "INSERT INTO notedef_user (notedef_id, user_id, permissions) VALUES (?, ?, ?) ";

    public Notedef save(NotedefView notedefview) {
        Notedef notedef = notedefview;
        if (notedefview.getId() > 0) {
            super.delete(REMOVE_NOTEDEF_ROLES, notedefview.getId());
            super.delete(REMOVE_NOTEDEF_USERS, notedefview.getId());
            update(notedefview);
        }
        else {
            notedef = create(notedefview);
            notedefview.setId(notedef.getId());
        }
        for (NotedefRoleView nr : notedefview.getRoles()) {
            super.update(INSERT_NOTEDEF_ROLE,notedefview.getId(), nr.getRoleId(), nr.getPermissions());
        }
        for (NotedefUserView nu : notedefview.getUsers()) {
            super.update(INSERT_NOTEDEF_USER, notedefview.getId(), nu.getUserId(), nu.getPermissions());
        }
        return notedef;
    }
    
    public Notedef save(Notedef notedef, List<NotedefUser> users, List<NotedefRole> roles) {
        if (notedef.getId() > 0) {
            super.delete(REMOVE_NOTEDEF_ROLES, notedef.getId());
            super.delete(REMOVE_NOTEDEF_USERS, notedef.getId());
            update(notedef);
        }
        else {
            notedef = create(notedef);
        }
        logger.debug(notedef);
        for (NotedefRole nr : roles) {
            super.update(INSERT_NOTEDEF_ROLE,notedef.getId(), nr.getRoleId(), nr.getPermissions());
        }
        for (NotedefUser nu : users) {
            super.update(INSERT_NOTEDEF_USER, notedef.getId(), nu.getUserId(), nu.getPermissions());
        }
        return notedef;
    }
    
    public void delete(Integer notedefId) {
        if (notedefId != null) {
            super.delete(REMOVE_NOTEDEF_ROLES, notedefId);
            super.delete(REMOVE_NOTEDEF_USERS, notedefId);
            super.delete(notedefId);
        }
    }


    public void setEnabled(int notedefId, boolean enabled) {
        short value = 0;
        if (enabled) value = 1;
        
        super.update("UPDATE notedef SET enabled = ? WHERE id=?", value, notedefId);
    }

    
    private class NotedefViewRowMapper implements RowMapper {

        public NotedefView mapRow(ResultSet rs, int rowNum) throws SQLException {
                NotedefView nv = new NotedefView();
                nv.setId(rs.getInt("id"));
                nv.setProductId(rs.getInt("product_id"));
                nv.setName(rs.getString("name"));
                nv.setDescription(rs.getString("description"));
                nv.setWeight(rs.getInt("weight"));
                nv.setEnabled(rs.getBoolean("enabled"));

                String str = rs.getString("userIds");
                logger.debug("userIds :" + str +" : " + rs.getString("userpermissions"));
                if (str != null && str.trim().length() !=0) {
                    String[] userIds = str.split("\\|");
                    String[] userNames = rs.getString("usernames").split("\\|");
                    String[] userPermissions = rs.getString("userpermissions").split("\\|");
                    List<NotedefUserView> users = new ArrayList<NotedefUserView>();
                    for (int idx = 0; idx != userIds.length; idx ++) {
                        NotedefUserView u = new NotedefUserView();
                        u.setUserId(StringUtils.str2int(userIds[idx]));
                        u.setUserName(userNames[idx]);
                        if (idx < userPermissions.length)
                            u.setPermissions(StringUtils.str2int(userPermissions[idx]));
                        users.add(u);
                    }
                    nv.setUsers(users);
                }
                str = rs.getString("roleIds");
                logger.debug("roleIds :" + str +" : " + rs.getString("rolepermissions"));
                if (str != null && str.trim().length() !=0) {
                    String[] roleIds = str.split("\\|");
                    String[] roleNames = rs.getString("rolenames").split("\\|");
                    String[] rolePermissions = rs.getString("rolepermissions").split("\\|");
                    List<NotedefRoleView> roles = new ArrayList<NotedefRoleView>();
                    for (int idx = 0; idx != roleIds.length; idx ++) {
                        NotedefRoleView u = new NotedefRoleView();
                        u.setRoleId(StringUtils.str2int(roleIds[idx]));
                        u.setRoleName(roleNames[idx]);
                        if (idx < rolePermissions.length)
                            u.setPermissions(StringUtils.str2int(rolePermissions[idx]));
                        roles.add(u);
                    }
                    nv.setRoles(roles);
                }
                return nv;
            }
    }


}
