/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.*;
import com.ocs.indaba.po.*;
import com.ocs.indaba.vo.ProjectAdminVO;
import com.ocs.indaba.vo.ProjectInfo;
import com.ocs.indaba.vo.ProjectMemberShipVO;
import com.ocs.util.Pagination;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;

/**
 *
 * @author Jeff
 */
public class ProjectService {

    private static final Logger logger = Logger.getLogger(ProjectService.class);
    private OrganizationService orgSrvc = null;
    private TaskService taskSrvc = null;
    private ProductService prodSrvc = null;
    private UserService userSrvc = null;
    private ProjectDAO projectDao = null;
    private ProjectAdminDAO projAdminDao = null;
    private ProjectOwnerDAO projOwnerDao = null;
    private ProjectTargetDAO projTargetDao = null;
    private ProjectRoleDAO projRoleDao = null;
    private ProjectMembershipDAO projMembershipDao = null;
    private RoleDAO roleDao = null;
    private UserDAO userDao = null;
    private TargetDAO targetDao = null;

    public Project getProjectByName(String projName) {
        return projectDao.getProjectByName(projName);
    }

    public boolean existsTargetsByProjectId(Integer projId) {
        return projectDao.existsTargetByProjectId(projId);
    }

    public boolean existsMembershipByProjectId(Integer projId) {
        return projectDao.existsMembershipByProjectId(projId);
    }

    public Project getProjectById(Integer id) {
        return projectDao.getProjectById(id);
    }

    public Project getProjectByTaskId(int taskId) {
        return projectDao.selectProjectByTaskId(taskId);
    }

    public List<Project> getProjects(int userId) {
        return projectDao.selectProjectsByUserId(userId);
    }

    public Project getProjectById(int projectId) {
        return projectDao.selectProjectById(projectId);
    }

    public List<Project> getAllProjects() {
        return projectDao.selectAllProjects();
    }

    public Project addProject(Project proj) {
        return projectDao.create(proj);
    }

    public Project updateProject(Project proj) {
        return projectDao.save(proj);
    }

    public void deleteProjectRelations(int projId) {
        deleteProjectAdminsByProjectId(projId);
        deleteProjectRolesByProjectId(projId);
        deleteProjectTargetsByProjectId(projId);
        deleteProjectOwnersByProjectId(projId);
    }

    public ProjectOwner addProjectOwner(ProjectOwner projOwner) {
        return projOwnerDao.create(projOwner);
    }

    public ProjectOwner updateProjectOwner(ProjectOwner projOwner) {
        return projOwnerDao.save(projOwner);
    }

    public ProjectOwner getProjectOwner(int projectId, int orgId) {
        return projOwnerDao.selectProjectOwenerByProjectAndOrg(projectId, orgId);
    }

    public void deleteProjectOwner(int projectOwnerId) {
        projOwnerDao.delete(projectOwnerId);
    }

    public boolean hasProjectOwner(int projectId, int orgId) {
        return projOwnerDao.selectProjectOwenerByProjectAndOrg(projectId, orgId) != null;
    }

    // get secondary project owners
    public List<ProjectOwner> getProjectOwners(int projId) {
        return projOwnerDao.selectProjectOwners(projId);
    }

    public void deleteProjectOwnersByProjectId(int projId) {
        projOwnerDao.deleteByProjectId(projId);
    }

    public ProjectAdmin addProjectAdmin(ProjectAdmin projAdmin) {
        return projAdminDao.create(projAdmin);
    }

    public ProjectAdmin updateProjectAdmin(ProjectAdmin projAdmin) {
        return projAdminDao.save(projAdmin);
    }

    public List<ProjectAdminVO> getProjectAdmins(int projectId) {
        List<Integer> projIds = new ArrayList<Integer>();
        projIds.add(projectId);
        return projAdminDao.selectProjectAdmins(projIds);
    }

    public List<ProjectAdmin> getProjectAdminsOfProject(int projectId) {
        return projAdminDao.selectProjectAdminByProjectId(projectId);
    }

    public ProjectAdmin getProjectAdmin(int projectId, int userId) {
        return projAdminDao.selectProjectAdminByProjectAndUser(projectId, userId);
    }

