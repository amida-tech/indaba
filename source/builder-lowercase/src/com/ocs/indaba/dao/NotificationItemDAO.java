/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.NotificationItem;
import com.ocs.indaba.vo.NotificationItemView;
import com.ocs.util.ListUtils;
import com.ocs.util.StringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author luwb
 */
public class NotificationItemDAO extends SmartDaoMySqlImpl<NotificationItem, Integer> {

    private static final Logger logger = Logger.getLogger(NotificationItemDAO.class);

    /**
    private static final String SELECT_NOTIFICATION_ITEM_BY_USER_ID_AND_TYPE_NAME =
            "SELECT ni.* FROM notification_item ni, user u, notification_type nt"
            + " WHERE u.id=? AND nt.name=? AND ni.language_id=u.language_id AND"
            + " ni.notification_type_id = nt.id";
     *
     **/

    private static final String SELECT_NOTIFICATION_ITEM_BY_USER_AND_TYPE =
            "SELECT ni.* FROM notification_item ni, user u"
            + " WHERE u.id=? AND ni.language_id=u.language_id AND"
            + " ni.notification_type_id = ?";
    private static final String SELECT_NOTIFICATION_ITEM_BY_LANGUAGE_ID_AND_TYPE_NAME =
            "SELECT ni.* FROM notification_item ni, notification_type nt"
            + " WHERE ni.language_id=? AND nt.name=? AND"
            + " ni.notification_type_id = nt.id";
   
    private static final String SELECT_NOTIFICATION_ITEM_BY_TYPE_AND_LANGUAGE = 
            "SELECT * FROM notification_item WHERE notification_type_id=? AND language_id=?";

    /**
    public NotificationItem selectNotificationItemByNameAndLanguage(String name, int langId) {
        return super.findSingle(SELECT_NOTIFICATION_ITEM_BY_NOTIFICATION_NAME, name, langId);
    }
     * **/

    public NotificationItem selectNotificationItemByTypeAndLanguage(int typeId, int langId) {
        return super.findSingle(SELECT_NOTIFICATION_ITEM_BY_TYPE_AND_LANGUAGE, typeId, langId);
    }
    
    public NotificationItem selectNotificationItemByUserAndType(int userId, int typeId) {
        return super.findSingle(SELECT_NOTIFICATION_ITEM_BY_USER_AND_TYPE, userId, typeId);
    }

    /**
    public NotificationItem selectNotificationItemByUserIdAndTypeName(int userId, String notificationTypeName) {
        Object[] values = new Object[]{userId, notificationTypeName};
        return super.findSingle(SELECT_NOTIFICATION_ITEM_BY_USER_ID_AND_TYPE_NAME, values);
    }
     * **/

    public NotificationItem selectNotificationItemByLanguageAndTypeName(int languageId, String notificationTypeName) {
        Object[] values = new Object[]{languageId, notificationTypeName};
        return super.findSingle(SELECT_NOTIFICATION_ITEM_BY_LANGUAGE_ID_AND_TYPE_NAME, values);
    }
    
    private static final String SELECT_NOTIFICATION_ITEMS = "SELECT ni.*, lang.language_desc as language_name, nt.name as type_name "
            + "FROM notification_item ni "
            + "JOIN language lang ON lang.id=ni.language_id "
            + "JOIN notification_type nt ON nt.id=ni.notification_type_id ";

    public List<NotificationItemView> selectNotificationItemViewByLanguageIdAndTypeId(int lang_id, int type_id) {
        String condition = "";
        if (lang_id > 0) 
            condition = " WHERE ni.language_id=" + lang_id;
        if (type_id > 0) {
            if (condition.length() == 0)
                condition = " WHERE ";
            else 
                condition +=" AND ";
            condition +="ni.notification_type_id="+type_id;
        }
        return getJdbcTemplate().query(SELECT_NOTIFICATION_ITEMS+condition, new NotificationItemViewRowMapper());
    }

