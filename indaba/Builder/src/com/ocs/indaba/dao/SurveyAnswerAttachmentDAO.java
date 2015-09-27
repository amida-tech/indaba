/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import org.apache.log4j.Logger;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Attachment;
import com.ocs.indaba.po.SurveyAnswerAttachment;
import com.ocs.indaba.vo.SurveyAnswerAttachmentVO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import static java.sql.Types.INTEGER;


/**
 *
 * @author Jeff
 */
public class SurveyAnswerAttachmentDAO extends SmartDaoMySqlImpl<SurveyAnswerAttachment, Integer> {

    private static final Logger log = Logger.getLogger(SurveyAnswerAttachmentDAO.class);
    private static final String DELETE_SURVEY_ANSWER_ATTACHMENT = "DELETE FROM survey_answer_attachment WHERE attachment_id=? AND survey_answer_id=?";

    public void deleteSurveyAnswerAttachment(int attachId, int answerId) {
        log.debug("Delete survey_answer_attachment: attachid=" + attachId + ", answerId=" + answerId);
        super.update(DELETE_SURVEY_ANSWER_ATTACHMENT, attachId, answerId);
    }


    private static final String SELECT_SURVEY_ANSWER_ATTACHMENTS_BY_PRODUCT_ID =
        "SELECT DISTINCT saa.* FROM survey_answer_attachment saa, survey_answer sa, survey_content_object sco, content_header ch, horse h " +
        "WHERE sco.content_header_id = ch.id AND sa.survey_content_object_id = sco.id AND h.id = ch.horse_id " +
        "AND h.product_id=? AND saa.survey_answer_id = sa.id " +
        "ORDER BY saa.survey_answer_id ASC";

    public List<SurveyAnswerAttachment> selectSurveyAnswerAttachmentsByProductId(int productId) {
        return super.find(SELECT_SURVEY_ANSWER_ATTACHMENTS_BY_PRODUCT_ID, productId);
    }


    private static final String SELECT_SURVEY_ANSWER_ATTACHMENT_VOS_BY_PRODUCT_ID =
        "SELECT DISTINCT saa.survey_answer_id, a.* " +
        "FROM attachment a, survey_answer_attachment saa, survey_answer sa, survey_content_object sco, content_header ch, horse h " +
        "WHERE a.id=saa.attachment_id AND sco.content_header_id = ch.id AND sa.survey_content_object_id = sco.id AND h.id = ch.horse_id " +
        "AND h.product_id=? AND saa.survey_answer_id = sa.id";

    public List<SurveyAnswerAttachmentVO> selectSurveyAnswerAttachmentVOsByProductId(int productId) {
        RowMapper mapper = new RowMapper() {

            public SurveyAnswerAttachmentVO mapRow(ResultSet rs, int i) throws SQLException {
                SurveyAnswerAttachmentVO vo = new SurveyAnswerAttachmentVO();
                vo.setSurveyAnswerId(rs.getInt("survey_answer_id"));

                Attachment a = new Attachment();
                a.setContentHeaderId(rs.getInt("content_header_id"));
                a.setFilePath(rs.getString("file_path"));
                a.setId(rs.getInt("id"));
                a.setName(rs.getString("name"));
                a.setNote(rs.getString("note"));
                a.setSize(rs.getInt("size"));
                a.setType(rs.getString("type"));
                a.setUserId(rs.getInt("user_id"));
                
                vo.setAttachment(a);
                return vo;
            }
        };

        List<SurveyAnswerAttachmentVO> list =
                getJdbcTemplate().query(SELECT_SURVEY_ANSWER_ATTACHMENT_VOS_BY_PRODUCT_ID,
                new Object[]{productId},
                new int[]{INTEGER},
                mapper);

        return list;
    }
}