    public void deleteProjectAdmin(int projectAdminId) {
        projAdminDao.delete(projectAdminId);
    }

    public List<ProjectAdminVO> getProjectAdmins(List<Integer> projectIds) {
        return projAdminDao.selectProjectAdmins(projectIds);
    }

    public void deleteProjectAdminsByProjectId(int projId) {
        projAdminDao.deleteByProjectId(projId);
    }

    public ProjectTarget addProjectTarget(ProjectTarget projTarget) {
        return projTargetDao.create(projTarget);
    }

    public ProjectTarget updateProjectTarget(ProjectTarget projTarget) {
        return projTargetDao.save(projTarget);
    }

    public List<ProjectTarget> getProjectTargets(int projId) {
        return projTargetDao.selectProjectTargets(projId);
    }

    public ProjectTarget getProjectTarget(int projectId, int targetId) {
        return projTargetDao.selectProjectTargetByProjectAndTarget(projectId, targetId);
    }

    public void deleteProjectTarget(int projectTargetId) {
        projTargetDao.delete(projectTargetId);
    }

    public void deleteProjectTargetsByProjectId(int projId) {
        projTargetDao.deleteByProjectId(projId);
    }

    public ProjectRoles addProjectRole(ProjectRoles projRole) {
        return projRoleDao.create(projRole);
    }

    public ProjectRoles updateProjectRole(ProjectRoles projRole) {
        return projRoleDao.save(projRole);
    }

    public ProjectRoles getProjectRoles(int project, int roleId) {
        return projRoleDao.selectProjectRoleByProjectAndRole(project, roleId);
    }

    public void deleteProjectRoles(int projectRolesId) {
        projRoleDao.delete(projectRolesId);
    }

    public List<ProjectRoles> getProjectRoles(int projId) {
        return projRoleDao.selectProjectRoles(projId);
    }

    public void deleteProjectRolesByProjectId(int projId) {
        projRoleDao.deleteByProjectId(projId);
    }

    public List<Integer> getUserIdsByProjectId(int projId) {
        return projMembershipDao.selectUserIdsByProjectId(projId);
    }

    public long getProjectMembershipCountByProject(int projectId) {
        return projMembershipDao.selectProjectMembershipCountByProjectId(projectId);
    }

    public List<ProjectMembership> getProjectMembershipsByProjectAndRole(int projectId, int roleId) {
        return projMembershipDao.selectProjectMembershipByProjectAndRole(projectId, roleId);
    }

    public ProjectMembership getProjectMembership(int projectId, int userId) {
        return projMembershipDao.selectProjectMembership(projectId, userId);
    }

    public ProjectMembership addProjectMembership(ProjectMembership pm) {
        return projMembershipDao.create(pm);
    }

    public ProjectMembership getProjectMembership(int pmId) {
        return projMembershipDao.selectProjectMembershipById(pmId);
    }

    public ProjectMembership updateProjectMembership(ProjectMembership pm) {
        return projMembershipDao.save(pm);
    }

    public void deleteProjectMembership(int pmId) {
        projMembershipDao.delete(pmId);
    }

    public Pagination<ProjectMemberShipVO> getProjectMembershipsByProjectId(int projectId, String sortName, String sortOrder, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        int count = pageSize;

        long totalCount = projMembershipDao.selectProjectMembershipCountByProjectId(projectId);
        List<ProjectMemberShipVO> projMemberships = projMembershipDao.selectProjectMembershipsByProjectId(projectId, sortName, sortOrder, offset, count);

        Pagination<ProjectMemberShipVO> pagination = new Pagination<ProjectMemberShipVO>(totalCount, page, pageSize);
        pagination.setRows(projMemberships);
        pagination.setTotal((int) totalCount);

        return pagination;
    }

    public Pagination<ProjectMemberShipVO> getProjectMembershipsByFilter(Map<String, Object> filters, String sortName, String sortOrder, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        int count = pageSize;

        long totalCount = projMembershipDao.selectProjectMembershipCountByFilter(filters);
        List<ProjectMemberShipVO> projMemberships = projMembershipDao.findProjectMembershipByFilter(filters, sortName, sortOrder, offset, count);

        Pagination<ProjectMemberShipVO> pagination = new Pagination<ProjectMemberShipVO>(totalCount, page, pageSize);
        pagination.setRows(projMemberships);

        return pagination;
    }

