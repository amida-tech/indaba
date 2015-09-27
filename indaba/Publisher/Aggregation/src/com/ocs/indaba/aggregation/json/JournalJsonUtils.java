/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.json;

import com.ocs.common.Config;
import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.vo.JournalContentObjectInfo;
import com.ocs.indaba.aggregation.vo.JournalReview;
import com.ocs.indaba.aggregation.vo.JournalSummaryInfo;
import java.text.SimpleDateFormat;
import com.ocs.indaba.po.Attachment;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author jiangjeff
 */
public class JournalJsonUtils extends JsonUtils {

    private final static String KEY_BODY = "body";
    private final static String KEY_ATTACHMENTS = "attachments";
    private final static String KEY_REVIEW_AUTHOR = "author";
    private final static String KEY_REVIEW_COMMENT = "comment";
    private final static String KEY_REVIEW_TIME = "updatedTime";

    public static JSONObject journal2Json(JournalContentObjectInfo journal) {

        JSONObject root = new JSONObject();
        if (journal == null) {
            return root;
        }
        // base info
        root.put(KEY_HORSE_ID, journal.getHorseId());
        root.put(KEY_PRODUCT, formatStrVal(journal.getProductName()));
        root.put(KEY_TARGET, formatStrVal(journal.getTargetName()));
        root.put(KEY_TITLE, formatStrVal(journal.getTargetName() + ' ' + journal.getProductName()));
        root.put(KEY_BODY, formatStrVal(journal.getBody()));
        JSONArray attachJsonArr = new JSONArray();
        List<Attachment> attachments = journal.getAttachments();
        if (attachments != null && !attachments.isEmpty()) {
            String attachUrlPattern = Config.getString(Constants.KEY_JOURNAL_ATTACHMENT_URL);
            for (Attachment attach : attachments) {
                JSONObject o = new JSONObject();
                o.put(KEY_ATTACH_NAME, attach.getName());
                o.put(KEY_ATTACH_SIZE, attach.getSize());
                o.put(KEY_ATTACH_URL, MessageFormat.format(attachUrlPattern, attach.getId()));
                attachJsonArr.add(o);
            }
        }
        root.put(KEY_ATTACHMENTS, attachJsonArr);
        // horses
        List<JournalReview> reviews = journal.getJournalReviews();
        JSONArray jsonArr = new JSONArray();
        if (reviews != null && !reviews.isEmpty()) {
            for (JournalReview rev : reviews) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put(KEY_REVIEW_AUTHOR, formatStrVal("Reviewer " + rev.getUserId()));
                Date lastUpdated = (rev.getLastUpdated() != null) ? rev.getLastUpdated() : rev.getSubmitTime();
                jsonObj.put(KEY_REVIEW_TIME, formatDate(lastUpdated));
                jsonObj.put(KEY_REVIEW_COMMENT, formatStrVal(rev.getComment()));
                jsonArr.add(jsonObj);
            }
        }
        root.put(KEY_REVIEWS, jsonArr);
        return root;
    }

    public static JSONObject journalSummary2Json(JournalSummaryInfo journal) {

        JSONObject root = new JSONObject();
        if (journal == null) {
            return root;
        }
        // base info
        root.put(KEY_PROJECTT_NAME, formatStrVal(journal.getProjectName()));
        root.put(KEY_ORGANIZATION_NAME, formatStrVal(journal.getOrganizationName()));
        root.put(KEY_PRODUCT_NAME, formatStrVal(journal.getProductName()));
        root.put(KEY_STUDY_PERIOD, formatStrVal(journal.getStudyPeriod()));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        root.put(KEY_EXPORT_DATE, formatStrVal(df.format(journal.getExportDate())));

        // target list
        List<String> targetList = journal.getTargetName();
        if (targetList != null && !targetList.isEmpty()) {
            JSONArray jsonArr = new JSONArray();
            for (String target : targetList) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put(KEY_TARGET_NAME, formatStrVal(target));
                jsonArr.add(jsonObj);
            }
            root.put(KEY_TARGET_NAME_LIST, jsonArr);
        }
        return root;
    }
}
