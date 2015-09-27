/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.aggregation.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.idef.xo.DenormalizedSurveyAnswer;
import com.ocs.indaba.idef.xo.IndicatorChoices;
import com.ocs.indaba.idef.xo.QuestionAspect;
import com.ocs.indaba.po.ReferenceChoice;
import com.ocs.indaba.po.SurveyIndicator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;


/**
 *
 * @author yc06x
 */
public class IdefDAO extends SmartDaoMySqlImpl<SurveyIndicator, Integer> {

    private static final Logger logger = Logger.getLogger(IdefDAO.class);

    static private final String GET_ANSWERS_BY_PRODUCT =
            "SELECT DISTINCT proj.id projId, proj.code_name projName, prod.id prodId, prod.name prodName, sc.is_tsc, " +
            "qst.id qstId, qst.public_name qstLabel, ind.id indId, ind.question indQst, qst.weight qstWeight, " +
            "ind.answer_type indDataType, cat.id catId, cat.title catTitle, cat.weight catWeight, " +
            "target.id targetId, target.name targetName, target.description targetDesc, " +
            "answer.id answerId, answer.answer_user_id answerUserId, u.first_name answerUserFirstName, u.last_name answerUserLastName, " +
            "answer.answer_time answerTime, " +
            "aoc.choices answerChoices, aof.value answerFloat, aot.value answerText, aoi.value answerInt, answer.comments answerComments, " +
            "ref.reference_id refId, ref.choices refChoices, ref.source_description refSourceDesc, " +
            "spr.reviewer_user_id sprUserId, spruser.first_name sprUserFirstName, spruser.last_name sprUserLastName, " +
            "spr.opinion sprOpinion, spr.comments sprComments, " +
            "spraoc.choices sprChoices, spraof.value sprFloat, spraoi.value sprInt, spraot.value sprText, role.name sprUserRoleName " +
            "FROM product prod " +
            "JOIN project proj ON proj.id=prod.project_id " +
            "JOIN survey_config sc ON sc.id=prod.product_config_id " +
            "JOIN survey_category cat ON cat.survey_config_id=sc.id " +
            "JOIN survey_question qst ON qst.survey_category_id=cat.id " +
            "JOIN survey_indicator ind ON ind.id=qst.survey_indicator_id " +
            "JOIN horse ON horse.product_id=prod.id " +
            "JOIN content_header ch ON ch.id=horse.content_header_id " +
            "JOIN target ON target.id=horse.target_id " +
            "JOIN survey_content_object sco ON sco.content_header_id=ch.id " +
            "JOIN survey_answer answer ON answer.survey_content_object_id=sco.id AND answer.survey_question_id=qst.id AND answer.answer_time IS NOT NULL " +
            "LEFT JOIN user u ON u.id=answer.answer_user_id " +
            "LEFT JOIN answer_object_choice aoc ON aoc.id=answer.answer_object_id AND (ind.answer_type=1 OR ind.answer_type=2) " +
            "LEFT JOIN answer_object_integer aoi ON aoi.id=answer.answer_object_id AND ind.answer_type=3 " +
            "LEFT JOIN answer_object_float aof ON aof.id=answer.answer_object_id AND ind.answer_type=4 " +
            "LEFT JOIN answer_object_text aot ON aot.id=answer.answer_object_id AND ind.answer_type=5 " +
            "LEFT JOIN reference_object ref ON ref.id=answer.reference_object_id " +
            "LEFT JOIN survey_peer_review spr ON spr.survey_answer_id=answer.id " +
            "LEFT JOIN user spruser ON spruser.id=spr.reviewer_user_id " +
            "LEFT JOIN answer_object_choice spraoc ON spraoc.id=spr.suggested_answer_object_id AND (ind.answer_type=1 OR ind.answer_type=2) " +
            "LEFT JOIN answer_object_integer spraoi ON spraoi.id=spr.suggested_answer_object_id AND ind.answer_type=3 " +
            "LEFT JOIN answer_object_float spraof ON spraof.id=spr.suggested_answer_object_id AND ind.answer_type=4 " +
            "LEFT JOIN answer_object_text spraot ON spraot.id=spr.suggested_answer_object_id AND ind.answer_type=5 " +
            "LEFT JOIN project_membership pm ON pm.user_id=spruser.id AND pm.project_id=proj.id " +
            "LEFT JOIN role ON role.id=pm.role_id " +
            "WHERE prod.id=? AND prod.content_type=0 AND target.id=? " +
            "ORDER BY qstId";


    public List<DenormalizedSurveyAnswer> getAnswersByProduct(int productId, int targetId) {
        try {
            List<DenormalizedSurveyAnswer> result = getJdbcTemplate().query(GET_ANSWERS_BY_PRODUCT,
                new Object[]{productId, targetId}, new DenormalizedSurveyAnswerRowMapper());

            return result;
        } catch (Throwable ex) {
            logger.error("Exception: " +ex);
            return null;
        }
    }


