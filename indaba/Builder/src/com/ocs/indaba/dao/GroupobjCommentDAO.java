/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.dao;

import com.ocs.common.db.SmartDaoMySqlImpl;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.GroupobjComment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class GroupobjCommentDAO extends SmartDaoMySqlImpl<GroupobjComment, Integer>  {

    private static final String SELECT_ALL_BY_GROUPOBJ =
            "SELECT * FROM groupobj_comment WHERE groupobj_id=? AND create_time > ? ORDER BY create_time ASC";

    public List<GroupobjComment> getAllCommentsByGroupobj(int groupobjId, long timestamp) {
        String dateStr = (timestamp <= 0) ? "" : getTimeStampString(timestamp);
        return super.find(SELECT_ALL_BY_GROUPOBJ, groupobjId, dateStr);
    }


    private static final String SELECT_UNHIDDEN_BY_GROUPOBJ =
            "SELECT * FROM groupobj_comment WHERE groupobj_id=? AND create_time > ? AND delete_time IS NULL ORDER BY create_time ASC";

    public List<GroupobjComment> getUnhiddenCommentsByGroupobj(int groupobjId, long timestamp) {
        String dateStr = (timestamp <= 0) ? "" : getTimeStampString(timestamp);
        return super.find(SELECT_UNHIDDEN_BY_GROUPOBJ, groupobjId, dateStr);
    }


    private static final String HIDE_COMMENT =
            "UPDATE groupobj_comment SET delete_user_id=?, delete_time=NOW() WHERE id=?";

    public void hideComment(int commentId, int userId) {
        super.update(HIDE_COMMENT, userId, commentId);
    }


    private static final String UNHIDE_COMMENT =
            "UPDATE groupobj_comment SET delete_user_id=?, delete_time=NULL WHERE id=?";

    public void unhideComment(int commentId, int userId) {
        super.update(UNHIDE_COMMENT, userId, commentId);
    }


    private String getTimeStampString(long timestamp) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timestamp);
        return f.format(date);
    }

}
