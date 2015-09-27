/*
 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.Message;
import com.ocs.indaba.util.Page;
import java.util.Date;

/**
 *
 * @author Tiger Tang
 */
public class MessageDAO extends SmartDaoMySqlImpl<Message, Integer> {

    private static final Logger log = Logger.getLogger(MessageDAO.class);
    private static final String SELECT_ALL_MESSAGES_BY_MSGBOARD_ID = "SELECT * FROM message WHERE msgboard_id = ?";
    private static final String SELECT_ACTIVE_MESSAGES_BY_MSGBOARD_ID = "SELECT * FROM message WHERE msgboard_id = ? AND delete_user_id IS NULL";
//  public static final String SELECT_USER_MESSAGE_WITH_READINGSTATUS_BY_MSGBOARDID = "SELECT t.*, mrs.timestamp mrs_timestamp FROM (SELECT m.*, u.id AS uid FROM message m, user u WHERE m.msgboard_id = ?) AS t LEFT OUTER JOIN msg_reading_status mrs ON mrs.user_id = t.uid AND mrs.message_id = t.id WHERE uid = ? ORDER BY t.id desc";
//  public static final String SELECT_NEW_MESSAGE_BY_MSGBOARD = "SELECT message.*, msg_reading_status.timestamp AS mrs_timestamp FROM msg_reading_status RIGHT OUTER JOIN message ON message.id = msg_reading_status.message_id JOIN msgboard ON message.msgboard_id = msgboard.id LEFT OUTER JOIN user ON message.author_user_id = user.id AND msg_reading_status.user_id = user.id WHERE msg_reading_status.timestamp is null AND message.msgboard_id = ? ORDER BY message.id DESC";
    private static final String SELECT_STAFF_AUTHOR_MSG_LIST_BY_HORSE_ID = "SELECT m.* FROM survey_answer sa, message m "
            + "WHERE sa.survey_content_object_id IN (SELECT content_object_id FROM content_header WHERE horse_id=?) AND "
            + "m.msgboard_id=sa.staff_author_msgboard_id";
    private static final String SELECT_INTERNAL_MSG_LIST_BY_HORSE_ID = "SELECT m.* FROM survey_answer sa, message m "
            + "WHERE sa.survey_content_object_id IN (SELECT content_object_id FROM content_header WHERE horse_id=?) AND "
            + "m.msgboard_id=sa.internal_msgboard_id";

    private static final String COUNT_OF_UNREADED_OUTBOX_MESSAGES =
            "SELECT count(1) FROM user u, message m USE INDEX (fk_message_author) WHERE m.author_user_id=? "
            + "AND u.msgboard_id=m.msgboard_id AND m.id NOT IN "
            + "(SELECT m.id FROM user u, message m USE INDEX (fk_message_author), msg_reading_status mrs "
            + "WHERE m.author_user_id=? AND u.msgboard_id=m.msgboard_id "
            + "AND mrs.message_id=m.id AND mrs.user_id=m.author_user_id)";

    private static final String UPDATE_MESSAGE_STATUS = "UPDATE message SET delete_user_id=?, delete_time=? WHERE id=?";

    public void deleteMessage(int msgId, int userId) {
        super.update(UPDATE_MESSAGE_STATUS, userId, new Date(), msgId);
    }

    public void undeleteMessage(int msgId) {
        super.update(UPDATE_MESSAGE_STATUS, null, null, msgId);
    }

    public Page<Message> getInboxMessageByMsgboard(int msgBoardId, int pageNumber, int pageSize) {
        if (pageNumber == 0) {
            pageNumber = 1;
            logger.debug("PageNumber is of invalid value zero, change it to DEFAULT value 1");
        }

        log.debug("Select messages in page by userid and msgBoardId: [msgBoardId=" + msgBoardId + ", pageNumber=" + pageNumber + ", pageSize=" + pageSize + "]");
        return find("SELECT * FROM message WHERE msgboard_id = ? ORDER BY id DESC", pageNumber, pageSize, new Object[]{msgBoardId});
    }

    public Page<Message> getOutboxMessageByUserId(int userId, int pageNumber, int pageSize) {
        if (pageNumber == 0) {
            pageNumber = 1;
            logger.debug("PageNumber is of invalid value zero, change it to DEFAULT value 1");
        }

        log.debug("Select messages in page by userid and msgBoardId: [userid=" + userId + ", pageNumber=" + pageNumber + ", pageSize=" + pageSize + "]");
        return find("SELECT m.* FROM user u, message m USE INDEX (fk_message_author) WHERE m.author_user_id = ? AND u.msgboard_id=m.msgboard_id ORDER BY m.id DESC", pageNumber, pageSize, new Object[]{userId});
    }

    public Page<Message> getInboxProjectMessageByMsgboard(int msgBoardId, int pageNumber, int pageSize) {
        log.debug("Select messages in page by userid and msgBoardId: [msgBoardId=" + msgBoardId + ", pageNumber=" + pageNumber + ", pageSize=" + pageSize + "]");

        return find("SELECT * FROM message WHERE msgboard_id = ? ORDER BY id DESC", pageNumber, pageSize, new Object[]{msgBoardId});
    }

