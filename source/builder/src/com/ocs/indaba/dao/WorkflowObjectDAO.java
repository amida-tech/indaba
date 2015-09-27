/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.dao;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.dao.common.SmartDaoMySqlImpl;
import com.ocs.indaba.po.WorkflowObject;

import java.util.List;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Jeff
 */
public class WorkflowObjectDAO extends SmartDaoMySqlImpl<WorkflowObject, Integer> {

    private static final Logger log = Logger.getLogger(WorkflowObjectDAO.class);

    private static final String SELECT_WORKFLOW_OBJECT_BY_WORKFLOW_ID =
            "SELECT * from workflow_object WHERE workflow_id=?";

    private static final String SELECT_WORKFLOW_OBJECT_BY_CONTENT_HEADER_ID =
            "SELECT * FROM workflow_object wo " +
            "JOIN horse h ON (h.workflow_object_id = wo.id) " +
            "JOIN content_header ch ON (ch.horse_id = h.id) " +
            "WHERE ch.id = ?";

    private static final String RESET_WORKFLOW_OBJECT_STATUS_BY_CASE_ID =
            "UPDATE workflow_object SET status = abs(status) WHERE id IN (" +
            "SELECT h.workflow_object_id FROM horse h " +
            "JOIN content_header ch ON (ch.horse_id = h.id) " +
            "JOIN case_object co ON (co.object_type = 1 AND co.object_id = ch.id) " +
            "JOIN cases c ON (c.id = co.cases_id AND c.id = ?)) ";

    private static final String UPDATE_WORKFLOW_STATUS_BY_ID =
            "UPDATE workflow_object SET status=? WHERE id=? ";

    private static final String UPDATE_WORKFLOW_STATUS_CANCEL_BY_ID =
            "UPDATE workflow_object SET orig_status=status, status=? WHERE id=?";

    private static final String UPDATE_WORKFLOW_STATUS_UNCANCEL_BY_ID =
            "UPDATE workflow_object SET status=orig_status, orig_status=0 WHERE id=?";

    public WorkflowObject selectWorkflowObjectById(int wfoId) {
        log.debug("Select WorkflowObject by id: " + wfoId + ".");
        return super.get(wfoId);
    }

    public List<WorkflowObject> selectWorkflowObjects() {
        log.debug("Select all WorkflowObjects. ");
        return super.findAll();
    }
    
    public void updateWorkflowObjectByContentHeaderId(int contentHeaderId, boolean isBlocked) {
        WorkflowObject wo = super.findSingle(SELECT_WORKFLOW_OBJECT_BY_CONTENT_HEADER_ID, contentHeaderId);
        if ((wo.getStatus() > 0 && isBlocked) || (wo.getStatus() < 0 && !isBlocked)) {
            wo.setStatus(wo.getStatus() * (-1));
        }
        super.update(wo);
    }

    public void resetWorkflowObjectByCaseId(int caseId) {
        super.update(RESET_WORKFLOW_OBJECT_STATUS_BY_CASE_ID, caseId);
    }

    public List<WorkflowObject> selectWorkflowObjects(int workflowId) {
        log.debug("====Select WorkflowObjects by workflowID: " + workflowId);
        return super.find(SELECT_WORKFLOW_OBJECT_BY_WORKFLOW_ID, workflowId);
    }

    public void updateWorkflowObjectStatusById(int workflowObjectId, int newStatus){
        super.update(UPDATE_WORKFLOW_STATUS_BY_ID, newStatus, workflowObjectId);
    }

    public void updateWorkflowObjectCancelById(int workflowObjectId){
        super.update(UPDATE_WORKFLOW_STATUS_CANCEL_BY_ID, Constants.HORSE_STATUS_CANCELLED, workflowObjectId);
    }

    public void updateWorkflowObjectUncancelById(int workflowObjectId){
        super.update(UPDATE_WORKFLOW_STATUS_UNCANCEL_BY_ID, workflowObjectId);
    }


    private static final String SELECT_WORKFLOW_OBJECT_BY_HORSE =
            "SELECT wo.* FROM workflow_object wo, horse WHERE horse.id=? AND wo.id=horse.workflow_object_id";

    public WorkflowObject selectWorkflowObjectByHorse(int horseId) {
        return super.findSingle(SELECT_WORKFLOW_OBJECT_BY_HORSE, horseId);
    }


    private static final String SELECT_WORKFLOW_OBJECT_BY_GROUPOBJ =
            "SELECT wo.* FROM workflow_object wo, horse, groupobj " +
            "WHERE groupobj.id=? AND horse.id=groupobj.horse_id AND wo.id=horse.workflow_object_id";

    public WorkflowObject selectWorkflowObjectByGroupobj(int groupobjId) {
        return super.findSingle(SELECT_WORKFLOW_OBJECT_BY_GROUPOBJ, groupobjId);
    }
}
