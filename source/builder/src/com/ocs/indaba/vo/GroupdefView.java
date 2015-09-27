/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

import com.ocs.indaba.po.Groupdef;
import java.util.List;

/**
 *
 * @author ningshan
 */
public class GroupdefView extends Groupdef {
    List<GroupdefUserView> users;
    List<GroupdefRoleView> roles;

    public List<GroupdefUserView> getUsers() {
        return users;
    }

    public void setUsers(List<GroupdefUserView> users) {
        this.users = users;
    }

    public List<GroupdefRoleView> getRoles() {
        return roles;
    }

    public void setRoles(List<GroupdefRoleView> roles) {
        this.roles = roles;
    }
    
}
