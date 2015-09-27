/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.model;

import com.ocs.util.ValueObject;
/**
 *
 * @author rick
 */
public class OrganizationVO extends ValueObject {
 
    public OrganizationVO(int id, String name, String addr, String firstName, String lastName) {
        this.id = id;
        this.name = name;
        this.address = addr;
        this.firstName = firstName;
        this.lastName = lastName;
        this.superAdmin = this.firstName + " " + this.lastName;
    }
    public OrganizationVO(int id, String name, String addr, String url, boolean enforceSecurity, String firstName, String lastName, String email, String userName) {
        this.id = id;
        this.name = name;
        this.address = addr;
        this.url = url;
        this.enforceAPISecurity = enforceSecurity;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.superAdmin = this.firstName + " " + this.lastName;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setAddress(String addr) {
        this.address = addr;
    }
    public String getAddress() {
        return address;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl(){
        return url;
    }
    public void setEnforceAPISecurity(boolean enforce) {
        this.enforceAPISecurity = enforce;
    }
    public boolean isEnforceAPISecurity() {
        return enforceAPISecurity;
    }
    public void setSuperAdmin(String admin) {
        this.superAdmin = admin;
    }
    public String getSuperAdmin() {
        return this.superAdmin;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
    public String getEmail() {
        return this.email;
    }
    public String getUserName() {
        return this.userName;
    }
    private int id;
    private String name;
    private String address;
    private String url;
    private boolean enforceAPISecurity;
    private String superAdmin;
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
}
