package com.ocs.indaba.dao;

import static java.sql.Types.INTEGER;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.SurveyPeerReview;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.indaba.util.ResultSetUtil;
import com.ocs.indaba.vo.SurveyPeerReviewBasicView;
import com.ocs.indaba.vo.SurveyPeerReviewVO;

public class SurveyPeerReviewDAO extends SmartDaoMySqlImpl<SurveyPeerReview, Integer> {

    private static final String SELECT_SURVEY_PEER_REVIEW_BY_OPINION_AND_HORSEID =
            "SELECT DISTINCT spr.survey_answer_id, sa.survey_question_id, sq.public_name, si.question, sa.staff_reviewed, sa.pr_reviewed "
            + "FROM survey_peer_review spr INNER JOIN survey_answer sa ON spr.survey_answer_id = sa.id "
            + "INNER JOIN survey_question sq ON sa.survey_question_id = sq.id "
            + "INNER JOIN survey_indicator si ON sq.survey_indicator_id = si.id "
            + "INNER JOIN content_header ch ON sa.survey_content_object_id = ch.id "
            + "WHERE sa.pr_reviewed = 1 AND spr.opinion = ? AND ch.horse_id = ?;";

    /** This one is very inefficient
    private static final String SELECT_SURVEY_PEER_REVIEWS_BY_HORSE_ID =
            "SELECT spr.* FROM survey_answer sa, survey_peer_review spr "
            + "WHERE sa.survey_content_object_id IN (SELECT content_object_id FROM content_header WHERE horse_id=?) AND "
            + "spr.survey_answer_id=sa.id";
     * ***/
     private static final String SELECT_SURVEY_PEER_REVIEWS_BY_HORSE_ID =
             "SELECT spr.* FROM survey_answer sa, survey_peer_review spr, content_header ch " +
             "WHERE sa.survey_content_object_id = ch.content_object_id AND ch.horse_id=? AND spr.survey_answer_id=sa.id";

     private static final String SELECT_SURVEY_PEER_REVIEWS_BY_PRODUCT_ID =
             "SELECT spr.* FROM survey_answer sa, survey_peer_review spr, content_header ch, horse " +
             "WHERE sa.survey_content_object_id = ch.content_object_id AND ch.horse_id=horse.id AND spr.survey_answer_id=sa.id " +
             "AND horse.product_id=?";



    @SuppressWarnings("unchecked")
    public SurveyPeerReview getSurveyPeerReviewsBySurveyAnswerIdAndReviewerId(Integer surveyAnswerId, int reviewerId) {
        List params = new ArrayList();
        params.add(surveyAnswerId);
        params.add(reviewerId);
        return findSingle("SELECT * FROM survey_peer_review WHERE survey_answer_id = ? AND reviewer_user_id = ?", params.toArray());
    }

    public SurveyPeerReview getCompletedSurveyPeerReviewsBySurveyAnswerIdAndReviewerId(Integer surveyAnswerId, int reviewerId) {
        List params = new ArrayList();
        params.add(surveyAnswerId);
        params.add(reviewerId);
        return findSingle("SELECT * FROM survey_peer_review "
                + "WHERE survey_answer_id = ? AND reviewer_user_id = ? AND (opinion!=-1 OR suggested_answer_object_id!=-1)", params.toArray());
    }

    public List<SurveyPeerReview> getSurveyPeerReviewsByUserId(int userId) {
        return find("SELECT * FROM survey_peer_review WHERE reviewer_user_id = ?", userId);
    }

    public List<SurveyPeerReview> getSurveyPeerReviewsBySurveyAnswerId(int surveyAnswerId) {
        return find("SELECT * FROM survey_peer_review WHERE survey_answer_id = ?", surveyAnswerId);
    }

    public SurveyPeerReview getSurveyPeerReviewBySurveyAnswerId(int surveyAnswerId) {
        return findSingle("SELECT * FROM survey_peer_review WHERE survey_answer_id = ?", surveyAnswerId);
    }

