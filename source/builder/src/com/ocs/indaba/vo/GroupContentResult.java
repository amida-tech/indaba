/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

import java.util.List;

/**
 *
 * @author yc06x
 */
public class GroupContentResult extends GroupActionResult {

    private GroupobjUIState uiState;
    private List<GroupComment> comments;
    private int flagId;   // the ID of standing flag, if any

    public GroupobjUIState getUIState() {
        return this.uiState;
    }

    public void setUIState(GroupobjUIState state) {
        this.uiState = state;
    }

    public List<GroupComment> getComments() {
        return this.comments;
    }

    public void setComments(List<GroupComment> comments) {
        this.comments = comments;
    }

    public int getFlagId() {
        return this.flagId;
    }

    public void setFlagId(int flagId) {
        this.flagId = flagId;
    }

}
