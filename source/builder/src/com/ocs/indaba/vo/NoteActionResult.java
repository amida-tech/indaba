/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

/**
 *
 * @author yc06x
 */
public class NoteActionResult {

    public static final short NOTE_STATE_NORMAL = 1;   // The note is normal
    public static final short NOTE_STATE_REMOVED = 2;  // the note no longer exists - remove the group from UI
    public static final short NOTE_STATE_DISABLED = 3; // the note is disabled - remove the group from UI


    // Return code
    private int code;

    // If this msg is not null, always show the msg in a popup
    private String msg;

    // If the noteState is not normal, remove the note widget from the UI
    private short state;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public short getState() {
        return this.state;
    }

    public void setState(short value) {
        this.state = value;
    }


    public int getCode() {
        return this.code;
    }

    public void setCode(int value) {
        this.code = value;
    }
}
