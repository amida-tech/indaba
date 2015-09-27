/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import static java.sql.Types.INTEGER;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.SurveyAnswer;
import com.ocs.indaba.util.ResultSetUtil;
import com.ocs.indaba.vo.SurveyAnswerProblemVO;
import com.ocs.util.ListUtils;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jeff
 */
public class SurveyAnswerDAO extends SmartDaoMySqlImpl<SurveyAnswer, Integer> {

    private static final Logger log = Logger.getLogger(SurveyAnswerDAO.class);
    private static final String SELECT_STAFF_REVIEWED_ANSWER_COUNT_BY_HORSEID =
            "SELECT COUNT(1) FROM survey_answer sa "
            + "INNER JOIN content_header ch ON sa.survey_content_object_id = ch.content_object_id "
            + "WHERE ch.horse_id = ? AND staff_reviewed = 1";
    private static final String SELECT_PR_REVIEWED_ANSWER_COUNT_BY_HORSEID =
            "SELECT COUNT(1) FROM survey_answer sa "
            + "INNER JOIN content_header ch ON sa.survey_content_object_id = ch.content_object_id "
            + "WHERE ch.horse_id = ? AND pr_reviewed = 1";
    private static final String SELECT_UNANSWERED_SURVEY_PROBLEMS_BY_HORSE_ID =
            "SELECT sa.* FROM content_header ch, survey_answer sa "
            + "WHERE ch.horse_id=? AND ch.content_object_id=sa.survey_content_object_id AND sa.reviewer_has_problem=1 AND sa.author_responded=0";
    private static final String SELECT_SURVEY_ANSWERS_BY_HORSE_ID =
            "SELECT sa.* FROM content_header ch, survey_answer sa "
            + "WHERE ch.horse_id=? AND ch.content_object_id=sa.survey_content_object_id AND sa.answer_object_id>0 ORDER BY sa.id";
    private static final String SELECT_PROBLEM_SURVEY_ANSWERS_BY_HORSE_ID =
            "SELECT sa.* FROM content_header ch, survey_answer sa "
            + "WHERE ch.horse_id=? AND ch.content_object_id=sa.survey_content_object_id AND sa.answer_object_id>0 AND sa.reviewer_has_problem=1 ORDER BY sa.id";
    private static final String SELECT_SURVEY_ANSWER_BY_CONTENT_OBJECT_ID =
            "SELECT * FROM survey_answer WHERE survey_content_object_id=? AND survey_question_id=?";
    private static final String SELECT_SURVEY_ANSWERS_BY_CONTENT_OBJECT_ID =
            "SELECT * FROM survey_answer WHERE survey_content_object_id=? AND survey_question_id IN ({0})";
    private static final String SELECT_SURVEY_QUESTION_ID =
            "SELECT * FROM survey_answer WHERE survey_question_id=?";
    private static final String GET_SURVEY_ANSWER_BY_SURVEY_ANSWER_ID =
            "select * from survey_answer where id = ?";
    private static final String UPDATE_SURVEY_ANSWER_BY_ID =
            "update survey_answer sa set sa.answer_object_id=?, sa.reference_object_id=?, sa.comments=?, "
            + "sa.answer_time=now(), sa.answer_user_id=? where id=?";
    private static final String EDIT_UPDATE_SURVEY_ANSWER_BY_ID =
            "update survey_answer sa set sa.answer_object_id=?, sa.reference_object_id=?, sa.comments=?, "
            + "sa.edited=? where id=?";
    private static final String OVERALL_REVIEW_UPDATE_SURVEY_ANSWER_BY_ID =
            "update survey_answer sa set sa.answer_object_id=?, sa.reference_object_id=?, sa.comments=?, "
            + "sa.overall_reviewed=? where id=?";
    private static final String SELECT_SURVEY_ANSWER_BY_SURVEY_CATEGORY_ID_AND_CONTENT_OBJECT_ID =
            "SELECT sa.* from survey_answer sa, survey_question sq "
            + "where sq.survey_category_id=? AND sa.survey_question_id=sq.id AND sa.survey_content_object_id=? "
            + "order by sa.id";
    private static final String SELECT_FIRST_SURVEY_ANSWER_BY_SURVEY_CATEGORY_ID_AND_CONTENT_OBJECT_ID =
            "SELECT sa.* from survey_answer sa, survey_question sq "
            + "where sq.survey_category_id=? AND sa.survey_question_id=sq.id AND sa.survey_content_object_id=? "
            + "order by sa.id limit 0,1";