    public Integer getPreviousMsgId(Message msg, int msgboardId) {
        final Message prevMsg = findSingle("SELECT id FROM message WHERE msgboard_id = ? AND id < ? ORDER BY id DESC LIMIT 0, 1", msgboardId, msg.getId());
        return prevMsg == null ? null : prevMsg.getId();
    }

    public Integer getNextMsgId(Message msg, int msgboardId) {
        final Message nextMsg = findSingle("SELECT id FROM message WHERE msgboard_id = ? AND id > ? ORDER BY id ASC LIMIT 0, 1", msgboardId, msg.getId());
        return nextMsg == null ? null : nextMsg.getId();
    }

    public Integer getPreviousOutMsgId(Message msg, int userId) {
        final Message prevMsg = findSingle("SELECT m.id FROM message m, user u WHERE m.author_user_id=? AND u.msgboard_id=m.msgboard_id AND m.id < ? ORDER BY m.id DESC LIMIT 0, 1", userId, msg.getId());
        return prevMsg == null ? null : prevMsg.getId();
    }

    public Integer getNextOutMsgId(Message msg, int userId) {
        final Message nextMsg = findSingle("SELECT m.id FROM message m, user u WHERE m.author_user_id=? AND u.msgboard_id=m.msgboard_id AND m.id > ? ORDER BY m.id ASC LIMIT 0, 1", userId, msg.getId());
        return nextMsg == null ? null : nextMsg.getId();
    }

    public long getInboxNewMessageCountByMsgboard(int uid, int msgboardId) {
        // return count("select count(1) from (SELECT m.*, u.id AS uid FROM message m, user u WHERE m.msgboard_id = ?) AS t LEFT OUTER JOIN msg_reading_status mrs ON mrs.user_id = t.uid AND mrs.message_id = t.id WHERE uid = ? AND mrs.user_id IS NULL", msgboardId, uid);
        return getInboxMessageCountByMsgboard(msgboardId) - getOldMessageCountByMsgboard(uid, msgboardId);
    }

    public long getOldMessageCountByMsgboard(int uid, int msgboardId) {
        return count("select count(*) from message m, msg_reading_status mrs WHERE m.msgboard_id = ? AND mrs.user_id = ? AND mrs.message_id = m.id", msgboardId, uid);
    }

    /**
     * public long getNewMessageCountByMsgboard(int msgboardId) { return
     * count("SELECT COUNT(1) FROM message m LEFT OUTER JOIN msg_reading_status
     * mrs ON m.id = mrs.message_id WHERE m.msgboard_id = ? AND mrs.timestamp IS
     * NULL;", msgboardId); } **
     */
    public long getOutboxNewMessageCountByUserId(int uid) {
         return count(COUNT_OF_UNREADED_OUTBOX_MESSAGES, uid, uid);
    }

    public long getInboxMessageCountByMsgboard(int msgboardId) {
        return count("SELECT COUNT(1) FROM message WHERE msgboard_id = ?", msgboardId);
    }

    public long getOutboxMessageCountByUserId(int userId) {
        return count("SELECT COUNT(1) FROM user u, message m USE INDEX (fk_message_author) WHERE m.author_user_id = ? AND u.msgboard_id=m.msgboard_id ORDER BY m.id", userId);
    }

    public Page<Message> getMessagesByMessageBoardId(int userMsgboardId, int pageNumber, int pageSize) {
        return find(SELECT_ALL_MESSAGES_BY_MSGBOARD_ID, pageNumber, pageSize, new Object[]{userMsgboardId});
    }

    private List<Message> getMessagesByMessageBoardId(String sqlStr, int userMsgboardId, int authorId, String sortColumn, String sortOrder) {
        StringBuilder query = new StringBuilder(sqlStr);

        List<Object> params = new ArrayList<Object>(2);
        params.add(userMsgboardId);

        if (authorId != Constants.INVALID_INT_ID) {
            query.append(" AND author_user_id = ? ");
            params.add(authorId);
        }

        if ("title".equalsIgnoreCase(sortColumn)) {
            query.append(" ORDER BY title ");
        } else {
            query.append(" ORDER BY id ");
        }

        if ("asc".equalsIgnoreCase(sortOrder)) {
            query.append(" ASC ");
        } else {
            query.append(" DESC ");
        }

        return find(query.toString(), params.toArray());
    }

    public List<Message> getAllMessagesByMessageBoardId(int userMsgboardId, int authorId, String sortColumn, String sortOrder) {
        return getMessagesByMessageBoardId(SELECT_ALL_MESSAGES_BY_MSGBOARD_ID, userMsgboardId, authorId, sortColumn, sortOrder);
    }

    public List<Message> getActiveMessagesByMessageBoardId(int userMsgboardId, int authorId, String sortColumn, String sortOrder) {
        return getMessagesByMessageBoardId(SELECT_ACTIVE_MESSAGES_BY_MSGBOARD_ID, userMsgboardId, authorId, sortColumn, sortOrder);
    }

    public List<Message> selectStaffAuthorMessagesByHorseId(int horseId) {
        return super.find(SELECT_STAFF_AUTHOR_MSG_LIST_BY_HORSE_ID, horseId);
    }

    public List<Message> selectInternalMessagesByHorseId(int horseId) {
        return super.find(SELECT_INTERNAL_MSG_LIST_BY_HORSE_ID, horseId);
    }
}
