/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.vo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jeff
 */
public class ProjectInfo {
    
    private int prjId = 0;
    private String prjName = "";
    private String primaryOrgName = "";
    private String secondaryOrgNames = "";
    private int ownerUserId = 0;
    private int primaryAdminUserId = 0;          // primary admin
    private String primaryAdminFullName = "";
    private String secondaryAdminFullNames = "";
    
    public ProjectInfo() {
    }
    
    public ProjectInfo(int prjId, String prjName) {
        this.prjId = prjId;
        this.prjName = prjName;
    }
    
    public int getPrjId() {
        return prjId;
    }
    
    public void setPrjId(int prjId) {
        this.prjId = prjId;
    }
    
    public String getPrjName() {
        return prjName;
    }
    
    public void setPrjName(String prjName) {
        this.prjName = prjName;
    }
    
    public String getPrimaryOrgName() {
        return primaryOrgName;
    }
    
    public void setPrimaryOrgName(String orgName) {
        this.primaryOrgName = orgName;
    }

    public String getSecondaryOrgNames() {
        return secondaryOrgNames;
    }

    public void setSecondaryOrgNames(String names) {
        this.secondaryOrgNames = names;
    }
    
    public int getOwnerUserId() {
        return ownerUserId;
    }
    
    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }
    

    public void setPrimaryAdminUID(int uid) {
        this.primaryAdminUserId = uid;
    }

    public int getPrimaryAdminUID() {
        return this.primaryAdminUserId;
    }

    public void setPrimaryAdminFullName(String name) {
        this.primaryAdminFullName = name;
    }

    public String getPrimaryAdminFullName() {
        return this.primaryAdminFullName;
    }

    public int getPrimaryAdminUserId() {
        return primaryAdminUserId;
    }

    public void setPrimaryAdminUserId(int primaryAdminUserId) {
        this.primaryAdminUserId = primaryAdminUserId;
    }

    public void setSecondaryAdminFullNames(String names) {
        this.secondaryAdminFullNames = names;
    }

    public String getSecondaryAdminFullNames() {
        return this.secondaryAdminFullNames;
    }

    @Override
    public String toString() {
        return "ProjectInfo{" + "prjId=" + prjId + ", prjName=" + prjName + ", primaryOrgName=" + primaryOrgName + '}';
    }
}
