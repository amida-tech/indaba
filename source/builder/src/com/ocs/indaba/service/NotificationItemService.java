/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.NotificationItemDAO;
import com.ocs.indaba.dao.NotificationTypeDAO;
import com.ocs.indaba.dao.ProjectNotifDAO;
import com.ocs.indaba.dao.TokensetDAO;
import com.ocs.indaba.po.NotificationItem;
import com.ocs.indaba.po.NotificationType;
import com.ocs.indaba.po.ProjectNotif;
import com.ocs.indaba.po.Tokenset;
import com.ocs.indaba.util.Templator;
import com.ocs.indaba.vo.NotificationItemView;
import com.ocs.indaba.vo.NotificationView;
import com.ocs.indaba.vo.ProjectNotifSaveResult;
import com.ocs.indaba.vo.ProjectNotifView;
import com.ocs.util.Pagination;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
//import com.ocs.indaba.common.Constants;

/**
 *
 * @author luwb
 */
public class NotificationItemService {

    static private short TEMPLATE_TYPE_OCS = 0;
    static private short TEMPLATE_TYPE_VELOCITY = 1;


    private static final Logger logger = Logger.getLogger(NotificationItemService.class);
    private static final int DEFAULT_LANGUAGE_ID = Constants.LANG_EN;
    private NotificationItemDAO notificationItemDao;
    private ProjectNotifDAO projectNotifDao;
    private NotificationTypeDAO notifTypeDao;
    private TokensetDAO tokensetDao;

    @Autowired
    public void setNotificationItemDao(NotificationItemDAO notificationItemDao) {
        this.notificationItemDao = notificationItemDao;
    }

    @Autowired
    public void setProjectNotifDao(ProjectNotifDAO dao) {
        this.projectNotifDao = dao;
    }

    @Autowired
    public void setNotificationTypeDao(NotificationTypeDAO dao) {
        this.notifTypeDao = dao;
    }

    @Autowired
    public void setTokensetDao(TokensetDAO dao) {
        this.tokensetDao = dao;
    }

    public NotificationItem getNotificationItemByNameAndLanguage(String name, int langId) {
        return notificationItemDao.selectNotificationItemByLanguageAndTypeName(langId, name);
    }

    public NotificationItem getNotificationItemByTypeIdAndLanguageId(int typeId, int langId) {
        return notificationItemDao.selectNotificationItemByTypeAndLanguage(typeId, langId);
    }

    public NotificationItem getNotificationItem(int id) {
        return notificationItemDao.get(id);
    }
    public NotificationItem updateNotificationItem(NotificationItem item) {
        return notificationItemDao.update(item);
    }
    
    public NotificationItem addNotificationItem(NotificationItem item) {
        return notificationItemDao.create(item);
    }

    public List<Map<Integer, NotificationItem>> getAllTextResourceMap() {
        Map<Integer, Map<Integer, NotificationItem>> notificationItemMap = new HashMap<Integer, Map<Integer, NotificationItem>>();
        List<NotificationItem> itemList = notificationItemDao.findAll();
        List<Map<Integer, NotificationItem>> list = new ArrayList<Map<Integer, NotificationItem>>();
        if (itemList != null && !itemList.isEmpty()) {
            for (NotificationItem item : itemList) {
                Map<Integer, NotificationItem> map = notificationItemMap.get(item.getNotificationTypeId());
                if (map == null) {
                    map = new HashMap<Integer, NotificationItem>();
                    notificationItemMap.put(item.getNotificationTypeId(), map);
                    list.add(map);
                }
                map.put(item.getLanguageId(), item);
            }
            return list;
        } else {
            return null;
        }
    }

    public List<NotificationItemView> getNotificationItemViewByLangIdAndTypeId(int lang_id, int type_id) {
        return notificationItemDao.selectNotificationItemViewByLanguageIdAndTypeId(lang_id, type_id);
    }

    public NotificationItem saveNotificationItem(NotificationItem item) {
        return notificationItemDao.saveNotificationItem(item);
    }
    
    public List<NotificationItemView> findNotificationItemsByIds(List<Integer> list) {
        return notificationItemDao.selectNotificationItemByItemIds(list);
    }
    