    static private Integer getInteger(ResultSet rs, String column) {
        try {
            Object obj = rs.getObject(column);
            if (obj == null) {
                return null;
            } else {
                return rs.getInt(column);
            }
        } catch (Exception ex) {
            return null;
        }
    }


    static private Float getFloat(ResultSet rs, String column) {
        try {
            Object obj = rs.getObject(column);
            if (obj == null) {
                return null;
            } else {
                return rs.getFloat(column);
            }
        } catch (Exception ex) {
            return null;
        }
    }


    private static class DenormalizedSurveyAnswerRowMapper implements RowMapper {

        public DenormalizedSurveyAnswer mapRow(ResultSet rs, int i) throws SQLException {
            DenormalizedSurveyAnswer result = new DenormalizedSurveyAnswer();

            Object o = rs.getObject("projId");
            result.setProjectId(rs.getInt("projId"));
            result.setProjectName(rs.getString("projName"));
            result.setProductId(rs.getInt("prodId"));            
            result.setProductName(rs.getString("prodName"));
            result.setIsTsc(rs.getShort("is_tsc"));
            result.setQuestionId(rs.getInt("qstId"));
            result.setQuestionLabel(rs.getString("qstLabel"));
            result.setQuestionWeight(rs.getInt("qstWeight"));
            result.setIndicatorId(rs.getInt("indId"));
            result.setIndicatorDataType(rs.getInt("indDataType"));
            result.setIndicatorQuestion(rs.getString("indQst"));

            result.setCategoryId(getInteger(rs, "catId"));
            result.setCategoryTitle(rs.getString("catTitle"));
            result.setCategoryWeight(getInteger(rs, "catWeight"));

            result.setTargetId(rs.getInt("targetId"));
            result.setTargetName(rs.getString("targetName"));
            result.setTargetDescription(rs.getString("targetDesc"));

            result.setAnswerId(rs.getInt("answerId"));
            result.setAnswerUserId(getInteger(rs, "answerUserId"));
            result.setAnswerUserFirstName(rs.getString("answerUserFirstName"));
            result.setAnswerUserLastName(rs.getString("answerUserLastName"));
            result.setAnswerComments(rs.getString("answerComments"));
            result.setAnswerTime(rs.getDate("answerTime"));
            result.setAnswerValueChoices(getInteger(rs, "answerChoices"));
            result.setAnswerValueInt(getInteger(rs, "answerInt"));
            result.setAnswerValueFloat(getFloat(rs, "answerFloat"));
            result.setAnswerValueText(rs.getString("answerText"));

            result.setReferenceId(getInteger(rs, "refId"));
            result.setRefChoices(getInteger(rs, "refChoices"));
            result.setRefSourceDescription(rs.getString("refSourceDesc"));

            result.setReviewUserId(getInteger(rs, "sprUserId"));
            result.setReviewUserFirstName(rs.getString("sprUserFirstName"));
            result.setReviewUserLastName(rs.getString("sprUserLastName"));
            result.setReviewOpinion(getInteger(rs, "sprOpinion"));
            result.setReviewComments(rs.getString("sprComments"));
            result.setReviewAnswerValueChoices(getInteger(rs, "sprChoices"));
            result.setReviewAnswerValueInt(getInteger(rs, "sprInt"));
            result.setReviewAnswerValueFloat(getFloat(rs, "sprFloat"));
            result.setReviewAnswerValueText(rs.getString("sprText"));

            String sprUserRole = rs.getString("sprUserRoleName");
            if (sprUserRole == null) sprUserRole = "";
            result.setReviewUserRole(sprUserRole);
            
            return result;
        }
    }


    private static final String GET_INDICATOR_CHOICES =
            "SELECT DISTINCT ind.id, atcc.label, atcc.score, atcc.mask " +
            "FROM survey_indicator ind, atc_choice atcc, product prod, survey_config sc, answer_type_choice atc, survey_question qst " +
            "WHERE prod.product_config_id=sc.id AND qst.survey_config_id=sc.id " +
            "AND (ind.answer_type=1 OR ind.answer_type=2) AND ind.id=qst.survey_indicator_id " +
            "AND atc.id=ind.answer_type_id AND atcc.answer_type_choice_id=atc.id AND prod.id=? " +
            "ORDER BY ind.id";


    public List<IndicatorChoices> getIndicatorChoicesByProduct(int productId) {
        List<IndicatorChoices> result = getJdbcTemplate().query(GET_INDICATOR_CHOICES,
                new Object[]{productId}, new IndicatorChoicesRowMapper());

        return result;
    }