    public List<ProjectMemberShipVO> getProjectMembershipsByProjectId(int projectId) {
        return projMembershipDao.selectProjectMembershipsByProjectId(projectId);
    }

    public List<Integer> getOwnedOrgIds(int projectId) {
        List<Integer> orgIds = new ArrayList<Integer>();
        Project proj = projectDao.get(projectId);
        if (proj != null) {
            orgIds.add(proj.getOrganizationId());
        }

        List<ProjectOwner> pOwners = getProjectOwners(projectId);
        if (pOwners != null && !pOwners.isEmpty()) {
            for (ProjectOwner owner : pOwners) {
                if (!orgIds.contains(owner.getOrgId())) {
                    orgIds.add(owner.getOrgId());
                }
            }
        }
        return orgIds;
    }

    public List<Integer> getSecondaryOrgIds(int projectId) {
        List<Integer> orgIds = new ArrayList<Integer>();
        List<ProjectOwner> pOwners = getProjectOwners(projectId);
        if (pOwners != null && !pOwners.isEmpty()) {
            for (ProjectOwner owner : pOwners) {
                orgIds.add(owner.getOrgId());
            }
        }
        return orgIds;
    }

    /**
     *
     * @param orderByName
     * @param ascend
     * @param offset
     * @param count
     * @param visibility
     * @return
     */
    public Pagination<ProjectInfo> getProjects(int userId, List<Integer> orgIds, int visibility, String sortName, String sortOrder, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        int count = pageSize;

        logger.debug("Select all of projects by limit[sortName=" + sortName + ",sortOrder=" + sortOrder + ",offset=" + offset
                + ",visibility=" + visibility + ",orgIds=" + orgIds + ",count=" + count + "]");


        long totalCount = projectDao.selectProjectsCountByOrgIdsAndVisibility(userId, visibility, orgIds);
        Pagination<ProjectInfo> pagination = new Pagination<ProjectInfo>(totalCount, page, pageSize);

        List<ProjectInfo> projects = projectDao.selectProjectsByOrgIdsAndVisibilityWithPagination(userId, visibility, orgIds, sortName, sortOrder, offset, count);

        /**
        if (projects != null && !projects.isEmpty()) {
        List<Organization> orgs = orgSrvc.getAllOrgs();
        Map<Integer, String> orgMap = new WeakHashMap<Integer, String>();
        if (orgs != null && !orgs.isEmpty()) {
        for (Organization o : orgs) {
        orgMap.put(o.getId(), o.getName());
        }
        }
        for (ProjectInfo p : projects) {
        List<Integer> secondaryOrgIds = getSecondaryOrgIds(p.getPrjId());
        if (secondaryOrgIds != null && !secondaryOrgIds.isEmpty()) {
        StringBuilder sBuf = new StringBuilder();
        if (!StringUtils.isEmpty(p.getOrgName())) {
        sBuf.append(p.getOrgName());
        }
        for (int oid : secondaryOrgIds) {
        String orgName = orgMap.get(oid);
        if (!StringUtils.isEmpty(orgName)) {
        if (orgName.equals(p.getOrgName())) {
        continue;
        }
        if (sBuf.length() > 0) {
        sBuf.append(", ");
        }
        sBuf.append(orgName);
        }
        }
        p.setOrgName(sBuf.toString());
        }
        }
        }
         **/
        pagination.setRows(projects);

        return pagination;
    }

    public List<Role> getRolesByProjectId(int projectId) {
        return roleDao.selectRolesByProjectId(projectId);
    }

    public List<User> getUsersByProjectId(int projId) {
        return userDao.selectUsersByProjectId(projId);
    }

    public void deleteProjectOwner(ProjectOwner po) {
        projOwnerDao.deleteProjectOwnerByProjectAndOrg(po.getProjectId(), po.getOrgId());
    }
    public static final int OK = 0;
    public static final int NON_EXISTENT_PROJECT = 1;
    public static final int INVALID_ORG_ID = 2;
    public static final int NON_EXISTENT_ORG = 3;
    public static final int INVALID_USER_ID = 4;
    public static final int NON_EXISTENT_USER = 5;
    public static final int SC_VISIBILITY_VIOLATION = 6;
    public static final int USER_HAS_TASKS = 7;
    public static final int ORG_PROJECT_ADMINS_IN_USE = 8;
    public static final int ORG_PRIVATE_TARGETS_IN_USE = 9;