    public Pagination<NotificationItemView> findNotificationItems(int filterLangId, int filterTypeId, String searchCol, String searchTerm, String sortCol, String sortOrder, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        int count = pageSize;
        int totalCount = (int) notificationItemDao.countNotificationItems(filterLangId, filterTypeId, searchCol, searchTerm);

        if (totalCount == 0) {
            return new Pagination<NotificationItemView>(totalCount, pageNum, pageSize);
        }

        List<NotificationItemView> nitems = notificationItemDao.findNotificationItems(filterLangId, filterTypeId, searchCol, searchTerm, sortCol, sortOrder, offset, count);
        
        Pagination<NotificationItemView> pagination = new Pagination<NotificationItemView>(totalCount, pageNum, pageSize);
        pagination.setRows(nitems);
        return pagination;
    }
    
    
    public NotificationView getDefaultNotificationView(String notificationTypeName, int langId, Map tokenMap) {
        logger.debug("Try to find default notification view of [" + notificationTypeName + "] for language " + langId);

        NotificationItem item = notificationItemDao.selectNotificationItemByLanguageAndTypeName(langId, notificationTypeName);

        if (item == null && langId != DEFAULT_LANGUAGE_ID) {
            logger.debug("Can't find it. Try to find default notification view of [" + notificationTypeName + "] for default language " + DEFAULT_LANGUAGE_ID);
            item = notificationItemDao.selectNotificationItemByLanguageAndTypeName(DEFAULT_LANGUAGE_ID, notificationTypeName);
        }
        
        if (item == null) {
            logger.error("Can't find default Notification Item - " + notificationTypeName);
            return null;
        }

        NotificationType nt = notifTypeDao.getNotificationTypeByName(notificationTypeName);

        if (nt == null) return null;

        return getNotificationView(item, tokenMap, nt.getTemplateType());
    }


    public NotificationView getProjectNotificationView(String notificationTypeName, int projectId, int roleId, int langId, Map tokenMap) {
        logger.debug("Try to find project notif: notif=" + notificationTypeName + "projId=" + projectId + " roleId=" + roleId + " langId=" + langId);

        // first try to find exact match
        ProjectNotif projectNotif = projectNotifDao.selectNotificationItem(projectId, roleId, langId, notificationTypeName);

        if (projectNotif == null && roleId != 0) {
            // try to find match with role 0
            projectNotif = projectNotifDao.selectNotificationItem(projectId, 0, langId, notificationTypeName);
        }

        if (projectNotif == null && langId != DEFAULT_LANGUAGE_ID) {
            // try to get for default language
            projectNotif = projectNotifDao.selectNotificationItem(projectId, roleId, DEFAULT_LANGUAGE_ID, notificationTypeName);

            if (projectNotif == null && roleId != 0) {
                // try to find match with role 0
                projectNotif = projectNotifDao.selectNotificationItem(projectId, 0, DEFAULT_LANGUAGE_ID, notificationTypeName);
            }
        }

        if (projectNotif == null) {
            // no project customized version - use the default
            return getDefaultNotificationView(notificationTypeName, langId, tokenMap);
        }

        NotificationType nt = notifTypeDao.getNotificationTypeByName(notificationTypeName);

        if (nt == null) return null;

        // Got a customized version!
        NotificationItem item = new NotificationItem();
        item.setBodyText(projectNotif.getBodyText());
        item.setLanguageId(projectNotif.getLanguageId());
        item.setSubjectText(projectNotif.getSubjectText());

        return getNotificationView(item, tokenMap, nt.getTemplateType());
    }

    
    private NotificationView getNotificationView(NotificationItem item, Map tokenMap, short templateType) {
        String body = null;
        String subject = null;

        if (templateType == TEMPLATE_TYPE_VELOCITY) {
            body = Templator.instantiateText(item.getBodyText(), tokenMap);
            subject = Templator.instantiateText(item.getSubjectText(), tokenMap);
        } else {
            body = tokenizeBody(item.getBodyText(), tokenMap);
            subject = tokenizeBody(item.getSubjectText(), tokenMap);
        }
        
        NotificationView view = new NotificationView();
        view.setBody(body);
        view.setSubject(subject);
        return view;
    }


