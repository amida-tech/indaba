/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.controlpanel.notifimporter;

import com.ocs.indaba.service.NotificationItemService;
import com.ocs.indaba.vo.ProjectNotifView;
import com.ocs.ssu.SpreadsheetWriter;
import com.ocs.ssu.UnsupportedFileTypeException;
import com.ocs.ssu.WriterFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author yc06x
 */
public class ProjectNotifExporter {
    
    private static final Logger logger = Logger.getLogger(ProjectNotifExporter.class);

    private List<Integer> projectNotifIds = null;
    private SpreadsheetWriter writer = null;

    private NotificationItemService notifItemSrvc;

    public ProjectNotifExporter(NotificationItemService notifItemSrvc, List<Integer> projectNotifIds, OutputStream output) throws IOException, UnsupportedFileTypeException {
        this.notifItemSrvc = notifItemSrvc;
        this.projectNotifIds = projectNotifIds;
        writer = WriterFactory.createWriter(output, WriterFactory.FILE_TYPE_XLS);
    }

    public int export() throws IOException {
        if (projectNotifIds == null || projectNotifIds.isEmpty()) return 0;

        List<ProjectNotifView> notifs = notifItemSrvc.findProjectNotifs(projectNotifIds);

        String[] line = new String[7];
        line[ProjectNotifImpDefs.COL_POS_TYPE] = ProjectNotifImpDefs.COL_LABEL_TYPE;
        line[ProjectNotifImpDefs.COL_POS_NAME] = ProjectNotifImpDefs.COL_LABEL_NAME;
        line[ProjectNotifImpDefs.COL_POS_DESC] = ProjectNotifImpDefs.COL_LABEL_DESC;
        line[ProjectNotifImpDefs.COL_POS_LANG] = ProjectNotifImpDefs.COL_LABEL_LANG;
        line[ProjectNotifImpDefs.COL_POS_ROLES] = ProjectNotifImpDefs.COL_LABEL_ROLES;
        line[ProjectNotifImpDefs.COL_POS_SUBJECT] = ProjectNotifImpDefs.COL_LABEL_SUBJECT;
        line[ProjectNotifImpDefs.COL_POS_BODY] = ProjectNotifImpDefs.COL_LABEL_BODY;
        writer.writeNext(line);

        for (ProjectNotifView notif : notifs) {
            line[ProjectNotifImpDefs.COL_POS_TYPE] = notif.getTypeName();
            line[ProjectNotifImpDefs.COL_POS_NAME] = notif.getName();
            line[ProjectNotifImpDefs.COL_POS_DESC] = notif.getDescription();
            line[ProjectNotifImpDefs.COL_POS_LANG] = notif.getLanguageName();
            line[ProjectNotifImpDefs.COL_POS_ROLES] = notif.getRoleNames();
            line[ProjectNotifImpDefs.COL_POS_SUBJECT] = notif.getSubjectText();
            line[ProjectNotifImpDefs.COL_POS_BODY] = notif.getBodyText();
            writer.writeNext(line);
        }

        writer.flush();
        writer.close();

        return notifs.size();
    }

}
