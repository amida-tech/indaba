/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

/**
 *
 * @author yc06x
 */
public class GroupobjView {

    private int objId;
    private String name;
    private String description;
    private GroupobjUIState uiState;

    // whether the user can manage (hide/unhide) comments
    // If user canManageComments, then each comment must have a hide/unhide icon in the comment list.
    private boolean canManageComments;

    public int getObjId() {
        return this.objId;
    }

    public void setObjId(int value) {
        this.objId = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public GroupobjUIState getUIState() {
        return this.uiState;
    }

    public void setUIState(GroupobjUIState uiState) {
        this.uiState = uiState;
    }

    public boolean getCanManageComments() {
        return this.canManageComments;
    }

    public void setCanManageComments(boolean value) {
        this.canManageComments = value;
    }
}
