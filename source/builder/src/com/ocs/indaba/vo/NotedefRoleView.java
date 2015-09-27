/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

import com.ocs.indaba.po.NotedefRole;

/**
 *
 * @author ningshan
 */
public class NotedefRoleView extends NotedefRole {
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "NotedefRoleView{" + "roleName=" + roleName + '}';
    }
    
}
