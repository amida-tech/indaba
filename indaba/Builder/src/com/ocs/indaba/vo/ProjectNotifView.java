/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

import com.ocs.indaba.po.ProjectNotif;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class ProjectNotifView extends ProjectNotif {

    private String roleNames;
    private String languageName;
    private String typeName;
    private List<Integer> roleIds;

    public void setRoleNames(String value) {
        this.roleNames = value;
    }

    public String getRoleNames() {
        return this.roleNames;
    }

    public void setLanguageName(String value) {
        this.languageName = value;
    }

    public String getLanguageName() {
        return this.languageName;
    }

    public void setTypeName(String value) {
        this.typeName = value;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public List<Integer> getRoleIds() {
        return this.roleIds;
    }
}
