/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.xo;

import com.ocs.indaba.po.Organization;
import com.ocs.indaba.po.StudyPeriod;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class Project {

    private String id;
    private List<Organization> orgs = new ArrayList<Organization>();
    private String projectName;
    private String description = null;
    private Date startTime;
    private Date closeTime = null;
    private short visibility;

    private User adminUser;
    private StudyPeriod studyPeriod;

    private int dboId;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void addOrg(Organization org) {
        orgs.add(org);
    }

    public List<Organization> getOrgs() {
        return orgs;
    }

    public void setName(String name) {
        this.projectName = name;
    }

    public String getName() {
        return this.projectName;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return this.description;
    }

    public void setStartTime(Date t) {
        this.startTime = t;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public void setCloseTime(Date t) {
        this.closeTime = t;
    }

    public Date getCloseTime() {
        return this.closeTime;
    }

    public void setVisibility(short v) {
        this.visibility = v;
    }

    public short getVisibility() {
        return this.visibility;
    }

    public void setAdminUser(User u) {
        this.adminUser = u;
    }

    public User getAdminUser() {
        return this.adminUser;
    }

    public void setDboId(int id) {
        this.dboId = id;
    }

    public int getDboId() {
        return this.dboId;
    }

    public void setStudyPeriod(StudyPeriod sp) {
        this.studyPeriod = sp;
    }

    public StudyPeriod getStudyPeriod() {
        return this.studyPeriod;
    }
}
