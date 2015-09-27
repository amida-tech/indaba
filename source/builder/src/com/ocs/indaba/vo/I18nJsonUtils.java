/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.NotificationItem;
import com.ocs.indaba.po.SourceFile;
import com.ocs.indaba.po.TextItem;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Jeff
 */
public class I18nJsonUtils {

    private static final String JSON_ATTR_PAGE = "page";
    private static final String JSON_ATTR_TOTAL = "total";
    private static final String JSON_ATTR_RECORDS = "records";
    private static final String JSON_ATTR_ROWS = "rows";
    //
    // Source File Attribute
    private static final String JSON_ATTR_ID = "id";
    private static final String JSON_ATTR_FILE = "file";
    private static final String JSON_ATTR_EXTENSION = "ext";
    private static final String JSON_ATTR_COUNT = "count";
    private static final String JSON_ATTR_STATUS = "status";
    //
    // Text Resource Attribute
    private static final String JSON_ATTR_SOURCE_FILE_ID = "srcFileId";
    private static final String JSON_ATTR_TEXT_RESOURCE_ID = "rsrcId";
    private static final String JSON_ATTR_EN_LANG_ID = "enLangId";
    private static final String JSON_ATTR_FR_LANG_ID = "frLangId";
    private static final String JSON_ATTR_TEXT_EN_ITEM_ID = "enItemId";
    private static final String JSON_ATTR_TEXT_FR_ITEM_ID = "frItemId";
    private static final String JSON_ATTR_RESOURCE_NAME = "rsrcName";
    private static final String JSON_ATTR_EN_TEXT = "enText";
    private static final String JSON_ATTR_FR_TEXT = "frText";
    private static final String JSON_ATTR_DESCRIPTION = "desc";
    //
    // Notification Item Attribute
    private static final String JSON_ATTR_NOTIFICATION_TYPE_ID = "typeId";
    private static final String JSON_ATTR_NOTIFICATION_RESOURCE_SUBJECT = "subject";
    private static final String JSON_ATTR_NOTIFICATION_TEXT_EN_ITEM_ID = "enItemId";
    private static final String JSON_ATTR_NOTIFICATION_TEXT_FR_ITEM_ID = "frItemId";
    private static final String JSON_ATTR_NOTIFICATION_EN_LANG_ID = "enLangId";
    private static final String JSON_ATTR_NOTIFICATION_FR_LANG_ID = "frLangId";
    private static final String JSON_ATTR_NOTIFICATION_EN_TEXT = "enText";
    private static final String JSON_ATTR_NOTIFICATION_FR_TEXT = "frText";

    public static JSONObject srcFileToJsonObject(SourceFile srcFilePo, int txtRsrcCount) {
        JSONObject entry = new JSONObject();
        entry.put(JSON_ATTR_ID, srcFilePo.getId());
        entry.put(JSON_ATTR_FILE, srcFilePo.getFilename());
        entry.put(JSON_ATTR_EXTENSION, srcFilePo.getExtension());
        entry.put(JSON_ATTR_COUNT, txtRsrcCount);
        entry.put(JSON_ATTR_STATUS, srcFilePo.getStatus());
        return entry;
    }

    public static JSONObject txtRsrcItemToJsonObject(TextResourceItemVO txtRsrcItem) {
        JSONObject entry = new JSONObject();
        entry.put(JSON_ATTR_SOURCE_FILE_ID, txtRsrcItem.getSourceFileId());
        entry.put(JSON_ATTR_TEXT_RESOURCE_ID, txtRsrcItem.getTextResourceId());
        entry.put(JSON_ATTR_DESCRIPTION, txtRsrcItem.getDescription());
        entry.put(JSON_ATTR_RESOURCE_NAME, txtRsrcItem.getResourceName());
        Map<Integer, TextItem> items = txtRsrcItem.getTextItems();
        /*
         * Set<Integer> keys = items.keySet(); if(keys != null &&
         * !keys.isEmpty()) { for(int langId: keys) { } }
         */
        TextItem textItem = items.get(Constants.LANG_EN);
        if (textItem != null) {
            entry.put(JSON_ATTR_TEXT_EN_ITEM_ID, textItem.getId());
            entry.put(JSON_ATTR_EN_LANG_ID, textItem.getLanguageId());
            entry.put(JSON_ATTR_EN_TEXT, textItem.getText());
        } else {
            entry.put(JSON_ATTR_TEXT_EN_ITEM_ID, -1);
            entry.put(JSON_ATTR_EN_LANG_ID, Constants.LANG_EN);
            entry.put(JSON_ATTR_EN_TEXT, "");
        }
        textItem = items.get(Constants.LANG_FR);
        if (textItem != null) {
            entry.put(JSON_ATTR_TEXT_FR_ITEM_ID, textItem.getId());
            entry.put(JSON_ATTR_FR_LANG_ID, textItem.getLanguageId());
            entry.put(JSON_ATTR_FR_TEXT, textItem.getText());
        } else {
            entry.put(JSON_ATTR_TEXT_FR_ITEM_ID, -1);
            entry.put(JSON_ATTR_FR_LANG_ID, Constants.LANG_FR);
            entry.put(JSON_ATTR_FR_TEXT, "");
        }
        return entry;
    }

