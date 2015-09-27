/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.po.NotificationType;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class NotificationTypeDAO extends SmartDaoMySqlImpl<NotificationType, Integer> {

    private static final String FIND_PROJECT_CUSTOMIZEABLE_TYPES =
            "SELECT * FROM notification_type WHERE project_customizable!=0 " +
            "ORDER BY name ASC";

    private static final String GET_ALL_TYPES = 
            "SELECT * FROM notification_type ORDER BY name ASC";

    /*
     * Return notification types that can be customized by project
     */
    public List<NotificationType> getProjectCustomizableNotificationTypes() {
        return this.find(FIND_PROJECT_CUSTOMIZEABLE_TYPES);
    }

    /*
     * Return all notification types
     */
    public List<NotificationType> getNotificationTypes() {
        return this.find(GET_ALL_TYPES);
    }

    private static final String SELECT_BY_NAME =
            "SELECT * FROM notification_type WHERE name=?";

    public NotificationType getNotificationTypeByName(String name) {
        return super.findSingle(SELECT_BY_NAME, name);
    }
}
