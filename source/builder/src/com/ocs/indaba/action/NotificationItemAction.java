/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.common.Config;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.NotificationItem;
import com.ocs.indaba.service.NotificationItemService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.I18nJsonUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class NotificationItemAction extends BaseAction {

    private static final Logger logger = Logger.getLogger(NotificationItemAction.class);
    private static final String PARAM_OPER = "oper";
    //private static final String PARAM_OPER_LIST = "list";
    private static final String PARAM_OPER_EDIT = "edit";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_ROWS = "rows";
    private static final String PARAM_SORT_INDEX = "sidx";
    private static final String PARAM_SORT_ORDER = "sord";
    private static final String PARAM_SEARCH_FIELD = "searchField";
    private static final String PARAM_SEARCH_STRING = "searchString";
    private static final String PARAM_NOTIFICATION_TYPE_ID = "typeId";
    private static final String PARAM_NOTIFICATION_SUBJECT = "subject";
    private static final String PARAM_EN_TEXT_ITEM_ID = "enItemId";
    private static final String PARAM_EN_TEXT_RSOURCE = "enText";
    private static final String PARAM_FR_TEXT_ITEM_ID = "frItemId";
    private static final String PARAM_FR_TEXT_RSOURCE = "frText";
    private static final int DEFAULT_ROWS = 20;
    private NotificationItemService notificationItemService = null;

    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String action = request.getParameter(PARAM_OPER);
        String searchField = request.getParameter(PARAM_SEARCH_FIELD);
        String searchString = request.getParameter(PARAM_SEARCH_STRING);
        try {
            if (PARAM_OPER_EDIT.equals(action)) {
                int typeId = StringUtils.str2int(request.getParameter(PARAM_NOTIFICATION_TYPE_ID));
                String subject = request.getParameter(PARAM_NOTIFICATION_SUBJECT);
                int enItemId = StringUtils.str2int(request.getParameter(PARAM_EN_TEXT_ITEM_ID), Constants.INVALID_INT_ID);
                String enTxt = request.getParameter(PARAM_EN_TEXT_RSOURCE);
                int frItemId = StringUtils.str2int(request.getParameter(PARAM_FR_TEXT_ITEM_ID), Constants.INVALID_INT_ID);
                String frTxt = request.getParameter(PARAM_FR_TEXT_RSOURCE);
                logger.debug("Edit notification item: "
                        + "\n\ttypeId=" + typeId
                        + "\n\tsubject=" + subject
                        + "\n\tenItemId=" + enItemId
                        + "\n\tenTxt=" + enTxt
                        + "\n\tfrItemId=" + frItemId
                        + "\n\tfrTxt=" + frTxt);
                /*
                 * NotificationItem enItem =
                 * notificationItemService.getNotificationItemByTypeIdAndLanguageId(typeId,
                 * Constants.LANG_EN); if (enItem == null) { enItem = new
                 * NotificationItem(); enItem.setLanguageId(Constants.LANG_EN);
                 * enItem.setSubjectText(subject);
                 * enItem.setNotificationTypeId(typeId);
                 * enItem.setBodyText(enTxt); } else {
                 * enItem.setBodyText(enTxt); }
                 */
                if (!StringUtils.isEmpty(frTxt)) {
                    NotificationItem frItem = notificationItemService.getNotificationItemByTypeIdAndLanguageId(typeId, Constants.LANG_FR);
                    if (frItem == null) {
                        frItem = new NotificationItem();
                        frItem.setLanguageId(Constants.LANG_FR);
                        frItem.setSubjectText(subject);
                        frItem.setNotificationTypeId(typeId);
                        frItem.setBodyText(StringEscapeUtils.unescapeHtml(frTxt));
                        notificationItemService.addNotificationItem(frItem);
                    } else {
                        frItem.setBodyText(StringEscapeUtils.unescapeHtml(frTxt));
                        notificationItemService.updateNotificationItem(frItem);
                    }
                }
            } else {
                int page = StringUtils.str2int(request.getParameter(PARAM_PAGE), 1);
                int rows = StringUtils.str2int(request.getParameter(PARAM_ROWS), DEFAULT_ROWS);
                String sortIndex = request.getParameter(PARAM_SORT_INDEX);
                String sortOrder = request.getParameter(PARAM_SORT_ORDER);
                logger.debug("Get list of notification items:"
                        + "\n\trows=" + rows
                        + "\n\tpage=" + page
                        + "\n\tsortIndex=" + sortIndex
                        + "\n\tsortOrder=" + sortOrder
                        + "\n\tsearchField=" + searchField
                        + "\n\tsearchString=" + searchString);
                List<Map<Integer, NotificationItem>> notificationItemList = notificationItemService.getAllTextResourceMap();
                super.writeMsgUTF8(response, I18nJsonUtils.notificationItemsToJQGridJson(notificationItemList, page, rows));
            }
        } catch (Exception ex) {
            logger.error("Error occurs!", ex);
        }
        return null;
    }

    private void writeSqlToFile(List<String> sqlList) {
        String ouptputDir = Config.getString(Config.KEY_I18N_RESOURCE_SOURCE_ORIG_DIR);

        File sqlFile = new File(new File(ouptputDir).getParent() + "/schema", "i18n_text_resources.sql");
        logger.debug("Add sql to file: " + sqlFile.toString() + "\n" + sqlList);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(sqlFile, true));
            if (sqlList != null && !sqlList.isEmpty()) {
                for (String sql : sqlList) {
                    writer.write(sql);
                    writer.newLine();
                }
                writer.newLine();
            }
        } catch (IOException ex) {
            logger.error("Fail to write SQLs to file: " + sqlFile.getAbsolutePath(), ex);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception ex) {
            }
        }
    }

    @Autowired
    public void setNotificationItemService(NotificationItemService notificationItemService) {
        this.notificationItemService = notificationItemService;
    }
}