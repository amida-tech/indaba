/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.SurveyIndicator;
import com.ocs.indaba.po.SurveyQuestion;
import com.ocs.indaba.util.ListUtils;
import com.ocs.indaba.vo.SurveyQuestionTreeView;
import com.ocs.indaba.vo.SurveyQuestionVO;
import com.ocs.util.StringUtils;
import java.sql.ResultSet;
import static java.sql.Types.INTEGER;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author luwb
 */
public class SurveyQuestionDAO extends SmartDaoMySqlImpl<SurveyQuestion, Integer> {

    private static final Logger log = Logger.getLogger(SurveyQuestionDAO.class);

    private static final String SELECT_SURVEY_QUESTIONS_BY_CATEGORY_ID =
            "SELECT sq.id survey_question_id, sq.name survey_question_name, "
            + "si.id survey_indicator_id, si.name survey_indicator_name, sq.*, si.* "
            + "FROM survey_question sq, survey_indicator si "
            + "WHERE sq.survey_category_id=? AND sq.survey_indicator_id=si.id ORDER BY sq.weight";

    private static final String SELECT_SURVEY_QUESTIONS_BY_SURVEY_CATEGORY_ID =
            "SELECT * from survey_question where survey_category_id=? ORDER BY weight";
    private static final String SELECT_FIRST_SURVEY_QUESTION_BY_SURVEY_CATEGORY_ID =
            "SELECT * from survey_question where survey_category_id=? ORDER BY weight limit 0,1";
    private static final String SELECT_LAST_SURVEY_QUESTION_BY_SURVEY_CATEGORY_ID =
            "SELECT * from survey_question where survey_category_id=? ORDER BY weight DESC limit 0,1";
    private static final String SELECT_SURVEY_QUESTION_COUNT_BY_HORSE_ID = "SELECT count(sq.id) count "
            + "FROM content_header ch, survey_content_object sco, survey_question sq "
            + "WHERE ch.horse_id=? AND sco.id=ch.content_object_id AND sco.survey_config_id=sq.survey_config_id";
    private static final String SELECT_SURVEY_QUESTION_BY_CONFIG_ID =
            "SELECT * FROM survey_question WHERE survey_config_id=? ORDER BY weight";
    private static final String SELECT_SURVEY_QUESTION_BY_CONFIG_ID_AND_CATEGORY_ID =
            "SELECT * FROM survey_question WHERE survey_config_id=? AND survey_category_id=? ORDER BY weight";
    private static final String SELECT_SURVEY_QUESTION_BY_CONFIGID_AND_INDICATOR_ID =
            "SELECT * FROM survey_question WHERE id!=? AND survey_config_id=? AND survey_indicator_id=?";
    private static final String SELECT_MAX_WEIGHT_BY_CONFIGID_AND_CATEGORYID =
            "SELECT MAX(weight) FROM survey_question WHERE survey_config_id=? AND survey_category_id=?";
    private static final String DELETE_SURVEY_QUESTION_BY_CONFIGID_AND_CATEGORYID =
            "DELETE FROM survey_question WHERE survey_config_id=? AND survey_category_id=?";
    private static final String DELETE_SURVEY_QUESTION_BY_CONFIGID =
            "DELETE FROM survey_question WHERE survey_config_id=?";
    private static final String INCREASE_WEIGHT_OF_SURVEY_QUESTIONS_BY_CONFIGID_AND_CATEGORYID =
            "UPDATE survey_question SET weight=weight+1 WHERE weight>? AND survey_config_id=? AND survey_category_id=?";
    private static final String DECREASE_WEIGHT_OF_SURVEY_QUESTIONS_BY_CONFIGID_AND_CATEGORYID =
            "UPDATE survey_question SET weight=weight-1 WHERE weight>? AND survey_config_id=? AND survey_category_id=?";
    private static final String IS_LAST_SURVEY_QUESTION_BY_CONFIGID_AND_CATEGORYID =
            "SELECT COUNT(1) FROM survey_question WHERE id=? AND "
            + "weight=(SELECT MAX(weight) FROM survey_category WHERE survey_config_id=? AND survey_category_id=?)";
    private static final String IS_FIRST_SURVEY_QUESTION_BY_CONFIGID_AND_CATEGORYID =
            "SELECT COUNT(1) FROM survey_question WHERE id=? AND "
            + "weight=(SELECT MIN(weight) FROM survey_category WHERE survey_config_id=? AND survey_category_id=?)";

