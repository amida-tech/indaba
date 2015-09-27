/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.po.Task;

/**
 *
 * @author Jeff
 */
public class TaskVO extends Task {
    private String assignMethod;
    private String roleNames;
    private int validity;
    private int userType;

    /*
     1 - not valid. Not all required fields are provided: tool, instructions
     2 - valid but not perfect: description not defined
     3- well defined
     */

    public String getAssignMethod() {
        return assignMethod;
    }

    public void setAssignMethod(String assignMethod) {
        this.assignMethod = assignMethod;
    }

   
    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public void setValidity(int v) {
        this.validity = v;
    }

    public int getValidity() {
        return this.validity;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int type) {
        this.userType = type;
    }

}
