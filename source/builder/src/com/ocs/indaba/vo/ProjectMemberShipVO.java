/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

/**
 *
 * @author Jeff
 */
public class ProjectMemberShipVO {

    private int id;
    private int userId;
    private String firstName;
    private String lastName;
    private String displayUserName;
    private int roleId;
    private String roleName;
    private int projectId;
    private String email;
    private String userName;
    private short status;

    public String getDisplayUserName() {
        return displayUserName;
    }

    public void setDisplayUserName(String displayUserName) {
        this.displayUserName = displayUserName;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public short getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return "ProjectMemberShipVO{" + "id=" + id + ", userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", displayUserName=" + displayUserName + ", roleId=" + roleId + ", roleName=" + roleName + ", projectId=" + projectId + '}';
    }
}
