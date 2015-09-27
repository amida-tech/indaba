/**
 * 
 */
package com.ocs.indaba.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.JournalPeerReview;
import com.ocs.indaba.po.TaskAssignment;

/**
 * @author Tiger Tang
 *
 */
public class JournalPeerReviewDAO extends SmartDaoMySqlImpl<JournalPeerReview, Integer> {

    @SuppressWarnings("unchecked")
    public JournalPeerReview getPeerReviewsByJournalContentAndUserId(int contentObjectId, int userId) {
        List params = new ArrayList();
        params.add(contentObjectId);
        params.add(userId);
        return findSingle("SELECT * FROM journal_peer_review WHERE journal_content_object_id = ? AND reviewer_user_id = ?", params.toArray());
    }

    public List<JournalPeerReview> getSubmittedPeerReviewsByJournalContent(int contentObjectId) {
        return find("SELECT * FROM journal_peer_review WHERE submit_time IS NOT NULL AND journal_content_object_id = ?", contentObjectId);
    }

    public List<JournalPeerReview> getPeerReviewsByJournalContent(int contentObjectId) {
        return find("SELECT * FROM journal_peer_review WHERE journal_content_object_id = ?", contentObjectId);
    }

    private static final String COMPLETE_JOURNAL_PEER_REVIEW_BY_USER =
            "UPDATE journal_peer_review SET submit_time=? WHERE reviewer_user_id=? AND submit_time IS NULL";

    public void completeJournalPeerReviewByUserId(int userId) {
        logger.debug("Update journal_peer_review by userId: " + userId);

        Object[] values = new Object[]{new Date(), userId};
        this.getJdbcTemplate().update(COMPLETE_JOURNAL_PEER_REVIEW_BY_USER, values);
    }

    private static final String SELECT_JOURNAL_PEER_REVIEW_BY_USER_AND_ASSIGNMENT =
            "SELECT DISTINCT jpr.* FROM journal_peer_review jpr, journal_content_object jco, task_assignment ta, horse " +
            "WHERE reviewer_user_id=? AND ta.id=? AND jpr.`journal_content_object_id`=jco.id AND jco.content_header_id=horse.content_header_id AND ta.horse_id=horse.id";

    public JournalPeerReview getPeerReviewByAssignment(TaskAssignment taskAssignment) {
        List params = new ArrayList();
        params.add(taskAssignment.getAssignedUserId());
        params.add(taskAssignment.getId());
        return findSingle(SELECT_JOURNAL_PEER_REVIEW_BY_USER_AND_ASSIGNMENT, params.toArray());
    }

    public void completeJournalPeerReviewByAssignment(TaskAssignment taskAssignment) {
        logger.debug("Update journal_peer_review by TaskAssignment: " + taskAssignment.getId());

        JournalPeerReview jpr = getPeerReviewByAssignment(taskAssignment);
        if (jpr != null && jpr.getSubmitTime() == null) {
            jpr.setSubmitTime(new Date());
            update(jpr);
        }
    }

    private static final String REMOVE_JOURNAL_PEER_REVIEW_BY_USER_AND_ASSIGNMENT =
            "DELETE jpr.* FROM journal_peer_review jpr, journal_content_object jco, task_assignment ta, horse " +
            "WHERE jpr.reviewer_user_id=? AND ta.id=? AND jpr.journal_content_object_id=jco.id AND jco.content_header_id=horse.content_header_id AND ta.horse_id=horse.id";

    public void removeJournalPeerReviewByAssignment(TaskAssignment taskAssignment) {
        logger.debug(REMOVE_JOURNAL_PEER_REVIEW_BY_USER_AND_ASSIGNMENT + ":====> " + taskAssignment.getId());

        Object[] values = new Object[]{taskAssignment.getAssignedUserId(), taskAssignment.getId()};
        this.getJdbcTemplate().update(REMOVE_JOURNAL_PEER_REVIEW_BY_USER_AND_ASSIGNMENT, values);
    }

}