    public List<SurveyPeerReview> getSubmittedSurveyPeerReviewsBySurveyAnswerId(int surveyAnswerId) {
        logger.debug("Trying to get peer reviews for survey answer " + surveyAnswerId);
        List<SurveyPeerReview> result = find("SELECT * FROM survey_peer_review WHERE survey_answer_id = ? AND submit_time IS NOT NULL", surveyAnswerId);
        logger.debug("Result size: " + result.size());
        return result;
    }


    public List<SurveyPeerReview> getSubmittedSurveyPeerReviewsBySurveyAnswerIdAndUserId(int surveyAnswerId, int uid) {
        return find("SELECT * FROM survey_peer_review WHERE survey_answer_id=? AND reviewer_user_id=? AND submit_time IS NOT NULL", (long)surveyAnswerId, uid);
    }


    public List<SurveyPeerReview> getCompletedSurveyPeerReviewsBySurveyAnswerId(int surveyAnswerId) {
        return find("SELECT * FROM survey_peer_review WHERE survey_answer_id = ? AND (opinion!=-1 OR suggested_answer_object_id!=-1)", surveyAnswerId);
    }

    public long getTotalCompletedSurveyPeerReviewsBySurveyAnswerId(int surveyAnswerId) {
        return count("SELECT count(*) FROM survey_peer_review WHERE survey_answer_id = 1 AND (opinion!=-1 OR suggested_answer_object_id!=-1)", surveyAnswerId);
    }

