/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */
package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.service.CaseService;
import com.ocs.indaba.service.HorseService;
import com.ocs.indaba.service.TaskService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.AssignedTask;
//import com.ocs.indaba.vo.JournalContentView;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jeff
 */
public class ContentGeneralAction extends com.ocs.indaba.action.BaseAction {

    private static final Logger logger = Logger.getLogger(ContentGeneralAction.class);
    /*
    private static final String Messages.KEY_COMMON_REMARK_INACTIVE = "java.action.cntntgen.remark.inactive";
    private static final String Messages.KEY_COMMON_REMARK_ACTIVE = "java.action.cntntgen.remark.active";
    private static final String Messages.KEY_COMMON_REMARK_AWARE = "java.action.cntntgen.remark.aware";
    private static final String Messages.KEY_COMMON_REMARK_NOTICED = "java.action.cntntgen.remark.noticed";
    private static final String Messages.KEY_COMMON_REMARK_STARTED = "java.action.cntntgen.remark.started";
    private static final String Messages.KEY_COMMON_REMARK_DONE = "java.action.cntntgen.remark.done";
    */
    private static final String PARAM_ASSIGN_ID = "assignid";
    private static final String ATTR_INSTRUCTIONS = "instructions";
    private HorseService horseService = null;
    private TaskService taskService = null;
    private CaseService caseService = null;
    //private ToolService toolService = null;


    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        preprocess(mapping, request, response);
        
        int assignId = StringUtils.str2int(request.getParameter(PARAM_ASSIGN_ID), Constants.INVALID_INT_ID);
        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
        
        if (horseId == Constants.INVALID_INT_ID) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "horseid"));
            return mapping.findForward(FWD_ERROR);
        }
        request.setAttribute(PARAM_ASSIGN_ID, assignId);
        if (assignId == Constants.INVALID_INT_ID) {
            logger.debug("No specify the parameter 'assigid'. Ignore assign task.");
        }
        /*else {
        AssignedTask myAssignedTask = taskService.getAssignedTaskOfHorseByUserIdAndAssignId(horseId, uid, assignId);

        if (myAssignedTask != null) {

        Tool tool = toolService.getToolById(myAssignedTask.getToolId());
        if (tool != null) {
        myAssignedTask.setToolName(tool.getName());
        myAssignedTask.setAction(tool.getAction());
        }
        if (myAssignedTask.getAssignmentId() == assignId) {
        request.setAttribute(ATTR_MY_ASSIGNED_OF_HORSE, myAssignedTask);
        }
        myAssignedTask.setInstructions(horseService.getInstructionsByHorseId(horseId));
        }
        }*/
        String inst = taskService.getInstructionsByTaskAssignId(assignId);
        request.setAttribute(ATTR_INSTRUCTIONS, inst == null ? "" : inst.replaceAll("\n", "<br/>"));
        List<AssignedTask> allAssignedTasks = taskService.getActiveAssignedTasksOfHorse(horseId);

        if (allAssignedTasks != null) {
            for (AssignedTask task : allAssignedTasks) {
                task.computeTaskStatus(getLanguageId(request));               
                task.setTaskStatusIcon(getLanguageId(request));
            }
        }

        String cntName = "";
        //ContentHeader cntHdr = horseService.getContentHeaderByHorseId(horseId);
        //if (cntHdr != null)
        //    cntName = cntHdr.getTitle();
        request.setAttribute(ATTR_CONTENT_NAME, cntName);

        request.setAttribute(ATTR_ALL_ASSIGNED_OF_HORSE_LIST, allAssignedTasks);
        request.setAttribute(ATTR_ALL_CASES, caseService.getCasesByHorseId(horseId));

        return mapping.findForward(FWD_CONTENT_GENERAL);
    }

    @Autowired
    public void setHorseService(HorseService horseService) {
        this.horseService = horseService;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Autowired
    public void setCaseService(CaseService caseService) {
        this.caseService = caseService;
    }

    /*
    @Autowired
    public void setToolService(ToolService toolService) {
        this.toolService = toolService;
    }
     * 
     */

    /*
    @Autowired
    public void setJournalService(JournalService jouralService) {
        this.journalService = jouralService;
    }
     * 
     */
}