    public boolean isLastSurveyQuestionByConfigIdAndCategoryId(int qstnId, int surveyConfigId, int surveyCategoryId) {
        return super.count(IS_LAST_SURVEY_QUESTION_BY_CONFIGID_AND_CATEGORYID, qstnId, surveyConfigId, surveyCategoryId) > 0;
    }

    public boolean isFirstSurveyQuestionByConfigIdAndCategoryId(int qstnId, int surveyConfigId, int surveyCategoryId) {
        return super.count(IS_FIRST_SURVEY_QUESTION_BY_CONFIGID_AND_CATEGORYID, qstnId, surveyConfigId, surveyCategoryId) > 0;
    }

    public int increaseWeightOfSurveyQuestionsByConfigId(int weight, int surveyConfigId, int surveyCategoryId) {
        logger.debug("Decrease weight[weight=" + weight + ", confId=" + surveyConfigId + ", pCatId=" + "]:\n\t" + INCREASE_WEIGHT_OF_SURVEY_QUESTIONS_BY_CONFIGID_AND_CATEGORYID);
        return super.update(INCREASE_WEIGHT_OF_SURVEY_QUESTIONS_BY_CONFIGID_AND_CATEGORYID, weight, surveyConfigId, surveyCategoryId);
    }

    public int decreaseWeightOfSurveyQuestionsByConfigId(int weight, int surveyConfigId, int surveyCategoryId) {
        logger.debug("Decrease weight[weight=" + weight + ", confId=" + surveyConfigId + ", pCatId=" + "]:\n\t" + DECREASE_WEIGHT_OF_SURVEY_QUESTIONS_BY_CONFIGID_AND_CATEGORYID);
        return super.update(DECREASE_WEIGHT_OF_SURVEY_QUESTIONS_BY_CONFIGID_AND_CATEGORYID, weight, surveyConfigId, surveyCategoryId);
    }

    public void deleteByConfigId(int surveyConfigId) {
        super.delete(DELETE_SURVEY_QUESTION_BY_CONFIGID, surveyConfigId);
    }

    public void deleteByConfigIdAndCategoryId(int surveyConfigId, int surveyCategoryId) {
        super.delete(DELETE_SURVEY_QUESTION_BY_CONFIGID_AND_CATEGORYID, surveyConfigId, surveyCategoryId);
    }

    public int selectMaxWeight(int surveyConfigId, int surveyCategoryId) {
        return (int) super.count(SELECT_MAX_WEIGHT_BY_CONFIGID_AND_CATEGORYID, surveyConfigId, surveyCategoryId);
    }

    public SurveyQuestion selectSurveyQuestionExcept(int suerveyQuestionId, int surveyConfigId, int surveyIndicatorId) {
        return super.findSingle(SELECT_SURVEY_QUESTION_BY_CONFIGID_AND_INDICATOR_ID, suerveyQuestionId, surveyConfigId, surveyIndicatorId);
    }

    public List<SurveyQuestion> selectSurveyQuestionsByConfigId(int surveyConfigId) {
        return super.find(SELECT_SURVEY_QUESTION_BY_CONFIG_ID, surveyConfigId);
    }

    private static final String SELECT_SURVEY_QUESTION_TREE_VIEWS_BY_CONFIG_ID =
            "SELECT sq.*, si.question, si.answer_type FROM survey_question sq, survey_indicator si "
            + "WHERE sq.survey_config_id=? AND si.id=sq.survey_indicator_id ORDER BY sq.weight";

    public List<SurveyQuestionTreeView> selectSurveyQuestionTreeViewsByConfigId(int surveyConfigId) {
        RowMapper mapper = new SurveyQuestionTreeViewRowMapper();
        
        List<SurveyQuestionTreeView> list = getJdbcTemplate().query(
                SELECT_SURVEY_QUESTION_TREE_VIEWS_BY_CONFIG_ID,
                new Object[]{surveyConfigId},
                new int[]{INTEGER},
                mapper);

        return list;
    }

