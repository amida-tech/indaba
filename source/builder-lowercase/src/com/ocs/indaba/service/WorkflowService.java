/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.service;

import com.ocs.indaba.workflow.WorkflowEngine;
import com.ocs.indaba.po.Workflow;
import com.ocs.indaba.po.WorkflowObject;
import com.ocs.indaba.dao.WorkflowDAO;
import com.ocs.indaba.dao.WorkflowObjectDAO;
import com.ocs.indaba.vo.WorkflowView;
import com.ocs.indaba.vo.WorkflowObjectView;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 *
 * @author Luke
 */
public class WorkflowService {

    private static final Logger logger = Logger.getLogger(WorkflowService.class);
    private WorkflowEngine workflowEngine = null;
    private WorkflowDAO workflowDAO = null;
    private WorkflowObjectDAO workflowObjectDAO = null;

    @Autowired
    public void setWorkflowEngine(WorkflowEngine workflowEngine) {
        this.workflowEngine = workflowEngine;
    }

    @Autowired
    public void setWorkflowDao(WorkflowDAO workflowDAO) {
        this.workflowDAO = workflowDAO;
    }

    @Autowired
    public void setWorkflowObjectDao(WorkflowObjectDAO workflowObjectDAO) {
        this.workflowObjectDAO = workflowObjectDAO;
    }

    public String runAllWorkflow() {
        String retStr = "";
        List<Workflow> wfList = workflowDAO.findAll();
        if (wfList != null && wfList.size() > 0) {
            for (Workflow workflow : wfList) {
                retStr += runWorkflow(workflow.getId());
            }
            wfList.clear();
            wfList = null;
        }
        return retStr;
    }

    public String runWorkflow(int workflowId) {
        String retStr = "";
        List<WorkflowObject> wfoList = workflowObjectDAO.selectWorkflowObjects(workflowId);
        if (wfoList != null && wfoList.size() > 0) {
            for (WorkflowObject wfo : wfoList) {
                retStr += runWorkflowObject(wfo);
            }
            wfoList.clear();
            wfoList = null;
        }
        return retStr;
    }

    public String runWorkflowObject(int wfoId) {
        return workflowEngine.runWorkflowObject(wfoId);
    }

    public String runWorkflowObject(WorkflowObject wfo) {
        return workflowEngine.runWorkflowObject(wfo);
    }

    public List<Workflow> getWorkflowList() {
        return workflowDAO.findAll();
    }

    public List<WorkflowView> getWorkflowViewList() {
        return workflowDAO.getWorkflowViewList();
    }

    public List<WorkflowObject> getWorkflowObjectList(int workflowId) {
        return workflowObjectDAO.selectWorkflowObjects(workflowId);
    }

    public List<WorkflowObjectView> getWorkflowObjectViewList() {
        return workflowDAO.getWorkflowObjectViewList();
    }
    
    public Workflow createWorkflow(Workflow workflow) {
        return workflowDAO.create(workflow);
    }
}
