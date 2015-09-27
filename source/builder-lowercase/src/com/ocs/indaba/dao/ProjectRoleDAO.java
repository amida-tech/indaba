/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.ProjectRoles;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff
 */
public class ProjectRoleDAO extends SmartDaoMySqlImpl<ProjectRoles, Integer> {

    private static final Logger log = Logger.getLogger(ProjectRoleDAO.class);
    private static final String SELECT_PROJECT_ROLES_BY_PROJECT_ID = "SELECT * FROM project_roles WHERE project_id=? ORDER BY id ASC";
    private static final String SQL_DELETE_BY_PROJECT_ID =
            "DELETE FROM project_roles WHERE project_id=?";
    private static final String SELECT_PROJECT_ROLE_BY_PRJECT_AND_ROLE = "SELECT * FROM project_roles WHERE project_id=? and role_id=?";

    public void deleteByProjectId(int projId) {
        log.debug("==== DELETING ALL ROLES OF PROJECT " + projId);
        super.delete(SQL_DELETE_BY_PROJECT_ID, projId);
    }

    public List<ProjectRoles> selectProjectRoles(int projId) {
        return super.find(SELECT_PROJECT_ROLES_BY_PROJECT_ID, projId);
    }

    public ProjectRoles selectProjectRoleByProjectAndRole(int projectId, int roleId){
        return super.findSingle(SELECT_PROJECT_ROLE_BY_PRJECT_AND_ROLE, projectId, roleId);
    }
}