    public static JSONObject notificationItemMapToJsonObject(Map<Integer, NotificationItem> map) {
        JSONObject entry = new JSONObject();
        NotificationItem notificationItemEn = map.get(Constants.LANG_EN);
        if (notificationItemEn != null) {
            entry.put(JSON_ATTR_NOTIFICATION_TYPE_ID, notificationItemEn.getNotificationTypeId());
            entry.put(JSON_ATTR_NOTIFICATION_RESOURCE_SUBJECT, StringEscapeUtils.escapeHtml(notificationItemEn.getSubjectText()));
            entry.put(JSON_ATTR_NOTIFICATION_TEXT_EN_ITEM_ID, notificationItemEn.getId());
            entry.put(JSON_ATTR_NOTIFICATION_EN_LANG_ID, Constants.LANG_EN);
            entry.put(JSON_ATTR_NOTIFICATION_EN_TEXT, StringEscapeUtils.escapeHtml(notificationItemEn.getBodyText()));
        } else {
            entry.put(JSON_ATTR_NOTIFICATION_TEXT_EN_ITEM_ID, -1);
            entry.put(JSON_ATTR_NOTIFICATION_EN_LANG_ID, Constants.LANG_EN);
            entry.put(JSON_ATTR_NOTIFICATION_EN_TEXT, "");
        }
        NotificationItem notificationItemFr = map.get(Constants.LANG_FR);
        if (notificationItemFr != null) {
            if (notificationItemEn == null) {
                entry.put(JSON_ATTR_NOTIFICATION_TYPE_ID, notificationItemFr.getNotificationTypeId());
                entry.put(JSON_ATTR_NOTIFICATION_RESOURCE_SUBJECT, StringEscapeUtils.escapeHtml(notificationItemFr.getSubjectText()));
            }
            entry.put(JSON_ATTR_NOTIFICATION_TEXT_FR_ITEM_ID, notificationItemFr.getId());
            entry.put(JSON_ATTR_NOTIFICATION_FR_LANG_ID, Constants.LANG_FR);
            entry.put(JSON_ATTR_NOTIFICATION_FR_TEXT, StringEscapeUtils.escapeHtml(notificationItemFr.getBodyText()));
        } else {
            entry.put(JSON_ATTR_NOTIFICATION_TEXT_FR_ITEM_ID, -1);
            entry.put(JSON_ATTR_NOTIFICATION_FR_LANG_ID, Constants.LANG_FR);
            entry.put(JSON_ATTR_NOTIFICATION_FR_TEXT, "");
        }
        return entry;
    }

    public static String txtRsrcItemToJQGridJson(List<TextResourceItemVO> txtResourceItems, int page, int rows) {
        int size = txtResourceItems.size();
        if (page < 1) {
            page = 1;
        }
        int fromIndex = (page > 0) ? (page - 1) * rows : 0;
        int toIndex = ((fromIndex + rows) >= size) ? size : (fromIndex + rows);
        int count = (size % rows == 0) ? (size / rows) : (size / rows + 1);
        JSONArray jsonArr = new JSONArray();
        for (TextResourceItemVO element : txtResourceItems.subList(fromIndex, toIndex)) {
            jsonArr.add(txtRsrcItemToJsonObject(element));
        }
        return wrapJsonObjectToJQGridJson(page, rows, count, jsonArr.size(), jsonArr);
    }

    public static String srcFileToJQGridJson(List<SourceFile> srcFiles, Map<Integer, Integer> countMap,
            int page, int rows, String sortIndex, String sortOrder) {
        int size = srcFiles.size();
        if (page < 1) {
            page = 1;
        }
        int fromIndex = (page > 0) ? (page - 1) * rows : 0;
        int toIndex = ((fromIndex + rows) >= size) ? size : (fromIndex + rows);
        int count = (size % rows == 0) ? (size / rows) : (size / rows + 1);
        JSONArray jsonArr = new JSONArray();
        for (SourceFile element : srcFiles.subList(fromIndex, toIndex)) {
            jsonArr.add(srcFileToJsonObject(element, (countMap.get(element.getId()) == null) ? 0 : countMap.get(element.getId())));
        }
        return wrapJsonObjectToJQGridJson(page, rows, count, jsonArr.size(), jsonArr);
    }

    public static String notificationItemsToJQGridJson(List<Map<Integer, NotificationItem>> notificationItemList, int page, int rows) {
        int size = notificationItemList.size();
        if (page < 1) {
            page = 1;
        }
        int fromIndex = (page > 0) ? (page - 1) * rows : 0;
        int toIndex = ((fromIndex + rows) >= size) ? size : (fromIndex + rows);
        int count = (size % rows == 0) ? (size / rows) : (size / rows + 1);
        JSONArray jsonArr = new JSONArray();
        for (Map<Integer, NotificationItem> map : notificationItemList.subList(fromIndex, toIndex)) {
            jsonArr.add(notificationItemMapToJsonObject(map));
        }
        return wrapJsonObjectToJQGridJson(page, rows, count, jsonArr.size(), jsonArr);
    }

    private static String wrapJsonObjectToJQGridJson(int page, int rows, int count, int records, JSONArray jsonArr) {

        JSONObject root = new JSONObject();
        root.put(JSON_ATTR_PAGE, page);
        root.put(JSON_ATTR_TOTAL, count);
        root.put(JSON_ATTR_RECORDS, records);

        root.put(JSON_ATTR_ROWS, jsonArr);
        return root.toJSONString();
    }
}
