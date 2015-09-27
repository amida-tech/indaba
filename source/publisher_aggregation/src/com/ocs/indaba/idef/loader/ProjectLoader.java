/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.idef.loader;

import com.ocs.indaba.idef.imp.ProcessContext;
import com.ocs.indaba.idef.xo.Project;
import com.ocs.indaba.idef.xo.User;
import com.ocs.indaba.po.Organization;
import com.ocs.indaba.po.ProjectOwner;
import java.util.Date;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class ProjectLoader extends Loader {

    
    public ProjectLoader(ProcessContext ctx) {
        super.createLoader(ctx);
    }

    public void load() {
        List<Project> list = ctx.getProjects();

        if (list == null || list.isEmpty()) {
            ctx.addError("No projects to load!");
            return;
        }

        for (Project project : list) {
            loadProject(project);
        }
    }


    private void loadProject(Project xo) {
        com.ocs.indaba.po.Project po = new com.ocs.indaba.po.Project();
        User userXo = xo.getAdminUser();
        int userId = userXo == null ? 0 : userXo.getDboId();
        List<Organization> orgs = xo.getOrgs();

        po.setAccessMatrixId(1);
        po.setAdminUserId(userId);
        po.setCloseTime(xo.getCloseTime());
        po.setCodeName(ctx.addImpPrefix() ? getQualifiedName(xo.getName()) : xo.getName());
        po.setCreationTime(new Date());
        po.setDescription(xo.getDescription());
        po.setImportId(ctx.getImportId());
        po.setIsActive(true);
        po.setOrganizationId(orgs.get(0).getId());
        po.setOwnerUserId(userId);
        po.setStartTime(xo.getStartTime());
        po.setStatus(2);  // completed
        po.setStudyPeriodId(xo.getStudyPeriod().getId());
        po.setViewMatrixId(0);
        po.setVisibility(xo.getVisibility());

        projDao.create(po);
        xo.setDboId(po.getId());
        
        if (orgs.size() > 1) {
            for (int i = 1; i < orgs.size(); i++) {
                ProjectOwner projOwner = new ProjectOwner();
                projOwner.setOrgId(orgs.get(i).getId());
                projOwner.setProjectId(po.getId());
                projOwnerDao.create(projOwner);
            }
        }

    }
}
