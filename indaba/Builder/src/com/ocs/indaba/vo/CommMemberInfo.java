/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

/**
 *
 * @author yc06x
 */
public class CommMemberInfo {

    private int userId;
    private String displayName;
    private String roleName;
    private int roleId;
    private String firstName;
    private String lastName;
    private String userName;

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int value) {
        this.userId = value;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String value) {
        this.displayName = value;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String value) {
        this.roleName = value;
    }

    public int getRoleId() {
        return this.roleId;
    }

    public void setRoleId(int value) {
        this.roleId = value;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String value) {
        this.firstName = value;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String value) {
        this.lastName = value;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String value) {
        this.userName = value;
    }

}
