/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

/**
 *
 * @author yc06x
 */
public class GroupobjUIState {

    public static final short FLAG_BUTTON_STATE_UNSET = 1;        // no standing flag - user can click to raise flag
    public static final short FLAG_BUTTON_STATE_SET_BY_ME = 2;  // flag set by the current user - user can click to resolve or reassign the flag
    public static final short FLAG_BUTTON_STATE_ASSIGNED_TO_ME = 3;  // flag assigned to the current user - not clickable
    public static final short FLAG_BUTTON_STATE_SET_OTHERS = 4;  // flag set but not by nor assigned to current user - not clickable
    public static final short FLAG_BUTTON_STATE_HIDE = 5;        // hide the flag button from the UI

    public static final short TEXT_BOX_STATE_REGULAR = 1;         // when user submits text, send a regular comment
    public static final short TEXT_BOX_STATE_FLAG_RESPONSE = 2;   // when user submits text, send a flag response comment

    public static final short TITLE_BAR_STATE_NO_FLAG = 1;   // no standing flag
    public static final short TITLE_BAR_STATE_HAS_FLAG = 2;   // has standing flag

    private short flagButtonState;
    private short textBoxState;
    private short titleBarState;

    public short getFlagButtonState() {
        return this.flagButtonState;
    }

    public void setFlagButtonState(short value) {
        this.flagButtonState = value;
    }

    public short getTextBoxState() {
        return this.textBoxState;
    }

    public void setTextBoxState(short value) {
        this.textBoxState = value;
    }


    public short getTitleBarState() {
        return this.titleBarState;
    }

    public void setTitleBarState(short value) {
        this.titleBarState = value;
    }

}
