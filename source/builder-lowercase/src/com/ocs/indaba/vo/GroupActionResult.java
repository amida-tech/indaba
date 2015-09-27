/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

import com.ocs.util.DateUtils;
import java.util.Date;

/**
 *
 * @author yc06x
 */
public class GroupActionResult {

    public static final short GROUP_STATE_NORMAL = 1;   // The group is normal
    public static final short GROUP_STATE_REMOVED = 2;  // the group no longer exists - remove the group from UI
    public static final short GROUP_STATE_DISABLED = 3; // the group is disabled - remove the group from UI
    public static final short GROUP_STATE_OUT_OF_SYNC = 4; // the state of the group on client is out of sync with server. Client must reload the group.

    private int code;
    private String msg;
    private short state;
    private long timestamp;     // DB check timestamp

    public short getState() {
        return this.state;
    }

    public void setState(short value) {
        this.state = value;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setTimestamp(long ts) {
        this.timestamp = ts;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public String getTimestampStr() {
        return DateUtils.format(new Date(timestamp));
    }
    public int getCode() {
        return this.code;
    }

    public void setCode(int value) {
        this.code = value;
    }
}