    private static final String SELECT_SURVEY_QUESTION_TREE_VIEWS_BY_CONFIG_ID_AND_LANGUAGE =
            "SELECT sq.*, sii.question, si.answer_type FROM survey_question sq, survey_indicator si, survey_indicator_intl sii "
            + "WHERE sq.survey_config_id=? AND si.id=sq.survey_indicator_id AND sii.survey_indicator_id=si.id AND sii.language_id=? ORDER BY sq.weight";

    public List<SurveyQuestionTreeView> selectSurveyQuestionTreeViewsByConfigIdAndLanguage(int surveyConfigId, int langId) {
        RowMapper mapper = new SurveyQuestionTreeViewRowMapper();

        List<SurveyQuestionTreeView> list = getJdbcTemplate().query(
                SELECT_SURVEY_QUESTION_TREE_VIEWS_BY_CONFIG_ID,
                new Object[]{surveyConfigId},
                new int[]{INTEGER},
                mapper);

        List<SurveyQuestionTreeView> listIntl = getJdbcTemplate().query(
                SELECT_SURVEY_QUESTION_TREE_VIEWS_BY_CONFIG_ID_AND_LANGUAGE,
                new Object[]{surveyConfigId, langId},
                new int[]{INTEGER, INTEGER},
                mapper);

        if (listIntl != null && !listIntl.isEmpty()) {
            for (SurveyQuestionTreeView q : list) {
                for (SurveyQuestionTreeView qi : listIntl) {
                    if (q.getSurveyIndicatorId() == qi.getSurveyIndicatorId()) {
                        if (!StringUtils.isEmpty(qi.getText())) {
                            q.setText(qi.getText());
                        }
                        break;
                    }
                }
            }
        }

        return list;
    }

    private static final String SELECT_SURVEY_QUESTION_TREE_VIEWS_BY_CONFIG_ID_AND_CATEGORY_ID =
            "SELECT sq.*, si.question, si.answer_type FROM survey_question sq, survey_indicator si "
            + "WHERE sq.survey_config_id=? AND sq.survey_category_id=? AND si.id=sq.survey_indicator_id ORDER BY weight";

    public List<SurveyQuestionTreeView> selectSurveyQuestionTreeViewsByConfigIdAndCategoryId(int surveyConfigId, int surveyCategoryId) {
        RowMapper mapper = new SurveyQuestionTreeViewRowMapper();

        List<SurveyQuestionTreeView> list = getJdbcTemplate().query(
                SELECT_SURVEY_QUESTION_TREE_VIEWS_BY_CONFIG_ID_AND_CATEGORY_ID,
                new Object[]{surveyConfigId, surveyCategoryId},
                new int[]{INTEGER, INTEGER},
                mapper);

        return list;
    }
    

    public List<SurveyQuestion> selectSurveyQuestionsByConfigIdAndCategoryId(int surveyConfigId, int surveyCategoryId) {
        return super.find(SELECT_SURVEY_QUESTION_BY_CONFIG_ID_AND_CATEGORY_ID, (Object) surveyConfigId, surveyCategoryId);
    }

    public SurveyQuestion selectSurveyQuestionById(int surveyQuestionId) {
        return super.get(surveyQuestionId);
    }