    private static class IndicatorChoicesRowMapper implements RowMapper {

        public IndicatorChoices mapRow(ResultSet rs, int i) throws SQLException {

            IndicatorChoices result = new IndicatorChoices();

            result.setIndicatorId(rs.getInt("id"));
            result.setLabel(rs.getString("label"));
            result.setMask(getInteger(rs, "mask"));

            Integer scoreInt = getInteger(rs, "score");
            Float score = null;
            if (scoreInt != null) {
                score = (float)(scoreInt / 10000);
            }
            result.setScore(score);

            return result;
        }
    }


    private static final String GET_REF_CHOICES_BY_PRODUCT =
            "SELECT DISTINCT rc.* " +
            "FROM survey_indicator ind, product prod, survey_config sc,  survey_question qst, reference ref, reference_choice rc " +
            "WHERE prod.product_config_id=sc.id AND qst.survey_config_id=sc.id AND ind.id=qst.survey_indicator_id " +
            "AND ref.id=ind.reference_id AND rc.reference_id=ref.id " +
            "AND prod.id=? " +
            "ORDER BY rc.reference_id";

    public List<ReferenceChoice> getReferenceChoicesByProduct(int productId) {
        List<ReferenceChoice> result = getJdbcTemplate().query(GET_REF_CHOICES_BY_PRODUCT,
                new Object[]{productId}, new ReferenceChoiceRowMapper());

        return result;
    }


    private static class ReferenceChoiceRowMapper implements RowMapper {

        public ReferenceChoice mapRow(ResultSet rs, int i) throws SQLException {

            ReferenceChoice result = new ReferenceChoice();

            result.setId(rs.getInt("id"));
            result.setLabel(rs.getString("label"));
            result.setMask(getInteger(rs, "mask"));
            result.setReferenceId(rs.getInt("reference_id"));
            result.setSname(rs.getString("sname"));

            return result;
        }
    }


    private static final String SELECT_QUESTION_ASPECTS =
        "SELECT DISTINCT sq.id qstId, sq.name qstName, sq.public_name qstPubName, si.question qstText, " +
        "atcc.label, atcc.criteria, atcc.weight, atcc.score, si.answer_type, " +
        "ati.criteria iCriteria, atf.criteria fCriteria, att.criteria tCriteria " +
        "FROM product prod " +
        "JOIN survey_question sq ON prod.product_config_id = sq.survey_config_id " +
        "JOIN survey_indicator si ON (sq.survey_indicator_id = si.id) " +
        "LEFT JOIN atc_choice atcc ON ((si.answer_type=1 OR si.answer_type=2) AND atcc.answer_type_choice_id = si.answer_type_id) " +
        "LEFT JOIN answer_type_integer ati ON (si.answer_type =3 AND ati.id = si.answer_type_id) " +
        "LEFT JOIN answer_type_integer atf ON (si.answer_type =4 AND atf.id = si.answer_type_id) " +
        "LEFT JOIN answer_type_integer att ON (si.answer_type =5 AND att.id = si.answer_type_id) " +
        "WHERE prod.id=? " +
        "ORDER BY sq.id, weight";

    public List<QuestionAspect> getQuestionAspectsByProduct(int productId) {
        try {
            List<QuestionAspect> result = getJdbcTemplate().query(SELECT_QUESTION_ASPECTS,
                new Object[]{productId}, new QuestionAspectRowMapper());
            return result;
        } catch (Throwable ex) {
            logger.error("Exception: " +ex);
            return null;
        }
    }

    private static class QuestionAspectRowMapper implements RowMapper {

        public QuestionAspect mapRow(ResultSet rs, int i) throws SQLException {

            QuestionAspect result = new QuestionAspect();

            result.setQuestionId(rs.getInt("qstId"));
            result.setQuestionName(rs.getString("qstName"));
            result.setQuestionPublicName(rs.getString("qstPubName"));
            result.setQuestionText(rs.getString("qstText"));
            result.setChoiceLabel(rs.getString("label"));

            int answerType = rs.getInt("answer_type");
            String criteria = null;
            switch (answerType) {
                case 1:
                case 2:
                    // choice type
                    criteria = rs.getString("criteria");
                    break;

                case 3:  // int
                    criteria = rs.getString("iCriteria");
                    break;

                case 4:  // float
                    criteria = rs.getString("fCriteria");
                    break;

                case 5:  // text
                    criteria = rs.getString("tCriteria");
                    break;
            }
            result.setCriteria(criteria);
            result.setAnswerType(answerType);

            Integer scoreInt = getInteger(rs, "score");
            Double score = null;
            if (scoreInt != null) {
                score = (double)(scoreInt / 10000);
            }
            result.setScore(score);

            return result;
        }
    }

}
