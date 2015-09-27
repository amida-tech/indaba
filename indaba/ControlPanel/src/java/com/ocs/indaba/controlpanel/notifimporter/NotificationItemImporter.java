package com.ocs.indaba.controlpanel.notifimporter;

import com.ocs.indaba.controlpanel.common.ControlPanelConstants;
import com.ocs.indaba.controlpanel.common.ControlPanelMessages;
import com.ocs.indaba.controlpanel.model.LoginUser;
import com.ocs.indaba.controlpanel.model.Utilities;
import com.ocs.indaba.po.NotificationItem;
import com.ocs.indaba.po.NotificationType;
import com.ocs.indaba.service.NotificationItemService;
import com.ocs.indaba.vo.NotificationItemView;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ningshan
 */
public class NotificationItemImporter {
    
    private static final Logger logger = Logger.getLogger(NotificationItemImporter.class);

    private static NotificationItemService notifItemSrvc = null;

    private SpreadsheetReader reader = null;
    private File file;
    private LoginUser user;
    
    private List<ImpNotificationItem> notifs = null;
    private Map<String, ImpNotificationItem> notifMap = null;
    Map<String, NotificationType> typeMap = null;
    Map<String, Integer> langMap = null;

    private List<String> errors = null;

    private String notifKey(int typeId, int langId) {
        return "" + typeId + ":" + langId;
    }

    private void addNotifToMap(ImpNotificationItem notif) {
        String key = notifKey(notif.getType().getId(), notif.getLanguageId());
        ImpNotificationItem n = notifMap.get(key);

        if (n != null) {
            addError(user.message(ControlPanelMessages.NOTIF_IMPORT__NOTIF_CONFLICT,
                    notif.getName(), notif.getLineNumber(), n.getName(), n.getLineNumber()));
        } else {
            notifMap.put(key, notif);
        }
    }


    private void addNotif(ImpNotificationItem notif) {
        if (notifs == null) {
            notifs = new ArrayList<ImpNotificationItem>();
            notifMap = new HashMap<String, ImpNotificationItem>();
        }

        notifs.add(notif);

    }


    public List<ImpNotificationItem> getNotifs() {
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
    public void setNotificationItemService(NotificationItemService srvc) {
        logger.debug("Got NotificationItem Service");
        notifItemSrvc = srvc;
    }

    // This dummy constructor is needed for autowired to work
    public NotificationItemImporter() {}


    public NotificationItemImporter(LoginUser user, File file) {
        this.file = file;
        this.user = user;
        reader = null;        
    }

    public NotificationItemImporter(LoginUser user, String fileName) {
        this.file = new File(fileName);
        this.user = user;
        reader = null;
    }


    private boolean checkHeaderLine(String[] row) {
        if (row.length < NotificationItemImpDefs.COL_COUNT) {
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

        typeMap = new HashMap<String, NotificationType>();
        
        //Get all notification
        List<NotificationType> allNotifTypes = notifItemSrvc.getNotificationTypes();
        for (NotificationType nt : allNotifTypes) {
            typeMap.put(Utilities.normalizeString(nt.getName()), nt);
        }

        langMap = Utilities.buildLanguageMapN2I();

        while ((row = reader.readNext()) != null) {

            if (row.length < NotificationItemImpDefs.COL_POS_TYPE+1 || StringUtils.isEmpty(row[NotificationItemImpDefs.COL_POS_TYPE])) {
                addError(formatError(
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, NotificationItemImpDefs.COL_LABEL_TYPE)));
                continue;
            }

            if (row.length < NotificationItemImpDefs.COL_POS_NAME+1 || StringUtils.isEmpty(row[NotificationItemImpDefs.COL_POS_NAME])) {
                addError(formatError(
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, NotificationItemImpDefs.COL_LABEL_NAME)));
                continue;
            }

            if (row.length < NotificationItemImpDefs.COL_POS_LANG+1 || StringUtils.isEmpty(row[NotificationItemImpDefs.COL_POS_LANG])) {
                addError(formatError(
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, NotificationItemImpDefs.COL_LABEL_LANG)));
                continue;
            }
            
            if (row.length < NotificationItemImpDefs.COL_POS_SUBJECT+1 || StringUtils.isEmpty(row[NotificationItemImpDefs.COL_POS_SUBJECT])) {
                addError(formatError(
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, NotificationItemImpDefs.COL_LABEL_SUBJECT)));
                continue;
            }
            
            if (row.length < NotificationItemImpDefs.COL_POS_BODY+1 || StringUtils.isEmpty(row[NotificationItemImpDefs.COL_POS_BODY])) {
                addError(formatError(
                        user.message(ControlPanelMessages.INDICATOR_IMPORT__MISSING_COLUMN_VALUE, NotificationItemImpDefs.COL_LABEL_BODY)));
                continue;
            }

            String typeName = row[NotificationItemImpDefs.COL_POS_TYPE];
            String name = row[NotificationItemImpDefs.COL_POS_NAME];
            String langName = row[NotificationItemImpDefs.COL_POS_LANG];
            String subject = row[NotificationItemImpDefs.COL_POS_SUBJECT];
            String body = row[NotificationItemImpDefs.COL_POS_BODY];

            if (name.length() > ControlPanelConstants.MAX_LENGTH_NOTIF_NAME) {
                addError(formatError(user.message(ControlPanelMessages.INDICATOR_IMPORT__FIELD_TOO_LONG, 
                        NotificationItemImpDefs.COL_LABEL_NAME, ControlPanelConstants.MAX_LENGTH_NOTIF_NAME)));
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

            ImpNotificationItem notif = new ImpNotificationItem();
            notif.setBody(body);
            notif.setLanguageId(langId);
            notif.setLanguageName(langName);
            notif.setLineNumber(reader.getLineNumber());
            notif.setName(name);
            notif.setSubject(subject);
            notif.setType(notifType);
            notif.setExistingNotificationItemId(0);
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

    private void validateAgainstExistingNotifs() {
 
        for (Map.Entry<String, Integer> lang : langMap.entrySet()) {
            int langid = lang.getValue();
            List<NotificationItemView> existingNotifs = notifItemSrvc.getNotificationItemViewByLangIdAndTypeId(langid, 0);
            if (existingNotifs == null) 
                break;
            for (NotificationItemView existingNotif : existingNotifs) {
                ImpNotificationItem notif = notifMap.get(notifKey(existingNotif.getNotificationTypeId(), existingNotif.getLanguageId()));
                if (notif != null) {
                        addError(user.message(ControlPanelMessages.NOTIF_IMPORT__DB_CONFLICT,
                                notif.getName(), notif.getLineNumber(), existingNotif.getName()));
                        break;
                }
            }
        }
    }


    private void loadNotif(ImpNotificationItem notif) {
        NotificationItem ni = new NotificationItem();
        ni.setBodyText(notif.getBody());
        ni.setId(0);
        ni.setLanguageId(notif.getLanguageId());
        ni.setName(notif.getName());
        ni.setNotificationTypeId(notif.getType().getId());
        ni.setSubjectText(notif.getSubject());

        if (notif.getExistingNotificationItemId() > 0) {
            // replace the notif
            ni.setId(notif.getExistingNotificationItemId());
        }
        notifItemSrvc.saveNotificationItem(ni);
    }


    public int load() {
        try {
            parse();
        } 
        catch (Exception ex) {
            return -1;
        }

        if (getErrorCount() > 0) return -1;

        // validate against DB
        validateAgainstExistingNotifs();

        if (getErrorCount() > 0) return -1;

        // load into DB
        for (ImpNotificationItem notif : notifs) {
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