    public int selectSurveyQuestionCountByHorseId(int horseId) {
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("count");
            }
        };
        List<Integer> list = getJdbcTemplate().query(
                SELECT_SURVEY_QUESTION_COUNT_BY_HORSE_ID,
                new Object[]{horseId},
                new int[]{INTEGER},
                mapper);
        return (list != null && list.size() > 0) ? list.get(0) : 0;
    }

    public List<SurveyQuestionVO> selectSurveyQuestionsBy(int surveyCategoryId) {
        RowMapper mapper = new SurveyQuestionRowMapper();
        List<SurveyQuestionVO> list = getJdbcTemplate().query(
                SELECT_SURVEY_QUESTIONS_BY_CATEGORY_ID,
                new Object[]{surveyCategoryId},
                new int[]{INTEGER},
                mapper);
        return list;
    }


    private static final String SELECT_SURVEY_QUESTIONS_BY_SURVEY_CONFIG_ID =
            "SELECT sq.id survey_question_id, sq.name survey_question_name, "
            + "si.id survey_indicator_id, si.name survey_indicator_name, sq.*, si.* "
            + "FROM survey_question sq, survey_indicator si "
            + "WHERE sq.survey_config_id=? AND sq.survey_indicator_id=si.id ORDER BY sq.weight";

    public List<SurveyQuestionVO> selectSurveyQuestions(int surveyConfigId) {
        RowMapper mapper = new SurveyQuestionRowMapper();
        List<SurveyQuestionVO> list = getJdbcTemplate().query(
                SELECT_SURVEY_QUESTIONS_BY_SURVEY_CONFIG_ID,
                new Object[]{surveyConfigId},
                new int[]{INTEGER},
                mapper);
        return list;
    }

    public List<Integer> selectSurveyQuestionIdsBy(int surveyCategoryId) {
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("survey_question_id");
            }
        };
        List<Integer> list = getJdbcTemplate().query(
                SELECT_SURVEY_QUESTIONS_BY_CATEGORY_ID,
                new Object[]{surveyCategoryId},
                new int[]{INTEGER},
                mapper);
        return list;
    }

    public List<SurveyQuestion> getSurveyQuestionBySurveyCategoryId(int surveyCategoryId) {
        return super.find(SELECT_SURVEY_QUESTIONS_BY_SURVEY_CATEGORY_ID, surveyCategoryId);
    }

    public SurveyQuestion getFirstSurveyQuestionBySurveyCategoryId(int surveyCategoryId) {
        return super.findSingle(SELECT_FIRST_SURVEY_QUESTION_BY_SURVEY_CATEGORY_ID, surveyCategoryId);
    }

    public SurveyQuestion getLastSurveyQuestionBySurveyCategoryId(int surveyCategoryId) {
        return super.findSingle(SELECT_LAST_SURVEY_QUESTION_BY_SURVEY_CATEGORY_ID, surveyCategoryId);
    }


    private static final String SELECT_SURVEY_QUESTION_TREE_VIEWS_BY_QUESTION_ID =
            "SELECT sq.*, si.question, si.answer_type FROM survey_question sq, survey_indicator si "
            + "WHERE sq.id=? AND si.id=sq.survey_indicator_id";


    public SurveyQuestionTreeView getSurveyQuestionTreeView(int qstnId) {
        RowMapper mapper = new SurveyQuestionTreeViewRowMapper();

        SurveyQuestionTreeView qst = (SurveyQuestionTreeView) getJdbcTemplate().queryForObject(
                SELECT_SURVEY_QUESTION_TREE_VIEWS_BY_QUESTION_ID,
                new Object[]{qstnId},
                new int[]{INTEGER},
                mapper);

        return qst;
    }


    private static final String SELECT_SURVEY_QUESTION_TREE_VIEWS_BY_QUESTION_ID_AND_LANGUAGE =
            "SELECT sq.*, sii.question, si.answer_type FROM survey_question sq, survey_indicator si, survey_indicator_intl sii "
            + "WHERE sq.id=? AND si.id=sq.survey_indicator_id AND sii.survey_indicator_id=si.id AND sii.language_id=?";

    public SurveyQuestionTreeView getSurveyQuestionTreeView(int qstnId, int langId) {
        RowMapper mapper = new SurveyQuestionTreeViewRowMapper();

        SurveyQuestionTreeView qst = (SurveyQuestionTreeView) getJdbcTemplate().queryForObject(
                SELECT_SURVEY_QUESTION_TREE_VIEWS_BY_QUESTION_ID,
                new Object[]{qstnId},
                new int[]{INTEGER},
                mapper);

        SurveyQuestionTreeView qstIntl = (SurveyQuestionTreeView) getJdbcTemplate().queryForObject(
                SELECT_SURVEY_QUESTION_TREE_VIEWS_BY_QUESTION_ID_AND_LANGUAGE,
                new Object[]{qstnId, langId},
                new int[]{INTEGER, INTEGER},
                mapper);

        if (qstIntl != null) {
            if (!StringUtils.isEmpty(qstIntl.getText())) {
                qst.setText(qstIntl.getText());
            }
        }

        return qst;
    }


    public void updateParentAndWeights(List<Integer> qstIds, int parentId, List<Integer> weights) {
        if (qstIds == null || qstIds.isEmpty()) return;
        
        String baseSql = "UPDATE survey_question SET survey_category_id = ?, weight = CASE id {0} END "
                + "WHERE id in ({1})";

        StringBuilder wsb = new StringBuilder();

        for (int i = 0; i < qstIds.size(); i++) {
            wsb.append("WHEN " + qstIds.get(i) + " THEN " + weights.get(i) + " ");
        }

        String sql = MessageFormat.format(baseSql, wsb.toString(), ListUtils.listToString(qstIds));

        logger.debug("SQL: " + sql);

        super.update(sql, parentId);
    }


    
    private static final String DELETE_QUESTIONS_IN_CATS_BY_CONFIGID =
            "DELETE FROM survey_question WHERE survey_config_id=? AND survey_category_id IN ({0})";

    public void deleteQuestionsInCategories(int scId, List<Integer> catIds) {
        if (catIds == null || catIds.isEmpty()) return;
        
         String catIdsString = StringUtils.list2Str(catIds);

        logger.debug("Delete questions of survey-config " + scId + " from categories: " + catIdsString);

        String sql = MessageFormat.format(DELETE_QUESTIONS_IN_CATS_BY_CONFIGID, catIdsString);
        super.delete(sql, scId);
    }
}

