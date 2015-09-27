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
public class NoteMembershipResult extends NoteActionResult {

    // Client Instructions:
    // Only show the member's displayName!
    List<CommMemberInfo> members;

    public void setMembers(List<CommMemberInfo> value) {
        this.members = value;
    }

    public List<CommMemberInfo> getMembers() {
        return this.members;
    }

}
