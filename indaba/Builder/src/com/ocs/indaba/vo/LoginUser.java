/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.vo;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.po.Project;
import java.text.MessageFormat;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class LoginUser {

    private String token = null;
    private int uid = Constants.INVALID_INT_ID;
    private String username = null;
    private String name = null;
    private int prjid = Constants.INVALID_INT_ID;
    private String prjName = null;
    private int languageId;
    private int siteAdmin;
    private List<Project> listProj;
    private Project project;

    public Project getProject() {
        return project;
    }

    public void setProject(Project p) {
        project = p;
    }

    public List<Project> getListProj() {
        return listProj;
    }

    public void setListProj(List<Project> listProj) {
        this.listProj = listProj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrjName() {
        return prjName;
    }

    public void setPrjName(String prjName) {
        this.prjName = prjName;
    }

    public int getPrjid() {
        return prjid;
    }

    public void setPrjid(int prjid) {
        this.prjid = prjid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public int getSiteAdmin() {
        return siteAdmin;
    }

    public void setSiteAdmin(int siteAdmin) {
        this.siteAdmin = siteAdmin;
    }

    public boolean isSiteAdmin() {
        return (siteAdmin == 1);
    }

    public String getMessage(String key, Object... arguments) {
        return MessageFormat.format(Messages.getInstance().getMessage(key, this.languageId), arguments);
    }
}
