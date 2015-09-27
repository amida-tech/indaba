/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.builder.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Message;
import java.util.List;

/**
 *
 * @author Jeff
 */
public class MessageDAO extends SmartDaoMySqlImpl<Message, Integer> {

    private static final String SELECT_MESSAGE_BY_MSGBOARD_ID = "SELECT * FROM message WHERE msgboard_id = ?";

    private static final String SELECT_STAFF_AUTHOR_MSG_LIST_BY_HORSE_ID = "SELECT m.* FROM survey_answer sa, message m "
            + "WHERE sa.survey_content_object_id IN (SELECT content_object_id FROM content_header WHERE horse_id=?) AND "
            + "m.msgboard_id=sa.staff_author_msgboard_id";

    private static final String SELECT_INTERNAL_MSG_LIST_BY_HORSE_ID = "SELECT m.* FROM survey_answer sa, message m "
            + "WHERE sa.survey_content_object_id IN (SELECT content_object_id FROM content_header WHERE horse_id=?) AND "
            + "m.msgboard_id=sa.internal_msgboard_id";

    private static final String SELECT_STAFF_AUTHOR_MSG_LIST_BY_PRODUCT_ID = "SELECT m.* FROM survey_answer sa, message m, content_header ch, horse "
            + "WHERE sa.survey_content_object_id=ch.content_object_id AND horse.content_header_id=ch.id AND horse.product_id=? "
            + "AND m.msgboard_id=sa.staff_author_msgboard_id";

    private static final String SELECT_INTERNAL_MSG_LIST_BY_PRODUCT_ID = "SELECT m.* FROM survey_answer sa, message m, content_header ch, horse "
            + "WHERE sa.survey_content_object_id=ch.content_object_id AND horse.content_header_id=ch.id AND horse.product_id=? "
            + "AND m.msgboard_id=sa.internal_msgboard_id";


    public List<Message> selectMessagesByMsgBoardId(int msgBoardId) {
        return super.find(SELECT_MESSAGE_BY_MSGBOARD_ID, msgBoardId);
    }

    public List<Message> selectStaffAuthorMessagesByHorseId(int horseId) {
        return super.find(SELECT_STAFF_AUTHOR_MSG_LIST_BY_HORSE_ID, horseId);
    }

    public List<Message> selectInternalMessagesByHorseId(int horseId) {
        return super.find(SELECT_INTERNAL_MSG_LIST_BY_HORSE_ID, horseId);
    }

    public List<Message> selectStaffAuthorMessagesByProductId(int productId) {
        return super.find(SELECT_STAFF_AUTHOR_MSG_LIST_BY_PRODUCT_ID, productId);
    }

    public List<Message> selectInternalMessagesByProductId(int productId) {
        return super.find(SELECT_INTERNAL_MSG_LIST_BY_PRODUCT_ID, productId);
    }

    private static final String SELECT_PEER_REVIEW_MSG_LIST_BY_PRODUCT_ID =
            "SELECT m.* FROM survey_answer sa, message m, content_header ch, horse, survey_peer_review spr "
            + "WHERE sa.survey_content_object_id=ch.content_object_id AND horse.content_header_id=ch.id AND horse.product_id=? "
            + "AND m.msgboard_id=spr.msgboard_id AND spr.survey_answer_id = sa.id";

    public List<Message> selectPeerReviewMessagesByProductId(int productId) {
        return super.find(SELECT_PEER_REVIEW_MSG_LIST_BY_PRODUCT_ID, productId);
    }
}
