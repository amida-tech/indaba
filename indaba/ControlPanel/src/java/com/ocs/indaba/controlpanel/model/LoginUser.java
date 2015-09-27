/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.model;

import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.Organization;
import com.ocs.indaba.po.User;
import java.text.MessageFormat;
import java.util.List;

/**
 *
 * @author Jeff
 */
public class LoginUser {
    private String token;
    private Checker checker;
    private User user;


    public LoginUser(User user) {
        this.user = user;
        this.checker = new Checker(user);
    }

    public int getLangId() {
        return user.getLanguageId();
    }

    public int getOrgId() {
        return user.getOrganizationId();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return user.getId();
    }


    public String getUsername() {
        return user.getUsername();
    }

    public boolean isSiteAdmin() {
        return user.getSiteAdmin() != 0;
    }

    public String getFirstname() {
        return user.getFirstName();
    }

    public String getLastname() {
        return user.getLastName();
    }

    public User getUser() {
        return user;
    }

    public Checker getChecker() {
        return checker;
    }

    public List<Organization> getAccessibleOrgs(int visibility) {
        return checker.getAccessibleOrgList(visibility);
    }

    public List<Integer> getAccessibleOrgIds(int visibility) {
        return checker.getAccessibleOrgIdList(visibility);
    }

    public Organization getOrg(int orgId) {
        return checker.getOrg(orgId);
    }

    private static String getMessage(String msgKey, int userLang) {
        String msg = Messages.getInstance().getMessage(msgKey, userLang);
        if (msg == null) msg = msgKey;
        return msg;
    }


    public String message(String key, Object... args) {
        return MessageFormat.format(getMessage(key, this.getLangId()), args);
    }

    public String message(String key) {
        return getMessage(key, this.getLangId());
    }

    @Override
    public String toString() {
        return "LoginUser{" + "token=" + token + ", user=" + user + '}';
    }
    
}
