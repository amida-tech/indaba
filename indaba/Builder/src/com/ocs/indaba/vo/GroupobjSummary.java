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
public class GroupobjSummary extends GroupActionResult {

    List<GroupobjView> groups;

    public void setGroups(List<GroupobjView> groups) {
        this.groups = groups;
    }

    public List<GroupobjView> getGroups() {
        return this.groups;
    }

}
