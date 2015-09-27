/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.action;

import com.ocs.indaba.common.Constants;
import com.ocs.indaba.po.TaskAssignment;
import com.ocs.indaba.service.CommPanelService;
import com.ocs.indaba.service.TaskService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author yc06x
 */
public class CheckFlagAssignmentsAction extends BaseAction {

    @Autowired
    private TaskService taskService = null;

    @Autowired
    private CommPanelService commPanelService = null;


    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // also adjust flag tasks just in case
        List<TaskAssignment> flagAssignments = taskService.getActiveFlagAssignments();
        if (flagAssignments != null && !flagAssignments.isEmpty()) {
            for (TaskAssignment assignment : flagAssignments) {
                switch (assignment.getTaskId()) {
                    case Constants.TASK_ID_FLAG_RESPONSE:
                        commPanelService.adjustFlagResponseAssignment(assignment.getAssignedUserId(), assignment.getHorseId(), null, false);
                        break;

                    case Constants.TASK_ID_UNSET_FLAG:
                        commPanelService.adjustFlagUnsetAssignment(assignment.getAssignedUserId(), assignment.getHorseId(), null, false);
                        break;

                    default:
                        break;
                }
            }
        }

        return null;
    }

}
