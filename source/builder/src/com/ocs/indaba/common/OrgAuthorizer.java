/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.common;

import com.ocs.indaba.dao.OrganizationDAO;
import com.ocs.indaba.po.Organization;
import com.ocs.indaba.po.User;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author seanpcheng
 */

@Component
public class OrgAuthorizer {
    private User user = null;
    private List<Organization> fullOrgList = null;
    private List<Organization> accessibleOrgList = null;

    private static OrganizationDAO orgDao = null;

    @Autowired
    public void setOrganizationDAO(OrganizationDAO dao) {
        orgDao = dao;
    }

    // must have this dummy constructor to make autowire work
    public OrgAuthorizer() {}

    public OrgAuthorizer(User user) {
        this.user = user;
        buildOrgList();
    }

    public boolean isSiteAdmin() {
        return user.getSiteAdmin() != 0;
    }

    public boolean hasOrgAuthority(int orgId) {
        if (user == null) return false;

        if (user.getSiteAdmin() != 0) return true;

        if (orgId <= 0) return false;   // invalid org

        if (accessibleOrgList == null) return false;

        for (Organization org : accessibleOrgList) {
            if (org.getId() == orgId) return true;
        }

        return false;
    }


    public boolean hasAnyOrgAuthority() {
        if (user == null) return false;
        if (user.getSiteAdmin() != 0) return true;
        return accessibleOrgList != null;
    }


    public List<Organization> getAccessibleOrgList(int visibility) {
        return (visibility == Constants.VISIBILITY_PUBLIC) ? fullOrgList : accessibleOrgList;
    }


    /**
     * Build the list orgs that the user has access to
     * @return
     */

    private void buildOrgList() {
        fullOrgList = orgDao.selectAllOrgs();

        if (user == null) return;
        
        if (user.getSiteAdmin() != 0) {
            accessibleOrgList = fullOrgList;
        } else {
            addAccessibleOrgs(orgDao.selectOrgIdsByUserIdFromOrg(user.getId()));
            addAccessibleOrgs(orgDao.selectOrgIdsByUserIdFromOrgAdmin(user.getId()));
        }
    }


    private void addAccessibleOrgs(List<Integer> orgIds) {
        if (orgIds == null) return;
        for (int orgId : orgIds) {
            Organization org = getOrg(orgId);
            if (org != null) {
                if (accessibleOrgList == null) {
                    accessibleOrgList = new ArrayList<Organization>();
                }

                if (!accessibleOrgList.contains(org)) {
                    accessibleOrgList.add(org);
                }
            }
        }
    }

    public Organization getOrg(int orgId) {
        for (Organization org : fullOrgList) {
            if (org.getId() == orgId) return org;
        }

        return null;
    }


    public List<Integer> getAccessibleOrgIdList(int visibility) {
        List<Organization> orgs = getAccessibleOrgList(visibility);
        
        if (orgs == null) return null;

        ArrayList<Integer> idList = new ArrayList<Integer>();

        for (Organization org : orgs) {
            idList.add(org.getId());
        }

        return idList;
    }
}
