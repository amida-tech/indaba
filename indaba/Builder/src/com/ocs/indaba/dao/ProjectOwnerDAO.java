/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.ProjectOwner;
import com.ocs.indaba.po.ProjectTarget;
import com.ocs.util.ListUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Jeff
 */
public class ProjectOwnerDAO extends SmartDaoMySqlImpl<ProjectOwner, Integer> {

    private static final Logger log = Logger.getLogger(ProjectOwnerDAO.class);
    private static final String SELECT_PROJECT_OWNERS_BY_PROJECT_ID = "SELECT * FROM project_owner WHERE project_id=? ORDER BY id ASC";
    private static final String SELECT_PROJECT_IDS_BY_ORG_IDS = "SELECT project_id FROM project_owner WHERE org_id IN ({0}) ORDER BY id ASC";
    private static final String SQL_DELETE_BY_PROJECT_ID = 
            "DELETE FROM project_owner WHERE project_id=?";
    private static final String SELECT_PROJECT_OWNER_BY_PROJECT_AND_ORG = "SELECT * FROM project_owner WHERE project_id=? AND org_id=?";
    private static final String DELETE_PROJECT_OWNER_BY_PROJECT_AND_ORG = "DELETE FROM project_owner WHERE project_id=? AND org_id=?";
    
    public void deleteByProjectId(int projId){
        super.delete(SQL_DELETE_BY_PROJECT_ID, projId);
    }


    public List<ProjectOwner> selectProjectOwners(int projId) {
        return super.find(SELECT_PROJECT_OWNERS_BY_PROJECT_ID, projId);
    }

    public List<Integer> selectProjectIdsByOrgIds(List<Integer> orgIds) {
        if (orgIds == null || orgIds.isEmpty()) {
            return null;
        }
        return getJdbcTemplate().query(MessageFormat.format(SELECT_PROJECT_IDS_BY_ORG_IDS, ListUtils.listToString(orgIds)), new Object[]{}, new RowMapper() {

            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("project_id");
            }
        });
    }

    public ProjectOwner selectProjectOwenerByProjectAndOrg(int projectId, int orgId){
        return super.findSingle(SELECT_PROJECT_OWNER_BY_PROJECT_AND_ORG, projectId, orgId);
    }

    public void deleteProjectOwnerByProjectAndOrg(int projectId, int orgId){
        super.delete(DELETE_PROJECT_OWNER_BY_PROJECT_AND_ORG, projectId, orgId);
    }
}
