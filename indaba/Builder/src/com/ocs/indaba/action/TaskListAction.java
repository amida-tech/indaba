/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.action;

import static com.ocs.indaba.action.BaseAction.ATTR_ALL_ASSIGNED_WITH_GRID;
import com.ocs.indaba.common.Constants;
import com.ocs.indaba.common.Messages;
import com.ocs.indaba.common.Rights;
import com.ocs.indaba.po.ContentHeader;
import com.ocs.indaba.po.Goal;
import com.ocs.indaba.service.HorseService;
import com.ocs.indaba.service.TaskService;
import com.ocs.util.StringUtils;
import com.ocs.indaba.vo.AssignedTask;
import com.ocs.indaba.vo.LoginUser;
import com.ocs.indaba.vo.TaskAssignmentWithGrid;
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
 * @author Luke
 */
public class TaskListAction extends com.ocs.indaba.action.BaseAction {
    private static final Logger logger = Logger.getLogger(TaskListAction.class);

    private HorseService horseService = null;
    private TaskService taskService = null;
    

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
        LoginUser loginUser = preprocess(mapping, request, response);

        int horseId = StringUtils.str2int(request.getParameter(PARAM_HORSE_ID), Constants.INVALID_INT_ID);
        int goalId = StringUtils.str2int(request.getParameter(PARAM_GOAL_ID), 0);
        
        Goal goal = taskService.getGoal(goalId);
        
        if (horseId == Constants.INVALID_INT_ID) {
            request.setAttribute(Constants.ATTR_ERR_MSG, getMessage(request, Messages.KEY_COMMON_ERR_INVALID_PARAMETER, "horseid"));
            return mapping.findForward(FWD_ERROR);
        }
        
        boolean hasEditDeadlineRight =
                accessPermissionService.checkProjectPermission(loginUser.getPrjid(), loginUser.getUid(), Rights.MANAGE_ALL_USERS) ||
                accessPermissionService.checkProjectPermission(loginUser.getPrjid(), loginUser.getUid(), Rights.EDIT_ASSIGNMENT_DEADLINES);

        //List<AssignedTask> allAssignedTasks = taskService.getTaskAssignmentsOfHorseAndGoal(horseId, goalId);
         List<TaskAssignmentWithGrid> taskAssignmentWithGridList = taskService.getTaskAssignmentWithGridsOfHorseAndGoal(horseId, goalId);
        if (taskAssignmentWithGridList != null && !taskAssignmentWithGridList.isEmpty()) {
            for (TaskAssignmentWithGrid taskWithGrid : taskAssignmentWithGridList) {
                AssignedTask task = taskWithGrid.getAssignment();
                task.computeTaskStatus(getLanguageId(request));
                task.setTaskStatusIcon(getLanguageId(request));
            }
        }
        
        //request.setAttribute(ATTR_ALL_ASSIGNED_OF_HORSE_LIST, allAssignedTasks);
        request.setAttribute(ATTR_ALL_ASSIGNED_WITH_GRID, taskAssignmentWithGridList);
        
        if (taskAssignmentWithGridList == null || taskAssignmentWithGridList.isEmpty()) {
            request.setAttribute(ATTR_TASKS, taskService.getTasksOfGoal(goalId, horseId));
        }
        
        String cntName = "";
        ContentHeader cntHdr = horseService.getContentHeaderByHorseId(horseId);
        if (cntHdr != null) {
            cntName = cntHdr.getTitle();
            if (goal != null)
                cntName += ": " + goal.getName();
        }
        
        request.setAttribute(ATTR_CONTENT_NAME, cntName);
        request.setAttribute(ATTR_HAS_EDIT_DEADLINE_RIGHT, hasEditDeadlineRight);
        
        return mapping.findForward(FWD_SUCCESS);
    }

    @Autowired
    public void setHorseService(HorseService horseService) {
        this.horseService = horseService;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
    
}
