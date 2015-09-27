/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.builder.dao;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.vo.AnswerVO;
import com.ocs.indaba.aggregation.vo.HorseVO;
import com.ocs.indaba.aggregation.vo.QuestionNode;
import com.ocs.indaba.aggregation.vo.IndicatorVO;
import com.ocs.indaba.aggregation.vo.ProductInfo;
import com.ocs.indaba.aggregation.vo.QuestionOption;
import com.ocs.indaba.aggregation.vo.ScorecardBaseNode;
import com.ocs.indaba.aggregation.vo.ScorecardInfo;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.util.ResultSetUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;
import static java.sql.Types.INTEGER;
import java.util.Date;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author jiangjeff
 */
public class ScorecardQuestionDAO extends SmartDaoMySqlImpl<SurveyIndicator, Integer> {

    private static final Logger logger = Logger.getLogger(ScorecardQuestionDAO.class);
    /**
     * private static final String SELECT_INDICATOR_BY_PROJECT_IDS = "SELECT
     * DISTINCT cfg.project_id, cfg.org_id, cfg.org_name, si.id indicator_id,
     * si.name inicator_name, si.question question_title, sq.id question_id,
     * sq.name question_name " + "FROM survey_indicator si, survey_question sq,
     * " + " (SELECT DISTINCT p.id project_id, org.id org_id, org.name org_name,
     * sco.survey_config_id survey_config_id " + " FROM survey_content_object
     * sco, content_header ch, organization org, project p " + " WHERE
     * ch.id=sco.survey_config_id AND p.id=ch.project_id AND
     * p.organization_id=org.id AND ch.project_id IN ({0})) cfg " + "WHERE
     * si.id=sq.survey_indicator_id AND
     * sq.survey_config_id=cfg.survey_config_id";
     *
     */
    private static final String SELECT_INDICATOR_BY_PROJECT_IDS =
            "SELECT DISTINCT prj.id project_id, prj.organization_id org_id, org.name org_name, si.id indicator_id, si.name inicator_name, si.question question_title, sq.id question_id, sq.name question_name "
            + "FROM project prj, organization org, survey_indicator si, survey_question sq, survey_config sc, product prd "
            + "WHERE prj.id IN ({0}) AND prj.organization_id=org.id AND prd.content_type=0 AND prd.project_id=prj.id "
            + "AND prd.product_config_id=sc.id AND sq.survey_config_id=sc.id AND si.id=sq.survey_indicator_id";
    private static final String SELECT_INDICATOR_BY_INDICATOR_IDS = "SELECT * FROM survey_indicator WHERE id IN ({0})";
    private static final String SELECT_INDICATOR_SCORES_BY_CATEGORY_ID = "SELECT si.id indicator_id, atc.score "
            + "FROM survey_question sq, survey_indicator si, survey_answer sa, answer_object_choice aoc, atc_choice atc "
            + "WHERE sq.survey_category_id=? AND sq.survey_indicator_id=si.id AND si.answer_type=1 AND "
            + "sa.survey_question_id=sq.id AND sa.answer_object_id=aoc.id AND (aoc.choices & atc.mask)=aoc.choices AND "
            + "si.answer_type_id=atc.answer_type_choice_id";
    /////////////////////////////////////////////////////////////////
    //
    // Select answer of SINGLE Choice type question
    private static final String SELECT_SINGLE_CHOICE_ANSWER_BY_INDICATOR_ID =
            "SELECT si.id indicator_id, atc.score, atc.id choice_id, atc.label label, atc.use_score use_score, sa.id answer_id, sa.comments, "
            + "sa.internal_msgboard_id internal_msgboard_id, sa.staff_author_msgboard_id staff_author_msgboard_id, si.answer_type answer_type,"
            + "sa.answer_user_id answer_user_id, sa.reference_object_id reference_object_id, ro.source_description, r.id reference_id, r.name reference_name "
            + "FROM survey_question sq, survey_indicator si, survey_answer sa, answer_object_choice aoc, atc_choice atc, survey_content_object sco, content_header ch, reference_object ro, reference r "
            + "WHERE si.id=? AND ch.horse_id=? AND ch.content_type=0 AND ch.id=sco.content_header_id AND sq.survey_indicator_id=si.id AND si.answer_type=1 AND sa.survey_question_id=sq.id AND "
            + "sa.survey_content_object_id=sco.id AND sa.answer_object_id=aoc.id AND (aoc.choices & atc.mask)=aoc.choices AND si.answer_type_id=atc.answer_type_choice_id AND "
            + "ro.id=sa.reference_object_id AND ro.reference_id=r.id";
    //
    // Select answer of MULTI Choice type question
    private static final String SELECT_MULTI_CHOICE_ANSWER_BY_INDICATOR_ID =
            "SELECT si.id indicator_id, sa.id answer_id, sa.comments comments, aoc.choices choices, sa.internal_msgboard_id internal_msgboard_id, si.answer_type answer_type, sa.answer_user_id answer_user_id, "
            + "sa.staff_author_msgboard_id staff_author_msgboard_id, sa.reference_object_id reference_object_id, ro.source_description source_description, r.id reference_id, r.name reference_name "
            + "FROM survey_question sq, survey_indicator si, survey_answer sa, answer_object_choice aoc, survey_content_object sco, content_header ch, reference_object ro, reference r "
            + "WHERE si.id=? AND ch.horse_id=? AND ch.content_type=0 AND ch.id=sco.content_header_id AND sq.survey_indicator_id=si.id AND si.answer_type=2 AND sa.survey_question_id=sq.id AND "
            + "sa.survey_content_object_id=sco.id AND sa.answer_object_id=aoc.id AND "
            + "ro.id=sa.reference_object_id AND ro.reference_id=r.id";
    //
    // Select answer of Integer type question
    private static final String SELECT_INTEGER_ANSWER_BY_INDICATOR_ID =
            "SELECT si.id indicator_id, aoi.value input_value, sa.id answer_id, sa.comments, sa.internal_msgboard_id internal_msgboard_id, "
            + "sa.staff_author_msgboard_id staff_author_msgboard_id, sa.answer_user_id answer_user_id, sa.reference_object_id reference_object_id, "
            + "ro.source_description, r.id reference_id, r.name reference_name "
            + "FROM survey_question sq, survey_indicator si, survey_answer sa, answer_object_integer aoi, "
            + "survey_content_object sco, content_header ch, reference_object ro, reference r "
            + "WHERE si.id=? AND ch.horse_id=? AND ch.content_type=0 AND ch.id=sco.content_header_id AND sq.survey_indicator_id=si.id AND "
            + "si.answer_type=3 AND sa.survey_question_id=sq.id AND sa.survey_content_object_id=sco.id AND sa.answer_object_id=aoi.id AND "
            + "ro.id=sa.reference_object_id AND ro.reference_id=r.id";
    //
    // Select answer of Float type question
    private static final String SELECT_FLOAT_ANSWER_BY_INDICATOR_ID =
            "SELECT si.id indicator_id, aof.value input_value, sa.id answer_id, sa.comments, sa.internal_msgboard_id internal_msgboard_id, "
            + "sa.staff_author_msgboard_id staff_author_msgboard_id, sa.answer_user_id answer_user_id, sa.reference_object_id reference_object_id, "
            + "ro.source_description, r.id reference_id, r.name reference_name "
            + "FROM survey_question sq, survey_indicator si, survey_answer sa, answer_object_float aof, "
            + "survey_content_object sco, content_header ch, reference_object ro, reference r "
            + "WHERE si.id=? AND ch.horse_id=? AND ch.content_type=0 AND ch.id=sco.content_header_id AND sq.survey_indicator_id=si.id AND "
            + "si.answer_type=4 AND sa.survey_question_id=sq.id AND sa.survey_content_object_id=sco.id AND sa.answer_object_id=aof.id AND "
            + "ro.id=sa.reference_object_id AND ro.reference_id=r.id"; //
    // Select answer of Text type question
    private static final String SELECT_TEXT_ANSWER_BY_INDICATOR_ID =
            "SELECT si.id indicator_id, aot.value input_value, sa.id answer_id, sa.comments, sa.internal_msgboard_id internal_msgboard_id, "
            + "sa.staff_author_msgboard_id staff_author_msgboard_id, sa.answer_user_id answer_user_id, sa.reference_object_id reference_object_id, "
            + "ro.source_description, r.id reference_id, r.name reference_name "
            + "FROM survey_question sq, survey_indicator si, survey_answer sa, answer_object_text aot, "
            + "survey_content_object sco, content_header ch, reference_object ro, reference r "
            + "WHERE si.id=? AND ch.horse_id=? AND ch.content_type=0 AND ch.id=sco.content_header_id AND sq.survey_indicator_id=si.id AND "
            + "si.answer_type=5 AND sa.survey_question_id=sq.id AND sa.survey_content_object_id=sco.id AND sa.answer_object_id=aot.id AND "
            + "ro.id=sa.reference_object_id AND ro.reference_id=r.id";
    /////////////////////////////////////////////////////////////////
    private static final String SELECT_ANSWERS_BY_HORSE_ID =
            "SELECT si.id indicator_id, atc.score, atc.id choice_id, atc.label label, atc.use_score use_score, sa.id answer_id, sa.comments, "
            + "sa.internal_msgboard_id internal_msgboard_id, sa.staff_author_msgboard_id staff_author_msgboard_id, "
            + "sa.answer_user_id answer_user_id, sa.reference_object_id reference_object_id, ro.source_description, r.id reference_id, r.name reference_name "
            + "FROM survey_question sq, survey_indicator si, survey_answer sa, answer_object_choice aoc, atc_choice atc, survey_content_object sco, content_header ch, reference_object ro, reference r "
            + "WHERE ch.horse_id=? AND ch.content_type=0 AND ch.id=sco.content_header_id AND sq.survey_indicator_id=si.id AND si.answer_type=1 AND sa.survey_question_id=sq.id AND "
            + "sa.survey_content_object_id=sco.id AND sa.answer_object_id=aoc.id AND (aoc.choices & atc.mask)=aoc.choices AND si.answer_type_id=atc.answer_type_choice_id AND "
            + "ro.id=sa.reference_object_id AND ro.reference_id=r.id";
    private static final String SELECT_SCORECARD_INFO_BY_HORSE_ID =
            "SELECT h.id horse_id, prj.id project_id, prj.organization_id organization_id, ch.id content_header_id, "
            + "ch.submit_time, prd.id product_id, prd.name product_name, prd.description product_desc, t.id target_id, t.name target_name, t.short_name target_short_name, "
            + "sc.id survey_config_id, sc.name survey_name, ch.title title, sp.id study_period_id, "
            + "sp.name study_period_name, wo.status workflow_object_status "
            + "FROM horse h, product prd, target t, content_header ch, survey_content_object sco, "
            + "survey_config sc, project prj, study_period sp, workflow_object wo "
            + "WHERE h.id=? AND h.product_id=prd.id AND h.target_id=t.id AND ch.horse_id=h.id AND "
            + "ch.content_object_id=sco.id AND sco.survey_config_id=sc.id AND prj.id=prd.project_id AND "
            + "sp.id=prj.study_period_id AND wo.id=h.workflow_object_id";
    private static final String SELECT_PRODUCT_INFO_BY_PRODUCT_ID =
            "SELECT DISTINCT prd.id product_id, prd.name product_name, prd.description product_desc, sc.id survey_config_id, sc.name survey_name, "
            + "sp.id study_period_id, sp.name study_period_name "
            + "FROM product prd, survey_config sc, project prj, study_period sp "
            + "WHERE prd.id=? AND prd.content_type=0 AND sc.id=prd.product_config_id "
            + "AND prj.id=prd.project_id AND sp.id=prj.study_period_id";
    /////////////////////////////////////////////////////////////////////
    //
    private static final String SELECT_NON_CHOICE_CRITERA_BY_INDICATOR_ID =
            "SELECT answertype.criteria criteria"
            + " FROM {0} answertype, survey_indicator si "
            + "WHERE si.id={1,number,#} AND si.answer_type={2} AND si.answer_type_id=answertype.id";           
    //    
    private static final String SELECT_INDICATORS_BY_CATEGORY_ID = "SELECT si.id indicator_id, si.tip tip, si.question question_text, si.name indicator_name, "
            + "si.answer_type answer_type, sq.id question_id, sq.name question_name, sq.public_name, r.classification "
            + "FROM survey_indicator si, survey_question sq, reference r "
            + "WHERE sq.survey_category_id=? AND si.id=sq.survey_indicator_id AND r.id=si.reference_id ORDER BY sq.weight"; // get all answer types

