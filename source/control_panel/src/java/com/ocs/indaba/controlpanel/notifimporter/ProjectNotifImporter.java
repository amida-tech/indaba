/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.controlpanel.notifimporter;

import com.ocs.indaba.controlpanel.common.ControlPanelConstants;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.controlpanel.model.Utilities;
import com.ocs.indaba.po.NotificationType;
import com.ocs.indaba.po.ProjectNotif;
import com.ocs.indaba.po.Role;
import com.ocs.indaba.service.NotificationItemService;
import com.ocs.indaba.service.RoleService;
import com.ocs.indaba.vo.ProjectNotifView;
import com.ocs.ssu.NotUTF8EncodedException;
import com.ocs.ssu.ReaderFactory;
import com.ocs.ssu.SpreadsheetReader;
import com.ocs.ssu.UnsupportedFileTypeException;
import com.ocs.util.StringUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author yc06x
 */
@Component
public class ProjectNotifImporter {

    private static final Logger logger = Logger.getLogger(ProjectNotifImporter.class);

    private static RoleService roleSrvc = null;
    private static NotificationItemService notifItemSrvc = null;

    private SpreadsheetReader reader = null;
    private File file;
    private LoginUser user;
    private int projectId;
    
    private List<ImpProjectNotif> notifs = null;
    private Map<String, ImpProjectNotif> notifMap = null;
    Map<String, Role> roleNameMap = null;
    Map<Integer, Role> roleIdMap = null;
    Map<String, NotificationType> typeMap = null;
    Map<String, Integer> langMap = null;

    private List<String> errors = null;

    private String notifKey(int typeId, int langId, int roleId) {
        return "" + typeId + ":" + langId + ":" + roleId;
    }

    private void addNotifToMap(ImpProjectNotif notif, int roleId) {
        String key = notifKey(notif.getType().getId(), notif.getLanguageId(), roleId);
        ImpProjectNotif n = notifMap.get(key);

        if (n != null) {
            addError(user.message(ControlPanelMessages.NOTIF_IMPORT__NOTIF_CONFLICT,
                    notif.getName(), notif.getLineNumber(), n.getName(), n.getLineNumber()));
        } else {
            notifMap.put(key, notif);
        }
    }


    private void addNotif(ImpProjectNotif notif) {
        if (notifs == null) {
            notifs = new ArrayList<ImpProjectNotif>();
            notifMap = new HashMap<String, ImpProjectNotif>();
        }

        notifs.add(notif);

        List<Integer> roleIds = notif.getRoleIds();

        if (roleIds.isEmpty()) {
            roleIds.add(0);
        }
        
        for (Integer roleId : roleIds) {
            addNotifToMap(notif, roleId);
        }
    }


    public List<ImpProjectNotif> getNotifs() {
        return notifs;
    }


    private void addError(String error) {
        if (errors == null) {
            errors = new ArrayList<String>();
        }
        errors.add(error);
    }

    public List<String> getErrors() {
        return errors;
    }

    public int getErrorCount() {
        return (errors == null) ? 0 : errors.size();
    }

    @Autowired
    public void setRoleService(RoleService srvc) {
        logger.debug("Got Role Service");
        roleSrvc = srvc;
    }

    @Autowired
    public void setNotificationItemService(NotificationItemService srvc) {
        logger.debug("Got NotificationItem Service");
        notifItemSrvc = srvc;
    }

    // This dummy constructor is needed for autowired to work
    public ProjectNotifImporter() {}


    public ProjectNotifImporter(LoginUser user, File file, int projectId) {
        this.file = file;
        this.user = user;
        this.projectId = projectId;
        reader = null;        
    }

    public ProjectNotifImporter(LoginUser user, String fileName, int projectId) {
        this.file = new File(fileName);
        this.user = user;
        this.projectId = projectId;
        reader = null;
    }


