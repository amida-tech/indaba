/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.controlpanel.notifimporter;

import com.ocs.indaba.service.NotificationItemService;
import com.ocs.indaba.vo.NotificationItemView;
import com.ocs.ssu.SpreadsheetWriter;
import com.ocs.ssu.UnsupportedFileTypeException;
import com.ocs.ssu.WriterFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author ningshan
 */
public class NotificationItemExporter {
    
    private static final Logger logger = Logger.getLogger(NotificationItemExporter.class);

    private List<Integer>  niList = null;           //List of Notification Items
    private SpreadsheetWriter writer = null;

    private NotificationItemService notifItemSrvc;

    public NotificationItemExporter(NotificationItemService notifItemSrvc, List<Integer> niList, OutputStream output) throws IOException, UnsupportedFileTypeException {
        this.notifItemSrvc = notifItemSrvc;
        this.niList = niList;
        writer = WriterFactory.createWriter(output, WriterFactory.FILE_TYPE_XLS);
    }

    public int export() throws IOException {
        
        if (niList == null || niList.isEmpty()) 
            return 0;

        List<NotificationItemView> notifs = notifItemSrvc.findNotificationItemsByIds(niList);

        String[] line = new String[NotificationItemImpDefs.COL_COUNT];
        line[NotificationItemImpDefs.COL_POS_TYPE] = NotificationItemImpDefs.COL_LABEL_TYPE;
        line[NotificationItemImpDefs.COL_POS_NAME] = NotificationItemImpDefs.COL_LABEL_NAME;
        line[NotificationItemImpDefs.COL_POS_LANG] = NotificationItemImpDefs.COL_LABEL_LANG;
        line[NotificationItemImpDefs.COL_POS_SUBJECT] = NotificationItemImpDefs.COL_LABEL_SUBJECT;
        line[NotificationItemImpDefs.COL_POS_BODY] = NotificationItemImpDefs.COL_LABEL_BODY;
        writer.writeNext(line);

        for (NotificationItemView notif : notifs) {
            line[NotificationItemImpDefs.COL_POS_TYPE] = notif.getTypeName();
            line[NotificationItemImpDefs.COL_POS_NAME] = notif.getName();
            line[NotificationItemImpDefs.COL_POS_LANG] = notif.getLanguageName();
            line[NotificationItemImpDefs.COL_POS_SUBJECT] = notif.getSubjectText();
            line[NotificationItemImpDefs.COL_POS_BODY] = notif.getBodyText();
            writer.writeNext(line);
        }

        writer.flush();
        writer.close();

        return notifs.size();
    }
    
}
