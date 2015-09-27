/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

import com.ocs.indaba.po.User;

/**
 *
 * @author yc06x
 */
public class ProjectUserView extends User {

    private int projectId;
    private int roleId;

    public void setProjectId(Integer projectId) {
        if (projectId == null) projectId = 0;
        this.projectId = projectId;
    }

    public int getProjectId() {
        return this.projectId;
    }

    public void setRoleId(Integer roleId) {
        if (roleId == null) roleId = 0;
        this.roleId = roleId;
    }

    public int getRoleId() {
        return this.roleId;
    }
    

}
