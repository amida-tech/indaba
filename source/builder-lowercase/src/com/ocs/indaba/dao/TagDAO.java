/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Tag;
import com.ocs.indaba.vo.TagContent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;

import static java.sql.Types.*;

/**
 *
 * @author Jeanbone
 */
public class TagDAO extends SmartDaoMySqlImpl<Tag, Integer> {


    private final String SELECT_TAG_CONTENT =
            "SELECT tag.id tag_id, tag.tagged_object_id survey_answer_id, tag.label tag_label, " +
                "survey_question.public_name q_public_name, survey_indicator.question, survey_question.id q_id " +
            "FROM indaba.tag, indaba.survey_answer, indaba.survey_question, indaba.survey_indicator, " +
                "indaba.content_header, indaba.horse " +
            "WHERE tag.tagged_object_id = survey_answer.id AND " +
                "survey_answer.survey_question_id = survey_question.id AND " +
                "survey_question.survey_indicator_id = survey_indicator.id AND " +
                "content_header.content_object_id = tag.tagged_object_scope_id AND " +
                "horse.content_header_id = content_header.id AND " +
                "horse.id = ? AND tag.tag_type = ? AND tag.tagged_object_type = ? AND tag.label = ? " +
            "ORDER BY tag_label";
    private final String SELECT_TAG_LABEL =
            "SELECT t.label, COUNT(t.tagged_object_id) 'count' FROM horse h " +
            "JOIN content_header ch ON (h.content_header_id = ch.id) " +
            "JOIN tag t ON (ch.content_object_id = t.tagged_object_scope_id) " +
            "WHERE h.id = ? AND t.tag_type = ? AND t.tagged_object_type = ? " +
            "GROUP BY t.label " +
            "ORDER BY t.label";
    private final String SELECT_TAG_LABEL_LIKE =
            "SELECT t.label, COUNT(t.tagged_object_id) 'count' FROM horse h " +
            "JOIN content_header ch ON (h.content_header_id = ch.id) " +
            "JOIN tag t ON (ch.content_object_id = t.tagged_object_scope_id) " +
            "WHERE h.id = ? AND t.tag_type = ? AND t.tagged_object_type = ? AND t.label LIKE ? " +
            "GROUP BY t.label " +
            "ORDER BY t.label";

    private final String SELECT_TAG_LABELS_BY_ANSWERID =
            "SELECT DISTINCT t.label, MAX(t.tagged_object_id = ?) exist FROM tag t " +
            "WHERE t.tag_type = ? AND t.tagged_object_type = ? AND t.tagged_object_scope_id = ? " +
            "GROUP BY t.label "+
            "ORDER BY t.label";

    private final String SQL_DELETE_TAG =
            "DELETE FROM tag " +
            "WHERE tag_type = ? AND tagged_object_type = ? AND tagged_object_id = ? " +
            "AND tagged_object_scope_id = ? AND label = ?";

    public Tag insertTag(Tag tag) {
        Tag queryTag = super.findSingle(
                "SELECT * FROM indaba.tag WHERE tagged_object_id = ? AND user_id = ? AND label = ?",
                tag.getTaggedObjectId(), tag.getUserId(), tag.getLabel());

        if (queryTag != null) {
            tag.setId(queryTag.getId());
            return super.update(tag);
        } else {
            return super.create(tag);
        }
    }
    public List<String[]> selectTagLabel(int horseId, String startWith) {

        RowMapper mapper = new RowMapper() {
            public String[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                String[] tagCount = new String[2];
                tagCount[0] = new String(rs.getString("label"));
                tagCount[1] = new String("" + rs.getInt("count"));
                return tagCount;
            }
         };
        
        if (startWith.equals("")) {
            return getJdbcTemplate().query(SELECT_TAG_LABEL,
                    new Object[]{horseId, Constants.USER_TAG, Constants.TAGGED_OBJECT_TYPE_SURVEY_ANSWER},
                    new int[]{INTEGER, INTEGER, INTEGER},
                    mapper);
        } else {
            return getJdbcTemplate().query(SELECT_TAG_LABEL_LIKE,
                    new Object[]{horseId, Constants.USER_TAG, Constants.TAGGED_OBJECT_TYPE_SURVEY_ANSWER, startWith.concat("%")},
                    new int[]{INTEGER, INTEGER, INTEGER, VARCHAR},
                    mapper);
        }
    }
    

    public List<TagContent> selectTagContent(int horseId, String label) {

        RowMapper mapper = new RowMapper() {
            public TagContent mapRow(ResultSet rs, int rowNum) throws SQLException {
                TagContent tagContent = new TagContent();
                tagContent.setTag_id(rs.getInt("tag_id"));
                tagContent.setSurvey_answer_id(rs.getInt("survey_answer_id"));
                tagContent.setTag_label(rs.getString("tag_label"));
                tagContent.setQ_public_name(rs.getString("q_public_name"));
                tagContent.setSurvey_question_id(rs.getInt("q_id"));
                tagContent.setQuestion(rs.getString("question"));
                return tagContent;
            }
        };

        return getJdbcTemplate().query(SELECT_TAG_CONTENT,
                new Object[]{horseId, Constants.USER_TAG, Constants.TAGGED_OBJECT_TYPE_SURVEY_ANSWER, label},
                new int[]{INTEGER, INTEGER, INTEGER, VARCHAR},
                mapper);
    }

    public List<String[]> selectTagLabelsByAnswerId(int answerId, int contentObjectId) {
        RowMapper mapper = new RowMapper() {
            public String[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                String[] tagCount = new String[2];
                tagCount[0] = new String(rs.getString("label"));
                tagCount[1] = new String("" + rs.getInt("exist"));
                return tagCount;
            }
         };
        return getJdbcTemplate().query(SELECT_TAG_LABELS_BY_ANSWERID,
                    new Object[]{answerId, Constants.USER_TAG, 
                    Constants.TAGGED_OBJECT_TYPE_SURVEY_ANSWER, contentObjectId},
                    new int[]{INTEGER, INTEGER, INTEGER, INTEGER},
                    mapper);
    }

    public int deleteTag(Tag tag){
        logger.debug("delete tag :"+ tag.getLabel());

        try{
            return getJdbcTemplate().update(SQL_DELETE_TAG,
                    new Object[] {Constants.USER_TAG, Constants.TAGGED_OBJECT_TYPE_SURVEY_ANSWER,
                    tag.getTaggedObjectId(), tag.getTaggedObjectScopeId(), tag.getLabel()});
        }
        catch(Exception e){
            return 0;
        }
    }
}
