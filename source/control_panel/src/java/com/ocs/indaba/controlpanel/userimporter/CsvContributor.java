/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.controlpanel.userimporter;

/**
 *
 * @author yc06x
 */
public class CsvContributor {

    private String email;
    private String firstName;
    private String lastName;
    private String userName;
    private int roleId;

    public CsvContributor(String email, String firstName, String lastName, int roleId) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = null;
        this.roleId = roleId;
    }

    public CsvContributor(String email, String firstName, String lastName, String userName, int roleId) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.roleId = roleId;
    }


    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRoleId() {
        return this.roleId;
    }
}
