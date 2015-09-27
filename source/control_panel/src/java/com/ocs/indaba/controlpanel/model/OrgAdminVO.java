/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.model;
import com.ocs.util.ValueObject;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
/**
 *
 * @author rick
 */
public class OrgAdminVO extends ValueObject {
    private static final Logger logger = Logger.getLogger(OrgAdminVO.class);
    private int id;
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return this.id;
    }
    private String firstName;
    public void setFirstName(String name) {
        this.firstName = name;
    }
    public String getFirstName() {
        return this.firstName;
    }
    private String lastName;
    public void setLastName(String name) {
        this.lastName = name;
    }
    public String getLastName() {
        return this.lastName;
    }
    public String getName() {
        return this.firstName + " " + this.lastName;
    }
    private String userName;
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return this.userName;
    }
    private String email;
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return this.email;
    }

    private String actions = "";
    public void setActions(String v) {
        this.actions = v;
    }
    public String getActions() {
        return this.actions;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jObj = new JSONObject();
        jObj.put(ATTR_ID, id);
        jObj.put(ATTR_FIRST_NAME, this.firstName);
        jObj.put(ATTR_LAST_NAME, this.lastName);
        jObj.put(ATTR_NAME, this.getName());
        jObj.put(ATTR_USER_NAME, this.userName);
        jObj.put(ATTR_EMAIL, this.email);
        return jObj;
    }
    private static String ATTR_FIRST_NAME = "first_name";
    private static String ATTR_LAST_NAME = "last_name";
    private static String ATTR_NAME = "name";
    private static String ATTR_USER_NAME = "username";
    private static String ATTR_EMAIL = "email";
    private static String ATTR_ID = "id";
}