    private static final String SELECT_QUESTION_OPTIONS_BY_INDICATOR_ID = "SELECT atc.*, si.tip tip FROM atc_choice atc, survey_indicator si "
            + "WHERE si.id=? AND si.answer_type_id=atc.answer_type_choice_id";
    private static final String SELECT_QUESTION_COUNT_BY_HORSE_ID = "SELECT COUNT(sq.id) count FROM survey_question sq, content_header ch, survey_content_object sco "
            + "WHERE ch.horse_id=? AND ch.content_object_id=sco.id AND sco.survey_config_id=sq.survey_config_id";
    private static final String SELECT_PEER_REVIEWED_COUNT_BY_HORSE_ID = "SELECT COUNT(DISTINCT spr.survey_answer_id) peer_reviewed_count FROM survey_peer_review spr WHERE spr.survey_answer_id IN ( "
            + "SELECT sa.id answer_id FROM survey_answer sa, survey_question sq, content_header ch, survey_content_object sco "
            + "WHERE ch.horse_id=? AND ch.content_object_id=sco.id AND sco.survey_config_id=sq.survey_config_id AND sq.id=sa.survey_question_id AND sa.survey_content_object_id=sco.id)";
    private static final String SELECT_UNIQUE_PEER_REVIEWER_COUNT_BY_HORSE_ID = "SELECT COUNT(DISTINCT spr.reviewer_user_id) peer_reviewer_count FROM survey_peer_review spr WHERE spr.survey_answer_id IN ( "
            + "SELECT sa.id answer_id FROM survey_answer sa, survey_question sq, content_header ch, survey_content_object sco "
            + "WHERE ch.horse_id=? AND ch.content_object_id=sco.id AND sco.survey_config_id=sq.survey_config_id AND sq.id=sa.survey_question_id AND sa.survey_content_object_id=sco.id)";
    private static final String SELECT_ALL_HORSES_BY_PRODUCT_ID = "SELECT h.id horse_id, h.completion_time completion_time, t.id target_id, t.name target_name "
            + "FROM horse h, target t, workflow_object wfo WHERE h.product_id=? AND h.target_id=t.id AND h.workflow_object_id=wfo.id AND wfo.status!=5 ORDER BY t.name";

