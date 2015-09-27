/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.service;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.aggregation.dao.PubOrganizationDAO;
import com.ocs.indaba.aggregation.vo.OrganizationVO;
import com.ocs.indaba.aggregation.vo.WsProjectVO;
import com.ocs.indaba.builder.dao.ProjectDAO;
import com.ocs.indaba.builder.dao.UserDAO;
import com.ocs.indaba.common.OrgAuthorizer;
import com.ocs.indaba.dao.OrganizationDAO;
import com.ocs.indaba.po.Organization;
import com.ocs.indaba.po.Project;
import com.ocs.indaba.po.User;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author jiangjeff
 */
public class OrganizationService {

    private ProjectDAO projDao = null;
    private OrganizationDAO organizationDao = null;
    private PubOrganizationDAO pubOrganizationDao = null;
    private UserDAO userDao = null;

    public Organization getOrganizationByWorksetId(int worksetId) {
        return pubOrganizationDao.selectOrganizationByWorksetId(worksetId);
    }

    public List<Organization> getAllOrgs() {
        return organizationDao.selectAllOrgs();
    }

    public Organization getOrganization(int orgId) {
        return organizationDao.get(orgId);
    }

    public List<Organization> getOrganizationsByIds(List<Integer> orgIds) {
        return organizationDao.selectOrganizationsByIds(orgIds);
    }

    public List<OrganizationVO> getAllOrganizations() {
        List<Organization> organizations = organizationDao.selectAllOrgs();
        if (organizations == null) {
            return null;
        }

        List<OrganizationVO> orgs = new ArrayList<OrganizationVO>(organizations.size());
        for (Organization o : organizations) {
            List<Project> projects = projDao.selectProjectsByOrgId(o.getId());
            OrganizationVO org = new OrganizationVO(o.getId(), o.getName());
            org.setProjects(projectToWsProject(projects));
            orgs.add(org);
        }

        return orgs;
    }

    public List<OrganizationVO> getOrganizationsByUser(int userId) {
        if (userId <= 0) {
            return null;
        }
        User user = userDao.get(userId);

        OrgAuthorizer orgAuth = (user == null) ? null : new OrgAuthorizer(user);

        if (orgAuth == null) return null;

        List<Organization> organizations = orgAuth.getAccessibleOrgList(com.ocs.indaba.common.Constants.VISIBILITY_PRIVATE);
        
        if (organizations == null) {
            return null;
        }

        //List<Project> pubProjects = projDao.selectPublicProjects();
        //List<WsProjectVO> pubWsProjects = projectToWsProject(pubProjects);
        List<OrganizationVO> orgs = new ArrayList<OrganizationVO>(organizations.size());
        for (Organization o : organizations) {
            OrganizationVO org = new OrganizationVO(o.getId(), o.getName());
            /*List<Project> projects = projDao.selectPrivateProjectsByOrgId(o.getId());
            if(projects == null || projects.size() == 0){
            org.setProjects(pubWsProjects);
            }else{
            List<WsProjectVO> wsProjects = projectToWsProject(projects);
            wsProjects.addAll(pubWsProjects);
            org.setProjects(wsProjects);
            }*/
            orgs.add(org);
        }

        return orgs;
    }

    public List<WsProjectVO> getProjectsByOrgIdAndVisibility(int orgId, int visibility) {
        if (visibility == Constants.PUBLIC_WORKSET) {
            List<Project> pubProjects = projDao.selectPublicProjectsByOrgId(orgId);
            List<WsProjectVO> pubWsProjects = projectToWsProject(pubProjects);
            return pubWsProjects;
        } else if (visibility == Constants.PRIVATE_WORKSET) {
            List<Project> projects = projDao.selectPrivateProjectsByOrgId(orgId);
            List<WsProjectVO> wsProjects = projectToWsProject(projects);
            return wsProjects;
        } else {
            return null;
        }
    }

    private List<WsProjectVO> projectToWsProject(List<Project> projects) {
        List<WsProjectVO> wsp = null;
        if (projects != null && projects.size() > 0) {
            wsp = new ArrayList<WsProjectVO>();
            for (Project p : projects) {
                WsProjectVO po = new WsProjectVO();
                po.setProjectId(p.getId());
                po.setProjcetName(p.getCodeName());
                wsp.add(po);
            }
        }
        return wsp;
    }

    @Autowired
    public void setProjectDao(ProjectDAO projDao) {
        this.projDao = projDao;
    }

    @Autowired
    public void setOrganizationDao(OrganizationDAO organizationDao) {
        this.organizationDao = organizationDao;
    }

    @Autowired
    public void setPubOrganizationDao(PubOrganizationDAO pubOrganizationDao) {
        this.pubOrganizationDao = pubOrganizationDao;
    }

    @Autowired
    public void setUserDAO(UserDAO userDao) {
        this.userDao = userDao;
    }
}