class SurveyQuestionRowMapper implements RowMapper {

    public SurveyQuestionVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        SurveyQuestion question = new SurveyQuestion();
        question.setDefaultAnswerId(rs.getInt("default_answer_id"));
        question.setId(rs.getInt("survey_question_id"));
        question.setName(rs.getString("survey_question_name"));
        question.setPublicName(rs.getString("public_name"));
        question.setSurveyCategoryId(rs.getInt("survey_category_id"));
        question.setSurveyConfigId(rs.getInt("survey_config_id"));
        question.setSurveyIndicatorId(rs.getInt("survey_indicator_id"));
        question.setWeight(rs.getInt("weight"));
        SurveyIndicator indicator = new SurveyIndicator();
        indicator.setAnswerType(rs.getShort("answer_type"));
        indicator.setAnswerTypeId(rs.getInt("answer_type_id"));
        indicator.setCreateTime(rs.getDate("create_time"));
        indicator.setCreateUserId(rs.getInt("create_user_id"));
        indicator.setId(rs.getInt("survey_indicator_id"));
        indicator.setName(rs.getString("survey_indicator_name"));
        indicator.setOriginalIndicatorId(rs.getInt("original_indicator_id"));
        indicator.setQuestion(rs.getString("question"));
        indicator.setReferenceId(rs.getInt("reference_id"));
        indicator.setReusable(rs.getBoolean("reusable"));
        indicator.setTip(rs.getString("tip"));
        SurveyQuestionVO vo = new SurveyQuestionVO();
        vo.setQuestion(question);
        vo.setIndicator(indicator);
        return vo;
    }
}


class SurveyQuestionTreeViewRowMapper implements RowMapper {

    public SurveyQuestionTreeView mapRow(ResultSet rs, int rowNum) throws SQLException {
        SurveyQuestionTreeView question = new SurveyQuestionTreeView();
        question.setId(rs.getInt("id"));
        question.setName(rs.getString("name"));
        question.setPublicName(rs.getString("public_name"));
        question.setSurveyCategoryId(rs.getInt("survey_category_id"));
        question.setSurveyConfigId(rs.getInt("survey_config_id"));
        question.setSurveyIndicatorId(rs.getInt("survey_indicator_id"));
        question.setDefaultAnswerId(rs.getInt("default_answer_id"));
        question.setWeight(rs.getInt("weight"));
        question.setText(rs.getString("question"));
        question.setAnswerType(rs.getShort("answer_type"));
        
        return question;
    }
}