    private static final String SELECT_COMPLETED_SURVEY_ANSWER_COUNT_BY_HORSE_ID = "SELECT count(sa.id) count "
            + "FROM content_header ch, survey_answer sa, survey_question sq, survey_indicator si "
            + "WHERE ch.horse_id=? AND ch.content_object_id=sa.survey_content_object_id AND sa.answer_object_id>0 "
            + "AND sq.id=sa.survey_question_id AND si.id=sq.survey_indicator_id "
            + "AND (si.answer_type <> 6 OR sa.completed <> 0)";


    private static final String SELECT_PROBLEM_COUNT_BY_HORSE_ID =
            "SELECT count(sa.id) count FROM content_header ch, survey_answer sa "
            + "WHERE ch.horse_id = ? AND ch.content_object_id = sa.survey_content_object_id "
            + "AND sa.reviewer_has_problem = 1";

    private static final String SELECT_RESPONDED_PROBLEM_COUNT_BY_HORSE_ID =
            "SELECT count(sa.id) count FROM content_header ch, survey_answer sa "
            + "WHERE ch.horse_id = ? AND ch.content_object_id = sa.survey_content_object_id "
            + "AND sa.reviewer_has_problem = 1 AND sa.author_responded = 1";
    private static final String SELECT_PEER_REVIEWED_ANSWER_COUNT_BY_HORSEID_AND_REVIEWER_ID =
            "SELECT count(1) "
            + "FROM survey_peer_review spr JOIN survey_answer sa ON spr.survey_answer_id = sa.id "
            + "JOIN content_header ch ON ch.content_object_id = sa.survey_content_object_id "
            + "WHERE ch.horse_id = ? AND reviewer_user_id = ? AND (spr.opinion > -1 OR spr.suggested_answer_object_id > -1)";

    public static final String SELECT_SURVEY_ANSWER_PROBLEMS_VIEW =
            "SELECT sa.id answer_id, sq.id question_id, sq.public_name, si.id indicator_id, si.question question, sa.author_responded "
            + "FROM content_header ch, survey_answer sa, survey_question sq, survey_indicator si "
            + "WHERE ch.horse_id=? AND ch.content_type=? AND sa.reviewer_has_problem=? AND sa.survey_question_id=sq.id AND "
            + "ch.content_object_id=sa.survey_content_object_id AND si.id=sq.survey_indicator_id ORDER BY sq.public_name";

    private static final String UPDATE_EDITED_BY_ID =
            "update survey_answer sa set sa.edited=? where id=?";
    private static final String UPDATE_OVERALL_REVIEWED_BY_ID =
            "update survey_answer sa set sa.overall_reviewed=? where id=?";
    private static final String UPDATE_AUTHOR_RESPONDED_BY_ID =
            "update survey_answer sa set sa.author_responded=? where id=?";
    private static final String SELECT_COMPLETED_EDITED_SURVEY_ANSWER_COUNT_BY_HORSE_ID = "SELECT count(sa.id) count "
            + "FROM content_header ch, survey_answer sa "
            + "WHERE ch.horse_id=? AND ch.content_object_id=sa.survey_content_object_id AND sa.edited=1";
    private static final String SELECT_COMPLETED_OVERALL_REVIEWED_SURVEY_ANSWER_COUNT_BY_HORSE_ID = "SELECT count(sa.id) count "
            + "FROM content_header ch, survey_answer sa "
            + "WHERE ch.horse_id=? AND ch.content_object_id=sa.survey_content_object_id AND sa.overall_reviewed=1";
    private static final String SELECT_ALL_COMPLETED_SURVEY_ANSWERS_BY_HORSE_ID =
            "SELECT sa.* FROM content_header ch, survey_answer sa "
            + "WHERE ch.horse_id=? AND ch.content_object_id=sa.survey_content_object_id AND sa.answer_object_id>0";

    public void addAllSurveyAnswersToQuestionList(int horseId) {
        super.run("UPDATE survey_answer sa LEFT JOIN content_header ch ON ch.content_object_id=sa.survey_content_object_id "
                + "SET sa.reviewer_has_problem=1 WHERE ch.content_type=0 AND ch.horse_id=" + horseId);
    }

    public void addTaggedSurveyAnswersToQuestionList(int horseId, String tag) {
        String sql = "UPDATE survey_answer sa LEFT JOIN tag ON sa.id=tag.tagged_object_id "
                + "LEFT JOIN content_header ch ON ch.content_object_id=sa.survey_content_object_id "
                + "SET sa.reviewer_has_problem=1 WHERE tag.tagged_object_type=1 AND tag.label='" + tag + "' AND ch.horse_id=" + horseId;
        super.run(sql);
    }