    public List<SurveyPeerReviewVO> selectSurveyPeerReviewByOpinionAndHorseId(int opinion, int horseId) {
        RowMapper mapper = new RowMapper() {

            public SurveyPeerReviewVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                SurveyPeerReviewVO problem = new SurveyPeerReviewVO();
                problem.setAnswerId(ResultSetUtil.getInt(rs, "survey_answer_id"));
                problem.setQuestionId(ResultSetUtil.getInt(rs, "survey_question_id"));
                problem.setPublicName(ResultSetUtil.getString(rs, "public_name"));
                problem.setQuestion(ResultSetUtil.getString(rs, "question"));
                problem.setStaffReviewed(ResultSetUtil.getInt(rs, "staff_reviewed") == 1);
                problem.setPrReviewed(ResultSetUtil.getInt(rs, "pr_reviewed") == 1);
                return problem;
            }
        };
        return getJdbcTemplate().query(
                SELECT_SURVEY_PEER_REVIEW_BY_OPINION_AND_HORSEID,
                new Object[]{opinion, horseId},
                new int[]{INTEGER, INTEGER},
                mapper);
    }
    private static final String COMPLETE_SURVEY_PEER_REVIEW_BY_USER =
            "UPDATE survey_peer_review SET submit_time=? WHERE reviewer_user_id=? AND submit_time IS NULL";

    public void completeSurveyPeerReviewByUserId(int userId) {
        logger.debug("Update survey_peer_review by userId: " + userId);

        Object[] values = new Object[]{new Date(), userId};
        this.getJdbcTemplate().update(COMPLETE_SURVEY_PEER_REVIEW_BY_USER, values);
    }

    private static final String SELECT_SURVEY_PEER_REVIEW_BY_USER_AND_ASSIGNMENT =
            "SELECT DISTINCT spr.* FROM survey_peer_review spr, survey_answer sa, survey_content_object sco, task_assignment ta, horse "
            + "WHERE reviewer_user_id=? AND ta.id=? AND spr.survey_answer_id=sa.id AND sa.survey_content_object_id=sco.id AND sco.content_header_id=horse.content_header_id AND ta.horse_id=horse.id";

    public List<SurveyPeerReview> getPeerReviewsByAssignment(TaskAssignment taskAssignment) {
        return getPeerReviewsByUserAndAssignment(taskAssignment.getAssignedUserId(), taskAssignment.getId());
    }

    public List<SurveyPeerReview> getPeerReviewsByUserAndAssignment(int userId, int assignmentId) {
        return find(SELECT_SURVEY_PEER_REVIEW_BY_USER_AND_ASSIGNMENT, (long)userId, assignmentId);
    }


    public void completeSurveyPeerReviewByAssignment(TaskAssignment taskAssignment) {
        if (taskAssignment == null) return;

        logger.debug("Update survey_peer_review by TaskAssignment: " + taskAssignment.getId());

        List<SurveyPeerReview> list = getPeerReviewsByAssignment(taskAssignment);

        if (list == null || list.isEmpty()) return;

        for (SurveyPeerReview spr : list) {
            if (spr != null && spr.getSubmitTime() == null) {
                spr.setSubmitTime(new Date());
                update(spr);
            }
        }
    }
    //
    private static final String REMOVE_SURVEY_PEER_REVIEW_BY_USER_AND_ASSIGNMENT =
            "DELETE spr.* FROM survey_peer_review spr, survey_answer sa, survey_content_object sco, task_assignment ta, horse "
            + "WHERE spr.reviewer_user_id=? AND ta.id=? AND spr.survey_answer_id=sa.id AND sa.survey_content_object_id=sco.id AND sco.content_header_id=horse.content_header_id AND ta.horse_id=horse.id";

    public void removeSurveyPeerReviewByAssignment(TaskAssignment taskAssignment) {
        logger.debug(REMOVE_SURVEY_PEER_REVIEW_BY_USER_AND_ASSIGNMENT + ":====> " + taskAssignment.getId());

        Object[] values = new Object[]{taskAssignment.getAssignedUserId(), taskAssignment.getId()};
        this.getJdbcTemplate().update(REMOVE_SURVEY_PEER_REVIEW_BY_USER_AND_ASSIGNMENT, values);
    }

    public List<SurveyPeerReview> selectSurveyPeerReviewsByHorseId(int horseId) {
        return find(SELECT_SURVEY_PEER_REVIEWS_BY_HORSE_ID, horseId);
    }

    public List<SurveyPeerReview> selectSurveyPeerReviewsByProductId(int productId) {
        return find(SELECT_SURVEY_PEER_REVIEWS_BY_PRODUCT_ID, productId);
    }

 

    private static final String SELECT_BASIC_VIEWS_BY_USER_AND_ASSIGNMENT =
            "SELECT DISTINCT spr.*, sa.survey_question_id "
            + "FROM survey_peer_review spr, survey_answer sa, survey_content_object sco, task_assignment ta, horse "
            + "WHERE reviewer_user_id=? AND ta.id=? AND spr.survey_answer_id=sa.id AND sa.survey_content_object_id=sco.id AND sco.content_header_id=horse.content_header_id AND ta.horse_id=horse.id";


    public List<SurveyPeerReviewBasicView> getPeerReviewBasicViewsByUserAndAssignment(int userId, int assignmentId) {
        RowMapper mapper = new RowMapper() {

            public SurveyPeerReviewBasicView mapRow(ResultSet rs, int rowNum) throws SQLException {
                SurveyPeerReviewBasicView vo = new SurveyPeerReviewBasicView();
                vo.setQuestionId(ResultSetUtil.getInt(rs, "survey_question_id"));
                vo.setSurveyAnswerId(ResultSetUtil.getInt(rs, "survey_answer_id"));
                vo.setOpinion(ResultSetUtil.getShort(rs, "opinion"));
                vo.setSuggestedAnswerObjectId(ResultSetUtil.getInt(rs, "suggested_answer_object_id"));
                vo.setComments(ResultSetUtil.getString(rs, "comments"));
                vo.setReviewerUserId(ResultSetUtil.getInt(rs, "reviewer_user_id"));
                vo.setSubmitTime(ResultSetUtil.getDate(rs, "submit_time"));
                return vo;
            }
        };

        return getJdbcTemplate().query(
                SELECT_BASIC_VIEWS_BY_USER_AND_ASSIGNMENT,
                new Object[]{userId, assignmentId},
                new int[]{INTEGER, INTEGER},
                mapper);
    }
}
