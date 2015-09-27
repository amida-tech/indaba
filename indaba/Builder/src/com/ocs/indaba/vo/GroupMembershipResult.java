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
public class GroupMembershipResult extends GroupActionResult {
    
    private List<CommMemberInfo> members;

    public List<CommMemberInfo> getMembers() {
        return this.members;
    }

    public void setMembers(List<CommMemberInfo> members) {
        this.members = members;
    }

}