    public List<SurveyAnswer> getAllSurveyAnswersByHorse(int horseId) {
        return super.find(SELECT_SURVEY_ANSWERS_BY_HORSE_ID, horseId);
    }

    public List<SurveyAnswer> getProblemSurveyAnswersByHorse(int horseId) {
        return super.find(SELECT_PROBLEM_SURVEY_ANSWERS_BY_HORSE_ID, horseId);
    }

    public List<SurveyAnswer> getAllCompletedSurveyAnswersByHorse(int horseId) {
        return super.find(SELECT_ALL_COMPLETED_SURVEY_ANSWERS_BY_HORSE_ID, horseId);
    }

    public List<SurveyAnswer> getUnansweredSurveyProblemsByHorse(int horseId) {
        //logger.debug("---->>>>" + SELECT_UNANSWERED_SURVEY_PROBLEMS_BY_HORSE_ID + ":" + horseId);
        return super.find(SELECT_UNANSWERED_SURVEY_PROBLEMS_BY_HORSE_ID, horseId);
    }

    public int selectCompletedSurveyAnswerCountByHorseId(int horseId) {
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("count");
            }
        };
        List<Integer> list = getJdbcTemplate().query(
                SELECT_COMPLETED_SURVEY_ANSWER_COUNT_BY_HORSE_ID,
                new Object[]{horseId},
                new int[]{INTEGER},
                mapper);
        return (list != null && list.size() > 0) ? list.get(0) : 0;
    }

    public int selectCompletedEditedSurveyAnswerCountByHorseId(int horseId) {
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("count");
            }
        };
        List<Integer> list = getJdbcTemplate().query(
                SELECT_COMPLETED_EDITED_SURVEY_ANSWER_COUNT_BY_HORSE_ID,
                new Object[]{horseId},
                new int[]{INTEGER},
                mapper);
        return (list != null && list.size() > 0) ? list.get(0) : 0;
    }

    public int selectCompletedOverallReviewedSurveyAnswerCountByHorseId(int horseId) {
        RowMapper mapper = new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("count");
            }
        };
        List<Integer> list = getJdbcTemplate().query(
                SELECT_COMPLETED_OVERALL_REVIEWED_SURVEY_ANSWER_COUNT_BY_HORSE_ID,
                new Object[]{horseId},
                new int[]{INTEGER},
                mapper);
        return (list != null && list.size() > 0) ? list.get(0) : 0;
    }

    public List<SurveyAnswerProblemVO> selectSuveryAnswerProblems(int horseId) {
        RowMapper mapper = new RowMapper() {

            public SurveyAnswerProblemVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                SurveyAnswerProblemVO problem = new SurveyAnswerProblemVO();
                problem.setAnswerId(ResultSetUtil.getInt(rs, "answer_id"));
                problem.setIndicatorId(ResultSetUtil.getInt(rs, "indicator_id"));
                problem.setQuestionId(ResultSetUtil.getInt(rs, "question_id"));
                problem.setPublicName(ResultSetUtil.getString(rs, "public_name"));
                problem.setQuestion(ResultSetUtil.getString(rs, "question"));
                problem.setResponded(ResultSetUtil.getInt(rs, "author_responded") == 1);
                return problem;
            }
        };

        return getJdbcTemplate().query(
                SELECT_SURVEY_ANSWER_PROBLEMS_VIEW,
                new Object[]{horseId, Constants.CONTENT_TYPE_SURVEY, 1},
                new int[]{INTEGER, INTEGER, INTEGER},
                mapper);
    }

    public int getQuestionIdbySurveyAnswerId(int surveyAnswerId) {
        return this.findSingle(GET_SURVEY_ANSWER_BY_SURVEY_ANSWER_ID, surveyAnswerId).getSurveyQuestionId();
    }

    public SurveyAnswer selectSurveyAnswerBy(int cntObjId, int questionId) {
        logger.debug("Select survey answer by question id and conent object id:\n " + SELECT_SURVEY_ANSWER_BY_CONTENT_OBJECT_ID + "[questionId=" + questionId + ", cntObjId=" + cntObjId + "].");
        return super.findSingle(SELECT_SURVEY_ANSWER_BY_CONTENT_OBJECT_ID, cntObjId, questionId);
    }

    /**
     * Get survey answers
     * @param cntObjId
     * @param questionIds
     * @return
     */
    public Map<Integer, SurveyAnswer> selectSurveyAnswersBy(int cntObjId, List<Integer> questionIds) {
        logger.debug("Select survey answers by question id and conent object id:\n " + SELECT_SURVEY_ANSWERS_BY_CONTENT_OBJECT_ID + "[questionIds=" + questionIds + ", cntObjId=" + cntObjId + "].");
        Map<Integer, SurveyAnswer> map = new HashMap<Integer, SurveyAnswer>();
        if (questionIds == null || questionIds.isEmpty()) {
            return map;
        }
        List<SurveyAnswer> surveyAnswers = super.find(MessageFormat.format(SELECT_SURVEY_ANSWERS_BY_CONTENT_OBJECT_ID, ListUtils.listToString(questionIds)), cntObjId);
        if (!ListUtils.isEmptyList(surveyAnswers)) {
            for (SurveyAnswer answer : surveyAnswers) {
                map.put(answer.getSurveyQuestionId(), answer);
            }
        }
        return map;
    }

    public SurveyAnswer selectSurveyAnswerByQuestionId(int questionId) {
        return super.findSingle(SELECT_SURVEY_QUESTION_ID, questionId);
    }

    public void updateSurveyAnswerById(int answerObjectId, int referObjectId, String comments, int userId, int surveyAnswerId) {
        logger.debug("Update survey_answer by creator");

        Object[] values = new Object[]{answerObjectId, referObjectId, comments, userId, surveyAnswerId};
        this.getJdbcTemplate().update(UPDATE_SURVEY_ANSWER_BY_ID, values);
    }

    public void editUpdateSurveyAnswerById(int answerObjectId, int referObjectId, String comments, int surveyAnswerId) {
        logger.debug("Update survey_answer by editor");

        Object[] values = new Object[]{answerObjectId, referObjectId, comments, true, surveyAnswerId};
        this.getJdbcTemplate().update(EDIT_UPDATE_SURVEY_ANSWER_BY_ID, values);
    }

    public void overallReviewUpdateSurveyAnswerById(int answerObjectId, int referObjectId, String comments, int surveyAnswerId) {
        logger.debug("Update survey_answer by overall Reviewer");

        Object[] values = new Object[]{answerObjectId, referObjectId, comments, true, surveyAnswerId};
        this.getJdbcTemplate().update(OVERALL_REVIEW_UPDATE_SURVEY_ANSWER_BY_ID, values);
    }

    public List<SurveyAnswer> getSurveyAnswerByCategoryIdAndContentId(int contentObjectId, int surveyCategoryId) {
        Object[] values = new Object[]{surveyCategoryId, contentObjectId};
        return super.find(SELECT_SURVEY_ANSWER_BY_SURVEY_CATEGORY_ID_AND_CONTENT_OBJECT_ID, values);
    }

    public SurveyAnswer getFirstSurveyAnswer(int contentObjectId, int surveyCategoryId) {
        Object[] values = new Object[]{surveyCategoryId, contentObjectId};
        return super.find(SELECT_FIRST_SURVEY_ANSWER_BY_SURVEY_CATEGORY_ID_AND_CONTENT_OBJECT_ID, values).get(0);
    }

    public int getReviewedAnswerCountByHorseId(int horseId) {
        log.debug("Select reviewed answer count by horse id: " + horseId);
        return (int) count(SELECT_STAFF_REVIEWED_ANSWER_COUNT_BY_HORSEID, horseId);
    }

    public int getPRReviewedAnswerCountByHorseId(int horseId) {
        log.debug("Select pr reviewed answer count by horse id: " + horseId);
        return (int) count(SELECT_PR_REVIEWED_ANSWER_COUNT_BY_HORSEID, horseId);
    }

    public void resetSurveyAnswerFlag(int taskType, int horseId) {
        List<String> fields = new ArrayList<String>();
        switch (taskType) {
            case Constants.TASK_TYPE_SURVEY_EDIT:
            case Constants.TASK_TYPE_SURVEY_CREATE:
                fields.add("edited");
                break;
            case Constants.TASK_TYPE_SURVEY_REVIEW:
                fields.add("staff_reviewed");
                fields.add("reviewer_has_problem");
                fields.add("author_responded");
                break;
            case Constants.TASK_TYPE_SURVEY_PR_REVIEW:
                fields.add("pr_reviewed");
                fields.add("reviewer_has_problem");
                fields.add("author_responded");
                break;
            case Constants.TASK_TYPE_SURVEY_OVERALL_REVIEW:
                fields.add("overall_reviewed");
                break;
        }
        if (fields.size() > 0) {
            StringBuffer sb = new StringBuffer();
            sb.append("UPDATE survey_answer SET ");

            for (String field : fields) {
                sb.append(field).append(" = 0,");
            }
            sb.deleteCharAt(sb.length() - 1);

            sb.append(" WHERE survey_content_object_id = ").append("(SELECT sco.id FROM survey_content_object sco ").append("JOIN content_header ch ON (ch.content_type = 0 AND ch.content_object_id = sco.id) ").append("JOIN horse h ON (h.content_header_id = ch.id) ").append("WHERE h.id = ? ").append("LIMIT 0, 1)");
            this.getJdbcTemplate().update(sb.toString(), new Object[]{horseId});
        }
    }

    public void updateSurveyAnswerFlag(int task_type, int surveyAnswerId) {
        String field = null;
        StringBuilder sb = new StringBuilder();
        switch (task_type) {
            case Constants.TASK_TYPE_SURVEY_EDIT:
                field = "edited";
                break;
            case Constants.TASK_TYPE_SURVEY_REVIEW:
                field = "staff_reviewed";
                break;
            case Constants.TASK_TYPE_SURVEY_PR_REVIEW:
                field = "pr_reviewed";
                break;
            case Constants.TASK_TYPE_SURVEY_REVIEW_RESPONSE:
                field = "author_responded";
                break;
        }
        if (field != null) {
            sb.append("UPDATE survey_answer SET ").append(field).append(" = 1 WHERE id = ?");
        }
        this.getJdbcTemplate().update(sb.toString(), new Object[]{surveyAnswerId});
    }

    public void updateEdited(int surveyAnswerId) {
        Object[] values = new Object[]{true, surveyAnswerId};
        this.getJdbcTemplate().update(UPDATE_EDITED_BY_ID, values);
    }

    public void updateOverallReviewed(int surveyAnswerId) {
        Object[] values = new Object[]{true, surveyAnswerId};
        this.getJdbcTemplate().update(UPDATE_OVERALL_REVIEWED_BY_ID, values);
    }

    public void updateAuthorResponded(int surveyAnswerId) {
        Object[] values = new Object[]{true, surveyAnswerId};
        this.getJdbcTemplate().update(UPDATE_AUTHOR_RESPONDED_BY_ID, values);
    }

    public int getPeerReviewedAnswerCountByHorseIdAndReviewerId(int horseId, int uid) {
        log.debug("Select peer reviewed answer count by horse id: " + horseId + " and reviewer id: " + uid);
        int result = (int) count(SELECT_PEER_REVIEWED_ANSWER_COUNT_BY_HORSEID_AND_REVIEWER_ID, horseId, uid);
        logger.debug("Peer reviewed questions: " + result);
        return result;
    }

    public int getProblemCountByHorseId(int horseId) {
        return (int) count(SELECT_PROBLEM_COUNT_BY_HORSE_ID, horseId);
    }

    public int getRespondedProblemCountByHorseId(int horseId) {
        return (int) count(SELECT_RESPONDED_PROBLEM_COUNT_BY_HORSE_ID, horseId);
    }


    static private final String UPDATE_COMPLETED_STATUS =
            "UPDATE survey_answer SET completed=?, answer_object_id=? WHERE id=?";

    public void updateCompletedStatus(int surveyAnswerId, int answerCount, int questionCount) {
        boolean completed = false;
        int answerObjectId = 0;

        if (answerCount > 0) answerObjectId = 1;  // indicate there are answers
        if (answerCount >= questionCount) completed = true;

        super.update(UPDATE_COMPLETED_STATUS, completed, answerObjectId, surveyAnswerId);
    }

    static private final String SELECT_SURVEY_ANSWER_BY_FLAG =
            "SELECT sa.* FROM survey_answer sa, groupobj go, groupobj_flag f, horse, survey_content_object sco " +
            "WHERE f.id=? AND go.id=f.groupobj_id AND sa.survey_question_id=go.survey_question_id " +
            "AND horse.id=go.horse_id AND sco.content_header_id=horse.content_header_id AND sa.survey_content_object_id=sco.id";

    public SurveyAnswer getSurveyAnswerByFlag(int flagId) {
        return super.findSingle(SELECT_SURVEY_ANSWER_BY_FLAG, flagId);
    }
}
