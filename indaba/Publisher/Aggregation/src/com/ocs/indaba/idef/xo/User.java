/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.xo;

/**
 *
 * @author yc06x
 */
public class User {

    private String id;
    private String userName = null;
    private String email;
    private String firstName;
    private String lastName;
    private String phone = null;
    private String cell = null;
    private String address = null;
    private String bio = null;

    private int dboId;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setLastName(String name) {
        this.lastName = name;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setPhone(String v) {
        this.phone = v;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setCell(String v) {
        this.cell = v;
    }

    public String getCell() {
        return this.cell;
    }

    public void setAddress(String v) {
        this.address = v;
    }

    public String getAddress() {
        return this.address;
    }

    public void setBio(String v) {
        this.bio = v;
    }

    public String getBio() {
        return this.bio;
    }

    public void setDboId(int id) {
        this.dboId = id;
    }

    public int getDboId() {
        return this.dboId;
    }
}