    public List<NotificationItemView> selectNotificationItemByItemIds(List<Integer> lstItems) {
        String sql = SELECT_NOTIFICATION_ITEMS + " WHERE ni.id IN (" + ListUtils.listToString(lstItems) + ")";
        return getJdbcTemplate().query(sql, new NotificationItemViewRowMapper());
    }
    
    private class NotificationItemViewRowMapper implements RowMapper {

        public NotificationItemView mapRow(ResultSet rs, int rowNum) throws SQLException {
                NotificationItemView niv = new NotificationItemView();
                niv.setId(rs.getInt("id"));
                niv.setBodyText(rs.getString("body_text"));
                niv.setSubjectText(rs.getString("subject_text"));
                niv.setNotificationTypeId(rs.getInt("notification_type_id"));
                niv.setTypeName(rs.getString("type_name"));
                niv.setLanguageId(rs.getInt("language_id"));
                niv.setLanguageName(rs.getString("language_name"));
                niv.setName(rs.getString("name"));
                return niv;
            }
    }
    
    private static final String REMOVE_NOTIFiCATIONS_BY_IDS =
            "DELETE FROM notification_item WHERE id IN ({0})";

    public void deleteNotificationItems(List<Integer> itemIds) {
        String ids = ListUtils.listToString(itemIds);
        super.delete(MessageFormat.format(REMOVE_NOTIFiCATIONS_BY_IDS, ids));
    }
    
    public NotificationItem saveNotificationItem(NotificationItem item) {
        if (item.getId() > 0) {
            update(item);
        } else {
            item = super.create(item);
        }
        return item;
    }

    public long countNotificationItems(int filterLangId, int filterTypeId, String searchCol, String searchTerm) {
        String selectSql = SELECT_NOTIFICATION_ITEMS + getFilterConditionSql(filterLangId, filterTypeId, searchCol, searchTerm); 
        String countSql = "SELECT COUNT(1) FROM (" + selectSql + ") xxx";
        return super.count(countSql);
    }
    
    public List<NotificationItemView> findNotificationItems(int filterLangId, int filterTypeId, String searchCol, String searchTerm, 
                                    String sortCol, String sortOrder, int offset, int count) {
        
        String selectSql = SELECT_NOTIFICATION_ITEMS + getFilterConditionSql(filterLangId, filterTypeId, searchCol, searchTerm); 
        if (sortCol != null && sortOrder != null)
            selectSql += " ORDER BY " + sortCol + " " + sortOrder;
        selectSql +=" LIMIT " + offset + ", " + count;
        return getJdbcTemplate().query(selectSql, new NotificationItemViewRowMapper());
    }

    private String getFilterConditionSql(int filterLangId, int filterTypeId, String searchCol, String searchTerm) {
 
        String retSql = "";
        String search = "";
        if (filterLangId > 0) {
            retSql = " WHERE ni.language_id=" + filterLangId;
        }
        if (filterTypeId > 0) {
            if (retSql.length() == 0)
                retSql =" WHERE ";
            else
                retSql =" AND ";
            retSql +="ni.notification_type_id=" + filterTypeId;
        }

        if (!StringUtils.isEmpty(searchCol) && !StringUtils.isEmpty(searchTerm)) {
            if (searchCol.equalsIgnoreCase("name")) {
                search = "ni.name LIKE '%" + searchTerm + "%'";
            } else if (searchCol.equalsIgnoreCase("subject")) {
                search = "ni.subject_text LIKE '%" + searchTerm + "%'";
            } else if (searchCol.equalsIgnoreCase("body")) {
                search = "ni.body_text LIKE '%" + searchTerm + "%'";
            }
            if (search.length()!=0) {
                if (retSql.length() == 0)
                    retSql =" WHERE ";
                else
                    retSql =" AND ";
                retSql +=search;
            }
        }
        return retSql;
    }
}