    public static void velocitize(NotificationView view, Map tokenMap) {
        Map varMap = new HashMap();
        Iterator it = tokenMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            String key = (String)pairs.getKey();
            String var = "$" + key;
            varMap.put(key, var);
        }
        String velocitizedSubject = tokenizeBody(view.getSubject(), varMap);
        String velocitizedBody = tokenizeBody(view.getBody(), varMap);
        view.setSubject(velocitizedSubject);
        view.setBody(velocitizedBody);
    }


    private static String tokenizeBody(String body, Map tokenMap) {
        String result = tokenizeBody(body, tokenMap, '<', '>');
        return tokenizeBody(result, tokenMap, '[', ']');
    }
    

    private static String tokenizeBody(String body, Map tokenMap, char startChar, char endChar) {
        if (body == null) return null;
        
        StringBuilder sb = new StringBuilder();
        StringBuilder token = new StringBuilder();
        boolean isToken = false;
        for (int i = 0; i < body.length(); i++) {
            char c = body.charAt(i);
            if (isToken) {
                if (c == endChar) {
                    String key = token.toString();
                    if (tokenMap.containsKey(key)) {
                        String value = (String) tokenMap.get(key);
                        sb.append(value);
                    } else {
                        sb.append(startChar);
                        sb.append(key);
                        sb.append(endChar);
                    }
                    isToken = false;
                    token.delete(0, token.length());
                } else if (c == startChar) {
                    sb.append(startChar);
                    sb.append(token.toString());
                    token.delete(0, token.length());
                } else {
                    token.append(c);
                }
            } else {
                if (c == startChar) {
                    isToken = true;
                } else {
                    sb.append(c);
                }
            }
        }
        if (isToken) {
            sb.append(startChar);
            sb.append(token.toString());
        }
        return sb.toString();
    }


    /*
     * findProjectNotifs: this is to support the Project Notification list management in the Control Panel
     */
    public Pagination<ProjectNotifView> findProjectNotifs(int projectId, int filterLangId, int filterRoleId, int filterTypeId, String searchCol, String searchTerm, String sortCol, String sortOrder, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        int count = pageSize;
        int totalCount = (int) projectNotifDao.countProjectNotifs(projectId, filterLangId, filterRoleId, filterTypeId, searchCol, searchTerm);

        if (totalCount == 0) {
            return new Pagination<ProjectNotifView>(totalCount, pageNum, pageSize);
        }

        List<ProjectNotifView> notifs = projectNotifDao.findProjectNotifs(projectId, filterLangId, filterRoleId, filterTypeId, searchCol, searchTerm, sortCol, sortOrder, offset, count);

        if (filterRoleId >= 0) {
            // need to get notifs by IDs. This is because the a notif could have multiple roles, and the filtered
            // results have only the filtered role.
            List<Integer> ids = new ArrayList<Integer>();
            for (ProjectNotifView n : notifs) {
                ids.add(n.getId());
            }
            notifs = projectNotifDao.findProjectNotifs(ids);
        }
        
        Pagination<ProjectNotifView> pagination = new Pagination<ProjectNotifView>(totalCount, pageNum, pageSize);
        pagination.setRows(notifs);
        return pagination;
    }


    public List<ProjectNotifView> findProjectNotifs(int projectId) {
        return projectNotifDao.findProjectNotifs(projectId);
    }

    public List<ProjectNotifView> findProjectNotifs(List<Integer> projectNotifIds) {
        return projectNotifDao.findProjectNotifs(projectNotifIds);
    }

    /*
     * saveProjectNotif: used for creating or updating a notif.
     * The notif object must be fully populated.
     * If creating new notif, the ID of the notif must <= 0.
     * If notif ID is > 0, it must already eixst. Call getProjectNotif() to make sure.
     * The roles list contains the list of role IDs to be applied to the notif.
     * The roles list cannot be empty. If no role is explictly specified, the list must contain the ID 0.
     * The role list must not contain duplicate role IDs.
     *
     * Validate the notif and make sure it won't introduce any ambiguity.
     * If conflict is detected, return null; otherwise return the updated/created notif.
     *
     */

    public ProjectNotifSaveResult saveProjectNotif(ProjectNotif notif, List<Integer> roles) {
        if (roles == null) roles = new ArrayList<Integer>();
        if (roles.isEmpty()) roles.add(0);
        ProjectNotifSaveResult result = new ProjectNotifSaveResult();

        ProjectNotif pn = projectNotifDao.findConflict(notif, roles);

        if (pn != null) {
            result.setProjectNotif(pn);
            result.setResultCode(ProjectNotifSaveResult.RESULT_CODE_CONFLICT);
            return result;
        }

        pn = projectNotifDao.saveProjectNotif(notif, roles);

        if (pn != null) {
            result.setProjectNotif(pn);
            result.setResultCode(ProjectNotifSaveResult.RESULT_CODE_OK);
        } else {
            result.setResultCode(ProjectNotifSaveResult.RESULT_CODE_ERROR);
        }

        return result;
    }


    /*
     * Find a project notif based on ID
     *
     */

    public ProjectNotif getProjectNotif(int projectNotifId) {
        return projectNotifDao.get(projectNotifId);
    }


    /*
     * getProjectNotifView: this is used by the notif editing/creation page in CP.
     */
    public ProjectNotifView getProjectNotifView(int projectNotifId) {
        return projectNotifDao.getProjectNotifView(projectNotifId);
    }

    /*
     * getProjectCustomizableNotificationTypes: this is used to populate notification type selectoin list
     * in CP's project notif management functions.
     * The list only contains project-customizable types.
     * 
     */
    public List<NotificationType> getProjectCustomizableNotificationTypes() {
        return notifTypeDao.getProjectCustomizableNotificationTypes();
    }

    /* 
    * Return all notification type
    */
    public List<NotificationType> getNotificationTypes() {
        return notifTypeDao.getNotificationTypes();
    }
    /*
     * Delete a list of project notifs.
     * This is used by the CP to delete one or more notifs.
     */
    public void deleteProjectNotif(List<Integer> projectNotifIds) {
        projectNotifDao.deleteProjectNotif(projectNotifIds);
    }

    public void deleteNotificationItems(List<Integer> NotifIds) {
        notificationItemDao.deleteNotificationItems(NotifIds);
    }

    public List<Tokenset> getTokensets() {
        return tokensetDao.findAll();
    }

}
