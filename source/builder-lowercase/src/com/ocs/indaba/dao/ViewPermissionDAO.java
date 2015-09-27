/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.ViewMatrix;
import com.ocs.indaba.po.ViewPermission;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author menglong
 */
public class ViewPermissionDAO extends SmartDaoMySqlImpl<ViewPermission, Integer> {

    private static final Logger log = Logger.getLogger(ViewPermissionDAO.class);

    private static final String SELECT_VIEW_PERMISSION =
            "SELECT * from view_permission WHERE view_matrix_id=? AND subject_role_id=? AND target_role_id=?";
    private ViewMatrixDAO viewMatrixDao = null;

    public ViewPermission selectViewPermission(int viewMatrixId, int subjectRoleId, int targetRoleId) {
        log.debug("Select table view_permission: " + viewMatrixId + " [subjectRoleId=" + subjectRoleId + ", targetRoleId=" + targetRoleId + "].");
        return super.findSingle(SELECT_VIEW_PERMISSION, viewMatrixId, subjectRoleId, targetRoleId);

    } 
    
    public ViewMatrix getViewMatrix(int viewMatrixId) {
        return viewMatrixDao.get(viewMatrixId);
    }
    
    private static final String SELECT_VIEW_PERMISSIONS_BY_SUBJECT =
            "SELECT * from view_permission WHERE view_matrix_id=? AND subject_role_id=?";

    public List<ViewPermission> selectViewPermissionsBySubject(int viewMatrixId, int subjectRoleId) {
        return super.find(SELECT_VIEW_PERMISSIONS_BY_SUBJECT, (long)viewMatrixId, subjectRoleId);
    }

    @Autowired
    public void setViewMatrixDao(ViewMatrixDAO viewMatrixDao) {
        this.viewMatrixDao = viewMatrixDao;
    }
}


