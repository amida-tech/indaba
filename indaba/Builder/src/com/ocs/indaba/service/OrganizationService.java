/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.dao.OrgadminDAO;
import com.ocs.indaba.dao.OrganizationDAO;
import com.ocs.indaba.po.Organization;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class OrganizationService {

    private static final Logger logger = Logger.getLogger(OrganizationService.class);
    private OrganizationDAO organizationDao = null;
    private OrgadminDAO orgadminDao = null;

    public List<Organization> getOrgsByAdminId(int userId) {
        List<Organization> list = new ArrayList<Organization>();
        List<Organization> orgs = organizationDao.selectOrgsByUserIdFromOrgAdmin(userId);
        if (orgs != null && !orgs.isEmpty()) {
            list.addAll(orgs);
        }
        orgs = organizationDao.selectOrgsByUserIdFromOrg(userId);
        if (orgs != null && !orgs.isEmpty()) {
            for (Organization org : orgs) {
                if (!list.contains(org)) {
                    list.add(org);
                }
            }
        }
        return list;
    }

    public List<Integer> getOrgIdsByAdminId(int userId) {
        List<Integer> list = new ArrayList<Integer>();
        List<Integer> orgs = organizationDao.selectOrgIdsByUserIdFromOrgAdmin(userId);
        if (orgs != null && !orgs.isEmpty()) {
            list.addAll(orgs);
        }
        orgs = organizationDao.selectOrgIdsByUserIdFromOrg(userId);
        if (orgs != null && !orgs.isEmpty()) {
            for (int orgId : orgs) {
                if (!list.contains(orgId)) {
                    list.add(orgId);
                }
            }
        }
        return list;
    }

    public boolean existsByUserIdAndOrgId(int userId, int orgId) {
        return orgadminDao.existsByUserIdAndOrgId(userId, orgId);
    }

    public List<Organization> getAllOrgs() {
        return organizationDao.selectAllOrgs();
    }

    public Organization getOrgById(int orgId) {
        return organizationDao.get(orgId);
    }

    public List<Organization> getOrganizationsByConfigIdAndOrgIds(int surveyConfigId, List<Integer> orgIds) {
        return organizationDao.selectOrganizationsByConfigIdAndOrgIds(surveyConfigId, orgIds);
    }

    public List<Organization> getAllOrganizationsByConfigId(int surveyConfigId) {
        return organizationDao.selectAllOrganizationsByConfigId(surveyConfigId);
    }

    /*
     * public List<Integer> getOrganizationIdsByUserId(int userId) { return
     * organizationDao.selectOrganizationIdsByUserId(userId); }
     */
    @Autowired
    public void setOrganizatonDao(OrganizationDAO organizationDao) {
        this.organizationDao = organizationDao;
    }

    @Autowired
    public void setOrgadminDao(OrgadminDAO orgadminDao) {
        this.orgadminDao = orgadminDao;
    }
}