    public List<IndicatorVO> selectSurveyIndicatorsByProjectIds(List<Integer> projectIds) {
        logger.debug("Select indicators by project ids: " + projectIds);
        if (projectIds == null || projectIds.isEmpty()) {
            return null;
        }
        RowMapper mapper = new RowMapper() {

            public IndicatorVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                IndicatorVO indicator = new IndicatorVO();
                indicator.setProjectId(ResultSetUtil.getInt(rs, "project_id"));
                indicator.setOrgId(ResultSetUtil.getInt(rs, "org_id"));
                indicator.setOrgName(ResultSetUtil.getString(rs, "org_name"));
                indicator.setId(ResultSetUtil.getInt(rs, "indicator_id"));
                indicator.setName(ResultSetUtil.getString(rs, "inicator_name"));
                indicator.setQuestionTitle(ResultSetUtil.getString(rs, "question_title"));
                indicator.setQuestionId(ResultSetUtil.getInt(rs, "question_id"));
                indicator.setQuestionName(ResultSetUtil.getString(rs, "question_name"));
                return indicator;
            }
        };

        String ids = projectIds.toString();
        String sqlStr = MessageFormat.format(SELECT_INDICATOR_BY_PROJECT_IDS, ids.substring(1, ids.length() - 1));
        return getJdbcTemplate().query(
                sqlStr,
                new Object[]{},
                new int[]{},
                mapper);
    }

    public List<QuestionNode> selectIndicatorsByCategoryId(int categoryId) {
        logger.debug("Select indicators by category id: " + categoryId);
        RowMapper mapper = new RowMapper() {

            public QuestionNode mapRow(ResultSet rs, int rowNum) throws SQLException {
                QuestionNode qn = new QuestionNode();
                qn.setId(ResultSetUtil.getInt(rs, "indicator_id"));
                qn.setName(ResultSetUtil.getString(rs, "indicator_name"));
                qn.setQuestionId(ResultSetUtil.getInt(rs, "question_id"));
                qn.setQuestionName(ResultSetUtil.getString(rs, "question_name"));
                qn.setTip(ResultSetUtil.getString(rs, "tip"));
                qn.setPublicName(ResultSetUtil.getString(rs, "public_name"));
                qn.setQuestionType(ResultSetUtil.getInt(rs, "answer_type"));
                qn.setQuestionText(ResultSetUtil.getString(rs, "question_text"));
                qn.setRefClassification(ResultSetUtil.getInt(rs, "classification"));
                return qn;
            }
        };
        return getJdbcTemplate().query(
                SELECT_INDICATORS_BY_CATEGORY_ID,
                new Object[]{categoryId},
                new int[]{INTEGER},
                mapper);
    }

    public String selectNonChoiceQuestionCriteraByIndicatorId(int indicatorId, int answerType) {

        logger.debug("Select indicator critera: " + indicatorId);
        RowMapper mapper = new RowMapper() {

            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return ResultSetUtil.getString(rs, "criteria");
            }
        };
        String tablename = "";
        if (Constants.ANSWER_TYPE_INTEGER == answerType) {
            tablename = "answer_type_integer";
        } else if (Constants.ANSWER_TYPE_FLOAT == answerType) {
            tablename = "answer_type_float";
        } else if (Constants.ANSWER_TYPE_TEXT == answerType) {
            tablename = "answer_type_text";
        }
        List<String> list = getJdbcTemplate().query(
                MessageFormat.format(SELECT_NON_CHOICE_CRITERA_BY_INDICATOR_ID, tablename, indicatorId, answerType),
                null,
                null,
                mapper);
        return (list != null && !list.isEmpty()) ? list.get(0) : "";
    }

    public List<ScorecardBaseNode> selectIndicatorScoresByCategoryId(Integer categoryId) {
        logger.debug("Select indicator scores by category id: " + categoryId);
        RowMapper mapper = new RowMapper() {

            public ScorecardBaseNode mapRow(ResultSet rs, int rowNum) throws SQLException {
                QuestionNode indicator = new QuestionNode();
                indicator.setId(ResultSetUtil.getInt(rs, "indicator_id"));
                indicator.setScore(ResultSetUtil.getInt(rs, "score"));
                return indicator;
            }
        };
        return getJdbcTemplate().query(
                SELECT_INDICATOR_SCORES_BY_CATEGORY_ID,
                new Object[]{categoryId},
                new int[]{INTEGER},
                mapper);
    }

    public AnswerVO selectSingleChoiceAnswerByHorseId(int indicatorId, int horseId) {
        logger.debug("Select single-choice answer by horse id: " + horseId + " and indicator id: " + indicatorId);
        RowMapper mapper = new RowMapper() {

            public AnswerVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                AnswerVO answer = new AnswerVO();
                answer.setId(ResultSetUtil.getInt(rs, "answer_id"));
                answer.setIndicatorId(ResultSetUtil.getInt(rs, "indicator_id"));
                answer.setChoiceId(ResultSetUtil.getInt(rs, "choice_id"));
                answer.setScore(ResultSetUtil.getFloat(rs, "score") / Constants.SCORE_BASE_VALUE);
                answer.setUseScore((Constants.ANSWER_TYPE_SINGLE == ResultSetUtil.getInt(rs, "answer_type") && ResultSetUtil.getInt(rs, "use_score") == 1));
                answer.setLabel(ResultSetUtil.getString(rs, "label"));
                answer.setReferenceObjectId(ResultSetUtil.getInt(rs, "reference_object_id"));
                answer.setReferenceId(ResultSetUtil.getInt(rs, "reference_id"));
                answer.setReferenceName(ResultSetUtil.getString(rs, "reference_name"));
                answer.setReferences(ResultSetUtil.getString(rs, "source_description"));
                answer.setComments(ResultSetUtil.getString(rs, "comments"));
                answer.setAnswerUserId(ResultSetUtil.getInt(rs, "answer_user_id"));
                answer.setInternalMsgboardId(ResultSetUtil.getInt(rs, "internal_msgboard_id"));
                answer.setStaffAuthorMsgboardId(ResultSetUtil.getInt(rs, "staff_author_msgboard_id"));

                return answer;
            }
        };
        List<AnswerVO> list = getJdbcTemplate().query(
                SELECT_SINGLE_CHOICE_ANSWER_BY_INDICATOR_ID,
                new Object[]{indicatorId, horseId},
                new int[]{INTEGER, INTEGER},
                mapper);
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }

    public AnswerVO selectMultiChoiceAnswerByHorseId(int indicatorId, int horseId) {
        logger.debug("Select multi-choice answer by horse id: " + horseId + " and indicator id: " + indicatorId);
        RowMapper mapper = new RowMapper() {

            public AnswerVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                AnswerVO answer = new AnswerVO();
                answer.setId(ResultSetUtil.getInt(rs, "answer_id"));
                answer.setIndicatorId(ResultSetUtil.getInt(rs, "indicator_id"));
                //answer.setChoiceId(ResultSetUtil.getInt(rs, "choice_id"));
                //answer.setScore(ResultSetUtil.getFloat(rs, "score") / Constants.SCORE_BASE_VALUE);
                answer.setUseScore(false);
                answer.setChoices(ResultSetUtil.getInt(rs, "choices"));
                //answer.setLabel(ResultSetUtil.getString(rs, "label"));
                answer.setReferenceObjectId(ResultSetUtil.getInt(rs, "reference_object_id"));
                answer.setReferenceId(ResultSetUtil.getInt(rs, "reference_id"));
                answer.setReferenceName(ResultSetUtil.getString(rs, "reference_name"));
                answer.setReferences(ResultSetUtil.getString(rs, "source_description"));
                answer.setComments(ResultSetUtil.getString(rs, "comments"));
                answer.setAnswerUserId(ResultSetUtil.getInt(rs, "answer_user_id"));
                answer.setInternalMsgboardId(ResultSetUtil.getInt(rs, "internal_msgboard_id"));
                answer.setStaffAuthorMsgboardId(ResultSetUtil.getInt(rs, "staff_author_msgboard_id"));

                return answer;
            }
        };
        List<AnswerVO> list = getJdbcTemplate().query(
                SELECT_MULTI_CHOICE_ANSWER_BY_INDICATOR_ID,
                new Object[]{indicatorId, horseId},
                new int[]{INTEGER, INTEGER},
                mapper);
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }

    public AnswerVO selectInputAnswerByHorseId(int indicatorId, int horseId, int answerType) {
        logger.debug("Select integer answer by horse id: " + horseId + " and indicator id: " + indicatorId);
        String sqlStr = null;
        switch (answerType) {
            case Constants.ANSWER_TYPE_INTEGER:
                sqlStr = SELECT_INTEGER_ANSWER_BY_INDICATOR_ID;
                break;
            case Constants.ANSWER_TYPE_FLOAT:
                sqlStr = SELECT_FLOAT_ANSWER_BY_INDICATOR_ID;
                break;
            case Constants.ANSWER_TYPE_TEXT:
                sqlStr = SELECT_TEXT_ANSWER_BY_INDICATOR_ID;
            default:
                break;
        }
        List<AnswerVO> list = getJdbcTemplate().query(
                sqlStr,
                new Object[]{indicatorId, horseId},
                new int[]{INTEGER, INTEGER},
                new AnswerRowMapper());
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }

    public List<AnswerVO> selectAnswersByHorseId(int horseId) {
        logger.debug("Select answers by horse id: " + horseId);
        RowMapper mapper = new RowMapper() {

            public AnswerVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                AnswerVO answer = new AnswerVO();
                answer.setId(ResultSetUtil.getInt(rs, "answer_id"));
                answer.setIndicatorId(ResultSetUtil.getInt(rs, "indicator_id"));
                answer.setChoiceId(ResultSetUtil.getInt(rs, "choice_id"));
                answer.setScore(ResultSetUtil.getFloat(rs, "score") / Constants.SCORE_BASE_VALUE);
                answer.setUseScore((ResultSetUtil.getInt(rs, "use_score") == 1));
                answer.setLabel(ResultSetUtil.getString(rs, "label"));
                answer.setReferenceObjectId(ResultSetUtil.getInt(rs, "reference_object_id"));
                answer.setReferenceId(ResultSetUtil.getInt(rs, "reference_id"));
                answer.setReferenceName(ResultSetUtil.getString(rs, "reference_name"));
                answer.setReferences(ResultSetUtil.getString(rs, "source_description"));
                answer.setComments(ResultSetUtil.getString(rs, "comments"));
                answer.setAnswerUserId(ResultSetUtil.getInt(rs, "answer_user_id"));
                answer.setInternalMsgboardId(ResultSetUtil.getInt(rs, "internal_msgboard_id"));
                answer.setStaffAuthorMsgboardId(ResultSetUtil.getInt(rs, "staff_author_msgboard_id"));
                return answer;
            }
        };
        List<AnswerVO> list = getJdbcTemplate().query(
                SELECT_ANSWERS_BY_HORSE_ID,
                new Object[]{horseId},
                new int[]{INTEGER},
                mapper);
        return list;
    }

    /**
     *
     * @param horseId
     * @return
     */
    public ScorecardInfo selectScorecardInfoByHorseId(int horseId) {
        logger.debug("Select scorecard by horse id: " + horseId);
        RowMapper mapper = new RowMapper() {

            public ScorecardInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                ScorecardInfo scorecard = new ScorecardInfo();
                scorecard.setId(ResultSetUtil.getInt(rs, "horse_id"));
                scorecard.setHorseId(ResultSetUtil.getInt(rs, "horse_id"));
                scorecard.setProjectId(ResultSetUtil.getInt(rs, "project_id"));
                scorecard.setOrgId(ResultSetUtil.getInt(rs, "organization_id"));
                scorecard.setContentHeaderId(ResultSetUtil.getInt(rs, "content_header_id"));
                scorecard.setProductId(ResultSetUtil.getInt(rs, "product_id"));
                scorecard.setProdouctName(ResultSetUtil.getString(rs, "product_name"));
                scorecard.setProdouctDesc(ResultSetUtil.getString(rs, "product_desc"));
                scorecard.setTargetId(ResultSetUtil.getInt(rs, "target_id"));
                scorecard.setTargetName(ResultSetUtil.getString(rs, "target_name"));
                scorecard.setTargetShortName(ResultSetUtil.getString(rs, "target_short_name"));
                scorecard.setSurveyConfigId(ResultSetUtil.getInt(rs, "survey_config_id"));
                scorecard.setSurveyName(ResultSetUtil.getString(rs, "survey_name"));
                scorecard.setTitle(ResultSetUtil.getString(rs, "title"));
                scorecard.setStudyPeriodId(ResultSetUtil.getInt(rs, "study_period_id"));
                scorecard.setStudyPeriod(ResultSetUtil.getString(rs, "study_period_name"));
                Date submitTime = ResultSetUtil.getDate(rs, "submit_time");
                int status = ResultSetUtil.getInt(rs, "workflow_object_status");
                if (submitTime == null) {
                    scorecard.setStatus(Constants.SCORECARD_STATUS_NO_DATA);
                } else if (status == com.ocs.indaba.common.Constants.WORKFLOW_OBJECT_STATUS_DONE) {
                    scorecard.setStatus(Constants.SCORECARD_STATUS_COMPLETED);
                } else {
                    scorecard.setStatus(Constants.SCORECARD_STATUS_SUBMITTED);
                }
                return scorecard;
            }
        };
        List<ScorecardInfo> list = getJdbcTemplate().query(
                SELECT_SCORECARD_INFO_BY_HORSE_ID,
                new Object[]{horseId},
                new int[]{INTEGER},
                mapper);
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }

    /**
     *
     * @param horseId
     * @return
     */
    public ProductInfo selectProductInfoByProdId(int productId) {
        logger.debug("Select product info by product id: " + productId);
        RowMapper mapper = new RowMapper() {

            public ProductInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                ProductInfo scorecard = new ProductInfo();
                scorecard.setProductId(ResultSetUtil.getInt(rs, "product_id"));
                scorecard.setProdouctName(ResultSetUtil.getString(rs, "product_name"));
                scorecard.setProdouctDesc(ResultSetUtil.getString(rs, "product_desc"));
                scorecard.setSurveyConfigId(ResultSetUtil.getInt(rs, "survey_config_id"));
                scorecard.setSurveyName(ResultSetUtil.getString(rs, "survey_name"));
                scorecard.setStudyPeriodId(ResultSetUtil.getInt(rs, "study_period_id"));
                scorecard.setStudyPeriod(ResultSetUtil.getString(rs, "study_period_name"));
                return scorecard;
            }
        };
        List<ProductInfo> list = getJdbcTemplate().query(
                SELECT_PRODUCT_INFO_BY_PRODUCT_ID,
                new Object[]{productId},
                new int[]{INTEGER},
                mapper);
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }

    public List<SurveyIndicator> selectSurveyIndicatorsByIndicatorIds(List<Integer> indicatorIds) {
        logger.debug("Select indicators by ids: " + indicatorIds);
        if (indicatorIds == null || indicatorIds.isEmpty()) {
            return null;
        }

        String ids = indicatorIds.toString();
        String sqlStr = MessageFormat.format(SELECT_INDICATOR_BY_INDICATOR_IDS, ids.substring(1, ids.length() - 1));

        return super.find(sqlStr);
    }

    public List<QuestionOption> selectQuestionOptionsByIndicatorId(int indicatorId) {
        logger.debug("Select question options by indicator id: " + indicatorId);

        RowMapper mapper = new RowMapper() {

            public QuestionOption mapRow(ResultSet rs, int rowNum) throws SQLException {
                QuestionOption option = new QuestionOption();
                option.setId(ResultSetUtil.getInt(rs, "id"));
                option.setWeight(ResultSetUtil.getInt(rs, "weight"));
                option.setScore(ResultSetUtil.getInt(rs, "score") / Constants.SCORE_BASE_VALUE);
                option.setCriteria(ResultSetUtil.getString(rs, "criteria"));
                option.setLabel(ResultSetUtil.getString(rs, "label"));
                option.setMask(ResultSetUtil.getInt(rs, "mask"));
                option.setUseScore(ResultSetUtil.getInt(rs, "use_score") == 1);
                option.setHint(ResultSetUtil.getString(rs, "tip"));
                return option;
            }
        };
        return getJdbcTemplate().query(
                SELECT_QUESTION_OPTIONS_BY_INDICATOR_ID,
                new Object[]{indicatorId},
                new int[]{INTEGER},
                mapper);
    }

    /**
     * Select question count of a specified horse
     *
     * @param horseId
     * @return
     */
    public int selectQuestionCountByHorseId(int horseId) {
        logger.debug("Select question count by horse id: " + horseId);

        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return ResultSetUtil.getInt(rs, "count");
            }
        };
        List<Integer> list = getJdbcTemplate().query(
                SELECT_QUESTION_COUNT_BY_HORSE_ID,
                new Object[]{horseId},
                new int[]{INTEGER},
                mapper);

        return (list == null || list.isEmpty()) ? 0 : list.get(0);
    }

    /**
     * Select peer reviewed count of a specified horse
     *
     * @param horseId
     * @return
     */
    public int selectPeerReviewedCountByHorseId(int horseId) {
        logger.debug("Select peer reviewed count by horse id: " + horseId);

        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return ResultSetUtil.getInt(rs, "peer_reviewed_count");
            }
        };
        List<Integer> list = getJdbcTemplate().query(
                SELECT_PEER_REVIEWED_COUNT_BY_HORSE_ID,
                new Object[]{horseId},
                new int[]{INTEGER},
                mapper);

        return (list == null || list.isEmpty()) ? 0 : list.get(0);
    }

    /**
     * Select unique peer reviewer count of a specified horse
     *
     * @param horseId
     * @return
     */
    public int selectUniquePeerReviewerCountByHorseId(int horseId) {
        logger.debug("Select unique peer reviewer count by horse id: " + horseId);

        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return ResultSetUtil.getInt(rs, "peer_reviewer_count");
            }
        };
        List<Integer> list = getJdbcTemplate().query(
                SELECT_UNIQUE_PEER_REVIEWER_COUNT_BY_HORSE_ID,
                new Object[]{horseId},
                new int[]{INTEGER},
                mapper);

        return (list == null || list.isEmpty()) ? 0 : list.get(0);
    }

    public List<HorseVO> selectAllHorsesByProductId(int productId) {
        logger.debug("Select indicators by project ids: " + productId);

        RowMapper mapper = new RowMapper() {

            public HorseVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                HorseVO horse = new HorseVO();
                horse.setHorseId(ResultSetUtil.getInt(rs, "horse_id"));
                horse.setTargetId(ResultSetUtil.getInt(rs, "target_id"));
                horse.setTargetName(ResultSetUtil.getString(rs, "target_name"));
                horse.setDone(ResultSetUtil.getDate(rs, "completion_time") == null);
                return horse;
            }
        };

        return getJdbcTemplate().query(
                SELECT_ALL_HORSES_BY_PRODUCT_ID,
                new Object[]{productId},
                new int[]{INTEGER},
                mapper);
    }

    /**
     * For Integer/Float/Text type answer ONLY
     */
    class AnswerRowMapper implements RowMapper {

        public AnswerVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            AnswerVO answer = new AnswerVO();
            answer.setId(ResultSetUtil.getInt(rs, "answer_id"));
            answer.setIndicatorId(ResultSetUtil.getInt(rs, "indicator_id"));
            answer.setInputValue(ResultSetUtil.getString(rs, "input_value"));
            answer.setUseScore(false);
            answer.setLabel(ResultSetUtil.getString(rs, "label"));
            answer.setReferenceObjectId(ResultSetUtil.getInt(rs, "reference_object_id"));
            answer.setReferenceId(ResultSetUtil.getInt(rs, "reference_id"));
            answer.setReferenceName(ResultSetUtil.getString(rs, "reference_name"));
            answer.setReferences(ResultSetUtil.getString(rs, "source_description"));
            answer.setComments(ResultSetUtil.getString(rs, "comments"));
            answer.setAnswerUserId(ResultSetUtil.getInt(rs, "answer_user_id"));
            answer.setInternalMsgboardId(ResultSetUtil.getInt(rs, "internal_msgboard_id"));
            answer.setStaffAuthorMsgboardId(ResultSetUtil.getInt(rs, "staff_author_msgboard_id"));

            return answer;
        }
    };
}
