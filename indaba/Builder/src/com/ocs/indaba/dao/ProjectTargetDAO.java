/**
 * Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.ProjectTarget;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeff
 */
public class ProjectTargetDAO extends SmartDaoMySqlImpl<ProjectTarget, Integer> {

    private static final Logger log = Logger.getLogger(ProjectTargetDAO.class);
    private static final String SELECT_PROJECT_TARGETS_BY_PROJECT_ID = "SELECT * FROM project_target WHERE project_id=? ORDER BY id ASC";
    private static final String SQL_DELETE_BY_PROJECT_ID = 
            "DELETE FROM project_target WHERE project_id=?";
    private static final String SELECT_PROJECT_TARGET_BY_PROJECT_AND_TARGET =
            "SELECT * FROM project_target WHERE project_id=? AND target_id=?";
    
    public void deleteByProjectId(int projId){
        super.delete(SQL_DELETE_BY_PROJECT_ID, projId);
    }


    public List<ProjectTarget> selectProjectTargets(int projId) {
        return super.find(SELECT_PROJECT_TARGETS_BY_PROJECT_ID, projId);
    }

    public ProjectTarget selectProjectTargetByProjectAndTarget(int projectId, int targetId){
        return super.findSingle(SELECT_PROJECT_TARGET_BY_PROJECT_AND_TARGET, projectId, targetId);
    }
}
