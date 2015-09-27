/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

import com.ocs.indaba.po.Notedef;
import java.util.List;

/**
 *
 * @author ningshan
 */
public class NotedefView extends Notedef {
    private List<NotedefUserView> users;
    private List<NotedefRoleView> roles;

    public List<NotedefUserView> getUsers() {
        return users;
    }

    public void setUsers(List<NotedefUserView> users) {
        this.users = users;
    }

    public List<NotedefRoleView> getRoles() {
        return roles;
    }

    public void setRoles(List<NotedefRoleView> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "NotedefView{" + "users=" + users + ", roles=" + roles + '}';
    }

    
}