    public class CheckOrgChangeResult {

        public int status;
        public ProjectAdminVO paInUse = null;
        public Target targetInUse = null;
        public SurveyConfig violatedConfig = null;

        public CheckOrgChangeResult(int status) {
            this.status = status;
        }

        public CheckOrgChangeResult(int status, ProjectAdminVO pa) {
            this.status = status;
            this.paInUse = pa;
        }

        public CheckOrgChangeResult(int status, Target target) {
            this.status = status;
            this.targetInUse = target;
        }

        public CheckOrgChangeResult(int status, SurveyConfig config) {
            this.status = status;
            this.violatedConfig = config;
        }
    }

    public CheckOrgChangeResult changePrimaryOwner(Project project, int newPrimaryOrgId, int newPrimaryAdminUid) {
        if (newPrimaryOrgId < 0) {
            return new CheckOrgChangeResult(INVALID_ORG_ID);
        }
        if (newPrimaryAdminUid < 0) {
            return new CheckOrgChangeResult(INVALID_USER_ID);
        }

        if (project == null) {
            return new CheckOrgChangeResult(NON_EXISTENT_PROJECT);
        }

        if (newPrimaryOrgId > 0) {
            Organization org = orgSrvc.getOrgById(newPrimaryOrgId);
            if (org == null) {
                return new CheckOrgChangeResult(NON_EXISTENT_ORG);
            }
        }

        if (newPrimaryAdminUid > 0) {
            User user = userDao.get(newPrimaryAdminUid);
            if (user == null) {
                return new CheckOrgChangeResult(NON_EXISTENT_USER);
            }
        }

        int curPrimaryOrgId = project.getOrganizationId();
        if (newPrimaryOrgId > 0 && curPrimaryOrgId != newPrimaryOrgId) {
            // primary org change
            // see if the current primary org can be removed
            CheckOrgChangeResult rt = checkRemoveOrg(project, curPrimaryOrgId, newPrimaryOrgId);
            if (rt.status != OK) {
                return rt;
            }

            // see if the new org is already is a secondary owner
            List<ProjectOwner> secondaryOwners = this.getProjectOwners(project.getId());
            ProjectOwner po = null;
            if (secondaryOwners != null && !secondaryOwners.isEmpty()) {
                for (ProjectOwner owner : secondaryOwners) {
                    if (owner.getOrgId() == newPrimaryOrgId) {
                        po = owner;
                        break;
                    }
                }
            }

            if (po == null) {
                // need to see whether this new org would violate survey config visibility rule for private project
                SurveyConfig sc = this.findViolatedSurveyConfig(project, newPrimaryOrgId);

                if (sc != null) {
                    return new CheckOrgChangeResult(SC_VISIBILITY_VIOLATION, sc);
                }
            } else {
                // remove from secondary owner list
                deleteProjectOwner(po);
            }
        }

        // do the update
        if (newPrimaryOrgId > 0) {
            project.setOrganizationId(newPrimaryOrgId);
        }

        if (newPrimaryAdminUid > 0) {
            // see if the user is a secondary PA already. If so , remove it.
            ProjectAdmin pa = getProjectAdmin(project.getId(), newPrimaryAdminUid);

            if (pa != null) {
                deleteProjectAdmin(pa.getId());
            }

            project.setAdminUserId(newPrimaryAdminUid);
        }

        return new CheckOrgChangeResult(OK);
    }

    // check whether survey config visibility rule would be violated if the org is added to the project
    public SurveyConfig findViolatedSurveyConfig(Project project, int orgId) {
        if (project.getVisibility() != Constants.VISIBILITY_PRIVATE) {
            return null;  // okay
        }
        List<Product> products = prodSrvc.getProductsByProjectId(project.getId());

        if (products == null || products.isEmpty()) {
            return null;
        }

        // check each product
        for (Product prod : products) {
            SurveyConfig sc = prodSrvc.findViolatedSurveyConfig(prod, orgId);

            if (sc != null) {
                return sc;
            }
        }

        return null;
    }

