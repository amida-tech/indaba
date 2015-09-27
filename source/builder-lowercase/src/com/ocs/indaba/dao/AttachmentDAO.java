/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Attachment;
//import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author lukeshi
 */
public class AttachmentDAO extends SmartDaoMySqlImpl<Attachment, Integer> {

    private static final Logger log = Logger.getLogger(AttachmentDAO.class);
    private static final String SELECT_ATTACHMENTS_BY_CONTENT_HEADER_ID = "SELECT * FROM attachment WHERE content_header_id=? ORDER BY update_time";
    private static final String SELECT_ATTACHMENT_BY_NAME = "SELECT * FROM attachment WHERE name=?";
    private static final String SELECT_ATTACHMENTS_BY_CONTENT_HORSE_ID = "SELECT a.* FROM content_header ch, attachment a WHERE ch.horse_id=? AND a.content_header_id=ch.id";
    private static final String SELECT_SURVEY_ANSWER_ATTACHMENTS_BY_ANSWER_ID =
            "SELECT attach.* FROM attachment attach, survey_answer_attachment saa "
            + "WHERE saa.survey_answer_id=? AND saa.attachment_id=attach.id";

    private static final String SELECT_SURVEY_ANSWER_ATTACHMENTS_VERSION_BY_ANSWER_ID =
            "SELECT saav.* FROM survey_answer_attachment_version saav, survey_answer_version sav, survey_answer sa, content_version cv " +
            "WHERE sa.id=? AND cv.id=? AND sa.survey_question_id = sav.survey_question_id " +
            "AND sav.content_version_id = cv.id AND saav.survey_answer_version_id=sav.id";

    private static final String SELECT_SURVEY_ANSWER_ATTACHMENTS_VERSION_BY_ATTACH_ID =
            "SELECT * FROM survey_answer_attachment_version WHERE id=?";
    private static final String SELECT_SURVEY_ANSWER_ATTACHMENT_BY_ATTACHID_AND_ANSWERID =
            "SELECT attach.* FROM attachment attach, survey_answer_attachment saa "
            + "WHERE attach.id=? AND saa.survey_answer_id=? AND saa.attachment_id=attach.id";
    //private static final String SELECT_ATTACHMENTS_BY_ID = "SELECT * FROM attachment WHERE attachment_id=?";

    public List<Attachment> selectAttachmentsByCntHdrId(int cntHdrId) {
        log.debug("Select attachments for a content header: " + cntHdrId + ". [" + SELECT_ATTACHMENTS_BY_CONTENT_HEADER_ID + "].");
        return super.find(SELECT_ATTACHMENTS_BY_CONTENT_HEADER_ID, cntHdrId);
    }

    public List<Attachment> selectAttachmentsByHorseId(int horseId) {
        log.debug("Select attachments by horse_id: " + horseId + ". [" + SELECT_ATTACHMENTS_BY_CONTENT_HORSE_ID + "].");
        return super.find(SELECT_ATTACHMENTS_BY_CONTENT_HORSE_ID, horseId);
    }

    public List<Attachment> selectSurveyAnswerAttachmentsByAnswerId(int answerId) {
        log.debug("Select survey answer attachments by answer id: " + answerId + ". [" + SELECT_SURVEY_ANSWER_ATTACHMENTS_BY_ANSWER_ID + "].");
        return super.find(SELECT_SURVEY_ANSWER_ATTACHMENTS_BY_ANSWER_ID, answerId);
    }

    public List<Attachment> selectSurveyAnswerAttachmentsVersionByAnswerId(int contentVersionId, int answerId) {
        log.debug("Select survey answer original attachments by contentVersionId " + contentVersionId + " and answer id: " + answerId + ". [" + SELECT_SURVEY_ANSWER_ATTACHMENTS_VERSION_BY_ANSWER_ID + "].");
        return super.find(SELECT_SURVEY_ANSWER_ATTACHMENTS_VERSION_BY_ANSWER_ID, (long)answerId, contentVersionId);
    }

    public Attachment selectSurveyAnswerAttachmentVersionByAttachId(int attachId) {
        log.debug("Select survey answer attachment version by attach id: " + attachId + "].");
        return super.findSingle(SELECT_SURVEY_ANSWER_ATTACHMENTS_VERSION_BY_ATTACH_ID, attachId);
    }

    public Attachment selectSurveyAnswerAttachmentByAttachIdAndAnswerId(int attachId, int answerId) {
        log.debug("Select survey answer attachment by attach id and answer id: " + answerId + ". [" + SELECT_SURVEY_ANSWER_ATTACHMENT_BY_ATTACHID_AND_ANSWERID + "].");
        return super.findSingle(SELECT_SURVEY_ANSWER_ATTACHMENT_BY_ATTACHID_AND_ANSWERID, attachId, answerId);
    }

    public Attachment selectAttachmentById(int attachmentId) {
        log.debug("Select attachment by id: " + attachmentId + ".");
        return super.get(attachmentId);
    }

    public Attachment selectAttachmentByName(String filename) {
        log.debug("Select attachment by name: " + filename + ": " + SELECT_ATTACHMENT_BY_NAME);
        return super.findSingle(SELECT_ATTACHMENT_BY_NAME, filename);
    }

    public void deleteAttachmentById(int attachmentId) {
        log.debug("Delete attachments by id: " + attachmentId + ".");
        super.delete(attachmentId);
    }

    public Attachment insertAttachment(Attachment attachment) {
        //Attachment attach = selectAttachmentByName(attachment.getName());
        //if (attach == null) {
            log.debug("Insert attachment.");
            Attachment attach = super.create(attachment);
        //} else {
            //log.debug("Attachment existed[filename=" + attachment.getName() + "]. Just refresh the update_time.");
            //attach.setUpdateTime(new Date());
            //updateAttachment(attach);
        //}
        return attach;
    }

    public Attachment updateAttachment(Attachment attachment) {
        log.debug("Save attachment.");
        return super.update(attachment);
    }

    private static final String SELECT_ATTACHMENTS_BY_PRODUCT_ID =
            "SELECT DISTINCT a.* FROM attachment a, content_header ch, horse h " +
            "WHERE a.content_header_id = ch.id AND h.id = ch.horse_id AND h.product_id=?";

    public List<Attachment> selectAttachmentsByProductId(int productId) {
        return super.find(SELECT_ATTACHMENTS_BY_PRODUCT_ID, productId);
    }
}
