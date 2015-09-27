/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.controlpanel.model;

import com.ocs.indaba.vo.ProjectInfo;
import com.ocs.util.ValueObject;

/**
 *
 * @author Jeff Jiang 
 */
public class ProjectVO extends ValueObject {

    private int id;
    private String orgName;
    private String secondaryOrgNames;
    private String name;
    private String displayAdminUsers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getSecondaryOrgNames() {
        return secondaryOrgNames;
    }

    public void setSecondaryOrgNames(String names) {
        this.secondaryOrgNames = names;
    }

    /*
     * public List<String> getAdminUsers() { return adminUsers; }
     *
     * public void addAdminUser(String adminUser) { if (this.adminUsers == null)
     * { this.adminUsers = new ArrayList<String>(); }
     * this.adminUsers.add(adminUser); }
     *
     * public void setAdminUsers(List<String> admins) { this.adminUsers =
     * admins; }
     */

    public String getDisplayAdminUsers() {
        return displayAdminUsers;
    }

    public void setDisplayAdminUsers(String displayAdminUsers) {
        this.displayAdminUsers = displayAdminUsers;
    }

    public static ProjectVO initWithProjectInfo(ProjectInfo pInfo) {
        ProjectVO proj = new ProjectVO();
        proj.setName(pInfo.getPrjName());
        proj.setOrgName(pInfo.getPrimaryOrgName());
        proj.setId(pInfo.getPrjId());        
        proj.setDisplayAdminUsers(pInfo.getPrimaryAdminFullName());
        proj.setSecondaryOrgNames(pInfo.getSecondaryOrgNames());
        return proj;
    }

    @Override
    public String toString() {
        return "ProjectVO{" + "id=" + id + ", orgName=" + orgName + ", name=" + name + ", displayAdminUsers=" + displayAdminUsers + '}';
    }
}
