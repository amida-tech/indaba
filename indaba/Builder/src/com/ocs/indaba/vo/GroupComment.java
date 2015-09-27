/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.common.Constants;
import com.ocs.util.DateUtils;
import java.util.Date;

/**
 *
 * @author yc06x
 */
public class GroupComment {

    public static final short COMMENT_TYPE_REGULAR = Constants.GROUP_COMMENT_TYPE_REGULAR;
    public static final short COMMENT_TYPE_SET_FLAG = Constants.GROUP_COMMENT_TYPE_SET_FLAG;
    public static final short COMMENT_TYPE_FLAG_RESPONSE = Constants.GROUP_COMMENT_TYPE_FLAG_RESPONSE;
    public static final short COMMENT_TYPE_UNSET_FLAG = Constants.GROUP_COMMENT_TYPE_UNSET_FLAG;
    public static final short COMMENT_STATE_NORMAL = 0;
    public static final short COMMENT_STATE_HIDDEN = 1;
    private short type;   // use this type to determine icon for the comment
    private String userDisplayName;
    private Date time;
    private String text;
    private int commentId;
    private short state;   // comment state. Show hide/unhide icon based on this, if user canManageComments.

    public void setType(short type) {
        this.type = type;
    }

    public short getType() {
        return this.type;
    }

    public void setState(short state) {
        this.state = state;
    }

    public short getState() {
        return this.state;
    }

    public void setUserDisplayName(String name) {
        this.userDisplayName = name;
    }

    public String getUserDisplayName() {
        return this.userDisplayName;
    }

    public void setTime(Date ts) {
        this.time = ts;
    }

    public Date getTime() {
        return this.time;
    }

    public String getTimeStr() {
       return DateUtils.format(time, "yyyy/MM/dd");
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setCommentId(int id) {
        this.commentId = id;
    }

    public int getCommentId() {
        return this.commentId;
    }
}