    public CheckOrgChangeResult checkRemoveOrg(Project project, int removeOrgId) {
        return checkRemoveOrg(project, removeOrgId, -1);
    }

    public CheckOrgChangeResult checkRemoveOrg(Project project, int removeOrgId, int addOrgId) {
        if (removeOrgId == addOrgId) {
            return new CheckOrgChangeResult(OK);
        }

        List<Integer> orgList = getOwnedOrgIds(project.getId());

        if (orgList == null || orgList.isEmpty()) {
            return new CheckOrgChangeResult(OK);
        }

        // get all current OA user IDs
        List<Integer> curOAs = userSrvc.getOrgAdminUserIdsByOrgIds(orgList);

        if (curOAs == null || curOAs.isEmpty()) {
            return new CheckOrgChangeResult(OK);
        }

        // remove the orgId from current orgs
        boolean removed = orgList.remove((Integer) removeOrgId);

        if (removed) {
            if (addOrgId > 0) {
                if (!orgList.contains(addOrgId)) {
                    orgList.add(addOrgId);
                }
            }
            List<Integer> newOAs = userSrvc.getOrgAdminUserIdsByOrgIds(orgList);

            if (newOAs != null && !newOAs.isEmpty()) {
                curOAs.removeAll(newOAs);
            }

            // now curOAs contains users that cannot be PA of the project if the org is removed
            if (curOAs.size() > 0) {
                // see if the current PAs are in the list
                List<ProjectAdminVO> paList = getProjectAdmins(project.getId());

                if (paList != null) {
                    for (ProjectAdminVO pa : paList) {
                        if (curOAs.contains(pa.getUserId())) {
                            return new CheckOrgChangeResult(ORG_PROJECT_ADMINS_IN_USE, pa);
                        }
                    }
                }
            }

            Target target = checkTargetForOrg(project, removeOrgId);

            if (target != null) {
                return new CheckOrgChangeResult(ORG_PRIVATE_TARGETS_IN_USE, target);
            }
        }

        return new CheckOrgChangeResult(OK);
    }

    public Target checkTargetForOrg(Project project, int orgId) {
        if (project.getVisibility() != Constants.VISIBILITY_PRIVATE) {
            return null;
        }

        List<Target> targets = targetDao.selectTargetsByProjectAndOrg(project.getId(), orgId, Constants.VISIBILITY_PRIVATE);

        if (targets != null && !targets.isEmpty()) {
            return targets.get(0);
        }

        return null;
    }

    @Autowired
    public void setProjectDao(ProjectDAO projectDao) {
        this.projectDao = projectDao;
    }

    @Autowired
    public void setProjectAdminDAO(ProjectAdminDAO projAdminDao) {
        this.projAdminDao = projAdminDao;
    }

    @Autowired
    public void setProjectRoleDAO(ProjectRoleDAO projRoleDao) {
        this.projRoleDao = projRoleDao;
    }

    @Autowired
    public void setProjectTargetDAO(ProjectTargetDAO projTargetDao) {
        this.projTargetDao = projTargetDao;
    }

    @Autowired
    public void setProjectOwnerDAO(ProjectOwnerDAO projOwnerDao) {
        this.projOwnerDao = projOwnerDao;
    }

    @Autowired
    public void setProjectMembershipDAO(ProjectMembershipDAO projMembershipDao) {
        this.projMembershipDao = projMembershipDao;
    }

    @Autowired
    public void setOrganizationService(OrganizationService orgSrvc) {
        this.orgSrvc = orgSrvc;
    }

    @Autowired
    public void setProductService(ProductService srvc) {
        this.prodSrvc = srvc;
    }

    @Autowired
    public void setTaskService(TaskService srvc) {
        this.taskSrvc = srvc;
    }

    @Autowired
    public void setUserService(UserService srvc) {
        this.userSrvc = srvc;
    }

    @Autowired
    public void setRoleDAO(RoleDAO roleDao) {
        this.roleDao = roleDao;
    }

    @Autowired
    public void setUserDAO(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setTargetDAO(TargetDAO dao) {
        this.targetDao = dao;
    }
}