    private boolean checkHeaderLine(String[] row) {
        if (row.length < ProjectNotifImpDefs.COL_COUNT) {
            addError(formatError(user.message(ControlPanelMessages.INDICATOR_IMPORT__INCOMPLETE_HEADER)));
            return false;
        }

        for (int i = 0; i < row.length; i++) {
            if (row[i].isEmpty()) {
                addError(formatError(user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_HEADER)));
                return false;
            }
        }

        if (!Utilities.similar(row[ProjectNotifImpDefs.COL_POS_TYPE], ProjectNotifImpDefs.COL_LABEL_TYPE)) {
            addError(formatError(user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN,
                    ProjectNotifImpDefs.COL_LABEL_TYPE, row[ProjectNotifImpDefs.COL_POS_TYPE])));
            return false;
        } else if (!Utilities.similar(row[ProjectNotifImpDefs.COL_POS_NAME], ProjectNotifImpDefs.COL_LABEL_NAME)) {
            addError(formatError(user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN,
                    ProjectNotifImpDefs.COL_LABEL_NAME, row[ProjectNotifImpDefs.COL_POS_NAME])));
            return false;
        } else if (!Utilities.similar(row[ProjectNotifImpDefs.COL_POS_DESC], ProjectNotifImpDefs.COL_LABEL_DESC)) {
            addError(formatError(user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN,
                    ProjectNotifImpDefs.COL_LABEL_DESC, row[ProjectNotifImpDefs.COL_POS_DESC])));
            return false;
        } else if (!Utilities.similar(row[ProjectNotifImpDefs.COL_POS_LANG], ProjectNotifImpDefs.COL_LABEL_LANG)) {
            addError(formatError(user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN,
                    ProjectNotifImpDefs.COL_LABEL_LANG, row[ProjectNotifImpDefs.COL_POS_LANG])));
            return false;
        } else if (!Utilities.similar(row[ProjectNotifImpDefs.COL_POS_ROLES], ProjectNotifImpDefs.COL_LABEL_ROLES)) {
            addError(formatError(user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN,
                    ProjectNotifImpDefs.COL_LABEL_ROLES, row[ProjectNotifImpDefs.COL_POS_ROLES])));
            return false;
        } else if (!Utilities.similar(row[ProjectNotifImpDefs.COL_POS_SUBJECT], ProjectNotifImpDefs.COL_LABEL_SUBJECT)) {
            addError(formatError(user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN,
                    ProjectNotifImpDefs.COL_LABEL_SUBJECT, row[ProjectNotifImpDefs.COL_POS_SUBJECT])));
            return false;
        } else if (!Utilities.similar(row[ProjectNotifImpDefs.COL_POS_BODY], ProjectNotifImpDefs.COL_LABEL_BODY)) {
            addError(formatError(user.message(ControlPanelMessages.INDICATOR_IMPORT__INVALID_COLUMN,
                    ProjectNotifImpDefs.COL_LABEL_BODY, row[ProjectNotifImpDefs.COL_POS_BODY])));
            return false;
        } else {
            return true;
        }
    }

    private void parse() throws IOException, FileNotFoundException {
        try {
            reader = ReaderFactory.createReader(file);
        } catch (UnsupportedFileTypeException ex) {
            addError(user.message(ControlPanelMessages.INDICATOR_IMPORT__FILE_TYPE_NOT_SUPPORTED, ex.getFileType()));
            return;
        } catch (NotUTF8EncodedException ex) {
            addError(user.message(ControlPanelMessages.INDICATOR_IMPORT__FILE_NOT_UTF8));
        }

        String[] row;

        row = reader.readNext();

        // Look for header line
        if (!checkHeaderLine(row)) {
            return;
        }

        roleNameMap = new HashMap<String, Role>();
        roleIdMap = new HashMap<Integer, Role>();

        List<Role> allRoles = roleSrvc.getAllRoles(projectId);
        for (Role role : allRoles) {
            roleNameMap.put(Utilities.normalizeString(role.getName()), role);
            roleIdMap.put(role.getId(), role);
        }

        typeMap = new HashMap<String, NotificationType>();
        List<NotificationType> allNotifTypes = notifItemSrvc.getProjectCustomizableNotificationTypes();
        for (NotificationType nt : allNotifTypes) {
            typeMap.put(Utilities.normalizeString(nt.getName()), nt);
        }

        langMap = Utilities.buildLanguageMapN2I();

        while ((row = reader.readNext()) != null) {
            if (row.length < ProjectNotifImpDefs.COL_POS_TYPE+1 || StringUtils.isEmpty(row[ProjectNotifImpDefs.COL_POS_TYPE])) {
                addError(formatError(
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, ProjectNotifImpDefs.COL_LABEL_TYPE)));
                continue;
            }

            if (row.length < ProjectNotifImpDefs.COL_POS_NAME+1 || StringUtils.isEmpty(row[ProjectNotifImpDefs.COL_POS_NAME])) {
                addError(formatError(
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, ProjectNotifImpDefs.COL_LABEL_NAME)));
                continue;
            }

            if (row.length < ProjectNotifImpDefs.COL_POS_DESC+1 || StringUtils.isEmpty(row[ProjectNotifImpDefs.COL_POS_DESC])) {
                addError(formatError(
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, ProjectNotifImpDefs.COL_LABEL_DESC)));
                continue;
            }
            
            if (row.length < ProjectNotifImpDefs.COL_POS_LANG+1 || StringUtils.isEmpty(row[ProjectNotifImpDefs.COL_POS_LANG])) {
                addError(formatError(
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, ProjectNotifImpDefs.COL_LABEL_LANG)));
                continue;
            }
            
            if (row.length < ProjectNotifImpDefs.COL_POS_SUBJECT+1 || StringUtils.isEmpty(row[ProjectNotifImpDefs.COL_POS_SUBJECT])) {
                addError(formatError(
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, ProjectNotifImpDefs.COL_LABEL_SUBJECT)));
                continue;
            }
            
            if (row.length < ProjectNotifImpDefs.COL_POS_BODY+1 || StringUtils.isEmpty(row[ProjectNotifImpDefs.COL_POS_BODY])) {
                addError(formatError(
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, ProjectNotifImpDefs.COL_LABEL_BODY)));
                continue;
            }

            String typeName = row[ProjectNotifImpDefs.COL_POS_TYPE];
            String name = row[ProjectNotifImpDefs.COL_POS_NAME];
            String desc = row[ProjectNotifImpDefs.COL_POS_DESC];
            String langName = row[ProjectNotifImpDefs.COL_POS_LANG];
            String roleNameStr = (row.length > ProjectNotifImpDefs.COL_POS_ROLES) ? row[ProjectNotifImpDefs.COL_POS_ROLES] : "";
            String subject = row[ProjectNotifImpDefs.COL_POS_SUBJECT];
            String body = row[ProjectNotifImpDefs.COL_POS_BODY];

            if (name.length() > ControlPanelConstants.MAX_LENGTH_NOTIF_NAME) {
                addError(formatError(user.message(ControlPanelMessages.INDICATOR_IMPORT__FIELD_TOO_LONG, 
                        ProjectNotifImpDefs.COL_LABEL_NAME, ControlPanelConstants.MAX_LENGTH_NOTIF_NAME)));
                continue;
            }

            NotificationType notifType = typeMap.get(Utilities.normalizeString(typeName));
            if (notifType == null) {
                addError(formatError(user.message(ControlPanelMessages.NOTIF_IMPORT__INVALID_TYPE, typeName)));
                continue;
            }

            Integer langId = langMap.get(Utilities.normalizeString(langName));
            if (langId == null) {
                addError(formatError(user.message(ControlPanelMessages.NOTIF_IMPORT__INVALID_LANGUAGE, langName)));
                continue;
            }

            List<Integer> roleIds = new ArrayList<Integer>();
            String[] roleNames = roleNameStr.split(",");

            if (roleNames != null && roleNames.length > 0) {
                for (String roleName : roleNames) {
                    if (roleName.trim().isEmpty()) continue;
                    
                    Role role = roleNameMap.get(Utilities.normalizeString(roleName));
                    if (role == null) {
                        addError(formatError(user.message(ControlPanelMessages.USER_IMPORT__INVALID_ROLE, roleName)));
                        continue;
                    }

                    boolean found = false;
                    for (Integer rid : roleIds) {
                        if (role.getId().intValue() == rid.intValue()) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        roleIds.add(role.getId());
                    }
                }
            }

            ImpProjectNotif notif = new ImpProjectNotif();
            notif.setBody(body);
            notif.setDescription(desc);
            notif.setLanguageId(langId);
            notif.setLanguageName(langName);
            notif.setLineNumber(reader.getLineNumber());
            notif.setName(name);
            notif.setRoleIds(roleIds);
            notif.setSubject(subject);
            notif.setType(notifType);
            notif.setExistingNotifId(0);
            addNotif(notif);
        }
        reader.close();

        if (getErrorCount() == 0) {
            if (notifs == null || notifs.isEmpty()) {
                addError(user.message(ControlPanelMessages.NOTIF_IMPORT__NO_NOTIFS));
            }
        }
    }

    private String formatError(String error) {
        return "Line " + reader.getLineNumber() + ": " + error;
    }


    private boolean sameNotif(ProjectNotifView existingNotif, ImpProjectNotif notif) {
        List<Integer> nRoleIds = notif.getRoleIds();
        List<Integer> eRoleIds = existingNotif.getRoleIds();

        if (nRoleIds.size() != eRoleIds.size()) return false;

        for (Integer nrid : nRoleIds) {
            boolean found = false;
            for (Integer erid : eRoleIds) {
                if (nrid.intValue() == erid.intValue()) {
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }

        return true;
    }


    private void validateAgainstExistingNotifs() {
        List<ProjectNotifView> existingNotifs = notifItemSrvc.findProjectNotifs(projectId);

        if (existingNotifs == null) return;

        for (ProjectNotifView existingNotif : existingNotifs) {
            for (Integer roleId : existingNotif.getRoleIds()) {
                ImpProjectNotif notif = notifMap.get(notifKey(existingNotif.getNotificationTypeId(), existingNotif.getLanguageId(), roleId));

                if (notif != null) {
                    if (!sameNotif(existingNotif, notif)) {
                        // error
                        addError(user.message(ControlPanelMessages.NOTIF_IMPORT__DB_CONFLICT,
                                notif.getName(), notif.getLineNumber(), existingNotif.getName()));
                    } else {
                        notif.setExistingNotifId(existingNotif.getId());
                    }
                    break;
                }
            }
        }
    }


    private void loadNotif(ImpProjectNotif notif) {
        ProjectNotif pn = new ProjectNotif();
        pn.setBodyText(notif.getBody());
        pn.setDescription(notif.getDescription());
        pn.setId(0);
        pn.setLanguageId(notif.getLanguageId());
        pn.setName(notif.getName());
        pn.setNotificationTypeId(notif.getType().getId());
        pn.setProjectId(projectId);
        pn.setSubjectText(notif.getSubject());

        if (notif.getExistingNotifId() > 0) {
            // replace the notif
            pn.setId(notif.getExistingNotifId());
        }

        notifItemSrvc.saveProjectNotif(pn, notif.getRoleIds());
    }


    public int load() {
        try {
            parse();
        } catch (Exception ex) {
            return -1;
        }

        if (getErrorCount() > 0) return -1;

        // validate against DB
        validateAgainstExistingNotifs();

        if (getErrorCount() > 0) return -1;

        // load into DB
        for (ImpProjectNotif notif : notifs) {
            loadNotif(notif);
        }

        return notifs.size();
    }


    public int validate() {
        try {
            parse();
        } catch (Exception ex) {
            return -1;
        }

        if (getErrorCount() > 0) return -1;

        // validate against DB
        validateAgainstExistingNotifs();

        if (getErrorCount() > 0) return -1;

        return notifs.size();
    }


    public void close() {
        if (reader != null) reader.close();
    }

}
